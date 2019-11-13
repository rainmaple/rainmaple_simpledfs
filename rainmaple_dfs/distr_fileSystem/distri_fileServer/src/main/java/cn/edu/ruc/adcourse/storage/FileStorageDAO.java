package cn.edu.ruc.adcourse.storage;

import com.google.gson.reflect.TypeToken;
import cn.edu.ruc.adcourse.utils.GsonUtil;

import java.io.*;
import java.util.*;

/**
 * 存储节点服务器DAO类
 * Created by rainmaple
 */
public class FileStorageDAO {
    //用来存储存储服务器的具体信息
    private static Map<String, FileStorageInfo> map = Collections.synchronizedMap(new LinkedHashMap<>());

    private static List<GroupInfo> groupInfos = Collections.synchronizedList(new ArrayList<>());

    //存储服务器每组的数量
    private static final int groupNum = 2;

    static {
        try {
            init("./distri_fileServer/src/main/java/cn/edu/ruc/adcourse/storage/FileStorageInfo.dat");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 程序启动时加载
     *
     * @param dataPath
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static void init(String dataPath) throws IOException, ClassNotFoundException {
        File file = new File(dataPath);
        if (!file.exists())
            return;
        FileInputStream fis = new FileInputStream(file);
        DataInputStream dis = new DataInputStream(fis);
        map = GsonUtil.getInstance().fromJson(dis.readUTF(),
                new TypeToken<Map<String, FileStorageInfo>>() {
                }.getType());
        GroupInfo groupInfo = null;
        //遍历 map
        for (Map.Entry<String, FileStorageInfo> entry : map.entrySet()) {
            groupInfo = queryGroupInfoById(entry.getValue().getGroupId());
            //如果组不存在就创建
            if (groupInfo == null) {
                groupInfo = new GroupInfo.Builder()
                        .groupId(groupInfos.size())
                        .nodes(new ArrayList<>())
                        .build();
                groupInfos.add(groupInfo);
            }
            //将存储节点添加到对应的组中
            groupInfo.getNodes().add(entry.getValue());

        }
        dis.close();
        fis.close();
    }

    /**
     * 将当前记录序列化到文件中
     *
     * @param filePath
     * @throws IOException
     */
    public static void recordToFile(String filePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF(GsonUtil.getInstance().toJson(map));
        dos.flush();
        dos.close();
        fos.close();
    }

    /**
     * 添加服务器记录或更新服务器状态
     *
     * @param fsi 要添加的服务器信息
     * @return 返回操作是否成功
     */
    public static boolean addOrUpdate(FileStorageInfo fsi) {
        FileStorageInfo result = map.get(fsi.getName());
        fsi.setExpire(System.currentTimeMillis() + 6000);
        if (result == null) {                         //首次添加
            GroupInfo lastGroup = null;
            System.out.println(groupInfos.size());
            if (groupInfos.size() != 0 && groupInfos.get(groupInfos.size() - 1).getMemberNum() < groupNum) {     //如果有组，且最后一组未满，就加到最后一组
                lastGroup = groupInfos.get(groupInfos.size() - 1);
                lastGroup.addMember(fsi);
                fsi.setGroupId(lastGroup.getGroupId());
            } else {                                                //如果最后一组已满，就放到新的一组
                lastGroup = new GroupInfo.Builder()
                        .groupId(groupInfos.size())
                        .nodes(new ArrayList<>())
                        .build();
                groupInfos.add(lastGroup);
                fsi.setGroupId(lastGroup.getGroupId());
                lastGroup.addMember(fsi);
            }
            System.out.println("新节点加入：" + fsi.getName());
            map.put(fsi.getName(), fsi);
            return true;

        }

        //如果之前已经注册过，就更新信息即可
        result.setCapacity(fsi.getCapacity());
        result.setLeftCapacity(fsi.getLeftCapacity());
        result.setConnectNum(fsi.getConnectNum());
        result.setIp(fsi.getIp());
        result.setPort(fsi.getPort());
        result.setExpire(fsi.getExpire());
        return true;
    }

    /**
     * 根据存储服务器的名称获取服务器的信息
     *
     * @param fileNodeName
     * @return
     */
    public static FileStorageInfo queryStorageNodeByName(String fileNodeName) {
        return map.get(fileNodeName);
    }

    /**
     * 根据组号获取存储组所有成员的信息
     *
     * @param groupId
     * @return
     */
    public static List<FileStorageInfo> queryStorageGroupById(int groupId) {
        if (groupId >= groupInfos.size()) {
            return null;
        }
        return groupInfos.get(groupId).getNodes();
    }

    /**
     * 根据组号获取存储组的信息
     *
     * @param groupId
     * @return
     */
    public static GroupInfo queryGroupInfoById(int groupId) {
        if (groupId >= groupInfos.size())
            return null;
        return groupInfos.get(groupId);
    }

    /**
     * 根据各个服务器的状况，选择一个最合适的组
     *
     * @return 返回最合适的节点组
     */
    public static List<FileStorageInfo> selectASuitableGroup(long size) {
        GroupInfo groupInfo;
        try {
            groupInfo = Collections.max(groupInfos, Comparator.comparingInt(o -> calculateWeight(o, size)));
        } catch (Exception e) {
            return null;
        }
        //如果选中的组是个无用的组（即组内所有成员都宕机的组），则返回null
        if (!groupInfo.isUseful())
            return null;
        return groupInfo.getNodes();
    }

    /**
     * 计算节点组的权值，以获取到合适的节点组
     *
     * @param o1
     * @param size
     * @return
     */
    private static int calculateWeight(GroupInfo o1, long size) {
        int weight1 = 0;
        //如果不可用，优先级较低，为负值
        if (!o1.isUseful())
            return -100;
        for (FileStorageInfo fsi : o1.getNodes()) {
            //首先目标节点得存的下
            if (fsi.getLeftCapacity() > size && fsi.isUseful()) {
                weight1 += 10;
                weight1 -= fsi.getConnectNum();
                weight1 += fsi.getLeftCapacity() / fsi.getCapacity() * 2.0;
            } else {
                weight1 -= 10;
            }
        }

        return weight1;
    }

    /**
     * 获取所有节点组的信息
     *
     * @return
     */
    public static List<GroupInfo> getGroupInfos() {
        return groupInfos;
    }

    /**
     * 获取所有节点的上传文件情况
     *
     * @return
     */
    public static Map<String, FileStorageInfo> getNodesFileInfo() {
        return map;
    }

    /**
     * 向一个服务器节点记录中添加一个文件信息，表示这个节点新添加了一个文件
     * @param name  节点服务器的名字
     * @param uuid  文件的uuid
     */
    public static void addFileInfoToNode(String name, String uuid) {
        FileInfo fi = FileInfoDAO.query(uuid);
        if (fi != null)
            map.get(name).addFileInfo(fi);
        else
            System.out.println("addFileInfoToNode: " + uuid + "->文件不存在");
    }

    /**
     * 删除节点服务器的文件的信息
     * @param name  节点服务器的名字
     * @param uuid  文件的uuid
     */
    public static void subFileInfoFromNode(String name, String uuid) {
        map.get(name).subFileInfo(uuid);
    }
}
