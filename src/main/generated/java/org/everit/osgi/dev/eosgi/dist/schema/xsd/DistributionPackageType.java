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
// Generated on: 2015.10.26 at 10:22:15 AM CET 
//


package org.everit.osgi.dev.eosgi.dist.schema.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DistributionPackageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistributionPackageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="artifacts" type="{http://everit.org/eosgi/dist/definition/4.0.0}ArtifactsType" minOccurs="0"/>
 *         &lt;element name="parseables" type="{http://everit.org/eosgi/dist/definition/4.0.0}ParseablesType" minOccurs="0"/>
 *         &lt;element name="launchConfiguration" type="{http://everit.org/eosgi/dist/definition/4.0.0}LaunchConfigurationType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="environmentId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="copyMode" use="required" type="{http://everit.org/eosgi/dist/definition/4.0.0}CopyModeType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DistributionPackageType", propOrder = {
    "artifacts",
    "parseables",
    "launchConfiguration"
})
public class DistributionPackageType {

    protected ArtifactsType artifacts;
    protected ParseablesType parseables;
    protected LaunchConfigurationType launchConfiguration;
    @XmlAttribute(name = "environmentId", required = true)
    protected String environmentId;
    @XmlAttribute(name = "copyMode", required = true)
    protected CopyModeType copyMode;

    /**
     * Gets the value of the artifacts property.
     * 
     * @return
     *     possible object is
     *     {@link ArtifactsType }
     *     
     */
    public ArtifactsType getArtifacts() {
        return artifacts;
    }

    /**
     * Sets the value of the artifacts property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArtifactsType }
     *     
     */
    public void setArtifacts(ArtifactsType value) {
        this.artifacts = value;
    }

    /**
     * Gets the value of the parseables property.
     * 
     * @return
     *     possible object is
     *     {@link ParseablesType }
     *     
     */
    public ParseablesType getParseables() {
        return parseables;
    }

    /**
     * Sets the value of the parseables property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParseablesType }
     *     
     */
    public void setParseables(ParseablesType value) {
        this.parseables = value;
    }

    /**
     * Gets the value of the launchConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link LaunchConfigurationType }
     *     
     */
    public LaunchConfigurationType getLaunchConfiguration() {
        return launchConfiguration;
    }

    /**
     * Sets the value of the launchConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link LaunchConfigurationType }
     *     
     */
    public void setLaunchConfiguration(LaunchConfigurationType value) {
        this.launchConfiguration = value;
    }

    /**
     * Gets the value of the environmentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvironmentId() {
        return environmentId;
    }

    /**
     * Sets the value of the environmentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvironmentId(String value) {
        this.environmentId = value;
    }

    /**
     * Gets the value of the copyMode property.
     * 
     * @return
     *     possible object is
     *     {@link CopyModeType }
     *     
     */
    public CopyModeType getCopyMode() {
        return copyMode;
    }

    /**
     * Sets the value of the copyMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CopyModeType }
     *     
     */
    public void setCopyMode(CopyModeType value) {
        this.copyMode = value;
    }

}
