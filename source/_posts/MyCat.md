---
title: MyCat
date: 2025-05-21 10:17:17
tags:
- MyCat
categories:
- 中间件
published: false
---

# 1. MyCat简介

## 1.1 MyCat 引入

​    如今随着互联网的发展，数据的量级也是成指数式的增长，从GB到TB到PB。对数据的各种操作也是愈加的困难，传统的关系性数据库已经无法满足快速查询与插入数据的需求,这个时候NoSQL的出现暂时解决了这一危机。它通过降低数据的安全性，减少对事务的支持，减少对复杂查询的支持，来获取性能上的提升。但是，在有些场合NoSQL一些折衷是无法满足使用场景的，就比如有些使用场景是绝对要有事务与安全指标的。这个时候NoSQL肯定是无法满足的，所以还是需要使用关系性数据库。如何使用关系型数据库解决海量存储的问题呢？此时就需要做数据库集群，为了提高查询性能将一个数据库的数据分散到不同的数据库中存储，为应对此问题就出现了——MyCat 。

  MyCat的目标是：低成本的将现有的单机数据库和应用平滑迁移到"云"端，解决海量数据存储和业务规模迅速增长情况下的数据存储和访问的瓶颈问题 。

![1575902332595](MyCat/1575902332595.png)   





## 1.2 MyCat 历史

1). MyCat 背后是阿里曾经开源的知名产品——Cobar。Cobar 的核心功能和优势是 MySQL 数据库分片，此产品曾经广为流传，据说最早的发起者对 Mysql 很精通，后来从阿里跳槽了，阿里随后开源的 Cobar，并维持到 2013 年年初，然后，就没有然后了。 Cobar 的思路和实现路径的确不错。基于 Java 开发的，实现了 MySQL 公开的二进制传输协议，巧妙地将自己伪装成一个 MySQL Server，目前市面上绝大多数 MySQL 客户端工具和应用都能兼容。比自己实现一个新的数据库协议要明智的多，因为生态环境在哪里摆着。 

2). MyCat 是基于 cobar 演变而来，相对于cobar来说 , 有两个显著优势 : ①. 对 cobar 的代码进行了彻底的重构，MyCat在I/O方面进行了重大改进,将原来的BIO改成了NIO, 并发量有大幅提高 ;  ②. 增加了对Order By、Group By、limit等聚合功能的支持，同时兼容绝大多数数据库成为通用的数据库中间件 。

3). 简单的说，MyCat就是：一个新颖的数据库中间件产品支持mysql集群，或者 mariadb cluster，提供高可用性数据分片集群。你可以像使用mysql一样使用 MyCat 。对于开发人员来说根本感觉不到MyCat的存在。

![1571824365101](MyCat/1571824365101.png) 



## 1.3 MyCat 优势

MyCat 是一个彻底开源的，面向企业应用数据库中间件 , 支持事务， 可以视为MySQL集群的企业级数据库，用来替代昂贵的Oracle集群, 在MyCat 中融合内存缓存技术、NoSQL技术、HDFS大数据的新型SQL Server , 并结合传统数据库和新型分布式数据仓库的新一代企业级数据库中间件产品 。

并具有优势: 

**1). 性能可靠稳定**

基于阿里开源的Cobar产品而研发，Cobar的稳定性、可靠性、优秀的架构和性能以及众多成熟的使用案例使得MyCat一开始就拥有一个很好的起点，站在巨人的肩膀上，我们能看到更远。业界优秀的开源项目和创新思路被广泛融入到MyCat的基因中，使得MyCat在很多方面都领先于目前其他一些同类的开源项目，甚至超越某些商业产品。

**2). 强大的技术团队**

MyCat 现在由一支强大的技术团队维护 , 吸引和聚集了一大批业内大数据和云计算方面的资深工程师、架构师、DBA，优秀的团队保障了MyCat的稳定高效运行。而且MyCat不依托于任何商业公司，而且得到大批开源爱好者的支持。

**3). 体系完善**

MyCat已经形成了一系列的周边产品,比较有名的是 MyCat-web、MyCat-NIO、MyCat-Balance等,已经形成了一个比较完整的解决方案,而不仅仅是一个中间件。

**4). 社区活跃**

与MyCat数据库中间件类似的产品还有 TDDL、Amoeba、Cobar 。

①. TDDL（Taobao Distributed Data Layer）不同于其它几款产品，并非独立的中间件，只能算作中间层，是以Jar包方式提供给应用调用 ，属于JDBC Shard的思想 。

②. Amoeba是作为一个真正的独立中间件提供服务,应用去连接Amoeba操作MySQL集群，就像操作单个MySQL一样。Amoeba算中间件中的早期产品,后端还在使用JDBC Driver。

③. Cobar是在Amoeba基础上进化的版本，一个显著变化是把后端JDBC Driver改为原生的MySQL通信协议层。

④. MyCat又是在Cobar基础上发展的版本, 性能优良, 功能强大, 社区活跃 。



## 1.4 MyCat 使用场合

要想用好MyCat，就需要了解其适用场景，以下几个场景适合适用MyCat。

**1). 高可用性与MySQL读写分离**

高可用：利用MyCat可以轻松实现热备份，当一台服务器停机时，可以由集群中的另一台服务器自动接管业务，无需人工干预，从而保证高可用。

读写分离：通过MySQL数据库的binlog日志完成主从复制，并可以通过MyCat轻松实现读写分离，实现insert、update、delete走主库，而在select时走从库，从而缓解单台服务器的访问压力。



**2). 业务数据分级存储保障**

企业的数据量总是无休止的增长，这些数据的格式不一样，访问效率不一样，重要性也不一样。可以针对不同级别的数据，采用不同的存储设备，通过分级存储管理软件实现数据客体在存储设备之间自动迁移及自动访问切换。



**3). 大表水平拆分，集群并行计算**

数据切分是MyCat的核心功能，是指通过某种特定的条件，将存放在同一个数据库的数据，分散存储在多个数据库中，以达到分散单台设备负载的效果。当数据库量超过800万行且需要做分片时，就可以考虑使用MyCat实现数据切分。



**4). 数据库路由器**

MyCat基于MySQL实例的连接池复用机制，可以让每个应用最大程度共享一个MySQL实例的所有连接池，让数据库的并发访问能力大大提升。



**5). 整合多种数据源**

当一个项目中使用了多个数据库（Oracle，MySQL，SQL Server,PostgreSQL），并配置了多个数据源，操作起来就比较烦锁，这时就可以使用MyCat进行整合，最终我们的应用程序只需要访问一个数据源即可。



## 1.5 MyCat 下载

下载地址: https://github.com/MyCatApache/MyCat-download

![1573485852837](MyCat/1573485852837.png)  



最新下载地址: http://dl.MyCat.io/

![1573485998327](MyCat/1573485998327.png)  





# 2. MyCat入门

本章节主要讲解MyCat的在Linux中的安装、基本概念和基本原理，这些是学习MyCat的基础。

## 2.1 环境搭建

MyCat是采用java语言开发的开源的数据库中间件，支持Windows和Linux运行环境，下面介绍MyCat的Linux中的环境搭建。

A. MySQL

B. JDK

C. MyCat



### 2.1.1 安装配置MySQL

#### 2.1.1.1 安装MySQL

```
A). 卸载 centos 中预安装的 mysql
	
	rpm -qa | grep -i mysql
	
	rpm -e mysql-libs-5.1.71-1.el6.x86_64 --nodeps
	
B). 上传 mysql 的安装包
	
	alt + p -------> put  E:/test/MySQL-5.6.22-1.el6.i686.rpm-bundle.tar

C). 解压 mysql 的安装包 
	
	mkdir mysql
	
	tar -xvf MySQL-5.6.22-1.el6.i686.rpm-bundle.tar -C /root/mysql
	
D). 安装依赖包 
	
	yum -y install libaio.so.1 libgcc_s.so.1 libstdc++.so.6 libncurses.so.5 --setopt=protected_multilib=false
	
	yum  update libstdc++-4.4.7-4.el6.x86_64
	
E). 安装 mysql-client
	
	rpm -ivh MySQL-client-5.6.22-1.el6.i686.rpm
	
F). 安装 mysql-server
	
	rpm -ivh MySQL-server-5.6.22-1.el6.i686.rpm
	
```



#### 2.1.1.2 启动停止MySQL

```
service mysql start

service mysql stop

service mysql status

service mysql restart
```



#### 2.1.1.3 登录MySQL

```
mysql 安装完成之后, 会自动生成一个随机的密码, 并且保存在一个密码文件中 : /root/.mysql_secret

mysql -u root -p 

登录之后, 修改密码 :

set password = password('itcast');

授权远程访问 : 

grant all privileges on *.* to 'root' @'%' identified by 'itcast';
flush privileges;

```

授权远程访问之后 , 就可以通过sqlYog来连接Linux上的MySQL , 但是记得关闭Linux上的防火墙(或者配置防火墙): 

![1573536143760](MyCat/1573536143760.png) 



### 2.1.2 安装JDK1.8

```shell
A. 上传JDK的安装包到Linux的root目录下
	alt + p -----------> put D:/jdk-8u181-linux-x64.tar.gz
	
B. 解压压缩包 , 到 /usr/share 目录下
	tar -zxvf jdk-8u181-linux-x64.tar.gz -C /usr/share/

C. 配置PATH环境变量 , 在该配置文件(/etc/profile)的最后加入如下配置
	export JAVA_HOME=/usr/share/jdk1.8.0_181
	export PATH=$PATH:$JAVA_HOME/bin
```





### 2.1.3 安装MyCat

1). 上传MyCat的压缩包
​    alt + p --------> put D:/MyCat-server-1.6.7.3-release-20190927161129-linux.tar.gz

2). 解压MyCat的压缩包
​    tar -zxvf MyCat-server-1.6.7.3-release-20190927161129-linux.tar.gz -C /usr/local	

3). MyCat的目录结构介绍

​    ![1573556739880](MyCat/1573556739880.png)  



## 2.2 MyCat核心概念

### 2.2.1 分片

简单来说，就是指通过某种特定的条件，将我们存放在同一个数据库中的数据分散存放到多个数据库（主机）上面，以达到分散单台设备负载的效果。 
数据的切分（Sharding）根据其切分规则的类型，可以分为两种切分模式。

1). 一种是按照不同的表（或者Schema）来切分到不同的数据库（主机）之上，这种切分可以称之为数据的垂直（纵向）切分。

![1573622314361](MyCat/1573622314361.png)  

2). 另外一种则是根据表中的数据的逻辑关系，将同一个表中的数据按照某种条件拆分到多台数据库（主机）上面，这种切分称之为数据的水平（横向）切分。

![1573622358104](MyCat/1573622358104.png)  

MyCat 分片策略 :

![1573659631804](MyCat/1573659631804.png)  

虚线以上是逻辑结构图, 虚线以下是物理结构图 ;



### 2.2.2 逻辑库(schema)

MyCat是一个数据库中间件，通常对实际应用来说，并不需要知道中间件的存在，业务开发人员只需要知道数据库的概念，所以数据库中间件可以被看做是一个或多个数据库集群构成的逻辑库。



### 2.2.3 逻辑表（table）

既然有逻辑库，那么就会有逻辑表，分布式数据库中，对应用来说，读写数据的表就是逻辑表。逻辑表，可以是数据切分后，分布在一个或多个分片库中，也可以不做数据切分，不分片，只有一个表构成。

1). 分片表

是指那些原有的很大数据的表，需要切分到多个数据库的表，这样，每个分片都有一部分数据，所有分片构成了完整的数据。 总而言之就是需要进行分片的表。如 ：tb_order 表是一个分片表, 数据按照规则被切分到dn1、dn2两个节点。



2). 非分片表

一个数据库中并不是所有的表都很大，某些表是可以不用进行切分的，非分片是相对分片表来说的，就是那些不需要进行数据切分的表。如： tb_city是非分片表 , 数据只存于其中的一个节点 dn1 上。



3). ER表

关系型数据库是基于实体关系模型(Entity Relationship Model)的, MyCat中的ER表便来源于此。 MyCat提出了基于ER关系的数据分片策略 , 字表的记录与其所关联的父表的记录存放在同一个数据分片中, 通过表分组(Table Group)保证数据关联查询不会跨库操作。



4). 全局表

在一个大型的项目中,会存在一部分字典表(码表) , 在其中存储的是项目中的一些基础的数据 , 而这些基础的数据 , 数据量都不大 , 在各个业务表中可能都存在关联 。当业务表由于数据量大而分片后 ， 业务表与附属的数据字典表之间的关联查询就变成了比较棘手的问题 ， 在MyCat中可以通过数据冗余来解决这类表的关联查询 ， 即所有分片都复制这一份数据（数据字典表），因此可以把这些冗余数据的表定义为全局表。



### 2.2.4 分片节点(dataNode)

数据切分后，一个大表被分到不同的分片数据库上面，每个表分片所在的数据库就是分片节点（dataNode）。



### 2.2.5 节点主机(dataHost)

数据切分后，每个分片节点（dataNode）不一定都会独占一台机器，同一机器上面可以有多个分片数据库，这样一个或多个分片节点（dataNode）所在的机器就是节点主机（dataHost）,为了规避单节点主机并发数限制，尽量将读写压力高的分片节点（dataNode）均衡的放在不同的节点主机（dataHost）。



### 2.2.6 分片规则(rule)

前面讲了数据切分，一个大表被分成若干个分片表，就需要一定的规则，这样按照某种业务规则把数据分到某个分片的规则就是分片规则，数据切分选择合适的分片规则非常重要，将极大的避免后续数据处理的难度。



## 2.3 分片配置测试

### 2.3.1 需求

由于 TB_TEST 表中数据量很大, 现在需要对 TB_TEST 表进行数据分片, 分为三个数据节点 , 每一个节点主机位于不同的服务器上, 具体的结构 ,参考下图 : 

![1575724614686](MyCat/1575724614686.png)  



### 2.3.2 环境准备

准备三台虚拟机 , 且安装好MySQL , 并配置好 : 

```
IP 地址列表 : 
	192.168.192.157
	192.168.192.158
	192.168.192.159
```



### 2.3.3 配置 schema.xml 

schema.xml 作为MyCat中重要的配置文件之一，管理着MyCat的逻辑库、逻辑表以及对应的分片规则、DataNode以及DataSource。弄懂这些配置，是正确使用MyCat的前提。这里就一层层对该文件进行解析。

| 属性     | 含义                                                         |
| -------- | ------------------------------------------------------------ |
| schema   | 标签用于定义MyCat实例中的逻辑库                              |
| table    | 标签定义了MyCat中的逻辑表, rule用于指定分片规则，auto-sharding-long的分片规则是按ID值的范围进行分片 1-5000000 为第1片  5000001-10000000 为第2片....  具体设置我们会在第四节中讲解。 |
| dataNode | 标签定义了MyCat中的数据节点，也就是我们通常说所的数据分片。  |
| dataHost | 标签在mycat逻辑库中也是作为最底层的标签存在，直接定义了具体的数据库实例、读写分离配置和心跳语句。 |



在服务器上创建3个数据库，命名为 db1

修改schema.xml如下：

```xml
<?xml version="1.0"?>
<!DOCTYPE MyCat:schema SYSTEM "schema.dtd">
<MyCat:schema xmlns:MyCat="http://io.MyCat/">
    <!-- 逻辑库配置 -->
	<schema name="ITCAST" checkSQLschema="false" sqlMaxLimit="100">
        <!-- 逻辑表配置 -->
		<table name="TB_TEST" dataNode="dn1,dn2,dn3" rule="auto-sharding-long" />
	</schema>
    
    <!-- 数据节点配置 -->
	<dataNode name="dn1" dataHost="host1" database="db1" />
	<dataNode name="dn2" dataHost="host2" database="db1" />
	<dataNode name="dn3" dataHost="host3" database="db1" />
    
    <!-- 节点主机配置 -->
	<dataHost name="host1" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.192.157:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
    <dataHost name="host2" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.192.158:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
    <dataHost name="host3" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.192.159:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
</MyCat:schema>
```



### 2.3.4 配置 server.xml

server.xml几乎保存了所有MyCat需要的系统配置信息。最常用的是在此配置用户名、密码及权限。在system中添加UTF-8字符集设置，否则存储中文会出现问号

```xml
<property name="charset">utf8</property>
```

修改user的设置 ,  我们这里为 ITCAST 设置了两个用户 : 

```xml
<user name="root">
    <property name="password">123456</property>
    <property name="schemas">ITCAST</property>
</user>

<user name="test">
    <property name="password">123456</property>
    <property name="schemas">ITCAST</property>
</user>
```

并且需要将原来的逻辑库的配置 , 替换为 ITCAST逻辑库 ;



### 2.3.5 启动MyCat

启动:

```
bin/MyCat start
bin/MyCat stop
bin/MyCat status
```

查看MyCat:

连接端口号  8066 

1). 通过命令行

```
mysql -h 127.0.0.1 -P 8066 -u root -p
```

![1573916419746](MyCat/1573916419746.png) 



2). 通过sqlyog连接

![1573832298131](MyCat/1573832298131.png) 



### 2.3.6 MyCat分片测试

进入MyCat ，执行下列语句创建一个表

```sql
CREATE TABLE TB_TEST (
  id BIGINT(20) NOT NULL,
  title VARCHAR(100) NOT NULL ,
  PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8 ;
```



我们再查看MySQL的3个库，发现表都自动创建好啦。好神奇。

接下来是插入表数据，注意，在写 INSERT 语句时一定要写把字段列表写出来，否则会出现下列错误提示：

错误代码： 1064

partition table, insert must provide ColumnList

我们试着插入一些数据：

```sql
INSERT INTO TB_TEST(ID,TITLE) VALUES(1,'goods1');
INSERT INTO TB_TEST(ID,TITLE) VALUES(2,'goods2');
INSERT INTO TB_TEST(ID,TITLE) VALUES(3,'goods3');
```

我们会发现这些数据被写入到第一个节点中了，那什么时候数据会写到第二个节点中呢？

我们插入下面的数据就可以插入第二个节点了

```sql
INSERT INTO TB_TEST(ID,TITLE) VALUES(5000001,'goods5000001');
```

因为我们采用的分片规则是每节点存储500万条数据，所以当ID大于5000000则会存储到第二个节点上。

目前只设置了两个节点，如果数据大于1000万条，会怎么样呢？执行下列语句测试一下

```sql
INSERT INTO TB_TEST(ID,TITLE) VALUES(10000001,'goods10000001');
```





## 2.4 MyCat原理介绍

MyCat原理中最重要的一个动词就是 "拦截", 它拦截了用户发送过来的SQL语句, 首先对SQL语句做一些特定的分析,如分片分析、路由分析、读写分离分析、缓存分析等，然后将此SQL语句发往后端的真实数据库，并将返回的结果做适当处理，最终再返回给用户，如图所示。

![1577358066323](MyCat/1577358066323.png) 

在图中,user表被分为三个分片节点dn1、dn2、dn3, 他们分布式在三个MySQL Server(dataHost)上，因此可以使用1-N台服务器来分片,分片规则(sharding rule)为典型的字符串枚举分片规则, 一个规则的定义是分片字段+分片函数。这里的分片字段为 status，分片函数则为字符串枚举方式。

MyCat收到一条SQL语句时，首先解析SQL语句涉及到的表，接着查看此表的定义，如果该表存在分片规则，则获取SQL语句里分片字段的值，并匹配分片函数，得到该SQL语句对应的分片列表，然后将SQL语句发送到相应的分片去执行，最后处理所有分片返回的数据并返回给客户端。以"select * from user where status='0'" 为例, 查找 status='0' ,按照分片函数, '0' 值存放在dn1,于是SQL语句被发送到第一个节点中执行, 然后再将查询的结果返回给用户。

如果发送的SQL语句为 "select * from user where status in ('0','1')" , 那么SQL语句会被发送到dn1,dn2对应的主机上执行, 然后将结果集合并后输出给用户。



# 3. MyCat配置文件详解

## 3.1 server.xml

### 3.1.1 system 标签

| 属性                      | 取值       | 含义                                                         |
| ------------------------- | ---------- | ------------------------------------------------------------ |
| charset                   | utf8       | 设置Mycat的字符集, 字符集需要与MySQL的字符集保持一致         |
| nonePasswordLogin         | 0,1        | 0为需要密码登陆、1为不需要密码登陆 ,默认为0，设置为1则需要指定默认账户 |
| useHandshakeV10           | 0,1        | 使用该选项主要的目的是为了能够兼容高版本的jdbc驱动, 是否采用HandshakeV10Packet来与client进行通信, 1:是, 0:否 |
| useSqlStat                | 0,1        | 开启SQL实时统计, 1 为开启 , 0 为关闭 ;<br />开启之后, MyCat会自动统计SQL语句的执行情况 ;<br />mysql -h 127.0.0.1 -P 9066 -u root -p<br />查看MyCat执行的SQL, 执行效率比较低的SQL , SQL的整体执行情况、读写比例等 ;<br />show @@sql ; show @@sql.slow ; show @@sql.sum ; |
| useGlobleTableCheck       | 0,1        | 是否开启全局表的一致性检测。1为开启 ，0为关闭 。             |
| sqlExecuteTimeout         | 1000       | SQL语句执行的超时时间 , 单位为 s ;                           |
| sequnceHandlerType        | 0,1,2      | 用来指定Mycat全局序列类型，0 为本地文件，1 为数据库方式，2 为时间戳列方式，默认使用本地文件方式，文件方式主要用于测试 |
| sequnceHandlerPattern     | 正则表达式 | 必须带有MYCATSEQ_或者 mycatseq_进入序列匹配流程 注意MYCATSEQ_有空格的情况 |
| subqueryRelationshipCheck | true,false | 子查询中存在关联查询的情况下,检查关联字段中是否有分片字段 .默认 false |
| useCompression            | 0,1        | 开启mysql压缩协议 , 0 : 关闭, 1 : 开启                       |
| fakeMySQLVersion          | 5.5,5.6    | 设置模拟的MySQL版本号                                        |
| defaultSqlParser          |            | 由于MyCat的最初版本使用了FoundationDB的SQL解析器, 在MyCat1.3后增加了Druid解析器, 所以要设置defaultSqlParser属性来指定默认的解析器; 解析器有两个 : druidparser 和 fdbparser, 在MyCat1.4之后,默认是druidparser, fdbparser已经废除了 |
| processors                | 1,2....    | 指定系统可用的线程数量, 默认值为CPU核心 x 每个核心运行线程数量; processors 会影响processorBufferPool, processorBufferLocalPercent, processorExecutor属性, 所有, 在性能调优时, 可以适当地修改processors值 |
| processorBufferChunk      |            | 指定每次分配Socket Direct Buffer默认值为4096字节, 也会影响BufferPool长度, 如果一次性获取字节过多而导致buffer不够用, 则会出现警告, 可以调大该值 |
| processorExecutor         |            | 指定NIOProcessor上共享 businessExecutor固定线程池的大小; MyCat把异步任务交给 businessExecutor线程池中, 在新版本的MyCat中这个连接池使用频次不高, 可以适当地把该值调小 |
| packetHeaderSize          |            | 指定MySQL协议中的报文头长度, 默认4个字节                     |
| maxPacketSize             |            | 指定MySQL协议可以携带的数据最大大小, 默认值为16M             |
| idleTimeout               | 30         | 指定连接的空闲时间的超时长度;如果超时,将关闭资源并回收, 默认30分钟 |
| txIsolation               | 1,2,3,4    | 初始化前端连接的事务隔离级别,默认为 REPEATED_READ , 对应数字为3<br />READ_UNCOMMITED=1;<br />READ_COMMITTED=2;<br />REPEATED_READ=3;<br />SERIALIZABLE=4; |
| sqlExecuteTimeout         | 300        | 执行SQL的超时时间, 如果SQL语句执行超时,将关闭连接; 默认300秒; |
| serverPort                | 8066       | 定义MyCat的使用端口, 默认8066                                |
| managerPort               | 9066       | 定义MyCat的管理端口, 默认9066                                |



