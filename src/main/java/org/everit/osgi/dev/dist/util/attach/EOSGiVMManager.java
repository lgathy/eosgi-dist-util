/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.everit.osgi.dev.dist.util.attach;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.everit.osgi.dev.dist.util.DistConstants;
import org.everit.osgi.dev.dist.util.attach.internal.reflect.VirtualMachineDescriptorReflect;
import org.everit.osgi.dev.dist.util.attach.internal.reflect.VirtualMachineReflect;
import org.everit.osgi.dev.dist.util.attach.internal.reflect.VirtualMachineStaticReflect;

/**
 * Tracks virtual machines that run EOSGi environment.
 */
public class EOSGiVMManager implements Closeable {

  private static final int BUFFER_SIZE = 1024;

  private static final long DEFAULT_VM_CALL_TIMEOUT = 3000;

  /**
   * Static method to list the id of all available virtual machines.
   *
   * @param classLoader
   *          The {@link ClassLoader} to use Attach API.
   * @return the ids of the available VMs. If empty, it might mean that TMPDIR/hsperfdata_USERNAME
   *         folder is somehow corrupted or -XX:+PerfDisableSharedMem is present on all VMs.
   */
  public static Collection<String> getAvailableVMIds(final ClassLoader classLoader) {
    VirtualMachineStaticReflect virtualMachineStaticReflect =
        new VirtualMachineStaticReflect(classLoader);

    List<VirtualMachineDescriptorReflect> vms = virtualMachineStaticReflect.list();
    Set<String> result = new HashSet<>();

    for (VirtualMachineDescriptorReflect virtualMachineDescriptorReflect : vms) {
      result.add(virtualMachineDescriptorReflect.id());
    }
    return result;
  }

  private boolean closed = false;

  private final Consumer<EOSGiVMManagerEventData> deadlockMessageConsumer;

  private final Map<String, String> environmentIdByVmId = new HashMap<>();

  private final Map<String, Set<EnvironmentRuntimeInfo>> environmentInfosByEnvironmentId =
      new HashMap<>();

  private final Function<EOSGiVMManagerEventData, Boolean> exceptionDuringAttachVMHandler;

  private final Map<String, String> launchIdByVmId = new HashMap<>();

  private Set<String> processedVMIds = new HashSet<>();

  private File shutdownAgentFile = null;

  private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

  private final List<Runnable> stateChangeListeners = new ArrayList<>();

  private final VirtualMachineStaticReflect virtualMachineStatic;

  private final Map<String, String> vmIdByLaunchId = new HashMap<>();

  /**
   * Constructor.
   *
   * @param parameter
   *          The parameters how the VMManager is instantiated. See the doc in the
   *          {@link EOSGiVMManagerParameter} class.
   */
  public EOSGiVMManager(final EOSGiVMManagerParameter parameter) {
    Objects.requireNonNull(parameter);
    if (parameter.classLoader == null) {
      throw new NullPointerException();
    }
    this.virtualMachineStatic = new VirtualMachineStaticReflect(parameter.classLoader);

    this.deadlockMessageConsumer = (parameter.deadlockEventHandler != null)
        ? parameter.deadlockEventHandler
        : (eventData) -> {
          throw new RuntimeException("Could not execute command on VM. This happens sometimes"
              + " on Windows systems when the VM stops at the same time as the command is called: "
              + eventData.virtualMachineId);
        };

    this.exceptionDuringAttachVMHandler =
        (parameter.exceptionDuringAttachVMHandler != null)
            ? parameter.exceptionDuringAttachVMHandler
            : (eventData) -> false;

    refresh();

  }

  public synchronized void addStateChangeListener(final Runnable listener) {
    stateChangeListeners.add(listener);
  }

  private void attachAndProcessVM(final VirtualMachineDescriptorReflect virtualMachineDescriptor) {
    VirtualMachineReflect virtualMachine = null;
    try {
      virtualMachine = virtualMachineStatic.attach(virtualMachineDescriptor);
    } catch (RuntimeException e) {
      if (!handleExceptionByConsumer(e, virtualMachineDescriptor.id())) {
        throw e;
      }
      return;
    }

    try (VirtualMachineReflect attachedVirtualMachine = virtualMachine) {
      processVirtualMachine(attachedVirtualMachine);
    }
  }

