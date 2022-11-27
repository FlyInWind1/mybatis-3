package org.apache.ibatis.type;

import org.apache.ibatis.type.resolved.ResolvedType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author FlyInWind
 */
public class ListTypeHandler extends CollectionTypeHandler<List<Object>> {

  public ListTypeHandler(ResolvedType type) {
    super(type);
  }

  @Override
  protected List<Object> toCollection(Object[] array) {
    return new ArrayList<>(Arrays.asList(array));
  }
}
