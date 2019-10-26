package cn.sensordb2.stcloud.server.gate;

/**
 * Created by sensordb on 16/2/18.
 */

import cn.sensordb2.stcloud.util.PacketFormatException;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;
import org.apache.log4j.Logger;

import java.util.Vector;

public class BinaryProtocolUtil {
    private static final byte PADDING = 0;
    public static Logger logger = Logger.getLogger(BinaryProtocolUtil.class);

    public static boolean isBufferCheckable(Buffer buffer) {
        return buffer != null && buffer.length() >= BinaryHeader.getHeaderLength();
    }

    public static boolean hasBinaryProtocolPacket(Buffer buffer) {
        int magicIndex = Tools.indexOf(buffer, BinaryHeader.getDefaultMagic());
        if (magicIndex == -1) {
            return false;
        }
        else return true;
    }

    public static BufferFindFirstPacketResult findFirstPacket(Buffer buffer) {
        BufferFindFirstPacketResult bufferFindFirstPacketResult = new BufferFindFirstPacketResult();
        if (buffer.length() < BinaryHeader.getHeaderLength()) {
            bufferFindFirstPacketResult.setNoLength(true);
            return bufferFindFirstPacketResult;
        }

        int magicIndex = Tools.indexOf(buffer, BinaryHeader.getDefaultMagic());
        if (magicIndex == -1) {
            bufferFindFirstPacketResult.setNoMagic(true);
            return bufferFindFirstPacketResult;
        }

        if(buffer.length()<magicIndex+5) {
            bufferFindFirstPacketResult.setNoMagic(false);
            bufferFindFirstPacketResult.setNoLength(true);
            return bufferFindFirstPacketResult;
        }

        int length = BinaryHeader.caculatePayloadLength(buffer, magicIndex);
        if (buffer.length() < magicIndex + BinaryHeader.getLocalHeaderLength() + length) {
            bufferFindFirstPacketResult.setNoMagic(false);
            bufferFindFirstPacketResult.setPacketNotComplete(true);
            return bufferFindFirstPacketResult;
        }
        else {
            if(magicIndex< BinaryHeader.getCloudHeaderLength()) {
                bufferFindFirstPacketResult.setNoMagic(false);
                bufferFindFirstPacketResult.setCloudHeaderError(true);
                int beginIndex = 0;
                int endIndex = magicIndex + BinaryHeader.getLocalHeaderLength() + length;
                bufferFindFirstPacketResult.setPacket(buffer.getBuffer(beginIndex, endIndex));
                bufferFindFirstPacketResult.setBeginIndex(beginIndex);
                bufferFindFirstPacketResult.setEndIndex(endIndex);
                return bufferFindFirstPacketResult;
            }
            else {
                bufferFindFirstPacketResult.setCloudHeaderError(false);
                bufferFindFirstPacketResult.setFound(true);
                int beginIndex = magicIndex - BinaryHeader.getCloudHeaderLength();
                int endIndex = beginIndex + BinaryHeader.getHeaderLength() + length;
                bufferFindFirstPacketResult.setPacket(buffer.getBuffer(beginIndex, endIndex));
                bufferFindFirstPacketResult.setBeginIndex(beginIndex);
                bufferFindFirstPacketResult.setEndIndex(endIndex);
                return bufferFindFirstPacketResult;
            }
        }
    }

    public static BufferSplitResult splitBinary(Buffer buffer) {
        Buffer lastBuffer = Buffer.buffer();
        Buffer remainBuffer = buffer;
        Vector<Buffer> tokens = new Vector<Buffer>();
        Buffer token;
        BufferFindFirstPacketResult findResult;
        while (remainBuffer.length() > 0) {
            findResult = BinaryProtocolUtil.findFirstPacket(remainBuffer);

            if(findResult.isFound()) {
                token = findResult.getPacket();
                tokens.add(token);
                remainBuffer = remainBuffer.slice(findResult.getEndIndex(), remainBuffer.length());
            }
            else {
                if(findResult.isNoMagic()||findResult.isNoLength()||findResult.isPacketNotComplete()) {
                    lastBuffer = remainBuffer;
                    break;
                }
                else {
                    logger.error(String.format("Found Error Packet:%s", Tools.bufferToPrettyByteStringForGate(findResult.getPacket())));
                    remainBuffer = remainBuffer.slice(findResult.getEndIndex(), remainBuffer.length());
                }
            }
        }
        return new BufferSplitResult(tokens, lastBuffer);
    }

    public static Buffer fixedSizeWithPadding(int size, String str) {
        return BinaryProtocolUtil.fixedSizeWithPadding(size, Buffer.buffer(str));
    }

    /*
     */
    public static Buffer fixedSizeWithPadding(int size, Buffer buffer) {
        if(buffer.length()>size) {
            return buffer.getBuffer(0, size);
        }
        Buffer result = Buffer.buffer(size);
        result.appendBuffer(buffer);

        while (result.length() < size) {
            result.appendByte(PADDING);
        }
        return result;
    }

    public static Buffer trimPadding(Buffer buffer) {
        int endPaddingIndex = buffer.length();

        int i;
        for (i = endPaddingIndex - 1; i >= 0; i--) {
            if (buffer.getByte(i) != 0)
                break;
        }

        if (i < 0) {
            return Buffer.buffer();
        } else {
            return buffer.getBuffer(0, i+1);
        }
    }

    /*calculate checksum
     */
    public static byte caculateSum(Buffer buffer) {
        return 0;

    }

    public static int getReturnCode(Buffer buffer) throws  PacketFormatException{
        if (buffer == null || buffer.length() < BinaryHeader.getHeaderLength() + 1) {
            throw new PacketFormatException("buffer error");
        }

        return buffer.getByte(BinaryHeader.getHeaderLength());
    }

}