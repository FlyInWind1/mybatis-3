package org.apache.ibatis.type.resolved;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author FlyInWind
 */
public abstract class BaseResolvedType<T extends Type, F extends ResolvedTypeFactory> implements ResolvedType {
  protected final F resolvedTypeFactory;
  protected final T type;

  protected BaseResolvedType(T type, F resolvedTypeFactory) {
    Objects.requireNonNull(type);
    this.resolvedTypeFactory = resolvedTypeFactory;
    this.type = type;
  }

  protected ResolvedType toResolvedType(T type) {
    return resolvedTypeFactory.constructType(type);
  }

  @Override
  public ResolvedMethod findMethod(String name) {
    Method[] methods = getRawClass().getMethods();
    Method foundMethod = null;
    for (Method method : methods) {
      if (method.getName().equals(name) && !method.isDefault() && !method.isBridge()) {
        foundMethod = method;
        break;
      }
    }
    if (foundMethod != null) {
      return this.resolveMethod(foundMethod);
    }
    return null;
  }

  @Override
  public ResolvedMethod resolveMethod(String name, Class<?>... parameterTypes) {
    try {
      Method method = getRawClass().getMethod(name, parameterTypes);
      return resolveMethod(method);
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  @Override
  public ResolvedMethod resolveMethod(Method method) {
    return new DefaultResolvedMethod(this, method);
  }

  @Override
  public ResolvedTypeFactory getResolvedTypeFactory() {
    return resolvedTypeFactory;
  }

  @Override
  public ResolvedType[] getTypeParameters() {
    return findTypeParameters(getRawClass());
  }

  @Override
  public ResolvedType resolveFieldType(Field field) {
    return findSuperType(field.getDeclaringClass()).resolveMemberType(field.getGenericType());
  }

  @Override
  public T getProxied() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    return type.equals(o);
  }

  @Override
  public int hashCode() {
    return type.hashCode();
  }

  @Override
  public String getName() {
    return getRawClass().getName();
  }

  @Override
  public String getTypeName() {
    return type.getTypeName();
  }

  @Override
  public String toString() {
    return type.toString();
  }

}
