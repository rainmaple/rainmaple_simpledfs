package cn.edu.ruc.adcourse.fileServer;

import cn.edu.ruc.adcourse.utils.SimpleThreadPool;
import cn.edu.ruc.adcourse.utils.Tool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 该类负责接收来自节点服务器的心跳包，并把它加入到等待队列，自己不处理
 * Created by rainmaple on 2019/11/22.
 */
public class FileStorageServer {
    private static final int BUFFER_SIZE = 1024;

    public FileStorageServer(int port) {
        System.out.println("===============================");
        System.out.println("分布式文件存储模块服务端 已经加载完毕并启动！！");
        System.out.println("===============================");
        try {
            DatagramSocket ds = new DatagramSocket(port);
            DatagramPacket dp = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE,
                    InetAddress.getLocalHost(), port);
            FileStorageDataDealRunnable fsddr = new FileStorageDataDealRunnable();

            //在另一个线程异步处理节点服务器发上来的心跳包
            SimpleThreadPool.getInstance()
                    .submit(fsddr);

            while(!SimpleThreadPool.getInstance().isShutdownJudge()){
                ds.receive(dp);
                //收到一个心跳包就将它放到等待队列
                fsddr.add(Tool.subByteArray(dp.getData(), 0, dp.getLength()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
