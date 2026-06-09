---
title: Spring Security + OAuth2 
date: 2023-02-21 08:56:42
tags:
- Spring Security + OAuth2 
categories:
- 后端框架
---

大佬文章：[江南一点雨 (javaboy.org)](http://www.javaboy.org/springsecurity/)

# [Spring Security](https://blog.csdn.net/weixin_42375707/article/details/110678638)

## 原理

>  springsecurity最重要的两个部分: **authentication(认证) 和 authorization(授权)**
>
>  **认证**: 就是判定你是什么身份，管理员还是普通人
>
>  **授权**: 什么样的身份拥有什么样的权利。

![Spring Security的执行流程](Spring%20Security%20+%20OAuth2/Spring%20Security%E7%9A%84%E6%89%A7%E8%A1%8C%E6%B5%81%E7%A8%8B.png)



<img src="Spring%20Security%20+%20OAuth2/20201205150732178.png" alt="img" style="zoom: 60%;" />

## 注意点

> 1. 访问/login时必须要用post方法！, 访问的参数名必须为username和password  
>
> 2. 访问/logout时即可用post也可用get方法
>
> 3. 角色授权时，不能带前缀ROLE_
>
>    ```java
>    //数据库中角色为用户角色为ROLE_USER
>    
>    //这里不可以带
>    .antMatchers("/index").hasRole("USER")
>           .antMatchers("/hello").hasRole("ADMIN")
>           
>           
>    //这里可以带       
>    @PreAuthorize("hasRole('ROLE_USER')")  
>    public void index(){}
>    ```
>    
>    Spring Security 的 issue 上也看到了一个类似的问题：https://github.com/spring-projects/spring-security/issues/4912
>    
>    从作者对这个问题的回复中，也能看到一些端倪：
>    
>    1. 作者承认了目前加 `ROLE_` 前缀的方式一定程度上给开发者带来了困惑，但这是一个历史积累问题。
>    2. 作者说如果不喜欢 `ROLE_`，那么可以直接使用 `hasAuthority` 代替 `hasRole`，言下之意，就是这两个功能是一样的。
>    3. 作者还说了一些关于权限问题的看法，权限是典型的对对象的控制，但是 Spring Security 开发者不能向 Spring Security 用户添加所有权限，因为在大多数系统中，权限都过于复杂庞大而无法完全包含在内存中。当然，如果开发者有需要，可以自定义类继承自 GrantedAuthority 以扩展其功能。
>    
>    从作者的回复中我们也可以看出来，`hasAuthority` 和 `hasRole` 功能上没什么区别，设计层面上确实是两个不同的东西。
>    
>    历史沿革
>    
>    实际上，在 Spring Security4 之前，`hasAuthority` 和 `hasRole` 几乎是一模一样的，连 `ROLE_` 区别都没有！
>    
>    即 `hasRole("admin")` 和 `hasAuthority("admin")` 是一样的。
>    
>    而在 Spring Security4 之后，才有了前缀 `ROLE_` 的区别。

## 数据库

> 密码123

```sql
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(30) NOT NULL,
  `password` varchar(60) NOT NULL,
  `role` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'answer', '$2a$10$BL1OOSFGJD0eMsR/Q.pCKeTs59pm7r6M0/Z7/8Dh.fr8aKQ83f1SC', 'ROLE_USER');
INSERT INTO `user` VALUES ('2', 'wenxin', '$2a$10$BL1OOSFGJD0eMsR/Q.pCKeTs59pm7r6M0/Z7/8Dh.fr8aKQ83f1SC', 'ROLE_ADMIN');

```

## 编码

### POM

```xml
 <dependencies>
        <!--转换成json字符串的工具-->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.2</version>
        </dependency>
        <!--springboot集成web操作7-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--springsecurity-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <!--mysql驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!--lombok依赖-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!--mybatis-plus依赖-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.1</version>
        </dependency>
      
        <!--springboot-自带测试工具-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-config</artifactId>
            <version>5.3.4.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-web</artifactId>
            <version>5.3.4.RELEASE</version>
        </dependency>
    </dependencies>
```

### 自定义结果集

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Msg {
    int code;   //错误码
    String Message; //消息提示
    Map<String,Object> data=new HashMap<String,Object>();   //数据
 
    //无权访问
    public static Msg denyAccess(String message){
        Msg result=new Msg();
        result.setCode(300);
        result.setMessage(message);
        return result;
    }
 
    //操作成功
    public static Msg success(String message){
        Msg result=new Msg();
        result.setCode(200);
        result.setMessage(message);
        return result;
    }
 
    //客户端操作失败
    public static Msg fail(String message){
        Msg result=new Msg();
        result.setCode(400);
        result.setMessage(message);
        return result;
    }
 
    public Msg add(String key,Object value){
        this.data.put(key,value);
        return this;
    }
}
```

### user实体类

```java

@Data
public class User implements Serializable {

    private Integer id;

    private String account;

    private String password;

    private String role;

}
```

### Mapper

> mapper接口，这里使用的mybatis plus

```java
package com.lz.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lz.security.entriy.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
 
}
```

### Service

> **service接口继承UserDetailsService接口**

```java

public interface UserService extends UserDetailsService {

}
```

### 实现类

> 实现类实现了UserService接口，因为UserService接口继承UserDetailsService接口，实现类就实现了UserDetailsService接口
>
> **这是实现自定义用户认证的核心逻辑，`loadUserByUsername(String username)`的参数就是登录时提交的用户名，返回类型是一个叫UserDetails 的接口，需要在这里构造出他的一个实现类User，这是Spring security提供的用户信息实体。**

```java

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lz
 * @since 2023-02-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService,UserDetailsService {

    @Autowired
    UserMapper userMapper;

    //加载用户
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        /*根据用户名，查询用户*/
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.eq("account",userName);
        User user=userMapper.selectOne(wrapper);    //user即为查询结果
        if(user==null){
            throw new UsernameNotFoundException("用户名错误！！");
        }

        //获取用户权限，并把其添加到GrantedAuthority中
        List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
        // 获取用户角色
        List<String> roles = rolePermissionDao.getRuleByUserId(user.getId());
        // 获取用户权限(url)
        List<String> per = rolePermissionDao.getPermissByUserId(user.getId());
        for (String role : roles) {
            per.add(role);
        }
        for (String sp : per) {
            GrantedAuthority e = new SimpleGrantedAuthority(sp);
            grantedAuthorities.add(e);
        }


        //方法的返回值要求返回UserDetails这个数据类型，  UserDetails是接口，找它的实现类就好了
        //new org.springframework.security.core.userdetails.User(String username,String password,Collection<? extends GrantedAuthority> authorities) 就是它的实现类
        return new User(userName, user.getPassword(), grantedAuthorities);

    }
}

```

#### 明文/密文

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

#### **`{bcrypt}` + `userEntity.getPassword()` 的作用**

1. **`{bcrypt}`**：

   - 这是一个前缀，用于告诉 Spring Security 密码的加密方式是 **BCrypt**。
   - Spring Security 支持多种加密方式（如 `{noop}` 表示明文，`{pbkdf2}` 表示 PBKDF2 加密等），`{bcrypt}` 是其中一种标识。

2. **`userEntity.getPassword()`**：

   - 这是从数据库中获取的加密后的密码（密文）。

3. **组合起来**：

   `{bcrypt}`+`userEntity.getPassword()`的完整格式是：

   ```
    {bcrypt}$2a$10$9S8u5WZ8z3z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2
   ```

   这种格式明确告诉 Spring Security：密码是使用 BCrypt 加密的，并且密文是 `$2a$10$9S8u5WZ8z3z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2`。

#### **登录时的密码验证流程**

1. **用户输入密码**：

   - 用户在登录表单中输入明文密码，例如 `123456`。

2. **从数据库加载密码**：

   - Spring Security 从数据库中加载用户的密码，例如：

     ```
      {bcrypt}$2a$10$9S8u5WZ8z3z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2z2
     ```

3. **解析加密方式**：

   - Spring Security 根据前缀 `{bcrypt}` 知道密码是使用 BCrypt 加密的。

4. **加密用户输入的明文密码**：

   - Spring Security 使用 BCrypt 对用户输入的明文密码（如 `123456`）进行加密，生成一个新的密文。

5. **对比密文**：

   - Spring Security 将新生成的密文与数据库中存储的密文进行比对。
   - 如果两者一致，则验证通过；否则验证失败。

### controller

```java
@RestController
public class UserController {

    @GetMapping("index")
    // hasRole 方法会自动添加 ROLE_ 前缀。
    @PreAuthorize("hasRole('ADMIN')") // 实际检查的是 `ROLE_ADMIN`
    @PreAuthorize("hasAuthority('ADMIN')") // 直接匹配 `ADMIN`
    public String index(){
        return "index";
    }

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }
}

```

<table id="common-expressions" class="tableblock frame-all grid-all stretch">
<table id="common-expressions" class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 50%;">
<col style="width: 50%;">
</colgroup>
<thead>
<tr>
<th class="tableblock halign-left valign-top" _msttexthash="7004712" _msthash="2223">表达</th>
<th class="tableblock halign-left valign-top" _msttexthash="6157333" _msthash="2224">描述</th>
</tr>
</thead>
<tbody>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>hasRole(String role)</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="101552867" _msthash="2225">如果当前主体具有指定的角色，则返回。</font><code>true</code></p>
<p class="tableblock"><font _mstmutation="1" _msttexthash="4236089" _msthash="2226">例如</font><code>hasRole('admin')</code></p>
<p class="tableblock"><font _mstmutation="1" _msttexthash="334673300" _msthash="2227">默认情况下，如果提供的角色不以“ROLE_”开头，则会添加该角色。
这可以通过修改 on .</font><code>defaultRolePrefix</code><code>DefaultWebSecurityExpressionHandler</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>hasAnyRole(String…&ZeroWidthSpace; roles)</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="359017776" _msthash="2228">如果当前主体具有任何提供的角色（以逗号分隔的字符串列表形式给出），则返回。</font><code>true</code></p>
<p class="tableblock"><font _mstmutation="1" _msttexthash="4236089" _msthash="2229">例如</font><code>hasAnyRole('admin', 'user')</code></p>
<p class="tableblock"><font _mstmutation="1" _msttexthash="334673300" _msthash="2230">默认情况下，如果提供的角色不以“ROLE_”开头，则会添加该角色。
这可以通过修改 on .</font><code>defaultRolePrefix</code><code>DefaultWebSecurityExpressionHandler</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>hasAuthority(String authority)</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="100738911" _msthash="2231">如果当前主体具有指定的权限，则返回。</font><code>true</code></p>
<p class="tableblock"><font _mstmutation="1" _msttexthash="4236089" _msthash="2232">例如</font><code>hasAuthority('read')</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>hasAnyAuthority(String…&ZeroWidthSpace; authorities)</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="351235924" _msthash="2233">如果当前主体具有任何提供的权限（以逗号分隔的字符串列表形式给出），则返回</font><code>true</code></p>
<p class="tableblock"><font _mstmutation="1" _msttexthash="4236089" _msthash="2234">例如</font><code>hasAnyAuthority('read', 'write')</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>principal</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock" _msttexthash="92521377" _msthash="2235">允许直接访问表示当前用户的主体对象</p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>authentication</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="27004640" _msthash="2236">允许直接访问从</font><code>Authentication</code><code>SecurityContext</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>permitAll</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="16626194" _msthash="2237">始终计算为</font><code>true</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>denyAll</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="16626194" _msthash="2238">始终计算为</font><code>false</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>isAnonymous()</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="76873472" _msthash="2239">如果当前主体是匿名用户，则返回</font><code>true</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>isRememberMe()</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="96782816" _msthash="2240">如果当前主体是“记住我”用户，则返回</font><code>true</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>isAuthenticated()</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="64327523" _msthash="2241">如果用户不是匿名的，则返回</font><code>true</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>isFullyAuthenticated()</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="123555809" _msthash="2242">如果用户不是匿名用户或记住我用户，则返回</font><code>true</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>hasPermission(Object target, Object permission)</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="163515274" _msthash="2243">如果用户有权访问给定权限的提供目标，则返回。
例如</font><code>true</code><code>hasPermission(domainObject, 'read')</code></p></td>
</tr>
<tr>
<td class="tableblock halign-left valign-top"><p class="tableblock"><code>hasPermission(Object targetId, String targetType, Object permission)</code></p></td>
<td class="tableblock halign-left valign-top"><p class="tableblock"><font _mstmutation="1" _msttexthash="163515274" _msthash="2244">如果用户有权访问给定权限的提供目标，则返回。
例如</font><code>true</code><code>hasPermission(1, 'com.example.domain.Message', 'read')</code></p></td>
</tr>
</tbody>
</table>

**使用 `hasAuthority` 更具有一致性，你不用考虑要不要加 `ROLE_` 前缀，数据库什么样这里就是什么样！而 `hasRole` 则不同，代码里如果写的是 `admin`，框架会自动加上 `ROLE_` 前缀，所以数据库就必须是 `ROLE_admin`。**

### 跨域

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
      // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}

```

### SpringsecurityConfig

> **WebSecurityConfigurerAdapter 管理着Spring Security的整个配置体系，主要包括用户身份认证的管理、全局安全过滤管理和URL访问权限控制的管理。**

```java
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的
public class SpringsecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    AuthenticationEnryPoint authenticationEnryPoint;    //未登录
    @Autowired
    AuthenticationSuccess authenticationSuccess;    //登录成功
    @Autowired
    AuthenticationFailure authenticationFailure;    //登录失败
    @Autowired
    AuthenticationLogout authenticationLogout;      //注销
    @Autowired
    AccessDeny accessDeny;      //无权访问
    @Autowired
    SessionInformationExpiredStrategy sessionInformationExpiredStrategy;    //检测异地登录
    @Autowired
    SelfAuthenticationProvider selfAuthenticationProvider;      //自定义登录认证逻辑处理

    //加密方式
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    void configure(WebSecurity web) {
        web.ignoring().antMatchers("/js/**", "/css/**");
    }

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(selfAuthenticationProvider); //自定义登录认证逻辑处理，也可以不用自己逻辑，可以用下面的
		// auth.userDetailsService(userServiceImpl).passwordEncoder(bCryptPasswordEncoder());//注入实现UserDetailsService接口的类
    }

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //cors()解决跨域问题，csrf()会与restful风格冲突，默认springsecurity是开启的，所以要disable()关闭一下
        http.csrf().disable();
        //允许跨域
        http.cros();

        http.authorizeRequests()
//                .antMatchers("/index").hasRole("USER")  //可以在这里设置访问此路径需要的权限，也可以在方法上添加注解
//                .antMatchers("/hello").hasRole("ADMIN")
                .anyRequest().authenticated()  //任何请求都需要验证

                .and()
                .formLogin()  //开启登录
                .permitAll()  //允许所有人访问
                .successHandler(authenticationSuccess) // 登录成功逻辑处理
                .failureHandler(authenticationFailure) // 登录失败逻辑处理

                .and()
                .logout()   //开启注销
                .permitAll()    //允许所有人访问
                .logoutSuccessHandler(authenticationLogout) //注销逻辑处理
                .deleteCookies("JSESSIONID")    //删除cookie

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeny)    //权限不足的时候的逻辑处理
                .authenticationEntryPoint(authenticationEnryPoint)  //未登录是的逻辑处理

                .and()
                .sessionManagement()
                .maximumSessions(1)     //最多只能一个用户登录一个账号
                .expiredSessionStrategy(sessionInformationExpiredStrategy);  //异地登录的逻辑处理

    }
}

```

#### 坑

```
http.authorizeRequests()
        .antMatchers("/admin/**").hasAuthority("admin")
        .antMatchers("/user/**").hasAuthority("user")
        .anyRequest().authenticated()

http.authorizeRequests()
        .antMatchers("/admin/**").hasRole("admin")
        .antMatchers("/user/**").hasRole("user")
        .anyRequest().authenticated()
```

hasRole 的处理逻辑和 hasAuthority 似乎一模一样，不同的是，**hasRole 这里会自动给传入的字符串加上 `ROLE_` 前缀，所以在数据库中的权限字符串需要加上 `ROLE_` 前缀。即数据库中存储的用户角色如果是 `ROLE_admin`，这里就是 admin。**

**我们在调用 `hasAuthority` 方法时，如果数据是从数据库中查询出来的，这里的权限和数据库中保存一致即可，可以不加 `ROLE_` 前缀。即数据库中存储的用户角色如果是 admin，这里就是 admin。**

**也就是说，使用 `hasAuthority` 更具有一致性，你不用考虑要不要加 `ROLE_` 前缀，数据库什么样这里就是什么样！而 `hasRole` 则不同，代码里如果写的是 `admin`，框架会自动加上 `ROLE_` 前缀，所以数据库就必须是 `ROLE_admin`。**

看起来 hasAuthority 和 hasRole 的区别似乎仅仅在于有没有 `ROLE_` 前缀。

##### 官方回复

Spring Security 的 issue 上也看到了一个类似的问题：https://github.com/spring-projects/spring-security/issues/4912

从作者对这个问题的回复中，也能看到一些端倪：

1. 作者承认了目前加 `ROLE_` 前缀的方式一定程度上给开发者带来了困惑，但这是一个历史积累问题。
2. 作者说如果不喜欢 `ROLE_`，那么可以直接使用 `hasAuthority` 代替 `hasRole`，言下之意，就是这两个功能是一样的。
3. 作者还说了一些关于权限问题的看法，权限是典型的对对象的控制，但是 Spring Security 开发者不能向 Spring Security 用户添加所有权限，因为在大多数系统中，权限都过于复杂庞大而无法完全包含在内存中。当然，如果开发者有需要，可以自定义类继承自 GrantedAuthority 以扩展其功能。

从作者的回复中我们也可以看出来，`hasAuthority` 和 `hasRole` 功能上没什么区别，设计层面上确实是两个不同的东西。

##### 历史沿革

实际上，在 Spring Security4 之前，`hasAuthority` 和 `hasRole` 几乎是一模一样的，连 `ROLE_` 区别都没有！

即 `hasRole("admin")` 和 `hasAuthority("admin")` 是一样的。

而在 Spring Security4 之后，才有了前缀 `ROLE_` 的区别。

### 处理逻辑

#### 自定义认证逻辑处理

> 这部分也可以不用写，写了之后就是用的我们的逻辑了

