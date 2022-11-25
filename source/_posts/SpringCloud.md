---
title: Spring Cloud
date: 2022-11-23 18:45:24
tags:
- Spring Cloud
categories: 
- 分布式
- 微服务
---

# 1.微服务概述

## 什么是微服务？

> - 微服务化的核心就是将传统的一站式应用，根据业务拆分成一个一个的服务，彻底地去耦合,每一个微服务提供单个业务功能的服务，一个服务做一件事，从技术角度看就是一种小而独立的处理过程，类似进程概念，能够自行单独启动或销毁，拥有自己独立的数据库。
> - Spring Cloud=分布式微服务架构下的一站式解决方案，是各个微服务架构落地技术的集合体，俗称微服务全家桶

# 2.从2.2.x和H版开始说起

## Spring cloud和Spring boot之间的依赖关系

> 在[spring cloud官网](https://spring.io/projects/spring-cloud#overview)查看

<img src="SpringCloud/image-20221123232401415.png" alt="image-20221123232401415" style="zoom:60%;" />

> 更详细的版本对应查看方法：https://start.spring.io/actuator/info

<img src="SpringCloud/image-20221123232645198.png" alt="image-20221123232645198" style="zoom:80%;" />

> 本课版本选型

<img src="SpringCloud/image-20221123232814386.png" alt="image-20221123232814386" style="zoom:67%;" />

# 3.关于Cloud各种组件的停更/升级/替换

## 由停更引发的“升级惨案”

![image-20221124140642717](SpringCloud/image-20221124140642717.png)

## 参考资料

> - [spring cloud官方文档](https://cloud.spring.io/spring-cloud-static/Hoxton.SR1/reference/htmlsingle/)
> - [spring cloud中文文档](https://www.bookstack.cn/read/spring-cloud-docs/docs-index.md)
> - [spring boot官方文档](https://docs.spring.io/spring-boot/docs/2.2.2.RELEASE/reference/htmlsingle/)

# 4.微服务架构编码构建

## IDEA新建project工作空间

> 创建一个maven项目，作为父工程

> 字符编码

<img src="SpringCloud/image-20221124145256044.png" alt="image-20221124145256044" style="zoom:50%;" />

> 注解生效激活

<img src="SpringCloud/image-20221124145234857.png" alt="image-20221124145234857" style="zoom:50%;" />

> java编译版本选8

<img src="SpringCloud/image-20221124145356837.png" alt="image-20221124145356837" style="zoom:50%;" />

> File Type过滤

<img src="SpringCloud/image-20221124145511460.png" alt="image-20221124145511460" style="zoom:50%;" />

> 父工程POM

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.atguigu</groupId>
    <artifactId>cloud2020</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--  父工程一定要打pom包  -->
    <packaging>pom</packaging>

    <!-- 统一管理jar包版本 -->
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <junit.version>4.12</junit.version>
        <log4j.version>1.2.17</log4j.version>
        <lombok.version>1.16.18</lombok.version>
        <mysql.version>5.1.47</mysql.version>
        <druid.version>1.1.16</druid.version>
        <mybatis.spring.boot.version>1.3.0</mybatis.spring.boot.version>
    </properties>

    <!-- 子模块继承之后，提供作用：锁定版本+子modlue不用写groupId和version  -->
    <dependencyManagement>
        <dependencies>
            <!--spring boot 2.2.2-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>2.2.2.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--spring cloud Hoxton.SR1-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.SR1</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--spring cloud alibaba 2.1.0.RELEASE-->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>2.1.0.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>${druid.version}</version>
            </dependency>
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${mybatis.spring.boot.version}</version>
            </dependency>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
            </dependency>
            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
                <version>${log4j.version}</version>
            </dependency>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
                <optional>true</optional>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                    <addResources>true</addResources>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

> maven中跳过单元测试

```xml
<!-- 方法一 -->
<build><!-- maven中跳过单元测试 -->
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
                <skip>true</skip>
            </configuration>
        </plugin>
    </plugins>
</build>
```

方法二

<img src="SpringCloud/image-20221124153144155.png" alt="方法二" style="zoom:70%;" />

> 父工程创建完成执行`mvn:install`将父工程发布到仓库方便子工程继承

<img src="SpringCloud/image-20221124153337897.png" alt="image-20221124153337897" style="zoom:67%;" />

## Rest微服务工程构建

### 微服务提供者支付Module模块

#### 创建maven子工程

> cloud-provider-payment8001	微服务提供者支付Module模块
>
> 创建完成后请回到父工程查看pom文件变化

![image-20221124153852258](SpringCloud/image-20221124153852258.png)

#### 改POM

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>cloud2020</artifactId>
        <groupId>com.atguigu</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>cloud-provider-payment8001</artifactId>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <!--mysql-connector-java-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!--jdbc-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

#### 写YML

```yaml
server:
  port: 8001

spring:
  application:
    name: cloud-payment-service
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource            # 当前数据源操作类型
    driver-class-name: org.gjt.mm.mysql.Driver              # mysql驱动包 com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/db2019?useUnicode=true&characterEncoding=utf-8&useSSL=false
    username: root
    password: 123456


mybatis:
  mapperLocations: classpath:mapper/*.xml
  type-aliases-package: com.atguigu.springcloud.entities    # 所有Entity别名类所在包
```

#### 主启动

```java
package com.atguigu.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author:lz
 * @Date:2022-11-24 15:42
 * @Description
 */
@SpringBootApplication
public class PaymentMain8001 {

    public static void main(String[] args)
    {
        SpringApplication.run(PaymentMain8001.class,args);
    }

}
```

#### 业务类

> controller—service—dao

```java
package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: lz
 * @Date: 2022-11-24 0024 17:08
 * @Description:
 */

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    /**
     * 添加serial
     *
     * @param payment
     * @return
     */
    @PostMapping("/create")
    public CommonResult create(@RequestBody Payment payment) {
        int i = paymentService.create(payment);
        if (i > 0) {
            return new CommonResult(200, "添加数据库成功", i);
        } else {
            return new CommonResult(444, "添加数据库失败");
        }
    }

    @GetMapping("/getSerialById/{id}")
    public CommonResult getSerialById(@PathVariable("id") Long id) {
        Payment payment = paymentService.getSerialById(id);
        if (payment != null) {
            return new CommonResult(200, "查询成功", payment);
        }
        return new CommonResult(444, "查询失败", null);
    }

}

```

#### 测试

<img src="SpringCloud/image-20221125142111769.png" alt="image-20221125142111769" style="zoom:67%;" />

### dev-tools热部署

> 在子项目中添加依赖

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
```

> 在父项目中配置插件

```xml
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <fork>true</fork>
                    <addResources>true</addResources>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

> 开启自动构建

<img src="SpringCloud/image-20221125132516516.png" alt="image-20221125132516516" style="zoom:50%;" />

> ctrl + shift + alt + /

![image-20221125132554379](SpringCloud/image-20221125132554379.png)

<img src="SpringCloud/image-20221125133732509.png" alt="image-20221125133732509" style="zoom:65%;" />

### 微服务消费者订单Module模块

> 创建子工程cloud-consumer-order80	微服务消费者订单Module模块
>
> - 改pom
> - 写yaml
> - 主启动（同上一个模块）

#### RestTemplate

> RestTemplate提供了多种便捷访问远程Http服务的方法， 是一种简单便捷的**访问restful服务模板类**，是Spring提供的用于访问Rest服务的客户端模板工具集

#### 业务类

> 配置RestTemplate

```java
package com.atguigu.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: lz
 * @Date: 2022-11-25 0025 14:06
 * @Description:
 */
@Configuration
public class ApplicationContextConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

```

> controller

```java
package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: lz
 * @Date: 2022-11-25 0025 14:04
 * @Description:
 */

@RestController
@Slf4j
@RequestMapping("/consumer")
public class ConsumerController {

    // 提供者支付module模块的url
    public static final String PAYMENT_URL = "http://localhost:8001";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/create")
    public CommonResult create(Payment payment) {

        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
    }


    @GetMapping("/getSerialById/{id}")
    public CommonResult getSerialById(@PathVariable("id") Long id) {

        return restTemplate.getForObject(PAYMENT_URL + "/payment/getSerialById/" + id, CommonResult.class, id);
    }


}
```

#### 测试

> 先直接访问提供者支付模块的controller

<img src="SpringCloud/image-20221125142111769.png" alt="image-20221125142111769" style="zoom:67%;" />

> 再访问消费者订单模块，同样也可以调用到

<img src="SpringCloud/image-20221125142047104.png" alt="image-20221125142047104" style="zoom:67%;" />

> 测试消费者订单模块的添加serial

<img src="SpringCloud/image-20221125143408377.png" alt="image-20221125143408377" style="zoom:67%;" />

<img src="SpringCloud/image-20221125143435385.png" alt="image-20221125143435385" style="zoom:80%;" />

### 工程重构

> 在我们的提供者支付模块和消费者订单模块都有payment实体类和统一返回类型的类，这样就会造成代码的冗余，我们可以把实体类和统一返回类型的类提取到一个公共的工程中
>
> 步骤
>
> - 创建公共工程cloud-api-commons
>
> - 修改pom
>
> - 将实体类和统一返回类型的类放在此工程中
>
> - maven命令`clean install`
>
> - 提供者支付模块和消费者订单模块删除这两个类，在pom中导入公共工程
>
>   ![image-20221125161045902](SpringCloud/image-20221125161045902.png)

### 目前工程样图

<img src="SpringCloud/%E7%9B%AE%E5%89%8D%E5%B7%A5%E7%A8%8B%E6%A0%B7%E5%9B%BE.png" style="zoom:60%;" />