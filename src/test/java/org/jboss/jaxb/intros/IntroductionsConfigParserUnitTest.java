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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;
import org.jboss.jaxb.intros.configmodel.JaxbIntros;
import org.jboss.jaxb.intros.configmodel.ClassIntroConfig;

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
