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

import junit.framework.TestCase;

import org.jboss.jaxb.intros.configmodel.ClassIntroConfig;
import org.jboss.jaxb.intros.configmodel.JaxbIntros;

/**
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class IntroductionsConfigParserUnitTest extends TestCase {

    public void test() throws ConfigurationException, IOException {
        JaxbIntros config = IntroductionsConfigParser.parseConfig(new File("target/test-classes/intro-config-01.xml").toURL().openStream());

        assertEquals("http://jbossesb.x.jboss.org", config.getDefaultNamespace());
        assertEquals(2, config.getClazz().size());
        ClassIntroConfig classIntroConfig = config.getClazz().get(0);
        assertEquals(TestBean1.class.getName(), classIntroConfig.getName());
        assertEquals("http://jbossesb.y.jboss.org", classIntroConfig.getXmlType().getNamespace());
        assertEquals(1, classIntroConfig.getMethod().size());

        // Could add more tests here but we're OK for now... JAXB seems to be working fine and
        // we'll have more tests on the AnnotationReader!
    }
}
