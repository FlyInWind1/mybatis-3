**I know this is really a big change, and may bring up some backward compatible problem, so suggest is welcome**

### Add future

1. Add ResolvedType, only wrap Jackson JavaType. For not depend on jackson, copy classes relate to JavaType into
   org.apache.ibatis.reflection.type.jackson package. Now use JavaType from external Jackson first. #2187
2. Add ResolvedMethod, to get returnType and parameterType easier
3. Implement ListTypeHandler SetTypeHandler. #1445
4. Register ListTypeHandler, SetTypeHandler and ArrayTypeHandler into TypeHandlerRegistry, then we don't
   need to write typeHandler="ArrayTypeHandler" in xml
5. resultType and resultMap in xml is not obligatory, get Mapper Class by namespace and get Method by id, then we can
   get returnType

### Other changes

1. Add ParamNameResolver#getNamedParamsType, this may return a MapDescriptorResolvedType when
   ParamNameResolver#getNamedParams return a ParamMap, MapDescriptorResolvedType contains each value ResolvedType
2. List or array index access check, see PropertyTokenizer#isIndexAccess, without this change ArrayTypeHandler may used
   for #{array[0]}
3. LanguageDriver interface add two createSqlSource methods that use ResolvedType instead of Class, all createSqlSource
   methods are default for backward compatible
4. TypeReference do not resolve TypeParameter at construct time, getRawType is useless for now
5. Make some method and field protected. I tried to make my futures compatible with mybatisPlus, but fond they copy
   lost of code to their project. make method protected, so that mybatisPlus do not need copy

### May be not backward compatible

1. TypeHandlerRegistry#typeHandlerMap use ResolvedType as map key, this may have problem for user custom typeHandler.
   For example user write a typeHandler for ArrayList\<Object> but hope autoMapping for ArrayList\<String>, this may no
   problem before, but now autoMapping will can't find typeHandler, because of typeParameter not match
2. TypeHandlerRegistry#getMappingTypeHandler will check whether the TypeHandler need a Class for construct. I hope user
   can just write typeHandler without write javaType, because javaType attribute can't resolve ParameterizedType. #995
3. Try to resolve TypeHandler at SqlSourceBuilder, not use UnknownTypeHandler, because in UnknownTypeHandler can only
   get parameter Class, not ParameterizedType
4. DynamicSqlSource add ParamType, for pass ParameterizedType, I add a parameterType parameter to constructor, this may
   occur exception if user pass instance that not instanceof parameterType
5. User can pass Map<String,String> into function with Map<Map,Integer> parameter by cast, just like (Map)
   stringValueMap. Mybatis use UnknownTypeHandler for Map parameter at before, there is no problem if user pass wrong
   type. but now we can know we just need use IntegerTypeHandler to handle Map values, use IntegerTypeHandler to handle
   String will occur exception
