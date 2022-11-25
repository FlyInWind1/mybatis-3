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
package org.apache.ibatis.type;

import com.fasterxml.jackson.databind.JavaType;
import org.apache.ibatis.util.JavaTypeUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * References a generic type.
 *
 * @param <T> the referenced type
 * @since 3.1.0
 * @author Simone Tripodi
 */
public abstract class TypeReference<T> {

  private final JavaType resolvedType;

  protected TypeReference() {
    resolvedType = getSuperclassTypeParameter(getClass());
  }

  JavaType getSuperclassTypeParameter(Class<?> clazz) {
    JavaType javaType = JavaTypeUtil.constructType(clazz);
    JavaType[] typeParameters = javaType.findTypeParameters(TypeReference.class);
    if (typeParameters == null || typeParameters.length == 0) {
      throw new TypeException("'" + getClass() + "' extends TypeReference but misses the type parameter. "
        + "Remove the extension or add a type parameter to it.");
    }
    return typeParameters[0];
  }

  public final Type getRawType() {
    return JavaTypeUtil.getRawClass(resolvedType);
  }

  public final JavaType getResolvedType() {
    return resolvedType;
  }

  @Override
  public String toString() {
    return resolvedType.toCanonical();
  }

}
