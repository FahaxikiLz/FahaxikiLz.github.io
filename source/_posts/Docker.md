---
title: Docker
date: 2022-12-15 13:11:24
tags: 
- Docker
categories: 
- 容器
---

# Docker概述

> 官网地址：[www.docker.com](https://www.docker.com/)
>
> 文档地址：[Docker Documentation | Docker Documentation](https://docs.docker.com/)
>
> 仓库地址：[Docker Hub](https://hub.docker.com/)

## Docker为什么会出现？

> - 环境配置是十分的麻烦，每一个机器都要部署环境（集群Redis、ES、Hadoop....），费时费力。
> - 发布一个项目( jar + ( Redis MySQL jdk ES ) )，项目能不能都带上环境安装打包!
> - java --- jar (环境) --- 打包项目带上环境（镜像) --- ( Docker仓库∶商店) --- 下载我们发布的镜像 --- 直接运行即可!
> - 隔离:Docker核心思想!打包装箱!每个箱子是互相隔离的。

### 虚拟技术

> 1. 不用的应用 共用 同一个 后台库 + 内核，比较庞大
> 2. 资源占用十分多
> 3. 冗余步骤多
> 4. 启动很慢！

<img src="Docker/image-20221215142733777.png" alt="image-20221215142733777" style="zoom:67%;" />

### 容器化技术

> 不同的 （应用 + 本应用运用到的后台库） 封装到同一个容器里 ，运行在同一个内核上
>
> - 应用更快速的交付和部署
>
>   传统：一堆帮助文档，安装程序!
>   Docker：打包镜像发布测试，一键运行!
>
> - 更便捷的升级和扩缩容
>
>   使用了Docker之后，我们部署应用就和搭积木一样！
>   项目打包为一个镜像，扩展服务器A！服务器B!
>
> - 更简单的系统运维
>
>   在容器化之后，我们的开发，测试环境都是高度一致的。
>
> - 更高效的计算资源利用
>
>   Docker是内核级别的虚拟化，可以在一个物理机上可以运行很多的容器实例！服务器的性能可以被压榨到极致。

<img src="Docker/image-20221215142819457.png" alt="image-20221215142819457" style="zoom:67%;" />

#### 对比

> - 传统虚拟机，虚拟出一条硬件，运行一个完整的操作系统，然后在这个系统上安装和运行软件。
> - 容器内的应用直接运行在宿主机的内核中，容器是没有自己的内核的，也没有虚拟我们的硬件，所以就轻便了。
> - 每个容器间是互相隔离，每个容器内都有一个属于自己的文件系统，互不影响。

# Docker安装

## Docker的基本组成

<img src="Docker/image-20221215150657323.png" alt="image-20221215150657323" style="zoom:67%;" />

> **镜像（ image）**
>
> docker镜像就好比是一个模板，可以通过这个模板来创建容器服务， 通过这个镜像可以创建多个容器（最终服务运行或者项目运行就是在容器中的），镜像没有启动，停止 这些操作
>
> **容器（ container)**
>
> Docker利用容器技术，独立运行一个或者一个组应用，通过镜像来创建的。
> 启动，停止，刪除，基本命令！
> 目前就可以把这个容器理解为就是一个简易的inux系统
>
> **仓库（ repository）**
>
> 仓库就是存放镜像的地方！
> 仓库分为公有仓库和私有仓库！
> Docker Hub(默认是国外的)
> 阿里云 腾讯云 …都有容器服务器(配置镜像加速！）

## 安装

### 1.查看宿主系统

> Docker要求宿主机系统的内核版本高于 3.10 

```shell
[root@lz ~]# uname -r
3.10.0-514.el7.x86_64

[root@lz ~]# cat /etc/os-release
NAME="CentOS Linux"
VERSION="7 (Core)"
ID="centos"
ID_LIKE="rhel fedora"
VERSION_ID="7"
PRETTY_NAME="CentOS Linux 7 (Core)"
ANSI_COLOR="0;31"
CPE_NAME="cpe:/o:centos:centos:7"
HOME_URL="https://www.centos.org/"
BUG_REPORT_URL="https://bugs.centos.org/"

CENTOS_MANTISBT_PROJECT="CentOS-7"
CENTOS_MANTISBT_PROJECT_VERSION="7"
REDHAT_SUPPORT_PRODUCT="centos"
REDHAT_SUPPORT_PRODUCT_VERSION="7"
```

### 2.卸载旧版本

```shell
yum remove docker
```

### 3.需要的安装包

```shell
yum install -y yum-utils
```

### 4.设置镜像

```shell
yum-config-manager —add-repo https://download.docker.com/linux/centos/docker-ce.repo # 默认是从国外的。

yum-config-manager --add-repo   http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo # 推荐使用阿里云的。
```

### 5.更新yum软件包索引

```shell
yum makecache fast
```

### 6.安装Docker

> docker-ce（社区版）docker-ee（企业版）

```shell
yum install docker-ce docker-ce-cli containerd.io
```

### 7.启动Docker

```shell
systemctl start docker
```

### 8.查看版本

```shell
docker version
```

### 9.测试hello-world

```
docker run hello-world
```

<img src="Docker/image-20221215162324883.png" alt="image-20221215162324883" style="zoom:67%;" />

<img src="Docker/image-20221215162533475.png" alt="image-20221215162533475" style="zoom:55%;" />

### 10.查看镜像

```shell
[root@lz ~]# docker images
REPOSITORY    TAG       IMAGE ID       CREATED         SIZE
hello-world   latest    feb5d9fea6a5   14 months ago   13.3kB
```

### 11.卸载Docker

```shell
# 卸载依赖
yum remove docker-ce docker-ce-cli containerd.io
# 删除资源：
rm -rf /var/lib/docker
```

## 阿里云镜像加速

> 1. 登录阿里云，找到容器服务。
> 2. 找到镜像加速地址。
> 3. 配置使用。

## 底层原理

### Docker是怎么工作的？

> Docker 是一个Client-Server结构的系统，Docker的守护进程运行在主机上。通过Socket从客户端访问！
> DockerServer 接收到Docker-Client的指令，就会执行这个命令！

![image-20221215164005436](Docker/image-20221215164005436.png)

### Docker为什么比VM快？

> 1. Docker有着比虚拟机更少的抽象层。
> 2. docker利用的是宿主机的内核，vm需要是Guest OS。
> 3. 新建一个容器的时候，docker不需要像虚拟机一样重新加载一个操作系统内核，避免引导。
>    虚拟机是加载GuestOS，分钟级别的，而docker是利用宿主机的操作系统，省略了这个复杂的过程，秒级！

<img src="Docker/image-20221215164107426.png" alt="image-20221215164107426" style="zoom:80%;" />

# Docker的常用命令

## 帮助命令

> 帮助文档的地址：[Reference documentation | Docker Documentation](https://docs.docker.com/reference/)

```
docker version        # 显示docker的版本信息
docker info              # 显示docker的系统信息，包括镜像和容器的数量
docker 命令 --help         # 帮助命令
```

```shell
[root@lz ~]# docker version
Client: Docker Engine - Community
 Version:           20.10.21
 API version:       1.41
 Go version:        go1.18.7
 Git commit:        baeda1f
 Built:             Tue Oct 25 18:04:24 2022
 OS/Arch:           linux/amd64
 Context:           default
 Experimental:      true

Server: Docker Engine - Community
 Engine:
  Version:          20.10.21
  API version:      1.41 (minimum version 1.12)
  Go version:       go1.18.7
  Git commit:       3056208
  Built:            Tue Oct 25 18:02:38 2022
  OS/Arch:          linux/amd64
  Experimental:     false
 containerd:
  Version:          1.6.12
  GitCommit:        a05d175400b1145e5e6a735a6710579d181e7fb0
 runc:
  Version:          1.1.4
  GitCommit:        v1.1.4-0-g5fd4c4d
 docker-init:
  Version:          0.19.0
  GitCommit:        de40ad0
[root@lz ~]# docker info
Client:
 Context:    default
 Debug Mode: false
 Plugins:
  app: Docker App (Docker Inc., v0.9.1-beta3)
  buildx: Docker Buildx (Docker Inc., v0.9.1-docker)
  scan: Docker Scan (Docker Inc., v0.21.0)

Server:
 Containers: 1
  Running: 0
  Paused: 0
  Stopped: 1
 Images: 1
 Server Version: 20.10.21
 Storage Driver: overlay2
  Backing Filesystem: xfs
  Supports d_type: true
  Native Overlay Diff: false
  userxattr: false
 Logging Driver: json-file
 Cgroup Driver: cgroupfs
 Cgroup Version: 1
 Plugins:
  Volume: local
  Network: bridge host ipvlan macvlan null overlay
  Log: awslogs fluentd gcplogs gelf journald json-file local logentries splunk syslog
 Swarm: inactive
 Runtimes: io.containerd.runc.v2 io.containerd.runtime.v1.linux runc
 Default Runtime: runc
 Init Binary: docker-init
 containerd version: a05d175400b1145e5e6a735a6710579d181e7fb0
 runc version: v1.1.4-0-g5fd4c4d
 init version: de40ad0
 Security Options:
  seccomp
   Profile: default
 Kernel Version: 3.10.0-514.el7.x86_64
 Operating System: CentOS Linux 7 (Core)
 OSType: linux
 Architecture: x86_64
 CPUs: 4
 Total Memory: 7.624GiB
 Name: lz
 ID: JIAG:DHCU:EBLG:ZXFY:SBMW:LPSW:YTXP:DLXM:E4WS:O2DX:DNBN:EWFS
 Docker Root Dir: /var/lib/docker
 Debug Mode: false
 Registry: https://index.docker.io/v1/
 Labels:
 Experimental: false
 Insecure Registries:
  127.0.0.0/8
 Live Restore Enabled: false

[root@lz ~]# docker info --help

Usage:  docker info [OPTIONS]

Display system-wide information

Options:
  -f, --format string   Format the output using the given Go template
```

## 镜像命令

### dokcer images

> 查看所有本地的主机上的镜像

```shell
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker images
REPOSITORY    TAG       IMAGE ID       CREATED         SIZE
hello-world   latest    feb5d9fea6a5   10 months ago   13.3kB
# 解释
REPOSITORY    镜像的仓库源
TAG            镜像的标签
IMAGE ID    镜像的id
CREATED        镜像的创建时间
SIZE        镜像的大小
# 命令参数可选项
 -a, --all         # 显示所有镜像 (docker images -a)
 -q, --quiet       # 仅显示镜像id (docker images -q)
```

### docker search

> - 搜索镜像
> - 还可以通过[Docker Hub](https://hub.docker.com/)搜索镜像

```shell
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker search mysql
NAME                           DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
mysql                          MySQL is a widely used, open-source relation…   12940     [OK]
mariadb                        MariaDB Server is a high performing open sou…   4957      [OK]
phpmyadmin                     phpMyAdmin - A web interface for MySQL and M…   587       [OK]
percona                        Percona Server is a fork of the MySQL relati…   582       [OK]
# 解释
# 命令参数可选项 (通过搜索来过滤)
--filter=STARS=3000     # 搜索出来的镜像就是stars大于3000的
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker search mysql --filter=STARS=3000
NAME      DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
mysql     MySQL is a widely used, open-source relation…   12941     [OK]
mariadb   MariaDB Server is a high performing open sou…   4957      [OK]
```

### docker pull

> 下载镜像

```shell
# 下载镜像：docker pull 镜像名[:tag]
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker pull mysql
Using default tag: latest            # 如果不写tag，默认就是latest，最新的版本
latest: Pulling from library/mysql
72a69066d2fe: Pull complete            # 分层下载，docker image的核心，联合文件下载
93619dbc5b36: Pull complete
99da31dd6142: Pull complete
626033c43d70: Pull complete
37d5d7efb64e: Pull complete
ac563158d721: Pull complete
d2ba16033dad: Pull complete
688ba7d5c01a: Pull complete
00e060b6d11d: Pull complete
1c04857f594f: Pull complete
4d7cfa90e6ea: Pull complete
e0431212d27d: Pull complete
Digest: sha256:e9027fe4d91c0153429607251656806cc784e914937271037f7738bd5b8e7709 #签名
Status: Downloaded newer image for mysql:latest
docker.io/library/mysql:latest        # 真实地址
# 两个命令是等价的
docker pull mysql
docker pull docker.io/library/mysql:latest
# 指定版本下载
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker pull mysql:5.7
5.7: Pulling from library/mysql
72a69066d2fe: Already exists        # 联合文件下载，已经存在的资源可以共用
93619dbc5b36: Already exists
99da31dd6142: Already exists
626033c43d70: Already exists
37d5d7efb64e: Already exists
ac563158d721: Already exists
d2ba16033dad: Already exists
0ceb82207cd7: Pull complete
37f2405cae96: Pull complete
e2482e017e53: Pull complete
70deed891d42: Pull complete
Digest: sha256:f2ad209efe9c67104167fc609cca6973c8422939491c9345270175a300419f94
Status: Downloaded newer image for mysql:5.7
docker.io/library/mysql:5.7
```

### docker rmi

> 删除镜像

```shell
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker rmi -f 镜像id                    # 删除指定的镜像
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker rmi -f 镜像id 镜像id 镜像id    # 删除多个镜像（空格分隔）
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker rmi -f $(docker images -aq)    # 删除全部的镜像
```

## 容器命令

> 说明：我们有了镜像才可以创建容器，linux，下载一个centos 镜像来测试学习。

```shell
docker pull centos
```

### 新建容器并启动

```shell
docker run [可选参数] image
# 参数说明
--name="name"         容器名字：用来区分容器
-d                    后台方式运行：相当于nohup
-it                   使用交互式运行：进入容器查看内容
-p                    指定容器的端口（四种方式）小写字母p
    -p ip:主机端口：容器端口
    -p 主机端口：容器端口
    -p 容器端口
    容器端口
-P                     随机指定端口（大写字母P）

# 测试：启动并进入容器
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker run -it centos /bin/bash
[root@526c31d2c298 /]# ls        # 查看容器内的centos（基础版本，很多命令都是不完善的）
bin  dev  etc  home  lib  lib64  lost+found  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var

# 从容器中退回到主机
[root@526c31d2c298 /]# exit
exit
[root@iZbp13qr3mm4ucsjumrlgqZ /]# ls
bin  boot  dev  etc  home  lib  lib64  lost+found  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var  www
```

### 列出所有运行的容器

```shell
docker ps    # 列出当前正在运行的容器
# 命令参数可选项
-a        # 列出当前正在运行的容器+历史运行过的容器
-n=?    # 显示最近创建的容器（可以指定显示几条，比如-n=1）
-q        # 只显示容器的编号

[root@iZbp13qr3mm4ucsjumrlgqZ /]# docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
[root@iZbp13qr3mm4ucsjumrlgqZ /]# docker ps -a
CONTAINER ID   IMAGE          COMMAND       CREATED         STATUS                     PORTS     NAMES
526c31d2c298   centos         "/bin/bash"   4 minutes ago   Exited (0) 2 minutes ago             optimistic_allen
ce0eb11fbf8a   feb5d9fea6a5   "/hello"      4 hours ago     Exited (0) 4 hours ago               keen_ellis
[root@iZbp13qr3mm4ucsjumrlgqZ /]# docker ps -a -n=1
CONTAINER ID   IMAGE     COMMAND       CREATED         STATUS                     PORTS     NAMES
526c31d2c298   centos    "/bin/bash"   5 minutes ago   Exited (0) 3 minutes ago             optimistic_allen
```

### 退出容器

```shell
exit        # 容器直接停止，并退出
ctrl+P+Q    # 容器不停止，退出
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker run -it centos /bin/bash
[root@c5d61aa9d7df /]# [root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker ps
CONTAINER ID   IMAGE     COMMAND       CREATED          STATUS          PORTS     NAMES
c5d61aa9d7df   centos    "/bin/bash"   56 seconds ago   Up 55 seconds             kind_clarke
[root@iZbp13qr3mm4ucsjumrlgqZ ~]#
```

### 删除容器

```shell
docker rm 容器id                    # 删除容器（不能删除正在运行的容器）如果要强制删除：docker rm -f 容器id
docker rm -f $(docker ps -aq)        # 删除全部容器
docker ps -a -q|xargs docker rm        # 删除所有容器
```

### 启动和停止容器的操作

```shell
docker start 容器id        # 启动容器
docker restart 容器id    # 重启容器
docker stop 容器id        # 停止当前正在运行的容器
docker kill 容器id        # 强制停止当前容器
```

## 常用其他命令

### 后台启动容器

```shell
# 命令docker run -d 镜像名
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker run -d centos

# 问题：docker ps发现centos停止了

# 常见的坑：docker容器使用后台运行，就必须要有要一个前台进程，docker发现没有应用，就会自动停止。

# 比如：nginx，容器启动后，发现自己没有提供服务，就会立刻停止，就是没有程序了
```

### 查看日志

```shell
# docker logs -tf --tail 容器id
# 自己编写一段shell脚本
# docker run -d centos /bin/sh -c "while true;do echo kuangshen;sleep 1;done"

[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker logs -tf  容器id
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker logs -tf --tail 10  容器id

# 显示日志
-tf              # 显示日志
--tail number    # 要显示的日志条数
```

### 查看容器中进程的信息

```shell
# 命令 docker top 容器id 
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker top 88d23bcbe1f2
UID                 PID                 PPID                C                   STIME               TTY                 TIME                CMD
root                21212               21193               0                   16:23               ?                   00:00:00            
root                21600               21212               0                   16:29               ?                   00:00:00
```

### 查看镜像的元数据

```shell
# 命令docker inspect 容器id
[
    {
        "Id": "88d23bcbe1f28e1f8ae5d2b63fa8d57d2abcbdacf193db05852f4f74a95b9ffe",
        "Created": "2022-07-29T08:23:56.862239223Z",
        "Path": "/bin/sh",
        "Args": [
            "-c",
            "while true;do echo kuangshen;sleep 1;done"
        ],
        "State": {
            "Status": "running",
            "Running": true,
            "Paused": false,
            "Restarting": false,
            "OOMKilled": false,
            "Dead": false,
            "Pid": 21212,
            "ExitCode": 0,
            "Error": "",
            "StartedAt": "2022-07-29T08:23:57.109766809Z",
            "FinishedAt": "0001-01-01T00:00:00Z"
        },
        "Image": "sha256:5d0da3dc976460b72c77d94c8a1ad043720b0416bfc16c52c45d4847e53fadb6",
        "ResolvConfPath": "/var/lib/docker/containers/88d23bcbe1f28e1f8ae5d2b63fa8d57d2abcbdacf193db05852f4f74a95b9ffe/resolv.conf",
        "HostnamePath": "/var/lib/docker/containers/88d23bcbe1f28e1f8ae5d2b63fa8d57d2abcbdacf193db05852f4f74a95b9ffe/hostname",
        "HostsPath": "/var/lib/docker/containers/88d23bcbe1f28e1f8ae5d2b63fa8d57d2abcbdacf193db05852f4f74a95b9ffe/hosts",
        "LogPath": "/var/lib/docker/containers/88d23bcbe1f28e1f8ae5d2b63fa8d57d2abcbdacf193db05852f4f74a95b9ffe/88d23bcbe1f28e1f8ae5d2b63fa8d57d2abcbdacf193db05852f4f74a95b9ffe-json.log",
        "Name": "/silly_lichterman",
        "RestartCount": 0,
        "Driver": "overlay2",
        "Platform": "linux",
        "MountLabel": "",
        "ProcessLabel": "",
        "AppArmorProfile": "",
        "ExecIDs": null,
        "HostConfig": {
            "Binds": null,
            "ContainerIDFile": "",
            "LogConfig": {
                "Type": "json-file",
                "Config": {}
            },
            "NetworkMode": "default",
            "PortBindings": {},
            "RestartPolicy": {
                "Name": "no",
                "MaximumRetryCount": 0
            },
            "AutoRemove": false,
            "VolumeDriver": "",
            "VolumesFrom": null,
            "CapAdd": null,
            "CapDrop": null,
            "CgroupnsMode": "host",
            "Dns": [],
            "DnsOptions": [],
            "DnsSearch": [],
            "ExtraHosts": null,
            "GroupAdd": null,
            "IpcMode": "private",
            "Cgroup": "",
            "Links": null,
            "OomScoreAdj": 0,
            "PidMode": "",
            "Privileged": false,
            "PublishAllPorts": false,
            "ReadonlyRootfs": false,
            "SecurityOpt": null,
            "UTSMode": "",
            "UsernsMode": "",
            "ShmSize": 67108864,
            "Runtime": "runc",
            "ConsoleSize": [
                0,
                0
            ],
            "Isolation": "",
            "CpuShares": 0,
            "Memory": 0,
            "NanoCpus": 0,
            "CgroupParent": "",
            "BlkioWeight": 0,
            "BlkioWeightDevice": [],
            "BlkioDeviceReadBps": null,
            "BlkioDeviceWriteBps": null,
            "BlkioDeviceReadIOps": null,
            "BlkioDeviceWriteIOps": null,
            "CpuPeriod": 0,
            "CpuQuota": 0,
            "CpuRealtimePeriod": 0,
            "CpuRealtimeRuntime": 0,
            "CpusetCpus": "",
            "CpusetMems": "",
            "Devices": [],
            "DeviceCgroupRules": null,
            "DeviceRequests": null,
            "KernelMemory": 0,
            "KernelMemoryTCP": 0,
            "MemoryReservation": 0,
            "MemorySwap": 0,
            "MemorySwappiness": null,
            "OomKillDisable": false,
            "PidsLimit": null,
            "Ulimits": null,
            "CpuCount": 0,
            "CpuPercent": 0,
            "IOMaximumIOps": 0,
            "IOMaximumBandwidth": 0,
            "MaskedPaths": [
                "/proc/asound",
                "/proc/acpi",
                "/proc/kcore",
                "/proc/keys",
                "/proc/latency_stats",
                "/proc/timer_list",
                "/proc/timer_stats",
                "/proc/sched_debug",
                "/proc/scsi",
                "/sys/firmware"
            ],
            "ReadonlyPaths": [
                "/proc/bus",
                "/proc/fs",
                "/proc/irq",
                "/proc/sys",
                "/proc/sysrq-trigger"
            ]
        },
        "GraphDriver": {
            "Data": {
                "LowerDir": "/var/lib/docker/overlay2/59b088d44f6e67f4ed336de44d19a5784c1a13fa856760bd2b166c4a4d421e2b-init/diff:/var/lib/docker/overlay2/7fc43e24b63e4656ab7e7718d3e4ef5297fe82509452be305a01605a7cdc3b97/diff",
                "MergedDir": "/var/lib/docker/overlay2/59b088d44f6e67f4ed336de44d19a5784c1a13fa856760bd2b166c4a4d421e2b/merged",
                "UpperDir": "/var/lib/docker/overlay2/59b088d44f6e67f4ed336de44d19a5784c1a13fa856760bd2b166c4a4d421e2b/diff",
                "WorkDir": "/var/lib/docker/overlay2/59b088d44f6e67f4ed336de44d19a5784c1a13fa856760bd2b166c4a4d421e2b/work"
            },
            "Name": "overlay2"
        },
        "Mounts": [],
        "Config": {
            "Hostname": "88d23bcbe1f2",
            "Domainname": "",
            "User": "",
            "AttachStdin": false,
            "AttachStdout": false,
            "AttachStderr": false,
            "Tty": false,
            "OpenStdin": false,
            "StdinOnce": false,
            "Env": [
                "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"
            ],
            "Cmd": [
                "/bin/sh",
                "-c",
                "while true;do echo kuangshen;sleep 1;done"
            ],
            "Image": "centos",
            "Volumes": null,
            "WorkingDir": "",
            "Entrypoint": null,
            "OnBuild": null,
            "Labels": {
                "org.label-schema.build-date": "20210915",
                "org.label-schema.license": "GPLv2",
                "org.label-schema.name": "CentOS Base Image",
                "org.label-schema.schema-version": "1.0",
                "org.label-schema.vendor": "CentOS"
            }
        },
        "NetworkSettings": {
            "Bridge": "",
            "SandboxID": "bfc28d2ba671adfe5c93325173b93c335d50d8b017eed4fdce2ab75410d6ac2e",
            "HairpinMode": false,
            "LinkLocalIPv6Address": "",
            "LinkLocalIPv6PrefixLen": 0,
            "Ports": {},
            "SandboxKey": "/var/run/docker/netns/bfc28d2ba671",
            "SecondaryIPAddresses": null,
            "SecondaryIPv6Addresses": null,
            "EndpointID": "ad0252d454e751e2c5ecef24e64c32b0884c53242925bd66e9e5dbf5542af179",
            "Gateway": "172.17.0.1",
            "GlobalIPv6Address": "",
            "GlobalIPv6PrefixLen": 0,
            "IPAddress": "172.17.0.2",
            "IPPrefixLen": 16,
            "IPv6Gateway": "",
            "MacAddress": "02:42:ac:11:00:02",
            "Networks": {
                "bridge": {
                    "IPAMConfig": null,
                    "Links": null,
                    "Aliases": null,
                    "NetworkID": "7254ffccbdf53e0c72c1d19252980f04fa65153ea3c7ca72af55cfac504cbe3f",
                    "EndpointID": "ad0252d454e751e2c5ecef24e64c32b0884c53242925bd66e9e5dbf5542af179",
                    "Gateway": "172.17.0.1",
                    "IPAddress": "172.17.0.2",
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "MacAddress": "02:42:ac:11:00:02",
                    "DriverOpts": null
                }
            }
        }
    }
]
```

### 进入当前正在运行的容器

```shell
# 我们通常容器都是使用后台方式运行的，需要进入容器，修改一些配置
# 命令
# docker exec -it 容器id /bin/bash

# 测试
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED          STATUS          PORTS     NAMES
88d23bcbe1f2   centos    "/bin/sh -c 'while t…"   13 minutes ago   Up 13 minutes             silly_lichterman
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker exec -it 88d23bcbe1f2 /bin/bash
[root@88d23bcbe1f2 /]# ps -ef
UID        PID  PPID  C STIME TTY          TIME CMD
root         1     0  0 08:23 ?        00:00:00 /bin/sh -c while true;do echo kuangshen;sleep 1;done
root       841     0  0 08:37 pts/0    00:00:00 /bin/bash
root       858     1  0 08:37 ?        00:00:00 /usr/bin/coreutils --coreutils-prog-shebang=sleep /usr/bin/sleep 1
root       859   841  0 08:37 pts/0    00:00:00 ps -ef

# 方式二
# docker attach 容器id
# 测试
[root@iZbp13qr3mm4ucsjumrlgqZ ~]# docker attach 88d23bcbe1f2
正在执行当前的代码...

# docker exec        # 进入容器后开启一个新的终端，可以再里面操作（常用）
# docker attach        # 进入容器正在执行的终端，不会启动新的进程。
```

### 从容器内拷贝文件到主机上

```shell
# docker cp 容器id:容器内路径 目的主机的路径
[root@iZbp13qr3mm4ucsjumrlgqZ home]# ll
total 0
[root@iZbp13qr3mm4ucsjumrlgqZ home]# docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
[root@iZbp13qr3mm4ucsjumrlgqZ home]# docker run -it centos /bin/bash
[root@6eda31ad7987 /]# [root@iZbp13qr3mm4ucsjumrlgqZ home]# docker ps
CONTAINER ID   IMAGE     COMMAND       CREATED          STATUS          PORTS     NAMES
6eda31ad7987   centos    "/bin/bash"   17 seconds ago   Up 16 seconds             stoic_kepler
# 进入到容器内部
[root@iZbp13qr3mm4ucsjumrlgqZ home]# docker attach 6eda31ad7987
[root@6eda31ad7987 /]# ls
bin  dev  etc  home  lib  lib64  lost+found  media  mnt  opt  proc  root  run  sbin  srv  sys  tmp  usr  var
[root@6eda31ad7987 /]# cd /home/
[root@6eda31ad7987 home]# ls
# 在容器的/home路径下创建test.java文件
[root@6eda31ad7987 home]# touch test.java
[root@6eda31ad7987 home]# ls
test.java
[root@6eda31ad7987 home]# exit
exit
[root@iZbp13qr3mm4ucsjumrlgqZ home]# docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
[root@iZbp13qr3mm4ucsjumrlgqZ home]# docker ps -a
CONTAINER ID   IMAGE     COMMAND       CREATED              STATUS                      PORTS     NAMES
6eda31ad7987   centos    "/bin/bash"   About a minute ago   Exited (0) 28 seconds ago             stoic_kepler
# 将文件拷贝出来到主机上（在主机上执行该命令）
[root@iZbp13qr3mm4ucsjumrlgqZ home]# docker cp 6eda31ad7987:/home/test.java /home
[root@iZbp13qr3mm4ucsjumrlgqZ home]# ls
test.java
# 拷贝是一个手动过程，未来我们使用 -v 卷的技术，可以实现，自动同步（容器内的/home路径和主机上的/home路径打通）
```

## 小结

![](Docker/383865dd59f64bd88cbe6b4a56f1315c.png)

```shell
attach    Attach to a running container                                  #当前shell下attach连接指定运行镜像
build     Build an image from a Dockerfile                               #通过Dockerfile定制镜像
commit    Create a new image from a containers changes                   #提交当前容器为新的镜像
cp          Copy files/folders from a container to a HOSTDIR or to STDOUT  #从容器中拷贝指定文件或者目录到宿主机中
create    Create a new container                                         #创建一个新的容器，同run 但不启动容器
diff      Inspect changes on a containers filesystem                     #查看docker容器变化
events    Get real time events from the server                           #从docker服务获取容器实时事件
exec      Run a command in a running container                           #在已存在的容器上运行命令
export    Export a containers filesystem as a tar archive                #导出容器的内容流作为一个tar归档文件(对应import)
history   Show the history of an image                                   #展示一个镜像形成历史
images    List images                                                    #列出系统当前镜像
import    Import the contents from a tarball to create a filesystem image  #从tar包中的内容创建一个新的文件系统映像(对应export)
info      Display system-wide information                                #显示系统相关信息
inspect   Return low-level information on a container or image           #查看容器详细信息
kill      Kill a running container                                       #kill指定docker容器
load      Load an image from a tar archive or STDIN                      #从一个tar包中加载一个镜像(对应save)
login     Register or log in to a Docker registry                        #注册或者登陆一个docker源服务器
logout    Log out from a Docker registry                                 #从当前Docker registry退出
logs      Fetch the logs of a container                                  #输出当前容器日志信息
pause     Pause all processes within a container                         #暂停容器
port      List port mappings or a specific mapping for the CONTAINER     #查看映射端口对应的容器内部源端口
ps        List containers                                                #列出容器列表
pull      Pull an image or a repository from a registry                  #从docker镜像源服务器拉取指定镜像或者库镜像
push      Push an image or a repository to a registry                    #推送指定镜像或者库镜像至docker源服务器
rename    Rename a container                                             #重命名容器
restart   Restart a running container                                    #重启运行的容器
rm        Remove one or more containers                                  #移除一个或者多个容器
rmi       Remove one or more images                                      #移除一个或多个镜像(无容器使用该镜像才可以删除，否则需要删除相关容器才可以继续或者-f强制删除)
run       Run a command in a new container                               #创建一个新的容器并运行一个命令
save      Save an image(s) to a tar archive                              #保存一个镜像为一个tar包(对应load)
search    Search the Docker Hub for images                               #在docker hub中搜索镜像
start     Start one or more stopped containers                           #启动容器
stats     Display a live stream of container(s) resource usage statistics  #统计容器使用资源
stop      Stop a running container                                       #停止容器
tag       Tag an image into a repository                                 #给源中镜像打标签
top       Display the running processes of a container                   #查看容器中运行的进程信息
unpause   Unpause all processes within a container                       #取消暂停容器
version   Show the Docker version information                            #查看容器版本号
wait      Block until a container stops, then print its exit code        #截取容器停止时的退出状态值
```

## 练习

### 安装nginx

```shell
[root@lz ~]# docker search nginx
NAME                                              DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
nginx                                             Official build of Nginx.                        17820     [OK]                        
[root@lz ~]# docker pull nginx
Using default tag: latest
latest: Pulling from library/nginx
025c56f98b67: Pull complete 
ec0f5d052824: Pull complete 
cc9fb8360807: Pull complete 
defc9ba04d7c: Pull complete 
885556963dad: Pull complete 
f12443e5c9f7: Pull complete 
Digest: sha256:75263be7e5846fc69cb6c42553ff9c93d653d769b94917dbda71d42d3f3c00d3
Status: Downloaded newer image for nginx:latest
docker.io/library/nginx:latest
[root@lz ~]# docker run -d -p3344:80 --name mynginx nginx
02ab5a5c26c811ea075ccfd7c3c6a45a72f8a8b2dd82ae647df81197631e9432
[root@lz ~]# docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED         STATUS         PORTS                                   NAMES
02ab5a5c26c8   nginx     "/docker-entrypoint.…"   5 seconds ago   Up 4 seconds   0.0.0.0:3344->80/tcp, :::3344->80/tcp   mynginx
[root@lz ~]# curl http://localhost:3344
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
html { color-scheme: light dark; }
body { width: 35em; margin: 0 auto;
font-family: Tahoma, Verdana, Arial, sans-serif; }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
```

<img src="Docker/2021072014364032.png" alt="端口暴露示意图" style="zoom:67%;" />

### 安装tomcat

```shell
# 官方的使用
docker run -it --rm tomcat:9.0
# 我们之前的启动都是后台，停止了容器之后，容器还是可以查到     docker run -it --rm，一般用来测试，用完就删除

# 下载再启动
docker pull tomcat 

# 启动运行
docker run -d -p 3355:8080 --name tomcat01 tomcat
# 测试访问没有问题

# 进入容器
[root@iZ8vbgc3u6dvwrjyp45lyrZ home]# docker exec -it tomcat01 /bin/bash

# 发现问题：1、linux命令少了，2、没有webapps，阿里云镜像的原因。默认是最小的镜像，所有不必要的都剔除掉了。
# 保证最小可运行的环境
```

### 安装es+kibana

```shell
# es 暴露的端口很多！

# es 十分耗内存

# es 的数据一般需要放置到安全目录！挂载

# --net somenetwork ? 网络配置

# 下载启动elasticsearch（Docker一步搞定）
docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e
"discovery.type=single-node" elasticsearch:7.6.2

# 测试一下es是否成功启动
➜ ~ curl localhost:9200
{
"name" : "d73ad2f22dd3",
"cluster_name" : "docker-cluster",
"cluster_uuid" : "atFKgANxS8CzgIyCB8PGxA",
"version" : {
"number" : "7.6.2",
"build_flavor" : "default",
"build_type" : "docker",
"build_hash" : "ef48eb35cf30adf4db14086e8aabd07ef6fb113f",
"build_date" : "2020-03-26T06:34:37.794943Z",
"build_snapshot" : false,
"lucene_version" : "8.4.0",
"minimum_wire_compatibility_version" : "6.8.0",
"minimum_index_compatibility_version" : "6.0.0-beta1"
},
"tagline" : "You Know, for Search"
}

# 查看docker容器使用内存情况（每秒刷新，也挺耗内存的一个命令）
➜ ~ docker stats

#关闭，添加内存的限制，修改配置文件 -e 环境配置修改
➜ ~ docker rm -f d73ad2f22dd3
➜ ~ docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e
"discovery.type=single-node" -e ES_JAVA_OPTS="-Xms64m -Xmx512m"
elasticsearch:7.6.2

➜ ~ curl localhost:9200
{
"name" : "b72c9847ec48",
"cluster_name" : "docker-cluster",
"cluster_uuid" : "yNAK0EORSvq3Wtaqe2QqAg",
"version" : {
"number" : "7.6.2",
"build_flavor" : "default",
"build_type" : "docker",
"build_hash" : "ef48eb35cf30adf4db14086e8aabd07ef6fb113f",
"build_date" : "2020-03-26T06:34:37.794943Z",
"build_snapshot" : false,
"lucene_version" : "8.4.0",
"minimum_wire_compatibility_version" : "6.8.0",
"minimum_index_compatibility_version" : "6.0.0-beta1"
},
"tagline" : "You Know, for Search"
}
```

> 使用kibana连接es，思考网络如何才能连接？

![image-20221215225912666](Docker/image-20221215225912666.png)