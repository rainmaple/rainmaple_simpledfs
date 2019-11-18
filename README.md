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

1. 实验实现了分布式文件系统下文件的添加，以及本地化，以及删除操作。

2. 实验实现了分布式文件系统的基本逻辑，建立文件多副本的存储，以及多个节点的设立。

3. 实验实现了分布式文件系统中master(服务端)，多个slave节点文件系统以及相应的存储系统。

4. 多个子节点，以及子节点同主节点之间的进程通信，包括心跳包的建立使用socket异步实现。传输内容校验的使用CRC完成校验。

![分布式文件系统](/Users/rainmaple/Downloads/分布式文件系统.png)

#### 使用说明

1. 将项目导入 i􏰂dea 中
2. 导入 m􏰁aven􏰄 依赖 

3.  为监控服务绑定Tomcat Server
4. 分别按顺序运行distri_fileServer下的模块中cn.edu.ruc.adcourse.fileServer.FileServer_bootstrap.java 启动服务器
5. 运行distri_fileClient下的模块中cn.edu.ruc.adcourse.fileClient.FileClient_bootstrap.java 启动客户端
6. 运行distri_fileStorage下的模块中cn.edu.ruc.adcourse.fileStorage.FileStorage_bootstrap.java 启动存储节点
7. 想要添加节点需要修改cn.edu.ruc.adcourse.fileStorage.FileStorage_bootstrap.java中参数

在客户端中上传文件、下载文件样例指令

`` upload [path]``

``download [uuid]``