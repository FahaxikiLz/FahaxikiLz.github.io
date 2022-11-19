---
title: SpringBoot_智慧校园项目
date: 2022-10-27 20:22:51
tags: 
- SpringBoot_智慧校园项目
categories: 
- 练手项目
---

# 技术栈

> - Spring Boot
> - Mybatis Plus
> - 验证码生成工具
> - Swagger2

# 尚硅谷毕设——智慧校园

## 项目搭建

### 导入依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.1.RELEASE</version>
        <relativePath/>
    </parent>
    <groupId>com.lz</groupId>
    <artifactId>zhxy</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>zhxy</name>
    <description>Demo project for Spring Boot</description>
    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--单元测试启动器-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- thymeleaf支持-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>

        <!--  mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.3.1</version>
        </dependency>

        <!-- 简化POJO实体类开发 -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!--mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!--swagger-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.7.0</version>
        </dependency>
        <!--swagger ui-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.7.0</version>
        </dependency>
        <!--swagger2  增强版接口文档-->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-spring-boot-starter</artifactId>
            <version>2.0.4</version>
        </dependency>


        <!--开发者工具-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>

         <!-- json，web-start中好像有-->
<!--        <dependency>-->
<!--            <groupId>com.alibaba</groupId>-->
<!--            <artifactId>fastjson</artifactId>-->
<!--            <version>1.2.29</version>-->
<!--        </dependency>-->

        <!-- JWT生成Token-->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt</artifactId>
            <version>0.7.0</version>
        </dependency>

        <!-- 文件上传 -->
<!--        <dependency>-->
<!--            <groupId>commons-fileupload</groupId>-->
<!--            <artifactId>commons-fileupload</artifactId>-->
<!--            <version>1.4</version>-->
<!--        </dependency>-->
    </dependencies>

    <build>
        <plugins>
            <!--spring boot maven插件 , 可以将项目运行依赖的jar包打到我们的项目中-->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```

### 准备配置文件

```yml
server:
  port: 9001
  
spring:
  #解决SpringBoot2.6.0与swagger冲突问题【原因是在springboot2.6.0中将SpringMVC 默认路径匹配策略从AntPathMatcher 更改为PathPatternParser，导致出错，解决办法是切换回原先的AntPathMatcher】
  mvc:
    pathmatch:
      matching-strategy: ant_path_matcher
  #配置数据源
  datasource:
    #配置数据源类型
    type: com.zaxxer.hikari.HikariDataSource
    #配置数据库连接属性
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/zhxy_db?characterEncoding=utf-8&serverTimezone=GMT%2B8&userSSL=false
    username: root
    password: root
    #mybatis-plus内置连接池
    hikari:
      connection-test-query: SELECT 1
      connection-timeout: 60000
      idle-timeout: 500000
      max-lifetime: 540000
      maximum-pool-size: 12
      minimum-idle: 10
      pool-name: GuliHikariPool
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
  servlet:
    #设置文件上传上限
    multipart:
      max-file-size: 10MB
      max-request-size: 100MB
      
mybatis-plus:
  configuration:
    #添加日志支持
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath*:/mapper/**/*.xml
```

### 准备配置类

#### 分页插件

```java
@Configuration
@MapperScan("com.lz.zhxy.mapper")
public class MyConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
        return paginationInterceptor;
    }
}

```

#### 准备Swagger2的配置类

``` java 
package com.lz.zhxy.util;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置信息
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket webApiConfig() {

        //添加head参数start
        List<Parameter> pars = new ArrayList<>();
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        tokenPar.name("userId")
//                .description("用户ID")
//                .defaultValue("1")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build();
//        pars.add(tokenPar.build());
//
//        ParameterBuilder tmpPar = new ParameterBuilder();
//        tmpPar.name("userTempId")
//                .description("临时用户ID")
//                .defaultValue("1")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build();
//        pars.add(tmpPar.build());
        //添加head参数end

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                //.apis(RequestHandlerSelectors.withClassAnnotation(ApiOperation.class))
                //扫描包下，一般为controller包
                .apis(RequestHandlerSelectors.basePackage("com.lz.zhxy.controller"))
                //过滤掉admin路径下的所有页面
                //.paths(Predicates.and(PathSelectors.regex("/sms/.*")))
                //过滤掉所有error或error.*页面
                //.paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .globalOperationParameters(pars);

    }


    private ApiInfo webApiInfo() {

        return new ApiInfoBuilder()
                .title("网站-API文档")
                .description("本文档描述了网站微服务接口定义")
                .version("1.0")
                .contact(new Contact("atguigu", "http://atguigu.com", "512111559@qq.com"))
                .build();
    }

    private ApiInfo adminApiInfo() {

        return new ApiInfoBuilder()
                .title("后台管理系统-API文档")
                .description("本文档描述了后台管理系统微服务接口定义")
                .version("1.0")
                .contact(new Contact("atguigu", "http://atguigu.com", "512111559@qq.com"))
                .build();
    }


}
```

##### Swagger2使用

```java
@RestController
@RequestMapping("/sms/system")//指定控制器
@Api(tags = "公共控制器")
public class SystemController {
    
