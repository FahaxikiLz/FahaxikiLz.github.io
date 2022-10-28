---
title: JavaWeb（新）
date: 2022-8-31 15:10:56
update: 2022-10-17 19:18:30
categories:
- JavaWeb
tags:
- JavaWeb
---

# 新

# IDEA创建项目

> 创建javaweb项目，记得勾上那个选项。不勾的话创建的是常规的java项目

![image-20220311171526867](javaweb(新)/image-20220311171526867.png)

# 配置Tomcat

> tomcat可以不配置环境的，前提是本地要安装jdk并配置JAVA_HOME环境变量

![image-20220311174746176](javaweb(新)/image-20220311174746176.png)

# 问题

## 新建项目没有web

![image-20220311183903813](javaweb(新)/image-20220311183903813.png)

![image-20220311183740800](javaweb(新)/image-20220311183740800.png)

## 导入他人项目

![image-20220311183901723](javaweb(新)/image-20220311183901723.png)

![image-20220311183839442](javaweb(新)/image-20220311183839442.png)

## servlet继承HttpServlet时没有jar包

![image-20220311183907577](javaweb(新)/image-20220311183907577.png)

![image-20220311183511732](javaweb(新)/image-20220311183511732.png)

# 初识

> 提交表单，每一项都要有name属性

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <form action="add" method="post">
        名称<input name="name" type="text"/>
        单价<input name="price" type="text"/>
        库存<input name="inventory" type="text"/>
        备注<input name="remake" type="text"/>
        <input type="submit" value="提交"/>
    </form>
</body>
</html>
```

> - servlet类需要继承HttpServlet类
> - 表单提交方式是post或者get，servlet类就重写doPost或者doGet

```java
//@WebServlet("/add")
public class AddServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //        POST请求需要设置中文编码问题
        req.setCharacterEncoding("UTF-8");
        
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        String inventory = req.getParameter("inventory");
        String remake = req.getParameter("remake");

        System.out.println(name);
        System.out.println(price);
        System.out.println(inventory);
        System.out.println(remake);
    }
}
```

> - 配置表单提交到哪个servlet类
> - 也可以在selvlet类上方用注解@WebSevlet("/xxx")

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <!--配置servlet类-->
    <servlet>
        <!--类名-->
        <servlet-name>AddServlet</servlet-name>
        <!--类的路径-->
        <servlet-class>com.lz.servlet.AddServlet</servlet-class>
    </servlet>

    <!--servlet类的映射-->
    <servlet-mapping>
        <!--类名，与上方类名相同-->
        <servlet-name>AddServlet</servlet-name>
        <!--表单提交的servlet路径-->
        <url-pattern>/add</url-pattern>
    </servlet-mapping>
</web-app>
```

## 编码问题

> 注意：设置编码要在获取参数之前

```java
//        GET请求在tomcat8之后不需要设置编码
//        在tomcat8之前需要设置编码
//        String name1 = req.getParameter("name");
//        byte[] bytes = name1.getBytes("ISO-8859-1");
//        String s = new String(bytes, "UTF-8");


        //        POST请求需要设置中文编码问题
        req.setCharacterEncoding("UTF-8");
```

# Service

## Servlet的生命周期

>    1） 生命周期：从出生到死亡的过程就是生命周期。对应Servlet中的三个方法：init(),service(),destroy()
>    2） 默认情况下：
>         第一次接收请求时，这个Servlet会进行实例化(调用构造方法)、初始化(调用init())、然后服务(调用service())
>         从第二次请求开始，每一次都是服务
>         当容器关闭时，其中的所有的servlet实例会被销毁，调用销毁方法
>    3） 通过案例我们发现：
>
>         - Servlet实例tomcat只会创建一个，所有的请求都是这个实例去响应。
>                 - 默认情况下，第一次请求时，tomcat才会去实例化，初始化，然后再服务.这样的好处是什么？ 提高系统的启动速度 。 这样的缺点是什么？ 第一次请求时，耗时较长。
>    - 因此得出结论： 如果需要提高系统的启动速度，当前默认情况就是这样。如果需要提高响应速度，我们应该设置Servlet的初始化时机。
>
>    4） Servlet的初始化时机：
>
>              - 默认是第一次接收请求时，实例化，初始化
>                        - 我们可以通过<load-on-startup>来设置servlet启动的先后顺序,数字越小，启动越靠前，最小值0
>
>    5） Servlet在容器中是：单例的、线程不安全的
>
>    - 单例：所有的请求都是同一个实例去响应
>    - 线程不安全：一个线程需要根据这个实例中的某个成员变量值去做逻辑判断。但是在中间某个时机，另一个线程改变了这个成员变量的值，从而导致第一个线程的执行路径发生了变化
>    - 我们已经知道了servlet是线程不安全的，给我们的启发是： 尽量的不要在servlet中定义成员变量。如果不得不定义成员变量，那么不要去：①不要去修改成员变量的值 ②不要去根据成员变量的值做一些逻辑判断

## Session

> - request.getSession() -> 获取当前的会话，没有则创建一个新的会话
> - request.getSession(true) -> 效果和不带参数相同
> - request.getSession(false) -> 获取当前会话，没有则返回null，不会创建新的
> - session.getId() -> 获取sessionID
> - session.isNew() -> 判断当前session是否是新的
> - session.getMaxInactiveInterval() -> session的非激活间隔时长，默认1800秒
> - session.setMaxInactiveInterval() -> 设置非激活间隔时长，即多长时间不操作就失活
> - session.invalidate() -> 强制性让会话立即失效

