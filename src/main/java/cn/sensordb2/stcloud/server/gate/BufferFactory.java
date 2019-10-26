package cn.sensordb2.stcloud.server.gate;

import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/2/20.
 */
public class BufferFactory {
    public static Buffer byteBuffer(byte b) {
        return Buffer.buffer().appendByte(b);
    }

    public static Buffer paddingBuffer(int size) {
        Buffer buffer = Buffer.buffer(size);
        for (int i = 0; i < size; i++) {
            buffer.appendByte((byte)0);
        }
        return buffer;
    }

    public static Buffer appIDBuffer(String appID) {
        return BinaryProtocolUtil.fixedSizeWithPadding(BinaryHeader.getAppidLength(), appID);
    }

    public static Buffer macBuffer(Buffer mac) {
        return BinaryProtocolUtil.fixedSizeWithPadding(BinaryHeader.getMacLength(), mac);
    }

    public static Buffer reverseBuffer(Buffer mac) {
        byte[] macBytes = mac.getBytes();
        byte[] reverseMacBytes = new byte[macBytes.length];

        for (int i = 0; i<macBytes.length; i++) {
            reverseMacBytes[i] = macBytes[macBytes.length-i-1];
        }
        return Buffer.buffer(reverseMacBytes);
    }

    public static String macBufferToString(Buffer mac) {
        return Tools.encodeBufferBase64(BufferFactory.reverseBuffer(mac));
    }

}
