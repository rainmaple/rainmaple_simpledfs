package cn.edu.ruc.adcourse.util;

import com.google.gson.Gson;

/**
 * Created by rainmaple on 2019/7/7 0007.
 * 单例
 */
public class GsonUtil {
    public static Gson instance = null;
    private static Object lock = new byte[0];

    public static Gson getInstance() {
        if(instance == null){
            synchronized (lock){
                instance = new Gson();
            }
        }
        return instance;
    }
}