```java
@WebServlet("/demo03_session")
public class demo03_session extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        获取session，如果没有则创建一个新的
        HttpSession session = req.getSession();
//        获取session的id
        System.out.println(session.getId());//F9EE19E2579BF9548DCE6B2767D84729

//        获取session创建时间
        System.out.println(session.getCreationTime());//1647254612605
//        获取session最后一次访问时间
        System.out.println(session.getLastAccessedTime());//1647254612605
//        获取session最大非激活状态时间
        System.out.println(session.getMaxInactiveInterval());//1800
//        设置session最大非激活时间
        session.setMaxInactiveInterval(2000);
//        获取session最大非激活状态时间
        System.out.println(session.getMaxInactiveInterval());//2000
    }
}
```

### session保存作用域

>   - session保存作用域是和具体的某一个session对应的
>   - 常用的API：
>     void session.setAttribute(k,v)
>     Object session.getAttribute(k)
>     void removeAttribute(k)

![03.session保存作用域](javaweb(新)/03.session_actionScope.png)

## 服务器转发和客户端重定向

> - 服务器转发：请求路径不会改变，只发送一次网络请求
> - 客户端重定向：请求路径会改变，发送两次网络请求

```java
/**
 * 用于测试服务器转发和客户端重定向
 */
@WebServlet("/demo06_Dispatcher_sendRedirect")
public class demo06_Dispatcher_sendRedirect extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        服务器转发
//        req.getRequestDispatcher("demo07_Dispatcher_sendRedirect").forward(req, resp);
//        System.out.println("demo06...........");


//        客户端重定向
        resp.sendRedirect("demo07_Dispatcher_sendRedirect");
        System.out.println("demo06...........");


    }
}
```

# [Thymeleaf](https://heavy_code_industry.gitee.io/code_heavy_industry/pro001-javaweb/lecture/chapter08/)

## 初始化项目

> 1. 添加Thymeleaf的jar包
>
> 2. 新建一个Servlet类ViewBaseServlet
>
> 3. 在web.xml文件中添加配置
>
>    - 配置前缀 view-prefix
>    - 配置后缀 view-suffix
>
> 4. 使得我们的Servlet继承ViewBaseServlet
>
> 5. 根据逻辑视图名称得到物理视图名称
>
>    - //此处的视图名称是 index
>      //那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图 名称上去
>      //逻辑视图名称 ：   index
>      //物理视图名称 ：   view-prefix + 逻辑视图名称 + view-suffix
>      //所以真实的视图名称是：/       index       .html
>
>      ```java
>      super.processTemplate("index", req, resp);
>      ```

> 步骤二，新建一个Servlet类ViewBaseServlet
>
> ```java
> public class ViewBaseServlet extends HttpServlet {
> 
>     private TemplateEngine templateEngine;
> 
>     @Override
>     public void init() throws ServletException {
> 
>         // 1.获取ServletContext对象
>         ServletContext servletContext = this.getServletContext();
> 
>         // 2.创建Thymeleaf解析器对象
>         ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
> 
>         // 3.给解析器对象设置参数
>         // ①HTML是默认模式，明确设置是为了代码更容易理解
>         templateResolver.setTemplateMode(TemplateMode.HTML);
> 
>         // ②设置前缀
>         String viewPrefix = servletContext.getInitParameter("view-prefix");
> 
>         templateResolver.setPrefix(viewPrefix);
> 
>         // ③设置后缀
>         String viewSuffix = servletContext.getInitParameter("view-suffix");
> 
>         templateResolver.setSuffix(viewSuffix);
> 
>         // ④设置缓存过期时间（毫秒）
>         templateResolver.setCacheTTLMs(60000L);
> 
>         // ⑤设置是否缓存
>         templateResolver.setCacheable(true);
> 
>         // ⑥设置服务器端编码方式
>         templateResolver.setCharacterEncoding("utf-8");
> 
>         // 4.创建模板引擎对象
>         templateEngine = new TemplateEngine();
> 
>         // 5.给模板引擎对象设置模板解析器
>         templateEngine.setTemplateResolver(templateResolver);
> 
>     }
> 
>     protected void processTemplate(String templateName, HttpServletRequest req, HttpServletResponse resp) throws IOException {
>         // 1.设置响应体内容类型和字符集
>         resp.setContentType("text/html;charset=UTF-8");
> 
>         // 2.创建WebContext对象
>         WebContext webContext = new WebContext(req, resp, getServletContext());
> 
>         // 3.处理模板数据
>         templateEngine.process(templateName, webContext, resp.getWriter());
>     }
> }
> ```

> 步骤三，在web.xml文件中添加配置
>
> ```xml
> <?xml version="1.0" encoding="UTF-8"?>
> <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
>          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
>          xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
>          version="4.0">
> 
>     <!-- 在上下文参数中配置视图前缀和视图后缀 -->
>     <context-param>
>         <param-name>view-prefix</param-name>
>         <param-value>/</param-value>
>     </context-param>
>     <context-param>
>         <param-name>view-suffix</param-name>
>         <param-value>.html</param-value>
>     </context-param>
> </web-app>
> ```

> 步骤四，使得我们的Servlet继承ViewBaseServlet
>
> ```java
> @WebServlet("/index")
> public class indexServlet extends ViewBaseServlet {
>     @Override
>     protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
>         FruitDaoImpl fruitDao = new FruitDaoImpl();
>         List<Fruit> fruitList = fruitDao.getFruitList();
> 
>         req.getSession().setAttribute("fruitlist", fruitList);
> 
>         //此处的视图名称是 index
>         //那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图 名称上去
>         //逻辑视图名称 ：   index
>         //物理视图名称 ：   view-prefix + 逻辑视图名称 + view-suffix
>         //所以真实的视图名称是： /       index       .html
>         super.processTemplate("index", req, resp);
> 
>         System.out.println(fruitList);
>     }
> }
> ```

# 保存作用域

> - 存作用域我们可以认为有四个： page（页面级别，现在几乎不用） , request（一次请求响应范围） , session（一次会话范围） , application（整个应用程序范围）
>   1. request：一次请求响应范围
>   2. session：一次会话范围有效
>   3. application： 一次应用程序范围有效

