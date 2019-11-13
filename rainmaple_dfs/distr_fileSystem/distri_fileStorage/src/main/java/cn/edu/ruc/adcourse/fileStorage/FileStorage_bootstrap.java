package cn.edu.ruc.adcourse.fileStorage;

import cn.edu.ruc.adcourse.fileStorage.communicate_with_server.CommunicateWithServerStrategyImpl;
import cn.edu.ruc.adcourse.fileStorage.communicate_with_server.CommunicateWithServerThreadSupport;
import cn.edu.ruc.adcourse.fileStorage.trans_with_client.TransWithClientServer;
import cn.edu.ruc.adcourse.fileStorage.trans_with_client.TransWithClientThreadSupport;
import cn.edu.ruc.adcourse.fileStorage.trans_with_client.TransWithWithClientImpl;
import cn.edu.ruc.adcourse.properties.StorageNode;

import java.io.IOException;

/**
 * 绑定服务器主程序同客户端和存储节点的监听
 * Created by rainmaple on 2019/10/22.
 */
public class FileStorage_bootstrap {
    public static void main(String[] args) throws IOException {
//        初始化节点信息
        StorageNode.init(StorageNode.propertiesBasePath + "storage6.properties");
        //启动与服务器的联系，定期发送心跳包
        new CommunicateWithServerThreadSupport(
                new CommunicateWithServerStrategyImpl(),
                StorageNode.fileServerIp,
                StorageNode.fileServerPort);
        //启动与clientServer交互的程序，并监听来自clientServer的请求
        new TransWithClientServer(
                StorageNode.nodePort,
                new TransWithClientThreadSupport(new TransWithWithClientImpl()));
    }
}
