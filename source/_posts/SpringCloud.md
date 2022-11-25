---
title: Spring Cloud
date: 2022-11-23 18:45:24
tags:
- Spring Cloud
categories: 
- 分布式
- 微服务
---

✔️ `:heavy_check_mark:`

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

# 5.服务注册与发现

## :x:Eureka

> -  什么是服务治理
>
>   Spring Cloud 封装了 Netflix 公司开发的 Eureka 模块来实现服务治理
>
>   在传统的rpc远程调用框架中，管理每个服务与服务之间依赖关系比较复杂，管理比较复杂，所以需要使用服务治理，管理服务于服务之间依赖关系，可以实现服务调用、负载均衡、容错等，实现服务发现与注册。
>
> -   什么是服务注册与发现
>
>   Eureka采用了CS的设计架构，Eureka Server 作为服务注册功能的服务器，它是服务注册中心。而系统中的其他微服务，使用 Eureka的客户端连接到 Eureka Server并维持心跳连接。这样系统的维护人员就可以通过 Eureka Server 来监控系统中各个微服务是否正常运行。
>
>   在服务注册与发现中，有一个注册中心。当服务器启动的时候，会把当前自己服务器的信息 比如 服务地址通讯地址等以别名方式注册到注册中心上。另一方（消费者|服务提供者），以该别名的方式去注册中心上获取到实际的服务通讯地址，然后再实现本地RPC调用RPC远程调用框架核心设计思想：在于注册中心，因为使用注册中心管理每个服务与服务之间的一个依赖关系(服务治理概念)。在任何rpc远程框架中，都会有一个注册中心(存放服务地址相关信息(接口地址))

![Eureka和Dubbo的对比](SpringCloud/image-20221125165408158.png)

### 生成EurekaServer端服务注册中心

#### 创建maven子工程

> 创建子工程cloud-eureka-server7001

#### 改POM

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId><!--注意这里是server-->
</dependency>

```

#### 写yaml

```yaml
server:
  port: 7001

eureka:
  instance:
    hostname: localhost #eureka服务端的实例名称
  client:
    #false表示不向注册中心注册自己。
    register-with-eureka: false
    #false表示自己端就是注册中心，我的职责就是维护服务实例，并不需要去检索服务
    fetch-registry: false
    service-url:
      #设置与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址。
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

#### 主启动

<img src="SpringCloud/image-20221125170633503.png" alt="image-20221125170633503" style="zoom:80%;" />

#### 测试

> 访问localhost:7001

![image-20221125171122487](SpringCloud/image-20221125171122487.png)

### 修改提供者支付模块

> EurekaClient端将cloud-provider-payment8001注册进EurekaServer成为服务提供者provider

#### 改pom

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId><!--注意这里是client-->
</dependency>
```

#### 写yaml

```yaml
eureka:
  client:
    #表示是否将自己注册进EurekaServer默认为true。
    register-with-eureka: true
    #是否从EurekaServer抓取已有的注册信息，默认为true。单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    fetchRegistry: true
    service-url:
      defaultZone: http://localhost:7001/eureka
```

#### 主启动

![image-20221125171308002](SpringCloud/image-20221125171308002.png)

#### 测试

<img src="SpringCloud/image-20221125171442381.png" alt="image-20221125171442381" style="zoom:80%;" />

### 修改消费者订单模块

> EurekaClient端将cloud-consumer-order80注册进EurekaServer成为服务消费者consumer

#### 改pom

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId><!--注意这里是client-->
</dependency>
```

#### 写yaml

```yaml
server:
  port: 80


spring:
  application:
    name: cloud-order-service

eureka:
  client:
    #表示是否将自己注册进EurekaServer默认为true。
    register-with-eureka: true
    #是否从EurekaServer抓取已有的注册信息，默认为true。单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    fetchRegistry: true
    service-url:
      defaultZone: http://localhost:7001/eureka
 
```

#### 主启动

![image-20221125171823880](SpringCloud/image-20221125171823880.png)

#### 测试

![image-20221125171920036](SpringCloud/image-20221125171920036.png)