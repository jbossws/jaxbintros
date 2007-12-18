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

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.jaxb.intros.configmodel.XmlRootElementIntro;

/**
 * @author <a href="mailto:chris@swaton.org">chris@swaton.org</a>
 */
public class XmlRootElementHandler implements InvocationHandler
{

   private static final Log logger = LogFactory.getLog(XmlRootElementHandler.class);
   private XmlRootElementIntro xmlRootElementIntro;

   private XmlRootElementHandler(XmlRootElementIntro xmlRootElementIntro)
   {
      this.xmlRootElementIntro = xmlRootElementIntro;
   }

   public static Annotation createProxy(XmlRootElementIntro xmlRootElementIntro)
   {
      return (Annotation)Proxy.newProxyInstance(XmlRootElementIntro.class.getClassLoader(),
        new Class[]{XmlRootElement.class, ClassValue.class},
        new XmlRootElementHandler(xmlRootElementIntro));
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      /*
      <xsd:complexType>
          <xsd:attribute name="namespace" use="optional" default="##default"/>
          <xsd:attribute name="name" use="optional" default="##default"/>
      </xsd:complexType>
      */

      String methodName = method.getName();
      if (methodName.equals("namespace"))
      {
         return xmlRootElementIntro.getNamespace();
      }
      else if (methodName.equals("name"))
      {
         return xmlRootElementIntro.getName();
      }
      return null;
   }
}
