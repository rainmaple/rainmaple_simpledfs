package cn.edu.ruc.adcourse.fileClient;

import cn.edu.ruc.adcourse.storage.FileInfo;
import cn.edu.ruc.adcourse.storage.FileStorageInfo;

import java.io.IOException;
import java.util.List;

/**
 * 该接口主要用于FileClient向FileServer请求可用的存储节点的信息，以及委托服务器删除文件
 * Created by rainmaple on 2019/10/23.
 */
public interface CommunicateWithFileServerOperator {
    /**
     * 向服务器请求可用的存储节点以上传文件，
     * FileServer会返回UUID作为即将上传到存储节点的文件的UUID
     * 同时会返回可上传节点组
     */
    List<FileStorageInfo> getUploadAvailableStorage(String path) throws IOException;


    /**
     * 通过uuid向FileServer索取可用存储节点以下载文件
     * uuid可能无效，也有可能uuid有效但是存储节点已经宕机，同样无法下载到目标文件，需要抛出异常
     * @param uid
     */
    List<FileStorageInfo> getDownloadAvailableStorage(String uid) throws IOException;

    /**
     * 通过uuid从FileServer上获取文件的信息
     * @param uuid 要获取文件的uuid
     * @return  如果获取成功，返回uuid对应的文件信息
     * @throws IOException
     */
    FileInfo getFileInfoByUUID(String uuid) throws IOException;

    /**
     * 通知服务器删除文件
     * @param uuid  要删除文件的uuid
     * @return  返回删除的结果
     * @throws IOException
     */
    int removeFile(String uuid) throws IOException;

    /**
     * 在文件传输到节点服务器以后，客户端要向FileServer汇报上传是否成功
     * 如果上传成功，则让服务器更新文件信息，添加saveSize属性
     * 如果上传失败，则让服务器删除该文件信息的表项
     * @param isUploadSuccess   是否上传成功
     * @param uuid              文件的uuid
     * @param fi                文件的信息
     * @throws IOException
     */
    void updateFileInfoByUUID(boolean isUploadSuccess, String uuid, FileInfo fi) throws IOException;
}
