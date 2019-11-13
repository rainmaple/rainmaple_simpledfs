package cn.edu.ruc.adcourse.fileStorage.communicate_with_server;

import cn.edu.ruc.adcourse.utils.SimpleThreadPool;

import java.io.IOException;

/**
 * 该类提供了节点服务器与FileServer通信的异步支持
 * Created by rainmaple on 2019/11/5.
 */
public class CommunicateWithServerThreadSupport implements CommunicateWithServerStrategy{
    private CommunicateWithServerStrategy cwss;
    private String ip;
    private int port;

    public CommunicateWithServerThreadSupport(CommunicateWithServerStrategy cwss,
                                              String fileServerIp,
                                              int fileServerPort)
    throws IOException {
        this.cwss = cwss;
        this.ip = fileServerIp;
        this.port = fileServerPort;
        registerOrUpdate(ip, port);
    }
    @Override
    public void registerOrUpdate(String ip, int port) throws IOException {
        //将向服务器发送心跳包的任务交给一个线程异步发送
        SimpleThreadPool.getInstance().submit(new CommunicateWithServerRunnable(cwss, ip, port));
    }
}
