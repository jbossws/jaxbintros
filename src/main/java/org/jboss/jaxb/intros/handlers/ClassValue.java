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
package org.jboss.jaxb.intros.handlers;

import java.lang.annotation.Annotation;

/**
 * Annotation Class Value interface.
 * <p>
 * See same methods on the {@link org.glassfish.jaxb.core.v2.model.annotation.AnnotationReader} interface
 * We want to proxy these calls back to the annotation handlers.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public interface ClassValue
{

   Class<?> getClassValue(Annotation a, String name);

   Class<?>[] getClassArrayValue(Annotation a, String name);
}
