---
title: Nginx
date: 2022-11-13 12:23:30
tags:
- Nginx
categories:
- 服务器
---

# Nginx简介

## 什么是Nginx

> **Nginx 是高性能的 HTTP 和反向代理的服务器，处理高并发能力是十分强大的，能经受高负载的考验**,有报告表明能支持高达 50,000 个并发连接数。

## 正向代理

> **需要在客户端配置代理服务器进行指定网站访问**

<img src="Nginx/image-20221113124559197.png" alt="image-20221113124559197" style="zoom: 67%;" />

## 反向代理

> **暴露的是代理服务器地址，隐藏了真实服务器 IP 地址。**

<img src="Nginx/image-20221113124805504.png" alt="image-20221113124805504" style="zoom:67%;" />

## 负载均衡

> **增加服务器的数量，然后将请求分发到各个服务器上，将原先请求集中到单个服务器上的情况改为将请求分发到多个服务器上，将负载分发到不同的服务器**，也就是我们所说的负载均衡

<img src="Nginx/image-20221113124927666.png" alt="image-20221113124927666" style="zoom:60%;" />

## 动静分离

<img src="Nginx/image-20221113125008290.png" alt="image-20221113125008290" style="zoom:67%;" />

# 安装Nginx

> 1. 进入 nginx 官网，[下载](http://nginx.org/)
>
>    <img src="Nginx/image-20221113131713008.png" alt="image-20221113131713008" style="zoom:50%;" />
>
> 2. 安装pcre依赖
>
>    1. 联网下载 pcre 压缩文件依赖
>
>       ```sh
>       wget http://downloads.sourceforge.net/project/pcre/pcre/8.37/pcre-8.37.tar.gz
>       ```
>
>    2. 解压压缩文件
>
>       ```sh
>       tar -zxvf pcre-8.37.tar.gz
>       ```
>
>    3. 进入解压之后的文件夹
>
>       ```sh
>       ./configure
>       
>       make && make install
>       
>       # 查看版本
>       pcre-config --version
>       ```
>
>    4. 遇到的问题
>
>       1. 执行`./configure`的问题
>
>          ![image-20221113133058738](Nginx/image-20221113133058738.png)
>
>          需要c++的环境`yum install -y gcc gcc-c++`
>
>       2. 执行`make && make install`的问题
>
>          ![image-20221113133321716](Nginx/image-20221113133321716.png)
>
>          `./configure`所以没能生成makefile
>
> 3. 安装 openssl 、zlib 、 gcc 依赖
>
>    ```sh
>    yum -y install make zlib zlib-devel gcc-c++ libtool openssl openssl-devel
>    ```
>
> 4. 安装 nginx 
>
>    1. 使用命令解压 
>
>       ```sh
>       tar -zxvf nginx-1.12.2.tar.gz 
>       ```
>
>    2. 进入解压之后的文件夹
>
>       ```sh
>       ./configure 
>       
>       make && make install
>       ```
>
> 5. 进入目录`/usr/local/nginx/sbin`启动服务
>
>    ```sh
>    [root@lz sbin]# cd /usr/local/nginx/sbin
>    [root@lz sbin]# ./nginx
>    [root@lz sbin]# ps -ef | grep nginx
>    root      33615      1  0 13:38 ?        00:00:00 nginx: master process ./nginx
>    nobody    33616  33615  0 13:38 ?        00:00:00 nginx: worker process
>    root      33800   2766  0 13:39 pts/0    00:00:00 grep --color=auto nginx
>    ```
>
> 6. 设置防火墙
>
>    ```sh
>    # 查看开放的端口号
>    firewall-cmd --list-all
>    # 设置开放的端口号
>    firewall-cmd --add-service=http –permanent
>    firewall-cmd --add-port=80/tcp --permanent
>    # 重启防火墙
>    firewall-cmd –reload
>    ```
>
> 7. 访问IP+80端口`192.168.221.100:80`
>
>    ![image-20221113134315499](Nginx/image-20221113134315499.png)

# nginx常用的命令和配置文件

## 常用命令

> - 查看nginx版本号 ，在`/usr/local/nginx/sbin`目录下执行
>
>   ```sh
>   ./nginx -v
>   ```
>
> - 启动命令，在`/usr/local/nginx/sbin`目录下执行
>
>   ```sh
>   ./nginx
>   ```
>
> - 关闭命令，在`/usr/local/nginx/sbin`目录下执行
>
>   ```sh
>   ./nginx -s stop 
>   ```
>
> - 重新加载命令，在`/usr/local/nginx/sbin`目录下执行
>
>   ```sh
>   ./nginx -s reload
>   ```

## 配置文件

> - nginx 配置文件位置
>
>   ```sh
>   vim /usr/local/nginx/conf/nginx.conf
>   ```
>
> - 配置文件中的内容
>
>   1. 全局块：配置服务器整体运行的配置指令
>
>      <img src="Nginx/image-20221113155627574.png" alt="image-20221113155627574" style="zoom:80%;" />
>
>      比如 worker_processes 1;处理并发数的配置
>
>   2. events块：影响 Nginx 服务器与用户的网络连接
>
>      <img src="Nginx/image-20221113155649537.png" alt="image-20221113155649537" style="zoom:80%;" />
>
>      比如 worker_connections 1024; 支持的最大连接数为 1024
>
>   3. http块：包含两部分： http全局块 server块

# Nginx配置实例-反向代理

## 实例一

> - 实现效果：打开浏览器，在浏览器地址栏输入地址 www.123.com，跳转到 liunx 系统 tomcat 主页面中 
>
> - 准备工作
>
>   - 在 liunx 系统安装 tomcat，使用默认端口 8080 
>
>   - tomcat 安装文件放到 liunx 系统中，解压
>
>   - 进入 tomcat 的 bin 目录中，`./startup.sh`启动 tomcat 服务器
>
>   - 对外开放访问的端口(或者关闭防火墙)
>
>     ```sh
>     firewall-cmd --add-port=8080/tcp --permanent
>     firewall-cmd –reload
>     # 查看已经开放的端口号
>     firewall-cmd --list-all
>     ```
>
>   - 在 windows 系统中通过浏览器访问 tomcat 服务器（IP+8080端口）
>
>     <img src="Nginx/image-20221113162917202.png" alt="image-20221113162917202" style="zoom:60%;" />
>
> - 访问过程的分析
>
>   正常访问流程是，根据域名查看本地hosts文件中的配置，hosts中没有配置根据DNS域名解析系统访问服务器
>
>   ![image-20221113163213128](Nginx/image-20221113163213128.png)
>
> - 具体配置
>
>   1. 在 windows 系统的 host 文件进行域名和 ip 对应关系的配置
>
>      ![image-20221113163503455](Nginx/image-20221113163503455.png)
>
>      ![image-20221113163620423](Nginx/image-20221113163620423.png)
>
>   2. 在 nginx 进行请求转发的配置（反向代理配置）`vim /usr/local/nginx/conf/nginx.conf`
>
>      注意后面的分号不要忘记
>
>      ![image-20221113164942382](Nginx/image-20221113164942382.png)
>
>   3. 重启nginx
>
> - 测试
>
>   <img src="Nginx/image-20221113164839584.png" alt="image-20221113164839584" style="zoom:67%;" />

## 实例二

> - 实现效果
>
>   使用 nginx 反向代理，根据访问的路径跳转到不同端口的服务中 nginx 监听端口为 9001，
>
>   访问 http://192.168.221.100:9001/edu/ 直接跳转到 127.0.0.1:8080
>
>   访问 http://192.168.221.100:9001/vod/ 直接跳转到 127.0.0.1:8081
>
> - 准备工作
>
>   准备两个 tomcat 服务器，一个 8080 端口，一个 8081 端口 
>
>   创建文件夹和测试页面
>
>   ![image-20221113194804406](Nginx/image-20221113194804406.png)
>
>   ![image-20221113194826565](Nginx/image-20221113194826565.png)
>
> - 具体配置 
>
>   找到 nginx 配置文件，进行反向代理配置`vim /usr/local/nginx/conf/nginx.conf`
>
>   ![image-20221113173639222](Nginx/image-20221113173639222.png) 
>
>   开放对外访问的端口号 9001 8080 8081或者关闭防火墙
>
> - 最终测试
>
>   ![image-20221113195026213](Nginx/image-20221113195026213.png)
>
>   ![image-20221113195052318](Nginx/image-20221113195052318.png)

### location 指令说明 

> location有两种匹配规则：
>
> - 匹配URL类型，有四种参数可选，当然也可以不带参数。
>   `location [ = | ~ | ~* | ^~ ] uri { … }`
> - 命名location，用@标识，类似于定于goto语句块。
>   `location @name { … }`

#### `“=”` 

> 精确匹配，内容要同表达式完全一致才匹配成功

```
location = /abc/ {
  .....
 }
        
# 只匹配http://abc.com/abc
#http://abc.com/abc [匹配成功]
#http://abc.com/abc/index [匹配失败]
```

#### `“~”`

> 执行正则匹配，区分大小写。

```
location ~ /Abc/ {
  .....
}
#http://abc.com/Abc/ [匹配成功]
#http://abc.com/abc/ [匹配失败]
```

#### `“~*”`

> 执行正则匹配，忽略大小写

```
location ~* /Abc/ {
  .....
}
# 则会忽略 uri 部分的大小写
#http://abc.com/Abc/ [匹配成功]
#http://abc.com/abc/ [匹配成功]
```

#### `“^~”`

> 表示普通字符串匹配上以后不再进行正则匹配。

```
location ^~ /index/ {
  .....
}
#以 /index/ 开头的请求，都会匹配上
#http://abc.com/index/index.page  [匹配成功]
#http://abc.com/error/error.page [匹配失败]
```

#### 不加任何规则时

> 默认是大小写敏感，前缀匹配，相当于加了“~”与“^~”

```
location /index/ {
  ......
}
#http://abc.com/index  [匹配成功]
#http://abc.com/index/index.page  [匹配成功]
#http://abc.com/test/index  [匹配失败]
#http://abc.com/Index  [匹配失败]
# 匹配到所有uri
```

#### `“@”`

> nginx内部跳转

```
location /index/ {
  error_page 404 @index_error;
}
location @index_error {
  .....
}
#以 /index/ 开头的请求，如果链接的状态为 404。则会匹配到 @index_error 这条规则上。
```

# Nginx 配置实例-负载均衡

> 1. 实现效果
>
>    浏览器地址栏输入地址 http://192.168.221.100/edu/a.html，负载均衡效果，平均 8080 和 8081 端口中
>
> 2. 准备工作
>
>    准备两台 tomcat 服务器，一台 8080，一台 8081 
>
>    在两台 tomcat 里面 webapps 目录中，创建名称是 edu 文件夹，在 edu 文件夹中创建 页面 a.html，用于测
>
> 3. 在 nginx 的配置文件中进行负载均衡的配置
>
>    <img src="Nginx/image-20221113214002528.png" alt="image-20221113214002528" style="zoom:80%;" />
>
> 4. nginx 分配服务器策略
>
>    - 第一种 轮询（默认）
>
>      每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器 down 掉，能自动剔除。
>
>    - 第二种 weight
>
>      weight 代表权重默认为 1,权重越高被分配的客户端越多
>
>      ![image-20221113214321148](Nginx/image-20221113214321148.png)
>
>    - 第三种 ip_hash
>
>      每个请求按访问 ip 的 hash 结果分配，这样每个访客固定访问一个后端服务器
>
>      ![image-20221113214552892](Nginx/image-20221113214552892.png)
>
>    - 第四种 fair（第三方）
>
>      按后端服务器的响应时间来分配请求，响应时间短的优先分配。
>
>      ![image-20221113214657179](Nginx/image-20221113214657179.png)

# Nginx 配置实例-动静分离

<img src="Nginx/image-20221114103740323.png" alt="image-20221114103740323" style="zoom: 50%;" />

> Nginx 动静分离简单来说就是把动态跟静态请求分开，不能理解成只是单纯的把动态页面和 静态页面物理分离。严格意义上说应该是动态请求跟静态请求分开，可以理解成使用 Nginx  处理静态页面，Tomcat 处理动态页面。
>
> 动静分离从目前实现角度来讲大致分为两种：
>
> 一种是纯粹把静态文件独立成单独的域名，放在独立的服务器上，也是目前主流推崇的方案； 
>
> 另外一种方法就是动态跟静态文件混合在一起发布，通过 nginx 来分开。 通过 location 指定不同的后缀名实现不同的请求转发。通过 expires 参数设置，可以使 浏览器缓存过期时间，减少与服务器之前的请求和流量。具体 Expires 定义：是给一个资 源设定一个过期时间，也就是说无需去服务端验证，直接通过浏览器自身确认是否过期即可， 所以不会产生额外的流量。此种方法非常适合不经常变动的资源。（如果经常更新的文件， 不建议使用 Expires 来缓存），我这里设置 3d，表示在这 3 天之内访问这个 URL，发送 一个请求，比对服务器该文件最后更新时间没有变化，则不会从服务器抓取，返回状态码 304，如果有修改，则直接从服务器重新下载，返回状态码 200。

> 1. 准备工作 
>
>    在 liunx 系统中准备静态资源，用于进行访问
>
>    ![image-20221114103844175](Nginx/image-20221114103844175.png)
>
> 2. 具体配置
>
>    在 nginx 配置文件中进行配置`vim /usr/local/nginx/conf/nginx.conf`
>
>    `autoindex on`：展示文件夹里的所有内容
>
>    <img src="Nginx/image-20221114103943751.png" alt="image-20221114103943751" style="zoom:67%;" />
>
> 3. 最终测试
>
>    浏览器中输入地址192.168.221.100/www/a.html
>
>    <img src="Nginx/image-20221114104050390.png" alt="image-20221114104050390" style="zoom:67%;" />
>
>    浏览器中输入地址192.168.221.100/images/ 		192.168.221.100/images/404.png
>
>    <img src="Nginx/image-20221114104206710.png" alt="image-20221114104206710" style="zoom: 67%;" />
>
>    <img src="Nginx/image-20221114104307748.png" alt="image-20221114104307748" style="zoom:67%;" />

# Nginx 配置高可用的集群

> 1. 什么是 nginx 高可用
>
>    <img src="Nginx/image-20221114171955560.png" alt="image-20221114171955560" style="zoom:60%;" />
>
>    需要两台 nginx 服务器
>
>    需要 keepalived
>
>    需要虚拟 ip
>
> 2. 配置高可用的准备工作
>
>    需要两台服务器 192.168.221.100 和 192.168.221.111
>
>    在两台服务器安装 nginx 
>
>    在两台服务器安装 keepalived，安装之后，在 etc 里面生成目录 keepalived，有文件 keepalived.conf
>
>    ```sh
>    yum install keepalived –y
>    ```
>
> 3. 完成高可用配置（主从配置）
>
>    修改`/etc/keepalived/keepalivec.conf`配置文件
>
>    ```
>    bal_defs {
>       notification_email {
>       acassen@firewall.loc
>       failover@firewall.loc
>       sysadmin@firewall.loc
>     }
>    
>       notification_email_from Alexandre.Cassen@firewall.loc        #定义利用什么邮箱发送邮件
>       smtp_server smtp.163.com     #定义邮件服务器信息
>       smtp_connect_timeout 30      #定义邮件发送超时时间
>       router_id 192.168.221.100    #（重点参数）局域网keppalived主机身份标识信息(每台唯一)
>       script_user root             #添加运行健康检查脚本的用户
>       enable_script_security       #添加运行健康检查脚本的组
>    }
>    
>    vrrp_script chk_http_port {
>     script "/usr/local/src/nginx_check.sh"         #表示将一个脚本信息赋值给变量check_web
>     interval 2      #检测脚本执行的间隔
>     weight -20      #监测失败，则相应的vrrp_instance的优先级会减少20个点
>    }
>    
>    vrrp_instance VI_1 {
>     state MASTER           #keepalived角色描述信息，备份服务器上将 MASTER 改为 BACKUP
>     interface ens33        #将虚拟ip用于那块网卡
>     virtual_router_id 51   #主、备机的 virtual_router_id 必须相同
>     priority 100            #主、备机取不同的优先级，主机值较大，备份机值较小
>     advert_int 1           #主服务器组播包发送间隔时间
>    
>    authentication {        # 主备主机之间的认证表示信息
>       auth_type PASS       #采用明文认证机制
>       auth_pass 1111       #编写明文密码
>     }
>     virtual_ipaddress {
>       192.168.221.50      #设置虚拟ip地址信息，此参数备节点设置和主节点相同
>     }
>     track_script {
>        chk_http_port       #调用执行脚本
>      }
>    }
>    ```
>
>    在`/usr/local/src`添加检测脚本
>
>    ```sh
>    #!/bin/bash
>    A=`ps -C nginx 每no-header |wc -l`
>    if [ $A -eq 0 ];then
>        /usr/local/nginx/sbin/nginx
>        sleep 2
>        if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then
>            killall keepalived
>        fi
>    fi
>    ```
>
> 4. 把两台服务器上 nginx 和 keepalived 启动
>
>    启动 nginx：`./nginx`
>
>    启动 keepalived：`systemctl start keepalived.service`
>
> 5. 最终测试 
>
>    在浏览器地址栏输入 虚拟 ip 地址 192.168.221.50
>
>    <img src="Nginx/image-20221114180040911.png" alt="image-20221114180040911" style="zoom:67%;" />
>
>    把主服务器（192.168.221.100）nginx 和 keepalived 停止，再输入 192.168.221.50
>
>    <img src="Nginx/image-20221114180042938.png" alt="image-20221114180042938" style="zoom:67%;" />

# Nginx 的原理

## mater 和 worker

![image-20221114210836879](Nginx/image-20221114210836879.png)

## worker 如何进行工作的

![image-20221114210856853](Nginx/image-20221114210856853.png)

## 一个master和多个woker有好处

> 1. 可以使用 nginx –s reload 热部署，利用 nginx 进行热部署操作
> 2. 每个 woker 是独立的进程，如果有其中的一个 woker 出现问题，其他 woker 独立的， 继续进行争抢，实现请求过程，不会造成服务中断

## 设置多少个 woker 合适

> worker 数和服务器的 cpu 数相等是最为适宜的

## 连接数 worker_connection

> - 第一个：发送请求，占用了 woker 的几个连接数？
>
>   答案：2 或者 4 个 
>
> - 第二个：nginx 有一个 master，有四个 woker，每个 woker 支持最大的连接数 1024，支持的 最大并发数是多少？ 
>
>   - 普通的静态访问最大并发数是： worker_connections * worker_processes /2，
>   - 而如果是 HTTP 作 为反向代理来说，最大并发数量应该是 worker_connections *  worker_processes/4