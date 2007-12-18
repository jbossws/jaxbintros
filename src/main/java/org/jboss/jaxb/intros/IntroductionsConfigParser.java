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
package org.jboss.jaxb.intros;

import org.jboss.jaxb.intros.configmodel.JaxbIntros;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

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