  @SuppressWarnings("deprecation")
  private <R> R callWithTimeout(final Supplier<R> supplier, final String vmId) {
    AtomicReference<Thread> executorThread = new AtomicReference<>();
    Future<R> future = singleThreadExecutor.submit(() -> {
      executorThread.set(Thread.currentThread());
      return supplier.get();
    });

    try {
      return future.get(DEFAULT_VM_CALL_TIMEOUT, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    } catch (TimeoutException e) {
      Thread thread = executorThread.get();
      if (thread != null && thread.isAlive()) {
        thread.stop();
      }
      singleThreadExecutor.shutdown();
      singleThreadExecutor = Executors.newSingleThreadExecutor();

      EOSGiVMManagerEventData eventData = new EOSGiVMManagerEventData();
      eventData.virtualMachineId = vmId;
      eventData.vmManager = this;
      deadlockMessageConsumer.accept(eventData);
      return null;
    }
  }

  @Override
  public synchronized void close() {
    if (closed) {
      return;
    }
    closed = true;
    singleThreadExecutor.shutdown();
    if (this.shutdownAgentFile != null && !shutdownAgentFile.delete()) {
      throw new UncheckedIOException(new IOException(
          "Could not delete shutdown agent file: " + shutdownAgentFile.getAbsolutePath()));
    }
    launchIdByVmId.clear();
    processedVMIds.clear();
    environmentInfosByEnvironmentId.clear();
    environmentIdByVmId.clear();
    vmIdByLaunchId.clear();
  }

  /**
   * Returns the information of all currently tracked environments.
   *
   * @return the information of all currently tracked environments.
   */
  public synchronized Set<EnvironmentRuntimeInfo> getRuntimeInformations() {
    Set<EnvironmentRuntimeInfo> result = new HashSet<>();

    for (Collection<EnvironmentRuntimeInfo> infos : environmentInfosByEnvironmentId.values()) {

      result.addAll(infos);
    }
    return result;
  }

  /**
   * Returns the available runtime informations for an environment.
   *
   * @param environmentId
   *          The id of the environment.
   * @param environmentRootDir
   *          The root dir of the environment.
   * @return A set of runtime informations or an empty set if no running JVM is available.
   */
  public synchronized Set<EnvironmentRuntimeInfo> getRuntimeInformations(final String environmentId,
      final File environmentRootDir) {
    if (closed) {
      return Collections.emptySet();
    }
    Set<EnvironmentRuntimeInfo> result = new HashSet<>();
    Set<EnvironmentRuntimeInfo> environmentInfos =
        environmentInfosByEnvironmentId.get(environmentId);
    if (environmentInfos == null) {
      return result;
    }

    for (EnvironmentRuntimeInfo environmentRuntimeInfo : environmentInfos) {
      if (isParentOrSameDir(environmentRootDir, environmentRuntimeInfo.userDir)) {
        result.add(environmentRuntimeInfo);
      }
    }
    return result;
  }

  private String getShutdownAgentPath() {
    if (shutdownAgentFile != null) {
      return shutdownAgentFile.getAbsolutePath();
    }

    try {
      this.shutdownAgentFile = File.createTempFile("eosgi-shutdownJavaAgent", null);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    try (InputStream is =
        EOSGiVMManager.class.getResourceAsStream("/org.everit.jdk.javaagent.shutdown-1.0.0.jar");
        OutputStream out = new FileOutputStream(this.shutdownAgentFile)) {

      byte[] buffer = new byte[BUFFER_SIZE];
      int r = is.read(buffer);
      while (r >= 0) {
        out.write(buffer, 0, r);
        r = is.read(buffer);
      }
      return this.shutdownAgentFile.getAbsolutePath();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public synchronized String getVirtualMachineIdByIUniqueLaunchId(final String uniqueLaunchId) {
    return vmIdByLaunchId.get(uniqueLaunchId);
  }

  private boolean handleExceptionByConsumer(final RuntimeException e, final String vmId) {
    EOSGiVMManagerEventData eventData = new EOSGiVMManagerEventData();
    eventData.cause = e;
    eventData.virtualMachineId = vmId;
    eventData.vmManager = this;

    return exceptionDuringAttachVMHandler.apply(eventData);
  }

  private boolean isParentOrSameDir(final File environmentRootDir, final File userDir) {
    File currentDir = userDir;
    while (currentDir != null) {
      if (currentDir.equals(environmentRootDir)) {
        return true;
      }
      currentDir = currentDir.getParentFile();
    }
    return false;
  }

  private void processVirtualMachine(final VirtualMachineReflect virtualMachine) {
    String vmId = virtualMachine.id();
    Properties systemProperties = callWithTimeout(() -> virtualMachine.getSystemProperties(), vmId);

    if (systemProperties == null) {
      return;
    }

    String launchUniqueId = systemProperties.getProperty(DistConstants.SYSPROP_LAUNCH_UNIQUE_ID);

    if (launchUniqueId != null) {
      vmIdByLaunchId.put(launchUniqueId, vmId);
      launchIdByVmId.put(vmId, launchUniqueId);
    }

    String environmentId = systemProperties.getProperty(DistConstants.SYSPROP_ENVIRONMENT_ID);
    if (environmentId == null) {
      return;
    }

    String jmxURL = virtualMachine.startLocalManagementAgent();

    if (jmxURL == null) {
      return;
    }

    String userDir = String.valueOf(systemProperties.get("user.dir"));

    EnvironmentRuntimeInfo environmentRuntimeInfo = new EnvironmentRuntimeInfo();
    environmentRuntimeInfo.jmxServiceURL = jmxURL;
    environmentRuntimeInfo.userDir = new File(userDir);
    environmentRuntimeInfo.virtualMachineId = vmId;
    environmentRuntimeInfo.systemProperties = systemProperties;

    Set<EnvironmentRuntimeInfo> environmentInfos =
        environmentInfosByEnvironmentId.get(environmentId);
    if (environmentInfos == null) {
      environmentInfos = new HashSet<>();
      environmentInfosByEnvironmentId.put(environmentId, environmentInfos);
    }
    environmentInfos.add(environmentRuntimeInfo);
    environmentIdByVmId.put(vmId, environmentId);
  }

  /**
   * Refreshes the information of EOSGi Environment VMs.
   */
  public synchronized void refresh() {
    if (closed) {
      return;
    }
    Set<String> aliveVMIds = new HashSet<>();
    List<VirtualMachineDescriptorReflect> virtualMachines = virtualMachineStatic.list();
    for (VirtualMachineDescriptorReflect virtualMachineDescriptor : virtualMachines) {
      String vmId = virtualMachineDescriptor.id();
      aliveVMIds.add(vmId);
      if (!processedVMIds.contains(vmId)) {

        attachAndProcessVM(virtualMachineDescriptor);
      }
    }
    removeDeadVms(aliveVMIds);

    if (!processedVMIds.equals(aliveVMIds)) {
      processedVMIds = aliveVMIds;
      for (Runnable listener : stateChangeListeners) {
        listener.run();
      }
    }
  }

  private void removeDeadVms(final Set<String> aliveVMIds) {
    for (String vmId : processedVMIds) {
      if (!aliveVMIds.contains(vmId)) {
        String launchId = launchIdByVmId.remove(vmId);
        if (launchId != null) {
          vmIdByLaunchId.remove(launchId);
        }

        String environmentId = environmentIdByVmId.remove(vmId);
        if (environmentId != null) {

          Set<EnvironmentRuntimeInfo> runtimeInfos =
              environmentInfosByEnvironmentId.get(environmentId);

          Iterator<EnvironmentRuntimeInfo> iterator = runtimeInfos.iterator();
          while (iterator.hasNext()) {
            EnvironmentRuntimeInfo environmentRuntimeInfo = iterator.next();
            if (environmentRuntimeInfo.virtualMachineId.equals(vmId)) {
              iterator.remove();
            }
          }
          if (runtimeInfos.isEmpty()) {
            environmentInfosByEnvironmentId.remove(environmentId);
          }
        }
      }
    }
  }

  public synchronized void removeStateChangeListener(final Runnable listener) {
    stateChangeListeners.remove(listener);
  }

  /**
   * Shuts down a Java VirtualMachine.
   *
   * @param virtualMachineId
   *          The id of the virtual machine.
   * @param exitcode
   *          The exit code that the application should return after a normal shutdown.
   * @param forcedShutdownParameter
   *          Parameter that tells why and how forced shutdown should be applied or
   *          <code>null</code> if no forced shutdown should be done.
   */
  public synchronized void shutDownVirtualMachine(final String virtualMachineId, final int exitcode,
      final ForcedShutdownParameter forcedShutdownParameter) {
    if (closed) {
      return;
    }

    try (VirtualMachineReflect virtualMachine = virtualMachineStatic.attach(virtualMachineId)) {
      callWithTimeout(() -> {
        if (forcedShutdownParameter == null) {
          virtualMachine.loadAgent(getShutdownAgentPath());
        } else {
          virtualMachine.loadAgent(getShutdownAgentPath(), "timeout="
              + forcedShutdownParameter.timeout + ",haltcode=" + forcedShutdownParameter.haltCode);
        }
        return null;
      }, virtualMachine.id());

    }
  }
}