## request保存作用域

> - 服务器转发
>   - 同一个浏览器：能接收到数据，但是好像要用原生的HttpServletRequest接收
>   - 不同浏览器：不能接收到数据
> - 客户端重定向
>   - 同一个浏览器：不能接收到数据
>   - 不同浏览器：不能接收到数据
> - 一次请求响应范围

![](javaweb(新)/01.Request_actionScope.png)

```java
/**
 * 用于测试request保存作用域（demo01Servlet demo02Servlet）
 */
@WebServlet("/demo01")
public class demo01Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("name", "lz");

//        客户端重定向
//        resp.sendRedirect("demo02");//null

//        服务器转发
        req.getRequestDispatcher("demo02").forward(req, resp);//lz

    }
}
```

## session保存作用域

> - 服务器转发
>   - 同一个浏览器：能接收到数据
>   - 不同浏览器：不能接收到数据
> - 客户端重定向
>   - 同一个浏览器：能接收到数据
>   - 不同浏览器：不能接收到数据
> - 只要会话保持，不管是服务器转发，还是客户端重定向，都可以收到数据
> - 不同的浏览器不可以收到数据

![](javaweb(新)/02.Session_actionScope.png)

```java
/**
 * 用于测试session保存作用域（demo03Servlet demo04Servlet）
 */
@WebServlet("/demo03")
public class demo03Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("name", "lz");

//        客户端重定向
//        resp.sendRedirect("demo04");//lz

//        服务器转发
        req.getRequestDispatcher("demo04").forward(req, resp);//lz

    }
}
```

## application保存作用域

> - 服务器转发
>   - 同一个浏览器：能接收到数据
>   - 不同浏览器：能接收到数据
> - 客户端重定向
>   - 同一个浏览器：能接收到数据
>   - 不同浏览器：能接收到数据
> - 只要项目不关闭application作用域中的数据就不会丢失

![](javaweb(新)/03.Application_actionScope.png)

```java

/**
 * 用于测试application保存作用域（demo05Servlet demo06Servlet）
 */
@WebServlet("/demo05")
public class demo05Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        servletContext.setAttribute("name", "lz");

//        客户端重定向
//        resp.sendRedirect("demo06");//lz

//        服务器转发
        req.getRequestDispatcher("demo06").forward(req, resp);//lz

    }
}

```

# 分页查询

## 思路

> 1. ```sql
>    select * from xxx limit 0,5
>    ```
>
>    0是第一个数据，5是到第五个，也就是说第一页从第一个数据开始，每页5个
>
> 2. 在servlet中`req.getParameter("页数")`获取当前页数
>
> 3. 总页数=（总记录数+每页记录数-1）/每页记录数
>
> 4. 每页开始条数 = （当前页码 - 1）* 每页条数
>
>    第六页从第几条开始？就是前五页所有条数+1，也就是（第六页 - 1） * 每页条数

## 代码实现

```java
public class FruitDaoImpl extends baseDAO<Fruit> implements FruitDao {
    //   分页查询水果的数据
    @Override
    public List<Fruit> getFruitList(int pageNo) {
        String sql = "select * from t_fruit limit ?,5";
        List<Fruit> fruits = super.executeQuery(sql, (pageNo - 1) * 5);
        return fruits;
    }

    //    查询总数据条数
    @Override
    public int getFruitCount() {
        String sql = "select count(*) from t_fruit";
        return ((Long) super.executeComplexQuery(sql)[0]).intValue();

    }
}

```

```java
@WebServlet("/index")
public class indexServlet extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //        设置默认页码
        int pageNo = 1;
        //        获取页码
        String pageNo1 = req.getParameter("pageNo");

        if (pageNo1 != null) {
            pageNo = Integer.parseInt(pageNo1);
        }

        FruitDaoImpl fruitDao = new FruitDaoImpl();
        List<Fruit> fruitList = fruitDao.getFruitList(pageNo);

//        查询总数据条数
        int fruitCount = fruitDao.getFruitCount();
//        总页数=（总记录数+每页记录数-1）/每页记录数
        int pageCount = (fruitCount + 5 - 1) / 5;


        HttpSession session = req.getSession();
        session.setAttribute("fruitlist", fruitList);

        session.setAttribute("pageNo", pageNo);

        session.setAttribute("pageCount", pageCount);

        //此处的视图名称是 index
        //那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图 名称上去
        //逻辑视图名称 ：   index
        //物理视图名称 ：   view-prefix + 逻辑视图名称 + view-suffix
        //所以真实的视图名称是： /       index       .html
        super.processTemplate("index", req, resp);

    }
}

```

```java
// 上一页 下一页
<div>
    <button th:onclick="page(1)" th:disabled="${session.pageNo==1}">首页</button>
    <button th:onclick="|page(${session.pageNo-1})|" th:disabled="${session.pageNo==1}">上一页</button>
    <button th:onclick="|page(${session.pageNo+1})|" th:disabled="${session.pageNo==session.pageCount}">下一页
    </button>
    <button th:onclick="|page(${session.pageCount})|" th:disabled="${session.pageNo==session.pageCount}">尾页
    </button>
</div>
```

# 关键字查询

## 思路

> 1. 在html页面设置查询的表单，里面可以加一个隐藏域。因为需要判断是加载的页面还是提交的表单
> 2. 在servlet中判断是否是表单提交，然后在session保存查询关键字，在加载页面中取出session中的查询关键字的值。切记不可将加载页面中的查询关键字设置为空，因为在切换页面时会再请求网络，这样就是就在页面了，查询的就是空值了
> 3. 在sql语句中不可以写`select * from xxx where name like %?%`，只能在sql传参的时候为参数加上%

## 代码实现

```html
<form method="post" th:action="@{/index}">
    <input type="hidden" value="search" name="search">
    <input type="text" name="keyword" th:value="${session.keyword}">
    <input type="submit" value="提交">
</form>
```

