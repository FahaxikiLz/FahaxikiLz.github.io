---
title: 尚硅谷Spring Security
date: 2023-11-03 13:26:56
tags:
- Spring Security + OAuth2 
categories:
- 后端框架
published: false
---

# SpringSecurity

## 简介

一般来说，Web 应用的安全性包括**用户认证（Authentication）和用户授权 （Authorization）**两个部分，这两点也是 Spring Security 重要核心功能。 

1. 用户认证指的是：验证某个用户是否为系统中的合法主体，也就是说用户能否访问 该系统。用户认证一般要求用户提供用户名和密码。系统通过校验用户名和密码来完成认 证过程。通俗点说就是系统认为用户是否能登录 
2. 用户授权指的是验证某个用户是否有权限执行某个操作。在一个系统中，不同用户 所具有的权限是不同的。比如对一个文件来说，有的用户只能进行读取，而有的用户可以 进行修改。一般来说，系统会为不同的用户分配不同的角色，而每个角色则对应一系列的 权限。通俗点讲就是系统判断用户是否有权限去做某些事情

## 入门案例

### maven

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.0.5</version>
        </dependency>
        <!--mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.37</version>
        </dependency>
        <!--lombok用来简化实体类-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
    </dependencies>
```

### 一个简单的controller

```java
package com.lz.springsecurityatguigu.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello security";
    }
}

```

### 启动

![image-20231103142719877](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231103142719877.png)

默认的用户名：user 

密码在项目启动的时候在控制台会打印，注意每次启动的时候密码都回发生变化

![image-20231103142742564](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231103142742564.png)



![image-20231103142814041](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231103142814041.png)

##  基本原理

**SpringSecurity 本质是一个过滤器链**

从启动是可以获取到过滤器链

```java
org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter
org.springframework.security.web.context.SecurityContextPersistenceFilter 
org.springframework.security.web.header.HeaderWriterFilter
org.springframework.security.web.csrf.CsrfFilter
org.springframework.security.web.authentication.logout.LogoutFilter 
org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter 
org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter 
org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter
org.springframework.security.web.savedrequest.RequestCacheAwareFilter
org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter
org.springframework.security.web.authentication.AnonymousAuthenticationFilter 
org.springframework.security.web.session.SessionManagementFilter 
org.springframework.security.web.access.ExceptionTranslationFilter 
org.springframework.security.web.access.intercept.FilterSecurityInterceptor
```

### 重要的三个过滤器

1. FilterSecurityInterceptor：是一个方法级的权限过滤器, 基本位于过滤链的最底部。
2. ExceptionTranslationFilter：是个异常过滤器，用来处理在认证授权过程中抛出的异常
3. UsernamePasswordAuthenticationFilter ：**对/login 的 POST 请求做拦截，校验表单中用户 名，密码。**

### UserDetailsService 接口

当什么也没有配置的时候，账号和密码是由 Spring Security 定义生成的。而在实际项目中 账号和密码都是从数据库中查询出来的。 所以我们要通过自定义逻辑控制认证逻辑。如果需要自定义逻辑时，只需要实现 UserDetailsService 接口即可。

<img src="尚硅谷Spring Security/image-20231103151736369.png" alt="image-20231103151736369" style="zoom:67%;" />

#### 实现类

<img src="尚硅谷Spring Security/image-20231103151749958.png" alt="image-20231103151749958" style="zoom:80%;" />

<img src="尚硅谷Spring Security/image-20231103151834257.png" alt="image-20231103151834257" style="zoom:80%;" />

#### 返回值 UserDetails

这个类是系统默认的用户“主体

```java
// 表示获取登录用户所有权限
Collection<? extends GrantedAuthority> getAuthorities();
// 表示获取密码
String getPassword();
// 表示获取用户名
String getUsername();
// 表示判断账户是否过期
boolean isAccountNonExpired();
// 表示判断账户是否被锁定
boolean isAccountNonLocked();
// 表示凭证{密码}是否过期
boolean isCredentialsNonExpired();
// 表示当前用户是否可用
boolean isEnabled();

```

#### 方法参数 username

表示**用户名**。此值是客户端表单传递过来的数据。默认情况下必须叫 username，否则无 法接收。

### PasswordEncoder 接口

```java
// 表示把参数按照特定的解析规则进行解析
String encode(CharSequence rawPassword);

// 表示验证从存储中获取的编码密码与编码后提交的原始密码是否匹配。如果密码匹配，则返回 true；如果不匹配，则返回 false。第一个参数表示需要被解析的密码。第二个参数表示存储的密码。
boolean matches(CharSequence rawPassword, String encodedPassword);

// 表示如果解析的密码能够再次进行解析且达到更安全的结果则返回 true，否则返回false。默认返回 false。
default boolean upgradeEncoding(String encodedPassword) {
	return false;
}

```

#### 实现类

<img src="尚硅谷Spring Security/image-20231103151925031.png" alt="image-20231103151925031" style="zoom:80%;" />

- BCryptPasswordEncoder 是 Spring Security 官方推荐的密码解析器，平时多使用这个解析 器。 
- BCryptPasswordEncoder 是对 bcrypt 强散列方法的具体实现。是**基于 Hash 算法实现的单向加密**。可以通过 strength 控制加密强度，默认 10.

## 权限方案

### 实现数据库认证来完成用户登录

#### 准备sql

```mysql
-- 创建用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) UNIQUE NOT NULL,
    password VARCHAR(100)
);

-- 插入用户数据
INSERT INTO users (id, username, password) VALUES
(1, '张san', '$2a$10$2R/M6iU3mCZt3ByG7kwYTeeW0w7/UqdeXrb27zkBIizBvAven0/na'),
(2, '李si', '$2a$10$2R/M6iU3mCZt3ByG7kwYTeeW0w7/UqdeXrb27zkBIizBvAven0/na');

