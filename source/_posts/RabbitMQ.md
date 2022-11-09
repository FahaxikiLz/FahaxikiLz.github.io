---
title: RabbitMQ
date: 2022-11-08 16:24:22
tags: 
- RabbitMQ
categories: 
- 消息中间件
---

# RabbitMQ 介绍

## RabbitMQ的概念

> RabbitMQ 是一个消息中间件：它接受并转发消息。你可以把它当做一个快递站点，当你要发送一个包裹时，你把你的包裹放到快递站，快递员最终会把你的快递送到收件人那里，按照这种逻辑 RabbitMQ 是 一个快递站，一个快递员帮你传递快件。RabbitMQ 与快递站的主要区别在于，它不处理快件而是接收，存储和转发消息数据。

## 四大核心概念

> - 生产者：产生数据发送消息的程序
> - 交换机：是 RabbitMQ 非常重要的一个部件，一方面它接收来自生产者的消息，另一方面它将消息 推送到队列中。交换机必须确切知道如何处理它接收到的消息，是将这些消息推送到特定队列还是推送到多个队列，亦或者是把消息丢弃，这个得有交换机类型决定
> - 队列：是 RabbitMQ 内部使用的一种数据结构，尽管消息流经 RabbitMQ 和应用程序，但它们只能存储在队列中。队列仅受主机的内存和磁盘限制的约束，本质上是一个大的消息缓冲区。许多生产者可以将消息发送到一个队列，许多消费者可以尝试从一个队列接收数据。这就是我们使用队列的方式
> - 消费者：消费与接收具有相似的含义。消费者大多时候是一个等待接收消息的程序。请注意生产者，消费者和消息中间件很多时候并不在同一机器上。同一个应用程序既可以是生产者又是可以是消费者



![image-20221108162435509](RabbitMQ/image-20221108162435509.png)

## 各个名词介绍

![](RabbitMQ/image.15tq7c46aey.png)

> - `Broker`：接收和分发消息的应用，RabbitMQ Server 就是 Message Broker
> - `Virtual host`：出于多租户和安全因素设计的，把 AMQP 的基本组件划分到一个虚拟的分组中，类似于网络中的 namespace 概念。当多个不同的用户使用同一个 RabbitMQ server 提供的服务时，可以划分出多个 vhost，每个用户在自己的 vhost 创建 exchange／queue 等
> - `Connection`：publisher／consumer 和 broker 之间的 TCP 连接
> - `Channel`：如果每一次访问 RabbitMQ 都建立一个 Connection，在消息量大的时候建立 TCP Connection 的开销将是巨大的，效率也较低。Channel 是在 connection 内部建立的逻辑连接，如果应用程序支持多线程，通常每个 thread 创建单独的 channel 进行通讯，AMQP method 包含了 channel id 帮助客 户端和 message broker 识别 channel，所以 channel 之间是完全隔离的。Channel 作为轻量级的 Connection 极大减少了操作系统建立 TCP connection 的开销
> - `Exchange`：message 到达 broker 的第一站，根据分发规则，匹配查询表中的 routing key，分发 消息到 queue 中去。常用的类型有：direct (point-to-point)，topic (publish-subscribe) and fanout (multicast)
> - `Queue`：消息最终被送到这里等待 consumer 取走
> - `Binding`：exchange 和 queue 之间的虚拟连接，binding 中可以包含 routing key，Binding 信息被保 存到 exchange 中的查询表中，用于 message 的分发依据

# RabbitMQ 安装

## 下载RabbitMQ

