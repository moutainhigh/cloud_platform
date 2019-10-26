package cn.sensordb2.stcloud.server.gate;

import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/2/19.
 */
public class GateBinaryRequestFactory {
    public static Buffer heartBeat(String appID, Buffer mac, int id) {
        BinaryHeader binaryHeader = new BinaryHeader();
        binaryHeader.setAppID(BinaryProtocolUtil.fixedSizeWithPadding(BinaryHeader.getAppidLength(), appID));
        binaryHeader.setIsRelay((byte) 0);
        binaryHeader.setMac(BinaryProtocolUtil.fixedSizeWithPadding(BinaryHeader.getMacLength(), mac));
        binaryHeader.setMagic(Buffer.buffer(binaryHeader.getDefaultMagic()));
        binaryHeader.setCmd(GateBinaryJsonConvertor.HEART_BEART);
        binaryHeader.setId((byte) id);
        binaryHeader.setLen((byte) 0);
        binaryHeader.setSum((byte)0);
        return binaryHeader.toBuffer();
    }

    public static Buffer login(String appID, Buffer mac, int id, String loginID, String password) {
        BinaryHeader binaryHeader = new BinaryHeader();
        binaryHeader.setAppID(BinaryProtocolUtil.fixedSizeWithPadding(BinaryHeader.getAppidLength(), appID));
        binaryHeader.setIsRelay((byte) 0);
        binaryHeader.setMac(BinaryProtocolUtil.fixedSizeWithPadding(BinaryHeader.getMacLength(), mac));
        binaryHeader.setMagic(Buffer.buffer(binaryHeader.getDefaultMagic()));
        binaryHeader.setCmd(GateBinaryJsonConvertor.LOGIN);
        binaryHeader.setId((byte) id);

        int loginIDLength = 15;
        int passwordLength = 40;
        Buffer payload = Buffer.buffer();
        payload.appendBuffer(BinaryProtocolUtil.fixedSizeWithPadding(loginIDLength, loginID));
        payload.appendBuffer(BinaryProtocolUtil.fixedSizeWithPadding(passwordLength, password));

        binaryHeader.setLen((byte) payload.length());
        binaryHeader.setSum((byte) 0);

        return binaryHeader.toBuffer().appendBuffer(payload);
    }
}
