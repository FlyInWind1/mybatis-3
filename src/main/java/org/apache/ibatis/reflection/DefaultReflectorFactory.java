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
package org.apache.ibatis.reflection;

import org.apache.ibatis.type.resolved.ResolvedType;
import org.apache.ibatis.type.resolved.ResolvedTypeFactory;
import org.apache.ibatis.type.resolved.ResolvedTypeUtil;
import org.apache.ibatis.util.MapUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultReflectorFactory implements ReflectorFactory {
  private boolean classCacheEnabled = true;
  private final ConcurrentMap<ResolvedType, Reflector> reflectorMap = new ConcurrentHashMap<>();
  private final ResolvedTypeFactory resolvedTypeFactory;

  public DefaultReflectorFactory() {
    this(ResolvedTypeUtil.getResolvedTypeFactory());
  }

  public DefaultReflectorFactory(ResolvedTypeFactory resolvedTypeFactory) {
    this.resolvedTypeFactory = resolvedTypeFactory;
  }

  @Override
  public boolean isClassCacheEnabled() {
    return classCacheEnabled;
  }

  @Override
  public void setClassCacheEnabled(boolean classCacheEnabled) {
    this.classCacheEnabled = classCacheEnabled;
  }

  @Override
  public ResolvedTypeFactory getResolvedTypeFactory() {
    return resolvedTypeFactory;
  }

  @Override
  public Reflector findForClass(Class<?> type) {
    return findForType(resolvedTypeFactory.constructType(type));
  }

  @Override
  public Reflector findForType(ResolvedType type) {
    if (classCacheEnabled) {
      // synchronized (type) removed see issue #461
      return MapUtil.computeIfAbsent(reflectorMap, type, Reflector::new);
    } else {
      return new Reflector(type);
    }
  }
}
