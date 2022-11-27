package org.apache.ibatis.type.resolved;

import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * @author FlyInWind
 */
public class ResolvedTypeUtil {

    public static final ResolvedType[] EMPTY_TYPE_ARRAY = new ResolvedType[0];

    public static Class<?> getRawClass(ResolvedType type) {
    if (type == null) {
      return null;
    }
    return type.getRawClass();
  }

  public static boolean isNotInstance(ResolvedType type, Object obj) {
    if (type == null) {
      return true;
    }
    return !type.getRawClass().isInstance(obj);
  }


  public static ResolvedMethod findMethod(ResolvedType type, String methodName) {
    if (type == null || methodName == null) {
      return null;
    }
    return type.findMethod(methodName);
  }

  protected static final JacksonResolvedTypeFactory JACKSON_RESOLVED_TYPE_FACTORY = new JacksonResolvedTypeFactory(TypeFactory.defaultInstance());

  @Deprecated
  public static ResolvedTypeFactory getResolvedTypeFactory() {
    // TODO: 11/26/22 spring ResolvableType
    // TODO: 11/26/22 build in SimpleResolvedType;
    return JACKSON_RESOLVED_TYPE_FACTORY;
  }
}
