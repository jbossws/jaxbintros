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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import org.jboss.jaxb.intros.configmodel.JaxbIntros;

/**
 * Configuration Parser for a JBossESB JAXB Annotations Introduction Configuration.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public abstract class IntroductionsConfigParser
{

   /**
    * Parse a JAXB Annotations Introduction Configuration stream.
    *
    * @param config The configuration stream.
    * @return The configuration model.
    * @throws ConfigurationException Bad configuration.
    */
   public static JaxbIntros parseConfig(InputStream config) throws ConfigurationException
   {
      try
      {
         JAXBContext jaxbContext = JAXBContext.newInstance(JaxbIntros.class);
         Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

         return (JaxbIntros)unmarshaller.unmarshal(config);
      }
      catch (JAXBException e)
      {
         throw new ConfigurationException("Bad JAXB Annotations Introduction Configuration.", e);
      }
   }
}