-- 创建角色表
CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20)
);

-- 插入角色数据
INSERT INTO role (id, name) VALUES
(1, '管理员'),
(2, '普通用户');

-- 创建用户角色关联表
CREATE TABLE role_user (
    uid BIGINT,
    rid BIGINT
);

-- 插入用户角色关联数据
INSERT INTO role_user (uid, rid) VALUES
(1, 1),
(2, 2);

-- 创建菜单表
CREATE TABLE menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20),
    url VARCHAR(100),
    parentid BIGINT,
    permission VARCHAR(20)
);

-- 插入菜单数据
INSERT INTO menu (id, name, url, parentid, permission) VALUES
(1, '系统管理', '', 0, 'menu:system'),
(2, '用户管理', '', 0, 'menu:user');

-- 创建角色菜单关联表
CREATE TABLE role_menu (
    mid BIGINT,
    rid BIGINT
);

-- 插入角色菜单关联数据
INSERT INTO role_menu (mid, rid) VALUES
(1, 1),
(2, 1),
(2, 2);

```

#### 实体类

```java
@Data
public class Users {
    private Integer id;
    private String username;
    private String password;
}
```

#### 配置类

```java
package com.lz.springsecurityatguigu.config;

import com.lz.springsecurityatguigu.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsersServiceImpl usersServiceImpl;

    /*密码加密*/
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /*认证*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersServiceImpl);
    }
}

```

#### 实现类

```java
package com.lz.springsecurityatguigu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lz.springsecurityatguigu.entity.Users;
import com.lz.springsecurityatguigu.mapper.UsersMapper;
import com.lz.springsecurityatguigu.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;

/**
 * @author 13902
 * @description 针对表【users】的数据库操作Service实现
 * @createDate 2023-11-03 16:18:53
 */
@Service("usersServiceImpl")
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
        implements UsersService, UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Users::getUsername, username);
        Users users = usersMapper.selectOne(wrapper);
        Assert.notNull(users, "用户不存在");

        // 使用工具类，设置角色
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_admin");

        // 加密
        return new User(username, users.getPassword(), grantedAuthorities);
    }
}
```

**注意**

1. 如果SpringsecurityConfig中设置了加密方式，也就是**数据库中存储的密码是加密之后的**，前端表单提交的密码，会在加密之后比对。

   <img src="%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20240218161726959.png" alt="image-20240218161726959" style="zoom: 80%;" />

2. 另一种方式或者**数据库中存储明文**的时候，不设置加密方式

   ```java
   // 密文
   User user = new User(username, "{bcrypt}" + userEntity.getPassword(), authorities);
   
   // 明文
   User user = new User(username, "{noop}" + userEntity.getPassword(), authorities);
   ```

   ```java
   // {bcrypt}：BCrypt强哈希方法
   {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
       
   // {noop}：无加密
   {noop}password
       
   // {PBKDF2}：PBKDF2加密
   {pbkdf2}5d923b44a6d129f3ddf3e3c8d29412723dcbde72445e8ef6bf3b508fbf17fa4ed4d6b99ca763d8dc
       
   // {scrypt}：scrypt加密
   {scrypt}$e0801$8bWJaSu2IKSn9Z9kM+TPXfOc/9bdYSrN1oD9qfVThWEwdRTnO7re7Ei+fUZRJ68k9lTyuTeUp4of4g24hHnazw==$OAOec05+bXxvuu/1qZ6NUR+xQYvYv7BeL1QxwRpY5Pc=
       
   // {sha256}：sha256加密
   {sha256}97cde38028ad898ebc02e690819fa220e88c62e0699403e94fff291cfffaf8410849f27605abcbc0
   ```

### 未认证请求跳转到登录页

#### 登录页面

```html
<!--这里的提交路径和登录访问路径一致-->
<form action="/user/login" method="post">
    username:<input type="text" name="username"/></br>
    password:<input type="text" name="password"/></br>
    <input type="submit"/>
</form>
```

**注意：页面提交方式必须为 post 请求，所以上面的页面不能使用，用户名，密码必须为 username,password** 

原因： 在执行登录的时候会走一个过滤器 UsernamePasswordAuthenticationFilter

<img src="尚硅谷Spring Security/image-20231106105837102.png" alt="image-20231106105837102" style="zoom:80%;" />

如果修改配置可以调用 usernameParameter()和 passwordParameter()方法。

<img src="尚硅谷Spring Security/image-20231106105852886.png" alt="image-20231106105852886" style="zoom:67%;" />

#### controller

```java

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "hello security";
    }

    @GetMapping("/index")
    public String index() {
        return "hello index";
    }
}

```

#### 配置类

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	......

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login.html")   //登录页
                .loginProcessingUrl("/user/login")  //登录访问路径
                .defaultSuccessUrl("/test/index").permitAll()   //登录成功后，跳转路径
                .and().authorizeRequests()
                .antMatchers("/", "/test/hello", "/user/login").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();    //关闭csrf防护
    }
}
```

#### 测试

访问/test/index需要登录

![image-20231106105657182](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231106105657182.png)

登录成功

![image-20231106105741493](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231106105741493.png)

### 基于角色或权限进行访问控制

- hasAuthority方法：如果当前的主体具有指定的权限，则返回 true,否则返回 false
- hasAnyAuthority方法：如果当前的主体有任何提供的角色（给定的作为一个逗号分隔的字符串列表）的话，返回 true.
- hasRole方法：如果用户具备给定角色就允许访问,否则出现 403。 如果当前主体具有指定的角色，则返回 true。
- hasAnyRole方法：表示用户具备任何一个条件都可以访问。

#### 配置

![image-20231106113343699](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231106113343699.png)

注意配置文件中设置角色不需要添加”ROLE_“，因为上述的底层代码会自动添加与之进行匹配。

