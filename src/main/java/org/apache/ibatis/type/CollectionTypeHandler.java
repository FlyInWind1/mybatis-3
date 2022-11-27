package org.apache.ibatis.type;

import org.apache.ibatis.type.resolved.ResolvedType;

import java.sql.*;
import java.util.Collection;

import static org.apache.ibatis.type.ArrayTypeHandler.DEFAULT_TYPE_NAME;
import static org.apache.ibatis.type.ArrayTypeHandler.STANDARD_MAPPING;

/**
 * @author FlyInWind
 */
public abstract class CollectionTypeHandler<T extends Collection<Object>> extends BaseTypeHandler<T> {

  protected final ResolvedType type;
  protected String arrayTypeName;

  protected CollectionTypeHandler(ResolvedType type) {
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
    Object[] javaArray = parameter.toArray();
    Array array = ps.getConnection().createArrayOf(getArrayTypeName(), javaArray);
    ps.setArray(i, array);
    array.free();
  }

  protected Class<?> getComponentType() {
    ResolvedType compType = type.findTypeParameters(Collection.class)[0];
    return compType.getRawClass();
  }

  protected String getArrayTypeName() {
    if (this.arrayTypeName == null) {
      this.arrayTypeName = resolveTypeName(getComponentType());
    }
    return this.arrayTypeName;
  }

  protected String resolveTypeName(Class<?> type) {
    return STANDARD_MAPPING.getOrDefault(type, DEFAULT_TYPE_NAME);
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return extractArray(rs.getArray(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return extractArray(rs.getArray(columnIndex));
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return extractArray(cs.getArray(columnIndex));
  }

  protected T extractArray(Array array) throws SQLException {
    if (array == null) {
      return null;
    }
    Object[] javaArray = (Object[]) array.getArray();
    array.free();
    return toCollection(javaArray);
  }

  protected abstract T toCollection(Object[] array);
}
