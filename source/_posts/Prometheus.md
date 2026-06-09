---
title: Prometheus
date: 2025-04-03 11:56:29
categories:
- 运维
tags:
- Prometheus
---



## 一、普罗米修斯监控概述

### 1.1 什么是普罗米修斯监控

![在这里插入图片描述](Prometheus/8a27432d1cea410e17dab98072b92e7b.png)

Prometheus\(由go语言\(golang\)开发\)是一开源的监控\&报警\&时间序列数据库的组合。 适合监控docker容器。因为kubernetes\(俗称k8s\)的流行带动了 prometheus的发展。

官方网站：<https://prometheus.io/>

---

## 二、时间序列数据

### 2.1 什么是序列数据

![在这里插入图片描述](Prometheus/65106d6260656741717a75c923add6b5.png)

时间序列数据（TimeSeries Data）：按照时间顺序记录系统，设备状态变化的数据被称为时序数据。

应用的场景很多，如：

- 无人驾驶车辆运行中要记录的经度,纬度,速度,方向,旁边物体的距离等等。每时每刻都要将数据记录下来做分析。
- 某一个地区的各车辆的行驶轨迹数据
- 传统证券行业实时交易数据
- 实时运维监控数据等

---

### 2.2 时间序列数据特点

● 性能好  
关系型数据库对于大规模数据的处理性能糟糕。NOSQL 可以比较好的处理大规模数据，让依然比不上时间序列数据库。

● 存储成本低  
高效的压缩算法,节省存储空间,有效降低I0

Prometheus有着非常高效的时间序列数据存储方法，每个采样数据仅仅占用3.5byte左右空间，上百万条时间序列, 30秒间隔,保留60天,大概花了200多G \(来自官方数据\)

---

### 2.3 普罗米修斯特征

- 多维度数据模型
- 灵活的查询语言
- 不依赖分布式存储,单个服务器节点是自主的
- 以HTTP方式,通过pull模型拉去时间序列数据
- 也可以通过中间网关支持push模型
- 通过服务发现或者静态配置,来发现目标服务对象
- 支持多种多样的图表和界面展示

---

## 三、普罗米修斯原理架构图

![在这里插入图片描述](Prometheus/45e931b8511caf440c8415b0136e88dd.png)  
\_\_\_\_Prometheus 直接或通过中介推送网关从检测的作业中抓取指标，用于短期作业。它将所有抓取的样本存储在本地，并对这些数据运行规则，以从现有数据聚合和记录新的时间序列或生成警报。Grafana或其他 API 使用者可用于可视化收集的数据。

---

## 四、普罗米修斯监控系统的部署

### 4.1 环境准备工作

服务器准备

| 服务器类型       | 系统和IP地址                     | 备注                    |
| ---------------- | -------------------------------- | ----------------------- |
| Prometheus服务器 | CentOS7.4\(64 位\) 192.168.80.10 | Prometheus              |
| grafana服务器    | CentOS7.4\(64 位\) 192.168.80.20 | mariadb、node\_exporter |
| 被监控服务器     | CentOS7.4\(64 位\) 192.168.80.30 | Grafana                 |

所有服务器关闭防火墙和SElinux

```handlebars
systemctl stop firewalld
setenforce 0
```

修改/etc/hosts

```handlebars
192.168.80.10 prometheus
192.168.80.20 grafana
192.168.80.30 client
```

---

### 4.2 普罗米修斯的部署

1.设置时间同步  
① 所有服务器安装`ntpdate`

```handlebars
yum install ntpdate -y
```

![在这里插入图片描述](Prometheus/d23719006d7ff13f04f79f1edfbf5955.png)  
② 进行时间同步

```handlebars
ntpdate cn.ntp.org.cn
date
```

![在这里插入图片描述](Prometheus/d15e4bef5d407fb9646861edbe77bf79.png)  
2.安装普罗米修斯  
下载地址：<https://prometheus.io/download/> 。我这边之前下载过，直接传入就可以了。  
① 传入安装包并解压

```handlebars
cd /opt/
rz -E				##传入安装包
tar zxvf prometheus-2.29.1.linux-amd64.tar.gz
```

