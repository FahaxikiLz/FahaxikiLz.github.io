---
title: Spring Security
date: 2022-11-05 16:36:18
tags:
- Spring Security
categories:
- 后端框架
---

# SSM整合Spring Security

## Spring Security简介

> Spring Security是 Spring提供的安全认证服务的框架。 使用Spring Security可以帮助我们来简化认证和授权的过程。官网：https://spring.io/projects/spring-security

## 入门案例

### 工程搭建

> 对应的maven坐标：

```xml
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
```

#### 配置web.xml

> 在web.xml中主要配置SpringMVC的DispatcherServlet和用于整合第三方框架的DelegatingFilterProxy，用于整合Spring Security。

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

#### 配置`spring-security.xml`

> 在`spring-security.xml`中主要配置Spring Security的拦截规则和认证管理器。

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
                <security:user name="admin" password="{noop}123" authorities="ROLE_ADMIN"></security:user>
            </security:user-service>
        </security:authentication-provider>
    </security:authentication-manager>
</beans>
~~~

#### 优化

##### 配置可匿名访问的资源

```xml
...
<!--
  http：用于定义相关权限控制
  指定哪些资源不需要进行权限校验，可以使用通配符
-->
<security:http security="none" pattern="/pages/a.html" />
<security:http security="none" pattern="/paegs/b.html" />
<security:http security="none" pattern="/pages/**"></security:http>
...
```

##### 使用指定的登录页面

```xml
	...   
	<security:http auto-config="true" use-expressions="true">
        <security:intercept-url pattern="/**" access="hasRole('ROLE_ADMIN')"/>

        <!--如果我们要使用自己指定的页面作为登录页面，必须配置登录表单.页面提交的登录表单请求是由框架负责处理-->
        <!--
            login-page:指定登录页面访问URL
        -->
        <security:form-login
                login-page="/login.html"
                username-parameter="username"
                password-parameter="password"
                login-processing-url="/login.do"
                default-target-url="/index.html"
                authentication-failure-url="/login.html"></security:form-login>

        <!--
          csrf：对应CsrfFilter过滤器
          disabled：是否启用CsrfFilter过滤器，如果使用自定义登录页面需要关闭此项，否则登录操作会被禁用（403）
        -->
        <security:csrf disabled="true"></security:csrf>
    </security:http>
	...
```

##### 从数据库查询用户信息

```java
package com.iteheima.service;

import com.iteheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringSecurityUserService implements UserDetailsService {
    //模拟数据库中的用户数据
    public static Map<String, User> map = new HashMap<>();

    static {
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword("admin");

        User user2 = new User();
        user2.setUsername("xiaoming");
        user2.setPassword("1234");

        map.put(user1.getUsername(), user1);
        map.put(user2.getUsername(), user2);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        模拟向数据库中查询用户
        User user = map.get(username);

//        查询的用户为空返回false
        if (user == null) {
            return null;
        }

//        获取查询用户的密码
        String password = user.getPassword();


//        设置权限和角色
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("permission_A"));//授权
        list.add(new SimpleGrantedAuthority("permission_B"));

//        为admin设置ROLE_ADMIN角色，xiaoming就没有设置角色，xiaoming用户可以登录但是被禁用，因为没有角色
        if (user.getUsername().equals("admin")) {
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

//        将用户名，密码，以及权限角色交给框架，框架用这个密码同表单输入的密码做对比
//		  noop,设置明文
        org.springframework.security.core.userdetails.User userdetail =
                new org.springframework.security.core.userdetails.User(username, "{noop}" + password, list);
        
        return userdetail;
    }
}
```

```xml
	...
    <security:authentication-manager>
        <security:authentication-provider user-service-ref="SpringSecurityUserService">
        </security:authentication-provider>
    </security:authentication-manager>


    <bean id="SpringSecurityUserService" class="com.iteheima.service.SpringSecurityUserService"></bean>
	...
```

##### 对密码进行加密

