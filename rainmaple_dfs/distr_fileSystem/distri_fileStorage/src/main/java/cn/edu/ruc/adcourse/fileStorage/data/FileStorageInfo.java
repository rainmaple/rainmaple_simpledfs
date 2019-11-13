package cn.edu.ruc.adcourse.fileStorage.data;

import cn.edu.ruc.adcourse.properties.StorageNode;

import java.io.File;
import java.io.Serializable;

/**
 * FileStorage服务器的信息
 * 包括名称，ip，端口，容量，实际容量，剩余容量，文件数量，是否可用等信息。
 * Created by rainmaple on 2019/11/4.
 */
public class FileStorageInfo implements Serializable {
    private static final long serialVersionUID = -7056705484611179571L;

    private String name;            //名称
    private String ip;              //IP
    private int port;               //PORT
    private float capacity;         //容量
    private float leftCapacity;     //剩余容量
    private int groupNumber;        //组号
    private int connectNum;         //正在传输的链接个数
    private long expire;           //存活期

    public static FileStorageInfo instance = null;

    public static void init(String nodeRootFolder) {
        FileStorageInfo fsi = getInstance();
        fsi.name = StorageNode.nodeName;
        fsi.ip = StorageNode.nodeIP;
        fsi.port = StorageNode.nodePort;
        fsi.capacity = StorageNode.volume;

        fsi.leftCapacity = fsi.capacity - calculateAllFileSize(nodeRootFolder);
    }


    /**
     * 减少剩余容量
     *
     * @param size
     */
    public void subLeftCapacity(long size) {
        if (size < 0)
            return;
        leftCapacity -= size;
    }

    /**
     * 增加剩余容量
     *
     * @param size
     */
    public void addLeftCapacity(long size) {
        if (size < 0)
            return;
        leftCapacity += size;
    }

    /**
     * 增加链接数
     * @param num
     */
    public void addConnectNum(int num){
        connectNum += num;
    }

    /**
     * 减少连接数
     * @param num
     */
    public void subConnectNum(int num){
        connectNum -= num;
    }

    /**
     * 计算已用容量
     *
     * @param nodeRootFolder
     * @return
     */
    private static long calculateAllFileSize(String nodeRootFolder) {
        File file = new File(nodeRootFolder);
        if (!file.exists())
            return 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null)
                return 0;
            long totalLength = 0;
            for (File f : files) {
                totalLength += calculateAllFileSize(f.getPath());
            }
            return totalLength;
        } else {
            return file.length();
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    public float getLeftCapacity() {
        return leftCapacity;
    }

    public void setLeftCapacity(float leftCapacity) {
        this.leftCapacity = leftCapacity;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getConnectNum() {
        return connectNum;
    }

    public void setConnectNum(int connectNum) {
        this.connectNum = connectNum;
    }

    public boolean isUseful() {
        return expire > System.currentTimeMillis();
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "FileStorageInfo{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", capacity=" + capacity +
                ", leftCapacity=" + leftCapacity +
                '}';
    }

    public static FileStorageInfo getInstance() {
        if (instance == null)
            instance = new FileStorageInfo();
        return instance;
    }

    /**
     * 构造函数私有化
     */
    private FileStorageInfo() {

    }
}