	@ApiOperation("登录请求")//指定方法
    @PostMapping("/login")
    
    //指定参数
    public Result login(@ApiParam("登录表单") @RequestBody LoginForm loginForm,
                        HttpSession session) {...}
}
```

#### 验证码图片工具类

``` java
package com.atguigu.zhxy.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @project: ssm_sms
 * @description: 绘制验证码图片
 */
public class CreateVerifiCodeImage {

    private static int WIDTH = 90;
    private static int HEIGHT = 35;
    private static int FONT_SIZE = 20; //字符大小
    private static char[] verifiCode; //验证码
    private static BufferedImage verifiCodeImage; //验证码图片

    /**
     * @description: 获取验证码图片
     * @param: no
     * @return: java.awt.image.BufferedImage
     */
    public static BufferedImage getVerifiCodeImage() {
        verifiCodeImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);// create a image
        Graphics graphics = verifiCodeImage.getGraphics();

        verifiCode = generateCheckCode();
        drawBackground(graphics);
        drawRands(graphics, verifiCode);

        graphics.dispose();

        return verifiCodeImage;
    }

    /**
     * @description: 获取验证码
     * @param: no
     * @return: char[]
     */
    public static char[] getVerifiCode() {
        return verifiCode;
    }

    /**
     * @description: 随机生成验证码
     * @param: no
     * @return: char[]
     */
    private static char[] generateCheckCode() {
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] rands = new char[4];
        for (int i = 0; i < 4; i++) {
            int rand = (int) (Math.random() * (10 + 26 * 2));
            rands[i] = chars.charAt(rand);
        }
        return rands;
    }

    /**
     * @description: 绘制验证码
     * @param: g
     * @param: rands
     * @return: void
     */
    private static void drawRands(Graphics g, char[] rands) {
        g.setFont(new Font("Console", Font.BOLD, FONT_SIZE));

        for (int i = 0; i < rands.length; i++) {

            g.setColor(getRandomColor());
            g.drawString("" + rands[i], i * FONT_SIZE + 10, 25);
        }
    }

    /**
     * @description: 绘制验证码图片背景
     * @param: g
     * @return: void
     */
    private static void drawBackground(Graphics g) {

        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 绘制验证码干扰点
        for (int i = 0; i < 200; i++) {
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * HEIGHT);
            g.setColor(getRandomColor());
            g.drawOval(x, y, 1, 1);

        }
    }


    /**
     * @description: 获取随机颜色
     * @param: no
     * @return: java.awt.Color
     */
    private static Color getRandomColor() {
        Random ran = new Random();
        return new Color(ran.nextInt(220), ran.nextInt(220), ran.nextInt(220));
    }
}

```

#### token口令生成工具 JwtHelper

``` java 
package com.atguigu.zhxy.util;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JwtHelper {
    private static long tokenExpiration = 24*60*60*1000;
    private static String tokenSignKey = "123456";

    //生成token字符串
    public static String createToken(Long userId, Integer userType) {
        String token = Jwts.builder()

                .setSubject("YYGH-USER")

                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))

                .claim("userId", userId)