![image-20231106114113271](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231106114113271.png)

如果在配置文件中的角色以"ROLE"开始就会报错：`role should not start with 'ROLE_' since it is automatically inserted. Got 'ROLE_admin'`

#### 设置角色和权限

![image-20231106113435551](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231106113435551.png)

#### 自定义403页

403 Forbidden是 HTTP协议中的一个状态码(Status Code)，可以简单理解为没有权限访问此站。

```java
http.exceptionHandling().accessDeniedPage("/unauth.html");
```

![image-20231106114615809](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231106114615809.png)

#### 基于注解

##### @Secured 

- 判断是否具有任一角色，另外需要注意的是**这里匹配的字符串需要添加前缀“ROLE_“。**
- **使用注解先要开启注解功能：`@EnableGlobalMethodSecurity(securedEnabled=true)`**

```java
    @Secured({"ROLE_admin","ROLE_guest"})
    @GetMapping("/select")
    public String select() {
        return "hello select";
    }
```

![image-20231106141226132](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231106141226132.png)

##### @PreAuthorize/@PostAuthorize

- **开启注解功能： `@EnableGlobalMethodSecurity(prePostEnabled = true)`**
- @PreAuthorize注解适合进入方法前的权限验证， @PreAuthorize 可以将登录用 户的 roles/permissions 参数传到方法中。
- @PostAuthorize注解使用并不多，在方法执行后再进行权限验证，适合验证带有返回值 的权限。

```java
//    @PreAuthorize("hasRole('ROLE_admin')")
//    @PreAuthorize("hasAnyAuthority('select','update')")
    @PostAuthorize("hasAuthority('select')")
    @GetMapping("/select")
    public String select() {
        return "hello select";
    }
```

![image-20231106142142010](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231106142142010.png)

##### @PostFilter/@PreFilter???

- @PostFilter ：权限验证之后对数据进行过滤
- @PreFilter: 进入控制器之前对数据进行过滤

```java
    @RequestMapping("getAll")
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostFilter("returnObject.username == 'admin1'")
    public List<Users> getAllUser() {
        ArrayList<Users> list = new ArrayList<>();
        list.add(new Users(1l, "admin1", "666"));
        list.add(new Users(2l, "admin2", "888"));
        return list;//[Users(id=1, username=admin1, password=666)]
    }


    @RequestMapping("getTestPreFilter")
    @PreAuthorize("hasRole('ROLE_管理员')")
    @PreFilter(value = "filterObject.id%2==0")
    public List<Users> getTestPreFilter(@RequestBody List<Users> list) {
        System.out.println(list.toString());//[Users(id=2, username=admin2, password=888)]
        return list;
    }
```

#### [权限表达式](https://docs.spring.io/spring-security/site/docs/5.3.4.RELEASE/reference/html5/#el-access)

### 基于数据库的记住我

#### 创建数据库

也可以不手动创建，可以设置框架启动自动创建

```mysql
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

```

#### 配置类

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsersServiceImpl usersServiceImpl;

    @Autowired
    private DataSource dataSource;

    ......
    
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);// 注入数据源
        repository.setCreateTableOnStartup(true);// 启动时创建数据库表
        return repository;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
		......
            
        /*记住我功能*/
        http.rememberMe().tokenRepository(persistentTokenRepository()).userDetailsService(usersServiceImpl);
    }
}

```

#### 登录页面

```html
<form action="/user/login" method="post">
    username:<input type="text" name="username"/></br>
    password:<input type="text" name="password"/></br>
    记住我:<input type="checkbox" name="remember-me"/></br>
    <input type="submit"/>
