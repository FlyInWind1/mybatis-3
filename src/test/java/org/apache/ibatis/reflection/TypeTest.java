package org.apache.ibatis.reflection;

import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.TypeVariable;
import java.util.List;

public class TypeTest {

  @Test
  void test1() {
    TypeVariable<Class<List>>[] typeParameters = List.class.getTypeParameters();
//    TypeFactory.defaultInstance().withModifier()
    System.out.println(typeParameters);
  }
}
