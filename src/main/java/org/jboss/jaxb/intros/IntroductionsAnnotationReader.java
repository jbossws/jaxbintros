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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.jaxb.intros.configmodel.ClassIntroConfig;
import org.jboss.jaxb.intros.configmodel.ClassMemberIntroConfig;
import org.jboss.jaxb.intros.configmodel.FieldIntroConfig;
import org.jboss.jaxb.intros.configmodel.JaxbIntros;
import org.jboss.jaxb.intros.configmodel.MethodIntroConfig;
import org.jboss.jaxb.intros.handlers.ClassValue;
import org.jboss.jaxb.intros.handlers.Handler;

import com.sun.xml.bind.v2.model.annotation.AbstractInlineAnnotationReaderImpl;
import com.sun.xml.bind.v2.model.annotation.Locatable;
import com.sun.xml.bind.v2.model.annotation.LocatableAnnotation;
import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.bind.v2.model.annotation.RuntimeInlineAnnotationReader;

/**
 * JAXB Annotation Reader for the JAXB RI context interface.
 * <p/>
 * Used for introduction of annotations on bean classes which are not annotated
 * for JAXB.  Allows us to use JAXB (and hence JBossWS 2.x+) with unannotated
 * interfaces.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 * @author <a href="mailto:stf@molindo.at">Stefan Fussenegger</a> 
 */
@SuppressWarnings("unchecked")
public class IntroductionsAnnotationReader extends AbstractInlineAnnotationReaderImpl<Type, Class, Field, Method> implements RuntimeAnnotationReader
{

   private static final Log logger = LogFactory.getLog(IntroductionsAnnotationReader.class);
   private RuntimeAnnotationReader baseReader = new RuntimeInlineAnnotationReader();
   private JaxbIntros introductions;

   public static Set<Class<? extends Annotation>> AVAILABLE_CLASS_ANNOTATIONS = IntroductionsAnnotationReader.<Class<? extends Annotation>>unmodifiableSet(
		   XmlAccessorType.class,
		   XmlType.class,
		   XmlRootElement.class,
		   XmlTransient.class,
		   XmlJavaTypeAdapter.class   
   );

   public static Set<Class<? extends Annotation>> AVAILABLE_MEMBER_ANNOTATIONS = IntroductionsAnnotationReader.<Class<? extends Annotation>>unmodifiableSet(
		   XmlElement.class,
		   XmlAttribute.class,
		   XmlTransient.class,
		   XmlID.class,
		   XmlIDREF.class,
		   XmlElementWrapper.class,
		   XmlJavaTypeAdapter.class
   );

   private static <T> Set<T> unmodifiableSet(T ... objects) {
	   return Collections.unmodifiableSet(new HashSet<T>(Arrays.asList(objects)));
   }
   
   public IntroductionsAnnotationReader(JaxbIntros introductions)
   {
      if (introductions == null)
      {
         throw new IllegalArgumentException("arg 'introductions' is null.");
      }
      this.introductions = introductions;
   }

   public <A extends Annotation> A getFieldAnnotation(Class<A> annotation, Field field, Locatable srcPos)
   {
      Annotation proxy = getProxy(annotation, field);

      if (proxy != null)
      {
         return annotation.cast(proxy);
      }

      return LocatableAnnotation.create(field.getAnnotation(annotation), srcPos);
   }

   public boolean hasFieldAnnotation(Class<? extends Annotation> annotationType, Field field)
   {
      FieldIntroConfig fieldIntroConfig = getFieldIntroConfig(field);

      if (fieldIntroConfig != null)
      {
         return isMemberAnnotationIntroAvailable(annotationType, fieldIntroConfig);
      }

      return field.isAnnotationPresent(annotationType);
   }

   public Annotation[] getAllFieldAnnotations(Field field, Locatable srcPos)
   {
      return getAllMemberAnnotations(field, srcPos);
   }

   public <A extends Annotation> A getMethodAnnotation(Class<A> annotation, Method method, Locatable srcPos)
   {
      A proxy = annotation.cast(getProxy(annotation, method));

      if (proxy != null)
      {
         return proxy;
      }

      return LocatableAnnotation.create(method.getAnnotation(annotation), srcPos);
   }

   public boolean hasMethodAnnotation(Class<? extends Annotation> annotation, Method method)
   {
      MethodIntroConfig methodAnnotations = getMethodIntroConfig(method);

      if (methodAnnotations != null)
      {
         return isMemberAnnotationIntroAvailable(annotation, methodAnnotations);
      }

      return method.isAnnotationPresent(annotation);
   }

