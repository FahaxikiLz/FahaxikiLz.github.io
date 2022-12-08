---
title: ssm_传智健康项目
date: 2022-11-02 14:46:09
tags:
- ssm_传智健康项目
categories:	
- 练手项目
---

> 前端：vue、axios、elementui、ECharts
>
> 后端：spring、srping mvc、mybatis、dubbo、七牛云、Redis、Quartz、Apache POI、短信服务、Spring Security

# 第1章 项目概述和环境搭建

## 1. 项目概述

### 1.1 项目介绍

传智健康管理系统是一款应用于健康管理机构的业务系统，实现健康管理机构工作内容可视化、会员管理专业化、健康评估数字化、健康干预流程化、知识库集成化，从而提高健康管理师的工作效率，加强与会员间的互动，增强管理者对健康管理机构运营情况的了解。

详见：资料中的传智健康PRD文档.docx

### 1.2 原型展示

参见资料中的静态原型。

### 1.3 技术架构

![1](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/1.png)

### 1.4 功能架构

![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2.png)

### 1.5 软件开发流程

软件开发一般会经历如下几个阶段，整个过程是顺序展开，所以通常称为瀑布模型。

![11](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/11.png)

## 2. 环境搭建

### 2.1 项目结构

本项目采用maven分模块开发方式，即对整个项目拆分为几个maven工程，每个maven工程存放特定的一类代码，具体如下：

![10](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/10.png)

各模块职责定位：

health_parent：父工程，打包方式为pom，统一锁定依赖的版本，同时聚合其他子模块便于统一执行maven命令

health_common：通用模块，打包方式为jar，存放项目中使用到的一些工具类、实体类、返回结果和常量类

health_interface：打包方式为jar，存放服务接口

health_service_provider：Dubbo服务模块，打包方式为war，存放服务实现类、Dao接口、Mapper映射文件等，作为服务提供方，需要部署到tomcat运行

health_backend：传智健康管理后台，打包方式为war，作为Dubbo服务消费方，存放Controller、HTML页面、js、css、spring配置文件等，需要部署到tomcat运行

health_mobile：移动端前台，打包方式为war，作为Dubbo服务消费方，存放Controller、HTML页面、js、css、spring配置文件等，需要部署到tomcat运行

### 2.2 maven项目搭建

通过前面的项目功能架构图可以知道本项目分为传智健康管理后台和传智健康前台（微信端）

#### 2.2.1 health_parent

创建health_parent，父工程，打包方式为pom，用于统一管理依赖版本

pom.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
		 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.itheima</groupId>
    <artifactId>health_parent</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    <!-- 集中定义依赖版本号 -->
    <properties>
        <junit.version>4.12</junit.version>
        <spring.version>5.0.5.RELEASE</spring.version>
        <pagehelper.version>4.1.4</pagehelper.version>
        <servlet-api.version>2.5</servlet-api.version>
        <dubbo.version>2.6.0</dubbo.version>
        <zookeeper.version>3.4.7</zookeeper.version>
        <zkclient.version>0.1</zkclient.version>
        <mybatis.version>3.4.5</mybatis.version>
        <mybatis.spring.version>1.3.1</mybatis.spring.version>
        <mybatis.paginator.version>1.2.15</mybatis.paginator.version>
        <mysql.version>5.1.32</mysql.version>
        <druid.version>1.0.9</druid.version>
        <commons-fileupload.version>1.3.1</commons-fileupload.version>
        <spring.security.version>5.0.5.RELEASE</spring.security.version>
        <poi.version>3.14</poi.version>
        <jedis.version>2.9.0</jedis.version>
        <quartz.version>2.2.1</quartz.version>
    </properties>
    <!-- 依赖管理标签  必须加 -->
    <dependencyManagement>
        <dependencies>
            <!-- Spring -->
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-context</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-beans</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-web</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-webmvc</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-jdbc</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-aspects</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-jms</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-context-support</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-test</artifactId>
                <version>${spring.version}</version>
            </dependency>
            <!-- dubbo相关 -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>dubbo</artifactId>
                <version>${dubbo.version}</version>
            </dependency>
            <dependency>
                <groupId>org.apache.zookeeper</groupId>
                <artifactId>zookeeper</artifactId>
                <version>${zookeeper.version}</version>
            </dependency>
            <dependency>
                <groupId>com.github.sgroschupf</groupId>
                <artifactId>zkclient</artifactId>
                <version>${zkclient.version}</version>
            </dependency>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.12</version>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>1.2.47</version>
            </dependency>
            <dependency>
                <groupId>javassist</groupId>
                <artifactId>javassist</artifactId>
                <version>3.12.1.GA</version>
            </dependency>
            <dependency>
                <groupId>commons-codec</groupId>
                <artifactId>commons-codec</artifactId>
                <version>1.10</version>
            </dependency>
            <dependency>
                <groupId>com.github.pagehelper</groupId>
                <artifactId>pagehelper</artifactId>
                <version>${pagehelper.version}</version>
            </dependency>
            <!-- Mybatis -->
            <dependency>
                <groupId>org.mybatis</groupId>
                <artifactId>mybatis</artifactId>
                <version>${mybatis.version}</version>
            </dependency>
            <dependency>
                <groupId>org.mybatis</groupId>
                <artifactId>mybatis-spring</artifactId>
                <version>${mybatis.spring.version}</version>
            </dependency>
            <dependency>
                <groupId>com.github.miemiedev</groupId>
                <artifactId>mybatis-paginator</artifactId>
                <version>${mybatis.paginator.version}</version>
            </dependency>
            <!-- MySql -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <!-- 连接池 -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>${druid.version}</version>
            </dependency>
            <!-- 文件上传组件 -->
            <dependency>
                <groupId>commons-fileupload</groupId>
                <artifactId>commons-fileupload</artifactId>
                <version>${commons-fileupload.version}</version>
            </dependency>
            <dependency>
                <groupId>org.quartz-scheduler</groupId>
                <artifactId>quartz</artifactId>
                <version>${quartz.version}</version>
            </dependency>
            <dependency>
                <groupId>org.quartz-scheduler</groupId>
                <artifactId>quartz-jobs</artifactId>
                <version>${quartz.version}</version>
            </dependency>
            <dependency>
                <groupId>com.sun.jersey</groupId>
                <artifactId>jersey-client</artifactId>
                <version>1.18.1</version>
            </dependency>
            <dependency>
                <groupId>com.qiniu</groupId>
                <artifactId>qiniu-java-sdk</artifactId>
                <version>7.2.0</version>
            </dependency>
            <!--POI报表-->
            <dependency>
                <groupId>org.apache.poi</groupId>
                <artifactId>poi</artifactId>
                <version>${poi.version}</version>
            </dependency>
            <dependency>
                <groupId>org.apache.poi</groupId>
                <artifactId>poi-ooxml</artifactId>
                <version>${poi.version}</version>
            </dependency>
            <dependency>
                <groupId>redis.clients</groupId>
                <artifactId>jedis</artifactId>
                <version>${jedis.version}</version>
            </dependency>
            <!-- 安全框架 -->
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-web</artifactId>
                <version>${spring.security.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-config</artifactId>
                <version>${spring.security.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-taglibs</artifactId>
                <version>${spring.security.version}</version>
            </dependency>
            <dependency>
                <groupId>com.github.penggle</groupId>
                <artifactId>kaptcha</artifactId>
                <version>2.3.2</version>
                <exclusions>
                    <exclusion>
                        <groupId>javax.servlet</groupId>
                        <artifactId>javax.servlet-api</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>dom4j</groupId>
                <artifactId>dom4j</artifactId>
                <version>1.6.1</version>
            </dependency>
            <dependency>
                <groupId>xml-apis</groupId>
                <artifactId>xml-apis</artifactId>
                <version>1.4.01</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
            <version>${servlet-api.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <!-- java编译插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.2</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
~~~

#### 2.2.2 health_common

创建health_common，子工程，打包方式为jar，存放通用组件，例如工具类、实体类等

pom.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
		 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>health_parent</artifactId>
        <groupId>com.itheima</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>health_common</artifactId>
    <packaging>jar</packaging>
    <dependencies>
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
        </dependency>
        <!-- Mybatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.miemiedev</groupId>
            <artifactId>mybatis-paginator</artifactId>
        </dependency>
        <!-- MySql -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!-- 连接池 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
        </dependency>
        <!-- Spring -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jms</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
        </dependency>
        <!-- dubbo相关 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.sgroschupf</groupId>
            <artifactId>zkclient</artifactId>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
        </dependency>
        <dependency>
            <groupId>javassist</groupId>
            <artifactId>javassist</artifactId>
        </dependency>
        <dependency>
            <groupId>commons-codec</groupId>
            <artifactId>commons-codec</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
        </dependency>
        <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
        </dependency>
        <dependency>
            <groupId>com.qiniu</groupId>
            <artifactId>qiniu-java-sdk</artifactId>
        </dependency>
        <dependency>
            <groupId>com.sun.jersey</groupId>
            <artifactId>jersey-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-taglibs</artifactId>
        </dependency>
    </dependencies>
</project>
~~~

#### 2.2.5 health_interface

创建health_interface，子工程，打包方式为jar，存放服务接口

pom.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>health_parent</artifactId>
        <groupId>com.itheima</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>health_interface</artifactId>
    <packaging>jar</packaging>
    <dependencies>
        <dependency>
            <groupId>com.itheima</groupId>
            <artifactId>health_common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
~~~

#### 2.2.6 health_service_provider

创建health_service_provider，子工程，打包方式为war，作为服务单独部署，存放服务类、Dao接口和Mapper映射文件等

pom.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                            http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>com.itheima</groupId>
        <artifactId>health_parent</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>health_service_provider</artifactId>
    <packaging>war</packaging>
    <dependencies>
        <dependency>
            <groupId>com.itheima</groupId>
            <artifactId>health_interface</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <configuration>
                    <!-- 指定端口 -->
                    <port>81</port>
                    <!-- 请求路径 -->
                    <path>/</path>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
~~~

log4j.properties

~~~properties
### direct log messages to stdout ###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.err
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### direct messages to file mylog.log ###
log4j.appender.file=org.apache.log4j.FileAppender
log4j.appender.file.File=c:\\mylog.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### set log levels - for more verbose logging change 'info' to 'debug' ###

log4j.rootLogger=debug, stdout
~~~

SqlMapConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
		"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<plugins>
		<!-- com.github.pagehelper 为 PageHelper 类所在包名 -->
		<plugin interceptor="com.github.pagehelper.PageHelper">
			<!-- 设置数据库类型 Oracle,Mysql,MariaDB,SQLite,Hsqldb,PostgreSQL 六种数据库-->
			<property name="dialect" value="mysql"/>
		</plugin>
	</plugins>
</configuration>
```

spring-dao.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
							http://www.springframework.org/schema/beans/spring-beans-4.2.xsd
							http://www.springframework.org/schema/context
							http://www.springframework.org/schema/context/spring-context.xsd
							http://www.springframework.org/schema/aop
							http://www.springframework.org/schema/aop/spring-aop.xsd
							http://www.springframework.org/schema/tx
							http://www.springframework.org/schema/tx/spring-tx.xsd
							http://www.springframework.org/schema/util
							http://www.springframework.org/schema/util/spring-util.xsd">

	<!--数据源-->
	<bean id="dataSource" 
      	  class="com.alibaba.druid.pool.DruidDataSource" destroy-method="close">
		<property name="username" value="root" />
		<property name="password" value="root" />
		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
		<property name="url" value="jdbc:mysql://localhost:3306/health" />
	</bean>
	<!--spring和mybatis整合的工厂bean-->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="configLocation" value="classpath:SqlMapConfig.xml" />
	</bean>
	<!--批量扫描接口生成代理对象-->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<!--指定接口所在的包-->
		<property name="basePackage" value="com.itheima.dao" />
	</bean>
</beans>
```

spring-tx.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                        http://www.springframework.org/schema/beans/spring-beans.xsd
                        http://www.springframework.org/schema/mvc
                        http://www.springframework.org/schema/mvc/spring-mvc.xsd
                        http://www.springframework.org/schema/tx
                        http://www.springframework.org/schema/tx/spring-tx.xsd
                        http://www.springframework.org/schema/context
                        http://www.springframework.org/schema/context/spring-context.xsd">
    <!-- 事务管理器  -->
    <bean id="transactionManager" 
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>
    <!--
        开启事务控制的注解支持
        注意：此处必须加入proxy-target-class="true"，
              需要进行事务控制，会由Spring框架产生代理对象，
              Dubbo需要将Service发布为服务，要求必须使用cglib创建代理对象。
    -->
    <tx:annotation-driven transaction-manager="transactionManager" 
                          proxy-target-class="true"/>
</beans>
~~~

spring-service.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                            http://www.springframework.org/schema/beans/spring-beans.xsd
                            http://www.springframework.org/schema/mvc
                            http://www.springframework.org/schema/mvc/spring-mvc.xsd
                            http://code.alibabatech.com/schema/dubbo
                            http://code.alibabatech.com/schema/dubbo/dubbo.xsd
                            http://www.springframework.org/schema/context
                            http://www.springframework.org/schema/context/spring-context.xsd">
    <!-- 指定应用名称 -->
    <dubbo:application name="health_service_provider"/>
    <!--指定暴露服务的端口，如果不指定默认为20880-->
    <dubbo:protocol name="dubbo" port="20887"/>
    <!--指定服务注册中心地址-->
    <dubbo:registry address="zookeeper://127.0.0.1:2181"/>
    <!--批量扫描，发布服务-->
    <dubbo:annotation package="com.itheima.service"/>
</beans>
~~~

web.xml

~~~xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >
<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <!-- 加载spring容器 -->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath*:applicationContext*.xml</param-value>
  </context-param>
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
</web-app>
~~~

#### 2.2.7 health_backend

创建health_backend，子工程，打包方式为war，单独部署，存放Controller、页面等

pom.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                            http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>health_parent</artifactId>
        <groupId>com.itheima</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>health_backend</artifactId>
    <packaging>war</packaging>
    <dependencies>
        <dependency>
            <groupId>com.itheima</groupId>
            <artifactId>health_interface</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <configuration>
                    <!-- 指定端口 -->
                    <port>82</port>
                    <!-- 请求路径 -->
                    <path>/</path>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
~~~

log4j.properties

~~~properties
### direct log messages to stdout ###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.err
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### direct messages to file mylog.log ###
log4j.appender.file=org.apache.log4j.FileAppender
log4j.appender.file.File=c:\\mylog.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### set log levels - for more verbose logging change 'info' to 'debug' ###

log4j.rootLogger=info, stdout
~~~

springmvc.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
						http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/mvc
						http://www.springframework.org/schema/mvc/spring-mvc.xsd
						http://code.alibabatech.com/schema/dubbo
						http://code.alibabatech.com/schema/dubbo/dubbo.xsd
						http://www.springframework.org/schema/context
						http://www.springframework.org/schema/context/spring-context.xsd">
	<mvc:annotation-driven>
	  <mvc:message-converters register-defaults="true">
	    <bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter">
	      <property name="supportedMediaTypes" value="application/json"/>
	      <property name="features">
	        <list>
	          <value>WriteMapNullValue</value>
	          <value>WriteDateUseDateFormat</value>
	        </list>
	      </property>
	    </bean>
	  </mvc:message-converters>
	</mvc:annotation-driven>
	<!-- 指定应用名称 -->
	<dubbo:application name="health_backend" />
	<!--指定服务注册中心地址-->
	<dubbo:registry address="zookeeper://127.0.0.1:2181"/>
	<!--批量扫描-->
	<dubbo:annotation package="com.itheima.controller" />
	<!--
		超时全局设置 10分钟
		check=false 不检查服务提供方，开发阶段建议设置为false
		check=true 启动时检查服务提供方，如果服务提供方没有启动则报错
	-->
	<dubbo:consumer timeout="600000" check="false"/>
	<!--文件上传组件-->
	<bean id="multipartResolver" 
          class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
		<property name="maxUploadSize" value="104857600" />
		<property name="maxInMemorySize" value="4096" />
		<property name="defaultEncoding" value="UTF-8"/>
	</bean>
</beans>
~~~

web.xml

~~~xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd">
<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <!-- 解决post乱码 -->
  <filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>utf-8</param-value>
    </init-param>
    <init-param>
      <param-name>forceEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!-- 指定加载的配置文件 ，通过参数contextConfigLocation加载 -->
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:springmvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>*.do</url-pattern>
  </servlet-mapping>
</web-app>
~~~

#### 注意点

> 1. 父项目打包方式为`pom`
> 2. 父工程health_parent中管理多有的依赖，并且用`<dependencyManagement>`标签包裹，这样子项目就不能直接继承父项目的依赖，需要显式的设置依赖。这样的好处是子项目只需要显式设置依赖，但是不用设置依赖的版本，因为会继承父项目的版本。如果去除这个标签，那么子项目就会继承父项目中所有的依赖，这样的话，子项目中用不到的依赖也会被继承
> 3. 各各项目之间的调用是通过在`pom`中引入需要调用的项目，以及调用项目的依赖也会引入
> 4. 配置项需要通过`web.xml`加载，可以直接加载多个配置项classpath：spring*.xml
> 5. 使用注解，要开启注解，在xml配置文件中或在配置类中使用注解

## 3. Power Designer

### 3.1 Power Designer介绍

PowerDesigner是Sybase公司的一款软件，使用它可以方便地对系统进行分析设计，他几乎包括了数据库模型设计的全过程。利用PowerDesigner可以制作数据流程图、概念数据模型、物理数据模型、面向对象模型。

在项目设计阶段通常会使用PowerDesigner进行数据库设计。使用PowerDesigner可以更加直观的表现出数据库中表之间的关系，并且可以直接导出相应的建表语句。

### 3.2 Power Designer使用

#### 3.2.1 创建物理数据模型

操作步骤：

（1）创建数据模型PDM

![12](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/12.png)

（2）选择数据库类型

![13](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/13.png)

（3）创建表和字段

![14](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/14.png)

指定表名

![15](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/15.png)

创建字段

![16](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/16.png)

设置某个字段属性，在字段上右键

![17](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/17.png)

添加外键约束

![18](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/18.png)

![19](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/19.png)

![20](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/20.png)

![21](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/21.png)

![22](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/22.png)

#### 3.2.2 从PDM导出SQL脚本

可以通过PowerDesigner设计的PDM模型导出为SQL脚本，如下：

![23](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/23.png)

![24](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/24.png)

#### 3.2.3 逆向工程

上面我们是首先创建PDM模型，然后通过PowerDesigner提供的功能导出SQL脚本。实际上这个过程也可以反过来，也就是我们可以通过SQL脚本逆向生成PDM模型，这称为逆向工程，操作如下：

![25](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/25.png)

![26](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/26.png)

![27](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/27.png)

![28](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/28.png)

#### 3.2.4 生成数据库报表文件

通过PowerDesigner提供的功能，可以将PDM模型生成报表文件，具体操作如下：

（1）打开报表向导窗口

![29](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/29.png)

（2）指定报表名称和语言

![30](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/30.png)

（3）选择报表格式和样式

![31](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/31.png)

![32](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/32.png)

（4）选择对象类型

![33](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/33.png)

（5）执行生成操作

![34](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/34.png)

# 第2章 预约管理-检查项管理

## 1. 需求分析

传智健康管理系统是一款应用于健康管理机构的业务系统，实现健康管理机构工作内容可视化、患者管理专业化、健康评估数字化、健康干预流程化、知识库集成化，从而提高健康管理师的工作效率，加强与患者间的互动，增强管理者对健康管理机构运营情况的了解。

系统分为传智健康后台管理系统和移动端应用两部分。其中后台系统提供给健康管理机构内部人员（包括系统管理员、健康管理师等）使用，微信端应用提供给健康管理机构的用户（体检用户）使用。

本项目功能架构图：

![1](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/1-1667398283370.png)

通过上面的功能架构图可以看到，传智健康后台管理系统有会员管理、预约管理、健康评估、健康干预等功能。移动端有会员管理、体检预约、体检报告等功能。后台系统和移动端应用都会通过Dubbo调用服务层发布的服务来完成具体的操作。本项目属于典型的SOA架构形式。

本章节完成的功能开发是预约管理功能，包括检查项管理、检查组管理、体检套餐管理、预约设置等（参见产品原型）。预约管理属于系统的基础功能，主要就是管理一些体检的基础数据。

## 2. 基础环境搭建

### 2.1 导入预约管理模块数据表

操作步骤：

（1）根据资料中提供的itcasthealth.pdm文件导出SQL脚本

![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2-1667398283370.png)



![3](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/3.png)

（2）创建本项目使用的数据库itcast_health

![4](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day02/%25E8%25AE%25B2%25E4%25B9%2589/4.png)

（3）将PowerDesigner导出的SQL脚本导入itcast_health数据库进行建表

![5](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/5.png)

### 2.2 导入预约管理模块实体类

将资料中提供的POJO实体类复制到health_common工程中。

### 2.3 导入项目所需公共资源

项目开发过程中一般会提供一些公共资源，供多个模块或者系统来使用。

本章节我们导入的公共资源有：

（1）返回消息常量类MessageConstant，放到health_common工程中

~~~java
package com.itheima.constant;

/**
 * 消息常量
 */
public class MessageConstant {
    public static final String DELETE_CHECKITEM_FAIL = "删除检查项失败";
    public static final String DELETE_CHECKITEM_SUCCESS = "删除检查项成功";
    public static final String ADD_CHECKITEM_SUCCESS = "新增检查项成功";
    public static final String ADD_CHECKITEM_FAIL = "新增检查项失败";
    public static final String EDIT_CHECKITEM_FAIL = "编辑检查项失败";
    public static final String EDIT_CHECKITEM_SUCCESS = "编辑检查项成功";
    public static final String QUERY_CHECKITEM_SUCCESS = "查询检查项成功";
    public static final String QUERY_CHECKITEM_FAIL = "查询检查项失败";
    public static final String UPLOAD_SUCCESS = "上传成功";
    public static final String ADD_CHECKGROUP_FAIL = "新增检查组失败";
    public static final String ADD_CHECKGROUP_SUCCESS = "新增检查组成功";
    public static final String DELETE_CHECKGROUP_FAIL = "删除检查组失败";
    public static final String DELETE_CHECKGROUP_SUCCESS = "删除检查组成功";
    public static final String QUERY_CHECKGROUP_SUCCESS = "查询检查组成功";
    public static final String QUERY_CHECKGROUP_FAIL = "查询检查组失败";
    public static final String EDIT_CHECKGROUP_FAIL = "编辑检查组失败";
    public static final String EDIT_CHECKGROUP_SUCCESS = "编辑检查组成功";
    public static final String PIC_UPLOAD_SUCCESS = "图片上传成功";
    public static final String PIC_UPLOAD_FAIL = "图片上传失败";
    public static final String ADD_SETMEAL_FAIL = "新增套餐失败";
    public static final String ADD_SETMEAL_SUCCESS = "新增套餐成功";
    public static final String IMPORT_ORDERSETTING_FAIL = "批量导入预约设置数据失败";
    public static final String IMPORT_ORDERSETTING_SUCCESS = "批量导入预约设置数据成功";
    public static final String GET_ORDERSETTING_SUCCESS = "获取预约设置数据成功";
    public static final String GET_ORDERSETTING_FAIL = "获取预约设置数据失败";
    public static final String ORDERSETTING_SUCCESS = "预约设置成功";
    public static final String ORDERSETTING_FAIL = "预约设置失败";
    public static final String ADD_MEMBER_FAIL = "新增会员失败";
    public static final String ADD_MEMBER_SUCCESS = "新增会员成功";
    public static final String DELETE_MEMBER_FAIL = "删除会员失败";
    public static final String DELETE_MEMBER_SUCCESS = "删除会员成功";
    public static final String EDIT_MEMBER_FAIL = "编辑会员失败";
    public static final String EDIT_MEMBER_SUCCESS = "编辑会员成功";
    public static final String TELEPHONE_VALIDATECODE_NOTNULL = "手机号和验证码都不能为空";
    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String VALIDATECODE_ERROR = "验证码输入错误";
    public static final String QUERY_ORDER_SUCCESS = "查询预约信息成功";
    public static final String QUERY_ORDER_FAIL = "查询预约信息失败";
    public static final String QUERY_SETMEALLIST_SUCCESS = "查询套餐列表数据成功";
    public static final String QUERY_SETMEALLIST_FAIL = "查询套餐列表数据失败";
    public static final String QUERY_SETMEAL_SUCCESS = "查询套餐数据成功";
    public static final String QUERY_SETMEAL_FAIL = "查询套餐数据失败";
    public static final String SEND_VALIDATECODE_FAIL = "验证码发送失败";
    public static final String SEND_VALIDATECODE_SUCCESS = "验证码发送成功";
    public static final String SELECTED_DATE_CANNOT_ORDER = "所选日期不能进行体检预约";
    public static final String ORDER_FULL = "预约已满";
    public static final String HAS_ORDERED = "已经完成预约，不能重复预约";
    public static final String ORDER_SUCCESS = "预约成功";
    public static final String GET_USERNAME_SUCCESS = "获取当前登录用户名称成功";
    public static final String GET_USERNAME_FAIL = "获取当前登录用户名称失败";
    public static final String GET_MENU_SUCCESS = "获取当前登录用户菜单成功";
    public static final String GET_MENU_FAIL = "获取当前登录用户菜单失败";
    public static final String GET_MEMBER_NUMBER_REPORT_SUCCESS = "获取会员统计数据成功";
    public static final String GET_MEMBER_NUMBER_REPORT_FAIL = "获取会员统计数据失败";
    public static final String GET_SETMEAL_COUNT_REPORT_SUCCESS = "获取套餐统计数据成功";
    public static final String GET_SETMEAL_COUNT_REPORT_FAIL = "获取套餐统计数据失败";
    public static final String GET_BUSINESS_REPORT_SUCCESS = "获取运营统计数据成功";
    public static final String GET_BUSINESS_REPORT_FAIL = "获取运营统计数据失败";
    public static final String GET_SETMEAL_LIST_SUCCESS = "查询套餐列表数据成功";
    public static final String GET_SETMEAL_LIST_FAIL = "查询套餐列表数据失败";
}
~~~

（2）返回结果Result和PageResult类，放到health_common工程中

~~~java
package com.itheima.entity;
import java.io.Serializable;
/**
 * 封装返回结果
 */
public class Result implements Serializable{
    private boolean flag;//执行结果，true为执行成功 false为执行失败
    private String message;//返回提示信息，主要用于页面提示信息
    private Object data;//返回数据
    public Result(boolean flag, String message) {
        super();
        this.flag = flag;
        this.message = message;
    }
    public Result(boolean flag, String message, Object data) {
        this.flag = flag;
        this.message = message;
        this.data = data;
    }
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}
~~~

~~~java
package com.itheima.entity;
import java.io.Serializable;
import java.util.List;
/**
 * 分页结果封装对象
 */
public class PageResult implements Serializable{
    private Long total;//总记录数
    private List rows;//当前页结果
    public PageResult(Long total, List rows) {
        super();
        this.total = total;
        this.rows = rows;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    }
    public List getRows() {
        return rows;
    }
    public void setRows(List rows) {
        this.rows = rows;
    }
}
~~~

（3）封装查询条件的QueryPageBean类，放到health_common工程中

~~~java
package com.itheima.entity;
import java.io.Serializable;
/**
 * 封装查询条件
 */
public class QueryPageBean implements Serializable{
    private Integer currentPage;//页码
    private Integer pageSize;//每页记录数
    private String queryString;//查询条件
    public Integer getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public String getQueryString() {
        return queryString;
    }
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
~~~

（4）html、js、css、图片等静态资源，放到health_backend工程中

注意：后续随着项目开发还会陆续导入其他一些公共资源。

## 3. 新增检查项

### 3.1 完善页面

检查项管理页面对应的是checkitem.html页面，根据产品设计的原型已经完成了页面基本结构的编写，现在需要完善页面动态效果。

#### 3.1.1 弹出新增窗口

页面中已经提供了新增窗口，只是处于隐藏状态。只需要将控制展示状态的属性dialogFormVisible改为true就可以显示出新增窗口。

新建按钮绑定的方法为handleCreate，所以在handleCreate方法中修改dialogFormVisible属性的值为true即可。同时为了增加用户体验度，需要每次点击新建按钮时清空表单输入项。

~~~javascript
// 重置表单
resetForm() {
	this.formData = {};
},
// 弹出添加窗口
handleCreate() {
	this.resetForm();
	this.dialogFormVisible = true;
}
~~~

#### 3.1.2 输入校验

~~~javascript
rules: {//校验规则
	code: [{ required: true, message: '项目编码为必填项', trigger: 'blur' }],
	name: [{ required: true, message: '项目名称为必填项', trigger: 'blur' }]
}
~~~

#### 3.1.3 提交表单数据

点击新增窗口中的确定按钮时，触发handleAdd方法，所以需要在handleAdd方法中进行完善。

~~~javascript
handleAdd () {
  //校验表单输入项是否合法
  this.$refs['dataAddForm'].validate((valid) => {
    if (valid) {
      //表单数据校验通过，发送ajax请求将表单数据提交到后台
      axios.post("/checkitem/add.do",this.formData).then((response)=> {
        //隐藏新增窗口
        this.dialogFormVisible = false;
        //判断后台返回的flag值，true表示添加操作成功，false为添加操作失败
        if(response.data.flag){
          this.$message({
            message: response.data.message,
            type: 'success'
          });
        }else{
          this.$message.error(response.data.message);
        }
      }).finally(()=> {
        this.findPage();
      });
    } else {
      this.$message.error("表单数据校验失败");
      return false;
    }
  });
}
~~~

### 3.2 后台代码

#### 3.2.1 Controller

在health_backend工程中创建CheckItemController

~~~java
package com.itheima.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * 体检检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    //新增
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }
}
~~~

#### 3.2.2 服务接口

在health_interface工程中创建CheckItemService接口

~~~java
package com.itheima.service;
import com.itheima.pojo.CheckItem;
import java.util.List;
/**
 * 检查项服务接口
 */
public interface CheckItemService {
    public void add(CheckItem checkItem);
}
~~~

#### 3.2.3 服务实现类

在health_service_provider工程中创建CheckItemServiceImpl实现类

~~~java
package com.itheima.service;
import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.CheckItemDao;
import com.itheima.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
/**
 * 检查项服务
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
  	//新增
    public void add(@RequestBody CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
}
~~~

##### 注意点

> - 当添加了事务注解`@Service`必须这么写`@Service(interfaceClass = CheckItemService.class)`，否则报错
> - 前端发送请求发送的是json数据，后端controller用实体类接受需要用`@RequestBody`注解封装成对象

#### 3.2.4 Dao接口

在health_service_provider工程中创建CheckItemDao接口，本项目是基于Mybatis的Mapper代理技术实现持久层操作，故只需要提供接口和Mapper映射文件，无须提供实现类

~~~java
package com.itheima.dao;
import com.itheima.pojo.CheckItem;
/**
 * 持久层Dao接口
 */
public interface CheckItemDao {
    public void add(CheckItem checkItem);
}
~~~

#### 3.2.5 Mapper映射文件

在health_service_provider工程中创建CheckItemDao.xml映射文件，需要和CheckItemDao接口在同一目录下

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.itheima.dao.CheckItemDao">
    <!--新增-->
    <insert id="add" parameterType="com.itheima.pojo.CheckItem">
        insert into t_checkitem(code,name,sex,age,price,type,remark,attention)
                      values 
        (#{code},#{name},#{sex},#{age},#{price},#{type},#{remark},#{attention})
    </insert>
</mapper>
~~~

## 4. 检查项分页

本项目所有分页功能都是基于ajax的异步请求来完成的，请求参数和后台响应数据格式都使用json数据格式。

请求参数包括页码、每页显示记录数、查询条件。

请求参数的json格式为：{currentPage:1,pageSize:10,queryString:''itcast''}

后台响应数据包括总记录数、当前页需要展示的数据集合。

响应数据的json格式为：{total:1000,rows:[]}

如下图：

![6](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/6.png)

### 4.1 完善页面

#### 4.1.1 定义分页相关模型数据

```javascript
pagination: {//分页相关模型数据
  currentPage: 1,//当前页码
  pageSize:10,//每页显示的记录数
  total:0,//总记录数
  queryString:null//查询条件
},
dataList: [],//当前页要展示的分页列表数据
```

#### 4.1.2 定义分页方法

在页面中提供了findPage方法用于分页查询，为了能够在checkitem.html页面加载后直接可以展示分页数据，可以在VUE提供的钩子函数created中调用findPage方法

```javascript
//钩子函数，VUE对象初始化完成后自动执行
created() {
  this.findPage();
}
```

```javascript
//分页查询
findPage() {
  //分页参数
  var param = {
    currentPage:this.pagination.currentPage,//页码
    pageSize:this.pagination.pageSize,//每页显示的记录数
    queryString:this.pagination.queryString//查询条件
  };
  //请求后台
  axios.post("/checkitem/findPage.do",param).then((response)=> {
    //为模型数据赋值，基于VUE的双向绑定展示到页面
    this.dataList = response.data.rows;
    this.pagination.total = response.data.total;
  });
}
```

#### 4.1.3 完善分页方法执行时机

除了在created钩子函数中调用findPage方法查询分页数据之外，当用户点击查询按钮或者点击分页条中的页码时也需要调用findPage方法重新发起查询请求。

为查询按钮绑定单击事件，调用findPage方法

~~~html
<el-button @click="findPage()" class="dalfBut">查询</el-button>
~~~

为分页条组件绑定current-change事件，此事件是分页条组件自己定义的事件，当页码改变时触发，对应的处理函数为handleCurrentChange

~~~html
<el-pagination
               class="pagiantion"
               @current-change="handleCurrentChange"
               :current-page="pagination.currentPage"
               :page-size="pagination.pageSize"
               layout="total, prev, pager, next, jumper"
               :total="pagination.total">
</el-pagination>
~~~

定义handleCurrentChange方法

~~~javascript
//切换页码
handleCurrentChange(currentPage) {
  //currentPage为切换后的页码
  this.pagination.currentPage = currentPage;
  this.findPage();
}
~~~

### 4.2 后台代码

#### 4.2.1 Controller

在CheckItemController中增加分页查询方法

~~~java
//分页查询
@RequestMapping("/findPage")
public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
    PageResult pageResult = checkItemService.pageQuery(
    queryPageBean.getCurrentPage(), 
    queryPageBean.getPageSize(), 
    queryPageBean.getQueryString());
    return pageResult;
}
~~~

#### 4.2.2 服务接口

在CheckItemService服务接口中扩展分页查询方法

~~~java
public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);
~~~

#### 4.2.3 服务实现类

在CheckItemServiceImpl服务实现类中实现分页查询方法，基于Mybatis分页助手插件实现分页

~~~java
public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
  PageHelper.startPage(currentPage,pageSize);
  Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
  return new PageResult(page.getTotal(),page.getResult());
}
~~~