```java
package com.org.Handler;

import com.org.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SelfAuthenticationProvider implements AuthenticationProvider{
    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account= authentication.getName();     //获取用户名
        String password= (String) authentication.getCredentials();  //获取密码
        UserDetails userDetails= userServiceImpl.loadUserByUsername(account);
        boolean checkPassword= bCryptPasswordEncoder.matches(password,userDetails.getPassword());
        if(!checkPassword){
            throw new BadCredentialsException("密码不正确，请重新登录!");
        }
        return new UsernamePasswordAuthenticationToken(account,password,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}

```

#### 登录成功逻辑处理

```java
package com.org.Handler;

import com.google.gson.Gson;
import com.org.Message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccess implements AuthenticationSuccessHandler{
    @Autowired
    Gson gson;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //登录成功时返回给前端的数据
        Msg result=Msg.success("登录成功！！！！！");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(result));
    }
}

```

#### 登录失败逻辑处理

```Java
package com.org.Handler;

import com.google.gson.Gson;
import com.org.Message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//登录失败返回给前端消息
@Component
public class AuthenticationFailure implements AuthenticationFailureHandler{
    @Autowired
    Gson gson;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Msg msg=null;
        if(e instanceof UsernameNotFoundException){
            msg=Msg.fail(e.getMessage());
        }else if(e instanceof BadCredentialsException){
            msg=Msg.fail("密码错误!!");
        }else {
            msg=Msg.fail(e.getMessage());
        }
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //返回给前台
        response.getWriter().write(gson.toJson(msg));
    }
}

```

#### 注销逻辑处理

```java
package com.org.Handler;

import com.google.gson.Gson;
import com.org.Message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationLogout implements LogoutSuccessHandler{
    @Autowired
    Gson gson;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Msg result=Msg.success("注销成功");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(result));
    }
}

```

#### 权限不足逻辑处理

```java
package com.org.Handler;

import com.google.gson.Gson;
import com.org.Message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//无权访问
@Component
public class AccessDeny implements AccessDeniedHandler{
    @Autowired
    Gson gson;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        Msg result= Msg.denyAccess("无权访问，need Authorities!!");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(result));
    }
}

```



#### 未登录逻辑处理

```java
package com.org.Handler;

import com.google.gson.Gson;
import com.org.Message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEnryPoint implements AuthenticationEntryPoint {

    @Autowired
    Gson gson;

    //未登录时返回给前端数据
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Msg result=Msg.fail("需要登录!!");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(result));
    }
}

```

#### 异地登录逻辑处理

```java
package com.org.Handler;

import com.google.gson.Gson;
import com.org.Message.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//异地登录、账号下线
@Component
public class SessionInformationExpiredStrategy implements org.springframework.security.web.session.SessionInformationExpiredStrategy{
    @Autowired
    Gson gson;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        Msg result= Msg.fail("您的账号在异地登录，建议修改密码");
        HttpServletResponse response=event.getResponse();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(result));
    }
}

```

#### 记住我

##### 配置类

```java

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的
public class SpringsecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcTokenRepositoryImpl rememberMe() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);//注入数据源
        return repository;
    }

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl);
    }


    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	......
    http.rememberMe().tokenRepository(rememberMe()).userDetailsService(userServiceImpl);
    }
}

```

##### 登录

登录发送post请求，设置remember-me状态，属性名默认为remember-me

<img src="Spring%20Security%20+%20OAuth2/image-20231108140642944.png" alt="image-20231108140642944" style="zoom:67%;" />

登录成功之后返回cookie，将每次请求都携带cookie就可以实现“记住我”

<img src="Spring%20Security%20+%20OAuth2/image-20231108141358852.png" alt="image-20231108141358852" style="zoom: 67%;" />



<img src="Spring%20Security%20+%20OAuth2/image-20231108141605886.png" alt="image-20231108141605886" style="zoom: 67%;" />

### CSRF

#### CSRF原理

想要防御 CSRF 攻击，那我们得先搞清楚什么是 CSRF 攻击，松哥通过下面一张图，来和大家梳理 CSRF 攻击流程：

[![img](Spring%20Security%20+%20OAuth2/csrf-1.png)](http://img.itboyhub.com/2020/05/csrf-1.png)

其实这个流程很简单：

1. 假设用户打开了招商银行网上银行网站，并且登录。
2. 登录成功后，网上银行会返回 Cookie 给前端，浏览器将 Cookie 保存下来。
3. 用户在没有登出网上银行的情况下，在浏览器里边打开了一个新的选项卡，然后又去访问了一个危险网站。
4. 这个危险网站上有一个超链接，超链接的地址指向了招商银行网上银行。
5. 用户点击了这个超链接，由于这个超链接会自动携带上浏览器中保存的 Cookie，所以用户不知不觉中就访问了网上银行，进而可能给自己造成了损失。

CSRF 的流程大致就是这样，接下来松哥用一个简单的例子和小伙伴们展示一下 CSRF 到底是怎么回事。

#### CSRF实践

接下来，我创建一个名为 csrf-1 的 Spring Boot 项目，这个项目相当于我们上面所说的网上银行网站，创建项目时引入 Web 和 Spring Security 依赖，如下：

[![img](Spring%20Security%20+%20OAuth2/20200516201607.png)](http://img.itboyhub.com/2020/05/20200516201607.png)

创建成功后，方便起见，我们直接将 Spring Security 用户名/密码 配置在 application.properties 文件中：

```
spring.security.user.name=javaboy
spring.security.user.password=123
```

然后我们提供两个测试接口：

```
@RestController
public class HelloController {
    @PostMapping("/transfer")
    public void transferMoney(String name, Integer money) {
        System.out.println("name = " + name);
        System.out.println("money = " + money);
    }
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
```

假设 `/transfer` 是一个转账接口（这里是假设，主要是给大家演示 CSRF 攻击，真实的转账接口比这复杂）。

最后我们还需要配置一下 Spring Security，因为 Spring Security 中默认是可以自动防御 CSRF 攻击的，所以我们要把这个关闭掉：

```
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .csrf()
                .disable();
    }
}
```

配置完成后，我们启动 csrf-1 项目。

接下来，我们再创建一个 csrf-2 项目，这个项目相当于是一个危险网站，为了方便，这里创建时我们只需要引入 web 依赖即可。

项目创建成功后，首先修改项目端口：

```
server.port=8081
```

然后我们在 resources/static 目录下创建一个 hello.html ，内容如下：

```
<body>
<form action="http://localhost:8080/transfer" method="post">
    <input type="hidden" value="javaboy" name="name">
    <input type="hidden" value="10000" name="money">
    <input type="submit" value="点击查看美女图片">
</form>
</body>
```

这里有一个超链接，超链接的文本是**点击查看美女图片**，当你点击了超链接之后，会自动请求 `http://localhost:8080/transfer` 接口，同时隐藏域还携带了两个参数。

配置完成后，就可以启动 csrf-2 项目了。

接下来，用户首先访问 csrf-1 项目中的接口，在访问的时候需要登录，用户就执行了登录操作，访问完整后，用户并没有执行登出操作，然后用户访问 csrf-2 中的页面，看到了超链接，好奇这美女到底长啥样，一点击，结果钱就被人转走了。

#### CSRF防御

先来说说防御思路。

CSRF 防御，一个核心思路就是在前端请求中，添加一个随机数。

因为在 CSRF 攻击中，黑客网站其实是不知道用户的 Cookie 具体是什么的，他是让用户自己发送请求到网上银行这个网站的，因为这个过程会自动携带上 Cookie 中的信息。

所以我们的防御思路是这样：用户在访问网上银行时，除了携带 Cookie 中的信息之外，还需要携带一个随机数，如果用户没有携带这个随机数，则网上银行网站会拒绝该请求。黑客网站诱导用户点击超链接时，会自动携带上 Cookie 中的信息，但是却不会自动携带随机数，这样就成功的避免掉 CSRF 攻击了。

将 `_csrf` 放在 Model 中返回前端了，而是放在 Cookie 中返回前端，配置方式如下：

```JAVA
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
```

有小伙伴可能会说放在 Cookie 中不是又被黑客网站盗用了吗？其实不会的，大家注意如下两个问题：

1. 黑客网站根本不知道你的 Cookie 里边存的啥，他也不需要知道，因为 CSRF 攻击是浏览器自动携带上 Cookie 中的数据的。
2. 我们将服务端生成的随机数放在 Cookie 中，前端需要从 Cookie 中自己提取出来 `_csrf` 参数，然后拼接成参数传递给后端，单纯的将 Cookie 中的数据传到服务端是没用的。

理解透了上面两点，你就会发现 `_csrf` 放在 Cookie 中是没有问题的，但是大家注意，配置的时候我们通过 withHttpOnlyFalse 方法获取了 CookieCsrfTokenRepository 的实例，该方法会设置 Cookie 中的 HttpOnly 属性为 false，也就是允许前端通过 js 操作 Cookie（否则你就没有办法获取到 `_csrf`）。

配置完成后，重启项目，此时我们就发现返回的 Cookie 中多了一项：

![img](Spring%20Security%20+%20OAuth2/20200516234139.png)

接下来，我们通过自定义登录页面，来看看前端要如何操作。

首先我们在 resources/static 目录下新建一个 html 页面叫做 login.html：

```HTML
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="js/jquery.min.js"></script>
    <script src="js/jquery.cookie.js"></script>
</head>
<body>
<div>
    <input type="text" id="username">
    <input type="password" id="password">
    <input type="button" value="登录" id="loginBtn">
</div>
<script>
    $("#loginBtn").click(function () {
        let _csrf = $.cookie('XSRF-TOKEN');
        $.post('/login.html',{username:$("#username").val(),password:$("#password").val(),_csrf:_csrf},function (data) {
            alert(data);
        })
    })
</script>
</body>
</html>
```

这段 html 我给大家解释下：

1. 首先引入 jquery 和 jquery.cookie ，方便我们一会操作 Cookie。
2. 定义三个 input，前两个是用户名和密码，第三个是登录按钮。
3. 点击登录按钮之后，我们先从 Cookie 中提取出 XSRF-TOKEN，这也就是我们要上传的 csrf 参数。
4. 通过一个 POST 请求执行登录操作，注意携带上 `_csrf` 参数。

服务端我们也稍作修改，如下：

```JAVA
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .successHandler((req,resp,authentication)->{
                    resp.getWriter().write("success");
                })
                .permitAll()
                .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
```

一方面这里给 js 文件放行。

另一方面配置一下登录页面，以及登录成功的回调，这里简单期间，登录成功的回调我就给一个字符串就可以了。

OK，所有事情做完之后，我们访问 login.html 页面，输入用户名密码进行登录，结果如下：

[![img](Spring%20Security%20+%20OAuth2/20200517000551.png)](http://img.itboyhub.com/2020/05/20200517000551.png)

可以看到，我们的 `_csrf` 配置已经生效了。

### 前后端分离

#### 登录

> - 前后端分离登录访问接口,springsecurity已经封装好，无需自己再写。
>
> - **/login是springsecurity封装好的登录接口，/logout是封装好的登出接口。**
> - **登录使用post请求，注销用get/post请求**
> - **登录成功之后，要在登录成功处理逻辑中将token返回给前端**

<img src="Spring%20Security%20+%20OAuth2/image-20230223092602528.png" alt="image-20230223092602528" style="zoom:67%;" />

#### 过滤器

> - **当前端带着token访问页面时，经过过滤器，由过滤器验证token**
> - **创建一个过滤器的类继承OncePerRequestFilter。**OncePerRequestFilter作为SpringMVC中的一个过滤器，在每次请求的时候都会执行。它是对于Filter的抽象实现。比起特定的过滤器来说，**这个过滤器对于每次的请求都进行请求过滤。**

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.services.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 获取请求路径
        String requestURI = request.getRequestURI();
        // 如果请求路径不是登录路径，则执行过滤器逻辑
        if (!requestURI.equals("/login")) {
            
        // 从请求头中获取JWT令牌
        String authToken = request.getHeader("Authorization");

        // 检查令牌是否存在并以 "Bearer " 开头
        if (authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7); // 移除 "Bearer " 前缀

            // 从令牌中提取用户名
            String username = JwtUtils.getMemberAccountByJwtToken(authToken);

            // 如果用户名不为空且用户尚未经过身份验证，则进行令牌验证
            if (username != null) {
                // 检查令牌的有效性
                if (JwtUtils.checkToken(authToken)) {
                    // 使用用户服务加载用户详细信息，通常包括角色和权限
                    UserDetails userDetails = userService.loadUserByUsername(username);

                    // 如果用户详细信息存在
                    if (userDetails != null) {
                        // 验证令牌与用户详细信息，并创建身份验证对象
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        // 设置身份验证对象的详细信息
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 将身份验证对象设置为Spring Security上下文中的当前身份验证对象
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        // 继续处理请求链
        filterChain.doFilter(request, response);
                    }
                }
            }
        }else {
            // 如果令牌不存在或格式不正确，则不继续处理请求链，直接返回
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }   
    }else{
         // 如果是登录请求，直接继续处理请求链
         filterChain.doFilter(request, response);
     }
  }
}

```

当我们自定义认证逻辑时，需要用下面的过滤器。与上面的过滤器的区别就是对身份先进行验证再获取用户信息。

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.services.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个过滤器用来判断JWT是否有效
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        // 获取请求路径
        String requestURI = request.getRequestURI();
        // 如果请求路径不是登录路径，则执行过滤器逻辑
        if (!requestURI.equals("/login")) {
        
        // 从请求头中获取JWT令牌
        String authToken = httpServletRequest.getHeader("UserToken");

        // 获取到当前用户的account
        String account = JwtUtils.getMemberAccountByJwtToken(authToken);

        System.out.println("自定义JWT过滤器获得用户名为" + account);

        // 当token中的username不为空时进行验证token是否是有效的token
        if (!account.equals("")) {
            // token中username不为空，并且Context中的认证为空，进行token验证
            if (JwtUtils.checkToken(authToken)) {   // 验证当前token是否有效

                // 创建一个未经过身份验证的 Authentication 对象，这里使用用户名作为凭证
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(account, null);

                // 设置身份验证对象的详细信息，例如远程地址、会话ID等
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                // 调用身份验证管理器进行身份验证
                Authentication authentication = authenticationManager.authenticate(authenticationToken);

                // 将经过身份验证的 Authentication 对象设置到安全上下文中
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // 放行给下个过滤器
        filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        }else {
            // 如果令牌不存在或格式不正确，则不继续处理请求链，直接返回
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
      }else{
         // 如果是登录请求，直接继续处理请求链
         filterChain.doFilter(request, response);
     }
  }
}

```

使用 `.addFilterBefore()` 方法将自定义的 `JwtAuthenticationTokenFilter` 添加到 Spring Security 过滤器链中，确保它在 `UsernamePasswordAuthenticationFilter` 之前执行。

![image-20240220153940072](Spring%20Security%20+%20OAuth2/image-20240220153940072.png)

## 效果图

> 未登录时访问指定资源， 返回未登录的json字符串 ， index是我在controller层写的一个简单接口,返回index字符串

![img](Spring%20Security%20+%20OAuth2/20201205144157413.png)

> 输入账号错误，返回用户名错误的json字符串 , 需说明一点，/login是springsecurity封装好的接口，无须你在controller写login接口，/logout也同理。

![img](Spring%20Security%20+%20OAuth2/20201205144529987.png)

> 输入密码错误，返回密码错误的json字符串

![img](Spring%20Security%20+%20OAuth2/20201205144719112.png)

> 登录成功， 返回登录成功的json字符串并返回cookie

![img](Spring%20Security%20+%20OAuth2/20201205144806765.png)

> 登录成功并且拥有权限访问指定资源， 返回资源相关数据的json字符串

![img](Spring%20Security%20+%20OAuth2/20201205145102911.png)

![img](Spring%20Security%20+%20OAuth2/20201205145223200.png)

![img](Spring%20Security%20+%20OAuth2/20201205145452540.png)

> 登录成功但无权限访问指定资源时，返回权限不足的json字符串

![img](Spring%20Security%20+%20OAuth2/20201205145636353.png)

> 异地登录，返回异地登录，强制下线的json字符串 ， 测试的基础是要在两台不同的机器上登录，然后访问/index。

![img](Spring%20Security%20+%20OAuth2/20201205145859176.png)

> 注销成功，返回注销成功的json字符串并删除cookie

![img](Spring%20Security%20+%20OAuth2/20201205150235122.png)

## 从SpringSecurity5到SpringSecurity6

### 1、概览

