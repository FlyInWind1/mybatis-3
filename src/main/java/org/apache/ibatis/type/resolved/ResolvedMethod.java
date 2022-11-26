package org.apache.ibatis.type.resolved;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.reflection.ParamNameResolver;

import java.lang.reflect.Method;

/**
 * @author FlyInWind
 */
public interface ResolvedMethod {

  /**
   * @return may be {@link Method#getDeclaringClass()} or the subclass of {@link Method#getDeclaringClass()}
   */
  ResolvedType getRawClass();

  /**
   * @return the wrapped {@link Method}
   */
  Method getMethod();

  /**
   * @return {@link Method#getParameterCount()}
   */
  int getParameterCount();

  /**
   * @return the resolved {@link Method#getGenericParameterTypes()}
   */
  ResolvedType[] getParameterTypes();

  /**
   * @return the resolved {@link Method#getGenericReturnType()}
   */
  ResolvedType getReturnType();

  /**
   * @return null if no parameter, return the first type if only one parameter, or {@link ParamMap} type of more than one parameter
   * @see ParamNameResolver#getNamedParams(Object[])
   */
  ResolvedType namedParamsType();

  /**
   * @return {@link Method#getName}
   */
  String getName();

  @Override
  String toString();

}
