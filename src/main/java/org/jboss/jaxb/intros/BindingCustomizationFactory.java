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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jboss.jaxb.intros.configmodel.JaxbIntros;

import org.glassfish.jaxb.runtime.api.JAXBRIContext;

/**
 * A factory class that creates BindingCustomization instances using JAXBIntros.
 * This basically a convenient class for getting a binding customizations
 * ready for being installed in a jaxb context while hiding the internals
 * of jaxb-impl. 
 * 
 * @author alessio.soldano@jboss.com
 * @since 07-Oct-2009
 *
 */
public class BindingCustomizationFactory
{
   public static Map<String, Object> getBindingCustomization(InputStream introsConfigStream)
   {
      return getBindingCustomization(introsConfigStream, null);
   }

   public static Map<String, Object> getBindingCustomization(InputStream introsConfigStream, String namespace)
   {
      Map<String, Object> jaxbCustomizations = new HashMap<String, Object>();
      populateBindingCustomization(introsConfigStream, namespace, jaxbCustomizations);
      return jaxbCustomizations;
   }
   
   public static void populateBindingCustomization(InputStream introsConfigStream, Map<String, Object> customization)
   {
      populateBindingCustomization(introsConfigStream, null, customization);
   }

   public static void populateBindingCustomization(InputStream introsConfigStream, String namespace, Map<String, Object> customization)
   {
      JaxbIntros jaxbIntros = IntroductionsConfigParser.parseConfig(introsConfigStream);
      IntroductionsAnnotationReader annotationReader = new IntroductionsAnnotationReader(jaxbIntros);
      String defaultNamespace = namespace != null ? namespace : jaxbIntros.getDefaultNamespace();

      customization.put(JAXBRIContext.ANNOTATION_READER, annotationReader);
      if (defaultNamespace != null)
      {
         customization.put(JAXBRIContext.DEFAULT_NAMESPACE_REMAP, defaultNamespace);
      }
   }
}