[Spring Security 6](https://springdoc.cn/spring-security/) 有几处重大变化，包括删除了一些类和已废弃的方法，并引入了一些新方法。

从 Spring Security 5 迁移到 Spring Security 6 可以在不破坏现有代码的情况下逐步完成。此外，还可以使用 [OpenRewrite](https://www.baeldung.com/java-openrewrite) 等第三方插件来促进向最新版本的迁移。

本文将带你了解如何把 Spring Security 5 的现有应用迁移到 Spring Security 6（替换过时的方法，并利用 Lambda DSL 简化配置。此外，还利用 OpenRewrite 加快迁移速度）。

### 2、Spring Security 和 Spring Boot 版本

[Spring Boot](https://springdoc.cn/spring-boot/) 基于 Spring 框架，各版本的 Spring Boot 使用最新版本的 Spring 框架。Spring Boot 2 默认使用 Spring Security 5，而 Spring Boot 3 使用 Spring Security 6。

要在 Spring Boot 应用中使用 Spring Security，首先需要在 `pom.xml` 中添加 `spring-boot-starter-security` 依赖。

可以在 `pom.xml` 的 `properties` 部分 指定所需的版本，从而覆盖默认的 Spring Security 版本 ：

```xml
<properties>
    <spring-security.version>5.8.9</spring-security.version>
</properties>
```

如上，指定在项目中使用 Spring Security *5.8.9*，覆盖默认版本。通过这种方法，可以在 Spring Boot 2 中使用 Spring Security 6。

### 3、Spring Security 6 的新功能

Spring Security 6 引入了多项功能更新，以提高安全性和健壮性。它现在至少需要 Java 17，并使用 `jakarta` 命名空间。

主要变化之一是删除了 `WebSecurityConfigurerAdapter`，转而使用基于组件的安全配置。

此外，还删除了 `authorizeRequests()` 并代之以 `authorizeHttpRequests()` 来定义授权规则（Authorization Rule）。

它还引入了 `requestMatcher()` 和 `securityMatcher()` 等方法 ， 以取代 `antMatcher()` 和 `mvcMatcher()` 来配置请求资源的安全配置。`requestMatcher()` 方法更安全，因为它会为请求配置选择合适的 `RequestMatcher` 实现。

其他已废弃的方法，如 `cors()` 和 `csrf()`，现在有了函数式的替代方法。

### 4、项目设置

首先，在 `pom.xml` 中添加 [spring-boot-starter-web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web) 和 [spring-boot-starter-security](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security) 来创建 *Spring Boot 2.7.5* 项目 ：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.7.5</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>2.7.5</version>
</dependency>
```

`spring-boot-starter-security` 依赖使用 Spring Security 5。

接下来，创建一个 `WebSecurityConfig` 配置类：

```java
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
}
```

如上，该类继承了 `WebSecurityConfigurerAdapter` 类，继承了各种安全配置方法。用 `@EnableWebSecurity` 对该类进行注解，以启动为 Web 请求配置安全配置的过程。此外，还启用了方法级授权。

此外，定义一个基于内存的用户，用于身份认证：

```java
@Override
void configure(AuthenticationManagerBuilder auth) throws Exception {
    UserDetails user = User.withDefaultPasswordEncoder()
      .username("Admin")
      .password("password")
      .roles("ADMIN")
      .build();
    auth.inMemoryAuthentication().withUser(user);
}
```

如上，通过覆写 `configure` 方法创建了一个内存用户。

接下来，覆写 `configure(WebSecurity web)` 方法，将静态资源排除在安全配置之外：

```java
@Override
void configure(WebSecurity web) {
    web.ignoring().antMatchers("/js/**", "/css/**");
}
```

最后，通过覆写 `configure(HttpSecurity http)` 方法来创建 `HttpSecurity`：

```java
@Override
void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
      .antMatchers("/").permitAll()
      .anyRequest().authenticated()
      .and()
      .formLogin()
      .and()
      .httpBasic();
}
```

该设置展示了典型的 Spring Security 5 配置。接下来，我们要把这些代码移植到 Spring Security 6。

### 5、将项目迁移到 Spring Security 6

Spring 建议采用增量迁移方法，以防止在升级到 Spring Security 6 时破坏现有代码。在升级到 Spring Security 6 之前，可以先将 Spring Boot 应用升级到 Spring Security 5.8.5，并更新代码以使用新功能。迁移到 5.8.5 版本为预期的版本 6 变更做好准备。

在增量迁移过程中，IDEA 会提醒我们注意已废弃的功能。这有助于增量更新过程。

为了简化起见，我们通过将应用更新为 *Spring Boot 3.2.2* 来直接将示例项目迁移到 *Spring Security 6*。如果应用使用的是 Spring Boot 2，可以在 `properties` 部分中指定 Spring Security 6。

修改 `pom.xml` 以使用最新的 Spring Boot 版本：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.2.2</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>3.2.2</version>
</dependency>
```

在初始的设置中，使用的是 Spring Boot 2.7.5，它默认使用 Spring Security 5。

**注意，Spring Boot 3 的最低 Java 版本是 Java 17**。

接下来，重构现有代码以使用 Spring Security 6。

#### 5.1、@Configuration 注解

在 Spring Security 6 之前，`@Configuration` 注解是 `@EnableWebSecurity` 的一部分（元注解），但在最新更新后，必须用 `@Configuration` 来注解我们的安全配置：

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
}
```

如上，单独引入了 `@Configuration`，因为它不再是 `@EnableWebSecurity` 注解的 一部分。也不再是 `@EnableMethodSecurity`、`@EnableWebFluxSecurity` 或`@EnableGlobalMethodSecurity` 注解的一部分。

此外，`@EnableGlobalMethodSecurity` 被标记为已弃用，并将被 `@EnableMethodSecurity` 替代。默认情况下，它启用了 Spring 的 *pre-post* 注解。因此，我们引入 `@EnableMethodSecurity` 来在方法级别提供授权功能。

#### 5.2、WebSecurityConfigurerAdapter

最新更新删除了 `WebSecurityConfigurerAdapter` 类，采用了基于组件的配置：

```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
}
```

在这里，我们移除了 `WebSecurityConfigurerAdapter`，这样就不需要再覆写安全配置的方法了。相反，我们可以注册 Bean 来进行安全配置。我们可以注册`WebSecurityCustomizer` Bean 来配置 Web Security，`SecurityFilterChain` Bean 来配置 HTTP Security，`InMemoryUserDetails` Bean 来注册自定义用户等等。

#### 5.3、WebSecurityCustomizer Bean

通过定义 `WebSecurityCustomizer` Bean 来排除静态资源：

```java
@Bean
WebSecurityCustomizer webSecurityCustomizer() {
   return (web) -> web.ignoring().requestMatchers("/js/**", "/css/**");
}
```

`WebSecurityCustomizer` 接口取代了 `WebSecurityConfigurerAdapter` 接口中的`configure(Websecurity web)` 方法。

#### 5.4、AuthenticationManager Bean

在前面的章节中，我们通过覆写 `WebSecurityConfigurerAdapter` 中的 `configure(AuthenticationManagerBuilder auth)` 方法创建了一个内存用户。

现在，通过注册 `InMemoryUserDetailsManager` Bean 来重构认证凭证：

```java
@Bean
InMemoryUserDetailsManager userDetailsService() {
    UserDetails user = User.withDefaultPasswordEncoder()
      .username("Admin")
      .password("admin")
      .roles("USER")
      .build();

    return new InMemoryUserDetailsManager(user);
}
```

如上，定义了一个具有 `USER` 角色的内存用户，以提供基于角色的授权。

#### 5.5、HTTP Security 配置

在以前的 Spring Security 版本中，我们通过覆写 `WebSecurityConfigurer` 类中的 `configure` 方法来配置 `HttpSecurity`。由于在最新版本中删除了该方法，所以需要通过注册 `SecurityFilterChain` Bean 来配置 HTTP Security：

```java
@Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(
          request -> request
            .requestMatchers("/").permitAll()
            .anyRequest().authenticated()
      )
      .formLogin(Customizer.withDefaults())
      .httpBasic(Customizer.withDefaults());
   return http.build();
}
```

在上面的代码中，我们用 `authorizeHttpRequests()` 方法替换了`authorizeRequest()` 方法。新方法使用 `AuthorizationManager` API，简化了重用和定制。

此外，它还能通过延迟身份认证查询来提高性能。只有当请求需要授权时，才会进行身份认证查询。

如果没有自定义规则，就使用 `Customizer.withDefaults()` 方法来使用默认配置。

此外，使用 `requestMatchers()` 而不是 `antMatcher()` 或 `mvcMatcher()` 来确保资源安全。

#### 5.6、RequestCache

请求缓存有助于在需要进行身份认证时保存用户请求，并在用户成功通过身份认证后将其重定向到请求。在 Spring Security 6 之前，`RequestCache` 会在每个传入请求上进行检查，以查看是否有保存的请求需要重定向。这会读取每个 `RequestCache` 上的 `HttpSession`。

在 Spring Security 6 中，请求缓存只检查请求是否包含名为 “*continue*” 的特殊参数。这不仅提高了性能，还避免了不必要地读取 `HttpSession`。

### 6.总结

将SpringSecurity5的[SpringsecurityConfig](http://114.115.170.237/2023/02/21/spring-security-oauth2/#SpringsecurityConfig)中的转成SpringSecurity6

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private AuthenticationEnryPoint authenticationEnryPoint;
    @Autowired
    private AuthenticationSuccess authenticationSuccess;
    @Autowired
    private AuthenticationFailure authenticationFailure;
    @Autowired
    private LogoutSuccessHandler authenticationLogout;
    @Autowired
    private AccessDeny accessDeny;
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;
    @Autowired
    SelfAuthenticationProvider selfAuthenticationProvider;      //自定义登录认证逻辑处理

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**");
    }
    
    // 自定义认证，不写这个也可以，但是需要实现AuthenticationProvider接口的类
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authorizeHandler);
		return authenticationManagerBuilder.build();
	}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::configureDefaults)

            .authorizeHttpRequests((requests) -> requests
                .anyRequest().authenticated())

            .formLogin((form) -> form
                .permitAll()
                .successHandler(authenticationSuccess)
                .failureHandler(authenticationFailure))

            .logout((logout) -> logout
                .permitAll()
                .logoutSuccessHandler(authenticationLogout)
                .deleteCookies("JSESSIONID"))

            .exceptionHandling((exceptions) -> exceptions
                .accessDeniedHandler(accessDeny)
                .authenticationEntryPoint(authenticationEnryPoint))

            .sessionManagement((session) -> session
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy));

        return http.build();
    }
}
```



# [OAuth2.0](https://blog.kdyzm.cn/post/24)

原作者源码地址：https://gitee.com/kdyzm/spring-security-oauth-study

克隆的源码：https://gitee.com/SuperLz0418/spring-security-oauth-study

将代码拉取到idea中，切换标签查看不同版本进度的代码，更加系统学习复习

## 认识OAuth2

### 1.为什么需要 OAuth2

关于我们为什么需要 OAuth2 的问题，网上的文章很多，我们常见的第三方登录就是一个 OAuth2 的典型应用，阮一峰大佬之前有一篇文章非常形象的解释了这个问题

#### 1.1 快递员问题

我住在一个大型的居民小区。

<img src="Spring%20Security%20+%20OAuth2/oath-1690273827740.png" style="zoom:50%;" />

小区有门禁系统。

<img src="Spring%20Security%20+%20OAuth2/oauth-1-2-1690273827756.png" style="zoom:50%;" />

进入的时候需要输入密码。

<img src="Spring%20Security%20+%20OAuth2/oath-1-3-1690273827768.png" style="zoom:50%;" />

我经常网购和外卖，每天都有快递员来送货。我必须找到一个办法，让快递员通过门禁系统，进入小区。

<img src="Spring%20Security%20+%20OAuth2/oauth-1-4-1690273827780.png" style="zoom:50%;" />

如果我把自己的密码，告诉快递员，他就拥有了与我同样的权限，这样好像不太合适。万一我想取消他进入小区的权力，也很麻烦，我自己的密码也得跟着改了，还得通知其他的快递员。

有没有一种办法，让快递员能够自由进入小区，又不必知道小区居民的密码，而且他的唯一权限就是送货，其他需要密码的场合，他都没有权限？

#### 1.2 授权机制的设计

于是，我设计了一套授权机制。

第一步，门禁系统的密码输入器下面，增加一个按钮，叫做”获取授权”。快递员需要首先按这个按钮，去申请授权。

第二步，他按下按钮以后，屋主（也就是我）的手机就会跳出对话框：有人正在要求授权。系统还会显示该快递员的姓名、工号和所属的快递公司。

我确认请求属实，就点击按钮，告诉门禁系统，我同意给予他进入小区的授权。

第三步，门禁系统得到我的确认以后，向快递员显示一个进入小区的令牌（access token）。令牌就是类似密码的一串数字，只在短期内（比如七天）有效。

第四步，快递员向门禁系统输入令牌，进入小区。

有人可能会问，为什么不是远程为快递员开门，而要为他单独生成一个令牌？这是因为快递员可能每天都会来送货，第二天他还可以复用这个令牌。另外，有的小区有多重门禁，快递员可以使用同一个令牌通过它们。

#### 1.3 互联网场景

我们把上面的例子搬到互联网，就是 OAuth 的设计了。

首先，居民小区就是储存用户数据的网络服务。比如，微信储存了我的好友信息，获取这些信息，就必须经过微信的”门禁系统”。

其次，快递员（或者说快递公司）就是第三方应用，想要穿过门禁系统，进入小区。

最后，我就是用户本人，同意授权第三方应用进入小区，获取我的数据。

简单说，OAuth 就是一种授权机制。数据的所有者告诉系统，同意授权第三方应用进入系统，获取这些数据。系统从而产生一个短期的进入令牌（token），用来代替密码，供第三方应用使用。

#### 1.4 令牌与密码

令牌（token）与密码（password）的作用是一样的，都可以进入系统，但是有三点差异。

（1）令牌是短期的，到期会自动失效，用户自己无法修改。密码一般长期有效，用户不修改，就不会发生变化。

（2）令牌可以被数据所有者撤销，会立即失效。以上例而言，屋主可以随时取消快递员的令牌。密码一般不允许被他人撤销。

（3）令牌有权限范围（scope），比如只能进小区的二号门。对于网络服务来说，只读令牌就比读写令牌更安全。密码一般是完整权限。

上面这些设计，保证了令牌既可以让第三方应用获得权限，同时又随时可控，不会危及系统安全。这就是 OAuth2.0 的优点。

注意，只要知道了令牌，就能进入系统。系统一般不会再次确认身份，所以令牌必须保密，泄漏令牌与泄漏密码的后果是一样的。 这也是为什么令牌的有效期，一般都设置得很短的原因。

OAuth2.0 对于如何颁发令牌的细节，规定得非常详细。具体来说，一共分成四种授权类型（authorization grant），即四种颁发令牌的方式，适用于不同的互联网场景。

这段看完，相信大家已经大概明白 OAuth2 的作用了。

### 2.什么是 OAuth2

OAuth 是一个开放标准，该标准允许用户让第三方应用访问该用户在某一网站上存储的私密资源（如头像、照片、视频等），而在这个过程中无需将用户名和密码提供给第三方应用。实现这一功能是通过提供一个令牌（token），而不是用户名和密码来访问他们存放在特定服务提供者的数据。采用令牌（token）的方式可以让用户灵活的对第三方应用授权或者收回权限。

OAuth2 是 OAuth 协议的下一版本，但不向下兼容 OAuth 1.0。传统的 Web 开发登录认证一般都是基于 session 的，但是在前后端分离的架构中继续使用 session 就会有许多不便，因为移动端（Android、iOS、微信小程序等）要么不支持 cookie（微信小程序），要么使用非常不便，对于这些问题，使用 OAuth2 认证都能解决。

对于大家而言，我们在互联网应用中最常见的 OAuth2 应该就是各种第三方登录了，例如 QQ 授权登录、微信授权登录、微博授权登录、GitHub 授权登录等等。

![img](Spring%20Security%20+%20OAuth2/20210615151716340.png)

### 3.四种模式

OAuth2 协议一共支持 4 种不同的授权模式：

1.  授权码模式：常见的第三方平台登录功能基本都是使用这种模式。
2.  简化模式：简化模式是不需要客户端服务器参与，直接在浏览器中向授权服务器申请令牌（token），一般如果网站是纯静态页面则可以采用这种方式。
3.  密码模式：密码模式是用户把用户名密码直接告诉客户端，客户端使用说这些信息向授权服务器申请令牌（token）。这需要用户对客户端高度信任，例如客户端应用和服务提供商就是同一家公司，我们自己做前后端分离登录就可以采用这种模式。
4.  客户端模式：客户端模式是指客户端使用自己的名义而不是用户的名义向服务提供者申请授权，严格来说，客户端模式并不能算作 OAuth 协议要解决的问题的一种解决方案，但是，对于开发者而言，在一些前后端分离应用或者为移动端提供的认证授权服务器上使用这种模式还是非常方便的。

#### 3.1 授权码模式

授权码模式是最安全并且使用最广泛的一种模式。假如我要引入微信登录功能，那么我的流程可能是这样：

![image-20231113173010896](Spring%20Security%20+%20OAuth2/image-20231113173010896.png)

在授权码模式中，我们分**授权服务器和资源服务器**，授权服务器用来派发 Token，拿着 Token 则可以去资源服务器获取资源，这两个服务器可以分开，也可以合并。

上面这张流程图的含义，具体是这样：

1. 首先，我会在我的 www.fahaxikilz.com 这个网页上放一个超链接（我的网站相当于是第三方应用），用户 A （服务方的用户，例如微信用户）点击这个超链接就会去请求授权服务器（微信的授权服务器），用户点击的过程其实也就是我跟用户要授权的过程，这就是上图中的 1、2 步。

2. 接下来的第三步，就是用户点击了超链接之后，像授权服务器发送请求，一般来说，我放在 www.fahaxikilz.com 网页上的超链接可能有如下参数：

   ```
   https://wx.qq.com/oauth/authorize?response_type=code&client_id=lz&redirect_uri=www.fahaxikilz.com&scope=all
   ```

这里边有好几个参数，在后面的代码中我们都会用到，这里先和大家简单解释一下：

* **response\_type 表示授权类型，使用授权码模式的时候这里固定为 code，表示要求返回授权码（将来拿着这个授权码去获取 access\_token）。**
* client\_id 表示客户端 id，也就是我应用的 id。有的小伙伴对这个不好理解，我说一下，如果我想让我的 www.fahaxikilz.com 接入微信登录功能，我肯定得去微信开放平台注册，去填入我自己应用的基本信息等等，弄完之后，微信会给我一个 APPID，也就是我这里的 client\_id，所以，从这里可以看出，授权服务器在校验的时候，会做两件事：1.校验客户端的身份；2.校验用户身份。
* **redirect\_uri 表示用户登录在成功/失败后，跳转的地址（成功登录微信后，跳转到 www.fahaxikilz.com 中的哪个页面），跳转的时候，还会携带上一个授权码参数。**
* scope 表示授权范围，即 www.fahaxikilz.com 这个网站拿着用户的 token 都能干啥（一般来说就是获取用户非敏感的基本信息）。

3.  接下来第四步，www.fahaxikilz.com 这个网站，拿着第三步获取到的 code 以及自己的 client\_id 和 client\_secret 以及其他一些信息去授权服务器请求令牌，微信的授权服务器在校验过这些数据之后，就会发送一个令牌回来。这个过程一般是在后端完成的，而不是利用 js 去完成。
4.  接下来拿着这个 token，我们就可以去请求用户信息了。

一般情况下我们认为授权码模式是四种模式中最安全的一种模式，因为这种模式我们的 access\_token 不用经过浏览器或者移动端 App，是直接从我们的后台发送到授权服务器上，这样就很大程度减少了 access\_token 泄漏的风险。

OK，这是我们介绍的授权码模式。

#### 3.2 简化模式

简化模式是怎么一回事呢？

搭建的博客/电子书都是典型的纯前端应用，就是只有页面，没有后端，对于这种情况，如果我想接入微信登录该怎么办呢？这就用到了我们说的简化模式。

我们来看下简化模式的流程图：

<img src="Spring%20Security%20+%20OAuth2/oauth-1-8-1690273827812.png" alt="图片源自网络" style="zoom:150%;" />



这个流程是这样：

1. 在我 www.fahaxikilz.com 网站上有一个微信登录的超链接，这个超链接类似下面这样：

   ```
   https://wx.qq.com/oauth/authorize?response_type=token&client_id=lz&redirect_uri=www.fahaxikilz.com&scope=all
   ```
   
   这里的参数和前面授权码模式的基本相同，只有 **response\_type 的值不一样，这里是 token，表示要求授权服务器直接返回 access\_token。**
   
