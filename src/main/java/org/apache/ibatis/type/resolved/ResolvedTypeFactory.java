package org.apache.ibatis.type.resolved;

import java.lang.reflect.Type;

/**
 * @author FlyInWind
 */
public interface ResolvedTypeFactory {

  ResolvedType constructType(Type type);

  ResolvedType constructType(Class<?> type);

  ResolvedType constructFromCanonical(String canonical);

  ResolvedType getObjectType();

  ResolvedType getResultSetType();

  ResolvedType getParamMapType();

}
