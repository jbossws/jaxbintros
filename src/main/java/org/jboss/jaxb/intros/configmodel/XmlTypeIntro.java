//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.04 at 10:24:07 AM CEST 
//


package org.jboss.jaxb.intros.configmodel;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XmlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XmlType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="##default" />
 *       &lt;attribute name="propOrder" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="" />
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="##default" />
 *       &lt;attribute name="factoryClass" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="factoryMethod" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XmlType")
public class XmlTypeIntro {

    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String name;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String propOrder;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String namespace;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String factoryClass;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String factoryMethod;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        if (name == null) {
            return "##default";
        } else {
            return name;
        }
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the propOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPropOrder() {
        if (propOrder == null) {
            return "";
        } else {
            return propOrder;
        }
    }

    /**
     * Sets the value of the propOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPropOrder(String value) {
        this.propOrder = value;
    }

    /**
     * Gets the value of the namespace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNamespace() {
        if (namespace == null) {
            return "##default";
        } else {
            return namespace;
        }
    }

    /**
     * Sets the value of the namespace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNamespace(String value) {
        this.namespace = value;
    }

    /**
     * Gets the value of the factoryClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryClass() {
        return factoryClass;
    }

    /**
     * Sets the value of the factoryClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryClass(String value) {
        this.factoryClass = value;
    }

    /**
     * Gets the value of the factoryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactoryMethod() {
        if (factoryMethod == null) {
            return "";
        } else {
            return factoryMethod;
        }
    }

    /**
     * Sets the value of the factoryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactoryMethod(String value) {
        this.factoryMethod = value;
    }

}
