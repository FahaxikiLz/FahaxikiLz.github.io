---
title: JavaWeb（旧）
date: 2022-8-31 15:10:56
update: 2022-10-17 19:18:30
categories:
- JavaWeb
tags:
- JavaWeb
---

# 一、使用IDEA新建JavaWeb项目

> 注意：version 3.0及以上可以使用@WebServlet()注解，相对配置而言简单

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222002120835.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

# 二、Servlet概述

> Servlet是使用Java语言编写的运行在服务器端的程序。狭义的Servlet是指Java语言实现的一个接口，广义的**Servlet是指任何实现了这个Servlet接口的类**，一般情况下，人们将Servlet 理解为后者。**Servlet主要用于处理客户端传来的HTTP请求，并返回一个响应，它能够处理的 请求有doGet()和doPost()等方法。**

## 1、Servlet接口及其实现方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200221235439165.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
```go
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/demo01")
public class HelloWorldServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //得到输出流PrinterWriter对象，Servlet使用输出流来产生响应
        PrintWriter out = response.getWriter();
        //使用输出流对象向客户端发送字符数据
        out.print("hello world");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}


```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222134954524.png)
### 1.1、ServletConfig接口

> 在Servlet运行期间，经常需要一些辅助信息，例如，文件使用的编码、使用Servlet程序 的共享等，这些信息可以在web.xml文件中使用一个或多个\<init- param>元素进行配置。当 Tomcat初始化一个Servlet时，会将该Servlet的配置信息封装到一个ServletConfig对象中， 通过调用init(ServletConfigconfig)方法将ServletConfig 对象传递给Servlet。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222221252551.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <servlet>
        <servlet-name>testServlet</servlet-name>
        <servlet-class>
            TestServlet
        </servlet-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>testServlet</servlet-name>
        <url-pattern>testServlet</url-pattern>
    </servlet-mapping>
</web-app>
```

```go
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class TestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        //获得ServletConfig对象
        ServletConfig config = this.getServletConfig();
        //获得参数名为encoding对应的参数值
        String param = config.getInitParameter("encoding");
        out.print("encoding"+param);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222223648328.png)

### 1.2、ServletContext接口

> 当Servlet 容器启动时，会为每个Web应用创建一个唯一的ServletContext对象代表当前Web应用，该对象不仅封装了当前Web应用的所有信息，而且实现了多个Servlet之间数据的共享

```go
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    
    <context-param>
        <param-name>companyName</param-name>
        <param-value>itcast</param-value>
    </context-param>
    <context-param>
        <param-name>address</param-name>
        <param-value>beijing</param-value>
    </context-param>
    <servlet>
        <servlet-name>testServlet02</servlet-name>
        <servlet-class>TestServlet02</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>testServlet02</servlet-name>
        <url-pattern>testServlet02</url-pattern>
    </servlet-mapping>
</web-app>
```

```go
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class TestServlet02 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        //得到ServletContext对象
        ServletContext context = this.getServletContext();
        //得到包含所有初始化参数名的Enumeration对象
        Enumeration<String> paramNames = context.getInitParameterNames();
        out.print("all thie paramNames and paramValue are following");
        //遍历所有的初始化参数名，得到相应的参数值并打印
        while(paramNames.hasMoreElements()){
            String name = paramNames.nextElement();
            String value = context.getInitParameter(name);
            out.println(name +":"+value);
            out.println("<br/>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020022222594093.png)

## 2、实现多个Servlet对象共享数据

> 由于一个Web应用中的所有Servlet共享同一个ServletContext对象,因此, ServletContext对象的域属性可以被该Web应用中的所有Servlet访问。在ServletContext接口中定义了分别用于增加、删除、设置ServletContext域属性的4个方法，如表3-4所示。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222230046373.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/testServlet03")
public class TestServlet03 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        //通过setAttribute()方法设置属性值
        context.setAttribute("data","this servlet save data");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

```go
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/testServlet04")
public class TestServlet04 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ServletContext context = this.getServletContext();
        //通过getAttribute()方法获取属性值
        String data = (String) context.getAttribute("data");
        out.println(data);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request,response);
    }
}

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222231027854.png)

## 3、读取Web应用下的资源文件

> 在实际开发中，有时候可能会需要读取Web应用中的一些资源文件，比如配置文件、图片等。

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020022223144175.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
Company = itcast
Address = Beijing
```

```go
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet(name = "TestServlet05")
public class TestServlet05 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        ServletContext context = this.getServletContext();
        PrintWriter out = response.getWriter();
        //获取相对路径的输入流对象
        InputStream in = context.getResourceAsStream("/WEB-INF/itcast.properties");
        Properties pros = new Properties();
        pros.load(in);
        out.println("Company="+pros.getProperty("Company")+"<br/>");
        out.println("Address="+pros.getProperty("Address")+"<br/>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

```go
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

@WebServlet("/testServlet06")
public class TestServlet06 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        ServletContext context = this.getServletContext();
        //获取文件绝对路径
        String path = context.getRealPath("WEB-INF/itcast.properties");
        FileInputStream in = new FileInputStream(path);
        Properties pros = new Properties();
        pros.load(in);
        out.println("Company="+pros.getProperty("Company")+"<br/>");
        out.println("Address="+pros.getProperty("Address")+"<br/>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222232234663.png)

# 三、请求和响应

## 1、HttpServletResponse对象

### 1.1、发送状态码相关方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222234052763.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

### 1.2、发送响应消息头相关的方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222234337623.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

> 需要注意的是，在表4-1列举的一系列方法中，addHeader()、setHeader()、addIntHeader()、setIntHeader()方法都是用于设置各种头字段的，而setContentType()、setl ocale()和setCharacterEncoding()方法用于设置字符编码,这些设置字符编码的方法可以有效解决乱码问题。

### 1.3、发送响应消息体相关方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222234627132.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/printServlet")
public class PrintServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data ="itcast";
        //获取字节输出流对象
        ServletOutputStream out = response.getOutputStream();
        out.write(data.getBytes());//输出消息
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200222235304234.png)

```go
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/printServlet02")
public class PrintServlet02 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data ="itcast";
        //获取字符输出流对象
        PrintWriter out = response.getWriter();
        out.write(data);//输出消息
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020022223555056.png)
## 2、HttpServletResponse应用
### 2.1、解决中文乱码问题
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020022223571188.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

### 2.2、实现网页定时刷新并跳转

> response对象的setHeader()方法实现了网页的定时刷新并跳转的功能

```go
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/refreshServlet")
public class RefreshServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //2秒后刷新并跳转到百度
        response.setHeader("Refresh","2;URL=http://www.baidu.com");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
```

```go
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/refreshServlet02")
public class RefreshServlet02 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //每隔3秒定时刷新当前页面
        response.setHeader("Refresh","3");
        response.getWriter().println(new java.util.Date());//输出当前时间

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

### 2.3、实现请求重定向

> 登录成功自动跳转到welcome.html,登录失败自动跳转login.html

```go
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <!--注意：此处的action路径谢loginServlet，如果写LoginServlet会找不到路径。-->
    <form action="loginServlet" method="post">
        用户名：<input type="text" name="username"/><br>
        密码：<input type="password" name="password"/><br>
        <input type="submit" value="登录"/>
    </form>

</body>
</html>
```

```go
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    欢迎，登录成功！！！

</body>
</html>
```

```go
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html;charset=utf-8");
            //用HttpServletRequest对象的getParameter（）方法获取用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (("root").equals(username)&&("123").equals(password)){
            response.sendRedirect("welcome.html");
        }else{
            response.sendRedirect("login.html");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

## 3、HttpServletResquest对象

### 3.1、获取请求行信息的相关方法

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223221051851.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
package request;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/requestLineServlet")
public class RequestLineServlet extends HttpServlet {
	public void doGet(HttpServletRequest request,
        HttpServletResponse response)throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		// 获取请求行的相关信息
		out.println("getMethod : " + request.getMethod() + "<br>");
		out.println("getRequestURI : " + request.getRequestURI() + "<br>");
		out.println("getQueryString:"+request.getQueryString() + "<br>");
		out.println("getProtocol : " + request.getProtocol() + "<br>");
		out.println("getContextPath:"+request.getContextPath() + "<br>");
		out.println("getPathInfo : " + request.getPathInfo() + "<br>");
		out.println("getPathTranslated : " + request.getPathTranslated() + "<br>");
		out.println("getServletPath:"+request.getServletPath() + "<br>");
		out.println("getRemoteAddr : " + request.getRemoteAddr() + "<br>");
		out.println("getRemoteHost : " + request.getRemoteHost() + "<br>");
		out.println("getRemotePort : " + request.getRemotePort() + "<br>");
		out.println("getLocalAddr : " + request.getLocalAddr() + "<br>");
		out.println("getLocalName : " + request.getLocalName() + "<br>");
		out.println("getLocalPort : " + request.getLocalPort() + "<br>");
		out.println("getServerName : " + request.getServerName() + "<br>");
		out.println("getServerPort : " + request.getServerPort() + "<br>");
		out.println("getScheme : " + request.getScheme() + "<br>");
		out.println("getRequestURL : " + request.getRequestURL() + "<br>");
	}
	public void doPost(HttpServletRequest request, 
        HttpServletResponse response)throws ServletException, IOException {
		doGet(request, response);
	}
}

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223221648545.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