### 3.1.2 user 标签

```xml
<user name="root" defaultAccount="true">
    <property name="password">123456</property>
    <property name="schemas">ITCAST</property>
    <property name="readOnly">true</property>
    <property name="benchmark">1000</property>
    <property name="usingDecrypt">0</property>
    
    <!-- 表级 DML 权限设置 -->
    <!-- 		
    <privileges check="false">
        <schema name="TESTDB" dml="0110" >
            <table name="tb01" dml="0000"></table>
            <table name="tb02" dml="1111"></table>
        </schema>
    </privileges>		
    -->
</user>
```

user标签主要用于定义登录MyCat的用户和权限 :

1). \<user name="root" defaultAccount="true"> : name 属性用于声明用户名 ;

2). \<property name="password">123456\</property> : 指定该用户名访问MyCat的密码 ;

3). \<property name="schemas">ITCAST\</property> : 能够访问的逻辑库, 多个的话, 使用 "," 分割

4). \<property name="readOnly">true\</property> : 是否只读

5). \<property name="benchmark">11111\</property> : 指定前端的整体连接数量 , 0 或不设置表示不限制 

6). \<property name="usingDecrypt">0\</property> : 是否对密码加密默认 0 否 , 1 是

```
java -cp MyCat-server-1.6.7.3-release.jar io.MyCat.util.DecryptUtil 0:root:123456
```

7). \<privileges check="false">

A. 对用户的 schema 及 下级的 table 进行精细化的 DML 权限控制; 

B. privileges 节点中的 check 属性是用 于标识是否开启 DML 权限检查， 默认 false 标识不检查，当然 privileges 节点不配置，等同 check=false, 由于 MyCat 一个用户的 schemas 属性可配置多个 schema ，所以 privileges 的下级节点 schema 节点同样 可配置多个，对多库多表进行细粒度的 DML 权限控制;

C. 权限修饰符四位数字(0000 - 1111)，对应的操作是 IUSD ( 增，改，查，删 )。同时配置了库跟表的权限，就近原则。以表权限为准。



### 3.1.3 firewall 标签

firewall标签用来定义防火墙；firewall下whitehost标签用来定义 IP白名单 ，blacklist用来定义 SQL黑名单。

```xml
<firewall>
    <!-- 白名单配置 -->
    <whitehost>
        <host user="root" host="127.0.0.1"></host>
    </whitehost>
    <!-- 黑名单配置 -->
    <blacklist check="true">
        <property name="selelctAllow">false</property>
    </blacklist>
</firewall>
```

黑名单拦截明细配置:

| 配置项                      | 缺省值 | 描述                                                         |
| --------------------------- | ------ | ------------------------------------------------------------ |
| selelctAllow                | true   | 是否允许执行 SELECT 语句                                     |
| selectAllColumnAllow        | true   | 是否允许执行 SELECT * FROM T 这样的语句。如果设置为 false，不允许执行 select * from t，但可以select * from (select id, name from t) a。这个选项是防御程序通过调用 select * 获得数据表的结构信息。 |
| selectIntoAllow             | true   | SELECT 查询中是否允许 INTO 字句                              |
| deleteAllow                 | true   | 是否允许执行 DELETE 语句                                     |
| updateAllow                 | true   | 是否允许执行 UPDATE 语句                                     |
| insertAllow                 | true   | 是否允许执行 INSERT 语句                                     |
| replaceAllow                | true   | 是否允许执行 REPLACE 语句                                    |
| mergeAllow                  | true   | 是否允许执行 MERGE 语句，这个只在 Oracle 中有用              |
| callAllow                   | true   | 是否允许通过 jdbc 的 call 语法调用存储过程                   |
| setAllow                    | true   | 是否允许使用 SET 语法                                        |
| truncateAllow               | true   | truncate 语句是危险，缺省打开，若需要自行关闭                |
| createTableAllow            | true   | 是否允许创建表                                               |
| alterTableAllow             | true   | 是否允许执行 Alter Table 语句                                |
| dropTableAllow              | true   | 是否允许修改表                                               |
| commentAllow                | false  | 是否允许语句中存在注释，Oracle 的用户不用担心，Wall 能够识别 hints和注释的区别 |
| noneBaseStatementAllow      | false  | 是否允许非以上基本语句的其他语句，缺省关闭，通过这个选项就能够屏蔽 DDL。 |
| multiStatementAllow         | false  | 是否允许一次执行多条语句，缺省关闭                           |
| useAllow                    | true   | 是否允许执行 mysql 的 use 语句，缺省打开                     |
| describeAllow               | true   | 是否允许执行 mysql 的 describe 语句，缺省打开                |
| showAllow                   | true   | 是否允许执行 mysql 的 show 语句，缺省打开                    |
| commitAllow                 | true   | 是否允许执行 commit 操作                                     |
| rollbackAllow               | true   | 是否允许执行 roll back 操作                                  |
| 拦截配置－永真条件          |        |                                                              |
| selectWhereAlwayTrueCheck   | true   | 检查 SELECT 语句的 WHERE 子句是否是一个永真条件              |
| selectHavingAlwayTrueCheck  | true   | 检查 SELECT 语句的 HAVING 子句是否是一个永真条件             |
| deleteWhereAlwayTrueCheck   | true   | 检查 DELETE 语句的 WHERE 子句是否是一个永真条件              |
| deleteWhereNoneCheck        | false  | 检查 DELETE 语句是否无 where 条件，这是有风险的，但不是 SQL 注入类型的风险 |
| updateWhereAlayTrueCheck    | true   | 检查 UPDATE 语句的 WHERE 子句是否是一个永真条件              |
| updateWhereNoneCheck        | false  | 检查 UPDATE 语句是否无 where 条件，这是有风险的，但不是SQL 注入类型的风险 |
| conditionAndAlwayTrueAllow  | false  | 检查查询条件(WHERE/HAVING 子句)中是否包含 AND 永真条件       |
| conditionAndAlwayFalseAllow | false  | 检查查询条件(WHERE/HAVING 子句)中是否包含 AND 永假条件       |
| conditionLikeTrueAllow      | true   | 检查查询条件(WHERE/HAVING 子句)中是否包含 LIKE 永真条件      |
| 其他拦截配置                |        |                                                              |
| selectIntoOutfileAllow      | false  | SELECT ... INTO OUTFILE 是否允许，这个是 mysql 注入攻击的常见手段，缺省是禁止的 |
| selectUnionCheck            | true   | 检测 SELECT UNION                                            |
| selectMinusCheck            | true   | 检测 SELECT MINUS                                            |
| selectExceptCheck           | true   | 检测 SELECT EXCEPT                                           |
| selectIntersectCheck        | true   | 检测 SELECT INTERSECT                                        |
| mustParameterized           | false  | 是否必须参数化，如果为 True，则不允许类似 WHERE ID = 1 这种不参数化的 SQL |
| strictSyntaxCheck           | true   | 是否进行严格的语法检测，Druid SQL Parser 在某些场景不能覆盖所有的SQL 语法，出现解析 SQL 出错，可以临时把这个选项设置为 false，同时把 SQL 反馈给 Druid 的开发者。 |
| conditionOpXorAllow         | false  | 查询条件中是否允许有 XOR 条件。XOR 不常用，很难判断永真或者永假，缺省不允许。 |
| conditionOpBitwseAllow      | true   | 查询条件中是否允许有"&"、"~"、"\|"、"^"运算符。              |
| conditionDoubleConstAllow   | false  | 查询条件中是否允许连续两个常量运算表达式                     |
| minusAllow                  | true   | 是否允许 SELECT * FROM A MINUS SELECT * FROM B 这样的语句    |
| intersectAllow              | true   | 是否允许 SELECT * FROM A INTERSECT SELECT * FROM B 这样的语句 |
| constArithmeticAllow        | true   | 拦截常量运算的条件，比如说 WHERE FID = 3 - 1，其中"3 - 1"是常量运算表达式。 |
| limitZeroAllow              | false  | 是否允许 limit 0 这样的语句                                  |
| 禁用对象检测配置            |        |                                                              |
| tableCheck                  | true   | 检测是否使用了禁用的表                                       |
| schemaCheck                 | true   | 检测是否使用了禁用的 Schema                                  |
| functionCheck               | true   | 检测是否使用了禁用的函数                                     |
| objectCheck                 | true   | 检测是否使用了“禁用对对象”                                   |
| variantCheck                | true   | 检测是否使用了“禁用的变量”                                   |
| readOnlyTables              | 空     | 指定的表只读，不能够在 SELECT INTO、DELETE、UPDATE、INSERT、MERGE 中作为"被修改表"出现 |



## 3.2 schema.xml

schema.xml 作为MyCat中最重要的配置文件之一 , 涵盖了MyCat的逻辑库 、 表 、 分片规则、分片节点及数据源的配置。

### 3.2.1 schema 标签

```xml
<schema name="ITCAST" checkSQLschema="false" sqlMaxLimit="100">
	<table name="TB_TEST" dataNode="dn1,dn2,dn3" rule="auto-sharding-long" />
</schema>
```

schema 标签用于定义 MyCat实例中的逻辑库 , 一个MyCat实例中, 可以有多个逻辑库 , 可以通过 schema 标签来划分不同的逻辑库。MyCat中的逻辑库的概念 ， 等同于MySQL中的database概念 , 需要操作某个逻辑库下的表时, 也需要切换逻辑库:

```
use ITCAST;
```



#### 3.2.1.1 属性

schema 标签的属性如下 : 

1). name

指定逻辑库的库名 , 可以自己定义任何字符串 ;



2). checkSQLschema

取值为 true / false ;

如果设置为true时 , 如果我们执行的语句为 "select * from ITCAST.TB_TEST;" , 则MyCat会自动把schema字符去掉, 把SQL语句修改为 "select * from TB_TEST;" 可以避免SQL发送到后端数据库执行时, 报table不存在的异常 。

不过当我们在编写SQL语句时, 指定了一个不存在schema, MyCat是不会帮我们自动去除的 ,这个时候数据库就会报错, 所以在编写SQL语句时,最好不要加逻辑库的库名, 直接查询表即可。



3). sqlMaxLimit

当该属性设置为某个数值时,每次执行的SQL语句如果没有加上limit语句, MyCat也会自动在limit语句后面加上对应的数值 。也就是说， 如果设置了该值为100，则执行 select * from TB_TEST 与 select * from TB_TEST limit 100 是相同的效果 。

所以在正常的使用中, 建立设置该值 , 这样就可以避免每次有过多的数据返回。



#### 3.2.1.2 子标签table

table 标签定义了MyCat中逻辑库schema下的逻辑表 , 所有需要拆分的表都需要在table标签中定义 。

```xml
<table name="TB_TEST" dataNode="dn1,dn2,dn3" rule="auto-sharding-long" />
```

属性如下 ： 

![1574954845578](MyCat/1574954845578.png) 



1). name 

定义逻辑表的表名 , 在该逻辑库下必须唯一。



2). dataNode

定义的逻辑表所属的dataNode , 该属性需要与dataNode标签中的name属性的值对应。 如果一张表拆分的数据，存储在多个数据节点上，多个节点的名称使用","分隔 。

![1574955059453](MyCat/1574955059453.png) 



3). rule

该属性用于指定逻辑表的分片规则的名字, 规则的名字是在rule.xml文件中定义的, 必须与tableRule标签中name属性对应。

![1574955534319](MyCat/1574955534319.png) 



4). ruleRequired

该属性用于指定表是否绑定分片规则, 如果配置为true, 但是没有具体的rule, 程序会报错。



5). primaryKey

逻辑表对应真实表的主键

如: 分片规则是使用主键进行分片, 使用主键进行查询时, 就会发送查询语句到配置的所有的datanode上; 如果使用该属性配置真实表的主键, 那么MyCat会缓存主键与具体datanode的信息, 再次使用主键查询就不会进行广播式查询了, 而是直接将SQL发送给具体的datanode。



6). type

该属性定义了逻辑表的类型，目前逻辑表只有全局表和普通表。

全局表：type的值是 global , 代表 全局表 。

普通表：无



7). autoIncrement

mysql对非自增长主键，使用last_insert_id() 是不会返回结果的，只会返回0。所以，只有定义了自增长主键的表，才可以用last_insert_id()返回主键值。
MyCat提供了自增长主键功能，但是对应的mysql节点上数据表，没有auto_increment,那么在MyCat层调用last_insert_id()也是不会返回结果的。

如果使用这个功能， 则最好配合数据库模式的全局序列。使用 autoIncrement="true" 指定该表使用自增长主键,这样MyCat才不会抛出 "分片键找不到" 的异常。 autoIncrement的默认值为 false。



8). needAddLimit

指定表是否需要自动在每个语句的后面加上limit限制, 默认为true。



### 3.2.2 dataNode 标签

```xml
<dataNode name="dn1" dataHost="host1" database="db1" />
```

dataNode标签中定义了MyCat中的数据节点, 也就是我们通常说的数据分片。一个dataNode标签就是一个独立的数据分片。



具体的属性 ： 

| 属性     | 含义                 | 描述                                                         |
| -------- | -------------------- | ------------------------------------------------------------ |
| name     | 数据节点的名称       | 需要唯一 ; 在table标签中会引用这个名字, 标识表与分片的对应关系 |
| dataHost | 数据库实例主机名称   | 引用自 dataHost 标签中name属性                               |
| database | 定义分片所属的数据库 |                                                              |



### 3.2.3 dataHost 标签

```xml
<dataHost name="host1" maxCon="1000" minCon="10" balance="0"
          writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
    <heartbeat>select user()</heartbeat>
    <writeHost host="hostM1" url="192.168.192.147:3306" user="root" password="itcast"></writeHost>
</dataHost>	
```

该标签在MyCat逻辑库中作为底层标签存在, 直接定义了具体的数据库实例、读写分离、心跳语句。



#### 3.2.3.1 属性

| 属性       | 含义           | 描述                                                         |
| ---------- | -------------- | ------------------------------------------------------------ |
| name       | 数据节点名称   | 唯一标识， 供上层标签使用                                    |
| maxCon     | 最大连接数     | 内部的writeHost、readHost都会使用这个属性                    |
| minCon     | 最小连接数     | 内部的writeHost、readHost初始化连接池的大小                  |
| balance    | 负载均衡类型   | 取值0,1,2,3 ; 后面章节会详细介绍;                            |
| writeType  | 写操作分发方式 | 0 : 写操作都转发到第1台writeHost, writeHost1挂了, 会切换到writeHost2上; <br />1 : 所有的写操作都随机地发送到配置的writeHost上 ; |
| dbType     | 后端数据库类型 | mysql, mongodb , oracle                                      |
| dbDriver   | 数据库驱动     | 指定连接后端数据库的驱动,目前可选值有 native和JDBC。native执行的是二进制的MySQL协议，可以使用MySQL和MariaDB。其他类型数据库需要使用JDBC（需要在MyCat/lib目录下加入驱动jar） |
| switchType | 数据库切换策略 | 取值 -1,1,2,3 ; 后面章节会详细介绍;                          |



#### 3.2.3.2 子标签heartbeat

配置MyCat与后端数据库的心跳，用于检测后端数据库的状态。heartbeat用于配置心跳检查语句。例如 ： MySQL中可以使用 select user(), Oracle中可以使用 select 1 from dual等。



#### 3.2.3.3 子标签writeHost、readHost

指定后端数据库的相关配置， 用于实例化后端连接池。 writeHost指定写实例， readHost指定读实例。

在一个dataHost中可以定义多个writeHost和readHost。但是，如果writeHost指定的后端数据库宕机， 那么这个writeHost绑定的所有readHost也将不可用。



属性：

| 属性名       | 含义               | 取值                                                         |
| ------------ | ------------------ | ------------------------------------------------------------ |
| host         | 实例主机标识       | 对于writeHost一般使用 *M1；对于readHost，一般使用 *S1；      |
| url          | 后端数据库连接地址 | 如果是native，一般为 ip:port ; 如果是JDBC, 一般为jdbc:mysql://ip:port/ |
| user         | 数据库用户名       | root                                                         |
| password     | 数据库密码         | itcast                                                       |
| weight       | 权重               | 在readHost中作为读节点权重                                   |
| usingDecrypt | 密码加密           | 默认 0 否 , 1 是                                             |





## 3.3 rule.xml

rule.xml中定义所有拆分表的规则, 在使用过程中可以灵活的使用分片算法, 或者对同一个分片算法使用不同的参数, 它让分片过程可配置化。

### 3.3.1 tableRule标签

```xml
<tableRule name="auto-sharding-long">
    <rule>
        <columns>id</columns>
        <algorithm>rang-long</algorithm>
    </rule>
</tableRule>
```

A. name : 指定分片算法的名称

B. rule : 定义分片算法的具体内容 

C. columns : 指定对应的表中用于分片的列名

D. algorithm : 对应function中指定的算法名称



### 3.3.2 Function标签

```xml
<function name="rang-long" class="io.MyCat.route.function.AutoPartitionByLong">
	<property name="mapFile">autopartition-long.txt</property>
</function>
```

A. name : 指定算法名称, 该文件中唯一 

B. class : 指定算法的具体类

C. property : 根据算法的要求执行 



## 3.4 sequence 配置文件

在分库分表的情况下 , 原有的自增主键已无法满足在集群中全局唯一的主键 ,因此, MyCat中提供了全局sequence来实现主键 , 并保证全局唯一。那么在MyCat的配置文件 sequence_conf.properties 中就配置的是序列的相关配置。

主要包含以下几种形式：

1). 本地文件方式

2). 数据库方式

3). 本地时间戳方式

4). 其他方式

5). 自增长主键





# 4. MyCat分片

## 4.1 垂直拆分

### 4.1.1 概述

![1573622314361](MyCat/1573622314361.png) 

一种是按照不同的表（或者Schema）来切分到不同的数据库（主机）之上，这种切分可以称之为数据的垂直（纵向）切分。



### 4.1.2 案例场景

![1575725341210](MyCat/1575725341210.png) 

在业务系统中, 有以下表结构 ,但是由于用户与订单每天都会产生大量的数据, 单台服务器的数据存储及处理能力是有限的, 可以对数据库表进行拆分, 原有的数据库表: 

![1575726526012](MyCat/1575726526012.png) 



### 4.1.3 准备工作

1). 准备三台数据库实例

```
192.168.192.157
192.168.192.158
192.168.192.159
```



2). 在三台数据库实例中建库建表

将准备好的三个SQL脚本, 分别导入到三台MySQL实例中 ;

![1575730157557](MyCat/1575730157557.png) 

登录MySQL数据库之后, 使用source命令导入 ;



### 4.1.4 schema.xml的配置

```xml
<?xml version="1.0"?>
<!DOCTYPE MyCat:schema SYSTEM "schema.dtd">
<MyCat:schema xmlns:MyCat="http://io.MyCat/">
	
	<schema name="ITCAST_DB" checkSQLschema="false" sqlMaxLimit="100">
		<table name="tb_areas_city" dataNode="dn1" primaryKey="id" />
		<table name="tb_areas_provinces" dataNode="dn1" primaryKey="id" />
		<table name="tb_areas_region" dataNode="dn1" primaryKey="id" />
		<table name="tb_user" dataNode="dn1" primaryKey="id" />
		<table name="tb_user_address" dataNode="dn1" primaryKey="id" />
		
		<table name="tb_goods_base" dataNode="dn2" primaryKey="id" />
		<table name="tb_goods_desc" dataNode="dn2" primaryKey="goods_id" />
		<table name="tb_goods_item_cat" dataNode="dn2" primaryKey="id" />
		
		<table name="tb_order_item" dataNode="dn3" primaryKey="id" />
		<table name="tb_order_master" dataNode="dn3" primaryKey="order_id" />
		<table name="tb_order_pay_log" dataNode="dn3" primaryKey="out_trade_no" />
	</schema>
	
	
	<dataNode name="dn1" dataHost="host1" database="user_db" />
	<dataNode name="dn2" dataHost="host2" database="goods_db" />
	<dataNode name="dn3" dataHost="host3" database="order_db" />
	
    
	<dataHost name="host1" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.192.157:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
    <dataHost name="host2" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM2" url="192.168.192.158:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
    <dataHost name="host3" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM3" url="192.168.192.159:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
</MyCat:schema>
```



### 4.1.5 server.xml的配置

```xml
<user name="root" defaultAccount="true">
    <property name="password">123456</property>
    <property name="schemas">ITCAST_DB</property>
</user>

<user name="test">
    <property name="password">123456</property>
    <property name="schemas">ITCAST_DB</property>
</user>

<user name="user">
    <property name="password">123456</property>
    <property name="schemas">ITCAST_DB</property>
    <property name="readOnly">true</property>
</user>
```



### 4.1.6 测试

1). 查询数据

```
select * from tb_goods_base;
select * from tb_user;
select * from tb_order_master;
```



2). 插入数据

```sql
insert  into tb_user_address(id,user_id,province_id,city_id,town_id,mobile,address,contact,is_default,notes,create_date,alias) values (null,'java00001',NULL,NULL,NULL,'13900112222','钟楼','张三','0',NULL,NULL,NULL)
```

```sql
insert  into tb_order_item(id,item_id,goods_id,order_id,title,price,num,total_fee,pic_path,seller_id) values (null,19,149187842867954,3,'3G 6','1.00',5,'5.00',NULL,'qiandu')
```



3). 测试跨分片的查询

```sql
SELECT order_id , payment ,receiver, province , city , area FROM tb_order_master o , tb_areas_provinces p , tb_areas_city c , tb_areas_region r
WHERE o.receiver_province = p.provinceid AND o.receiver_city = c.cityid AND o.receiver_region = r.areaid ;
```

当运行上述的SQL语句时, MyCat会报错, 原因是因为当前SQL语句涉及到跨域的join操作 ;



### 4.1.7 全局表配置

1). 将数据节点user_db中的关联的字典表 tb_areas_provinces , tb_areas_city , tb_areas_region中的数据备份 ;

```sql
mysqldump -uroot -pitcast user_db tb_areas_provinces  > provinces;
mysqldump -uroot -pitcast user_db tb_areas_city > city;
mysqldump -uroot -pitcast user_db tb_areas_region > region;
```



2). 将备份的表结构及数据信息, 远程同步到其他两个数据节点的数据库中;

```sql
scp city root@192.168.192.158:/root
scp city root@192.168.192.159:/root

scp provinces root@192.168.192.158:/root
scp provinces root@192.168.192.159:/root

scp region root@192.168.192.158:/root
scp region root@192.168.192.159:/root
```



3). 导入到对应的数据库中

```
mysql -uroot -p goods_db < city
mysql -uroot -p goods_db < provinces 
mysql -uroot -p goods_db < region 
```



4). MyCat逻辑表中的配置

