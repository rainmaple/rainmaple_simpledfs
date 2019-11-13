package cn.edu.ruc.adcourse.fileClient;

import cn.edu.ruc.adcourse.storage.FileInfo;
import cn.edu.ruc.adcourse.storage.FileStorageInfo;
import cn.edu.ruc.adcourse.exceptions.EncryptFailException;
import cn.edu.ruc.adcourse.exceptions.FileIsADirectoryException;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by rainmaple on 2019/10/22.
 */
public class FileTransSupport implements FileTransOperator {

    private List<FileStorageInfo> list;
    private String uuid;
    private String ip;
    private int port;

    public FileTransSupport(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
    }


    /**
     * 上传文件到存储服务器
     *
     * @param path 即将要上传的文件的路径
     * @return
     * @throws IOException
     */
    @Override
    public FileInfo upload(String path) throws IOException {
        FileInfo fi = null;
        //从服务器上获取可用的存储节点组，同时服务器会返回给该文件分配的uuid
        try {
            CommunicateWithFileServerSupport cwfs = new CommunicateWithFileServerSupport(ip, port);
            list = cwfs.getUploadAvailableStorage(path);
            uuid = cwfs.getUuid();
        } catch (FileNotFoundException e) {
            System.out.println("文件不存在");
            return null;
        } catch (FileIsADirectoryException e) {
            System.out.println("不能上传目录");
            return null;
        } catch (IOException e) {
            System.out.println("获取节点组信息失败！！");
            return null;
        }

        if (list == null || list.size() == 0) {
            System.out.println("没有可用节点！！");
            return null;
        }

        boolean isUploadSuccess = false;
        for (FileStorageInfo fsi : list) {
//            //跳过不可用的节点（可能在一个节点在传输过程中关闭，此时会切换到另一个节点，不过本地存有的该节点的有效期可能已经过时，此时会判断错误）
//            if (!fsi.isUseful()) {
//                System.out.println("节点" + fsi.getName() + "不可用");
//                continue;
//            }
            System.out.println(fsi.toString());
            //开始尝试传输
            try {
                //将文件传输到存储节点
                fi = new FileTransToStorageSupport(fsi.getIp(), fsi.getPort(), uuid)
                        .upload(path);
                isUploadSuccess = true;
                break;
            } catch (IOException e) {
                System.out.println("传输失败，正在尝试传输到其他备份节点...");
            } catch (NoSuchAlgorithmException | EncryptFailException e) {
                e.printStackTrace();
            }
        }

        //文件传输完成后，向服务器禀报传输情况，同时更新文件的saveSize
        new CommunicateWithFileServerSupport(ip, port).updateFileInfoByUUID(isUploadSuccess, uuid, fi);

        if (isUploadSuccess) {
            System.out.println("文件上传成功，UUID = " + uuid);
        } else {
            System.out.println("文件上传失败");
        }
        return fi;
    }

    @Override
    public void download(String uid) {
        //从服务器端获取存储了要下载文件的节点组的信息
        getFileStorageByUUID(uid);

        if (list == null || list.size() == 0) {
            System.out.println("没有可用节点！！");
            return;
        }

        boolean isDownloadSuccess = false;
        for (FileStorageInfo fsi : list) {
//            //跳过不可用的节点
//            if (!fsi.isUseful())
//                continue;
            //开始尝试下载
            try {
                new FileTransToStorageSupport(fsi.getIp(), fsi.getPort(),
                        new CommunicateWithFileServerSupport(ip, port).getFileInfoByUUID(uid))
                        .download(uid);
                isDownloadSuccess = true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("下载失败，正在尝试传到其他备份节点下载...");
            }
        }
        if (isDownloadSuccess) {
            System.out.println("文件下载成功~~~");
        } else {
            System.out.println("文件下载失败！！");
        }
    }

    @Override
    public void remove(String uid) throws IOException {
        int result = new CommunicateWithFileServerSupport(ip, port).removeFile(uid);
        if(result > 0)
            System.out.println("删除文件成功");
        else
            System.out.println("删除文件失败");
    }

    /**
     * 根据uuid获取存文件的节点组信息
     *
     * @param uid
     * @return
     */
    private List<FileStorageInfo> getFileStorageByUUID(String uid) {
        try {
            list = new CommunicateWithFileServerSupport(ip, port)
                    .getDownloadAvailableStorage(uid);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("从服务器获取存储节点失败！！");
            return list;
        }
        return list;
    }


}
