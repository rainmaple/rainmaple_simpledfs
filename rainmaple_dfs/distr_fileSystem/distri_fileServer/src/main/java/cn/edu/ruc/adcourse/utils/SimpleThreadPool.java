package cn.edu.ruc.adcourse.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单线程池
 * Created by rainmaple on 2019/10/22 0027.
 */
public class SimpleThreadPool {
    private static SimpleThreadPool instance;
    private ExecutorService executorService;
    private static final Object lock = new byte[1];
    //该标记为用来终止线程
    private boolean shutdownJudge = false;

    private SimpleThreadPool(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static SimpleThreadPool getInstance() {
        if (instance == null || instance.executorService.isShutdown()) {
            synchronized(lock){     //创建的时候使用同步锁，防止多个并发时重复创建
                instance = new SimpleThreadPool(Executors.newCachedThreadPool());
            }
        }
        return instance;
    }

    public void submit(Runnable task) {
        instance.executorService.submit(task);
    }

    public void shutDown(){
        shutdownJudge = true;
        if(instance != null){
            executorService.shutdown();
            instance = null;
        }
    }

    public void shutDownNow(){
        System.out.println("ShutdownNow");
        shutdownJudge = true;
        if(instance != null){
            executorService.shutdownNow();
        }
    }

    public boolean isShutdownJudge() {
        return shutdownJudge;
    }
}