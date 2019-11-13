package cn.edu.ruc.adcourse.fileClient;

import cn.edu.ruc.adcourse.storage.FileInfo;
import cn.edu.ruc.adcourse.exceptions.EncryptFailException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * 该接口用于执行客户端最基本的三个功能
 * Created by rainmaple on 2019/10/22.
 */
public interface FileTransOperator {

    /**
     * 实现询问FileServer后将文件上传到可用的存储节点
     * 1.首先向FileServer索要一组可用存储节点的信息
     * 2.尝试向主节点传输文件
     * 3.如果主节点不可用或中途宕机，要捕获异常并转而尝试将文件传输到备份节点
     * @param path  即将要上传的文件的路径
     * @return 若上传成功则返回uuid
     */
    FileInfo upload(String path) throws IOException, NoSuchAlgorithmException, EncryptFailException;

    /**
     * 实现询问FileServer后到可用节点上下载uuid对应的文件
     * 1.首先询问FileServer，如果uuid无效，或者存储该文件的节点组都宕机，则下载失败
     * 2.尝试从主节点下载文件，如果
     * 主节点中途宕机，这捕获异常，并转而从备份节点下载文件
     * @param uid
     */
    void download(String uid) throws IOException;

    /**
     * 实现请求FileServer帮其移除uuid对应的文件
     * @param uid
     */
    void remove(String uid) throws IOException;
}