2. **用户点击我这个超链接之后，就会跳转到微信登录页面，然后用户进行登录。**
3. **用户登录成功后，微信会自动重定向到 redirect\_uri 参数指定的跳转网址，同时携带上 access\_token，这样用户在前端就获取到 access\_token 了。**

简化模式的弊端很明显，因为**没有后端，所以非常不安全，除非你对安全性要求不高，否则不建议使用。**

#### 3.3 密码模式

密码模式在 Spring Cloud 项目中有着非常广泛的应用，在本系列后面的文章中会重点讲解，这里我们先来了解下密码模式是怎么一回事。

密码模式有一个前提就是你高度信任第三方应用，举个不恰当的例子：如果我要在 www.fahaxikilz.com 这个网站上接入微信登录，我使用了密码模式，那你就要在 www.fahaxikilz.com 这个网站去输入微信的用户名密码，这肯定是不靠谱的，所以密码模式需要你非常信任第三方应用。

微服务中有一个特殊的场景，就是服务之间的调用，用密码模式做鉴权是非常恰当不过的了。这个以后再细说。

我们来看下密码模式的流程：

<img src="Spring%20Security%20+%20OAuth2/oauth-1-9-1690273827901.png" style="zoom:150%;" />

密码式的流程比较简单：

1. 首先 www.fahaxikilz.com 会发送一个 post 请求，类似下面这样的：

   ```
   https://wx.qq.com/oauth/authorize?response_type=password&client_id=lz&username=爱哭的小河豚&password=123
   ```
   
   这里的参数和前面授权码模式的略有差异，**response\_type 的值不一样，这里是 password，表示密码式，另外多了用户名/密码参数，没有重定向的 redirect\_uri ，因为这里不需要重定向。**
   
2. **微信校验过用户名/密码之后，直接在 HTTP 响应中把 access\_token 返回给客户端。**

#### 3.4 客户端模式

有的应用可能没有前端页面，就是一个后台，这种应用开发好了就没有后台。

我们来看一个客户端模式的流程图：

<img src="Spring%20Security%20+%20OAuth2/oauth-1-10-1690273827947.png" style="zoom:150%;" />

这个步骤也很简单，就两步：

1. 客户端发送一个请求到授权服务器，请求格式如下：

   ```
   GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&client_id=APPID&client_secret=APPSECRET
   ```
   
   这里有三个参数，含义如下：
   
   - grant\_type，获取access\_token填写client\_credential
   - client\_id 和 client\_secret 用来确认客户端的身份

2.  授权服务器通过验证后，会直接返回 access\_token 给客户端。

大家发现，在这个过程中好像没有用户什么事了！是的，**客户端模式给出的令牌，就是针对第三方应用的，而不是针对用户的。**

在接入微信公众号后台的时候，有一个获取 Access\_token 的步骤，其实就是这种模式，我截了一张微信开发平台文档的图，大家看下：

![](Spring%20Security%20+%20OAuth2/oauth-1-11-1690273827940.png)

可以看到，这其实就是客户端模式。

## 认证授权一：框架搭建和认证测试
### OAuth2.0介绍

OAuth（开放授权）是一个开放标准，**允许用户授权第三方应用访问他们存储在另外的服务提供者上的信息，而不 需要将用户名和密码提供给第三方应用或分享他们数据的所有内容**。

#### stackoverflow和github

我们登录stackoverflow，网站上会提示几种登录方式，如下所示

![微信图片_20210108011337.jpg](Spring%20Security%20+%20OAuth2/249CRP9PIQENLAPJ003S93NOHT.jpg)

> 其中有一种github登录的方式，点一下进入以下页面

![微信图片_20210108011323.jpg](Spring%20Security%20+%20OAuth2/18ML0LJL2FC3U6VEIUU8DSNOUI.jpg)

这个页面实际上是github授权登陆stackoverflow的页面，只要点击授权按钮，就可以使用github上注册的相关信息注册stackoverflow了，仔细看下这个授权页面，这个授权页面上有几个值得注意的点：

1. 图片中介绍了**三方角色信息：当前操作人，github以及stackoverflow**
2. stackoverflow想通过github获取你的个人信息，哪些个人信息呢？Email addresses (read-only),邮箱地址，而且是只读，也就是说就算你授权了stackoverflow，它也修改不了你github上的个人信息。
3. 授权按钮，以及下面的一行小字`Authorizing will redirect to https://stackauth.com`，也就是说如果你点击了授权按钮，页面将重定向到stackauth.com页面。点击授权按钮之后就仿佛使用github的账号登录上了stackoverflow一样。

#### auth2.0协议

看下OAuth2.0认证流程：引自OAauth2.0协议rfc6749 https://tools.ietf.org/html/rfc6749

```
     +--------+                               +---------------+
     |        |--(A)- Authorization Request --|   Resource    |
     |        |                               |     Owner     |
     |        |<-(B)-- Authorization Grant ---|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(C)-- Authorization Grant ---| Authorization |
     | Client |                               |     Server    |
     |        |<-(D)----- Access Token -------|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(E)----- Access Token -------|    Resource   |
     |        |                               |     Server    |
     |        |<-(F)--- Protected Resource ---|               |
     +--------+                               +---------------+

                     Figure 1: Abstract Protocol Flow

   The abstract OAuth 2.0 flow illustrated in Figure 1 describes the
   interaction between the four roles and includes the following steps:

   (A)  The client requests authorization from the resource owner.  The
        authorization request can be made directly to the resource owner
        (as shown), or preferably indirectly via the authorization
        server as an intermediary.

   (B)  The client receives an authorization grant, which is a
        credential representing the resource owner's authorization,
        expressed using one of four grant types defined in this
        specification or using an extension grant type.  The
        authorization grant type depends on the method used by the
        client to request authorization and the types supported by the
        authorization server.

   (C)  The client requests an access token by authenticating with the
        authorization server and presenting the authorization grant.

   (D)  The authorization server authenticates the client and validates
        the authorization grant, and if valid, issues an access token.

   (E)  The client requests the protected resource from the resource
        server and authenticates by presenting the access token.

   (F)  The resource server validates the access token, and if valid,
        serves the request.

Hardt                        Standards Track                    [Page 7]
```

OAauth2.0包括以下角色：

**1、客户端**

本身不存储资源，需要通过资源拥有者的授权去请求资源服务器的资源，比如：Android客户端、Web客户端（浏 览器端）、微信客户端等，在上面的例子中，stackoverflow扮演的正是这个角色。

**2、资源拥有者**

通常为用户，也可以是应用程序，即该资源的拥有者。在上面的例子中，资源拥有者指的是在github上已经注册的用户。

**3、授权服务器（也称认证服务器）**

用于服务提供商对资源拥有的身份进行认证、对访问资源进行授权，认证成功后会给客户端发放令牌 （access_token），作为客户端访问资源服务器的凭据。本例为github。

**4、资源服务器**

存储资源的服务器，本例子为github存储的用户信息。

如此，上面使用github登陆stackoverflow的流程大体上如下图所示：

<img src="Spring%20Security%20+%20OAuth2/US734ID6BAGJ6L72OEFTS2KJ3.png" alt="516671-20210108105029674-669659689.png" style="zoom:75%;" />

### 使用springboot搭建OAuth2.0认证中心

```
├── docs
│   └── sql
│       └── init.sql
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── kdyzm
    │   │           └── spring
    │   │               └── security
    │   │                   └── auth
    │   │                       └── center
    │   │                           ├── AuthCenterApplication.java
    │   │                           ├── config
    │   │                           │   ├── AuthorizationServer.java
    │   │                           │   ├── MybatisPlusConfig.java
    │   │                           │   ├── TokenConfig.java
    │   │                           │   └── WebSecurityConfig.java
    │   │                           ├── controller
    │   │                           │   └── GrantController.java
    │   │                           ├── entity
    │   │                           │   └── TUser.java
    │   │                           ├── handler
    │   │                           │   └── MyAuthenticationFailureHandler.java
    │   │                           ├── mapper
    │   │                           │   └── UserMapper.java
    │   │                           └── service
    │   │                               └── MyUserDetailsServiceImpl.java
    │   └── resources
    │       ├── application.yml
    │       ├── static
    │       │   ├── css
    │       │   │   ├── bootstrap.min.css
    │       │   │   └── signin.css
    │       │   └── login.html
    │       └── templates
    │           └── grant.html

```

项目源代码地址：https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v2.0.0/ （认准v2.0.0标签）

#### 创建父工程，引入maven

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.lz</groupId>
    <artifactId>oauth2</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

<!--    <modules>-->
<!--        <module>register-server</module>-->
<!--        <module>auth-server</module>-->
<!--        <module>resource-server</module>-->
<!--        <module>common</module>-->
<!--        <module>gateway-server</module>-->
<!--        <module>business-server</module>-->
<!--    </modules>-->

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencyManagement>
        <dependencies>

            <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-netflix-eureka-client -->
            <!--            <dependency>
                            <groupId>org.springframework.cloud</groupId>
                            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
                            <version>2.1.0.RELEASE</version>
                        </dependency>-->

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Greenwich.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-parent</artifactId>
                <version>2.1.3.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>javax.servlet</groupId>
                <artifactId>javax.servlet-api</artifactId>
                <version>3.1.0</version>
                <scope>provided</scope>
            </dependency>

            <dependency>
                <groupId>javax.interceptor</groupId>
                <artifactId>javax.interceptor-api</artifactId>
                <version>1.2</version>
            </dependency>

            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>1.2.47</version>
            </dependency>

            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.0</version>
            </dependency>

            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>5.1.47</version>
            </dependency>


            <dependency>
                <groupId>org.springframework.security</groupId>
                <artifactId>spring-security-jwt</artifactId>
                <version>1.0.10.RELEASE</version>
            </dependency>


            <dependency>
                <groupId>org.springframework.security.oauth.boot</groupId>
                <artifactId>spring-security-oauth2-autoconfigure</artifactId>
                <version>2.1.3.RELEASE</version>
            </dependency>


        </dependencies>
    </dependencyManagement>


    <build>
        <finalName>${project.name}</finalName>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
                <includes>
                    <include>**/*</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>
        <plugins>
            <!--<plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>-->

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>

            <plugin>
                <artifactId>maven-resources-plugin</artifactId>
                <configuration>
                    <encoding>utf-8</encoding>
                    <useDefaultDelimiters>true</useDefaultDelimiters>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

#### 创建auth-server工程

##### 1.引入maven依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.lz</groupId>
        <artifactId>oauth2</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.lz</groupId>
    <artifactId>auth-server</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>auth-server</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        
        <!--三个重要的maven依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-jwt</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.1</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!--页面要用到的框架-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>com.lz</groupId>
            <artifactId>common</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

    </dependencies>

</project>

```

像是mybatis、mybatis plus、fastjson、lombok、thymeleaf等依赖都是辅助依赖，不赘述

##### 2.配置文件

```properties
server:
  port: 30000
spring:
  application:
    name: auth-center
  datasource:
    url: jdbc:mysql://localhost:3306/security?useSSL=false&userUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    username: root
    password: 123456
    driver-class-name: com.mysql.jdbc.Driver
  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html
    cache: false

mybatis:
  mapper-locations: classpath:mapper/**/*.xml

## 引入了eureka的maven依赖，不配置会报错
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false

```

配置完以上三项，就可以将项目正常启动起来了，但是是一个一片空白的项目。接下来配置核心代码实现，需要说明的一点是，https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v1.0.0 该标签下的代码未配置自定义登录页面和授权页面，是实现OAuth2.0认证中心最简单的代码。

##### 3.EnableAuthorizationServer

可以**用 @EnableAuthorizationServer 注解并继承AuthorizationServerConfigurerAdapter来配置OAuth2.0 授权服务器。**

在config包下创建AuthorizationServer：

```java
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter{
    ......
}
```

**AuthorizationServerConfigurerAdapter要求重写以下三个方法并配置方法中的几个类**，这几个类是由Spring创建的独立的配置对象，它们会被Spring传入AuthorizationServerConfigurer中进行配置。

```java
public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {}
public void configure(ClientDetailsServiceConfigurer clients) throws Exception {}
public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {}
```

- ClientDetailsServiceConfigurer ：用来**配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。**
- AuthorizationServerEndpointsConfigurer ：**用来配置令牌（token）的访问端点和令牌服务(tokenservices)。**
- AuthorizationServerSecurityConfigurer ：用来**配置令牌端点的安全约束。**

##### 4.配置客户端详细信息

**ClientDetailsServiceConfigurer 能够使用内存或者JDBC来实现客户端详情服务（ClientDetailsService），ClientDetailsService负责查找ClientDetails，而ClientDetails有几个重要的属性**如下列表：

- **clientId ：（必须的）用来标识客户的Id。**
- **secret ：（需要值得信任的客户端）客户端安全码，如果有的话。**
- **scope ：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。**
- **authorizedGrantTypes ：此客户端可以使用的授权类型，默认为空。**
- **authorities ：此客户端可以使用的权限（基于Spring Security authorities）。**

客户端详情（Client Details）能够在应用程序运行的时候进行更新，可以通过访问底层的存储服务（例如将客户端详情存储在一个关系数据库的表中，就可以使用 JdbcClientDetailsService）或者通过自己实现ClientRegistrationService接口（同时你也可以实现 ClientDetailsService 接口）来进行管理。

我们暂时使用内存方式存储客户端详情信息，配置如下:

```java
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()              //使用in‐memory存储
                .withClient("c1")
                .secret(new BCryptPasswordEncoder().encode("secret"))//$2a$10$0uhIO.ADUFv7OQ/kuwsC1.o3JYvnevt5y3qX/ji0AUXs4KYGio3q6
                .resourceIds("r1")
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")//该client允许的授权类型
                .scopes("all")          //授权范围
                .autoApprove(false)
                .redirectUris("https://www.baidu.com");
    }
```

##### 5.管理令牌

**AuthorizationServerTokenServices 接口定义了一些操作使得你可以对令牌进行一些必要的管理，令牌可以被用来加载身份信息，里面包含了这个令牌的相关权限。**

**自己可以创建 AuthorizationServerTokenServices 这个接口的实现，则需要继承 DefaultTokenServices 这个类，里面包含了一些有用实现，你可以使用它来修改令牌的格式和令牌的存储。默认的，当它尝试创建一个令牌的时候，是使用随机值来进行填充的，除了持久化令牌是委托一个 TokenStore 接口来实现以外，这个类几乎帮你做了所有的事情。并且 TokenStore 这个接口有一个默认的实现，它就是 InMemoryTokenStore ，如其命名，所有的令牌是被保存在了内存中。**除了使用这个类以外，你还可以使用一些其他的预定义实现，下面有几个版本，它们都实现了TokenStore接口：

- **InMemoryTokenStore ：这个版本的实现是被默认采用的，它可以完美的工作在单服务器上（即访问并发量压力不大的情况下，并且它在失败的时候不会进行备份），大多数的项目都可以使用这个版本的实现来进行尝试，你可以在开发的时候使用它来进行管理，因为不会被保存到磁盘中，所以更易于调试。**
- **JdbcTokenStore ：这是一个基于JDBC的实现版本，令牌会被保存进关系型数据库。使用这个版本的实现时，你可以在不同的服务器之间共享令牌信息**，使用这个版本的时候请注意把"spring-jdbc"这个依赖加入到你的classpath当中。
- **JwtTokenStore ：这个版本的全称是 JSON Web Token（JWT），它可以把令牌相关的数据进行编码（因此对于后端服务来说，它不需要进行存储，这将是一个重大优势），但是它有一个缺点，那就是撤销一个已经授权令牌将会非常困难，所以它通常用来处理一个生命周期较短的令牌以及撤销刷新令牌（refresh_token）。另外一个缺点就是这个令牌占用的空间会比较大，如果你加入了比较多用户凭证信息。JwtTokenStore 不会保存任何数据，但是它在转换令牌值以及授权信息方面与 DefaultTokenServices 所扮演的角色是一样的。**

###### 5.1 定义TokenConfig

在config包下定义TokenConfig，我们暂时先**使用InMemoryTokenStore，生成一个普通的令牌。**

```java
@Configuration 
public class TokenConfig {
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}
```

###### 5.2 定义ClientDetailsService

由于配置了4，所以**这里spring会帮我们生成一个基于内存的 ClientDetailsService**

###### 5.3 定义AuthorizationServerTokenServices

在AuthorizationServer中定义AuthorizationServerTokenServices

```java
@Autowired
private TokenStore tokenStore;

@Autowired
private ClientDetailsService clientDetailsService;

@Bean
public AuthorizationServerTokenServices tokenServices(){
    DefaultTokenServices services = new DefaultTokenServices();
    services.setClientDetailsService(clientDetailsService);
    services.setSupportRefreshToken(true);
    services.setTokenStore(tokenStore);
    services.setAccessTokenValiditySeconds(7200);
    services.setRefreshTokenValiditySeconds(259200);
    return services;
}
```

##### 6.令牌访问端点配置

**AuthorizationServerEndpointsConfigurer 这个对象的实例可以完成令牌服务以及令牌endpoint配置。**

###### 6.1 AuthorizationServerEndpointsConfigurer 授权类型

AuthorizationServerEndpointsConfigurer 通过设定以下属性**决定支持的授权类型**（Grant Types）:

- authenticationManager ：认证管理器，**当你选择了资源所有者密码（password）授权类型的时候，请设置这个属性注入一个 AuthenticationManager 对象**。
- userDetailsService ：**如果你设置了这个属性的话，那说明你有一个自己的 UserDetailsService 接口的实现**，或者你可以把这个东西设置到全局域上面去（例如 GlobalAuthenticationManagerConfigurer 这个配置对象），当你设置了这个之后，那么 "refresh_token" 即刷新令牌授权类型模式的流程中就会包含一个检查，用来确保这个账号是否仍然有效，假如说你禁用了这个账户的话。
- authorizationCodeServices ：这个属性是用来**设置授权码服务的（即 AuthorizationCodeServices 的实例对象），主要用于 "authorization_code" 授权码类型模式。**
- implicitGrantService ：这个属性用于设置隐式授权模式，用来管理隐式授权模式的状态。
- tokenGranter ：**当你设置了这个东西（即 TokenGranter 接口实现），那么授权将会交由你来完全掌控，并且会忽略掉上面的这几个属性，这个属性一般是用作拓展用途的，即标准的四种授权模式已经满足不了你的需求的时候，才会考虑使用这个。**

###### 6.2 AuthorizationServerEndpointsConfigurer授权端点

AuthorizationServerEndpointsConfigurer 这个配置对象有一个叫做 **pathMapping() 的方法用来配置端点URL链接**，它有两个参数：

- **第一个参数： String 类型的，这个端点URL的默认链接。**
- **第二个参数： String 类型的，你要进行替代的URL链接。**

**以上的参数都将以 "/" 字符为开始的字符串**，框架的默认URL链接如下列表，可以作为这个 pathMapping() 方法的第一个参数：

- **/oauth/authorize ：授权端点。**
- **/oauth/token ：令牌端点。**
- **/oauth/confirm_access ：用户确认授权提交端点。**
- **/oauth/error ：授权服务错误信息端点。**
- **/oauth/check_token ：用于资源服务访问的令牌解析端点。**
- **/oauth/token_key ：提供公有密匙的端点，如果你使用JWT令牌的话。**

综上AuthorizationServerEndpointsConfigurer配置如下

```java
@Autowired
private AuthenticationManager authenticationManager;

