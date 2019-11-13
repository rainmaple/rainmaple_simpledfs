package cn.edu.ruc.adcourse.fileServer;

import cn.edu.ruc.adcourse.info.TransProtocol;
import com.google.gson.reflect.TypeToken;
import cn.edu.ruc.adcourse.requestContent.RequestBody;
import cn.edu.ruc.adcourse.storage.FileInfo;
import cn.edu.ruc.adcourse.storage.FileInfoDAO;
import cn.edu.ruc.adcourse.storage.FileStorageDAO;
import cn.edu.ruc.adcourse.storage.FileStorageInfo;
import cn.edu.ruc.adcourse.utils.GsonUtil;
import cn.edu.ruc.adcourse.utils.UUIDUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * 策略

 * Created by Rainmaple on 2019/10/25 0006.
 */
public class FCStrategyImpl implements FCStrategy {
    @Override
    public void service(Socket socket) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        //先获取code判断类型
        int code = dis.readInt();
        String info = dis.readUTF();
        int result = 1;
        switch (code) {

            case TransProtocol.CODE_GET_GROUP_INFO:         //存储节点向服务器询问其他节点的信息，以向其他节点备份文件

                //根据节点的名字查找本节点的信息（包含组号）
                FileStorageInfo fileStorageInfo = FileStorageDAO.queryStorageNodeByName(info);
                //根据组号获得本组所有成员的信息
                List<FileStorageInfo> group = FileStorageDAO.queryStorageGroupById(fileStorageInfo.getGroupId());
                dos.writeUTF(GsonUtil.getInstance().toJson(group));
                dos.flush();
                break;

            case TransProtocol.CODE_GET_UPLOAD_STORAGE:     //获取可上传节点组请求

                RequestBody<FileInfo> body = GsonUtil.getInstance().fromJson(info,
                        new TypeToken<RequestBody<FileInfo>>() {
                        }.getType());
                FileInfo fileInfo = body.getData();
                List<FileStorageInfo> fsis = FileStorageDAO.selectASuitableGroup(fileInfo.getOriginalSize());
                if (fsis != null && fsis.size() != 0) {
                    String uuid = UUIDUtil.randomUUID();
                    fileInfo.setUid(uuid);
                    for (FileStorageInfo fsi : fsis) {
                        fileInfo.addNodeName(fsi.getName());
                    }
                    fileInfo.setGroupId(fsis.get(0).getGroupId());
                    FileInfoDAO.insert(fileInfo);
                    dos.writeUTF(uuid);
                    dos.writeUTF(GsonUtil.getInstance().toJson(fsis));
                } else {
                    dos.writeUTF("");
                }
                break;
            case TransProtocol.CODE_GET_DOWNLOAD_STORAGE:       //获取下载文件组信息

                RequestBody<String> body1 = GsonUtil.getInstance().fromJson(info,
                        new TypeToken<RequestBody<String>>() {
                        }.getType());
                String uid = body1.getData();
                FileInfo fi = FileInfoDAO.query(uid);
                if (fi != null) {
                    dos.writeUTF(uid);
                    dos.writeUTF(GsonUtil.getInstance().toJson(FileStorageDAO.queryStorageGroupById(fi.getGroupId())));
                } else {
                    dos.writeUTF("");       //借此通知客户端，没有该文件可以下载
                }
                break;
            case TransProtocol.CODE_GET_FILE_INFO_BY_UUID:      //根据UUID获取文件的信息

                fi = FileInfoDAO.query(info);
                dos.writeUTF(GsonUtil.getInstance().toJson(fi));
                dos.flush();
                break;
            case TransProtocol.CODE_UPDATE_FILE_INFO_BY_UUID:   //根据UUID更新文件信息
                if (dis.readBoolean()) {      //判断文件是否上传成功
                    RequestBody<FileInfo> rb = GsonUtil.getInstance()
                            .fromJson(dis.readUTF(),
                                    new TypeToken<RequestBody<FileInfo>>() {
                                    }.getType());
                    //将文件信息更新
                    FileInfoDAO.update(rb.getData());
                    System.out.println("文件信息更新成功：" + FileInfoDAO.query(info));
                } else {                    //上传失败，则根据uuid删除掉对应的文件信息
                    FileInfoDAO.remove(info);
                }
                break;
            case TransProtocol.CODE_REMOVE_FILE:                //移除文件请求
                fi = FileInfoDAO.query(info);
                if (fi == null) {
                    result = -2;
                    break;
                }
                group = FileStorageDAO.queryStorageGroupById(fi.getGroupId());
                if (group != null) {

                    //可能某些存储节点宕机，但是存有该文件，会产生冗余

                    //遍历组让所有成员删除此uuid的文件
                    for (FileStorageInfo fsi : group) {
                        try {
                            new CommunicateWithStorageStrategyImpl(
                                    fsi.getIp(), fsi.getPort()).remove(info, fsi.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(fsi.getName() + " : " + e.getMessage());
                            continue;
                        }
                        System.out.println("在节点" + fsi.getName() + "上移除UUID = " + info + " 的文件成功");
                    }

                    //将该文件的记录从数据库中删除
                    FileInfoDAO.remove(info);
                    result = 1;
                } else {
                    result = -1;
                }
                break;

            case TransProtocol.CODE_MONITOR_GET_INFO:           //监控程序向服务器请求所有节点组的数据
                dos.writeUTF(GsonUtil.getInstance().toJson(FileStorageDAO.getGroupInfos()));
                break;
            case TransProtocol.CODE_MONITOR_GET_FILE_INFO:      //监控程序向服务器请求所有文件的信息
                dos.writeUTF(GsonUtil.getInstance().toJson(FileInfoDAO.getFileInfo()));
                dos.writeUTF(GsonUtil.getInstance().toJson(FileStorageDAO.getNodesFileInfo()));
                break;

        }
        dos.writeInt(result);
        dos.flush();
    }
}
