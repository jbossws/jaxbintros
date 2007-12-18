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

import org.jboss.jaxb.intros.configmodel.XmlAttributeIntro;

import javax.xml.bind.annotation.XmlAttribute;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.annotation.Annotation;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class XmlAttributeHandler implements InvocationHandler
{

   private XmlAttributeIntro xmlAttributeIntro;

   public XmlAttributeHandler(XmlAttributeIntro xmlAttributeIntro)
   {
      this.xmlAttributeIntro = xmlAttributeIntro;
   }

   public static Annotation createProxy(XmlAttributeIntro xmlAttributeIntro)
   {
      return (Annotation)Proxy.newProxyInstance(XmlAttributeIntro.class.getClassLoader(),
        new Class[]{XmlAttribute.class},
        new XmlAttributeHandler(xmlAttributeIntro));
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      /*
     <xsd:complexType>
         <xsd:attribute name="name" use="optional" default="##default"/>
         <xsd:attribute name="required" type="xsd:boolean" use="optional" default="false"/>
         <xsd:attribute name="namespace" use="optional" default="##default"/>
     </xsd:complexType>
      */

      String methodName = method.getName();

      if (methodName.equals("namespace"))
      {
         return xmlAttributeIntro.getNamespace();
      }
      else if (methodName.equals("name"))
      {
         return xmlAttributeIntro.getName();
      }
      else if (methodName.equals("required"))
      {
         return xmlAttributeIntro.isRequired();
      }
      else if (methodName.equals("annotationType"))
      {
         return XmlAttribute.class;
      }

      return null;
   }
}