@Autowired
private AuthorizationCodeServices authorizationCodeServices;

@Bean
public AuthorizationCodeServices authorizationCodeServices(){
    return new InMemoryAuthorizationCodeServices();
}

@Override
public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .authenticationManager(authenticationManager)
        .authorizationCodeServices(authorizationCodeServices)
        .tokenServices(tokenServices())
        .allowedTokenEndpointRequestMethods(HttpMethod.POST);

    endpoints.pathMapping("/oauth/confirm_access","/custom/confirm_access");//自定义授权页面需要
}
```

上面需要的AuthenticationManager的定义在SpringSecurity的配置中，下面会讲到。

##### 7.令牌端点的安全约束

**AuthorizationServerSecurityConfigurer 用来配置令牌端点(Token Endpoint)的安全约束**，在AuthorizationServer中配置如下。

```java
@Override
public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
        .tokenKeyAccess("permitAll()") //(1)
        .checkTokenAccess("permitAll()")//(2)
        .allowFormAuthenticationForClients();//(3)
}
```

（1）tokenkey这个endpoint当使用JwtToken且使用非对称加密时，资源服务用于获取公钥而开放的，这里指这个endpoint完全公开。

（2）checkToken这个endpoint完全公开

（3） 允许表单认证

##### 8.web安全配置

这里可以配置安全拦截机制、自定义登录页面、登录失败拦截器等等

在以下的配置中创建了`AuthenticationManager` bean，这是 6.2 中所需要的。

```java
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    //认证管理器
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    //密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login*","/css/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .failureHandler(myAuthenticationFailureHandler);

    }
}
```

 认证失败handler

```java
/**
 * 认证失败handler
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(exception.getMessage()));
    }
}

```

#### 自定义登陆页面

spring security默认带的登录页面不可修改，加载速度贼慢，原因是使用的css链接是国外的。所以从各方面来说自定义登录页面都是需要的。

##### 创建login.html文件

这个非常简单，只需要将spring security加载速度贼慢的那个页面扒下来就好。

项目中代码链接：https://gitee.com/SuperLz0418/spring-security-oauth-study/blob/v2.0.0/auth-center/src/main/resources/static/login.html

```html
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>请登录</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/signin.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <form class="form-signin" method="post" action="/login">
        <h2 class="form-signin-heading">请登录</h2>
        <p>
            <label for="username" class="sr-only">用户名</label>
            <input type="text" id="username" name="username" class="form-control" placeholder="Username" required=""
                   autofocus="">
        </p>
        <p>
            <label for="password" class="sr-only">密码</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="Password"
                   required="">
        </p>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
    </form>
</div>
</body>
</html>
```

#### 自定义授权页面

默认的授权页面非常丑，这里重写该页面，页面代码地址：https://gitee.com/SuperLz0418/spring-security-oauth-study/blob/v2.0.0/auth-center/src/main/resources/templates/grant.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>授权</title>
</head>
<style>

    html {
        padding: 0px;
        margin: 0px;
    }

    .title {
        background-color: #007BFF;
        height: 50px;
        padding-left: 20%;
        padding-right: 20%;
        color: white;
        line-height: 50px;
        font-size: 18px;
    }

    .title-left {
        float: right;
    }

    .title-right {
        float: left;
    }

    .title-left a {
        color: white;
    }

    .container {
        clear: both;
        text-align: center;
    }

    .btn {
        width: 350px;
        height: 35px;
        line-height: 35px;
        cursor: pointer;
        margin-top: 20px;
        border-radius: 3px;
        background-color: #007BFF;
        color: white;
        border: none;
        font-size: 15px;
    }
</style>
<body style="margin: 0px">
<div class="title">
    <div class="title-right">OAUTH 授权</div>
    <div class="title-left">
        <a href="#help">帮助</a>
    </div>
</div>
<div class="container">
    <h3 th:text="${clientId}+' 请求授权，该应用将获取你的以下信息'"></h3>
    <th:block th:each="item:${scopes}">
        <span th:text="${item}"></span>&nbsp;
    </th:block>

    授权后表明你已同意 <a href="#boot" style="color: #007BFF">OAUTH 服务协议</a>
    <form method="post" action="/oauth/authorize">
        <input type="hidden" name="user_oauth_approval" value="true">
        <div th:each="item:${scopes}">
            <input type="radio" th:name="'scope.'+${item}" value="true" hidden="hidden" checked="checked"/>
        </div>
        <button class="btn" type="submit"> 同意/授权</button>
    </form>
</div>
</body>
</html>
```

然后配置OAuth访问端点替换掉原来的地址：

```java
endpoints.pathMapping("/oauth/confirm_access","/custom/confirm_access");
```

同时，由于重写了页面地址，需要实现/custom/confirm_access 接口

```java
@Controller
@SessionAttributes("authorizationRequest")
public class GrantController {

    /**
     * @see WhitelabelApprovalEndpoint#getAccessConfirmation(java.util.Map, javax.servlet.http.HttpServletRequest)
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/custom/confirm_access")
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest) model.get("authorizationRequest");
        ModelAndView view = new ModelAndView();
        view.setViewName("grant");
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scopes",authorizationRequest.getScope());
        return view;
    }

}
```

#### 实现UserDetailsService接口

完成以上配置之后基本上已经配置完了，但是还差一点，那就是实现UserDetailsService接口，不实现该接口，会出现后端死循环导致的stackoverflow问题。

为什么要实现该接口？

该接口通过userName获取用户密码信息用于校验用户密码登陆和权限信息等。

```java
@Service
@Slf4j
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TUser tUser = userMapper.selectOne(new LambdaQueryWrapper<TUser>().eq(TUser::getUsername, username));
        if (Objects.isNull(tUser)) {
            throw new UsernameNotFoundException(username + "账号不存在");//return null也可以
        }
        List<String> allPermissions = userMapper.findAllPermissions(tUser.getId());
        String[] array = null;
        if (CollectionUtils.isEmpty(allPermissions)) {
            log.warn("{} 无任何权限", tUser.getUsername());
            array = new String[]{};
        } else {
            array = new String[allPermissions.size()];
            allPermissions.toArray(array);
        }
        return User
                .withUsername(tUser.getUsername())
                .password(tUser.getPassword())
                .authorities(array).build();
    }
}
```

#### 接口测试

在测试前，需要先执行数据库脚本并启动服务

1. 执行sql/init.sql 文件，创建数据库并创建相关的表
2. 修改auth-center项目下的配置文件中的数据库连接配置

然后运行 AuthCenterApplication 程序，测试几种oauth认证模式

##### 1. 授权码认证模式

> **最安全的一种模式。一般用于client是Web服务器端应用或第三方的原生App调用资源服务的时候。**因为在这种模式中access_token不会经过浏览器或移动端的App，而是直接从服务端去交换，这样就最大限度的减小了令牌泄漏的风险。 该模式下获取token需要分两步走，**第一步获取授权码，第二步获取token。**

###### 获取授权码

![授权码模式 - 获取授权码.gif](Spring%20Security%20+%20OAuth2/3OVOCPM2KK9UDJBG4U1T251HA9.gif)

**接口地址** `http://127.0.0.1:30000/oauth/authorize`

**请求方式** `GET`

**请求参数**

| 字段名        | 描述                                                  |
| :------------ | :---------------------------------------------------- |
| client_id     | **必须和配置在clients中的值保持一致**                 |
| response_type | **固定传值`code`表示使用授权码模式进行认证**          |
| scope         | **必须配置的clients中的值一致**                       |
| redirect_uri  | **获取code之后重定向的地址，必须和配置的clients一致** |

**请求示例**

http://127.0.0.1:30000/oauth/authorize?client_id=c1&response_type=code&scope=all&redirect_uri=https://www.baidu.com

账号密码分别输入：zhangsan/123，进入授权页面之后点击授权按钮，页面跳转之后获取到code。

![image-20230411090420802](Spring%20Security%20+%20OAuth2/image-20230411090420802.png)

###### 获取token

在上一步获取到code之后，利用获取到的该code获取token。

**接口地址** `http://127.0.0.1:30000/oauth/token`

**请求方式** `POST`

**请求参数**

| 字段名        | 描述                                            |
| :------------ | :---------------------------------------------- |
| code          | 上一步**获取到的code**                          |
| grant_type    | **在授权码模式，固定使用`authorization_code`**  |
| client_id     | **必须和配置在clients中的值保持一致**           |
| client_secret | **必须和代码中配置的clients中配置的保持一致**   |
| redirect_uri  | **获取token之后重定向的地址，该地址可以随意写** |

**请求示例**

http://127.0.0.1:30000/oauth/token

请求体  X-www-form-urlencoded

```X-www-form-urlencoded
code:5Rmc3m
grant_type:authorization_code
client_id:c1
client_secret:secret
redirect_uri:https://www.baidu.com
```

**响应结果**

| 字段名        | 描述         |
| ------------- | ------------ |
| access_token  | 令牌         |
| token_type    | 令牌类型     |
| refresh_token | 刷新令牌     |
| expires_in    | 令牌到期时间 |
| scope         | 适用范围     |

![image-20230411090559048](Spring%20Security%20+%20OAuth2/image-20230411090559048.png)

##### 2.简化模式

> **该模式去掉了授权码，用户登陆之后直接获取token并显示在浏览器地址栏中，参数和请求授权码的接口基本上一模一样，唯一的区别就是`response_type`字段，授权码模式下使用的是code字段，在简化模式下使用的是token字段。**一般来说，**简化模式用于没有服务器端的第三方单页面应用，因为没有服务器端就无法接收授权码。**

**接口地址** `http://127.0.0.1:30000/oauth/authorize`

**请求方式** `GET`

**请求参数**

| 字段名        | 描述                                                  |
| :------------ | :---------------------------------------------------- |
| client_id     | **必须和配置在clients中的值保持一致**                 |
| response_type | **固定传值`token`表示使用简化模式进行认证**           |
| scope         | **必须和配置的clients中的值一致**                     |
| redirect_uri  | **获取code之后重定向的地址，必须和配置的clients一致** |

**请求示例**

http://127.0.0.1:30000/oauth/authorize?client_id=c1&response_type=token&scope=all&redirect_uri=https://www.baidu.com

![image-20230411090630902](Spring%20Security%20+%20OAuth2/image-20230411090630902.png)

##### 3.密码模式

> 这种模式十分简单，但是却意味着直接将用户敏感信息泄漏给了client，因此这就说明这种模式只能用于client是我 们自己开发的情况下。因此密码模式一般用于我们自己开发的，第一方原生App或第一方单页面应用

**接口地址** `http://127.0.0.1:30000/oauth/token`

**请求方式** `POST`

**请求参数**

| 字段名      | 描述                                   |
| :---------- | :------------------------------------- |
| client_id   | **必须和配置在clients中的值保持一致**  |
| client_secr | **必须和配置在clients中的值保持一致**  |
| grant_type  | **在密码模式下，该值固定为`password`** |
| username    | **用户名**                             |
| password    | **密码**                               |

**请求示例**

http://127.0.0.1:30000/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=zhangsan&password=123

**响应结果**

| 字段名        | 描述         |
| ------------- | ------------ |
| access_token  | 令牌         |
| token_type    | 令牌类型     |
| refresh_token | 刷新令牌     |
| expires_in    | 令牌到期时间 |
| scope         | 适用范围     |

![image-20230411090654896](Spring%20Security%20+%20OAuth2/image-20230411090654896.png)

##### 4.客户端模式

> **这种模式是最方便但最不安全的模式。**因此这就要求我们对client完全的信任，而client本身也是安全的。因此**这种模式一般用来提供给我们完全信任的服务器端服务。比如，合作方系统对接，拉取一组用户信息。**

**接口地址** `http://127.0.0.1:30000/oauth/token`

**请求方式** `POST`

**请求参数**

| 字段名        | 描述                                             |
| :------------ | :----------------------------------------------- |
| client_id     | **必须和配置在clients中的值保持一致**            |
| client_secret | **必须和配置在clients中的值保持一致**            |
| grant_type    | **在密码模式下，该值固定为`client_credentials`** |

**请求示例**

http://127.0.0.1:30000/oauth/token?client_id=c1&client_secret=secret&grant_type=client_credentials

**响应结果**

| 字段名       | 描述         |
| ------------ | ------------ |
| access_token | 令牌         |
| token_type   | 令牌类型     |
| expires_in   | 令牌到期时间 |
| scope        | 适用范围     |

![image-20230411090717319](Spring%20Security%20+%20OAuth2/image-20230411090717319.png)

##### 5.refresh_token换取新token

**接口地址** `http://127.0.0.1:30000/oauth/token`

**请求方式** `POST`

**请求参数**

| 字段名        | 描述                                                         |
| :------------ | :----------------------------------------------------------- |
| client_id     | **必须和配置在clients中的值保持一致**                        |
| client_secret | **必须和配置在clients中的值保持一致**                        |
| grant_type    | **如果想根据refresh_token换新的token，这里固定传值`refresh_token`** |
| refresh_token | **未失效的refresh_token**                                    |

**请求示例**

http://127.0.0.1:30000/oauth/token?grant_type=refresh_token&refresh_token=09c9d11a-525a-4e5f-bac1-4f32e9025301&client_id=c1&client_secret=secret

**响应结果**

| 字段名        | 描述         |
| ------------- | ------------ |
| access_token  | 令牌         |
| token_type    | 令牌类型     |
| refresh_token | 刷新令牌     |
| expires_in    | 令牌到期时间 |
| scope         | 适用范围     |

![image-20230411090915968](Spring%20Security%20+%20OAuth2/image-20230411090915968.png)

#### 源码地址

本部分源码地址：https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v2.0.0

## 认证授权二：搭建资源服务

在[上一篇文章](https://fahaxikilz.github.io/2023/02/21/spring-security-oauth2/#认证授权一：框架搭建和认证测试)详细讲解了如何搭建一个基于spring boot + oauth2.0的认证服务，这篇文章将会介绍如何搭建一个资源服务。

根据oath2.0协议内容，应当有一个资源服务管理资源服务并提供访问安全控制。

### 1. 创建resource-server工程，引入maven依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-jwt</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

这里虽然引入了jwt的依赖，但是暂时还未用到。

### 2. 配置配置文件

```yaml
server:
  port: 30001
spring:
  application:
    name: resource-server

## 引入了eureka的maven依赖，不配置会报错
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

### 3. 新建启动类

```java
@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
}
```

### 4. Resource服务核心配置

#### 4.1 新建ResouceServerConfig类

**该类需要继承ResourceServerConfigurerAdapter类并需要使用@EnableResourceServer注解注释。**

```java
@Configuration
@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {
    ......
}
```

#### 4.2 核心配置

重写ResouceServerConfig类以下方法以实现ResourceServer的基本配置：

org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer)

```java
@Override
public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources
        .resourceId(RESOURCE_ID)
        .tokenServices(resourceServerTokenServices)//令牌服务
        .stateless(true);
}
```

- **resourceId方法标志了该服务的id，需要和在auth-center服务中配置的id一致。**

- **tokenServices方法指定了令牌管理的实例**，Bean创建方法如下

  ```java
  	@Bean
      public ResourceServerTokenServices resourceServerTokenServices(){
          RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
          remoteTokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:30000/oauth/check_token");
          remoteTokenServices.setClientId("c1");
          remoteTokenServices.setClientSecret("secret");
          return remoteTokenServices;
      }
  ```

- **stateless方法指定了当前资源是否仅仅允许token验证的方法进行校验，默认为true**

#### 4.3 auth2.0安全配置

```java
@Override
public void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/**").access("#oauth2.hasScope('all')")
        .and()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
}
```

**该配置和下面的Web安全配置很像，但是不一样，这里仅仅对auth2.0的安全进行配置。**这里的**.antMatchers("/*\*").access("#oauth2.hasScope('all')")表示所有的请求携带的令牌都必须拥有all的授权范围，其中all授权范围必须和认证服务中的配置相一致。**

#### 4.4 Web安全配置

```java
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/r/r1").hasAuthority("p2")
                .antMatchers("/r/r2").hasAuthority("p2")
                .antMatchers("/**").authenticated()//所有的r/**请求必须认证通过
                .anyRequest().permitAll();//其它所有请求都可以随意访问
    }
}
```

#### 4.5 暴露资源接口

```java
@RestController
@Slf4j
public class OrderController {

    @GetMapping("/r1")
    @PreAuthorize("hasAnyAuthority('p1')")
    public String r1(){
        return "访问资源r1";
    }
}
```

**由于4.4启用了prePostEnabled，所以这里可以使用@PreAuthorize注解对资源安全请求进行管理。**

**`@PreAuthorize("hasAnyAuthority('p1')")`表示请求者必须拥有p1权限**，p1权限定义在auth-server服务表的t_permission表。

### 5. 接口测试

#### 5.1 准备工作

配置好数据库和表、配置文件，配置完成之后分别启动认证服务auth-center服务（端口号30000）和资源服务resource-server（端口号30001）。

#### 5.2 获取token

[有四种获取token的方式。](http://114.115.170.237/2023/02/21/spring-security-oauth2/#%E6%8E%A5%E5%8F%A3%E6%B5%8B%E8%AF%95)

#### 5.3 请求资源服务

假设在5.2已经成功获取到了token：

```json
{
    "access_token": "54039544-252a-4e8b-a5ad-a87f01e2ef0a",
    "token_type": "bearer",
    "refresh_token": "5c2ce05b-13cb-44ce-a6af-d6afcf45f214",
    "expires_in": 6996,
    "scope": "all"
}
```

接下来要携带着token请求资源服务：

| header        | value                                       |
| :------------ | :------------------------------------------ |
| Authorization | Bearer 54039544-252a-4e8b-a5ad-a87f01e2ef0a |

GET请求：http://127.0.0.1:30001/r1

请求成功，结果返回：

```json
访问资源r1
```

请求失败，结果返回：

```json
{
    "error": "invalid_token",
    "error_description": "54039544-252a-4e8b-a5ad-a87f01e2ef0a"
}
```

#### 5.4 请求演示

下面演示使用postman基于密码模式获取token并请求资源服务的过程：

![](Spring%20Security%20+%20OAuth2/UGT1B15V1RJR1HSVMS2F5C2BQ.gif)

### 6.源代码

源代码地址：https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v3.0.0/

## 认证授权三：使用JWT令牌

前面两篇文章详细讲解了如何基于spring boot + oath2.0搭建认证中心和资源中心，本篇文章将会讲解集成jwt以及将客户端信息和授权码信息保存到数据库。

### 一、 JWT

#### 1. JWT简介

JSON Web Token（JWT）是一个开放的行业标准（RFC 7519），它定义了一种简介的、自包含的协议格式，用于在通信双方传递json对象，传递的信息经过数字签名可以被验证和信任。JWT可以使用HMAC算法或使用RSA的公钥/私钥对来签名，防止被篡改。

官网：https://jwt.io/

标准： https://tools.ietf.org/html/rfc7519

JWT令牌的优点：

1. jwt基于json，非常方便解析。
2. 可以在令牌中自定义丰富的内容，易扩展。
3. 通过非对称加密算法及数字签名技术，JWT防止篡改，安全性高。
4. 资源服务使用JWT可不依赖认证服务即可完成授权。

缺点：

1. JWT令牌较长，占存储空间比较大，这意味着会耗费一定的带宽资源
2. JWT签名和验签都要耗费处理器资源

#### 2. JWT令牌结构

**JWT令牌由三部分组成，每部分中间使用点（.）分隔，比如：xxxxx.yyyyy.zzzzz**

**2.1 Header**

**头部包括令牌的类型（即JWT）及使用的哈希算法（如HMAC SHA256或RSA）**

一个例子如下：

下边是Header部分的内容

```json
{ 
  "alg": "HS256",
  "typ": "JWT"
}
```

将上边的内容使用Base64Url编码，得到一个字符串就是JWT令牌的第一部分。

**2.2 Payload**

第二部分是负载，内容也是一个json对象，它是存放有效信息的地方，它可以存放jwt提供的现成字段，比如：iss（签发者）,exp（过期时间戳）, sub（面向的用户）等，也可自定义字段。此部分不建议存放敏感信息，因为此部分可以解码还原原始内容。最后将第二部分负载使用Base64Url编码，得到一个字符串就是JWT令牌的第二部分。

一个例子：

```json
{ 
  "sub": "1234567890",
  "name": "456",
  "admin": true
}
```

**2.3 Signature**

第三部分是签名，此部分用于防止jwt内容被篡改。

这个部分使用base64url将前两部分进行编码，编码后使用点（.）连接组成字符串，最后使用header中声明签名算法进行签名。

一个例子：

```java
HMACSHA256( 
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)
```

- base64UrlEncode(header)：jwt令牌的第一部分。
- base64UrlEncode(payload)：jwt令牌的第二部分。
- secret：签名所使用的密钥。

### 二、配置JWT

#### 1.认证服务配置JWT

TokenConfig类的修改

```java
@Configuration
public class TokenConfig {
    
    private static final String SIGNING_KEY = "auth123";
    
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);//对称秘钥，资源服务器使用该秘钥来验证
        return jwtAccessTokenConverter;
    }
}
```

AuthorizationServerTokenServices设置Token增强类

```java
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Qualifier("clientDetailsServices")
    @Autowired
    private ClientDetailsService clientDetailsService;

    @Bean
    public JdbcAuthorizationCodeServices authorizationCodeServices(DataSource dataSource){
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        services.setAccessTokenValiditySeconds(7200);
        services.setRefreshTokenValiditySeconds(259200);

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);
        
        return services;
    }
```

然后就可以测试了：

POST请求接口：http://127.0.0.1:30000/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=zhangsan&password=123

得到响应结果：

```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJ6aGFuZ3NhbiIsInNjb3BlIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiLCJST0xFX0FQSSJdLCJleHAiOjE2MTAzNzI5MzUsImF1dGhvcml0aWVzIjpbInAxIiwicDIiXSwianRpIjoiOWQzMzRmZGMtOTcwZC00YmJkLWI2MmMtZDU4MDZkNTgzM2YwIiwiY2xpZW50X2lkIjoiYzEifQ.gZraRNeX-o_jKiH7XQgg3TlUQBpxUcXa2-qR_Treu8U",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJ6aGFuZ3NhbiIsInNjb3BlIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiLCJST0xFX0FQSSJdLCJhdGkiOiI5ZDMzNGZkYy05NzBkLTRiYmQtYjYyYy1kNTgwNmQ1ODMzZjAiLCJleHAiOjE2MTA2MjQ5MzUsImF1dGhvcml0aWVzIjpbInAxIiwicDIiXSwianRpIjoiN2U1NzE0NTgtNmU2Zi00YjlmLTkxODQtOWUzZmVmZmQ1YTNjIiwiY2xpZW50X2lkIjoiYzEifQ.wyiS-z-xhBPZSODXZHQVDJCQ6dcmeJjAwBPWe2GhT94",
    "expires_in": 7199,
    "scope": "ROLE_ADMIN ROLE_USER ROLE_API",
    "jti": "9d334fdc-970d-4bbd-b62c-d5806d5833f0"
}
```

会发现accessToken长了很多，这是因为token是jwt字符串，分为三部分，第二部分payload携带了很多信息，打开jwt.io网站，将上面的accessToken贴上去，可以看到Base64解码后的信息：

![2021-01-11_201526.png](Spring%20Security%20+%20OAuth2/2B8J8S7BH0UB15MRJNP45M1G6J.png)

#### 2.资源服务配置

第一步，将认证服务中的TokenConfig直接拷贝到资源服务中

第二步，修改ResouceServerConfig 类

```java
    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .resourceId(RESOURCE_ID)
//                .tokenServices(resourceServerTokenServices)//令牌服务
                .tokenStore(tokenStore)
                .stateless(true);
    }
```

即可完成资源服务集成jwt的功能。

#### 3.接口测试

POST请求 http://127.0.0.1:30000/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=zhangsan&password=123 获取令牌，获取到accessToken之后携带token GET 请求 http://127.0.0.1:30001/r1 ，得到响应结果：

```txt
访问资源r1
```

即可证明成功。

### 三、客户端信息保存到数据库

认证服务客户端信息还是保存在内存中，现在将其改造放到数据库中

#### 1. 新建表

```sql
DROP TABLE IF EXISTS `oauth_client_details`;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL COMMENT '客户端标识',
  `resource_ids` varchar(255) DEFAULT NULL COMMENT '接入资源列表',
  `client_secret` varchar(255) DEFAULT NULL COMMENT '客户端秘钥',
  `scope` varchar(255) DEFAULT NULL,
  `authorized_grant_types` varchar(255) DEFAULT NULL,
  `web_server_redirect_uri` varchar(255) DEFAULT NULL,
  `authorities` varchar(255) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` longtext,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `archived` tinyint(4) DEFAULT NULL,
  `trusted` tinyint(4) DEFAULT NULL,
  `autoapprove` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='接入客户端信息';

/*Data for the table `oauth_client_details` */

