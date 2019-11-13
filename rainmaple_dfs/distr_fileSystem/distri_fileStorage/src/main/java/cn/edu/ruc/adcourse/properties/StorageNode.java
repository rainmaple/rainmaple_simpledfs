package cn.edu.ruc.adcourse.properties;

import cn.edu.ruc.adcourse.fileStorage.data.FileStorageInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 完成存储节点初始化配置绑定
 * Created by rainmaple on 2019/11/7 0007.
 */
public class StorageNode {
    public static String nodeName;
    public static String nodeIP;
    public static int nodePort;
    public static String nodeRootFolder;
    public static float volume;              //GB
    public static String fileServerIp;
    public static int fileServerPort;
    public static final String propertiesBasePath = "./distri_fileStorage/src/main/java/cn/edu/ruc/adcourse/properties/";
    //静态初始化代码块，加载的时候执行，默认采用第一种配置

    static {
        try {
            init(propertiesBasePath + "storage1.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init(String propertiesPath) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertiesPath));
        nodeName = properties.getProperty("NodeName");
        nodeIP = properties.getProperty("NodeIP");
        nodePort = Integer.parseInt(properties.getProperty("NodePort"));
        nodeRootFolder = properties.getProperty("RootFolder");
        volume = Float.parseFloat(properties.getProperty("Volume"));
        fileServerIp = properties.getProperty("FileServerIP");
        fileServerPort = Integer.parseInt(properties.getProperty("FileServerPort"));
        File file = new File(nodeRootFolder);
        //根目录不存在的话创建根目录
        if(!file.exists()){
            System.out.println(nodeRootFolder);
            boolean success = file.mkdirs();
            System.out.println("success: " + success);
        } else {
            System.out.println(file.getAbsolutePath() + " exist");
        }
        FileStorageInfo.init(nodeRootFolder);
    }

}