### 3.2、获取请求消息头的相关信息

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223222225107.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
package request;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/requestHeadersServlet")
public class RequestHeadersServlet extends HttpServlet {
	public void doGet(HttpServletRequest request,
        HttpServletResponse response)throws ServletException, IOException {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
	         // 获取请求消息中所有头字段
			Enumeration headerNames = request.getHeaderNames();
			// 使用循环遍历所有请求头，并通过getHeader()方法获取一个指定名称的头字段
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				out.print(headerName + " : "
	                       + request.getHeader(headerName)+ "<br>");
			}
		}
		public void doPost(HttpServletRequest request,
	        HttpServletResponse response)throws ServletException, IOException {
			doGet(request, response);
		}
	}

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223222832608.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

### 3.3、防盗链
```go
package request;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/downManagerServlet")
public class DownManagerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        //获取referer头的值
        String referer = request.getHeader("referer");
        //获取访问地址
        String sitePart = "http://+request.getServerName()";
        //判断referer头是否为空，这个头的首地址是否以sitePart开始的
        if (referer !=null &&referer.startsWith(sitePart)){
            //处理正在下载的请求
            out.println("dealing download...");
        }else{
            //非法下载请求跳转到download.html页面
            RequestDispatcher rd = request.getRequestDispatcher("/download.html");
            rd.forward(request,response);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
```

## 4、HttpServletResquest应用

### 4.1、获取请求参数
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200223231130151.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="requestParamsServlet" method="post">
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
    爱好：<input type="checkbox" name="hobby" value="sing">唱
    <input type="checkbox" name="hobby" value="dance">跳
    <input type="checkbox" name="hobby" value="rap">rap
    <input type="checkbox" name="hobby" value="basketball">篮球
    <input type="submit" value="提交">
</form>

</body>
</html>
```

```go
package request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/requestParamsServlet")
public class RequestParamsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("用户名："+username);
        System.out.println("密码："+password);
        String[] hobbies = request.getParameterValues("hobby");
        System.out.println("爱好：");
        for (int i= 0;i<hobbies.length;i++){
            System.out.print(hobbies[i]+",");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200225142352494.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200225142446162.png)

### 4.2、通过Request对象传递数据
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020022514280063.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

## 5、RequestDiapatcher对象的应用

> 当一个Web资源收到客户端的请求后，如果希望服务器通知另外一个资源去处理请求，这时，除了使用sendRedirect()方法实现请求重定向外，还可以通过RequestDispatcher接口的实例对象来实现
>
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200225143016651.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

### 5.1、请求转发

> 在Servlet中，如果当前Web资源不想处理请求时，可以通过forward()方法将当前请求传递给其他的Web资源进行处理，这种方式称为请求转发。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200225143136289.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
package dispatcher;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/requestForwardServlet")
public class RequestForwardServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        // 将数据存储到request对象中
        request.setAttribute("company", "北京传智播客教育有限公司");
        RequestDispatcher dispatcher = request
                .getRequestDispatcher("/resultServlet");
        dispatcher.forward(request, response);
    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)throws ServletException, IOException {
        doGet(request, response);
    }
}

```

```go
package dispatcher;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/resultServlet")
public class ResultServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        // 获取PrintWriter对象用于输出信息
        PrintWriter out = response.getWriter();
        // 获取request请求对象中保存的数据
        String company = (String) request.getAttribute("company");
        if (company != null) {
            out.println("公司名称：" + company + "<br>");
        }
    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)throws ServletException, IOException {
        doGet(request, response);
    }
}

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200225145747839.png)
### 5.2、请求包含

