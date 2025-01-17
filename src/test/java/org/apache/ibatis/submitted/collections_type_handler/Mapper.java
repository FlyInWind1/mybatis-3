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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Mapper {

  void insert(User user);

  void insertWithoutAssignTypeHandler(User user);

  int getUserCount();

  /**
   * HSQL returns NULL when asked for the cardinality of an array column with NULL value :-(
   */
  Map<String, Long> getNicknameCount();

  List<User> selectAll();

  List<User> selectByNicknameList(Collection<String> nicknameList);

  List<User> selectByNicknameSet(Set<String> nicknameList);

  void updateNickname(Integer id, List<String> nicknameList, Set<String> nicknameSet);
}