//                .claim("userName", userName)
                .claim("userType", userType)

                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    //从token字符串获取userid
    public static Long getUserId(String token) {
        if(StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
    }

    //从token字符串获取userType
    public static Integer getUserType(String token) {
        if(StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (Integer)(claims.get("userType"));
    }

    //从token字符串获取userName
    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)) return "";
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userName");
    }

    //判断token是否有效
    public static boolean isExpiration(String token){
        try {
            boolean isExpire = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration().before(new Date());
            //没有过期，有效，返回false
            return isExpire;
        }catch(Exception e) {
            //过期出现异常，返回true
            return true;
        }
    }


    /**
     * 刷新Token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody();
            refreshedToken = JwtHelper.createToken(getUserId(token), getUserType(token));
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

        public static void main(String[] args) {
//        String token = JwtHelper.createToken(1L, "lucy");
//        System.out.println(token);
//        System.out.println(JwtHelper.getUserId(token));
//        System.out.println(JwtHelper.getUserName(token));
    }
}

```

>解析request请求中的 token口令的工具AuthContextHolder

``` java
package com.atguigu.zhxy.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 对JwtHelper进一步封装，传入request就可以获取请求头中的token
 */
public class AuthContextHolder {

    //从请求头token获取userid
    public static Long getUserIdToken(HttpServletRequest request) {
        //从请求头token
        String token = request.getHeader("token");
        //调用工具类
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }

    //从请求头token获取name
    public static String getUserName(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取username
        String userName = JwtHelper.getUserName(token);
        return userName;
    }
}

```

#### MD5加密工具类

``` java
package com.atguigu.zhxy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 明文转密文
 */
public final class MD5 {

    public static String encrypt(String strSrc) {
        try {
            char hexChars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            byte[] bytes = strSrc.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                chars[k++] = hexChars[b >>> 4 & 0xf];
                chars[k++] = hexChars[b & 0xf];
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("MD5加密出错！！+" + e);
        }
    }


}

```

#### JSON响应结果格式封装类

``` java
package com.lz.zhxy.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 全局统一返回结果类
 */
@Data
@ApiModel(value = "全局统一返回结果")
public class Result<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public Result() {
    }

    public Result<T> message(String msg) {
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

    public boolean isOk() {
        if (this.getCode().intValue() == ResultCodeEnum.SUCCESS.getCode().intValue()) {
            return true;
        }
        return false;
    }

    /**
     * 返回数据
     */
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<T>();
        if (data != null)
            result.setData(data);
        return result;
    }

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
    
    /**
     * 操作成功
     */
    public static <T> Result<T> ok() {
        return Result.ok(null);
    }


    public static <T> Result<T> ok(T data) {
        Result<T> result = build(data);
        return build(data, ResultCodeEnum.SUCCESS);
    }

    /**
     * 操作失败
     */
    public static <T> Result<T> fail() {
        return Result.fail(null);
    }

    public static <T> Result<T> fail(T data) {
        Result<T> result = build(data);
        return build(data, ResultCodeEnum.FAIL);
    }

}
```

> 响应结果类型码枚举

``` java
package com.atguigu.zhxy.util;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    ILLEGAL_REQUEST( 204, "非法请求"),
    PAY_RUN(205, "支付中"),
    ARGUMENT_VALID_ERROR(206, "参数校验错误"),

    LOGIN_ERROR(207, "用户名或密码错误"),
    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),
    SECKILL_NO_START(210, "秒杀还没开始"),
    SECKILL_RUN(211, "正在排队中"),
    SECKILL_NO_PAY_ORDER(212, "您有未支付的订单"),
    SECKILL_FINISH(213, "已售罄"),
    SECKILL_END(214, "秒杀已结束"),
    SECKILL_SUCCESS(215, "抢单成功"),
    SECKILL_FAIL(216, "抢单失败"),
    SECKILL_ILLEGAL(217, "请求不合法"),
    SECKILL_ORDER_SUCCESS(218, "下单成功"),
    COUPON_GET(220, "优惠券已经领取"),
    COUPON_LIMIT_GET(221, "优惠券已发放完毕"),
    //2022-02-22
    LOGIN_CODE(222,"长时间未操作,会话已失效,请刷新页面后重试!"),
    CODE_ERROR(223,"验证码错误!"),
    TOKEN_ERROR(224,"Token无效!")
    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

```

#### 文件上传工具类

``` java
package com.atguigu.zhxy.util;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @project: zhxy
 * @description: 上传文件的工具类
 */
public class UploadFile {

    //存储文件上传失败的错误信息
    private static Map<String, Object> error_result = new HashMap<>();
    //存储头像的上传结果信息
    private static Map<String, Object> upload_result = new HashMap<>();

