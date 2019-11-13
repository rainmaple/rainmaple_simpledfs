package cn.edu.ruc.adcourse.exceptions;

import java.io.IOException;

/**
 * 文件目录判别异常
 * Created by rainmaple on 2019/10/17 0007.
 */
public class FileIsADirectoryException extends IOException{
    private static final long serialVersionUID = 5791125441456382360L;

    public FileIsADirectoryException(String message) {
        super(message);
    }
}
