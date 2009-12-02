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
