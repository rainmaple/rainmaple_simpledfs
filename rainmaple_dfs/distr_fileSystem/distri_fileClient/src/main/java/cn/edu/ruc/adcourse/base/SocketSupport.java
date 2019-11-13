package cn.edu.ruc.adcourse.base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Socket支持类，传入IP端口后就会自动连接并获取输入输出流
 *
 * Created by Sunny on 2017/7/13 0013.
 */
public class SocketSupport {
    protected Socket socket;
    protected String ip;
    protected int port;
    protected DataInputStream dis;
    protected DataOutputStream dos;

    public SocketSupport(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.ip = ip;
        this.port = port;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * 关闭流操作
     */
    protected void close() {
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
