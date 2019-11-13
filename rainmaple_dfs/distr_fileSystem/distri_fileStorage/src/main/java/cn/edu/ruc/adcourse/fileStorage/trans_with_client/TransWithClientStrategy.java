package cn.edu.ruc.adcourse.fileStorage.trans_with_client;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

/**
 * 该策略用于应对客户端上传下载文件或服务器发送的删除文件的请求
 * Created by rainmaple on 2019/10/21.
 */
public interface TransWithClientStrategy {
    public void service(Socket socket) throws IOException, NoSuchAlgorithmException;
}
