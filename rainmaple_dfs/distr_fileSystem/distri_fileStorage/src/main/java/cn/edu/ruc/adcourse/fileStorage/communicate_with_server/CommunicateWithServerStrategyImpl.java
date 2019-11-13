package cn.edu.ruc.adcourse.fileStorage.communicate_with_server;

import cn.edu.ruc.adcourse.fileStorage.data.FileStorageInfo;
import cn.edu.ruc.adcourse.fileStorage.data.LinkedBlockingQueueManager;
import cn.edu.ruc.adcourse.fileStorage.data.TransProtocol;
import cn.edu.ruc.adcourse.fileStorage.content.RequestBody;
import cn.edu.ruc.adcourse.properties.StorageNode;
import cn.edu.ruc.adcourse.utils.CheckSumUtil;
import cn.edu.ruc.adcourse.utils.GsonUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 该类是节点服务器与服务器通信策略的一个实现类
 * created by rainmaple on 2019/11/5.
 */
public class CommunicateWithServerStrategyImpl implements CommunicateWithServerStrategy {

    @Override
    public void registerOrUpdate(String ip, int port) throws IOException {
        DatagramSocket datagramSocket = new DatagramSocket();

        RequestBody<FileStorageInfo> body = new RequestBody<>(TransProtocol.CODE_UPDATE_STORAGE_INFO, FileStorageInfo.getInstance());
        String uuid = LinkedBlockingQueueManager.getInstance().poll();
        String info = GsonUtil.getInstance().toJson(body);
        if(uuid == null){
            info += "####" + 0;
        }else{
            info += "####" + uuid;
        }

        //准备数据，并计算出32bit的CRC校验码放在数据末尾
        byte[] data = info.getBytes(StandardCharsets.UTF_8);
        byte[] dataWithCRC = Arrays.copyOf(data, data.length + 4);
        byte[] checkSum = CheckSumUtil.getCRC32Value(data);
        System.arraycopy(checkSum, 4, dataWithCRC, data.length, dataWithCRC.length - data.length);

        //开始发送数据包，不管服务器在不在线，只管发
        DatagramPacket datagramPacket = new DatagramPacket(dataWithCRC, dataWithCRC.length
                , new InetSocketAddress(StorageNode.fileServerIp
                , StorageNode.fileServerPort));
        datagramSocket.send(datagramPacket);
        System.out.println("send : " + info);
        datagramSocket.close();
    }

}
