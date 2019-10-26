package cn.sensordb2.stcloud.server.gate;

import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * Created by sensordb on 16/2/19.
 */
public class BinaryHeader {
    private static final int APPID_OFFSET = 0;
    private static final int APPID_LENGTH = 15;
    private static final int ISRELAY_OFFSET = 15;
    private static final int MAC_OFFSET = 20;
    private static final int MAC_LENGTH = 6;
    private static final int MAGIC_OFFSET_BEGIN = 26;
    private static final int MAGIC_OFFSET_END = 30;
    private static final int LENGTH_OFFSET = 30;
    private static final int SUM_OFFSET = 31;
    private static final int CMD_OFFSET = 32;
    private static final int ID_OFFSET = 33;
    private static final int HEADER_LENGTH = 34;
    private static final int CLOUD_HEADER_LENGTH = 26;
    private static final int LOCAL_HEADER_LENGTH = 8;
    static byte[] reserve = {0, 0, 0, 0};
    public static final String MAGIC = "HAPI";
    private Buffer buffer;
    private boolean isValid = false;
    private short len;
    private Buffer appID;
    private Buffer realAppID;
    private Buffer mac;
    private byte isRelay;
    private Buffer magic;
    private short sum;
    private byte cmd;
    private short id;

    public BinaryHeader() {
    }

    public BinaryHeader(Buffer buffer) {
        this.buffer = buffer;
        this.parse(buffer);
    }

    protected Buffer trimEndingZero(Buffer buffer) {
        if (buffer == null) {
            return buffer;
        }

        int index = Tools.indexOf(buffer, 0);

        if(index!=-1) {
            return buffer.getBuffer(0, index);
        }
        else return buffer;
    }

    protected void parse(Buffer buffer) {
        if(buffer==null||buffer.length()<HEADER_LENGTH) {
            this.isValid = false;
            return;
        }
        this.isValid = true;
        this.appID = this.buffer.getBuffer(APPID_OFFSET, APPID_OFFSET + APPID_LENGTH);
        this.realAppID = this.trimEndingZero(this.appID);
        this.isRelay = this.buffer.getByte(ISRELAY_OFFSET);
        this.mac = this.buffer.getBuffer(MAC_OFFSET, MAC_OFFSET + MAC_LENGTH);
        this.magic = this.buffer.getBuffer(MAGIC_OFFSET_BEGIN, MAGIC_OFFSET_END);
        this.len = this.buffer.getUnsignedByte(LENGTH_OFFSET);
        this.sum = this.buffer.getUnsignedByte(SUM_OFFSET);
        this.cmd = this.buffer.getByte(CMD_OFFSET);
        this.id = this.buffer.getUnsignedByte(ID_OFFSET);
    }

    public static int getMagicOffsetBegin() {
        return MAGIC_OFFSET_BEGIN;
    }

    public static int getMagicOffsetEnd() {
        return MAGIC_OFFSET_END;
    }

    public static int getLengthOffset() {
        return LENGTH_OFFSET;
    }

    public boolean isValid() {
        return isValid;
    }

    public short getLen() {
        return len;
    }

    public Buffer getAppID() {
        return appID;
    }

    public Buffer getMac() {
        return mac;
    }

    public byte getIsRelay() {
        return isRelay;
    }

    public Buffer getMagic() {
        return magic;
    }

    public short getSum() {
        return sum;
    }

    public byte getCmd() {
        return cmd;
    }

    public short getId() {
        return id;
    }

    public static byte getHeaderLength() {
        return HEADER_LENGTH;
    }

    public void setLen(short len) {
        this.len = len;
    }

    public void setAppID(Buffer appID) {
        this.appID = appID;
    }

    public void setMac(Buffer mac) {
        this.mac = mac;
    }

    public void setMacReverse(Buffer mac) {
        this.mac = BufferFactory.reverseBuffer(mac);
    }


    public void setIsRelay(byte isRelay) {
        this.isRelay = isRelay;
    }

    public void setMagic(Buffer magic) {
        this.magic = magic;
    }

    public void setSum(short sum) {
        this.sum = sum;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public void setId(short id) {
        this.id = id;
    }

    public static int getAppidLength() {
        return APPID_LENGTH;
    }

    public static int getMacLength() {
        return MAC_LENGTH;
    }

    public Buffer toBuffer() {
        Buffer buffer = Buffer.buffer();

        buffer.appendBuffer(appID);
        buffer.appendByte(isRelay);
        buffer.appendBytes(reserve);
        buffer.appendBuffer(mac);
        buffer.appendBuffer(magic);
        buffer.appendUnsignedByte(len);
        buffer.appendUnsignedByte(sum);
        buffer.appendUnsignedByte(cmd);
        buffer.appendUnsignedByte(id);

        return buffer;
    }

    public static String getDefaultMagic() {
        return MAGIC;
    }

    public String toString() {
        JsonObject result = new JsonObject();
        result.put("appID", this.getAppID().toString());
        result.put("isRelay", this.getIsRelay());
        result.put("mac", this.getMac().toString());
        result.put("len", this.getLen());
        result.put("sum", this.getSum());
        result.put("cmd", this.getCmd());
        result.put("id", this.getId());
        return result.toString();
    }

    public static int caculatePayloadLength(Buffer buffer, int magicIndex) {
        return buffer.getUnsignedByte(magicIndex + 4);
    }

    public static int getLocalHeaderLength() {
        return BinaryHeader.LOCAL_HEADER_LENGTH;
    }

    public static int getCloudHeaderLength() {
        return BinaryHeader.CLOUD_HEADER_LENGTH;
    }

    public Buffer getRealAppID() {
        return realAppID;
    }
}
