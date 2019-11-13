package cn.edu.ruc.adcourse.info;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * 客户端要访问的FileServer的信息
 */
public class APIInfo {
    public static String ip = null;
    public static int port = -1;

    public static final String IP_PARAM_NAME = "serverIP";
    public static final String PORT_PARAM_NAME = "serverPort";

    public static void init() throws IOException {
        // 加载配置文件的信息
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(PropertiesPathInfo.FileServerInfo));
            APIInfo.ip = properties.getProperty(APIInfo.IP_PARAM_NAME);
            APIInfo.port = Integer.parseInt(properties.getProperty(APIInfo.PORT_PARAM_NAME));
            System.out.println("ip : " + APIInfo.ip);
            System.out.println("port : " + APIInfo.port);
        } catch (IOException e) {
            throw new IOException("FileServer配置文件加载异常: " + e.getLocalizedMessage(), e.getCause());
        }
    }
}
