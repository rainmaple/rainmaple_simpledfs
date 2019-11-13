package cn.edu.ruc.adcourse;

import java.io.IOException;

/**
 * Created by rainmaple on 2019/7/7 0007.
 * 单例
 */

public interface CommunicateWithFileServerStrategy {
    /**
     * 获取所有节点服务器的信息
     * @return
     * @throws IOException
     */
    String getStorageInfo() throws IOException;

    /**
     * 获取所有文件的信息
     * @return
     * @throws IOException
     */
    String[] getFileInfo() throws IOException;
}
