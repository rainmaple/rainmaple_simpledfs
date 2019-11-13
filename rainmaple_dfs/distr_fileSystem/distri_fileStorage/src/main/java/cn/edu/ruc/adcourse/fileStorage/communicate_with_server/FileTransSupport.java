package cn.edu.ruc.adcourse.fileStorage.communicate_with_server;

import cn.edu.ruc.adcourse.fileStorage.data.TransProtocol;

import java.io.*;
import java.net.Socket;

/**
 * 该类是用来将文件备份到其他节点的任务类
 * Created by rainmaple on 2019/11/4.
 */
public class FileTransSupport implements Runnable {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedInputStream bis;
    private BufferedOutputStream bos;

    private String uuid;
    private File file;

    public FileTransSupport(String ip, int port, String uuid, String path) throws IOException {
        socket = new Socket(ip, port);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        this.uuid = uuid;
        this.file = new File(path);
    }

    public void upload(File file) throws IOException {
        //表示自己要上传文件
        dos.writeInt(TransProtocol.CODE_BACKUP_FILE);
        //存储节点上保存文件时的名字
        dos.writeUTF(uuid);
        dos.flush();

        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        bos = new BufferedOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = bufferedInputStream.read(buffer)) != -1){
            bos.write(buffer, 0, len);
        }
        bos.flush();
        //关闭文件流
        bufferedInputStream.close();
        fileInputStream.close();

        socket.close();
    }

    @Override
    public void run() {
        try {
            upload(file);
        } catch (IOException e) {
            System.out.println();
        }
    }
}
