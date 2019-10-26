package cn.sensordb2.stcloud.server.gate.binaryToJson;

import cn.sensordb2.stcloud.server.common.BinaryConvertor;
import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.server.gate.BufferFactory;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/3/12.
 */
public class ConvertLockUploadWarning extends BinaryConvertor {
    public static byte CMD_ID = (byte)0x82;

    @Override
    public Request convert(Buffer buffer, BinaryHeader binaryHeader) {
        int typeLength = 1;
        int headerLength = BinaryHeader.getHeaderLength();
        int totalLength = headerLength+typeLength;

        if (buffer.length() != totalLength) {
            return null;
        }

        int type = buffer.getByte(headerLength);
        //门锁设备传递的mac地址在高位
        Buffer mac = BufferFactory.reverseBuffer(binaryHeader.getMac());
        String lockID = Tools.encodeBufferBase64(mac);

        Request request = RequestFactory.LOCK_UPLOAD_WARNING(binaryHeader.getId(), lockID, type);
        request.setBinaryHeader(binaryHeader);
        return request;
    }
}