insert  into `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`create_time`,`archived`,`trusted`,`autoapprove`) values
('c1','res1','$2a$10$X2xVwW.7cOEh2niPqHYAne9EnjRJFj7QI4TqfmnDou9fT/45sCFEm','ROLE_ADMIN,ROLE_USER,ROLE_API','client_credentials,password,authorization_code,implicit,refresh_token','https://www.baidu.com',NULL,7200,259200,NULL,'2021-01-11 09:09:53',0,0,'false'),
('c2','res2','$2a$10$X2xVwW.7cOEh2niPqHYAne9EnjRJFj7QI4TqfmnDou9fT/45sCFEm','ROLE_API','client_credentials,password,authorization_code,implicit,refresh_token','https://www.baidu.com',NULL,31536000,2592000,NULL,'2021-01-11 09:09:56',0,0,'false');

/*Table structure for table `oauth_code` */

DROP TABLE IF EXISTS `oauth_code`;

CREATE TABLE `oauth_code` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `code` varchar(255) DEFAULT NULL,
  `authentication` blob,
  KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
```

上述SQL新建了两张表**`oauth_client_details`以及`oauth_code`分别用于存储客户端信息以及授权码信息**，由于使用了**jwt token（jwt token本身就存储了数据），所以不再保存数据库**，**两张表均为spring oauth2.0内置表，不需要写SQL，内置框架自动识别表。**

#### 2.修改认证服务配置

对应上述两张表，分别修改Bean对象的创建为jdbc类型的：

```java
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource){
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;//密码编辑器实例

    @Bean
    public ClientDetailsService clientDetailsServices(DataSource dataSource) {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }
```

之后修改客户端配置对象：

```java
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
//        clients.inMemory()
//                .withClient("c1")
//                .secret(new BCryptPasswordEncoder().encode("secret"))//$2a$10$0uhIO.ADUFv7OQ/kuwsC1.o3JYvnevt5y3qX/ji0AUXs4KYGio3q6
//                .resourceIds("r1")
//                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
//                .scopes("all")
//                .autoApprove(false)
//                .redirectUris("https://www.baidu.com");
    }
```

完成修改。

#### 3.接口测试

GET请求：http://127.0.0.1:30000/oauth/authorize?client_id=c1&response_type=code&scope=ROLE_API&redirect_uri=https://www.baidu.com 获取授权码后，观察表oauth_code，里面应当已经有了授权码数据。

### 四、源码地址

源码地址：https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v4.0.0/

## 认证授权四：分布式系统认证授权

前面几篇文章讲解了如何从头开始搭建认证服务和资源服务，从颁发普通令牌到颁发jwt令牌，最终完成了jwt令牌的颁发和校验。本篇文章将会讲解分布式环境下如何进行认证和授权。

### 一、设计思路

![分布式授权图.png](Spring%20Security%20+%20OAuth2/UM6P6K76NI6L08605UC3KB78D.png)

一般来说，一个典型的分布式系统架构如上图所示，这里进行一个简单的设计，来完成分布式系统下的认证和授权。

**整体设计思路是使用OAuth2.0颁发令牌，使用JWT对令牌签名并颁发JWT令牌给客户端。**既然决定使用JWT令牌了，则不需要再调用认证服务器对令牌进行验证了，因为JWT本身就包含了所需要的信息，而且只要验签成功，则可认为令牌可信任且有效。

如上所述，则可以如此设计：

1. 用户请求登陆之后认证服务颁发令牌给用户，浏览器将令牌储存下来。
2. 浏览器请求资源的的时候携带着令牌，**网关拦截请求对令牌验证**，验证的方法很简单，**不请求认证服务而是直接使用密钥（对称或非对称）验签，只要验证成功则将jwt payload中的信息解析成明文放到请求头中转发请求到资源服务。**
3. 资源服务拿到明文信息，根据明文信息中的权限信息验证是否有权限访问该资源，有权限则返回资源信息，无权限则返回401。

综上，整体思路就是网关认证，资源服务鉴权。

典型的微服务架构下会有注册中心、网关等服务，接下来会依次介绍和搭建相关服务。

### 二、注册中心搭建

为了方便程序本地调试方便，这里使用eureka server作为服务注册中心，使用起来也非常简单

#### 1.添加maven依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
</dependencies>
```

#### 2.新建启动类

```java
@SpringBootApplication
@EnableEurekaServer
public class RegisterServer {
    public static void main(String[] args) {
        SpringApplication.run(RegisterServer.class,args);
    }
}
```

#### 3.新建配置文件

```yaml
spring:
  application:
    name: register-server

server:
  port: 8765 #启动端口

eureka:
  server:
    enable-self-preservation: false    #关闭服务器自我保护，客户端心跳检测15分钟内错误达到80%服务会保护，导致别人还认为是好用的服务
    eviction-interval-timer-in-ms: 10000 #清理间隔（单位毫秒，默认是60*1000）5秒将客户端剔除的服务在服务注册列表中剔除#
    shouldUseReadOnlyResponseCache: true #eureka是CAP理论种基于AP策略，为了保证强一致性关闭此切换CP 默认不关闭 false关闭
  client:
    register-with-eureka: false  #false:不作为一个客户端注册到注册中心
    fetch-registry: false      #为true时，可以启动，但报异常：Cannot execute request on any known server
    instance-info-replication-interval-seconds: 10
    serviceUrl:
      defaultZone: http://localhost:${server.port}/eureka/
  instance:
    hostname: ${spring.cloud.client.ip-address}
    prefer-ip-address: true
    instance-id: ${spring.application.name}:${spring.cloud.client.ip-address}:${spring.application.instance_id:${server.port}}
```

然后启动启动类，访问浏览器，[http://127.0.0.1:8765](http://127.0.0.1:8765/)，出现如下页面即表示已经成功

![eureka server.png](Spring%20Security%20+%20OAuth2/VT5JBMOV48IAVM9F2V5CAJON7.png)

### 二、网关搭建

这里选用spring cloud gateway作为网关（不是zuul）

#### 1.添加maven依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
    <!--gateway 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
        <version>2.2.5.RELEASE</version>
    </dependency>
    <!--actuator 依赖 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <!-- jwt依赖 -->
    <dependency>
        <groupId>org.springframework.security</groupId>
        <artifactId>spring-security-jwt</artifactId>
    </dependency>
</dependencies>
```

#### 2.新建启动类

```java
@SpringBootApplication
public class GatewayServer {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServer.class, args);
    }
}
```

#### 3.新建配置文件

```yaml
server:
  port: 8761
spring:
  cloud:
    gateway:
      routes:
        - id: resource_server
          uri: "lb://resource-server"
          predicates:
            - Path=/r**
  application:
    name: gateway-server

eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:8765/eureka
  instance:
    prefer-ip-address: true
    instance-id: ${spring.application.name}:${spring.cloud.client.ip‐address}:${spring.application.instance_id:${server.port}}
```

如此，一个网关就已经搭建好了，但是还不具备我们想要的认证功能。

#### 4.添加token全局过滤器

知识点有以下几点：

- **全局过滤器要实现GlobalFilter接口**
- **为了实现token过滤器最先被调用，要实现Order接口并将优先级调到最大**
- **使用JwtHelper工具类对jwt验签，签名的key必须和认证中心中配置的key保持一致**
- 验签成功后将jwt中payload明文信息放到token-info的header值中传递给目标服务

实现代码如下：

```java
@Component
@Slf4j
public class TokenFilter implements GlobalFilter, Ordered {
    private static final String BEAR_HEADER = "Bearer ";
    /**
     * 该值要和auth-server中配置的签名相同
     *
     * com.kdyzm.spring.security.auth.center.config.TokenConfig#SIGNING_KEY
     */
    private static final String SIGNING_KEY = "auth123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        //如果没有token，则直接返回401
        if(StringUtils.isEmpty(token)){
            return unAuthorized(exchange);
        }
        //验签并获取PayLoad
        String payLoad;
        try {
            Jwt jwt = JwtHelper.decodeAndVerify(token.replace(BEAR_HEADER,""), new MacSigner(SIGNING_KEY));
            payLoad = jwt.getClaims();
        } catch (Exception e) {
            log.error("验签失败",e);
            return unAuthorized(exchange);
        }
        //将PayLoad数据放到header
        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        builder.header("token-info", payLoad).build();
        //继续执行
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    private Mono<Void> unAuthorized(ServerWebExchange exchange){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    /**
     * 将该过滤器的优先级设置为最高，因为只要认证不通过，就不能做任何事情
     *
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
```

### 三、资源服务修改

原来资源服务已经集成了OAuth2.0、Spring Security、JWT等组件，根据现在的设计方案，需要删除OAuth2.0和JWT组件，只留下Spring Security组件。

#### 1.移除OAuth2.0、JWT组件

这里要删除maven依赖，同时将相关配置删除

**第一步，删除maven依赖**，直接将以下两个依赖移除就好

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-jwt</artifactId>
</dependency>
```

**第二步，删除相关配置**

将ResouceServerConfig、TokenConfig两个类直接删除 即可。

#### 2.添加过滤器

这里需要使用过滤器做，首先写一个过滤器，**实现OncePerRequestFilter接口，该过滤器的作用就是获取网关传过来的token-info明文数据，封装成JwtTokenInfo对象，并将该相关信息添加到SpringSecurity上下文以备之后的鉴权使用。**

代码实现如下：

```java
@Component
@Slf4j
public class AuthFilterCustom extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String tokenInfo=request.getHeader("token-info");
        if(StringUtils.isEmpty(tokenInfo)){
            log.info("未找到token信息");
            filterChain.doFilter(request,response);
            return;
        }
        JwtTokenInfo jwtTokenInfo = objectMapper.readValue(tokenInfo, JwtTokenInfo.class);
        log.info("tokenInfo={}",objectMapper.writeValueAsString(jwtTokenInfo));
        List<String> authorities1 = jwtTokenInfo.getAuthorities();
        String[] authorities=new String[authorities1.size()];
        authorities1.toArray(authorities);
        //将用户信息和权限填充 到用户身份token对象中
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(jwtTokenInfo.getUser_name(),null, AuthorityUtils.createAuthorityList(authorities));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //将authenticationToken填充到安全上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
```

#### 3.将过滤器注册到过滤器链

修改WebSecurityConfig类，使用如下方法注册过滤器：

```java
.addFilterAfter(authFilterCustom, BasicAuthenticationFilter.class)//添加过滤器
```

同时，一定要关闭session功能，否则会出现上下文缓存问题

```java
.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);//禁用session
```

完整代码如下：

```java
    @Autowired
    private AuthFilterCustom authFilterCustom;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
//                .antMatchers("/r/r1").hasAuthority("p2")
//                .antMatchers("/r/r2").hasAuthority("p2")
                .antMatchers("/**").authenticated()//所有的请求必须认证通过
                .anyRequest().permitAll()//其它所有请求都可以随意访问
                .and()
                .addFilterAfter(authFilterCustom, BasicAuthenticationFilter.class)//添加过滤器
        .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);//禁用session

    }