    /**
     * @description: 效验所上传图片的大小及格式等信息...
     * @param: photo
     * @param: path
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    private static Map<String, Object> uploadPhoto(MultipartFile photo, String path) {
        //限制头像大小(20M)
        int MAX_SIZE = 20971520;
        //获取图片的原始名称
        String orginalName = photo.getOriginalFilename();
        //如果保存文件的路径不存在,则创建该目录
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        //限制上传文件的大小
        if (photo.getSize() > MAX_SIZE) {
            error_result.put("success", false);
            error_result.put("msg", "上传的图片大小不能超过20M哟!");
            return error_result;
        }
        // 限制上传的文件类型
        String[] suffixs = new String[]{".png", ".PNG", ".jpg", ".JPG", ".jpeg", ".JPEG", ".gif", ".GIF", ".bmp", ".BMP"};
        SuffixFileFilter suffixFileFilter = new SuffixFileFilter(suffixs);
        if (!suffixFileFilter.accept(new File(path + orginalName))) {
            error_result.put("success", false);
            error_result.put("msg", "禁止上传此类型文件! 请上传图片哟!");
            return error_result;
        }

        return null;
    }

    /**
     * @description: (提取公共代码 : 提高代码的可重用性)获取头像的上传结果信息
     * @param: photo
     * @param: dirPaht
     * @param: portraitPath
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     */
    public static Map<String, Object> getUploadResult(MultipartFile photo, String dirPaht, String portraitPath) {

        if (!photo.isEmpty() && photo.getSize() > 0) {
            //获取图片的原始名称
            String orginalName = photo.getOriginalFilename();
            //上传图片,error_result:存储头像上传失败的错误信息
            Map<String, Object> error_result = UploadFile.uploadPhoto(photo, dirPaht);
            if (error_result != null) {
                return error_result;
            }
            //使用UUID重命名图片名称(uuid__原始图片名称)
            String newPhotoName = UUID.randomUUID() + "__" + orginalName;
            //将上传的文件保存到目标目录下
            try {
                photo.transferTo(new File(dirPaht + newPhotoName));
                upload_result.put("success", true);
                upload_result.put("portrait_path", portraitPath + newPhotoName);//将存储头像的项目路径返回给页面
            } catch (IOException e) {
                e.printStackTrace();
                upload_result.put("success", false);
                upload_result.put("msg", "上传文件失败! 服务器端发生异常!");
                return upload_result;
            }

        } else {
            upload_result.put("success", false);
            upload_result.put("msg", "头像上传失败! 未找到指定图片!");
        }
        return upload_result;
    }
}
```

## 登录页面

![image-20221119202020302](SpringBoot-%E6%99%BA%E6%85%A7%E6%A0%A1%E5%9B%AD%E9%A1%B9%E7%9B%AE/image-20221119202020302.png)

![](SpringBoot-%E6%99%BA%E6%85%A7%E6%A0%A1%E5%9B%AD%E9%A1%B9%E7%9B%AE/%E7%99%BB%E5%BD%95.png)

### 验证码功能实现

```java
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpSession session, HttpServletResponse response) {
//        获取验证码图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
//        获取验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
//        将验证码保存到session域中
        session.setAttribute("verifiCode", verifiCode);
//        将验证码图片通过io流响应到前端页面
        try {
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

```

### 登录校验功能实现

```java
    /**
     * 登录请求
     *
     * @param loginForm
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpSession session) {
//        验证码校验
        String verifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if (loginVerifiCode.equals("")) {
            return Result.fail().message("验证码失效，请重新输入");
        }
        if (!loginVerifiCode.equalsIgnoreCase(verifiCode)) {
            return Result.fail().message("验证码有误，请重新输入");
        }
//        移除session中的验证码
        session.removeAttribute("verifiCode");
//        分用户类型进行校验
        HashMap<String, Object> map = new HashMap<>();
        switch (loginForm.getUserType()) {
            case 1:
                Admin admin = adminService.login(loginForm);
                try {
                    if (admin != null) {
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token", token);
                        return Result.ok(map);
                    } else {
                        throw new RuntimeException("此用户不存在");
                    }
                } catch (RuntimeException e) {
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                Teacher teacher = teacherService.login(loginForm);
                if (teacher != null) {
                    String token = JwtHelper.createToken(teacher.getId().longValue(), 2);
                    map.put("token", token);
                    return Result.ok(map);
                }
            case 3:
                Student student = studentService.login(loginForm);
                if (student != null) {
                    String token = JwtHelper.createToken(student.getId().longValue(), 3);
                    map.put("token", token);
                    return Result.ok(map);
                }
        }

        return Result.fail().message("查无此用户！！！");
    }
```

```java
    @Override
    public Admin login(LoginForm loginForm) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("name", loginForm.getUsername())
                .eq("password", MD5.encrypt(loginForm.getPassword()));//对密码加密存储

        Admin admin = baseMapper.selectOne(adminQueryWrapper);
        return admin;
    }

    ====================================================================
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("name", loginForm.getUsername())
                .eq("password", MD5.encrypt(loginForm.getPassword()));
        Student student = baseMapper.selectOne(studentQueryWrapper);
        return student;
    }

    ====================================================================
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("name", loginForm.getUsername())
                .eq("password", MD5.encrypt(loginForm.getPassword()));
        Teacher teacher = baseMapper.selectOne(teacherQueryWrapper);
        return teacher;
    }
```

### 跳转至首页功能实现

```java
    /**
     * 获取用户信息，进行登录跳转
     *
     * @param token
     * @return
     */
    @GetMapping("/getInfo")
    public Result getInfo(@RequestHeader("token") String token) {
//        判断token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            System.out.println("请重新登录");
            return Result.fail().message("token已过期，请重新登录");
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        HashMap<String, Object> map = new HashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getUserInfoById(userId);
                map.put("user", admin);
                map.put("userType", 1);
                break;
            case 2:
                Student student = studentService.getUserInfoById(userId);
                map.put("user", student);
                map.put("userType", 2);
                break;
            case 3:
                Teacher teacher = teacherService.getUserInfoById(userId);
                map.put("user", teacher);
                map.put("userType", 3);
                break;
        }
        return Result.ok(map);
    }
```

```java
    @Override
    public Admin getUserInfoById(Long userId) {

        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("id", userId);
        Admin admin = baseMapper.selectOne(adminQueryWrapper);
        return admin;
    }
    
    
    ====================================================================
    @Override
    public Student getUserInfoById(Long userId) {

        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("id", userId);
        Student student = baseMapper.selectOne(studentQueryWrapper);
        return student;
    }
    
    ====================================================================
    @Override
    public Teacher getUserInfoById(Long userId) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("id", userId);
        Teacher teacher = baseMapper.selectOne(teacherQueryWrapper);
        return teacher;
    }
