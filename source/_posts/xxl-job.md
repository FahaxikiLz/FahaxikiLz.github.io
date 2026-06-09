---
title: xxl-job
date: 2023-12-06 13:05:32
tags:
- xxl-job
categories:
- 进阶技术
---

## 一. XXL-JOB概述

### 1.1 XXL-JOB是什么？

- 官方说明：XXL-JOB是一个轻量级分布式任务调度平台，其核心设计目标是开发迅速、学习简单、轻量级、易扩展。现已开放源代码并接入多家公司线上产品线，开箱即用。
- 通俗来讲：XXL-JOB是一个任务调度框架，通过引入XXL-JOB相关的依赖，按照相关格式撰写代码后，可在其可视化界面进行任务的**启动**，**执行**，**中止**以及包含了**日志记录与查询**和**任务状态监控**
- 如果将XXL-JOB形容为一个人的话，每一个引入xxl-job的微服务就相当于一个独立的人\(执行器\)，而按照相关约定格式撰写的`Handler`为餐桌上的`食物`，可视化界面则可以决定哪个执行器\(人\)，吃东西或者不吃某个东西\(定时任务\)，在什么时间吃\(Corn表达式控制或者执行或终止或者；立即开始\)；
- Quartz的不足：Quartz作为开源任务调度中的佼佼者，是任务调度的首选。但是在集群环境中，Quartz采用API的方式对任务进行管理，这样存在以下问题：
  - 通过调用API的方式操作任务，不人性化。
  - 需要持久化业务的QuartzJobBean到底层数据表中，系统侵入性相当严重。
  - 调度逻辑和QuartzJobBean耦合在同一个项目中，这将导致一个问题，在调度任务数量逐渐增多，同时调度任务逻辑逐渐加重的情况下，此时调度系统的性能将大大受限于业务。

> XXL-JOB弥补了Quartz的上述不足之处。

- RemoteHttpJobBean:
  - 常规的Quartz的开发，任务逻辑一般维护在QuartzJobBean中，耦合很严重。

  - XXL-JOB中“调度模块”和“任务模块”完全解耦，调度模块中的所有调度任务使用同一个QuartzJobBean，即RemoteHttpJobBean。不同的调度任务将各自的调度参数维护在各自的扩展表数据中，当触发RemoteHttpJobBean执行时，将会解析不同的调度参数发起远程调用，调用各自的远程执行器服务。

  - 这种调用模型类似RPC调用，RemoteHttpJobBean提供调用代理的功能，而执行器提供远程服务的功能。
  
- 架构设计
  - XL-JOB将调度行为抽象形成“调度中心”公共平台，而平台自身并不承担业务逻辑，“调度中心”只负责发起调度请求。

  - 将任务抽象成分散的JobHandler，交由“执行器”统一管理，“执行器”负责接收调度请求并执行对应的JobHandler中的业务逻辑。

  - 因此，“调度”和“任务”两部分可以解耦成调度模块和执行模块，提高业务系统的整体稳定性和扩展性：

  - 调度模块（调度中心）：负责管理调度信息，按照调度配置发出调度请求，自身不承担业务代码。调度系统与任务解耦，提高了系统可用性和稳定性，同时调度系统的性能不再受限于任务模块；支持可视化、简单且动态的管理调度信息，包括任务的新建、更新、删除，GLUE开发和任务报警等，所有上述操作都会实时生效，同时支持监控调度结果以及执行日志，支持执行器Failover。

  - 执行模块（执行器）：负责接收调度请求并执行任务逻辑。任务模块专注于任务的执行等操作，开发和维护更加简单和高效；接收“调度中心”的执行请求、终止请求和日志请求等。
  
