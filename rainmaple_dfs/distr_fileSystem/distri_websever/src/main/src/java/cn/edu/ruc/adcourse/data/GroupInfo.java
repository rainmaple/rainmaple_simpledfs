package cn.edu.ruc.adcourse.data;

import java.io.Serializable;
import java.util.List;

/**
 * 文件组信息
 */
public class GroupInfo implements Serializable{
    private static final long serialVersionUID = -7055055563078712482L;
    private int groupId = -1;
    private List<FileStorageInfo> nodes;

    private GroupInfo(Builder builder) {
        setGroupId(builder.groupId);
        setNodes(builder.nodes);
    }


    public int getMemberNum(){
        return nodes.size();
    }
    public void addMember(FileStorageInfo fsi){
        nodes.add(fsi);
    }
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public List<FileStorageInfo> getNodes() {
        return nodes;
    }

    public void setNodes(List<FileStorageInfo> nodes) {
        this.nodes = nodes;
    }

    public boolean isUseful(){
        boolean result = false;
        for (FileStorageInfo fsi : nodes) {
            if (fsi.isUseful()){
                result = true;
            }
        }
        return result;
    }

    public static final class Builder {
        private int groupId;
        private List<FileStorageInfo> nodes;

        public Builder() {
        }

        public Builder groupId(int val) {
            groupId = val;
            return this;
        }

        public Builder nodes(List<FileStorageInfo> val) {
            nodes = val;
            return this;
        }

        public GroupInfo build() {
            return new GroupInfo(this);
        }
    }
}