```xml
<table name="tb_areas_city" dataNode="dn1,dn2,dn3" primaryKey="id" type="global"/>
<table name="tb_areas_provinces" dataNode="dn1,dn2,dn3" primaryKey="id"  type="global"/>
<table name="tb_areas_region" dataNode="dn1,dn2,dn3" primaryKey="id"  type="global"/>
```



5). 重启MyCat

```
bin/MyCat restart
```



6). 测试

再次执行相同的连接查询 , 是可以正常查询出对应的数据的 ;

```
SELECT order_id , payment ,receiver, province , city , area FROM tb_order_master o , tb_areas_provinces p , tb_areas_city c , tb_areas_region r
WHERE o.receiver_province = p.provinceid AND o.receiver_city = c.cityid AND o.receiver_region = r.areaid ;
```



当我们对MyCat全局表进行增删改的操作时, 其他节点主机上的后端MySQL数据库中的数据时会同步变化的;

```
update tb_areas_city set city = '石家庄' where id = 5;
```





## 4.2 水平拆分

### 4.2.1 概述

根据表中的数据的逻辑关系，将同一个表中的数据按照某种条件拆分到多台数据库（主机）上面，这种切分称之为数据的水平（横向）切分。

![1577151764698](MyCat/1577151764698.png) 



### 4.2.2 案例场景

![1577152000136](MyCat/1577152000136.png) 

在业务系统中, 有一张表(日志表), 业务系统每天都会产生大量的日志数据 , 单台服务器的数据存储及处理能力是有限的, 可以对数据库表进行拆分, 原有的数据库表拆分成以下表 : 

![1577152168606](MyCat/1577152168606.png) 



### 4.2.3 准备工作

1). 准备三台数据库实例

```
192.168.192.157
192.168.192.158
192.168.192.159
```

2). 在三台数据库实例中创建数据库

```sql
create database log_db DEFAULT CHARACTER SET utf8mb4;
```



### 4.2.4 schema.xml的配置

```xml
<?xml version="1.0"?>
<!DOCTYPE MyCat:schema SYSTEM "schema.dtd">
<MyCat:schema xmlns:MyCat="http://io.MyCat/">
	
	<schema name="LOG_DB" checkSQLschema="false" sqlMaxLimit="100">
		<table name="tb_log" dataNode="dn1,dn2,dn3" primaryKey="id"  rule="mod-long" />
	</schema>
	
	<dataNode name="dn1" dataHost="host1" database="log_db" />
	<dataNode name="dn2" dataHost="host2" database="log_db" />
	<dataNode name="dn3" dataHost="host3" database="log_db" />
	
    
	<dataHost name="host1" maxCon="1000" minCon="10" balance="0" writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.192.157:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
    <dataHost name="host2" maxCon="1000" minCon="10" balance="0" writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM2" url="192.168.192.158:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
    <dataHost name="host3" maxCon="1000" minCon="10" balance="0" writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM3" url="192.168.192.159:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
</MyCat:schema>
```



### 4.2.5 server.xml的配置

```xml
<user name="root" defaultAccount="true">
    <property name="password">123456</property>
    <property name="schemas">LOG_DB</property>
</user>

<user name="test">
    <property name="password">123456</property>
    <property name="schemas">LOG_DB</property>
</user>

<user name="user">
    <property name="password">123456</property>
    <property name="schemas">LOG_DB</property>
    <property name="readOnly">true</property>
</user>
```



### 4.2.6 测试

1). 在MyCat数据库中执行建表语句

```sql
CREATE TABLE `tb_log` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `model_name` varchar(200) DEFAULT NULL COMMENT '模块名',
  `model_value` varchar(200) DEFAULT NULL COMMENT '模块值',
  `return_value` varchar(200) DEFAULT NULL COMMENT '返回值',
  `return_class` varchar(200) DEFAULT NULL COMMENT '返回值类型',
  `operate_user` varchar(20) DEFAULT NULL COMMENT '操作用户',
  `operate_time` varchar(20) DEFAULT NULL COMMENT '操作时间',
  `param_and_value` varchar(500) DEFAULT NULL COMMENT '请求参数名及参数值',
  `operate_class` varchar(200) DEFAULT NULL COMMENT '操作类',
  `operate_method` varchar(200) DEFAULT NULL COMMENT '操作方法',
  `cost_time` bigint(20) DEFAULT NULL COMMENT '执行方法耗时, 单位 ms',
  `source` int(1) DEFAULT NULL COMMENT '来源 : 1 PC , 2 Android , 3 IOS',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

2). 插入数据

```sql
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`, `return_class`, `operate_user`, `operate_time`, `param_and_value`, `operate_class`, `operate_method`, `cost_time`，`source`) VALUES('1','user','insert','success','java.lang.String','10001','2020-02-26 18:12:28','{\"age\":\"20\",\"name\":\"Tom\",\"gender\":\"1\"}','cn.itcast.controller.UserController','insert','10',1);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`, `return_class`, `operate_user`, `operate_time`, `param_and_value`, `operate_class`, `operate_method`, `cost_time`，`source`) VALUES('2','user','insert','success','java.lang.String','10001','2020-02-26 18:12:27','{\"age\":\"20\",\"name\":\"Tom\",\"gender\":\"1\"}','cn.itcast.controller.UserController','insert','23',1);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`, `return_class`, `operate_user`, `operate_time`, `param_and_value`, `operate_class`, `operate_method`, `cost_time`，`source`) VALUES('3','user','update','success','java.lang.String','10001','2020-02-26 18:16:45','{\"age\":\"20\",\"name\":\"Tom\",\"gender\":\"1\"}','cn.itcast.controller.UserController','update','34',1);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`, `return_class`, `operate_user`, `operate_time`, `param_and_value`, `operate_class`, `operate_method`, `cost_time`，`source`) VALUES('4','user','update','success','java.lang.String','10001','2020-02-26 18:16:45','{\"age\":\"20\",\"name\":\"Tom\",\"gender\":\"1\"}','cn.itcast.controller.UserController','update','13',2);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`, `return_class`, `operate_user`, `operate_time`, `param_and_value`, `operate_class`, `operate_method`, `cost_time`，`source`) VALUES('5','user','insert','success','java.lang.String','10001','2020-02-26 18:30:31','{\"age\":\"200\",\"name\":\"TomCat\",\"gender\":\"0\"}','cn.itcast.controller.UserController','insert','29',3);
INSERT INTO `tb_log` (`id`, `model_name`, `model_value`, `return_value`, `return_class`, `operate_user`, `operate_time`, `param_and_value`, `operate_class`, `operate_method`, `cost_time`，`source`) VALUES('6','user','find','success','java.lang.String','10001','2020-02-26 18:30:31','{\"age\":\"200\",\"name\":\"TomCat\",\"gender\":\"0\"}','cn.itcast.controller.UserController','find','29',2);
```



## 4.3 分片规则

MyCat的分片规则配置在conf目录下的rule.xml文件中定义 ; 

环境准备 : 

1). schema.xml中的内容做好备份 , 并配置逻辑库; 

```xml
<schema name="PARTITION_DB" checkSQLschema="false" sqlMaxLimit="100">
	<table name="" dataNode="dn1,dn2,dn3" rule=""/>
</schema>


<dataNode name="dn1" dataHost="host1" database="partition_db" />
<dataNode name="dn2" dataHost="host2" database="partition_db" />
<dataNode name="dn3" dataHost="host3" database="partition_db" />


<dataHost name="host1" maxCon="1000" minCon="10" balance="0"
writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
    <heartbeat>select user()</heartbeat>
    <writeHost host="hostM1" url="192.168.192.157:3306" user="root" password="itcast"></writeHost>
</dataHost>	

<dataHost name="host2" maxCon="1000" minCon="10" balance="0"
writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
    <heartbeat>select user()</heartbeat>
    <writeHost host="hostM2" url="192.168.192.158:3306" user="root" password="itcast"></writeHost>
</dataHost>	

<dataHost name="host3" maxCon="1000" minCon="10" balance="0"
writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
    <heartbeat>select user()</heartbeat>
    <writeHost host="hostM3" url="192.168.192.159:3306" user="root" password="itcast"></writeHost>
</dataHost>	
```

2). 在MySQL的三个节点的数据库中 , 创建数据库partition_db

```
create database partition_db DEFAULT CHARACTER SET utf8mb4;
```



### 4.3.1 取模分片

```xml
<tableRule name="mod-long">
    <rule>
        <columns>id</columns>
        <algorithm>mod-long</algorithm>
    </rule>
</tableRule>

<function name="mod-long" class="io.MyCat.route.function.PartitionByMod">
    <property name="count">3</property>
</function>
```

配置说明 : 

| 属性      | 描述                             |
| --------- | -------------------------------- |
| columns   | 标识将要分片的表字段             |
| algorithm | 指定分片函数与function的对应关系 |
| class     | 指定该分片算法对应的类           |
| count     | 数据节点的数量                   |



### 4.3.2 范围分片

根据指定的字段及其配置的范围与数据节点的对应情况， 来决定该数据属于哪一个分片 ， 配置如下： 

```xml
<tableRule name="auto-sharding-long">
	<rule>
		<columns>id</columns>
		<algorithm>rang-long</algorithm>
	</rule>
</tableRule>

<function name="rang-long" class="io.MyCat.route.function.AutoPartitionByLong">
	<property name="mapFile">autopartition-long.txt</property>
    <property name="defaultNode">0</property>
</function>
```
autopartition-long.txt 配置如下： 

```properties
 range start-end ,data node index
 K=1000,M=10000.
0-500M=0
500M-1000M=1
1000M-1500M=2
```

含义为 ： 0 - 500 万之间的值 ， 存储在0号数据节点 ； 500万 - 1000万之间的数据存储在1号数据节点 ； 1000万 - 1500 万的数据节点存储在2号节点 ；



配置说明: 

| 属性        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| columns     | 标识将要分片的表字段                                         |
| algorithm   | 指定分片函数与function的对应关系                             |
| class       | 指定该分片算法对应的类                                       |
| mapFile     | 对应的外部配置文件                                           |
| type        | 默认值为0 ; 0 表示Integer , 1 表示String                     |
| defaultNode | 默认节点 <br />默认节点的所用:枚举分片时,如果碰到不识别的枚举值, 就让它路由到默认节点 ; 如果没有默认值,碰到不识别的则报错 。 |

**测试: **

配置

```xml
<table name="tb_log" dataNode="dn1,dn2,dn3" rule="auto-sharding-long"/>
```

数据

```sql
1). 创建表
CREATE TABLE `tb_log` (
  id bigint(20) NOT NULL COMMENT 'ID',
  operateuser varchar(200) DEFAULT NULL COMMENT '姓名',
  operation int(2) DEFAULT NULL COMMENT '1: insert, 2: delete, 3: update , 4: select',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


2). 插入数据
insert into tb_log (id,operateuser ,operation) values(1,'Tom',1);
insert into tb_log (id,operateuser ,operation) values(2,'Cat',2);
insert into tb_log (id,operateuser ,operation) values(3,'Rose',3);
insert into tb_log (id,operateuser ,operation) values(4,'Coco',2);
insert into tb_log (id,operateuser ,operation) values(5,'Lily',1);
```





### 4.3.3 枚举分片

通过在配置文件中配置可能的枚举值, 指定数据分布到不同数据节点上, 本规则适用于按照省份或状态拆分数据等业务 , 配置如下: 

```xml
<tableRule name="sharding-by-intfile">
    <rule>
        <columns>status</columns>
        <algorithm>hash-int</algorithm>
    </rule>
</tableRule>

<function name="hash-int" class="io.MyCat.route.function.PartitionByFileMap">
    <property name="mapFile">partition-hash-int.txt</property>
    <property name="type">0</property>
    <property name="defaultNode">0</property>
</function>
```

partition-hash-int.txt ，内容如下 :

```
1=0
2=1
3=2
```

配置说明: 

| 属性        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| columns     | 标识将要分片的表字段                                         |
| algorithm   | 指定分片函数与function的对应关系                             |
| class       | 指定该分片算法对应的类                                       |
| mapFile     | 对应的外部配置文件                                           |
| type        | 默认值为0 ; 0 表示Integer , 1 表示String                     |
| defaultNode | 默认节点 ; 小于0 标识不设置默认节点 , 大于等于0代表设置默认节点 ; <br />默认节点的所用:枚举分片时,如果碰到不识别的枚举值, 就让它路由到默认节点 ; 如果没有默认值,碰到不识别的则报错 。 |

测试: 

配置

```xml
<table name="tb_user" dataNode="dn1,dn2,dn3" rule="sharding-by-enum-status"/>
```

数据

```sql
1). 创建表
CREATE TABLE `tb_user` (
  id bigint(20) NOT NULL COMMENT 'ID',
  username varchar(200) DEFAULT NULL COMMENT '姓名',
  status int(2) DEFAULT '1' COMMENT '1: 未启用, 2: 已启用, 3: 已关闭',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


2). 插入数据
insert into tb_user (id,username ,status) values(1,'Tom',1);
insert into tb_user (id,username ,status) values(2,'Cat',2);
insert into tb_user (id,username ,status) values(3,'Rose',3);
insert into tb_user (id,username ,status) values(4,'Coco',2);
insert into tb_user (id,username ,status) values(5,'Lily',1);
```





### 4.3.4 范围求模算法

该算法为先进行范围分片, 计算出分片组 , 再进行组内求模。

**优点**： 综合了范围分片和求模分片的优点。 分片组内使用求模可以保证组内的数据分布比较均匀， 分片组之间采用范围分片可以兼顾范围分片的特点。

**缺点**： 在数据范围时固定值（非递增值）时，存在不方便扩展的情况，例如将 dataNode Group size 从 2 扩展为 4 时，需要进行数据迁移才能完成 ； 如图所示： 

![image-20200110193319982](MyCat/image-20200110193319982.png) 



配置如下： 

```xml
<tableRule name="auto-sharding-rang-mod">
	<rule>
		<columns>id</columns>
		<algorithm>rang-mod</algorithm>
	</rule>
</tableRule>

<function name="rang-mod" class="io.MyCat.route.function.PartitionByRangeMod">
	<property name="mapFile">autopartition-range-mod.txt</property>
    <property name="defaultNode">0</property>
</function>
```

autopartition-range-mod.txt 配置格式 :

```
#range  start-end , data node group size
0-500M=1
500M1-2000M=2
```

在上述配置文件中, 等号前面的范围代表一个分片组 , 等号后面的数字代表该分片组所拥有的分片数量;



配置说明: 

| 属性        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| columns     | 标识将要分片的表字段名                                       |
| algorithm   | 指定分片函数与function的对应关系                             |
| class       | 指定该分片算法对应的类                                       |
| mapFile     | 对应的外部配置文件                                           |
| defaultNode | 默认节点 ; 未包含以上规则的数据存储在defaultNode节点中, 节点从0开始 |



**测试:**

配置

```xml
<table name="tb_stu" dataNode="dn1,dn2,dn3" rule="auto-sharding-rang-mod"/>
```

数据

```sql
1). 创建表
    CREATE TABLE `tb_stu` (
      id bigint(20) NOT NULL COMMENT 'ID',
      username varchar(200) DEFAULT NULL COMMENT '姓名',
      status int(2) DEFAULT '1' COMMENT '1: 未启用, 2: 已启用, 3: 已关闭',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


2). 插入数据
    insert into tb_stu (id,username ,status) values(1,'Tom',1);
    insert into tb_stu (id,username ,status) values(2,'Cat',2);
    insert into tb_stu (id,username ,status) values(3,'Rose',3);
    insert into tb_stu (id,username ,status) values(4,'Coco',2);
    insert into tb_stu (id,username ,status) values(5,'Lily',1);
    
    insert into tb_stu (id,username ,status) values(5000001,'Roce',1);
    insert into tb_stu (id,username ,status) values(5000002,'Jexi',2);
    insert into tb_stu (id,username ,status) values(5000003,'Mini',1);
```





### 4.3.5 固定分片hash算法

该算法类似于十进制的求模运算，但是为二进制的操作，例如，取 id 的二进制低 10 位 与 1111111111 进行位 & 运算。

最小值：

![image-20200112180630348](MyCat/image-20200112180630348.png) 

最大值：

![image-20200112180643493](MyCat/image-20200112180643493.png) 



**优点**： 这种策略比较灵活，可以均匀分配也可以非均匀分配，各节点的分配比例和容量大小由partitionCount和partitionLength两个参数决定

**缺点**：和取模分片类似。

配置如下 ： 

```xml
<tableRule name="sharding-by-long-hash">
    <rule>
        <columns>id</columns>
        <algorithm>func1</algorithm>
    </rule>
</tableRule>

<function name="func1" class="org.opencloudb.route.function.PartitionByLong">
    <property name="partitionCount">2,1</property>
    <property name="partitionLength">256,512</property>
</function>
```

在示例中配置的分片策略，希望将数据水平分成3份，前两份各占 25%，第三份占 50%。



配置说明: 

| 属性            | 描述                             |
| --------------- | -------------------------------- |
| columns         | 标识将要分片的表字段名           |
| algorithm       | 指定分片函数与function的对应关系 |
| class           | 指定该分片算法对应的类           |
| partitionCount  | 分片个数列表                     |
| partitionLength | 分片范围列表                     |

约束 : 

1). 分片长度 : 默认最大2^10 , 为 1024 ;

2). count, length的数组长度必须是一致的 ;

3). 两组数据的对应情况: (partitionCount[0]partitionLength[0])=(partitionCount[1]partitionLength[1])

以上分为三个分区:0-255,256-511,512-1023



**测试:**

配置

```xml
<table name="tb_brand" dataNode="dn1,dn2,dn3" rule="sharding-by-long-hash"/>
```

数据

```sql
1). 创建表
    CREATE TABLE `tb_brand` (
      id int(11) NOT NULL COMMENT 'ID',
      name varchar(200) DEFAULT NULL COMMENT '名称',
      firstChar char(1)  COMMENT '首字母',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


2). 插入数据
    insert into tb_brand (id,name ,firstChar) values(1,'七匹狼','Q');
    insert into tb_brand (id,name ,firstChar) values(529,'八匹狼','B');
    insert into tb_brand (id,name ,firstChar) values(1203,'九匹狼','J');
    insert into tb_brand (id,name ,firstChar) values(1205,'十匹狼','S');
    insert into tb_brand (id,name ,firstChar) values(1719,'六匹狼','L');
```





### 4.3.6 取模范围算法

该算法先进行取模，然后根据取模值所属范围进行分片。

**优点**：可以自主决定取模后数据的节点分布

**缺点**：dataNode 划分节点是事先建好的，需要扩展时比较麻烦。

 配置如下:

```xml
<tableRule name="sharding-by-pattern">
	<rule>
		<columns>id</columns>
		<algorithm>sharding-by-pattern</algorithm>
	</rule>
</tableRule>

<function name="sharding-by-pattern" class="io.MyCat.route.function.PartitionByPattern">
	<property name="mapFile">partition-pattern.txt</property>
    <property name="defaultNode">0</property>
    <property name="patternValue">96</property>
</function>
```

partition-pattern.txt 配置如下: 

```
0-32=0
33-64=1
65-96=2
```

在mapFile配置文件中, 1-32即代表id%96后的分布情况。如果在1-32, 则在分片0上 ; 如果在33-64, 则在分片1上 ; 如果在65-96, 则在分片2上。



配置说明: 

| 属性         | 描述                                                       |
| ------------ | ---------------------------------------------------------- |
| columns      | 标识将要分片的表字段                                       |
| algorithm    | 指定分片函数与function的对应关系                           |
| class        | 指定该分片算法对应的类                                     |
| mapFile      | 对应的外部配置文件                                         |
| defaultNode  | 默认节点 ; 如果id不是数字, 无法求模, 将分配在defaultNode上 |
| patternValue | 求模基数                                                   |



**测试:**

配置

```xml
<table name="tb_mod_range" dataNode="dn1,dn2,dn3" rule="sharding-by-pattern"/>
```

数据

```sql
1). 创建表
    CREATE TABLE `tb_mod_range` (
      id int(11) NOT NULL COMMENT 'ID',
      name varchar(200) DEFAULT NULL COMMENT '名称',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


2). 插入数据
    insert into tb_mod_range (id,name) values(1,'Test1');
    insert into tb_mod_range (id,name) values(2,'Test2');
    insert into tb_mod_range (id,name) values(3,'Test3');
    insert into tb_mod_range (id,name) values(4,'Test4');
    insert into tb_mod_range (id,name) values(5,'Test5');
```



<font color='red'>注意 : 取模范围算法只能针对于数字类型进行取模运算 ; 如果是字符串则无法进行取模分片 ;</font>





### 4.3.7 字符串hash求模范围算法

与取模范围算法类似, 该算法支持数值、符号、字母取模，首先截取长度为 prefixLength 的子串，在对子串中每一个字符的 ASCII 码求和，然后对求和值进行取模运算（sum%patternValue），就可以计算出子串的分片数。

**优点**：可以自主决定取模后数据的节点分布

**缺点**：dataNode 划分节点是事先建好的，需要扩展时比较麻烦。



配置如下：

```xml
<tableRule name="sharding-by-prefixpattern">
	<rule>
		<columns>id</columns>
		<algorithm>sharding-by-prefixpattern</algorithm>
	</rule>
</tableRule>

<function name="sharding-by-prefixpattern" class="io.MyCat.route.function.PartitionByPrefixPattern">
	<property name="mapFile">partition-prefixpattern.txt</property>
    <property name="prefixLength">5</property>
    <property name="patternValue">96</property>
</function>
```

partition-prefixpattern.txt 配置如下: 

```
 range start-end ,data node index
 ASCII
 48-57=0-9
 64、65-90=@、A-Z
 97-122=a-z
##### first host configuration
0-32=0
33-64=1
65-96=2
```



配置说明: 

| 属性         | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| columns      | 标识将要分片的表字段                                         |
| algorithm    | 指定分片函数与function的对应关系                             |
| class        | 指定该分片算法对应的类                                       |
| mapFile      | 对应的外部配置文件                                           |
| prefixLength | 截取的位数; 将该字段获取前prefixLength位所有ASCII码的和, 进行求模sum%patternValue ,获取的值，在通配范围内的即分片数 ; |
| patternValue | 求模基数                                                     |

如 : 

```
字符串 :
	gf89f9a
	
截取字符串的前5位进行ASCII的累加运算 : 
	g - 103
	f - 102
	8 - 56
	9 - 57
	f - 102
	
    sum求和 : 103 + 102 + + 56 + 57 + 102 = 420
    求模 : 420 % 96 = 36
    
```

附录 ASCII码表 : 

![1577267028771](MyCat/1577267028771.png) 



**测试:**

配置

```xml
<table name="tb_u" dataNode="dn1,dn2,dn3" rule="sharding-by-prefixpattern"/>
```

数据

```sql
1). 创建表
    CREATE TABLE `tb_u` (
      username varchar(50) NOT NULL COMMENT '用户名',
      age int(11) default 0 COMMENT '年龄',
      PRIMARY KEY (`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


2). 插入数据
    insert into tb_u (username,age) values('Test100001',18);
    insert into tb_u (username,age) values('Test200001',20);
    insert into tb_u (username,age) values('Test300001',19);
    insert into tb_u (username,age) values('Test400001',25);
    insert into tb_u (username,age) values('Test500001',22);
