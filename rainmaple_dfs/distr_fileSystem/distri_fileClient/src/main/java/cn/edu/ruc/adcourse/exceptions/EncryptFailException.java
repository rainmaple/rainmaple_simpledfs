package cn.edu.ruc.adcourse.exceptions;

/**
 * 加密失败异常
 * Created by rainmaple on 2019/10/23 0013.
 */
public class EncryptFailException extends Exception{
    public EncryptFailException(String message) {
        super(message);
    }
}
