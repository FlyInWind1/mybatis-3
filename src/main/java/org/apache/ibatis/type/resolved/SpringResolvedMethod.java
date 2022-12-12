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
package org.apache.ibatis.type.resolved;

import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;

/**
 * @author FlyInWind
 */
public class SpringResolvedMethod extends DefaultResolvedMethod {

  protected SpringResolvedMethod(SpringResolvedType implementationType, Method method) {
    super(implementationType, method);
  }

  public ResolvedType toResolvableType(ResolvableType type) {
    return ((SpringResolvedTypeFactory) resolvedTypeFactory).toResolvedType(type);
  }

  @Override
  public ResolvedType[] getParameterTypes() {
    if (parameterTypes != null) {
      return parameterTypes;
    }
    int count = method.getParameterCount();
    ResolvedType[] result = new ResolvedType[count];
    for (int i = 0; i < count; i++) {
      ResolvableType type = ResolvableType.forMethodParameter(method, i, implementationType.getRawClass());
      result[i] = toResolvableType(type);
    }
    this.parameterTypes = result;
    return result;
  }

  @Override
  public ResolvedType getReturnType() {
    ResolvableType type = ResolvableType.forMethodReturnType(method, implementationType.getRawClass());
    return toResolvableType(type);
  }

}