![在这里插入图片描述](Prometheus/983f863b830f7efe5a299f57b610e742.png)  
② 移动并修改名

```handlebars
mv prometheus-2.29.1.linux-amd64 /usr/local/promethues
cd /usr/local/promethues/
```

![在这里插入图片描述](Prometheus/868f6249939c6211bace69ccfda5ecef.png)  
③ 后台启动普罗米修斯

```handlebars
cd /usr/local/promethues/
./prometheus --config.file="/usr/local/promethues/prometheus.yml" &

netstat -napt | grep prometheus			#查看服务是否启动 端口为9090
或
lsof -i:9090
```

![在这里插入图片描述](Prometheus/69124597ff098d82daebad925fc43d25.png)  
④ 浏览器中进行访问测试

```xml
http://192.168.80.10:9090
```

![在这里插入图片描述](Prometheus/60c1881aa777ed8232dcb610993e2084.png)

⑤ 查看 Status 中的 Targets 项。可以看到默认监控的 localhost 也就是本机。  
![在这里插入图片描述](Prometheus/b184d9eb6b1ca274e706024fc57faedb.png)

---

![在这里插入图片描述](Prometheus/87fb2ee2665ab5b18c602b1cbcf87af1.png)  
⑥ 监控接口：通过http://服务器IP:9090/metrics可以查看监控的数据  
![在这里插入图片描述](Prometheus/a606e41e9999673212399a102dd25c7f.png)

### 4.3 普罗米修斯的 web 页面操作

① 查看cpu的运行状态（首页搜索栏中输入process\_cpu\_seconds\_total）  
![在这里插入图片描述](Prometheus/ff6bc19239b794fd41b25aedd3a19086.png)  
② 点击 Graph 选项  
![在这里插入图片描述](Prometheus/a1b07f0787ff60c4c476c3b94b8aa7a3.png)

### 4.4 监控远程 Linux 主机

① 在远程linux主机（被监控端agent1）上安装 node\_exporter 组件，我这边直接上传。  
官网下载地址为：<https://github.com/prometheus/node_exporter/releases/download/v1.2.2/node_exporter-1.2.2.linux-amd64.tar.gz>

```handlebars
cd /opt/
tar zxvf node_exporter-1.2.2.linux-amd64.tar.gz -C /usr/local/
cd /usr/local/
mv node_exporter-1.2.2.linux-amd64/ node_exporter
```

![在这里插入图片描述](Prometheus/fc4be6c8dcd8164e12ffefc51d876398.png)  
② 启动 node\_exporter 程序

```handlebars
cd node_exporter/
nohup /usr/local/node_exporter/node_exporter &		#永久运行并在后台运行
netstat -natp | grep :9100							#查看端口占用情况  
```

扩展: nohup命令:如果把启动node\_exporter的终端给关闭，那么进程也会随之关闭。nohup命令会帮你解决这个问题。

![在这里插入图片描述](Prometheus/3f42f44ed569d00203f1c0e8199ac25e.png)  
③ 使用http协议+9100端口收集Linux主机信息  
通过浏览器访问：http://192.168.80.30:9100/metrics 就可以看到 node\_exporter 在被监控端主机的监控信息  
![在这里插入图片描述](Prometheus/a4414e7c5fc352b21534679a428b1bfe.png)  
④ 在 Prometheus 服务器的配置文件中添加被监控机器的配置段

```handlebars
vim /usr/local/promethues/prometheus.yml

##添加以下配置项
  - job_name: "agent1"
    static_configs:
      - targets: ["192.168.80.30:9100"]
```

![在这里插入图片描述](Prometheus/b6dceddb2614be611ea03ac7d3ad34dd.png)  
⑤ 重新启动prometheus监控系统

```handlebars
pkill prometheus
lsof -i:9090
cd /usr/local/promethues/
./prometheus --config.file="/usr/local/promethues/prometheus.yml" &
lsof -i:9090
```

![在这里插入图片描述](Prometheus/1b47913eda060a460ba7da5478a83199.png)

⑥ 重新回到普罗米修斯web端查看 Status 中的 Targets 项，可以看到被监控端 node\_exporter 了，并且状态为 up。  
![在这里插入图片描述](Prometheus/1f357972c0799e2e82d605683117a8dd.png)