> 请求包含指的是使用include()方法将Servlet请求转发给其他Web资源进行处理，与请求转发不同的是，在请求包含返回的响应消息中，既包含了当前Servlet的响应消息，也包含了其 他Web资源所作出的响应消息。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200225150050674.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
package request;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/includingServlet")
public class IncludingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = request.getRequestDispatcher("/includedServlet?p1=abc");
        out.println("before including"+"<br/>");
        rd.include(request,response);
        out.println("after including"+"<br/>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

```go
package request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/includedServlet")
public class IncludedServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        PrintWriter out = response.getWriter();
        out.println("中国"+"<br/>");
        out.println("URI:"+request.getRequestURI()+"<br/>");
        out.println("QueryString:"+request.getQueryString()+"<br/>");
        out.println("parameter p1:"+request.getParameter("p1")+"<br/>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200225151548794.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

> 从第一次运行结果中可以看到，输出的中文字符出现了乱码，说明在IncludedServlet类中，用于设置响应字符编码的第9行代码没有起到作用。这是因为浏览器在请求IncludingServlet时，用于封装响应消息的HttpServletResponse对象已经创建，该对象在编码时采用的是默认的ISO- -8859-1,所以，当客户端对接收到的数据进行解码时, Web服务器会继续保持调用HttpServletResponse 对象中的信息，从而使IncludedServlet中的输出内容发生乱码。为了解决第一次运行结果所示的乱码问题，接下来，对IncludingServlet 类进行修改，增加一行代码来设置浏览器显示内容时的编码，具体如下:
```go
 response.setContentType("text/html;charset=utf-8");
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020022515181877.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
# 四、会话及其会话技术

> Web应用中的会话过程类似于生活中的打电话过程，它指的是一个客户端(浏览器)与Web 服务器之间连续发生的一系列请求和响应过程，例如，一个用户在某网站上的整个购物过程就 是一个会话。

## 1、Cookie对象

### 1.1、什么是Cookie

> Cookie是一种会话技术， 它用于将会话过程中的数据保存到用户的浏览器中，从而使浏览 器和服务器可以更好地进行数据交互。
> 当用户通过浏览器访问Web服务器时，服务器会给客户端发送一些信息，这些信息都保存在Cookie 中。这样，当该浏览器再次访问服务器时，都会在请求头中将Cookie 发送给服务器，方便服务器对浏览器做出正确的响应。服务器向客户端发送Cookie时，会在HTTP响应头字段中增加Set- -Cookie响应头字段。
```go
Set-Cookie: user=itcast; Path=/;
```
> 在上述示例中，user表示Cookie的名称，itcast 表示Cookie的值，Path 表示Cookie的属性。需要注意的是，Cookie 必须以键值对的形式存在，其属性可以有多个，但这些属性之间必 须用分号和空格分隔。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200227202518980.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
> 图5-1描述了Cookie在浏览器和服务器之间的传输过程。当用户第1次访问服务器时，服 务器会在响应消息中增加Set-Cookie头字段，将用户信息以Cookie的形式发送给浏览器。一旦用户浏览器接受了服务器发送的Cookie信息，就会将它保存在浏览器的缓冲区中。这样，当浏览器后续访问该服务器时,都会在请求消息中将用户信息以Cookie的形式发送给Web服务器，从而使服务器端分辨出当前请求是由哪个用户发出的。

### 1.2、Cookie API
> 为了封装Cookie信息，在Servlet API中提供了一个javax.servlet.http.Cookie类，该类包含了生成Cookie信息和提取Cookie信息各个属性的方法。
#### 1.2.1、构造方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200227203242929.png)
#### 1.2.1、Cookie类的常用方法
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200227203340102.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

##### 1.2.1.1、setMaxAge ( int expiry )和getMaxAge()方法
> 上面的这两个方法用于设置和返回Cookie在浏览器上保持有效的秒数。如果设置的值为一个正整数时，浏览器会将Cookie信息保存在本地硬盘中。从当前时间开始，在没有超过指定的 秒数之前,这个Cookie都保持有效,并且同一台计算机上运行的该浏览器都可以使用这个Cookie信息。如果设置值为负整数时，浏览器会将Cookie信息保存在缓存中，当浏览器关闭时, Cookie信息会被删除。如果设置值为0时，则表示通知浏览器立即删除这个Cookie信息。默认情况下， Max- -Age属性的值是-1。

##### 1.2.1.2、setPath ( String uri )和getPath()方法
> 上面的这两个方法是针对Cookie的Path属性的。如果创建的某个Cookie对象没有设置Path属性，那么该Cookie只对当前访问路径所属的目录及其子目录有效。如果想让某个Cookie项 对站点的所有目录下的访问路径都有效，应调用Cookie对象的setPath()方法将其Path属性设 置为“/”。

##### 1.2.1.3、setDomain ( String pattern )和getDomain()方法
> 上面的这两个方法是针对Cookie的domain属性的。domain属性是用来指定浏览器访问的域。例如，传智播客的域为"itcast.cn"。 那么，当设置domain属性时，其值必须以“."开头，如domain=.itcast.cn。默认情况下，domain属性的值为当前主机名，浏览器在访问当前主机下的资源时，都会将Cookie信息回送给服务器。需要注意的是，domain 属性的值是不区分大小 写的。

## 2、显示用户上次访问时间

```go
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/lastAccessServlet")
public class LastAccessServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String lastAccessTime = null;
        //获取所有的cookie，并将这些cookie存放在数组中
        Cookie[] cookies = request.getCookies();
        //遍历cookie数组
        for (int i =0;cookies !=null&&i<cookies.length;i++){
            if ("lastAccess".equals(cookies[i].getName())){
                //如果cookie的名称为lastAccess,则获取改cookie的值
                lastAccessTime = cookies[i].getValue();
                break;
            }
        }
        //判断是否存在名称为lastAccess的cookie
        if (lastAccessTime == null){
            response.getWriter().print("您是首次访问本站");
        }else{
            response.getWriter().print("您上次访问的时间是："+lastAccessTime);
        }
        //创建cookie，将当前时间作为cookie的值发送个客户端
        String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        Cookie cookie = new Cookie("lastAccess",currentTime);
        //发送cookie
        response.addCookie(cookie);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200227212331851.png)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200227212345660.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

## 3、Session对象
> Cookie技术可以将用户的信息保存在各自的浏览器中，并且可以在多次请求下实现数据的共享。但是,如果传递的信息比较多，使用Cookie技术显然会增大服务器端程序处理的难度，这时，可以使用Session技术。Session是一种将会话数据保存到服务器端的技术。

### 3.1、什么是Session

> 当客户端后续访问服务器时，只要将标识号传递给服务器，服务器就能判断出该请求是哪个客户端发送的，从而选择与之对应的Session对象为其服务。需要注意的是，由于客户端需要接收、记录和回送Session对象的ID，因此，通常情况下，Session是借助Cookie技术来传递ID属性的。
>
> 在图5- -5中，用户甲和乙都调用buyServlet将商品添加到购物车中，调用payServlet 进行 商品结算。由于甲和乙购买商品的过程类似，在此，以用户甲为例进行详细说明。当用户甲访问购物网站时，服务器为甲创建了一个Session对象(相当于购物车)。当甲将Nokia手机添加到购物车时，Nokia手机的信息便存放到了Session对象中。同时，服务器将Session对象的ID 属性以Cookie(Set-Cookie: JSESSIONID=111)的形式返回给甲的浏览器。当甲完成购物进行结账时，需要向服务器发送结账请求，这时，浏览器自动在请求消息头中将Cookie (Cookie:
> JSESSIONID=111)信息回送给服务器,服务器根据ID属性找到为用户甲所创建的Session对象，并将Session对象中所存放的Nokia手机信息取出进行结算。
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200227214549248.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

### 3.2、HttpSession API
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200227214742926.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020022721530120.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

### 3.3、Session超时管理

> 当客户端第1次访问某个能开启会话功能的资源时，Web服务器就会创建一个 与该客户端对应的HttpSession对象。在HTTP协议中，Web服务器无法判断当前的客户端浏览器是否还会继续访问，也无法检测客户端浏览器是否关闭。所以，即使客户端已经离开或关闭了浏览器，Web服务器还要保留与之对应的HttpSession对象。随着时间的推移，这些不再使用的HttpSession对象会在Web服务器中积累的越来越多，从而使Web服务器内存耗尽。
> 为了解决上面的问题，Web服务器采用了“超时限制”的办法来判断客户端是否还在继续 访问。在一定时间内，如果某个客户端一直没有请求访问，那么，Web服务器就会认为该客户 端已经结束请求，并且将与该客户端会话所对应的HttpSession对象变成垃圾对象，等待垃圾收集器将其从内存中彻底清除。反之，如果浏览器超时后，再次向服务器发出请求访问，那么， Web服务器则会创建一个新的HttpSession对象，并为其分配一个新的ID属性。

> * 方案一
>
> * 在tomcat服务器的conf/web.xml文件配置session的超时 
>
> 时间是分钟为单位,此时配置session的超时管理适用于所有的web应用
>

```go
<session-config>
 
 <session-timeout>20</session-timeout>
 
 </session-config
```
> * 方案二
>
> * 在当前的web应用的web.xml文件中配置session的超时管理,会覆盖tomcat服务器的web.xml文件中的配置
>

```go
 <session-config>

<!-- 配置session的超时管理,以分钟为单位 -->

<session-timeout>30</session-timeout>

</session-config>
```
> * 方案三
>
> 
>
```go
//设置一个秒数，这个秒数表示客户端在不发出请求时，session被Servlet引擎维持的最长时间。
session.setMaxInactiveInterval(5);
```
> 
>
> 优限级:
>
> 方案三 --->方案二---> 方案一

## 4、实现购物车案例
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200228225632736.png)
```go
package shopping_cart;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;

    public Book(){
    }
    public Book(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```

```go
package shopping_cart;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class BookDB {
    private  static Map<String,Book> books = new LinkedHashMap<String,Book>();
    static {
        books.put("1",new Book("1","javaweb开发"));
        books.put("2",new Book("2","jabc开发"));
        books.put("3",new Book("3","java基础"));
        books.put("4",new Book("4","struts开发" ));
        books.put("5",new Book("5","spring开发"));
    }
    //获得所有的图书
    public static Collection<Book> getAll(){
        return  books.values();
    }
    //根据指定的id获得图书
    public static  Book getBook(String id){
        return books.get(id);
    }
}

```

```go
package shopping_cart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet("/listBookServlet")
public class ListBookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        Collection<Book> books = BookDB.getAll();
        out.write("本站提供的图书有：<br/>");
        for (Book book:books){
            String url = "/chapter03/purchaseServlet?id="+book.getId();
            out.write(book.getName()+"<a href='"+url+"'>点击购买</a><br/>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

```go
package shopping_cart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/purchaseServlet")
public class PurchaseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得用户购买的商品
        String id = request.getParameter("id");
        if (id == null){
            //如果id为null，重定向到ListBookServlet页面
            String url ="/chapter03/listBookServlet";
            response.sendRedirect(url);
            return;
        }
        Book book = BookDB.getBook(id);
        //创建或者获得用户的Session对象
        HttpSession session = request.getSession();
        //从Session对象中获得用户的购物车
        List<Book> cart =(List)session.getAttribute("cart");
        if (cart == null){
            //首次购买，为用户创建一个购物车（List集合模拟购物车）
            cart = new ArrayList<Book>();
            //将购物车存入Session对象
            session.setAttribute("cart",cart);
        }
        //将商品放入购物车
        cart.add(book);
        //创建Cookie存放Session的标识号
        Cookie cookie = new Cookie("JSESSIONID",session.getId());
        cookie.setMaxAge(60*30);
        cookie.setPath("chapter03");
        response.addCookie(cookie);
        //重定向到购物车页面
        String url = "/chapter03/cartServlet";
        response.sendRedirect(url);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

```go
package shopping_cart;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/cartServlet")
public class CartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        //变量cart引用用户的购物车
        List<Book> cart = null;
        //变量pruFlag标记用户是否买过商品
        boolean purFlag = true;
        //获得用户的session
        HttpSession session = request.getSession(false);
        //如果session为null，purFlag置位false
        if (session == null){
            purFlag= false;
        }else {
            //获得用户购物车
            cart=(List) session.getAttribute("cart");
            //如果用的购物车为null，purFlag置位false
            if (cart == null){
                purFlag = false;
            }
        }
        /**
         * 如果purFlag为false，表明用户没有购买图书，重定向到ListServlet页面
         */
        if (!purFlag){
            out.write("对不起！您还任何商品！！<br/>");
        }else{
            //否则显示用户购买图书的信息
            out.write("您购买的图书有：<br/>");
            double price = 0;
            for (Book book : cart) {
                out.write(book.getName()+"<br/>");
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

## 5、实现用户登录案例
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200228225613431.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
```go
package login;

public class User {
    private  String username;
    private  String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

```

```go
package login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/indexServlet")
public class IndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //解决乱码问题
        response.setContentType("text/html;charset=utf-8");
        //创建或者获取保存用户信息的Session对象
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null){
            response.getWriter().print("您还没有登录，请<a href='/chapter03/login.html'>登录</a>");
        }else{
            response.getWriter().print("您已登录，欢迎你，"+user.getUsername()+"!");
            response.getWriter().print("<a href='chapter03/logoutServlet'>退出</a>");

            //创建Cookie存放Session的标识号
            Cookie cookie = new Cookie("JSESSIONID", session.getId());
            cookie.setMaxAge(60*30);
            cookie.setPath("/chapter03");
            response.addCookie(cookie);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}

```

```go
package login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        PrintWriter pw = response.getWriter();

        if (("itcast").equals(username)&&("123").equals(password)){
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            request.getSession().setAttribute("user",user);
            response.sendRedirect("/chapter03/indexServlet");
        }else{
            pw.write("用户名或密码错误，登录失败");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

```go
package login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //将Session对象中的User对象移除
        request.getSession().removeAttribute("user");
        response.sendRedirect("/chapter03/indexServlet");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

### 5.1、用Session实现一次性验证码

```go
import java.io.*;
 import javax.servlet.*;
 import javax.servlet.http.*;
 import java.awt.*;
 import java.awt.image.*;
 import javax.imageio.ImageIO;
 public class CheckServlet extends HttpServlet
 {
 	private static int WIDTH = 60; //验证码图片宽度
 	private static int HEIGHT = 20; //验证码图片高度
 public void doGet(HttpServletRequest request,HttpServletResponse response) 
 			throws ServletException,IOException{		
 		HttpSession session = request.getSession();
 		response.setContentType("image/jpeg");
 		ServletOutputStream sos = response.getOutputStream();
 		//设置浏览器不要缓存此图片
 		response.setHeader("Pragma","No-cache");
 		response.setHeader("Cache-Control","no-cache");
 		response.setDateHeader("Expires", 0);
 		//创建内存图象并获得其图形上下文
 		BufferedImage image = 
 			new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); 
 		Graphics g = image.getGraphics();
 		//产生随机的认证码
 		char [] rands = generateCheckCode();
 		//产生图像
 		drawBackground(g);
 		drawRands(g,rands);
 		//结束图像的绘制过程，完成图像
 		g.dispose();
 		//将图像输出到客户端
 		ByteArrayOutputStream bos = new ByteArrayOutputStream();
 		ImageIO.write(image, "JPEG", bos);
 		byte [] buf = bos.toByteArray();
 		response.setContentLength(buf.length);
 		//下面的语句也可写成：bos.writeTo(sos);
 		sos.write(buf);
 		bos.close();
 		sos.close();
 		//将当前验证码存入到Session中
 		session.setAttribute("check_code",new String(rands));
 		//直接使用下面的代码将有问题，Session对象必须在提交响应前获得
 	//request.getSession().setAttribute("check_code",new String(rands));
 	}
        //生成一个4字符的验证码
 	private char [] generateCheckCode()
 	{
 		//定义验证码的字符表
 		String chars = "0123456789abcdefghijklmnopqrstuvwxyz";
 		char [] rands = new char[4];
 		for(int i=0; i<4; i++)
 		{
 			int rand = (int)(Math.random() * 36);
 			rands[i] = chars.charAt(rand);
 		}
 		return rands;
 	}
 	private void drawRands(Graphics g , char [] rands)
 	{
 		g.setColor(Color.BLACK);
 		g.setFont(new Font(null,Font.ITALIC|Font.BOLD,18));
 		//在不同的高度上输出验证码的每个字符		
 		g.drawString("" + rands[0],1,17);
 		g.drawString("" + rands[1],16,15);
 		g.drawString("" + rands[2],31,18);
 		g.drawString("" + rands[3],46,16);
 		System.out.println(rands);
 	}
 	private void drawBackground(Graphics g)
 	{
  		//画背景
 		g.setColor(new Color(0xDCDCDC));
 		g.fillRect(0, 0, WIDTH, HEIGHT);
 		//随机产生120个干扰点
 		for(int i=0; i<120; i++)
 		{
 			int x = (int)(Math.random() * WIDTH);
 			int y = (int)(Math.random() * HEIGHT);
 			int red = (int)(Math.random() * 255);
 			int green = (int)(Math.random() * 255);
 			int blue = (int)(Math.random() * 255);
 			g.setColor(new Color(red,green,blue));		
 			g.drawOval(x,y,1,0);
 		}
 	}
 }

```

# 五、JSP技术

## 1、JSP基础语法

### 1.1、jsp脚本元素

> JSP脚本元素是指嵌套在\<%和%>之中的一条或多条Java程序代码。通过JSP脚本元素可 以将Java代码嵌入HTML页面中，所有可执行的Java代码，都可以通过JSP脚本来执行。
> JSP脚本元素主要包含如下3种类型:
> - JSP Scriptlets
> - JSP 声明语句
> - JSP 表达式

#### 1.1.1、JSP Scriptlets

> 定义java代码，在service中可以定义什么，这里就可以定义什么

```go
<% java代码（变量、方法、表达式等） %>
```
#### 1.1.2、JSP 声明语句
> JSP声明语句中定义的都是成员方法、成员变量、静态方法、静态变量、静态代码块等。

```go
<%!
		定义的变量或方法等
%>
```
#### 1.1.3、JSP 表达式
> 用于将程序数据输出到客户端。
```go
<%= expression %>
```

```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/2/26
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>

    <%!
    int a = 1,b=2;//定义两个变量a,b
    %>

    <%!
        public String print(){//定义print方法
            String str = "itcast";//方法内定义的变量str
            return str;
        }
    %>

<body>

    <%
        out.println(a+b);   //输出两个变量的和
    %>

    <%
        out.println(print());//调用print（）方法，输出其返回值
    %>

    <%=
        a+b
    %>

    <%=
        print()
    %>

</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226161505456.png)

## 2、JSP指令
### 2.1、page指令

> 在JSP页面中，经常需要对页面的某些特性进行描述，例如，页面的编码方式、JSP页面 采用的语言等，这时，可以通过page指令来实现。
```go
<%@ page 属性名 1 = "属性值1" 属性名2 = "属性值2"  ... %>
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226161944446.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
> 注意：page指令常见属性中，除了import属性外，其他的属性都只能出现一次，否则会编译失败。page指令的属性名称都是区分大小写的。

**示例：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226162301795.png)

### 2.2、include指令

> 在实际开发时，有时需要在JSP页面静态包含- -个文件，例如HTML文件、文本文件等， 这时，可以通过include指令来实现。

```go
<%@ include file = "被包含的文件地址"%>
```

```go
<%@ page import="javax.xml.crypto.Data" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/2/26
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>date.jsp</title>
</head>
<body>
    <% out.println(new java.util.Date().toLocaleString()); %>

</body>
</html>

```

```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/2/26
  Time: 16:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>include.jsp</title>
</head>
<body>
    欢迎你，现在的时间是：
    <%@ include file="date.jsp" %>

</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020022616301552.png)
>注意： file属性的设置值必须使用相对路径，如果以“/” 开头，表示相对于当前Web应用程 序的根目录(注意不是站点根目录)，否则，表示相对于当前文件。需要注意的是，这里的file 属性指定的相对路径是相对于文件(file ),而不是相对于页面( page )。

## 3、JSP隐式对象
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226163649554.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

### 3.1、out

```go
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        out.println("first line<br/>");
        response.getWriter().println("second line<br/>");
    %>

</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226164344561.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

#### 3.1.1、使用page指令设置out对象的缓冲区大小

> 从前面运行可以看出，尽管out.printIn();语句位于response .getWriter().printIn();语句之前，但它的输出内容却在后面。由此可以说明，out 对象通过print 语句写入数据后，直到整个JSP页面结束，out对象中输入缓冲区的数据(即first line )才真正写入到Serlvet引擎提供的缓 冲区中。而response.getWriter() printin);语句则是直接把内容(即second line )写入Servlet引擎提供的缓冲区中，Servlet引擎按照缓冲区中的数据存放顺序输出内容。

```go
<%@ page contentType="text/html;charset=UTF-8" language="java"
         buffer="0kb" %><%--设置一个buffer,还有注意jsp中注释的写法--%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        out.println("first line<br/>");
        response.getWriter().println("second line<br/>");
    %>

</body>
</html>

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226165115546.png)

### 3.2、pageContext对象

> 在JSP页面中，使用pageContext对象可以获取JSP的其他8个隐式对象。pageContext对象是javax.serlet.jsp.PageContext类的实例对象，它代表当前JSP页面的运行环境，并提供了一系列用于获取其他隐式对象的方法。
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226165536678.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

> pageContext对象不仅提供了获取隐式对象的方法,还提供了存储数据的功能。pageContext 对象存储数据是通过操作属性来实现的。
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226165655917.png)
> 在pageContext对象操作属性的相关方法，参数name指定的是属性名 称，参数scope指定的是属性的作用范围。pageContext对象的作用范围有4个值：
> - pageContext.PAGE_ SCOPE:表示页面范围
> - pageContext.REQUEST_ SCOPE:表示请求范围
> - pageContext SESSION_ SCOPE:表示会话范围
> - pageContext. APPLICATION_ SCOPE:表示Web应用程序范围
>
> 需要注意的是，当使用findAttribute()方 法查找名称为 name的属性时，会按照page. request、session和application 的顺序依次进行查找，如果找到，则返回属性的名称，否则返 回null。

```go
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        //获取request对象
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        //设置page范围内属性
        pageContext.setAttribute("str","java",pageContext.PAGE_SCOPE);
        //设置request范围内属性
        req.setAttribute("str","java web");
        //获得的page范围属性
        String str1 = (String)pageContext.getAttribute("str",pageContext.PAGE_SCOPE);
        //获得的request范围属性
        String str2 = (String)pageContext.getAttribute("str",pageContext.REQUEST_SCOPE);
    %>
    <%= "page范围："+str1%><br/>
    <%= "request范围："+str2%><br/>
</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226173302144.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

### 3.3、exception对象

> 在JSP页面中，经常需要处理一些异常信息， 这时，可以通过exception 对象来实现。exception对象是java.lang.Exception类的实例对象，它用于封装JSP中抛出的异常信息。需要 注意的是，exception对象只有在错误处理页面才可以使用，即page指令中指定了属性\<%@ page isErrorPage="true"%>的页面。

```go

<%@ page contentType="text/html;charset=UTF-8" language="java"
        errorPage="error.jsp" %><%--设置错误页面--%>
<html>
<head>
    <title>exception.jsp</title>
</head>
<body>
    <%
        int a =3;
        int b = 0;
    %>
    输出结果为：<%=(a/b)%><%--此处会产生异常--%>
</body>
</html>

```

```go

<%@ page contentType="text/html;charset=UTF-8" language="java"
        isErrorPage="true" %><%--此处设置为true为开启--%>
<html>
<head>
    <title>error.jsp</title>
</head>
<body>
    <%--显示异常信息--%>
    <%=exception.getMessage()%>

</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226175614356.png)

## 4、JSP动作元素

### 4.1、\<jsp:include>动作元素

> 在JSP页面中，为了把其他资源的输出内容插入到当前JSP页面的输出内容中，JSP 技术 提供了\<jsp:include>动作元素

```go
<jsp:include page = "relativeURL" flush="true|false"/>
```
> 在上述语法格式中，page属性用于指定被引入资源的相对路径; flush属性用于指定是否将当前页面的输出内容刷新到客户端，默认情况下，flush属性的值为false。

```go
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>include.jsp</title>
</head>
<body>
    <%Thread.sleep(5000);%>
    include.jsp内的中文<br/>

</body>
</html>

```

```go
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>dynamicInclude.jsp</title>
</head>
<body>
    dynamicInclude.jsp内的中文
    <br>
    <jsp:include page="included.jsp" flush="true"/>
</body>
</html>

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200226183023828.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
> 启动Tomcat服务器,访问地址tpt/ocalhost:8080/chapter04/dynamicInclude.jsp"后，发现浏览器首先会显示dynamicInclude.jsp 页面中的输出内容，等待5秒后，才会显示included,jsp页面的输出内容。说明被引用的资源included jisp在当前JSP页面输出内容后才被调用。
> 修改dynamicInclude,jsp文件，将\<jsp:include> 动作元素中的flush属性设置为false,刷新浏览器，再次访问地址htpt:/ocalhost:8080/chapter04/dynamicInclude.jsp", 这时，浏览器等待5秒后,将dynamicInclude jsp和included.jsp页面的输出内容同时显示了出来。由此可见，Tomcat调用被引入的资源included.jsp时，并没有将当前JSP页面中已输出的内容刷新到客户端。

#### 4.1.1、include指令和\<jsp:include>标签的区别

> 虽然include指令和\<jsp:include>标签都能够包含-一个文件，但它们之间有 很大的区别:
>  - \<jsp:include>标签中要引入的资源和当前JSP页面是两个彼此独立的执行实体，即被动态引入的资源必须能够被Web容器独立执行。而include指令只能引入遵循JSP格式的文件，被引入文件与当前JSP文件需要共同合并才能翻译成一个Servlet源文件。
>  - \<jsp:include> 标签中引入的资源是在运行时才包含的，而且只包含运行结果。而include指令引入的资源是在编译时期包含的，包含的是源代码。
>  - \<jsp:include> 标签运行原理与RequestDispatcher include()方法类似，即被包含的页面不能改变响应状态码或者设置响应头，而include指令没有这方面的限制。

### 4.2、\<jsp:forward>动作元素

> \<jsp:forward>动作元素将当前请求转发到其他Web资源(HTML页面、JSP页面和Servlet等)，在执行请求转发之后的当前页面将不再执行，而是执行该元素指定的目标页面。
> <jsp:forward page = "relativeURL"/>

```go
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>jspforward.jsp</title>
</head>
<body>
    <jsp:forward page="welcome.jsp"/>

</body>
</html>

```

```go

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>welcome.jsp</title>
</head>
<body>
    你好，欢迎进入首页，当前访问时间是：
    <%
        out.print(new java.util.Date());
    %>

</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/2020022618281717.png)

# 六、EL表达式和JSTL
## 1、JavaBean

> JavaBean是Java开发语言中一个可以重复使用的软件组件，它本质上就是- -个 Java类。 为了规范JavaBean的开发，Sun公司发布了JavaBean的规范，它要求一个标准的JavaBean 组件需要遵循一定的编码规范。
>  - 它必须具有一个公共的、无参的构造方法，这个方法可以是编译器自动产生的默认构造方法。
>  - 它提供公共的setter方法和getter方法，让外部程序设置和获取JavaBean的属性。

```go
//大概就是我们说的实体类
public class Book {
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

```

>  - getName()方法:称为getter方法或者属性访问器，该方法以小写的get前缀开始，后属性名,属性名的第1个字母要大写，例如，nickName属性的getter方法为getNickName()。
>  - setName()方法:称为setter方法或者属性修改器，该方法必须以小写的set前缀开始，跟属性名,属性名的第1个字母要大写,例如,nickName属性的setter方法为setNickName()。

### 1.1、BeanUtils工具
使用BeanUtils工具类在官网下载需要的jar包（一个beanutils.jar包，一个logging.jar包）		[下载地址](http://commons.apache.org/proper/commons-beanutils/download_beanutils.cgi)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303123757271.png)

```go
package beanutils;

import org.apache.commons.beanutils.BeanUtils;
import java.util.HashMap;

public class BeanUtilsDemo {
    public static void main(String[] args) throws Exception{
        Person p = new Person();
        //使用BeanUtils为属性赋值
        BeanUtils.setProperty(p,"name","Jack");
        BeanUtils.setProperty(p,"age","10");
        //使用BeanUtils获取属性值
        String name = BeanUtils.getProperty(p, "name");
        String age = BeanUtils.getProperty(p, "age");
        System.out.println("我的名字是"+name+",我今年"+age+"岁了！");
        //创建map集合，用户存放属性及其属性值
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("age",10);
        //使用populate（）方法为对象的属性赋值
        BeanUtils.populate(p,map);
        //打印赋值后对象的信息
        System.out.println("姓名："+p.getName()+",年龄："+p.getAge());

    }
}

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303125835366.png)

## 2、EL表达式
> EL用来简化JSP页面的书写
```go
语法：${表达式}
```

```go
package EL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/myServlet")
public class MyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("username","itcast");
        request.setAttribute("password","123");
        request.getRequestDispatcher("/myjsp.jsp").forward(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}

```

```go
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    用户名:<%=request.getAttribute("username")%><br/>
    密码:<%=request.getAttribute("password")%><br/>
<hr/>
    使用EL表达式<br/>
    用户名：${username}<br/>
    密码：  ${password}<br/>
</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303130934705.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
### 2.1、EL中的标识符

> 在EL表达式中，经常需要使用一些符号来标记一些名称，如变量名、自定义函数名等，些符号被称为标识符。EL表达式中的标识符可以由任意的大小写字母、数字和下划线组成。
### 2.2、EL中的运算符
#### 2.2.1、点运算符（.）
> EL表达式中的点运算符，用于访问JSP页面中某些对象的属性，如JavaBean对象、List 集合、Array 数组等。
> `${customer.name}`
#### 2.2.2、方括号运算符（[ ]）
> EL表达式中的方括号运算符与点运算符的功能相同，都用于访问JSP页面中某些对象的属性。当获取的属性名中包含一些特殊符号，如“-”或“?"等并非字母或数字的符号，就只能使 用方括号运算符来访问该属性。
> `${user["My-Name"]}`
> 需要注意的是，在访问对象的属性时,通常情况都会使用点运算符作为简单的写法。但实际
> 上,方括号运算符比点运算符应用更广泛。接下来就对比一下这两种运算符在实际开发中的应用。
> - 点运算符和方 括号运算符在某种情况下可以互换，如γ{student.name} 等价于 ￥{student["name"]]。
> - 方括号运算符还可以访问 List集合或数组中指定索引的某个元素，如表达式y{users[]用
> 于访问集合或数组中第1个元素。在这种情况下，只能使用方括号运算符，而不能使用点运算符。
> - 方括号运算符和点运算符可以相互结合使用，例如，表达式y{users[0] userName)可以
> 访问集合或数组中的第1个元素的userName属性。
#### 2.2.3、算数运算符
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303132629179.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
#### 2.2.4、比较运算符
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303132710857.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
#### 2.2.5、逻辑运算符
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303132820797.png)
#### 2.2.6、empty运算符
> EL表达式中的empty运算符用于判断某个对象是否为null或" "，结果为布尔类型。
> `${empty var}`		
> - var变量不存在，即没有定义，例如表达式y{empty name), 如果不存在name变量，就
> 返回true;
> - var变量的值为null, 例如表达式Y {empty customer name},如果customer name的值
> 为null,就返回true;
> - var变量引用集合(Set、Map 和List )类型对象，并且在集合对象中不包含任何元素时，
> 则返回值为true。例如，如果表达式Y{empty list)中list 集合中没有任何元素，就返回true。
#### 2.2.7、条件运算符
> EL表达式中条件运算符用于执行某种条件判断，它类似于Java语言中的三元运算符。
> `${A?B:C}`
### 2.3、EL隐式对象
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303133242232.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
#### 2.3.1、pageContext对象
```go
${pageContext.response.characterEncoding}
```

```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/3
  Time: 14:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    请求URI为：${pageContext.request.requestURI}<br/>
    Content-Type响应头：${pageContext.response.contentType}<br/>
    服务器信息为:${pageContext.servletContext.serverInfo}<br/>
    Servlet注册名为：${pageContext.servletConfig.servletName}


</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303142543407.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
#### 2.3.2、Web域相关对象

> 在Web开发中，PageContext. HttpServletRequest. HttpSession 和ServletContext这4个对象之所以可以存储数据，是因为它们内部都定义了一个Map集合，这些Map集合是有一定作用范围的，例如，HttpServletRequest 对象存储的数据只在当前请求中可以获取到。我们习惯把这些Map集合称为域，这些Map集合所在的对象称为域对象。在EL表达式中，为了获取指 定域中的数据，提供了pageScope、
> requestScope、sessionScope和applicationScope4个隐 式对象。
> `${pageScope.userName}`
> `${requestScope.userName}`
> `${sessionScope.userName}`
> `${applicationScope.userName}`

```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/3
  Time: 14:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%pageContext.setAttribute("userName","itcast");%>
<%request.setAttribute("bookName","JavaWeb");%>
<%session.setAttribute("userName","itheima");%>
<%application.setAttribute("bookName","Java基础");%>
表达式\${pageScope.userName}的值为：${pageScope.userName}<br/>
表达式\${requestScope.userName}的值为：${requestScope.userName}<br/>
表达式\${sessionScope.userName}的值为：${sessionScope.userName}<br/>
表达式\${applicationScope.userName}的值为：${applicationScope.userName}<br/>
表达式\${userName}的值为：${userName}<br/>
</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303143738983.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

> 由运行结果可以看出,使用pageScope .requestScope、sessionScope和applicationScope 这4个隐式对象成功地获取到了相应JSP域对象中的属性值。需要注意的是，使用EL表达式获取某个域对象中的属性时，也可以不使用这些隐式对象来指定查找域，而是直接引用域中的属性名称即可，例如表达式Y{userName}就是在page、request. session. application 这4个作用域内按顺序依次查找userName属性的。
#### 2.3.3、param和paramValues对象
> 在JSP页面中，经常需要获取客户端传递的请求参数，为此，EL表达式提供了param和paramValues两个隐式对象，这两个隐式对象专门用于获取客户端访问JSP页面时传递的请求 参数。
> param对象用于获取请求参数的某个值，它是Map类型，与request.getParameter()方法
> 相同，在使用EL获取参数时，如果参数不存在，返回的是空字符串，而不是null。
> `${param.num}`
> 如果一个请求参数有多个值，可以使用paramValues对象来获取请求参数的所有值，该对象
> 用于返回请求参数所有值组成的数组。如果要获取某个请求参数的第1个值。
> `${paramValues.num[0]}`

```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/3
  Time: 14:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/param.jsp">
    num1:<input type="text" name="num1"><br/>
    num2:<input type="text" name="num"><br/>
    num3:<input type="text" name="num"><br/>
    <input type="submit" value="提交">&nbsp;&nbsp;
    <input type="submit" value="重置">
    <hr/>
    num1:${param.num1}<br/>
    num2:${paramValues.num[0]}<br/>
    num3:${paramValues.num[1]}<br/>
</form>
</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303145112103.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303145127671.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
#### 2.3.4、Cookie对象

> 在JSP开发中，经常需要获取客户端的Cookie信息，为此，在EL表达式中，提供了Cookie隐式对象,该对象是一个代表所有Cookie信息的Map集合,Map集合中元素的键为各个Cookie 的名称，值则为对应的Cookie对象。
> `获取cookie对象的信息：${cookie.userName}`
> `获取cookie对象的名称：${cookie.userName.name}`
> `获取cookie对象的值：${cookie.userName.value}`

```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/3
  Time: 14:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
Cookie对象的信息：<br/>
${cookie.userName}<br/>
Cookie对象的名称和值：<br/>
${cookie.userName.name}=${cookie.userName.value}
<%response.addCookie(new Cookie("userName","itcast"));%>
</body>
</html>

```

> 启动Tomcat 服务器，在浏览器地址栏中输入地址“https://localhost:8080/chapter05/cookie.jsp"访问cookie.jsp 页面，由于是浏览器第1次访问cookie.jsp页面，此时，服务器还 没有接收到名为userName的Cookie信息，因此，浏览器窗口中不会显示。接下来刷新浏览器，第2次访问cookie.jsp页面，此时浏览器窗口中显示的结果如下图。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303145807853.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

> 从上图中可以看出，浏览器窗口中显示了获取到的Cookie信息，这是因为第1次访问服务器时，服务器会向浏览器回写一个Cookie,此时的Cookie信息是存储在浏览器中的。当刷新浏览器，第2次访问cookie.jsp页面时，由于浏览器中已经存储了名为userName的Cookie信息，当再次访问相同资源时，浏览器会将此Cookie信息一同发送给服务器，这时使用表达式 ${cookie.userName.name}和${cookie.userName. value }便可以获取Cookie的名称和值。
## 3、JSTL
> 从JSP 1.1规范开始, JSP就支持使用自定义标签,使用自定义标签大大降低了JSP页面的复杂度，同时增强了代码的重用性。为此，许多Web应用厂商都定制了自身应用的标签库，然而同一功能的标签由不同的Web应用厂商制定可能是不同的，这就导致市面上出现了很多功能相同的标签，令网页制作者无从选择。为了解决这个问题，Sun公司制定了-套标准标签库 ( JavaServer Pages Standard Tag Library), 简称JSTL。
> JSTL虽然被称为标准标签库，而实际上这个标签库是由5个不同功能的标签库共同组成的。
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303160953982.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
> 需要导入jar包（一个jstl.jar包，一个standard.jar包）  [点击下载](http://archive.apache.org/dist/jakarta/taglibs/standard/binaries/)

### 1、测试JSTL
```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/3
  Time: 16:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <c:out value="Hello World!"></c:out>
</body>
</html>

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200303164815781.png)

### 2、JSTL中的Core标签库
#### 2.1、\<c:out>标签
![](https://img-blog.csdnimg.cn/20200306145607893.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/6
  Time: 14:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%--第一个out标签--%>
    userName属性的值为：
    <c:out value="${param.username}" default="unknown"/><br/>
    <%--第二个out标签--%>
    userName属性的值为：
    <c:out value="${param.username}">
        unknown
    </c:out>
</body>
</html>

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200306143000546.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

> 如果不想让\<c:out>标签输出默认值，可以在客户端访问c_out1.jsp页面时传递一个参数，在浏览器地址栏中输入http://ocalhost:8080/chapter05/c_out1.jsp?username=857857"。效果为下图。
> ![在这里插入图片描述](https://img-blog.csdnimg.cn/20200306143135970.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
##### 使用\<c:out>标签的escapeXml属性对特殊字符进行转义
```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/6
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:out value="${param.username}" escapeXml="true">
    <meta http-equiv="refresh" content="0;url=http://www.baidu.com"/>
</c:out>

</body>
</html>

```

> 浏览器窗口中显示的是百度网站的信息,这是因为在\<c:out>标签中将escapeXml的属性值设置为false,因此，\<c:out>标签
> 不会对特殊字符进行HTML转换，\<meta>标签便可以发挥作用，在访问c_out2.jsp页面时就会跳转到www.baidu.com网站。如果想对页面中输出的特殊字符进行转义，可以将escapeXml属性的值设置为true,将\<c:out>标签中escapeXml属性的值设置为true后，在JSP页面中输入的\<meta>标签便会进行HTML编码转换，最终以字符串的形式输出了。需要注意的是，如果在\<c:out>标签中不设置escapeXml属性，则该属性的默认值为true。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200306151141642.png)

####  2.2、\<c:if>标签
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200306145702319.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/6
  Time: 14:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:set value="1" var = "visitCount" property="visitCount"/>
<c:if test="${visitCount==1}">
    This is you first visit Welcome to the site!
</c:if>
</body>
</html>

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200306145407453.png)

> 使用了\<c:if>标签，当执行到\<c:if>标签时会通过test属性来判断表达式\${visitCount== 1)是否为true。如
> 果为true就输出标签体中的内容，否则输出空字符串。由于使用了\<c:set>标签将visitCount的值设置为1,因此，表达式${isitCount==1}的结果为true,便会输出\<c:if>标签体中的内容。
#### 2.3、<c:choose>标签
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200306150310987.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```go
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/3/6
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:choose>
    <c:when test ="${empty param.username}">
        unKnown user.
    </c:when>
    <c:when test="${param.username=='itcast'}">
        ${param.username} is manager
    </c:when>
    <c:otherwise>
        ${param.username} is employee
    </c:otherwise>
</c:choose>
</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200306150616608.png)
#### 2.4、\<c:forEach>标签
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200411134707123.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```java
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head></head>
<body>
        <%
            String[] fruits = { "apple", "orange", "grape", "banana" };
        %>

        String数组中的元素：<br />
        <c:forEach var="name" items="<%=fruits%>">
            ${name}<br />
        </c:forEach>

        <hr/>

        <%
            Map userMap = new HashMap();
            userMap.put("Tom", "123");
            userMap.put("Make", "123");
            userMap.put("Lina", "123");
        %>

        HashMap集合中的元素：<br />
        <c:forEach var="entry" items="<%=userMap%>">
            ${entry.key}&nbsp;${entry.value}<br />
        </c:forEach>
</body>
</html>

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200411140502427.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```java
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head></head>
<body>
        colorsList集合（指定迭代范围和步长）<br />
        <%
            List colorsList=new ArrayList();
            colorsList.add("red");
            colorsList.add("yellow");
            colorsList.add("blue");
            colorsList.add("green");
            colorsList.add("black");
        %>
        <c:forEach var="color" items="<%=colorsList%>" begin="1"
                   end="3" step="2">
            ${color}&nbsp;
        </c:forEach>
</body>
</html>

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200411140530345.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200411140735585.png)

```java
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head></head>
<body style="text-align: center;">
<%
    List userList = new ArrayList();
    userList.add("Tom");
    userList.add("Make");
    userList.add("Lina");
%>
<table border="1">
    <tr>
        <td>序号</td>
        <td>索引</td>
        <td>是否为第一个元素</td>
        <td>是否为最后一个元素</td>
        <td>元素的值</td>
    </tr>
    <c:forEach var="name" items="<%=userList%>" varStatus="status">
        <tr>
            <td>${status.count}</td>
            <td>${status.index}</td>
            <td>${status.first}</td>
            <td>${status.last}</td>
            <td>${name}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200411140954572.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
#### 2.5、\<c:param>标签和\<c:url>标签
![在这里插入图片描述](https://img-blog.csdnimg.cn/202003061517494.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)

```java
<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head></head>
<body>
	使用绝对路径构造URL:<br />
	<c:url var="myURL" 
     value="http://localhost:8080/chapter07/register.jsp">
		<c:param name="username" value="张三" />
		<c:param name="country" value="中国" />
	</c:url>
	<a href="${myURL}">register.jsp</a><br />
	使用相对路径构造URL:<br />
	<c:url var="myURL" 
     value="register.jsp?username=Tom&country=France" />
	<a href="${ myURL}">register.jsp</a>
</body>
</html>

```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200421102353499.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
# 七、Servlet高级
## 1、过滤器Filter
### 自动登录

```java
package automatic_login;

public class User {
	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}

```

```java
package automatic_login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        // 获得用户名和密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        // 检查用户名和密码
        if ("itcast".equals(username) && "123456".equals(password)) {
            // 登录成功
            // 将用户状态 user 对象存入 session域
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            request.getSession().setAttribute("user", user);
            // 发送自动登录的cookie
            String autoLogin = request.getParameter("autologin");
            if (autoLogin != null) {
                // 注意 cookie 中的密码要加密
                Cookie cookie = new Cookie("autologin", username + "-" + password);
                cookie.setMaxAge(Integer.parseInt(autoLogin));
                cookie.setPath(request.getContextPath());
                response.addCookie(cookie);
            }
            // 跳转至首页
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        } else {
            request.setAttribute("errerMsg", "用户名或密码错");
            request.getRequestDispatcher("/login.jsp")
                    .forward(request,response);
        }
    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

```

```java
package automatic_login;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebServlet("/*")
public class AutoLoginFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    public void doFilter(ServletRequest req, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        // 获得一个名为 autologin 的cookie
        Cookie[] cookies = request.getCookies();
        String autologin = null;
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            if ("autologin".equals(cookies[i].getName())) {
                // 找到了指定的cookie
                autologin = cookies[i].getValue();
                break;
            }
        }
        if (autologin != null) {
            // 做自动登录
            String[] parts = autologin.split("-");
            String username = parts[0];
            String password = parts[1];
            // 检查用户名和密码
            if ("itcast".equals(username)&& ("123456").equals(password)) {
                // 登录成功,将用户状态 user 对象存入 session域
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                request.getSession().setAttribute("user", user);
            }
        }
        // 放行
        chain.doFilter(request, response);
    }
    public void destroy() {
    }
}

```

```java
package automatic_login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logoutServlet")
public class LogoutServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        // 用户注销
        request.getSession().removeAttribute("user");
        // 从客户端删除自动登录的cookie
        Cookie cookie = new Cookie("autologin", "msg");
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.sendRedirect(request.getContextPath()+"/index.jsp");
    }
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