> 常见的密码加密方式有：
>
> 3DES、AES、DES：使用对称加密算法，可以通过解密来还原出原始密码
>
> MD5、SHA1：使用单向HASH算法，无法通过计算还原出原始密码，但是可以建立彩虹表进行查表破解
>
> bcrypt：将salt随机并混入最终加密后的密码，验证时也无需单独提供之前的salt，从而无需单独处理salt问题
>
> 加密后的格式一般为：
>
> ~~~properties
> $2a$10$/bTVvqqlH9UiE0ZJZ7N2Me3RIgUCdgMheyTgV0B4cMCSokPa.6oCa
> ~~~
>
> 加密后字符串的长度为固定的60位。其中：$是分割符，无意义；2a是bcrypt加密版本号；10是cost的值；而后的前22位是salt值；再然后的字符串就是密码的密文了。

```xml
    ...
	<security:authentication-manager>
        <security:authentication-provider user-service-ref="SpringSecurityUserService2">
            <!--指定度密码进行加密的对象-->
            <security:password-encoder ref="BCryptPasswordEncoder"></security:password-encoder>
        </security:authentication-provider>
    </security:authentication-manager>

    <bean id="SpringSecurityUserService2" class="com.iteheima.service.SpringSecurityUserService2"></bean>

    <!--配置密码加密对象-->
    <bean id="BCryptPasswordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"></bean>

    <!--  开启spring注解  -->
    <context:component-scan base-package="com.iteheima.service"></context:component-scan>
	...
```

```java
package com.iteheima.service;

import com.iteheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringSecurityUserService2 implements UserDetailsService {

    //引入加密对象
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //模拟数据库中的用户数据
    public Map<String, User> map = new HashMap<>();

    public void initDate() {
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword(bCryptPasswordEncoder.encode("admin"));//加密

        User user2 = new User();
        user2.setUsername("xiaoming");
        user2.setPassword(bCryptPasswordEncoder.encode("1234"));

        map.put(user1.getUsername(), user1);
        map.put(user2.getUsername(), user2);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initDate();

//        模拟向数据库中查询用户
        User user = map.get(username);

//        查询的用户为空返回false
        if (user == null) {
            return null;
        }

//        获取查询用户的密码
        String password = user.getPassword();


//        设置权限和角色
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("permission_A"));
        list.add(new SimpleGrantedAuthority("permission_B"));

//        为admin设置ROLE_ADMIN角色，xiaoming就没有设置角色，xiaoming用户可以登录但是被禁用，因为没有角色
        if (user.getUsername().equals("admin")) {
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

//        将用户名，密码，以及权限角色交给框架，框架用这个密码同表单输入的密码做对比
        org.springframework.security.core.userdetails.User userdetail =
                new org.springframework.security.core.userdetails.User(username, password, list);

        return userdetail;
    }
}
```

##### 配置多种校验规则

```xml
    ...
	<security:http auto-config="true" use-expressions="true">

        <!--只要认证通过就可以访问-->
        <security:intercept-url pattern="/pages/a.html" access="isAuthenticated()"/>

        <!--拥有add权限就可以访问b.html页面-->
        <security:intercept-url pattern="/pages/b.html" access="hasAuthority('add')"/>

        <!--拥有ROLE_ADMIN角色就可以访问c.html页面-->
        <security:intercept-url pattern="/pages/c.html" access="hasRole('ROLE_ADMIN')"/>

        <!--拥有ROLE_ADMIN角色就可以访问d.html页面，
            注意：此处虽然写的是ADMIN角色，框架会自动加上前缀ROLE_-->
        <security:intercept-url pattern="/pages/d.html" access="hasRole('ADMIN')"/>

        <security:form-login
                login-page="/login.html"
                username-parameter="username"
                password-parameter="password"
                login-processing-url="/login.do"
                default-target-url="/index.html"
                authentication-failure-url="/login.html"></security:form-login>

        <security:csrf disabled="true"></security:csrf>
    </security:http>
	...
```

