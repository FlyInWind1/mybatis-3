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
package org.apache.ibatis.submitted.collections_type_handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CollectionsTypeHandlerTest {

  private SqlSessionFactory sqlSessionFactory;

  @BeforeEach
  void setUp() throws Exception {
    try (Reader reader = Resources
        .getResourceAsReader("org/apache/ibatis/submitted/collections_type_handler/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
      "org/apache/ibatis/submitted/collections_type_handler/CreateDB.sql");
  }

  @Test
  void shouldInsertArrayValue() throws Exception {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      Mapper mapper = sqlSession.getMapper(Mapper.class);

      User user1 = new User();
      user1.setId(1);
      user1.setName("User 1");
      user1.setNicknames(new String[] { "User", "one" });
      user1.setNicknameList(Arrays.asList(user1.getNicknames()));
      user1.setNicknameSet(new HashSet<>(Arrays.asList("User", "User")));
      mapper.insert(user1);

      User user2 = new User();
      user2.setId(2);
      user2.setName("User 2");
      user2.setNicknames(new String[] { "User2", "two" });
      user2.setNicknameList(Arrays.asList(user2.getNicknames()));
      user2.setNicknameSet(new HashSet<>(Arrays.asList("User2", "User3")));
      mapper.insertWithoutAssignTypeHandler(user2);

      sqlSession.commit();

      int usersInDatabase = mapper.getUserCount();
      assertEquals(2, usersInDatabase);

      Map<String, Long> nicknameCount = mapper.getNicknameCount();
      assertEquals(4, nicknameCount.get("nicknames_count"));
      assertEquals(4, nicknameCount.get("nickname_list_count"));
      assertEquals(3, nicknameCount.get("nickname_set_count"));

      List<User> userList = mapper.selectAll();
      assertEquals(user1.getNicknameList(), userList.get(0).getNicknameList());
      assertEquals(user1.getNicknameSet(), userList.get(0).getNicknameSet());
      assertEquals(user2.getNicknameList(), userList.get(1).getNicknameList());
      assertEquals(user2.getNicknameSet(), userList.get(1).getNicknameSet());
    }
  }

  @Test
  void shouldInsertNullValue() throws Exception {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      User user = new User();
      user.setId(1);
      user.setName("User 1");
      // note how the user does not have nicknames

      Mapper mapper = sqlSession.getMapper(Mapper.class);
      mapper.insert(user);
      user.setId(2);
      mapper.insertWithoutAssignTypeHandler(user);
      sqlSession.commit();

      int usersInDatabase = mapper.getUserCount();
      assertEquals(2, usersInDatabase);

      Map<String, Long> nicknameCount = mapper.getNicknameCount();
      assertNull(nicknameCount);
    }
  }
}
