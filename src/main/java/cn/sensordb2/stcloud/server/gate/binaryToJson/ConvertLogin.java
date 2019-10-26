package cn.sensordb2.stcloud.server.gate.binaryToJson;

import cn.sensordb2.stcloud.server.common.BinaryConvertor;
import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/3/12.
 */
public class ConvertLogin extends BinaryConvertor {
    public static byte CMD_ID = (byte)0x80;

    @Override
    public Request convert(Buffer buffer, BinaryHeader binaryHeader) {
        int login_id_length = 15;
        int hashed_pwd_length = 40;
        int headerLength = BinaryHeader.getHeaderLength();
        int totalLength = headerLength+login_id_length+hashed_pwd_length;

        if (buffer.length() != totalLength) {
            return null;
        }

        String login_id = buffer.getString(headerLength, headerLength + login_id_length);

        int hashed_pwd_offset = headerLength+login_id_length;
        String hashed_pwd = buffer.getString(hashed_pwd_offset, hashed_pwd_offset+hashed_pwd_length);
        Request request = RequestFactory.LOGIN(binaryHeader.getId(), login_id, hashed_pwd);

        request.setBinaryHeader(binaryHeader);
        return request;
    }
}
