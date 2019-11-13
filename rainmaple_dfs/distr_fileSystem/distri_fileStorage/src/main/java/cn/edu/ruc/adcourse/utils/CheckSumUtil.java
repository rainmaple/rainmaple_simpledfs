package cn.edu.ruc.adcourse.utils;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class CheckSumUtil {
    private static final CRC32 c = new CRC32();

    /**
     * 根据传入的字节数组返回添加的CRC校验码
     * @param bs
     * @return
     */
    public static byte[] getCRC32Value(byte[] bs){
        c.reset();
        c.update(bs);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(0, c.getValue());
        byte[] result = byteBuffer.array();
        return result;
    }
}