```java
package com.iteheima.service;

import com.iteheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringSecurityUserService2 implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //模拟数据库中的用户数据
    public Map<String, User> map = new HashMap<>();

    public void initDate() {
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword(bCryptPasswordEncoder.encode("admin"));

        User user2 = new User();
        user2.setUsername("xiaoming");
        user2.setPassword(bCryptPasswordEncoder.encode("1234"));

        map.put(user1.getUsername(), user1);
        map.put(user2.getUsername(), user2);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initDate();

//        模拟向数据库中查询用户
        User user = map.get(username);

//        查询的用户为空返回false
        if (user == null) {
            return null;
        }

//        获取查询用户的密码
        String password = user.getPassword();


//        设置权限和角色
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("permission_A"));//授权
        list.add(new SimpleGrantedAuthority("permission_B"));

//        为admin设置ROLE_ADMIN角色，xiaoming就没有设置角色，xiaoming用户可以登录但是被禁用，因为没有角色
        if (user.getUsername().equals("admin")) {
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            list.add(new SimpleGrantedAuthority("add"));//授权
        }

//        将用户名，密码，以及权限角色交给框架，框架用这个密码同表单输入的密码做对比
        org.springframework.security.core.userdetails.User userdetail =
                new org.springframework.security.core.userdetails.User(username, password, list);

        return userdetail;
    }
}

```

##### 注解方式权限控制

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:security="http://www.springframework.org/schema/security"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
						http://www.springframework.org/schema/beans/spring-beans.xsd
					    http://www.springframework.org/schema/security
                        http://www.springframework.org/schema/security/spring-security.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">

    <security:http security="none" pattern="/login.html"></security:http>

    <security:http auto-config="true" use-expressions="true">
     
        <security:form-login
                login-page="/login.html"
                username-parameter="username"
                password-parameter="password"
                login-processing-url="/login.do"
                default-target-url="/index.html"
                authentication-failure-url="/login.html"></security:form-login>

        <security:csrf disabled="true"></security:csrf>

        <security:logout logout-url="/exit.do" logout-success-url="/login.html"
                         invalidate-session="true"></security:logout>
    </security:http>

    <security:authentication-manager>
         <security:authentication-provider user-service-ref="SpringSecurityUserService2">
            <security:password-encoder ref="BCryptPasswordEncoder"></security:password-encoder>
        </security:authentication-provider>
    </security:authentication-manager>

    <bean id="SpringSecurityUserService2" class="com.iteheima.service.SpringSecurityUserService2"></bean>

    <bean id="BCryptPasswordEncoder" class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder"></bean>

    <!--  开启spring注解  -->
    <context:component-scan base-package="com.iteheima"></context:component-scan>

    <!--  开启spring security注解  -->
    <security:global-method-security pre-post-annotations="enabled"></security:global-method-security>
</beans>
```

```java
package com.iteheima.service;

import com.iteheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpringSecurityUserService2 implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //模拟数据库中的用户数据
    public Map<String, User> map = new HashMap<>();

    public void initDate() {
        User user1 = new User();
        user1.setUsername("admin");
        user1.setPassword(bCryptPasswordEncoder.encode("admin"));

        User user2 = new User();
        user2.setUsername("xiaoming");
        user2.setPassword(bCryptPasswordEncoder.encode("1234"));

        map.put(user1.getUsername(), user1);
        map.put(user2.getUsername(), user2);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initDate();

//        模拟向数据库中查询用户
        User user = map.get(username);

//        查询的用户为空返回false
        if (user == null) {
            return null;
        }

//        获取查询用户的密码
        String password = user.getPassword();


//        设置权限和角色
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("permission_A"));
        list.add(new SimpleGrantedAuthority("permission_B"));

//        为admin设置ROLE_ADMIN角色，xiaoming就没有设置角色，xiaoming用户可以登录但是被禁用，因为没有角色
        if (user.getUsername().equals("admin")) {
            list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            list.add(new SimpleGrantedAuthority("add"));
        }

//        将用户名，密码，以及权限角色交给框架，框架用这个密码同表单输入的密码做对比
        org.springframework.security.core.userdetails.User userdetail =
                new org.springframework.security.core.userdetails.User(username, password, list);

        return userdetail;
    }
}

