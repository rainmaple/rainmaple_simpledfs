package cn.edu.ruc.adcourse.fileStorage.data;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 有序阻塞队列管理类，提供一个单例化的阻塞队列，可以往里面放和取数据
 * Created by rainmaple on 2019/11/3.
 */
public class LinkedBlockingQueueManager {
    private static LinkedBlockingQueue<String> instance = null;

    public static synchronized LinkedBlockingQueue<String> getInstance() {
        if(instance == null)
            instance = new LinkedBlockingQueue<>();
        return instance;
    }

    private LinkedBlockingQueueManager(){

    }

    public void add(String info){
        getInstance().add(info);
    }
    public void take() throws InterruptedException {
        getInstance().take();
    }

    public void poll(){
        getInstance().poll();
    }

    public void peek(){
        getInstance().peek();
    }
}
