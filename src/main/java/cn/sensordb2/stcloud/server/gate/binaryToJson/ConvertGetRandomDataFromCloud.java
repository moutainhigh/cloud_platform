package cn.sensordb2.stcloud.server.gate.binaryToJson;

import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.common.BinaryConvertor;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import io.vertx.core.buffer.Buffer;

/**
 {
     "zl_cloud": "1.0",
     "method": "GET_RANDOM_DATA",
     "params":{
        "userID":#
     },
     "id": #
 }
 */

public class ConvertGetRandomDataFromCloud extends BinaryConvertor {
    public static byte CMD_ID = (byte)0x83;

    @Override
    public Request convert(Buffer buffer, BinaryHeader binaryHeader) {
        int headerLength = BinaryHeader.getHeaderLength();

        if (buffer.length() < headerLength+1) {
            return null;
        }

        int inputStringLength = buffer.getByte(headerLength);

        if (inputStringLength <= 0) {
            return null;
        }

        int totalLength = headerLength+1+inputStringLength;
        if (buffer.length() < totalLength) {
            return null;
        }

        String userID = buffer.getString(headerLength+1, totalLength);

        Request request = RequestFactory.GET_RANDOM_DATA(binaryHeader.getId(), userID);

        request.setBinaryHeader(binaryHeader);
        return request;
    }
}