### 4.5 远程监控 MySQL 数据库

① 在被监控端服务器上安装 mysqld\_exporter 组件，我这边直接上传。  
官方下载地址为：<https://github.com/prometheus/mysqld_exporter/releases/download/v0.13.0/mysqld_exporter-0.13.0.linux-amd64.tar.gz>

```handlebars
cd /opt/
tar zxvf mysqld_exporter-0.13.0.linux-amd64.tar.gz -C /usr/local/
cd /usr/local/
mv mysqld_exporter-0.13.0.linux-amd64/ mysqld_exporter
```

![在这里插入图片描述](Prometheus/e4721ed6eaab86597682c6fa2e44fef0.png)  
② 节省时间安装 mariadb 数据库

```handlebars
yum install mariadb\* -y
```

![在这里插入图片描述](Prometheus/c0b0ad120a6b9968b555d26b2abd045c.png)  
③ 开启并设置为开机自启，然后登录 mysql  
![在这里插入图片描述](Prometheus/d3c4b53e1c5eddc08d208a004b519530.png)  
④ 创建 mysql 账号帮刷新权限用于收集数据

```handlebars
grant select,replication client,process ON *.* to 'mysql_monitor'@'localhost' identified by 'abc123';
账号为：mysql_monitor
密码为：abc123

flush privileges;
exit
```

![在这里插入图片描述](Prometheus/5562f9704f604dd2027adbbcc7099f79.png)

⑤ 在mysql\_exporter 组件中创建配置文件，配置 mysql 信息

```handlebars
vim /usr/local/mysqld_exporter/.my.cnf

[client]
user=mysql_monitor
password=abc123
```

![在这里插入图片描述](Prometheus/08eb746852e4189194906829f8e7d03b.png)  
⑥ 启动 mysql\_exporter 组件

```handlebars
cd mysqld_exporter/
./mysqld_exporter --config.my-cnf=/usr/local/mysqld_exporter/.my.cnf &
```

![在这里插入图片描述](Prometheus/2db1626d4db15376d72fe3c6db0d4deb.png)  
⑦ 回到 prometheus 服务器的配置文件里添加被监控的 mariadb 的配置段

```handlebars
vim /usr/local/promethues/prometheus.yml
## 添加下面三行
  - job_name: 'agent1_mariadb'
    static_configs:
      - targets: ['192.168.80.30:9104']
```

![在这里插入图片描述](Prometheus/7b2d54dc1a7465fe1c98b3d30929f4ce.png)

⑧ 改完配置文件后,重启服务

```handlebars
pkill prometheus
lsof -i:9090
./prometheus --config.file="/usr/local/promethues/prometheus.yml" &
lsof -i:9090
```

![在这里插入图片描述](Prometheus/f317cc8fa275370bbbf032d18f0882f7.png)  
⑨ 回到web管理界面 \--》点Status \--》点Targets \--》可以看到监控mariadb了  
![在这里插入图片描述](Prometheus/b855d21ea51bdd22d4a3dbadd371ce98.png)

⑩ 查看 mysql 线程连接  
搜索栏中输入：mysql\_global\_status\_threads\_connected 进行查看

![在这里插入图片描述](Prometheus/50e38142824c9ccd3b32117adebc7657.png)

---

## 五、Grafana可视化图形工具

### 5.1 什么是Grafana

![在这里插入图片描述](Prometheus/2faf073a5e1555d3a5e87e802e0768b1.png)

Grafana是一个开源的度量分析和可视化工具，可以通过将采集的数据分析，查询，然后进行可视化的展示,并能实现报警。

### 5.2 使用Grafana连接Prometheus

① 在grafana服务器上安装 grafana，我这边之前下载过，直接上传  
官方下载地址:<https://grafana.com/grafana/download>

```handlebars
cd /opt/
rz -E
rpm -ivh grafana-5.3.4-1.x86_64.rpm 
systemctl start grafana-server
systemctl enable grafana-server
lsof -i:3000
```

