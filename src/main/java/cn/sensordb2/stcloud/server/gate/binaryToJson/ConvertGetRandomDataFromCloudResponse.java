package cn.sensordb2.stcloud.server.gate.binaryToJson;

import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.common.BinaryConvertor;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;

/**
 {
     "zl_cloud": "1.0",
     "method": "GET_RANDOM_DATA_RESPONSE",
     "params":{
        "randomData":#
     },
     "id": #
 }
 */

public class ConvertGetRandomDataFromCloudResponse extends BinaryConvertor {
    public static byte CMD_ID = (byte)0x83;

    @Override
    public Request convert(Buffer buffer, BinaryHeader binaryHeader) {
        int headerLength = BinaryHeader.getHeaderLength();

        if (buffer.length() < headerLength+24) {
            return null;
        }

        String randomData = Tools.encodeBufferBase64(buffer.getBuffer(headerLength+1, headerLength+1+16));

        Request request = RequestFactory.GET_RANDOM_DATA_RESPONSE(binaryHeader.getId(), randomData);

        request.setBinaryHeader(binaryHeader);
        return request;
    }
}
