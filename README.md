MyBatis SQL Mapper Framework for Java
=====================================

[![build](https://github.com/mybatis/mybatis-3/workflows/Java%20CI/badge.svg)](https://github.com/mybatis/mybatis-3/actions?query=workflow%3A%22Java+CI%22)
[![Coverage Status](https://coveralls.io/repos/mybatis/mybatis-3/badge.svg?branch=master&service=github)](https://coveralls.io/github/mybatis/mybatis-3?branch=master)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/org.mybatis/mybatis.svg)](https://oss.sonatype.org/content/repositories/snapshots/org/mybatis/mybatis/)
[![License](https://img.shields.io/:license-apache-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Stack Overflow](https://img.shields.io/:stack%20overflow-mybatis-brightgreen.svg)](https://stackoverflow.com/questions/tagged/mybatis)
[![Project Stats](https://www.openhub.net/p/mybatis/widgets/project_thin_badge.gif)](https://www.openhub.net/p/mybatis)

![mybatis](https://mybatis.org/images/mybatis-logo.png)

The MyBatis SQL mapper framework makes it easier to use a relational database with object-oriented applications.
MyBatis couples objects with stored procedures or SQL statements using an XML descriptor or annotations.
Simplicity is the biggest advantage of the MyBatis data mapper over object relational mapping tools.

Essentials
----------

* [See the docs](https://mybatis.org/mybatis-3)
* [Download Latest](https://github.com/mybatis/mybatis-3/releases)
* [Download Snapshot](https://oss.sonatype.org/content/repositories/snapshots/org/mybatis/mybatis/)


### Maven
``` xml
<dependency>
    <groupId>io.github.flyinwind1</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.11.2</version>
</dependency>
```

### Todo

- [x] Use jackson JavaType instead of Class to parse GenericType
- [x] Create an interface ResolvedType to hide JavaType
- [x] Impl ListTypeHandler SetTypeHandler
- [x] Compatible with spring-mybatis, mybatis-plus
- [x] Make xml resultType and resultMap not required
- [x] Register all handleable type of ArrayTypeHandler to TypeHandlerRegister
- [x] Pass ParamMap values`s type to SqlSourceBuilder
- [ ] Cache ResolvedType and ResolvedMethod
- [ ] Resolve types in xml tags like \<foreach>
- [ ] Impl SpringResolvedType wrap org.springframework.core.ResolvableType
- [ ] Impl build in SimpleResolvedType