```

### 四、其他注意事项

认证服务auth-server以及资源服务resource-server、网关服务gateway-server都要集成eureka client组件

### 五、测试

测试前需要将各个服务依次启动起来：

- 启动注册中心 register-server：https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v5.0.0/register-server
- 启动网关 gateway-server：https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v5.0.0/gateway-server
- 启动认证服务 auth-server：https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v5.0.0/auth-server
- 启动资源服务 resource-server：https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v5.0.0/resource-server

**第一步，获取token**

这里使用password模式直接获取token，POST请求如下接口：

http://127.0.0.1:30000/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=zhangsan&password=123

即可获取token。

**第二步，访问资源**

通过网关请求资源服务的r1接口，GET请求如下接口：

http://127.0.0.1:8761/r1

需要带上Header，key为`Authorization`,value格式如下：

```text
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX25hbWUiOiJ6aGFuZ3NhbiIsInNjb3BlIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiLCJST0xFX0FQSSJdLCJleHAiOjE2MTAzNzI5MzUsImF1dGhvcml0aWVzIjpbInAxIiwicDIiXSwianRpIjoiOWQzMzRmZGMtOTcwZC00YmJkLWI2MmMtZDU4MDZkNTgzM2YwIiwiY2xpZW50X2lkIjoiYzEifQ.gZraRNeX-o_jKiH7XQgg3TlUQBpxUcXa2-qR_Treu8U
```

如果相应结果如下，则表示测试通过

```
访问资源r1
```

否则，会返回401状态码。

### 六、项目源代码

项目源代码： https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v5.0.0/

参考文章：

https://www.baeldung.com/spring-security-custom-filter

https://stackoverflow.com/questions/44951041/spring-security-when-to-clear-securitycontextholder

https://blog.csdn.net/jxchallenger/article/details/86760245

## 认证授权五：用户信息扩展到jwt

上一篇文章讲解了如何在分布式系统环境下进行认证和鉴权，总体来说就是网关认证，目标服务鉴权，但是存在着一个问题：关于用户信息，目标服务只能获取到网关转发过来的username信息，为啥呢，因为认证服务颁发jwt令牌的时候就只存放了这么多信息，我们到[jwt.io网站](https://jwt.io/)上贴出jwt令牌查看下payload中内容就就知道有什么内容了：

![jwt base64 decode结果](Spring%20Security%20+%20OAuth2/2B8J8S7BH0UB15MRJNP45M1G6J-1682069430734.png)

本篇文章的目的就是为了解决该问题，把用户信息（用户名、头像、手机号、邮箱等）放到jwt token中，经过网关解析之后携带用户信息访问目标服务，目标服务将用户信息保存到上下文并保证线程安全性的情况下封装成工具类提供给各种环境下使用。

注：本文章基于源代码https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v5.0.0/分析和改造。

### 一、实现UserDetailsService接口

#### 1.问题分析和修改

jwt令牌中用户信息过于少的原因在于认证服务auth-server中[com.kdyzm.spring.security.auth.center.service.MyUserDetailsServiceImpl#loadUserByUsername](https://gitee.com/kdyzm/spring-security-oauth-study/blob/v5.0.0/auth-server/src/main/java/com/kdyzm/spring/security/auth/center/service/MyUserDetailsServiceImpl.java#L44) 方法中的这段代码

```java
return User
                .withUsername(tUser.getUsername())
                .password(tUser.getPassword())
                .authorities(array).build();
```

这里User类实现了`UserDetailsService`接口，并使用建造者模式生成了需要的`UserDetailsService`对象，可以看到生成该对象仅仅传了三个参数，而用户信息仅仅有用户名和password两个参数———那么如何扩展用户信息就一目了然了，我们自己也实现`UserDetailsService`接口然后返回改值不就好了吗？不好！！实现`UserDetailsService`接口要实现它需要的好几个方法，**不如直接继承User类，在改动最小的情况下保持原有的功能基本不变，这里定义`UserDetailsExpand`继承`User`类**

```java
public class UserDetailsExpand extends User {
    public UserDetailsExpand(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    //userId
    private Integer id;
    //电子邮箱
    private String email;
    //手机号
    private String mobile;
    private String fullname;
    //Getter/Setter方法略
}
```

之后，修改[`com.kdyzm.spring.security.auth.center.service.MyUserDetailsServiceImpl#loadUserByUsername`](https://gitee.com/kdyzm/spring-security-oauth-study/blob/v5.0.0/auth-server/src/main/java/com/kdyzm/spring/security/auth/center/service/MyUserDetailsServiceImpl.java#L29)方法返回该类的对象即可

```java
        UserDetailsExpand userDetailsExpand = new UserDetailsExpand(tUser.getUsername(), tUser.getPassword(), AuthorityUtils.createAuthorityList(array));
        userDetailsExpand.setId(tUser.getId());
        userDetailsExpand.setMobile(tUser.getMobile());
        userDetailsExpand.setFullname(tUser.getFullname());
        return userDetailsExpand;
```

#### 2.测试修改和源码分析

修改了以上代码之后我们启动服务，获取jwt token之后查看其中的内容，会发现用户信息并没有填充进去，测试失败。。。。再分析下，为什么会没有填充进去？关键在于`JwtAccessTokenConverter`这个类，该类未发起作用的时候，返回请求放的token只是一个uuid类型（好像是uuid）的简单字符串，经过该类的转换之后就将一个简单的uuid转换成了jwt字符串，该类中的`org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter#convertAccessToken`方法在起作用，顺着该方法找下去：`org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter#convertAccessToken`，然后就发现了这行代码

```java
response.putAll(token.getAdditionalInformation());
```

这个token就是`OAuth2AccessToken`对象，也就是真正返回给请求者的对象，查看该类中该字段的解释

```java
/**
     * The additionalInformation map is used by the token serializers to export any fields used by extensions of OAuth.
     * @return a map from the field name in the serialized token to the value to be exported. The default serializers 
     * make use of Jackson's automatic JSON mapping for Java objects (for the Token Endpoint flows) or implicitly call 
     * .toString() on the "value" object (for the implicit flow) as part of the serialization process.
     */
    Map<String, Object> getAdditionalInformation();
```

可以看到，该字段是专门用来扩展OAuth字段的属性，万万没想到JWT同时用它扩展jwt串。。。接下来就该想想怎么给`OAuth2AccessToken`对象填充这个扩展字段了。

如果仔细看`JwtAccessTokenConverter`这个类的源码，可以看到有个方法`org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter#enhance`，该方法有个参数`OAuth2AccessToken accessToken`，同时它的返回值也是`OAuth2AccessToken`，也就是说这个方法，传入了`OAuth2AccessToken`对象，完事儿了之后还传出了`OAuth2AccessToken`对象，再根据`enhance`这个名字，可以推测出，它是一个增强方法，修改了或者代理了`OAuth2AccessToken`对象，查看父接口，是`TokenEnhancer`接口

```java
public interface TokenEnhancer {
    /**
     * Provides an opportunity for customization of an access token (e.g. through its additional information map) during
     * the process of creating a new token for use by a client.
     * 
     * @param accessToken the current access token with its expiration and refresh token
     * @param authentication the current authentication including client and user details
     * @return a new token enhanced with additional information
     */
    OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication);
}
```

根据该注释可以看出该方法用于定制access_token，那么通过这个方法填充access token的AdditionalInformation属性貌似正合适（别忘了目的是干啥的）。

看下`JwtAccessTokenConverter`是如何集成到认证服务的

```java
    @Bean
    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService);
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore);
        services.setAccessTokenValiditySeconds(7200);
        services.setRefreshTokenValiditySeconds(259200);

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(jwtAccessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);
        return services;
    }
```

可以看到这里的`tokenEnhancerChain`可以传递一个列表，这里只传了一个`jwtAccessTokenConverter`对象，那么解决方案就有了，实现TokenEnhancer接口并将对象填到该列表中就可以了

#### 3.实现TokenEnhancer接口

```java
@Slf4j
@Component
public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String,Object> additionalInfo = new HashMap<>();
        Object principal = authentication.getPrincipal();
        try {
            String s = objectMapper.writeValueAsString(principal);
            Map map = objectMapper.readValue(s, Map.class);
            map.remove("password");
            map.remove("authorities");
            map.remove("accountNonExpired");
            map.remove("accountNonLocked");
            map.remove("credentialsNonExpired");
            map.remove("enabled");
            additionalInfo.put("user_info",map);
            ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(additionalInfo);
        } catch (IOException e) {
            log.error("",e);
        }
        return accessToken;
    }
}
```

以上代码干了以下几件事儿：

- 从OAuth2Authentication对象取出principal对象
- 转换principal对象为map并删除map对象中的若干个不想要的字段属性
- 将map对象填充进入OAuth2AccessToken对象的additionalInfo属性

实现TokenEnhancer接口后将该对象加入到TokenEnhancerChain中

```java
TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer,jwtAccessTokenConverter));
```

#### 4.接口测试

POST请求http://127.0.0.1:30000/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=zhangsan&password=123得到结果

```json
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX2luZm8iOnsidXNlcm5hbWUiOiJ6aGFuZ3NhbiIsImlkIjoxLCJlbWFpbCI6IjEyMzQ1NkBmb3htYWlsLmNvbSIsIm1vYmlsZSI6IjEyMzQ1Njc4OTEyIiwiZnVsbG5hbWUiOiLlvKDkuIkifSwidXNlcl9uYW1lIjoiemhhbmdzYW4iLCJzY29wZSI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIiwiUk9MRV9BUEkiXSwiZXhwIjoxNjEwNjM4NjQzLCJhdXRob3JpdGllcyI6WyJwMSIsInAyIl0sImp0aSI6IjFkOGY3OGFmLTg1N2EtNGUzMS05ODYxLTZkYWJjNjU4NzcyNiIsImNsaWVudF9pZCI6ImMxIn0.Y9f5psNCgZi_I2KY3PLBLjuK5-U1VhXIB1vjKjMb9fc",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX2luZm8iOnsidXNlcm5hbWUiOiJ6aGFuZ3NhbiIsImlkIjoxLCJlbWFpbCI6IjEyMzQ1NkBmb3htYWlsLmNvbSIsIm1vYmlsZSI6IjEyMzQ1Njc4OTEyIiwiZnVsbG5hbWUiOiLlvKDkuIkifSwidXNlcl9uYW1lIjoiemhhbmdzYW4iLCJzY29wZSI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIiwiUk9MRV9BUEkiXSwiYXRpIjoiMWQ4Zjc4YWYtODU3YS00ZTMxLTk4NjEtNmRhYmM2NTg3NzI2IiwiZXhwIjoxNjEwODkwNjQzLCJhdXRob3JpdGllcyI6WyJwMSIsInAyIl0sImp0aSI6IjM1OGFkMzA1LTU5NzUtNGM3MS05ODI4LWQ2N2ZjN2MwNDMyMCIsImNsaWVudF9pZCI6ImMxIn0._bhajMIdqnUL1zgc8d-5xlXSzhsCWbZ2jBWlNb8m_hw",
    "expires_in": 7199,
    "scope": "ROLE_ADMIN ROLE_USER ROLE_API",
    "user_info": {
        "username": "zhangsan",
        "id": 1,
        "email": "123456@foxmail.com",
        "mobile": "12345678912",
        "fullname": "张三"
    },
    "jti": "1d8f78af-857a-4e31-9861-6dabc6587726"
}
```

可以看到结果中多了user_info字段，而且access_token长了很多，我们的目的是为了在jwt也就是access_token中放入用户信息，先不管为何user_info会以明文出现在这里，我们先看下access_token中多了哪些内容

POST请求hhttp://127.0.0.1:30000/oauth/check_token?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzMSJdLCJ1c2VyX2luZm8iOnsidXNlcm5hbWUiOiJ6aGFuZ3NhbiIsImlkIjoxLCJlbWFpbCI6IjEyMzQ1NkBmb3htYWlsLmNvbSIsIm1vYmlsZSI6IjEyMzQ1Njc4OTEyIiwiZnVsbG5hbWUiOiLlvKDkuIkifSwidXNlcl9uYW1lIjoiemhhbmdzYW4iLCJzY29wZSI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIiwiUk9MRV9BUEkiXSwiZXhwIjoxNjEwNjM4NjQzLCJhdXRob3JpdGllcyI6WyJwMSIsInAyIl0sImp0aSI6IjFkOGY3OGFmLTg1N2EtNGUzMS05ODYxLTZkYWJjNjU4NzcyNiIsImNsaWVudF9pZCI6ImMxIn0.Y9f5psNCgZi_I2KY3PLBLjuK5-U1VhXIB1vjKjMb9fc，得到相应结果

```json
{
    "aud": [
        "res1"
    ],
    "user_info": {
        "username": "zhangsan",
        "id": 1,
        "email": "123456@foxmail.com",
        "mobile": "12345678912",
        "fullname": "张三"
    },
    "user_name": "zhangsan",
    "scope": [
        "ROLE_ADMIN",
        "ROLE_USER",
        "ROLE_API"
    ],
    "exp": 1610638643,
    "authorities": [
        "p1",
        "p2"
    ],
    "jti": "1d8f78af-857a-4e31-9861-6dabc6587726",
    "client_id": "c1"
}
```

可以看到user_info也已经填充到了jwt串中，那么为什么这个串还会以明文的形式出现在相应结果的其它字段中呢？还记得本文章中说过的一句话`"可以看到，该字段是专门用来扩展OAuth字段的属性，万万没想到JWT同时用它扩展jwt串"`，我们给`OAuth2AccessToken`对象填充了`AdditionalInformation`字段，而这本来是为了扩展OAuth用的，所以返回结果中自然会出现这个字段。

到此为止，接口测试已经成功了，接下来修改网关和目标服务（这里是资源服务），将用户信息提取出来并保存到上下文中

### 二、修改网关

网关其实不需要做啥大的修改，但是会出现中文乱码问题，这里使用Base64编码之后再将用户数据放到请求头带给目标服务。修改TokenFilter类

```java
//builder.header("token-info", payLoad).build();
builder.header("token-info", Base64.encode(payLoad.getBytes(StandardCharsets.UTF_8))).build();
```

### 三、修改资源服务

#### 1.修改AuthFilterCustom

上一篇文章中床架了该类并将userName填充到了UsernamePasswordAuthenticationToken对象的Principal，这里我们需要将扩展的UserInfo整个填充到Principal，完整代码如下

```java
public class AuthFilterCustom extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String base64Token = request.getHeader("token-info");
        if(StringUtils.isEmpty(base64Token)){
            log.info("未找到token信息");
            filterChain.doFilter(request,response);
            return;
        }
        byte[] decode = Base64.decode(base64Token);
        String tokenInfo = new String(decode, StandardCharsets.UTF_8);
        JwtTokenInfo jwtTokenInfo = objectMapper.readValue(tokenInfo, JwtTokenInfo.class);
        List<String> authorities1 = jwtTokenInfo.getAuthorities();
        String[] authorities=new String[authorities1.size()];
        authorities1.toArray(authorities);
        //将用户信息和权限填充 到用户身份token对象中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        jwtTokenInfo.getUser_info(),
                null,
                AuthorityUtils.createAuthorityList(authorities)
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //将authenticationToken填充到安全上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
```

这里JwtTokenInfo新增了user_info字段，而其类型正是前面说的`UserDetailsExpand`类型。

通过上述修改，我们可以在Controller中使用如下代码获取到上下文中的信息

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
UserDetailsExpand principal = (UserDetailsExpand)authentication.getPrincipal();
```

经过测试，结果良好，但是还存在问题，那就是在异步情况下，比如使用线程池或者新开线程的情况下，极有可能出现线程池内缓存或者取不到数据的情况（未测试，瞎猜的），具体可以参考我以前的文章[`使用 transmittable-thread-local 组件解决 ThreadLocal 父子线程数据传递问题`](https://blog.kdyzm.cn/post/14)

#### 2.解决线程安全性问题

这一步是选做，但是还是建议做，如果不考虑线程安全性问题，上一步就可以了。

首先新增AuthContextHolder类维护我们需要的ThreadLocal，这里一定要使用TransmittableThreadLocal。

```java
public class AuthContextHolder {
    private TransmittableThreadLocal threadLocal = new TransmittableThreadLocal();
    private static final AuthContextHolder instance = new AuthContextHolder();

    private AuthContextHolder() {
    }

    public static AuthContextHolder getInstance() {
        return instance;
    }

    public void setContext(UserDetailsExpand t) {
        this.threadLocal.set(t);
    }

    public UserDetailsExpand getContext() {
        return (UserDetailsExpand)this.threadLocal.get();
    }

    public void clear() {
        this.threadLocal.remove();
    }
}
```

然后新建拦截器AuthContextIntercepter

```java
@Component
public class AuthContextIntercepter implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.isNull(authentication) || Objects.isNull(authentication.getPrincipal())){
            //无上下文信息，直接放行
            return true;
        }
        UserDetailsExpand principal = (UserDetailsExpand) authentication.getPrincipal();
        AuthContextHolder.getInstance().setContext(principal);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContextHolder.getInstance().clear();
    }
}
```

该拦截器在AuthFilter之后执行的，所以一定能获取到SecurityContextHolder中的内容，之后，我们就可以在Controller中使用如下代码获取用户信息了

```java
UserDetailsExpand context = AuthContextHolder.getInstance().getContext();
```

是不是简单了很多~

#### 3.其他问题

如果走到了上一步，则一定要使用阿里巴巴配套的TransmittableThreadLocal解决方案，否则TransmittableThreadLocal和普通的ThreadLocal没什么区别。具体参考[`使用 transmittable-thread-local 组件解决 ThreadLocal 父子线程数据传递问题`](https://blog.kdyzm.cn/post/14)

### 四、源代码

https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v6.0.0/

## 认证授权六：前后端分离下的登录授权

本篇文章将会解决上一篇文章[《Spring Security OAuth2.0认证授权五：用户信息扩展到jwt 》](http://114.115.170.237/2023/02/21/spring-security-oauth2/#%E8%AE%A4%E8%AF%81%E6%8E%88%E6%9D%83%E4%BA%94%EF%BC%9A%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF%E6%89%A9%E5%B1%95%E5%88%B0jwt)中遗留的问题，并在原有的项目中新增模块`business-server`用来充当前端页面的web容器并转发登录请求和更换token的请求等，以模拟前后端分离下的登录以及更换token操作。

### 一、jwt令牌在网关处的过期时间校验

上一篇文章中讲了在网关处解析token并转发到目标服务的操作，因为使用了jwt令牌的原因，所以省了一步到认证服务器认证的操作，只要验签成功，就认为令牌有效。这实际上留下了一个bug：服务端无法主动取消jwt令牌，所以这个令牌只要客户端保存下来，如果不调用认证服务器的令牌验证接口，这个jwt令牌将永远有效。因此需要在网关处加上对过期时间的校验。

在TokenFilter中添加以下代码逻辑

```java
//取出exp字段，判断token是否已经过期
try {
    Map<String, Object> map = objectMapper.readValue(payLoad, new TypeReference<Map<String, Object>>() {
    });
    long expiration = ((Integer) map.get("exp")) * 1000L;
    if (expiration < new Date().getTime()) {
        return unAuthorized(exchange, "未认证的请求：token存在，但是已经失效",WrapperResult.TOKEN_EXPIRE);
    }
} catch (IOException e) {
    log.error("", e);
    return unAuthorized(exchange, "未认证的请求：错误的token",null);
}
```

### 二、refresh-token接口缺少用户信息

refresh-token在access_token过期，但是refresh-token未过期的时候使用，目的是使用refresh_token更新已经过期的access_token，这样理论上来说，客户端只要能在refresh_token过期之前进行任意操作，就可以避免重新登录了。

上一篇文章中将用户信息放到了jwt token中并返回给客户端，但是如果使用refresh_token更新token，后端会报错，前端取到的token中则缺少了用户信息。究其原因，和`JwtAccessTokenConverter`有关系，关于这个类的实例，当初创建的方法如下

```java
@Bean
public JwtAccessTokenConverter accessTokenConverter(){
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);//对称秘钥，资源服务器使用该秘钥来验证
    return jwtAccessTokenConverter;
}
```

这里的`new`操作省了很多默认参数的指定，且先看下为啥会缺少用户信息，扩展用户信息的关键在于方法`com.kdyzm.spring.security.auth.center.service.MyUserDetailsServiceImpl#loadUserByUsername`，这里扩展了用户信息，使其从单纯的username字符串变成了`UserDetailsExpand`对象，然后在增强方法`com.kdyzm.spring.security.auth.center.enhancer.CustomTokenEnhancer#enhance`中将扩展信息取出来放到Token中。

