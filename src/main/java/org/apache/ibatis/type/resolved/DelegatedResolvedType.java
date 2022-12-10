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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class DelegatedResolvedType implements ResolvedType {

  protected final ResolvedType wrapped;

  public DelegatedResolvedType(ResolvedType wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public Class<?> getRawClass() {
    return wrapped.getRawClass();
  }

  @Override
  public boolean isPrimitive() {
    return wrapped.isPrimitive();
  }

  @Override
  public boolean isArrayType() {
    return wrapped.isArrayType();
  }

  @Override
  public boolean isEnumType() {
    return wrapped.isEnumType();
  }

  @Override
  public boolean isRecordType() {
    return wrapped.isRecordType();
  }

  @Override
  public boolean isInterface() {
    return wrapped.isInterface();
  }

  @Override
  public boolean isTypeOrSubTypeOf(Class<?> clazz) {
    return wrapped.isTypeOrSubTypeOf(clazz);
  }

  @Override
  public boolean isTypeOrSuperTypeOf(Class<?> clazz) {
    return wrapped.isTypeOrSuperTypeOf(clazz);
  }

  @Override
  public boolean isJavaLangObject() {
    return wrapped.isJavaLangObject();
  }

  @Override
  public boolean hasRawClass(Class<?> clazz) {
    return wrapped.hasRawClass(clazz);
  }

  @Override
  public boolean hasContentType() {
    return wrapped.hasContentType();
  }

  @Override
  public ResolvedType findSuperType(Class<?> clazz) {
    return wrapped.findSuperType(clazz);
  }

  @Override
  public ResolvedType[] getInterfaces() {
    return wrapped.getInterfaces();
  }

  @Override
  public ResolvedType getSuperclass() {
    return wrapped.getSuperclass();
  }

  @Override
  public ResolvedType getContentType() {
    return wrapped.getContentType();
  }

  @Override
  public ResolvedType[] findTypeParameters(Class<?> rawClass) {
    return wrapped.findTypeParameters(rawClass);
  }

  @Override
  public ResolvedType[] getTypeParameters() {
    return wrapped.getTypeParameters();
  }

  @Override
  public ResolvedMethod findMethod(String name) {
    return wrapped.findMethod(name);
  }

  @Override
  public ResolvedMethod resolveMethod(String name, Class<?>... parameterTypes) {
    return wrapped.resolveMethod(name, parameterTypes);
  }

  @Override
  public ResolvedMethod resolveMethod(Method method) {
    return wrapped.resolveMethod(method);
  }

  @Override
  public ResolvedType resolveMemberType(Type type) {
    return wrapped.resolveMemberType(type);
  }

  @Override
  public ResolvedType resolveFieldType(Field field) {
    return wrapped.resolveFieldType(field);
  }

  @Override
  public ResolvedTypeFactory getResolvedTypeFactory() {
    return wrapped.getResolvedTypeFactory();
  }

  @Override
  public Type getProxied() {
    return wrapped.getProxied();
  }

  @Override
  public boolean equals(Object o) {
    return wrapped.equals(o);
  }

  @Override
  public int hashCode() {
    return wrapped.hashCode();
  }

  @Override
  public String getName() {
    return wrapped.getName();
  }

  @Override
  public String toString() {
    return wrapped.toString();
  }

  @Override
  public String toCanonical() {
    return wrapped.toCanonical();
  }

  @Override
  public String getTypeName() {
    return wrapped.getTypeName();
  }

}
