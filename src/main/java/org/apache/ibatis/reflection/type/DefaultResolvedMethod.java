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
package org.apache.ibatis.reflection.type;

import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author FlyInWind
 */
public class DefaultResolvedMethod implements ResolvedMethod {
  protected final ResolvedTypeFactory resolvedTypeFactory;
  protected final ResolvedType rawClass;
  protected final Method method;
  protected ResolvedType[] parameterTypes;

  protected DefaultResolvedMethod(ResolvedType rawClass, Method method) {
    this.resolvedTypeFactory = rawClass.getResolvedTypeFactory();
    this.rawClass = rawClass;
    this.method = method;
  }

  @Override
  public ResolvedTypeFactory getResolvedTypeFactory() {
    return resolvedTypeFactory;
  }

  @Override
  public ResolvedType getRawClass() {
    return rawClass;
  }

  @Override
  public Method getMethod() {
    return method;
  }

  @Override
  public int getParameterCount() {
    return method.getParameterCount();
  }

  public ResolvedType getMethodDeclaringType() {
    return rawClass.findSuperType(method.getDeclaringClass());
  }

  @Override
  public ResolvedType[] getParameterTypes() {
    if (parameterTypes != null) {
      return parameterTypes;
    }
    int count = method.getParameterTypes().length;
    if (count == 0) {
      parameterTypes = ResolvedTypeUtil.EMPTY_TYPE_ARRAY;
    } else {
      ResolvedType[] types = new ResolvedType[count];
      ResolvedType superType = getMethodDeclaringType();
      if (superType == null) {
        // jackson don't support get superType of String
        superType = rawClass;
      }
      Type[] genericParameterTypes = method.getGenericParameterTypes();
      for (int i = 0; i < count; i++) {
        types[i] = superType.resolveMemberType(genericParameterTypes[i]);
      }
      parameterTypes = types;
    }
    return parameterTypes;
  }

  @Override
  public ResolvedType getReturnType() {
    Type returnType = method.getGenericReturnType();
    ResolvedType declaringType = getMethodDeclaringType();
    return declaringType.resolveMemberType(returnType);
  }

  @Override
  public ResolvedType namedParamsType(Configuration configuration) {
    return ParamNameResolver.namedParamsType(configuration, this);
  }

  @Override
  public String getName() {
    return method.getName();
  }

  @Override
  public String toString() {
    return method.toString();
  }
}
