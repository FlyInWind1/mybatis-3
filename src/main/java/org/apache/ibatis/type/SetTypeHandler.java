package org.apache.ibatis.type;

import org.apache.ibatis.type.resolved.ResolvedType;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author FlyInWind
 */
public class SetTypeHandler extends CollectionTypeHandler<Set<Object>> {

  public SetTypeHandler(ResolvedType type) {
    super(type);
  }

  @Override
  protected Set<Object> toCollection(Object[] array) {
    return new LinkedHashSet<>(Arrays.asList(array));
  }
}
