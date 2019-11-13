package cn.edu.ruc.adcourse.fileServer;

import cn.edu.ruc.adcourse.utils.SimpleThreadPool;

import java.net.Socket;

/**
 * 该类实现了处理请求的策略，同时提供线程异步处理支持，将不同的请求放到不同的线程处理
 * Created by rainmaple on 2019/10/20 0006.
 */
public class FileClientThreadSupport implements FCStrategy{
    private FCStrategy fcs;

    public FileClientThreadSupport(FCStrategy fcs) {
        this.fcs = fcs;
    }

    @Override
    public void service(Socket socket) {
        //将处理socket的工作交给线程池，让线程池处理
        SimpleThreadPool.getInstance().submit(new FileClientRunnable(socket, fcs));
    }
}