```

## 2、监听器Listenter
> 不再书写
# 八、文件上传和下载
## 1、实现文件上传
> 要实现Web开发中的文件.上传功能，通常需完成两步操作: -是在Web页面中添加上传输入项;二是在Servlet中读取上传文件的数据，并保存到本地硬盘中。
> 由于大多数文件的，上传都是通过表单的形式提交给服务器的，因此，要想在程序中实现文件上传的功能，首先要创建一个用于提交上传文件的表单页面。在页面中，需要使用\<input type="file">标签在Web页面中添加文件上传输入项。 \<input type= "file" >标签的使用需要注意以下两点。
>  - 必须要设置input输入项的name属性，否则浏览器将不会发送上传文件的数据。
>  - 必须将表单页面的method 属性设置为post 方式，enctype 属性设置为“multipart/ form-data"类型。
>
> 当浏览器通过表单提交上传文件时，由于文件数据都附带在HTTP请求消息体中，并且采用MIME类型(多用途互联网邮件扩展类型)进行描述，在后台使用request对象提供的getInputStream()方法可以读取到客户端提交过来的数据。但由于用户可能会同时上传多个文件，而在Servlet端直接读取上传数据，并分别解析出相应的文件数据是一项非常麻烦的工 作。为了方便处理用户，上传数据,Apache组织提供了一个开源组件Commons- FileUpload。 该组件可以方便地将“multipart/form-data"类型请求中的各种表单域解析出来，并实现一 个或多个文件的上传，同时也可以限制上传文件的大小等内容。其性能十分优异，使用极其 简单。
> 需要注意的是，在使用FileUpload组件时，要导入commons -filupload.jar和commons-io.jar两个JAR包，这两个JAR包可以去Apache[官网](http://comons%20apache.org/)下载(进入该网址页面后，在Apache Commons Proper 下方表格的Components列中找到FileUpload和I0,单击进入后即可找到下载链接)。
### 1.1、相关API
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200314202405372.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
	![在这里插入图片描述](https://img-blog.csdnimg.cn/20200314202814434.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200314203246822.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
```go
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="uploadServlet" method="post" enctype="multipart/form-data">
    <table width="600px">
        <tr>
            <td>上传者</td>
            <td><input type="text" name="name"></td>
        </tr>
        <tr>
            <td>上传文件</td>
            <td><input type="file" name="myfile"> </td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit"value="上传"/> </td>
        </tr>
    </table>
