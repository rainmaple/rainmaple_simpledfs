package cn.edu.ruc.adcourse.info;

/**
 * 策略封装
 */
public class TransProtocol {
    public static final int CODE_UPLOAD_FILE = 1;               //上传文件请求
    public static final int CODE_DOWNLOAD_FILE = 2;             //下载文件请求
    public static final int CODE_REMOVE_FILE = 3;               //移除文件请求
    public static final int CODE_GET_UPLOAD_STORAGE = 4;        //获取可上传节点组请求
    public static final int CODE_GET_DOWNLOAD_STORAGE = 5;      //获取下载文件组信息
    public static final int CODE_UPDATE_STORAGE_INFO = 6;       //存储节点向服务器更新自己的信息
    public static final int CODE_GET_FILE_INFO_BY_UUID = 7;     //根据UUID获取文件的信息
    public static final int CODE_GET_GROUP_INFO = 8;            //存储节点向服务器询问其他节点的信息，以向其他节点备份文件
    public static final int CODE_BACKUP_FILE = 9;               //存储节点之前互相备份文件
    public static final int CODE_UPDATE_FILE_INFO_BY_UUID = 10; //根据UUID更新文件信息
}
