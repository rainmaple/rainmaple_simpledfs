package cn.edu.ruc.adcourse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by rainmaple on 2019/10/19 0013.
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
