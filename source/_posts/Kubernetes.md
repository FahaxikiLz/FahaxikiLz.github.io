---
title: Kubernetes 
date: 2025-05-16 14:11:40
tags:
- Kubernetes 
categories:
- 容器
published: false
---

# <font style="color:#FFA39E;">〓</font> Kubernetes - K8s

## 01. 前置知识

### 1.1 云平台

#### 私有网络 VPC 实战
在服务器内，采用 私网IP 来互相访问（<font style="color:#E8323C;">防火墙、安全组要记得配置</font>）

![画板](Kubernetes/48fa20eb86934b4a85597a5f2e264b4d.jpeg)



VPC：私有网络、专有网络。（可以在  阿里云 后台管理 创建 VPC）

![](Kubernetes/a3bb739b9fc64772a84c101f3f9a5d72.png)



##### 子网掩码换算
在线换算：[https://www.sojson.com/convert/subnetmask.html](https://www.sojson.com/convert/subnetmask.html)

![](Kubernetes/4c5f68e267604e11b182fb7e52945f67.png)

![](Kubernetes/c4b0f298712a4f40ba0001e95157afd4.png)



##### 创建VPC
![](Kubernetes/edae9954815c4a7fb5008c0f334afb71.png)

![](Kubernetes/4a6ca5e2448e4ee08e7ff025629df429.png)

在购买新的实例时候，可以选择 自己创建的 网段、交换机。 



下图是 雷神 购买的 3台 按量计费的 服务器。网络选择 自己创建的VPC，交换机也是选择同一个。所以下面的私网 IP 都是 同一个网段的，都是可以互相 ping 通的， 他这里 在 购买时候 <font style="color:#E8323C;">没有 选中分配公网IP</font>，这就没有公网IP了，<font style="color:#E8323C;">用 xshell 远程 连接不上</font>，只能在这个界面<font style="color:#E8323C;"> 远程连接</font> 连接使用。

![](Kubernetes/6e005743985c4e218e1a1fae0622be30.png)

雷神老师 演示完之后，在这里 就全部 释放（<font style="color:#E8323C;">释放设置</font>）了。

---

## 02. Kubernetes 简介


[Kubernetes 官网文档](https://kubernetes.io/zh/docs/home/) （<font style="color:#F5222D;">很重要，里面更完整 也有演示</font>）



**<font style="color:rgb(34, 34, 34);">Kubernetes</font>**<font style="color:rgb(34, 34, 34);"> 这个名字源于希腊语，意为“舵手”或“飞行员”。</font>

<font style="color:rgb(34, 34, 34);">k8s 这个缩写是因为 k 和 s 之间有八个字符的关系。</font>

### 2.1 Kubernetes 作用
[让我们回顾一下为什么 Kubernetes 如此有用。](https://kubernetes.io/zh/docs/concepts/overview/what-is-kubernetes/)

![](Kubernetes/07b15d80bdcd4433908ea947363ef7b1.png)

**<font style="color:rgb(34, 34, 34);">传统部署时代：</font>**

<font style="color:rgb(34, 34, 34);">早期，各个组织机构在物理服务器上运行应用程序。无法为物理服务器中的应用程序定义资源边界，这会导致资源分配问题。 例如，如果在物理服务器上运行多个应用程序，则可能会出现一个应用程序占用大部分资源的情况， 结果可能导致其他应用程序的性能下降。 一种解决方案是在不同的物理服务器上运行每个应用程序，但是由于资源利用不足而无法扩展， 并且维护许多物理服务器的成本很高。</font>

**<font style="color:rgb(34, 34, 34);">虚拟化部署时代：</font>**

<font style="color:rgb(34, 34, 34);">作为解决方案，引入了虚拟化。虚拟化技术允许你在单个物理服务器的 CPU 上运行多个虚拟机（VM）。 虚拟化允许应用程序在 VM 之间隔离，并提供一定程度的安全，因为一个应用程序的信息 不能被另一应用程序随意访问。</font>

<font style="color:rgb(34, 34, 34);">虚拟化技术能够更好地利用物理服务器上的资源，并且因为可轻松地添加或更新应用程序 而可以实现更好的可伸缩性，降低硬件成本等等。</font>

<font style="color:rgb(34, 34, 34);">每个 VM 是一台完整的计算机，在虚拟化硬件之上运行所有组件，包括其自己的操作系统。</font>

**<font style="color:rgb(34, 34, 34);">容器部署时代：</font>**

<font style="color:rgb(34, 34, 34);">容器类似于 VM，但是它们具有被放宽的隔离属性，可以在应用程序之间共享操作系统（OS）。 因此，容器被认为是轻量级的。容器与 VM 类似，具有自己的文件系统、CPU、内存、进程空间等。 由于它们与基础架构分离，因此可以跨云和 OS 发行版本进行移植。</font>

<font style="color:rgb(34, 34, 34);"></font>

#### <font style="color:rgb(34, 34, 34);">2.1.1 Kubernetes 特性</font>


+ **<font style="color:rgb(34, 34, 34);">服务发现和负载均衡 </font>**

<font style="color:rgb(34, 34, 34);">Kubernetes 可以使用 DNS 名称或自己的 IP 地址公开容器，如果进入容器的流量很大， Kubernetes 可以负载均衡并分配网络流量，从而使部署稳定。</font>

+ **<font style="color:rgb(34, 34, 34);">存储编排 </font>**

<font style="color:rgb(34, 34, 34);">Kubernetes 允许你自动挂载你选择的存储系统，例如本地存储、公共云提供商等。</font>

+ **<font style="color:rgb(34, 34, 34);">自动部署和回滚</font>**

<font style="color:rgb(34, 34, 34);">你可以使用 Kubernetes 描述已部署容器的所需状态，它可以以受控的速率将实际状态 更改为期望状态。例如，你可以自动化 Kubernetes 来为你的部署创建新容器， 删除现有容器并将它们的所有资源用于新容器。</font>

+ **<font style="color:rgb(34, 34, 34);">自动完成装箱计算</font>**

<font style="color:rgb(34, 34, 34);">Kubernetes 允许你指定每个容器所需 CPU 和内存（RAM）。 当容器指定了资源请求时，Kubernetes 可以做出更好的决策来管理容器的资源。</font>

+ **<font style="color:rgb(34, 34, 34);">自我修复</font>**

<font style="color:rgb(34, 34, 34);">Kubernetes 重新启动失败的容器、替换容器、杀死不响应用户定义的 运行状况检查的容器，并且在准备好服务之前不将其通告给客户端。</font>

+ **<font style="color:rgb(34, 34, 34);">密钥与配置管理</font>**

<font style="color:rgb(34, 34, 34);">Kubernetes 允许你存储和管理敏感信息，例如密码、OAuth 令牌和 ssh 密钥。 你可以在不重建容器镜像的情况下部署和更新密钥和应用程序配置，也无需在堆栈配置中暴露密钥。</font>



### 2.2 Kubernetes 架构


#### 2.2.1 工作方式


<font style="color:rgb(34, 34, 34);">Kubernetes </font>安装都是 <font style="color:#E8323C;">集群模式</font>，<font style="color:rgb(34, 34, 34);">部署完 Kubernetes, 即拥有了一个完整的集群。</font>

<font style="color:rgb(34, 34, 34);">Kubernetes Cluster = N Master Node + N Worker Node</font>

<font style="color:rgb(34, 34, 34);"> N 主节点           + N 工作节点 	（ N >= 1）</font>



#### 2.2.2 组件架构


[官方文档 - Kubernetes 组件](https://kubernetes.io/zh/docs/concepts/overview/components/)

![](Kubernetes/da2eeee01b2c405fb3409dd32e96fb2c.png)



---

## 03. Kubernetes 集群搭建


需要 <font style="color:#E8323C;">3台 云服务器</font>，每台机器装上 Docker、Kubelet、Kubectl、Kubeadm。

![](Kubernetes/5b806bb3d8a14fdca11d53b9bc84167e.png)



### 3.1 购买青云服务器（白嫖）


[青云 - 官网](https://console.qingcloud.com/)，自己注册个账号，个人认证后，会给你 优惠券

**<font style="color:#E8323C;">别担心，不要钱，可以白嫖一段时间（服务器 可用 25元的优惠券）</font>**

![](Kubernetes/75106ee7afb34cd29c3dc5f79a033da4.png)

![](Kubernetes/d55d0c6b979c4efc8f465f52673de699.png)

释放了服务器， 公网IP 这里，如果有 就删除。

![](Kubernetes/717a0348f18f47e88c296bdcf2ffe342.png)

<font style="color:#F5222D;">我的配额</font> 这里，要确保 公网IP 有3个 以上。如果没有，在 <font style="color:#F5222D;">工单 -> 创建工单</font>，联系客服 要。

![](Kubernetes/831e1c1603ae4e6c8f82f220d2f3387b.png)

#### 创建 VPC
![](Kubernetes/2654677f92604f74a249a7142aa5f6cb.png)

~~创建好 VPC 后，点进去 创建私有网络~~

![](Kubernetes/cada10cb0cbb4538a85d32db488aa962.png)

<font style="color:#E8323C;">这里默认有一个了 172.31.0.0/24，上图是创建不了的， 这里修改个名字就好。</font>

![](Kubernetes/17c75b9b3e024d1394f9e6a09a591cbf.png)



#### 购买3台 2核4G 的服务器
![](Kubernetes/91030330681e46959652341d6dd4e29d.png)

先创建好 VPC 和 私有网络后，

![](Kubernetes/ea296e93b246417dba65757f22897d09.png)

![](Kubernetes/7746a5ff9a534f47a6aa6d3af84051f2.png)

![](Kubernetes/ceea8e8f5542416da3d09b1e45d03034.png)

点击<font style="color:#E8323C;"> 立即购买</font>，刷新一下 即可看到下面的 公网IPv4，我这里不就 开放了哈。

![](Kubernetes/37acadc099214759806f7e47b035d025.png)

![](Kubernetes/50f913da8dfa4a80befb36e3aee84524.png)

#### 连接测试
互相 ping，都可以 通。（<font style="color:#F5222D;">其他不在 这个 VPC 网段的服务器，是ping 不通的</font>）

![](Kubernetes/f67fd3b457fc4f0f9577f7796e7b8bd1.png)

![](Kubernetes/7b369b6e3a924c719316c21a006c9fd2.png)

![](Kubernetes/0a69fbb4138a4795ab162e7642d4ffa0.png)



### 3.2 安装 Docker


#### 3.2.1 安装 docker 20.10.7
<font style="color:#000000;">注意</font><font style="color:#F5222D;"> Docker 版本</font>，要和 Kubernetes 版本 有对应关系。

```bash
# 安装/更新 yum-utils
yum install -y yum-utils

# 配置 yum源
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

# 安装 docker 20.10.7 版本
yum -y install docker-ce-20.10.7 docker-ce-cli-20.10.7 containerd.io

# 查看 docker 版本
docker -v

# 启动 docker
systemctl start docker

# 查看 docker 是否成功, 有 Client 和 Server 即成功
docker version
```

![](Kubernetes/2026fdff21c34e088c960c7f85308092.png)

安装完成

![](Kubernetes/82e04670c7764ac0849fec5e08af168c.png)

分别查看 3台服务器 docker 状态

![](Kubernetes/beaf17eb7c8d4d46a0e8eb20f053fb85.png)

![](Kubernetes/36844adb88b74b7f9990d7c155d679fa.png)

![](Kubernetes/48e2dfd269fa4fdb86fd1a39803664d6.png)



#### 3.2.2 配置加速镜像


[https://82m9ar63.mirror.aliyuncs.com](https://82m9ar63.mirror.aliyuncs.com)，这个镜像加速 是 雷神的（个人具体可以在 阿里云 配置）

```bash
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://82m9ar63.mirror.aliyuncs.com"],
  "exec-opts": ["native.cgroupdriver=systemd"],
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m"
  },
  "storage-driver": "overlay2"
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker

# 查看是否配置成功
docker info
```

![](Kubernetes/b0f444fb2c044615aec1a519b587d958.png)

![](Kubernetes/6f36e214f56d4ab8a48b4a90f7d27e1e.png)

![](Kubernetes/e1bcc8d8492b4154ac878588a179dbd7.png)



### 3.3 安装 Kubernetes


#### 3.3.1 要求
- [x] 每台机器 2GB 或者 更多的 RAM（如果少于这个数字 将会影响应用的运行内存）
- [x] CPU 2核 以上
- [x] 集群中 所有的服务器 的网络 彼此可以相互连接。

<font style="color:#E8323C;">在 3.1 章节中，购买的 2核4G 的云服务器，并且 相互可以 ping通。</font>



#### 3.3.2 设置hostname
- [x] 每台服务器的 主机名不可重复。

```bash
# 查看主机名
hostname

# 设置主机名
hostnamectl set-hostname k8s-master
hostnamectl set-hostname k8s-node-1
hostnamectl set-hostname k8s-node-2

# 更新
bash
```

![](Kubernetes/556c2166104045448d5975e8b8e239b0.png)

![](Kubernetes/fa3bead991ff4199ae227adf1aeef829.png)

![](Kubernetes/2a9e966d94454bbf93ad2547cb8c02fb.png)

#### 3.3.3 关闭交换区
下面是一些 <font style="color:#E8323C;">安全设置</font>

```bash
# 查看 交换分区
free -m

# 将 SELinux 设置为 permissive 模式（相当于将其禁用）  第一行是临时禁用，第二行是永久禁用
setenforce 0
sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

# 关闭swap；第一行是临时禁用，第二行是永久禁用
swapoff -a  
sed -ri 's/.*swap.*/#&/' /etc/fstab

# 允许 iptables 检查桥接流量 （K8s 官方要求）
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
br_netfilter
EOF
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF

# 让配置生效
sysctl --system
```

![](Kubernetes/78d858770629404c8632fae0251b55b8.png)

![](Kubernetes/fdacab6898ff4d288c8cf036984c8574.png)

![](Kubernetes/cfabf1c72852408dadb2fe4449ecb79c.png)

##### 虚拟机
```bash
# 禁用 selinux
vim /etc/selinux/config
SELINUX=disabled

# 关闭swap
swapoff -a  # 临时
vim /etc/fstab  # 永久
# 注释下面这行
#/dev/mapper/centos-swap swap                    swap    defaults        0 0

# 查看是否成功
free -m
```

![](Kubernetes/7309f309282844619760af91b46d5902.png)



<font style="color:#E8323C;">重启后 系统进入了 紧急模式，输入密码 进入命令行模式。 </font>[解决方案参考](https://www.58jb.com/html/swap-off-caused-system-boot-failure.html)

```bash
vi /boot/grub2/grub.cfg

# 删除 rd.lvm.lv=centos/swap，保存 然后重启.
```

![](Kubernetes/f0d937aab39d416299634de91c3b840b.png)



#### 3.3.4 安装 K8s 三大件
安装 kubelet、kebeadm、kubectl；<font style="color:#E8323C;">注意版本 （1.20.9）</font>

```bash
# 配置 k8s 的 yum 源地址
cat <<EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg
   http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
```

```bash
# 安装 kubelet、kubeadm、kubectl
yum install -y kubelet-1.20.9 kubeadm-1.20.9 kubectl-1.20.9 --disableexcludes=kubernetes

# 启动kubelet
systemctl enable --now kubelet

# 查看 kubelet 状态：一会停止 一会运行。 这个状态是对的，kubelet 等待 kubeadm 发号指令。
systemctl status kubelet
```

![](Kubernetes/7642d3f2a0c84766a2be8f79225981a3.png)

![](Kubernetes/05b3bab650114cda97f3013812b8fca1.png)

![](Kubernetes/1c22a12eb50e42bfae31633799c59c85.png)



#### 3.3.5 创建 control-plane ★
Kubeadm 引导集群

```bash
Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

You can now join any number of control-plane nodes by copying certificate authorities
and service account keys on each node and then running the following as root:

  kubeadm join cluster-endpoint:6443 --token euztag.6yjbhn94ehg6nhn7 \
    --discovery-token-ca-cert-hash sha256:a6e917b751e424c7a761c2276ae02db95d87ba5a3cc6b374182519ce89aba28b \
    --control-plane 

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join cluster-endpoint:6443 --token euztag.6yjbhn94ehg6nhn7 \
    --discovery-token-ca-cert-hash sha256:a6e917b751e424c7a761c2276ae02db95d87ba5a3cc6b374182519ce89aba28b 

```

##### 3.3.5.1 下载镜像
```bash
# 配置镜像，生成 images.sh
sudo tee ./images.sh <<-'EOF'
#!/bin/bash
images=(
kube-apiserver:v1.20.9
kube-proxy:v1.20.9
kube-controller-manager:v1.20.9
kube-scheduler:v1.20.9
coredns:1.7.0
etcd:3.4.13-0
pause:3.2
)
for imageName in ${images[@]} ; do
docker pull registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/$imageName
done
EOF

# 拉取镜像
chmod +x ./images.sh && ./images.sh
```

![](Kubernetes/2fa5355fb11e46a1a88edcd243ac7829.png)

![](Kubernetes/2ba1e621edc541a281f552753c003676.png)

<font style="color:#E8323C;">报这个错，没启动 docker</font>

![](Kubernetes/d957570c10d348c6a43836838bccdf82.png)

![](Kubernetes/83daf0534abd4aa7996041d6d07f0678.png)

![](Kubernetes/ee4040fd51624b999d9adf31765205ef.png)

![](Kubernetes/d0dbee84ca58478e877f5f089ad447b5.png)

###### 网段小知识
![](Kubernetes/2b8d1cf57150447fba44b8d5d3511868.png)

可以看到 docker 的 ip 是<font style="color:#E8323C;"> 172.17.x.x</font>，所以 vps 设置 网段时候，不选择 172.17.x.x 的。



##### 3.3.5.2 初始化主节点
```bash
# 所有机器添加 master 域名映射，以下 IP 为 master 的 IP；
# 访问 cluster-endpoint 即 访问 172.31.0.4
echo "172.31.0.4  cluster-endpoint" >> /etc/hosts

# 主节点初始化 （只在 master 服务器执行， 其他 node 不用）
# --apiserver-advertise-address: master 的 IP
# --control-plane-endpoint: master 的域名
# --service-cidr 和 --pod-network-cidr 是网络范围，雷神 建议不要改。要改的话 2 个cidr 和 vps（172.31.x.x） 的，3 个网络互相不能重叠；还要修改 calico.yaml的 IP（下图有写）。
kubeadm init \
--apiserver-advertise-address=172.31.0.4 \
--control-plane-endpoint=cluster-endpoint \
--image-repository registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images \
--kubernetes-version v1.20.9 \
--service-cidr=10.96.0.0/16 \
--pod-network-cidr=192.168.0.0/16
```



![](Kubernetes/a5d842de17e44ad7992876f0a16a9df0.png)

![](Kubernetes/bce1032952e04f4683f1cd557208672c.png)

![](Kubernetes/e132f3d3b22549c991e38565369d24b2.png)

<font style="color:#E8323C;">注：初始化 主节点命令，只在 master 服务执行。其他 node 服务器 不用。</font>

![](Kubernetes/fc28947817574218ab23bb9b0880851b.png)

![](Kubernetes/1a61fddbc1c648519b8b043a720457a6.png)

<font style="color:#E8323C;">复制上面 输出的语句，这些命令 需要用到</font>

```bash
Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

You can now join any number of control-plane nodes by copying certificate authorities
and service account keys on each node and then running the following as root:

  kubeadm join cluster-endpoint:6443 --token euztag.6yjbhn94ehg6nhn7 \
    --discovery-token-ca-cert-hash sha256:a6e917b751e424c7a761c2276ae02db95d87ba5a3cc6b374182519ce89aba28b \
    --control-plane 

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join cluster-endpoint:6443 --token euztag.6yjbhn94ehg6nhn7 \
    --discovery-token-ca-cert-hash sha256:a6e917b751e424c7a761c2276ae02db95d87ba5a3cc6b374182519ce89aba28b 

```

![](Kubernetes/997d6a7201b14e458c5c774c48a1c7db.png)

##### 
##### 3.3.5.3 安装网络组件
<font style="color:#E8323C;">注：只在 master 服务执行。其他 node 服务器 不用。</font>

```bash
# 下载 calico.yaml
curl https://docs.projectcalico.org/manifests/calico.yaml -O
# 上面的命令可能不会，执行加载配置可能报错，可以试试这个
curl -L https://docs.projectcalico.org/manifests/calico.yaml -o calico.yaml

# 加载配置
kubectl apply -f calico.yaml
```

![](Kubernetes/63470e81f463458bbf4a1eca789dc43a.png)

如果修改了 初始化主节点中的

<font style="color:#E8323C;">--pod-network-cidr=192.168.0.0/16</font>

将 calico.yaml 的配置， # 去掉，IP 写 改的 IP。

![](Kubernetes/2184676019a04d279e48f4de3ef0e6a0.png)



#### 3.3.6 Worker 加入集群 ★
Worker 节点加入到集群中。

![](Kubernetes/59ca6e7a603b4a978fab7bbc745976af.png)

<font style="color:#F5222D;">注：此处要复制自己的，不要复制 下面的命令。</font>

```bash
# 加入到 集群中，只在 2 个 node 服务器运行。
kubeadm join cluster-endpoint:6443 --token euztag.6yjbhn94ehg6nhn7 \
    --discovery-token-ca-cert-hash sha256:a6e917b751e424c7a761c2276ae02db95d87ba5a3cc6b374182519ce89aba28b 
```

![](Kubernetes/a566cdc11e984316afd1b9b179aea489.png)

![](Kubernetes/b922ce329d2749e19dc6e72c8167dbcc.png)

回到 master 中 查看

![](Kubernetes/92f504a1d28e4975b2d67adfd343ccb7.png)

![](Kubernetes/3ba8a5209735486b9d9b124e28620c06.png)

监控一下 服务器的状态，可以发现 master 服务器 消耗比较大。

![](Kubernetes/0f3a100b34ee46b69158045ccc3d64d6.png)

##### 令牌过期
如果令牌过期了，重新 获取令牌。（<font style="color:#F5222D;">在 master 服务器中 执行</font>）

```bash
# 重新获取令牌
kubeadm token create --print-join-command
```

![](Kubernetes/f0247c9148b445d8914cb3f835544e7c.png)

在 worker 服务器上 执行 上述命令 加入到 k8s 集群中。



#### 3.3.7 集群自我修复测试
集群自我修复能力测试  （<font style="color:#F5222D;">需要 docker 启动的前提下，systemctl start docker</font>）

3台 服务器 分别重启

```bash
# 重启
reboot
```

集群 还是好好的。 nodes 的 STATUS 都是  Ready，查看 pods STATUS 过一会 都是 Running。

![](Kubernetes/849f9b1a27a94b969bb76b46b9efac06.png)



#### 3.3.8 部署 Dashboard
部署 dashboard（可视化页面），[kubernetes 官方提供的可视化界面](https://github.com/kubernetes/dashboard)



##### 3.3.8.1 运行 pod（创建资源）
```bash
# 根据 在线配置文件 创建资源
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.3.1/aio/deploy/recommended.yaml
```

<font style="color:#F5222D;">只在 master 服务器中执行</font>

![](Kubernetes/525ac42d961d4bffabd8c72eb12f6ab1.png)

等到 Running

![](Kubernetes/5649e52e42fc48eaa08492b0a79a43b0.png)



##### 3.3.8.2 设置访问端口
```bash
# 修改配置文件 找到 type，将 ClusterIP 改成 NodePort
kubectl edit svc kubernetes-dashboard -n kubernetes-dashboard

# 找到端口，在安全组放行
kubectl get svc -A |grep kubernetes-dashboard
```

输入 /type，找到 type，将 ClusterIP 改成 NodePort

![](Kubernetes/b0b04dbe291c4475abb1b52d71d43f9d.png)

![](Kubernetes/dc09b299409b463592e3807348a74669.png)

![](Kubernetes/a036e227481f4430abb0ff3ad669c6fe.png)



##### 3.3.8.3 开放安全组
在 ID 右键 -> 解绑，找到 用的哪个资源。

![](Kubernetes/a5174f8d0b2e40b9854378c92cb0888e.png)

添加规则

![](Kubernetes/029b96c52bfa4ea59c82539d9399a7be.png)

添加完后，在点击 应用修改

![](Kubernetes/2e263d2f3a9b423c80bfde4cd0aae7cc.png)



---

**分割线，上面白嫖的 青云服务器 到期了。 笔者 搭了****<font style="color:#E8323C;"> 3个虚拟机，</font>****一路从上面 走下来，到这里继续。**

| **系统** | **IP** | **备注** |
| :---: | :---: | :---: |
| <font style="color:rgb(79, 79, 79);">CentOS 7</font> | <font style="color:rgb(79, 79, 79);">192.168.27.251</font> | <font style="color:rgb(79, 79, 79);">k8s-master</font> |
| <font style="color:rgb(79, 79, 79);">CentOS 7</font> | <font style="color:rgb(79, 79, 79);">192.168.27.252</font> | <font style="color:rgb(79, 79, 79);">k8s-node-1</font> |
| <font style="color:rgb(79, 79, 79);">CentOS 7</font> | <font style="color:rgb(79, 79, 79);">192.168.27.253</font> | <font style="color:rgb(79, 79, 79);">k8s-node-2</font> |


![虚拟机搭建 K8s 集群成功](Kubernetes/1a76bd5e4248423984c932b3fcf2c214.png)



##### 3.3.8.4 访问页面
[https://192.168.27.251:31876](https://192.168.27.251:31876/) （要注意是 <font style="color:#F5222D;">https</font>，port 是映射的端口，在配置文件查看）

![](Kubernetes/efbdc65299ce4052a4749fa13cb3187f.png)

![](Kubernetes/6105a2d00f0d40c89d304fc3f25980a7.png)

![](Kubernetes/6649a64ece9c48b9bc4c93d3935a0423.png)



###### 创建访问账号
```bash
# 创建 dash-usr.yaml，加入下面配置
vi dash-usr.yaml

# 创建访问账号，准备一个yaml文件，加入下面配置
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
  
# 在 k8s 集群中创建资源
kubectl apply -f dash-usr.yaml

# 获取访问令牌
kubectl -n kubernetes-dashboard get secret $(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}") -o go-template="{{.data.token | base64decode}}"
```

![](Kubernetes/7180d2f62e85414b933866e4809e2983.png)



###### 复制 token，粘贴到页面中，然后登录进去。
```bash
eyJhbGciOiJSUzI1NiIsImtpZCI6IlA5bm13LXZTa0xJR0VSZUxua3VZdmdiS2tpWWNOQjBWbnJ0MmxZT25Na1kifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLWpncmpmIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJlOTNjZjVkZC02YmY3LTQwYjQtODJmNS0wMzQ5NGIyMmNhYmQiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.SwgtiTfs6cXys9OFxsXX-ZabTWGVRsBsx6jwDzGaPrgdARBvKvS7aU84v-RpgXrmXtbS-tiVM-EuDIGidacLawg6iLoqp5K7i2WwS5Slw5YiqaaDphRaa5lJAuF--fI0ULsuAGAGo0Y2KZh05f1YwwPTD92OB1uRfHxcLrg8PNWkdB2oFLwZWhEAmodxh0NP2M0zbC827dhl3jcX3R7qie8-_M1rWKXXtlbpfTEk_M8OfKj69GOSDegmmuqS4a1Utckbt4ErDalfbFKzuLXljS1-1XBZUiH74CnpGf8kleEtpd-yIf4dtOSxU9fOl4hVJkK96nlffcXNyw-6b-BZBQ
```

![](Kubernetes/1a953ec2cfae41be8e8a992f37359c12.png)

![](Kubernetes/1193a54bdbe74e18a1db44f7e4516b46.png)

![](Kubernetes/015b1fa07376409a9f5093520695c867.png)



###### Bug：Pod 启动失败
修改了 NodePort 后，Pod kubernetes-dashboard 起不来了。（环境：<font style="color:#F5222D;">虚拟机</font>）

```bash
# 查看日志
kubectl logs -f -n kubernetes-dashboard kubernetes-dashboard-658485d5c7-f89v7
```

![](Kubernetes/f5b5dfd83bb8421f9b9e7e1f0d78ffb5.png)

> 两种解决方案
>
> + 将 dashboard 部署到 master上，因为 master 刚安装了网络组件
> + 让 工作节点 也能访问 apiServer
>

无法访问，查看 部署到 哪个 node 上了， 将 dashboard 部署到 master 上。

![](Kubernetes/debf05bc7bf749c88c0049655bdf40bd.png)

修改 recommended.yaml，添加下面

![](Kubernetes/f6e3e4ca33bf439cb147eb7515eed80f.png)

![](Kubernetes/aeee3dc5e48b44ddb7818f037ecad134.png)



完整配置如下（这里就将 在线的配置复制下来，然后 加入上面的 2个 nodeName: k8s-master）

```yaml
# Copyright 2017 The Kubernetes Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

apiVersion: v1
kind: Namespace
metadata:
  name: kubernetes-dashboard

---

apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard

---

kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
spec:
  ports:
    - port: 443
      targetPort: 8443
  selector:
    k8s-app: kubernetes-dashboard

---

apiVersion: v1
kind: Secret
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-certs
  namespace: kubernetes-dashboard
type: Opaque

---

apiVersion: v1
kind: Secret
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-csrf
  namespace: kubernetes-dashboard
type: Opaque
data:
  csrf: ""

---

apiVersion: v1
kind: Secret
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-key-holder
  namespace: kubernetes-dashboard
type: Opaque

---

kind: ConfigMap
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-settings
  namespace: kubernetes-dashboard

---

kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
rules:
  # Allow Dashboard to get, update and delete Dashboard exclusive secrets.
  - apiGroups: [""]
    resources: ["secrets"]
    resourceNames: ["kubernetes-dashboard-key-holder", "kubernetes-dashboard-certs", "kubernetes-dashboard-csrf"]
    verbs: ["get", "update", "delete"]
    # Allow Dashboard to get and update 'kubernetes-dashboard-settings' config map.
  - apiGroups: [""]
    resources: ["configmaps"]
    resourceNames: ["kubernetes-dashboard-settings"]
    verbs: ["get", "update"]
    # Allow Dashboard to get metrics.
  - apiGroups: [""]
    resources: ["services"]
    resourceNames: ["heapster", "dashboard-metrics-scraper"]
    verbs: ["proxy"]
  - apiGroups: [""]
    resources: ["services/proxy"]
    resourceNames: ["heapster", "http:heapster:", "https:heapster:", "dashboard-metrics-scraper", "http:dashboard-metrics-scraper"]
    verbs: ["get"]

---

kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
rules:
  # Allow Metrics Scraper to get metrics from the Metrics server
  - apiGroups: ["metrics.k8s.io"]
    resources: ["pods", "nodes"]
    verbs: ["get", "list", "watch"]

---

apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: kubernetes-dashboard
subjects:
  - kind: ServiceAccount
    name: kubernetes-dashboard
    namespace: kubernetes-dashboard

---

apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: kubernetes-dashboard
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: kubernetes-dashboard
subjects:
  - kind: ServiceAccount
    name: kubernetes-dashboard
    namespace: kubernetes-dashboard

---

kind: Deployment
apiVersion: apps/v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kubernetes-dashboard
spec:
  replicas: 1
  revisionHistoryLimit: 10
  selector:
    matchLabels:
      k8s-app: kubernetes-dashboard
  template:
    metadata:
      labels:
        k8s-app: kubernetes-dashboard
    spec:
      nodeName: k8s-master
      containers:
        - name: kubernetes-dashboard
          image: kubernetesui/dashboard:v2.3.1
          imagePullPolicy: Always
          ports:
            - containerPort: 8443
              protocol: TCP
          args:
            - --auto-generate-certificates
            - --namespace=kubernetes-dashboard
            # Uncomment the following line to manually specify Kubernetes API server Host
            # If not specified, Dashboard will attempt to auto discover the API server and connect
            # to it. Uncomment only if the default does not work.
            # - --apiserver-host=http://my-address:port
          volumeMounts:
            - name: kubernetes-dashboard-certs
              mountPath: /certs
              # Create on-disk volume to store exec logs
            - mountPath: /tmp
              name: tmp-volume
          livenessProbe:
            httpGet:
              scheme: HTTPS
              path: /
              port: 8443
            initialDelaySeconds: 30
            timeoutSeconds: 30
          securityContext:
            allowPrivilegeEscalation: false
            readOnlyRootFilesystem: true
            runAsUser: 1001
            runAsGroup: 2001
      volumes:
        - name: kubernetes-dashboard-certs
          secret:
            secretName: kubernetes-dashboard-certs
        - name: tmp-volume
          emptyDir: {}
      serviceAccountName: kubernetes-dashboard
      nodeSelector:
        "kubernetes.io/os": linux
      # Comment the following tolerations if Dashboard must not be deployed on master
      tolerations:
        - key: node-role.kubernetes.io/master
          effect: NoSchedule

---

kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: dashboard-metrics-scraper
  name: dashboard-metrics-scraper
  namespace: kubernetes-dashboard
spec:
  ports:
    - port: 8000
      targetPort: 8000
  selector:
    k8s-app: dashboard-metrics-scraper

---

kind: Deployment
apiVersion: apps/v1
metadata:
  labels:
    k8s-app: dashboard-metrics-scraper
  name: dashboard-metrics-scraper
  namespace: kubernetes-dashboard
spec:
  replicas: 1
  revisionHistoryLimit: 10
  selector:
    matchLabels:
      k8s-app: dashboard-metrics-scraper
  template:
    metadata:
      labels:
        k8s-app: dashboard-metrics-scraper
      annotations:
        seccomp.security.alpha.kubernetes.io/pod: 'runtime/default'
    spec:
      nodeName: k8s-master
      containers:
        - name: dashboard-metrics-scraper
          image: kubernetesui/metrics-scraper:v1.0.6
          ports:
            - containerPort: 8000
              protocol: TCP
          livenessProbe:
            httpGet:
              scheme: HTTP
              path: /
              port: 8000
            initialDelaySeconds: 30
            timeoutSeconds: 30
          volumeMounts:
          - mountPath: /tmp
            name: tmp-volume
          securityContext:
            allowPrivilegeEscalation: false
            readOnlyRootFilesystem: true
            runAsUser: 1001
            runAsGroup: 2001
      serviceAccountName: kubernetes-dashboard
      nodeSelector:
        "kubernetes.io/os": linux
      # Comment the following tolerations if Dashboard must not be deployed on master
      tolerations:
        - key: node-role.kubernetes.io/master
          effect: NoSchedule
      volumes:
        - name: tmp-volume
          emptyDir: {}
```



重新安装 dashboard

```yaml
vi recommended.yaml

# 将上述 配置 粘贴进去.

kubectl apply -f recommended.yaml

kubectl get pods -A -owide
```

![](Kubernetes/491a80460feb4bed9750991b054f0933.png)

然后 就可以访问了。

![](Kubernetes/efbdc65299ce4052a4749fa13cb3187f.png)



---

## 04. Kubernetes 命令
| kubectl get nodex | 查看 k8s 集群 所有节点 |
| --- | --- |
| | |
| kubectl get ns | 查看命名空间 |
| kubectl get pods -A<br/><font style="color:#389E0D;">对应 docker 中 的</font><br/><font style="color:#389E0D;">docker ps</font> | 查看 k8s 集群 部署了 哪些应用<br/><font style="color:#389E0D;">运行的应用在 do</font><font style="color:#F5222D;"></font><font style="color:#389E0D;">cker 中叫 container（容器），</font><br/><font style="color:#389E0D;">在 k8s 中叫 pod</font> |
| kubectl get pods | 查看 默认命名空间（default）部署的应用 |
| kubectl get pod -A -owide | 查看 完整的信息，有 部署的 节点、节点IP 等。 |
| kubectl get pod -w | 打印 整个状态变化过程 |
| **<font style="color:#F5222D;">Namespace 命名空间（-n）</font>** | |
| kubectl get pod -n kubernetes-dashboard | 查看 某个命名空间（kubernetes-dashboard）部署的应用 |
| kubectl create ns hello | 创建命名空间 hello |
| kubectl delete ns hello | 删除命名空间 hello |
| **<font style="color:#F5222D;">集群资源</font>** | |
| kubectl apply -f xxxx.yaml | 根据配置文件，给 k8s 集群创建资源 |
| kubectl delete -f hello-yaml.yaml | 删除 hello-yaml.yaml 配置文件创建的资源 |
| **<font style="color:#F5222D;">Pod</font>** | |
| kubectl run mynginx-k8s --image=nginx | 创建名为 mynginx-k8s 的 Pod （默认命名空间） |
| kubectl exec -it Pod名字 -- /bin/bash | 进入到 Pod 里面（和 docker exec -it 一样的） |
| kubectl describe pod mynginx-k8s | 查看 Pod mynginx-k8s 的描述 |
| ~~kubectl delete pod mynginx-k8s~~ | 删除 在默认命名空间的 mynginx-k8s |
| kubectl delete pod mynginx-k8s -n xxx | 删除 在 xxx 命名空间的 mynginx-k8s |
| kubectl logs mynginx-k8s | 查看 Pod mynginx-k8s 的运行日志（默认的命名空间） |
| kubectl logs -f -n xxx命名空间 xxxPodName | 查看 Pod 运行日志（-n 就是 namespace 命名空间） |
| **<font style="color:#F5222D;">Deployment</font>** | |
| kubectl create deployment mytomcat --image=tomcat:8.5.68 | 用 Deployment 部署 Pod，deploy 名为 mytomcat，<br/>使用镜像 tomcat:8.5.68 |
| kubectl get deploy | 查看 Deployment 创建的资源 |
| kubectl delete deploy mytomcat | 删除 Deployment 创建的资源，用 delete 是删不掉的 |
| kubectl create deployment my-dep --image=nginx --replicas=3 | --replicas=3，部署 3个 Pod my-dep |
| kubectl scale deploy/my-dep --relicas=5 | 对 Deployment my-dep 扩容 成 5 个 Pod |
| kubectl scale deploy/my-dep --relicas=2 | 对 Deployment my-dep 缩容 成 2 个 Pod |
| kubectl get deploy/my-dep -oyaml | 查看之前用的镜像（spec.container.image） |
| kubectl get deploy/my-dep -oyaml | grep image | 查看之前用的镜像 |
| kubectl set image deployment/my-dep nginx=nginx:1.16.1 --record<br/>kubectl rollout status deployment/my-dep | 改变 my-dep 中 nginx 版本（nginx 最新版 -> 1.16.1）<br/>滚动更新 |
| kubectl rollout history deployment/my-dep | 历史记录 |
| kubectl rollout histroy deployment/my-dep --revision=2 | 查看 my-dep 某个历史详情 |
| kubectl rollout undo deployment/my-dep | my-dep 回滚（回到上个版本） |
| kubectl rollout undo deployment/my-dep --to-revision=2 | my-dep 回滚到指定版本 |
| | |
| | |
| | |
| | |



---

## 05. Kubernetes 核心实战


dashboard：[https://192.168.27.251:31876](https://192.168.27.251:31876)

```bash
eyJhbGciOiJSUzI1NiIsImtpZCI6IlA5bm13LXZTa0xJR0VSZUxua3VZdmdiS2tpWWNOQjBWbnJ0MmxZT25Na1kifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlcm5ldGVzLWRhc2hib2FyZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJhZG1pbi11c2VyLXRva2VuLWpncmpmIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6ImFkbWluLXVzZXIiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiJlOTNjZjVkZC02YmY3LTQwYjQtODJmNS0wMzQ5NGIyMmNhYmQiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6a3ViZXJuZXRlcy1kYXNoYm9hcmQ6YWRtaW4tdXNlciJ9.SwgtiTfs6cXys9OFxsXX-ZabTWGVRsBsx6jwDzGaPrgdARBvKvS7aU84v-RpgXrmXtbS-tiVM-EuDIGidacLawg6iLoqp5K7i2WwS5Slw5YiqaaDphRaa5lJAuF--fI0ULsuAGAGo0Y2KZh05f1YwwPTD92OB1uRfHxcLrg8PNWkdB2oFLwZWhEAmodxh0NP2M0zbC827dhl3jcX3R7qie8-_M1rWKXXtlbpfTEk_M8OfKj69GOSDegmmuqS4a1Utckbt4ErDalfbFKzuLXljS1-1XBZUiH74CnpGf8kleEtpd-yIf4dtOSxU9fOl4hVJkK96nlffcXNyw-6b-BZBQ
```



### 5.1 资源创建方式


#### 5.1.1 yaml
用 yaml 配置 在 kubernetes 中创建资源

```bash
kubectl apply -f xxxx.yaml
```

Kubernetes 集群搭建时候 创建资源的方式，有用到 过。



#### 5.1.2 命令行
用 命令行 在 kubernetes 中创建资源

```bash
# 查看所有命名空间
kubectl get ns
# 创建命名空间
kubectl create ns xxx
# 删除命名空间
kubectl delete ns xxx
```



### 5.2 NameSpace


**NameSpace（命名空间）**：用来对集群资源进行划分；默认只隔离资源，不隔离网络。 （<font style="color:#F5222D;">-n</font>）

![画板](Kubernetes/3d03b96454754bc0a231f078b2f86994.jpeg)





![](Kubernetes/ab84838acbea44969d29e74ec99a798d.png)

kube-nod-lease、kube-public、kube-system 命名空间在装 kubernetes 就带着，

kubernetes-dashboard 命名空间 在装了 dashboard 时候 才创建的。

![](Kubernetes/19b2b771b4ab4eb8b92e159909304049.png)



#### 5.2.1 命令行 创建
```bash
# 查看命名空间
kubectl get ns

# 查看 部署了哪些应用
kubectl get pods -A
# 查看 默认命名空间（default）部署的应用
kubectl get pods

# 查看 某个命名空间（kubernetes-dashboard）部署的应用
kubectl get pod -n kubernetes-dashboard

# 创建命名空间
kubectl create ns hello
# 删除命名空间
kubectl delete ns hello
```

![](Kubernetes/dd2da51363e44200a208edcc38f232a0.png)

![](Kubernetes/f0d49a395f1b489ead0c71bca240fbb1.png)



#### 5.2.2 yaml 创建
```yaml
# 版本号
apiVersion: v1
# 类型
kind: Namespace
# 元数据
metadata:
  # Namespace（命名空间） 的 名称
  name: hello-yaml
```

```bash
vi hello-yaml.yaml

# 将上述内容 粘贴进 hello-yaml.yaml

kubectl apply -f hello-yaml.yaml

kubectl get ns
# kubectl delete ns hello-yaml
# 配置文件创建的资源 用配置文件 删
kubectl delete -f hello-yaml.yaml

```

![](Kubernetes/3fa7ce7c58614378ad9a4c6ee5833ffc.png)



### 5.3 Pod


**Pod**：运行的一组容器，是 kubernetes 找那个应用的最小单位。（工作负载）

![画板](Kubernetes/a02939c62b1a4385bcf051e17bf6ea55.jpeg)

```bash
docker ps -a

kubectl get pod -A

# 查看 每个 Pod 分配的 IP，IP 范围 在下图配置的
kubectl get pod -owide
```

![查看 每个 Pod 分配的 IP 范围设置](Kubernetes/f022221a43c24ed3b7671f91ded303bb.png)

![](Kubernetes/21460eafa7424c59a19b5dbfc1dd7e6f.png)



#### 5.3.1 命令行 创建
```bash
# mynginx 是自定义的名称，nginx 镜像; 默认到 default 命名空间
kubectl run mynginx-k8s --image=nginx

kubectl get pods -A

# 默认 命名空间
kubectl get pods

# STATUS 是 ContainerCreating 的，查看进度（Events 属性）
kubectl describe pod mynginx-k8s

# 在 3 个 Xshell 终端中 （查看在哪个 节点服务器上 运行着）
docker ps

# 删除 Pod（默认的命名空间），kubectl delete pod mynginx-k8s -n 命名空间  （非默认命名空间）
kubectl delete pod mynginx-k8s

kubectl get pods
```

![](Kubernetes/4d607fb04c6d45ccbcba97ca89ab4c0e.png)

看到上面 一直 peding 状态，查看 Events

![查看 pending 原因](Kubernetes/57db6d7898b941a4aaafc2b0e8216e59.png)

[解决方案](https://blog.csdn.net/weixin_43114954/article/details/119153903)

```bash
# 执行
kubectl taint nodes --all node-role.kubernetes.io/master-
```

![解决 taint，nginx Pod 开始 Run](Kubernetes/88b6647a82364444b51f7ee1090fe88a.png)

![过了一会  nginx Pod  已 Running](Kubernetes/ebbd92b8dd414bcba60d992d37f4a01d.png)

![](Kubernetes/d4c89fe27b694271a57074bc399ced87.png)



#### 5.3.2 yaml 创建
```yaml
apiVersion: v1
kind: Pod
metadata:
  labels:
    run: mynginx-yaml
  # Pod 的 名字  
  name: mynginx-yaml
  # 指定命名空间 （不写是 默认 default）
  namespace: default
spec:
  containers:
  # 多个 容器，多个 - 
  - image: nginx
    # 容器的名字
    name: mgninx-yaml
```

```bash
vi mynginx-yaml.yaml

# 将上述内容 粘贴进来

kubectl apply -f mynginx-yaml.yaml

kubectl get pod
kubectl describe pod mynginx-yaml

# 配置文件创建的资源 用配置文件 删
kubectl delete -f mynginx-yaml.yaml
```

![](Kubernetes/61f95bbac48242d6967a649e81f14698.png)



#### 5.3.3 可视化界面 创建
> 小知识： 左边菜单 有 N 标注的， 是需要 命名空间的。
>

![](Kubernetes/99f022bca3bf403e83f1015b580eecca.png)



##### 5.3.3.1 Pod nginx
![可视化页面 上传 yaml 创建 Pod](Kubernetes/3f0251da6d6d46fa9fdf88d503b14d61.png)

![来到 Pod栏 查看 创建的 Pod](Kubernetes/56560771dfc04986982c9ab448b8698b.png)

![命令行 查看](Kubernetes/194433bd2432464aa086c94855932f26.png)

```bash
kubectl get pods -A -owide

# mynginx-yaml 是 Pod Name
kubectl exec -it mynginx-yaml 是 Pod Name -- /bin/bash

whereis nginx
cd /usr/share/nginx/html/
echo "hello k8s ngnix" > index.html
# Ctrl P + Q 退出

# 查看的 映射的IP，输出 hello k8s ngnix
curl 192.168.109.68:80
```

![](Kubernetes/6d52402adf5a4a72892a83c052ccf01c.png)



可以在 dashboard 页面，右边， 下拉选点击 执行。

![](Kubernetes/b99021838c294416b7086da13b3a0070.png)

```bash
echo "hello k8s ngnix dashboard" > index.html
```

![dashboard 页面 进入终端 修改](Kubernetes/5e940e56610449f781ba294e652faf9c.png)

![访问 nginx 首页，修改成功](Kubernetes/3e87b77eae0149ed835574ed6c5a9bfc.png)



###### 总结
k8s 给 每一个pod 分配 IP，IP网段是在<font style="color:#E8323C;"> 3.3.5.2 初始化主节点中 </font>中 设置的

> --pod-network-cidr=192.168.0.0/16
>

<font style="color:#E8323C;">集群中的任意一个机器以及 任意的应用 都能通过 Pod 分配的 IP 来访问 Pod，只能在集群内访问，</font>

<font style="color:#E8323C;">如果要在集群外访问，暴露 k8s 端口 和 安全组。</font>

<font style="color:#E8323C;"></font>

##### 5.3.3.2 Pod nginx + tomcat
<font style="color:#E8323C;"></font>

下面是 1 个 Pod 里面 有 2 个 容器（nginx 和 tomcat）

```yaml
apiVersion: v1
kind: Pod
metadata:
  labels:
    run: myapp
  # Pod 的 名字  
  name: myapp
  # 指定命名空间 （不写是 默认 default）
  namespace: default
spec:
  containers:
  # 多个 容器，多个 - 
  - image: nginx
    # 容器的名字
    name: myngnix
  - image: tomcat:8.5.68
    name: mytomcat
```

```bash
vi multicontainer-pod.yaml

# 将上述内容 粘贴进来

kubectl apply -f multicontainer-pod.yaml

kubectl get pod
kubectl describe pod myapp

# 配置文件创建的资源 用配置文件 删
kubectl delete -f multicontainer-pod.yaml

```

![Ready 是 0 / 2了。 有2个 容器（nginx 和 tomcat）](Kubernetes/31a07ab890ba42008e7ee31579359004.png)

```bash
kubectl get pod 
kubectl get pods -owide

curl ip:80
curl ip:8080  # 这个会404，但后面是 apache tomcat 打印出来的
```

![稍后， Status 变成 Running，Ready 是 2 / 2](Kubernetes/0c879c4304b64a3e8975691ed679eafd.png)

![](Kubernetes/8e899f39803741a18ac8e386eb827d26.png)

在 同一个 Pod 共享一个 网络空间、共享网络存储。

在 可视化界面上， ngnix 中 curl 127.0.0.1:8080；tomcat 中 127.0.0.1:80 ；都是可以的。

![nginx 中访问](Kubernetes/c823dab442934ea791816fa50b1e873e.png)

![tomcat 中访问](Kubernetes/4f4437f84c114caeabf3df8b1179a4d6.png)



##### 5.3.3.3 Pod 2ngnix
```yaml
apiVersion: v1
kind: Pod
metadata:
  labels:
    run: myapp-2
  # Pod 的 名字  
  name: myapp-2
  # 指定命名空间 （不写是 默认 default）
  namespace: default
spec:
  containers:
  - image: nginx
    name: myngnix01
  - image: nginx
    name: myngnix02
```

![创建 Pod](Kubernetes/bdf22b367922444fa70fd1fdb9b1472c.png)

![查看 Pod 列表](Kubernetes/a147940444ae46a0873639d2929dfeb1.png)

![第二个 启动失败](Kubernetes/0de24ab370a24bdbbc328160265eb03a.png)

![显示 1 / 2](Kubernetes/fdf9437469b847e183bbd0c566f967aa.png)

过了一会， 1 / 2   状态是 ERROR， 端口冲突了

```bash
kubectl get pods

# 看 Events 轨迹
kubectl describe pod myapp-2
```

![](Kubernetes/09519233b4e24fc789f9d04729f37c1f.png)

![dashboard 页面 也可以看到 Events](Kubernetes/037c56ee3bc94a3fbdf803feb7fda3f1.png)



![查看 Pod 日志](Kubernetes/0d657365bb6b47699d8a41956daf3062.png)

![查看 mynginx02 启动日志，发现 80端口被占用](Kubernetes/792d9f60332a4a0689f1bb15c0ee935a.png)

然后把这个 删掉，用 <font style="color:#F5222D;">deployment </font>解决。

![删掉 Pod myapp-2](Kubernetes/930ddbba9d674d9785951ca14bb5a0ca.png)



### 5.4 Deployment ★


**Deployment**：控制 Pod，使 Pod 拥有<font style="color:#F5222D;">多副本</font>、<font style="color:#F5222D;">自愈</font>、<font style="color:#F5222D;">扩缩容</font> 等能力。（工作负载）



#### 5.4.1 自愈 和 故障转移
<font style="color:#E8323C;">应用</font>：某一个 节点宕机后，用 Deployment 部署的容器 会在 另一个 节点 重新启动。



##### 5.4.1.1 自愈
```bash
# 删除之前的 Pod
kubectl get pods
kubectl delete pod xxx xxx -n default

kubectl run mgnginx --image=nginx
kubectl create deployment mytomcat --image=tomcat:8.5.68
kubectl get pods

# 删了就没了
kubectl delete pod mgnginx -n default
# 删了之后，以一个 新的 ID 重新启动了；用 delete 删不掉的
kubectl delete pod mytomcat-xxx -n default

# 查看 deployment 创建的资源
kubectl get deploy

# 删除 deployment 创建的资源
kubectl delete deploy mytomcat
```

![删除之前的 Pod](Kubernetes/0bc8219c212146f2a6513106de8daff8.png)

![deployment 创建的 Pod  用 kubectl delete 删不掉的，会自愈](Kubernetes/550537dc3c3d4e04aa72661bb41f9200.png)

![删除 deployment 创建的 Pod，kubectl delete deploy xxx](Kubernetes/766e888d921e465cadc27a19640024d7.png)

![](Kubernetes/fd54fc01715d493bad419f43e0ce2e1d.png)



##### 5.4.1.2 故障转移
```bash
kubectl create deployment mytomcat --image=tomcat:8.5.68
kubectl get pod -owide

docker ps | grep xxx

docker stop xxx
```

Restarts，重启次数 + 1。

![查看 Pod 所在 的 node](Kubernetes/ec0c392afb2d46f3914cd68992c749aa.png)

![到 所在的 node 服务器 docker stop，回到 master 可以看到 restart + 1, 然后 接着 Running](Kubernetes/9e77e1b2e0a94f48aa96d6c7a14f303f.png)



把 部署节点 的服务器关机，强制关机。 

```bash
# 动态
kubectl get pod -w
```

![把 一开始所在的 node2 关机后，又在 node1 启了一个。](Kubernetes/e3112e3977374ee0b6cb94c1f9277aa0.png)

这样子，k8s 天天 杀 Pod，起 Pod；可以设置一个时间 2min / 5min 等。





#### 5.4.2 多副本
<font style="color:#52C41A;">--replicas</font>：一次性部署多个 Pod。



##### 5.4.2.1 命令行 创建
```bash
# --replicas=3，部署 3个 Pod my-dep
kubectl create deployment my-dep --image=nginx --replicas=3

kubectl get deploy
```

![](Kubernetes/677cdb7d8c1f4f3b87a120812bb68cee.png)

![](Kubernetes/cf91c3fab7a94327a7aa8dcab6d58aad.png)

![](Kubernetes/287c8c4644974d359a68494d4b24a20f.png)



##### 5.4.2.2 yaml 创建
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: my-dep
  name: my-dep
spec:
  replicas: 3
  selector:
    matchLabels:
      app: my-dep
  template:
    metadata:
      labels:
        app: my-dep
    spec:
      containers:
      - image: nginx
        name: mynginx
```

![](Kubernetes/c340d372f69d4ca8ac5d71cd4e89051f.png)

![](Kubernetes/e2ec3e566ac3474f82acb9b9144dfb42.png)

![](Kubernetes/a3f0be3dec314e6f809c4cb7ce96e7e2.png)

![](Kubernetes/b075032a2bba4b9a8de61ebabc376e5b.png)



##### 5.4.2.3 可视化界面 创建
![](Kubernetes/e923220e68fa4dfa91557a318a838804.png)

```bash
# 查看部署的 NODE 和 IP
kubectl get pod -owide

kubectl get deploy
```

![](Kubernetes/66667bccb36d42478aa1418904e5345b.png)

![](Kubernetes/82f347c3d256494ab0dc1bce2267530f.png)

![](Kubernetes/cd45461c57f64e7e8c97102518f1ce73.png)



#### 5.4.3 扩缩容
<font style="color:#52C41A;">kubectl scale</font>

![画板](Kubernetes/adf1026a2f404e8faf3c6abbc2687d06.jpeg)

上图举出了 扩容，缩容类似。<font style="color:#E8323C;"> Kubernetes 可以 动态的 对 Pod 扩缩容。</font>

```bash
kubectl get deploy
kubectl get pod -owide

# 扩容
kubectl scale deploy/my-dep --replicas=5

kubectl get pod -owide

# 缩容
kubectl scale deploy/my-dep --replicas=2

kubectl get pod -owide
kubectl get deploy

# 也可以通过 修改 yaml 中的 replicas 来达到 扩缩容
kubectl edit deploy my-dep

kubectl get pod
kubectl get deploy
```

![将 deploy my-dep 由 3 扩容到 5](Kubernetes/66c76258bae14df1ad2d7b0afcb5570d.png)

![将 deploy my-dep 由 5 缩容到 3](Kubernetes/e624622ba029416b926fd939826425ac.png)

![修改 yaml 中的 replicas 来达到 扩缩容](Kubernetes/0dc5f8e5e10649a89e6ba5d0f20d9fff.png)

![可视化界面 扩缩容](Kubernetes/c5f41a04470a4f2faa9297c22999f705.png)

![可视化界面 扩缩容](Kubernetes/17b4e7d4015340a8a753268a69796045.png)



#### 5.4.4 滚动更新
类似于<font style="color:#52C41A;"> 灰度发布</font>，先启动 一个新的 Pod，然后将旧的 Pod 下线。

```bash
# 查看之前用的镜像名称（spec.container.name）
kubectl get deploy my-dep -oyaml

# 改变 my-dep 中 nginx 的版本 （最新 -> 1.16.1）   --record 在 rollout histroy 中记录
kubectl set image deployment/my-dep mynginx=nginx:1.16.1 --record
kubectl rollout status deployment/my-dep
```

![当前 deploy](Kubernetes/4d748b99be7e4a2e8cd86db335bd0369.png)

![查看 spec.containers.name，此时镜像是 nginx](Kubernetes/ac4caed9fb924bcdb6eb76b9ab1aacb6.png)

![更换 nginx 镜像版本](Kubernetes/8c95fcc0d1df459dba6f64e64949a83d.png)



用  kubectl get pod -w 观察过程

之前那旧的 还在，先准备 新的。 可以看到 新的 先 Running 后 在 Terminating 旧的。

![新开一个选项，用  kubectl get pod -w 观察过程](Kubernetes/f229c5bed7aa4773865be6e3010fe0dd.png)

![查看 spec.containers.name，镜像改成 nginx:1.16.1](Kubernetes/15467bf286e24dd9880ac710a2af5f63.png)



#### 5.4.5 版本回退
```bash
# 历史记录
kubectl rollout history deployment/my-dep

# 查看某个历史详情
kubectl rollout history deployment/my-dep --revision=2

# 回滚（回到上个版本）
kubectl rollout undo deployment/my-dep

# 观察过程，也是 先 启动新的，在终止旧的
kubectl get pod
kubectl get pod -w

# 查看 nginx 版本
kubectl get deploy/my-dep -oyaml | grep image

# 回滚（回到指定版本）
kubectl rollout undo deployment/my-dep --to-revision=2
```

![查看历史记录](Kubernetes/1f5b503a108b42d597bf94ef82cafd9a.png)

![my-dep 回退到 上个版本](Kubernetes/b216c5e8ae20466db1c63f64a606ab6d.png)

![kubectl get pods -w 观察过程](Kubernetes/cf4c8e2413bf498d9bab603b7eef6629.png)

![查看结果，上面 写错了，应该是 nginx:1.16.1](Kubernetes/49c033e8a96e4b6ab1005cfdde186de7.png)

![](Kubernetes/df44294d2c224431a825c2a0a2bfd283.png)



#### 5.4.6 其他工作负载
除了Deployment，k8s 还有 `StatefulSet` 、`DaemonSet` 、`Job`  等 类型资源，我们都称为 `工作负载`

有状态应用使用  `StatefulSet`  部署，无状态应用使用 `Deployment` 部署。

> <font style="color:#E8323C;">无状态的 进容器，有状态的 物理部署（如Mysql、Redis等）</font>
>

官网文档：[https://kubernetes.io/zh/docs/concepts/workloads/controllers/](https://kubernetes.io/zh/docs/concepts/workloads/controllers/)

![](Kubernetes/11872f5be9d24707a8b6de52cc7d1bd7.png)

![可视化页面 — 工作负载](Kubernetes/d6829d4f90a84fdcbdaeabef8472d47f.png)



### 5.5 Service ★
**<font style="color:#000000;">Service</font>**：Pod 的服务发现 与 负载均衡；将一组 Pods 公开为 网络服务的抽象方法。（服务）

![画板](Kubernetes/a359eaeea15f4fe9a0033d944fc1932f.jpeg)



#### 5.5.1 ClusterIP
ClusterIP 分配的 IP 在 这里配置。

![](Kubernetes/24e0813bc4e5449a8f5703deb0a33594.png)

<font style="color:#52C41A;">kubectl expose：</font> 暴露端口，只能在集群内部 通过 ClusterIP 访问。



![deploymenet](Kubernetes/1333682ad21e44d5a1c19a353c91aa5f.png)

```bash
cd /usr/share/nginx/html
echo '1111' > index.html
cat index.html
```

![启动的3个 pod，分别 修改 index.html 演示负载均衡 。](Kubernetes/eb9391d0a6594a0d9caf47e1d25dffab.png)

![](Kubernetes/4e96661eff5b4f7f82db803b887355de.png)

![](Kubernetes/047e3883e9ed4ea79b5bacb9edb8afb7.png)

![](Kubernetes/0a2d2e4481fd4d66bee38132749badd9.png)



##### 5.5.1.1 命令行 创建
```bash
# kubectl expose 暴露端口，只能在集群内部 ClusterIP 访问。--type=ClusterIP 不传默认就是 ClusterP，target-port 目标端口（源端口） 
kubectl expose deploy my-dep-02 --port=8000 --target-port=80

# 查看 service，里面有 CLUSTER-IP
# kubectl get service 或者 kubectl get svc
kubectl get service

# 查看 pod 标签
kubectl get pod --show-labels

# 删除
kubectl delete svc my-dep-02
```

![创建 service 体验 负载均衡](Kubernetes/3daf9d2ff3794a53a4df90dbe84d22a0.png)

![cluster-ip 在集群内都可以调用，无法在外网访问。](Kubernetes/b5606ea4e2924185b96456743c3369d9.png)

![cluster-ip 在集群内都可以调用，无法在外网访问。](Kubernetes/14320024760246ff9e8fe325febf4ecb.png)

![删除 service](Kubernetes/96f7e07820b14fb0b0bd9a3d85a1f315.png)



##### 5.5.1.2 可视化界面 创建
```yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    k8s-app: my-dep-02
  name: my-dep-02
spec:
  selector:
    k8s-app: my-dep-02
  ports:
    - port: 8000
      protocal: TCP
      targetPort: 80
```

![](Kubernetes/18b8c55db60a4e728349b122fcfff76a.png)

![](Kubernetes/d94bebc7e00247979d2e2dc8aa8993e2.png)



<font style="color:#F5222D;">域名：默认是 服务.所在命名空间.svc</font>， my-dep-02.default.svc



进入到 pod 内部 curl my-dep-02.default.svc:8000 也可以访问。

这个域名只能在 <font style="color:#E8323C;"> pod 内</font>可以访问。

![](Kubernetes/5d4a91cd2040488e8190519c82b8cbd1.png)

![](Kubernetes/0dad40abada14da7bb50800abbc6abff.png)

如果在 外部，访问 域名是不行的。 但是访问 IP 是可以的。

![](Kubernetes/3c882c966fe347bbacb241d72ff66edb.png)





在可视化界面3 个 缩到 2个 nginx， 在访问 测试 负载均衡。在恢复到3个，测试 负载均衡。

**结论剧透**：1111、2222、初始的index（不是之前的 3333）， <font style="color:#F5222D;">这里 并没有 持久化存储</font>。

![缩放到 2个Pod](Kubernetes/dff3638981184723bb0ef58705cbe79f.png)

![](Kubernetes/9c1f300b47364dd4989a8b54d58aedb2.png)

![在扩容到 3个Pod](Kubernetes/c2b0a40465c344ba86c1a89c33803ccc.png)

![](Kubernetes/96e2502672f84d4cae8a11b53bb631ae.png)

**结论**：1111、2222、初始的index（不是之前的 3333）， <font style="color:#F5222D;">这里 并没有 持久化存储</font>。



#### 5.5.2 NodePort
可以在 公网 访问。<font style="color:#E8323C;"> 这个缺点：暴露出来的端口是 随机的。</font>

```bash
# 只能集群内部访问（--type不写 默认 ClusterIP）
kubectl expose deploy my-dep-02 --port=8000 --target-port=80 --type=ClusterIP

# 集群外部也可以访问
kubectl expose deploy my-dep-02 --port=8000 --target-port=80 --type=NodePort

# 查看
kubectl get svc
```

![](Kubernetes/865bdd9240024394852f8e660a8a5931.png)



每个 pod 都开放了 <font style="color:#F5222D;">31951 </font>端口。（如果有，则安全组开放）

然后 任何一台 公网的 IP:30948 即可发 负载均衡的访问。

![](Kubernetes/408fbbcde77b4d1e8ba69e4f800782da.png)

![](Kubernetes/b581ee361c0b4328a6050521e25364db.png)





### 5.6 Ingress ★
**Ingress**：Service 的统一网关入口，底层就是 nginx。（服务）



官网地址：[https://kubernetes.github.io/ingress-nginx/](https://kubernetes.github.io/ingress-nginx/) （<font style="color:#E8323C;">都是从这里看的</font>）



所有的请求都先通过 Ingress，由 Ingress 来 打理这些请求。类似微服务中的 网关。

![](Kubernetes/cfeb1e99e2d24d53870373da54c27073.png)

#### 5.6.1 安装 Ingress
```bash
wget https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v0.47.0/deploy/static/provider/baremetal/deploy.yaml

# 修改镜像
vi deploy.yaml
# 将 image 的值改为如下值
registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/ingress-nginx-controller:v0.46.0

# 安装资源
kubectl apply -f deploy.yaml

# 检查安装的结果
kubectl get pod,svc -n ingress-nginx

# 最后别忘记把 svc 暴露的端口 在安全组放行
```

![](Kubernetes/d5d6e14858d94f7786683be673d4d931.png)

![输入/image 定位，修改 image 的值](Kubernetes/e9caa43ccbd04b92a1bc18662eefa48e.png)

![](Kubernetes/cf30ff105038444d9e3d0effe8a947fd.png)

![终于尼玛起来了。搞了一下午（2022/05/16）](Kubernetes/386d6d362fe34c6d9764356d754fc854.png)

![查看 映射后的 IP](Kubernetes/9dd3151c90f64fc7a6ee331e3bc12022.png)

[http://192.168.27.251:31065/](http://192.168.27.251:31065/)  访问后，返回的错误 是nginx 返回的。

![](Kubernetes/066c3b41dad8499a83a9c030385dc8fc.png)



##### Bug：安装失败
![](Kubernetes/3a3e0d7723cb449d906065dee2ad0a5b.png)

GitHub上也有很多人提过：[https://github.com/kubernetes/ingress-nginx/issues/5932](https://github.com/kubernetes/ingress-nginx/issues/5932)



笔者弄了一下午，终于解决了，再次记录一下。若想直接解决，请直接跳转 3. token Name + hostnetwork



###### 1. 只修改 token Name
既然 报 找不到 那个密钥，就把密钥修改成 那个名字。

![](Kubernetes/73b367b29b964f80b25f91d638d1fa19.png)

![可以看到没分配到 IP](Kubernetes/3015f6d5165d4a20aead1716924d4880.png)

![这里的 secret 后面带着 -token-随机数](Kubernetes/e13d46b015d54bc4a949633bb3237cdf.png)

![修改 deploy 配置里的 secretName](Kubernetes/8044640fee694ef8a3ebf5829e87c4c0.png)

![没启动成功](Kubernetes/d70bd33c4fa5462aa502eef8b9bbcbce.png)

![](Kubernetes/80762a05dbf048ce8843ef59ab52ca9a.png)

```bash
kubectl delete -f xxx.yaml
```

###### 2. hostnetwork
![](Kubernetes/20ef6b364dca4f9084d9bf6381be925d.png)

![可以看到 分配IP了，依旧没有 Running](Kubernetes/0f1c815e9b8b4ecaa74cc46548400f9f.png)

![dashboard 也还是报错](Kubernetes/f00b70dfeaca47328126ad7f4dc59cbc.png)

```bash
kubectl delete -f xxx.yaml
```

###### 3. token Name + hostnetwork
![](Kubernetes/20ef6b364dca4f9084d9bf6381be925d.png)

![](Kubernetes/7ea4320fd0624d03a887cba4fb73e3a8.png)

![](Kubernetes/b428c9eeec044a32acf523949ca10953.png)

![成功启动](Kubernetes/52368a23f87b46b6a925da7e30c6d131.png)



#### 5.6.2 域名访问
```bash
kubectl get svc -A

kubectl get nodes
```

![](Kubernetes/1ea407108ef847ea9b9d408a591cf6fb.png)



每台服务器 都开放  映射后的 端口：31065、32541

![Ingress -> service -> pod](Kubernetes/312c1b4d4ac24b5192e707cf04997412.png)

##### 5.6.2.1 搭建 Service
```bash
vi test.yaml

# 复制下面

kubectl apply -f test.yaml
```

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hello-server
spec:
  replicas: 2
  selector:
    matchLabels:
      app: hello-server
  template:
    metadata:
      labels:
        app: hello-server
    spec:
      containers:
      - name: hello-server
        image: registry.cn-hangzhou.aliyuncs.com/lfy_k8s_images/hello-server
        ports:
        - containerPort: 9000
---
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: nginx-demo
  name: nginx-demo
spec:
  replicas: 2
  selector:
    matchLabels:
      app: nginx-demo
  template:
    metadata:
      labels:
        app: nginx-demo
    spec:
      containers:
      - image: nginx
        name: nginx
---
apiVersion: v1
kind: Service
metadata:
  labels:
    app: nginx-demo
  name: nginx-demo
spec:
  selector:
    app: nginx-demo
  ports:
  - port: 8000
    protocol: TCP
    targetPort: 80
---
apiVersion: v1
kind: Service
metadata:
  labels:
    app: hello-server
  name: hello-server
spec:
  selector:
    app: hello-server
  ports:
  - port: 8000
    protocol: TCP
    targetPort: 9000
```

![](Kubernetes/b1aa2c633c43405a977abfd8ccc7fd02.png)

![](Kubernetes/270da2ee63064b77b067201b3b6712bf.png)



##### 5.6.2.2 配置 Ingress
```bash
vi ingress-rule.yaml

# 复制下面配置

kubectl apply -f ingress-rule.yaml

# 查看 集群中的 Ingress
kubectl get ingress
```

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress  
metadata:
  name: ingress-host-bar
spec:
  ingressClassName: nginx
  rules:
  - host: "hello.atguigu.com"
    http:
      paths:
      - pathType: Prefix
        path: "/"
        backend:
          service:
            name: hello-server
            port:
              number: 8000 # hello-server （service） 的端口是 8000
  - host: "demo.atguigu.com"
    http:
      paths:
      - pathType: Prefix
        path: "/"  # 把请求会转给下面的服务，下面的服务一定要能处理这个路径，不能处理就是404
        backend:
          service:
            name: nginx-demo  #java，比如使用路径重写，去掉前缀nginx
            port:
              number: 8000


apiVersion: networking.k8s.io/v1
kind: Ingress  
metadata:
  name: ingress-host-bar
spec:
  ingressClassName: nginx
  rules:
    - host: "hello.atguigu.com"
      http:
        paths:
          - pathType: Prefix
            path: "/"
            backend:
              service:
                name: hello-server
                port:
                  number: 8000 # hello-server 的端口是 8000
    - host: "demo.atguigu.com"
      http:
        paths:
          - pathType: Prefix
            path: "/"
            backend:
              service:
                name: nginx-demo
                port:
                  number: 8000
```



![报错了](Kubernetes/09229d3466674907bad46170d5a5f24a.png)

```bash
kubectl get ValidatingWebhookConfiguration
# 把该死的 admission 删掉
kubectl delete -A ValidatingWebhookConfiguration ingress-nginx-admission
```

![](Kubernetes/6d367905989840ca966968e92286dac7.png)



##### 5.6.2.3 测试
在 自己电脑（不是虚拟机） hosts 中增加映射：

master的公网IP hello.atguigu.com

master的公网IP demo.atguigu.com

![](Kubernetes/d3cef607acfb404e82b259f0d4aa840c.png)

![](Kubernetes/498367e75d6e459ea0f0941f46cf5d47.png)

<font style="color:rgb(77, 77, 77);">生效：Win+R->cmd->ipconfig /flushdns</font>



![查看映射的 IP](Kubernetes/0654142870854a8cb3569acfe2c00da4.png)

浏览器：

hello.atguigu.com:31065

demo.atguigu.com:31065

![](Kubernetes/2c8bf92b6c754ce99dec3cd2e8d7f9c4.png)

![](Kubernetes/4779fc5fdce343d4924b2436d459a01e.png)

##### 5.6.2.4 测试II
```bash
# kubectl get ingress
kubectl get ing

kubectl edit ing ingress的NAME -n

# 改变匹配的 path
  - host: "demo.atguigu.com"
    http:
      paths:
      - pathType: Prefix
        path: "/nginx" # 匹配请求 /nginx 的，并且查找 nginx 文件.
        backend:
          service:
            name: nginx-demo
            port:
              number: 8000
```

![](Kubernetes/fb5151f8fd13442688877a78316ce922.png)

![之前](Kubernetes/a6cb1c6952b6444f81fd8b32a0bf166b.png)

![修改 path](Kubernetes/0d6e9490880143ab83f1670497e7a1df.png)

![随便写 /xxx 不匹配 nginx的，都返回 Ingress的 404的nginx](Kubernetes/f3cf25ab46204862abbccedcfb8d9d79.png)

![这个是 通过了 Ingress，Service 里的 Pod 没匹配到，才返回的 404（下面打印的 nginx 版本不一样的）](Kubernetes/a872535564f54299957896f49e56613e.png)



页面 进入 Pod 的那个nginx

```bash
cd /usr/share/nginx/html
ls
echo "hello cgxin" > nginx
```

![](Kubernetes/846eca0d036c4fff8b25f1424c6b0ffd.png)

![](Kubernetes/1d399f324a0a45ff817f63a439688c69.png)

![](Kubernetes/ef3212ad097348c8a6fcff92c71236b8.png)

![](Kubernetes/46b62950f2264916aacd973274d7c282.png)



#### 5.6.3 路径重写
这样子 就和 SpringCloud Gateway 网管转发一样的效果了

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress  
metadata:
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /$2
  name: ingress-host-bar
spec:
  ingressClassName: nginx
  rules:
  - host: "hello.atguigu.com"
    http:
      paths:
      - pathType: Prefix
        path: "/"
        backend:
          service:
            name: hello-server
            port:
              number: 8000
  - host: "demo.atguigu.com"
    http:
      paths:
      - pathType: Prefix
        path: "/nginx(/|$)(.*)" 
        backend:
          service:
            name: nginx-demo  
            port:
              number: 8000
```

![或者 在可视化界面 创建](Kubernetes/318058beb6ef4b85aa0cc025dab29046.png)

![](Kubernetes/7c1d7343859941ea8767b2d601e04806.png)

![](Kubernetes/2c90d72256de44a897ba8dbeac7d105c.png)



#### 5.6.4 限流
官网文档：[https://kubernetes.github.io/ingress-nginx/user-guide/nginx-configuration](https://kubernetes.github.io/ingress-nginx/user-guide/nginx-configuration)

![](Kubernetes/065c819369db46d7bdb3de876ee4e8ed.png)

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ingress-limit-rate
  annotations:
    # 限流
    nginx.ingress.kubernetes.io/limit-rps: "1"
spec:
  ingressClassName: nginx
  rules:
  - host: "haha.atguigu.com"
    http:
      paths:
      - pathType: Exact
        path: "/"
        backend:
          service:
            name: nginx-demo
            port:
              number: 8000
```

```bash
vim ingress-rule-2.yaml

# 复制上面配置

kubectl apply -f ingress-rule-2.yaml

kubect get ing
```

![或者 在可视化界面 创建](Kubernetes/38b0166926f84e87943d73e412f898d5.png)

![](Kubernetes/a558afd9fbfd485084364f66c8557239.png)



在 自己电脑（不是虚拟机） hosts 中增加映射：

公网IP haha.atguigu.com

![](Kubernetes/d3cef607acfb404e82b259f0d4aa840c.png)

![](Kubernetes/4d6a064519b94283a1b4c9190b96fbce.png)

<font style="color:rgb(77, 77, 77);">生效：Win+R->cmd->ipconfig /flushdns</font>



##### 5.6.4.1 测试
![](Kubernetes/acf9359d878e41a5b20f50eeb47087b9.png)

[http://haha.atguigu.com:31065/](http://haha.atguigu.com:31065/)

![](Kubernetes/fdcbda13817d45dc85b31d21da6e5468.png)



刷新过快 返回 503，官网文档也写了。

![刷新过快，返回 503](Kubernetes/1e4f508507d14e46ae40fbe44f4633a5.png)

![](Kubernetes/dab7ea48d9d142e598a8b8902afae864.png)

![中文](Kubernetes/4de2815d930442bf847b87feb0814384.png)

也可以参照 官网文档 去修改 限流返回的内容。（老师这里没讲）



---

## 06. Kubernetes 存储抽象
类似于 Docker 中的 挂载。但要考虑 自愈、故障转移 时的情况。

![](Kubernetes/173007753793446ebbc86873b9d2d659.png)



### 6.1 NFS 搭建
网络文件系统

![](Kubernetes/fefdb62ae34f4fd9ac0f365c87664fff.png)



```bash
# 所有机器执行
yum install -y nfs-utils

# 只在 mster 机器执行：nfs主节点，rw 读写
echo "/nfs/data/ *(insecure,rw,sync,no_root_squash)" > /etc/exports

mkdir -p /nfs/data
systemctl enable rpcbind --now
systemctl enable nfs-server --now

# 配置生效
exportfs -r
```

![](Kubernetes/bee9d45840864488bcd2db0ca3ef2a9d.png)

```bash
# 查看
exportfs
```

![](Kubernetes/dce86a03a5f44879861423735bc5bc27.png)

```bash
# 检查，下面的 IP 是master IP
showmount -e 192.168.27.251

# 在 2 个从服务器 执行，执行以下命令挂载 nfs 服务器上的共享目录到本机路径 /root/nfsmount
mkdir -p /nfs/data

# 在 2 个从服务器执行，将远程 和本地的 文件夹 挂载
mount -t nfs 192.168.27.251:/nfs/data /nfs/data

# 在 master 服务器，写入一个测试文件
echo "hello nfs server" > /nfs/data/test.txt

# 在 2 个从服务器查看
cd /nfs/data
ls

# 在 从服务器 修改，然后去 其他 服务器 查看，也能 同步
```

![](Kubernetes/0790a500e74546a8a2043efcd2415a6c.png)

![](Kubernetes/e38d153a4cb745d0bed7d43ed3a0912e.png)

![](Kubernetes/391e1a04b0b44912a536f078d1d4064f.png)



### 6.2 原生方式 数据挂载
在 /nfs/data/nginx-pv 挂载，然后 修改， 里面 两个 Pod 也会 同步修改。

问题：<font style="color:#E8323C;">删掉之后，文件还在，内容也在，是没法管理大小的。</font>

![](Kubernetes/723959f63a744ce8976df62abeaceaad.png)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: nginx-pv-demo
  name: nginx-pv-demo
spec:
  replicas: 2
  selector:
    matchLabels:
      app: nginx-pv-demo
  template:
    metadata:
      labels:
        app: nginx-pv-demo
    spec:
      containers:
      - image: nginx
        name: nginx
        volumeMounts:
        - name: html
          mountPath: /usr/share/nginx/html # 挂载目录
      volumes:
        # 和 volumeMounts.name 一样
        - name: html
          nfs:
            # master IP
            server: 192.168.27.251
            path: /nfs/data/nginx-pv # 要提前创建好文件夹，否则挂载失败
```

```bash
cd /nfs/data
mkdir -p nginx-pv
ls

vi deploy.yaml

# 复制上面配置

kubectl apply -f deploy.yaml

kubectl get pod -owide
```

![](Kubernetes/295bd64e184a4f91891cb3d038a75f9d.png)

![](Kubernetes/ecc13131f6ab4290b362a9565fd1b5af.png)



```bash
cd /nfs/data/
ls
cd nginx-pv/
echo "cgxin" > index.html

# 进入 pod 里面查看
```

![在主机上 挂载文件，curl nginx 查看 index.html，说明 挂载成功了](Kubernetes/bde3ba4b9831464e90fd817ef5d9bb93.png)

![进入 pod 查看](Kubernetes/8c3cccedfaec4c46b194535a136007bf.png)

![](Kubernetes/fa63d81646104880b10705c97aa531c6.png)

#### 问题：占用空间
![](Kubernetes/5ada30dab64d490db2a38376eccc3556.png)

<font style="color:#E8323C;">删掉之后，文件还在，内容也在，是没法管理大小的。</font>



### 6.3 PV 和 PVC ★
**PV**：持久卷（Persistent Volume），将应用需要持久化的数据保存到指定位置

**PVC**：持久卷申明（Persistent Volume Claim），申明需要使用的持久卷规格



挂载目录。ConfigMap 挂载配置文件。

<font style="color:#E8323C;">这里是 是 静态的， 就是自己创建好了 容量，然后 PVC 去挑。 还有 动态供应的，不用手动去创建 PV池子</font>。

![](Kubernetes/ed90f57bddc0453d8604d448fb8a281f.png)



#### 6.3.1 创建 PV 池
```bash
# 在 nfs主节点（master服务器） 执行
mkdir -p /nfs/data/01
mkdir -p /nfs/data/02
mkdir -p /nfs/data/03
```

![](Kubernetes/e1d08fe2789441cfbf8bce9f5781bab7.png)

```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv01-10m
spec:
  # 限制容量
  capacity:
    storage: 10M
  # 读写模式：可读可写
  accessModes:
    - ReadWriteMany
  storageClassName: nfs
  nfs:
    # 挂载 上面创建过的文件夹
    path: /nfs/data/01
    # nfs 主节点服务器的 IP
    server: 192.168.27.251
---
apiVersion: v1
kind: PersistentVolume
metadata:
  # 这个name 要小写，如 Gi 大写就不行
  name: pv02-1gi
spec:
  capacity:
    storage: 1Gi
  accessModes:
    - ReadWriteMany
  storageClassName: nfs
  nfs:
    path: /nfs/data/02
    # nfs 主节点服务器的 IP
    server: 192.168.27.251
---
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv03-3gi
spec:
  capacity:
    storage: 3Gi
  accessModes:
    - ReadWriteMany
  storageClassName: nfs
  nfs:
    path: /nfs/data/03
    # nfs 主节点服务器的 IP
    server: 192.168.27.251
```

```bash
vi pv.yaml

# 复制上面文件

kubectl apply -f pv.yaml

# 查看 pv， kubectl get pv
kubectl get persistentvolume
```

![](Kubernetes/fa953c3ed5524ff2882530f06b47f08f.png)



#### 6.3.2 创建、绑定 PCV
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: nginx-pvc
spec:
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      # 需要 200M的 PV
      storage: 200Mi
  # 上面 PV 写的什么 这里就写什么    
  storageClassName: nfs
```

```bash
vi pvc.yaml

# 复制上面配置

kubectl get pv

kubectl apply -f pvc.yaml

kubectl get pv

kubectl get pvc
```

![绑定了， 绑定了1G的，10M 不够，3G太大，就选择了 1G](Kubernetes/149db3c3bcd84cb9b77fe79c55898843.png)

![删除了，就 释放了（released），然后又运行了一遍，挂到3G的了](Kubernetes/79d120790cf5408cb2152fa9033ae4dc.png)



#### 6.3.3 创建 Pod 绑定 PVC
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: nginx-deploy-pvc
  name: nginx-deploy-pvc
spec:
  replicas: 2
  selector:
    matchLabels:
      app: nginx-deploy-pvc
  template:
    metadata:
      labels:
        app: nginx-deploy-pvc
    spec:
      containers:
      - image: nginx
        name: nginx
        volumeMounts:
        - name: html
          mountPath: /usr/share/nginx/html
      volumes:
        - name: html
          # 之前是 nfs，这里用 pvc
          persistentVolumeClaim:
            claimName: nginx-pvc
```

```bash
vi dep02.yaml

# 复制上面 yaml

kubectl apply -f dep02.yaml

kubectl get pod

kubectl get pv

kubectl get pvc
```

![](Kubernetes/e59ee72c34b94be7a8444b287d320c72.png)



测试：

![挂载后，测试](Kubernetes/2bb9a2cb1c3b4b518e32fd2bab10035b.png)

![进入 Pod 内部查看 同步的文件](Kubernetes/e23eb0a099024d89a47a6c725e2d52c7.png)

![成功](Kubernetes/ce23679a313d499c8f7583e8b4f95552.png)



### 6.4 ConfigMap ★
**ConfigMap**：抽取应用配置，并且可以自动更新。<font style="color:#E8323C;">挂载配置文件</font>， PV 和 PVC 是挂载目录的。



#### 6.4.1 redis示例
##### 1. 创建 ConfigMap
```bash
vi redis.conf
# 写
appendonly yes

# 创建配置，redis保存到k8s的etcd；
kubectl create cm redis-conf --from-file=redis.conf

# 查看
kubectl get cm

rm -rf redis.conf
```

![](Kubernetes/6dce5d66a2c74caa85176a49dfd72e29.png)



```bash
# 查看 ConfigMap 的 yaml 配置咋写的
kubectl get cm redis-conf -oyaml
```

![](Kubernetes/0de7e618494b486c825bcd9caac55f01.png)

```yaml
apiVersion: v1
data:    # data是所有真正的数据，key：默认是文件名   value：配置文件的内容（appendonly yes 是随便写的）
  redis.conf: |
    appendonly yes
kind: ConfigMap
metadata:
  name: redis-conf
  namespace: default
```



##### 2. 创建 Pod
```yaml
apiVersion: v1
kind: Pod
metadata:
  name: redis
spec:
  containers:
  - name: redis
    image: redis
    command:
      # 启动命令
      - redis-server
      # 指的是redis容器内部的位置
      - "/redis-master/redis.conf"  
    ports:
    - containerPort: 6379
    volumeMounts:
    - mountPath: /data
      name: data
    - mountPath: /redis-master
      name: config
  volumes:
    - name: data
      emptyDir: {}
    - name: config
      configMap:
        name: redis-conf
        items:
        - key: redis.conf
          path: redis.conf
```

![redis.conf 会放在 /redis-master 下](Kubernetes/47caf00006414fa59d10bd0d4200cda8.png)



```bash
vi redis.yaml

# 复制上面配置

kubectl apply -f redis.yaml

kubectl get pod
```

![](Kubernetes/ecd7e87e1b39432ab91678fcfb8ec039.png)



![页面中 进入刚才创建的 pod redis 内部](Kubernetes/328be647f12b488591572b642643b013.png)

![查看 redis.conf 配置文件 内容](Kubernetes/785f8cbab633433baad3501e16aae6d3.png)



```bash
kubectl get cm

# 修改配置 里 redis.conf 的内容
kubectl edit cm redis-conf
```

![修改 redis-conf 的 redis.conf 内容](Kubernetes/9feb9460d5044a0e90f0d30538306a67.png)

![修改 redis-conf 的 redis.conf 内容](Kubernetes/af21b1666f994ab89d2873723c78ff42.png)

![过了一会， 就同步了](Kubernetes/2e8312cece0f479e949bcf1f36946246.png)

##### 3. 检查默认配置
```bash
kubectl exec -it redis -- redis-cli

127.0.0.1:6379> CONFIG GET appendonly
127.0.0.1:6379> CONFIG GET requirepass
```

![还是原生 redis 的 默认配置，说明 配置值 没有同步，但 配置文件 已同步](Kubernetes/d09d033a180c4700b2e17a5e72a402fa.png)

![和 命令行一样的](Kubernetes/06a3dbb45e3d45c0b9fc7a881c428ead.png)

![删除，重新创建 Pod，更新 配置文件的 配置值](Kubernetes/2711e02d753f4b36aa453fb53717a59b.png)

![查看 更新的 配置值](Kubernetes/33257d80893f4784bbc6846f3724e3a6.png)

##### 总结：
+ 修改了 ConfigMap，Pod里面的配置文件会跟着同步。
+ 但配置值 未更改，需要重新启动 Pod 才能从关联的ConfigMap 中获取 更新的值。 Pod 部署的中间件 自己本身没有热更新能力。



### 6.5 Secret
**<font style="color:rgb(34, 34, 34);">Secret</font>**<font style="color:rgb(34, 34, 34);"> ：是对象类型，用来保存敏感信息，例如密码、OAuth 令牌和 SSH 密钥。 将这些信息放在 secret 中比放在 </font>[Pod](https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/)<font style="color:rgb(34, 34, 34);"> 的定义或者 </font>[容器镜像](https://kubernetes.io/zh/docs/reference/glossary/?all=true#term-image)<font style="color:rgb(34, 34, 34);"> 中来说更加安全和灵活。</font>



#### 6.5.1 拉取失败
[Docker hub](https://hub.docker.com/)<font style="color:rgb(34, 34, 34);"> 仓库中，自己的仓库设置成私有的。 然后去 下载私有的。下载不了（未登录）。</font>

![自己的仓库设置成私有的](Kubernetes/0882ab876ef14f268b86c731909d28f8.png)

![查看 拉取命令](Kubernetes/25a9ceac752e4d059fac032340de91a3.png)![拒绝拉取](Kubernetes/0f5ae8ec04344570bacdc9b64cdf2485.png)

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: private-cgxin-docker
spec:
  containers:
  - name: private-cgxin-docker
    image: cgxin/cgxin_docker:1.0
```

```bash
vi mypod.yaml

# 复制上面配置

kubectl apply -f mypod.yaml

kubectl get pod
```

![提示 镜像拉取失败](Kubernetes/cc40e2eb23924c9b86bc903875c49cd3.png)

![可视化界面 查看错误描述：也是没有权限。](Kubernetes/f6345a22493a486a84bb1e818466a3d8.png)

![删除配置文件 创建的错误 Pod](Kubernetes/bb8b76ffb83e42b4836d216711f5a2fb.png)

#### 6.5.2 创建 Secret
```bash
kubectl create secret docker-registry cgxin-docker-secret \
--docker-username=leifengyang \
--docker-password=Lfy123456 \
--docker-email=534096094@qq.com

##命令格式
kubectl create secret docker-registry regcred \
  --docker-server=<你的镜像仓库服务器> \
  --docker-username=<你的用户名> \
  --docker-password=<你的密码> \
  --docker-email=<你的邮箱地址>
```

```bash
# 查看
kubectl get secret

kubectl get secret cgxin-docker-secret -oyaml
```

![](Kubernetes/93a46c3bc8414c7d86013805d98ec75f.png)

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: private-cgxin-docker
spec:
  containers:
  - name: private-cgxin-docker
    image: cgxin/cgxin_docker:1.0
  # 加上 Secret  
  imagePullSecrets:
  - name: cgxin-docker-secret
```

```bash
vi mypod.yaml

# 复制上面配置

kubectl apply -f mypod.yaml

kubectl get pod
```

![使用 Secret 后，可以成功 拉取下来了。](Kubernetes/02fdd381f22b43bdbbce314cac760536.png)

![Docker Hub 镜像 复原成 public](Kubernetes/1ebab2074edf4b319f147b7a40423415.png)



### 总结：
![可视化界面 操作很方便](Kubernetes/f20bb33bfdb944ce875882ed2eef5539.png)



## 07. 记录


[雷神笔记](https://www.yuque.com/leifengyang/oncloud/ghnb83#TOwqu)



#### 网友评论
> <font style="color:rgb(34, 34, 34);">看完谷粒商城的架构篇 我就已经把nacos集群、sentinel集群、redis集群、mysql主从、rabbitmq集群、rocketmq集群、seata集群、zipkin集群、一股脑全都扔到了k8s里并自己保存好了yaml文件。</font>
>
> <font style="color:rgb(34, 34, 34);">但是后面来了个大神指点，说</font><font style="color:#E8323C;">没有自动扩容缩容的不能放到k8s里</font><font style="color:rgb(34, 34, 34);">，目前正在把redis集群和mysql主从迁出来。</font>
>
> <font style="color:rgb(34, 34, 34);"></font>
>
> <font style="color:rgb(34, 34, 34);">就是无状态进容器，有状态物理部吧。</font>
>



### 7.1 虚拟机重启后


```bash
systemctl stop firewalld
systemctl disable firewalld
```



#### 7.1.1 启动 Docker
```bash
# 3台服务器 启动 dokcer （因为 docker 没有开机运行）
systemctl start docker

# master 服务器 查看 k8s 集群节点状态
kubectl get nodes
```

![](Kubernetes/226877d3e9cb43a0a92d0cc61deff347.png)

![](Kubernetes/b46b955aff8847908c9cbd62e8cb5e58.png)

不得不说，k8s 的集群自我修复是真的牛。



#### 7.1.2 进入了紧急模式
<font style="color:#E8323C;">重启后 系统进入了 紧急模式，输入密码 进入命令行模式。 </font>[解决方案参考](https://www.58jb.com/html/swap-off-caused-system-boot-failure.html)

```bash
vi /boot/grub2/grub.cfg

# 删除 rd.lvm.lv=centos/swap，保存 然后重启.
```

![](Kubernetes/f0d937aab39d416299634de91c3b840b.png?x-oss-process=image%2Fwatermark%2Ctype_d3F5LW1pY3JvaGVp%2Csize_23%2Ctext_Y2d4aW4%3D%2Ccolor_FFFFFF%2Cshadow_50%2Ct_80%2Cg_se%2Cx_10%2Cy_10)

不要在 vmware上 关闭客户机， 要用 init 0 来关闭。

vmware 关闭客户机 每次都会进入了 紧急模式，然后 去删除上面命令，才能正常启动。

## 08. 卸载

```
yum remove -y kubelet kubeadm kubectl
 
kubeadm reset -f
modprobe -r ipip
lsmod
rm -rf ~/.kube/
rm -rf /etc/kubernetes/
rm -rf /etc/systemd/system/kubelet.service.d
rm -rf /etc/systemd/system/kubelet.service
rm -rf /usr/bin/kube*
rm -rf /etc/cni
rm -rf /opt/cni
rm -rf /var/lib/etcd
rm -rf /var/etcd
```

