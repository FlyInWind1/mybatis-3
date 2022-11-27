package org.apache.ibatis.type.resolved;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.type.TypeReference;

import java.lang.reflect.Type;
import java.sql.ResultSet;

/**
 * @author FlyInWind
 */
public class JacksonResolvedTypeFactory implements ResolvedTypeFactory {
  protected final TypeFactory typeFactory;

  protected final ResolvedType objectType;
  protected final ResolvedType resultSetType;
  protected final ResolvedType paramMapType;

  public JacksonResolvedTypeFactory(TypeFactory typeFactory) {
    this.typeFactory = typeFactory;
    this.objectType = constructType(Object.class);
    this.resultSetType = constructType(ResultSet.class);
    this.paramMapType = constructType(ParamMap.class);
  }

  public ResolvedType toResolvedType(JavaType javaType) {
    return new JacksonResolvedType(javaType, this);
  }

  @Override
  public ResolvedType constructType(Type type) {
    if (type == null) {
      return null;
    }
    return toResolvedType(typeFactory.constructType(type));
  }

  @Override
  public ResolvedType constructType(Class<?> clazz) {
    if (clazz == null) {
      return null;
    }
    return toResolvedType(typeFactory.constructType(clazz));
  }

  @Override
  public <T> ResolvedType constructType(TypeReference<T> typeReference) {
    return constructType(typeReference.getClass()).findTypeParameters(TypeReference.class)[0];
  }

  @Override
  public ResolvedType constructFromCanonical(String canonical) {
    if (canonical == null || canonical.isEmpty()) {
      return null;
    }
    return toResolvedType(typeFactory.constructFromCanonical(canonical));
  }

  @Override
  public ResolvedType getObjectType() {
    return objectType;
  }

  @Override
  public ResolvedType getResultSetType() {
    return resultSetType;
  }

  @Override
  public ResolvedType getParamMapType() {
    return paramMapType;
  }
}
