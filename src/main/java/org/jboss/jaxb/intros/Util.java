package org.jboss.jaxb.intros;

import java.lang.annotation.Annotation;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;

public class Util {
	private static final ConvertUtilsBean CONVERT_UTILS = new ConvertUtilsBean();
	
	private Util() {
		
	}
	
	public static Object getProperty(Object obj, String name) {
		try {
			return PropertyUtils.getProperty(obj, name);
		} catch (Exception e) {
			throw new RuntimeException(String.format("failed to get property %s from object %s", name, obj), e);
		}
	}
	
	public static Object getProperty(Object obj, Class<? extends Annotation> annotation) {
		return getProperty(obj, getPropertyName(annotation));
	}
	
   private static String getPropertyName(Class<?> cls) {
	   String name = cls.getSimpleName();
	   if (name.length() == 1) {
		   return name.toLowerCase();
	   } else {
		   return name.substring(0,1).toLowerCase() + name.substring(1);
	   }
   }

	public static Object convert(Object value, Class<?> returnType) {
		if (value == null) {
			return null;
		}
		if (value != null && !nonPrimitive(returnType).isAssignableFrom(nonPrimitive(value.getClass()))) {
			if (returnType.isEnum()) {
				return convertEnum(returnType, value.toString());
			}
			value = CONVERT_UTILS.convert(value.toString(), returnType);
		}
		// don't cast due to (possibly necessary) auto-unboxing
		return value;
	}

	@SuppressWarnings("unchecked")
	private static Object convertEnum(Class returnType, String value) {
		return Enum.valueOf(returnType, value);
	}
	
	public static Class<?> nonPrimitive(Class<?> cls) {
		if (!cls.isPrimitive()) {
			return cls;
		}
		if (boolean.class == cls) return Boolean.class;
		if (byte.class == cls) return Byte.class;
		if (char.class == cls) return Character.class;
		if (short.class == cls) return Short.class;
		if (int.class == cls) return Integer.class;
		if (long.class == cls) return Long.class;
		if (float.class == cls) return Float.class;
		if (byte.class == cls) return Double.class;
		if (void.class == cls) return Void.class;
		throw new RuntimeException("undiscovered primitive type? " + cls.getName());
	}
}
