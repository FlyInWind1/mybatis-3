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
package org.apache.ibatis.builder.annotation;

import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.resolved.ResolvedMethod;
import org.apache.ibatis.type.resolved.ResolvedType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @author Clinton Begin
 * @author Kazuki Shimizu
 */
public class ProviderSqlSource implements SqlSource {

  private final Configuration configuration;
  private final ResolvedType mapperType;
  private final Class<?> providerType;
  private final ResolvedType providerResolvedType;
  private final LanguageDriver languageDriver;
  private final ResolvedMethod resolvedMapperMethod;
  private final ResolvedType namedParamsType;
  private final Method providerMethod;
  private final String[] providerMethodArgumentNames;
  private final ResolvedType[] providerMethodParameterTypes;
  private final ProviderContext providerContext;
  private final Integer providerContextIndex;

  /**
   * This constructor will remove at a future version.
   *
   * @param configuration the configuration
   * @param provider      the provider
   * @deprecated Since 3.5.3, Please use the {@link #ProviderSqlSource(Configuration, Annotation, Class, Method)}
   * instead of this.
   */
  @Deprecated
  public ProviderSqlSource(Configuration configuration, Object provider) {
    this(configuration, provider, null, null);
  }

  /**
   * This constructor will remove at a future version.
   *
   * @param configuration the configuration
   * @param provider      the provider
   * @param mapperType    the mapper type
   * @param mapperMethod  the mapper method
   * @since 3.4.5
   * @deprecated Since 3.5.3, Please use the {@link #ProviderSqlSource(Configuration, Annotation, Class, Method)} instead of this.
   */
  @Deprecated
  public ProviderSqlSource(Configuration configuration, Object provider, Class<?> mapperType, Method mapperMethod) {
    this(configuration, (Annotation) provider, mapperType, mapperMethod);
  }

  /**
   * Instantiates a new provider sql source.
   *
   * @param configuration the configuration
   * @param provider      the provider
   * @param mapperType    the mapper type
   * @param mapperMethod  the mapper method
   * @since 3.5.3
   */
  public ProviderSqlSource(Configuration configuration, Annotation provider, Class<?> mapperType, Method mapperMethod) {
    this(configuration, provider, configuration.constructType(mapperType), mapperMethod);
  }

  /**
   * Instantiates a new provider sql source.
   *
   * @param configuration the configuration
   * @param provider      the provider
   * @param mapperType    the mapper type
   * @param mapperMethod  the mapper method
   * @since 3.5.3
   */
  public ProviderSqlSource(Configuration configuration, Annotation provider, ResolvedType mapperType, Method mapperMethod) {
    String candidateProviderMethodName;
    Method candidateProviderMethod = null;
    try {
      this.configuration = configuration;
      this.mapperType = mapperType;
      Lang lang = mapperMethod == null ? null : mapperMethod.getAnnotation(Lang.class);
      this.languageDriver = configuration.getLanguageDriver(lang == null ? null : lang.value());
      this.providerType = getProviderType(configuration, provider, mapperMethod);
      this.providerResolvedType = configuration.constructType(providerType);
      candidateProviderMethodName = (String) provider.annotationType().getMethod("method").invoke(provider);

      if (candidateProviderMethodName.length() == 0 && ProviderMethodResolver.class.isAssignableFrom(this.providerType)) {
        candidateProviderMethod = ((ProviderMethodResolver) this.providerType.getDeclaredConstructor().newInstance())
          .resolveMethod(new ProviderContext(mapperType, mapperMethod, configuration.getDatabaseId()));
      }
      if (candidateProviderMethod == null) {
        candidateProviderMethodName = candidateProviderMethodName.length() == 0 ? "provideSql" : candidateProviderMethodName;
        for (Method m : this.providerType.getMethods()) {
          if (candidateProviderMethodName.equals(m.getName()) && CharSequence.class.isAssignableFrom(m.getReturnType())) {
            if (candidateProviderMethod != null) {
              throw new BuilderException("Error creating SqlSource for SqlProvider. Method '"
                + candidateProviderMethodName + "' is found multiple in SqlProvider '" + this.providerType.getName()
                + "'. Sql provider method can not overload.");
            }
            candidateProviderMethod = m;
          }
        }
      }
    } catch (BuilderException e) {
      throw e;
    } catch (Exception e) {
      throw new BuilderException("Error creating SqlSource for SqlProvider.  Cause: " + e, e);
    }
    if (candidateProviderMethod == null) {
      throw new BuilderException("Error creating SqlSource for SqlProvider. Method '"
        + candidateProviderMethodName + "' not found in SqlProvider '" + this.providerType.getName() + "'.");
    }
    this.providerMethod = candidateProviderMethod;
    ResolvedMethod providerResolveMethod = this.providerResolvedType.resolveMethod(this.providerMethod);
    this.providerMethodArgumentNames = new ParamNameResolver(configuration, providerResolveMethod).getNames();
    this.providerMethodParameterTypes = providerResolveMethod.getParameterTypes();

    ProviderContext candidateProviderContext = null;
    Integer candidateProviderContextIndex = null;
    for (int i = 0; i < this.providerMethodParameterTypes.length; i++) {
      ResolvedType parameterType = this.providerMethodParameterTypes[i];
      if (parameterType.hasRawClass(ProviderContext.class)) {
        if (candidateProviderContext != null) {
          throw new BuilderException("Error creating SqlSource for SqlProvider. ProviderContext found multiple in SqlProvider method ("
            + this.providerType.getName() + "." + providerResolveMethod.getName()
            + "). ProviderContext can not define multiple in SqlProvider method argument.");
        }
        candidateProviderContext = new ProviderContext(mapperType, mapperMethod, configuration.getDatabaseId());
        candidateProviderContextIndex = i;
      }
    }
    this.providerContext = candidateProviderContext;
    this.providerContextIndex = candidateProviderContextIndex;
    this.resolvedMapperMethod = mapperType.resolveMethod(mapperMethod);
    this.namedParamsType = resolvedMapperMethod.namedParamsType(configuration);
  }

