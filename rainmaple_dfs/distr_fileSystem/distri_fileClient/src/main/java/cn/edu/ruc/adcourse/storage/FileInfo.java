package cn.edu.ruc.adcourse.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件信息
 * 文件信息包括：编号，文件原始名称，文件大小，主存储节点信息，备份节点信息，等等。
 */
public class FileInfo implements Serializable{
    private static final long serialVersionUID = -7766044696786379589L;

    private String uid;                                     //编号(UUID)
    private String originalName;                            //文件原始名称
    private long originalSize;                              //文件原始大小（字节为单位）
    private long saveSize;                                  //文件在存储节点上保存的大小
    private int groupId;                                    //文件保存在哪一组
    private List<String> saveNodeInfo = new ArrayList<>();  //储节点信息----哪些节点存储此文件

    private FileInfo(Builder builder) {
        uid = builder.uid;
        originalName = builder.originalName;
        originalSize = builder.originalSize;
        saveSize = builder.saveSize;
        groupId = builder.groupNumber;
        saveNodeInfo = builder.saveNodeInfo;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "uid='" + uid + '\'' +
                ", originalName='" + originalName + '\'' +
                ", originalSize=" + originalSize +
                ", saveSize=" + saveSize +
                ", groupId=" + groupId +
                ", saveNodeInfo=" + saveNodeInfo +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public long getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(long originalSize) {
        this.originalSize = originalSize;
    }

    public long getSaveSize() {
        return saveSize;
    }

    public void setSaveSize(long saveSize) {
        this.saveSize = saveSize;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<String> getSaveNodeInfo() {
        return saveNodeInfo;
    }

    public void setSaveNodeInfo(List<String> saveNodeInfo) {
        this.saveNodeInfo = saveNodeInfo;
    }

    /**
     * 添加一个节点的信息
     * @param nodeName
     */
    public void addNodeName(String nodeName){
        if(saveNodeInfo == null)
            saveNodeInfo = new ArrayList<>();
        saveNodeInfo.add(nodeName);
    }

    /**
     * 删除一个节点的信息
     * @param nodeName
     * @return
     */
    public boolean removeNodeName(String nodeName){
        return saveNodeInfo.remove(nodeName);
    }

    public static final class Builder {
        private String uid;
        private String originalName;
        private long originalSize;
        private long saveSize;
        private int groupNumber;
        private List<String> saveNodeInfo;

        public Builder() {
        }

        public Builder uid(String val) {
            uid = val;
            return this;
        }

        public Builder originalName(String val) {
            originalName = val;
            return this;
        }

        public Builder originalSize(long val) {
            originalSize = val;
            return this;
        }

        public Builder saveSize(long val) {
            saveSize = val;
            return this;
        }

        public Builder groupNumber(int val) {
            groupNumber = val;
            return this;
        }

        public Builder saveNodeInfo(List<String> val) {
            saveNodeInfo = val;
            return this;
        }

        public FileInfo build() {
            return new FileInfo(this);
        }
    }
}