</form>

</body>
</html>

```

```go

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
//上传文件的Servlet类
@WebServlet("/uploadServlet")
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //设置ContentType字段值
            response.setContentType("text/html;charset=utf-8");
            // 创建DiskFileItemFactory工厂对象
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置文件缓存目录，如果该目录不存在则新创建一个
            File f = new File("E:\\TempFolder");
            if (!f.exists()) {
                f.mkdirs();
            }
            // 设置文件的缓存路径
            factory.setRepository(f);
            // 创建 ServletFileUpload对象
            ServletFileUpload fileupload = new ServletFileUpload(factory);
            //设置字符编码
            fileupload.setHeaderEncoding("utf-8");
            // 解析 request，得到上传文件的FileItem对象
            List<FileItem> fileitems = fileupload.parseRequest(request);
            //获取字符流
            PrintWriter writer = response.getWriter();
            // 遍历集合
            for (FileItem fileitem : fileitems) {
                // 判断是否为普通字段
                if (fileitem.isFormField()) {
                    // 获得字段名和字段值
                    String name = fileitem.getFieldName();
                    if(name.equals("name")){
                        //如果文件不为空，将其保存在value中
                        if(!fileitem.getString().equals("")){
                            String value = fileitem.getString("utf-8");
                            writer.print("上传者：" + value + "<br>");
                        }
                    }
                } else {
                    // 获取上传的文件名
                    String filename = fileitem.getName();
                    //处理上传文件
                    if(filename != null && !filename.equals("")){
                        writer.print("上传的文件名称是：" + filename + "<br>");
                        // 截取出文件名
                        filename = filename.substring(filename.lastIndexOf("\\") + 1);
                        // 文件名需要唯一
                        filename = UUID.randomUUID().toString() + "_" + filename;
                        // 在服务器创建同名文件
                        String webPath = "/upload/";
                        //将服务器中文件夹路径与文件名组合成完整的服务器端路径
                        String filepath = getServletContext().getRealPath(webPath + filename);
                        // 创建文件
                        File file = new File(filepath);
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                        // 获得上传文件流
                        InputStream in = fileitem.getInputStream();
                        // 使用FileOutputStream打开服务器端的上传文件
                        FileOutputStream out = new FileOutputStream(file);
                        // 流的对拷
                        byte[] buffer = new byte[1024];//每次读取1个字节
                        int len;
                        //开始读取上传文件的字节，并将其输出到服务端的上传文件输出流中
                        while ((len = in.read(buffer)) > 0)
                            out.write(buffer, 0, len);
                        // 关闭流
                        in.close();
                        out.close();
                        // 删除临时文件
                        fileitem.delete();
                        writer.print("上传文件成功！<br>");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200314210523491.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020031421053895.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200314210704538.png)
## 2、文件下载

```go
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>文件下载</title>
</head>
<body>
<!-- 	<a href="/chapter12/DownloadServlet?filename=1.jpg">文件下载 </a> -->
<a href="/chapter06/downloadServlet?filename=<%=URLEncoder.encode("风景.jpg", "utf-8")%>">文件下载 </a>
</body>
</html>
```

```go

import java.io.*;
import java.net.URLEncoder;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/downloadServlet")
public class DownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public void doGet(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        //设置ContentType字段值
        response.setContentType("text/html;charset=utf-8");
        //设置相应消息编码
        response.setCharacterEncoding("utf-8");
        //设置请求消息编码
        request.setCharacterEncoding("utf-8");
        //获取所要下载的文件名称
        String filename = request.getParameter("filename");
        //对文件名称编码
        filename = new String(filename.trim().getBytes("iso8859-1"),"UTF-8");
        //下载文件所在目录
        String folder = "/download/";
        // 通知浏览器以下载的方式打开
        response.addHeader("Content-Type", "application/octet-stream");
        response.addHeader("Content-Disposition",
                "attachment;filename="+URLEncoder.encode(filename,"utf-8"));
        // 通过文件流读取文件
        InputStream in = getServletContext().getResourceAsStream(
                folder+filename);
        // 获取response对象的输出流
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        //循环取出流中的数据
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

    }
    public void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        doGet(request, response);
    }
}
```
# 小结
## 1、String path = request.getContextPath(); String basePath = request.getScheme() + ": //" + request.getServerName() + ":" + request.getServerPort() + path + "/";作用！！！！！

```go
 <% String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
 %>
```

>
> 这个语句是用来拼装当前网页的相对路径的。
>
> \<base href="...">是用来表明当前页面的相对路径所使用的根路径的。 比如，页面内部有一个连接，完整的路径应该是
> http://localhost:80/myblog/authen/login.do
> 其中http://server/是服务器的基本路径，myblog是当前应用程序的名字，那么，我的根路径应该是那么http://localhost:80/myblog/。
> 有了这个 <base ... >以后，我的页面内容的连接，我不想写全路径，我只要写 authen/login.do就可以了。服务器会自动把 \<base ...>指定的路径和页面内的相对路径拼装起来，组成完整路径。 如果没有这个 \<base...>，那么我页面的连链接就必须写全路径，否则服务器会找不到。
> request.getSchema()可以返回当前页面使用的协议，就是上面例子中的“http”
> request.getServerName()可以返回当前页面所在的服务器的名字，就是上面例子中的“localhost"
> request.getServerPort()可以返回当前页面所在的服务器使用的端口,就是80，
> request.getContextPath()可以返回当前页面所在的应用的名字，就是上面例子中的myblog
> 这四个拼装起来，就是当前应用的跟路径了

## 2、response.setContentType与 request.setCharacterEncoding 区别

> 这两句代码都是设置字符集为utf-8 response.setCharacterEncoding("utf-8");
> response.setContentType("text/html;charset=utf-8");
>
> response.setContentType作用是让浏览器用utf-8来解析返回的数据
> response.setCharacterEncoding作用是告诉servlet用utf-8转码，而不是用默认的iso8859-1
>
> 参考资料：
>
> https://blog.csdn.net/tlms_/article/details/78749980

**----------------------------------------以前做的树状图，结合一下，图比较大---------------------------------------------------------**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200227224337580.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80NDQwNjU3OQ==,size_16,color_FFFFFF,t_70)