![在这里插入图片描述](Prometheus/c1494863bbdea877253f3f7d24addd1b.png)  
② 通过浏览器访问 http:// grafana服务器IP:3000就到了登录界面,使用默认的admin用户,admin密码就可以登陆了  
![在这里插入图片描述](Prometheus/1d8e2cb2b791f98b1e3c803841c382f7.png)

③ 重新修改密码为：abc123  
![在这里插入图片描述](Prometheus/482f016f369f7c2b82b3f756434e255b.png)  
④ 把 prometheus 服务器收集的数据做为一个数据源添加到 grafana,让 grafana 可以得到 prometheus 的数据  
![在这里插入图片描述](Prometheus/f0fc73ee1bdace9cd8aa376abc07c4f8.png)  
![在这里插入图片描述](Prometheus/511d3b998666448c7dd941e681be3ff7.png)

```xml
部分选项解释
Auth项：公网传输数据加密与验证用的，我们这边暂时不需要设置
Advanced HTTP Settings项：15秒获取一次数据，GET方式
```

![在这里插入图片描述](Prometheus/3e0702ef03b5144c8fcee9b8ef44b453.png)  
⑤ 为添加好的数据源做图形显示（查看被监控端1分钟、5分钟和15分钟的负载情况）  
![在这里插入图片描述](Prometheus/6a7fc803286bef3e4afaea26f5c1e1ba.png)  
![在这里插入图片描述](Prometheus/a261737370526e2e72c36d22cd0d9c7a.png)  
![在这里插入图片描述](Prometheus/dd9c4fcd026fdc211455be7a0bddac10.png)  
![在这里插入图片描述](Prometheus/5cfdf5b6d322b8a84a0dd263bbe7efcb.png)  
![在这里插入图片描述](Prometheus/2a0b64a5001bbc38f288c296bac41737.png)  
![在这里插入图片描述](Prometheus/cbe512b82abe088a8ef01ab72eb7279e.png)  
⑥ 匹配条件显示（根据IP或普罗米修斯里面设置的job）  
![在这里插入图片描述](Prometheus/09d4930e594eef5a88d8b1781ec1d56e.png)

### 5.3 Grafana图形显示MySQL监控数据

① 在 grafana 上修改配置文件,并下载安装mysql监控的 dashboard（包含相关json文件，这些json文件可以看作是开发人员开发的一个监控模板\)，我这边直接上传。  
官方下载网址: <https://github.com/percona/grafana-dashboards>

```handlebars
vim /etc/grafana/grafana.ini

[dashboards.json] 
enabled = true 
path = /var/lib/grafana/dashboards


git clone https://github.com/percona/grafana-dashboards.git
```

![在这里插入图片描述](Prometheus/9020b36bedee9c21fbf8ab01b0ae42c7.png)

② 下载json模板并上传到 grafana 服务器后解压缩  
参考网址: <https://github.com/percona/grafana-dashboards>  
![在这里插入图片描述](Prometheus/b49d96e3323e3b57a68084d461c6f1c2.png)

```handlebars
unzip grafana-dashboards-main.zip
```

![在这里插入图片描述](Prometheus/067534440924b216c33090f55b33a47d.png)

③ 复制到/var/lib/grafana目录下并改名，然后重启服务

```handlebars
cp -r grafana-dashboards-1.17.4/ /usr/lib/grafana/
systemctl restart grafana-server.service 
```

![在这里插入图片描述](Prometheus/fee03c97645d89579614acbbf15ba516.png)  
![在这里插入图片描述](Prometheus/b3df6b97417ae84e90a27be7cceae035.png)

④ 把下载压缩包在本地解压  
![在这里插入图片描述](Prometheus/4b3f9c380dd5c20a941c979341a192b9.png)  
⑤ 在grafana图形界面导入相关json文件  
![在这里插入图片描述](Prometheus/a4c18cc73907075a7f5440909a479105.png)  
![在这里插入图片描述](Prometheus/3c05667b992b3b379e4e72924c38987e.png)

![在这里插入图片描述](Prometheus/0792c6dda4b20fb429cf9d7e4e997482.png)

