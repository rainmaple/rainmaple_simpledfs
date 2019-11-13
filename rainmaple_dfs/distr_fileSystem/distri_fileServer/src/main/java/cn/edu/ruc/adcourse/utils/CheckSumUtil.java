package cn.edu.ruc.adcourse.utils;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

/**
 * Created by Rainmaple on 2019/7/8 0008.
 */
public class CheckSumUtil {
    private static final CRC32 c = new CRC32();

    /**
     * 根据传入的字节数组
     * @param bs
     * @return
     */
    public static byte[] getCRC32Value(byte[] bs, int off, int length){
        c.reset();
        c.update(bs, off, length);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(0, c.getValue());
        byte[] result = byteBuffer.array();
        return result;
    }
}
