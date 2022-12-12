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

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.type.TypeReference;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class SimpleResolvedTypeFactory implements ResolvedTypeFactory {
//  protected final Cache cache;

//  public SimpleResolvedTypeFactory(Cache cache) {
//    this.cache = cache;
//  }

  @Override
  public ResolvedType constructType(Type type) {
    return null;
  }

  @Override
  public ResolvedType constructType(Class<?> type) {
    TypeVariable<? extends Class<?>>[] typeParameters = type.getTypeParameters();
    TypeBindings typeBindings;
    if (typeParameters.length == 0) {
      typeBindings = TypeBindings.emptyBindings();
    } else {
      ResolvedType[] resolvedTypeParams = new ResolvedType[typeParameters.length];
      for (int i = 0; i < typeParameters.length; i++) {
        TypeVariable<? extends Class<?>> typeParameter = typeParameters[i];
        String name = typeParameter.getName();
        Type[] bounds = typeParameter.getBounds();

      }
    }
    return null;
  }

  @Override
  public <T> ResolvedType constructType(TypeReference<T> typeReference) {
    return null;
  }

  @Override
  public ResolvedType constructParametricType(Class<?> rawClass, Class<?>... typeParameters) {
    return null;
  }

  @Override
  public ResolvedType constructFromCanonical(String canonical) {
    return null;
  }

  @Override
  public ResolvedType getObjectType() {
    return null;
  }

  @Override
  public ResolvedType getIntegerType() {
    return null;
  }

  @Override
  public ResolvedType getLongType() {
    return null;
  }

  @Override
  public ResolvedType getResultSetType() {
    return null;
  }

  @Override
  public ResolvedType getMapType() {
    return null;
  }

  @Override
  public ResolvedType getParamMapType() {
    return null;
  }

  public ResolvedType resolvedMemberType(Type type, TypeBindings typeBindings) {
    return null;
  }

  protected ResolvedType fromClass(Class<?> type, TypeBindings typeBindings) {
    return null;
  }

//  @Override
//  public MapValueResolvedType paramMapType() {
//    return new DefaultMapValueResolvedType(this);
//  }
}
