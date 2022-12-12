package org.apache.ibatis.type.resolved;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.type.TypeReference;
import org.springframework.core.ResolvableType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.Map;

public class SpringResolvedTypeFactory implements ResolvedTypeFactory {

  protected final ResolvedType objectType;
  protected final ResolvedType integerType;
  protected final ResolvedType longType;
  protected final ResolvedType resultSetType;
  protected final ResolvedType mapType;
  protected final ResolvedType paramMapType;

  public SpringResolvedTypeFactory() {
    this.objectType = constructType(Object.class);
    this.integerType = constructType(Integer.class);
    this.longType = constructType(Long.class);
    this.resultSetType = constructType(ResultSet.class);
    this.mapType = constructType(Map.class);
    this.paramMapType = constructType(MapperMethod.ParamMap.class);
  }

  protected static final MethodHandle METHOD_HANDLE;

  static {
    try {
      Method resolveType = ResolvableType.class.getDeclaredMethod("resolveType");
      resolveType.setAccessible(true);
      METHOD_HANDLE = MethodHandles.lookup().unreflect(resolveType);
    } catch (IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  public ResolvedType toResolvedType(ResolvableType type) {
    if (type == ResolvableType.NONE) {
      return getObjectType();
    }
//    if (type.hasUnresolvableGenerics()) {
//      try {
//        type = (ResolvableType) METHOD_HANDLE.invoke(type);
//      } catch (Throwable e) {
//        throw new RuntimeException(e);
//      }
//    }
    return new SpringResolvedType(type, this);
  }

  @Override
  public ResolvedType constructType(Type type) {
    return toResolvedType(ResolvableType.forType(type));
  }

  @Override
  public ResolvedType constructType(Class<?> type) {
    return toResolvedType(ResolvableType.forClass(type));
  }

  @Override
  public <T> ResolvedType constructType(TypeReference<T> typeReference) {
    ResolvableType type = ResolvableType.forClass(TypeReference.class, typeReference.getClass())
      .getGeneric(0);
    return toResolvedType(type);
  }

  @Override
  public ResolvedType constructParametricType(Class<?> rawClass, Class<?>... typeParameters) {
    return toResolvedType(ResolvableType.forClassWithGenerics(rawClass, typeParameters));
  }

  @Override
  public ResolvedType constructFromCanonical(String canonical) {
    try {
      // TODO: 12/12/22
      return constructType(Class.forName(canonical));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResolvedType getObjectType() {
    return objectType;
  }

  @Override
  public ResolvedType getIntegerType() {
    return integerType;
  }

  @Override
  public ResolvedType getLongType() {
    return longType;
  }

  @Override
  public ResolvedType getMapType() {
    return mapType;
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
