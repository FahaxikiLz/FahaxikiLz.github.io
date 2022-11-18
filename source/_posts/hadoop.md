---
title: hadoop
date: 2022-10-16 19:55:30
comments: true
tags:
- hadoop
categories: 
- 大数据
---

# 集群搭建

**使用三台机器搭建环境集群**

- 使用三台机器搭建环境集群

| 用户    | 服务     | IP             |
| ------- | -------- | -------------- |
| thpffcj | thpffcj  | 172.19.191.11  |
| thpffcj | thpffcj1 | 172.19.240.128 |
| thpffcj | thpffcj2 | 172.19.240.135 |

- 修改/etc/hosts文件


        172.19.191.11   thpffcj
        172.19.240.128  thpffcj1
        172.19.240.135  thpffcj2

## 1. Hadoop集群

| 服务 | 主节点          | 从节点      |
| ---- | --------------- | ----------- |
| HDFS | NameNode        | DataNode    |
| YARN | ResourceManager | NodeManager |

### 1. 下载解压

### 2. 配置环境变量

### 3. 修改配置文件

- etc/hadoop/core-site.xml


        <configuration>
            <property>
                <name>fs.defaultFS</name>
                <value>hdfs://thpffcj:9000</value>
            </property>
            <property>
                <name>hadoop.tmp.dir</name>
                <value>/Users/thpffcj/Public/data/hadoop</value>
            </property>
        </configuration>

- etc/hadoop/hdfs-site.xml

        <configuration>
            <property>
                <name>dfs.replication</name>
                <value>1</value>
            </property>
        </configuration>

- etc/hadoop/mapred-site.xml

        <configuration>
            <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
            </property>
        </configuration>

- etc/hadoop/yarn-site.xml

        <configuration>
            <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>thpffcj</value>
            </property>
            <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
            </property>
        </configuration>

- slaves

        thpffcj1
        thpffcj2

### 4. 把安装包分别分发给其他的节点

**重点强调： 每台服务器中的hadoop安装包的目录必须一致， 安装包的配置信息还必须保持一致**

- 这点主要因为本机是Mac，两台云服务器是CentOS环境，目录结构不是特别一直，导致集群出现了一些问题，我拷贝到Users目录下主要是为了保持和Mac目录一致，建议使用三台Linux环境


        thpffcj:software thpffcj$ scp -r hadoop-2.6.0-cdh5.16.2/ thpffcj1:/Users/thpffcj/Public/software/
    
        thpffcj:software thpffcj$ scp -r hadoop-2.6.0-cdh5.16.2/ thpffcj2:/Users/thpffcj/Public/software/

- 拷贝之后，每台主机都需要配置环境变量

### 5. 其他问题

**ssh免密登录**

    Starting namenodes on [thpffcj1]
    thpffcj1: Permission denied (publickey,gssapi-keyex,gssapi-with-mic,password).
    Starting datanodes
    localhost: Permission denied (publickey,gssapi-keyex,gssapi-with-mic,password).
    Starting secondary namenodes [thpffcj1]
    thpffcj1: Permission denied (publickey,gssapi-keyex,gssapi-with-mic,password).

- 需要配置免密登录