  @Override
  public BoundSql getBoundSql(Object parameterObject) {
    SqlSource sqlSource = createSqlSource(parameterObject);
    return sqlSource.getBoundSql(parameterObject);
  }

  private SqlSource createSqlSource(Object parameterObject) {
    try {
      String sql;
      if (parameterObject instanceof Map) {
        int bindParameterCount = providerMethodParameterTypes.length - (providerContext == null ? 0 : 1);
        if (bindParameterCount == 1
          && providerMethodParameterTypes[Integer.valueOf(0).equals(providerContextIndex) ? 1 : 0].isTypeOrSuperTypeOf(parameterObject.getClass())) {
          sql = invokeProviderMethod(extractProviderMethodArguments(parameterObject));
        } else {
          @SuppressWarnings("unchecked")
          Map<String, Object> params = (Map<String, Object>) parameterObject;
          sql = invokeProviderMethod(extractProviderMethodArguments(params, providerMethodArgumentNames));
        }
      } else if (providerMethodParameterTypes.length == 0) {
        sql = invokeProviderMethod();
      } else if (providerMethodParameterTypes.length == 1) {
        if (providerContext == null) {
          sql = invokeProviderMethod(parameterObject);
        } else {
          sql = invokeProviderMethod(providerContext);
        }
      } else if (providerMethodParameterTypes.length == 2) {
        sql = invokeProviderMethod(extractProviderMethodArguments(parameterObject));
      } else {
        throw new BuilderException("Cannot invoke SqlProvider method '" + providerMethod
          + "' with specify parameter '" + (parameterObject == null ? null : parameterObject.getClass())
          + "' because SqlProvider method arguments for '" + resolvedMapperMethod + "' is an invalid combination.");
      }
      return languageDriver.createSqlSource(configuration, sql, namedParamsType);
    } catch (BuilderException e) {
      throw e;
    } catch (Exception e) {
      throw new BuilderException("Error invoking SqlProvider method '" + providerMethod
        + "' with specify parameter '" + (parameterObject == null ? null : parameterObject.getClass()) + "'.  Cause: " + extractRootCause(e), e);
    }
  }

  private Throwable extractRootCause(Exception e) {
    Throwable cause = e;
    while (cause.getCause() != null) {
      cause = cause.getCause();
    }
    return cause;
  }

  private Object[] extractProviderMethodArguments(Object parameterObject) {
    if (providerContext != null) {
      Object[] args = new Object[2];
      args[providerContextIndex == 0 ? 1 : 0] = parameterObject;
      args[providerContextIndex] = providerContext;
      return args;
    } else {
      return new Object[]{parameterObject};
    }
  }

  private Object[] extractProviderMethodArguments(Map<String, Object> params, String[] argumentNames) {
    Object[] args = new Object[argumentNames.length];
    for (int i = 0; i < args.length; i++) {
      if (providerContextIndex != null && providerContextIndex == i) {
        args[i] = providerContext;
      } else {
        args[i] = params.get(argumentNames[i]);
      }
    }
    return args;
  }

  private String invokeProviderMethod(Object... args) throws Exception {
    Object targetObject = null;
    if (!Modifier.isStatic(providerMethod.getModifiers())) {
      targetObject = providerType.getDeclaredConstructor().newInstance();
    }
    CharSequence sql = (CharSequence) providerMethod.invoke(targetObject, args);
    return sql != null ? sql.toString() : null;
  }

  private Class<?> getProviderType(Configuration configuration, Annotation providerAnnotation, Method mapperMethod)
    throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<?> type = (Class<?>) providerAnnotation.annotationType().getMethod("type").invoke(providerAnnotation);
    Class<?> value = (Class<?>) providerAnnotation.annotationType().getMethod("value").invoke(providerAnnotation);
    if (value == void.class && type == void.class) {
      if (configuration.getDefaultSqlProviderType() != null) {
        return configuration.getDefaultSqlProviderType();
      }
      throw new BuilderException("Please specify either 'value' or 'type' attribute of @"
        + providerAnnotation.annotationType().getSimpleName()
        + " at the '" + mapperMethod.toString() + "'.");
    }
    if (value != void.class && type != void.class && value != type) {
      throw new BuilderException("Cannot specify different class on 'value' and 'type' attribute of @"
        + providerAnnotation.annotationType().getSimpleName()
        + " at the '" + mapperMethod.toString() + "'.");
    }
    return value == void.class ? type : value;
  }

}