   public Annotation[] getAllMethodAnnotations(Method method, Locatable srcPos)
   {
      return getAllMemberAnnotations(method, srcPos);
   }

   public <A extends Annotation> A getMethodParameterAnnotation(Class<A> annotation, Method method, int paramIndex, Locatable srcPos)
   {
      return baseReader.getMethodParameterAnnotation(annotation, method, paramIndex, srcPos);
   }

   public <A extends Annotation> A getClassAnnotation(Class<A> annotation, Class clazz, Locatable srcPos)
   {
      Annotation proxy = getProxy(annotation, clazz);

      if (proxy != null)
      {
         return annotation.cast(proxy);
      }

      return LocatableAnnotation.create(((Class<?>)clazz).getAnnotation(annotation), srcPos);
   }

   public boolean hasClassAnnotation(Class clazz, Class<? extends Annotation> annotationType)
   {
      ClassIntroConfig classAnnotations = getClassIntroConfig(clazz);

      if (classAnnotations != null)
      {
         return isClassAnnotationIntroAvailable(annotationType, classAnnotations);
      }

      return clazz.isAnnotationPresent(annotationType);
   }

   public <A extends Annotation> A getPackageAnnotation(Class<A> a, Class clazz, Locatable srcPos)
   {
      return baseReader.getPackageAnnotation(a, clazz, srcPos);
   }

   public Class<?> getClassValue(Annotation a, String name)
   {
      if (a instanceof ClassValue)
      {
         return ((ClassValue)a).getClassValue(a, name);
      }
      return (Class<?>)baseReader.getClassValue(a, name);
   }

   public Class<?>[] getClassArrayValue(Annotation a, String name)
   {
      if (a instanceof ClassValue)
      {
         return ((ClassValue)a).getClassArrayValue(a, name);
      }
      return (Class[])baseReader.getClassArrayValue(a, name);
   }

   protected String fullName(Method m)
   {
      return m.getDeclaringClass().getName() + '#' + m.getName();
   }

   private ClassIntroConfig getClassIntroConfig(Class<?> clazz)
   {
      String className = clazz.getName();
      ClassIntroConfig globalIntro = null;

      for (ClassIntroConfig classIntro : introductions.getClazz())
      {
         if (classIntro.getName().equals(className))
         {
            return classIntro;
         }
         else if (globalIntro == null && isRegexMatch(className, classIntro.getName()))
         {
            globalIntro = classIntro;
         }
      }

      return globalIntro;
   }

   private FieldIntroConfig getFieldIntroConfig(Field field)
   {
      ClassIntroConfig classIntroConfig = getClassIntroConfig(field.getDeclaringClass());

      if (classIntroConfig != null)
      {
         String fieldName = field.getName();

         for (FieldIntroConfig fieldIntro : classIntroConfig.getField())
         {
            if (fieldIntro.getName().equals(fieldName))
            {
               return fieldIntro;
            }
            else if (isRegexMatch(fieldName, fieldIntro.getName()))
            {
               return fieldIntro;
            }
         }
      }

      return null;
   }

   private MethodIntroConfig getMethodIntroConfig(Method method)
   {
      ClassIntroConfig classIntroConfig = getClassIntroConfig(method.getDeclaringClass());

      if (classIntroConfig != null)
      {
         String methodName = method.getName();

         for (MethodIntroConfig methodIntro : classIntroConfig.getMethod())
         {
            if (methodIntro.getName().equals(methodName))
            {
               return methodIntro;
            }
            else if (isRegexMatch(methodName, methodIntro.getName()))
            {
               return methodIntro;
            }
         }
      }

      return null;
   }

   private static Map<String, Pattern> patternCache = new HashMap<String, Pattern>();

   private boolean isRegexMatch(String string, String regex)
   {
      Pattern pattern = patternCache.get(regex);

      if (pattern == null)
      {
         try
         {
            pattern = Pattern.compile(regex);
         }
         catch (Exception e)
         {
            logger.warn("Error compiling '" + regex + "' as a regular expression: " + e.getMessage());
            return false;
         }
         patternCache.put(regex, pattern);
      }

      return pattern.matcher(string).matches();
   }

   private <T extends Annotation> T getMemberAnnotationProxy(Class<T> annotationType, ClassMemberIntroConfig memberIntroConfig)
   {
	   return getAnnotationProxy(annotationType, memberIntroConfig, AVAILABLE_MEMBER_ANNOTATIONS);
   }

   private <T extends Annotation> T getClassAnnotationProxy(Class<T> annotationType, ClassIntroConfig classIntroConfig) 
   {
	   return getAnnotationProxy(annotationType, classIntroConfig, AVAILABLE_CLASS_ANNOTATIONS);
   }
   
