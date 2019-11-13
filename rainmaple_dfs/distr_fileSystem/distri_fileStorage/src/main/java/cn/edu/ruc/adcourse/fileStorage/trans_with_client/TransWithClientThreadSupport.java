package cn.edu.ruc.adcourse.fileStorage.trans_with_client;

import cn.edu.ruc.adcourse.utils.SimpleThreadPool;

import java.net.Socket;

/**
 * Created by rainmaple on 2019/10/24.
 */
public class TransWithClientThreadSupport implements TransWithClientStrategy {
    private TransWithClientStrategy fcs;

    public TransWithClientThreadSupport(TransWithClientStrategy fcs) {
        this.fcs = fcs;
    }

    @Override
    public void service(Socket socket) {
        //将处理socket的工作交给线程池，让线程池处理
        SimpleThreadPool.getInstance().submit(new TransWithClientRunnable(socket, fcs));
    }
}
