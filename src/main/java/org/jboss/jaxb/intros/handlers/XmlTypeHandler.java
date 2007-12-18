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

import org.jboss.jaxb.intros.configmodel.XmlTypeIntro;
import org.jboss.jaxb.intros.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.annotation.XmlType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.annotation.Annotation;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class XmlTypeHandler implements InvocationHandler
{

   private static final Log logger = LogFactory.getLog(XmlTypeHandler.class);
   private XmlTypeIntro xmlTypeIntro;

   private XmlTypeHandler(XmlTypeIntro xmlTypeIntro)
   {
      this.xmlTypeIntro = xmlTypeIntro;
   }

   public static Annotation createProxy(XmlTypeIntro xmlTypeIntro)
   {
      return (Annotation)Proxy.newProxyInstance(XmlTypeIntro.class.getClassLoader(),
        new Class[]{XmlType.class, ClassValue.class},
        new XmlTypeHandler(xmlTypeIntro));
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      /*
      <xsd:complexType>
          <xsd:attribute name="name" use="optional" default="##default"/>
          <xsd:attribute name="propOrder" use="optional" default=""/>
          <xsd:attribute name="namespace" use="optional" default="##default"/>
          <xsd:attribute name="factoryClass" use="optional"/>
          <xsd:attribute name="factoryMethod" use="optional" default=""/>
      </xsd:complexType>
      */
      String methodName = method.getName();

      if (methodName.equals("getClassValue"))
      {
         methodName = (String)args[1];
      }

      if (methodName.equals("namespace"))
      {
         return xmlTypeIntro.getNamespace();
      }
      else if (methodName.equals("name"))
      {
         return xmlTypeIntro.getName();
      }
      else if (methodName.equals("propOrder"))
      {
         try
         {
            String[] propOrder = xmlTypeIntro.getPropOrder().split(",");
            for (int i = 0; i < propOrder.length; i++) 
            {
               String s = propOrder[i];
               propOrder[i] = s.trim();
            }
            return propOrder;
         }
         catch (Exception e)
         {
            logger.warn("Bad 'XmlType.propOrder' config value '" + xmlTypeIntro.getPropOrder() + "' in JAXB Annotation Introduction config.  Must be a CSV String.");
         }
      }
      else if (methodName.equals("factoryClass"))
      {
         if (xmlTypeIntro.getFactoryClass() != null)
         {
            try
            {
               return Class.forName(xmlTypeIntro.getFactoryClass());
            }
            catch (ClassNotFoundException e)
            {
               throw new ConfigurationException("Bad 'XmlType.factoryClass' config value '" + xmlTypeIntro.getFactoryClass() + "' in JAXB Annotation Introduction config.  Class not found.");
            }
         }
         return XmlType.DEFAULT.class;
      }
      else if (methodName.equals("factoryMethod"))
      {
         return xmlTypeIntro.getFactoryMethod();
      }

      return null;
   }
}