[配置ssh免密登录](https://blog.csdn.net/snail_bing/article/details/81772982)

- 每台机器上生成秘钥


        ssh-keygen
        cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

- 将主节点公钥拷贝到从节点上


        ssh-copy-id -i ~/.ssh/id_rsa.pub thpffcj1
        ssh-copy-id -i ~/.ssh/id_rsa.pub thpffcj2

- 需要保持三台机器登录用户一致
- 期间还遇见配置不成功，查看日志发现没有文件权限：[配置免密登录不生效](https://blog.csdn.net/lisongjia123/article/details/78513244)

**开放端口**

- 云主机没有开发必要的通信端口，在启动时不会报错，需要查看日志发现长时间重连失败


        sudo firewall-cmd --zone=public --add-port=9000/tcp --permanent
        sudo firewall-cmd --zone=public --add-port=8031/tcp --permanent
        sudo firewall-cmd --reload
        sudo firewall-cmd --list-port

## 5. 启动集群

- 只需在主节点机器操作
- 初始化文件系统


      thpffcj:hadoop-2.6.0-cdh5.16.2 thpffcj$ hadoop namenode -format

- 启动集群

  
      thpffcj:hadoop-2.6.0-cdh5.16.2 thpffcj$ sbin/start-all.sh

## 6. 经验

- **日志在log目录底下，解决不了的问题看看日志**
- **配置完一个节点后直接复制所有文件到另一个节点**

## 2. Zookeeper集群

- 集群配置大同小异，一个配置成功了，其他都会比较顺利

### 1. 修改配置文件

- zoo.cfg


        dataDir=/Users/thpffcj/Public/data/zookeeper
        server.0=thpffcj:2888:3888
        server.1=thpffcj1:2888:3888
        server.2=thpffcj2:2888:3888

- 创建data目录和myid文件


        echo 0 > /Users/thpffcj/Public/data/zookeeper/myid

### 2. 拷贝到其他两个机器

    thpffcj:software thpffcj$ scp -r apache-zookeeper-3.5.5-bin/ thpffcj1:/Users/thpffcj/Public/software/
    thpffcj:software thpffcj$ scp -r apache-zookeeper-3.5.5-bin/ thpffcj2:/Users/thpffcj/Public/software/

- 修改其他两台机器上的myid分别为1和2

## 3. 启动集群

- 每台机器都需要启动服务


        zkServer.sh start
    
        zkServer.sh status
    
        zkServer.sh stop

## 3. Kafka集群

### 1. 修改配置文件

- server.properties


        broker.id=0
        listeners=PLAINTEXT://thpffcj:9092
        zookeeper.connect=thpffcj:2181,thpffcj1:2181,thpffcj2:2181

### 2. 拷贝到其他两个机器

    thpffcj:software thpffcj$ scp -r kafka_2.12-2.2.0/ thpffcj1:/Users/thpffcj/Public/software/
    thpffcj:software thpffcj$ scp -r kafka_2.12-2.2.0/ thpffcj2:/Users/thpffcj/Public/software/

- 需要修改broker.id为1和2，listeners为thpffcj1和thpffcj2

## 3. 启动集群

    kafka-server-start.sh $KAFKA_HOME/config/server.properties &
    
    kafka-server-stop.sh

- 创建topic


       kafka-topics.sh --create --zookeeper thpffcj:2181 --replication-factor 3 --partitions 1 --topic test-topic

- 查看topic


      kafka-topics.sh --describe --zookeeper thpffcj:2181 --topic test-topic

## 4. Spark集群

### 1. 下载编译Spark源码

- 我这里采用了自己编译源码的方式，也可以直接下载编译好的spark直接解压
- [spark-2.4.0 源码编译](https://blog.csdn.net/jyx00123/article/details/85913478)

### 2. 修改配置文件

- spark-env.sh


        export JAVA_HOME=/Users/thpffcj/Public/software/jdk1.8.0_221
        export SCALA_HOME=/Users/thpffcj/Public/software/scala-2.12.8
        # 指定默认master的ip或主机名
        export SPARK_MASTER_HOST=thpffcj
        # 每个worker从节点能够支配的内存数
        export SPARK_WORKER_MEMORY=1g
        # 指向包含Hadoop集群的（客户端）配置文件的目录，运行在Yarn上配置此项 
        export HADOOP_CONF_DIR=/Users/thpffcj/Public/software/hadoop-2.6.0-cdh5.16.2/etc/hadoop

- slaves

  
        thpffcj1
        thpffcj2

### 3. 拷贝到其他两个机器

    thpffcj:software thpffcj$ scp -r spark-2.4.0-bin-2.6.0-cdh5.16.2/ thpffcj1:/Users/thpffcj/Public/software/
    thpffcj:software thpffcj$ scp -r spark-2.4.0-bin-2.6.0-cdh5.16.2/ thpffcj2:/Users/thpffcj/Public/software/

### 4. 启动集群

    cd $SPARK_HOME
    ./sbin/start-all.sh