> - [下载地址](https://github.com/rabbitmq/rabbitmq-server/releases)
> - linux系统使用.rpm结尾的包

![image-20221109095715704](RabbitMQ/image-20221109095715704.png)

## 下载Erlang

> - RabbitMQ 是采用 Erlang 语言开发的，所以系统环境必须提供 Erlang 环境，需要先安装 Erlang。
>
> - `Erlang` 和 `RabbitMQ` [版本对照](https://www.rabbitmq.com/which-erlang.html)
>
> - [Erlang 21.3下载地址](https://packagecloud.io/rabbitmq/erlang/packages/el/7/erlang-21.3.8.16-1.el7.x86_64.rpm)
>
> - CentOs 7.x 版本需要e17。
>
>   CentOs 8.x 版本需要e18。包括 Red Hat 8,modern Fedora 版本。

## 上传文件

> 文件上传：上传到`/usr/local/software`目录下(如果没有 software 需要自己创建)

![image-20221108170031677](RabbitMQ/image-20221108170031677.png)

## 安装Erlang

```sh
cd /usr/local/software
rpm -ivh erlang-21.3-1.el7.x86_64.rpm

# 查看版本号，显示版本号就是安装成功了
erl -v
```

### 安装Erlang遇到的问题

> 如果安装 Erlang 过程出现了如下问题：

![](RabbitMQ/QQ%E5%9B%BE%E7%89%8720221109100326.png)

> 出现这个错误的主要原因是没有`libcrypto.so.10(OPENSSL_1.0.2)(64bit)`依赖，我们去下载一个就可以了
>
> 下载地址：[libcrypto.so.10(OPENSSL_1.0.2)(64bit)(opens new window)](https://rpmfind.net/linux/rpm2html/search.php?query=libcrypto.so.10(OPENSSL_1.0.2)(64bit)&submit=Search ...&system=&arch=)
>
> 滑到最下面，下载最后一个
>
> ![image-20221109100502504](RabbitMQ/image-20221109100502504.png)
>
> 下载到本地后上传到 Linux 中，传输目录一致。接着使用命令安装
>
> ```sh
> rpm -ivh openssl-libs-1.0.2k-19.el7.x86_64.rpm --force
> ```

## 安装RabbitMQ

>  `RabiitMQ` 安装过程中需要依赖 `socat` 插件，首先安装该插件

```sh
yum install socat -y
```

```sh
[root@master rabbitmq]# rpm -ivh rabbitmq-server-3.8.8-1.el7.noarch.rpm
警告：rabbitmq-server-3.8.8-1.el7.noarch.rpm: 头V4 RSA/SHA256 Signature, 密钥 ID 6026dfca: NOKEY
准备中...                          ################################# [100%]
正在升级/安装...
   1:rabbitmq-server-3.8.8-1.el7      ################################# [100%]
```

## 启动

```sh
# 启动服务
systemctl start rabbitmq-server

# 查看服务状态
systemctl status rabbitmq-server

# 开机自启动
systemctl enable rabbitmq-server
```

> 状态显示active就是启动成功了

![image-20221109100742331](RabbitMQ/image-20221109100742331.png)

## 管理界面及授权操作

> - RabbitMQ 的默认访问端口是 15672
> - 如果 Linux 有防火墙，记得开放 15672 端口，否则 Windows 无法访问。要不就关闭防火墙
> - 默认情况下，RabbiMQ 没有安装 Web 端的客户端软件，需要安装才可以生效

```sh
rabbitmq-plugins enable rabbitmq_management

# 安装完毕重启RabbitMQ
systemctl restart rabbitmq-server
```

> 通过 `http://ip:15672` 访问，ip 为 Linux 的 ip。`rabbitmq` 有一个默认的账号密码 `guest`，但是登录该账号密码会出现权限问题

![image-20221109101114877](RabbitMQ/image-20221109101114877.png)

## 添加新用户

```sh
# 创建账号
rabbitmqctl add_user admin 123

# 设置用户角色
# 角色固定有四种级别：
# administrator：可以登录控制台、查看所有信息、并对rabbitmq进行管理
# monToring：监控者；登录控制台，查看所有信息
# policymaker：策略制定者；登录控制台指定策略
# managment：普通管理员；登录控制
rabbitmqctl set_user_tags admin administrator

# 设置用户权限
# set_permissions [-p <vhostpath>] <user> <conf> <write> <read>
# 用户 user_admin具有/vhost1 这个 virtual host 中所有资源的配置、写、读权限
rabbitmqctl set_permissions -p "/" admin ".*" ".*" ".*"
```

> 使用新创建的账户登录管理界面

> 其他指令：

```sh
# 修改密码
rabbitmqctl change_ password 用户名 新密码

# 删除用户
rabbitmqctl delete_user 用户名

# 查看用户清单
rabbitmqctl list_user
```

# Hello World

## 依赖

```xml
    <dependencies>
        <!--rabbitmq 依赖客户端-->
        <dependency>
            <groupId>com.rabbitmq</groupId>
            <artifactId>amqp-client</artifactId>
            <version>5.8.0</version>
        </dependency>
        <!--操作文件流的一个依赖-->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>
    </dependencies>
```

## 消息生产者

```java
package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Provider {
    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        //工厂IP 连接RabbitMQ的队列
        factory.setHost("192.168.221.88");

        //用户名
        factory.setUsername("admin");

        //密码
        factory.setPassword("123");

        //创建连接
        Connection connection = factory.newConnection();

        //获取信道
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化 默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
         * 4.是否自动删除 最后一个消费者端开连接以后 该队列是否自动删除 true 自动删除
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        String message = "hello world";

        /**
         * 发送一个消息的参数
         * 1.发送到那个交换机
         * 2.路由的 key 是哪个
         * 3.其他的参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
    }
}
```

## 消息消费者

```java
package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {


    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.221.100");
        factory.setUsername("admin");
        factory.setPassword("123");


        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();


        /**
         * 消费者消费消息的参数
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者成功消费的回调
         * 4.消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, (var1, var2) -> {
            System.out.println(new String(var2.getBody()));
        }, var1 -> {
            System.out.println("消息消费被中断");
        });

    }
}
```

# Work Queues

> 工作队列(又称任务队列)的主要思想是避免立即执行资源密集型任务，而不得不等待它完成。 相反我们安排任务在之后执行。我们把任务封装为消息并将其发送到队列。在后台运行的工作进 程将弹出任务并最终执行作业。当有多个工作线程时，这些工作线程将一起处理这些任务。

![image-20221109153546408](RabbitMQ/image-20221109153546408.png)

## 轮训分发消息

### 提取工具类

```java
package com.atguigu.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 这是一个连接RabbitMQ的工具类
 */
public class RabbitMQUtil {

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("192.168.221.100");

        factory.setUsername("admin");

        factory.setPassword("123");
        factory.setHandshakeTimeout(60000);


        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        return channel;
    }
}
```

### 消息生产者

```java
package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Task01 {


    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMQUtil.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            System.out.println(message + "发送完成");
        }


    }
}

```

### 消息消费者

```java
package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker01 {

    private static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMQUtil.getChannel();

        System.out.println("worker02准备接收消息...");

        channel.basicConsume(QUEUE_NAME, true, (var1, var2) -> {
            System.out.println(new String(var2.getBody()));
        }, var1 -> {
            System.out.println("消息消费被中断");
        });
    }
}

```

> 消费者勾选这个可以启动多个线程

![image-20221109153624839](RabbitMQ/image-20221109153624839.png)

> - 输入AA BB CC DD
> - 输出结果：
>
> ![image-20221109153711376](RabbitMQ/image-20221109153711376.png)
>
> ![image-20221109153720464](RabbitMQ/image-20221109153720464.png)

## 消息应答

### 概念

> 消费者完成一个任务可能需要一段时间，如果其中一个消费者处理一个长的任务并仅只完成 了部分突然它挂掉了，会发生什么情况。**RabbitMQ一旦向消费者传递了一条消息，便立即将该消息标记为删除。在这种情况下，突然有个消费者挂掉了，我们将丢失正在处理的消息。以及后续发送给该消费这的消息，因为它无法接收到。** 
>
> 为了保证消息在发送过程中不丢失，rabbitmq引入消息应答机制，**消息应答就是:消费者在接收到消息并且处理该消息之后，告诉 rabbitmq 它已经处理了，rabbitmq 可以把该消息删除了。** 

### 自动应答

> 消息发送后立即被认为已经传送成功，**这种模式需要在高吞吐量和数据传输安全性方面做权衡**,因为这种模式**如果消息在接收到之前，消费者那边出现连接或者 channel 关闭，那么消息就丢失了**,当然另一方面这种模式**消费者那边可以传递过载的消息，没有对传递的消息数量进行限制， 当然这样有可能使得消费者这边由于接收太多还来不及处理的消息，导致这些消息的积压，最终使得内存耗尽，最终这些消费者线程被操作系统杀死**，所以**这种模式仅适用在消费者可以高效并以某种速率能够处理这些消息的情况下使用**

### 消息手动应答的方法

> 1. `Channel.basicAck`(用于肯定确认) RabbitMQ 已知道该消息并且成功的处理消息，可以将其丢弃了 
> 2. `Channel.basicNack`(用于否定确认)
> 3. `Channel.basicReject`(用于否定确认) 与 `Channel.basicNack` 相比少一个参数 不处理该消息了直接拒绝，可以将其丢弃了

### Multiple的解释 

> 手动应答的好处是可以批量应答并且减少网络拥堵

![image-20221109161137480](RabbitMQ/image-20221109161137480.png)

> multiple 的 true 和 false 代表不同意思
>
> - true 代表批量应答 channel 上未应答的消息
>   比如说 channel 上有传送 tag 的消息 5,6,7,8 当前 tag 是 8 那么此时 5-8 的这些还未应答的消息都会被确认收到消息应答
> - false 同上面相比只会应答 tag=8 的消息 5,6,7 这三个消息依然不会被确认收到消息应答

### 消息自动重新入队

> 如果消费者由于某些原因失去连接(其通道已关闭，连接已关闭或 TCP 连接丢失)，导致消息未发送 ACK 确认，RabbitMQ 将了解到消息未完全处理，并将对其重新排队。如果此时其他消费者可以处理，它将很快将其重新分发给另一个消费者。这样，即使某个消费者偶尔死亡，也可以确保不会丢失任何消息。

![image-20221109162240762](RabbitMQ/image-20221109162240762.png)

### 消息手动应答代码

#### 消息生产者

```java
package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Task02 {

    private static final String ACK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();

        channel.queueDeclare(ACK_QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", ACK_QUEUE_NAME, null, message.getBytes("UTF-8"));

            System.out.println("提供者发送消息" + message);
        }

    }
}
```

#### 两个消息消费者

> 两个消费者，Worker03等待1s之后应答，Worker04等待20s之后应答

```java
package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker03 {

    private static final String ACK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMQUtil.getChannel();

        System.out.println("c1准备接收数据，等待时间较短");

        //第二个参数为false，表示为不自动应答，需要我们手动应答
        channel.basicConsume(ACK_QUEUE_NAME, false, (var1, var2) -> {
            try {
                Thread.sleep(1000);//睡一秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(new String(var2.getBody()));

            /**
             * 1.消息标记 tag
             * 2.是否批量应答未应答消息
             */
            channel.basicAck(var2.getEnvelope().getDeliveryTag(), false);
        }, (var) -> {
            System.out.println("消费者取消消费接口回调逻辑");
        });

    }
}
```

```java
package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author:lz
 * @Date:2022/11/9 16:58
 * @Description
 */
public class Worker04 {
    private static final String ACK_QUEUE_NAME = "ack_queue";


    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitMQUtil.getChannel();

        System.out.println("c2准备接收消息，等待时间较长");

        //第二个参数为false，表示为不自动应答，需要我们手动应答
        channel.basicConsume(ACK_QUEUE_NAME, false, (var1, var2) -> {

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(new String(var2.getBody()));

            /**
             * 1.消息标记 tag
             * 2.是否批量应答未应答消息
             */
            channel.basicAck(var2.getEnvelope().getDeliveryTag(), false);
        }, (var) -> {
            System.out.println("消费者取消消费接口回调逻辑");
        });
    }
}
```

#### 测试

> 生产者输入AA BB
>
> 1s后worker03输出AA
>
> 20s后worker04输出BB

![image-20221109170803454](RabbitMQ/image-20221109170803454.png)

![image-20221109170720272](RabbitMQ/image-20221109170720272.png)

![image-20221109170728519](RabbitMQ/image-20221109170728519.png)

> 生产者输入AA BB
>
> 1s后worker03输出AA
>
> 20s内我们停止worker04，worker04不会应答消息重新回到消息队列，在worker03输出

![image-20221109171015223](RabbitMQ/image-20221109171015223.png)

![image-20221109171024119](RabbitMQ/image-20221109171024119.png)

![image-20221109171033519](RabbitMQ/image-20221109171033519.png)

## 持久化

### 概念

> 刚刚我们已经看到了如何处理任务不丢失的情况，但是如何保障当 RabbitMQ 服务停掉以后消息生产者发送过来的消息不丢失。**默认情况下 RabbitMQ 退出或由于某种原因崩溃时，它忽视队列和消息，除非告知它不要这样做。确保消息不会丢失需要做两件事：我们需要将队列和消息都标 记为持久化。**

### 队列如何实现持久化

> 之前我们创建的队列都是非持久化的，rabbitmq 如果重启的化，该队列就会被删除掉，如果要队列实现持久化需要**在声明队列的时候把 durable 参数设置为持久化（消息生产者中）**

![image-20221109185655404](RabbitMQ/image-20221109185655404.png)

> 如果是之前没有队列持久化的消息生产者直接设置了true会报错。需要把之前的删除重新运行

```
Caused by: com.rabbitmq.client.ShutdownSignalException: channel error; protocol method: #method<channel.close>(reply-code=406, reply-text=PRECONDITION_FAILED - inequivalent arg 'durable' for queue 'ack_queue' in vhost '/': received 'true' but current is 'false', class-id=50, method-id=10)
	at com.rabbitmq.client.impl.ChannelN.asyncShutdown(ChannelN.java:522)
	at com.rabbitmq.client.impl.ChannelN.processAsync(ChannelN.java:346)
	at com.rabbitmq.client.impl.AMQChannel.handleCompleteInboundCommand(AMQChannel.java:182)
	at com.rabbitmq.client.impl.AMQChannel.handleFrame(AMQChannel.java:114)
	at com.rabbitmq.client.impl.AMQConnection.readFrame(AMQConnection.java:719)
	at com.rabbitmq.client.impl.AMQConnection.access$300(AMQConnection.java:48)
	at com.rabbitmq.client.impl.AMQConnection$MainLoop.run(AMQConnection.java:646)
	at java.lang.Thread.run(Thread.java:748)
```

![image-20221109190000401](RabbitMQ/image-20221109190000401.png)

![image-20221109190017195](RabbitMQ/image-20221109190017195.png)

> 重新运行，这里显示一个D 就是表示队列持久化

![image-20221109190118616](RabbitMQ/image-20221109190118616.png)

### 消息实现持久化

> **要想让消息实现持久化需要在消息生产者修改代码，添加`MessageProperties.PERSISTENT_TEXT_PLAIN`这个属性。**

![image-20221109190308824](RabbitMQ/image-20221109190308824.png)

> 将消息标记为持久化并不能完全保证不会丢失消息。尽管它告诉 RabbitMQ 将消息保存到磁盘，但是 这里依然存在当消息刚准备存储在磁盘的时候 但是还没有存储完，消息还在缓存的一个间隔点。此时并没 有真正写入磁盘。持久性保证并不强，但是对于我们的简单任务队列而言，这已经绰绰有余了。如果需要 更强有力的持久化策略，参考后边课件发布确认章节。

## 不公平分发

> 在最开始的时候我们学习到 RabbitMQ 分发消息采用的轮训分发，但是在某种场景下这种策略并不是很好，比方说有两个消费者在处理任务，其中有个消费者 1 处理任务的速度非常快，而另外一个消费者 2 处理速度却很慢，这个时候我们还是采用轮训分发的化就会到这处理速度快的这个消费者很大一部分时间处于空闲状态，而处理慢的那个消费者一直在干活，这种分配方式在这种情况下其实就不太好，但是 RabbitMQ 并不知道这种情况它依然很公平的进行分发。
>
> 为了避免这种情况，我们可以**在消息消费者中设置参数`channel.basicQos(1);`**

![image-20221109192617095](RabbitMQ/image-20221109192617095.png)

### 测试结果

> 在消息生产者中输入11 22 33 44 55 66 77
>
> c1，1s后应答
>
> c2，20s后应答

![image-20221109192846720](RabbitMQ/image-20221109192846720.png)

![image-20221109192855527](RabbitMQ/image-20221109192855527.png)

![image-20221109192904158](RabbitMQ/image-20221109192904158.png)

> 同时在管理控制台中也可以看到设置的不公平分发

![image-20221109193106082](RabbitMQ/image-20221109193106082.png)

## 预取值

> 本身消息的发送就是异步发送的，所以在任何时候，channel 上肯定不止只有一个消息另外来自消费者的手动确认本质上也是异步的。因此**这里就存在一个未确认的消息缓冲区，因此希望开发人员能限制此缓冲区的大小，以避免缓冲区里面无限制的未确认消息问题。这个时候就可以通过使用`basicqos`方法设置“预取计数”值来完成的。该值定义通道上允许的未确认消息的最大数量。一旦数量达到配置的数量， RabbitMQ 将停止在通道上传递更多消息，除非至少有一个未处理的消息被确认**
>
> 例如，假设在通道上有未确认的消息 5、6、7，8，并且通道的预取计数设置为 4，此时 RabbitMQ 将不会在该通道上再传递任何消息，除非至少有一个未应答的消息被 ack。比方说 tag=6 这个消息刚刚被确认 ACK，RabbitMQ 将会感知这个情况到并再发送一条消息。
>
> **消息应答和 QoS 预取值对用户吞吐量有重大影响。通常，增加预取将提高向消费者传递消息的速度。虽然自动应答传输消息速率是最佳的，但是，在这种情况下已传递但尚未处理 的消息的数量也会增加，从而增加了消费者的 RAM 消耗(随机存取存储器)，**应该小心使用具有无限预处理的自动确认模式或手动确认模式，消费者消费了大量的消息如果没有确认的话，会导致消费者连接节点的 内存消耗变大，所以找到合适的预取值是一个反复试验的过程，**不同的负载该值取值也不同 100 到 300 范 围内的值通常可提供最佳的吞吐量，并且不会给消费者带来太大的风险。预取值为1是最保守的。当然这将使吞吐量变得很低，特别是消费者连接延迟很严重的情况下，特别是在消费者连接等待时间较长的环境中。对于大多数应用来说，稍微高一点的值将是最佳的**

![image-20221109195256430](RabbitMQ/image-20221109195256430.png)

![image-20221109195243798](RabbitMQ/image-20221109195243798.png)

### 测试结果

> c1,1s后应答，设置预取值为2
>
> c2,20s后应答，设置预取值为5
>
> 在消息生产者中输入11 22 33 44 55 66 77

![image-20221109195435360](RabbitMQ/image-20221109195435360.png)

![image-20221109195446331](RabbitMQ/image-20221109195446331.png)

> 同样在管理控制台可以看到预取值

![image-20221109201319292](RabbitMQ/image-20221109201319292.png)

### 理解

![image-20221109201957138](RabbitMQ/image-20221109201957138.png)



> 两个消费者，一个预取值为2，一个为5，整个通道就是有7个值，也就是最多有7个，其他的都在准备（ready），当一个消息被应答，准备中的就有一个继续发送
>
> 不公平分发设置的都是1，也就是通道可以有2个值，其他的都在准备，所以说设置的值影响吞吐量

# 发布确认

> 设置队列的持久化、消息的持久化，可能会在持久化到磁盘之间宕机了，这样数据也会丢失。所以使用发布确认，一旦消息被投递到所有匹配的队列之后，broker就会发送一个确认给生产者(包含消息的唯一 ID)，这就使得生产者知道消息已经正确到达目的队列了，如果消息和队列是可持久化的，那么确认消息会在将消息写入磁盘之后发出。

![image-20221109203454035](RabbitMQ/image-20221109203454035.png)

## 开启发布确认的方法 

> 发布确认默认是没有开启的，如果要开启需要调用方法`confirmSelect`，每当你要想使用发布确认，都需要在 channel 上调用该方法

![image-20221109204318036](RabbitMQ/image-20221109204318036.png)

## 单个确认发布

> 这是一种简单的确认方式，它是一种**同步确认发布的方式**，也就是发布一个消息之后只有它被确认发布，后续的消息才能继续发布`waitForConfirmsOrDie(long)`这个方法只有在消息被确认 的时候才返回，如果在指定时间范围内这个消息没有被确认那么它将抛出异常。
>
> 这种确认方式有一个最大的缺点就是:**发布速度特别的慢**，因为如果没有确认发布的消息就会 阻塞所有后续消息的发布，这种方式最多提供每秒不超过数百条发布消息的吞吐量。当然对于某 些应用程序来说这可能已经足够了。

```java
package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @Author:lz
 * @Date:2022/11/9 21:43
 * @Description
 */
public class ConfirmMessage {

    public static final int i = 1000;

    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
//        单个确认
        ConfirmMessage.publishMessageIndividually();//发布1000个单独确认消息,耗时620ms
    }

    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtil.getChannel();

        String queueName = UUID.randomUUID().toString();

        // 确认发布
        channel.confirmSelect();

        long begin = System.currentTimeMillis();

        channel.queueDeclare(queueName, false, false, false, null);

        for (int j = 0; j < i; j++) {
            String s = j + "";
            channel.basicPublish("", queueName, null, s.getBytes());

            //单个确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("发送成功");
            }
        }


        long end = System.currentTimeMillis();

        System.out.println("发布" + i + "个单独确认消息,耗时" + (end - begin) + "ms");

    }
}
```

## 批量确认发布

> 与单个等待确认消息相比，先发布一批消息然后一起确认可以极大地 提高吞吐量，当然这种方式的缺点就是:**当发生故障导致发布出现问题时，不知道是哪个消息出现问题了，我们必须将整个批处理保存在内存中，以记录重要的信息而后重新发布消息。当然这种方案仍然是同步的，也一样阻塞消息的发布**

```java
package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @Author:lz
 * @Date:2022/11/9 21:43
 * @Description
 */
public class ConfirmMessage {

    public static final int i = 1000;

    public static void main(String[] args) throws InterruptedException, TimeoutException, IOException {
//        批量确认
        ConfirmMessage.publishMessageBatch();//发布1000个批量确认消息,耗时89ms
    }

    //    批量确认
    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtil.getChannel();

        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, false, false, false, null);

        //设置发布确认
        channel.confirmSelect();

        long begin = System.currentTimeMillis();

        for (int j = 0; j < i; j++) {

            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

//            100条数据批量确认一次
            if (j % 100 == 0) {
                channel.waitForConfirms();
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("发布" + i + "个批量确认消息,耗时" + (end - begin) + "ms");

    }
}
```

