package cn.sensordb2.stcloud.server.gate.binaryToJson;

import cn.sensordb2.stcloud.server.common.BinaryConvertor;
import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;

/**
 {
     "zl_cloud":"1.0",
     "method": "AES_DESCRPT_RANDOM_DATA",
     "result":{
        "userID":#,
        "randomDataAes ":#
     },
     "id":#
 }
 */

public class ConvertDecryptAesedRandomData extends BinaryConvertor {
    public static byte CMD_ID = (byte)0x86;

    @Override
    public Request convert(Buffer buffer, BinaryHeader binaryHeader) {
        int headerLength = BinaryHeader.getHeaderLength();

        if (buffer.length() < headerLength+1) {
            return null;
        }

        int userIDLength = buffer.getByte(headerLength);

        if (userIDLength <= 0) {
            return null;
        }

        int includingUserIDLength = headerLength+1+userIDLength;
        if (buffer.length() < includingUserIDLength+16) {
            return null;
        }

        String userID = buffer.getString(headerLength+1, includingUserIDLength);
        Buffer randomDataAes = buffer.getBuffer(includingUserIDLength, includingUserIDLength+16);
        String randomDataAesString = Tools.encodeBufferBase64(randomDataAes);

        Request request = RequestFactory.AES_DESCRPT_RANDOM_DATA(binaryHeader.getId(), userID, randomDataAesString);

        request.setBinaryHeader(binaryHeader);
        return request;
    }
}