经过debug，发现

![2021-01-29_162556.jpg](Spring%20Security%20+%20OAuth2/2RSFDFATQ1M619CP8NEJIBD7KJ.jpg)

最终发现是如下代码的问题`org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter#extractAuthentication`

```java
public Authentication extractAuthentication(Map<String, ?> map) {
    if (map.containsKey(USERNAME)) {
        Object principal = map.get(USERNAME);
        Collection<? extends GrantedAuthority> authorities = getAuthorities(map);
        //运行到这里的时候userDetailsService为空，所以并没有执行自定义的loadUserByUsername方法
        if (userDetailsService != null) {
            UserDetails user = userDetailsService.loadUserByUsername((String) map.get(USERNAME));
            authorities = user.getAuthorities();
            principal = user;
        }
        return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
    }
    return null;
}
```

层层网上追寻调用链，竟然是`JwtAccessTokenConverter`创建的时候省略参数导致的，只需要如此做就可以解决问题了

```java
@Bean
public JwtAccessTokenConverter accessTokenConverter(){
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
    DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
    userTokenConverter.setUserDetailsService(userDetailsService);
    tokenConverter.setUserTokenConverter(userTokenConverter);
    jwtAccessTokenConverter.setAccessTokenConverter(tokenConverter);
    jwtAccessTokenConverter.setSigningKey(SIGNING_KEY);//对称秘钥，资源服务器使用该秘钥来验证
    return jwtAccessTokenConverter;
}
```

JwtAccessTokenConverter对象创建的时候指定DefaultUserAuthenticationConverter使用的userDetailsService即可。

### 三、新建business-server模块作为web容器

这里新建的business-server模块有两个功能

1. 充当web容器，该服务并没有使用模板化技术，使用的是纯html、css实现前端
2. 转发前端登录、更换token请求

可能会有人对第二条有疑问，为什么要这么做？之前测试的时候基本上都是使用postman发起的请求，请求的方式是这样的`http://127.0.0.1:30000/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=zhangsan&password=123`可以看到这里传递了很重要的参数`client_id`和`client_secret`，这两个参数无论如何也不应当泄露给前端，通常都是中间的真正的客户端服务拼接这两个参数再将请求转发给认证服务

### 四、前后端分离

设计上想要实现以下功能

1. 首页未登录则提示用户登录，已经登录则展示用户个人信息
2. 用户登录之后将令牌保存到localStorage
3. token过期之后用户可以选择使用refresh_token更换已经过期的令牌（access_token）
4. 已经过期的refresh_token不能用于更换新的令牌

#### 1、关闭认证服务表单登录

以前请求认证服务的任意接口，如果没有认证，则都会跳转到系统自带的登录页面，现在我们想要实现前后端分离了，原来系统自带的登录页面就有些碍眼了，直接关闭就好。关闭方法如下，spring security的配置更改为如下：

```java
                .formLogin()
                .disable();
```

#### 2、前后端代码

前端代码在`business-server/src/main/resources/static`目录下，只有两个页面，一个首页，一个登陆页面

后端只有两个接口

- 登录接口：com.kdyzm.spring.security.oauth.study.business.server.controller.LoginController#login

  ```java
      @PostMapping("/login")
      public WrapperResult<String> login(@RequestBody LoginReq loginReq) throws JsonProcessingException {
          log.info("收到登陆请求:{}", objectMapper.writeValueAsString(loginReq));
          String reqUrl = "http://auth-server/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=" + loginReq.getUsername() + "&password=" + loginReq.getPassword();
          ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(reqUrl, null, String.class);
          log.info(stringResponseEntity.getBody());
          return WrapperResult.success(stringResponseEntity.getBody());
      }
  ```

- 更新token接口：com.kdyzm.spring.security.oauth.study.business.server.controller.TokenController#refreshToken

  ```java
      @PostMapping("/refresh-token")
      public WrapperResult<String> refreshToken(@RequestParam("refreshToken") String refreshToken) {
          String reqUrl = "http://auth-server/oauth/token?grant_type=refresh_token&refresh_token=" + refreshToken + "&client_id=c1&client_secret=secret";
          ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(reqUrl, null, String.class);
          log.info(stringResponseEntity.getBody());
          return WrapperResult.success(stringResponseEntity.getBody());
      }
  ```

其它不做赘述，不过前端页面写起来挺麻烦的。。难是不难的

### 五、测试

源代码：

测试前首先需要重新执行初始化sql（auth-server/docs/sql/init.sql），然后依次启动` register-server`、`gateway-server`、`auth-server`、`resource-server`、`business-server` 五个服务

启动成功后打开浏览器，输入`http://127.0.0.1:30002/`地址，就会看到以下页面![2021-01-29_171442.jpg](Spring%20Security%20+%20OAuth2/37O3G2E10TALEFAS3ALF00230B.jpg)点击登录之后，出现登录框![2021-01-29_171532.jpg](Spring%20Security%20+%20OAuth2/2PGMT3VTG1OESBLN16OFMLS5EL.jpg)输入账号密码之后，登录成功之后会跳转首页，就会看到个人信息![2021-01-29_171754.jpg](Spring%20Security%20+%20OAuth2/28U9O80NNJSPPSMJCD5TLOB3S6.jpg)

这里设置的token有效期为10秒，所以很快token就会失效，十秒钟之后刷新页面就会有新的提示![2021-01-29_171858.jpg](Spring%20Security%20+%20OAuth2/3295DDR52RTT4MS0ST2LEUJ33E.jpg)接下来可以有两种选择，一种是使用refresh-token更新失效的令牌，另外一种是重新登录，这里refresh_token的有效期也很短，只有30秒，如果超出30秒，则会更新失败，提示如下![2021-01-29_172049.jpg](Spring%20Security%20+%20OAuth2/1LSKQARTFUQ5APGU0GADACQLHP.jpg)而如果在30秒内刷新令牌，则会重新获取到令牌并刷新当前页

### 六、源代码地址

https://gitee.com/SuperLz0418/spring-security-oauth-study/tree/v7.0.0/

## Spring Boot+OAuth2，一个注解搞定单点登录！

好了，开始今天的正文。单点登录是我们在分布式系统中很常见的一个需求。分布式系统由多个不同的子系统组成，而我们在使用系统的时候，只需要登录一次即可，这样其他系统都认为用户已经登录了，不用再去登录。前面和小伙伴们分享了 OAuth2+JWT 的登录方式，这种无状态登录实际上天然的满足单点登录的需求，可以参考：[想让 OAuth2 和 JWT 在一起愉快玩耍？请看松哥的表演](https://mp.weixin.qq.com/s?__biz=MzI1NDY0MTkzNQ==&mid=2247488267&idx=2&sn=0ac88e1685ef0915e71eb3c223bd732f&scene=21#wechat_redirect)。当然大家也都知道，无状态登录也是有弊端的。所以今天松哥想和大家说一说 Spring Boot+OAuth2 做单点登录，利用 \@EnableOAuth2Sso 注解快速实现单点登录功能。松哥依然建议大家在阅读本文时，先看看本系列前面的文章，这有助于更好的理解本文。

### 1.项目创建

前面的案例中，松哥一直都把授权服务器和资源服务器分开创建，今天这个案例，为了省事，我就把授权服务器和资源服务器搭建在一起（不过相信大家看了前面的文章，应该也能自己把这两个服务器拆分开）。所以，今天我们一共需要三个服务：

| 项目        | 端口 | 描述                  |
| :---------- | :--- | :-------------------- |
| auth-server | 1111 | 授权服务器+资源服务器 |
| client1     | 1112 | 子系统1               |
| client2     | 1113 | 子系统2               |

auth-server 用来扮演授权服务器+资源服务器的角色，client1 和 client2 则分别扮演子系统的角色，将来等 client1 登录成功之后，我们也就能访问 client2 了，这样就能看出来单点登录的效果。我们创建一个名为 oauth2-sso 的 Maven 项目作为父工程即可。

### 2.统一认证中心

接下来我们来搭建统一认证中心。首先我们创建一个名为 auth-server 的 module，创建时添加如下依赖：

![img](Spring%20Security%20+%20OAuth2/oauth-6-1.png)

项目创建成功之后，这个模块由于要扮演授权服务器+资源服务器的角色，所以我们先在这个项目的启动类上添加 @EnableResourceServer 注解，表示这是一个资源服务器：

```java
@SpringBootApplication
@EnableResourceServer
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
```

接下来我们进行授权服务器的配置，由于资源服务器和授权服务器合并在一起，因此授权服务器的配置要省事很多：

```java
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("javaboy")
                .secret(passwordEncoder.encode("123"))
                .autoApprove(true)
                .redirectUris("http://localhost:1112/login", "http://localhost:1113/login")
                .scopes("user")
                .accessTokenValiditySeconds(7200)
                .authorizedGrantTypes("authorization_code");

    }
}
```

这里我们只需要简单配置一下客户端的信息即可，这里的配置很简单，前面的文章也讲过了，大家要是不懂，可以参考本系列前面的文章：[这个案例写出来，还怕跟面试官扯不明白 OAuth2 登录流程？](https://mp.weixin.qq.com/s?__biz=MzI1NDY0MTkzNQ==&mid=2247488214&idx=1&sn=5601775213285217913c92768d415eca&scene=21#wechat_redirect)。当然这里为了简便，客户端的信息配置是基于内存的，如果大家想将客户端信息存入数据库中，也是可以的，参考：[OAuth2 令牌还能存入 Redis ？越玩越溜！](https://mp.weixin.qq.com/s?__biz=MzI1NDY0MTkzNQ==&mid=2247488246&idx=2&sn=b1ee410dbe86f2b748845304f7734d62&scene=21#wechat_redirect)接下来我们再来配置 Spring Security：

```java
@Configuration
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login.html", "/css/**", "/js/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers("/login")
                .antMatchers("/oauth/authorize")
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("sang")
                .password(passwordEncoder().encode("123"))
                .roles("admin");
    }
}
```

关于 Spring Security 的配置，如果小伙伴们不懂，可以看看松哥最近正在连载的 Spring Security 系列。我这里来大致捋一下：

1.  首先提供一个 BCryptPasswordEncoder 的实例，用来做密码加解密用。
2.  由于我自定义了登录页面，所以在 WebSecurity 中对这些静态资源方形。
3.  HttpSecurity 中，我们对认证相关的端点放行，同时配置一下登录页面和登录接口。
4.  AuthenticationManagerBuilder 中提供一个基于内存的用户（小伙伴们可以根据 Spring Security 系列第 7 篇文章自行调整为从数据库加载）。
5.  另外还有一个比较关键的地方，因为资源服务器和授权服务器在一起，所以我们需要一个 \@Order 注解来提升 Spring Security 配置的优先级。

SecurityConfig 和 AuthServerConfig 都是授权服务器需要提供的东西（如果小伙伴们想将授权服务器和资源服务器拆分，请留意这句话），接下来，我们还需要提供一个暴露用户信息的接口（如果将授权服务器和资源服务器分开，这个接口将由资源服务器提供）：

```java
@RestController
public class UserController {
    @GetMapping("/user")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }
}
```

最后，我们在 application.properties 中配置一下项目端口：

```properties 
server.port=1111
```

另外，松哥自己提前准备了一个登录页面，如下：

![图片](Spring%20Security%20+%20OAuth2/640.png)

将登录页面相关的 html、css、js 等拷贝到 resources/static 目录下：

![img](Spring%20Security%20+%20OAuth2/oauth-6-3.png)

这个页面很简单，就是一个登录表单而已，我把核心部分列出来：

```html
<form action="/login" method="post">
    <div class="input">
        <label for="name">用户名</label>
        <input type="text" name="username" id="name">
        <span class="spin"></span>
    </div>
    <div class="input">
        <label for="pass">密码</label>
        <input type="password" name="password" id="pass">
        <span class="spin"></span>
    </div>
    <div class="button login">
        <button type="submit">
            <span>登录</span>
            <i class="fa fa-check"></i>
        </button>
    </div>
</form>
```

注意一下 action 提交地址不要写错即可。**「文末可以下载源码。」**如此之后，我们的统一认证登录平台就算是 OK 了。

### 3.客户端创建

接下来我们来创建一个客户端项目，创建一个名为 client1 的 Spring Boot 项目，添加如下依赖：

![img](Spring%20Security%20+%20OAuth2/oauth-6-1-1700621962349.png)

项目创建成功之后，我们来配置一下 Spring Security：

```java
@Configuration
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().and().csrf().disable();
    }
}
```

这段配置很简单，就是说我们 client1 中所有的接口都需要认证之后才能访问，另外添加一个 @EnableOAuth2Sso 注解来开启单点登录功能。接下来我们在 client1 中再来提供一个测试接口：

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName() + Arrays.toString(authentication.getAuthorities().toArray());
    }
}
```

这个测试接口返回当前登录用户的姓名和角色信息。接下来我们需要在 client1 的 application.properties 中配置 oauth2 的相关信息：

```properties 
security.oauth2.client.client-secret=123
security.oauth2.client.client-id=javaboy
security.oauth2.client.user-authorization-uri=http://localhost:1111/oauth/authorize
security.oauth2.client.access-token-uri=http://localhost:1111/oauth/token
security.oauth2.resource.user-info-uri=http://localhost:1111/user

server.port=1112

server.servlet.session.cookie.name=s1
```

这里的配置也比较熟悉，我们来看一下：

1.  client-secret 是客户端密码。
2.  client-id 是客户端 id。
3.  user-authorization-uri 是用户授权的端点。
4.  access-token-uri 是获取令牌的端点。
5.  user-info-uri 是获取用户信息的接口（从资源服务器上获取）。
6.  最后再配置一下端口，然后给 cookie 取一个名字。

如此之后，我们的 client1 就算是配置完成了。按照相同的方式，我们再来配置一个 client2，client2 和 client1 一模一样，就是 cookie 的名字不同（随意取，不相同即可）。

### 4.测试

接下来，我们分别启动 auth-server、client1 和 client2，首先我们尝试去方式 client1 中的 hello 接口，这个时候会自动跳转到统一认证中心：

![图片](https://mmbiz.qpic.cn/mmbiz_png/GvtDGKK4uYkCibibdVlUNB13d46WyEZnOIAicSzudnbcTuL3xicfwhZpOLXXDnr0eNhnHmibCszH1p75ic4icyv8HwZtQ/640?wx_fmt=png)

然后输入用户名密码进行登录。登录成功之后，会自动跳转回 client1 的 hello 接口，如下：

![img](Spring%20Security%20+%20OAuth2/oauth-6-7.png)

此时我们再去访问 client2 ，发现也不用登录了，直接就可以访问：

![img](Spring%20Security%20+%20OAuth2/oauth-6-8.png)

OK，如此之后，我们的单点登录就成功了。

### 5.流程解析

最后，我再来和小伙伴们把上面代码的一个执行流程捋一捋：

1.  首先我们去访问 client1 的 /hello 接口，但是这个接口是需要登录才能访问的，因此我们的请求被拦截下来，拦截下来之后，系统会给我们重定向到 client1 的 /login 接口，这是让我们去登录。

![img](Spring%20Security%20+%20OAuth2/oauth-6-9.png)

2.  当我们去访问 client1 的登录接口时，由于我们配置了 \@EnableOAuth2Sso 注解，这个操作会再次被拦截下来，单点登录拦截器会根据我们在 application.properties 中的配置，自动发起请求去获取授权码：

![img](Spring%20Security%20+%20OAuth2/oauth-6-10.png)

3.  在第二步发送的请求是请求 auth-server 服务上的东西，这次请求当然也避免不了要先登录，所以再次重定向到 auth-server 的登录页面，也就是大家看到的统一认证中心。
4.  在统一认真中心我们完成登录功能，登录完成之后，会继续执行第二步的请求，这个时候就可以成功获取到授权码了。

![img](Spring%20Security%20+%20OAuth2/oauth-6-11.png)

5.  获取到授权码之后，这个时候会重定向到我们 client1 的 login 页面，但是实际上我们的 client1 其实是没有登录页面的，所以这个操作依然会被拦截，此时拦截到的地址包含有授权码，拿着授权码，在 OAuth2ClientAuthenticationProcessingFilter 类中向 auth-server 发起请求，就能拿到 access\_token 了（参考：[这个案例写出来，还怕跟面试官扯不明白 OAuth2 登录流程？](https://mp.weixin.qq.com/s?__biz=MzI1NDY0MTkzNQ==&mid=2247488214&idx=1&sn=5601775213285217913c92768d415eca&scene=21#wechat_redirect)）。
6.  在第五步拿到 access\_token 之后，接下来在向我们配置的 user-info-uri 地址发送请求，获取登录用户信息，拿到用户信息之后，在 client1 上自己再走一遍 Spring Security 登录流程，这就 OK 了。

OK，本文和小伙伴们聊了一些 SpringBoot +OAuth2 单点登录的问题，完整案例下载地址：https://github.com/lenve/