#### 4.2.4 Dao接口

在CheckItemDao接口中扩展分页查询方法

~~~java
public Page<CheckItem> selectByCondition(String queryString);
~~~

#### 4.2.5 Mapper映射文件

在CheckItemDao.xml文件中增加SQL定义

~~~xml
<select id="selectByCondition" parameterType="string" 
        resultType="com.itheima.pojo.CheckItem">
  select * from t_checkitem
  <if test="value != null and value.length > 0">
    where code = #{value} or name = #{value}
  </if>
</select>
~~~

### 注意点

> 使用分页插件时需要的三个参数，当前页，每页多少条数据，查询条件。不管是mybatis还是mybatisplus的分页插件

## 5. 删除检查项

### 5.1 完善页面

为了防止用户误操作，点击删除按钮时需要弹出确认删除的提示，用户点击取消则不做任何操作，用户点击确定按钮再提交删除请求。

#### 5.1.1 绑定单击事件

需要为删除按钮绑定单击事件，并且将当前行数据作为参数传递给处理函数

~~~html
<el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
~~~

~~~javascript
// 删除
handleDelete(row) {
  alert(row.id);
}
~~~

#### 5.1.2 弹出确认操作提示

用户点击删除按钮会执行handleDelete方法，此处需要完善handleDelete方法，弹出确认提示信息。ElementUI提供了$confirm方法来实现确认提示信息弹框效果

~~~javascript
// 删除
handleDelete(row) {
  //alert(row.id);
  this.$confirm("确认删除当前选中记录吗？","提示",{type:'warning'}).then(()=>{
    //点击确定按钮时只需此处代码
    alert('用户点击的是确定按钮');
  });
}
~~~

#### 5.1.3 发送请求

如果用户点击确定按钮就需要发送ajax请求，并且将当前检查项的id作为参数提交到后台进行删除操作

~~~javascript
// 删除
handleDelete(row) {
  //alert(row.id);
  this.$confirm("确认删除吗？","提示",{type:'warning'}).then(()=>{
    //点击确定按钮时只需此处代码
    //alert('用户点击的是确定按钮');
    axios.get("/checkitem/delete.do?id=" + row.id).then((res)=> {
      if(!res.data.flag){
        //删除失败
        this.$message.error(res.data.message);
      }else{
        //删除成功
        this.$message({
          message: res.data.message,
          type: 'success'
        });
        //调用分页，获取最新分页数据
        this.findPage();
      }
    });
  });
}
~~~

### 5.2 后台代码

#### 5.2.1 Controller

在CheckItemController中增加删除方法

~~~java
//删除
@RequestMapping("/delete")
public Result delete(Integer id){
  try {
    checkItemService.delete(id);
  }catch (RuntimeException e){
    return new Result(false,e.getMessage());
  }catch (Exception e){
    return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
  }
  return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
}
~~~

#### 5.2.2 服务接口

在CheckItemService服务接口中扩展删除方法

~~~java
public void delete(Integer id);
~~~

#### 5.2.3 服务实现类

注意：不能直接删除，需要判断当前检查项是否和检查组关联，如果已经和检查组进行了关联则不允许删除

~~~java
//删除
public void delete(Integer id) throws RuntimeException{
  //查询当前检查项是否和检查组关联
  long count = checkItemDao.findCountByCheckItemId(id);
  if(count > 0){
    //当前检查项被引用，不能删除
    throw new RuntimeException("当前检查项被引用，不能删除");
  }
  checkItemDao.deleteById(id);
}
~~~

#### 5.2.4 Dao接口

在CheckItemDao接口中扩展方法findCountByCheckItemId和deleteById

~~~java
public void deleteById(Integer id);
public long findCountByCheckItemId(Integer checkItemId);
~~~

#### 5.2.5 Mapper映射文件

在CheckItemDao.xml中扩展SQL语句

~~~xml
<!--删除-->
<delete id="deleteById" parameterType="int">
  delete from t_checkitem where id = #{id}
</delete>
<!--根据检查项id查询中间关系表-->
<select id="findCountByCheckItemId" resultType="long" parameterType="int">
  select count(*) from t_checkgroup_checkitem where checkitem_id = #{checkitem_id}
</select>
~~~

## 6. 编辑检查项

### 6.1 完善页面

用户点击编辑按钮时，需要弹出编辑窗口并且将当前记录的数据进行回显，用户修改完成后点击确定按钮将修改后的数据提交到后台进行数据库操作。

#### 6.1.1 绑定单击事件

需要为编辑按钮绑定单击事件，并且将当前行数据作为参数传递给处理函数

~~~html
<el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
~~~

~~~javascript
handleUpdate(row) {
  alert(row);
}
~~~

#### 6.1.2 弹出编辑窗口回显数据

当前页面中的编辑窗口已经提供好了，默认处于隐藏状态。在handleUpdate方法中需要将编辑窗口展示出来，并且需要发送ajax请求查询当前检查项数据用于回显

~~~javascript
// 弹出编辑窗口
handleUpdate(row) {
  //发送请求获取检查项信息
  axios.get("/checkitem/findById.do?id=" + row.id).then((res)=>{
    if(res.data.flag){
      //设置编辑窗口属性，dialogFormVisible4Edit为true表示显示
      this.dialogFormVisible4Edit = true;
      //为模型数据设置值，基于VUE双向数据绑定回显到页面
      this.formData = res.data.data;
    }else{
      this.$message.error("获取数据失败，请刷新当前页面");
    }
  });
}
~~~

#### 6.1.3 发送请求

在编辑窗口中修改完成后，点击确定按钮需要提交请求，所以需要为确定按钮绑定事件并提供处理函数handleEdit

~~~html
<el-button type="primary" @click="handleEdit()">确定</el-button>
~~~

~~~javascript
//编辑
handleEdit() {
  //表单校验
  this.$refs['dataEditForm'].validate((valid)=>{
    if(valid){
      //表单校验通过，发送请求
      axios.post("/checkitem/edit.do",this.formData).then((response)=> {
        //隐藏编辑窗口
        this.dialogFormVisible4Edit = false;
        if(response.data.flag){
          //编辑成功，弹出成功提示信息
          this.$message({
            message: response.data.message,
            type: 'success'
          });
        }else{
          //编辑失败，弹出错误提示信息
          this.$message.error(response.data.message);
        }
      }).finally(()=> {
        //重新发送请求查询分页数据
        this.findPage();
      });
    }else{
      //表单校验失败
      this.$message.error("表单数据校验失败");
      return false;
    }
  });
}
~~~

### 6.2 后台代码

#### 6.2.1 Controller

在CheckItemController中增加编辑方法

```java
//编辑
@RequestMapping("/edit")
public Result edit(@RequestBody CheckItem checkItem){
  try {
    checkItemService.edit(checkItem);
  }catch (Exception e){
    return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
  }
  return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
}

@RequestMapping("/findById")
public Result findById(Integer id){
    try{
        CheckItem checkItem = checkItemService.findById(id);
        return  new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }catch (Exception e){
        e.printStackTrace();
        //服务调用失败
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }
}
```

#### 6.2.2 服务接口

在CheckItemService服务接口中扩展编辑方法

```java
public void edit(CheckItem checkItem);
public CheckItem findById(Integer id);
```

#### 6.2.3 服务实现类

在CheckItemServiceImpl实现类中实现编辑方法

```java
//编辑
public void edit(CheckItem checkItem) {
  checkItemDao.edit(checkItem);
}

public CheckItem findById(Integer id) {
    return checkItemDao.findById(id);
}
```

#### 6.2.4 Dao接口

在CheckItemDao接口中扩展edit方法

```java
public void edit(CheckItem checkItem);
public CheckItem findById(Integer id);
```

#### 6.2.5 Mapper映射文件

在CheckItemDao.xml中扩展SQL语句

```xml
<!--编辑-->
<update id="edit" parameterType="com.itheima.pojo.CheckItem">
  update t_checkitem
  <set>
    <if test="name != null">
      name = #{name},
    </if>
    <if test="sex != null">
      sex = #{sex},
    </if>
    <if test="code != null">
      code = #{code},
    </if>
    <if test="age != null">
      age = #{age},
    </if>
    <if test="price != null">
      price = #{price},
    </if>
    <if test="type != null">
      type = #{type},
    </if>
    <if test="attention != null">
      attention = #{attention},
    </if>
    <if test="remark != null">
      remark = #{remark},
    </if>
  </set>
  where id = #{id}
</update>

<select id="findById" parameterType="int" resultType="com.itheima.pojo.CheckItem">
    select * from t_checkitem where id = #{id}
</select>
```

# 第3章 预约管理-检查组管理

## 1. 需求分析

检查组其实就是多个检查项的集合，例如有一个检查组为“一般检查”，这个检查组可以包括多个检查项：身高、体重、收缩压、舒张压等。所以在添加检查组时需要选择这个检查组包括的检查项。

检查组对应的实体类为CheckGroup，对应的数据表为t_checkgroup。检查组和检查项为多对多关系，所以需要中间表t_checkgroup_checkitem进行关联。

## 2. 新增检查组

### 2.1 完善页面

检查组管理页面对应的是checkgroup.html页面，根据产品设计的原型已经完成了页面基本结构的编写，现在需要完善页面动态效果。

#### 2.1.1 弹出新增窗口

页面中已经提供了新增窗口，只是出于隐藏状态。只需要将控制展示状态的属性dialogFormVisible改为true即可显示出新增窗口。点击新建按钮时绑定的方法为handleCreate，所以在handleCreate方法中修改dialogFormVisible属性的值为true即可。同时为了增加用户体验度，需要每次点击新建按钮时清空表单输入项。

由于新增检查组时还需要选择此检查组包含的检查项，所以新增检查组窗口分为两部分信息：基本信息和检查项信息，如下图：

![1](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/1-1667456461878.png)

![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2-1667456461879.png)

新建按钮绑定单击事件，对应的处理函数为handleCreate

~~~html
<el-button type="primary" class="butT" @click="handleCreate()">新建</el-button>
~~~

~~~javascript
// 重置表单
resetForm() {
	this.formData = {};
},
// 弹出添加窗口
handleCreate() {
	this.resetForm();
	this.dialogFormVisible = true;
}
~~~

#### 2.1.2 动态展示检查项列表

现在虽然已经完成了新增窗口的弹出，但是在检查项信息标签页中需要动态展示所有的检查项信息列表数据，并且可以进行勾选。具体操作步骤如下：

（1）定义模型数据

~~~javascript
tableData:[],//新增和编辑表单中对应的检查项列表数据
checkitemIds:[],//新增和编辑表单中检查项对应的复选框，基于双向绑定可以进行回显和数据提交
~~~

（2）动态展示检查项列表数据，数据来源于上面定义的tableData模型数据

~~~html
<table class="datatable">
  <thead>
    <tr>
      <th>选择</th>
      <th>项目编码</th>
      <th>项目名称</th>
      <th>项目说明</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="c in tableData">
      <td>
        <input :id="c.id" v-model="checkitemIds" type="checkbox" :value="c.id">
      </td>
      <td><label :for="c.id">{{c.code}}</label></td>
      <td><label :for="c.id">{{c.name}}</label></td>
      <td><label :for="c.id">{{c.remark}}</label></td>
    </tr>
  </tbody>
</table>
~~~

（3）完善handleCreate方法，发送ajax请求查询所有检查项数据并将结果赋值给tableData模型数据用于页面表格展示

~~~javascript
// 弹出添加窗口
handleCreate() {
  this.dialogFormVisible = true;
  this.resetForm();
  //默认切换到第一个标签页（基本信息）
  this.activeName='first';
  //重置
  this.checkitemIds = [];
  //发送ajax请求查询所有检查项信息
  axios.get("/checkitem/findAll.do").then((res)=> {
    if(res.data.flag){
      //将检查项列表数据赋值给模型数据用于页面表格展示
      this.tableData = res.data.data;
    }else{
      this.$message.error(res.data.message);
    }
  });
}
~~~

（4）分别在CheckItemController、CheckItemService、CheckItemServiceImpl、CheckItemDao、CheckItemDao.xml中扩展方法查询所有检查项数据

CheckItemController：

~~~java
//查询所有
@RequestMapping("/findAll")
public Result findAll(){
  List<CheckItem> checkItemList = checkItemService.findAll();
  if(checkItemList != null && checkItemList.size() > 0){
    Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS);
    result.setData(checkItemList);
    return result;
  }
  return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
}
~~~

CheckItemService：

~~~java
public List<CheckItem> findAll();
~~~

CheckItemServiceImpl：

~~~java
public List<CheckItem> findAll() {
  return checkItemDao.findAll();
}
~~~

CheckItemDao：

~~~java
public List<CheckItem> findAll();
~~~

CheckItemDao.xml：

~~~xml
<select id="findAll" resultType="com.itheima.pojo.CheckItem">
  select * from t_checkitem
</select>
~~~

#### 2.1.3 提交请求

当用户点击新增窗口中的确定按钮时发送ajax请求将数据提交到后台进行数据库操作。提交到后台的数据分为两部分：检查组基本信息（对应的模型数据为formData）和检查项id数组（对应的模型数据为checkitemIds）。

为确定按钮绑定单击事件，对应的处理函数为handleAdd

~~~html
<el-button type="primary" @click="handleAdd()">确定</el-button>
~~~

完善handleAdd方法

~~~javascript
//添加
handleAdd () {
  //发送ajax请求将模型数据提交到后台处理
  axios.post(
    		"/checkgroup/add.do?checkitemIds=" + this.checkitemIds,
    		this.formData
  			)
    .then((response)=> {
      //关闭新增窗口
      this.dialogFormVisible = false;
      if(response.data.flag){
        //新增成功，弹出成功提示
        this.$message({
          message: response.data.message,
          type: 'success'
        });
      }else{
        //新增失败，弹出错误提示
        this.$message.error(response.data.message);
      }
  }).finally(()=> {
    this.findPage();
  });
}
~~~

### 2.2 后台代码

#### 2.2.1 Controller

在health_backend工程中创建CheckGroupController

~~~java
package com.itheima.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * 检查组管理
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    //新增
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
        }catch (Exception e){
            //新增失败
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //新增成功
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
}
~~~

#### 2.2.2 服务接口

在health_interface工程中创建CheckGroupService接口

~~~java
package com.itheima.service;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import java.util.List;
/**
 * 检查组服务接口
 */
public interface CheckGroupService {
    void add(CheckGroup checkGroup,Integer[] checkitemIds);
}
~~~

#### 2.2.3 服务实现类

在health_service_provider工程中创建CheckGroupServiceImpl实现类

~~~java
package com.itheima.service;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 检查组服务
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;
    
    //添加检查组合，同时需要设置检查组合和检查项的关联关系
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }
  	//设置检查组合和检查项的关联关系
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
          if(checkitemIds != null && checkitemIds.length > 0){
              for (Integer checkitemId : checkitemIds) {
                  Map<String,Integer> map = new HashMap<>();
                  map.put("checkgroup_id",checkGroupId);
                  map.put("checkitem_id",checkitemId);
                  checkGroupDao.setCheckGroupAndCheckItem(map);
              }
          }
      }
}
~~~

#### 2.2.4 Dao接口

创建CheckGroupDao接口

~~~java
package com.itheima.dao;
import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import java.util.List;
import java.util.Map;
/**
 * 持久层Dao接口
 */
public interface CheckGroupDao {
    void add(CheckGroup checkGroup);
    void setCheckGroupAndCheckItem(Map map);
}
~~~

#### 2.2.5 Mapper映射文件

