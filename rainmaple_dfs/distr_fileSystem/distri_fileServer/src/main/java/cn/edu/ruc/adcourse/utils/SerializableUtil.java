package cn.edu.ruc.adcourse.utils;

import com.google.gson.reflect.TypeToken;
import cn.edu.ruc.adcourse.storage.FileInfo;

import java.io.*;
import java.util.Map;

/**
 * Created by Sunny on 2017/7/13 0013.
 */
public class SerializableUtil {


    /**
     *  将Map序列化到指定的文件当中
     * @param map
     * @param path
     * @throws IOException
     */
    public static void serializeMapToFile(Map<String, FileInfo> map, String path, boolean isAppend) throws IOException {
        FileOutputStream fos = new FileOutputStream(path, isAppend);
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF(GsonUtil.getInstance().toJson(map));
        dos.close();
        fos.close();
    }


    /**
     * 将文件中的信息反序列化为Map
     * @param path
     * @return
     * @throws IOException
     */
    public static Map<String, FileInfo> reverseSerializeMap(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        DataInputStream dis = new DataInputStream(fis);
        String info = dis.readUTF();
        dis.close();
        fis.close();
        return GsonUtil.getInstance().fromJson(info, new TypeToken<Map<String, FileInfo>>(){}.getType());
    }
}
