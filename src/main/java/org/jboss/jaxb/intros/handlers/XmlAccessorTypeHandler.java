/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006, JBoss Inc.
 */
package org.jboss.jaxb.intros.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.jaxb.intros.configmodel.XmlAccessorTypeIntro;

/**
 * @author <a href="mailto:chris@swaton.org">chris@swaton.org</a>
 */
public class XmlAccessorTypeHandler implements InvocationHandler
{

   private static final Log logger = LogFactory.getLog(XmlAccessorTypeHandler.class);
   private XmlAccessorTypeIntro xmlAccessorTypeIntro;

   private XmlAccessorTypeHandler(XmlAccessorTypeIntro xmlAccessorTypeIntro)
   {
      this.xmlAccessorTypeIntro = xmlAccessorTypeIntro;
   }

   public static Annotation createProxy(XmlAccessorTypeIntro xmlAccessorTypeIntro)
   {
      return (Annotation)Proxy.newProxyInstance(XmlAccessorTypeIntro.class.getClassLoader(),
        new Class[]{XmlAccessorType.class, ClassValue.class},
        new XmlAccessorTypeHandler(xmlAccessorTypeIntro));
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      /*
      <xsd:complexType>
        <xsd:attribute name="value" use="optional" default="NONE">
          <xsd:simpleType>
            <xsd:restriction base="xsd:NMTOKEN">
              <xsd:enumeration value="PROPERTY"/>
              <xsd:enumeration value="FIELD"/>
              <xsd:enumeration value="PUBLIC_MEMBER"/>
              <xsd:enumeration value="NONE"/>
            </xsd:restriction>
          </xsd:simpleType>
        </xsd:attribute>
      </xsd:complexType>
      */

      String methodName = method.getName();
      if (methodName.equals("value"))
      {
         return XmlAccessType.valueOf(xmlAccessorTypeIntro.getValue());
      }
      return null;
   }
}
