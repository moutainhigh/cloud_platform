package cn.sensordb2.stcloud.server.gate.binaryToJson;

import cn.sensordb2.stcloud.server.common.BinaryConvertor;
import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/3/12.
 */
public class ConvertHeartBeat extends BinaryConvertor {
    public static byte CMD_ID = (byte)0x81;

    @Override
    public Request convert(Buffer buffer, BinaryHeader binaryHeader) {
        int headerLength = BinaryHeader.getHeaderLength();
        int totalLength = headerLength;

        if (buffer.length() != totalLength) {
            return null;
        }

        Request heartbeat = RequestFactory.HEARTBEAT(binaryHeader.getId());
        heartbeat.setBinaryHeader(binaryHeader);
        return heartbeat;
    }
}