创建CheckGroupDao.xml映射文件

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.itheima.dao.CheckGroupDao">
    <!--新增-->
    <insert id="add" parameterType="com.itheima.pojo.CheckGroup">
        <selectKey resultType="java.lang.Integer" order="AFTER" keyProperty="id">
            SELECT LAST_INSERT_ID()
        </selectKey>
        insert into t_checkgroup(code,name,sex,helpCode,remark,attention)
            values 
      	(#{code},#{name},#{sex},#{helpCode},#{remark},#{attention})
    </insert>
	<!--设置检查组和检查项的关联关系-->
    <insert id="setCheckGroupAndCheckItem" parameterType="hashmap">
        insert into t_checkgroup_checkitem(checkgroup_id,checkitem_id) 
      		values
      	(#{checkgroup_id},#{checkitem_id})
    </insert>
</mapper>
~~~

## 3. 检查组分页

### 3.1 完善页面

#### 3.1.1 定义分页相关模型数据

```javascript
pagination: {//分页相关模型数据
  currentPage: 1,//当前页码
  pageSize:10,//每页显示的记录数
  total:0,//总记录数
  queryString:null//查询条件
},
dataList: [],//当前页要展示的分页列表数据
```

#### 3.1.2 定义分页方法

在页面中提供了findPage方法用于分页查询，为了能够在checkgroup.html页面加载后直接可以展示分页数据，可以在VUE提供的钩子函数created中调用findPage方法

```javascript
//钩子函数，VUE对象初始化完成后自动执行
created() {
  this.findPage();
}
```

```javascript
//分页查询
findPage() {
  //分页参数
  var param = {
    currentPage:this.pagination.currentPage,//页码
    pageSize:this.pagination.pageSize,//每页显示的记录数
    queryString:this.pagination.queryString//查询条件
  };
  //请求后台
  axios.post("/checkgroup/findPage.do",param).then((response)=> {
    //为模型数据赋值，基于VUE的双向绑定展示到页面
    this.dataList = response.data.rows;
    this.pagination.total = response.data.total;
  });
}
```

#### 3.1.3 完善分页方法执行时机

除了在created钩子函数中调用findPage方法查询分页数据之外，当用户点击查询按钮或者点击分页条中的页码时也需要调用findPage方法重新发起查询请求。

为查询按钮绑定单击事件，调用findPage方法

```html
<el-button @click="findPage()" class="dalfBut">查询</el-button>
```

为分页条组件绑定current-change事件，此事件是分页条组件自己定义的事件，当页码改变时触发，对应的处理函数为handleCurrentChange

```html
<el-pagination
               class="pagiantion"
               @current-change="handleCurrentChange"
               :current-page="pagination.currentPage"
               :page-size="pagination.pageSize"
               layout="total, prev, pager, next, jumper"
               :total="pagination.total">
</el-pagination>
```

定义handleCurrentChange方法

```javascript
//切换页码
handleCurrentChange(currentPage) {
  //currentPage为切换后的页码
  this.pagination.currentPage = currentPage;
  this.findPage();
}
```

### 3.2 后台代码

#### 3.2.1 Controller

在CheckGroupController中增加分页查询方法

```java
//分页查询
@RequestMapping("/findPage")
public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
  PageResult pageResult = checkGroupService.pageQuery(
    queryPageBean.getCurrentPage(), 
    queryPageBean.getPageSize(), 
    queryPageBean.getQueryString()
  );
  return pageResult;
}
```

#### 3.2.2 服务接口

在CheckGroupService服务接口中扩展分页查询方法

```java
public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);
```

#### 3.2.3 服务实现类

在CheckGroupServiceImpl服务实现类中实现分页查询方法，基于Mybatis分页助手插件实现分页

```java
public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
  PageHelper.startPage(currentPage,pageSize);
  Page<CheckItem> page = checkGroupDao.selectByCondition(queryString);
  return new PageResult(page.getTotal(),page.getResult());
}
```

#### 3.2.4 Dao接口

在CheckGroupDao接口中扩展分页查询方法

```java
public Page<CheckGroup> selectByCondition(String queryString);
```

#### 3.2.5 Mapper映射文件

在CheckGroupDao.xml文件中增加SQL定义

```xml
<select id="selectByCondition" parameterType="string" resultType="com.itheima.pojo.CheckGroup">
  select * from t_checkgroup
  <if test="value != null and value.length > 0">
    where code = #{value} or name = #{value} or helpCode = #{value}
  </if>
</select>
```

## 4. 编辑检查组

### 4.1 完善页面

用户点击编辑按钮时，需要弹出编辑窗口并且将当前记录的数据进行回显，用户修改完成后点击确定按钮将修改后的数据提交到后台进行数据库操作。此处进行数据回显的时候，除了需要检查组基本信息的回显之外，还需要回显当前检查组包含的检查项（以复选框勾选的形式回显）。

#### 4.1.1 绑定单击事件

需要为编辑按钮绑定单击事件，并且将当前行数据作为参数传递给处理函数

```html
<el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
```

```javascript
handleUpdate(row) {
  alert(row);
}
```

#### 4.1.2 弹出编辑窗口回显数据

当前页面的编辑窗口已经提供好了，默认处于隐藏状态。在handleUpdate方法中需要将编辑窗口展示出来，并且需要发送多个ajax请求分别查询当前检查组数据、所有检查项数据、当前检查组包含的检查项id用于基本数据回显

```javascript
handleUpdate(row) {
  //发送ajax请求根据id查询检查组信息，用于基本信息回显
  axios.get("/checkgroup/findById.do?id=" + row.id).then((res)=>{
    if(res.data.flag){
      //弹出编辑窗口
      this.dialogFormVisible4Edit = true;
      //默认选中第一个标签页
      this.activeName='first';
      //为模型数据赋值，通过VUE数据双向绑定进行信息的回显
      this.formData = res.data.data;
      //发送ajax请求查询所有的检查项信息，用于检查项表格展示
      axios.get("/checkitem/findAll.do").then((res)=> {
        if(res.data.flag){
          //为模型数据赋值，通过VUE数据双向绑定进行信息的回显
          this.tableData = res.data.data;
          //查询当前检查组包含的所有检查项id，用于页面回显
          axios.get("/checkgroup/findCheckItemIdsByCheckGroupId.do?id=" + row.id).then((res)=> {
            //为模型数据赋值，通过VUE数据双向绑定进行信息的回显
            if(res.data.flag){
                this.checkitemIds = res.data.data;
            }else{
                this.$message.error(res.data.message);
            }
          });
        }else{
          this.$message.error(res.data.message);
        }
      });
    }else{
      this.$message.error("获取数据失败，请刷新当前页面");
    }
  });
}
```

#### 4.1.3 发送请求

在编辑窗口中修改完成后，点击确定按钮需要提交请求，所以需要为确定按钮绑定事件并提供处理函数handleEdit

```html
<el-button type="primary" @click="handleEdit()">确定</el-button>
```

```javascript
//编辑
handleEdit() {
  //发送ajax请求，提交模型数据
  axios.post("/checkgroup/edit.do?checkitemIds="+this.checkitemIds,this.formData).
  then((response)=> {
    //隐藏编辑窗口
    this.dialogFormVisible4Edit = false;
    if(response.data.flag){
      this.$message({
        message: response.data.message,
        type: 'success'
      });
    }else{
      this.$message.error(response.data.message);
    }
  }).finally(()=> {
    this.findPage();
  });
}
```

### 4.2 后台代码

#### 4.2.1 Controller

在CheckGroupController中增加方法

```java
//根据id查询
@RequestMapping("/findById")
public Result findById(Integer id){
  CheckGroup checkGroup = checkGroupService.findById(id);
  if(checkGroup != null){
    Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
    result.setData(checkGroup);
    return result;
  }
  return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
}

//根据检查组合id查询对应的所有检查项id
@RequestMapping("/findCheckItemIdsByCheckGroupId")
public Result findCheckItemIdsByCheckGroupId(Integer id){
    try{
        List<Integer> checkitemIds = 
            checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkitemIds);
    }catch (Exception e){
        e.printStackTrace();
        return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
    }
}

//编辑
@RequestMapping("/edit")
public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
  try {
    checkGroupService.edit(checkGroup,checkitemIds);
  }catch (Exception e){
    return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
  }
  return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
}
```

#### 4.2.2 服务接口

在CheckGroupService服务接口中扩展方法

```java
CheckGroup findById(Integer id);
List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
public void edit(CheckGroup checkGroup,Integer[] checkitemIds);
```

#### 4.2.3 服务实现类

在CheckGroupServiceImpl实现类中实现编辑方法

```java
public CheckGroup findById(Integer id) {
  return checkGroupDao.findById(id);
}

public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
  return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
}

//编辑检查组，同时需要更新和检查项的关联关系
public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
  //根据检查组id删除中间表数据（清理原有关联关系）
  checkGroupDao.deleteAssociation(checkGroup.getId());
  //向中间表(t_checkgroup_checkitem)插入数据（建立检查组和检查项关联关系）
  setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
  //更新检查组基本信息
  checkGroupDao.edit(checkGroup);
}

//向中间表(t_checkgroup_checkitem)插入数据（建立检查组和检查项关联关系）
public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
  if(checkitemIds != null && checkitemIds.length > 0){
    for (Integer checkitemId : checkitemIds) {
      Map<String,Integer> map = new HashMap<>();
      map.put("checkgroup_id",checkGroupId);
      map.put("checkitem_id",checkitemId);
      checkGroupDao.setCheckGroupAndCheckItem(map);
    }
  }
}
```

#### 4.2.4 Dao接口

在CheckGroupDao接口中扩展方法

```java
CheckGroup findById(Integer id);
List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
void setCheckGroupAndCheckItem(Map map);
void deleteAssociation(Integer id);
void edit(CheckGroup checkGroup);
```

#### 4.2.5 Mapper映射文件

在CheckGroupDao.xml中扩展SQL语句

~~~xml
<select id="findById" parameterType="int" resultType="com.itheima.pojo.CheckGroup">
  select * from t_checkgroup where id = #{id}
</select>
<select id="findCheckItemIdsByCheckGroupId" parameterType="int" resultType="int">
  select checkitem_id from t_checkgroup_checkitem where checkgroup_id = #{id}
</select>
<!--向中间表插入数据（建立检查组和检查项关联关系）-->
<insert id="setCheckGroupAndCheckItem" parameterType="hashmap">
  insert into t_checkgroup_checkitem(checkgroup_id,checkitem_id) 
  	values
  (#{checkgroup_id},#{checkitem_id})
</insert>
<!--根据检查组id删除中间表数据（清理原有关联关系）-->
<delete id="deleteAssociation" parameterType="int">
  delete from t_checkgroup_checkitem where checkgroup_id = #{id}
</delete>
<!--编辑-->
<update id="edit" parameterType="com.itheima.pojo.CheckGroup">
  update t_checkgroup
  <set>
    <if test="name != null">
      name = #{name},
    </if>
    <if test="sex != null">
      sex = #{sex},
    </if>
    <if test="code != null">
      code = #{code},
    </if>
    <if test="helpCode != null">
      helpCode = #{helpCode},
    </if>
    <if test="attention != null">
      attention = #{attention},
    </if>
    <if test="remark != null">
      remark = #{remark},
    </if>
  </set>
  where id = #{id}
</update>
~~~

#### 注意点

> 编辑页面的检查项可能会勾选，也有可能取消勾选，后端操作起来比较麻烦，可以直接清除所有的检查项，然后再根据检查组中的检查项添加

# 第4章 预约管理-套餐管理

## 1. 图片存储方案

### 1.1 介绍

在实际开发中，我们会有很多处理不同功能的服务器。例如：  

应用服务器：负责部署我们的应用  

数据库服务器：运行我们的数据库  

文件服务器：负责存储用户上传文件的服务器

![5](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/5-1667479080524.png)

分服务器处理的目的是让服务器各司其职，从而提高我们项目的运行效率。 

常见的图片存储方案：

方案一：使用nginx搭建图片服务器

方案二：使用开源的分布式文件存储系统，例如Fastdfs、HDFS等

方案三：使用云存储，例如阿里云、七牛云等

### 1.2 七牛云存储

七牛云（隶属于上海七牛信息技术有限公司）是国内领先的以视觉智能和数据智能为核心的企业级云计算服务商，同时也是国内知名智能视频云服务商，累计为 70 多万家企业提供服务，覆盖了国内80%网民。围绕富媒体场景推出了对象存储、融合 CDN 加速、容器云、大数据平台、深度学习平台等产品、并提供一站式智能视频云解决方案。为各行业及应用提供可持续发展的智能视频云生态，帮助企业快速上云，创造更广阔的商业价值。

官网：https://www.qiniu.com/

通过七牛云官网介绍我们可以知道其提供了多种服务，我们主要使用的是七牛云提供的对象存储服务来存储图片。

#### 1.2.1 注册、登录

要使用七牛云的服务，首先需要注册成为会员。地址：https://portal.qiniu.com/signup

![1](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/1-1667479080525.png)

注册完成后就可以使用刚刚注册的邮箱和密码登录到七牛云：

![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2-1667479080525.png)

登录成功后点击页面右上角管理控制台：

![8](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/8.png)



注意：登录成功后还需要进行实名认证才能进行相关操作。

#### 1.2.2 新建存储空间

要进行图片存储，我们需要在七牛云管理控制台新建存储空间。点击管理控制台首页对象存储下的立即添加按钮，页面跳转到新建存储空间页面：

![9](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/9.png)

可以创建多个存储空间，各个存储空间是相互独立的。

#### 1.2.3 查看存储空间信息

存储空间创建后，会在左侧的存储空间列表菜单中展示创建的存储空间名称，点击存储空间名称可以查看当前存储空间的相关信息

![10](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day04/%25E8%25AE%25B2%25E4%25B9%2589/10.png)

#### 1.2.4 开发者中心

可以通过七牛云提供的开发者中心学习如何操作七牛云服务，地址：https://developer.qiniu.com/

![11](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/11-1667479080525.png)

点击对象存储，跳转到对象存储开发页面，地址：https://developer.qiniu.com/kodo

![12](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/12-1667479080526.png)



七牛云提供了多种方式操作对象存储服务，本项目采用Java SDK方式，地址：https://developer.qiniu.com/kodo/sdk/1239/java

![13](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/13-1667479080526.png)

使用Java SDK操作七牛云需要导入如下maven坐标：

~~~xml
<dependency>
  <groupId>com.qiniu</groupId>
  <artifactId>qiniu-java-sdk</artifactId>
  <version>7.2.0</version>
</dependency>
~~~

#### 1.2.5 鉴权

Java SDK的所有的功能，都需要合法的授权。授权凭证的签算需要七牛账号下的一对有效的Access Key和Secret Key，这对密钥可以在七牛云管理控制台的个人中心（https://portal.qiniu.com/user/key）获得，如下图：

![14](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/14-1667479080526.png)

#### 1.2.6 Java SDK操作七牛云

本章节我们就需要使用七牛云提供的Java SDK完成图片上传和删除，我们可以参考官方提供的例子。

~~~java
//构造一个带指定Zone对象的配置类
Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释
UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
String accessKey = "your access key";
String secretKey = "your secret key";
String bucket = "your bucket name";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
String localFilePath = "/home/qiniu/test.png";
//默认不指定key的情况下，以文件内容的hash值作为文件名
String key = null;
Auth auth = Auth.create(accessKey, secretKey);
String upToken = auth.uploadToken(bucket);
try {
    Response response = uploadManager.put(localFilePath, key, upToken);
    //解析上传成功的结果
    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
    System.out.println(putRet.key);
    System.out.println(putRet.hash);
} catch (QiniuException ex) {
    Response r = ex.response;
    System.err.println(r.toString());
    try {
        System.err.println(r.bodyString());
    } catch (QiniuException ex2) {
        //ignore
    }
}
~~~

~~~java
//构造一个带指定Zone对象的配置类
Configuration cfg = new Configuration(Zone.zone0());
//...其他参数参考类注释

String accessKey = "your access key";
String secretKey = "your secret key";

String bucket = "your bucket name";
String key = "your file key";

Auth auth = Auth.create(accessKey, secretKey);
BucketManager bucketManager = new BucketManager(auth, cfg);
try {
    bucketManager.delete(bucket, key);
} catch (QiniuException ex) {
    //如果遇到异常，说明删除失败
    System.err.println(ex.code());
    System.err.println(ex.response.toString());
}
~~~

#### 1.2.7 封装工具类

为了方便操作七牛云存储服务，我们可以将官方提供的案例简单改造成一个工具类，在我们的项目中直接使用此工具类来操作就可以：

~~~java
package com.itheima.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 七牛云工具类
 */
public class QiniuUtils {
    public  static String accessKey = "dulF9Wze9bxujtuRvu3yyYb9JX1Sp23jzd3tO708";
    public  static String secretKey = "vZkhW7iot3uWwcWz9vXfbaP4JepdWADFDHVLMZOe";
    public  static String bucket = "qiniutest";

    public static void upload2Qiniu(String filePath,String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(filePath, fileName, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = 
              new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    //上传文件
    public static void upload2Qiniu(byte[] bytes, String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(bytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = 
              new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    //删除文件
    public static void deleteFileFromQiniu(String fileName){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        String key = fileName;
        Auth auth = Auth.create(accessKey, secretKey);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
    }
}

~~~

将此工具类放在health_common工程中，后续会使用到。

## 2. 新增套餐

### 2.1 需求分析

套餐其实就是检查组的集合，例如有一个套餐为“入职体检套餐”，这个体检套餐可以包括多个检查组：一般检查、血常规、尿常规、肝功三项等。所以在添加套餐时需要选择这个套餐包括的检查组。

套餐对应的实体类为Setmeal，对应的数据表为t_setmeal。套餐和检查组为多对多关系，所以需要中间表t_setmeal_checkgroup进行关联。

### 2.2 完善页面

套餐管理页面对应的是setmeal.html页面，根据产品设计的原型已经完成了页面基本结构的编写，现在需要完善页面动态效果。

#### 2.2.1 弹出新增窗口

页面中已经提供了新增窗口，只是出于隐藏状态。只需要将控制展示状态的属性dialogFormVisible改为true接口显示出新增窗口。点击新建按钮时绑定的方法为handleCreate，所以在handleCreate方法中修改dialogFormVisible属性的值为true即可。同时为了增加用户体验度，需要每次点击新建按钮时清空表单输入项。

由于新增套餐时还需要选择此套餐包含的检查组，所以新增套餐窗口分为两部分信息：基本信息和检查组信息，如下图：

![3](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/3-1667479080526.png)

![4](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/4.png)

新建按钮绑定单击事件，对应的处理函数为handleCreate

```html
<el-button type="primary" class="butT" @click="handleCreate()">新建</el-button>
```

```javascript
// 重置表单
resetForm() {
  this.formData = {};
  this.activeName='first';
  this.checkgroupIds = [];
  this.imageUrl = null;
}
// 弹出添加窗口
handleCreate() {
  this.dialogFormVisible = true;
  this.resetForm();
}
```

#### 2.2.2 动态展示检查组列表

现在虽然已经完成了新增窗口的弹出，但是在检查组信息标签页中需要动态展示所有的检查组信息列表数据，并且可以进行勾选。具体操作步骤如下：

（1）定义模型数据

```javascript
tableData:[],//添加表单窗口中检查组列表数据
checkgroupIds:[],//添加表单窗口中检查组复选框对应id
```

（2）动态展示检查组列表数据，数据来源于上面定义的tableData模型数据

```html
<table class="datatable">
  <thead>
    <tr>
      <th>选择</th>
      <th>项目编码</th>
      <th>项目名称</th>
      <th>项目说明</th>
    </tr>
  </thead>
  <tbody>
    <tr v-for="c in tableData">
      <td>
        <input :id="c.id" v-model="checkgroupIds" type="checkbox" :value="c.id">
      </td>
      <td><label :for="c.id">{{c.code}}</label></td>
      <td><label :for="c.id">{{c.name}}</label></td>
      <td><label :for="c.id">{{c.remark}}</label></td>
    </tr>
  </tbody>
</table>
```

（3）完善handleCreate方法，发送ajax请求查询所有检查组数据并将结果赋值给tableData模型数据用于页面表格展示

```javascript
// 弹出添加窗口
handleCreate() {
  this.dialogFormVisible = true;
  this.resetForm();
  axios.get("/checkgroup/findAll.do").then((res)=> {
    if(res.data.flag){
      this.tableData = res.data.data;
    }else{
      this.$message.error(res.data.message);
    }
  });
}
```

（4）分别在CheckGroupController、CheckGroupService、CheckGroupServiceImpl、CheckGroupDao、CheckGroupDao.xml中扩展方法查询所有检查组数据

CheckGroupController：

```java
//查询所有
@RequestMapping("/findAll")
public Result findAll(){
  List<CheckGroup> checkGroupList = checkGroupService.findAll();
  if(checkGroupList != null && checkGroupList.size() > 0){
    Result result = new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
    result.setData(checkGroupList);
    return result;
  }
  return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
}
```

CheckGroupService：

```java
List<CheckGroup> findAll();
```

CheckGroupServiceImpl：

```java
public List<CheckGroup> findAll() {
  return checkGroupDao.findAll();
}
```

CheckGroupDao：

```java
List<CheckGroup> findAll();
```

CheckGroupDao.xml：

```xml
<select id="findAll" resultType="com.itheima.pojo.CheckGroup">
  select * from t_checkgroup
</select>
```

#### 2.2.3 图片上传并预览

此处使用的是ElementUI提供的上传组件el-upload，提供了多种不同的上传效果，上传成功后可以进行预览。

实现步骤：

（1）定义模型数据，用于后面上传文件的图片预览：

```javascript
imageUrl:null,//模型数据，用于上传图片完成后图片预览
```

（2）定义上传组件：

```html
<!--
  el-upload：上传组件
  action：上传的提交地址
  auto-upload：选中文件后是否自动上传
  name：上传文件的名称，服务端可以根据名称获得上传的文件对象
  show-file-list：是否显示已上传文件列表
  on-success：文件上传成功时的钩子
  before-upload：上传文件之前的钩子
-->
<el-upload
           class="avatar-uploader"
           action="/setmeal/upload.do"
           :auto-upload="autoUpload"
           name="imgFile"
           :show-file-list="false"
           :on-success="handleAvatarSuccess"
           :before-upload="beforeAvatarUpload">
  <!--用于上传图片预览-->
  <img v-if="imageUrl" :src="imageUrl" class="avatar">
  <!--用于展示上传图标-->
  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
</el-upload>
```

（3）定义对应的钩子函数：

```javascript
//文件上传成功后的钩子，response为服务端返回的值，file为当前上传的文件封装成的js对象
handleAvatarSuccess(response, file) {
  this.imageUrl = "http://pqjroc654.bkt.clouddn.com/"+response.data;
  this.$message({
    message: response.message,
    type: response.flag ? 'success' : 'error'
  });
  //设置模型数据（图片名称），后续提交ajax请求时会提交到后台最终保存到数据库
  this.formData.img = response.data;
}

//上传文件之前的钩子
beforeAvatarUpload(file) {
  const isJPG = file.type === 'image/jpeg';
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isJPG) {
    this.$message.error('上传套餐图片只能是 JPG 格式!');
  }
  if (!isLt2M) {
    this.$message.error('上传套餐图片大小不能超过 2MB!');
  }
  return isJPG && isLt2M;
}
```

（4）创建SetmealController，接收上传的文件

```java
package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.UUID;
/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
  
  	//图片上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
        try{
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix = originalFilename.substring(lastIndexOf - 1);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //图片上传成功
            Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);
            result.setData(fileName);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            //图片上传失败
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
```

注意：别忘了在spring配置文件中配置文件上传组件

```xml
<!--文件上传组件-->
<bean id="multipartResolver" 
      class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
  <property name="maxUploadSize" value="104857600" />
  <property name="maxInMemorySize" value="4096" />
  <property name="defaultEncoding" value="UTF-8"/>
</bean>
```

#### 2.2.4 提交请求

当用户点击新增窗口中的确定按钮时发送ajax请求将数据提交到后台进行数据库操作。提交到后台的数据分为两部分：套餐基本信息（对应的模型数据为formData）和检查组id数组（对应的模型数据为checkgroupIds）。

为确定按钮绑定单击事件，对应的处理函数为handleAdd

```html
<el-button type="primary" @click="handleAdd()">确定</el-button>
```

完善handleAdd方法

```javascript
//添加
handleAdd () {
  axios.post("/setmeal/add.do?checkgroupIds=" + this.checkgroupIds,this.formData).
  then((response)=> {
    this.dialogFormVisible = false;
    if(response.data.flag){
      this.$message({
        message: response.data.message,
        type: 'success'
      });
    }else{
      this.$message.error(response.data.message);
    }
  }).finally(()=> {
    this.findPage();
  });
}
```

### 2.3 后台代码

#### 2.3.1 Controller

在SetmealController中增加方法

```java
//新增
@RequestMapping("/add")
public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
  try {
    setmealService.add(setmeal,checkgroupIds);
  }catch (Exception e){
    //新增套餐失败
    return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
  }
  //新增套餐成功
  return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
}
```

#### 2.3.2 服务接口

创建SetmealService接口并提供新增方法

```java
package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import java.util.List;
/**
 * 体检套餐服务接口
 */
public interface SetmealService {
    public void add(Setmeal setmeal, Integer[] checkgroupIds);
}
```

#### 2.3.3 服务实现类

创建SetmealServiceImpl服务实现类并实现新增方法

```java
package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 体检套餐服务实现类
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;

    //新增套餐
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        if(checkgroupIds != null && checkgroupIds.length > 0){
            //绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        }
    }
    //绑定套餐和检查组的多对多关系
    private void setSetmealAndCheckGroup(Integer id, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmeal_id",id);
            map.put("checkgroup_id",checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }
}
```

#### 2.3.4 Dao接口

创建SetmealDao接口并提供相关方法

```java
package com.itheima.dao;

import com.itheima.pojo.Setmeal;
import java.util.Map;

public interface SetmealDao {
    public void add(Setmeal setmeal);
    public void setSetmealAndCheckGroup(Map<String, Integer> map);
}
```

#### 2.3.5 Mapper映射文件

创建SetmealDao.xml文件并定义相关SQL语句

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.itheima.dao.SetmealDao" >
    <!--新增-->
    <insert id="add" parameterType="com.itheima.pojo.Setmeal">
        <selectKey resultType="java.lang.Integer" order="AFTER" keyProperty="id">
            SELECT LAST_INSERT_ID()
        </selectKey>
        insert into t_setmeal
      		(code,name,sex,age,helpCode,price,remark,attention,img)
        		values 
      		(#{code},#{name},#{sex},#{age},#{helpCode},#{price},#{remark},#{attention},#{img})
    </insert>
    <!--绑定套餐和检查组多对多关系-->
    <insert id="setSetmealAndCheckGroup" parameterType="hashmap">
        insert into t_setmeal_checkgroup
      		(setmeal_id,checkgroup_id) 
      			values
      		(#{setmeal_id},#{checkgroup_id})
    </insert>
</mapper>
```

### 2.4 完善文件上传

前面我们已经完成了文件上传，将图片存储在了七牛云服务器中。但是这个过程存在一个问题，就是如果用户只上传了图片而没有最终保存套餐信息到我们的数据库，这时我们上传的图片就变为了垃圾图片。对于这些垃圾图片我们需要定时清理来释放磁盘空间。这就需要我们能够区分出来哪些是垃圾图片，哪些不是垃圾图片。如何实现呢？

方案就是利用redis来保存图片名称，具体做法为：

1、当用户上传图片后，将图片名称保存到redis的一个Set集合中，例如集合名称为setmealPicResources

2、当用户添加套餐后，将图片名称保存到redis的另一个Set集合中，例如集合名称为setmealPicDbResources

3、计算setmealPicResources集合与setmealPicDbResources集合的差值，结果就是垃圾图片的名称集合，清理这些图片即可

本小节我们先来完成前面2个环节，第3个环节（清理图片环节）在后面会通过定时任务再实现。

实现步骤：

（1）在health_backend项目中提供Spring配置文件spring-redis.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
                         http://www.springframework.org/schema/beans/spring-beans.xsd
        				http://www.springframework.org/schema/mvc 
                         http://www.springframework.org/schema/mvc/spring-mvc.xsd
        				http://code.alibabatech.com/schema/dubbo 
                         http://code.alibabatech.com/schema/dubbo/dubbo.xsd
        				http://www.springframework.org/schema/context 
                         http://www.springframework.org/schema/context/spring-context.xsd">

	<!--Jedis连接池的相关配置-->
	<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
		<property name="maxTotal">
			<value>200</value>
		</property>
		<property name="maxIdle">
			<value>50</value>
		</property>
		<property name="testOnBorrow" value="true"/>
		<property name="testOnReturn" value="true"/>
	</bean>
	<bean id="jedisPool" class="redis.clients.jedis.JedisPool">
		<constructor-arg name="poolConfig" ref="jedisPoolConfig" />
		<constructor-arg name="host" value="127.0.0.1" />
		<constructor-arg name="port" value="6379" type="int" />
		<constructor-arg name="timeout" value="30000" type="int" />
	</bean>
</beans>
~~~

（2）在health_common工程中提供Redis常量类

~~~java
package com.itheima.constant;

public class RedisConstant {
    //套餐图片所有图片名称
    public static final String SETMEAL_PIC_RESOURCES = "setmealPicResources";
    //套餐图片保存在数据库中的图片名称
    public static final String SETMEAL_PIC_DB_RESOURCES = "setmealPicDbResources";
}
~~~

（3）完善SetmealController，在文件上传成功后将图片名称保存到redis集合中

~~~java
@Autowired
private JedisPool jedisPool;
//图片上传
@RequestMapping("/upload")
public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
  try{
    //获取原始文件名
    String originalFilename = imgFile.getOriginalFilename();
    int lastIndexOf = originalFilename.lastIndexOf(".");
    //获取文件后缀
    String suffix = originalFilename.substring(lastIndexOf - 1);
    //使用UUID随机产生文件名称，防止同名文件覆盖
    String fileName = UUID.randomUUID().toString() + suffix;
    QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
    //图片上传成功
    Result result = new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS);
    result.setData(fileName);
    //将上传图片名称存入Redis，基于Redis的Set集合存储
    jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
    return result;
  }catch (Exception e){
    e.printStackTrace();
    //图片上传失败
    return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
  }
}
~~~

（4）在health_service_provider项目中提供Spring配置文件applicationContext-redis.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
                         http://www.springframework.org/schema/beans/spring-beans.xsd
        				http://www.springframework.org/schema/mvc 
                         http://www.springframework.org/schema/mvc/spring-mvc.xsd
        				http://code.alibabatech.com/schema/dubbo 
                         http://code.alibabatech.com/schema/dubbo/dubbo.xsd
        				http://www.springframework.org/schema/context 
                         http://www.springframework.org/schema/context/spring-context.xsd">

	<!--Jedis连接池的相关配置-->
	<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
		<property name="maxTotal">
			<value>200</value>
		</property>
		<property name="maxIdle">
			<value>50</value>
		</property>
		<property name="testOnBorrow" value="true"/>
		<property name="testOnReturn" value="true"/>
	</bean>
	<bean id="jedisPool" class="redis.clients.jedis.JedisPool">
		<constructor-arg name="poolConfig" ref="jedisPoolConfig" />
		<constructor-arg name="host" value="127.0.0.1" />
		<constructor-arg name="port" value="6379" type="int" />
		<constructor-arg name="timeout" value="30000" type="int" />
	</bean>
</beans>
~~~

（5）完善SetmealServiceImpl服务类，在保存完成套餐信息后将图片名称存储到redis集合中

~~~java
@Autowired
private JedisPool jedisPool;
//新增套餐
public void add(Setmeal setmeal, Integer[] checkgroupIds) {
  setmealDao.add(setmeal);
  if(checkgroupIds != null && checkgroupIds.length > 0){
    setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
  }
  //将图片名称保存到Redis
  savePic2Redis(setmeal.getImg());
}
//将图片名称保存到Redis
private void savePic2Redis(String pic){
  jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
}
~~~

## 3. 体检套餐分页

### 3.1 完善页面

#### 3.1.1 定义分页相关模型数据

```javascript
pagination: {//分页相关模型数据
  currentPage: 1,//当前页码
  pageSize:10,//每页显示的记录数
  total:0,//总记录数
  queryString:null//查询条件
},
dataList: [],//当前页要展示的分页列表数据
```

#### 3.1.2 定义分页方法

在页面中提供了findPage方法用于分页查询，为了能够在setmeal.html页面加载后直接可以展示分页数据，可以在VUE提供的钩子函数created中调用findPage方法

```javascript
//钩子函数，VUE对象初始化完成后自动执行
created() {
  this.findPage();
}
```

```javascript
//分页查询
findPage() {
  //分页参数
  var param = {
    currentPage:this.pagination.currentPage,//页码
    pageSize:this.pagination.pageSize,//每页显示的记录数
    queryString:this.pagination.queryString//查询条件
  };
  //请求后台
  axios.post("/setmeal/findPage.do",param).then((response)=> {
    //为模型数据赋值，基于VUE的双向绑定展示到页面
    this.dataList = response.data.rows;
    this.pagination.total = response.data.total;
  });
}
```

#### 3.1.3 完善分页方法执行时机

除了在created钩子函数中调用findPage方法查询分页数据之外，当用户点击查询按钮或者点击分页条中的页码时也需要调用findPage方法重新发起查询请求。

为查询按钮绑定单击事件，调用findPage方法

```html
<el-button @click="findPage()" class="dalfBut">查询</el-button>
```

为分页条组件绑定current-change事件，此事件是分页条组件自己定义的事件，当页码改变时触发，对应的处理函数为handleCurrentChange

```html
<el-pagination
               class="pagiantion"
               @current-change="handleCurrentChange"
               :current-page="pagination.currentPage"
               :page-size="pagination.pageSize"
               layout="total, prev, pager, next, jumper"
               :total="pagination.total">
</el-pagination>
```

定义handleCurrentChange方法

```javascript
//切换页码
handleCurrentChange(currentPage) {
  //currentPage为切换后的页码
  this.pagination.currentPage = currentPage;
  this.findPage();
}
```

### 3.2 后台代码

#### 3.2.1 Controller

在SetmealController中增加分页查询方法

```java
//分页查询
@RequestMapping("/findPage")
public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
  PageResult pageResult = setmealService.pageQuery(
    queryPageBean.getCurrentPage(), 
    queryPageBean.getPageSize(), 
    queryPageBean.getQueryString()
  );
  return pageResult;
}
```

#### 3.2.2 服务接口

在SetmealService服务接口中扩展分页查询方法

```java
public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);
```

#### 3.2.3 服务实现类

在SetmealServiceImpl服务实现类中实现分页查询方法，基于Mybatis分页助手插件实现分页

```java
public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
  PageHelper.startPage(currentPage,pageSize);
  Page<CheckItem> page = checkGroupDao.selectByCondition(queryString);
  return new PageResult(page.getTotal(),page.getResult());
}
```

#### 3.2.4 Dao接口

在SetmealDao接口中扩展分页查询方法

```java
public Page<Setmeal> selectByCondition(String queryString);
```

#### 3.2.5 Mapper映射文件

在SetmealDao.xml文件中增加SQL定义

```xml
<!--根据条件查询-->
<select id="selectByCondition" parameterType="string" resultType="com.itheima.pojo.Setmeal">
  select * from t_setmeal
  <if test="value != null and value.length > 0">
    where code = #{value} or name = #{value} or helpCode = #{value}
  </if>
</select>
```

## 4. 定时任务组件Quartz

### 4.1 Quartz介绍

Quartz是Job scheduling（作业调度）领域的一个开源项目，Quartz既可以单独使用也可以跟spring框架整合使用，在实际开发中一般会使用后者。使用Quartz可以开发一个或者多个定时任务，每个定时任务可以单独指定执行的时间，例如每隔1小时执行一次、每个月第一天上午10点执行一次、每个月最后一天下午5点执行一次等。

官网：http://www.quartz-scheduler.org/

maven坐标：

~~~xml
<dependency>
  <groupId>org.quartz-scheduler</groupId>
  <artifactId>quartz</artifactId>
  <version>2.2.1</version>
</dependency>
<dependency>
  <groupId>org.quartz-scheduler</groupId>
  <artifactId>quartz-jobs</artifactId>
  <version>2.2.1</version>
</dependency>
~~~

### 4.2 Quartz入门案例

本案例基于Quartz和spring整合的方式使用。具体步骤：

（1）创建maven工程quartzdemo，导入Quartz和spring相关坐标，pom.xml文件如下

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.itheima</groupId>
    <artifactId>quartdemo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.quartz-scheduler</groupId>
            <artifactId>quartz</artifactId>
            <version>2.2.1</version>
        </dependency>
        <dependency>
            <groupId>org.quartz-scheduler</groupId>
            <artifactId>quartz-jobs</artifactId>
            <version>2.2.1</version>
        </dependency>
    </dependencies>
</project>
~~~

（2）自定义一个Job

~~~java
package com.itheima.jobs;
/**
 * 自定义Job
 */
public class JobDemo {
    public void run(){
        System.out.println("job execute...");
    }
}
~~~

（3）提供Spring配置文件spring-jobs.xml，配置自定义Job、任务描述、触发器、调度工厂等

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
						http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/mvc
						http://www.springframework.org/schema/mvc/spring-mvc.xsd
						http://code.alibabatech.com/schema/dubbo
						http://code.alibabatech.com/schema/dubbo/dubbo.xsd
						http://www.springframework.org/schema/context
						http://www.springframework.org/schema/context/spring-context.xsd">
	<!-- 注册自定义Job -->
    <bean id="jobDemo" class="com.itheima.jobs.JobDemo"></bean>
	<!-- 注册JobDetail,作用是负责通过反射调用指定的Job -->
    <bean id="jobDetail" 
          class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
        <!-- 注入目标对象 -->
        <property name="targetObject" ref="jobDemo"/>
        <!-- 注入目标方法 -->
        <property name="targetMethod" value="run"/>
    </bean>
    <!-- 注册一个触发器，指定任务触发的时间 -->
    <bean id="myTrigger" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
        <!-- 注入JobDetail -->
        <property name="jobDetail" ref="jobDetail"/>
        <!-- 指定触发的时间，基于Cron表达式 -->
        <property name="cronExpression">
            <value>0/10 * * * * ?</value>
        </property>
    </bean>
    <!-- 注册一个统一的调度工厂，通过这个调度工厂调度任务 -->
    <bean id="scheduler" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
        <!-- 注入多个触发器 -->
        <property name="triggers">
            <list>
                <ref bean="myTrigger"/>
            </list>
        </property>
    </bean>
</beans>
~~~

（4）编写main方法进行测试

~~~java
package com.itheima.jobs.com.itheima.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-jobs.xml");
    }
}
~~~

执行上面main方法观察控制台，可以发现每隔10秒会输出一次，说明每隔10秒自定义Job被调用一次。

### 4.3 cron表达式

上面的入门案例中我们指定了一个表达式：0/10 * * * * ?

这种表达式称为cron表达式，通过cron表达式可以灵活的定义出符合要求的程序执行的时间。本小节我们就来学习一下cron表达式的使用方法。如下图：

![6](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/6-1667479080526.png)

cron表达式分为七个域，之间使用空格分隔。其中最后一个域（年）可以为空。每个域都有自己允许的值和一些特殊字符构成。使用这些特殊字符可以使我们定义的表达式更加灵活。

下面是对这些特殊字符的介绍：

逗号（,）：指定一个值列表，例如使用在月域上1,4,5,7表示1月、4月、5月和7月

横杠（-）：指定一个范围，例如在时域上3-6表示3点到6点（即3点、4点、5点、6点）

星号（*）：表示这个域上包含所有合法的值。例如，在月份域上使用星号意味着每个月都会触发

斜线（/）：表示递增，例如使用在秒域上0/15表示每15秒

问号（?）：只能用在日和周域上，但是不能在这两个域上同时使用。表示不指定

井号（#）：只能使用在周域上，用于指定月份中的第几周的哪一天，例如6#3，意思是某月的第三个周五 (6=星期五，3意味着月份中的第三周)

L：某域上允许的最后一个值。只能使用在日和周域上。当用在日域上，表示的是在月域上指定的月份的最后一天。用于周域上时，表示周的最后一天，就是星期六

W：W 字符代表着工作日 (星期一到星期五)，只能用在日域上，它用来指定离指定日的最近的一个工作日

### 4.4 cron表达式在线生成器

前面介绍了cron表达式，但是自己编写表达式还是有一些困难的，我们可以借助一些cron表达式在线生成器来根据我们的需求生成表达式即可。

http://cron.qqe2.com/

![7](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/7.png)

## 5. 定时清理垃圾图片

前面我们已经完成了体检套餐的管理，在新增套餐时套餐的基本信息和图片是分两次提交到后台进行操作的。也就是用户首先将图片上传到七牛云服务器，然后再提交新增窗口中录入的其他信息。如果用户只是上传了图片而没有提交录入的其他信息，此时的图片就变为了垃圾图片，因为在数据库中并没有记录它的存在。此时我们要如何处理这些垃圾图片呢？

解决方案就是通过定时任务组件定时清理这些垃圾图片。为了能够区分出来哪些图片是垃圾图片，我们在文件上传成功后将图片保存到了一个redis集合中，当套餐数据插入到数据库后我们又将图片名称保存到了另一个redis集合中，通过计算这两个集合的差值就可以获得所有垃圾图片的名称。

本章节我们就会基于Quartz定时任务，通过计算redis两个集合的差值找出所有的垃圾图片，就可以将垃圾图片清理掉。

操作步骤：

（1）创建maven工程health_jobs，打包方式为war，导入Quartz等相关坐标

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>health_parent</artifactId>
        <groupId>com.itheima</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>health_jobs</artifactId>
    <packaging>war</packaging>
    <name>health_jobs Maven Webapp</name>
    <url>http://www.example.com</url>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
    <dependencies>
        <dependency>
            <groupId>com.itheima</groupId>
            <artifactId>health_interface</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.quartz-scheduler</groupId>
            <artifactId>quartz</artifactId>
        </dependency>
        <dependency>
            <groupId>org.quartz-scheduler</groupId>
            <artifactId>quartz-jobs</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <configuration>
                    <!-- 指定端口 -->
                    <port>83</port>
                    <!-- 请求路径 -->
                    <path>/</path>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
~~~

（2）配置web.xml

~~~xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >
<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <!-- 加载spring容器 -->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath*:applicationContext*.xml</param-value>
  </context-param>
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
</web-app>
~~~

（3）配置log4j.properties

~~~properties
### direct log messages to stdout ###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.err
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### direct messages to file mylog.log ###
log4j.appender.file=org.apache.log4j.FileAppender
log4j.appender.file.File=c:\\mylog.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### set log levels - for more verbose logging change 'info' to 'debug' ###

log4j.rootLogger=info, stdout
~~~

（4）配置applicationContext-redis.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans 
                         http://www.springframework.org/schema/beans/spring-beans.xsd
        				http://www.springframework.org/schema/mvc 
                          http://www.springframework.org/schema/mvc/spring-mvc.xsd
        				http://code.alibabatech.com/schema/dubbo 
                          http://code.alibabatech.com/schema/dubbo/dubbo.xsd
        				http://www.springframework.org/schema/context
                          http://www.springframework.org/schema/context/spring-context.xsd">

	<!--Jedis连接池的相关配置-->
	<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
		<property name="maxTotal">
			<value>200</value>
		</property>
		<property name="maxIdle">
			<value>50</value>
		</property>
		<property name="testOnBorrow" value="true"/>
		<property name="testOnReturn" value="true"/>
	</bean>
	<bean id="jedisPool" class="redis.clients.jedis.JedisPool">
		<constructor-arg name="poolConfig" ref="jedisPoolConfig" />
		<constructor-arg name="host" value="127.0.0.1" />
		<constructor-arg name="port" value="6379" type="int" />
		<constructor-arg name="timeout" value="30000" type="int" />
	</bean>
</beans>
~~~

（5）配置applicationContext-jobs.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
							http://www.springframework.org/schema/beans/spring-beans.xsd
							http://www.springframework.org/schema/mvc
							http://www.springframework.org/schema/mvc/spring-mvc.xsd
							http://code.alibabatech.com/schema/dubbo
							http://code.alibabatech.com/schema/dubbo/dubbo.xsd
							http://www.springframework.org/schema/context
							http://www.springframework.org/schema/context/spring-context.xsd">
	<context:annotation-config></context:annotation-config>
	<bean id="clearImgJob" class="com.itheima.jobs.ClearImgJob"></bean>
	<bean id="jobDetail" 
          class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
		<!-- 注入目标对象 -->
		<property name="targetObject" ref="clearImgJob"/>
		<!-- 注入目标方法 -->
		<property name="targetMethod" value="clearImg"/>
	</bean>
	<!-- 注册一个触发器，指定任务触发的时间 -->
	<bean id="myTrigger" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
		<!-- 注入JobDetail -->
		<property name="jobDetail" ref="jobDetail"/>
		<!-- 指定触发的时间，基于Cron表达式 -->
		<property name="cronExpression">
			<value>0 0 2 * * ?</value>
		</property>
	</bean>
	<!-- 注册一个统一的调度工厂，通过这个调度工厂调度任务 -->
	<bean id="scheduler" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
		<!-- 注入多个触发器 -->
		<property name="triggers">
			<list>
				<ref bean="myTrigger"/>
			</list>
		</property>
	</bean>
</beans>
~~~

（6）创建ClearImgJob定时任务类

~~~java
package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;
import java.util.Set;

/**
 * 自定义Job，实现定时清理垃圾图片
 */
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> set = 
            jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, 
                                          RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set != null){
            for (String picName : set) {
                //删除七牛云服务器上的图片
                QiniuUtils.deleteFileFromQiniu(picName);
                //从Redis集合中删除图片名称
                jedisPool.getResource().
                    srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            }
        }
    }
}
~~~

# 第5章 预约管理-预约设置

## 1. 需求分析

前面我们已经完成了检查项管理、检查组管理、套餐管理等。接下来我们需要进行预约设置，其实就是设置每一天的体检预约最大数量。客户可以通过微信端在线预约，在线预约时需要选择体检的时间，如果客户选择的时间已经预约满则无法进行预约。

## 2. Apache POI

### 2.1 POI介绍

Apache POI是用Java编写的免费开源的跨平台的Java API，Apache POI提供API给Java程序对Microsoft Office格式档案读和写的功能，其中使用最多的就是使用POI操作Excel文件。

jxl：专门操作Excel

maven坐标：

~~~xml
<dependency>
  <groupId>org.apache.poi</groupId>
  <artifactId>poi</artifactId>
  <version>3.14</version>
</dependency>
<dependency>
  <groupId>org.apache.poi</groupId>
  <artifactId>poi-ooxml</artifactId>
  <version>3.14</version>
</dependency>
~~~

POI结构：

~~~xml
HSSF － 提供读写Microsoft Excel XLS格式档案的功能
XSSF － 提供读写Microsoft Excel OOXML XLSX格式档案的功能
HWPF － 提供读写Microsoft Word DOC格式档案的功能
HSLF － 提供读写Microsoft PowerPoint格式档案的功能
HDGF － 提供读Microsoft Visio格式档案的功能
HPBF － 提供读Microsoft Publisher格式档案的功能
HSMF － 提供读Microsoft Outlook格式档案的功能
~~~

### 2.2 入门案例

#### 2.2.1 从Excel文件读取数据

使用POI可以从一个已经存在的Excel文件中读取数据

~~~java
//创建工作簿
XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");
//获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
XSSFSheet sheet = workbook.getSheetAt(0);
//遍历工作表获得行对象
for (Row row : sheet) {
  //遍历行对象获取单元格对象
  for (Cell cell : row) {
    //获得单元格中的值
    String value = cell.getStringCellValue();
    System.out.println(value);
  }
}
workbook.close();
~~~

通过上面的入门案例可以看到，POI操作Excel表格封装了几个核心对象：

~~~p
XSSFWorkbook：工作簿
XSSFSheet：工作表
Row：行
Cell：单元格
~~~

上面案例是通过遍历工作表获得行，遍历行获得单元格，最终获取单元格中的值。

还有一种方式就是获取工作表最后一个行号，从而根据行号获得行对象，通过行获取最后一个单元格索引，从而根据单元格索引获取每行的一个单元格对象，代码如下：

~~~java
//创建工作簿
XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");
//获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
XSSFSheet sheet = workbook.getSheetAt(0);
//获取当前工作表最后一行的行号，行号从0开始
int lastRowNum = sheet.getLastRowNum();
for(int i=0;i<=lastRowNum;i++){
  //根据行号获取行对象
  XSSFRow row = sheet.getRow(i);
  short lastCellNum = row.getLastCellNum();
  for(short j=0;j<lastCellNum;j++){
    String value = row.getCell(j).getStringCellValue();
    System.out.println(value);
  }
}
workbook.close();
~~~

#### 2.2.2 向Excel文件写入数据

使用POI可以在内存中创建一个Excel文件并将数据写入到这个文件，最后通过输出流将内存中的Excel文件下载到磁盘

~~~java
//在内存中创建一个Excel文件
XSSFWorkbook workbook = new XSSFWorkbook();
//创建工作表，指定工作表名称
XSSFSheet sheet = workbook.createSheet("传智播客");

//创建行，0表示第一行
XSSFRow row = sheet.createRow(0);
//创建单元格，0表示第一个单元格
row.createCell(0).setCellValue("编号");
row.createCell(1).setCellValue("名称");
row.createCell(2).setCellValue("年龄");

XSSFRow row1 = sheet.createRow(1);
row1.createCell(0).setCellValue("1");
row1.createCell(1).setCellValue("小明");
row1.createCell(2).setCellValue("10");

XSSFRow row2 = sheet.createRow(2);
row2.createCell(0).setCellValue("2");
row2.createCell(1).setCellValue("小王");
row2.createCell(2).setCellValue("20");

//通过输出流将workbook对象下载到磁盘
FileOutputStream out = new FileOutputStream("D:\\itcast.xlsx");
workbook.write(out);
out.flush();
out.close();
workbook.close();
~~~

## 3. 批量导入预约设置信息

预约设置信息对应的数据表为t_ordersetting，预约设置操作对应的页面为ordersetting.html

t_ordersetting表结构：

![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2-1667544879576.png)

orderDate：预约日期  

number：可预约人数  

reservations：已预约人数



批量导入预约设置信息操作过程：

1、点击模板下载按钮下载Excel模板文件

2、将预约设置信息录入到模板文件中

3、点击上传文件按钮将录入完信息的模板文件上传到服务器

4、通过POI读取上传文件的数据并保存到数据库

### 3.1 完善页面

#### 3.1.1 提供模板文件

资料中已经提供了Excel模板文件ordersetting_template.xlsx，将文件放在health_backend工程的template目录

#### 3.1.2 实现模板文件下载

为模板下载按钮绑定事件实现模板文件下载

~~~html
<el-button style="margin-bottom: 20px;margin-right: 20px" type="primary" 
           @click="downloadTemplate()">模板下载</el-button>
~~~

~~~javascript
//模板文件下载
downloadTemplate(){
	window.location.href="../../template/ordersetting_template.xlsx";
}
~~~

#### 注意点

> 文件下载使用window.location.href=xxxx,访问这个路径是就会下载文件

#### 3.1.3 文件上传

使用ElementUI的上传组件实现文件上传并绑定相关事件

~~~html
<el-upload action="/ordersetting/upload.do"
           name="excelFile"
           :show-file-list="false"
           :on-success="handleSuccess"
           :before-upload="beforeUpload">
  <el-button type="primary">上传文件</el-button>
</el-upload>
~~~

~~~javascript
handleSuccess(response, file) {
  if(response.flag){
    this.$message({
      message: response.message,
      type: 'success'
    });
  }else{
    this.$message.error(response.message);
  }
}

beforeUpload(file){
  const isXLS = file.type === 'application/vnd.ms-excel';
  if(isXLS){
    return true;
  }
  const isXLSX = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
  if (isXLSX) {
    return true;
  }
  this.$message.error('上传文件只能是xls或者xlsx格式!');
  return false;
}
~~~

### 3.2 后台代码

#### 3.2.1 Controller

将资料中的POIUtils工具类复制到health_common工程

在health_backend工程创建OrderSettingController并提供upload方法

~~~java
package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 预约设置
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * Excel文件上传，并解析文件内容保存到数据库
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile){
        try {
            //读取Excel文件数据
            List<String[]> list = POIUtils.readExcel(excelFile);
            if(list != null && list.size() > 0){
                List<OrderSetting> orderSettingList = new ArrayList<>();
                for (String[] strings : list) {
                    OrderSetting orderSetting = 
                      new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                    orderSettingList.add(orderSetting);
                }
                orderSettingService.add(orderSettingList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }
}
~~~

#### 注意点

> 图片上传为post请求，但是还是加了一个RequestParam注解，不加好像就拿不到

#### 3.2.2 服务接口

创建OrderSettingService服务接口并提供新增方法

~~~java
package com.itheima.service;

import com.itheima.pojo.OrderSetting;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    public void add(List<OrderSetting> list);
}
~~~

#### 3.2.3 服务实现类

创建服务实现类OrderSettingServiceImpl并实现新增方法

~~~java
package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
/**
 * 预约设置服务
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    //批量添加
    public void add(List<OrderSetting> list) {
        if(list != null && list.size() > 0){
            for (OrderSetting orderSetting : list) {
                //检查此数据（日期）是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(count > 0){
                    //已经存在，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else{
                    //不存在，执行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }
}
~~~

#### 3.2.4 Dao接口

创建Dao接口OrderSettingDao并提供更新和新增方法

~~~java
package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);
    public void editNumberByOrderDate(OrderSetting orderSetting);
  	public long findCountByOrderDate(Date orderDate);
}
~~~

#### 3.2.5 Mapper映射文件

创建Mapper映射文件OrderSettingDao.xml并提供相关SQL

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.itheima.dao.OrderSettingDao" >
    <!--新增-->
    <insert id="add" parameterType="com.itheima.pojo.OrderSetting">
        insert into t_ordersetting
      		(orderDate,number,reservations)
                      values 
      		(#{orderDate},#{number},#{reservations})
    </insert>
    <!--根据日期更新预约人数-->
    <update id="editNumberByOrderDate" parameterType="com.itheima.pojo.OrderSetting">
        update t_ordersetting set number = #{number} where orderDate = #{orderDate}
    </update>
    <!--根据预约日期查询-->
    <select id="findCountByOrderDate" parameterType="java.util.Date" resultType="long">
        select count(*) from t_ordersetting where orderDate = #{orderDate}
    </select>
</mapper>
~~~

## 4. 日历展示预约设置信息

前面已经完成了预约设置功能，现在就需要通过日历的方式展示出来每天设置的预约人数。

在页面中已经完成了日历的动态展示，我们只需要查询当前月份的预约设置信息并展示到日历中即可，同时在日历中还需要展示已经预约的人数，效果如下：

![1](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day05/%25E8%25AE%25B2%25E4%25B9%2589/1.png)

### 4.1 完善页面

#### 4.1.1 使用静态数据调试

为了能够快速看到效果，我们可以先使用静态数据模拟，然后再改为发送ajax请求查询数据库。

实现步骤：

（1）预约设置数据对应的模型数据为leftobj，在initData方法最后为leftobj模型数据赋值：

~~~javascript
this.leftobj = [
                  { date: 1, number: 120, reservations: 1 },
                  { date: 3, number: 120, reservations: 1 },
                  { date: 4, number: 120, reservations: 120 },
                  { date: 6, number: 120, reservations: 1 },
                  { date: 8, number: 120, reservations: 1 }
                ];
~~~

其中date表示日期，number表示可预约人数，reservations表示已预约人数

（2）使用VUE的v-for标签遍历上面的leftobj模型数据，展示到日历上：

~~~html
<template>
  <template v-for="obj in leftobj">
    <template v-if="obj.date == dayobject.day.getDate()">
      <template v-if="obj.number > obj.reservations">
        <div class="usual">
          <p>可预约{{obj.number}}人</p>
          <p>已预约{{obj.reservations}}人</p>
        </div>
      </template>
      <template v-else>
        <div class="fulled">
          <p>可预约{{obj.number}}人</p>
          <p>已预约{{obj.reservations}}人</p>
          <p>已满</p>
        </div>
      </template>
    </template>
  </template>
  <button v-if="dayobject.day > today" 
          @click="handleOrderSet(dayobject.day)" class="orderbtn">设置</button>
</template>
~~~

#### 4.1.2 发送ajax获取动态数据

将上面的静态模拟数据去掉，改为发送ajax请求，根据当前页面对应的月份查询数据库获取预约设置信息，将查询结果赋值给leftobj模型数据

~~~javascript
//发送ajax请求，根据当前页面对应的月份查询预约设置信息
axios.post(
  "/ordersetting/getOrderSettingByMonth.do?date="+this.currentYear+'-'+this.currentMonth
		  ).then((response)=>{
  if(response.data.flag){
    //为模型数据赋值，通过双向绑定展示到日历中
    this.leftobj = response.data.data;
  }else{
    this.$message.error(response.data.message);
  }
});
~~~

### 4.2 后台代码

#### 4.2.1 Controller

在OrderSettingController中提供getOrderSettingByMonth方法，根据月份查询预约设置信息

~~~java
/**
* 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
* @param date
* @return
*/
@RequestMapping("/getOrderSettingByMonth")
public Result getOrderSettingByMonth(String date){//参数格式为：2019-03
  try{
    List<Map> list = orderSettingService.getOrderSettingByMonth(date);
    //获取预约设置数据成功
    return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
  }catch (Exception e){
    e.printStackTrace();
    //获取预约设置数据失败
    return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
  }
}
~~~

#### 4.2.2 服务接口

在OrderSettingService服务接口中扩展方法getOrderSettingByMonth

~~~java
public List<Map> getOrderSettingByMonth(String date);//参数格式为：2019-03
~~~

#### 4.2.3 服务实现类

在OrderSettingServiceImpl服务实现类中实现方法getOrderSettingByMonth

~~~java
//根据日期查询预约设置数据
public List<Map> getOrderSettingByMonth(String date) {//2019-3
  String dateBegin = date + "-1";//2019-3-1
  String dateEnd = date + "-31";//2019-3-31
  Map map = new HashMap();
  map.put("dateBegin",dateBegin);
  map.put("dateEnd",dateEnd);
  List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
  List<Map> data = new ArrayList<>();
  for (OrderSetting orderSetting : list) {
    Map orderSettingMap = new HashMap();
    orderSettingMap.put("date",orderSetting.getOrderDate().getDate());//获得日期（几号）
    orderSettingMap.put("number",orderSetting.getNumber());//可预约人数
    orderSettingMap.put("reservations",orderSetting.getReservations());//已预约人数
    data.add(orderSettingMap);
  }
  return data;
}
~~~

#### 4.2.4 Dao接口

在OrderSettingDao接口中扩展方法getOrderSettingByMonth

~~~java
public List<OrderSetting> getOrderSettingByMonth(Map date);
~~~

#### 4.2.5 Mapper映射文件

在OrderSettingDao.xml文件中扩展SQL

~~~xml
<!--根据月份查询预约设置信息-->
<select id="getOrderSettingByMonth" 
        parameterType="hashmap" 
        resultType="com.itheima.pojo.OrderSetting">
  select * from t_ordersetting where orderDate between #{dateBegin} and #{dateEnd}
</select>
~~~

## 5. 基于日历实现预约设置

本章节要完成的功能为通过点击日历中的设置按钮来设置对应日期的可预约人数。效果如下：

![5](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/5-1667544879583.png)

### 5.1 完善页面

#### 5.1.1 为设置按钮绑定事件

为日历中的设置按钮绑定单击事件，当前日期作为参数

~~~html
<button v-if="dayobject.day > today" 
        @click="handleOrderSet(dayobject.day)" class="orderbtn">设置</button>
~~~

~~~javascript
//预约设置
handleOrderSet(day){
	alert(day);
}
~~~

#### 5.1.2 弹出预约设置窗口并发送ajax请求

完善handleOrderSet方法，弹出预约设置窗口，用户点击确定按钮则发送ajax请求

~~~javascript
//预约设置
handleOrderSet(day){
  this.$prompt('请输入可预约人数', '预约设置', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^[0-9]*[1-9][0-9]*$/,
    inputErrorMessage: '只能输入正整数'
  }).then(({ value }) => {
    //发送ajax请求根据日期修改可预约人数
    axios.post("/ordersetting/editNumberByDate.do",{
      orderDate:this.formatDate(day.getFullYear(),day.getMonth()+1,day.getDate()), //日期
      number:value   //可预约人数
    }).then((response)=>{
      if(response.data.flag){
        this.initData(this.formatDate(day.getFullYear(), day.getMonth() + 1, 1));
        this.$message({
          type: 'success',
          message: response.data.message
        });
      }else{
        this.$message.error(response.data.message);
      }
    });
  }).catch(() => {
    this.$message({
      type: 'info',
      message: '已取消'
    });
  });
}
~~~

### 5.2 后台代码

#### 5.2.1 Controller

在OrderSettingController中提供方法editNumberByDate

~~~java
/**
* 根据指定日期修改可预约人数
* @param orderSetting
* @return
*/
@RequestMapping("/editNumberByDate")
public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
  try{
    orderSettingService.editNumberByDate(orderSetting);
    //预约设置成功
    return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
  }catch (Exception e){
    e.printStackTrace();
    //预约设置失败
    return new Result(false,MessageConstant.ORDERSETTING_FAIL);
  }
}
~~~

#### 5.2.2 服务接口

在OrderSettingService服务接口中提供方法editNumberByDate

~~~java
public void editNumberByDate(OrderSetting orderSetting);
~~~

#### 5.2.3 服务实现类

在OrderSettingServiceImpl服务实现类中实现editNumberByDate

~~~java
//根据日期修改可预约人数
public void editNumberByDate(OrderSetting orderSetting) {
  long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
  if(count > 0){
    //当前日期已经进行了预约设置，需要进行修改操作
    orderSettingDao.editNumberByOrderDate(orderSetting);
  }else{
    //当前日期没有进行预约设置，进行添加操作
    orderSettingDao.add(orderSetting);
  }
}
~~~

#### 5.2.4 Dao接口

在OrderSettingDao接口中提供方法

~~~java
public void editNumberByOrderDate(OrderSetting orderSetting);
public long findCountByOrderDate(Date orderDate);
~~~

#### 5.2.5 Mapper映射文件

在OrderSettingDao.xml映射文件中提供SQL

~~~xml
<!--根据日期更新可预约人数-->
<update id="editNumberByOrderDate" parameterType="com.itheima.pojo.OrderSetting">
  update t_ordersetting set number = #{number} where orderDate = #{orderDate}
</update>
<!--根据预约日期查询-->
<select id="findCountByOrderDate" parameterType="java.util.Date" resultType="long">
  select count(*) from t_ordersetting where orderDate = #{orderDate}
</select>
~~~

# 第6章 移动端开发-体检预约

## 1. 移动端开发

### 1.1 移动端开发方式 

随着移动互联网的兴起和手机的普及，目前移动端应用变得愈发重要，成为了各个商家的必争之地。例如，我们可以使用手机购物、支付、打车、玩游戏、订酒店、购票等，以前只能通过PC端完成的事情，现在通过手机都能够实现，而且更加方便，而这些都需要移动端开发进行支持，那如何进行移动端开发呢？

移动端开发主要有三种方式：

1、基于手机API开发（原生APP）

2、基于手机浏览器开发（移动web）

3、混合开发（混合APP）

#### 1.1.1 基于手机API开发

手机端使用手机API，例如使用Android、ios 等进行开发，服务端只是一个数据提供者。手机端请求服务端获取数据（json、xml格式）并在界面进行展示。这种方式相当于传统开发中的C/S模式，即需要在手机上安装一个客户端软件。

这种方式需要针对不同的手机系统分别进行开发，目前主要有以下几个平台：

1、苹果ios系统版本，开发语言是Objective-C

2、安卓Android系统版本，开发语言是Java

3、微软Windows phone系统版本，开发语言是C#

4、塞班symbian系统版本，开发语言是C++

此种开发方式举例：手机淘宝、抖音、今日头条、大众点评

#### 1.1.2 基于手机浏览器开发

生存在浏览器中的应用，基本上可以说是触屏版的网页应用。这种开发方式相当于传统开发中的B/S模式，也就是手机上不需要额外安装软件，直接基于手机上的浏览器进行访问。这就需要我们编写的html页面需要根据不同手机的尺寸进行自适应调节，目前比较流行的是html5开发。除了直接通过手机浏览器访问，还可以将页面内嵌到一些应用程序中，例如通过微信公众号访问html5页面。

这种开发方式不需要针对不同的手机系统分别进行开发，只需要开发一个版本，就可以在不同的手机上正常访问。

本项目会通过将我们开发的html5页面内嵌到微信公众号这种方式进行开发。

#### 1.1.3 混合开发

是半原生半Web的混合类App。需要下载安装，看上去类似原生App，访问的内容是Web网页。其实就是把HTML5页面嵌入到一个原生容器里面。

### 1.2 微信公众号开发

要进行微信公众号开发，首先需要访问微信公众平台，官网：https://mp.weixin.qq.com/。

#### 1.2.1 帐号分类

在微信公众平台可以看到，有四种帐号类型：服务号、订阅号、小程序、企业微信（原企业号）。

![8](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/8-1667561940324.png)

![11](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/11-1667561940325.png)

本项目会选择订阅号这种方式进行公众号开发。

#### 1.2.2 注册帐号

要开发微信公众号，首先需要注册成为会员，然后就可以登录微信公众平台进行自定义菜单的设置。

注册页面：https://mp.weixin.qq.com/cgi-bin/registermidpage?action=index&lang=zh_CN&token=

![9](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/9-1667561940325.png)

选择订阅号进行注册：

![10](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/10-1667561940325.png)



输入邮箱、邮箱验证码、密码、确认密码等按照页面流程进行注册

#### 1.2.3 自定义菜单

注册成功后就可以使用注册的邮箱和设置的密码进行登录，登录成功后点击左侧“自定义菜单”进入自定义菜单页面

![12](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day06/%25E8%25AE%25B2%25E4%25B9%2589/12.png)

在自定义菜单页面可以根据需求创建一级菜单和二级菜单，其中一级菜单最多可以创建3个，每个一级菜单下面最多可以创建5个二级菜单。每个菜单由菜单名称和菜单内容组成，其中菜单内容有3中形式：发送消息、跳转网页、跳转小程序。

#### 1.2.4 上线要求

如果是个人用户身份注册的订阅号，则自定义菜单的菜单内容不能进行跳转网页，因为个人用户目前不支持微信认证，而跳转网页需要微信认证之后才有权限。

如果是企业用户，首先需要进行微信认证，通过后就可以进行跳转网页了，跳转网页的地址要求必须有域名并且域名需要备案通过。

## 2. 需求分析和环境搭建

### 2.1 需求分析

用户在体检之前需要进行预约，可以通过电话方式进行预约，此时会由体检中心客服人员通过后台系统录入预约信息。用户也可以通过手机端自助预约。本章节开发的功能为用户通过手机自助预约。

预约流程如下：

1、访问移动端首页

2、点击体检预约进入体检套餐列表页面

3、在体检套餐列表页面点击具体套餐进入套餐详情页面

4、在套餐详情页面点击立即预约进入预约页面

5、在预约页面录入体检人相关信息点击提交预约



效果如下图：

![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2-1667561940325.png)



![3](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/3-1667561940340.png)



![4](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/4-1667561940325.png)





![5](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/5-1667561940340.png)



### 2.2 搭建移动端工程

本项目是基于SOA架构进行开发，前面我们已经完成了后台系统的部分功能开发，在后台系统中都是通过Dubbo调用服务层发布的服务进行相关的操作。本章节我们开发移动端工程也是同样的模式，所以我们也需要在移动端工程中通过Dubbo调用服务层发布的服务，如下图：

![1](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/1-1667561940325.png)

#### 2.2.1 导入maven坐标

在health_common工程的pom.xml文件中导入阿里短信发送的maven坐标

~~~xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>3.3.1</version>
</dependency>
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-dysmsapi</artifactId>
  <version>1.0.0</version>
</dependency>
~~~

#### 2.2.2 导入通用组件

在health_common工程中导入如下通用组件

ValidateCodeUtils工具类：

~~~java
package com.itheima.utils;

import java.util.Random;

/**
 * 随机生成验证码工具类
 */
public class ValidateCodeUtils {
    /**
     * 随机生成验证码
     * @param length 长度为4位或者6位
     * @return
     */
    public static Integer generateValidateCode(int length){
        Integer code =null;
        if(length == 4){
            code = new Random().nextInt(9999);//生成随机数，最大为9999
            if(code < 1000){
                code = code + 1000;//保证随机数为4位数字
            }
        }else if(length == 6){
            code = new Random().nextInt(999999);//生成随机数，最大为999999
            if(code < 100000){
                code = code + 100000;//保证随机数为6位数字
            }
        }else{
            throw new RuntimeException("只能生成4位或6位数字验证码");
        }
        return code;
    }

    /**
     * 随机生成指定长度字符串验证码
     * @param length 长度
     * @return
     */
    public static String generateValidateCode4String(int length){
        Random rdm = new Random();
        String hash1 = Integer.toHexString(rdm.nextInt());
        String capstr = hash1.substring(0, length);
        return capstr;
    }
}
~~~

SMSUtils工具类：

~~~java
package com.itheima.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {
	public static final String VALIDATE_CODE = "SMS_159620392";//发送短信验证码
	public static final String ORDER_NOTICE = "SMS_159771588";//体检预约成功通知

	/**
	 * 发送短信
	 * @param phoneNumbers
	 * @param param
	 * @throws ClientException
	 */
	public static void sendShortMessage(String templateCode,String phoneNumbers,String param) throws ClientException{
		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient需要的几个参数
		final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
		// 替换成你的AK
		final String accessKeyId = "accessKeyId";// 你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = "accessKeySecret";// 你的accessKeySecret，参考本文档步骤2
		// 初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();
		// 使用post提交
		request.setMethod(MethodType.POST);
		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		request.setPhoneNumbers(phoneNumbers);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName("传智健康");
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{\"code\":\""+param+"\"}");
		// 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("yourOutId");
		// 请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			// 请求成功
			System.out.println("请求成功");
		}
	}
}
~~~

RedisMessageConstant常量类：

~~~java
package com.itheima.constant;

public class RedisMessageConstant {
    public static final String SENDTYPE_ORDER = "001";//用于缓存体检预约时发送的验证码
    public static final String SENDTYPE_LOGIN = "002";//用于缓存手机号快速登录时发送的验证码
    public static final String SENDTYPE_GETPWD = "003";//用于缓存找回密码时发送的验证码
}
~~~

#### 2.2.3 health_mobile

创建移动端工程health_mobile，打包方式为war，用于存放Controller，在Controller中通过Dubbo可以远程访问服务层相关服务，所以需要依赖health_interface接口工程。

pom.xml：

~~~xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>health_parent</artifactId>
        <groupId>com.itheima</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>health_mobile</artifactId>
    <packaging>war</packaging>
    <name>healthmobile_web Maven Webapp</name>
    <url>http://www.example.com</url>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
    <dependencies>
        <dependency>
            <groupId>com.itheima</groupId>
            <artifactId>health_interface</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId>
                <artifactId>tomcat7-maven-plugin</artifactId>
                <configuration>
                    <!-- 指定端口 -->
                    <port>80</port>
                    <!-- 请求路径 -->
                    <path>/</path>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
~~~

静态资源（CSS、html、img等，详见资料）：

![6](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/6-1667561940349.png)

web.xml：

~~~xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >
<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <!-- 解决post乱码 -->
  <filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>utf-8</param-value>
    </init-param>
    <init-param>
      <param-name>forceEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!-- 指定加载的配置文件 ，通过参数contextConfigLocation加载 -->
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:springmvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>*.do</url-pattern>
  </servlet-mapping>
  <welcome-file-list>
    <welcome-file>/pages/index.html</welcome-file>
  </welcome-file-list>
</web-app>
~~~

springmvc.xml：

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
		 http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/mvc
          http://www.springframework.org/schema/mvc/spring-mvc.xsd
          http://code.alibabatech.com/schema/dubbo
          http://code.alibabatech.com/schema/dubbo/dubbo.xsd
          http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context.xsd">

	<mvc:annotation-driven>
	  <mvc:message-converters register-defaults="true">
	    <bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter">
	      <property name="supportedMediaTypes" value="application/json"/>
	      <property name="features">
	        <list>
	          <value>WriteMapNullValue</value>
	          <value>WriteDateUseDateFormat</value>
	        </list>
	      </property>
	    </bean>
	  </mvc:message-converters>
	</mvc:annotation-driven>
	<!-- 指定应用名称 -->
	<dubbo:application name="health_mobile" />
    <!--指定服务注册中心地址-->
	<dubbo:registry address="zookeeper://127.0.0.1:2181"/>
    <!--批量扫描-->
	<dubbo:annotation package="com.itheima.controller" />
    <!--
        超时全局设置 10分钟
        check=false 不检查服务提供方，开发阶段建议设置为false
        check=true 启动时检查服务提供方，如果服务提供方没有启动则报错
    -->
	<dubbo:consumer timeout="600000" check="false"/>
	<import resource="spring-redis.xml"></import>
</beans>
~~~

spring-redis.xml：

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
		 http://www.springframework.org/schema/beans/spring-beans.xsd
          http://www.springframework.org/schema/mvc
          http://www.springframework.org/schema/mvc/spring-mvc.xsd
          http://code.alibabatech.com/schema/dubbo
          http://code.alibabatech.com/schema/dubbo/dubbo.xsd
          http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context.xsd">
  
	<context:property-placeholder location="classpath:redis.properties" />

	<!--Jedis连接池的相关配置-->
	<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
		<property name="maxTotal">
			<value>${redis.pool.maxActive}</value>
		</property>
		<property name="maxIdle">
			<value>${redis.pool.maxIdle}</value>
		</property>
		<property name="testOnBorrow" value="true"/>
		<property name="testOnReturn" value="true"/>
	</bean>

	<bean id="jedisPool" class="redis.clients.jedis.JedisPool">
		<constructor-arg name="poolConfig" ref="jedisPoolConfig" />
		<constructor-arg name="host" value="${redis.host}" />
		<constructor-arg name="port" value="${redis.port}" type="int" />
		<constructor-arg name="timeout" value="${redis.timeout}" type="int" />
	</bean>
</beans>
~~~

redis.properties：

~~~properties
#最大分配的对象数
redis.pool.maxActive=200
#最大能够保持idel状态的对象数
redis.pool.maxIdle=50
redis.pool.minIdle=10
redis.pool.maxWaitMillis=20000
#当池内没有返回对象时，最大等待时间
redis.pool.maxWait=300

#格式：redis://:[密码]@[服务器地址]:[端口]/[db index]
#redis.uri = redis://:12345@127.0.0.1:6379/0

redis.host = 127.0.0.1
redis.port = 6379
redis.timeout = 30000
~~~

log4j.properties：

~~~properties
### direct log messages to stdout ###
log4j.appender.stdout=org.apache.log4j.ConsoleAppender
log4j.appender.stdout.Target=System.err
log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### direct messages to file mylog.log ###
log4j.appender.file=org.apache.log4j.FileAppender
log4j.appender.file.File=c:\\mylog.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

### set log levels - for more verbose logging change 'info' to 'debug' ###

log4j.rootLogger=info, stdout
~~~

## 3. 套餐列表页面动态展示

移动端首页为/pages/index.html，效果如下：

![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2_.png)

点击体检预约直接跳转到体检套餐列表页面（/pages/setmeal.html）

### 3.1 完善页面

#### 3.1.1 展示套餐信息

~~~html
<ul class="list">
  <li class="list-item" v-for="setmeal in setmealList">
    <a class="link-page" :href="'setmeal_detail.html?id='+setmeal.id">
      <img class="img-object f-left" 
           :src="'http://pqjroc654.bkt.clouddn.com/'+setmeal.img" alt="">
      <div class="item-body">
        <h4 class="ellipsis item-title">{{setmeal.name}}</h4>
        <p class="ellipsis-more item-desc">{{setmeal.remark}}</p>
        <p class="item-keywords">
          <span>{{setmeal.sex == '0' ? '性别不限' : setmeal.sex == '1' ? '男':'女'}}</span>
          <span>{{setmeal.age}}</span>
        </p>
      </div>
    </a>
  </li>
</ul>
~~~

#### 3.1.2 获取套餐列表数据

~~~javascript
var vue = new Vue({
  el:'#app',
  data:{
    setmealList:[]
  },
  mounted (){
      //发送ajax请求，获取所有的套餐数据，赋值给setmealList模型数据，用于页面展示
      axios.get("/setmeal/getAllSetmeal.do").then((res) => {
          if(res.data.flag){
              //查询成功，给模型数据赋值
              this.setmealList = res.data.data;
          }else{
              //查询失败，弹出提示信息
              this.$message.error(res.data.message);
          }
      });
  }
});
~~~

### 3.2 后台代码

#### 3.2.1 Controller

在health_mobile工程中创建SetmealController并提供getSetmeal方法，在此方法中通过Dubbo远程调用套餐服务获取套餐列表数据

~~~java
package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference//(check = false)
    private SetmealService setmealService;

    //获取所有套餐信息
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        try{
            List<Setmeal> list = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }
}
~~~

#### 3.2.2 服务接口

在SetmealService服务接口中扩展findAll方法

```
public List<Setmeal> findAll();
```

#### 3.2.3 服务实现类

在SetmealServiceImpl服务实现类中实现findAll方法

~~~java
public List<Setmeal> findAll() {
  return setmealDao.findAll();
}
~~~

#### 3.2.4 Dao接口

在SetmealDao接口中扩展findAll方法

~~~java
public List<Setmeal> findAll();
~~~

#### 3.2.5 Mapper映射文件

在SetmealDao.xml映射文件中扩展SQL语句

~~~xml
<select id="findAll" resultType="com.itheima.pojo.Setmeal">
  select * from t_setmeal
</select>
~~~

## 4. 套餐详情页面动态展示

前面我们已经完成了体检套餐列表页面动态展示，点击其中任意一个套餐则跳转到对应的套餐详情页面（/pages/setmeal_detail.html），并且会携带此套餐的id作为参数提交。

请求路径格式：http://localhost/pages/setmeal_detail.html?id=10

在套餐详情页面需要展示当前套餐的信息（包括图片、套餐名称、套餐介绍、适用性别、适用年龄）、此套餐包含的检查组信息、检查组包含的检查项信息等。

### 4.1 完善页面

#### 4.1.1 获取请求参数中套餐id

在页面中已经引入了healthmobile.js文件，此文件中已经封装了getUrlParam方法可以根据URL请求路径中的参数名获取对应的值

~~~javascript
function getUrlParam(paraName) {
    var url = document.location.toString();
    //alert(url);
    var arrObj = url.split("?");
    if (arrObj.length > 1) {
        var arrPara = arrObj[1].split("&");
        var arr;
        for (var i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("=");
            if (arr != null && arr[0] == paraName) {
                return arr[1];
            }
        }
        return "";
    }
    else {
        return "";
    }
}
~~~

在setmeal_detail.html中调用上面定义的方法获取套餐id的值

~~~javascript
<script>
  var id = getUrlParam("id");
</script>
~~~

#### 4.1.2 获取套餐详细信息

~~~javascript
<script>
    var vue = new Vue({
        el:'#app',
        data:{
            imgUrl:null,//套餐对应的图片链接
            setmeal:{}
        },
        mounted(){
            axios.post("/setmeal/findById.do?id=" + id).then((response) => {
                if(response.data.flag){
                    this.setmeal = response.data.data;
                    this.imgUrl = 'http://pqjroc654.bkt.clouddn.com/' + this.setmeal.img;
                }
            });
        }
    });
</script>
~~~

#### 4.1.3 展示套餐信息

~~~html
<div class="contentBox">
  <div class="card">
    <div class="project-img">
      <img :src="imgUrl" width="100%" height="100%" />
    </div>
    <div class="project-text">
      <h4 class="tit">{{setmeal.name}}</h4>
      <p class="subtit">{{setmeal.remark}}</p>
      <p class="keywords">
        <span>{{setmeal.sex == '0' ? '性别不限' : setmeal.sex == '1' ? '男':'女'}}</span>
        <span>{{setmeal.age}}</span>
      </p>
    </div>
  </div>
  <div class="table-listbox">
    <div class="box-title">
      <i class="icon-zhen"><span class="path1"></span><span class="path2"></span></i>
      <span>套餐详情</span>
    </div>
    <div class="box-table">
      <div class="table-title">
        <div class="tit-item flex2">项目名称</div>
        <div class="tit-item  flex3">项目内容</div>
        <div class="tit-item  flex3">项目解读</div>
      </div>
      <div class="table-content">
        <ul class="table-list">
          <li class="table-item" v-for="checkgroup in setmeal.checkGroups">
            <div class="item flex2">{{checkgroup.name}}</div>
            <div class="item flex3">
              <label v-for="checkitem in checkgroup.checkItems">
                {{checkitem.name}}
              </label>
            </div>
            <div class="item flex3">{{checkgroup.remark}}</div>
          </li>
        </ul>
      </div>
      <div class="box-button">
        <a @click="toOrderInfo()" class="order-btn">立即预约</a>
      </div>
    </div>
  </div>
</div>
~~~

### 4.2 后台代码

#### 4.2.1 Controller

在SetmealController中提供findById方法

~~~java
//根据id查询套餐信息
@RequestMapping("/findById")
public Result findById(int id){
  try{
    Setmeal setmeal = setmealService.findById(id);
    return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
  }catch (Exception e){
    e.printStackTrace();
    return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
  }
}
~~~

#### 4.2.2 服务接口

在SetmealService服务接口中提供findById方法

~~~java
public Setmeal findById(int id);
~~~

#### 4.2.3 服务实现类

在SetmealServiceImpl服务实现类中实现findById方法

~~~java
public Setmeal findById(int id) {
  return setmealDao.findById(id);
}
~~~

#### 4.2.4 Dao接口

在SetmealDao接口中提供findById方法

~~~java
public Setmeal findById(int id);
~~~

#### 4.2.5 Mapper映射文件

此处会使用mybatis提供的关联查询，在根据id查询套餐时，同时将此套餐包含的检查组都查询出来，并且将检查组包含的检查项都查询出来。

SetmealDao.xml文件：

~~~xml
<resultMap type="com.itheima.pojo.Setmeal" id="baseResultMap">
  <id column="id" property="id"/>
  <result column="name" property="name"/>
  <result column="code" property="code"/>
  <result column="helpCode" property="helpCode"/>
  <result column="sex" property="sex"/>
  <result column="age" property="age"/>
  <result column="price" property="price"/>
  <result column="remark" property="remark"/>
  <result column="attention" property="attention"/>
  <result column="img" property="img"/>
</resultMap>
<resultMap type="com.itheima.pojo.Setmeal" 
           id="findByIdResultMap" 
           extends="baseResultMap">
  <collection property="checkGroups" 
              javaType="ArrayList"
              ofType="com.itheima.pojo.CheckGroup" 
              column="id"
              select="com.itheima.dao.CheckGroupDao.findCheckGroupById">
  </collection>
</resultMap>
<select id="findById" resultMap="findByIdResultMap">
  select * from t_setmeal  where id=#{id}
</select>
~~~

CheckGroupDao.xml文件：

~~~xml
<resultMap type="com.itheima.pojo.CheckGroup" id="baseResultMap">
  <id column="id" property="id"/>
  <result column="name" property="name"/>
  <result column="code" property="code"/>
  <result column="helpCode" property="helpCode"/>
  <result column="sex" property="sex"/>
  <result column="remark" property="remark"/>
  <result column="attention" property="attention"/>
</resultMap>
<resultMap type="com.itheima.pojo.CheckGroup" 
           id="findByIdResultMap" 
           extends="baseResultMap">
  <collection property="checkItems" 
              javaType="ArrayList"
              ofType="com.itheima.pojo.CheckItem" 
              column="id"
              select="com.itheima.dao.CheckItemDao.findCheckItemById">
  </collection>
</resultMap>
<!--根据套餐id查询检查项信息-->
<select id="findCheckGroupById" resultMap="findByIdResultMap">
  select * from t_checkgroup  
    where id
  	in (select checkgroup_id from t_setmeal_checkgroup where setmeal_id=#{id})
</select>
~~~

CheckItemDao.xml文件：

~~~xml
<!--根据检查组id查询检查项信息-->
<select id="findCheckItemById" resultType="com.itheima.pojo.CheckItem">
  select * from t_checkitem  
    where id
  	in (select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{id})
</select>
~~~

## 5. 短信发送

### 5.1 短信服务介绍

目前市面上有很多第三方提供的短信服务，这些第三方短信服务会和各个运营商（移动、联通、电信）对接，我们只需要注册成为会员并且按照提供的开发文档进行调用就可以发送短信。需要说明的是这些短信服务都是收费的服务。

本项目短信发送我们选择的是阿里云提供的短信服务。

短信服务（Short Message Service）是阿里云为用户提供的一种通信服务的能力，支持快速发送短信验证码、短信通知等。 三网合一专属通道，与工信部携号转网平台实时互联。电信级运维保障，实时监控自动切换，到达率高达99%。短信服务API提供短信发送、发送状态查询、短信批量发送等能力，在短信服务控制台上添加签名、模板并通过审核之后，可以调用短信服务API完成短信发送等操作。

### 5.2 注册阿里云账号

阿里云官网：https://www.aliyun.com/

点击官网首页免费注册跳转到如下注册页面：

![71](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/71.png)

### 5.3 设置短信签名

注册成功后，点击登录按钮进行登录。登录后进入短信服务管理页面，选择国内消息菜单：

![72](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/72.png)

点击添加签名按钮：

![73](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/73.png)

目前个人用户只能申请适用场景为验证码的签名

### 5.4 设置短信模板

在国内消息菜单页面中，点击模板管理标签页：

![74](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/74.png)

点击添加模板按钮：

![75](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/75.png)

### 5.5 设置access keys

在发送短信时需要进行身份认证，只有认证通过才能发送短信。本小节就是要设置用于发送短信时进行身份认证的key和密钥。鼠标放在页面右上角当前用户头像上，会出现下拉菜单：

![76](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/76.png)

点击accesskeys：

![77](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/77.png)

点击开始使用子用户AccessKey按钮：

![78](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/78.png)

![79](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/79.png)

![710](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/710.png)

![711](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/711.png)

创建成功，其中AccessKeyID为访问短信服务时使用的ID，AccessKeySecret为密钥。

可以在用户详情页面下禁用刚刚创建的AccessKey：

![712](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/712.png)

可以设置每日和每月短信发送上限：

![713](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/713.png)

由于短信服务是收费服务，所以还需要进行充值才能发送短信：

![714](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/714.png)

### 5.6 发送短信

#### 5.6.1 导入maven坐标

```xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>3.3.1</version>
</dependency>
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-dysmsapi</artifactId>
  <version>1.0.0</version>
</dependency>
```

#### 5.6.2 封装工具类

```java
package com.itheima.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {
	public static final String VALIDATE_CODE = "SMS_159620392";//发送短信验证码
	public static final String ORDER_NOTICE = "SMS_159771588";//体检预约成功通知

	/**
	 * 发送短信
	 * @param phoneNumbers
	 * @param param
	 * @throws ClientException
	 */
	public static void sendShortMessage(String templateCode,String phoneNumbers,String param) throws ClientException{
		// 设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		// 初始化ascClient需要的几个参数
		final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
		// 替换成你的AK
		final String accessKeyId = "accessKeyId";// 你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = "accessKeySecret";// 你的accessKeySecret，参考本文档步骤2
		// 初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		// 组装请求对象
		SendSmsRequest request = new SendSmsRequest();
		// 使用post提交
		request.setMethod(MethodType.POST);
		// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		request.setPhoneNumbers(phoneNumbers);
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName("传智健康");
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(templateCode);
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{\"code\":\""+param+"\"}");
		// 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		// request.setSmsUpExtendCode("90997");
		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("yourOutId");
		// 请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			// 请求成功
			System.out.println("请求成功");
		}
	}
}
```

#### 5.6.3 测试短信发送

```java
public static void main(String[] args)throws Exception {
		SMSUtils.sendShortMessage("SMS_159620392","13812345678","1234");
}
```

# 传智健康项目  第7章

## 1. 页面静态化介绍

本章课程中我们已经实现了移动端套餐列表页面和套餐详情页面的动态展示。但是我们需要思考一个问题，就是对于这两个页面来说，每次用户访问这两个页面都需要查询数据库获取动态数据进行展示，而且这两个页面的访问量是比较大的，这就对数据库造成了很大的访问压力，并且数据库中的数据变化频率并不高。那我们需要通过什么方法为数据库减压并提高系统运行性能呢？答案就是页面静态化。

页面静态化其实就是将原来的动态网页(例如通过ajax请求动态获取数据库中的数据并展示的网页)改为通过静态化技术生成的静态网页，这样用户在访问网页时，服务器直接给用户响应静态html页面，没有了动态查询数据库的过程。

那么这些静态HTML页面还需要我们自己去编写吗？其实并不需要，我们可以通过专门的页面静态化技术帮我们生成所需的静态HTML页面，例如：Freemarker、thymeleaf等。

## 2. Freemarker介绍

FreeMarker 是一个用 Java 语言编写的模板引擎，它基于模板来生成文本输出。FreeMarker与 Web 容器无关，即在 Web 运行时，它并不知道 Servlet 或 HTTP。它不仅可以用作表现层的实现技术，而且还可以用于生成 XML，JSP 或 Java 等。

![1](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/1-1667564715614.png)

## 3. Freemarker入门案例

### 3.1 环境搭建

创建maven工程并导入Freemarker的maven坐标

~~~xml
<dependency>
  <groupId>org.freemarker</groupId>
  <artifactId>freemarker</artifactId>
  <version>2.3.23</version>
</dependency>
~~~

### 3.2 创建模板文件

模板文件中有四种元素：

  1、文本，直接输出的部分
  2、注释，即<#--...-->格式不会输出
  3、插值（Interpolation）：即${..}部分,将使用数据模型中的部分替代输出
  4、FTL指令：FreeMarker指令，和HTML标记类似，名字前加#予以区分，不会输出

Freemarker的模板文件后缀可以任意，一般建议为ftl。

在D盘创建ftl目录，在ftl目录中创建名称为test.ftl的模板文件，内容如下：

~~~html
<html>
<head>
	<meta charset="utf-8">
	<title>Freemarker入门</title>
</head>
<body>
    <#--我只是一个注释，我不会有任何输出  -->
    ${name}你好，${message}
</body>
</html>
~~~

### 3.3 生成文件

使用步骤：

第一步：创建一个 Configuration 对象，直接 new 一个对象。构造方法的参数就是 freemarker的版本号。

第二步：设置模板文件所在的路径。

第三步：设置模板文件使用的字符集。一般就是 utf-8。

第四步：加载一个模板，创建一个模板对象。

第五步：创建一个模板使用的数据集，可以是 pojo 也可以是 map。一般是 Map。

第六步：创建一个 Writer 对象，一般创建 FileWriter 对象，指定生成的文件名。

第七步：调用模板对象的 process 方法输出文件。

第八步：关闭流。

~~~java
public static void main(String[] args) throws Exception{
	//1.创建配置类
	Configuration configuration=new Configuration(Configuration.getVersion());
	//2.设置模板所在的目录 
	configuration.setDirectoryForTemplateLoading(new File("D:\\ftl"));
	//3.设置字符集
	configuration.setDefaultEncoding("utf-8");
	//4.加载模板
	Template template = configuration.getTemplate("test.ftl");
	//5.创建数据模型
	Map map=new HashMap();
	map.put("name", "张三");
	map.put("message", "欢迎来到传智播客！");
	//6.创建Writer对象
	Writer out =new FileWriter(new File("d:\\test.html"));
	//7.输出
	template.process(map, out);
	//8.关闭Writer对象
	out.close();
}
~~~

上面的入门案例中Configuration配置对象是自己创建的，字符集和模板文件所在目录也是在Java代码中指定的。在项目中应用时可以将Configuration对象的创建交由Spring框架来完成，并通过依赖注入方式将字符集和模板所在目录注入进去。

## 4. Freemarker指令

### 4.1 assign指令

assign指令用于在页面上定义一个变量

（1）定义简单类型

~~~html
<#assign linkman="周先生">
联系人：${linkman}
~~~

（2）定义对象类型

~~~html
<#assign info={"mobile":"13812345678",'address':'北京市昌平区'} >
电话：${info.mobile}  地址：${info.address}
~~~

### 4.2 include指令

include指令用于模板文件的嵌套

（1）创建模板文件head.ftl

~~~html
<h1>黑马程序员</h1>
~~~

（2）修改入门案例中的test.ftl，在test.ftl模板文件中使用include指令引入上面的模板文件

~~~html
<#include "head.ftl"/>
~~~

### 4.3 if指令

if指令用于判断

（1）在模板文件中使用if指令进行判断

~~~html
<#if success=true>
  你已通过实名认证
<#else>  
  你未通过实名认证
</#if>
~~~

（2）在java代码中为success变量赋值

~~~java
map.put("success", true);
~~~

在freemarker的判断中，可以使用= 也可以使用==

### 4.4 list指令

list指令用于遍历

（1）在模板文件中使用list指令进行遍历

~~~html
<#list goodsList as goods>
  商品名称： ${goods.name} 价格：${goods.price}<br>
</#list>
~~~

（2）在java代码中为goodsList赋值

~~~java
List goodsList=new ArrayList();

Map goods1=new HashMap();
goods1.put("name", "苹果");
goods1.put("price", 5.8);

Map goods2=new HashMap();
goods2.put("name", "香蕉");
goods2.put("price", 2.5);

Map goods3=new HashMap();
goods3.put("name", "橘子");
goods3.put("price", 3.2);

goodsList.add(goods1);
goodsList.add(goods2);
goodsList.add(goods3);

map.put("goodsList", goodsList);
~~~

## 5. 生成移动端静态页面

前面我们已经学习了Freemarker的基本使用方法，下面我们就可以将Freemarker应用到项目中，帮我们生成移动端套餐列表静态页面和套餐详情静态页面。接下来我们需要思考几个问题：

（1）什么时候生成静态页面比较合适呢？

（2）将静态页面生成到什么位置呢？

（3）应该生成几个静态页面呢？

对于第一个问题，应该是当套餐数据发生改变时，需要生成静态页面，即我们通过后台系统修改套餐数据（包括新增、删除、编辑）时。

对于第二个问题，如果是在开发阶段可以将文件生成到项目工程中，如果上线后可以将文件生成到移动端系统运行的tomcat中。

对于第三个问题，套餐列表只需要一个页面就可以了，在这个页面中展示所有的套餐列表数据即可。套餐详情页面需要有多个，即一个套餐应该对应一个静态页面。



### 5.1 环境搭建

在health_common工程的pom文件中导入Freemarker的maven坐标

~~~xml
<dependency>
  <groupId>org.freemarker</groupId>
  <artifactId>freemarker</artifactId>
  <version>2.3.23</version>
</dependency>
~~~

### 5.2 创建模板文件

在health_service_provider工程的WEB-INF目录中创建ftl目录，在ftl目录中创建模板文件mobile_setmeal.ftl和mobile_setmeal_detail.ftl文件，前者是用于生成套餐列表页面的模板文件，后者是生成套餐详情页面的模板文件

（1）mobile_setmeal.ftl

~~~html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0,user-scalable=no,minimal-ui">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../img/asset-favico.ico">
    <title>预约</title>
    <link rel="stylesheet" href="../css/page-health-order.css" />
</head>
<body data-spy="scroll" data-target="#myNavbar" data-offset="150">
<div class="app" id="app">
    <!-- 页面头部 -->
    <div class="top-header">
        <span class="f-left"><i class="icon-back" onclick="history.go(-1)"></i></span>
        <span class="center">传智健康</span>
        <span class="f-right"><i class="icon-more"></i></span>
    </div>
    <!-- 页面内容 -->
    <div class="contentBox">
        <div class="list-column1">
            <ul class="list">
                <#list setmealList as setmeal>
                    <li class="list-item">
                        <a class="link-page" href="setmeal_detail_${setmeal.id}.html">
                            <img class="img-object f-left" 
                                 src="http://puco9aur6.bkt.clouddn.com/${setmeal.img}" 
                                 alt="">
                            <div class="item-body">
                                <h4 class="ellipsis item-title">${setmeal.name}</h4>
                                <p class="ellipsis-more item-desc">${setmeal.remark}</p>
                                <p class="item-keywords">
                                    <span>
                                        <#if setmeal.sex == '0'>
                                            性别不限
                                            <#else>
                                                <#if setmeal.sex == '1'>
                                                男
                                                <#else>
                                                女
                                                </#if>
                                        </#if>
                                    </span>
                                    <span>${setmeal.age}</span>
                                </p>
                            </div>
                        </a>
                    </li>
                </#list>
            </ul>
        </div>
    </div>
</div>
<!-- 页面 css js -->
<script src="../plugins/vue/vue.js"></script>
<script src="../plugins/vue/axios-0.18.0.js"></script>
</body>
~~~

注意上面模板文件中每个套餐对应的超链接如下：

~~~html
<a class="link-page" href="setmeal_detail_${setmeal.id}.html">
~~~

可以看到，链接的地址是动态构成的，如果套餐的id为1，则对应的超链接地址为setmeal_detail_1.html；如果套餐的id为5，则对应的超链接地址为setmeal_detail_5.html。所以我们需要为每个套餐生成一个套餐详情静态页面。

（2）mobile_setmeal_detail.ftl

~~~html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0,user-scalable=no,minimal-ui">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../img/asset-favico.ico">
    <title>预约详情</title>
    <link rel="stylesheet" href="../css/page-health-orderDetail.css" />
    <script src="../plugins/vue/vue.js"></script>
    <script src="../plugins/vue/axios-0.18.0.js"></script>
    <script src="../plugins/healthmobile.js"></script>
</head>
<body data-spy="scroll" data-target="#myNavbar" data-offset="150">
<div id="app" class="app">
    <!-- 页面头部 -->
    <div class="top-header">
        <span class="f-left"><i class="icon-back" onclick="history.go(-1)"></i></span>
        <span class="center">传智健康</span>
        <span class="f-right"><i class="icon-more"></i></span>
    </div>
    <!-- 页面内容 -->
    <div class="contentBox">
        <div class="card">
            <div class="project-img">
                <img src="http://puco9aur6.bkt.clouddn.com/${setmeal.img}" 
                     width="100%" height="100%" />
            </div>
            <div class="project-text">
                <h4 class="tit">${setmeal.name}</h4>
                <p class="subtit">${setmeal.remark}</p>
                <p class="keywords">
                    <span>
						<#if setmeal.sex == '0'>
							性别不限
							<#else>
								<#if setmeal.sex == '1'>
								男
								<#else>
								女
								</#if>
						</#if>
					</span>
                    <span>${setmeal.age}</span>
                </p>
            </div>
        </div>
        <div class="table-listbox">
            <div class="box-title">
                <i class="icon-zhen"><span class="path1"></span><span class="path2"></span></i>
                <span>套餐详情</span>
            </div>
            <div class="box-table">
                <div class="table-title">
                    <div class="tit-item flex2">项目名称</div>
                    <div class="tit-item  flex3">项目内容</div>
                    <div class="tit-item  flex3">项目解读</div>
                </div>
                <div class="table-content">
                    <ul class="table-list">
						<#list setmeal.checkGroups as checkgroup>
							<li class="table-item">
								<div class="item flex2">${checkgroup.name}</div>
								<div class="item flex3">
									<#list checkgroup.checkItems as checkitem>
										<label>
											${checkitem.name}
										</label>
									</#list>
								</div>
								<div class="item flex3">${checkgroup.remark}</div>
							</li>
						</#list>
                    </ul>
                </div>
                <div class="box-button">
                    <a @click="toOrderInfo()" class="order-btn">立即预约</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var vue = new Vue({
        el:'#app',
        methods:{
            toOrderInfo(){
                window.location.href = "orderInfo.html?id=${setmeal.id}";
            }
        }
    });
</script>
</body>
~~~

### 5.3 配置文件

（1）在health_service_provider工程中创建属性文件freemarker.properties

~~~makefile
out_put_path=D:/ideaProjects/health_parent/health_mobile/src/main/webapp/pages
~~~

通过上面的配置可以指定将静态HTML页面生成的目录位置

（2）在health_service_provider工程的Spring配置文件中配置

~~~xml
<bean id="freemarkerConfig" 
      class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
  <!--指定模板文件所在目录-->
  <property name="templateLoaderPath" value="/WEB-INF/ftl/" />
  <!--指定字符集-->
  <property name="defaultEncoding" value="UTF-8" />
</bean>
<context:property-placeholder location="classpath:freemarker.properties"/>
~~~

### 5.4 生成静态页面

修改health_service_provider工程中的SetmealServiceImpl类的add方法，加入生成静态页面的逻辑。

~~~java
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
  @Autowired
  private FreeMarkerConfigurer freeMarkerConfigurer;
  @Autowired
  private SetmealDao setmealDao;
  @Autowired
  private JedisPool jedisPool;
  
  @Value("${out_put_path}")//从属性文件读取输出目录的路径
  private String outputpath ;

  //新增套餐，同时关联检查组
  public void add(Setmeal setmeal, Integer[] checkgroupIds) {
    setmealDao.add(setmeal);
    Integer setmealId = setmeal.getId();//获取套餐id
    this.setSetmealAndCheckGroup(setmealId,checkgroupIds);
    //完成数据库操作后需要将图片名称保存到redis
    jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());

    //新增套餐后需要重新生成静态页面
    generateMobileStaticHtml();
  }
  
  //生成静态页面
  public void generateMobileStaticHtml() {
    //准备模板文件中所需的数据
    List<Setmeal> setmealList = this.findAll();
    //生成套餐列表静态页面
    generateMobileSetmealListHtml(setmealList);
    //生成套餐详情静态页面（多个）
    generateMobileSetmealDetailHtml(setmealList);
  }
  
  //生成套餐列表静态页面
  public void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("setmealList", setmealList);
    this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",dataMap);
  }
  
  //生成套餐详情静态页面（多个）
  public void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
    for (Setmeal setmeal : setmealList) {
      Map<String, Object> dataMap = new HashMap<String, Object>();
      dataMap.put("setmeal", this.findById(setmeal.getId()));
      this.generateHtml("mobile_setmeal_detail.ftl",
                        "setmeal_detail_"+setmeal.getId()+".html",
                        dataMap);
    }
  }
  
  public void generateHtml(String templateName,String htmlPageName,Map<String, Object> dataMap){
    Configuration configuration = freeMarkerConfigurer.getConfiguration();
    Writer out = null;
    try {
      // 加载模版文件
      Template template = configuration.getTemplate(templateName);
      // 生成数据
      File docFile = new File(outputpath + "\\" + htmlPageName);
      out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
      // 输出文件
      template.process(dataMap, out);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != out) {
          out.flush();
        }
      } catch (Exception e2) {
        e2.printStackTrace();
      }
    }
  }
}
~~~

通过上面代码可以看到，我们生成的套餐列表页面名称为m_setmeal.html，为了能够在移动端访问到此页面，需要将移动端工程中的/pages/index.html页面的超链接地址进行修改：

~~~html
<a href="/pages/m_setmeal.html" class="link-page">
  <div class="type-title">
    <h3>体检预约</h3>
    <p>实时预约</p>
  </div>
  <div class="type-icon">
    <i class="icon-zhen">
      <span class="path1"></span><span class="path2"></span>
    </i>
  </div>
</a>
~~~

# 第8章 移动端开发-体检预约

## 1. 体检预约流程

用户可以通过如下操作流程进行体检预约：

1、在移动端首页点击体检预约，页面跳转到套餐列表页面

2、在套餐列表页面点击要预约的套餐，页面跳转到套餐详情页面

3、在套餐详情页面点击立即预约，页面跳转到预约页面

4、在预约页面录入体检人信息，包括手机号，点击发送验证码

5、在预约页面录入收到的手机短信验证码，点击提交预约，完成体检预约

## 2. 体检预约

### 2.1 页面调整

在预约页面（/pages/orderInfo.html）进行调整

#### 2.1.1 展示预约的套餐信息

第一步：从请求路径中获取当前套餐的id

~~~javascript
<script>
  var id = getUrlParam("id");//套餐id
</script>
~~~

第二步：定义模型数据setmeal，用于套餐数据展示

~~~javascript
var vue = new Vue({
  el:'#app',
  data:{
    setmeal:{},//套餐信息
    orderInfo:{
      setmealId:id,
      sex:'1'
    }//预约信息
  }
});
~~~

~~~html
<div class="card">
  <div class="">
    <img :src="'http://pqjroc654.bkt.clouddn.com/'+setmeal.img" width="100%" height="100%" />
  </div>
  <div class="project-text">
    <h4 class="tit">{{setmeal.name}}</h4>
    <p class="subtit">{{setmeal.remark}}</p>
    <p class="keywords">
        <span>{{setmeal.sex == '0' ? '性别不限' : setmeal.sex == '1' ? '男':'女'}}</span>
        <span>{{setmeal.age}}</span>
    </p>
  </div>
  <div class="project-know">
    <a href="orderNotice.html" class="link-page">
      <i class="icon-ask-circle"><span class="path1"></span><span class="path2"></span></i>
      <span class="word">预约须知</span>
      <span class="arrow"><i class="icon-rit-arrow"></i></span>
    </a>
  </div>
</div>
~~~

第三步：在VUE的钩子函数中发送ajax请求，根据id查询套餐信息

~~~javascript
mounted(){
  axios.post("/setmeal/findById.do?id=" + id).then((response) => {
    this.setmeal = response.data.data;
  });
}
~~~

#### 2.1.2 手机号校验

第一步：在页面导入的healthmobile.js文件中已经定义了校验手机号的方法

~~~javascript
/**
 * 手机号校验
 1--以1为开头；
 2--第二位可为3,4,5,7,8,中的任意一位；
 3--最后以0-9的9个整数结尾。
 */
function checkTelephone(telephone) {
    var reg=/^[1][3,4,5,7,8][0-9]{9}$/;
    if (!reg.test(telephone)) {
        return false;
    } else {
        return true;
    }
}
~~~

第二步：为发送验证码按钮绑定事件sendValidateCode

~~~html
<div class="input-row">
  <label>手机号</label>
  <input v-model="orderInfo.telephone" 
         type="text" class="input-clear" placeholder="请输入手机号">
  <input style="font-size: x-small;" 
         id="validateCodeButton" @click="sendValidateCode()" type="button" value="发送验证码">
</div>
~~~

~~~javascript
//发送验证码
sendValidateCode(){
  //获取用户输入的手机号
  var telephone = this.orderInfo.telephone;
  //校验手机号输入是否正确
  if (!checkTelephone(telephone)) {
    this.$message.error('请输入正确的手机号');
    return false;
  }
}
~~~

#### 2.1.3 30秒倒计时效果

前面在sendValidateCode方法中进行了手机号校验，如果校验通过，需要显示30秒倒计时效果

~~~javascript
//发送验证码
sendValidateCode(){
  //获取用户输入的手机号
  var telephone = this.orderInfo.telephone;
  //校验手机号输入是否正确
  if (!checkTelephone(telephone)) {
    this.$message.error('请输入正确的手机号');
    return false;
  }
  validateCodeButton = $("#validateCodeButton")[0];
  clock = window.setInterval(doLoop, 1000); //一秒执行一次
}
~~~

其中，validateCodeButton和clock是在healthmobile.js文件中定义的变量，doLoop是在healthmobile.js文件中定义的方法

~~~javascript
var clock = '';//定时器对象，用于页面30秒倒计时效果
var nums = 30;
var validateCodeButton;
//基于定时器实现30秒倒计时效果
function doLoop() {
    validateCodeButton.disabled = true;//将按钮置为不可点击
    nums--;
    if (nums > 0) {
        validateCodeButton.value = nums + '秒后重新获取';
    } else {
        clearInterval(clock); //清除js定时器
        validateCodeButton.disabled = false;
        validateCodeButton.value = '重新获取验证码';
        nums = 30; //重置时间
    }
}
~~~

#### 2.1.4 发送ajax请求

在按钮上显示30秒倒计时效果的同时，需要发送ajax请求，在后台给用户发送手机验证码

~~~javascript
//发送验证码
sendValidateCode(){
  //获取用户输入的手机号
  var telephone = this.orderInfo.telephone;
  //校验手机号输入是否正确
  if (!checkTelephone(telephone)) {
    this.$message.error('请输入正确的手机号');
    return false;
  }
  validateCodeButton = $("#validateCodeButton")[0];
  clock = window.setInterval(doLoop, 1000); //一秒执行一次
  axios.post("/validateCode/send4Order.do?telephone=" + telephone).then((response) => {
    if(!response.data.flag){
      //验证码发送失败
      this.$message.error('验证码发送失败，请检查手机号输入是否正确');
    }
  });
}
~~~

创建ValidateCodeController，提供方法发送短信验证码，并将验证码保存到redis

~~~java
package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.JedisUtils;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.Random;

/**
 * 短信验证码
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
  
    //体检预约时发送手机验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(4);//生成4位数字验证码
        try {
            //发送短信
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            //验证码发送失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为：" + code);
        //将生成的验证码缓存到redis
        jedisPool.getResource().setex(
          telephone + RedisMessageConstant.SENDTYPE_ORDER,5 * 60,code.toString());
        //验证码发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
~~~

#### 2.1.5 日历展示

页面中使用DatePicker控件来展示日历。根据需求，最多可以提前一个月进行体检预约，所以日历控件只展示未来一个月的日期

~~~html
<div class="date">
  <label>体检日期</label>
  <i class="icon-date" class="picktime"></i>
  <input v-model="orderInfo.orderDate" type="text" class="picktime" readonly>
</div>
~~~

~~~javascript
<script>
  //日期控件
  var calendar = new datePicker();
  calendar.init({
    'trigger': '.picktime',/*按钮选择器，用于触发弹出插件*/
    'type': 'date',/*模式：date日期；datetime日期时间；time时间；ym年月；*/
    'minDate': getSpecifiedDate(new Date(),1),/*最小日期*/
    'maxDate': getSpecifiedDate(new Date(),30),/*最大日期*/
    'onSubmit': function() { /*确认时触发事件*/},
    'onClose': function() { /*取消时触发事件*/ }
  });
</script>
~~~

其中getSpecifiedDate方法定义在healthmobile.js文件中

~~~javascript
//获得指定日期后指定天数的日期
function getSpecifiedDate(date,days) {
    date.setDate(date.getDate() + days);//获取指定天之后的日期
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    return (year + "-" + month + "-" + day);
}
~~~

#### 2.1.6 提交预约请求

为提交预约按钮绑定事件

~~~html
<div class="box-button">
  <button @click="submitOrder()" type="button" class="btn order-btn">提交预约</button>
</div>
~~~

~~~javascript
//提交预约
submitOrder(){
  //校验身份证号格式
  if(!checkIdCard(this.orderInfo.idCard)){
    this.$message.error('身份证号码输入错误，请重新输入');
    return ;
  }
  axios.post("/order/submit.do",this.orderInfo).then((response) => {
    if(response.data.flag){
      //预约成功，跳转到预约成功页面
      window.location.href="orderSuccess.html?orderId=" + response.data.data;
    }else{
      //预约失败，提示预约失败信息
      this.$message.error(response.data.message);
    }
  });
}
~~~

其中checkIdCard方法是在healthmobile.js文件中定义的

~~~javascript
/**
 * 身份证号码校验
 * 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
 */
function checkIdCard(idCard){
    var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if(reg.test(idCard)){
        return true;
    }else{
        return false;
    }
}
~~~

### 2.2 后台代码

#### 2.2.1 Controller

在health_mobile工程中创建OrderController并提供submitOrder方法

~~~java
package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderService;
import com.itheima.utils.JedisUtils;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import java.util.HashMap;
import java.util.Map;

/**
 * 体检预约
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约
     * @param map
     * @return
     */
    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
        String codeInRedis = jedisPool.getResource().get(
          telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        //校验手机验证码
        if(codeInRedis == null || !codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result =null;
        //调用体检预约服务
        try{
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.order(map);
        }catch (Exception e){
            e.printStackTrace();
            //预约失败
            return result;
        }
        if(result.isFlag()){
            //预约成功，发送短信通知
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
~~~

#### 注意点

> 接受post参数，没有对应的实体类，可以用Map接收，Map比较灵活。注意需要加@RequestBody注解

#### 2.2.2 服务接口

在health_interface工程中创建体检预约服务接口OrderService并提供预约方法

~~~java
package com.itheima.service;

import com.itheima.entity.Result;
import java.util.Map;
/**
 * 体检预约服务接口
 */
public interface OrderService {
    //体检预约
    public Result order(Map map) throws Exception;
}
~~~

#### 2.2.3 服务实现类

在health_service_provider工程中创建体检预约服务实现类OrderServiceImpl并实现体检预约方法。

体检预约方法处理逻辑比较复杂，需要进行如下业务处理：

1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约

2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约

3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约

4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约

5、预约成功，更新当日的已预约人数

实现代码如下：

~~~java
package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;
import com.itheima.utils.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 体检预约服务
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
  
    //体检预约
    public Result order(Map map) throws Exception {
   		//检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
      
        //检查预约日期是否预约已满
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if(reservations >= number){
            //预约已满，不能预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }
      
        //检查当前用户是否为会员，根据手机号判断
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        //防止重复预约
        if(member != null){
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId,date,null,null,setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if(list != null && list.size() > 0){
                //已经完成了预约，不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        
      	//可以预约，设置预约人数加一
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        if(member == null){
            //当前用户不是会员，需要添加到会员表
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
      
        //保存预约信息到预约表
        Order order = new Order(member.getId(),
                                date,
                                (String)map.get("orderType"),
                                Order.ORDERSTATUS_NO,
                                Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);
      
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }
}
~~~

#### 2.2.4 Dao接口

~~~java
package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);
    //更新可预约人数
    public void editNumberByOrderDate(OrderSetting orderSetting);
    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
    public long findCountByOrderDate(Date orderDate);
    //根据日期范围查询预约设置信息
    public List<OrderSetting> getOrderSettingByMonth(Map date);
    //根据预约日期查询预约设置信息
    public OrderSetting findByOrderDate(Date orderDate);
}

~~~

~~~java
package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;
import java.util.List;

public interface MemberDao {
    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);
    public void add(Member member);
    public void deleteById(Integer id);
    public Member findById(Integer id);
    public Member findByTelephone(String telephone);
    public void edit(Member member);
    public Integer findMemberCountBeforeDate(String date);
    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();
}
~~~

~~~java
package com.itheima.dao;

import com.itheima.pojo.Order;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    public void add(Order order);
    public List<Order> findByCondition(Order order);
}
~~~

#### 2.2.5 Mapper映射文件

OrderSettingDao.xml

~~~xml
<!--根据日期查询预约设置信息-->
<select id="findByOrderDate" parameterType="date" resultType="com.itheima.pojo.OrderSetting">
  select * from t_ordersetting where orderDate = #{orderDate}
</select>
<!--更新已预约人数-->
<update id="editReservationsByOrderDate" parameterType="com.itheima.pojo.OrderSetting">
  update t_ordersetting set reservations = #{reservations} where orderDate = #{orderDate}
</update>
~~~

MemberDao.xml

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.itheima.dao.MemberDao" >
    <select id="findAll" resultType="com.itheima.pojo.Member">
        select * from t_member
    </select>

    <!--根据条件查询-->
    <select id="selectByCondition" 
            parameterType="string" resultType="com.itheima.pojo.Member">
        select * from t_member
        <if test="value != null and value.length > 0">
            where fileNumber = #{value} or phoneNumber = #{value} or name = #{value}
        </if>
    </select>

    <!--新增会员-->
    <insert id="add" parameterType="com.itheima.pojo.Member">
        <selectKey resultType="java.lang.Integer" order="AFTER" keyProperty="id">
            SELECT LAST_INSERT_ID()
        </selectKey>
        insert into 
      		t_member
              (fileNumber,name,sex,idCard,phoneNumber,
               regTime,password,email,birthday,remark)
             values
              (#{fileNumber},#{name},#{sex},#{idCard},#{phoneNumber},
               #{regTime},#{password},#{email},#{birthday},#{remark})
    </insert>

    <!--删除会员-->
    <delete id="deleteById" parameterType="int">
        delete from t_member where id = #{id}
    </delete>

    <!--根据id查询会员-->
    <select id="findById" parameterType="int" resultType="com.itheima.pojo.Member">
        select * from t_member where id = #{id}
    </select>

    <!--根据id查询会员-->
    <select id="findByTelephone" 
            parameterType="string" resultType="com.itheima.pojo.Member">
        select * from t_member where phoneNumber = #{phoneNumber}
    </select>

    <!--编辑会员-->
    <update id="edit" parameterType="com.itheima.pojo.Member">
        update t_member
        <set>
            <if test="fileNumber != null">
                fileNumber = #{fileNumber},
            </if>
            <if test="name != null">
                name = #{name},
            </if>
            <if test="sex != null">
                sex = #{sex},
            </if>
            <if test="idCard != null">
                idCard = #{idCard},
            </if>
            <if test="phoneNumber != null">
                phoneNumber = #{phoneNumber},
            </if>
            <if test="regTime != null">
                regTime = #{regTime},
            </if>
            <if test="password != null">
                password = #{password},
            </if>
            <if test="email != null">
                email = #{email},
            </if>
            <if test="birthday != null">
                birthday = #{birthday},
            </if>
            <if test="remark != null">
                remark = #{remark},
            </if>
        </set>
        where id = #{id}
    </update>

    <!--根据日期统计会员数，统计指定日期之前的会员数-->
    <select id="findMemberCountBeforeDate" parameterType="string" resultType="int">
        select count(id) from t_member where regTime &lt;= #{value}
    </select>

    <!--根据日期统计会员数-->
    <select id="findMemberCountByDate" parameterType="string" resultType="int">
        select count(id) from t_member where regTime = #{value}
    </select>

    <!--根据日期统计会员数，统计指定日期之后的会员数-->
    <select id="findMemberCountAfterDate" parameterType="string" resultType="int">
        select count(id) from t_member where regTime &gt;= #{value}
    </select>

    <!--总会员数-->
    <select id="findMemberTotalCount" resultType="int">
        select count(id) from t_member
    </select>
</mapper>
~~~

OrderDao.xml

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.itheima.dao.OrderDao" >
    <resultMap id="baseResultMap" type="com.itheima.pojo.Order">
        <id column="id" property="id"/>
        <result column="member_id" property="memberId"/>
        <result column="orderDate" property="orderDate"/>
        <result column="orderType" property="orderType"/>
        <result column="orderStatus" property="orderStatus"/>
        <result column="setmeal_id" property="setmealId"/>
    </resultMap>
    <!--新增-->
    <insert id="add" parameterType="com.itheima.pojo.Order">
        <selectKey resultType="java.lang.Integer" order="AFTER" keyProperty="id">
            SELECT LAST_INSERT_ID()
        </selectKey>
        insert into 
      		t_order
      	(member_id,orderDate,orderType,orderStatus,setmeal_id)
        	values 
      	(#{memberId},#{orderDate},#{orderType},#{orderStatus},#{setmealId})
    </insert>

    <!--动态条件查询-->
    <select id="findByCondition" 
            parameterType="com.itheima.pojo.Order" 
            resultMap="baseResultMap">
        select * from t_order
        <where>
            <if test="id != null">
                and id = #{id}
            </if>
            <if test="memberId != null">
                and member_id = #{memberId}
            </if>
            <if test="orderDate != null">
                and orderDate = #{orderDate}
            </if>
            <if test="orderType != null">
                and orderType = #{orderType}
            </if>
            <if test="orderStatus != null">
                and orderStatus = #{orderStatus}
            </if>
            <if test="setmealId != null">
                and setmeal_id = #{setmealId}
            </if>
        </where>
    </select>
</mapper>
~~~

## 3. 预约成功页面展示

前面已经完成了体检预约，预约成功后页面会跳转到成功提示页面（orderSuccess.html）并展示预约的相关信息（体检人、体检套餐、体检时间等）。

### 3.1 页面调整

提供orderSuccess.html页面，展示预约成功后相关信息

~~~html
<div class="info-title">
  <span class="name">体检预约成功</span>
</div>
<div class="notice-item">
  <div class="item-title">预约信息</div>
  <div class="item-content">
    <p>体检人：{{orderInfo.member}}</p>
    <p>体检套餐：{{orderInfo.setmeal}}</p>
    <p>体检日期：{{orderInfo.orderDate}}</p>
    <p>预约类型：{{orderInfo.orderType}}</p>
  </div>
</div>
~~~

~~~javascript
<script>
  //从请求URL根据参数名获取对应值，orderId为预约id
  var id = getUrlParam("orderId");
</script>
<script>
  var vue = new Vue({
    el:'#app',
    data:{
      orderInfo:{}
    },
    mounted(){
      axios.post("/order/findById.do?id=" + id).then((response) => {
        this.orderInfo = response.data.data;
      });
    }
  });
</script>
~~~

### 3.2 后台代码

#### 3.2.1 Controller

在OrderController中提供findById方法，根据预约id查询预约相关信息

~~~java
/**
 * 根据id查询预约信息，包括套餐信息和会员信息
 * @param id
 * @return
*/
@RequestMapping("/findById")
public Result findById(Integer id){
  try{
    Map map = orderService.findById(id);
    //查询预约信息成功
    return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
  }catch (Exception e){
    e.printStackTrace();
    //查询预约信息失败
    return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
  }
}
~~~

#### 3.2.2 服务接口

在OrderService服务接口中扩展findById方法

~~~java
//根据id查询预约信息，包括体检人信息、套餐信息
public Map findById(Integer id) throws Exception;
~~~

#### 3.2.3 服务实现类

在OrderServiceImpl服务实现类中实现findById方法

~~~java
//根据id查询预约信息，包括体检人信息、套餐信息
public Map findById(Integer id) throws Exception {
  Map map = orderDao.findById4Detail(id);
  if(map != null){
    //处理日期格式
    Date orderDate = (Date) map.get("orderDate");
    map.put("orderDate",DateUtils.parseDate2String(orderDate));
  }
  return map;
}
~~~

#### 注意点

> 返回的参数和实体类无法对应，也可以返回一个Map

#### 3.2.4 Dao接口

在OrderDao接口中扩展findById4Detail方法

~~~java
public Map findById4Detail(Integer id);
~~~

#### 3.2.5 Mapper映射文件

在OrderDao.xml映射文件中提供SQL语句

~~~xml
<!--根据预约id查询预约信息，包括体检人信息、套餐信息-->
<select id="findById4Detail" parameterType="int" resultType="map">
  select m.name member ,s.name setmeal,o.orderDate orderDate,o.orderType orderType
  from
  t_order o,
  t_member m,
  t_setmeal s
  where o.member_id=m.id and o.setmeal_id=s.id and o.id=#{id}
</select>
~~~

# 第9章 移动端开发-手机快速登录、权限控制

## 1. 需求分析

手机快速登录功能，就是通过短信验证码的方式进行登录。这种方式相对于用户名密码登录方式，用户不需要记忆自己的密码，只需要通过输入手机号并获取验证码就可以完成登录，是目前比较流行的登录方式。

![5](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/5-1667656312442.png)



![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2-1667656312442.png)

## 2. 手机快速登录

### 2.1 页面调整

登录页面为/pages/login.html

#### 2.1.1 发送验证码

为获取验证码按钮绑定事件，并在事件对应的处理函数中校验手机号，如果手机号输入正确则显示30秒倒计时效果并发送ajax请求，发送短信验证码

~~~html
<div class="input-row">
  <label>手机号</label>
  <div class="loginInput">
    <input v-model="loginInfo.telephone" id='account' type="text" 
           placeholder="请输入手机号">
    <input id="validateCodeButton" 
           @click="sendValidateCode()" type="button" style="font-size: 12px" 
           value="获取验证码">
  </div>
</div>
~~~

~~~javascript
<script>
  var vue = new Vue({
    el:'#app',
    data:{
      loginInfo:{}//登录信息
    },
    methods:{
      //发送验证码
      sendValidateCode(){
        var telephone = this.loginInfo.telephone;
        if (!checkTelephone(telephone)) {
          this.$message.error('请输入正确的手机号');
          return false;
        }
        validateCodeButton = $("#validateCodeButton")[0];
        clock = window.setInterval(doLoop, 1000); //一秒执行一次
        axios.
        post("/validateCode/send4Login.do?telephone=" + telephone).
        then((response) => {
          if(!response.data.flag){
            //验证码发送失败
            this.$message.error('验证码发送失败，请检查手机号输入是否正确');
          }
        });
      }
    }
  });
</script>
~~~

在ValidateCodeController中提供send4Login方法，调用短信服务发送验证码并将验证码保存到redis

~~~java
//手机快速登录时发送手机验证码
@RequestMapping("/send4Login")
public Result send4Login(String telephone){
  Integer code = ValidateCodeUtils.generateValidateCode(6);//生成6位数字验证码
  try {
    //发送短信
    SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
  } catch (ClientException e) {
    e.printStackTrace();
    //验证码发送失败
    return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
  }
  System.out.println("发送的手机验证码为：" + code);
  //将生成的验证码缓存到redis
  jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,
                                5 * 60,
                                code.toString());
  //验证码发送成功
  return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
}
~~~

#### 2.1.2 提交登录请求

为登录按钮绑定事件

~~~javascript
<div class="btn yes-btn"><a @click="login()" href="#">登录</a></div>
~~~

~~~javascript
//登录
login(){
  var telephone = this.loginInfo.telephone;
  if (!checkTelephone(telephone)) {
    this.$message.error('请输入正确的手机号');
    return false;
  }
  axios.post("/member/login.do",this.loginInfo).then((response) => {
    if(response.data.flag){
      //登录成功,跳转到会员页面
      window.location.href="member.html";
    }else{
      //失败，提示失败信息
      this.$message.error(response.data.message);
    }
  });
}
~~~

### 2.2 后台代码

#### 2.2.1 Controller

在health_mobile工程中创建MemberController并提供login方法进行登录检查，处理逻辑为：

1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败

2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册

3、向客户端写入Cookie，内容为用户手机号

4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟

~~~java
package com.itheima.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
/**
 * 会员登录
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;
  
    //使用手机号和验证码登录
    @RequestMapping("/login")
    public Result login(HttpServletResponse response,@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从Redis中获取缓存的验证码
        String codeInRedis = 
            jedisPool.getResource().get(telephone+RedisMessageConstant.SENDTYPE_LOGIN);
        if(codeInRedis == null || !codeInRedis.equals(validateCode)){
            //验证码输入错误
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }else{
            //验证码输入正确
            //判断当前用户是否为会员
            Member member = memberService.findByTelephone(telephone);
            if(member == null){
                //当前用户不是会员，自动完成注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
            //登录成功
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);
            //保存会员信息到Redis中
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
    }
}
~~~

#### 2.2.2 服务接口

在MemberService服务接口中提供findByTelephone和add方法

~~~java
public void add(Member member);
public Member findByTelephone(String telephone);
~~~

#### 2.2.3 服务实现类

在MemberServiceImpl服务实现类中实现findByTelephone和add方法

~~~java
//根据手机号查询会员
public Member findByTelephone(String telephone) {
  return memberDao.findByTelephone(telephone);
}
//新增会员
public void add(Member member) {
  if(member.getPassword() != null){
    member.setPassword(MD5Utils.md5(member.getPassword()));
  }
  memberDao.add(member);
}
~~~

#### 2.2.4 Dao接口

在MemberDao接口中声明findByTelephone和add方法

~~~java
public Member findByTelephone(String telephone);
public void add(Member member);
~~~

#### 2.2.5 Mapper映射文件

在MemberDao.xml映射文件中定义SQL语句

~~~xml
<!--新增会员-->
<insert id="add" parameterType="com.itheima.pojo.Member">
  <selectKey resultType="java.lang.Integer" order="AFTER" keyProperty="id">
    SELECT LAST_INSERT_ID()
  </selectKey>
  insert into t_member
  (fileNumber,name,sex,idCard,phoneNumber,regTime,password,email,birthday,remark)
  values 
  (#{fileNumber},#{name},#{sex},#{idCard},#{phoneNumber},#{regTime},#{password},#{email},#{birthday},#{remark})
</insert>
<!--根据手机号查询会员-->
<select id="findByTelephone" parameterType="string" resultType="com.itheima.pojo.Member">
  select * from t_member where phoneNumber = #{phoneNumber}
</select>
~~~

## 3. 权限控制

### 3.1 认证和授权概念

前面我们已经完成了传智健康后台管理系统的部分功能，例如检查项管理、检查组管理、套餐管理、预约设置等。接下来我们需要思考2个问题：

问题1：在生产环境下我们如果不登录后台系统就可以完成这些功能操作吗？

答案显然是否定的，要操作这些功能必须首先登录到系统才可以。

问题2：是不是所有用户，只要登录成功就都可以操作所有功能呢？

答案是否定的，并不是所有的用户都可以操作这些功能。不同的用户可能拥有不同的权限，这就需要进行授权了。



认证：系统提供的用于识别用户身份的功能，通常提供用户名和密码进行登录其实就是在进行认证，认证的目的是让系统知道你是谁。

授权：用户认证成功后，需要为用户授权，其实就是指定当前用户可以操作哪些功能。

本章节就是要对后台系统进行权限控制，其本质就是对用户进行认证和授权。

### 3.2 权限模块数据模型

前面已经分析了认证和授权的概念，要实现最终的权限控制，需要有一套表结构支撑：

用户表t_user、权限表t_permission、角色表t_role、菜单表t_menu、用户角色关系表t_user_role、角色权限关系表t_role_permission、角色菜单关系表t_role_menu。

表之间关系如下图：

![3](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/3-1667656312442.png)

通过上图可以看到，权限模块共涉及到7张表。在这7张表中，角色表起到了至关重要的作用，其处于核心位置，因为用户、权限、菜单都和角色是多对多关系。

接下来我们可以分析一下在认证和授权过程中分别会使用到哪些表：

认证过程：只需要用户表就可以了，在用户登录时可以查询用户表t_user进行校验，判断用户输入的用户名和密码是否正确。

授权过程：用户必须完成认证之后才可以进行授权，首先可以根据用户查询其角色，再根据角色查询对应的菜单，这样就确定了用户能够看到哪些菜单。然后再根据用户的角色查询对应的权限，这样就确定了用户拥有哪些权限。所以授权过程会用到上面7张表。

### 3.3 Spring Security简介

Spring Security是 Spring提供的安全认证服务的框架。 使用Spring Security可以帮助我们来简化认证和授权的过程。官网：https://spring.io/projects/spring-security

![4](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/4-1667656312443.png)

对应的maven坐标：

~~~xml
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-web</artifactId>
  <version>5.0.5.RELEASE</version>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-config</artifactId>
  <version>5.0.5.RELEASE</version>
</dependency>
~~~

常用的权限框架除了Spring Security，还有Apache的shiro框架。

### 3.4 Spring Security入门案例

#### 3.4.1 工程搭建

创建maven工程，打包方式为war，为了方便起见我们可以让入门案例工程依赖health_interface，这样相关的依赖都继承过来了。

pom.xml

~~~xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.itheima</groupId>
  <artifactId>springsecuritydemo</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>war</packaging>

  <name>springsecuritydemo Maven Webapp</name>
  <url>http://www.example.com</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
  </properties>

  <dependencies>
    <dependency>
      <groupId>com.itheima</groupId>
      <artifactId>health_interface</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.tomcat.maven</groupId>
        <artifactId>tomcat7-maven-plugin</artifactId>
        <configuration>
          <!-- 指定端口 -->
          <port>85</port>
          <!-- 请求路径 -->
          <path>/</path>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
~~~

提供index.html页面，内容为hello Spring Security!!

#### 3.4.2 配置web.xml

在web.xml中主要配置SpringMVC的DispatcherServlet和用于整合第三方框架的DelegatingFilterProxy，用于整合Spring Security。

~~~xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <filter>
    <!--
      DelegatingFilterProxy用于整合第三方框架
      整合Spring Security时过滤器的名称必须为springSecurityFilterChain，
	  否则会抛出NoSuchBeanDefinitionException异常
    -->
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!-- 指定加载的配置文件 ，通过参数contextConfigLocation加载 -->
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:spring-security.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>*.do</url-pattern>
  </servlet-mapping>

</web-app>
~~~

#### 3.4.3 配置spring-security.xml

在spring-security.xml中主要配置Spring Security的拦截规则和认证管理器。

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:security="http://www.springframework.org/schema/security"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
						http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/mvc
						http://www.springframework.org/schema/mvc/spring-mvc.xsd
						http://code.alibabatech.com/schema/dubbo
						http://code.alibabatech.com/schema/dubbo/dubbo.xsd
						http://www.springframework.org/schema/context
						http://www.springframework.org/schema/context/spring-context.xsd
                          http://www.springframework.org/schema/security
                          http://www.springframework.org/schema/security/spring-security.xsd">

    <!--
        http：用于定义相关权限控制
        auto-config：是否自动配置
                        设置为true时框架会提供默认的一些配置，例如提供默认的登录页面、登出处理等
                        设置为false时需要显示提供登录表单配置，否则会报错
        use-expressions：用于指定intercept-url中的access属性是否使用表达式
    -->
    <security:http auto-config="true" use-expressions="true">
        <!--
            intercept-url：定义一个拦截规则
            pattern：对哪些url进行权限控制
            access：在请求对应的URL时需要什么权限，默认配置时它应该是一个以逗号分隔的角色列表，
				  请求的用户只需拥有其中的一个角色就能成功访问对应的URL
        -->
        <security:intercept-url pattern="/**"  access="hasRole('ROLE_ADMIN')" />
    </security:http>

    <!--
        authentication-manager：认证管理器，用于处理认证操作
    -->
    <security:authentication-manager>
        <!--
            authentication-provider：认证提供者，执行具体的认证逻辑
        -->
        <security:authentication-provider>
            <!--
                user-service：用于获取用户信息，提供给authentication-provider进行认证
            -->
            <security:user-service>
                <!--
                    user：定义用户信息，可以指定用户名、密码、角色，后期可以改为从数据库查询用户信息
				  {noop}：表示当前使用的密码为明文
                -->
                <security:user name="admin" 
                               password="{noop}admin" 
                               authorities="ROLE_ADMIN">
              	</security:user>
            </security:user-service>
        </security:authentication-provider>
    </security:authentication-manager>
</beans>
~~~

### 3.5 对入门案例改进

前面我们已经完成了Spring Security的入门案例，通过入门案例我们可以看到，Spring Security将我们项目中的所有资源都保护了起来，要访问这些资源必须要完成认证而且需要具有ROLE_ADMIN角色。

但是入门案例中的使用方法离我们真实生产环境还差很远，还存在如下一些问题：

1、项目中我们将所有的资源（所有请求URL）都保护起来，实际环境下往往有一些资源不需要认证也可以访问，也就是可以匿名访问。

2、登录页面是由框架生成的，而我们的项目往往会使用自己的登录页面。

3、直接将用户名和密码配置在了配置文件中，而真实生产环境下的用户名和密码往往保存在数据库中。

4、在配置文件中配置的密码使用明文，这非常不安全，而真实生产环境下密码需要进行加密。

本章节需要对这些问题进行改进。

#### 3.5.1 配置可匿名访问的资源

第一步：在项目中创建pages目录，在pages目录中创建a.html和b.html

第二步：在spring-security.xml文件中配置，指定哪些资源可以匿名访问

~~~xml
<!--
  http：用于定义相关权限控制
  指定哪些资源不需要进行权限校验，可以使用通配符
-->
<security:http security="none" pattern="/pages/a.html" />
<security:http security="none" pattern="/paegs/b.html" />
<security:http security="none" pattern="/pages/**"></security:http>
~~~

通过上面的配置可以发现，pages目录下的文件可以在没有认证的情况下任意访问。

#### 3.5.2 使用指定的登录页面

第一步：提供login.html作为项目的登录页面

~~~html
<html>
<head>
    <title>登录</title>
</head>
<body>
    <form action="/login.do" method="post">
        username：<input type="text" name="username"><br>
        password：<input type="password" name="password"><br>
        <input type="submit" value="submit">
    </form>
</body>
</html>
~~~

第二步：修改spring-security.xml文件，指定login.html页面可以匿名访问

~~~xml
<security:http security="none" pattern="/login.html" />
~~~

第三步：修改spring-security.xml文件，加入表单登录信息的配置

~~~xml
<!--
  form-login：定义表单登录信息
-->
<security:form-login login-page="/login.html"
                     username-parameter="username"
                     password-parameter="password"
                     login-processing-url="/login.do"
                     default-target-url="/index.html"
                     authentication-failure-url="/login.html"
                     />
~~~

第四步：修改spring-security.xml文件，关闭CsrfFilter过滤器

~~~xml
<!--
  csrf：对应CsrfFilter过滤器
  disabled：是否启用CsrfFilter过滤器，如果使用自定义登录页面需要关闭此项，否则登录操作会被禁用（403）
-->
<security:csrf disabled="true"></security:csrf>
~~~

#### 3.5.3 从数据库查询用户信息

如果我们要从数据库动态查询用户信息，就必须按照spring security框架的要求提供一个实现UserDetailsService接口的实现类，并按照框架的要求进行配置即可。框架会自动调用实现类中的方法并自动进行密码校验。

实现类代码：

~~~java
package com.itheima.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService implements UserDetailsService {
    //模拟数据库中的用户数据
    public  static  Map<String, com.itheima.pojo.User> map = new HashMap<>();
    static {
        com.itheima.pojo.User user1 = new com.itheima.pojo.User();
        user1.setUsername("admin");
        user1.setPassword("admin");

        com.itheima.pojo.User user2 = new com.itheima.pojo.User();
        user2.setUsername("xiaoming");
        user2.setPassword("1234");

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }
    /**
     * 根据用户名加载用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username:" + username);
        com.itheima.pojo.User userInDb = map.get(username);//模拟根据用户名查询数据库
        if(userInDb == null){
            //根据用户名没有查询到用户
            return null;
        }

        //模拟数据库中的密码，后期需要查询数据库
        String passwordInDb = "{noop}" + userInDb.getPassword();

        List<GrantedAuthority> list = new ArrayList<>();
        //授权，后期需要改为查询数据库动态获得用户拥有的权限和角色
        list.add(new SimpleGrantedAuthority("add"));
        list.add(new SimpleGrantedAuthority("delete"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      	
        UserDetails user = new User(username,passwordInDb,list);
        return user;
    }
}
~~~

spring-security.xml：

~~~xml
<!--
  authentication-manager：认证管理器，用于处理认证操作
-->
<security:authentication-manager>
  <!--
    authentication-provider：认证提供者，执行具体的认证逻辑
  -->
  <security:authentication-provider user-service-ref="userService">
  </security:authentication-provider>
</security:authentication-manager>

<bean id="userService" class="com.itheima.security.UserService"></bean>
~~~

本章节我们提供了UserService实现类，并且按照框架的要求实现了UserDetailsService接口。在spring配置文件中注册UserService，指定其作为认证过程中根据用户名查询用户信息的处理类。当我们进行登录操作时，spring security框架会调用UserService的loadUserByUsername方法查询用户信息，并根据此方法中提供的密码和用户页面输入的密码进行比对来实现认证操作。

#### 3.5.4 对密码进行加密

前面我们使用的密码都是明文的，这是非常不安全的。一般情况下用户的密码需要进行加密后再保存到数据库中。

常见的密码加密方式有：

3DES、AES、DES：使用对称加密算法，可以通过解密来还原出原始密码

MD5、SHA1：使用单向HASH算法，无法通过计算还原出原始密码，但是可以建立彩虹表进行查表破解

bcrypt：将salt随机并混入最终加密后的密码，验证时也无需单独提供之前的salt，从而无需单独处理salt问题

加密后的格式一般为：

~~~properties
$2a$10$/bTVvqqlH9UiE0ZJZ7N2Me3RIgUCdgMheyTgV0B4cMCSokPa.6oCa
~~~

加密后字符串的长度为固定的60位。其中：$是分割符，无意义；2a是bcrypt加密版本号；10是cost的值；而后的前22位是salt值；再然后的字符串就是密码的密文了。



实现步骤：

第一步：在spring-security.xml文件中指定密码加密对象

~~~xml
<!--配置密码加密对象-->
<bean id="passwordEncoder" 
      class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />

<!--认证管理器，用于处理认证操作-->
<security:authentication-manager>
  <!--认证提供者，执行具体的认证逻辑-->
  <security:authentication-provider user-service-ref="userService">
    <!--指定密码加密策略-->
    <security:password-encoder ref="passwordEncoder" />
  </security:authentication-provider>
</security:authentication-manager>
<!--开启spring注解使用-->
<context:annotation-config></context:annotation-config>
~~~

第二步：修改UserService实现类

~~~java
package com.itheima.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public  Map<String, com.itheima.pojo.User> map = new HashMap<>();//模拟数据库中的用户数据

    public void initData(){
        com.itheima.pojo.User user1 = new com.itheima.pojo.User();
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("admin"));

        com.itheima.pojo.User user2 = new com.itheima.pojo.User();
        user2.setUsername("xiaoming");
        user2.setPassword(passwordEncoder.encode("1234"));

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }
    /**
     * 根据用户名加载用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initData();
        System.out.println("username:" + username);
        com.itheima.pojo.User userInDb = map.get(username);//模拟根据用户名查询数据库
        if(userInDb == null){
            //根据用户名没有查询到用户
            return null;
        }

        String passwordInDb = userInDb.getPassword();//模拟数据库中的密码，后期需要查询数据库

        List<GrantedAuthority> list = new ArrayList<>();
        //授权，后期需要改为查询数据库动态获得用户拥有的权限和角色
        list.add(new SimpleGrantedAuthority("add"));
        list.add(new SimpleGrantedAuthority("delete"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        UserDetails user = new User(username,passwordInDb,list);
        return user;
    }
}
~~~

#### 3.5.5 配置多种校验规则

为了测试方便，首先在项目中创建a.html、b.html、c.html、d.html几个页面

修改spring-security.xml文件：

~~~xml
<!--只要认证通过就可以访问-->
<security:intercept-url pattern="/index.jsp"  access="isAuthenticated()" />
<security:intercept-url pattern="/a.html"  access="isAuthenticated()" />

<!--拥有add权限就可以访问b.html页面-->
<security:intercept-url pattern="/b.html"  access="hasAuthority('add')" />

<!--拥有ROLE_ADMIN角色就可以访问c.html页面-->
<security:intercept-url pattern="/c.html"  access="hasRole('ROLE_ADMIN')" />

<!--拥有ROLE_ADMIN角色就可以访问d.html页面，
	注意：此处虽然写的是ADMIN角色，框架会自动加上前缀ROLE_-->
<security:intercept-url pattern="/d.html"  access="hasRole('ADMIN')" />
~~~

#### 3.5.6 注解方式权限控制

Spring Security除了可以在配置文件中配置权限校验规则，还可以使用注解方式控制类中方法的调用。例如Controller中的某个方法要求必须具有某个权限才可以访问，此时就可以使用Spring Security框架提供的注解方式进行控制。

实现步骤：

第一步：在spring-security.xml文件中配置组件扫描，用于扫描Controller

~~~xml
<mvc:annotation-driven></mvc:annotation-driven>
<context:component-scan base-package="com.itheima.controller"></context:component-scan>
~~~

第二步：在spring-security.xml文件中开启权限注解支持

~~~xml
<!--开启注解方式权限控制-->
<security:global-method-security pre-post-annotations="enabled" />
~~~

第三步：创建Controller类并在Controller的方法上加入注解进行权限控制

~~~java
package com.itheima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")//表示用户必须拥有add权限才能调用当前方法
    public String add(){
        System.out.println("add...");
        return "success";
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//表示用户必须拥有ROLE_ADMIN角色才能调用当前方法
    public String delete(){
        System.out.println("delete...");
        return "success";
    }
}
~~~

#### 3.5.7 退出登录

用户完成登录后Spring Security框架会记录当前用户认证状态为已认证状态，即表示用户登录成功了。那用户如何退出登录呢？我们可以在spring-security.xml文件中进行如下配置：

~~~xml
<!--
  logout：退出登录
  logout-url：退出登录操作对应的请求路径
  logout-success-url：退出登录后的跳转页面
-->
<security:logout logout-url="/logout.do" 
                 logout-success-url="/login.html" invalidate-session="true"/>
       
~~~

通过上面的配置可以发现，如果用户要退出登录，只需要请求/logout.do这个URL地址就可以，同时会将当前session失效，最后页面会跳转到login.html页面。

# 第10章 权限控制、图形报表

## 1. 在项目中应用Spring Security

前面我们已经学习了Spring Security框架的使用方法，本章节我们就需要将Spring Security框架应用到后台系统中进行权限控制，其本质就是认证和授权。

要进行认证和授权需要前面课程中提到的权限模型涉及的7张表支撑，因为用户信息、权限信息、菜单信息、角色信息、关联信息等都保存在这7张表中，也就是这些表中的数据是我们进行认证和授权的依据。所以在真正进行认证和授权之前需要对这些数据进行管理，即我们需要开发如下一些功能：

1、权限数据管理（增删改查）

2、菜单数据管理（增删改查）

3、角色数据管理（增删改查、角色关联权限、角色关联菜单）

4、用户数据管理（增删改查、用户关联角色）

鉴于时间关系，我们不再实现这些数据管理的代码开发。我们可以直接将数据导入到数据库中即可。

### 1.1 导入Spring Security环境

第一步：在health_parent父工程的pom.xml中导入Spring Security的maven坐标

~~~xml
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-web</artifactId>
  <version>${spring.security.version}</version>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-config</artifactId>
  <version>${spring.security.version}</version>
</dependency>
~~~

第二步：在health_backend工程的web.xml文件中配置用于整合Spring Security框架的过滤器DelegatingFilterProxy

~~~xml
<!--委派过滤器，用于整合其他框架-->
<filter>
  <!--整合spring security时，此过滤器的名称固定springSecurityFilterChain-->
  <filter-name>springSecurityFilterChain</filter-name>
  <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
  <filter-name>springSecurityFilterChain</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
~~~

### 1.2 实现认证和授权

第一步：在health_backend工程中按照Spring Security框架要求提供SpringSecurityUserService，并且实现UserDetailsService接口

~~~java
package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.CheckItemService;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService{
    @Reference //注意：此处要通过dubbo远程调用用户服务
    private UserService userService;
  
    //根据用户名查询用户信息
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      	//远程调用用户服务，根据用户名查询用户信息
        com.itheima.pojo.User user = userService.findByUsername(username);
        if(user == null){
              //用户名不存在
              return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for(Role role : roles){
            //授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for(Permission permission : permissions){
              	//授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        
        UserDetails userDetails = new User(username,user.getPassword(),list);
        return userDetails;
    }
}
~~~

第二步：创建UserService服务接口、服务实现类、Dao接口、Mapper映射文件等

~~~java
package com.itheima.service;

import com.itheima.pojo.User;
/**
 * 用户服务接口
 */
public interface UserService {
    public User findByUsername(String username);
}
~~~

~~~java
package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;

    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        Set<Role> roles = roleDao.findByUserId(userId);
        if(roles != null && roles.size() > 0){
            for(Role role : roles){
                Integer roleId = role.getId();
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                if(permissions != null && permissions.size() > 0){
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }
        return user;
    }
}
~~~

~~~java
package com.itheima.dao;

import com.itheima.pojo.User;

public interface UserDao {
    public User findByUsername(String username);
}
~~~

~~~java
package com.itheima.dao;

import com.itheima.pojo.Role;
import java.util.Set;

public interface RoleDao {
    public Set<Role> findByUserId(int id);
}
~~~

~~~java
package com.itheima.dao;

import com.itheima.pojo.Permission;
import java.util.Set;

public interface PermissionDao {
    public Set<Permission> findByRoleId(int roleId);
}
~~~

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.itheima.dao.UserDao" >
    <select id="findByUsername" 
            parameterType="string" 
            resultType="com.itheima.pojo.User">
        select * from t_user where username = #{username}
    </select>
</mapper>
~~~

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.itheima.dao.RoleDao" >
    <select id="findByUserId" 
            parameterType="int" 
            resultType="com.itheima.pojo.Role">
        select  r.* 
      		from t_role r ,t_user_role ur 
      		where r.id = ur.role_id and ur.user_id = #{userId}
    </select>
</mapper>
~~~

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.itheima.dao.PermissionDao" >
    <select id="findByRoleId" 
            parameterType="int" 
            resultType="com.itheima.pojo.Permission">
        select  p.* 
      		from t_permission p ,t_role_permission rp 
      		where p.id = rp.permission_id and rp.role_id = #{roleId}
    </select>
</mapper>
~~~

第三步：修改health_backend工程中的springmvc.xml文件，修改dubbo批量扫描的包路径

~~~xml
<!--批量扫描-->
<dubbo:annotation package="com.itheima" />
~~~

**注意：**此处原来扫描的包为com.itheima.controller，现在改为com.itheima包的目的是需要将我们上面定义的SpringSecurityUserService也扫描到，因为在SpringSecurityUserService的loadUserByUsername方法中需要通过dubbo远程调用名称为UserService的服务。

第四步：在health_backend工程中提供spring-security.xml配置文件

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:security="http://www.springframework.org/schema/security"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
						http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/mvc
						http://www.springframework.org/schema/mvc/spring-mvc.xsd
						http://code.alibabatech.com/schema/dubbo
						http://code.alibabatech.com/schema/dubbo/dubbo.xsd
						http://www.springframework.org/schema/context
						http://www.springframework.org/schema/context/spring-context.xsd
                          http://www.springframework.org/schema/security
                          http://www.springframework.org/schema/security/spring-security.xsd">

    <!--
        http：用于定义相关权限控制
        指定哪些资源不需要进行权限校验，可以使用通配符
    -->
    <security:http security="none" pattern="/js/**" />
    <security:http security="none" pattern="/css/**" />
    <security:http security="none" pattern="/img/**" />
    <security:http security="none" pattern="/plugins/**" />
  
    <!--
        http：用于定义相关权限控制
        auto-config：是否自动配置
                        设置为true时框架会提供默认的一些配置，例如提供默认的登录页面、登出处理等
                        设置为false时需要显示提供登录表单配置，否则会报错
        use-expressions：用于指定intercept-url中的access属性是否使用表达式
    -->
    <security:http auto-config="true" use-expressions="true">
        <security:headers>
            <!--设置在页面可以通过iframe访问受保护的页面，默认为不允许访问-->
            <security:frame-options policy="SAMEORIGIN"></security:frame-options>
        </security:headers>
        <!--
            intercept-url：定义一个拦截规则
            pattern：对哪些url进行权限控制
            access：在请求对应的URL时需要什么权限，默认配置时它应该是一个以逗号分隔的角色列表，
				  请求的用户只需拥有其中的一个角色就能成功访问对应的URL
            isAuthenticated()：已经经过认证（不是匿名用户）
        -->
        <security:intercept-url pattern="/pages/**"  access="isAuthenticated()" />
        <!--form-login：定义表单登录信息-->
        <security:form-login login-page="/login.html"
                             username-parameter="username"
                             password-parameter="password"
                             login-processing-url="/login.do"
                             default-target-url="/pages/main.html"
                             always-use-default-target="true"
                             authentication-failure-url="/login.html"
        />
        
        <!--
            csrf：对应CsrfFilter过滤器
            disabled：是否启用CsrfFilter过滤器，如果使用自定义登录页面需要关闭此项，
					否则登录操作会被禁用（403）
        -->
        <security:csrf disabled="true"></security:csrf>
    </security:http>

    <!--配置密码加密对象-->
    <bean id="passwordEncoder" 
          class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />

    <!--认证管理器，用于处理认证操作-->
    <security:authentication-manager>
        <!--认证提供者，执行具体的认证逻辑-->
        <security:authentication-provider user-service-ref="springSecurityUserService">
            <!--指定密码加密策略-->
            <security:password-encoder ref="passwordEncoder" />
        </security:authentication-provider>
    </security:authentication-manager>

    <!--开启注解方式权限控制-->
    <security:global-method-security pre-post-annotations="enabled" />
</beans>
~~~

第五步：在springmvc.xml文件中引入spring-security.xml文件

~~~xml
<import resource="spring-security.xml"></import>
~~~

第六步：在Controller的方法上加入权限控制注解，此处以CheckItemController为例

~~~java
package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.PermissionConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.exception.CustomException;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Member;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.util.List;

/**
 * 体检检查项管理
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    //分页查询
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")//权限校验
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.pageQuery(
                                    queryPageBean.getCurrentPage(), 
                                    queryPageBean.getPageSize(), 
                                    queryPageBean.getQueryString());
        return pageResult;
    }

    //删除
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            checkItemService.delete(id);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e){
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //新增
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")//权限校验
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //编辑
    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")//权限校验
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
}
~~~

第七步：修改页面，没有权限时提示信息设置，此处以checkitem.html中的handleDelete方法为例

~~~javascript
//权限不足提示
showMessage(r){
  if(r == 'Error: Request failed with status code 403'){
    //权限不足
    this.$message.error('无访问权限');
    return;
  }else{
    this.$message.error('未知错误');
    return;
  }
}
~~~

~~~javascript
// 删除
handleDelete(row) {
  this.$confirm('此操作将永久当前数据，是否继续?', '提示', {
    type: 'warning'
  }).then(()=>{
    //点击确定按钮执行此代码
    axios.get("/checkitem/delete.do?id=" + row.id).then((res)=> {
      if(!res.data.flag){
        //删除失败
        this.$message.error(res.data.message);
      }else{
        //删除成功
        this.$message({
          message: res.data.message,
          type: 'success'
        });
        this.findPage();
      }
    }).catch((r)=>{
      this.showMessage(r);
    });
  }).catch(()=> {
    //点击取消按钮执行此代码
    this.$message('操作已取消');
  });
}
~~~

### 1.3 显示用户名

前面我们已经完成了认证和授权操作，如果用户认证成功后需要在页面展示当前用户的用户名。Spring Security在认证成功后会将用户信息保存到框架提供的上下文对象中，所以此处我们就可以调用Spring Security框架提供的API获取当前用户的username并展示到页面上。

实现步骤：

第一步：在main.html页面中修改，定义username模型数据基于VUE的数据绑定展示用户名，发送ajax请求获取username

~~~javascript
<script>
    new Vue({
        el: '#app',
        data:{
            username:null,//用户名
    	    menuList:[]
        },
        created(){
            //发送请求获取当前登录用户的用户名
            axios.get('/user/getUsername.do').then((response)=>{
                this.username = response.data.data;
            });
        }
    });
</script>
~~~

~~~html
<div class="avatar-wrapper">
  <img src="../img/user2-160x160.jpg" class="user-avatar">
  <!--展示用户名-->
  {{username}}
</div>
~~~

第二步：创建UserController并提供getUsername方法

~~~java
package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername()throws Exception{
        try{
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        }catch (Exception e){
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
~~~

通过debug调试可以看到Spring Security框架在其上下文中保存的用户相关信息：

![1](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/1-1667723963857.png)

### 1.4 用户退出

第一步：在main.html中提供的退出菜单上加入超链接

~~~html
<el-dropdown-item divided>
  <span style="display:block;"><a href="/logout.do">退出</a></span>
</el-dropdown-item>
~~~

第二步：在spring-security.xml文件中配置

~~~xml
<!--
  logout：退出登录
  logout-url：退出登录操作对应的请求路径
  logout-success-url：退出登录后的跳转页面
-->
<security:logout logout-url="/logout.do" 
                 logout-success-url="/login.html" invalidate-session="true"/>

~~~

## 2. 图形报表ECharts

### 2.1 ECharts简介

ECharts缩写来自Enterprise Charts，商业级数据图表，是百度的一个开源的使用JavaScript实现的数据可视化工具，可以流畅的运行在 PC 和移动设备上，兼容当前绝大部分浏览器（IE8/9/10/11，Chrome，Firefox，Safari等），底层依赖轻量级的矢量图形库 [ZRender](https://github.com/ecomfe/zrender)，提供直观、交互丰富、可高度个性化定制的数据可视化图表。

官网：https://echarts.baidu.com/

下载地址：https://echarts.baidu.com/download.html

![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2-1667723963883.png)

下载完成可以得到如下文件：

![6](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/6-1667723963857.png)



解压上面的zip文件：

![7](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/7-1667723963857.png)



我们只需要将dist目录下的echarts.js文件引入到页面上就可以使用了

![8](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/8-1667723963857.png)

### 2.2 5分钟上手ECharts

我们可以参考官方提供的5分钟上手ECharts文档感受一下ECharts的使用方式，地址如下：

https://www.echartsjs.com/tutorial.html#5%20%E5%88%86%E9%92%9F%E4%B8%8A%E6%89%8B%20ECharts

第一步：创建html页面并引入echarts.js文件

~~~html
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!-- 引入 ECharts 文件 -->
    <script src="echarts.js"></script>
</head>
</html>
~~~

第二步：在页面中准备一个具备宽高的DOM容器。

~~~html
<body>
    <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 600px;height:400px;"></div>
</body>
~~~

第三步：通过echarts.init方法初始化一个 echarts 实例并通过setOption方法生成一个简单的柱状图

~~~javascript
<script type="text/javascript">
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('main'));

  // 指定图表的配置项和数据
  var option = {
    title: {
      text: 'ECharts 入门示例'
    },
    tooltip: {},
    legend: {
      data:['销量']
    },
    xAxis: {
      data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
    },
    yAxis: {},
    series: [{
      name: '销量',
      type: 'bar',
      data: [5, 20, 36, 10, 10, 20]
    }]
};

// 使用刚指定的配置项和数据显示图表。
myChart.setOption(option);
</script>
~~~

效果如下：

![10](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/10-1667723963884.png)

### 2.3 查看ECharts官方实例 

ECharts提供了很多官方实例，我们可以通过这些官方实例来查看展示效果和使用方法。

官方实例地址：https://www.echartsjs.com/examples/

![3](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/3-1667723963857.png)

可以点击具体的一个图形会跳转到编辑页面，编辑页面左侧展示源码（js部分源码），右侧展示图表效果，如下：

![4](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/4-1667723963857.png)

要查看完整代码可以点击右下角的Download按钮将完整页面下载到本地。



通过官方案例我们可以发现，使用ECharts展示图表效果，关键点在于确定此图表所需的数据格式，然后按照此数据格式提供数据就可以了，我们无须关注效果是如何渲染出来的。

在实际应用中，我们要展示的数据往往存储在数据库中，所以我们可以发送ajax请求获取数据库中的数据并转为图表所需的数据即可。

## 3. 会员数量折线图

### 3.1 需求分析

会员信息是体检机构的核心数据，其会员数量和增长数量可以反映出机构的部分运营情况。通过折线图可以直观的反映出会员数量的增长趋势。本章节我们需要展示过去一年时间内每个月的会员总数据量。展示效果如下图：

![5](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/5-1667723963857.png)

### 3.2 完善页面

会员数量折线图对应的页面为/pages/report_member.html。

#### 3.2.1 导入ECharts库

第一步：将echarts.js文件复制到health_backend工程的plugins目录下

第二步：在report_member.html页面引入echarts.js文件

~~~html
<script src="../plugins/echarts/echarts.js"></script>
~~~

#### 3.2.2 参照官方实例导入折线图 

~~~html
<div class="box">
  <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
  <div id="chart1" style="height:600px;"></div>
</div>
~~~

~~~javascript
<script type="text/javascript">
  // 基于准备好的dom，初始化echarts实例
  var myChart1 = echarts.init(document.getElementById('chart1'));
  //发送ajax请求获取动态数据
  axios.get("/report/getMemberReport.do").then((res)=>{
    myChart1.setOption(
      {
        title: {
          text: '会员数量'
        },
        tooltip: {},
        legend: {
          data:['会员数量']
        },
        xAxis: {
          data: res.data.data.months
        },
        yAxis: {
          type:'value'
        },
        series: [{
          name: '会员数量',
          type: 'line',
          data: res.data.data.memberCount
        }]
      });
  });
</script>
~~~

根据折线图对数据格式的要求，我们发送ajax请求，服务端需要返回如下格式的数据：

~~~json
{
	"data":{
			"months":["2019.01","2019.02","2019.03","2019.04"],
			"memberCount":[3,4,8,10]
		   },
	"flag":true,
	"message":"获取会员统计数据成功"
}
~~~

### 3.3 后台代码

#### 3.3.1 Controller

在health_backend工程中创建ReportController并提供getMemberReport方法

~~~java
package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import com.itheima.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 统计报表
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    /**
     * 会员数量统计
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);//获得当前日期之前12个月的日期

        List<String> list = new ArrayList<>();
        for(int i=0;i<12;i++){
            calendar.add(Calendar.MONTH,1);
            list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }

        Map<String,Object> map = new HashMap<>();
        map.put("months",list);

        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }
}
~~~

#### 3.3.2 服务接口

在MemberService服务接口中扩展方法findMemberCountByMonth

~~~java
public List<Integer> findMemberCountByMonth(List<String> month);
~~~

#### 3.2.3 服务实现类

在MemberServiceImpl服务实现类中实现findMemberCountByMonth方法

~~~java
//根据月份统计会员数量
public List<Integer> findMemberCountByMonth(List<String> month) {
  List<Integer> list = new ArrayList<>();
  for(String m : month){
    m = m + ".31";//格式：2019.04.31
    Integer count = memberDao.findMemberCountBeforeDate(m);
    list.add(count);
  }
  return list;
}
~~~

#### 3.3.4 Dao接口

在MemberDao接口中扩展方法findMemberCountBeforeDate

~~~java
public Integer findMemberCountBeforeDate(String date);
~~~

#### 3.3.5 Mapper映射文件

在MemberDao.xml映射文件中提供SQL语句

~~~xml
<!--根据日期统计会员数，统计指定日期之前的会员数-->
<select id="findMemberCountBeforeDate" parameterType="string" resultType="int">
  select count(id) from t_member where regTime &lt;= #{value}
</select>
~~~

# 第11章 图形报表、POI报表

## 1. 套餐预约占比饼形图

### 1.1 需求分析

会员可以通过移动端自助进行体检预约，在预约时需要选择预约的体检套餐。本章节我们需要通过饼形图直观的展示出会员预约的各个套餐占比情况。展示效果如下图：

![1](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/1-1667738012365.png)

### 1.2 完善页面

套餐预约占比饼形图对应的页面为/pages/report_setmeal.html。

#### 1.2.1 导入ECharts库

~~~html
<script src="../plugins/echarts/echarts.js"></script>
~~~

#### 1.2.2 参照官方实例导入饼形图 

~~~html
<div class="box">
  <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
  <div id="chart1" style="height:600px;"></div>
</div>
~~~

~~~javascript
<script type="text/javascript">
  // 基于准备好的dom，初始化echarts实例
  var myChart1 = echarts.init(document.getElementById('chart1'));
  //发送ajax请求获取动态数据
  axios.get("/report/getSetmealReport.do").then((res)=>{
    myChart1.setOption({
      title : {
        text: '套餐预约占比',
        subtext: '',
        x:'center'
      },
      tooltip : {//提示框组件
        trigger: 'item',//触发类型，在饼形图中为item
        formatter: "{a} <br/>{b} : {c} ({d}%)"//提示内容格式
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: res.data.data.setmealNames
      },
      series : [
        {
          name: '套餐预约占比',
          type: 'pie',
          radius : '55%',
          center: ['50%', '60%'],
          data:res.data.data.setmealCount,
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    });
  });
</script>
~~~

根据饼形图对数据格式的要求，我们发送ajax请求，服务端需要返回如下格式的数据：

~~~json
{
	"data":{
			"setmealNames":["套餐1","套餐2","套餐3"],
			"setmealCount":[
							{"name":"套餐1","value":10},
							{"name":"套餐2","value":30},
							{"name":"套餐3","value":25}
						   ]
		   },
	"flag":true,
	"message":"获取套餐统计数据成功"
}
~~~

### 1.3 后台代码

#### 1.3.1 Controller

在health_backend工程的ReportController中提供getSetmealReport方法

~~~java
@Reference
private SetmealService setmealService;
/**
 * 套餐占比统计
 * @return
 */
@RequestMapping("/getSetmealReport")
public Result getSetmealReport(){
  List<Map<String, Object>> list = setmealService.findSetmealCount();

  Map<String,Object> map = new HashMap<>();
  map.put("setmealCount",list);

  List<String> setmealNames = new ArrayList<>();
  for(Map<String,Object> m : list){
    String name = (String) m.get("name");
    setmealNames.add(name);
  }
  map.put("setmealNames",setmealNames);
  
  return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
}
~~~

#### 1.3.2 服务接口

在SetmealService服务接口中扩展方法findSetmealCount

~~~java
public List<Map<String,Object>> findSetmealCount();
~~~

#### 1.3.3 服务实现类

在SetmealServiceImpl服务实现类中实现findSetmealCount方法

~~~java
public List<Map<String, Object>> findSetmealCount() {
  return setmealDao.findSetmealCount();
}
~~~

#### 1.3.4 Dao接口

在SetmealDao接口中扩展方法findSetmealCount

~~~java
public List<Map<String,Object>> findSetmealCount();
~~~

#### 1.3.5 Mapper映射文件

在SetmealDao.xml映射文件中提供SQL语句

~~~xml
<select id="findSetmealCount" resultType="map">
  select s.name,count(o.id) as value 
  	from t_order o ,t_setmeal s 
  	where o.setmeal_id = s.id 
  	group by s.name
</select>
~~~

## 2. 运营数据统计

### 2.1 需求分析

通过运营数据统计可以展示出体检机构的运营情况，包括会员数据、预约到诊数据、热门套餐等信息。本章节就是要通过一个表格的形式来展示这些运营数据。效果如下图：

![2](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/2-1667738012366.png)

### 2.2 完善页面

运营数据统计对应的页面为/pages/report_business.html。

#### 2.2.1 定义模型数据

定义数据模型，通过VUE的数据绑定展示数据

~~~javascript
<script>
  var vue = new Vue({
    el: '#app',
    data:{
      reportData:{
        reportDate:null,
        todayNewMember :0,
        totalMember :0,
        thisWeekNewMember :0,
        thisMonthNewMember :0,
        todayOrderNumber :0,
        todayVisitsNumber :0,
        thisWeekOrderNumber :0,
        thisWeekVisitsNumber :0,
        thisMonthOrderNumber :0,
        thisMonthVisitsNumber :0,
        hotSetmeal :[]
      }
    }
  })
</script>
~~~

~~~html
<div class="box" style="height: 900px">
  <div class="excelTitle" >
    <el-button @click="exportExcel">导出Excel</el-button>运营数据统计
  </div>
  <div class="excelTime">日期：{{reportData.reportDate}}</div>
  <table class="exceTable" cellspacing="0" cellpadding="0">
    <tr>
      <td colspan="4" class="headBody">会员数据统计</td>
    </tr>
    <tr>
      <td width='20%' class="tabletrBg">新增会员数</td>
      <td width='30%'>{{reportData.todayNewMember}}</td>
      <td width='20%' class="tabletrBg">总会员数</td>
      <td width='30%'>{{reportData.totalMember}}</td>
    </tr>
    <tr>
      <td class="tabletrBg">本周新增会员数</td>
      <td>{{reportData.thisWeekNewMember}}</td>
      <td class="tabletrBg">本月新增会员数</td>
      <td>{{reportData.thisMonthNewMember}}</td>
    </tr>
    <tr>
      <td colspan="4" class="headBody">预约到诊数据统计</td>
    </tr>
    <tr>
      <td class="tabletrBg">今日预约数</td>
      <td>{{reportData.todayOrderNumber}}</td>
      <td class="tabletrBg">今日到诊数</td>
      <td>{{reportData.todayVisitsNumber}}</td>
    </tr>
    <tr>
      <td class="tabletrBg">本周预约数</td>
      <td>{{reportData.thisWeekOrderNumber}}</td>
      <td class="tabletrBg">本周到诊数</td>
      <td>{{reportData.thisWeekVisitsNumber}}</td>
    </tr>
    <tr>
      <td class="tabletrBg">本月预约数</td>
      <td>{{reportData.thisMonthOrderNumber}}</td>
      <td class="tabletrBg">本月到诊数</td>
      <td>{{reportData.thisMonthVisitsNumber}}</td>
    </tr>
    <tr>
      <td colspan="4" class="headBody">热门套餐</td>
    </tr>
    <tr class="tabletrBg textCenter">
      <td>套餐名称</td>
      <td>预约数量</td>
      <td>占比</td>
      <td>备注</td>
    </tr>
    <tr v-for="s in reportData.hotSetmeal">
      <td>{{s.name}}</td>
      <td>{{s.setmeal_count}}</td>
      <td>{{s.proportion}}</td>
      <td></td>
    </tr>
  </table>
</div>
~~~

#### 2.2.2 发送请求获取动态数据

在VUE的钩子函数中发送ajax请求获取动态数据，通过VUE的数据绑定将数据展示到页面

~~~javascript
<script>
  var vue = new Vue({
    el: '#app',
    data:{
      reportData:{
        reportDate:null,
        todayNewMember :0,
        totalMember :0,
        thisWeekNewMember :0,
        thisMonthNewMember :0,
        todayOrderNumber :0,
        todayVisitsNumber :0,
        thisWeekOrderNumber :0,
        thisWeekVisitsNumber :0,
        thisMonthOrderNumber :0,
        thisMonthVisitsNumber :0,
        hotSetmeal :[]
      }
    },
    created() {
      //发送ajax请求获取动态数据
      axios.get("/report/getBusinessReportData.do").then((res)=>{
        this.reportData = res.data.data;
      });
    }
  })
</script>
~~~

根据页面对数据格式的要求，我们发送ajax请求，服务端需要返回如下格式的数据：

~~~json
{
  "data":{
    "todayVisitsNumber":0,
    "reportDate":"2019-04-25",
    "todayNewMember":0,
    "thisWeekVisitsNumber":0,
    "thisMonthNewMember":2,
    "thisWeekNewMember":0,
    "totalMember":10,
    "thisMonthOrderNumber":2,
    "thisMonthVisitsNumber":0,
    "todayOrderNumber":0,
    "thisWeekOrderNumber":0,
    "hotSetmeal":[
      {"proportion":0.4545,"name":"粉红珍爱(女)升级TM12项筛查体检套餐","setmeal_count":5},
      {"proportion":0.1818,"name":"阳光爸妈升级肿瘤12项筛查体检套餐","setmeal_count":2},
      {"proportion":0.1818,"name":"珍爱高端升级肿瘤12项筛查","setmeal_count":2},
      {"proportion":0.0909,"name":"孕前检查套餐","setmeal_count":1}
    ],
  },
  "flag":true,
  "message":"获取运营统计数据成功"
}
~~~

### 2.3 后台代码

#### 2.3.1 Controller

在ReportController中提供getBusinessReportData方法

~~~java
@Reference
private ReportService reportService;

/**
 * 获取运营统计数据
 * @return
*/
@RequestMapping("/getBusinessReportData")
public Result getBusinessReportData(){
  try {
    Map<String, Object> result = reportService.getBusinessReport();
    return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
  } catch (Exception e) {
    e.printStackTrace();
    return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
  }
}
~~~

#### 2.3.2 服务接口

在health_interface工程中创建ReportService服务接口并声明getBusinessReport方法

~~~java
package com.itheima.service;

import java.util.Map;

public interface ReportService {
    /**
     * 获得运营统计数据
     * Map数据格式：
     *      todayNewMember -> number
     *      totalMember -> number
     *      thisWeekNewMember -> number
     *      thisMonthNewMember -> number
     *      todayOrderNumber -> number
     *      todayVisitsNumber -> number
     *      thisWeekOrderNumber -> number
     *      thisWeekVisitsNumber -> number
     *      thisMonthOrderNumber -> number
     *      thisMonthVisitsNumber -> number
     *      hotSetmeals -> List<Setmeal>
     */
    public Map<String,Object> getBusinessReport() throws Exception;
}
~~~

#### 2.3.3 服务实现类

在health_service_provider工程中创建服务实现类ReportServiceImpl并实现ReportService接口

~~~java
package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计报表服务
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 获得运营统计数据
     * Map数据格式：
     *      todayNewMember -> number
     *      totalMember -> number
     *      thisWeekNewMember -> number
     *      thisMonthNewMember -> number
     *      todayOrderNumber -> number
     *      todayVisitsNumber -> number
     *      thisWeekOrderNumber -> number
     *      thisWeekVisitsNumber -> number
     *      thisMonthOrderNumber -> number
     *      thisMonthVisitsNumber -> number
     *      hotSetmeal -> List<Setmeal>
     */
    public Map<String, Object> getBusinessReport() throws Exception{
      	//获得当前日期
        String today = DateUtils.parseDate2String(DateUtils.getToday());
        //获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获得本月第一天的日期  
        String firstDay4ThisMonth = 
          					DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(today);

        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();

        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);

        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);

        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

        //热门套餐（取前4）
        List<Map> hotSetmeal = orderDao.findHotSetmeal();

        Map<String,Object> result = new HashMap<>();
        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);

        return result;
    }
}
~~~

#### 2.3.4 Dao接口

在OrderDao和MemberDao中声明相关统计查询方法

~~~java
package com.itheima.dao;

import com.itheima.pojo.Order;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    public void add(Order order);
    public List<Order> findByCondition(Order order);
    public Map findById4Detail(Integer id);
    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);
    public List<Map> findHotSetmeal();
}
~~~

~~~java
package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;
import java.util.List;

public interface MemberDao {
    public List<Member> findAll();
    public Page<Member> selectByCondition(String queryString);
    public void add(Member member);
    public void deleteById(Integer id);
    public Member findById(Integer id);
    public Member findByTelephone(String telephone);
    public void edit(Member member);
    public Integer findMemberCountBeforeDate(String date);
    public Integer findMemberCountByDate(String date);
    public Integer findMemberCountAfterDate(String date);
    public Integer findMemberTotalCount();
}
~~~

#### 2.3.5 Mapper映射文件

在OrderDao.xml和MemberDao.xml中定义SQL语句

OrderDao.xml：

~~~xml
<!--根据日期统计预约数-->
<select id="findOrderCountByDate" parameterType="string" resultType="int">
  select count(id) from t_order where orderDate = #{value}
</select>

<!--根据日期统计预约数，统计指定日期之后的预约数-->
<select id="findOrderCountAfterDate" parameterType="string" resultType="int">
  select count(id) from t_order where orderDate &gt;= #{value}
</select>

<!--根据日期统计到诊数-->
<select id="findVisitsCountByDate" parameterType="string" resultType="int">
  select count(id) from t_order where orderDate = #{value} and orderStatus = '已到诊'
</select>

<!--根据日期统计到诊数，统计指定日期之后的到诊数-->
<select id="findVisitsCountAfterDate" parameterType="string" resultType="int">
  select count(id) from t_order where orderDate &gt;= #{value} and orderStatus = '已到诊'
</select>

<!--热门套餐，查询前4条-->
<select id="findHotSetmeal" resultType="map">
  select 
      s.name, 
      count(o.id) setmeal_count ,
      count(o.id)/(select count(id) from t_order) proportion
    from t_order o inner join t_setmeal s on s.id = o.setmeal_id
    group by o.setmeal_id
    order by setmeal_count desc 
  	limit 0,4
</select>
~~~

MemberDao.xml：

~~~xml
<!--根据日期统计会员数，统计指定日期之前的会员数-->
<select id="findMemberCountBeforeDate" parameterType="string" resultType="int">
  select count(id) from t_member where regTime &lt;= #{value}
</select>

<!--根据日期统计会员数-->
<select id="findMemberCountByDate" parameterType="string" resultType="int">
  select count(id) from t_member where regTime = #{value}
</select>

<!--根据日期统计会员数，统计指定日期之后的会员数-->
<select id="findMemberCountAfterDate" parameterType="string" resultType="int">
  select count(id) from t_member where regTime &gt;= #{value}
</select>

<!--总会员数-->
<select id="findMemberTotalCount" resultType="int">
  select count(id) from t_member
</select>
~~~

## 3. 运营数据统计报表导出

### 3.1 需求分析

运营数据统计报表导出就是将统计数据写入到Excel并提供给客户端浏览器进行下载，以便体检机构管理人员对运营数据的查看和存档。

### 3.2 提供模板文件

本章节我们需要将运营统计数据通过POI写入到Excel文件，对应的Excel效果如下：

![3](ssm-%E4%BC%A0%E6%99%BA%E5%81%A5%E5%BA%B7%E9%A1%B9%E7%9B%AE/3-1667738012365.png)

通过上面的Excel效果可以看到，表格比较复杂，涉及到合并单元格、字体、字号、字体加粗、对齐方式等的设置。如果我们通过POI编程的方式来设置这些效果代码会非常繁琐。

在企业实际开发中，对于这种比较复杂的表格导出一般我们会提前设计一个Excel模板文件，在这个模板文件中提前将表格的结构和样式设置好，我们的程序只需要读取这个文件并在文件中的相应位置写入具体的值就可以了。

在本章节资料中已经提供了一个名为report_template.xlsx的模板文件，需要将这个文件复制到health_backend工程的template目录中

### 3.3 完善页面

在report_business.html页面提供导出按钮并绑定事件

~~~html
<div class="excelTitle" >
  <el-button @click="exportExcel">导出Excel</el-button>运营数据统计
</div>
~~~

~~~javascript
methods:{
  //导出Excel报表
  exportExcel(){
    window.location.href = '/report/exportBusinessReport.do';
  }
}
~~~

### 3.4 后台代码

在ReportController中提供exportBusinessReport方法，基于POI将数据写入到Excel中并通过输出流下载到客户端

~~~java
/**
  * 导出Excel报表
  * @return
*/
@RequestMapping("/exportBusinessReport")
public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
  try{
    //远程调用报表服务获取报表数据
    Map<String, Object> result = reportService.getBusinessReport();
    
    //取出返回结果数据，准备将报表数据写入到Excel文件中
    String reportDate = (String) result.get("reportDate");
    Integer todayNewMember = (Integer) result.get("todayNewMember");
    Integer totalMember = (Integer) result.get("totalMember");
    Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
    Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
    Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
    Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
    Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
    Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
    Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
    Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
    List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
	
    //获得Excel模板文件绝对路径
    String temlateRealPath = request.getSession().getServletContext().getRealPath("template") +
      											File.separator + "report_template.xlsx";
	
    //读取模板文件创建Excel表格对象
    XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
    XSSFSheet sheet = workbook.getSheetAt(0);
    
    XSSFRow row = sheet.getRow(2);
    row.getCell(5).setCellValue(reportDate);//日期

    row = sheet.getRow(4);
    row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
    row.getCell(7).setCellValue(totalMember);//总会员数

    row = sheet.getRow(5);
    row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
    row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

    row = sheet.getRow(7);
    row.getCell(5).setCellValue(todayOrderNumber);//今日预约数	
    row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

    row = sheet.getRow(8);
    row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
    row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

    row = sheet.getRow(9);
    row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
    row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

    int rowNum = 12;
    for(Map map : hotSetmeal){//热门套餐
      String name = (String) map.get("name");
      Long setmeal_count = (Long) map.get("setmeal_count");
      BigDecimal proportion = (BigDecimal) map.get("proportion");
      row = sheet.getRow(rowNum ++);
      row.getCell(4).setCellValue(name);//套餐名称
      row.getCell(5).setCellValue(setmeal_count);//预约数量
      row.getCell(6).setCellValue(proportion.doubleValue());//占比
    }

    //通过输出流进行文件下载
    ServletOutputStream out = response.getOutputStream();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
    workbook.write(out);
    
    out.flush();
    out.close();
    workbook.close();
    
    return null;
  }catch (Exception e){
    return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
  }
}
~~~

# 传智健康项目  第12章

在前面的课程中我们完成了将运营数据导出到Excel文件的功能。在企业开发中，除了常见的Excel形式报表，还有PDF形式的报表。那么如何导出PDF形式的报表呢？

## 1. 常见的PDF报表生成方式

### 1.1 iText

iText是著名的开放源码的站点sourceforge一个项目，是用于生成PDF文档的一个java类库。通过iText不仅可以生成PDF或rtf的文档，而且可以将XML、Html文件转化为PDF文件。 iText的安装非常方便，下载iText.jar文件后，只需要在系统的CLASSPATH中加入iText.jar的路径，在程序中就可以使用iText类库了。

maven坐标：

~~~xml
<dependency>
  <groupId>com.lowagie</groupId>
  <artifactId>itext</artifactId>
  <version>2.1.7</version>
</dependency>
~~~

示例代码:

```java
package com.itheima.app;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ItextDemo {
    public static void main(String[] args) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("D:\\test.pdf"));
            document.open();
            document.add(new Paragraph("hello itext"));
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
```

### 1.2 JasperReports

JasperReports是一个强大、灵活的报表生成工具，能够展示丰富的页面内容，并将之转换成PDF，HTML，或者XML格式。该库完全由Java写成，可以用于在各种Java应用程序，包括J2EE，Web应用程序中生成动态内容。一般情况下，JasperReports会结合Jaspersoft Studio(模板设计器)使用导出PDF报表。

maven坐标:

```xml
<dependency>
  <groupId>net.sf.jasperreports</groupId>
  <artifactId>jasperreports</artifactId>
  <version>6.8.0</version>
</dependency>
```

## 2. JasperReports概述

### 2.1 JasperReports快速体验

本小节我们先通过一个快速体验来感受一下JasperReports的开发过程。

第一步：创建maven工程，导入JasperReports的maven坐标

~~~xml
<dependency>
  <groupId>net.sf.jasperreports</groupId>
  <artifactId>jasperreports</artifactId>
  <version>6.8.0</version>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
~~~

第二步：将提前准备好的jrxml文件复制到maven工程中(后面会详细讲解如何创建jrxml文件)

![42](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/42.png)

第三步：编写单元测试，输出PDF报表

~~~java
@Test
public void testJasperReports()throws Exception{
    String jrxmlPath = 
        "D:\\ideaProjects\\projects111\\jasperdemo\\src\\main\\resources\\demo.jrxml";
    String jasperPath = 
        "D:\\ideaProjects\\projects111\\jasperdemo\\src\\main\\resources\\demo.jasper";

    //编译模板
    JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

    //构造数据
    Map paramters = new HashMap();
    paramters.put("reportDate","2019-10-10");
    paramters.put("company","itcast");
    List<Map> list = new ArrayList();
    Map map1 = new HashMap();
    map1.put("name","xiaoming");
    map1.put("address","beijing");
    map1.put("email","xiaoming@itcast.cn");
    Map map2 = new HashMap();
    map2.put("name","xiaoli");
    map2.put("address","nanjing");
    map2.put("email","xiaoli@itcast.cn");
    list.add(map1);
    list.add(map2);

    //填充数据
    JasperPrint jasperPrint = 
        JasperFillManager.fillReport(jasperPath, 
                                     paramters, 
                                     new JRBeanCollectionDataSource(list));

    //输出文件
    String pdfPath = "D:\\test.pdf";
    JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
}
~~~

### 2.2 JasperReports原理

![43](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/43.png)



- JRXML：报表填充模板，本质是一个xml文件
- Jasper：由JRXML模板编译成的二进制文件，用于代码填充数据
- Jrprint：当用数据填充完Jasper后生成的对象，用于输出报表
- Exporter：报表输出的管理类，可以指定要输出的报表为何种格式
- PDF/HTML/XML：报表形式

### 2.3 开发流程

使用JasperReports导出pdf报表，开发流程如下：

1. 制作报表模板
2. 模板编译
3. 构造数据
4. 填充数据
5. 输出文件

## 3. 模板设计器Jaspersoft Studio

Jaspersoft Studio是一个图形化的报表设计工具，可以非常方便的设计出PDF报表模板文件(其实就是一个xml文件)，再结合JasperReports使用，就可以渲染出PDF文件。

下载地址:https://community.jaspersoft.com/community-download

![1](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/1.png)

下载完成后会得到如下安装文件：

![2](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/2.png)

直接双击安装即可。

### 3.1 Jaspersoft Studio面板介绍

![4](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/4.png)

### 3.2 创建工程和模板文件

打开Jaspersoft Studio工具，首先需要创建一个工程，创建过程如下：

![5](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/5.png)



![6](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/6.png)



![7](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/7.png)



![8](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/8.png)



创建完工程后，可以在工程上点击右键，创建模板文件：

![9](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/9.png)





![10](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/10.png)





![11](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/11.png)



![12](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/12.png)



![13](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/13.png)



![14](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/14.png)

可以看到创建处理的模板文件后缀为jrxml，从设计区面板可以看到如下效果：

![15](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/15.png)

可以看到整个文件是可视化的，分为几大区域（Title、Page Header、Column Header等），如果某些区域不需要也可以删除。



在面板左下角可以看到有三种视图方式：Design（设计模式）、Source（源码模式）、Preview（预览模式）:

- 通过Design视图可以看到模板的直观结构和样式
- 通过Source视图可以看到文件xml源码
- 通过Preview视图可以预览PDF文件输出后的效果



通过右侧Palette窗口可以看到常用的元素：

![16](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/16.png)

### 3.3 设计模板文件

#### 3.3.1 增减Band

可以根据情况删除或者增加模板文件中的区域（称为Band），例如在Page Header区域上点击右键，选择删除菜单：

![17](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/17.png)



其中Detail区域可以添加多个，其他区域只能有一个。

#### 3.3.2 将元素应用到模板中

##### 3.3.2.1 Image元素

从右侧Palette面板中选择Image元素（图片元素），拖动到Title区域：

![18](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/18.png)



弹出如下对话框，有多种创建模式，选择URL模式，并在下面输入框中输入一个网络图片的连接地址：

![19](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/19.png)



![20](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/20.png)

可以选中图片元素，鼠标拖动调整位置，也可以通过鼠标调整图片的大小。

调整完成后，可以点击Preview进入预览视图，查看PDF输出效果：

![21](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/21.png)

点击Source进入源码视图，查看xml文件内容：

![22](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/22.png)

其实我们上面创建的demo1.jrxml模板文件，本质上就是一个xml文件，只不过我们不需要自己编写xml文件的内容，而是通过Jaspersoft Studio这个设计器软件进行可视化设计即可。

##### 3.3.2.2 Static Text元素

Static Text元素就是静态文本元素，用于在PDF文件上展示静态文本信息：

![23](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/23.png)

双击Title面板中的Static Text元素，可以修改文本内容：

![24](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/24.png)

选中元素，也可以调整文本的字体和字号：

![25](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/25.png)



点击Preview进入预览视图，查看效果：

![26](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/26.png)

##### 3.3.2.3 Current Date元素

Current Date元素用于在报表中输出当前系统日期，将改元素拖动到Title区域：

![27](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/27.png)

预览输出效果：

![28](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/28.png)

默认日期输出格式如上图所示，可以回到设计视图并选中元素，在Properties面板中的Text Field子标签中修改日期输出格式：

![29](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/29.png)

修改日期格式：

![30](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/30.png)

保存文件后重新预览：

![31](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/31.png)

#### 3.3.3 动态数据填充

上面我们在PDF文件中展示的都是一些静态数据，那么如果需要动态展示一些数据应该如何实现呢？我们可以使用Outline面板中的Parameters和Fields来实现。

![32](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/32.png)

Parameters通常用来展示单个数据，Fields通常用来展示需要循环的列表数据。

##### 3.3.3.1 Parameters

在Parameters上点击右键，创建一个Parameter参数：

![33](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/33.png)



可以在右侧的Properties面板中修改刚才创建的参数名称：

![34](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/34.png)



将刚才创建的Parameter参数拖动到面板中：

![35](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/35.png)

进入预览视图，查看效果：

![36](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/36.png)

由于模板中我们使用了Parameter动态元素，所以在预览之前需要为其动态赋值：

![37](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/37.png)

注意：由于我们是在Jaspersoft Studio软件中进行预览，所以需要通过上面的输入框动态为Parameter赋值，在后期项目使用时，需要我们在Java程序中动态为Parameter赋值进行数据填充。

##### 3.3.3.2 Fields

使用Fields方式进行数据填充，既可以使用jdbc数据源方式也可以使用JavaBean数据源方式。

- jdbc数据源数据填充

第一步：在Repository Explorer面板中，在Data Adapters点击右键，创建一个数据适配器

![38](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/38.png)



第二步：选择Database JDBC Connection

![39](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/39.png)

第三步：选择mysql数据库，并完善jdbc连接信息

![40](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/40.png)

为了能够在Jaspersoft Studio中预览到数据库中的数据，需要加入MySQL的驱动包

![41](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/41.png)

第四步：在Outline视图中，右键点击工程名，选择Database and Query菜单

![44](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/44.png)

第五步：在弹出的对话框中选择刚刚创建的JDBC数据库连接选项

![45](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/45.png)

第六步：在弹出对话框中Language选择sql，在右侧区域输入SQL语句并点击Read Fields按钮

![46](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/46.png)

可以看到通过点击上面的Read Fields按钮，已经读取到了t_setmeal表中的所有字段信息并展示在了下面，这些字段可以根据需要进行删除或者调整位置

第七步：在Outline视图中的Fields下可以看到t_setmeal表中相关字段信息，拖动某个字段到设计区的Detail区域并调整位置

![47](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/47.png)

可以看到，在拖动Fields到设计区时，同时会产生两个元素，一个是静态文本，一个是动态元素。静态文本相当于表格的表头，可以根据需要修改文本内容。最终设计完的效果如下：

![48](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/48.png)

第八步：使用Preview预览视图进行预览

![49](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/49.png)

通过上图可以看到，虽然列表数据展示出来了，但是展示的还存在问题。在每条数据遍历时表头也跟着遍历了一遍。这是怎么回事呢？这是由于我们设计的表头和动态Fields都在Detail区域。为了能够解决上面的问题，需要将表头放在Column Header区域，将动态Fields放在Detail区域。具体操作如下：

1、在Outline视图的Column Header点击右键创建出一个区域

![50](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/50.png)

2、将Detail下的静态文本拖动到Column Header下

![51](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/51.png)

拖动完成后如下：

![52](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/52.png)

3、调整静态文本在Column Header区域的位置，最终效果如下

![53](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/53.png)

4、预览查看效果

![54](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/54.png)



- JavaBean数据源数据填充

第一步：复制上面的demo1.jrxml文件，名称改为demo2.jrxml

![55](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/55.png)

修改Report Name：

![57](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/57.png)

第二步：打开demo2.jrxml文件，将detail区域中的动态Fields元素删除

![56](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/56.png)

第三步：将Outline面板中Fields下的字段全部删除

![58](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/58.png)

第四步：清除JDBC数据源和相关SQL语句

![61](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/61.png)

![62](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/62.png)

第五步：在Fields处点击右键创建新的Field

![59](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/59.png)

创建完成后在Properties属性面板中修改Field的名称

![60](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/60.png)

![63](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/63.png)

第六步：将创建的Fields拖动到Detail区域并调整好位置

![64](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/64.png)



注意：使用此种JavaBean数据源数据填充方式，无法正常进行预览，因为这些动态Fields需要在Java程序中动态进行数据填充。

### 3.4 结合JasperReports输出报表

前面我们已经使用Jaspersoft Studio设计了两个模板文件：demo1.jrxml和demo2.jrxml。其中demo1.jrxml的动态列表数据是基于JDBC数据源方式进行数据填充，demo2.jrxml的动态列表数据是基于JavaBean数据源方式进行数据填充。本小节我们就结合JasperReports的Java API来完成pdf报表输出。

#### 3.4.1 JDBC数据源方式填充数据

第一步：创建maven工程，导入相关maven坐标

~~~xml
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports</artifactId>
    <version>6.8.0</version>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
</dependency>
~~~

第二步：将设计好的demo1.jrxml文件复制到当前工程的resources目录下

![65](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/65.png)

第三步：编写单元测试

~~~java
@Test
public void testReport_JDBC() throws Exception{
    Class.forName("com.mysql.jdbc.Driver");
    Connection connection = 
        DriverManager.getConnection("jdbc:mysql://localhost:3306/health", 
                                    "root", 
                                    "root");

    String jrxmlPath = "D:\\ideaProjects\\projects111\\jasperreports_test\\src\\main\\resources\\demo1.jrxml";
    String jasperPath = "D:\\ideaProjects\\projects111\\jasperreports_test\\src\\main\\resources\\demo1.jasper";

    //编译模板
    JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

    //构造数据
    Map paramters = new HashMap();
    paramters.put("company","传智播客");

    //填充数据---使用JDBC数据源方式填充
    JasperPrint jasperPrint = 
        JasperFillManager.fillReport(jasperPath, 
                                    paramters, 
                                    connection);
    //输出文件
    String pdfPath = "D:\\test.pdf";
    JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
}
~~~

通过上面的操作步骤可以输出pdf文件，但是中文的地方无法正常显示。这是因为JasperReports默认情况下对中文支持并不友好，需要我们自己进行修复。具体操作步骤如下：

1、在Jaspersoft Studio中打开demo1.jrxml文件，选中中文相关元素，统一将字体设置为“华文宋体”并将修改后的demo1.jrxml重新复制到maven工程中

2、将本章资源/解决中文无法显示问题目录下的文件复制到maven工程的resources目录中

![66](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/66.png)

按照上面步骤操作后重新执行单元测试导出PDF文件：

![67](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/67.png)

#### 3.4.2 JavaBean数据源方式填充数据

第一步：为了能够避免中文无法显示问题，首先需要将demo2.jrxml文件相关元素字体改为“华文宋体”并将demo2.jrxml文件复制到maven工程的resources目录下

![68](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/68.png)

第二步：编写单元测试方法输出PDF文件

~~~java
@Test
public void testReport_JavaBean() throws Exception{
    String jrxmlPath = "D:\\ideaProjects\\projects111\\jasperreports_test\\src\\main\\resources\\demo2.jrxml";
    String jasperPath = "D:\\ideaProjects\\projects111\\jasperreports_test\\src\\main\\resources\\demo2.jasper";

    //编译模板
    JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

    //构造数据
    Map paramters = new HashMap();
    paramters.put("company","传智播客");

    List<Map> list = new ArrayList();
    Map map1 = new HashMap();
    map1.put("tName","入职体检套餐");
    map1.put("tCode","RZTJ");
    map1.put("tAge","18-60");
    map1.put("tPrice","500");

    Map map2 = new HashMap();
    map2.put("tName","阳光爸妈老年健康体检");
    map2.put("tCode","YGBM");
    map2.put("tAge","55-60");
    map2.put("tPrice","500");
    list.add(map1);
    list.add(map2);

    //填充数据---使用JavaBean数据源方式填充
    JasperPrint jasperPrint = 
        JasperFillManager.fillReport(jasperPath, 
                                     paramters, 
                                     new JRBeanCollectionDataSource(list));
    //输出文件
    String pdfPath = "D:\\test.pdf";
    JasperExportManager.exportReportToPdfFile(jasperPrint,pdfPath);
}
~~~

查看输出效果：

![69](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/69.png)

## 4. 在项目中输出运营数据PDF报表

本小节我们将在项目中实现运营数据的PDF报表导出功能。

### 4.1 设计PDF模板文件

使用Jaspersoft Studio设计运营数据PDF报表模板文件health_business3.jrxml，设计后的效果如下：

![70](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/70.png)

在资源中已经提供好了此文件，直接使用即可。

### 4.2 搭建环境

第一步：在health_common工程的pom.xml中导入JasperReports的maven坐标

~~~xml
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports</artifactId>
    <version>6.8.0</version>
</dependency>
~~~

第二步：将资源中提供的模板文件health_business3.jrxml复制到health_backend工程的template目录下

![71](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/71.png)

第三步：将解决中问题的相关资源文件复制到项目中

![72](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/72.png)

### 4.3 修改页面

修改health_backend工程的report_business.html页面，添加导出PDF的按钮并绑定事件

![73](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/73.png)



![74](F:/%25E9%25BB%2591%25E9%25A9%25AC%25E7%25A8%258B%25E5%25BA%258F%25E5%2591%2598%25E6%2596%2587%25E4%25BB%25B6/%25E4%25BC%25A0%25E6%2599%25BA%25E5%2581%25A5%25E5%25BA%25B7%25E9%25A1%25B9%25E7%259B%25AE/day12/%25E8%25AE%25B2%25E4%25B9%2589/74.png)

### 4.4 Java代码实现

在health_backend工程的ReportController中提供exportBusinessReport4PDF方法

~~~java
//导出运营数据到pdf并提供客户端下载
@RequestMapping("/exportBusinessReport4PDF")
public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response) {
    try {
        Map<String, Object> result = reportService.getBusinessReportData();

        //取出返回结果数据，准备将报表数据写入到PDF文件中
        List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

        //动态获取模板文件绝对磁盘路径
        String jrxmlPath = 
            request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jrxml";
        String jasperPath = 
            request.getSession().getServletContext().getRealPath("template") + File.separator + "health_business3.jasper";
        //编译模板
        JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);

        //填充数据---使用JavaBean数据源方式填充
        JasperPrint jasperPrint =
            JasperFillManager.fillReport(jasperPath,result,
                                         new JRBeanCollectionDataSource(hotSetmeal));

        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/pdf");
        response.setHeader("content-Disposition", "attachment;filename=report.pdf");

        //输出文件
        JasperExportManager.exportReportToPdfStream(jasperPrint,out);

        return null;
    } catch (Exception e) {
        e.printStackTrace();
        return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
    }
}
~~~