```

```java
package com.iteheima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    @PreAuthorize("hasRole('ROLE_ADMIN')")//表示用户必须拥有ROLE_ADMIN角色才能调用当前方法
    public String hello() {
        return "hello";
    }
}
```

##### 退出登录

```xml
   	... 
	<security:http auto-config="true" use-expressions="true">
		...
        <security:logout logout-url="/exit.do" logout-success-url="/login.html"
                         invalidate-session="true"></security:logout>
    </security:http>
	...
```

#### 配置项文件

```xml
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

    <!--配置哪些资源匿名可以访问（不登录也可以访问）-->
    <!--<security:http security="none" pattern="/pages/a.html"></security:http>
    <security:http security="none" pattern="/pages/b.html"></security:http>-->
    <!--<security:http security="none" pattern="/pages/**"></security:http>-->
    <security:http security="none" pattern="/login.html"></security:http>
    <!--
        auto-config:自动配置，如果设置为true，表示自动应用一些默认配置，比如框架会提供一个默认的登录页面
        use-expressions:是否使用spring security提供的表达式来描述权限
    -->
    <security:http auto-config="true" use-expressions="true">
        <!--配置拦截规则，/** 表示拦截所有请求-->
        <!--
            pattern:描述拦截规则
            asscess:指定所需的访问角色或者访问权限
        -->
        <!--只要认证通过就可以访问-->
        <security:intercept-url pattern="/pages/a.html"  access="isAuthenticated()" />

        <!--拥有add权限就可以访问b.html页面-->
        <security:intercept-url pattern="/pages/b.html"  access="hasAuthority('add')" />

        <!--拥有ROLE_ADMIN角色就可以访问c.html页面-->
        <security:intercept-url pattern="/pages/c.html"  access="hasRole('ROLE_ADMIN')" />

        <!--拥有ROLE_ADMIN角色就可以访问d.html页面，
            注意：此处虽然写的是ADMIN角色，框架会自动加上前缀ROLE_-->
        <security:intercept-url pattern="/pages/d.html"  access="hasRole('ADMIN')" />
        <security:intercept-url pattern="/**" access="hasRole('ROLE_ADMIN')"></security:intercept-url>

        <!--如果我们要使用自己指定的页面作为登录页面，必须配置登录表单.页面提交的登录表单请求是由框架负责处理-->
        <!--
            login-page:指定登录页面访问URL
        -->
        <security:form-login
                login-page="/login.html"
                username-parameter="username"
                password-parameter="password"
                login-processing-url="/login.do"
                default-target-url="/index.html"
                authentication-failure-url="/login.html"></security:form-login>

        <!--
          csrf：对应CsrfFilter过滤器
          disabled：是否启用CsrfFilter过滤器，如果使用自定义登录页面需要关闭此项，否则登录操作会被禁用（403）
        -->
        <security:csrf disabled="true"></security:csrf>

        <!--
          logout：退出登录
          logout-url：退出登录操作对应的请求路径
          logout-success-url：退出登录后的跳转页面
        -->
        <security:logout logout-url="/logout.do"
                         logout-success-url="/login.html" invalidate-session="true"/>

    </security:http>

    <!--配置认证管理器-->
    <security:authentication-manager>
        <!--配置认证提供者-->
        <security:authentication-provider user-service-ref="userService2">
            <!--
                    配置一个具体的用户，后期需要从数据库查询用户
            <security:user-service>
                <security:user name="admin" password="{noop}1234" authorities="ROLE_ADMIN"/>
            </security:user-service>
            -->
            <!--指定度密码进行加密的对象-->
            <security:password-encoder ref="passwordEncoder"></security:password-encoder>
        </security:authentication-provider>
    </security:authentication-manager>

    <bean id="userService" class="com.itheima.service.SpringSecurityUserService"></bean>
    <bean id="userService2" class="com.itheima.service.SpringSecurityUserService2"></bean>
    <!--配置密码加密对象-->
    <bean id="passwordEncoder"
          class="org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder" />

    <!--开启spring注解使用-->
    <context:annotation-config></context:annotation-config>

    <mvc:annotation-driven></mvc:annotation-driven>
    <context:component-scan base-package="com.itheima.controller"></context:component-scan>

    <!--开启注解方式权限控制-->
    <security:global-method-security pre-post-annotations="enabled" />
</beans>
```

