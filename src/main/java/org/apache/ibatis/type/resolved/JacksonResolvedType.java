package org.apache.ibatis.type.resolved;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.Type;

import static org.apache.ibatis.type.resolved.ResolvedTypeUtil.EMPTY_TYPE_ARRAY;

/**
 * @author FlyInWind
 */
public class JacksonResolvedType extends BaseResolvedType<JavaType, JacksonResolvedTypeFactory> {

  public JacksonResolvedType(JavaType type, JacksonResolvedTypeFactory resolvedTypeFactory) {
    super(type, resolvedTypeFactory);
  }

  @Override
  public Class<?> getRawClass() {
    return type.getRawClass();
  }

  @Override
  public boolean isPrimitive() {
    return type.isPrimitive();
  }

  @Override
  public boolean isArrayType() {
    return type.isArrayType();
  }

  @Override
  public boolean isEnumType() {
    return type.isEnumType();
  }

  @Override
  public boolean isRecordType() {
    return type.isRecordType();
  }

  @Override
  public boolean isInterface() {
    return type.isInterface();
  }

  @Override
  public boolean isTypeOrSubTypeOf(Class<?> clazz) {
    return type.isTypeOrSubTypeOf(clazz);
  }

  @Override
  public boolean isTypeOrSuperTypeOf(Class<?> clazz) {
    return type.isTypeOrSuperTypeOf(clazz);
  }

  @Override
  public boolean hasRawClass(Class<?> clazz) {
    return type.hasRawClass(clazz);
  }

  @Override
  public ResolvedType findSuperType(Class<?> clazz) {
    return toResolvedType(type.findSuperType(clazz));
  }

  @Override
  public ResolvedType getContentType() {
    return toResolvedType(type.getContentType());
  }

  @Override
  public ResolvedType[] findTypeParameters(Class<?> clazz) {
    JavaType[] types = type.findTypeParameters(clazz);
    if (types.length == 0) {
      return EMPTY_TYPE_ARRAY;
    }
    ResolvedType[] result = new ResolvedType[types.length];
    for (int i = 0; i < types.length; i++) {
      result[i] = toResolvedType(types[i]);
    }
    return result;
  }

  @Override
  public ResolvedType resolveMemberType(Type type) {
    JavaType javaType = resolvedTypeFactory.typeFactory.resolveMemberType(type, this.type.getBindings());
    return toResolvedType(javaType);
  }

  @Override
  public String toCanonical() {
    return type.toCanonical();
  }
}
