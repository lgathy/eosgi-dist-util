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
// Generated on: 2016.06.02 at 10:57:33 AM CEST 
//


package org.everit.osgi.dev.dist.util.configuration.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.everit.osgi.dev.dist.util.configuration.schema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Environment_QNAME = new QName("http://everit.org/eosgi/dist/definition/4.0.0", "environment");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.everit.osgi.dev.dist.util.configuration.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EnvironmentType }
     * 
     */
    public EnvironmentType createEnvironmentType() {
        return new EnvironmentType();
    }

    /**
     * Create an instance of {@link RuntimePathRegexesType }
     * 
     */
    public RuntimePathRegexesType createRuntimePathRegexesType() {
        return new RuntimePathRegexesType();
    }

    /**
     * Create an instance of {@link ParsablesType }
     * 
     */
    public ParsablesType createParsablesType() {
        return new ParsablesType();
    }

    /**
     * Create an instance of {@link PropertiesType }
     * 
     */
    public PropertiesType createPropertiesType() {
        return new PropertiesType();
    }

    /**
     * Create an instance of {@link LaunchConfigOverridesType }
     * 
     */
    public LaunchConfigOverridesType createLaunchConfigOverridesType() {
        return new LaunchConfigOverridesType();
    }

    /**
     * Create an instance of {@link ParsableType }
     * 
     */
    public ParsableType createParsableType() {
        return new ParsableType();
    }

    /**
     * Create an instance of {@link EntryType }
     * 
     */
    public EntryType createEntryType() {
        return new EntryType();
    }

    /**
     * Create an instance of {@link ArtifactsType }
     * 
     */
    public ArtifactsType createArtifactsType() {
        return new ArtifactsType();
    }

    /**
     * Create an instance of {@link ArtifactType }
     * 
     */
    public ArtifactType createArtifactType() {
        return new ArtifactType();
    }

    /**
     * Create an instance of {@link LaunchConfigType }
     * 
     */
    public LaunchConfigType createLaunchConfigType() {
        return new LaunchConfigType();
    }

    /**
     * Create an instance of {@link ArgumentsType }
     * 
     */
    public ArgumentsType createArgumentsType() {
        return new ArgumentsType();
    }

    /**
     * Create an instance of {@link LaunchConfigOverrideType }
     * 
     */
    public LaunchConfigOverrideType createLaunchConfigOverrideType() {
        return new LaunchConfigOverrideType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnvironmentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://everit.org/eosgi/dist/definition/4.0.0", name = "environment")
    public JAXBElement<EnvironmentType> createEnvironment(EnvironmentType value) {
        return new JAXBElement<EnvironmentType>(_Environment_QNAME, EnvironmentType.class, null, value);
    }

}