   private <T extends Annotation> T getAnnotationProxy(Class<T> annotation, Object introConfig, Set<Class<? extends Annotation>> availableAnnotations)
   {
	   T proxy = null;

	   if (availableAnnotations.contains(annotation)) {
		   Object intro;
		   intro = Util.getProperty(introConfig, annotation);
		   if (intro != null) {
			   proxy = annotation.cast(Handler.<T>createProxy(annotation, intro));
		   }
	   }
	   return proxy;
   }

   private boolean isMemberAnnotationIntroAvailable(Class<? extends Annotation> annotation, ClassMemberIntroConfig memberIntroConfig)
   {
      return isAnnotationIntroAvailable(annotation, memberIntroConfig, AVAILABLE_MEMBER_ANNOTATIONS);
   }

   private boolean isClassAnnotationIntroAvailable(Class<? extends Annotation> annotation, ClassIntroConfig classIntroConfig)
   {
      return isAnnotationIntroAvailable(annotation, classIntroConfig, AVAILABLE_CLASS_ANNOTATIONS);
   }

   private boolean isAnnotationIntroAvailable(Class<? extends Annotation> annotationType, Object introConfig, Set<Class<? extends Annotation>> availableAnnotations) {
	   if (availableAnnotations.contains(annotationType)) {
		   return Util.getProperty(introConfig, annotationType) != null;
	   }
	   return false;
   }
   
   private Annotation[] getAllMemberAnnotations(Member member, Locatable srcPos)
   {
      Annotation[] r = ((AnnotatedElement)member).getAnnotations();
      List<Annotation> annotations = new ArrayList<Annotation>();

      for (int i = 0; i < r.length; i++)
      {
         Class<? extends Object> annType = r[i].getClass();
         if (AVAILABLE_MEMBER_ANNOTATIONS.contains(annType))
         {
            // We'll handle these explicitly (below)!!
            continue;
         }
         annotations.add(LocatableAnnotation.create(r[i], srcPos));
      }

      // Now we explicitly handle the supported Introduction annotations...
      ClassMemberIntroConfig memberIntroConfig = null;
      if (member instanceof Field)
      {
         memberIntroConfig = getFieldIntroConfig((Field)member);
      }
      else if (member instanceof Method)
      {
         memberIntroConfig = getMethodIntroConfig((Method)member);
      }
      if (memberIntroConfig != null)
      {
    	  for (Class<? extends Annotation> annotationType : AVAILABLE_MEMBER_ANNOTATIONS) {
    		  addMemberAnnotation(annotationType, memberIntroConfig, annotations, member, srcPos);
    	  }
      }

      r = annotations.toArray(new Annotation[annotations.size()]);

      return r;
   }

   private void addMemberAnnotation(Class<? extends Annotation> annotationType, ClassMemberIntroConfig memberIntroConfig, List<Annotation> annotations, Member member, Locatable srcPos)
   {
      Annotation annotation = getMemberAnnotationProxy(annotationType, memberIntroConfig);
      if (annotation != null)
      {
         annotations.add(annotation);
      }
      else
      {
         annotation = ((AnnotatedElement)member).getAnnotation(annotationType);
         if (annotation != null)
         {
            annotations.add(LocatableAnnotation.create(annotation, srcPos));
         }
      }
   }

   private Annotation getProxy(Class<? extends Annotation> annotation, Field field)
   {
      FieldIntroConfig fieldIntroConfig = getFieldIntroConfig(field);

      if (fieldIntroConfig != null)
      {
         Annotation proxy = getMemberAnnotationProxy(annotation, fieldIntroConfig);
         if (proxy != null)
         {
            return proxy;
         }
      }

      return null;
   }


private <T extends Annotation> T getProxy(Class<T> annotation, Method method)
   {
      MethodIntroConfig methodIntroConfig = getMethodIntroConfig(method);

      if (methodIntroConfig != null)
      {
         T proxy = getMemberAnnotationProxy(annotation, methodIntroConfig);
         if (proxy != null)
         {
            return proxy;
         }
      }

      return null;
   }

   private <T extends Annotation> T getProxy(Class<T> annotation, Class<?> clazz)
   {
      ClassIntroConfig classIntroConfig = getClassIntroConfig(clazz);

      if (classIntroConfig != null)
      {
         T proxy = getClassAnnotationProxy(annotation, classIntroConfig);
         if (proxy != null)
         {
            return proxy;
         }
      }

      return null;
   }
}
