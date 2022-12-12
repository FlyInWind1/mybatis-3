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

import org.apache.ibatis.reflection.Reflector;

import java.lang.reflect.Type;

public abstract class BaseSimpleResolvedType extends BaseResolvedType<Class<?>, ResolvedTypeFactory> {
  protected final TypeBindings typeBindings;
  protected ResolvedType[] interfaces;

  protected BaseSimpleResolvedType(Class<?> type, TypeBindings typeBindings, SimpleResolvedTypeFactory resolvedTypeFactory) {
    super(type, resolvedTypeFactory);
    this.typeBindings = typeBindings;
  }

  @Override
  public Class<?> getRawClass() {
    return type;
  }

  @Override
  public boolean isPrimitive() {
    return type.isPrimitive();
  }

  @Override
  public boolean isArrayType() {
    return type.isArray();
  }

  @Override
  public boolean isEnumType() {
    return Enum.class.isAssignableFrom(type);
  }

  @Override
  public boolean isRecordType() {
    return Reflector.isRecord(type);
  }

  @Override
  public boolean isInterface() {
    return type.isInterface();
  }

  @Override
  public boolean isTypeOrSubTypeOf(Class<?> clazz) {
    return clazz.isAssignableFrom(type);
  }

  @Override
  public boolean isTypeOrSuperTypeOf(Class<?> clazz) {
    return type.isAssignableFrom(clazz);
  }

  @Override
  public boolean isJavaLangObject() {
    return type == Object.class;
  }

  @Override
  public boolean hasRawClass(Class<?> clazz) {
    return type.equals(clazz);
  }

  @Override
  public ResolvedType findSuperType(Class<?> clazz) {
    if (clazz == type) {
      return this;
    }
//    type.getGenericInterfaces()

    return null;
  }

  @Override
  public ResolvedType[] getInterfaces() {
    if (interfaces == null) {
//      interfaces = resolvedTypeFactory.getInterfaces(this);
    }
    return null;
  }

  @Override
  public ResolvedType getSuperclass() {
    return null;
  }

  @Override
  public ResolvedType getContentType() {
    return null;
  }

  @Override
  public ResolvedType[] findTypeParameters(Class<?> rawClass) {
    return null;
  }

  @Override
  public ResolvedType resolveMemberType(Type type) {
    return null;
  }

  @Override
  public String toCanonical() {
    return null;
  }
}
