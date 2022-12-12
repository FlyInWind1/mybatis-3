package org.apache.ibatis.type.resolved;

import org.apache.ibatis.reflection.Reflector;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class SpringResolvedType extends BaseResolvedType<ResolvableType, SpringResolvedTypeFactory> {
  protected SpringResolvedType(ResolvableType type, SpringResolvedTypeFactory resolvedTypeFactory) {
    super(type, resolvedTypeFactory);
  }

  @Override
  protected ResolvedType toResolvedType(ResolvableType type) {
    return resolvedTypeFactory.toResolvedType(type);
  }

  @Override
  public Class<?> getRawClass() {
    Class<?> rawClass = type.getRawClass();
    return rawClass != null ? rawClass : Object.class;
  }

  @Override
  public boolean isPrimitive() {
    return getRawClass().isPrimitive();
  }

  @Override
  public boolean isArrayType() {
    return type.isArray();
  }

  @Override
  public boolean isEnumType() {
    return Enum.class.isAssignableFrom(getRawClass());
  }

  @Override
  public boolean isRecordType() {
    return Reflector.isRecord(getRawClass());
  }

  @Override
  public boolean isInterface() {
    return getRawClass().isInterface();
  }

  @Override
  public boolean isTypeOrSubTypeOf(Class<?> clazz) {
    return clazz.isAssignableFrom(getRawClass());
  }

  @Override
  public boolean isTypeOrSuperTypeOf(Class<?> clazz) {
    return getRawClass().isAssignableFrom(clazz);
  }

  @Override
  public boolean isJavaLangObject() {
    return getRawClass() == Object.class;
  }

  @Override
  public boolean hasRawClass(Class<?> clazz) {
    return getRawClass() == Object.class;
  }

  @Override
  public boolean hasContentType() {
    return type.getComponentType() != ResolvableType.NONE;
  }

  @Override
  public ResolvedType findSuperType(Class<?> clazz) {
    return toResolvedType(type.as(clazz));
  }

  @Override
  public ResolvedType[] getInterfaces() {
    return toResolvedTypes(type.getInterfaces());
  }

  @Override
  public ResolvedType getSuperclass() {
    return toResolvedType(type.getSuperType());
  }

  @Override
  public ResolvedType getContentType() {
    return toResolvedType(type.getComponentType());
  }

  @Override
  public ResolvedMethod resolveMethod(Method method) {
    return new SpringResolvedMethod(this, method);
  }

  @Override
  public ResolvedType[] findTypeParameters(Class<?> rawClass) {
    return toResolvedTypes(type.as(rawClass).getGenerics());
  }

  @Override
  public ResolvedType resolveFieldType(Field field) {
    return toResolvedType(ResolvableType.forField(field, type));
  }

  @Override
  public ResolvedType resolveMemberType(Type type) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toCanonical() {
    return type.toString();
  }
}
