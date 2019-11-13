package cn.edu.ruc.adcourse.fileClient;

import cn.edu.ruc.adcourse.storage.FileInfo;
import cn.edu.ruc.adcourse.storage.FileStorageInfo;
import cn.edu.ruc.adcourse.exceptions.FileIsADirectoryException;
import cn.edu.ruc.adcourse.info.TransProtocol;
import cn.edu.ruc.adcourse.base.SocketSupport;
import cn.edu.ruc.adcourse.requestContent.RequestBody;
import com.google.gson.reflect.TypeToken;
import cn.edu.ruc.adcourse.utils.GsonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by rainmaple on 2019/10/23.
 */
public class CommunicateWithFileServerSupport extends SocketSupport implements CommunicateWithFileServerOperator {

    private String uuid;                    //向FileServer请求上传文件时，服务器返回的给即将上传的文件分配的uuid

    public CommunicateWithFileServerSupport(String ip, int port) throws IOException {
        super(ip, port);
    }

    /**
     * 将path指向的文件信息传给FileServer，并得到可上传的服务器的列表
     *
     * @param path 要上传的文件的路径
     * @return 返回可用的存储节点的列表
     * @throws IOException
     */
    @Override
    public List<FileStorageInfo> getUploadAvailableStorage(String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            throw new FileNotFoundException("文件不存在");
        if (file.isDirectory())
            throw new FileIsADirectoryException("文件对象是目录");

        FileInfo fileInfo = new FileInfo.Builder()
                .originalName(file.getName())
                .originalSize(file.length())
                .build();
        RequestBody<FileInfo> requestBody = new RequestBody<>(
                TransProtocol.CODE_GET_UPLOAD_STORAGE, fileInfo
        );
        //将requestBody序列成字符串，便于传输
        String info = GsonUtil.getInstance().toJson(requestBody);
        return getStorageInfo(info, TransProtocol.CODE_GET_UPLOAD_STORAGE);
    }

    /**
     * 通过uuid向服务器请求可以下载到对应文件的存储服务器列表
     *
     * @param uid
     * @throws IOException
     */
    @Override
    public List<FileStorageInfo> getDownloadAvailableStorage(String uid) throws IOException {
        RequestBody<String> requestBody = new RequestBody<String>(
                TransProtocol.CODE_GET_DOWNLOAD_STORAGE, uid
        );

        String info = GsonUtil.getInstance().toJson(requestBody);
        return getStorageInfo(info, TransProtocol.CODE_GET_DOWNLOAD_STORAGE);
    }

    /**
     * 通过封装好的请求体，向服务器请求可用的存储节点（可能用于上传，也可能用于下载）
     *
     * @param info
     * @throws IOException
     */
    private List<FileStorageInfo> getStorageInfo(String info, int code) throws IOException {
        List<FileStorageInfo> list = null;
        //先确定此次请求的目的
        dos.writeInt(code);
        //向FileServer发送请求
        dos.writeUTF(info);


        //FileServer回复的信息
        uuid = dis.readUTF();

        if (!uuid.equals("")) {
            String response = dis.readUTF();
            list = GsonUtil.getInstance().fromJson(response,
                    new TypeToken<List<FileStorageInfo>>() {
                    }.getType());
        } else {
            list = null;
        }
        close();
        return list;
    }


    /**
     * 根据uuid获取文件的信息
     *
     * @param uuid
     * @return
     * @throws IOException
     */
    @Override
    public FileInfo getFileInfoByUUID(String uuid) throws IOException {
        dos.writeInt(TransProtocol.CODE_GET_FILE_INFO_BY_UUID);
        dos.writeUTF(uuid);
        FileInfo fileInfo = GsonUtil.getInstance().fromJson(dis.readUTF(), FileInfo.class);
        close();
        System.out.println("获取文件信息成功：" + fileInfo.toString());
        return fileInfo;
    }

    /**
     * 通知FileServer删除文件
     * @param uuid  要删除文件的uuid
     * @return
     * @throws IOException
         */
    @Override
    public int removeFile(String uuid) throws IOException {
        dos.writeInt(TransProtocol.CODE_REMOVE_FILE);
        dos.writeUTF(uuid);
        int result = dis.readInt();
        close();
        return result;
    }

    /**
     * 根据uuid，更新该文件的信息
     *
     * @param uuid
     * @param fileInfo
     */
    @Override
    public void updateFileInfoByUUID(boolean isUploadSuccess, String uuid, FileInfo fileInfo) throws IOException {
        dos.writeInt(TransProtocol.CODE_UPDATE_FILE_INFO_BY_UUID);
        dos.writeUTF(uuid);
        if (isUploadSuccess) {
            dos.writeBoolean(true);
            RequestBody<FileInfo> requestBody = new RequestBody<>(
                    TransProtocol.CODE_UPDATE_FILE_INFO_BY_UUID, fileInfo
            );
            String info = GsonUtil.getInstance().toJson(requestBody);
            dos.writeUTF(info);
        } else {
            dos.writeBoolean(false);
        }
        dos.flush();

        //无用操作，只是吃掉服务器传的一个整数
        dis.readInt();
        close();
    }

    public String getUuid() {
        return uuid;
    }
}

//upload E:\My_DCIM\DCIM\Video\V70210-185559.mp4
//upload E:/Study/林文娴成绩报告.pdf