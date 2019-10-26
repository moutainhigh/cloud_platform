package cn.sensordb2.stcloud.server.common;

/**
 * Created by sensordb on 16/2/18.
 */
public class ConnectionProtocolType {
    public static final int BINARY = 1;
    public static final int JSON = 2;
    public static final int UNKNOWN = 0;

    public static boolean isUnknown(int type) {
        return type==UNKNOWN;
    }

    public static boolean isBinary(int type) {
        return type==BINARY;
    }
}
