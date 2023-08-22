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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.jboss.jaxb.intros.Util;

public class Handler implements InvocationHandler {

	public static <T extends Annotation> Annotation createProxy(Class<T> annotationClass, Object intro) {
		return annotationClass.cast(Proxy
				.newProxyInstance(Handler.class.getClassLoader(), new Class<?>[] {
						annotationClass, ClassValue.class }, new Handler(annotationClass, intro)));
	}

	private Class<? extends Annotation> _annotationClass;
	private Object _intro;

	private Handler(Class<? extends Annotation> annotationClass, Object intro) {
		if (annotationClass == null) {
			throw new NullPointerException("annotationClass");
		}
		if (intro == null) {
			throw new NullPointerException("intro");
		}
		_annotationClass = annotationClass;
		_intro = intro;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		if (methodName.equals("getClassValue")) {
			methodName = (String) args[1];
		}
		
		if (methodName.equals("annotationType")) {
			return _annotationClass;
		}

		Object value;
		value = Util.getProperty(_intro, methodName);
		if (value != null) {
			value = Util.convert(value, method.getReturnType());
		}
		if (value == null) {
			value = _annotationClass.getMethod(methodName).getDefaultValue();
		}
		return value;
	}
}
