## rainmaple_simpledfs

基于Socket简单实现的分布式文件系统
#### 项目简介

高级操作系统课程设计
1. server
2. client
3. nodes(storages)
  目前时间局限，自动化脚本尚未编写，需要导入idea中运行，并且需要maven3.0以上支持，jdk version> 1.8

#### 简单业务逻辑：

分布式文件系统涉及多个存储节点和充当协调器的服务端，这里的服务端类似于zookeeper 和 nameserver 在分布式文件架构中充当的作用。单独持有一个服务线程，并且封装了请求参数，服务线程根据请求参数对请求进行重定向，以Socket 作为进程之间通信的工具，将数据封装成 DataInputStream，DataOutputStream 来进行传输。由于请求类型多样，为了方便提供服务，实现了一个简单线程池。所有的服务线程从线程池中获取，FileServer 在运行过程中
充当一个协调器，和重定向的作用，将不同的请求分发给不同的线程去处理。

####  目前支持的功能如下：

![分布式文件系统](/Users/rainmaple/Downloads/分布式文件系统.png)

#### 使用说明

1. 将项目导入 i􏰂dea 中
2. 导入 m􏰁aven􏰄 依赖 

3. 为监控服务绑定 T􏰀􏰁omcat Server

4. 分别按顺序运行 d􏰂str􏰂i_f􏰂􏰃ileServer 下的模块中 cn􏰄.edu.ruc.adc􏰀urse.f􏰂􏰃ileServer.F􏰂􏰃ileServer_boo􏰀􏰀tstrap􏰅.􏰆java 启动服务

5.  运行 d􏰂str􏰂i_f􏰂􏰃ileCli􏰃􏰂e􏰄nt 下的模块中c􏰄n.edu.ruc.adco􏰀urse.fil􏰂􏰃eC􏰃􏰂lien􏰄t.F􏰂􏰃ileCli􏰃􏰂en􏰄t_boo􏰀􏰀tstrap􏰅.j􏰆ava 启动客户端
    运行 d􏰂stri􏰂_fil􏰂􏰃eSt􏰀orage 下的模块中 cn􏰄.edu.ruc.adc􏰀ourse.f􏰂􏰃ileSt􏰀rage.F􏰂􏰃ileSt􏰀orage_b􏰀􏰀ootstrap􏰅.􏰆java 启动存储节点

6. 想要添加节点需要修改 cn􏰄.edu.ruc.adc􏰀ourse.f􏰂􏰃ileSt􏰀orage.F􏰂􏰃ileSto􏰀rage_b􏰀􏰀ootstrap􏰅.j􏰆ava 中参数 

   在客户端中上传文件、下载文件样例指令

   `` upload [path]``

   ``download [uuid]