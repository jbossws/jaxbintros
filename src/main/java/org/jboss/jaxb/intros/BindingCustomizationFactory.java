/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.jaxb.intros;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jboss.jaxb.intros.configmodel.JaxbIntros;

import com.sun.xml.bind.api.JAXBRIContext;

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
       JaxbIntros jaxbIntros = IntroductionsConfigParser.parseConfig(introsConfigStream);
       IntroductionsAnnotationReader annotationReader = new IntroductionsAnnotationReader(jaxbIntros);
       String defaultNamespace = namespace != null ? namespace : jaxbIntros.getDefaultNamespace();
       Map<String, Object> jaxbCustomizations = new HashMap<String, Object>();

       jaxbCustomizations.put(JAXBRIContext.ANNOTATION_READER, annotationReader);
       if(defaultNamespace != null) {
          jaxbCustomizations.put(JAXBRIContext.DEFAULT_NAMESPACE_REMAP, defaultNamespace);
       }
       return jaxbCustomizations;
	}
}
