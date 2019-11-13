package cn.edu.ruc.adcourse;

import java.io.IOException;

/**
 * 同服务端进行交互，获取文件信息
 * Created by rainmaple on 2019/11/3.
 */
public class CommunicateWithFileServerStrategyImpl extends SocketSupport
        implements CommunicateWithFileServerStrategy{
    public CommunicateWithFileServerStrategyImpl(String ip, int port) throws IOException {
        super(ip, port);
    }

    @Override
    public String getStorageInfo() throws IOException {
        dos.writeInt(TransProtocol.CODE_MONITOR_GET_INFO);
        dos.writeUTF("");
        String result = dis.readUTF();
        dis.readInt();
        return result;
    }

    @Override
    public String[] getFileInfo() throws IOException {
        String[] results = new String[2];
        dos.writeInt(TransProtocol.CODE_MONITOR_GET_FILE_INFO);
        dos.writeUTF("");
        results[0] = dis.readUTF();
        results[1] = dis.readUTF();
        dis.readInt();
        return results;
    }
}
