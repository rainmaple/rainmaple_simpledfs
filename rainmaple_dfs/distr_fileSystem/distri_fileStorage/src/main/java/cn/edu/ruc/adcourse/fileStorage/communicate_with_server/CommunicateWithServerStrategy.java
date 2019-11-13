package cn.edu.ruc.adcourse.fileStorage.communicate_with_server;

import java.io.IOException;

/**
 * 该接口定义了存储节点和服务器交互的行为
 * Created by rainmaple on 2019/11/5.
 */
public interface CommunicateWithServerStrategy {

    /**
     * 在存储节点启动的时候首先要到服务器上注册
     * 或在存储节点运行过程中要定期向服务器发送心跳包更新自己的信息
     */
    public void registerOrUpdate(String ip, int port) throws IOException;
}
