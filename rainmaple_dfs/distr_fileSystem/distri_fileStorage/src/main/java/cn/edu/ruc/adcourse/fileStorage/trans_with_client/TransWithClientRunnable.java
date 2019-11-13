package cn.edu.ruc.adcourse.fileStorage.trans_with_client;

import cn.edu.ruc.adcourse.fileStorage.data.FileStorageInfo;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

/**
 *
 * Created by rainmaple on 2019/10/21.
 */
public class TransWithClientRunnable implements Runnable {

    private Socket socket;
    private TransWithClientStrategy fcs;

    public TransWithClientRunnable(Socket socket, TransWithClientStrategy fcs) {
        this.socket = socket;
        this.fcs = fcs;
    }

    @Override
    public void run() {
        try {
            fcs.service(socket);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            FileStorageInfo.getInstance().subConnectNum(1);
        }
    }
}
