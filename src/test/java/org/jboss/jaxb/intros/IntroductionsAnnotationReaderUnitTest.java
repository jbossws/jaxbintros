/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jboss.jaxb.intros;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.jboss.jaxb.intros.configmodel.JaxbIntros;
import org.jboss.jaxb.intros.testbeans.CustomerOrder;

import org.glassfish.jaxb.runtime.api.JAXBRIContext;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class IntroductionsAnnotationReaderUnitTest extends TestCase {

    public void test_annotation_intro() throws ConfigurationException, NoSuchMethodException, NoSuchFieldException, IOException {
        JaxbIntros config = IntroductionsConfigParser.parseConfig(new File("target/test-classes/intro-config-02.xml").toURL().openStream());
        IntroductionsAnnotationReader reader = new IntroductionsAnnotationReader(config);
        Method testBean1Method = TestBean1.class.getMethod("getOrderDate");
        Field testBean2Field = TestBean2.class.getField("orderNumber");

        assertTrue("Failed", reader.hasClassAnnotation(TestBean1.class, XmlType.class));
        XmlType xmlType = reader.getClassAnnotation(XmlType.class, TestBean1.class, null);
        assertEquals("http://jbossesb.y.jboss.org", xmlType.namespace());
        String[] propOrder = xmlType.propOrder();
        assertEquals(3, propOrder.length);
        assertTrue(propOrder[0].equals("a") && propOrder[1].equals("b") && propOrder[2].equals("c"));
        assertTrue(xmlType.factoryClass() == TestBean1.class);

        XmlAttribute xmlAttribute = reader.getMethodAnnotation(XmlAttribute.class, testBean1Method, null);
        assertEquals("##default", xmlAttribute.name());
        assertEquals("http://jbossesb.z.jboss.org", xmlAttribute.namespace());
        assertFalse(xmlAttribute.required());

        XmlElement xmlElement = reader.getFieldAnnotation(XmlElement.class, testBean2Field, null);
        assertEquals("OrderNum", xmlElement.name());
        assertTrue(xmlElement.nillable());
        assertTrue(xmlElement.type() == TestBean2.class);

        xmlAttribute = reader.getFieldAnnotation(XmlAttribute.class, testBean2Field, null);
        assertEquals("http://jbossesb.org", xmlAttribute.namespace());
    }

    public void test_jaxb_unmarshal() throws ConfigurationException, JAXBException, IOException {
        JaxbIntros config = IntroductionsConfigParser.parseConfig(new File("target/test-classes/intro-config-03.xml").toURL().openStream());
        IntroductionsAnnotationReader reader = new IntroductionsAnnotationReader(config);
        Map<String, Object> jaxbConfig = new HashMap<String, Object>();

        jaxbConfig.put(JAXBRIContext.DEFAULT_NAMESPACE_REMAP, "http://org.jboss.esb/namespace2");
        jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, reader);

        JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] {CustomerOrder.class}, jaxbConfig);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        JAXBElement<?> jbe = unmarshaller.unmarshal(new StreamSource(new File("target/test-classes/order-message.xml").toURL().openStream()), CustomerOrder.class);
        CustomerOrder order = (CustomerOrder) jbe.getValue();

        assertNotNull("null Order", order);
        assertNotNull("null Header", order.getHeader());
        assertNotNull("null Items", order.getItems());
        assertEquals("Shelton", order.getHeader().getBillTo().getAddress().getCity());
        assertEquals("06484", order.getHeader().getBillTo().getAddress().getZip());
        assertEquals("bob@activeSteel.com", order.getHeader().getBillTo().getContact().getEmail());
        assertEquals(1146441600000L, order.getHeader().getOrderDate().getTimeInMillis());
        assertEquals((new BigDecimal(490.00)).toBigInteger(), (order.getItems()[0].getPrice()).toBigInteger());
    }
}
