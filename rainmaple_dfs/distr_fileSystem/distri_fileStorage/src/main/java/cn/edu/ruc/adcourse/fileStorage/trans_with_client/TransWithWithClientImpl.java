package cn.edu.ruc.adcourse.fileStorage.trans_with_client;

import cn.edu.ruc.adcourse.fileStorage.data.FileStorageInfo;
import cn.edu.ruc.adcourse.fileStorage.data.LinkedBlockingQueueManager;
import cn.edu.ruc.adcourse.fileStorage.data.TransProtocol;
import cn.edu.ruc.adcourse.fileStorage.communicate_with_server.TransFileToOtherNode;
import cn.edu.ruc.adcourse.properties.StorageNode;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 该类是FileStorage与FileClient或者FileServer通信时的协议类，
 * 包含真正处理请求的逻辑
 * Created by rainmaple on 2019/10/21.
 */
public class TransWithWithClientImpl implements TransWithClientStrategy {

    @Override
    public void service(Socket socket) throws IOException, NoSuchAlgorithmException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        DataInputStream dis = new DataInputStream(is);
        DataOutputStream dos = new DataOutputStream(os);

        System.out.println("new connect in : ip->" + socket.getInetAddress() + "; port->" + socket.getPort());
        FileStorageInfo.getInstance().addConnectNum(1);

        //先获取code
        int code = dis.readInt();

        switch (code) {
            case TransProtocol.CODE_UPLOAD_FILE:    //客户端上传
                String uuid = dis.readUTF();
                File file = new File(StorageNode.nodeRootFolder + uuid);
                //如果不存在则创建文件
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                DataOutputStream fdos = new DataOutputStream(fos);

                byte[] buffer;
                int len = -1;
                MessageDigest md = MessageDigest.getInstance("MD5");
                while ((len = dis.readInt()) != -1) {
                    //读取字节流，直到读完字节数组为止
                    buffer = new byte[len];
                    dis.readFully(buffer);

                    md.update(buffer, 0, len);
                    //文件中存每段的长度，其后紧跟这个文件段的具体内容
                    fdos.writeInt(len);
                    fdos.write(buffer, 0, len);
                }
                //在文件尾写上结束标识
                fdos.writeInt(-1);

                String md5_from_client = dis.readUTF();
                fdos.close();
                fos.close();

                //更新本节点的可用容量信息
                FileStorageInfo.getInstance().subLeftCapacity(file.length());

                if (md5_from_client.equals(new BigInteger(md.digest()).toString())) {     //md5验证成功
                    LinkedBlockingQueueManager.getInstance().add(1 + "####" + file.getName());
                    System.out.println("文件：" + file.getName() + "上传成功");
                } else {
                    System.out.println("md5值不匹配，文件传输出错");
                    //把刚才存入的错误文件删除
                    file.deleteOnExit();
                    return;
                }

                new TransFileToOtherNode(StorageNode.fileServerIp, StorageNode.fileServerPort,
                        uuid, file.getPath());
                break;
            case TransProtocol.CODE_BACKUP_FILE:        //节点间备份
                uuid = dis.readUTF();
                file = new File(StorageNode.nodeRootFolder + uuid);
                //如果不存在则创建文件
                file.createNewFile();

                fos = new FileOutputStream(file);
                fdos = new DataOutputStream(fos);

                buffer = new byte[1024];
                while ((len = dis.read(buffer)) != -1) {
                    fdos.write(buffer, 0, len);
                }

                //更新本节点的可用容量信息
                FileStorageInfo.getInstance().subLeftCapacity(file.length());

                LinkedBlockingQueueManager.getInstance().add(1 + "####" + file.getName());
                fdos.close();
                dis.close();
                break;
            case TransProtocol.CODE_DOWNLOAD_FILE:
                FileInputStream fis = new FileInputStream(StorageNode.nodeRootFolder + dis.readUTF());
                DataInputStream fdis = new DataInputStream(fis);
                while ((len = fdis.readInt()) != -1) {
                    buffer = new byte[len];
                    fdis.readFully(buffer);

                    dos.writeInt(len);
                    dos.write(buffer, 0, len);
                }
                //告知文件已经传输结束
                dos.writeInt(-1);

                dos.flush();
                fdis.close();
                fis.close();
                break;
            case TransProtocol.CODE_REMOVE_FILE:        //移除文件
                uuid = dis.readUTF();
                file = new File(StorageNode.nodeRootFolder + uuid);
                //先存一下文件的大小，文件删除后节点的剩余容量要增加
                long length = file.length();
                if (!file.exists()) {      //文件不存在
                    dos.writeInt(2);
                    LinkedBlockingQueueManager.getInstance().add(2 + "####" + file.getName());
                } else if (file.delete()) {  //文件删除成功
                    dos.writeInt(1);
                    //更新本节点的可用容量信息
                    FileStorageInfo.getInstance().addLeftCapacity(length);
                    LinkedBlockingQueueManager.getInstance().add(2 + "####" + file.getName());
                } else                    //文件存在但是删除失败
                    dos.writeInt(-2);
                break;
        }

        FileStorageInfo.getInstance().subConnectNum(1);
    }

}
