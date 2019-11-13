package cn.edu.ruc.adcourse.fileStorage.communicate_with_server;

import cn.edu.ruc.adcourse.fileStorage.data.FileStorageInfo;
import cn.edu.ruc.adcourse.fileStorage.data.TransProtocol;
import com.google.gson.reflect.TypeToken;
import cn.edu.ruc.adcourse.properties.StorageNode;
import cn.edu.ruc.adcourse.utils.GsonUtil;
import cn.edu.ruc.adcourse.utils.SimpleThreadPool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * 该类是将文件备份到同组其他成员的主类
 * 1.先根据自己的名字从FileServer上获取同组其他成员的信息
 * 2.再遍历一遍同组的各个成员，试图将文件传输给他们
 * Created by rainmaple on 2019/11/4.
 */
public class TransFileToOtherNode {
    public TransFileToOtherNode(String fileServerIp, int fileServerPort, String uuid, String path) throws IOException {
        Socket socket = new Socket(fileServerIp, 2533);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        dos.writeInt(TransProtocol.CODE_GET_GROUP_INFO);
        dos.writeUTF(StorageNode.nodeName);

        List<FileStorageInfo> list = GsonUtil.getInstance().fromJson(dis.readUTF(),
                new TypeToken<List<FileStorageInfo>>(){}.getType());
        dis.readInt();
        socket.close();
        if(list == null)
            return;
        for (FileStorageInfo fsi:
                list) {
            //忽略自身
            if(fsi.getName().equals(StorageNode.nodeName))
                continue;
            //异步备份文件
            SimpleThreadPool.getInstance().submit(new FileTransSupport(fsi.getIp(), fsi.getPort(), uuid, path));
        }

    }
}