```







### 4.3.8 应用指定算法

由运行阶段由应用自主决定路由到那个分片 , 直接根据字符子串（必须是数字）计算分片号 , 配置如下 : 

```xml
<tableRule name="sharding-by-substring">
	<rule>
		<columns>id</columns>
		<algorithm>sharding-by-substring</algorithm>
	</rule>
</tableRule>

<function name="sharding-by-substring" class="io.MyCat.route.function.PartitionDirectBySubString">
	<property name="startIndex">0</property> <!-- zero-based -->
	<property name="size">2</property>
	<property name="partitionCount">3</property>
	<property name="defaultPartition">0</property>
</function>
```

配置说明: 

| 属性             | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| columns          | 标识将要分片的表字段                                         |
| algorithm        | 指定分片函数与function的对应关系                             |
| class            | 指定该分片算法对应的类                                       |
| startIndex       | 字符子串起始索引                                             |
| size             | 字符长度                                                     |
| partitionCount   | 分区(分片)数量                                               |
| defaultPartition | 默认分片(在分片数量定义时, 字符标示的分片编号不在分片数量内时,使用默认分片) |

示例说明 : 

id=05-100000002 , 在此配置中代表根据id中从 startIndex=0，开始，截取siz=2位数字即05，05就是获取的分区，如果没传默认分配到defaultPartition 。



**测试:**

配置

```xml
<table name="tb_app" dataNode="dn1,dn2,dn3" rule="sharding-by-substring"/>
```

数据

```sql
1). 创建表
    CREATE TABLE `tb_app` (
      id varchar(10) NOT NULL COMMENT 'ID',
      name varchar(200) DEFAULT NULL COMMENT '名称',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


2). 插入数据
	insert into tb_app (id,name) values('00-00001','Testx00001');
    insert into tb_app (id,name) values('01-00001','Test100001');
    insert into tb_app (id,name) values('01-00002','Test200001');
    insert into tb_app (id,name) values('02-00001','Test300001');
    insert into tb_app (id,name) values('02-00002','TesT400001');
    
```







### 4.3.9 字符串hash解析算法

截取字符串中的指定位置的子字符串, 进行hash算法， 算出分片 ， 配置如下： 

```xml
<tableRule name="sharding-by-stringhash">
	<rule>
		<columns>user_id</columns>
		<algorithm>sharding-by-stringhash</algorithm>
	</rule>
</tableRule>

<function name="sharding-by-stringhash" class="io.MyCat.route.function.PartitionByString">
	<property name="partitionLength">512</property> <!-- zero-based -->
	<property name="partitionCount">2</property>
	<property name="hashSlice">0:2</property>
</function>
```

配置说明: 

| 属性            | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| columns         | 标识将要分片的表字段                                         |
| algorithm       | 指定分片函数与function的对应关系                             |
| class           | 指定该分片算法对应的类                                       |
| partitionLength | hash求模基数 ; length*count=1024 (出于性能考虑)              |
| partitionCount  | 分区数                                                       |
| hashSlice       | hash运算位 , 根据子字符串的hash运算 ; 0 代表 str.length() , -1 代表 str.length()-1 , 大于0只代表数字自身 ; 可以理解为substring（start，end），start为0则只表示0 |



**测试:** 

配置

```xml
<table name="tb_strhash" dataNode="dn1,dn2,dn3" rule="sharding-by-stringhash"/>
```

数据

```sql
1). 创建表
create table tb_strhash(
	name varchar(20) primary key,
	content varchar(100)
)engine=InnoDB DEFAULT CHARSET=utf8mb4;

2). 插入数据
INSERT INTO tb_strhash (name,content) VALUES('T1001', UUID());
INSERT INTO tb_strhash (name,content) VALUES('ROSE', UUID());
INSERT INTO tb_strhash (name,content) VALUES('JERRY', UUID());
INSERT INTO tb_strhash (name,content) VALUES('CRISTINA', UUID());
INSERT INTO tb_strhash (name,content) VALUES('TOMCAT', UUID());
```



原理: 

![image-20200112234530612](MyCat/image-20200112234530612.png) 





### 4.3.10 一致性hash算法

一致性Hash算法有效的解决了分布式数据的拓容问题 , 配置如下: 

```xml
<tableRule name="sharding-by-murmur">
    <rule>
        <columns>id</columns>
        <algorithm>murmur</algorithm>
    </rule>
</tableRule>

<function name="murmur" class="io.MyCat.route.function.PartitionByMurmurHash">
    <property name="seed">0</property>
    <property name="count">3</property><!--  -->
    <property name="virtualBucketTimes">160</property>
    <!-- <property name="weightMapFile">weightMapFile</property> -->
    <!-- <property name="bucketMapPath">/etc/MyCat/bucketMapPath</property> -->
</function>
```

配置说明: 

| 属性               | 描述                                                         |
| ------------------ | ------------------------------------------------------------ |
| columns            | 标识将要分片的表字段                                         |
| algorithm          | 指定分片函数与function的对应关系                             |
| class              | 指定该分片算法对应的类                                       |
| seed               | 创建murmur_hash对象的种子，默认0                             |
| count              | 要分片的数据库节点数量，必须指定，否则没法分片               |
| virtualBucketTimes | 一个实际的数据库节点被映射为这么多虚拟节点，默认是160倍，也就是虚拟节点数是物理节点数的160倍;virtualBucketTimes*count就是虚拟结点数量 ; |
| weightMapFile      | 节点的权重，没有指定权重的节点默认是1。以properties文件的格式填写，以从0开始到count-1的整数值也就是节点索引为key，以节点权重值为值。所有权重值必须是正整数，否则以1代替 |
| bucketMapPath      | 用于测试时观察各物理节点与虚拟节点的分布情况，如果指定了这个属性，会把虚拟节点的murmur hash值与物理节点的映射按行输出到这个文件，没有默认值，如果不指定，就不会输出任何东西 |



**测试：**

配置

```xml
<table name="tb_order" dataNode="dn1,dn2,dn3" rule="sharding-by-murmur"/>
```

数据

```sql
1). 创建表
create table tb_order(
	id int(11) primary key,
	money int(11),
	content varchar(200)
)engine=InnoDB ;

