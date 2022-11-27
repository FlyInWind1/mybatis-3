package org.apache.ibatis.type.resolved;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Wrap class for {@link Class} with addon GenericType info
 *
 * @author FlyInWind
 */
public interface ResolvedType extends Type {

  Class<?> getRawClass();

  /**
   * @return {@link Class#isPrimitive}
   */
  boolean isPrimitive();

  /**
   * @return {@link Class#isArray}
   */
  boolean isArrayType();

  /**
   * @return {@link Enum}.class.isAssignableFrom({@link #getRawClass})
   */
  boolean isEnumType();

  /**
   * Java14-added new {@code Record} types
   */
  boolean isRecordType();

  /**
   * @return {@link Class#isInterface}
   */
  boolean isInterface();

  boolean isTypeOrSubTypeOf(Class<?> clazz);

  boolean isTypeOrSuperTypeOf(Class<?> clazz);

  /**
   * clazz == {@link #getRawClass()
   */
  boolean hasRawClass(Class<?> clazz);

  ResolvedType findSuperType(Class<?> clazz);

  /**
   * for {@link Collection}{@code <T>} , {@code T[]} , {@link Map}{@code <K,T>} return T
   *
   * @return collection content type
   */
  ResolvedType getContentType();

  ResolvedType[] findTypeParameters(Class<?> rawClass);

  ResolvedType[] getTypeParameters();

  ResolvedMethod findMethod(String name);

  ResolvedMethod resolveMethod(String name, Class<?>... parameterTypes);

  ResolvedMethod resolveMethod(Method method);

  ResolvedType resolveMemberType(Type type);

  ResolvedType resolveFieldType(Field field);

  ResolvedTypeFactory getResolvedTypeFactory();

  /**
   * @return {@link com.fasterxml.jackson.databind.JavaType}
   */
  Type getProxied();

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();

  /**
   * @return {@link Class#getName}
   */
  String getName();

  @Override
  String toString();

  String toCanonical();

}
