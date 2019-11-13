package cn.edu.ruc.adcourse.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * FileStorage服务器的信息
 * 包括名称，ip，端口，容量，实际容量，剩余容量，文件数量，是否可用等信息。
 */
public class FileStorageInfo implements Serializable{
    private static final long serialVersionUID = -7056705484611179571L;

    private String name;            //名称
    private String ip;              //IP
    private int port;               //PORT
    private long capacity;         //容量
    private long leftCapacity;     //剩余容量
    private int groupId;        //组号
    private int connectNum;         //正在传输的链接个数
    private long expire;            //存活期

    private List<FileInfo> fileInfos = new ArrayList<>();


    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void addFileInfo(FileInfo fi) {
        System.out.println(name + " add " + fi);
        if (fileInfos == null)
            fileInfos = new ArrayList<>();
        fileInfos.add(fi);
    }

    public void subFileInfo(String uuid) {
        FileInfo delete_target = null;
        for (FileInfo fi : fileInfos) {
            if(fi.getUid().equals(uuid))
                delete_target = fi;
        }
        if(delete_target != null)
            fileInfos.remove(delete_target);
    }

    private FileStorageInfo(Builder builder) {
        name = builder.name;
        ip = builder.ip;
        port = builder.port;
        capacity = builder.capacity;
        leftCapacity = builder.leftCapacity;
        groupId = builder.groupId;
        connectNum = builder.connectNum;
        expire = builder.expire;
    }


    @Override
    public String toString() {
        return "FileStorageInfo{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", capacity=" + capacity +
                ", leftCapacity=" + leftCapacity +
                ", groupId=" + groupId +
                ", connectNum=" + connectNum +
                ", expire=" + expire +
                '}';
    }

    public boolean isUseful() {
        return expire > System.currentTimeMillis(); //如果还在有效期内，则说明有效
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

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getLeftCapacity() {
        return leftCapacity;
    }

    public void setLeftCapacity(long leftCapacity) {
        this.leftCapacity = leftCapacity;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getConnectNum() {
        return connectNum;
    }

    public void setConnectNum(int connectNum) {
        this.connectNum = connectNum;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public static final class Builder {
        private String name;
        private String ip;
        private int port;
        private long capacity;
        private long leftCapacity;
        private int groupId;
        private int connectNum;
        private long expire;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder ip(String val) {
            ip = val;
            return this;
        }

        public Builder port(int val) {
            port = val;
            return this;
        }

        public Builder capacity(long val) {
            capacity = val;
            return this;
        }

        public Builder leftCapacity(long val) {
            leftCapacity = val;
            return this;
        }

        public Builder groupId(int val) {
            groupId = val;
            return this;
        }

        public Builder connectNum(int val) {
            connectNum = val;
            return this;
        }

        public Builder expire(long val) {
            expire = val;
            return this;
        }

        public FileStorageInfo build() {
            return new FileStorageInfo(this);
        }
    }
}
