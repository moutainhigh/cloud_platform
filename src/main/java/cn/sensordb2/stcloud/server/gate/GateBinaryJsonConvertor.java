package cn.sensordb2.stcloud.server.gate;

import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/2/19.
 */
public class GateBinaryJsonConvertor {
    public final static byte HEART_BEART = (byte)0x81;
    public final static byte LOGIN = (byte)0x80;
    private final static String APPID = "appID";
    private final static String MAC = "mac";


    public static Request convert(Buffer buffer) {
        BinaryHeader binaryHeader = new BinaryHeader(buffer);
        if (!binaryHeader.isValid()) {
            return null;
        }
        else {
            switch (binaryHeader.getCmd()) {
                case HEART_BEART:
                    return GateBinaryJsonConvertor.convertHeartBeat(buffer, binaryHeader);
                case LOGIN:
                    return GateBinaryJsonConvertor.convertLogin(buffer, binaryHeader);
                default:
                    return null;
            }
        }
    }

    public static Request convertLogin(Buffer buffer, BinaryHeader binaryHeader) {
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


    public static Request convertHeartBeat(Buffer buffer, BinaryHeader binaryHeader) {
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