</form>
```

<span style="color:red">注意：name属性值必须为remember-me，不能改为其他值</span>

#### 结果

登录之后，关闭浏览器，再次打开访问不用再次登录

![image-20231108094553377](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231108094553377.png)

### 踢掉已登录用户

```java
http.sessionManagement().maximumSessions(1);
```

### CSRF 

跨站请求伪造（英语：Cross-site request forgery），也被称为 one-click attack 或者 session riding，通常缩写为 CSRF 或者 XSRF， 是一种挟制用户在当前已 登录的 Web 应用程序上执行非本意的操作的攻击方法。跟跨网站脚本（XSS）相比，XSS 利用的是用户对指定网站的信任，CSRF 利用的是网站对用户网页浏览器的信任。

跨站请求攻击，简单地说，是攻击者通过一些技术手段欺骗用户的浏览器去访问一个 自己曾经认证过的网站并运行一些操作（如发邮件，发消息，甚至财产操作如转账和购买 商品）。由于浏览器曾经认证过，所以被访问的网站会认为是真正的用户操作而去运行。 这利用了 web 中用户身份验证的一个漏洞：简单的身份验证只能保证请求发自某个用户的 浏览器，却不能保证请求本身是用户自愿发出的。 

从 Spring Security 4.0 开始，默认情况下会启用 CSRF 保护，以防止 CSRF 攻击应用 程序，Spring Security CSRF 会针对 PATCH，POST，PUT 和 DELETE 方法进行防护。

#### 案例

关闭安全配置的类中的 csrf

```java
// http.csrf().disable();
```

在登录页面添加一个隐藏域：

```html
<input type="hidden" th:if="${_csrf}!=null" th:value="${_csrf.token}" name="_csrf"/>
```

#### 实现原理

生成 csrfToken 保存到 HttpSession 或者 Cookie 中。

<img src="尚硅谷Spring Security/image-20231108101225880.png" alt="image-20231108101225880" style="zoom:80%;" />

SaveOnAccessCsrfToken 类有个接口 CsrfTokenRepository

![image-20231108101239843](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20231108101239843.png)

当前接口实现类：HttpSessionCsrfTokenRepository，CookieCsrfTokenRepository

<img src="尚硅谷Spring Security/image-20231108101314076.png" alt="image-20231108101314076" style="zoom:80%;" />

<img src="尚硅谷Spring Security/image-20231108101321723.png" alt="image-20231108101321723" style="zoom:80%;" />

请求到来时，从请求中提取 csrfToken，和保存的 csrfToken 做比较，进而判断当 前请求是否合法。主要通过 CsrfFilter 过滤器来完成。

<img src="尚硅谷Spring Security/image-20231108101351092.png" alt="image-20231108101351092" style="zoom:80%;" />

# 4.SpringSecurity微服务权限

## 4.1 什么是微服务

**1、微服务由来**

微服务最早由Martin Fowler 与 James Lewis 于 2014 年共同提出，微服务架构风格是一种使用一套小服务来开发单个应用的方式途径，每个服务运行在自己的进程中，并使用轻量级机制通信，通常是 HTTP API，这些服务基于业务能力构建，并能够通过自动化部署机制来独立部署，这些服务使用不同的编程语言实现，以及不同数据存储技术，并保持最低限度的集中式管理。

**2 、微服务优势**

（1）微服务每个模块就相当于一个单独的项目，代码量明显减少，遇到问题也相对来说比较好解决。

（2）微服务每个模块都可以使用不同的存储方式（比如有的用 redis，有的用 mysql等），数据库也是单个模块对应自己的数据库。

（3) 微服务每个模块都可以使用不同的开发技术，开发模式更灵活。

**3 、微服务本质**

（1） 微服务，关键其实不仅仅是微服务本身，而是系统要提供一套基础的架构，这种架构使得微服务可以独立的部署、运行、升级，不仅如此，这个系统架构还让微服务与微服务之间在结构上“松耦合”，而在功能上则表现为一个统一的整体。这种所谓的“统一的整体”表现出来的是统一风格的界面，统一的权限管理，统一的安全策略，统一的上线过 程，统一的日志和审计方法，统一的调度方式，统一的访问入口等等。

（2） 微服务的目的是有效的拆分应用，实现敏捷开发和部署。

## 4.2 微服务认证与授权实现思路

### 1、认证授权过程分析

（1） 如果是基于Session，那么 Spring-security 会对 cookie 里的 sessionid 进行解析，找到服务器存储的 session 信息，然后判断当前用户是否符合请求的要求。

（2） 如果是token，则是解析出 token，然后将当前请求加入到 Spring-security 管理的权限信息中去

![img](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230122235150283.png)



如果系统的模块众多，每个模块都需要进行授权与认证，所以我们选择基于 token 的形式进行授权与认证，用户根据用户名密码认证成功，然后获取当前用户角色的一系列权限 值，并以用户名为key，权限列表为value 的形式存入 redis 缓存中，根据用户名相关信息生成token 返回，浏览器将 token 记录到 cookie 中，每次调用 api 接口都默认将token 携带到 header 请求头中，Spring-security 解析 header 头获取 token 信息，解析 token 获取当前用户名，根据用户名就可以从redis 中获取权限列表，这样 Spring-security 就能够判断当前请求是否有权限访问

### 2、权限管理数据模型

![image-20230122235855639](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230122235855639.png)

### 3、项目搭建

![image-20230125125127014](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125125127014.png)

#### 3.1、创建父工程

![image-20230125125538304](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125125538304.png)

![image-20230125125828478](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125125828478.png)

#### 3.2、创建common子模块

![image-20230125130116408](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125130116408.png)

![image-20230125130215249](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125130215249.png)

##### 3.2.1 common目录下创建子模块service_base

![image-20230125131403368](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125131403368.png)

##### 3.2.2 common目录下创建子模块spring_security

![image-20230125131449252](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125131449252.png)

#### 3.3、创建infrastructure子模块

![image-20230125132559462](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125132559462.png)

##### 3.3.1、创建api_gateway子模块

![image-20230125132750029](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125132750029.png)

#### 3.4、创建service子模块

![image-20230125133020703](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125133020703.png)

##### 3.4.1、创建service_acl子模块

![image-20230125133303390](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125133303390.png)

#### 3.5、项目规划

![image-20230125133628999](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230125133628999.png)

### 4、引入依赖

**acl_parent**

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <modules>
        <module>common</module>
        <module>infrastructure</module>
        <module>service</module>
    </modules>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
       	<version>2.3.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.atguigu</groupId>
    <artifactId>acl_parent</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>pom</packaging>
    <name>acl_parent</name>
    <description>acl_parent</description>

    <properties>
        <java.version>1.8</java.version>
        <mybatis-plus.version>3.0.5</mybatis-plus.version>
        <velocity.version>2.3</velocity.version>
        <swagger.version>2.9.2</swagger.version>
        <jwt.version>0.9.0</jwt.version>
        <fastjson.version>1.2.83</fastjson.version>
        <gson.version>2.8.4</gson.version>
        <json.version>20170516</json.version>
        <cloud-alibaba.version>0.2.2.RELEASE</cloud-alibaba.version>
        <mysql.version>8.0.28</mysql.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!--Spring Cloud-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${cloud-alibaba.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--mybatis-plus 持久层-->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>${mybatis-plus.version}</version>
            </dependency>

            <!-- velocity 模板引擎, Mybatis Plus 代码生成器需要 -->
            <dependency>
                <groupId>org.apache.velocity</groupId>
                <artifactId>velocity-engine-core</artifactId>
                <version>${velocity.version}</version>
            </dependency>

            <!--swagger-->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger2</artifactId>
                <version>${swagger.version}</version>
            </dependency>
            <!--swagger ui-->
            <dependency>
                <groupId>io.springfox</groupId>
                <artifactId>springfox-swagger-ui</artifactId>
                <version>${swagger.version}</version>
            </dependency>
            <!-- JWT -->
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt</artifactId>
                <version>${jwt.version}</version>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
            </dependency>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

**Common**

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>acl_parent</artifactId>
        <groupId>com.atguigu</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>common</artifactId>
    <packaging>pom</packaging>
    <modules>
        <module>service_base</module>
        <module>spring_security</module>
    </modules>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <scope>provided </scope>
        </dependency>

        <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <scope>provided </scope>
        </dependency>

        <!--lombok用来简化实体类：需要安装lombok插件-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided </scope>
        </dependency>
        <!--swagger-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <scope>provided </scope>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <scope>provided </scope>
        </dependency>
        <!-- redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- spring2.X集成redis所需common-pool2-->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
            <version>2.6.0</version>
        </dependency>
    </dependencies>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

</project>
```