2). 插入数据
INSERT INTO tb_order (id,money,content) VALUES(1, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(212, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(312, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(412, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(534, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(621, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(754563, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(8123, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(91213, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(23232, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(112321, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(21221, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(112132, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(12132, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(124321, 100 , UUID());
INSERT INTO tb_order (id,money,content) VALUES(212132, 100 , UUID());
```





### 4.3.11 日期分片算法

按照日期来分片

```xml
<tableRule name="sharding-by-date">
    <rule>
        <columns>create_time</columns>
        <algorithm>sharding-by-date</algorithm>
    </rule>
</tableRule>

<function name="sharding-by-date" class="io.MyCat.route.function.PartitionByDate">
	<property name="dateFormat">yyyy-MM-dd</property>
	<property name="sBeginDate">2020-01-01</property>
	<property name="sEndDate">2020-12-31</property>
    <property name="sPartionDay">10</property>
</function>
```

配置说明: 

| 属性        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| columns     | 标识将要分片的表字段                                         |
| algorithm   | 指定分片函数与function的对应关系                             |
| class       | 指定该分片算法对应的类                                       |
| dateFormat  | 日期格式                                                     |
| sBeginDate  | 开始日期                                                     |
| sEndDate    | 结束日期，如果配置了结束日期，则代码数据到达了这个日期的分片后，会重复从开始分片插入 |
| sPartionDay | 分区天数，默认值 10 ，从开始日期算起，每个10天一个分区       |

注意：配置规则的表的 dataNode 的分片，必须和分片规则数量一致，例如 2020-01-01 到 2020-12-31 ，每10天一个分片，一共需要37个分片。





### 4.3.12 单月小时算法

单月内按照小时拆分, 最小粒度是小时 , 一天最多可以有24个分片, 最小1个分片, 下个月从头开始循环, 每个月末需要手动清理数据。

配置如下 ： 

```xml
<tableRule name="sharding-by-hour">
    <rule>
        <columns>create_time</columns>
        <algorithm>sharding-by-hour</algorithm>
    </rule>
</tableRule>

<function name="sharding-by-hour" class="io.MyCat.route.function.LatestMonthPartion">
	<property name="splitOneDay">24</property>
</function>
```

配置说明: 

| 属性        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| columns     | 标识将要分片的表字段 ； 字符串类型（yyyymmddHH）， 需要符合JAVA标准 |
| algorithm   | 指定分片函数与function的对应关系                             |
| splitOneDay | 一天切分的分片数                                             |





### 4.3.13 自然月分片算法

使用场景为按照月份列分区, 每个自然月为一个分片, 配置如下: 

```xml
<tableRule name="sharding-by-month">
    <rule>
        <columns>create_time</columns>
        <algorithm>sharding-by-month</algorithm>
    </rule>
</tableRule>

<function name="sharding-by-month" class="io.MyCat.route.function.PartitionByMonth">
	<property name="dateFormat">yyyy-MM-dd</property>
	<property name="sBeginDate">2020-01-01</property>
	<property name="sEndDate">2020-12-31</property>
</function>
```

配置说明: 

| 属性       | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| columns    | 标识将要分片的表字段                                         |
| algorithm  | 指定分片函数与function的对应关系                             |
| class      | 指定该分片算法对应的类                                       |
| dateFormat | 日期格式                                                     |
| sBeginDate | 开始日期                                                     |
| sEndDate   | 结束日期，如果配置了结束日期，则代码数据到达了这个日期的分片后，会重复从开始分片插入 |



### 4.3.14 日期范围hash算法

其思想和范围取模分片一样，先根据日期进行范围分片求出分片组，再根据时间hash使得短期内数据分布的更均匀 ;

优点 : 可以避免扩容时的数据迁移，又可以一定程度上避免范围分片的热点问题

注意 : 要求日期格式尽量精确些，不然达不到局部均匀的目的

```xml
<tableRule name="range-date-hash">
    <rule>
        <columns>create_time</columns>
        <algorithm>range-date-hash</algorithm>
    </rule>
</tableRule>

<function name="range-date-hash" class="io.MyCat.route.function.PartitionByRangeDateHash">
	<property name="dateFormat">yyyy-MM-dd HH:mm:ss</property>
	<property name="sBeginDate">2020-01-01 00:00:00</property>
	<property name="groupPartionSize">6</property>
    <property name="sPartionDay">10</property>
</function>
```

配置说明: 

| 属性             | 描述                                   |
| ---------------- | -------------------------------------- |
| columns          | 标识将要分片的表字段                   |
| algorithm        | 指定分片函数与function的对应关系       |
| class            | 指定该分片算法对应的类                 |
| dateFormat       | 日期格式 , 符合Java标准                |
| sBeginDate       | 开始日期 , 与 dateFormat指定的格式一致 |
| groupPartionSize | 每组的分片数量                         |
| sPartionDay      | 代表多少天为一组                       |







# 5. MyCat高级

## 5.1 MyCat 性能监控

### 5.1.1 MyCat-web简介

MyCat-web 是 MyCat 可视化运维的管理和监控平台，弥补了 MyCat 在监控上的空白。帮 MyCat 分担统计任务和配置管理任务。MyCat-web 引入了 ZooKeeper 作为配置中心，可以管理多个节点。MyCat-web 主要管理和监控 MyCat 的流量、连接、活动线程和内存等，具备 IP 白名单、邮件告警等模块，还可以统计 SQL 并分析慢 SQL 和高频 SQL 等。为优化 SQL 提供依据。

![1577358192118](MyCat/1577358192118.png) 



### 5.1.2 MyCat-web下载

下载地址 : http://dl.MyCat.io/

![1577369790112](MyCat/1577369790112.png) 



### 5.1.3 MyCat-web安装配置

#### 5.1.3.1 安装

1). 安装Zookeeper

```
A. 上传安装包 
	alt + p -----> put D:\tmp\zookeeper-3.4.11.tar.gz
	
B. 解压
	tar -zxvf zookeeper-3.4.11.tar.gz -C /usr/local/

C. 创建数据存放目录
	mkdir data

D. 修改配置文件名称并配置
	mv zoo_sample.cfg zoo.cfg

E. 配置数据存放目录
	dataDir=/usr/local/zookeeper-3.4.11/data
	
F. 启动Zookeeper
	bin/zkServer.sh start
```



2). 安装MyCat-web

```
A. 上传安装包 
	alt + p --------> put D:\tmp\MyCat-web-1.0-SNAPSHOT-20170102153329-linux.tar.gz
	
B. 解压
	tar -zxvf MyCat-web-1.0-SNAPSHOT-20170102153329-linux.tar.gz -C /usr/local/

C. 目录介绍
    drwxr-xr-x. 2 root root  4096 Oct 19  2015 etc         ----> jetty配置文件
    drwxr-xr-x. 3 root root  4096 Oct 19  2015 lib         ----> 依赖jar包
    drwxr-xr-x. 7 root root  4096 Jan  1  2017 MyCat-web   ----> MyCat-web项目
    -rwxr-xr-x. 1 root root   116 Oct 19  2015 readme.txt
    -rwxr-xr-x. 1 root root 17125 Oct 19  2015 start.jar   ----> 启动jar
    -rwxr-xr-x. 1 root root   381 Oct 19  2015 start.sh    ----> linux启动脚本

D. 启动
	sh start.sh
	
E. 访问
	http://192.168.192.147:8082/MyCat
```

如果Zookeeper与MyCat-web不在同一台服务器上 , 需要设置Zookeeper的地址 ; 在/usr/local/MyCat-web/MyCat-web/WEB-INF/classes/MyCat.properties文件中配置 : 

![1577370960657](MyCat/1577370960657.png) 



#### 5.1.3.2 配置

![1577372353498](MyCat/1577372353498.png) 

![1577372371549](MyCat/1577372371549.png) 



### 5.1.4 MyCat-web之MyCat性能监控

在 MyCat-web 上可以进行 MyCat 性能监控，例如：内存分享、流量分析、连接分析、活动线程分析等等。 如下图: 

A. MyCat内存分析: 

![1577373437531](MyCat/1577373437531.png)  

MyCat的内存分析 , 反映了当前的内存使用情况与历史时间段的峰值、平均值。



B. MyCat流量分析: 

![1577373861622](MyCat/1577373861622.png) 

MyCat流量分析统计了历史时间段的流量峰值、当前值、平均值，是MyCat数据传输的重要指标， In代表输入， Out代表输出。



C. MyCat连接分析

![1577374030291](MyCat/1577374030291.png) 

MyCat连接分析, 反映了MyCat的连接数 



D. MyCat TPS分析

![1577374126073](MyCat/1577374126073.png) 

MyCat TPS 是并发性能的重要参数指标, 指系统在每秒内能够处理的请求数量。 MyCat TPS的值越高 , 代表MyCat单位时间内能够处理的请求就越多, 并发能力也就越高。



E. MyCat活动线程分析反映了MyCat线程的活动情况。

F. MyCat缓存队列分析, 反映了当前在缓存队列中的任务数量。



### 5.1.5 MyCat-web之MySQL性能监控指标

1). MySQL配置

![1577374634946](MyCat/1577374634946.png) 

2). MySQL监控指标

![1577374588708](MyCat/1577374588708.png) 

可以通过MySQL服务监控, 检测每一个MySQL节点的运行状态, 包含缓存命中率 、增删改查比例、流量统计、慢查询比例、线程、临时表等相关性能数据。



### 5.1.6 MyCat-web之SQL监控

1). SQL 统计

![1577374982024](MyCat/1577374982024.png) 



2). SQL表分析

![1577375016852](MyCat/1577375016852.png) 



3). SQL监控

![1577375043787](MyCat/1577375043787.png)  



4). 高频SQL

![1577375072881](MyCat/1577375072881.png) 



5). 慢SQL统计

![1577375100383](MyCat/1577375100383.png) 



6). SQL解析

![1577375162928](MyCat/1577375162928.png) 





## 5.2 MyCat 读写分离

### 5.2.1 MySQL主从复制原理

复制是指将主数据库的DDL 和 DML 操作通过二进制日志传到从库服务器中，然后在从库上对这些日志重新执行（也叫重做），从而使得从库和主库的数据保持同步。

MySQL支持一台主库同时向多台从库进行复制， 从库同时也可以作为其他从服务器的主库，实现链状复制。



MySQL主从复制的原理如下 : 

![image-20200103093716416](MyCat/image-20200103093716416.png) 

从上层来看，复制分成三步：

- Master 主库在事务提交时，会把数据变更作为时间 Events 记录在二进制日志文件 Binlog 中。
- 主库推送二进制日志文件 Binlog 中的日志事件到从库的中继日志 Relay Log 。

- slave重做中继日志中的事件，将改变反映它自己的数据。



MySQL 复制的优点：

- 主库出现问题，可以快速切换到从库提供服务。
- 可以在从库上执行查询操作，从主库中更新，实现读写分离，降低主库的访问压力。
- 可以在从库中执行备份，以避免备份期间影响主库的服务。





### 5.2.2 MySQL一主一从搭建

准备的两台机器: 

| MySQL  | IP              | 端口号 |
| ------ | --------------- | ------ |
| Master | 192.168.192.157 | 3306   |
| Slave  | 192.168.192.158 | 3306   |



#### 5.2.2.1 master

1） 在master 的配置文件（/usr/my.cnf）中，配置如下内容：

```properties
#mysql 服务ID,保证整个集群环境中唯一
server-id=1

#mysql binlog 日志的存储路径和文件名
log-bin=/var/lib/mysql/mysqlbin

#设置logbin格式
binlog_format=STATEMENT

#是否只读,1 代表只读, 0 代表读写
read-only=0

#忽略的数据, 指不需要同步的数据库
#binlog-ignore-db=mysql

#指定同步的数据库
binlog-do-db=db01
```



2） 执行完毕之后，需要重启Mysql：

```sql
service mysql restart ;
```



3） 创建同步数据的账户，并且进行授权操作：

```sql
grant replication slave on *.* to 'itcast'@'192.168.192.158' identified by 'itcast';	

flush privileges;
```



4） 查看master状态：

```sql
show master status;
```



![image-20200103102209631](MyCat/image-20200103102209631.png) 

字段含义:

```
File : 从哪个日志文件开始推送日志文件 
Position ： 从哪个位置开始推送日志
Binlog_Ignore_DB : 指定不需要同步的数据库
```





#### 5.2.2.2 slave

1） 在 slave 端配置文件/usr/my.cnf中，配置如下内容：

```properties
#mysql服务端ID,唯一
server-id=2

#指定binlog日志
log-bin=/var/lib/mysql/mysqlbin

#启用中继日志
relay-log=mysql-relay
```



2）  执行完毕之后，需要重启Mysql：

```
service mysql restart;
```



3） 执行如下指令 ：

```sql
change master to master_host= '192.168.192.157', master_user='itcast', master_password='itcast', master_log_file='mysqlbin.000001', master_log_pos=413;
```

指定当前从库对应的主库的IP地址，用户名，密码，从哪个日志文件开始的那个位置开始同步推送日志。



4） 开启同步操作

```
start slave;

show slave status;
```

 ![image-20200103144903105](MyCat/image-20200103144903105.png)  



5） 停止同步操作

```
stop slave;
```



#### 5.2.2.3 验证主从同步

1） 在主库中创建数据库，创建表，并插入数据 ：

```sql
create database db01;

user db01;

create table user(
	id int(11) not null auto_increment,
	name varchar(50) not null,
	sex varchar(1),
	primary key (id)
)engine=innodb default charset=utf8;

insert into user(id,name,sex) values(null,'Tom','1');
insert into user(id,name,sex) values(null,'Trigger','0');
insert into user(id,name,sex) values(null,'Dawn','1');
```



2） 在从库中查询数据，进行验证 ：

在从库中，可以查看到刚才创建的数据库：

![image-20200103103029311](MyCat/image-20200103103029311.png)  

在该数据库中，查询user表中的数据：

![image-20200103103049675](MyCat/image-20200103103049675.png)  





### 5.2.3 MyCat一主一从读写分离

#### 5.2.3.1 读写分离原理

![image-20200103140249789](MyCat/image-20200103140249789.png) 

读写分离,简单地说是把对数据库的读和写操作分开,以对应不同的数据库服务器。主数据库提供写操作，从数据库提供读操作，这样能有效地减轻单台数据库的压力。

通过MyCat即可轻易实现上述功能，不仅可以支持MySQL，也可以支持Oracle和SQL Server。

MyCat控制后台数据库的读写分离和负载均衡由schema.xml文件datahost标签的balance属性控制。



#### 5.2.3.2 读写分离配置

配置如下： 

1). 检查MySQL的主从复制是否运行正常 .

2). 修改MyCat 的conf/schema.xml 配置如下:

```xml
<?xml version="1.0"?>
<!DOCTYPE MyCat:schema SYSTEM "schema.dtd">
<MyCat:schema xmlns:MyCat="http://io.MyCat/">
	<schema name="ITCAST" checkSQLschema="true" sqlMaxLimit="100">
		<table name="user" dataNode="dn1" primaryKey="id"/>
	</schema>
    
	<dataNode name="dn1" dataHost="localhost1" database="db01" />
    
	<dataHost name="localhost1" maxCon="1000" minCon="10" balance="1" writeType="0" dbType="mysql" 	
				dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.192.157:3306" user="root" password="itcast">
			<readHost host="hostS1" url="192.168.192.158:3306" user="root" password="itcast" />
		</writeHost>
	</dataHost>  
</MyCat:schema>
```



3). 修改conf/server.xml

```xml
<user name="root" defaultAccount="true">
    <property name="password">123456</property>
    <property name="schemas">ITCAST</property>
</user>

<user name="test">
    <property name="password">123456</property>
    <property name="schemas">ITCAST</property>
</user>

<user name="user">
    <property name="password">123456</property>
    <property name="schemas">ITCAST</property>
    <property name="readOnly">true</property>
</user>
```



4). 配置完毕之后, 重启MyCat服务;



属性含义说明: 

```
checkSQLschema
	当该值设置为true时, 如果我们执行语句"select * from test01.user ;" 语句时, MyCat则会把schema字符去掉 , 可以避免后端数据库执行时报错 ;
	
	
balance
	负载均衡类型, 目前取值有4种:
	
	balance="0" : 不开启读写分离机制 , 所有读操作都发送到当前可用的writeHost上.
	
	balance="1" : 全部的readHost 与 stand by writeHost (备用的writeHost) 都参与select 语句的负载均衡,简而言之,就是采用双主双从模式(M1 --> S1 , M2 --> S2, 正常情况下，M2,S1,S2 都参与 select 语句的负载均衡。);
    
    balance="2" : 所有的读写操作都随机在writeHost , readHost上分发
    
    balance="3" : 所有的读请求随机分发到writeHost对应的readHost上执行, writeHost不负担读压力 ;balance=3 只在MyCat1.4 之后生效 .
```



#### 5.2.3.3 验证读写分离

修改balance的值, 查询MyCat中的逻辑表中的数据变化; 





### 5.2.4 MySQL双主双从搭建

#### 5.2.4.1 架构

一个主机 Master1 用于处理所有写请求，它的从机 Slave1 和另一台主机 Master2 还有它的从机 Slave2 负责所有读请求。当 Master1 主机宕机后，Master2 主机负责写请求，Master1 、Master2 互为备机。架构图如下: 

 ![image-20200103170452653](MyCat/image-20200103170452653.png) 





#### 5.2.4.2 双主双从配置

准备的机器如下: 

| 编号 | 角色    | IP地址          | 端口号 |
| ---- | ------- | --------------- | ------ |
| 1    | Master1 | 192.168.192.157 | 3306   |
| 2    | Slave1  | 192.168.192.158 | 3306   |
| 3    | Master2 | 192.168.192.159 | 3306   |
| 4    | Slave2  | 192.168.192.160 | 3306   |



**1). 双主机配置**

Master1配置: 

```properties
#主服务器唯一ID
server-id=1

#启用二进制日志
log-bin=mysql-bin

 设置不要复制的数据库(可设置多个)
 binlog-ignore-db=mysql
 binlog-ignore-db=information_schema

#设置需要复制的数据库
binlog-do-db=db02
binlog-do-db=db03
binlog-do-db=db04

#设置logbin格式
binlog_format=STATEMENT

 在作为从数据库的时候，有写入操作也要更新二进制日志文件
log-slave-updates
```



Master2配置: 

```properties
#主服务器唯一ID
server-id=3

#启用二进制日志
log-bin=mysql-bin

 设置不要复制的数据库(可设置多个)
#binlog-ignore-db=mysql
#binlog-ignore-db=information_schema

#设置需要复制的数据库
binlog-do-db=db02
binlog-do-db=db03
binlog-do-db=db04

#设置logbin格式
binlog_format=STATEMENT

 在作为从数据库的时候，有写入操作也要更新二进制日志文件
log-slave-updates
```



**2). 双从机配置**

Slave1配置: 

```properties
#从服务器唯一ID
server-id=2

#启用中继日志
relay-log=mysql-relay
```



Salve2配置:

```properties
#从服务器唯一ID
server-id=4

#启用中继日志
relay-log=mysql-relay
```



**3). 双主机、双从机重启 mysql 服务**

**4). 主机从机都关闭防火墙**

**5). 在两台主机上建立帐户并授权 slave**

```sql
#在主机MySQL里执行授权命令
GRANT REPLICATION SLAVE ON *.* TO 'itcast'@'%' IDENTIFIED BY 'itcast';

flush privileges;
```

查询Master1的状态 : 

![image-20200104090901765](MyCat/image-20200104090901765.png) 

查询Master2的状态 :

![image-20200104090922386](MyCat/image-20200104090922386.png) 



**6). 在从机上配置需要复制的主机**

Slave1 复制 Master1，Slave2 复制 Master2

slave1 指令: 

```sql
CHANGE MASTER TO MASTER_HOST='192.168.192.157',
MASTER_USER='itcast',
MASTER_PASSWORD='itcast',
MASTER_LOG_FILE='mysql-bin.000001',MASTER_LOG_POS=409;
```



slave2 指令:

```sql
CHANGE MASTER TO MASTER_HOST='192.168.192.159',
MASTER_USER='itcast',
MASTER_PASSWORD='itcast',
MASTER_LOG_FILE='mysql-bin.000001',MASTER_LOG_POS=409;
```



**7). 启动两台从服务器复制功能 , 查看主从复制的运行状态**

```
start slave;

show slave status\G;
```

![image-20200104091917814](MyCat/image-20200104091917814.png) 



![image-20200104091948213](MyCat/image-20200104091948213.png) 



**8). 两个主机互相复制**

Master2 复制 Master1，Master1 复制 Master2

Master1 执行指令: 

```sql
CHANGE MASTER TO MASTER_HOST='192.168.192.159',
MASTER_USER='itcast',
MASTER_PASSWORD='itcast',
MASTER_LOG_FILE='mysql-bin.000001',MASTER_LOG_POS=409;
```



Master2 执行指令:

```sql
CHANGE MASTER TO MASTER_HOST='192.168.192.157',
MASTER_USER='itcast',
MASTER_PASSWORD='itcast',
MASTER_LOG_FILE='mysql-bin.000001',MASTER_LOG_POS=409;
```



**9). 启动两台主服务器复制功能 , 查看主从复制的运行状态**

```
start slave;

show slave status\G;
```

![image-20200104092654432](MyCat/image-20200104092654432.png) 



![image-20200104092741892](MyCat/image-20200104092741892.png) 



**10). 验证**

```sql
create database db03;

use db03;

create table user(
	id int(11) not null auto_increment,
	name varchar(50) not null,
	sex varchar(1),
	primary key (id)
)engine=innodb default charset=utf8;

insert into user(id,name,sex) values(null,'Tom','1');
insert into user(id,name,sex) values(null,'Trigger','0');
insert into user(id,name,sex) values(null,'Dawn','1');


insert into user(id,name,sex) values(null,'Jack Ma','1');
insert into user(id,name,sex) values(null,'Coco','0');
insert into user(id,name,sex) values(null,'Jerry','1');
```



在Master1上创建数据库: 

![image-20200104095232047](MyCat/image-20200104095232047.png) 



在Master1上创建表 :

![image-20200104095521070](MyCat/image-20200104095521070.png) 



**11). 停止从服务复制功能**

```
stop slave;
```



**12). 重新配置主从关系**

```sql
stop slave;
reset master;
```





### 5.2.5 MyCat双主双从读写分离

#### 5.2.5.1 配置

修改\<dataHost>的 balance属性，通过此属性配置读写分离的类型 ; 

```xml
<?xml version="1.0"?>
<!DOCTYPE MyCat:schema SYSTEM "schema.dtd">
<MyCat:schema xmlns:MyCat="http://io.MyCat/">
	
	<schema name="ITCAST" checkSQLschema="true" sqlMaxLimit="100">
		<table name="user" dataNode="dn1" primaryKey="id"/>
	</schema>
	
	<dataNode name="dn1" dataHost="localhost1" database="db03" />

	<dataHost name="localhost1" maxCon="1000" minCon="10" balance="1" writeType="0" dbType="mysql" 	
				dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.192.147:3306" user="root" password="itcast">
			<readHost host="hostS1" url="192.168.192.149:3306" user="root" password="itcast" />
		</writeHost>
		
		<writeHost host="hostM2" url="192.168.192.150:3306" user="root" password="itcast">
			<readHost host="hostS2" url="192.168.192.151:3306" user="root" password="itcast" />
		</writeHost>
	</dataHost>
    
</MyCat:schema>
```

1). balance

1 : 代表 全部的 readHost 与 stand by writeHost 参与 select 语句的负载均衡，简单的说，当双主双从模式(M1->S1，M2->S2，并且 M1 与 M2 互为主备)，正常情况下，M2,S1,S2 都参与 select 语句的负载均衡 ;

2). writeType

0 : 写操作都转发到第1台writeHost, writeHost1挂了, 会切换到writeHost2上;

1 : 所有的写操作都随机地发送到配置的writeHost上 ;

3). switchType

-1 : 不自动切换

1 : 默认值, 自动切换

2 : 表示基于MySQL的主从同步状态决定是否切换, 心跳语句 : show slave status



#### 5.2.5.2 读写分离验证

查询数据 : select * from user;

![image-20200104101106144](MyCat/image-20200104101106144.png) 

插入数据 : insert into user(id,name,sex) values(null,'Dawn','1');

![image-20200104100956216](MyCat/image-20200104100956216.png) 





#### 5.2.5.3 可用性验证

关闭Master1 , 然后再执行写入的SQL语句 , 通过日志查询当前写入操作, 操作的是那台服务器 ;









# 6. MyCat高可用集群搭建

## 6.1 集群架构

### 6.1.1 MyCat实现读写分离架构

在上面的章节, 我们已经讲解过了通过MyCat来实现MySQL的读写分离, 从而完成MySQL集群的负载均衡 , 如下面的结构图: 

![image-20200104144550132](MyCat/image-20200104144550132.png) 

但是以上架构存在问题 , 由于MyCat中间件是单节点的服务, 前端客户端所有的压力过来都直接请求这一台MyCat , 存在单点故障。所以这个时候， 我们就需要考虑MyCat的集群 ；





### 6.1.2 MyCat集群架构

通过MyCat来实现后端MySQL的负载均衡 ， 通过HAProxy再实现MyCat集群的负载均衡 ; 

![image-20200104151016408](MyCat/image-20200104151016408.png) 

HAProxy 负责将请求分发到 MyCat 上，起到负载均衡的作用，同时 HAProxy 也能检测到 MyCat 是否存活，HAProxy 只会将请求转发到存活的 MyCat 上。如果一台 MyCat 服务器宕机，HAPorxy 转发请求时不会转发到宕机的 MyCat 上，所以 MyCat 依然可用。



**HAProxy介绍:**

HAProxy 是一个开源的、高性能的基于TCP(第四层)和HTTP(第七层)应用的负载均衡软件。 使用HAProxy可以快速、可靠地实现基于TCP与HTTP应用的负载均衡解决方案。

具有以下优点： 

①. 可靠性和稳定性好, 可以与硬件级的F5负载均衡服务器媲美 ;

②. 处理能力强, 最高可以通过维护4w-5w个并发连接, 单位时间处理的最大请求数达到2w个 ;

③. 支持多种负载均衡算法 ;

④. 有功能强大的监控界面, 通过此页面可以实时了解系统的运行情况 ;



但是， 上述的架构也是存在问题的， 因为所以的客户端请求都是先到达HAProxy, 由HAProxy再将请求再向下分发, 如果HAProxy宕机的话, 就会造成整个MyCat集群不能正常运行, 依然存在单点故障。





### 6.1.3 MyCat的高可用集群

![image-20200104153537319](MyCat/image-20200104153537319.png) 



**图解说明：**
1). HAProxy 实现了 MyCat 多节点的集群高可用和负载均衡，而 HAProxy 自身的高可用则可以通过Keepalived 来实现。因此，HAProxy 主机上要同时安装 HAProxy 和 Keepalived，Keepalived 负责为该服务器抢占 vip（虚拟 ip），抢占到 vip 后，对该主机的访问可以通过原来的 ip访问，也可以直接通过 vip访问。

2). Keepalived 抢占 vip 有优先级，在 keepalived.conf 配置中的 priority 属性决定。但是一般哪台主机上的Keepalived服务先启动就会抢占到vip，即使是slave，只要先启动也能抢到（要注意避免Keepalived的资源抢占问题）。

3). HAProxy 负责将对 vip 的请求分发到 MyCat 集群节点上，起到负载均衡的作用。同时 HAProxy 也能检测到 MyCat 是否存活，HAProxy 只会将请求转发到存活的 MyCat 上。

4). 如果 Keepalived+HAProxy 高可用集群中的一台服务器宕机，集群中另外一台服务器上的 Keepalived会立刻抢占 vip 并接管服务，此时抢占了 vip 的 HAProxy 节点可以继续提供服务。

5). 如果一台 MyCat 服务器宕机，HAPorxy 转发请求时不会转发到宕机的 MyCat 上，所以 MyCat 依然可用。



综上：MyCat 的高可用及负载均衡由 HAProxy 来实现，而 HAProxy 的高可用，由 Keepalived 来实现。



**keepalived介绍:**

Keepalived是一种基于VRRP协议来实现的高可用方案,可以利用其来避免单点故障。 通常有两台甚至多台服务器运行Keepalived，一台为主服务器(Master), 其他为备份服务器, 但是对外表现为一个虚拟IP(VIP), 主服务器会发送特定的消息给备份服务器, 当备份服务器接收不到这个消息时, 即认为主服务器宕机, 备份服务器就会接管虚拟IP, 继续提供服务, 从而保证了整个集群的高可用。
VRRP(虚拟路由冗余协议-Virtual Router Redundancy Protocol)协议是用于实现路由器冗余的协议，VRRP 协议将两台或多台路由器设备虚拟成一个设备，对外提供虚拟路由器 IP(一个或多个)，而在路由器组内部，如果实际拥有这个对外 IP 的路由器如果工作正常的话就是 MASTER，或者是通过算法选举产生。MASTER 实现针对虚拟路由器 IP 的各种网络功能，如 ARP 请求，ICMP，以及数据的转发等；其他设备不拥有该虚拟 IP，状态是 BACKUP，除了接收 MASTER 的VRRP 状态通告信息外，不执行对外的网络功能。当主机失效时，BACKUP 将接管原先 MASTER 的网络功能。VRRP 协议使用多播数据来传输 VRRP 数据，VRRP 数据使用特殊的虚拟源 MAC 地址发送数据而不是自身网卡的 MAC 地址，VRRP 运行时只有 MASTER 路由器定时发送 VRRP 通告信息，表示 MASTER 工作正常以及虚拟路由器 IP(组)，BACKUP 只接收 VRRP 数据，不发送数据，如果一定时间内没有接收到 MASTER 的通告信息，各 BACKUP 将宣告自己成为 MASTER，发送通告信息，重新进行 MASTER 选举状态。







## 6.2 高可用集群搭建

### 6.2.1 部署环境规划

| 名称                      |       IP        | 端口 | 用户名/密码 |
| :------------------------ | :-------------: | :--: | :---------: |
| MySQL Master              | 192.168.192.157 | 3306 | root/itcast |
| MySQL Slave               | 192.168.192.158 | 3306 | root/itcast |
| MyCat节点1                | 192.168.192.157 | 8066 | root/123456 |
| MyCat节点2                | 192.168.192.158 | 8066 | root/123456 |
| HAProxy节点1/keepalived主 | 192.168.192.159 |      |             |
| HAProxy节点2/keepalived备 | 192.168.192.160 |      |             |

![image-20200104153537319](MyCat/image-20200104153537320.png) 



### 6.2.2 MySQL主从复制搭建

#### 6.2.2.1 master

1） 在master 的配置文件（/usr/my.cnf）中，配置如下内容：

```properties
#mysql 服务ID,保证整个集群环境中唯一
server-id=1
#mysql binlog 日志的存储路径和文件名
log-bin=/var/lib/mysql/mysqlbin
#设置logbin格式
binlog_format=STATEMENT
#是否只读,1 代表只读, 0 代表读写
read-only=0
#指定同步的数据库
binlog-do-db=db01
binlog-do-db=db02
binlog-do-db=db03
```



2） 执行完毕之后，需要重启Mysql：

```sql
service mysql restart ;
```



3） 创建同步数据的账户，并且进行授权操作：

```sql
grant replication slave on *.* to 'itcast'@'%' identified by 'itcast';	

flush privileges;
```



4） 查看master状态：

```sql
show master status;
```

![image-20200104171647471](MyCat/image-20200104171647471.png)  

字段含义:

```
File : 从哪个日志文件开始推送日志文件 
Position ： 从哪个位置开始推送日志
Binlog_Do_DB : 指定需要同步的数据库
```



#### 6.2.2.2 slave

1） 在 slave 端配置文件中，配置如下内容：

```properties
#mysql服务端ID,唯一
server-id=2
#指定binlog日志
log-bin=/var/lib/mysql/mysqlbin
#启用中继日志
relay-log=mysql-relay
```



2）  执行完毕之后，需要重启Mysql：

```
service mysql restart;
```



3） 执行如下指令 ：

```sql
change master to master_host= '192.168.192.157', master_user='itcast', master_password='itcast', master_log_file='mysqlbin.000002', master_log_pos=120;
```

指定当前从库对应的主库的IP地址，用户名，密码，从哪个日志文件开始的那个位置开始同步推送日志。



4） 开启同步操作

```
start slave;
show slave status;
```

![image-20200103144903105](MyCat/image-20200103144903105.png)  



5） 停止同步操作

```
stop slave;
```



#### 6.2.2.3 测试验证

```java
create database db01;

user db01;

create table user(
	id int(11) not null auto_increment,
	name varchar(50) not null,
	sex varchar(1),
	primary key (id)
)engine=innodb default charset=utf8;

insert into user(id,name,sex) values(null,'Tom','1');
insert into user(id,name,sex) values(null,'Trigger','0');
insert into user(id,name,sex) values(null,'Dawn','1');
```



### 6.2.3 MyCat安装配置

#### 6.2.3.1 schema.xml

```xml
<?xml version="1.0"?>
<!DOCTYPE MyCat:schema SYSTEM "schema.dtd">
<MyCat:schema xmlns:MyCat="http://io.MyCat/">
	<schema name="ITCAST" checkSQLschema="true" sqlMaxLimit="100">
		<table name="user" dataNode="dn1" primaryKey="id"/>
	</schema>
	<dataNode name="dn1" dataHost="localhost1" database="db01" />
	<dataHost name="localhost1" maxCon="1000" minCon="10" balance="1" writeType="0" dbType="mysql" 	
				dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.192.157:3306" user="root" password="itcast">
			<readHost host="hostS1" url="192.168.192.158:3306" user="root" password="itcast" />
		</writeHost>
	</dataHost>  
</MyCat:schema>
```



#### 6.2.3.2 server.xml

```xml
<user name="root" defaultAccount="true">
    <property name="password">123456</property>
    <property name="schemas">ITCAST</property>
</user>

<user name="test">
    <property name="password">123456</property>
    <property name="schemas">ITCAST</property>
</user>
```



两台MyCat服务, 做相同的配置 ;



### 6.2.4 HAProxy安装配置

#### 6.2.4.1 安装

1). 准备好HAProxy安装包，传到/root目录下

```
haproxy-1.5.16.tar.gz
```



2). 解压到/usr/local/src目录下

```
tar -zxvf haproxy-1.5.16.tar.gz -C /usr/local/src
```



3). 进入解压后的目录，查看内核版本，进行编译

```
cd /usr/local/src/haproxy-1.5.16
uname -r
make TARGET=linux2632 PREFIX=/usr/local/haproxy ARCH=x86_64

 TARGET=linux310，内核版本，使用uname -r查看内核，如：2.6.32-431.el6.x86_64，此时该参数就为linux2632；
 ARCH=x86_64，系统位数；
 PREFIX=/usr/local/haprpxy #/usr/local/haprpxy，为haprpxy安装路径。
```



4). 编译完成后，进行安装

```
make install PREFIX=/usr/local/haproxy
```



5). 安装完成后，创建目录

```
mkdir -p /usr/data/haproxy/
```



6). 创建HAProxy配置文件

vim /usr/local/haproxy/haproxy.conf

```yml
global
	log 127.0.0.1 local0 
	maxconn 4096 
	chroot /usr/local/haproxy 
	pidfile /usr/data/haproxy/haproxy.pid
	uid 99
	gid 99
	daemon
	node mysql-haproxy-01
	description mysql-haproxy-01
defaults
	log global
	mode tcp
	option abortonclose
	option redispatch
	retries 3
	maxconn 2000
	timeout connect 50000ms
	timeout client 50000ms
	timeout server 50000ms
listen proxy_status
	bind 0.0.0.0:48066
		mode tcp
		balance roundrobin
		server MyCat_1 192.168.192.157:8066 check
		server MyCat_2 192.168.192.158:8066 check
frontend admin_stats
	bind 0.0.0.0:8888
		mode http
		stats enable
		option httplog
		maxconn 10
		stats refresh 30s
		stats uri /admin
		stats auth admin:123123
		stats hide-version
		stats admin if TRUE
```



**内容解析如下** : 

```yml
#global 配置中的参数为进程级别的参数，通常与其运行的操作系统有关
global
	#定义全局的syslog服务器, 最多可定义2个; local0 是日志设备, 对应于/etc/rsyslog.conf中的配置 , 默认收集info级别日志
	log 127.0.0.1 local0 
	#log 127.0.0.1 local1 notice
	#log loghost local0 info
	#设定每个haproxy进程所接受的最大并发连接数 ;
	maxconn 4096 
	#修改HAproxy工作目录至指定的目录并在放弃权限之前执行chroot操作, 可以提升haproxy的安全级别
	chroot /usr/local/haproxy 
	#进程ID保存文件
	pidfile /usr/data/haproxy/haproxy.pid
	#指定用户ID
	uid 99
	#指定组ID
	gid 99
	#设置HAproxy以守护进程方式运行
	daemon
	#debug
	#quiet
	node mysql-haproxy-01  # 定义当前节点的名称，用于 HA 场景中多 haproxy 进程共享同一个 IP 地址时
	description mysql-haproxy-01 # 当前实例的描述信息
	
#defaults：用于为所有其他配置段提供默认参数，这默认配置参数可由下一个"defaults"所重新设定
defaults
	#继承global中的log定义
	log global
	#所使用的处理模式(tcp:四层 , http:七层, health:状态检查,只返回OK)
	## tcp: 实例运行于纯 tcp 模式，在客户端和服务器端之间将建立一个全双工的连接，且不会对 7 层报文做任何类型的检查，此为默认模式
	## http:实例运行于 http 模式，客户端请求在转发至后端服务器之前将被深度分析，所有不与 RFC 模式兼容的请求都会被拒绝
	## health：实例运行于 health 模式，其对入站请求仅响应“OK”信息并关闭连接，且不会记录任何日志信息 ，此模式将用于相应外部组件的监控状态检测请求
	mode tcp
	#当服务器负载很高的时候，自动结束掉当前队列处理时间比较长的连接
	option abortonclose
		
	#当使用了cookie时，haproxy将会将请求的后端服务器的serverID插入到cookie中，以保证会话的session持久性，而此时，后端服务器宕机，但是客户端的cookie不会刷新，设置此参数，将会将客户请求强制定向到另外一个后端server上，以保证服务的正常。
	option redispatch
	retries 3
	 前端的最大并发连接数（默认为 2000）
	maxconn 2000
	 连接超时(默认是毫秒,单位可以设置 us,ms,s,m,h,d)
	timeout connect 5000
	 客户端超时时间
	timeout client 50000
	 服务器超时时间
	timeout server 50000

#listen: 用于定义通过关联“前端”和“后端”一个完整的代理，通常只对 TCP 流量有用
listen proxy_status
	bind 0.0.0.0:48066  绑定端口
		mode tcp
		balance roundrobin  定义负载均衡算法，可用于"defaults"、"listen"和"backend"中,默认为轮询
		#格式: server <name> <address> [:[port]] [param*]
		 weight : 权重，默认为 1，最大值为 256，0 表示不参与负载均衡
         backup : 设定为备用服务器，仅在负载均衡场景中的其他 server 均不可以启用此 server
         check  : 启动对此 server 执行监控状态检查，其可以借助于额外的其他参数完成更精细的设定
         inter  : 设定监控状态检查的时间间隔，单位为毫秒，默认为 2000，也可以使用 fastinter 和 downinter 来根据服务器端专题优化此事件延迟
         rise   : 设置 server 从离线状态转换至正常状态需要检查的次数（不设置的情况下，默认值为 2）
         fall   : 设置 server 从正常状态转换至离线状态需要检查的次数（不设置的情况下，默认值为 3）
         cookie : 为指定 server 设定 cookie 值，此处指定的值将会在请求入站时被检查，第一次为此值挑选的 server 将会被后续的请求所选中，其目的在于实现持久连接的功能
         maxconn: 指定此服务器接受的最大并发连接数，如果发往此服务器的连接数目高于此处指定的值，其将被放置于请求队列，以等待其他连接被释放
		server MyCat_1 192.168.192.157:8066 check inter 10s
		server MyCat_2 192.168.192.158:8066 check inter 10s

 用来匹配接收客户所请求的域名，uri等，并针对不同的匹配，做不同的请求处理
 HAProxy 的状态信息统计页面
frontend admin_stats
	bind 0.0.0.0:8888
		mode http
		stats enable
		option httplog
		maxconn 10
		stats refresh 30s
		stats uri /admin
		stats auth admin:123123
		stats hide-version
		stats admin if TRUE
```



HAProxy的负载均衡策略: 

| 策略               | 含义                                                         |
| ------------------ | ------------------------------------------------------------ |
| roundrobin         | 表示简单的轮循，即客户端每访问一次，请求轮循跳转到后端不同的节点机器上 |
| static-rr          | 基于权重轮循，根据权重轮循调度到后端不同节点                 |
| leastconn          | 加权最少连接，表示最少连接者优先处理                         |
| source             | 表示根据请求源IP，这个跟Nginx的IP_hash机制类似，使用其作为解决session问题的一种方法 |
| uri                | 表示根据请求的URL，调度到后端不同的服务器                    |
| url_param          | 表示根据请求的URL参数来进行调度                              |
| hdr（name）        | 表示根据HTTP请求头来锁定每一次HTTP请求                       |
| rdp-cookie（name） | 表示根据cookie（name）来锁定并哈希每一次TCP请求              |





#### 6.2.4.2 启动访问

1). 启动HAProxy

```
/usr/local/haproxy/sbin/haproxy -f /usr/local/haproxy/haproxy.conf
```



2). 查看HAProxy进程

```
ps -ef|grep haproxy
```



3). 访问

http://192.168.192.162:8888/admin



界面: 

![image-20200202214408231](MyCat/image-20200202214408231.png)  





### 6.2.5 Keepalived安装配置

![image-20200203010953404](MyCat/image-20200203010953404.png) 



#### 6.2.5.1 安装配置

1). 上传安装包到Linux

```
alt + p --------> put D:/tmp/keepalived-1.4.5.tar.gz
```



2). 解压安装包到目录 /usr/local/src

```
tar -zxvf keepalived-1.4.5.tar.gz -C /usr/local/src
```



3). 安装依赖插件

```
yum install -y gcc openssl-devel popt-devel
```



4). 进入解压后的目录，进行配置，进行编译

```
 cd /usr/local/src/keepalived-1.4.5
 
 ./configure --prefix=/usr/local/keepalived
```



5). 进行编译，完成后进行安装

```
make && make install
```



6). 运行前配置

```
cp /usr/local/src/keepalived-1.4.5/keepalived/etc/init.d/keepalived /etc/init.d/
mkdir /etc/keepalived
cp /usr/local/keepalived/etc/keepalived/keepalived.conf /etc/keepalived/
cp /usr/local/src/keepalived-1.4.5/keepalived/etc/sysconfig/keepalived /etc/sysconfig/
cp /usr/local/keepalived/sbin/keepalived /usr/sbin/
```



7). 修改配置文件 /etc/keepalived/keepalived.conf

Master: 

```
global_defs {
	notification_email {
		javadct@163.com
	}
	notification_email_from keepalived@showjoy.com
	smtp_server 127.0.0.1
	smtp_connect_timeout 30
	router_id haproxy01
	vrrp_skip_check_adv_addr
	vrrp_garp_interval 0
	vrrp_gna_interval 0
}

vrrp_script chk_haproxy {
	script "/etc/keepalived/haproxy_check.sh"
	interval 2
	weight 2
}

vrrp_instance VI_1 {
	#主机配MASTER，备机配BACKUP
	state MASTER
	#所在机器网卡
	interface eth1
	virtual_router_id 51
	#数值越大优先级越高
	priority 120
	advert_int 1
	authentication {
		auth_type PASS
		auth_pass 1111
	}
	# 将 track_script 块加入 instance 配置块
    track_script {
    	chk_haproxy # 检查 HAProxy 服务是否存活
    }
	virtual_ipaddress {
		#虚拟IP
		192.168.192.200
	}
}
```



BackUP:

```conf
global_defs {
	notification_email {
		javadct@163.com
	}
	notification_email_from keepalived@showjoy.com
	smtp_server 127.0.0.1
	smtp_connect_timeout 30
	#标识本节点
	router_id haproxy02
	vrrp_skip_check_adv_addr
	vrrp_garp_interval 0
	vrrp_gna_interval 0
}

 keepalived 会定时执行脚本并对脚本执行的结果进行分析，动态调整 vrrp_instance 的优先级
vrrp_script chk_haproxy {
	 检测 haproxy 状态的脚本路径
	script "/etc/keepalived/haproxy_check.sh"
	#检测时间间隔
	interval 2
	#如果条件成立，权重+2
	weight 2
}

vrrp_instance VI_1 {
	#主机配MASTER，备机配BACKUP
	state BACKUP
	#所在机器网卡
	interface eth1
	virtual_router_id 51
	#数值越大优先级越高
	priority 100
	advert_int 1
	authentication {
		auth_type PASS
		auth_pass 1111
	}
	# 将 track_script 块加入 instance 配置块
    track_script {
    	chk_haproxy # 检查 HAProxy 服务是否存活
    }
	virtual_ipaddress {
		#虚拟IP
		192.168.192.200
	}
}
```



8). 编写检测haproxy的shell脚本 haproxy_check.sh

```shell
#!/bin/bash

A=`ps -C haproxy --no-header | wc -l`

if [ $A -eq 0 ];then

  /usr/local/haproxy/sbin/haproxy -f /usr/local/haproxy/haproxy.conf

  echo "haproxy restart ..." &> /dev/null

  sleep 1

  if [ `ps -C haproxy --no-header | wc -l` -eq 0 ];then

    /etc/init.d/keepalived stop

    echo "stop keepalived" &> /dev/null

  fi

fi
```





#### 6.2.5.2 启动测试

1). 启动Keepalived

```
service keepalived start
```

2). 登录验证

```
mysql -uroot -p123456 -h 192.168.192.200 -P 48066
```



![image-20200202193227448](MyCat/image-20200202193227448.png) 





# 7. MyCat架构剖析

## 7.1 MyCat总体架构介绍

### 7.1.1 源码下载及导入

![image-20200202220149279](MyCat/image-20200202220149279.png) 

导入Idea

![image-20200202220220682](MyCat/image-20200202220220682.png) 



### 7.1.2 总体架构

MyCat在逻辑上由几个模块组成: 通信协议、路由解析、结果集处理、数据库连接、监控等模块。如图所示： 

![image-20200107230122662](MyCat/image-20200107230122662.png) 

1). 通信协议模块： 通信协议模块承担底层的收发数据、线程回调处理工作， MyCat通信协议默认采用Reactor模式，在协议层采用MySQL协议；

2). 路由解析模块: 负责对传入的SQL语句进行语法解析, 解析语句的条件、类型、关键字等，并进行优化；

3). SQL执行模块: 负责从连接池中获取连接, 再根据路由解析的结果, 把SQL语句分发到相应的节点执行;

4). 数据库连接模块: 负责创建、管理、维护后端的连接池。为减少每次建立数据库连接的开销，数据库使用连接池机制对连接声明周期进行管理；

5). 结果集处理模块: 负责对跨分片的查询结果进行汇聚、排序、截取等；

6). 监控管理模块: 负责MyCat的连接、内存等资源进行监控和管理。监控主要通过管理指令及监控服务展现一些监控数据； 管理则主要通过轮询事件来检测和释放不适用的资源；





### 7.1.3 总体执行流程

![image-20200107233001248](MyCat/image-20200107233001248.png) 





## 7.2 MyCat网络I/O架构及实现

### 7.2.1 BIO、NIO与AIO

1). BIO

BIO(同步阻塞I/O) 通常由一个单独的Acceptor线程负责监听客户端的连接, 接收到客户端的连接请求后, 会为每个客户端创建一个新的线程进行处理, 处理完成之后, 再给客户端返回结果, 销毁线程 。

每个客户端请求接入时， 都需要开启一个线程进行处理， 一个线程只能处理一个客户端连接。 当客户端变多时，会创建大量的处理线程， 每个线程都需要分配栈空间和CPU， 并且频繁的线程上下文切换也会造成性能的浪费。所以该模式， 无法满足高性能、高并发接入的需求。



2). NIO

NIO(同步非阻塞I/O)基于Reactor模式作为底层通信模型，Reactor模式可以将事件驱动的应用进行事件分派, 将客户端发送过来的服务请求分派给合适的处理类(handler)。当Socket有流可读或可写入Socket时, 操作系统会通知相应的应用程序进行处理, 应用程序再将流读取到缓冲区或写入操作系统。 这时已经不是一个连接对应一个处理线程了， 而是一个有效的请求对应一个线程， 当没有数据时， 就没有工作线程来处理。

NIO 的最大优点体现在线程轮询访问Selector, 当read或write到达时则处理, 未到达时则继续轮询。



3). AIO

AIO，全程 Asynchronous IO(异步非阻塞的IO), 是一种非阻塞异步的通信模式。在NIO的基础上引入了新的异步通道的概念，并提供了异步文件通道和异步套接字通道的实现。AIO中客户端的I/O请求都是由OS先完成了再通知服务器应用去启动线程进行处理。

AIO与NIO的主要区别在于回调与轮询, 客户端不需要关注服务处理事件是否完成, 也不需要轮询, 只需要关注自己的回调函数。



### 7.2.2 通信架构

在MyCat中实现了NIO与AIO两种I/O模式, 可以通过配置文件server.xml进行指定 : 

```xml
<property name="usingAIO">1</property>
```

usingAIO为1代表使用AIO模型 , 为0表示使用NIO模型;



**MyCat的AIO架构**

![image-20200108103954458](MyCat/image-20200108103954458.png) 

1). MyCatStartUp是整个MyCat服务启动的入口;

2). 在获取到MyCat的home目录后, 把主要的任务交给MyCatServer , 并调用其startup方法;

3). 初始化系统配置, 获取配置文件中的usingAIO的配置, 如果配置为1, 说明使用AIO模型 , 进入到AIO的分支, 并创建两个连接, 一个是管理后台连接(9066), 一个server的连接(8066);

4). 进入AIO分支 , 主要有AIOAcceptor接收客户端请求, 绑定端口, 创建服务端的异步Socket ;在accept方法中完成两件事: ①. FrontedConnection的创建, 这是前段连接的关键; ②. register注册事件, MySQL协议握手包就在此时发送;

![image-20200108111012502](MyCat/image-20200108111012502.png) 



**MyCat的NIO架构**

如果设置的usingAIO为0 ,那么将走NIOAcceptor通道 , 流程如下: 

![image-20200108111153230](MyCat/image-20200108111153230.png) 

1). 如果走NIO分支 , 将首先创建NIOAcceptor对象, 并调用其start方法;

2). NIOAcceptor 负责处理Accept事件, 服务端接收客户端的连接事件, 就是MyCat作为服务端去处理前端业务程序发过来的连接请求, 建立链接后, 调用NIOAcceptor的 NIOReactor.postRegister方法进行注册（并没有注解注册， 而是放入缓冲队列， 避免加锁的竞争）。 

NIOAcceptor的accept方法 ： 

![image-20200108112521438](MyCat/image-20200108112521438.png) 

NIOReactor的postRegister方法： 

![image-20200108112959564](MyCat/image-20200108112959564.png) 





## 7.3 MyCat实现MySQL协议

### 7.3.1 MySQL协议简介

#### 7.3.1.1 概述

MySQL协议处于应用层之下、TCP/IP之上, 在MySQL客户端和服务端之间使用。包含了链接器、MySQL代理、主从复制服务器之间通信，并支持SSL加密、传输数据的压缩、连接和身份验证及数据交互等。其中，握手认证阶段和命令执行阶段是MySQL协议中的两个重要阶段。



#### 7.3.1.2 握手认证阶段

![image-20200109113445831](MyCat/image-20200109113445831.png) 

A. 握手认证阶段是客户端连接服务器的必经之路, 客户端与服务端完成TCP的三次握手以后, 服务端会向客户端发送一个初始化握手包, 握手包中包含了协议版本、MySQLServer版本、线程ID、服务器的权能标识和字符集等信息。

B. 客户端在接收到服务端的初始化握手包之后， 会发送身份验证包给服务端（AuthPacket）, 该包中包含用户名、密码等信息。

C. 服务端接收到客户端的登录验证包之后，需要进行逻辑校验，校验该登录信息是否正确。如果信息都符合，则返回一个OKPacket，表示登录成功,否则返回ERR_Packet，表示拒绝。

Wireshark抓包如下:

![image-20200127165109223](MyCat/image-20200127165109223.png) 



报文分析如下： 

1). 初始化握手包

![image-20200109133647751](MyCat/image-20200109133647751.png) 

通过抓包工具Wireshark抓取到的握手包信息如下, 握手包格式:

![image-20200127162616334](MyCat/image-20200127162616334.png)  

说明: 

Packet Length : 包的长度;

Packet Number : 包的序号;

Server Greeting : 消息体, 包含了协议版本、MySQLServer版本、线程ID和字符集等信息。



2). 登录认证包

客户端在接收到服务端发来的初始握手包之后， 向服务端发出认证请求， 该请求包含以下信息（由Wireshark抓获） ： 

![image-20200127163702804](MyCat/image-20200127163702804.png) 



3). OK包或ERROR包

服务端接收到客户端的登录认证包之后，如果通过认证，则返回一个OKPacket，如果未通过认证，则返回一个ERROR包。

OK报文如下： 

![image-20200127163957990](MyCat/image-20200127163957990.png) 

ERROR报文如下 :

![image-20200127165156952](MyCat/image-20200127165156952.png) 



#### 7.3.1.3 命令执行阶段

在握手认证阶段通过并完成以后, 客户端可以向服务端发送各种命令来请求数据, 此阶段的流程是: 命令请求->返回结果集。

Wireshark 捕获的数据包如下： 

![image-20200127170112968](MyCat/image-20200127170112968.png) 

1). 命令包

![image-20200127170235143](MyCat/image-20200127170235143.png) 

2). 结果集包

![image-20200127170823882](MyCat/image-20200127170823882.png) 



### 7.3.2 MySQL协议在MyCat中实现

#### 7.3.2.1 握手认证实现

在MyCat中同时实现了NIO和AIO, 通过配置可以选择NIO和AIO。MyCat Server在启动阶段已经选择好采用NIO还是AIO，因此建立I/O通道后,MyCat服务端一直等待客户端端的连接,当有连接到来的时候,MyCat首先发送握手包。 



1). 握手包源码实现

MyCat中的源码中io.MyCat.net.FrontendConnection类的实现如下:

![image-20200127183259378](MyCat/image-20200127183259378.png) 

握手包信息组装完毕后, 通过FrontedConnection写回客户端。



2). 认证包源码实现

客户端接收到握手包后, 紧接着向服务端发起一个认证包, MyCat封装为类 AuthPacket:

![image-20200127231628215](MyCat/image-20200127231628215.png) 

客户端发送的认证包转由 FrontendAuthenticator 的Handler来处理, 主要操作就是 拆包, 检查用户名、密码合法性， 检查连接数是够超出限制。源码实现如下： 

![image-20200127232022594](MyCat/image-20200127232022594.png) 

认证失败， 调用failure方法， 认证成功调用success方法。

failure方法源码： 

![image-20200127232344040](MyCat/image-20200127232344040.png) 

success方法源码： 

![image-20200127232422887](MyCat/image-20200127232422887.png) 



#### 7.3.2.2 命令执行实现

命令执行阶段就是SQL命令和SQL语句执行阶段， 在该阶段MyCat主要需要做的事情， 就是对客户端发来的数据包进行拆包， 并判断命令的类型， 并解析SQL语句， 执行响应的SQL语句， 最后把执行结果封装在结果集包中， 返回给客户端。

从客户端发来的命令交给 FrontendCommandHandler 中的handle方法处理:

![image-20200127235140959](MyCat/image-20200127235140959.png) 

处理具体的请求, 返回客户端结果集数据包: 

![image-20200128000050787](MyCat/image-20200128000050787.png) 





## 7.4 MyCat线程架构与实现

### 7.4.1 MyCat线程池实现

在MyCat中大量用到了线程池， 通过线程池来避免频繁的创建和销毁线程而造成的系统性能的浪费。在MyCat中使用的线程池是JDK中提供的线程池 ThreadPoolExecutor 的子类 NameableExecutor ， 构造方法如下： 

![image-20200108114506434](MyCat/image-20200108114506434.png) 

父类构造为： 

![image-20200108114611505](MyCat/image-20200108114611505.png) 

构造参数含义: 

corePoolSize : 核心池大小

maximumPoolSize : 最大线程数

keepAliveTime: 线程没有任务执行时, 最多能够存活多久

timeUnit: 时间单位

workQueue: 阻塞任务队列

threadFactory: 线程工厂, 用来创建线程



### 7.4.2 MyCat线程架构

![image-20200108114952672](MyCat/image-20200108114952672.png) 

在MyCat中主要有两大线程池: timerExecutor 和 businessExecutor。

1). timerExecutor 线程池主要完成系统时间定时更新、处理器定时检查、数据节点定时连接空闲超时检查、数据节点定时心跳检测等任务。

2). businessExecutor是MyCat最重要的线程资源池, 该资源池的线程使用的范围非常广, 涵盖以下方面: 

A. 后端用原生协议连接数据

B. JDBC执行SQL语句

C. SQL拦截

D. 数据合并服务

E. 批量SQL作业

F. 查询结果的异步分发

G. 基于guava实现异步回调

![image-20200108141645417](MyCat/image-20200108141645417.png) 



## 7.5 MyCat内存管理及缓存框架与实现

这里所提到的内存管理指的是MyCat缓冲区管理, 众所周知设置缓冲区的唯一目的是提高系统的性能, 缓冲区通常是部分常用的数据存放在缓冲池中以便系统直接访问, 避免使用磁盘IO访问磁盘数据, 从而提高性能。

### 7.5.1 内存管理

1). 缓冲池组成

缓冲池的最小单位为chunk, 默认的chunk大小为4096字节(DEFAULT_BUFFER_CHUNK_SIZE), BufferPool的总大小为4096 x processors x 1000(其中processors为处理器数量)。对I/O进程而言, 他们共享一个缓冲池。缓冲池有两种类型： 本地缓存线程（以$_开头的线程）缓冲区和其他缓冲区， 分配buffer时, 优先获取ThreadLocalPool中的buffer, 没有命中时会获取BufferPool中的buffer。



2). 分配MyCat缓冲池

分配缓冲池时, 可以指定大小, 也可以用默认值。

A. allocate(): 先检测是否为本地线程， 当执行线程为本地缓存线程时， localBufferPool取出一个可用的buffer。如果不是， 则从ConcurrentLinkedQueue队列中取出一个buffer进行分配, 如果队列没有可用的buffer, 则创建一个直接缓冲区。

B. allocate(size): 如果用户指定的size不大于chunkSize, 则调用allocate()进行分配; 反之则调用createTempBuffer(size)创建临时非直接缓冲区。



3). MyCat缓冲池的回收

回收时先判断buffer是否有效, 有如下情况时缓冲池不回收。

A. 不是直接缓冲区

B. buffer是空的

C. buffer的容量大于chunkSize



### 7.5.2 MyCat缓存架构

1). 缓存框架选择

MyCat支持ehcache、mapdb、leveldb缓存, 可通过配置文件cacheserver.properties来进行配置;

![image-20200108154627518](MyCat/image-20200108154627518.png) 



2). 缓存内容

MyCat有路由缓存、表主键到datanode缓存、ER关系缓存。

A. 路由缓存: 即SQLRouteCache, 根据SQL语句查找路由信息的缓存, 该缓存只是针对select语句, 如果执行了之前已经执行过的某个SQL语句(缓存命中), 那么路由信息就不需要重复计算了, 直接从缓存中获取。

B. 表主键到datanode缓存: 当分片字段与主键字段不一致时, 直接通过主键值查询时无法定位具体分片的(只能全分片下发), 所以设置该缓存之后, 就可以利用主键值查找到分片名, 缓存的key是ID值, value是节点名。

C. ER关系缓存: 在ER分片时使用, 而且在insert查询中才会使用缓存, 当字表插入数据时, 根据父子关联字段确定子表分片, 下次可以直接从缓存中获取所在的分片。 

查看缓存指令： show @@cache；

![image-20200108155642414](MyCat/image-20200108155642414.png) 



## 7.6 MyCat连接池架构与实现

这里我们所讨论的连接池是MyCat的后端连接池， 也就是MyCat后端与各个数据库节点之间的连接架构。

1). 连接池创建

MyCat按照每个dataHost创建一个连接池, 根据schema.xml文件的配置取得最小的连接数minCon,  并初始化minCon个连接。在初始化连接时， 还需要判定用户选择的是JDBC还是原生的MySQL协议， 以便于创建对应的连接。

2). 连接池分配

分配连接就是从连接池队列中取出一个连接， 在取出一个连接时， MyCat需要根据负载均衡（balance属性）的类型选择不同的数据源， 因为连接和数据源绑在一起，所以需要知道MyCat读写的是那些数据源， 才能分配响应的连接。 

3). 架构

![image-20200108162456464](MyCat/image-20200108162456464.png) 



## 7.7 MyCat主从切换架构与实现

### 7.7.1 MyCat主从切换概述

MyCat实现MySQL读写分离的目的在于降低单节点数据库的访问压力,  原理就是让主数据库执行增删改操作, 从数据库执行查询操作, 利用MySQL数据库的复制机制将Master的数据同步到slave上。

当master宕机后，slave承载的业务如何切换到master继续提供服务，以及slave宕机后如何将master切换到slave上。手动切换数据源很简单， 但不是运维工作的首选，本节重点就是讲解如何实现自动切换。

MyCat的读写分离依赖于MySQL的主从同步, 也就是说MyCat没有实现数据的主从同步功能, 但是实现了自动切换功能。

**1). 自动切换**

自动切换是MyCat主从复制的默认配置 , 当主机或从机宕机后, MyCat自动切换到可用的服务器上。 假设写服务器为M， 读服务器为S， 则： 

正常时， 写M读S；

当M宕机后， 读写S ； 恢复M后， 写S， 读M ；

当S宕机后， 读写M ； 恢复S后， 写M， 读S ；

 

**2). 基于MySQL主从同步状态的切换**

这种切换方式与自动切换不同， MyCat检测到主从数据同步延迟时， 会自动切换到拥有最新数据的MySQL服务器上， 防止读到很久以前的数据。

原理就是通过检查MySQL的主从同步状态（show slave status）中的Seconds_Behind_Master、Slave_IO_Running、Slave_SQL_Running三个字段,来确定当前主从同步的状态以及主从之间的数据延迟。 Seconds_Behind_Master为0表示没有延迟，数值越大，则说明延迟越高。



### 7.7.2 MyCat主从切换实现

基于延迟的切换， 则判断结果集中的Slave_IO_Running、Slave_SQL_Running两个个字段是否都为yes，以及Seconds_Behind_Master 是否小于配置文件中配置的 slaveThreshold的值, 如果有其中任何一个条件不满足, 则切换。

主要流程如下:

![image-20200128005840029](MyCat/image-20200128005840029.png) 





## 7.8 MyCat核心技术

### 5.2.1 MyCat分布式事务实现

MyCat在1.6版本以后已经支持XA分布式事务类型了。具体的使用流程如下：

1). 在应用层需要设置事务不能自动提交

```
set autocommit=0;
```

2). 在SQL中设置XA为开启状态

```
set xa = on;
```

3). 执行SQL

```
insert into user(id,name,sex) values(1,'Tom','1'),(2,'Rose','2'),(3,'Leo','1'),(4,'Lee','1');
```

4). 对事务进行提交或回滚

```
commit/rollback
```



完整流程如下: 

![image-20200129223657058](MyCat/image-20200129223657058.png) 





### 5.2.2 MyCat SQL路由实现

MyCat的路由是和SQL解析组件息息相关的, SQL路由模块是MyCat数据库中间件最重要的模块之一, 使用MyCat主要是为了分库分表, 而分库分表的核心就是路由。

#### 5.2.2.1 路由的作用

![image-20200113225535847](MyCat/image-20200113225535847.png) 

如图所示， MyCat接收到应用系统发来的查询语句， 要将其发送到后端连接的MySQL数据库去执行， 但是后端有三个数据库服务器，具体要查询那一台数据库服务器呢， 这就是路由需要实现的功能。

SQL的路由既要保证数据的完整 ， 也不能造成资源的浪费， 还要保证路由的效率。



#### 5.2.2.2 SQL解析器

MyCat1.3版本之前模式使用的是Fdbparser的foundationdb的开源SQL解析器，在2015年被apple收购后，从开源变为闭源了。

目前版本的MyCat采用的是Druid的SQL解析器， 性能比采用Fdbparser整体性能提高20%以上。





### 5.2.3 MyCat跨库Join

#### 5.2.3.1 全局表

每个企业级的系统中, 都会存在一些系统的基础信息表, 类似于字典表、省份、城市、区域、语言表等， 这些表与业务表之间存在关系， 但不是业务主从关系，而是一种属性关系。

当我们对业务表进行分片处理时， 可以将这些基础信息表设置为全局表， 也就是在每个节点中都存在该表。

全局表的特性如下： 

A. 全局表的insert、update、delete操作会实时地在所有节点同步执行, 保持各个节点数据的一致性

B. 全局表的查询操作会从任意节点执行,因为所有节点的数据都一致

C. 全局表可以和任意表进行join操作

![image-20200128013501684](MyCat/image-20200128013501684.png) 



#### 5.2.3.2 ER表

关系型数据库是基于实体关系模型(Entity Relationship Model)的, MyCat中的ER表便来源于此。 MyCat提出了基于ER关系的数据分片策略 , 子表的记录与其所关联的父表的记录存放在同一个数据分片中, 通过表分组(Table Group)保证数据关联查询不会跨库操作。

![image-20200129101108379](MyCat/image-20200129101108379.png) 

#### 5.2.3.3 catlet

catlet是MyCat为了解决跨分片Join提出的一种创新思路, 也叫做人工智能(HBT)。MyCat参考了数据库中存储过程的实现方式，提出类似的跨库解决方案，用户可以根据系统提供的API接口实现跨分片Join。

采用这种方案开发时,必须要实现Catlet接口的两个方法 :

![image-20200129104415975](MyCat/image-20200129104415975.png) 

route 方法: 路由的方法, 传递系统配置和schema配置等 ;

processSQL方法: EngineCtx执行SQL并给客户端返回结果集 ;

当我们自定义Catlet完成之后, 需要将Catlet的实现类进行编译,并将其字节码文件 XXXCatlet.class存放在MyCat_home/catlet目录下, 系统会加载相关Class, 而且每隔1分钟扫描一次文件是否更新, 若更新则自动重新加载,因此无需重启服务。



**ShareJoin**

ShareJoin 是Catlet的实现， 是一个简单的跨分片Join， 目前支持两个表的Join，原理就是解析SQL语句， 拆分成单表的语句执行， 单后把各个节点的数据进行汇集。



要想使用Catlet完成join， 还需要借助于MyCat中的注解， 在执行SQL语句时，使用catlet注解:

```sql
/*!MyCat:catlet=demo.catlets.ShareJoin */ select a.id as aid , a.id , b.id as bid , b.name as name from customer a, company b where a.company_id=b.id and a.id = 1;
```



### 5.2.4 MyCat数据汇聚与排序

通过MyCat实现数据汇聚和排序,不仅可以减少各分片与客户端之间的数据传输IO, 也可以帮助开发者总复杂的数据处理中解放出来,从而专注于开发业务代码。



在MySQL中存在两种排序方式： 一种利用有序索引获取有序数据， 另一种通过相应的排序算法将获取到的数据在内存中进行排序。 而MyCat中数据排序采用堆排序法对多个分片返回有序数据，并在合并、排序后再返回给客户端。

![image-20200129113055429](MyCat/image-20200129113055429.png) 





# 8. MyCat综合案例

## 8.1 案例概述

### 8.1.1 案例介绍

本案例将模拟电商项目中的商品管理、订单管理、基础信息管理、日志管理模块，对整个系统中的数据表进行分片操作，将根据不同的业务需求，采用不同的分片方式 。



### 8.1.2 系统架构

![image-20200201153127417](MyCat/image-20200201153127417.png) 

本案例涉及到的模块： 

1). 商品微服务

2). 订单微服务

3). 日志微服务





### 8.1.3 技术选型

- SpringBoot
- SpringCloud
- SpringMVC
- Mybatis
- SpringDataRedis
- MySQL
- Redis

- Lombok









## 8.2 案例需求

1). 商品管理

A. 添加商品

B. 查询商品

![image-20200201194027874](MyCat/image-20200201194027874.png) 



2). 订单管理

A. 下订单

B. 查询订单

![image-20200201194121792](MyCat/image-20200201194121792.png) 



3). 日志管理

A. 日志记录

B. 日志查询

![image-20200201194159102](MyCat/image-20200201194159102.png) 

















## 8.3 案例环境搭建

### 8.3.1 数据库

1). 省份表 tb_provinces

| Field      | Type        | Comment  |
| ---------- | ----------- | -------- |
| provinceid | varchar(20) | 省份ID   |
| province   | varchar(50) | 省份名称 |



2). 市表 tb_cities

| Field      | Type        | Comment  |
| ---------- | ----------- | -------- |
| cityid     | varchar(20) | 城市ID   |
| city       | varchar(50) | 城市名称 |
| provinceid | varchar(20) | 省份ID   |



3). 区县表 tb_areas

| Field  | Type        | Comment  |
| ------ | ----------- | -------- |
| areaid | varchar(20) | 区域ID   |
| area   | varchar(50) | 区域名称 |
| cityid | varchar(20) | 城市ID   |



4). 商品分类表 tb_category

| Field     | Type        | Comment  |
| --------- | ----------- | -------- |
| id        | int(20)     | 分类ID   |
| name      | varchar(50) | 分类名称 |
| goods_num | int(11)     | 商品数量 |
| is_show   | char(1)     | 是否显示 |
| is_menu   | char(1)     | 是否导航 |
| seq       | int(11)     | 排序     |
| parent_id | int(20)     | 上级ID   |



5). 品牌表 tb_brand

| Field  | Type          | Comment      |
| ------ | ------------- | ------------ |
| id     | int(11)       | 品牌id       |
| name   | varchar(100)  | 品牌名称     |
| image  | varchar(1000) | 品牌图片地址 |
| letter | char(1)       | 品牌的首字母 |
| seq    | int(11)       | 排序         |



6). 商品SPU表 tb_spu

| Field          | Type          | Comment      |
| -------------- | ------------- | ------------ |
| id             | varchar(20)   | 主键         |
| sn             | varchar(60)   | 货号         |
| name           | varchar(100)  | SPU名        |
| caption        | varchar(100)  | 副标题       |
| brand_id       | int(11)       | 品牌ID       |
| category1_id   | int(20)       | 一级分类     |
| category2_id   | int(10)       | 二级分类     |
| category3_id   | int(10)       | 三级分类     |
| template_id    | int(20)       | 模板ID       |
| freight_id     | int(11)       | 运费模板id   |
| image          | varchar(200)  | 图片         |
| images         | varchar(2000) | 图片列表     |
| sale_service   | varchar(50)   | 售后服务     |
| introduction   | text          | 介绍         |
| spec_items     | varchar(3000) | 规格列表     |
| para_items     | varchar(3000) | 参数列表     |
| sale_num       | int(11)       | 销量         |
| comment_num    | int(11)       | 评论数       |
| is_marketable  | char(1)       | 是否上架     |
| is_enable_spec | char(1)       | 是否启用规格 |
| is_delete      | char(1)       | 是否删除     |
| status         | char(1)       | 审核状态     |



7). 商品SKU表 tb_sku

| Field         | Type          | Comment                         |
| ------------- | ------------- | ------------------------------- |
| id            | varchar(20)   | 商品id                          |
| sn            | varchar(100)  | 商品条码                        |
| name          | varchar(200)  | SKU名称                         |
| price         | int(20)       | 价格（分）                      |
| num           | int(10)       | 库存数量                        |
| alert_num     | int(11)       | 库存预警数量                    |
| image         | varchar(200)  | 商品图片                        |
| images        | varchar(2000) | 商品图片列表                    |
| weight        | int(11)       | 重量（克）                      |
| create_time   | datetime      | 创建时间                        |
| update_time   | datetime      | 更新时间                        |
| spu_id        | varchar(20)   | SPUID                           |
| category_id   | int(10)       | 类目ID                          |
| category_name | varchar(200)  | 类目名称                        |
| brand_name    | varchar(100)  | 品牌名称                        |
| spec          | varchar(200)  | 规格                            |
| sale_num      | int(11)       | 销量                            |
| comment_num   | int(11)       | 评论数                          |
| status        | char(1)       | 商品状态 1-正常，2-下架，3-删除 |
| version       | int(255)      |                                 |



8). 订单表 tb_order

| Field             | Type          | Comment                                                      |
| ----------------- | ------------- | ------------------------------------------------------------ |
| id                | varchar(200)  | 订单id                                                       |
| total_num         | int(11)       | 数量合计                                                     |
| total_money       | int(11)       | 金额合计                                                     |
| pre_money         | int(11)       | 优惠金额                                                     |
| post_fee          | int(11)       | 邮费                                                         |
| pay_money         | int(11)       | 实付金额                                                     |
| pay_type          | varchar(1)    | 支付类型，1、在线支付、0 货到付款                            |
| create_time       | datetime      | 订单创建时间                                                 |
| update_time       | datetime      | 订单更新时间                                                 |
| pay_time          | datetime      | 付款时间                                                     |
| consign_time      | datetime      | 发货时间                                                     |
| end_time          | datetime      | 交易完成时间                                                 |
| close_time        | datetime      | 交易关闭时间                                                 |
| shipping_name     | varchar(20)   | 物流名称                                                     |
| shipping_code     | varchar(20)   | 物流单号                                                     |
| username          | varchar(50)   | 用户名称                                                     |
| buyer_message     | varchar(1000) | 买家留言                                                     |
| buyer_rate        | char(1)       | 是否评价                                                     |
| receiver_contact  | varchar(50)   | 收货人                                                       |
| receiver_mobile   | varchar(12)   | 收货人手机                                                   |
| receiver_province | varchar(200)  | 收货人省份                                                   |
| receiver_city     | varchar(200)  | 收货人市                                                     |
| receiver_area     | varchar(200)  | 收货人区/县                                                  |
| receiver_address  | varchar(200)  | 收货人具体街道地址                                           |
| source_type       | char(1)       | 订单来源：1:web，2：app，3：微信公众号，4：微信小程序 5 H5手机页面 |
| transaction_id    | varchar(30)   | 交易流水号                                                   |
| order_status      | char(1)       | 订单状态                                                     |
| pay_status        | char(1)       | 支付状态 0:未支付 1:已支付                                   |
| consign_status    | char(1)       | 发货状态 0:未发货 1:已发货 2:已送达                          |
| is_delete         | char(1)       | 是否删除                                                     |



9). 订单明细表 tb_order_item

| Field        | Type         | Comment  |
| ------------ | ------------ | -------- |
| id           | varchar(200) | ID       |
| category_id1 | int(11)      | 1级分类  |
| category_id2 | int(11)      | 2级分类  |
| category_id3 | int(11)      | 3级分类  |
| spu_id       | varchar(200) | SPU_ID   |
| sku_id       | varchar(200) | SKU_ID   |
| order_id     | varchar(200) | 订单ID   |
| name         | varchar(200) | 商品名称 |
| price        | int(20)      | 单价     |
| num          | int(10)      | 数量     |
| money        | int(20)      | 总金额   |
| pay_money    | int(11)      | 实付金额 |
| image        | varchar(200) | 图片地址 |
| weight       | int(11)      | 重量     |
| post_fee     | int(11)      | 运费     |
| is_return    | char(1)      | 是否退货 |



10). 订单日志表 tb_order_log 

| Field          | Type         | Comment  |
| -------------- | ------------ | -------- |
| id             | varchar(20)  | ID       |
| operater       | varchar(50)  | 操作员   |
| operate_time   | datetime     | 操作时间 |
| order_id       | bigint(20)   | 订单ID   |
| order_status   | char(1)      | 订单状态 |
| pay_status     | char(1)      | 付款状态 |
| consign_status | char(1)      | 发货状态 |
| remarks        | varchar(100) | 备注     |



11). 操作日志表 tb_operatelog

| Field           | Type         | Comment               |
| --------------- | ------------ | --------------------- |
| id              | bigint(20)   | ID                    |
| model_name      | varchar(200) | 模块名                |
| model_value     | varchar(200) | 模块值                |
| return_value    | varchar(200) | 返回值                |
| return_class    | varchar(200) | 返回值类型            |
| operate_user    | varchar(20)  | 操作用户              |
| operate_time    | varchar(20)  | 操作时间              |
| param_and_value | varchar(500) | 请求参数名及参数值    |
| operate_class   | varchar(200) | 操作类                |
| operate_method  | varchar(200) | 操作方法              |
| cost_time       | bigint(20)   | 执行方法耗时, 单位 ms |



12). 字典表 tb_dictionary

| Field       | Type         | Comment       |
| ----------- | ------------ | ------------- |
| id          | int(11)      | 主键ID , 自增 |
| codeid      | int(11)      | 码表ID        |
| codetype    | varchar(2)   | 码值类型      |
| codename    | varchar(50)  | 名称          |
| codevalue   | varchar(50)  | 码值          |
| description | varchar(100) | 描述          |
| createtime  | datetime     | 创建时间      |
| updatetime  | datetime     | 修改时间      |
| createuser  | int(11)      | 创建人        |
| updateuser  | int(11)      | 修改人        |



### 8.3.2 工程预览

![image-20200201201704741](MyCat/image-20200201201704741.png) 

```
spring-boot-starter-parent
    |- v_parent	--------------------> 父工程, 统一管理依赖版本
        |- v_common ----------------> 通用工程, 存放通用的工具类及组件
        |- v_model -----------------> 实体类
        |- v_eureka ----------------> 注册中心
        |- v_feign_api -------------> feign远程调用的客户端接口
        |- v_gateway ---------------> 网关工程
        |- v_manage_web ------------> 模拟前端工程
        |- v_service_goods ---------> 商品微服务
        |- v_service_log -----------> 日志微服务
        |- v_service_order ---------> 订单微服务
```



### 8.3.3 工程层级关系

![image-20200205010155489](MyCat/image-20200205010155489.png) 



### 8.3.4 父工程搭建

工程名: v_parent

pom.xml

```xml
<!-- springBoot项目需要集成自父工程 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.4.RELEASE</version>
</parent>

<properties>
    <skipTests>true</skipTests>
</properties>

<!--依赖包-->
<dependencies>
    <!--测试包-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>Greenwich.SR1</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <!--MySQL数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.47</version>
        </dependency>

        <!--mybatis分页插件-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.2.3</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.51</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.6</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```



### 8.3.5 基础工程搭建

1). v_model

该基础工程中存放的是与数据库对应的实体类 ;

A. pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```



B. 导入实体类



2). v_common

该基础工程中存放的是通用的组件及工具类 , 比如 分页实体类, 结果实体类, 状态码 等

直接导入资料中提供的基础组件和工具类 ;



3). v_feign_api

该工程中, 主要存放的是Feign远程调用的客户端接口;

pom.xml

```xml
<dependencies>
    <!--web起步依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Feign起步依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>

    <dependency>
        <groupId>cn.itcast</groupId>
        <artifactId>v_common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <dependency>
        <groupId>cn.itcast</groupId>
        <artifactId>v_model</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

</dependencies>
```



### 8.3.6 Eureka Server搭建

1). pom.xml

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
</dependencies>
```



2). 引导类

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
    }
}
```



3). application.yml

```yml
spring:
  application:
    name: eureka
server:
  port: 8161
eureka:
  client:
    register-with-eureka: false #是否将自己注册到eureka中
    fetch-registry: false #是否从eureka中获取信息
    service-url:
      defaultZone: http://127.0.0.1:${server.port}/eureka/
  server:
    enable-self-preservation: true
```



### 8.3.7 GateWay 网关搭建

1). pom.xml

```xml
<!--网关依赖-->
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```



2). 引导类

```java
@SpringBootApplication
@EnableEurekaClient
public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class,args);
    }
}
```



3). application.yml

```yml
server:
  port: 8001
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:8161/eureka
  instance:
    prefer-ip-address: true
spring:
  application:
    name: gateway
  cloud:
    gateway:
      routes:
        - id: v_goods_route
          uri: lb://goods
          predicates:
            - Path=/goods/**
          filters:
            - StripPrefix=1

        - id: v_order_route
          uri: lb://order
          predicates:
            - Path=/order/**
          filters:
            - StripPrefix=1
```



4). Cors配置类

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsWebFilter(source);
    }

    private CorsConfiguration buildConfig(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
		//在生产环境上最好指定域名，以免产生跨域安全问题
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }
}
```





## 8.4 功能开发

### 8.4.1 商品管理模块

需求 : 

1). 根据ID查询商品SPU信息;

2). 根据条件查询商品SPU列表;

3). 根据ID查询商品SKU信息;

![image-20200216225916691](MyCat/image-20200216225916691.png) 



概念: 

1). SPU = Standard Product Unit  （标准产品单位）

概念 : SPU 是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。
通俗点讲，属性值、特性相同的货品就可以称为一个 SPU

例如：华为P30 就是一个 SPU



2). SKU=stock keeping unit( 库存量单位)

SKU  即库存进出计量的单位， 可以是以件、盒、托盘等为单位。
SKU  是物理上不可分割的最小存货单元。在使用时要根据不同业态，不同管理模式来处理。在服装、鞋类商品中使用最多最普遍。

例如：红色 64G 全网通 的华为P30 就是一个 SKU



#### 8.4.1.1 创建工程

pom.xml

```xml
 <dependencies>
        <!-- Eureka客户端依赖 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <!--MySQL数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!--mybatis分页插件-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
        </dependency>

        <!--web起步依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- redis 使用-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>

        <!-- fastJson依赖 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
        </dependency>

        <!-- Feign依赖 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

        <dependency>
            <groupId>cn.itcast</groupId>
            <artifactId>v_common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>cn.itcast</groupId>
            <artifactId>v_model</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>cn.itcast</groupId>
            <artifactId>v_feign_api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

    </dependencies>
```

application.yml

```yml
server:
  port: 9001
spring:
  application:
    name: goods
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/v_shop?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
    username: root
    password: 2143
  main:
    allow-bean-definition-overriding: true #当遇到同样名字的时候，是否允许覆盖注册
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://127.0.0.1:8161/eureka
  instance:
    prefer-ip-address: true
```



引导类

```java
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "cn.itcast.feign")
@MapperScan("cn.itcast.goods.mapper")
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class,args);
    }
}
```



#### 8.4.1.2 Mapper

1). mapper接口定义

```java
public interface SpuMapper {

