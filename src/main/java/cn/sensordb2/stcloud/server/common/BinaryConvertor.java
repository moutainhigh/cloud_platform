package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.server.message.Request;
import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/3/12.
 */
abstract public class BinaryConvertor {
    public abstract Request convert(Buffer buffer, BinaryHeader binaryHeader) throws Exception;
}
