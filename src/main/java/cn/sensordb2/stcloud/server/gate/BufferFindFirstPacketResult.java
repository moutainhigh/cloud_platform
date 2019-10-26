package cn.sensordb2.stcloud.server.gate;

import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/2/19.
 */
public class BufferFindFirstPacketResult {
    //�Ƿ��ҵ����Ƿ���magic���Ƿ��ư�ͷ��ȷ����Ҫ�ǳ��ȣ�
    boolean found = false;
    boolean noMagic = false;
    boolean noLength = false;
    boolean packetNotComplete = false;
    boolean cloudHeaderError = false;
    Buffer packet;
    //�ҵ�������
    int beginIndex,endIndex;


    public BufferFindFirstPacketResult() {
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean isNoMagic() {
        return noMagic;
    }

    public void setNoMagic(boolean noMagic) {
        this.noMagic = noMagic;
    }

    public Buffer getPacket() {
        return packet;
    }

    public void setPacket(Buffer packet) {
        this.packet = packet;
    }

    public boolean isCloudHeaderError() {
        return cloudHeaderError;
    }

    public void setCloudHeaderError(boolean cloudHeaderError) {
        this.cloudHeaderError = cloudHeaderError;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
    public boolean isNoLength() {
        return noLength;
    }

    public void setNoLength(boolean noLength) {
        this.noLength = noLength;
    }

    public boolean isPacketNotComplete() {
        return packetNotComplete;
    }

    public void setPacketNotComplete(boolean packetNotComplete) {
        this.packetNotComplete = packetNotComplete;
    }
}