```java
@WebServlet("/index")
public class indexServlet extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //关键字查询表单提交的post，然后这里调用一下get方法
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String search = req.getParameter("search");

        HttpSession session = req.getSession();

//        设置默认页码
        int pageNo = 1;
        String keyword = null;
        if (search != null && search != "" && search.equals("search")) {
//            提交的表单
            keyword = req.getParameter("keyword");
            if (keyword == null) {
                keyword = "";
            }
            pageNo = 1;
            session.setAttribute("keyword", keyword);
        } else {
//            不是提交的表单
//        	  获取页码
            String pageNo1 = req.getParameter("pageNo");

            if (pageNo1 != null) {
                pageNo = Integer.parseInt(pageNo1);
            }

//            这里要接收session，因为当你切换下一页上一页时会发送请求，会一直持有keyword的值。
//            如果直接这里写keyword=""，那么在切换页面时就换造成持有keyword的值为空
            Object keyword1 = session.getAttribute("keyword");
            if (keyword1 == null) {
                keyword = "";
            } else {
                keyword = (String) keyword1;
            }
        }

        FruitDaoImpl fruitDao = new FruitDaoImpl();
        List<Fruit> fruitList = fruitDao.getFruitList(keyword, pageNo);

//        查询总数据条数
        int fruitCount = fruitDao.getFruitCount(keyword);
//        总页数=（总记录数+每页记录数-1）/每页记录数
        int pageCount = (fruitCount + 5 - 1) / 5;
        
        session.setAttribute("fruitlist", fruitList);
        session.setAttribute("pageNo", pageNo);
        session.setAttribute("pageCount", pageCount);
        super.processTemplate("index", req, resp);

    }
}

```

```java
public class FruitDaoImpl extends baseDAO<Fruit> implements FruitDao {
    //   分页查询水果的数据
    @Override
    public List<Fruit> getFruitList(String keyword, int pageNo) {
        String sql = "select * from t_fruit where fname like ? or remark like ? limit ?,5";
        //        这里模糊查询的%必须写到传参的地方
        List<Fruit> fruits = super.executeQuery(sql, "%" + keyword + "%", "%" + keyword + "%", (pageNo - 1) * 5);
        return fruits;
    }
	......

    //    查询总数据条数
    @Override
    public int getFruitCount(String keyword) {
        String sql = "select count(*) from t_fruit where fname like ? or remark like ?";
//        这里模糊查询的%必须写到传参的地方
        return ((Long) super.executeComplexQuery(sql, "%" + keyword + "%", "%" + keyword + "%")[0]).intValue();

    }
}

```

# [MVC P40-45](https://www.bilibili.com/video/BV1AS4y177xJ?p=40)

## 初始版

> 1. 最初的做法是： 一个请求对应一个Servlet，这样存在的问题是servlet太多了
> 2. 把一些列的请求都对应一个Servlet, IndexServlet/AddServlet/EditServlet/DelServlet/UpdateServlet -> 合并成FruitServlet。
> 3. **前端每个请求中都带有一个operate参数，用于后端获取判断访问哪个页面**
> 4. 通过一个operate的值来决定调用FruitServlet中的哪一个方法。使用的是switch-case

```html
<form method="post" th:action="@{/fruit.do}">
    <input type="hidden" value="search" name="search">
    <input type="text" name="keyword" th:value="${session.keyword}">
    <input type="submit" value="提交">
</form>
	......
<a th:href="@{/fruit.do(fid=${fruit.fid},operate='edit')}">苹果</a>
```

```java
@WebServlet("/fruit.do")
public class FruitServlet extends ViewBaseServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String operate = req.getParameter("operate");

        if (operate == null || operate.equals("")) {
            operate = "index";
        }

        switch (operate) {
            case "index":
                index(req, resp);
                break;
            case "add":
                add(req, resp);
                break;
            case "del":
                del(req, resp);
                break;
            case "edit":
                edit(req, resp);
                break;
            case "update":
                update(req, resp);
                break;
            default:
                throw new RuntimeException("输入有误");

        }
    }


    private void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......

    }


    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......
    }


    private void del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......
    }


    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......

    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......

    }
}

```

## 优化1

> 在上一个版本中，Servlet中充斥着大量的switch-case，试想一下，随着我们的项目的业务规模扩大，那么会有很多的Servlet，也就意味着会有很多的switch-case，这是一种代码冗余
> 因此，我们在servlet中使用了反射技术，我们规定operate的值和方法名一致，那么接收到operate的值是什么就表明我们需要调用对应的方法进行响应，如果找不到对应的方法，则抛异常

```java
@WebServlet("/fruit.do")
public class FruitServlet extends ViewBaseServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String operate = req.getParameter("operate");

        if (operate == null || operate.equals("")) {
            operate = "index";
        }

        Method[] declaredMethods = this.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            String name = declaredMethod.getName();
            if (name.equals(operate)) {
                try {
                    declaredMethod.invoke(this, req, resp);
                    return;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
        throw new RuntimeException("输入有误");
    }


    private void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......

    }


    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......
    }


    private void del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......
    }


    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......

    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		......

    }
}

```

## 优化2

> 在上一个版本中我们使用了反射技术，但是其实还是存在一定的问题：每一个servlet中都有类似的反射技术的代码。因此继续抽取，设计了**中央控制器类：DispatcherServlet**
>
> 请求路径，例如http://localhost:8080/pro15/fruit.do?operate=edit。fruit和xml配置文件中id一致。operate是请求哪个方法

<img src="javaweb(新)/03.MVC03.png" />

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans>
    <!-- 这个bean标签的作用是 将来servletpath中涉及的名字对应的是fruit，那么就要FruitController这个类来处理 -->
    <bean id="fruit" class="com.atguigu.fruit.controllers.FruitController"/>
