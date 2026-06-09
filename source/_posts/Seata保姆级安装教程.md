---
title: Seata保姆级安装教程
date: 2024-05-10 16:30:51
categories:
- 微服务
tags:
- Seata保姆级安装教程
---

# Seata保姆级安装教程

> 参考：
>
> [Seata安装与使用以及部分常见问题](https://blog.csdn.net/qq_42767920/article/details/135743126#comments_32782341)
>
> [SpringCloud+Nacos集成Seata-1.7.0分布式事务](https://juejin.cn/post/7257713854745395255)
>
> [七步带你集成Seata 1.2 高可用搭建](https://mp.weixin.qq.com/s/2KSidJ72YsovpJ94P1aK1g)
>
> [ Seata 1.5 特性与升级分享](https://gitee.com/itCjb/spring-cloud-alibaba-seata-demo)

## 一、seata安装和部署

### 0.前提环境

- seata-1.7.1（1.5之前和之后的项目目录不一样，但是大致的部署方式应该是相同）

### [1.下载seata](https://seata.apache.org/zh-cn/unversioned/download/seata-server)

根目录展开。每个版本的不太一样。

![image-20240514102451911](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/image-20240514102451911.png)

### 2.创建seata数据库

- 为数据库添加表；升级1.5.0前，请注意表结构变更。
- 1.7.1中有`\seata\script\server\db`，其他版本没有的可以看[表结构详情](https://github.com/seata/seata/tree/1.5.0/script/server/db)
- 增加`distributed_lock`表用于`seata-server`异步任务调度

```mysql
-- -------------------------------- The script used when storeMode is 'db' --------------------------------
-- the table to store GlobalSession data
CREATE TABLE IF NOT EXISTS `global_table`
(
    `xid`                       VARCHAR(128) NOT NULL,
    `transaction_id`            BIGINT,
    `status`                    TINYINT      NOT NULL,
    `application_id`            VARCHAR(32),
    `transaction_service_group` VARCHAR(32),
    `transaction_name`          VARCHAR(128),
    `timeout`                   INT,
    `begin_time`                BIGINT,
    `application_data`          VARCHAR(2000),
    `gmt_create`                DATETIME,
    `gmt_modified`              DATETIME,
    PRIMARY KEY (`xid`),
    KEY `idx_status_gmt_modified` (`status` , `gmt_modified`),
    KEY `idx_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- the table to store BranchSession data
CREATE TABLE IF NOT EXISTS `branch_table`
(
    `branch_id`         BIGINT       NOT NULL,
    `xid`               VARCHAR(128) NOT NULL,
    `transaction_id`    BIGINT,
    `resource_group_id` VARCHAR(32),
    `resource_id`       VARCHAR(256),
    `branch_type`       VARCHAR(8),
    `status`            TINYINT,
    `client_id`         VARCHAR(64),
    `application_data`  VARCHAR(2000),
    `gmt_create`        DATETIME(6),
    `gmt_modified`      DATETIME(6),
    PRIMARY KEY (`branch_id`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- the table to store lock data
CREATE TABLE IF NOT EXISTS `lock_table`
(
    `row_key`        VARCHAR(128) NOT NULL,
    `xid`            VARCHAR(128),
    `transaction_id` BIGINT,
    `branch_id`      BIGINT       NOT NULL,
    `resource_id`    VARCHAR(256),
    `table_name`     VARCHAR(32),
    `pk`             VARCHAR(36),
    `status`         TINYINT      NOT NULL DEFAULT '0' COMMENT '0:locked ,1:rollbacking',
    `gmt_create`     DATETIME,
    `gmt_modified`   DATETIME,
    PRIMARY KEY (`row_key`),
    KEY `idx_status` (`status`),
    KEY `idx_branch_id` (`branch_id`),
    KEY `idx_xid_and_branch_id` (`xid` , `branch_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `distributed_lock`
(
    `lock_key`       CHAR(20) NOT NULL,
    `lock_value`     VARCHAR(20) NOT NULL,
    `expire`         BIGINT,
    primary key (`lock_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('AsyncCommitting', ' ', 0);
INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryCommitting', ' ', 0);
INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryRollbacking', ' ', 0);
INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('TxTimeoutCheck', ' ', 0);
```

将参与全局事务的数据库中加入undo_log这张表

```mysql
-- for AT mode you must to init this sql for you business database. the seata server not need it.
CREATE TABLE IF NOT EXISTS `undo_log`
(
    `branch_id`     BIGINT(20)   NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(100) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8 COMMENT ='AT transaction mode undo table';
```

### 3.修改seata配置文件

- 进入`seata/conf`目录下，有两个配置文件，把`application.yml`做个备份，然后把 `application.example.yml`修改成`application.yml`作为主要配置文件。

- 修改`application.yml`文件，这里使用的nacos作为注册中心

  ![image-20240511111840988](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/image-20240511111840988.png)

  ![image-20240514161000514](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/image-20240514161000514.png)

  ```yaml
  server:
    port: 7091
  
  spring:
    application:
      name: seata-server
  
  logging:
    config: classpath:logback-spring.xml
    file:
      path: ${user.home}/logs/seata
    extend:
      logstash-appender:
        destination: 127.0.0.1:4560
      kafka-appender:
        bootstrap-servers: 127.0.0.1:9092
        topic: logback_to_logstash
  
  console:
    user:
      username: seata
      password: seata
  
  seata:
    config:
      # support: nacos, consul, apollo, zk, etcd3
      type: nacos
      nacos:
        server-addr: 127.0.0.1:8848
        namespace:
        group: SEATA_GROUP
        username: nacos
        password: nacos
        data-id: seataServer.properties
    registry:
      # support: nacos, eureka, redis, zk, consul, etcd3, sofa
      type: nacos
      nacos:
        application: seata-server
        server-addr: 127.0.0.1:8848
        group: SEATA_GROUP
        namespace:
        cluster: SH # 关键！必须与客户端(项目)vgroup-mapping匹配
        username: nacos
        password: nacos
    store:
      # support: file 、 db 、 redis
      mode: db
      db:
        datasource: druid
        db-type: mysql
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/seata?rewriteBatchedStatements=true
        user: mysql
        password: mysql
        min-conn: 10
        max-conn: 100
        global-table: global_table
        branch-table: branch_table
        lock-table: lock_table
        distributed-lock-table: distributed_lock
        query-limit: 1000
        max-wait: 5000
      
  #  server:
  #    service-port: 8091 #If not configured, the default is '${server.port} + 1000'
    security:
      secretKey: SeataSecretKey0c382ef121d778043159209298fd40bf3850a017
      tokenValidityInMilliseconds: 1800000
      ignore:
        urls: /,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-fe/public/**,/api/v1/auth/login
  ```

- 第一步将原来`application.yml`文件中的console和security拷贝走，复制到主要配置文件（最初的`application.example.yml`）文件中
  ![在这里插入图片描述](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/0a5b59c85dfc41efbaa3395137fd3a62.png)

  console放在这个位置  

  ![在这里插入图片描述](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/d88612f6158b4bba8e0536035e0ebb1d.png)

  security放在最下面，注意缩进格式 

  ![在这里插入图片描述](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/97b3265c355447018457085922d9e886.png)

### 4.nacos设置配置中心

上面说到seata配置文件中的nacos配置文件要创建一个文件seataServer.properties

![image-20240511112419594](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/image-20240511112419594.png)

1.7.1文件内容在`\seata\script\config-center\config.txt`，其他版本没有可以看：[config.txt](https://github.com/apache/incubator-seata/tree/develop/script/config-center)

要将里面的数据库修改成自己的，可以删除一部分不需要的。

![image-20240511112534957](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/image-20240511112534957.png)

```properties
#For details about configuration items, see https://seata.io/zh-cn/docs/user/configurations.html
#Transport configuration, for client and server
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableTmClientBatchSendRequest=false
transport.enableRmClientBatchSendRequest=true
transport.enableTcServerBatchSendResponse=false
transport.rpcRmRequestTimeout=30000
transport.rpcTmRequestTimeout=30000
transport.rpcTcRequestTimeout=30000
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
transport.serialization=seata
transport.compressor=none

#Transaction routing rules configuration, only for the client
service.vgroupMapping.my_test_tx_group=SH # 事务组映射（需包含客户端（项目）的所有tx-service-group）
#If you use a registry, you can ignore it
service.default.grouplist=127.0.0.1:8091
service.enableDegrade=false
service.disableGlobalTransaction=false

#Transaction rule configuration, only for the client
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=true
client.rm.tableMetaCheckerInterval=60000
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
client.rm.sagaJsonParser=fastjson
client.rm.tccActionInterceptorOrder=-2147482648
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000
client.tm.interceptorOrder=-2147482648
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k
#For TCC transaction mode
tcc.fence.logTableName=tcc_fence_log
tcc.fence.cleanPeriod=1h

#Log rule configuration, for client and server
log.exceptionRate=100

#Transaction storage configuration, only for the server. The file, db, and redis configuration values are optional.
store.mode=file
store.lock.mode=file
store.session.mode=file
#Used for password encryption
store.publicKey=

#If `store.mode,store.lock.mode,store.session.mode` are not equal to `file`, you can remove the configuration block.
store.file.dir=file_store/data
store.file.maxBranchSessionSize=16384
store.file.maxGlobalSessionSize=512
store.file.fileWriteBufferCacheSize=16384
store.file.flushDiskMode=async
store.file.sessionReloadReadSize=100

#These configurations are required if the `store mode` is `db`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `db`, you can remove the configuration block.
store.db.datasource=druid
store.db.dbType=mysql
store.db.driverClassName=com.mysql.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&rewriteBatchedStatements=true
store.db.user=username
store.db.password=password
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.distributedLockTable=distributed_lock
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000

#These configurations are required if the `store mode` is `redis`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `redis`, you can remove the configuration block.
store.redis.mode=single
store.redis.single.host=127.0.0.1
store.redis.single.port=6379
store.redis.sentinel.masterName=
store.redis.sentinel.sentinelHosts=
store.redis.sentinel.sentinelPassword=
store.redis.maxConn=10
store.redis.minConn=1
store.redis.maxTotal=100
store.redis.database=0
store.redis.password=
store.redis.queryLimit=100

#Transaction rule configuration, only for the server
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.distributedLockExpireTime=10000
server.xaerNotaRetryTimeout=60000
server.session.branchAsyncQueueSize=5000
server.session.enableBranchAsyncRemove=false
server.enableParallelRequestHandle=false

#Metrics configuration, only for the server
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898
```

### 5.启动服务

找到`seata-server.bat`，点击启动，路径为：`seata->bin->seata-server.bat`，成功后可以在nacos控制台服务列表中看到多了一个服务。

如果闪退了说明有错误，需要在该目录下打开cmd控制台输入`seata-server.bat` 启动

**常见问题**

- java不是命令，java环境的问题，配置环境即可解决
- `javax.net.ssl.SSLHandshakeException: No appropriate protocol (protocol is disabled or cipher suites are inappropriate)`，数据库的url链接没有加`useSSL=false`或者数据库是8.0的但是驱动没有加`cj`
- `xxx.security` 该问题是复制的security缩进不对，没有被识别

部署成功后就会有seata-server的服务名的服务（其他服务是分布式服务，后续说明）

![在这里插入图片描述](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/5572039804184bd6971b902086ac4984.png)

## 二、使用脚本（可选）

先按照【一、seata安装和部署】中的前3步完成操作，第4步在本地操作`\seata\script\config-center\config.txt`文件，也就是不在nacos中自己手动配置了。

修改完`config.txt`文件，在`\seata\script\config-center\nacos`中有一个`nacos-config-interactive.sh`文件，执行这个文件

```sh
sh ${SEATAPATH}/script/config-center/nacos/nacos-config-interactive.sh
```

![image-20240514104356712](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/image-20240514104356712.png)

上面这个文件是交互式的，还是有一个非交互式的文件`\seata\script\config-center\nacosnacos-config.sh`

参数说明：

-h：host，默认值为 localhost。

-p：端口，默认值为 8848。

-g：配置分组，默认值为“SEATA_GROUP”。

-t：租户信息，对应 Nacos 的命名空间 ID 字段，默认值为 ''。

-u：用户名，Nacos 1.2.0+ 权限控制时，默认值为 ''。

-w：密码，权限控制上的 nacos 1.2.0+，默认值为 ''。

```sh
sh ${SEATAPATH}/script/config-center/nacos/nacos-config.sh -h localhost -p 8848 -g SEATA_GROUP -t 5a3c7d6c-f497-4d68-a71a-2e5e3340b3ca -u username -w password
```

其他版本没有config.txt、脚本和命令的可以看 [这里](https://github.com/apache/incubator-seata/tree/develop/script/config-center)

<img src="Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/image-20240514105933835.png" alt="image-20240514105933835" style="zoom:67%;" />

<img src="Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/image-20240514105951141.png" alt="image-20240514105951141" style="zoom: 67%;" />



在nacos的配置中心就会有好多配置，它是将config.txt中的每一条拆成了一条配置。在其他版本可能会创建一个`seataServer.properties`文件，将config.txt中的配置放入，就像【一、seata安装和部署】中我们手动配置的第4步。

![image-20240514104618437](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/image-20240514104618437.png)

之后再根据【一、seata安装和部署】中的第5步执行启动就可以了。

## 三、配置项目

### 1.引入依赖

```xml
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
                <version>2.2.2.RELEASE</version>
                <exclusions>
                    <exclusion>
                        <groupId>io.seata</groupId>
                        <artifactId>seata-spring-boot-starter</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>io.seata</groupId>
                <artifactId>seata-spring-boot-starter</artifactId>
                <version>1.7.1</version>
            </dependency>
```

### 2.配置yaml

这里的nacos注册和配置和`seata/config/application.yaml`中的配置基本相同

```yaml
seata:
  enabled: true
  application-id: applicationName # 项目服务名
  tx-service-group: my_test_tx_group # 事务组名（需全局唯一）
  service:
    vgroup-mapping: ## 事务组与TC服务cluster的映射关系
      my_test_tx_group: SH ## 集群名称
  enable-auto-data-source-proxy: true
  config:
    type: nacos
    nacos:
      namespace:
      serverAddr: 127.0.0.1:8848
      group: SEATA_GROUP
      username: "nacos"
      password: "nacos"
      data-id: seataServer.properties
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: 127.0.0.1:8848
      group: SEATA_GROUP
      namespace:
      username: "nacos"
      password: "nacos"
```

![](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/seata_config.png)

### 3.在需要事务的方法上添加注解

该步不需要每个都配置，只需要在主要的方法上添加开启事务即可

```java
// name任意，不重复即可  rollbackFor最好设置为Exception.class，不然有些异常不回滚
@GlobalTransactional(name = "order",rollbackFor = Exception.class)

// 案例
/**
    *
    * @param userId  用户id 目前只有1
    * @param productId 商品id 目前有1-4
    * @param quantity 购买数量
    * @param addressId 地址信息id
    * @return
    */
   @GetMapping("/order/add.do")
   @GlobalTransactional(name = "order",rollbackFor = Exception.class)
   public Map<String,Object> add(Long userId, Long productId, Integer quantity, Long addressId) {
       System.out.println("事务id是"+ RootContext.getXID());
       
       Order order = new Order();

       order.setUserId(userId);

       //通过productId获取商品信息
       Product product = IProductService.get(productId);
       order.setProductId(product.getId());
       order.setProductName(product.getName());//商品名称在后期可能发生变化，这里记录的是下单时商品名称
       order.setProductPrice(product.getPrice());//商品名称在后期可能发生变化，这里记录的是下单时商品名称

       //通过addressId获取地址信息

       Address address = IAddressService.get(addressId);
       order.setRealName(address.getRealName());//收件人姓名在后期可能发生变化，这里记录的是下单时用户选中的邮寄地址对应的收件人姓名
       order.setMobile(address.getMobile());//收件人手机号在后期可能发生变化，这里记录的是下单时用户选中的邮寄地址对应的收件人手机号
       order.setAddressDetail(address.getDetail());//收件人详细地址在后期可能发生变化，这里记录的是下单时用户选中的邮寄地址对应的收件人详细地址

       order.setQuantity(quantity);

       double amount = product.getPrice() * quantity;//计算商品总额

       Map<String,Object> map = new HashMap<>();
       if(IProductService.update(addressId,quantity) //修改商品库存
               && IWalletService.getWallet(userId,amount) //修改钱包余额
               && orderService.save(order)){//添加订单信息
           map.put("state","200");
           map.put("message","订单创建成功！");
       }else{
           map.put("state","100");
           map.put("message","订单创建失败！");

       }
       return map;
```

### 4.结果展示

添加@GlobalTransactional注解日志打印，显示已经回滚  

![在这里插入图片描述](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/b83752ff249f4644a4082b64dd197cd5.png)  

正常执行的子服务  

![在这里插入图片描述](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/49e6b025e1414b9ca4f5de84d102b0db.png)  

异常的子服务  

![在这里插入图片描述](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/33d620aff97c41a1a8e7716ab2be6432.png)

### 5.常见问题

**xxx.undo\_log不存在**

java.sql.SQLSyntaxErrorException: Table ‘mall.undo\_log’ doesn’t exist  

![在这里插入图片描述](Seata%E4%BF%9D%E5%A7%86%E7%BA%A7%E5%AE%89%E8%A3%85%E6%95%99%E7%A8%8B/596c824df44b4b1db5f8af88465416af.png)  

在服务操作的目标数据库执行下SQL添加表

```sql
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(0) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(0) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
```

**事务不回滚**

在添加@GlobalTransactional注解的服务中，日志显示status：RollBacked但是在其他服务模块中日志打印的是Commited

该问题一般是因为XID不一致导致的，一般不会出现该问题本文没有遇到该问题

提供一个方法来判断是否是XID不一致或者子服务中XID为null

```java
// RootContext.getXID()就是获取事务的XID 在子服务和总服务下输出一下就能知道是否一致
System.out.println("事务id是"+ RootContext.getXID());
```

**第一种情况**  

如果使用的不是本文的pring-cloud-starter-alibaba-seata这个依赖，而是使用的这以下依赖有可能出现该问题

```xml
<!--seata1.7.0-->
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-spring-boot-starter</artifactId>
    <version>1.7.0</version>
</dependency>
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-all</artifactId>
    <version>1.7.0</version>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    <version>2022.0.0.0-RC2</version>
    <exclusions>
        <exclusion>
            <groupId>io.seata</groupId>
            <artifactId>seata-spring-boot-starter</artifactId>
        </exclusion>
        <exclusion>
            <groupId>io.seata</groupId>
            <artifactId>seata-all</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

**第二种情况**  

seata 分布式事务没有传递xid导致事务失效

本文没有出现这种情况，是第一种情况所以在此提供的一种解决方案

<https://blog.csdn.net/qq_45278455/article/details/124364505>