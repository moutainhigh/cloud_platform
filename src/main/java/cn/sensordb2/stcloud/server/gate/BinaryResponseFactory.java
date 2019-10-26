package cn.sensordb2.stcloud.server.gate;

import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/2/19.
 */
public class BinaryResponseFactory {
    static byte[] reserve = {0, 0, 0, 0};
    static byte[] magic = {'H', 'A', 'P', 'I'};

    public static Buffer successResponse(Request request) {
        Buffer buffer = Buffer.buffer();
        Buffer appID = request.getBinaryHeader().getAppID();
        byte isRelay = 0;
        //reserve
        Buffer mac = request.getBinaryHeader().getMac();
        //magic
        byte len = 1;
        byte sum = 0;
        short cmd = request.getBinaryHeader().getCmd();
        short id = request.getBinaryHeader().getId();
        byte code = 0;

        buffer.appendBuffer(appID);
        buffer.appendByte(isRelay);
        buffer.appendBytes(reserve);
        buffer.appendBuffer(mac);
        buffer.appendBytes(magic);
        buffer.appendByte(len);
        buffer.appendByte(sum);
        buffer.appendUnsignedByte(cmd);
        buffer.appendUnsignedByte(id);
        buffer.appendByte(code);

        return buffer;
    }

    public static Buffer successResponse(Request request, Buffer extra) {
        Buffer buffer = Buffer.buffer();
        Buffer appID = request.getBinaryHeader().getAppID();
        byte isRelay = 0;
        //reserve
        Buffer mac = request.getBinaryHeader().getMac();
        //magic
        byte len = (byte)(1+extra.length());
        byte sum = 0;
        short cmd = request.getBinaryHeader().getCmd();
        short id = request.getBinaryHeader().getId();
        byte code = 0;

        buffer.appendBuffer(appID);
        buffer.appendByte(isRelay);
        buffer.appendBytes(reserve);
        buffer.appendBuffer(mac);
        buffer.appendBytes(magic);
        buffer.appendByte(len);
        buffer.appendByte(sum);
        buffer.appendUnsignedByte(cmd);
        buffer.appendUnsignedByte(id);
        buffer.appendByte(code);
        buffer.appendBuffer(extra);

        return buffer;
    }


    public static Buffer errorResponse(Request request, byte code) {
        Buffer buffer = Buffer.buffer();
        Buffer appID = request.getBinaryHeader().getAppID();
        byte isRelay = 0;
        //reserve
        Buffer mac = request.getBinaryHeader().getMac();
        //magic
        byte len = 1;
        byte sum = code;
        short cmd = request.getBinaryHeader().getCmd();
        short id = request.getBinaryHeader().getId();
        //byte code = 0;

        buffer.appendBuffer(appID);
        buffer.appendByte(isRelay);
        buffer.appendBytes(reserve);
        buffer.appendBuffer(mac);
        buffer.appendBytes(magic);
        buffer.appendByte(len);
        buffer.appendByte(sum);
        buffer.appendUnsignedByte(cmd);
        buffer.appendUnsignedByte(id);
        buffer.appendByte(code);


        return buffer;
    }

    public static Buffer errorResponse(Request request, byte code, Buffer extra) {
        Buffer buffer = Buffer.buffer();
        Buffer appID = request.getBinaryHeader().getAppID();
        byte isRelay = 0;
        //reserve
        Buffer mac = request.getBinaryHeader().getMac();
        //magic
        byte len = (byte)(1+extra.length());
        byte sum = code;
        short cmd = request.getBinaryHeader().getCmd();
        short id = request.getBinaryHeader().getId();
        //byte code = 0;

        buffer.appendBuffer(appID);
        buffer.appendByte(isRelay);
        buffer.appendBytes(reserve);
        buffer.appendBuffer(mac);
        buffer.appendBytes(magic);
        buffer.appendByte(len);
        buffer.appendByte(sum);
        buffer.appendUnsignedByte(cmd);
        buffer.appendUnsignedByte(id);
        buffer.appendByte(code);
        buffer.appendBuffer(extra);
        return buffer;
    }

    public static Buffer errorResponseForInvalidFormatRequest(byte code) {
        Buffer buffer = Buffer.buffer();
        Buffer appID = Tools.createSameValueBuffer(0, 15);
        byte isRelay = 0;
        //reserve
        Buffer mac = Buffer.buffer("000000");
        //magic
        byte len = 1;
        byte sum = code;
        short cmd = -1;
        short id = -1;
        //byte code = 0;

        buffer.appendBuffer(appID);
        buffer.appendByte(isRelay);
        buffer.appendBytes(reserve);
        buffer.appendBuffer(mac);
        buffer.appendBytes(magic);
        buffer.appendByte(len);
        buffer.appendByte(sum);
        buffer.appendUnsignedByte(cmd);
        buffer.appendUnsignedByte(id);
        buffer.appendByte(code);


        return buffer;
    }

}
