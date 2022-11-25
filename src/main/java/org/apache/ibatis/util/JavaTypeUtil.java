/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.util;

import com.fasterxml.jackson.core.type.ResolvedType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class JavaTypeUtil {
  private static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();

  private static final JavaType[] EMPTY_TYPE_ARRAY = new JavaType[]{};

  public static final JavaType CORE_TYPE_OBJECT = TYPE_FACTORY.constructType(Object.class);
  public static final JavaType CORE_TYPE_STRING = TYPE_FACTORY.constructType(String.class);
  public static final JavaType PARAM_MAP_STRING = TYPE_FACTORY.constructType(ParamMap.class);


  public static JavaType constructType(Type type) {
    if (type == null) {
      return null;
    }
    return TYPE_FACTORY.constructType(type);
  }

  public static JavaType constructFromCanonical(String canonical) {
    if (canonical == null) {
      return null;
    }
    return TYPE_FACTORY.constructFromCanonical(canonical);
  }

  public static Class<?> getRawClass(ResolvedType javaType) {
    if (javaType == null) {
      return null;
    }
    return javaType.getRawClass();
  }


  public static JavaType[] resolveParameterTypes(Class<?> clazz, Method method) {
    return resolveParameterTypes(TYPE_FACTORY.constructType(clazz), method);
  }

  public static JavaType[] resolveParameterTypes(JavaType clazz, Method method) {
    Type[] genericParameterTypes = method.getGenericParameterTypes();
    if (genericParameterTypes.length == 0) {
      return EMPTY_TYPE_ARRAY;
    }
    JavaType superType = clazz.findSuperType(method.getDeclaringClass());
    TypeBindings typeBindings = superType.getBindings();
    JavaType[] result = new JavaType[genericParameterTypes.length];
    for (int i = 0; i < genericParameterTypes.length; i++) {
      result[i] = TYPE_FACTORY.resolveMemberType(genericParameterTypes[i], typeBindings);
    }
    return result;
  }

  public static JavaType[] resolveParameterTypes(AnnotatedMethod method) {
    int count = method.getParameterCount();
    if (count == 0) {
      return EMPTY_TYPE_ARRAY;
    }
    JavaType[] result = new JavaType[count];
    for (int i = 0; i < count; i++) {
      result[i] = method.getParameterType(i);
    }
    return result;
  }

  public static JavaType resolveReturnType(JavaType type, Method method) {
    TypeBindings bindings = type.findSuperType(method.getDeclaringClass()).getBindings();
    return TYPE_FACTORY.resolveMemberType(method.getGenericReturnType(), bindings);
  }

  public static AnnotatedMethod resolvedMethod(JavaType clazz, String methodName) {
    if (clazz == null || methodName == null) {
      return null;
    }
    Method[] methods = clazz.getRawClass().getMethods();
    Method foundMethod = null;
    for (Method method : methods) {
      if (method.getName().equals(methodName) && !method.isDefault() && !method.isBridge()) {
        foundMethod = method;
        break;
      }
    }
    if (foundMethod != null) {
      return resolvedMethod(clazz, foundMethod);
    }
    return null;
  }

  public static AnnotatedMethod resolvedMethod(JavaType clazz, Method method) {
    TypeBindings typeBindings = clazz.findSuperType(method.getDeclaringClass()).getBindings();
    TypeResolutionContext resolutionContext = new TypeResolutionContext.Basic(TYPE_FACTORY, typeBindings);
    return new AnnotatedMethod(resolutionContext, method, null, null);
  }

  public static JavaType compressedParameterType(Class<?> clazz, Method method) {
    return compressedParameterType(constructType(clazz), method);
  }

  public static JavaType compressedParameterType(JavaType clazz, Method method) {
    JavaType[] parameterTypes = resolveParameterTypes(clazz, method);
    return compressedParameterType(parameterTypes);
  }

  public static JavaType compressedParameterType(AnnotatedMethod method) {
    JavaType[] parameterTypes = resolveParameterTypes(method);
    return compressedParameterType(parameterTypes);
  }

  public static JavaType compressedParameterType(JavaType[] parameterTypes) {
    JavaType parameterType = null;
    for (JavaType resolvedType : parameterTypes) {
      if (!resolvedType.isTypeOrSubTypeOf(RowBounds.class) && !resolvedType.isTypeOrSubTypeOf(ResultHandler.class)) {
        if (parameterType == null) {
          parameterType = resolvedType;
        } else {
          // issue #135
          parameterType = PARAM_MAP_STRING;
          break;
        }
      }
    }
    return parameterType;
  }

  public static boolean isNotInstance(JavaType type, Object obj) {
    if (type == null) {
      return true;
    }
    return !type.getRawClass().isInstance(obj);
  }

}
