package cn.sensordb2.stcloud.server.common;

/**
 * Created by sensordb on 16/8/19.
 */
public abstract class HYNetSocket {
    public abstract void write(byte[] bytes);
    public abstract void close();
}