```

## 年级管理功能实现

### 查询年级信息【分页带条件】

```java
    /**
     * 获取年级列表，以及按人员名称模糊查询
     *
     * @param pageNo
     * @param pageSize
     * @param gradeName
     * @return
     */
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            @RequestParam(value = "gradeName", required = false) String gradeName
    ) {
        Page<Grade> gradePage = new Page<>(pageNo, pageSize);
        IPage<Grade> pageRs = gradeService.getUserByPage(gradePage, gradeName);
        return Result.ok(pageRs);
    }
```

```java
    @Override
    public IPage<Grade> getUserByPage(Page<Grade> gradePage, String gradeName) {
        QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(gradeName)) {
            gradeQueryWrapper.like("manager", gradeName)
                    .orderByAsc("id");
        }
        Page<Grade> page = baseMapper.selectPage(gradePage, gradeQueryWrapper);
        return page;
    }

```

### 添加和修改年级信息

```java
    /**
     * 添加和修改年级信息
     *
     * @param grade
     * @return
     */
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@RequestBody Grade grade) {

        gradeService.saveOrUpdate(grade);//这个是service中的方法，可以添加和更新

        return Result.ok();
    }
```

## 学生管理功能实现

###  异步图片上传处理头像

``` java
@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @ApiOperation("头像上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("文件二进制数据") @RequestPart("multipartFile") MultipartFile multipartFile
    ){

        //使用UUID随机生成文件名
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        //生成新的文件名字
        String filename = uuid.concat(multipartFile.getOriginalFilename());
        //生成文件的保存路径(实际生产环境这里会使用真正的文件存储服务器)
        String portraitPath ="c:/code/zhxy/target/classes/public/upload/".concat(filename);
        //保存文件
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String headerImg ="upload/"+filename;
        return Result.ok(headerImg);
    }
}
```

