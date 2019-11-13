package cn.edu.ruc.adcourse.storage;

import cn.edu.ruc.adcourse.utils.SerializableUtil;

import java.io.IOException;
import java.util.*;

/**
 * 文件信息DAO
 *
 * Created by rainmaple on 2019/10/18 0008.
 */
public class FileInfoDAO {
    //用来存储文件信息
    private static Map<String, FileInfo> map = Collections.synchronizedMap(new HashMap<>());

    static {
        try {
            init("./distri_fileServer/src/main/java/cn/edu/ruc/adcourse/storage/FileInfoData.dat");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从序列化文件中恢复数据
     * @param dataPath
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void init(String dataPath) throws IOException, ClassNotFoundException {
        map = SerializableUtil.reverseSerializeMap(dataPath);
    }

    /**
     * 将文件信息序列化到文件当中
     * @param filePath
     * @throws IOException
     */
    public static void recordToFile(String filePath) throws IOException {
        SerializableUtil.serializeMapToFile(map, filePath, false);
    }

    /**
     * 插入操作
     * @param fileInfo
     * @return
     */
    public static boolean insert(FileInfo fileInfo){
        map.put(fileInfo.getUid(), fileInfo);
        return true;
    }

    /**
     * 根据uuid检索文件信息
     * @param uuid
     * @return
     */
    public static FileInfo query(String uuid){
        return map.get(uuid);
    }
    /**
     * 删除操作
     * @param uuid
     * @return
     */
    public static boolean remove(String uuid){
        return map.remove(uuid) != null;
    }

    /**
     * 更新数据库中已有的文件的存储节点的信息
     * @param fileInfo  要更新的数据
     * @return  返回更新是否成功
     */
    public static boolean update(FileInfo fileInfo){
        FileInfo fi;
        fi = map.get(fileInfo.getUid());
        if(fi == null)
            return false;
        else{
            fi.setSaveSize(fileInfo.getSaveSize());
        }
        return true;
    }

    /**
     * 获取存储在Map中所有文件的信息
     * @return  返回所有文件信息组成的List
     */
    public static List<FileInfo> getFileInfo(){
        return new ArrayList<>(map.values());
    }
}