</beans>

<!--
1.概念
HTML : 超文本标记语言
XML : 可扩展的标记语言
HTML是XML的一个子集

2.XML包含三个部分：
1) XML声明 ， 而且声明这一行代码必须在XML文件的第一行
2) DTD 文档类型定义
3) XML正文
 -->
```

> 根据url定位到能够处理这个请求的controller组件：
>     1)从url中提取servletPath : /fruit.do -> fruit
>     2)根据fruit找到对应的组件:FruitController ， 这个对应的依据我们存储在applicationContext.xml中
>       <bean id="fruit" class="com.atguigu.fruit.controllers.FruitController/>
>       通过DOM技术我们去解析XML文件，在中央控制器中形成一个beanMap容器，用来存放所有的Controller组件
>     3)根据获取到的operate的值定位到我们FruitController中需要调用的方法

```java
@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet {

    private Map<String, Object> beanMap = new HashMap<>();

    public void init() {
        /**
         * 解析appliactionContext.xml文件。将id和class对象放入Map集合
         */
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            //1.创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2.创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //3.创建Document对象
            Document document = documentBuilder.parse(inputStream);

            //4.获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Class controllerBeanClass = Class.forName(className);
                    Object beanObj = controllerBeanClass.newInstance();

                    Method setServletContextMethod = controllerBeanClass.getDeclaredMethod("setServletContext", ServletContext.class);
                    setServletContextMethod.invoke(beanObj, this.getServletContext());

                    beanMap.put(beanId, beanObj);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("UTF-8");

        /**
         * 解析请求路径中的参数，将解析出来的参数与我们controller中的方法进行比对。判断执行哪个controller
         *
         * 假设url是：  http://localhost:8080/pro15/hello.do
         * 那么servletPath是：    /hello.do
         *
         * 我的思路是：
         * 第1步： /hello.do ->   hello   或者  /fruit.do  -> fruit
         * 第2步： hello -> HelloController 或者 fruit -> FruitController
         */
        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex);


        /**
         * 根据解析的url参数作为键，取出controller对象
         */
        Object controllerBeanObj = beanMap.get(servletPath);

        // 获取operate的值
        String operate = request.getParameter("operate");
        if (StringUtil.isEmpty(operate)) {
            operate = "index";
        }

        try {
            // 从相应的controller中获取方法
            Method method = controllerBeanObj.getClass().getDeclaredMethod(operate, HttpServletRequest.class, HttpServletResponse.class);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(controllerBeanObj, request, response);
            } else {
                throw new RuntimeException("operate值非法!");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

```

```java
public class FruitController extends ViewBaseServlet {

    //之前FruitServlet是一个Sevlet组件，那么其中的init方法一定会被调用
    //之前的init方法内部会出现一句话：super.init();

    private ServletContext servletContext ;

    public void setServletContext(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;
        super.init(servletContext);
    }
    
    

    private FruitDAO fruitDAO = new FruitDAOImpl();

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ......
        response.sendRedirect("fruit.do");
    }

    private void edit(HttpServletRequest request , HttpServletResponse response)throws IOException, ServletException {
      		......
            super.processTemplate("edit",request,response);
        }
    }

    private void del(HttpServletRequest request , HttpServletResponse response)throws IOException, ServletException {
       		......
            response.sendRedirect("fruit.do");
        }
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ......
        response.sendRedirect("fruit.do");

    }

    private void index(HttpServletRequest request , HttpServletResponse response)throws IOException, ServletException {
        ......
        super.processTemplate("index",request,response);
    }
}

```

![](javaweb(新)/DispatcherServlet.png)

## 优化3

> **将客户端重定向和html页面渲染return出去，在中央控制器中统一处理**
>
> **请求方法获取参数不使用req.getParamas()，而是直接向方法传参。**表单的直接提交，其他方式需要拼接请求参数

```java
public class FruitController {
    private FruitDAO fruitDAO = new FruitDAOImpl();

    private String update(Integer fid , String fname , Integer price , Integer fcount , String remark ){
        //3.执行更新
        fruitDAO.updateFruit(new Fruit(fid,fname, price ,fcount ,remark ));
        //4.资源跳转
        return "redirect:fruit.do";
    }

    private String edit(Integer fid , HttpServletRequest request){
        if(fid!=null){
            Fruit fruit = fruitDAO.getFruitByFid(fid);
            request.setAttribute("fruit",fruit);
            //super.processTemplate("edit",request,response);
            return "edit";
        }
        return "error" ;
    }

    private String del(Integer fid  ){
        if(fid!=null){
            fruitDAO.delFruit(fid);
            return "redirect:fruit.do";
        }
        return "error";
    }

    private String add(String fname , Integer price , Integer fcount , String remark ) {
        Fruit fruit = new Fruit(0,fname , price , fcount , remark ) ;
        fruitDAO.addFruit(fruit);
        return "redirect:fruit.do";
    }

    private String index(String oper , String keyword , Integer pageNo , HttpServletRequest request ) {
        HttpSession session = request.getSession() ;

        if(pageNo==null){
            pageNo = 1;
        }
        if(StringUtil.isNotEmpty(oper) && "search".equals(oper)){
            pageNo = 1 ;
            if(StringUtil.isEmpty(keyword)){
                keyword = "" ;
            }
            session.setAttribute("keyword",keyword);
        }else{
            Object keywordObj = session.getAttribute("keyword");
            if(keywordObj!=null){
                keyword = (String)keywordObj ;
            }else{
                keyword = "" ;
            }
        }

        // 重新更新当前页的值
        session.setAttribute("pageNo",pageNo);

        FruitDAO fruitDAO = new FruitDAOImpl();
        List<Fruit> fruitList = fruitDAO.getFruitList(keyword , pageNo);
        session.setAttribute("fruitList",fruitList);

        //总记录条数
        int fruitCount = fruitDAO.getFruitCount(keyword);
        //总页数
        int pageCount = (fruitCount+5-1)/5 ;
        session.setAttribute("pageCount",pageCount);

        return "index" ;
    }
}

