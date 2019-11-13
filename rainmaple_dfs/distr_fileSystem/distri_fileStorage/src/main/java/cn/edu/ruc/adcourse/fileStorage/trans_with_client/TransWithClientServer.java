package cn.edu.ruc.adcourse.fileStorage.trans_with_client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

/**
 * 该类负责接收Client发出的请求，并将Socket对象传递给ThreadSupport，让线程异步处理请求
 * 自己则再次去等待新的连接链入
 * Created by rainmaple on 2019/10/21.
 */
public class TransWithClientServer {
    public TransWithClientServer(int port, TransWithClientStrategy fcs){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("==================");
            System.out.println("存储系统就绪！！");
            System.out.println("==================");
            while(true){
                Socket socket = serverSocket.accept();
                fcs.service(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
