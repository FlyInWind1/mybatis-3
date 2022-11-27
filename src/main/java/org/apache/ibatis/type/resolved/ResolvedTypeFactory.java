package org.apache.ibatis.type.resolved;

import org.apache.ibatis.type.TypeReference;

import java.lang.reflect.Type;

/**
 * @author FlyInWind
 */
public interface ResolvedTypeFactory {

  ResolvedType constructType(Type type);

  ResolvedType constructType(Class<?> type);

  <T> ResolvedType constructType(TypeReference<T> typeReference);

  ResolvedType constructFromCanonical(String canonical);

  ResolvedType getObjectType();

  ResolvedType getResultSetType();

  ResolvedType getParamMapType();

}