```

> 调用Controller组件中的方法：
>     1) 获取参数
>        获取即将要调用的方法的参数签名信息: Parameter[] parameters = method.getParameters();
>        通过parameter.getName()获取参数的名称；
>        准备了Object[] parameterValues 这个数组用来存放对应参数的参数值
>        另外，我们需要考虑参数的类型问题，需要做类型转化的工作。通过parameter.getType()获取参数的类型
>     2) 执行方法
>        Object returnObj = method.invoke(controllerBean , parameterValues);
>     3) 视图处理
>        String returnStr = (String)returnObj;
>        if(returnStr.startWith("redirect:")){
>         ....
>        }else if.....

```java
@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet{

    private Map<String,Object> beanMap = new HashMap<>();

    public DispatcherServlet(){
    }

    public void init() throws ServletException {
        super.init();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            //1.创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2.创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder() ;
            //3.创建Document对象
            Document document = documentBuilder.parse(inputStream);

            //4.获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for(int i = 0 ; i<beanNodeList.getLength() ; i++){
                Node beanNode = beanNodeList.item(i);
                if(beanNode.getNodeType() == Node.ELEMENT_NODE){
                    Element beanElement = (Element)beanNode ;
                    String beanId =  beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Class controllerBeanClass = Class.forName(className);
                    Object beanObj = controllerBeanClass.newInstance() ;
                    beanMap.put(beanId , beanObj) ;
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置编码
        request.setCharacterEncoding("UTF-8");

        String servletPath = request.getServletPath();
        servletPath = servletPath.substring(1);
        int lastDotIndex = servletPath.lastIndexOf(".do") ;
        servletPath = servletPath.substring(0,lastDotIndex);

        Object controllerBeanObj = beanMap.get(servletPath);

        String operate = request.getParameter("operate");
        if(StringUtil.isEmpty(operate)){
            operate = "index" ;
        }

        try {
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for(Method method : methods){
                if(operate.equals(method.getName())){
                    /**
                     * 1.统一获取请求参数
                     */

                    //1-1.获取当前方法的参数，返回参数数组
                    Parameter[] parameters = method.getParameters();
                    //1-2.parameterValues 用来承载参数的值
                    Object[] parameterValues = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName() ;
                        //如果参数名是request,response,session 那么就不是通过请求中获取参数的方式了
                        if("request".equals(parameterName)){
                            parameterValues[i] = request ;
                        }else if("response".equals(parameterName)){
                            parameterValues[i] = response ;
                        }else if("session".equals(parameterName)){
                            parameterValues[i] = request.getSession() ;
                        }else{
                            //从请求中获取参数值
                            String parameterValue = request.getParameter(parameterName);
                            String typeName = parameter.getType().getName();

                            Object parameterObj = parameterValue ;

                            if(parameterObj!=null) {
                                if ("java.lang.Integer".equals(typeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }
                            parameterValues[i] = parameterObj ;
                        }
                    }
                    
                    //2.controller组件中的方法调用
                    method.setAccessible(true);
                    Object returnObj = method.invoke(controllerBeanObj,parameterValues);


                    /**
                     * 3.视图处理
                     * controller将客户端重定向和渲染html返回出来，统一处理
                     */
                    String methodReturnStr = (String)returnObj ;
                    if(methodReturnStr.startsWith("redirect:")){        //比如：  redirect:fruit.do
                        String redirectStr = methodReturnStr.substring("redirect:".length());
                        response.sendRedirect(redirectStr);
                    }else{
                        super.processTemplate(methodReturnStr,request,response);    // 比如：  "edit"
                    }
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

// 常见错误： IllegalArgumentException: argument type mismatch
```

> 需要将idea配置，要不在获取方法参数时，无法看到参数名

![image-20220318185828374](javaweb(新)/image-20220318185828374.png)

![image-20220318190149256](javaweb(新)/image-20220318190149256.png)



## 优化4——引入service

> 什么是业务层
>
> 1. Model1和Model2
>        MVC : Model（模型）、View（视图）、Controller（控制器）
>        视图层：用于做数据展示以及和用户交互的一个界面
>        控制层：能够接受客户端的请求，具体的业务功能还是需要借助于模型组件来完成
>        模型层：模型分为很多种：有比较简单的pojo/vo(value object)，有业务模型组件，有数据访问层组件
>            1) pojo/vo : 值对象
>            2) DAO ： 数据访问对象
>            3) BO ： 业务对象
>
> 2. 区分业务对象和数据访问对象：
>
>      		1） DAO中的方法都是单精度方法或者称之为细粒度方法。什么叫单精度？一个方法只考虑一个操作，比如添加，那就是insert操作、查询那就是select操作....
>     		 2） BO中的方法属于业务方法，也实际的业务是比较复杂的，因此业务方法的粒度是比较粗的
>
>    ​				注册这个功能属于业务功能，也就是说注册这个方法属于业务方法。
>
>    ​				那么这个业务方法中包含了多个DAO方法。也就是说注册这个业务功能需要通过多个DAO方法的组合调用，从而完成注册功能的开发。
>
>    ​				注册：
>
>    ​						1.检查用户名是否已经被注册 - DAO中的select操作
>    ​						2.用户记录 - DAO中的insert操作
>
>    ​						3.向用户积分表新增一条记录（新用户默认初始化积分100分） - DAO中的insert操作
>    ​						4.向系统消息表新增一条记录（某某某新用户注册了，需要根据通讯录信息向他的联系人推送消息） - DAO中的insert操作
>
>    ​						5.向系统日志表新增一条记录（某用户在某IP在某年某月某日某时某分某秒某毫秒注册） - DAO中的insert操作
>
> 3. 在库存系统中添加业务层组件

![](javaweb(新)/01.MVC04.png)

```java
public class FruitServiceImpl implements FruitService {
    private FruitDAOImpl fruitdaoImpl = new FruitDAOImpl();

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        return fruitdaoImpl.getFruitList(keyword, pageNo);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return fruitdaoImpl.getFruitByFid(fid);
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitdaoImpl.updateFruit(fruit);
    }

    @Override
    public void delFruit(Integer fid) {
        fruitdaoImpl.delFruit(fid);
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitdaoImpl.addFruit(fruit);

    }

    @Override
    public int getPageCount(String keyword) {
        int count = fruitdaoImpl.getFruitCount(keyword);
        int pageCount = (count + 5 - 1) / 5;
        return pageCount;
    }
}
```

## 优化5——实现ioc

> 1. 耦合/依赖
>    依赖指的是某某某离不开某某某
>    在软件系统中，层与层之间是存在依赖的。我们也称之为耦合。
>    我们系统架构或者是设计的一个原则是： 高内聚低耦合。
>    层内部的组成应该是高度聚合的，而层与层之间的关系应该是低耦合的，最理想的情况0耦合（就是没有耦合）
>
> 2. IOC - 控制反转 / DI - 依赖注入
>
>    控制反转：
>        1) 之前在Servlet中，我们创建service对象 ， FruitService fruitService = new FruitServiceImpl();
>           这句话如果出现在servlet中的某个方法内部，那么这个fruitService的作用域（生命周期）应该就是这个方法级别；
>           如果这句话出现在servlet的类中，也就是说fruitService是一个成员变量，那么这个fruitService的作用域（生命周期）应该就是这个servlet实例级别
>        2) 之后我们在applicationContext.xml中定义了这个fruitService。然后通过解析XML，产生fruitService实例，存放在beanMap中，这个beanMap在一个BeanFactory中
>           因此，我们转移（改变）了之前的service实例、dao实例等等他们的生命周期。控制权从程序员转移到BeanFactory。这个现象我们称之为控制反转
>
>    依赖注入：
>    	1) 之前我们在控制层出现代码：FruitService fruitService = new FruitServiceImpl()；
>
>    ​		那么，控制层和service层存在耦合。
>    ​	2) 之后，我们将代码修改成FruitService fruitService = null ;
>       	然后，在配置文件中配置:
>       	<bean id="fruit" class="FruitController">
>    ​       	 <property name="fruitService" ref="fruitService"/>
>    ​	   </bean>

```xml
<?xml version="1.0" encoding="utf-8"?>

<beans>

    <bean id="FruitDAOImpl" class="com.atguigu.fruit.dao.impl.FruitDAOImpl"></bean>

    <bean id="FruitServiceImpl" class="com.atguigu.fruit.service.impl.FruitServiceImpl">
        <!--property标签用来表示属性；name标识属性名（serviceimpl中实例化daoimpl的名字）；ref表示引用其他bean的id值-->
        <property name="fruitdaoImpl" ref="FruitDAOImpl"></property>
    </bean>

    <bean id="fruit" class="com.atguigu.fruit.controllers.FruitController">
        <property name="fruitserviceImpl" ref="FruitServiceImpl"></property>
    </bean>
</beans>
```

```java
public class ClassPathXmlApplicationContextImpl implements BeanFactory {
    private HashMap<String, Object> beanMap = new HashMap<>();

    public ClassPathXmlApplicationContextImpl() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            //1.创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2.创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //3.创建Document对象
            Document document = documentBuilder.parse(inputStream);

            //4.获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Class controllerBeanClass = Class.forName(className);
                    Object beanObj = controllerBeanClass.newInstance();
                    beanMap.put(beanId, beanObj);
                }
            }
            //5.组装bean之间的依赖关系
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    NodeList beanChildNodeList = beanElement.getChildNodes();
                    for (int j = 0; j < beanChildNodeList.getLength(); j++) {
                        Node beanChildNode = beanChildNodeList.item(j);
                        if (beanChildNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(beanChildNode.getNodeName())) {
                            Element propertyElement = (Element) beanChildNode;
                            String propertyName = propertyElement.getAttribute("name");
                            String propertyRef = propertyElement.getAttribute("ref");
                            //1) 找到propertyRef对应的实例
                            Object refObj = beanMap.get(propertyRef);
                            //2) 将refObj设置到当前bean对应的实例的property属性上去
                            Object beanObj = beanMap.get(beanId);
                            Class beanClazz = beanObj.getClass();
                            Field propertyField = beanClazz.getDeclaredField(propertyName);
                            propertyField.setAccessible(true);
                            propertyField.set(beanObj, refObj);
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}

```

# Servlet API

## 获取初始化设置的数据

### 方式一：配置xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <servlet>
        <servlet-name>servletdemo01</servlet-name>
        <servlet-class>com.lz.servlet.ServletDemo01</servlet-class>
        <init-param>
            <param-name>name</param-name>
            <param-value>lz</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>servletdemo01</servlet-name>
        <url-pattern>/demo01</url-pattern>
    </servlet-mapping>


    <context-param>
        <param-name>hello</param-name>
        <param-value>word</param-value>
    </context-param>
</web-app>
```

```java
public class ServletDemo01 extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ServletConfig servletConfig = getServletConfig();
        String name = servletConfig.getInitParameter("name");
        System.out.println(name);//lz


        ServletContext servletContext = getServletContext();
        String hello = servletContext.getInitParameter("hello");
        System.out.println(hello);//word
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ServletContext servletContext = req.getServletContext();
//        ServletContext servletContext1 = req.getSession().getServletContext();
//        String initParameter = servletContext.getInitParameter();
    }
}

```

### 方式二：使用注解@WebServlet

```java
@WebServlet(urlPatterns = {"/demo01"}, initParams = {
        @WebInitParam(name = "name1", value = "lz"),
        @WebInitParam(name = "name2", value = "wmy")
})
public class ServletDemo01 extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ServletConfig servletConfig = getServletConfig();

        String name1 = servletConfig.getInitParameter("name1");
        String name2 = servletConfig.getInitParameter("name2");

        System.out.println(name1);//lz
        System.out.println(name2);//wmy
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        ServletContext servletContext = req.getServletContext();
//        ServletContext servletContext1 = req.getSession().getServletContext();
//        String initParameter = servletContext.getInitParameter();
    }
}
```

# Filter

> 1. Filter也属于Servlet规范
> 2.  Filter开发步骤：新建类实现Filter接口，然后实现其中的三个方法：init、doFilter、destroy
>       配置Filter，可以用注解@WebFilter，也可以使用xml文件 `<filter>` `<filter-mapping>`
> 3.  Filter在配置时，和servlet一样，也可以配置通配符，例如 @WebFilter("*.do")表示拦截所有以.do结尾的请求
> 4. 过滤器链
>       1）执行的顺序依次是： A B C demo03 C2 B2 A2
>       2）如果采取的是注解的方式进行配置，那么过滤器链的拦截顺序是按照全类名的先后顺序排序的
>       3）如果采取的是xml的方式进行配置，那么按照配置的先后顺序进行排序

### 方法一：配置xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <filter>
        <filter-name>demo01</filter-name>
        <filter-class>com.lz.Filter.demo01Filter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>demo01</filter-name>
        <url-pattern>/demo01</url-pattern>
    </filter-mapping>

</web-app>
```

```java
public class demo01Filter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("A");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("B");
    }

    @Override
    public void destroy() {

    }
}

```

### 方法二：使用注解@WebFilter

```java
package com.lz.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/demo01")
public class demo01Filter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("A");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("B");
    }

    @Override
    public void destroy() {

    }
}

```

# Listener

> 1) ServletContextListener - 监听ServletContext对象的创建和销毁的过程
> 2) HttpSessionListener - 监听HttpSession对象的创建和销毁的过程
> 3) ServletRequestListener - 监听ServletRequest对象的创建和销毁的过程
>
> 4) ServletContextAttributeListener - 监听ServletContext的保存作用域的改动(add,remove,replace)
> 5) HttpSessionAttributeListener - 监听HttpSession的保存作用域的改动(add,remove,replace)
> 6) ServletRequestAttributeListener - 监听ServletRequest的保存作用域的改动(add,remove,replace)
>
> 7) HttpSessionBindingListener - 监听某个对象在Session域中的创建与移除
> 8) HttpSessionActivationListener - 监听某个对象在Session域中的序列化和反序列化

### 方法一：配置xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <listener>
        <listener-class>com.lz.Listener.demo01Listener</listener-class>
    </listener>

</web-app>
```

```java
public class demo01Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("开始监听");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("结束监听");
    }
}

```

### 方法二：使用注解@WebListener

```java
@WebListener
public class demo01Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("开始监听");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("结束监听");
    }
}
```

# 前后端交互

## get请求

> 1. 后端localhost:8080,前端localhost:8081，需要跨域
> 2. `req.getParameter("name")`是根据前端axios请求参数params中的属性来的
> 3. 后端设置响应编码和内容类型

```vue
<template>
  <div>
    <input type="text" v-model="name" ><br/>
    <input type="text" v-model="age"><br/>
    <input type="submit" value="提交" @click="send"><br/>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      name:'lz',
      age:21
    }
  },
  methods:{
    send(){
      axios({
        method:'get',
        url: "http://localhost:8081/day11/demo01",
        params: {
          name:this.name,
          age:this.age
        }
      }).then((value)=>{
        console.log(value.data)//好的，收到了，给你响应了！！！
      }).catch((reason)=>{
        console.log(reason)
      })
    }

  }

}
</script>
```

```java
@WebServlet("/demo01")
public class demo01_get extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        接收前端发送来的请求
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String age = req.getParameter("age");
        System.out.println("name = " + name);
        System.out.println("age = " + age);


//        给前端响应
        // 设置响应内容的编码
        resp.setCharacterEncoding("UTF-8");
        // 设置响应内容的类型（普通文本格式）
        resp.setContentType("text/html;charset=utf-8");
        //这两种都可以设置响应
//        resp.getWriter().println("好的，收到了，给你响应了！！！");
        resp.getWriter().write("好的，收到了，给你响应了！！！");

    }
}

```

## post请求

> 1. 前端对象传到后端的格式是json字符串
> 2. 后端将json字符串响应给前端收到的是对象

```vue
<template>
  <div>
    <input type="text" v-model="name" ><br/>
    <input type="text" v-model="age"><br/>
    <input type="submit" value="提交" @click="send"><br/>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  data() {
    return {
      name:'lz',
      age:21
    }
  },
  methods:{
    send(){
      axios({
        method:'post',
        url: "http://localhost:8081/day11/demo02",
        data: {
          name:this.name,
          age:this.age
        }
      }).then((value)=>{
        console.log(value.data)
      }).catch((reason)=>{
        console.log(reason)
      })
    }

  }

}
</script>

```

```java
@WebServlet("/demo02")
public class demo02_post extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        接收前端的请求
        req.setCharacterEncoding("UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();
        int i;
        while ((i = reader.read()) != -1) {
            stringBuilder.append((char) i);
        }
        String str = stringBuilder.toString();


//        将接收的json字符串请求转成user对象
        Gson gson = new Gson();
        user user = gson.fromJson(str, user.class);
        System.out.println(user);

//        将json字符串响应给前端
        String userStr = gson.toJson(user);
        resp.setCharacterEncoding("UTF-8");
        // 设置响应内容的类型（json格式）
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(userStr);
    }
}

```