**spring_security**

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>common</artifactId>
        <groupId>com.atguigu</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>spring_security</artifactId>

    <dependencies>
        <dependency>
            <groupId>com.atguigu</groupId>
            <artifactId>service_base</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        <!-- Spring Security依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
        </dependency>
    </dependencies>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

</project>
```

**api_gateway**

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>infrastructure</artifactId>
        <groupId>com.atguigu</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>api_gateway</artifactId>

    <dependencies>
        <dependency>
            <groupId>com.atguigu</groupId>
            <artifactId>service_base</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>

        <!--gson-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
        </dependency>

        <!--服务调用-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

</project>
```

**service**

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>acl_parent</artifactId>
        <groupId>com.atguigu</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>service</artifactId>
    <packaging>pom</packaging>
    <modules>
        <module>service_acl</module>
    </modules>

    <dependencies>

        <dependency>
            <groupId>com.atguigu</groupId>
            <artifactId>service_base</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <!--服务注册-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!--服务调用-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
        </dependency>
        <!--swagger-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
        </dependency>
        <!--mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!-- velocity 模板引擎, Mybatis Plus 代码生成器需要 -->
        <dependency>
            <groupId>org.apache.velocity</groupId>
            <artifactId>velocity-engine-core</artifactId>
        </dependency>


        <!--lombok用来简化实体类：需要安装lombok插件-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!--gson-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
        </dependency>
    </dependencies>

    <build>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
    </build>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

</project>
```

**service_acl**

```XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>service</artifactId>
        <groupId>com.atguigu</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>service_acl</artifactId>

    <dependencies>
        <dependency>
            <groupId>com.atguigu</groupId>
            <artifactId>spring_security</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.83</version>
        </dependency>
    </dependencies>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

</project>
```

### 5、启动redis和Nacos

![image-20230126163559489](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126163559489.png)

### 6、编写common里面需要的工具类

![image-20230126171727242](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126171727242.png)

### 7、编写SpringSecurity认证授权工具类和处理器

![image-20230126175853459](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126175853459.png)

#### 7.1、编写密码处理工具类

```
DefaultPasswordEncoder.java
```

![image-20230126180634574](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126180634574.png)

```JAVA
package com.atguigu.security.security;

import com.atguigu.utils.MD5;
import org.omg.CORBA.WCharSeqHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultPasswordEncoder implements PasswordEncoder {

    public DefaultPasswordEncoder(){
        this(-1);
    }

    public DefaultPasswordEncoder(int strength){

    }

    //进行MD5加密
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5.encrypt(rawPassword.toString());
    }

    //进行密码比对
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5.encrypt(rawPassword.toString()));
    }
}
```

#### 7.2、编写token操作工具类

使用jwt生成token

![image-20230126184203614](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126184203614.png)

```JAVA
package com.atguigu.security.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {

    private long tokenExpiration=24*60*60*1000;

    private String tokenSignKey="123456";

    public String createToken(String username){
        String token = Jwts.builder().setSubject(username).setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();


        return token;
    }

    public String getUserFromToken(String token){
        String user=Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return user;
    }

    public void removeToken(String token){
        //jwttoken 无需删除 客户端扔掉即可
    }
}
```

#### 7.3、编写退出处理器

![image-20230126190615720](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126190615720.png)

```JAVA
package com.atguigu.security.security;

import com.atguigu.utils.R;
import com.atguigu.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager,RedisTemplate redisTemplate){

        this.tokenManager=tokenManager;
        this.redisTemplate=redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token=request.getHeader("token");
        if (token!=null){
            tokenManager.removeToken(token);
            //清空当前用户缓存中的权限数据
            String userName=tokenManager.getUserFromToken(token);
            redisTemplate.delete(userName);
        }
        ResponseUtil.out(response, R.ok());
    }
}
```

#### 7.4、编写未授权统一处理类

![image-20230126191141065](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126191141065.png)

```JAVA

package com.atguigu.security.security;

