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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.28 at 05:38:02 PM CET 
//


package org.everit.osgi.dev.eosgi.dist.schema.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LaunchConfigurationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LaunchConfigurationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mainJar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mainClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="classPath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="commandArguments" type="{http://everit.org/eosgi/dist/definition/4.0.0}CommandArgumentsType" minOccurs="0"/>
 *         &lt;element name="systemProperties" type="{http://everit.org/eosgi/dist/definition/4.0.0}SystemPropertiesType" minOccurs="0"/>
 *         &lt;element name="vmOptions" type="{http://everit.org/eosgi/dist/definition/4.0.0}VmOptionsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LaunchConfigurationType", propOrder = {
    "mainJar",
    "mainClass",
    "classPath",
    "commandArguments",
    "systemProperties",
    "vmOptions"
})
public class LaunchConfigurationType {

    protected String mainJar;
    protected String mainClass;
    protected String classPath;
    protected CommandArgumentsType commandArguments;
    protected SystemPropertiesType systemProperties;
    protected VmOptionsType vmOptions;

    /**
     * Gets the value of the mainJar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMainJar() {
        return mainJar;
    }

    /**
     * Sets the value of the mainJar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMainJar(String value) {
        this.mainJar = value;
    }

    /**
     * Gets the value of the mainClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMainClass() {
        return mainClass;
    }

    /**
     * Sets the value of the mainClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMainClass(String value) {
        this.mainClass = value;
    }

    /**
     * Gets the value of the classPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassPath() {
        return classPath;
    }

    /**
     * Sets the value of the classPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassPath(String value) {
        this.classPath = value;
    }

    /**
     * Gets the value of the commandArguments property.
     * 
     * @return
     *     possible object is
     *     {@link CommandArgumentsType }
     *     
     */
    public CommandArgumentsType getCommandArguments() {
        return commandArguments;
    }

    /**
     * Sets the value of the commandArguments property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommandArgumentsType }
     *     
     */
    public void setCommandArguments(CommandArgumentsType value) {
        this.commandArguments = value;
    }

    /**
     * Gets the value of the systemProperties property.
     * 
     * @return
     *     possible object is
     *     {@link SystemPropertiesType }
     *     
     */
    public SystemPropertiesType getSystemProperties() {
        return systemProperties;
    }

    /**
     * Sets the value of the systemProperties property.
     * 
     * @param value
     *     allowed object is
     *     {@link SystemPropertiesType }
     *     
     */
    public void setSystemProperties(SystemPropertiesType value) {
        this.systemProperties = value;
    }

    /**
     * Gets the value of the vmOptions property.
     * 
     * @return
     *     possible object is
     *     {@link VmOptionsType }
     *     
     */
    public VmOptionsType getVmOptions() {
        return vmOptions;
    }

    /**
     * Sets the value of the vmOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link VmOptionsType }
     *     
     */
    public void setVmOptions(VmOptionsType value) {
        this.vmOptions = value;
    }

}