    public TbSpu findById(String spuId);

    public List<TbSpu> search(Map<String,Object> searchMap);

}
```

```java
public interface SkuMapper  {
    //根据ID查询SKU
    public TbSku findById(String skuId);

}
```



2). mapper映射配置文件

SpuMapper.xml

```xml
<mapper namespace="cn.itcast.goods.mapper.SpuMapper" >

    <resultMap id="spuResultMap" type="cn.itcast.model.TbSpu">
        <id column="id" jdbcType="VARCHAR" property="id" />
        <result column="sn" jdbcType="VARCHAR" property="sn" />
        <result column="name" jdbcType="VARCHAR" property="name" />
        <result column="caption" jdbcType="VARCHAR" property="caption" />
        <result column="brand_id" jdbcType="INTEGER" property="brandId" />
        <result column="category1_id" jdbcType="INTEGER" property="category1Id" />
        <result column="category2_id" jdbcType="INTEGER" property="category2Id" />
        <result column="category3_id" jdbcType="INTEGER" property="category3Id" />
        <result column="template_id" jdbcType="INTEGER" property="templateId" />
        <result column="freight_id" jdbcType="INTEGER" property="freightId" />
        <result column="image" jdbcType="VARCHAR" property="image" />
        <result column="images" jdbcType="VARCHAR" property="images" />
        <result column="sale_service" jdbcType="VARCHAR" property="saleService" />
        <result column="spec_items" jdbcType="VARCHAR" property="specItems" />
        <result column="para_items" jdbcType="VARCHAR" property="paraItems" />
        <result column="sale_num" jdbcType="INTEGER" property="saleNum" />
        <result column="comment_num" jdbcType="INTEGER" property="commentNum" />
        <result column="is_marketable" jdbcType="CHAR" property="isMarketable" />
        <result column="is_enable_spec" jdbcType="CHAR" property="isEnableSpec" />
        <result column="is_delete" jdbcType="CHAR" property="isDelete" />
        <result column="status" jdbcType="CHAR" property="status" />
    </resultMap>

    <select id="findById" parameterType="java.lang.String" resultMap="spuResultMap">
        select
            *
        from
            tb_spu
        where
            id = #{spuId}
    </select>

    <select id="search" resultMap="spuResultMap">
        select * from tb_spu
        <where>
            <if test="name != null and name != ''">
                and name like '%${name}%'
            </if>
            <if test="caption != null and caption != ''" >
                and caption like '%${caption}%'
            </if>
            <if test="brandId != null">
                and brand_id = #{brandId}
            </if>
            <if test="status != null and status != ''">
                and status = #{status}
            </if>
        </where>
    </select>

