package cn.edu.ruc.adcourse.fileServer;

import java.io.IOException;
import java.net.Socket;

/**
 * 可放在线程中执行的任务
 * Created by rainmaple on 2019/10/19.
 */
public class FileClientRunnable implements Runnable {

    private Socket socket;
    private FCStrategy fcs;

    public FileClientRunnable(Socket socket, FCStrategy fcs) {
        this.socket = socket;
        this.fcs = fcs;
    }

    @Override
    public void run() {
        try {
            fcs.service(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
