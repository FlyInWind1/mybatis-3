/*
 *    Copyright 2009-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.type;

import org.apache.ibatis.reflection.type.ResolvedType;
import org.apache.ibatis.reflection.type.ResolvedTypeUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ListTypeHandlerTest extends BaseTypeHandlerTest {

  static final ResolvedType STRING_LIST_TYPE = ResolvedTypeUtil.getResolvedTypeFactory().constructType(new TypeReference<List<String>>() {
  });
  static final ListTypeHandler TYPE_HANDLER = new ListTypeHandler(JdbcType.VARCHAR.name());

  @Mock
  Array mockArray;

  @Override
  @Test
  public void shouldSetParameter() throws Exception {
    Connection connection = mock(Connection.class);
    when(ps.getConnection()).thenReturn(connection);

    Array array = mock(Array.class);
    when(connection.createArrayOf(anyString(), any(Object[].class))).thenReturn(array);

    TYPE_HANDLER.setParameter(ps, 1, Collections.singletonList("Hello World"), JdbcType.ARRAY);
    verify(ps).setArray(1, array);
    verify(array).free();
  }

  @Test
  public void shouldSetNullParameter() throws Exception {
    TYPE_HANDLER.setParameter(ps, 1, null, JdbcType.ARRAY);
    verify(ps).setNull(1, Types.ARRAY);
  }

  @Override
  public void shouldGetResultFromResultSetByName() throws Exception {
    when(rs.getArray("column")).thenReturn(mockArray);
    String[] stringArray = new String[]{"a", "b"};
    when(mockArray.getArray()).thenReturn(stringArray);
    List<String> list = new ArrayList<>(Arrays.asList(stringArray));
    assertEquals(list, TYPE_HANDLER.getResult(rs, "column"));
    verify(mockArray).free();
  }

  @Override
  public void shouldGetResultNullFromResultSetByName() throws Exception {
    when(rs.getArray("column")).thenReturn(null);
    assertNull(TYPE_HANDLER.getResult(rs, "column"));
  }

  @Override
  public void shouldGetResultFromResultSetByPosition() throws Exception {
    when(rs.getArray(1)).thenReturn(mockArray);
    String[] stringArray = new String[]{"a", "b"};
    when(mockArray.getArray()).thenReturn(stringArray);
    List<String> list = new ArrayList<>(Arrays.asList(stringArray));
    assertEquals(list, TYPE_HANDLER.getResult(rs, 1));
    verify(mockArray).free();
  }

  @Override
  public void shouldGetResultNullFromResultSetByPosition() throws Exception {
    when(rs.getArray(1)).thenReturn(null);
    assertNull(TYPE_HANDLER.getResult(rs, 1));
  }

  @Override
  public void shouldGetResultFromCallableStatement() throws Exception {
    when(cs.getArray(1)).thenReturn(mockArray);
    String[] stringArray = new String[]{"a", "b"};
    when(mockArray.getArray()).thenReturn(stringArray);
    List<String> list = new ArrayList<>(Arrays.asList(stringArray));
    assertEquals(list, TYPE_HANDLER.getResult(cs, 1));
    verify(mockArray).free();
  }

  @Override
  public void shouldGetResultNullFromCallableStatement() throws Exception {
    when(cs.getArray(1)).thenReturn(null);
    assertNull(TYPE_HANDLER.getResult(cs, 1));
  }
}
