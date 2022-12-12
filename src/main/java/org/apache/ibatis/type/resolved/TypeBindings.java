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
package org.apache.ibatis.type.resolved;


import java.util.Arrays;

public class TypeBindings {
  protected final String[] typeParametersNames;
  protected final ResolvedType[] typeParameters;

  public TypeBindings(String[] typeParametersNames, ResolvedType[] typeParameters) {
    this.typeParametersNames = typeParametersNames;
    this.typeParameters = typeParameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TypeBindings that = (TypeBindings) o;
    return Arrays.equals(typeParametersNames, that.typeParametersNames) && Arrays.equals(typeParameters, that.typeParameters);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(typeParametersNames);
    result = 31 * result + Arrays.hashCode(typeParameters);
    return result;
  }

  private static final TypeBindings EMPTY = new TypeBindings(new String[0], ResolvedTypeUtil.EMPTY_TYPE_ARRAY);

  public static TypeBindings emptyBindings() {
    return EMPTY;
  }
}
