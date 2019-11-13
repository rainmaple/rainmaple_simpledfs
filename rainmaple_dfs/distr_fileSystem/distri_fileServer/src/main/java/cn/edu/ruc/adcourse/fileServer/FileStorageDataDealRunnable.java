package cn.edu.ruc.adcourse.fileServer;

import com.google.gson.reflect.TypeToken;
import cn.edu.ruc.adcourse.requestContent.RequestBody;
import cn.edu.ruc.adcourse.storage.FileStorageDAO;
import cn.edu.ruc.adcourse.storage.FileStorageInfo;
import cn.edu.ruc.adcourse.utils.CheckSumUtil;
import cn.edu.ruc.adcourse.utils.GsonUtil;
import cn.edu.ruc.adcourse.utils.SimpleThreadPool;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 该类用来处理节点服务器发过来的心跳包
 * Created by Rainmaple on 2019/11/02.
 */
public class FileStorageDataDealRunnable implements Runnable{
    //下面这个阻塞队列用来取节点服务器发过来的数据，没有则一直等
    private LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

    /**
     * 往阻塞队列里面添加数据
     * @param data
     */
    public void add(byte[] data){
        if(data == null || data.length == 0)
            return;
        queue.add(data);
    }

    @Override
    public void run() {
        byte[] dataWithCheckSum = new byte[0];
        int length = dataWithCheckSum.length;
        while (!SimpleThreadPool.getInstance().isShutdownJudge()){
            try {
                //下面这个函数会从阻塞队列中取数据，如果有直接取出，如果没有则一直等
                dataWithCheckSum = queue.take();
                length = dataWithCheckSum.length;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //CRC验证
            byte[] chuckSum = CheckSumUtil.getCRC32Value(dataWithCheckSum, 0, length - 4);
            boolean isRight = true;
            for (int i = length - 4; i < length; i++) {
                if(chuckSum[i - length + 8] != dataWithCheckSum[i])
                    isRight = false;
            }
            if(isRight){
                String info = new String(dataWithCheckSum, 0, length - 4, StandardCharsets.UTF_8);
                String[] infos = info.split("####");
                RequestBody<FileStorageInfo> body = GsonUtil.getInstance().fromJson(infos[0],
                        new TypeToken<RequestBody<FileStorageInfo>>(){}.getType());

                if(FileStorageDAO.queryStorageNodeByName(body.getData().getName()) == null ||
                        !FileStorageDAO.queryStorageNodeByName(body.getData().getName()).isUseful()){
                    System.out.println("节点启动：" + body.getData().getName());
                }
                //如果心跳包携带的uuid信息是一个合法的uuid，则表示存储节点收到一个新文件
                //并通过心跳包携带信息告诉FileServer
                switch (Integer.valueOf(infos[1])){
                    case 0:     //表示普通心跳包
                        break;
                    case 1:     //表示携带添加文件信息的心跳包
                        System.out.println("添加文件心跳包");
                        FileStorageDAO.addFileInfoToNode(body.getData().getName(), infos[2]);
                        break;
                    case 2:     //表示携带删除文件信息的心跳包
                        System.out.println("删除文件心跳包");
                        FileStorageDAO.subFileInfoFromNode(body.getData().getName(), infos[2]);
                        break;
                }
                FileStorageDAO.addOrUpdate(body.getData());
            }else {
                System.out.println("CRC校验失败，本次传输出错");
            }
        }
    }
}
