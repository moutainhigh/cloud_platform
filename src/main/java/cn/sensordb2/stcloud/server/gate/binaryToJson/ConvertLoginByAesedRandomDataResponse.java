package cn.sensordb2.stcloud.server.gate.binaryToJson;

import cn.sensordb2.stcloud.server.common.BinaryConvertor;
import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import io.vertx.core.buffer.Buffer;

/**
 {
     "zl_cloud": "1.0",
     "method": "LOGIN_BY_RANDOM_DATA_RESPONSE",
     "params": {
         "code": #，
     },
     "id": #
 }
 */

public class ConvertLoginByAesedRandomDataResponse extends BinaryConvertor {
    public static byte CMD_ID = (byte)0x85;

    @Override
    public Request convert(Buffer buffer, BinaryHeader binaryHeader) {
        int headerLength = BinaryHeader.getHeaderLength();

        if (buffer.length() < headerLength+1) {
            return null;
        }

        int code = buffer.getByte(BinaryHeader.getHeaderLength());

        Request request = RequestFactory.LOGIN_BY_RANDOM_DATA_RESPONSE(binaryHeader.getId(), code);

        request.setBinaryHeader(binaryHeader);
        return request;
    }
}