</mapper>
```



SkuMapper.xml

```xml
<mapper namespace="cn.itcast.goods.mapper.SkuMapper" >

    <resultMap id="skuResultMap" type="cn.itcast.model.TbSku">
        <id column="id" jdbcType="VARCHAR" property="id" />
        <result column="sn" jdbcType="VARCHAR" property="sn" />
        <result column="name" jdbcType="VARCHAR" property="name" />
        <result column="price" jdbcType="INTEGER" property="price" />
        <result column="num" jdbcType="INTEGER" property="num" />
        <result column="alert_num" jdbcType="INTEGER" property="alertNum" />
        <result column="image" jdbcType="VARCHAR" property="image" />
        <result column="images" jdbcType="VARCHAR" property="images" />
        <result column="weight" jdbcType="INTEGER" property="weight" />
        <result column="create_time" jdbcType="TIMESTAMP" property="createTime" />
        <result column="update_time" jdbcType="TIMESTAMP" property="updateTime" />
        <result column="spu_id" jdbcType="VARCHAR" property="spuId" />
        <result column="category_id" jdbcType="INTEGER" property="categoryId" />
        <result column="category_name" jdbcType="VARCHAR" property="categoryName" />
        <result column="brand_name" jdbcType="VARCHAR" property="brandName" />
        <result column="spec" jdbcType="VARCHAR" property="spec" />
        <result column="sale_num" jdbcType="INTEGER" property="saleNum" />
        <result column="comment_num" jdbcType="INTEGER" property="commentNum" />
        <result column="status" jdbcType="CHAR" property="status" />
        <result column="version" jdbcType="INTEGER" property="version" />
    </resultMap>

    <select id="findById" parameterType="java.lang.String" resultMap="skuResultMap">
        select * from tb_sku where id = #{skuId}
    </select>