import com.atguigu.utils.R;
import com.atguigu.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnauthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtil.out(response, R.error());
    }
}
```

### 8、编写实体类

![image-20230126225223381](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126225223381.png)

SecurityUser.java

```JAVA
package com.atguigu.security.entity;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Slf4j
public class SecurityUser implements UserDetails {
    //当前登录用户
    private transient User currentUserInfo;
    //当前权限
    private List<String> permissionValueList; public SecurityUser() {
    }
    public SecurityUser(User user) { if (user != null) {
        this.currentUserInfo = user;
    }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String permissionValue : permissionValueList)
        {
            if(StringUtils.isEmpty(permissionValue)){
                SimpleGrantedAuthority  authority=new SimpleGrantedAuthority(permissionValue);
                authorities.add(authority);
            }

        }
        return authorities;
    }
    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }
    @Override
    public String getUsername() {
        return currentUserInfo.getUsername();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

User.java

```JAVA
package com.atguigu.security.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "用户实体类")
public class User implements Serializable {
    private String username;
    private String password;
    private String nickName;
    private String salt;
    private String token;
}
```

### 9、编写认证和授权的filter

#### 9.1、编写认证的filter

![image-20230126233821374](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126233821374.png)

```JAVA
package com.atguigu.security.filter;

import com.atguigu.security.entity.SecurityUser;
import com.atguigu.security.entity.User;
import com.atguigu.security.security.TokenManager;
import com.atguigu.utils.R;
import com.atguigu.utils.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    public TokenLoginFilter(AuthenticationManager authenticationManager,
                            TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
        this.setPostOnly(false);
        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/admin/acl/login","POST"));
    }
  //1.获取表单提交用户名和密码
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException
      //获取表单提交数据
    {
        try {
        User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>()));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    }
    /**认证成功调用的方法
     * 登录成功
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws
            IOException, ServletException {
      	//认证成功，得到认证成功之后的用户信息
        SecurityUser user = (SecurityUser) auth.getPrincipal();
      	//根据用户名生成token
        String token =tokenManager.createToken(user.getCurrentUserInfo().getUsername());
        //把用户名称和用户权限列表放大redis
      	redisTemplate.opsForValue().set(user.getCurrentUserInfo().getUsername(),
                user.getPermissionValueList());
      	//返回token
        ResponseUtil.out(res, R.ok().data("token", token));
    }
    /**
     * 登录失败
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException e) throws
            IOException, ServletException
    { ResponseUtil.out(response,
            R.error());
    }
}
```

#### 9.2、编写授权的filter

![image-20230126234948489](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230126234948489.png)

```JAVA
package com.atguigu.security.filter;

import com.atguigu.security.security.TokenManager;
import com.atguigu.utils.R;
import com.atguigu.utils.ResponseUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;
    public	TokenAuthenticationFilter(AuthenticationManager authManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        super(authManager); this.tokenManager = tokenManager; this.redisTemplate = redisTemplate;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        logger.info("=================" + req.getRequestURI());
        if (req.getRequestURI().indexOf("admin") == -1) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = null;
        try {
            authentication = getAuthentication(req);
        } catch (Exception e) {
            ResponseUtil.out(res, R.error());
        }
        if (authentication != null) {

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            ResponseUtil.out(res, R.error());
        }
            chain.doFilter(req, res);
        }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
// token 置于header 里
        String token = request.getHeader("token");
        if (token != null && !"".equals(token.trim())) {
            String userName = tokenManager.getUserFromToken(token); List<String> permissionValueList=(List<String>)
                    redisTemplate.opsForValue().get(userName);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for(String permissionValue : permissionValueList) {
                if(StringUtils.isEmpty(permissionValue)) continue;
                SimpleGrantedAuthority authority = new
                        SimpleGrantedAuthority(permissionValue);
                authorities.add(authority);
            }
            if (!StringUtils.isEmpty(userName)) {
                return new UsernamePasswordAuthenticationToken(userName, token,
                        authorities);
            }
            return null;
        }
        return null;
    }
}
```

### 10、编写核心配置类

![image-20230127153613189](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230127153613189.png)

```JAVA
package com.atguigu.security.config;

import com.atguigu.security.filter.TokenAuthenticationFilter;
import com.atguigu.security.filter.TokenLoginFilter;
import com.atguigu.security.security.DefaultPasswordEncoder;
import com.atguigu.security.security.TokenLogoutHandler;
import com.atguigu.security.security.TokenManager;
import com.atguigu.security.security.UnauthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {
    //自定义查询数据库用户名密码和权限信息
    private UserDetailsService userDetailsService; //token 管理工具类(生成 token)
    private TokenManager tokenManager; //密码管理工具类
    private DefaultPasswordEncoder defaultPasswordEncoder; //redis 操作工具类
    private RedisTemplate redisTemplate;

    @Autowired
    public TokenWebSecurityConfig(UserDetailsService userDetailsService, DefaultPasswordEncoder defaultPasswordEncoder,
                                  TokenManager tokenManager, RedisTemplate
                                          redisTemplate) {
        this.userDetailsService = userDetailsService;
        this.defaultPasswordEncoder = defaultPasswordEncoder; this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }
    /**
     * 配置设置 */
//设置退出的地址和 token，redis 操作地址
    @Override
    protected void configure(HttpSecurity http) throws Exception {http.exceptionHandling()
            .authenticationEntryPoint(new UnauthEntryPoint())//没有权限
            .and().csrf().disable()
            .authorizeRequests()
            .anyRequest().authenticated()
            .and().logout().logoutUrl("/admin/acl/index/logout")//退出路径
            .addLogoutHandler(new TokenLogoutHandler(tokenManager,redisTemplate)).and()
            .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisTemplate))
            .addFilter(new TokenAuthenticationFilter(authenticationManager(),tokenManager, redisTemplate)).httpBasic();
    }

    /**
     * 密码处理
     * @param auth
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     } }
     */
    @Override
    public void configure(WebSecurity web) throws Exception
    { web.ignoring().antMatchers("/api/**" , "/swagger-ui.html/**");
    }
}
```

### 11、编写userDetailService

导入service_acl模块

![image-20230128120636196](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230128120636196.png)

编写userDetailService

![image-20230128120709977](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230128120709977.png)

```JAVA
package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.User;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.UserService;
import com.atguigu.security.entity.SecurityUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.selectByUsername(username);

        if (user==null){
            throw new UsernameNotFoundException("用户不存在");
        }

        com.atguigu.security.entity.User curUser = new com.atguigu.security.entity.User();
        BeanUtils.copyProperties(user,curUser);

        //根据用户查询用户权限列表
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser=new SecurityUser();
        securityUser.setCurrentUserInfo(curUser);
        securityUser.setPermissionValueList(permissionValueList);
        return securityUser;
    }
}
```

### 12、编写网关部分

编写跨域配置类

![image-20230128160736922](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230128160736922.png)

```JAVA

package com.atguigu.gateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * description:
 *
 * @author wangpeng
 * @date 2019/01/02
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
```

编写启动类

![image-20230128160801112](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230128160801112.png)

```JAVA

package com.atguigu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

编写网关配置类

![image-20230128160903867](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230128160903867.png)

```PROPERTIES

# 服务端口
server.port=8222
# 服务名
spring.application.name=service-gateway
# nacos服务地址
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848

#使用服务发现路由
spring.cloud.gateway.discovery.locator.enabled=true

#设置路由id
spring.cloud.gateway.routes[0].id=service-acl
#设置路由的uri   lb://nacos注册服务名称
spring.cloud.gateway.routes[0].uri=lb://service-acl
#设置路由断言,代理servicerId为auth-service的/auth/路径
spring.cloud.gateway.routes[0].predicates= Path=/*/acl/**
```

### 13、测试

13.1、启动后端项目

![image-20230128175316694](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230128175316694.png)

![image-20230128231803932](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230128231803932.png)

# 5.SpringSecurity源码

## 5.1 SpringSecurity 的过滤器介绍

SpringSecurity 采用的是责任链的设计模式，它有一条很长的过滤器链。现在对这条过滤 器链的 15 个过滤器进行说明:

- （1） WebAsyncManagerIntegrationFilter：将 Security 上下文与 Spring Web 中用于 处理异步请求映射的 WebAsyncManager 进行集成。
- （2） SecurityContextPersistenceFilter：在每次请求处理之前将该请求相关的安全上 下文信息加载到 SecurityContextHolder 中，然后在该次请求处理完成之后，将 SecurityContextHolder 中关于这次请求的信息存储到一个“仓储”中，然后将 SecurityContextHolder 中的信息清除，例如在 Session 中维护一个用户的安全信 息就是这个过滤器处理的。
- （3） HeaderWriterFilter：用于将头信息加入响应中。
- （4） CsrfFilter：用于处理跨站请求伪造。
- （5）LogoutFilter：用于处理退出登录。
- （6）UsernamePasswordAuthenticationFilter：用于处理基于表单的登录请求，从表单中 获取用户名和密码。默认情况下处理来自 /login 的请求。从表单中获取用户名和密码 时，默认使用的表单 name 值为 username 和 password，这两个值可以通过设置这个 过滤器的 usernameParameter 和 passwordParameter 两个参数的值进行修改。
- （7）DefaultLoginPageGeneratingFilter：如果没有配置登录页面，那系统初始化时就会 配置这个过滤器，并且用于在需要进行登录时生成一个登录表单页面。
- （8）BasicAuthenticationFilter：检测和处理 http basic 认证。
- （9）RequestCacheAwareFilter：用来处理请求的缓存。
- （10）SecurityContextHolderAwareRequestFilter：主要是包装请求对象 request。
- （11）AnonymousAuthenticationFilter：检测 SecurityContextHolder 中是否存在 Authentication 对象，如果不存在为其提供一个匿名 Authentication。
- （12）SessionManagementFilter：管理 session 的过滤器
- （13）ExceptionTranslationFilter：处理 AccessDeniedException 和 AuthenticationException 异常。
- （14）FilterSecurityInterceptor：可以看做过滤器链的出口。
- （15）RememberMeAuthenticationFilter：当用户没有登录而直接访问资源时, 从 cookie 里找出用户的信息, 如果 Spring Security 能够识别出用户提供的 remember me cookie, 用户将不必填写用户名和密码, 而是直接登录进入系统，该过滤器默认不开启。

## 5.2 SpringSecurity 基本流程

Spring Security 采取过滤链实现认证与授权，只有当前过滤器通过，才能进入下一个 过滤器

![image-20230129004433899](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129004433899.png)

绿色部分是认证过滤器，需要我们自己配置，可以配置多个认证过滤器。认证过滤器可以 使用 Spring Security 提供的认证过滤器，也可以自定义过滤器（例如：短信验证）。认 证过滤器要在 configure(HttpSecurity http)方法中配置，没有配置不生效。下面会重 点介绍以下三个过滤器：

UsernamePasswordAuthenticationFilter 过滤器：该过滤器会拦截前端提交的 POST 方式 的登录表单请求，并进行身份认证。

ExceptionTranslationFilter 过滤器：该过滤器不需要我们配置，对于前端提交的请求会 直接放行，捕获后续抛出的异常并进行处理（例如：权限访问限制）。 FilterSecurityInterceptor 过滤器：该过滤器是过滤器链的最后一个过滤器，根据资源 权限配置来判断当前请求是否有权限访问对应的资源。如果访问受限会抛出相关异常，并 由 ExceptionTranslationFilter 过滤器进行捕获和处理。

## 5.3 SpringSecurity 认证流程

认证流程是在 UsernamePasswordAuthenticationFilter 过滤器中处理的，具体流程如下 所示：

![image-20230129004536165](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129004536165.png)

### 5.3.1 UsernamePasswordAuthenticationFilter 源码

当前端提交的是一个 POST 方式的登录表单请求，就会被该过滤器拦截，并进行身份认 证。该过滤器的 doFilter() 方法实现在其抽象父类

`AbstractAuthenticationProcessingFilter `中，查看相关源码：

![image-20230129004714222](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129004714222.png)

![image-20230129004845970](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129004845970.png)

![image-20230129004933128](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129004933128.png)

![image-20230129005030188](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005030188.png)

![image-20230129005058686](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005058686.png)

上述的 第二 过程调用了 UsernamePasswordAuthenticationFilter 的 attemptAuthentication() 方法，源码如下

![image-20230129005241485](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005241485.png)

![image-20230129005303010](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005303010.png)

![image-20230129005322808](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005322808.png)

上述的（3）过程创建的 UsernamePasswordAuthenticationToken 是 Authentication 接口的实现类，该类有两个构造器，一个用于封装前端请求传入的未认 证的用户信息，一个用于封装认证成功后的用户信息

![image-20230129005504200](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005504200.png)

Authentication 接口的实现类用于存储用户认证信息，查看该接口具体定义：

![image-20230129005548616](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005548616.png)

### 5.3.2 ProviderManager 源码

上述过程中，UsernamePasswordAuthenticationFilter 过滤器的 attemptAuthentication() 方法的（5）过程将未认证的 Authentication 对象传入 ProviderManager 类的 authenticate() 方法进行身份认证

ProviderManager 是 AuthenticationManager 接口的实现类，该接口是认证相关的核心接 口，也是认证的入口。在实际开发中，我们可能有多种不同的认证方式，例如：用户名+ 密码、邮箱+密码、手机号+验证码等，而这些认证方式的入口始终只有一个，那就是 AuthenticationManager。在该接口的常用实现类 ProviderManager 内部会维护一个 List列表，存放多种认证方式，实际上这是委托者模式 （Delegate）的应用。每种认证方式对应着一个 AuthenticationProvider， AuthenticationManager 根据认证方式的不同（根据传入的 Authentication 类型判断）委托 对应的 AuthenticationProvider 进行用户认证。

![image-20230129005659606](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005659606.png)

![image-20230129005711969](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005711969.png)

![image-20230129005731523](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005731523.png)

![image-20230129005751019](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005751019.png)

上述认证成功之后的（6）过程，调用 CredentialsContainer 接口定义的 eraseCredentials() 方法去除敏感信息。查看 UsernamePasswordAuthenticationToken 实现的 eraseCredentials() 方法，该方 法实现在其父类中

![image-20230129005819121](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005819121.png)

### 5.3.3 认证成功/失败处理

上述过程就是认证流程的最核心部分，接下来重新回到 UsernamePasswordAuthenticationFilter 过滤器的 doFilter() 方法，查看认证成 功/失败的处理

![image-20230129005913611](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005913611.png)

![image-20230129005951550](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129005951550.png)

![image-20230129010014754](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010014754.png)

## 5.4 SpringSecurity 权限访问流程

上一个部分通过源码的方式介绍了认证流程，下面介绍权限访问流程，主要是对 ExceptionTranslationFilter 过滤器和 FilterSecurityInterceptor 过滤器进行介绍。

### 5.4.1 ExceptionTranslationFilter 过滤器

该过滤器是用于处理异常的，不需要我们配置，对于前端提交的请求会直接放行，捕获后 续抛出的异常并进行处理（例如：权限访问限制）。具体源码如下：

![image-20230129010142040](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010142040.png)

### 5.4.2 FilterSecurityInterceptor 过滤器

FilterSecurityInterceptor 是过滤器链的最后一个过滤器，该过滤器是过滤器链 的最后一个过滤器，根据资源权限配置来判断当前请求是否有权限访问对应的资源。如果 访问受限会抛出相关异常，最终所抛出的异常会由前一个过滤器 ExceptionTranslationFilter 进行捕获和处理。具体源码如下

![image-20230129010224853](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010224853.png)

![image-20230129010237662](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010237662.png)

需要注意，Spring Security 的过滤器链是配置在 SpringMVC 的核心组件 DispatcherServlet 运行之前。也就是说，请求通过 Spring Security 的所有过滤器， 不意味着能够正常访问资源，该请求还需要通过 SpringMVC 的拦截器链。

## 5.5 SpringSecurity 请求间共享认证信息

一般认证成功后的用户信息是通过 Session 在多个请求之间共享，那么 Spring Security 中是如何实现将已认证的用户信息对象 Authentication 与 Session 绑定的进行 具体分析

![image-20230129010351662](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010351662.png)

在前面讲解认证成功的处理方法 successfulAuthentication() 时，有以下代码：

![image-20230129010414796](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010414796.png)

查 看 SecurityContext 接 口 及 其 实 现 类 SecurityContextImpl ， 该 类 其 实 就 是 对 Authentication 的封装：

查 看 SecurityContextHolder 类 ， 该 类 其 实 是 对 ThreadLocal 的 封 装 ， 存 储 SecurityContext 对象

![image-20230129010510710](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010510710.png)

![image-20230129010525525](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010525525.png)

![image-20230129010538734](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010538734.png)

![image-20230129010551278](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010551278.png)

### 5.5.1 SecurityContextPersistenceFilter 过滤器

前面提到过，在 UsernamePasswordAuthenticationFilter 过滤器认证成功之 后，会在认证成功的处理方法中将已认证的用户信息对象 Authentication 封装进 SecurityContext，并存入 SecurityContextHolder。 之后，响应会通过 SecurityContextPersistenceFilter 过滤器，该过滤器的位置 在所有过滤器的最前面，请求到来先进它，响应返回最后一个通过它，所以在该过滤器中 处理已认证的用户信息对象 Authentication 与 Session 绑定

认证成功的响应通过 SecurityContextPersistenceFilter 过滤器时，会从 SecurityContextHolder 中取出封装了已认证用户信息对象 Authentication 的 SecurityContext，放进 Session 中。当请求再次到来时，请求首先经过该过滤器，该过滤 器会判断当前请求的 Session 是否存有 SecurityContext 对象，如果有则将该对象取出再次 放入 SecurityContextHolder 中，之后该请求所在的线程获得认证用户信息，后续的资源访 问不需要进行身份认证；当响应再次返回时，该过滤器同样从 SecurityContextHolder 取出 SecurityContext 对象，放入 Session 中。具体源码如下

![image-20230129010632805](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010632805.png)

![image-20230129010644817](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010644817.png)

![image-20230129010654837](%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Security/image-20230129010654837.png)