- XXL-JOB的系统架构，如下图所示：

   ![在这里插入图片描述](xxl-job/99422fb8542241fc95ffe1817b8157ce_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

### 1.2 特性

1.  简单：支持通过Web页面对任务进行CRUD操作，操作简单，一分钟上手；
2.  动态：支持动态修改任务状态、启动/停止任务，以及终止运行中任务，即时生效；
3.  调度中心HA（中心式）：调度采用中心式设计，“调度中心”自研调度组件并支持集群部署，可保证调度中心HA；
4.  执行器HA（分布式）：任务分布式执行，任务"执行器"支持集群部署，可保证任务执行HA；
5.  注册中心: 执行器会周期性自动注册任务, 调度中心将会自动发现注册的任务并触发执行。同时，也支持手动录入执行器地址；
6.  弹性扩容缩容：一旦有新执行器机器上线或者下线，下次调度时将会重新分配任务；
7.  路由策略：执行器集群部署时提供丰富的路由策略，包括：第一个、最后一个、轮询、随机、一致性HASH、最不经常使用、最近最久未使用、故障转移、忙碌转移等；
8.  故障转移：任务路由策略选择"故障转移"情况下，如果执行器集群中某一台机器故障，将会自动Failover切换到一台正常的执行器发送调度请求。
9.  阻塞处理策略：调度过于密集执行器来不及处理时的处理策略，策略包括：单机串行（默认）、丢弃后续调度、覆盖之前调度；
10.  任务超时控制：支持自定义任务超时时间，任务运行超时将会主动中断任务；
11.  任务失败重试：支持自定义任务失败重试次数，当任务失败时将会按照预设的失败重试次数主动进行重试；其中分片任务支持分片粒度的失败重试；
12.  任务失败告警；默认提供邮件方式失败告警，同时预留扩展接口，可方便的扩展短信、钉钉等告警方式；
13.  分片广播任务：执行器集群部署时，任务路由策略选择"分片广播"情况下，一次任务调度将会广播触发集群中所有执行器执行一次任务，可根据分片参数开发分片任务；
14.  动态分片：分片广播任务以执行器为维度进行分片，支持动态扩容执行器集群从而动态增加分片数量，协同进行业务处理；在进行大数据量业务操作时可显著提升任务处理能力和速度。
15.  事件触发：除了"Cron方式"和"任务依赖方式"触发任务执行之外，支持基于事件的触发任务方式。调度中心提供触发任务单次执行的API服务，可根据业务事件灵活触发。
16.  任务进度监控：支持实时监控任务进度；
17.  Rolling实时日志：支持在线查看调度结果，并且支持以Rolling方式实时查看执行器输出的完整的执行日志；
18.  GLUE：提供Web IDE，支持在线开发任务逻辑代码，动态发布，实时编译生效，省略部署上线的过程。支持30个版本的历史版本回溯。
19.  脚本任务：支持以GLUE模式开发和运行脚本任务，包括Shell、Python、NodeJS、PHP、PowerShell等类型脚本;
20.  命令行任务：原生提供通用命令行任务Handler（Bean任务，"CommandJobHandler"）；业务方只需要提供命令行即可；
21.  任务依赖：支持配置子任务依赖，当父任务执行结束且执行成功后将会主动触发一次子任务的执行, 多个子任务用逗号分隔；
22.  一致性：“调度中心”通过DB锁保证集群分布式调度的一致性, 一次任务调度只会触发一次执行；
23.  自定义任务参数：支持在线配置调度任务入参，即时生效；
24.  调度线程池：调度系统多线程触发调度运行，确保调度精确执行，不被堵塞；
25.  数据加密：调度中心和执行器之间的通讯进行数据加密，提升调度信息安全性；
26.  邮件报警：任务失败时支持邮件报警，支持配置多邮件地址群发报警邮件；
27.  推送maven中央仓库: 将会把最新稳定版推送到maven中央仓库, 方便用户接入和使用;
28.  运行报表：支持实时查看运行数据，如任务数量、调度次数、执行器数量等；以及调度报表，如调度日期分布图，调度成功分布图等；
29.  全异步：任务调度流程全异步化设计实现，如异步调度、异步运行、异步回调等，有效对密集调度进行流量削峰，理论上支持任意时长任务的运行；
30.  跨平台：原生提供通用HTTP任务Handler（Bean任务，"HttpJobHandler"）；业务方只需要提供HTTP链接即可，不限制语言、平台；
31.  国际化：调度中心支持国际化设置，提供中文、英文两种可选语言，默认为中文；
32.  容器化：提供官方docker镜像，并实时更新推送dockerhub，进一步实现产品开箱即用；
33.  线程池隔离：调度线程池进行隔离拆分，慢任务自动降级进入"Slow"线程池，避免耗尽调度线程，提高系统稳定性；；
34.  用户管理：支持在线管理系统用户，存在管理员、普通用户两种角色；
35.  权限控制：执行器维度进行权限控制，管理员拥有全量权限，普通用户需要分配执行器权限后才允许相关操作；

### 1.3 官方提供的信息：

- 文档地址：[www.xuxueli.com/xxl-job/#/](https://link.juejin.cn?target=http%3A%2F%2Fwww.xuxueli.com%2Fxxl-job%2F%23%2F "http://www.xuxueli.com/xxl-job/#/")
- 源码地址：
  - GitHub: [github.com/xuxueli/xxl…](https://link.juejin.cn?target=https%3A%2F%2Fgithub.com%2Fxuxueli%2Fxxl-job "https://github.com/xuxueli/xxl-job")
  - 码云： [gitee.com/xuxueli0323…](https://link.juejin.cn?target=https%3A%2F%2Fgitee.com%2Fxuxueli0323%2Fxxl-job "https://gitee.com/xuxueli0323/xxl-job")
- 环境：
  - Maven 3+
  - Jdk 1.7+
  - Mysql 5.6+
- 快速入门：
  - 快速搭建XXL-JOB框架的步骤介绍，请参考官方文档： [www.xuxueli.com/xxl-job/#/\?…](https://link.juejin.cn?target=http%3A%2F%2Fwww.xuxueli.com%2Fxxl-job%2F%23%2F%3Fid%3D%25E3%2580%258A%25E5%2588%2586%25E5%25B8%2583%25E5%25BC%258F%25E4%25BB%25BB%25E5%258A%25A1%25E8%25B0%2583%25E5%25BA%25A6%25E5%25B9%25B3%25E5%258F%25B0xxl-job%25E3%2580%258B%25E3%2580%2581 "http://www.xuxueli.com/xxl-job/#/?id=%E3%80%8A%E5%88%86%E5%B8%83%E5%BC%8F%E4%BB%BB%E5%8A%A1%E8%B0%83%E5%BA%A6%E5%B9%B3%E5%8F%B0xxl-job%E3%80%8B%E3%80%81")

## 二. XXL-JOB的使用

### 2.1 准备工作- 配置调度中心

1.  下载官方源码
2.  将项目中 /xxl-job/doc/db/ 目录下的 tables\_xxl\_job.sql 的数据库表导入数据库

### 2.2 配置执行器

 1. 引入依赖包：

    ```xml
     <!-- xxl-job-core -->
     <dependency>
         <groupId>com.cdmtc</groupId>
         <artifactId>xxl-job-core</artifactId>
         <version>2.0.2</version>
     </dependency>
    ```

2. 配置配置项： 

   ![在这里插入图片描述](xxl-job/e762bd5c1b3f4e799e2a0c14da6ab9cf_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

> 修改了端口号为8888,所以这里的record.xxl.job.admin.address**可视化界面**配置的端口号改为了8888 executor： 执行器的配置信息

- XXL-JOB执行器的相关配置项的意义，如下所示：
  - xxl.job.admin.addresses 调度中心的部署地址。**若调度中心采用集群部署，存在多个地址，则用逗号分隔。**执行器将会使用该地址进行”执行器心跳注册”和”任务结果回调”。

  - xxl.job.executor.appname 执行器的应用名称，它是执行器心跳注册的分组依据。

  - xxl.job.executor.ip 执行器的IP地址，用于”调度中心请求并触发任务”和”执行器注册”。执行器IP默认为空，表示自动获取IP。多网卡时可手动设置指定IP，手动设置IP时将会绑定Host。

  - xxl.job.executor.port 执行器的端口号，默认值为9999。单机部署多个执行器时，注意要配置不同的执行器端口。

  - xxl.job.accessToken 执行器的通信令牌，非空时启用。

  - xxl.job.executor.logpath 执行器输出的日志文件的存储路径，需要拥有该路径的读写权限。

  - xxl.job.executor.logretentiondays 执行器日志文件的定期清理功能，指定日志保存天数，日志文件过期自动删除。限制至少保存3天，否则功能不生效。

> 注意，XXL-JOB执行器的配置文件也可以交给Disconf进行托管。

 3. 创建XxlJobConfig.java

    ```java
    package com.cdmtc.config;
    
    import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    
    /**
     * xxl-job config
     *
     * @author xuxueli 2017-04-28
     */
    @Configuration
    public class XxlJobConfig {
        private Logger logger = LoggerFactory.getLogger(XxlJobConfig.class);
    
        @Value("${record.xxl.job.admin.addresses}")
        private String adminAddresses;
    
        @Value("${record.xxl.job.executor.appname}")
        private String appName;
    
        @Value("${record.xxl.job.executor.ip}")
        private String ip;
    
        @Value("${record.xxl.job.executor.port}")
        private int port;
    
        @Value("${record.xxl.job.accessToken}")
        private String accessToken;
    
        @Value("${record.xxl.job.executor.logpath}")
        private String logPath;
    
        @Value("${record.xxl.job.executor.logretentiondays}")
        private int logRetentionDays;
    
    
        @Bean(initMethod = "start", destroyMethod = "destroy")
        public XxlJobSpringExecutor xxlJobExecutor() {
            logger.info(">>>>>>>>>>> xxl-job config init.");
            XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
            xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
            xxlJobSpringExecutor.setAppName(appName);
            xxlJobSpringExecutor.setIp(ip);
            xxlJobSpringExecutor.setPort(port);
            xxlJobSpringExecutor.setAccessToken(accessToken);
            xxlJobSpringExecutor.setLogPath(logPath);
            xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
    
            return xxlJobSpringExecutor;
        }
    
        /**
         * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
         *
         *      1、引入依赖：
         *          <dependency>
         *             <groupId>org.springframework.cloud</groupId>
         *             <artifactId>spring-cloud-commons</artifactId>
         *             <version>${version}</version>
         *         </dependency>
         *
         *      2、配置文件，或者容器启动变量
         *          spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
         *
         *      3、获取IP
         *          String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
         */
    
    
    }
    
    ```

> 这是一个执行器配置类，用来读取执行器的配置信息

- XxlJobConfig配置类有两点需要注意：

- 组件扫描

  - 第2行使用@ComponentScan注解，扫描com.example.demo.jobhandler包，将其中的任务处理器加载至Spring容器。

- 获取执行器实例

  - 第29行的xxlJobExecutor\(\)方法会实例化一个XXL-JOB执行器对象，执行器初始化时调用它的start\(\)方法，执行器销毁时调用它的destroy\(\)方法。

4. 添加定时任务： 

   ![在这里插入图片描述](xxl-job/48b283b2ef434c1cb507db09ebbde689_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

### 2.3 配置可视化界面

1. 配置数据库路径以及其他信息 

   ![在这里插入图片描述](xxl-job/58a64035939249da959b2cbd8f345cf4_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

2. 配置登陆账号密码 

   ![在这里插入图片描述](xxl-job/2cf9ae7e103c4dafabed61951728dc1d_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

> 可修改为其他账号密码

3.  启动项目：XxlJobAdminApplication.java
4.  登陆可视化界面 地址： [http://10.4.7.214:8080/xxl-job-admin/jobinfo](https://link.juejin.cn?target=http%3A%2F%2F10.4.7.214%3A8080%2Fxxl-job-admin%2Fjobinfo "http://10.4.7.214:8080/xxl-job-admin/jobinfo") 【可先等配置好了执行器再进行登陆，端口可自行配置或修改，默认是8080,这里演示的端口号为8888】

> 如需配置集群等其他操作可参考官方文档

### 2.4 开发第一个任务 Hello,world

1. 首先在配置好了执行器的微服务下创建定时任务代码： 

   ![在这里插入图片描述](xxl-job/060ea8edaa3d49c59d41dc8a84193712_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

2. 依次启动微服务项目\(Eureka,config...等\)

3. 启动调度中心： XxlJobAdminApplication.java

4. 登录调度中心，输入账号密码，然后配置执行器 

   ![在这里插入图片描述](xxl-job/7e530c7641de48068036491f1046a2c4_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp) 

   ![在这里插入图片描述](xxl-job/f4035f4e44b24f1f8a274bc6da0d5c34_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

5. 进入任务管理页面 

   ![在这里插入图片描述](xxl-job/df8524c0c0f649b9a2638c3e971659ce_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

6. 点击新增任务,配置对应相关参数 

   ![v](xxl-job/a57a7cafe55240ff9355c71ff48160c2_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

7. 返回任务管理页面 

   ![在这里插入图片描述](xxl-job/91f020f8280f465d904e83738ccef3c6_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

8. 如果点击**执行任务**，则会只执行一次 

   ![在这里插入图片描述](xxl-job/ab5f20fbdb364522b0ada36199e435a2_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

9. 如果点击**日志**，则跳转至调度日志页面 

   ![在这里插入图片描述](xxl-job/63364410b6d041568d7dd394d73e384f_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

10. 如果点击**启动**，则直接启动定时任务\(按照Corn表达式定时执行任务\)，并且启动按钮会变成停止按钮 

    ![在这里插入图片描述](xxl-job/e534f79805814fcaa7ecf4c74d6c50b0_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

    如果没有点击停止按钮的话，则会一直是启动状态，如果点击了停止按钮，则定时任务停止，Corn表达式不再生效

11. 如果点击**编辑**，则进入定时任务的更新页面 

    ![在这里插入图片描述](xxl-job/c808eb0dbd364b66b3ef3fbe4551bd17_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

12.  如果点击**删除**，则直接删除此定时任务配置；
13.  如果点击**执行器**，则展示该执行器下的对应定时任务。
14.  任务描述和JobHandler则为搜索条件，对定时任务配置进行搜索；

### 2.5 运行报表

- 可视化直观展示定时任务运行情况

  ![在这里插入图片描述](xxl-job/65f243a6aefa47afb630ce2c0b4c11fa_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

## 三. 可视化界面各功能详细介绍

### 3.1 新增执行器时，需要填写的信息，如下所示：

- AppName：这是用来唯一标识每个执行器集群的应用名称，执行器会周期性地以AppName为参数进行自动注册。可通过该配置自动发现注册成功的执行器，供任务调度时使用。
- 名称：执行器的名称，因为AppName限制字母数字等组成，可读性不强，名称可以提高执行器的可读性。
- 排序：执行器的排序，系统中需要执行器的地方，如任务新增，将会按照该排序读取可用的执行器列表。
- 注册方式：调度中心获取执行器地址的方式，有以下两种：
- 自动注册：执行器自动进行执行器注册，调度中心通过底层注册表可以动态发现执行器机器地址。
- 手动录入：人工手动录入执行器的地址信息，多地址逗号分隔，供调度中心使用。
- 机器地址：只有在“注册方式”为“手动录入”时可编辑，支持人工维护执行器的地址信息。 ![在这里插入图片描述](xxl-job/8c3070bdfc744d05b60b629e5e5030bb_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

> 注意，AppName的取值应该和示例工程的application.properties文件中的xxl.job.executor.appname字段的取值相同，注册方式应该选择自动注册。新增完成之后，就可以在执行器列表中看到新建的执行器， 而我在写入的时候将applcation.properties替换为了bootstrap.yml，但内容不变；

### 3.2 新增任务时，需要填写的信息，如下所示：

- 执行器：任务绑定的执行器，任务触发调度时将会自动发现注册成功的执行器，实现任务自动发现功能；另一方面，也可以方便地进行任务分组。每个任务必须绑定一个执行器，可以在“执行器管理”页面进行设置。
- 任务描述：任务的描述信息，便于任务管理。
- 路由策略：当执行器集群部署时，提供丰富的路由策略，包括：
  - FIRST（第一个）：固定选择第一个机器。
  - LAST（最后一个）：固定选择最后一个机器。
  - ROUND（轮询）：轮流选择每台机器。
  - RANDOM（随机）：随机选择在线的机器。
  - CONSISTENT\_HASH（一致性HASH）：每个任务按照Hash算法固定选择某一台机器，且所有任务均匀散列在不同机器上。
  - LEAST\_FREQUENTLY\_USED（最不经常使用）：使用频率最低的机器优先被选举。
  - LEAST\_RECENTLY\_USED（最近最久未使用）：最久为使用的机器优先被选举。
  - FAILOVER（故障转移）：按照顺序依次进行心跳检测，第一个心跳检测成功的机器选定为目标执行器并发起调度。
  - BUSYOVER（忙碌转移）：按照顺序依次进行空闲检测，第一个空闲检测成功的机器选定为目标执行器并发起调度。
  - SHARDING\_BROADCAST\(分片广播\)：广播触发对应集群中所有机器执行一次任务，同时传递分片参数；可根据分片参数开发分片任务。
- Cron：触发任务执行的Cron表达式，具体可百度。
- 运行模式：
  - BEAN模式：任务以JobHandler的方式维护在执行器端；需要结合 “JobHandler”属性匹配执行器中的任务；
  - GLUE模式\(Java\)：任务以源码方式维护在调度中心；该模式的任务实际上是一段继承自IJobHandler的Java类代码并以“groovy”源码的方式维护，它在执行器项目中运行，可使用\@Resource/\@Autowire注入执行器里中的其他服务；
  - GLUE模式\(Shell\)：任务以源码方式维护在调度中心；该模式的任务实际上是一段“shell”脚本；
  - GLUE模式\(Python\)：任务以源码方式维护在调度中心；该模式的任务实际上是一段“python”脚本；
  - GLUE模式\(NodeJS\)：任务以源码方式维护在调度中心；该模式的任务实际上是一段“nodejs”脚本；
- JobHandler：
  - 只有在运行模式为“BEAN模式”时生效，对应执行器中新开发的JobHandler类的“\@JobHandler”注解自定义的value值。
- 子任务：
  - 每个任务都拥有一个唯一的任务ID（任务ID可以从任务列表获取），当本任务执行结束并且执行成功时，将会触发子任务ID所对应的任务的一次主动调度。
- 阻塞处理策略：调度过于密集，执行器来不及处理时的处理策略：
  - 失败告警（默认）：调度失败和执行失败时，都将会触发失败报警，默认会发送报警邮件。
  - 失败重试：调度失败时，除了进行失败告警之外，将会自动重试一次；注意在执行失败时不会重试，而是根据回调返回值判断是否重试。
- 任务参数：任务执行所需的参数，多个参数时用逗号分隔，任务执行时将会把多个参数转换成数组传入。
- 报警邮件：任务调度失败时邮件通知的邮箱地址，支持配置多邮箱地址，配置多个邮箱地址时用逗号分隔。
- 负责人：任务的负责人。

> 注意，编辑任务时也会弹出类似的窗口，其中的输入项请参考新增任务窗口。 
>
> ![在这里插入图片描述](xxl-job/e484673aca844b12bea58a5f9f112ef0_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

### 3.3 BEAN模式任务

- BEAN模式：
  - 任务逻辑以JobHandler的形式存在于“执行器”所在项目中，如我们刚刚所演示的**Hello,World** 的入门案例
- 上述代码有三点需要注意：
  - 必须使用XXL-JOB的\@JobHandler注解（第1行），指定JobHandler的名称为“demoJobHandler”，在调度中心新建任务的JobHandler字段的取值要与此相同。
  - 必须继承IJobHandler抽象类（第3行），并且实现它的execute\(\)方法，这是实现任务逻辑的方法。
  - IJobHandler抽象类还有init\(\)方法和destroy\(\)方法，这两个方法是空方法，在任务实例初始化和销毁时调用，任务实现类可以选择性地覆盖这两个方法。

### 3.4 GLUE（Java）模式任务，

- 任务以源码方式维护在调度中心，支持通过Web IDE在线更新，实时编译和生效，因此不需要指定JobHandler。开发流程如下:
- Step-1 新建调度任务
  
  - 参考上文“任务调度属性”对新建的任务进行参数配置，运行模式选择“GLUE模式\(Java\)”，如下图所示： 
  
    ![在这里插入图片描述](xxl-job/101d07127dec48808ca0864fdd47707f_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

> 调度中心会每隔15分钟调度一次这个任务。

- Step-2 开发任务代码

- 在任务列表中选中指定的GLUE（Java）任务，点击该任务右侧的“GLUE”按钮，将会前往GLUE任务的Web IDE界面，在该界面支持对任务代码进行开发（也可以在IDE中开发完成后，复制粘贴到编辑中）。

- 版本回溯功能：在GLUE任务的Web IDE界面，选择右上角下拉框“版本回溯”，会列出该GLUE任务的更新历史（支持30个版本的版本回溯），选择相应版本即可显示该版本代码，保存后GLUE代码即回退到对应的历史版本。GLUE任务代码和Web IDE界面，如下图所示： 

  ![在这里插入图片描述](xxl-job/fd33b840af97445e84e36fcdb4a7bf81_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

### 3.5 分片广播任务

- 执行器集群部署时，任务路由策略选择“分片广播”的情况下，一次任务调度将会广播触发对应集群中所有执行器执行一次任务，同时传递分片参数，可以根据分片参数开发分片任务。

- “分片广播”以执行器为维度进行分片，支持动态扩容执行器集群从而动态增加分片数量，协同进行业务处理；在进行大数据量业务操作时可显著提升任务处理能力和速度。

- “分片广播”和普通任务开发流程一致，不同之处在于可以获取分片参数，通过分片参数进行分片业务处理。开发流程如下：

  - Step-1 开发JobHandler代码

  - 在示例工程的com.example.demo.jobhandler包中，新建ShardingJobHandler任务类，关键代码如下所示：
  
    ```java
    @JobHandler(value = "shardingJobHandler")
    @Service
    public class ShardingJobHandler extends IJobHandler {
        @Override
        public ReturnT<String> execute(String param) throws Exception {
            // 分片参数
            ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
            XxlJobLogger.log("分片参数：当前分片序号 = {0}, 总分片数 = {1}", shardingVO.getIndex(), shardingVO.getTotal());
            // 业务逻辑
            for (int i = 0; i < shardingVO.getTotal(); i++) {
                if (i == shardingVO.getIndex()) {
                    XxlJobLogger.log("第 {0} 片, 命中分片开始处理", i);
                } else {
                    XxlJobLogger.log("第 {0} 片, 忽略", i);
                }
            }
            return SUCCESS;
        }
    }
    
    ```
  
    

- 上述代码的第9行获取分片参数，第10行获取分片参数的两个属性：

  - shardingVO.getIndex\(\) 当前分片序号（从0开始），执行器集群列表中当前执行器的序号。
  - shardingVO.getTotal\(\) 总分片数，执行器集群的总机器数量。

- Step-2 新建调度任务

  - 参考上文“任务调度属性”对新建的任务进行参数配置，运行模式选择“BEAN模式”，路由策略选择“分片广播”，JobHandler属性填写任务注解\@JobHandler中定义的值，如下图所示： 
  
    ![在这里插入图片描述](xxl-job/81b75ea3b5804ec49524b384c212b2fb_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

> 调度中心会每隔15分钟广播调度一次shardingJobHandler任务\(因为Corn表达式设置了15分钟执行一次\)。

- 分片广播的路由策略不仅适用于BEAN运行模式，而且也适用于GLUE（Java）运行模式。这项功能适用于以下业务场景：
- 分片任务场景
  - 10个执行器的集群来处理10w条数据，每台机器只需要处理1w条数据，耗时降低10倍。
- 广播任务场景
  - 广播执行器机器运行shell脚本、广播集群节点进行缓存更新等。

### 3.6 任务管理

![在这里插入图片描述](xxl-job/8de04c29c61e460a967fb658d9e32697_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

- 在任务列表中，可以看到每个任务的任务ID、任务描述、运行模式、Cron、负责人和状态等信息。用户可以对任务进行以下几种操作：
  - 执行：手动触发一次任务调度，不影响原有调度规则。
  - 暂停/恢复：可对任务进行“暂停”和“恢复”操作。需要注意的是，此处的暂停/恢复仅针对任务的后续调度触发行为，不会影响到已经触发的调度任务。
  - 日志：可以查看任务历史调度日志。在历史调入日志界面可查看每次任务调度的调度结果、执行结果等，点击“执行日志”按钮可查看执行器完整日志。
  - 编辑：在弹出的“编辑任务”界面更新任务属性后保存即可，可以修改设置的任务属性信息。
  - GLUE：该操作仅针对GLUE任务。将会前往GLUE任务的Web IDE界面，在该界面支持对任务代码进行开发。
  - 删除：删除这个任务。

### 3.7 任务调度日志

- 在XXL-JOB调度中心，点击进入“调度日志”页面。

1. 查看调度日志

   - 在“调度日志”页面可以查看每次任务调度的调度结果、执行结果等信息，如下图所示： 

     ![在这里插入图片描述](xxl-job/3c493839017f490bb6418cc5968cfcf0_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

   - 从调度日志可以获取以下信息：
         - 调度时间：“调度中心”触发本次调度并向“执行器”发送任务执行信号的时间。
         - 调度结果：“调度中心”触发本次调度的结果，200表示成功，500或其他表示失败。
         - 调度备注：“调度中心”触发本次调度的日志信息。
         - 执行时间：“执行器”中本次任务执行结束后回调的时间。
         - 执行结果：“执行器”中本次任务执行的结果，200表示成功，500或其他表示失败。
         - 执行备注：“执行器”中本次任务执行的日志信息。
         - 在示例工程中，调度日志位于/data/applogs/xxl-job/xxl-job-demo.log，可以在logback.xml文件中进行配置。

2. 查看执行日志

   - 点击某行日志右侧的 “执行日志” 按钮，可跳转至执行日志界面，可以查看业务代码中打印的完整日志，如下图： 

     ![在这里插入图片描述](xxl-job/98d7e3816ebb4e59ab0dc2c425dfc47b_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

3.  终止运行中的任务

- 这项功能只针对执行中的任务。在任务日志页面，点击右侧的“终止任务”按钮，将会向本次任务对应的执行器发送任务终止请求，将会终止掉本次任务，同时会清空掉整个任务执行队列，如下图所示： 

  ![在这里插入图片描述](xxl-job/ef3ca89b903946f7b17ef70e0edd6596_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

- **任务终止是通过“interrupt”执行线程的方式实现的，将会触发“InterruptedException”异常。因此，如果JobHandler内部捕获到该异常并消化掉的话，任务终止功能将不起作用。**

- **因此, 如果遇到上述任务终止不起作用的情况, 需要在JobHandler中针对“InterruptedException”异常进行特殊处理（向上抛出）。另外，在JobHandler中开启子线程时，子线程也不可捕获处理“InterruptedException”，应该主动向上抛出。**

4. 删除执行日志 在任务日志页面，选择执行器和任务之后，点击右侧的“清理”按钮将会出现“日志清理”弹框，弹框中支持选择不同类型的日志清理策略，选中后点击“确定”按钮即可进行日志清理操作，如下图所示： 

   ![在这里插入图片描述](xxl-job/fb4d0e744a384f698f2244bfce1ada67_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

### 3.8 执行失败报警

- 概述： 当定时任务执行失败的时候，日志会自动记录失败结果，并且在cdmtc.xxl-job里面的application.properties中配置了email邮箱时，可邮件提醒；

- 效果演示： 

  ![在这里插入图片描述](xxl-job/e3266e535c73496c985b42039926636c_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

- 每一次执行失败均可提醒 

  ![在这里插入图片描述](xxl-job/d676a64801f744249c1ea227bc059af6_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

- 开启短信提醒功能，需要从邮箱中获取授权码，每个邮箱的获取方式可能不同，可具体百度；qq邮箱为从 **设置** 按钮中的 **账户** ，然后选择**POP3/SMTP** 服务，点击开启，按提示获取授权码即可；

![在这里插入图片描述](xxl-job/9da93b0f7b0144c9b8df8a692851e2a1_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

- 获取授权码图示： 

  ![在这里插入图片描述](xxl-job/0a39422271814a5cabebed1e388e0327_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

> 授权码就是配置文件中的password

## 四. 远程调用XXL-JOB

### 4.1 远程调用API说明

- 这里是使用RestTemplate实现的，也可以使用其他方式实现，后期也需要考虑集群的问题，这里仅以RestTemplate作为示例远程调用接口

### 4.2 环境准备

 - pom.xml:

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
   
       <parent>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-parent</artifactId>
           <version>2.1.2.RELEASE</version>
       </parent>
       <groupId>com.demo</groupId>
       <artifactId>springboot_quick</artifactId>
       <version>1.0-SNAPSHOT</version>
   
   
       <properties>
           <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
           <xdocreport.version>1.0.5</xdocreport.version>
       </properties>
   
   
       <dependencies>
           <!--web功能的起步依赖-->
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-web</artifactId>
           </dependency>
   
           <dependency>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-test</artifactId>
               <scope>test</scope>
           </dependency>
       </dependencies>
   
   </project>
   
   ```

   

### 4.3 启动类

 - MySpringBootApplication.java

   ```java
   package com.demo;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.boot.web.client.RestTemplateBuilder;
   import org.springframework.context.annotation.Bean;
   import org.springframework.web.client.RestTemplate;
   
   /**
    * create by: zhanglei
    */
   //@EnableFeignClients
   @SpringBootApplication
   public class MySpringBootApplication {
       public static void main(String[] args) {
   
           SpringApplication.run(MySpringBootApplication.class);
       }
       @Bean
       RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
           return restTemplateBuilder.build();
       }
   }
   
   ```

   

### 4.4 测试类

 - TestDemo.java

   ```java
   import com.demo.MySpringBootApplication;
   import org.junit.Test;
   import org.junit.runner.RunWith;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.boot.test.context.SpringBootTest;
   import org.springframework.http.HttpEntity;
   import org.springframework.http.HttpHeaders;
   import org.springframework.http.ResponseEntity;
   import org.springframework.test.context.junit4.SpringRunner;
   import org.springframework.util.LinkedMultiValueMap;
   import org.springframework.util.MultiValueMap;
   import org.springframework.web.client.RestTemplate;
   import java.util.ArrayList;
   import java.util.List;
   
   /**
    * @create_by: zhanglei
    * @craete_time 2019/7/2
    */
   @SpringBootTest(classes = MySpringBootApplication.class)
   @RunWith(SpringRunner.class)
   public class TestDemo {
   
       @Test
       public void test() {
           System.out.println("hello,world");
       }
   
       @Autowired
       private RestTemplate restTemplate;
   
       /* Cookie 是根据用户名密码生成的，基本不变，可直接保存数据库或者Redis，然后读取即不必反复登录 */
       /* Cookie 如果后期Cookie有失效时间了，则可用定时任务定时刷新或者失效重登重新保存即可 */
   
       /**
        * 模拟登录并拿到Cookie
        */
       @Test
       public void login(){
           HttpHeaders headers = new HttpHeaders();
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("userName", "admin");
           map.add("password","123456");
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/login", request, String.class);
           System.out.println(response.getHeaders().get("Set-Cookie").get(0));                //      XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly
       }
   
   
   /*   组操作---> 对执行器进行操作  */
   
       /**
        * 保存组Group
        */
       @Test
       public void saveGroup(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("appName", "xxl-job-executor-cdmtc-record");        //应用名称
           map.add("title", "测试执行器");      //执行器名称
           map.add("order", "1");          //排序方式
           map.add("addressType", "1");        //注册方式 ：  0为
           map.add("addressList", "10.4.7.214:9999,10.4.7.214:9999");          //多地址逗号分隔
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobgroup/save", request, String.class);
           System.out.println(response.getBody());        // {"code":200,"msg":null,"content":null}   返回此，且数据库增加数据即为成功
       }
   
       /**
        * 修改组
        */
       @Test
       public void updateGroup(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("id","4");          //修改的，id一定不能为空
           map.add("appName", "xxl-job-executor-cdmtc-record");        //应用名称
           map.add("title", "测试执行器323223");      //执行器名称
           map.add("order", "1");          //排序方式
           map.add("addressType", "1");        //注册方式 ：  0为
           map.add("addressList", "10.4.7.214:9999");          //多地址逗号分隔
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobgroup/update", request, String.class);
           System.out.println(response.getBody());         //{"code":200,"msg":null,"content":null}
       }
   
       /**
        * 删除组
        */
       @Test
       public void removeGroup(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("id","4");          //删除的，id一定不能为空
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobgroup/remove", request, String.class);
           System.out.println(response.getBody());         //{"code":200,"msg":null,"content":null}
       }
   
       /* 定时任务操作：查询，新增,编辑，启动，停止，删除等*/
       /**
        * 获取指定的执行器下的任务列表
        */
       @Test
       public void pageList(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("jobGroup", "2");
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobinfo/pageList", request, String.class);
           System.out.println(response.getBody());             //{"recordsFiltered":4,"data":[{"id":13,"jobGroup":2,"jobCron":"0/1 * * * * ? ","jobDesc":"测试HelloWorld","addTime":1561687650000,"updateTime":1562037928000,"author":"zhanglei","alarmEmail":"1326209681@qq.com","executorRouteStrategy":"FIRST","executorHandler":"firstJobHandler","executorParam":"456464564","executorBlockStrategy":"SERIAL_EXECUTION","executorTimeout":0,"executorFailRetryCount":0,"glueType":"BEAN","glueSource":"","glueRemark":"GLUE代码初始化","glueUpdatetime":1561687650000,"childJobId":"","jobStatus":"NONE"},{"id":12,"jobGroup":2,"jobCron":"0/1 * * * * ? ","jobDesc":"测试HelloWorld","addTime":1561612429000,"updateTime":1561612429000,"author":"zhanglei","alarmEmail":"","executorRouteStrategy":"FIRST","executorHandler":"firstJobHandler","executorParam":"","executorBlockStrategy":"SERIAL_EXECUTION","executorTimeout":0,"executorFailRetryCount":0,"glueType":"BEAN","glueSource":"","glueRemark":"GLUE代码初始化","glueUpdatetime":1561612429000,"childJobId":"","jobStatus":"NONE"},{"id":4,"jobGroup":2,"jobCron":"0/1 * * * * ? ","jobDesc":"测试任务1","addTime":1561538414000,"updateTime":1561538431000,"author":"XXL","alarmEmail":"","executorRouteStrategy":"FIRST","executorHandler":"firstJobHandler","executorParam":"123","executorBlockStrategy":"SERIAL_EXECUTION","executorTimeout":100,"executorFailRetryCount":0,"glueType":"BEAN","glueSource":"","glueRemark":"GLUE代码初始化","glueUpdatetime":1561538414000,"childJobId":"","jobStatus":"NONE"},{"id":2,"jobGroup":2,"jobCron":"0/1 * * * * ? ","jobDesc":"测试任务1","addTime":1561532680000,"updateTime":1561612757000,"author":"XXL","alarmEmail":"","executorRouteStrategy":"FIRST","executorHandler":"demoJobHandler","executorParam":"123","executorBlockStrategy":"SERIAL_EXECUTION","executorTimeout":101,"executorFailRetryCount":1,"glueType":"BEAN","glueSource":"","glueRemark":"GLUE代码初始化","glueUpdatetime":1561532680000,"childJobId":"","jobStatus":"NONE"}],"recordsTotal":4}
       }
   
       /**
        * 增加定时任务配置
        */
       @Test
       public void addInfo(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("jobGroup","1");        //执行器主键id
           map.add("jobCron","0/1 * * * * ? ");        //表达式
           map.add("jobDesc","测试任务我是最新的测试任务啊啊啊啊啊啊");         //任务描述
           map.add("author","zhanglei");           //负责人
           map.add("alarmEmail","1326209681@qq.com");     //报警邮件
           map.add("executorRouteStrategy","FIRST");            //执行器路由策略
           map.add("executorHandler","测试JobHandler");              //执行器，任务Handler名称
           map.add("executorParam","121454");            //执行器，任务参数
           map.add("executorBlockStrategy","SERIAL_EXECUTION");        //阻塞处理策略
           map.add("executorTimeout","101");          //任务执行超时时间，单位秒
           map.add("executorFailRetryCount","1");       //失败重试次数
           map.add("glueType","BEAN");                 //GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
           map.add("glueSource","");               //GLUE源代码
           map.add("glueRemark","GLUE代码初始化");               //GLUE备注
           map.add("childJobId","");               //子任务ID,多个逗号分隔
   //        map.add("jobStatus","");                //任务状态 【base on quartz】
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobinfo/add", request, String.class);
           System.out.println(response.getBody());             //{"code":200,"msg":null,"content":"15"}
       }
   
       /**
        * 修改定时任务配置
        */
       @Test
       public void updateInfo(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("id","14");             //注意：修改必须带主键
           map.add("jobGroup","1");        //执行器主键id
           map.add("jobCron","0/1 * * * * ? ");        //表达式
           map.add("jobDesc","测试任务我是最新的测试任务啊啊啊啊啊啊");         //任务描述
           map.add("author","zhanglei");           //负责人
           map.add("alarmEmail","1326209681@qq.com");     //报警邮件
           map.add("executorRouteStrategy","FIRST");            //执行器路由策略
           map.add("executorHandler","测试JobHandler");              //执行器，任务Handler名称
           map.add("executorParam","121454");            //执行器，任务参数
           map.add("executorBlockStrategy","SERIAL_EXECUTION");        //阻塞处理策略
           map.add("executorTimeout","101");          //任务执行超时时间，单位秒
           map.add("executorFailRetryCount","1");       //失败重试次数
           map.add("glueType","BEAN");                 //GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
           map.add("glueSource","");               //GLUE源代码
           map.add("glueRemark","GLUE代码初始化");               //GLUE备注
           map.add("childJobId","");               //子任务ID,多个逗号分隔
   //        map.add("jobStatus","");                //任务状态 【base on quartz】
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobinfo/update", request, String.class);
           System.out.println(response.getBody());             //{"code":200,"msg":null,"content":null}
       }
   
       /**
        * 删除定时任务配置
        */
       @Test
       public void removeInfo(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("id","15");             //注意：删除必须带主键
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobinfo/remove", request, String.class);
           System.out.println(response.getBody());             //{"code":200,"msg":null,"content":null}
       }
   
       /**
        * 启动定时任务
        */
       @Test
       public void startInfo(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("id","13");             //启动的任务id
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobinfo/start", request, String.class);
           System.out.println(response.getBody());             //{"code":200,"msg":null,"content":null}
       }
   
   
       /**
        * 停止定时任务
        */
       @Test
       public void stopInfo(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("id","13");             //启动的任务id
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobinfo/stop", request, String.class);
           System.out.println(response.getBody());             //{"code":200,"msg":null,"content":null}
       }
   
       /**
        * 执行一次定时任务
        */
       @Test
       public void startOne(){
           HttpHeaders headers = new HttpHeaders();
           List<String> cookies = new ArrayList<>();
           /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
           cookies.add("XXL_JOB_LOGIN_IDENTITY=6333303830376536353837616465323835626137616465396638383162336437; Path=/; HttpOnly");
           headers.put(HttpHeaders.COOKIE,cookies);
           MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
           map.add("id","13");             //启动的任务id
           map.add("executorParam","13");             //启动的任务参数
           HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
           ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8888/xxl-job-admin/jobinfo/trigger", request, String.class);
           System.out.println(response.getBody());             //{"code":200,"msg":null,"content":null}
       }
   
   
   }
   ```

## 结语

- 除了本文介绍的常用功能之外，XXL-JOB还有任务依赖、事件触发、访问令牌、故障转移等高级功能

- 通过示例工程可知，XXL-JOB具有相当强大的统一任务调度的功能，能够适用于绝大多数需要任务调度的业务场景。但是，金无足赤人无完人，XXL-JOB也有一个小缺点，那就是缺少用户权限机制，整个系统只有一个用户，它的用户名和密码固化在调度中心（服务器端）的xxl-job-admin.properties文件之中，如下图所示： 

  ![在这里插入图片描述](xxl-job/d275f0aeded24c4e8671c8d1a006cb0a_tplv-k3u1fbpfcp-zoom-in-crop-mark_1512_0_0_0.awebp)

> 作者在XXL-JOB官方文档的TODO LIST中已经将任务权限管理功能提上日程，后续版本将会越来越完美！