</mapper>
```



#### 8.4.1.3 Service

1). 接口定义 

```java
public interface SkuService {
    /**
     * 根据ID查询SKU
     * @param skuId
     * @return
     */
    public TbSku findById(String skuId);
}
```

```java
public interface SpuService {
    /**
     * 根据ID查询
     * @param id
     * @return
     */
    TbSpu findById(String id);
    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<TbSpu> findPage(Map<String, Object> searchMap, int page, int size);
}
```



2).接口实现

```java
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Override
    public TbSku findById(String skuId) {
        return skuMapper.findById(skuId);
    }
    
}
```

```java
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public TbSpu findById(String id){
        return  spuMapper.findById(id);
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<TbSpu> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        return (Page<TbSpu>) spuMapper.search(searchMap);
    }

}
```



#### 8.4.1.4 Controller

```java
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/sku")
public class SkuController {
    @Autowired
    private SkuService skuService;
    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<TbSku> findById(@PathVariable("id") String id){
        TbSku sku = skuService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",sku);
    }
}
```

```java
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/spu")
public class SpuController {

    @Autowired
    private SpuService spuService;

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<TbSpu> findById(@PathVariable("id") String id){
        TbSpu spu = spuService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",spu);
    }
    
    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<TbSpu> findPage(@RequestBody Map searchMap, @PathVariable  Integer page, @PathVariable  Integer size){
        com.github.pagehelper.Page<TbSpu> pageList = spuService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult<TbSpu>(pageList.getTotal(),pageList.getResult());
        return new Result<TbSpu>(true,StatusCode.OK,"查询成功",pageResult);
    }
}
```





### 8.4.2 订单模块

需求:

1). 下单业务分析

2). 根据条件分页查询订单

表结构: 

tb_order , tb_order_item , tb_order_log



#### 8.4.2.1 创建工程

1).pom.xml

```xml
 <dependencies
     <dependency>
         <groupId>cn.itcast</groupId>
         <artifactId>v_common</artifactId>
         <version>1.0-SNAPSHOT</version>
     </dependency>

     <dependency>
         <groupId>cn.itcast</groupId>
         <artifactId>v_model</artifactId>
         <version>1.0-SNAPSHOT</version>
     </dependency>

     <dependency>
         <groupId>cn.itcast</groupId>
         <artifactId>v_feign_api</artifactId>
         <version>1.0-SNAPSHOT</version>
     </dependency>

     <!-- Eureka客户端依赖 -->
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
     </dependency>
	
	<!-- springboot - Mybatis 起步依赖 -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.1.0</version>
    </dependency>

     <!--MySQL数据库驱动-->
     <dependency>
         <groupId>mysql</groupId>
         <artifactId>mysql-connector-java</artifactId>
     </dependency>

     <!--mybatis分页插件-->
     <dependency>
         <groupId>com.github.pagehelper</groupId>
         <artifactId>pagehelper-spring-boot-starter</artifactId>
     </dependency>

     <!--web起步依赖-->
     <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-web</artifactId>
     </dependency>

     <!-- redis 使用-->
     <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-data-redis</artifactId>
     </dependency>

     <!-- fastJson依赖 -->
     <dependency>
         <groupId>com.alibaba</groupId>
         <artifactId>fastjson</artifactId>
     </dependency>

     <!-- Feign依赖 -->
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-openfeign</artifactId>
     </dependency>

</dependencies>
```



2). application.yml

```yml
server:
  port: 9002
spring:
  application:
    name: order
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/v_shop?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
    username: root
    password: 2143
  main:
    allow-bean-definition-overriding: true #当遇到同样名字的时候，是否允许覆盖注册
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://127.0.0.1:8161/eureka
  instance:
    prefer-ip-address: true
feign:
  client:
    config:
      default:   #配置全局的feign的调用超时时间  如果 有指定的服务配置 默认的配置不会生效
        connectTimeout: 60000  指定的是 消费者 连接服务提供者的连接超时时间 是否能连接  单位是毫秒
        readTimeout: 20000   指定的是调用服务提供者的 服务 的超时时间（）  单位是毫秒
```



3). 引导类

```java
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "cn.itcast.order.mapper")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
```



#### 8.4.2.2 下单业务分析

![image-20200217102815624](MyCat/image-20200217102815624.png)



#### 8.4.2.3 查询订单

![image-20200217141007643](MyCat/image-20200217141007643.png) 

##### 8.4.2.3.1 Mapper

1). mapper接口

```java
public interface OrderMapper  {
    public List<TbOrder> search(Map<String,Object> searchMap);
}
```





2). mapper映射配置文件

OrderMapper.xml

```xml
<mapper namespace="cn.itcast.order.mapper.OrderMapper" >
    <resultMap id="orderResultMap" type="cn.itcast.model.TbOrder">
        <id column="id" jdbcType="VARCHAR" property="id" />
        <result column="total_num" jdbcType="INTEGER" property="totalNum" />
        <result column="total_money" jdbcType="INTEGER" property="totalMoney" />
        <result column="pre_money" jdbcType="INTEGER" property="preMoney" />
        <result column="post_fee" jdbcType="INTEGER" property="postFee" />
        <result column="pay_money" jdbcType="INTEGER" property="payMoney" />
        <result column="pay_type" jdbcType="VARCHAR" property="payType" />
        <result column="create_time" jdbcType="TIMESTAMP" property="createTime" />
        <result column="update_time" jdbcType="TIMESTAMP" property="updateTime" />
        <result column="pay_time" jdbcType="TIMESTAMP" property="payTime" />
        <result column="consign_time" jdbcType="TIMESTAMP" property="consignTime" />
        <result column="end_time" jdbcType="TIMESTAMP" property="endTime" />
        <result column="close_time" jdbcType="TIMESTAMP" property="closeTime" />
        <result column="shipping_name" jdbcType="VARCHAR" property="shippingName" />
        <result column="shipping_code" jdbcType="VARCHAR" property="shippingCode" />
        <result column="username" jdbcType="VARCHAR" property="username" />
        <result column="buyer_message" jdbcType="VARCHAR" property="buyerMessage" />
        <result column="buyer_rate" jdbcType="CHAR" property="buyerRate" />
        <result column="receiver_contact" jdbcType="VARCHAR" property="receiverContact" />
        <result column="receiver_mobile" jdbcType="VARCHAR" property="receiverMobile" />
        <result column="receiver_province" jdbcType="VARCHAR" property="receiverProvince" />
        <result column="receiver_city" jdbcType="VARCHAR" property="receiverCity" />
        <result column="receiver_area" jdbcType="VARCHAR" property="receiverArea" />
        <result column="receiver_address" jdbcType="VARCHAR" property="receiverAddress" />
        <result column="source_type" jdbcType="CHAR" property="sourceType" />
        <result column="transaction_id" jdbcType="VARCHAR" property="transactionId" />
        <result column="order_status" jdbcType="CHAR" property="orderStatus" />
        <result column="pay_status" jdbcType="CHAR" property="payStatus" />
        <result column="consign_status" jdbcType="CHAR" property="consignStatus" />
        <result column="is_delete" jdbcType="CHAR" property="isDelete" />
    </resultMap>


    <select id="search" resultType="cn.itcast.model.TbOrder">
        SELECT
            o.id ,
            o.`create_time` createTime,
            o.username ,
            o.`total_money` totalMoney,
            o.`total_num` totalNum,
            o.`pay_type` payType,
            o.`pay_status` payStatus,

            p.`province` receiverProvince
        FROM
            tb_order o , tb_provinces p
        WHERE
            o.receiver_province = p.provinceid
        <if test="orderId != null and orderId != ''">
            and o.id = #{orderId}
        </if>

        <if test="payType != null and payType != ''">
            and o.pay_type = #{payType}
        </if>

        <if test="username != null and username != ''">
            and o.username = #{username}
        </if>

        <if test="payStatus != null and payStatus != ''">
            and o.order_status = #{payStatus}
        </if>
    </select>
</mapper>
```



##### 8.4.2.3.2 Service

service接口

```java
public interface OrderService {

    /***
     * 新增
     * @param order
     */
    void add(TbOrder order);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<TbOrder> findPage(Map<String, Object> searchMap, int page, int size);

}
```

service实现

```java
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private IdWorker idWorker;

    /**
     * 增加
     * @param order
     */
    @Override
    public void add(TbOrder order){
        //1.获取购物车的相关数据(redis)
        
        //2.统计计算:总金额,总数量
        
        //3.填充订单数据并保存到tb_order

        //4.填充订单项数据并保存到tb_order_item

        //5.记录订单日志
        
        //6.扣减库存并增加销量

        //7.删除购物车数据(redis)

      
    }
    
    
    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<TbOrder> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        return (Page<TbOrder>)orderMapper.search(searchMap);
    }
}
```





##### 8.4.2.3.3 Controller

```java
@RestController
@CrossOrigin(value = {"*"})
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping
    @OperateLog
    public Result add(@RequestBody TbOrder order){
        //获取登录人名称
        orderService.add(order);
        return new Result(true,StatusCode.OK,"提交成功");
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    @OperateLog
    public Result findPage(@RequestBody Map searchMap, @PathVariable  Integer page, @PathVariable  Integer size){
        Page<TbOrder> pageList = orderService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }
}
```



### 8.4.3 日志模块

表结构: 

tb_operatelog



需求: 

1). 记录日志

2). 查询日志

![image-20200205224758890](MyCat/image-20200205224758890.png) 

![image-20200218031150324](MyCat/image-20200218031150324.png) 

#### 8.4.3.1 创建工程

1). pom.xml

```xml
    <dependencies>

        <dependency>
            <groupId>cn.itcast</groupId>
            <artifactId>v_common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>cn.itcast</groupId>
            <artifactId>v_model</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>cn.itcast</groupId>
            <artifactId>v_feign_api</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>


        <!-- Eureka客户端依赖 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.0</version>
        </dependency>

        <!--MySQL数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!--mybatis分页插件-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
        </dependency>

        <!--web起步依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- fastJson依赖 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
        </dependency>

        <!-- Feign依赖 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

    </dependencies>
```



2). application.yml

```yml
server:
  port: 9003
spring:
  application:
    name: log
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/v_shop?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
    username: root
    password: 2143
  main:
    allow-bean-definition-overriding: true #当遇到同样名字的时候，是否允许覆盖注册
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://127.0.0.1:8161/eureka
  instance:
    prefer-ip-address: true
```



3). 引导类

```java
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "cn.itcast.log.mapper")
public class LogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class,args);
    }
   
    @Bean
    public IdWorker idworker(){
        return new IdWorker(0,0);
    }
}
```





**分布式ID生成**

snowflake是 Twitter 开源的分布式ID生成算法，结果是一个long型的ID。其核心思想是：使用41bit作为毫秒数，10bit作为机器的ID（5个bit是数据中心，5个bit的机器ID），12bit作为毫秒内的流水号（意味着每个节点在每毫秒可以产生 4096 个 ID），最后还有一个符号位，永远是0 ;

![image-20200218010603410](MyCat/image-20200218010603410.png) 

使用方式: 

```java
IdWorker idWorker=new IdWorker(1,1);//0-31 , 0-31

for(int i=0;i<10000;i++){
	long id = idWorker.nextId();
	System.out.println(id);
}
```







#### 8.4.3.2 Mapper

mapper接口

```java
public interface OperateLogMapper {

    public void insert(TbOperatelog operationLog);

    public List<TbOperatelog> search(Map searchMap);

}
```

OperateLogMapper.xml

```xml
<mapper namespace="cn.itcast.log.mapper.OperateLogMapper" >

    <resultMap id="operateLogResultMap" type="cn.itcast.model.TbOperatelog">
        <id column="id" jdbcType="BIGINT" property="id" />
        <result column="model_name" jdbcType="VARCHAR" property="modelName" />
        <result column="model_value" jdbcType="VARCHAR" property="modelValue" />
        <result column="return_value" jdbcType="VARCHAR" property="returnValue" />
        <result column="return_class" jdbcType="VARCHAR" property="returnClass" />
        <result column="operate_user" jdbcType="VARCHAR" property="operateUser" />
        <result column="operate_time" jdbcType="VARCHAR" property="operateTime" />
        <result column="param_and_value" jdbcType="VARCHAR" property="paramAndValue" />
        <result column="operate_class" jdbcType="VARCHAR" property="operateClass" />
        <result column="operate_method" jdbcType="VARCHAR" property="operateMethod" />
        <result column="cost_time" jdbcType="BIGINT" property="costTime" />
    </resultMap>


    <insert id="insert" parameterType="cn.itcast.model.TbOperatelog">
    insert into tb_operatelog (id, model_name, model_value, 
      return_value, return_class, operate_user, 
      operate_time, param_and_value, operate_class, 
      operate_method, cost_time)
    values (#{id}, #{modelName}, #{modelValue}, 
      #{returnValue}, #{returnClass}, #{operateUser}, 
      #{operateTime}, #{paramAndValue}, #{operateClass}, 
      #{operateMethod}, #{costTime})
  </insert>


    <select id="search" resultMap="operateLogResultMap">
        select * from tb_operatelog
        <where>
            <if test="operateUser != null and operateUser != ''">
                and operate_user = #{operateUser}
            </if>
            <if test="operateMethod != null and operateMethod != ''">
                and operate_method = #{operateMethod}
            </if>
            <if test="returnClass != null and returnClass != ''">
                and return_class = #{returnClass}
            </if>
            <if test="costTime != null and costTime != '' ">
                and cost_time = #{costTime}
            </if>
        </where>
    </select>

</mapper>
```



#### 8.4.3.3 Service

接口

```
public interface OperateLogService {
    public void insert(TbOperatelog operationLog);

    public Page<TbOperatelog> findPage(Map searchMap, Integer pageNum , Integer pageSize);
}
```

实现

```java
@Service
@Transactional
public class OperateLogServiceImpl implements OperateLogService {


    @Autowired
    private OperateLogMapper operateLogMapper;

    public void insert(TbOperatelog operationLog){
        long id = idworker.nextId();
        operationLog.setId(id);
        operateLogMapper.insert(operationLog);
    }

    public Page<TbOperatelog> findPage(Map searchMap, Integer pageNum , Integer pageSize){
        System.out.println(searchMap);
	
        PageHelper.startPage(pageNum,pageSize);
        List<TbOperatelog> list = operateLogMapper.search(searchMap);

        return (Page<TbOperatelog>) list;
    }

}
```





#### 8.4.3.4 Controller

```java
@RestController
@RequestMapping("/operateLog")
public class OperateLogController {

    @Autowired
    private OperateLogService operateLogService;

    @RequestMapping("/search/{page}/{size}")
    public Result findList(@RequestBody Map dataMap, @PathVariable Integer page, @PathVariable  Integer size){

        Page<TbOperatelog> pageList = operateLogService.findPage(dataMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());

        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }


    @RequestMapping("/add")
    public Result add(@RequestBody TbOperatelog operatelog){
        operateLogService.insert(operatelog);
        return new Result(true,StatusCode.OK,"添加成功");
    }

}
```



#### 8.4.3.5 AOP记录日志

在需要记录操作日志的微服务中, 引入AOP记录日志的类 : 

1). 自定义注解

```java
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {
}
```

2). AOP通知类

```java
@Component
@Aspect
public class OperateAdvice {

	@Autowired
	private OperateLogFeign operateLogFeign;

	@Around("execution(* cn.itcast.goods.controller.*.*(..)) && @annotation(operateLog)")
	public Object insertLogAround(ProceedingJoinPoint pjp , OperateLog operateLog) throws Throwable{
		System.out.println(" *********************************** 记录日志 [start]  ****************************** ");

		TbOperatelog op = new TbOperatelog();

		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		op.setOperateTime(sdf.format(new Date()));
		op.setOperateUser("10000");
		
		op.setOperateClass(pjp.getTarget().getClass().getName());
		op.setOperateMethod(pjp.getSignature().getName());

		String paramAndValue = "";
		Object[] args = pjp.getArgs();
		if(args != null){
			for (Object arg : args) {
				if(arg instanceof String || arg instanceof Integer || arg instanceof Long){
					paramAndValue += arg +",";
				}else{
					paramAndValue += JSON.toJSONString(arg)+",";
				}
			}
			op.setParamAndValue(paramAndValue);
		}

		long start_time = System.currentTimeMillis();

		//放行
		Object object = pjp.proceed();

		long end_time = System.currentTimeMillis();
		op.setCostTime(end_time - start_time);

		if(object != null){
			op.setReturnClass(object.getClass().getName());
			op.setReturnValue(object.toString());
		}else{
			op.setReturnClass("java.lang.Object");
			op.setParamAndValue("void");
		}

		operateLogFeign.add(op);

		System.out.println(" *********************************** 记录日志 [end]  ****************************** ");

		return object;
	}
}
```



## 8.5 MyCat分片

当前数据库的情况 :

![image-20200209111951501](MyCat/image-20200209111951501.png) 

由于当前项目是一个电商项目，项目上线后，随着项目的运营，业务系统的数据库中的数据与日俱增，特别是订单、日志等数据，如果数据量过大，这个时候就需要考虑通过MyCat分库分表。



### 8.5.1 分片分析

1). 垂直拆分

数据量过大，需要考虑扩容，可以通过MyCat来实现数据库表的垂直拆分，将同一块业务的数据库表拆分到同一个数据库服务中。拆分方式如下： 

![image-20200209110344132](MyCat/image-20200209110344132.png) 



2). 全局表

按照上述的方式进行表结构的拆分，可以解决扩容的问题，但是存在另一个问题：由于省、市、区县、数据字典表，在订单及商品等模块中都需要用到，还会涉及到多表连接查询，那么这个时候涉及到跨库的join操作，可以使用全局表来解决。结构图如下： 

![image-20200209111118652](MyCat/image-20200209111118652.png) 



3). 水平拆分

即使我们在上述的方案中使用垂直拆分，将系统中的表结构拆分到了三个数据库服务器中，但是对于当前这个比较繁忙的业务系统来说，每天都会产生大量的用户操作日志，长年累月，这张表的数据在单台服务器中已经存储不下了，这个时候，我们就可以使用MyCat的水平拆分来解决这个问题。

![image-20200209171203841](MyCat/image-20200209171203841.png) 



### 8.5.2 服务器配置

| 名称         |       IP        | 端口 | 用户名/密码 |
| :----------- | :-------------: | :--: | :---------: |
| MyCat-Server | 192.168.192.157 | 8066 | root/123456 |
| MySQL-1      | 192.168.192.158 | 3306 | root/itcast |
| MySQL-2      | 192.168.192.159 | 3306 | root/itcast |
| MySQL-3      | 192.168.192.160 | 3306 | root/itcast |
| MySQL-4      | 192.168.192.161 | 3306 | root/itcast |



### 8.5.3 schema.xml

```xml
<?xml version="1.0"?>
<!DOCTYPE MyCat:schema SYSTEM "schema.dtd">
<MyCat:schema xmlns:MyCat="http://io.MyCat/">
	
	<schema name="V_SHOP" checkSQLschema="false" sqlMaxLimit="100">
       
	   <table name="tb_areas" dataNode="dn1,dn2,dn3,dn4" primaryKey="areaid" type="global"/>
	   <table name="tb_provinces" dataNode="dn1,dn2,dn3,dn4" primaryKey="provinceid" type="global"/>
	   <table name="tb_cities" dataNode="dn1,dn2,dn3,dn4" primaryKey="cityid"  type="global"/>
	   <table name="tb_dictionary" dataNode="dn1,dn2,dn3,dn4" primaryKey="id"  type="global"/>
		
		
		<table name="tb_brand" dataNode="dn1" primaryKey="id" />
		<table name="tb_category" dataNode="dn1" primaryKey="id" />
		<table name="tb_sku" dataNode="dn1" primaryKey="id" />
		<table name="tb_spu" dataNode="dn1" primaryKey="id" />
		
        
		<table name="tb_order" dataNode="dn2" primaryKey="id" />
		<table name="tb_order_item" dataNode="dn2" primaryKey="id" />
		<table name="tb_order_log" dataNode="dn2" primaryKey="id" />
		
        
		<table name="tb_operatelog" dataNode="dn3,dn4" primaryKey="id" rule="log-sharding-by-murmur"/>
	</schema>
    
	
	<dataNode name="dn1" dataHost="host1" database="v_goods" />
	<dataNode name="dn2" dataHost="host2" database="v_order" />
	<dataNode name="dn3" dataHost="host3" database="v_log" />
	<dataNode name="dn4" dataHost="host4" database="v_log" />
    
	
	<dataHost name="host1" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM1" url="192.168.192.158:3306" user="root" password="itcast">	</writeHost>
	</dataHost>	
    
    <dataHost name="host2" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM2" url="192.168.192.159:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
    
    <dataHost name="host3" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM3" url="192.168.192.160:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
	
	<dataHost name="host4" maxCon="1000" minCon="10" balance="0"
		writeType="0" dbType="mysql" dbDriver="native" switchType="1"  slaveThreshold="100">
		<heartbeat>select user()</heartbeat>
		<writeHost host="hostM4" url="192.168.192.161:3306" user="root" password="itcast"></writeHost>
	</dataHost>	
</MyCat:schema>
```





### 8.5.4 分片配置

1). 配置MyCat的schema.xml



2). 配置rule.xml

```xml
<tableRule name="log-sharding-by-murmur">
    <rule>
        <columns>id</columns>
        <algorithm>log-murmur</algorithm>
    </rule>
</tableRule>

<function name="log-murmur" class="io.MyCat.route.function.PartitionByMurmurHash">
    <property name="seed">0</property>
    <property name="count">2</property>
    <property name="virtualBucketTimes">160</property>
</function>
```



3). 配置server.xml

```xml
<user name="root" defaultAccount="true">
    <property name="password">GO0bnFVWrAuFgr1JMuMZkvfDNyTpoiGU7n/Wlsa151CirHQnANVk3NzE3FErx8v6pAcO0ctX3xFecmSr+976QA==</property>
    <property name="schemas">V_SHOP</property>
    <property name="readOnly">false</property>
    <property name="benchmark">1000</property>
    <property name="usingDecrypt">1</property>

    <!-- 表级 DML 权限设置

  <privileges check="true">
   <schema name="ITCAST" dml="1111" >
    <table name="TB_TEST" dml="1110"></table>
   </schema>
  </privileges>	
  -->		
</user>
```



4). 在各个MySQL数据库实例中创建数据库

```
MySQL-1 : v_goods
MySQL-2 : v_order
MySQL-3 : v_log
MySQL-4 : v_log
```



5). 导出本地的SQL脚本 , 在MyCat中执行SQL脚本 , 创建数据表 ,并导入数据



6). 连接测试



### 8.5.5 微服务连接MyCat

```yml
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://192.168.192.157:8066/V_SHOP?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
    username: root
    password: 123456
```



### 8.5.6 配置MyCat-Web监控

1). 启动Zookeeper

2). 启动MyCat-Web

3). 访问 

http://192.168.192.157:8082/MyCat

界面: 

![image-20200219231604626](MyCat/image-20200219231604626.png) 













































































































