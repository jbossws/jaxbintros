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

import org.jboss.jaxb.intros.configmodel.XmlElementIntro;
import org.jboss.jaxb.intros.ConfigurationException;

import javax.xml.bind.annotation.XmlElement;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.annotation.Annotation;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class XmlElementHandler implements InvocationHandler
{

   private XmlElementIntro xmlElementIntro;

   private XmlElementHandler(XmlElementIntro xmlElementIntro)
   {
      this.xmlElementIntro = xmlElementIntro;
   }

   public static Annotation createProxy(XmlElementIntro xmlElementIntro)
   {
      return (Annotation)Proxy.newProxyInstance(XmlElementIntro.class.getClassLoader(),
        new Class[]{XmlElement.class, ClassValue.class},
        new XmlElementHandler(xmlElementIntro));
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      /*
      <xsd:complexType>
          <xsd:attribute name="name" use="optional" default="##default"/>
          <xsd:attribute name="nillable" type="xsd:boolean" use="optional" default="false"/>
          <xsd:attribute name="required" type="xsd:boolean" use="optional" default="false"/>
          <xsd:attribute name="namespace" use="optional" default="##default"/>
          <xsd:attribute name="defaultValue" use="optional"/>
          <xsd:attribute name="type" use="optional"/>
      </xsd:complexType>
      */
      String methodName = method.getName();

      if (methodName.equals("getClassValue"))
      {
         methodName = (String)args[1];
      }

      if (methodName.equals("namespace"))
      {
         return xmlElementIntro.getNamespace();
      }
      else if (methodName.equals("name"))
      {
         return xmlElementIntro.getName();
      }
      else if (methodName.equals("nillable"))
      {
         return xmlElementIntro.isNillable();
      }
      else if (methodName.equals("required"))
      {
         return xmlElementIntro.isRequired();
      }
      else if (methodName.equals("defaultValue"))
      {
         String defaultVal = xmlElementIntro.getDefaultValue();
         return (defaultVal != null ? defaultVal : "\u0000");
      }
      else if (methodName.equals("type"))
      {
         if (xmlElementIntro.getType() != null)
         {
            try
            {
               return Class.forName(xmlElementIntro.getType());
            }
            catch (ClassNotFoundException e)
            {
               throw new ConfigurationException("Bad 'XmlElement.type' config value '" + xmlElementIntro.getType() + "' in JAXB Annotation Introduction config.  Class not found.");
            }
         }
         return XmlElement.DEFAULT.class;
      }
      else if (methodName.equals("annotationType"))
      {
         return XmlElement.class;
      }

      return null;
   }
}
