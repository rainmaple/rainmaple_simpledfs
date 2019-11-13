package cn.edu.ruc.adcourse.fileServer;

import cn.edu.ruc.adcourse.storage.FileInfoDAO;
import cn.edu.ruc.adcourse.storage.FileStorageDAO;
import cn.edu.ruc.adcourse.utils.SimpleThreadPool;

import java.io.IOException;
import java.util.Scanner;

/**
 * 服务器主程序，启动对客户端和存储节点的监听
 * Created by raimaple on 2019/10/23.
 */
public class FileServer_bootstrap {
    public static void main(String[] args) throws IOException {
        //监听节点服务器发送的心跳包，以更新存储节点的状态信息
        SimpleThreadPool.getInstance()
                .submit(() -> new FileStorageServer(2534));

        //启动与客户端交互的程序，并在端口2533处监听来自客户端的请求
        SimpleThreadPool.getInstance()
                .submit(() -> new FileClientServer(2533, new FileClientThreadSupport(new FCStrategyImpl())));

        Scanner scanner = new Scanner(System.in);
        String lineText = null;
        while (!(lineText = scanner.nextLine().trim()).equals("quit")) {
            if(lineText.equals("save")){
                FileInfoDAO.recordToFile("./distri_fileServer/src/main/java/cn/edu/ruc/adcourse/storage/FileInfoData.dat");
                FileStorageDAO.recordToFile("./distri_fileServer/src/main/java/cn/edu/ruc/adcourse/storage/FileStorageInfo.dat");
                System.out.println("保存信息成功");
            }
        }
        SimpleThreadPool.getInstance().shutDownNow();
    }
}
