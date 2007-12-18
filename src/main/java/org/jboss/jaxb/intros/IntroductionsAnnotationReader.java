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

import com.sun.xml.bind.v2.model.annotation.*;

import java.lang.reflect.*;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.jboss.jaxb.intros.configmodel.*;
import org.jboss.jaxb.intros.handlers.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * JAXB Annotation Reader for the JAXB RI context interface.
 * <p/>
 * Used for introduction of annotations on bean classes which are not annotated
 * for JAXB.  Allows us to use JAXB (and hence JBossWS 2.x+) with unannotated
 * interfaces.
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class IntroductionsAnnotationReader extends AbstractInlineAnnotationReaderImpl<Type, Class, Field, Method> implements RuntimeAnnotationReader
{

   private static final Log logger = LogFactory.getLog(IntroductionsAnnotationReader.class);
   private RuntimeAnnotationReader baseReader = new RuntimeInlineAnnotationReader();
   private JaxbIntros introductions;

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
         return (A)proxy;
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
      return getAllAnnotations(field, srcPos);
   }

   public <A extends Annotation> A getMethodAnnotation(Class<A> annotation, Method method, Locatable srcPos)
   {
      Annotation proxy = getProxy(annotation, method);

      if (proxy != null)
      {
         return (A)proxy;
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
      return getAllAnnotations(method, srcPos);
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
         return (A)proxy;
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

   public Class getClassValue(Annotation a, String name)
   {
      if (a instanceof ClassValue)
      {
         return ((ClassValue)a).getClassValue(a, name);
      }
      return (Class)baseReader.getClassValue(a, name);
   }

   public Class[] getClassArrayValue(Annotation a, String name)
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

   private ClassIntroConfig getClassIntroConfig(Class clazz)
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

   private Annotation getMemberAnnotationProxy(Class annotation, ClassMemberIntroConfig memberIntroConfig)
   {
      Annotation proxy = null;

      if (annotation == javax.xml.bind.annotation.XmlAttribute.class)
      {
         XmlAttributeIntro xmlAttributeIntro = memberIntroConfig.getXmlAttribute();
         if (xmlAttributeIntro != null)
         {
            proxy = XmlAttributeHandler.createProxy(xmlAttributeIntro);
         }
      }
      else if (annotation == javax.xml.bind.annotation.XmlElement.class)
      {
         XmlElementIntro xmlElementIntro = memberIntroConfig.getXmlElement();
         if (xmlElementIntro != null)
         {
            proxy = XmlElementHandler.createProxy(xmlElementIntro);
         }
      }

      return proxy;
   }

   private Annotation getClassAnnotationProxy(Class annotation, ClassIntroConfig classIntroConfig)
   {
      Annotation proxy = null;

      if (annotation == javax.xml.bind.annotation.XmlAccessorType.class)
      {
         XmlAccessorTypeIntro xmlAccessorTypeIntro = classIntroConfig.getXmlAccessorType();
         if (xmlAccessorTypeIntro != null)
         {
            proxy = XmlAccessorTypeHandler.createProxy(xmlAccessorTypeIntro);
         }
      }
      else if (annotation == javax.xml.bind.annotation.XmlType.class)
      {
         XmlTypeIntro xmlTypeIntro = classIntroConfig.getXmlType();
         if (xmlTypeIntro != null)
         {
            proxy = XmlTypeHandler.createProxy(xmlTypeIntro);
         }
      }
      else if (annotation == javax.xml.bind.annotation.XmlRootElement.class)
      {
         XmlRootElementIntro xmlRootElementIntro = classIntroConfig.getXmlRootElement();
         if (xmlRootElementIntro != null)
         {
            proxy = XmlRootElementHandler.createProxy(xmlRootElementIntro);
         }
      }

      return proxy;
   }

   private boolean isMemberAnnotationIntroAvailable(Class<? extends Annotation> annotation, ClassMemberIntroConfig memberIntroConfig)
   {
      if (annotation == javax.xml.bind.annotation.XmlAttribute.class)
      {
         return (memberIntroConfig.getXmlAttribute() != null);
      }
      else if (annotation == javax.xml.bind.annotation.XmlElement.class)
      {
         return (memberIntroConfig.getXmlElement() != null);
      }

      return false;
   }

   private boolean isClassAnnotationIntroAvailable(Class<? extends Annotation> annotation, ClassIntroConfig classIntroConfig)
   {
      if (annotation == javax.xml.bind.annotation.XmlType.class)
      {
         return (classIntroConfig.getXmlType() != null);
      }
      else if (annotation == javax.xml.bind.annotation.XmlAccessorType.class)
      {
         return (classIntroConfig.getXmlAccessorType() != null);
      }
      else if (annotation == javax.xml.bind.annotation.XmlRootElement.class)
      {
         return (classIntroConfig.getXmlRootElement() != null);
      }

      return false;
   }

   private Annotation[] getAllAnnotations(Member member, Locatable srcPos)
   {
      Annotation[] r = ((AnnotatedElement)member).getAnnotations();
      List<Annotation> annotations = new ArrayList<Annotation>();

      for (int i = 0; i < r.length; i++)
      {
         Class<? extends Object> annType = r[i].getClass();
         if (annType == XmlAttribute.class || annType == XmlElement.class)
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
         addMemberAnnotation(XmlAttribute.class, memberIntroConfig, annotations, member, srcPos);
         addMemberAnnotation(XmlElement.class, memberIntroConfig, annotations, member, srcPos);
      }

      r = annotations.toArray(new Annotation[annotations.size()]);

      return r;
   }

   private void addMemberAnnotation(Class annotationType, ClassMemberIntroConfig memberIntroConfig, List<Annotation> annotations, Member member, Locatable srcPos)
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

   private Annotation getProxy(Class annotation, Field field)
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

   private Annotation getProxy(Class annotation, Method method)
   {
      MethodIntroConfig methodIntroConfig = getMethodIntroConfig(method);

      if (methodIntroConfig != null)
      {
         Annotation proxy = getMemberAnnotationProxy(annotation, methodIntroConfig);
         if (proxy != null)
         {
            return proxy;
         }
      }

      return null;
   }

   private Annotation getProxy(Class annotation, Class clazz)
   {
      ClassIntroConfig classIntroConfig = getClassIntroConfig(clazz);

      if (classIntroConfig != null)
      {
         Annotation proxy = getClassAnnotationProxy(annotation, classIntroConfig);
         if (proxy != null)
         {
            return proxy;
         }
      }

      return null;
   }


}