⑥ 点import导入后,报prometheus数据源找不到,因为这些json文件里默认要找的就是叫Prometheus的数据源，但我们前面建立的数据源却是叫 prometheus\_data ，修改之前数据源的名称即可。

![在这里插入图片描述](Prometheus/80f1ddb331731dbbc790e16e241145fe.png)  
⑦ 修改完数据源名称后再次查看  
![在这里插入图片描述](Prometheus/596aed5248fb7c80d17d0b33fb2445f7.png)  
![在这里插入图片描述](Prometheus/b137bdeb471003e3b8453629a7489b73.png)  
⑧ 数据展示完成（过段时间再看，加载完成就会数据显示了）  
![在这里插入图片描述](Prometheus/af1af9501536b8326bd4533f0c17995c.png)

## 六、 Grafana+onealert报警

prometheus报警需要使用alertmanager这个组件，而且报警规则需要手动编写\(对运维来说不友好\)。所以我这里选用grafana+onealert报警。  
注意: 实现报警前把所有机器时间同步再检查一遍.

再次所有服务器同步时间  
![在这里插入图片描述](Prometheus/c867e0da8c719c95a02d2ba93c7591be.png)

### 6.1 注册账号并获取key

② 先在onealert里添加grafana应用，需要先申请onealert账号。  
官方网站：<https://caweb.aiops.com/#/>  
![在这里插入图片描述](Prometheus/4368fc4cdae8f51c5eb812523f02cc2b.png)

③ 在新建应用中找到 grafana 应用 ，填好名字后保存获取应用key

![在这里插入图片描述](Prometheus/d9a11eca9789effb5308ad76b211c8ed.png)  
然后可以看到配置步骤  
![在这里插入图片描述](Prometheus/2c124b8f81009e263a8ba9a74bf6a8a4.png)

### 6.2 配置通知策略

① 在 Grafana 中创建 Notification channel，选择类型为 Webhook；  
a. 将第一步中生成的Webhook URL填入Webhook settings Url；

```xml
URL格式：
http://api.aiops.com/alert/api/event/grafana/v1/4065f3ce446841ca859d41c675286833/
```

b. Http Method选择POST；  
c. Send Test\&Save；

![在这里插入图片描述](Prometheus/240617a49d0a2f9e1df6b27f0ba22cc3.png)  
![在这里插入图片描述](Prometheus/34f93121d28a43f2aa22b7b6efe0910a.png)

### 6.3 将配置的Webhook Notification Channel添加到Grafana Alert中

① 选择我们之前创建的cpu监控，点击Edit进行编辑  
![在这里插入图片描述](Prometheus/38c521fc76f5d2bdd08b4ad17e196d0b.png)  
② 填写name 和 IS above  
![在这里插入图片描述](Prometheus/937a94dc9216b7588933b5c25308e649.png)  
③ 现在发送的对象和发送的信息  
![在这里插入图片描述](Prometheus/5ca7c8a6644183b354a25d1538643834.png)  
④ 保存并自定义个名字  
![在这里插入图片描述](Prometheus/f07a34f6659126327031f477889195bb.png)

### 6.4 CPU告警测试

⑤ 现在可以去设置一个报警来测试了\(这里以我们前面加的cpu负载监控来做测试\)  
在被监控端的agent执行测试命令`cat /dev/urandom | md5sum`  
![在这里插入图片描述](Prometheus/12d7feb2b98c25351c9b81796233fbfc.png)

⑥ 等待一会，可以看到grafana监控页面CPU负载超过0.5了  
![在这里插入图片描述](Prometheus/cd7184d8cc510f3eff96333cc3683ff3.png)  
⑦ 这个时候会收到邮件提示  
![在这里插入图片描述](Prometheus/601dfbcb552b798bd2d2b4aa237c8b06.png)

## 七、总结报警不成功的可能原因

1.  各服务器之间时间不同步，这样时序数据会出问题，也会造成报警出问题
2.  必须写通知内容，留空内容是不会发报警的
3.  修改完报警配置后，记得要点右上角的保存
4.  保存配置后，需要由OK状态变为alerting状态才会报警\(也就是说，你配置保存后，就已经是alerting状态是不会报警的\)
5.  grafana与onealert通信有问题