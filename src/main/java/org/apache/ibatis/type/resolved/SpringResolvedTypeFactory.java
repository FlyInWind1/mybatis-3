package org.apache.ibatis.type.resolved;

import org.apache.ibatis.type.TypeReference;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Type;

public class SpringResolvedTypeFactory implements ResolvedTypeFactory {
  @Override
  public ResolvedType constructType(Type type) {
    return null;
  }

  @Override
  public ResolvedType constructType(Class<?> type) {
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

  public ResolvedType toResolvedType(ResolvableType type) {
    if (type == ResolvableType.NONE) {
      return getObjectType();
    }
    return new SpringResolvedType(type, this);
  }
}
