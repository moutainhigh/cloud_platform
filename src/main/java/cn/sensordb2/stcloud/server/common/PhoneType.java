package cn.sensordb2.stcloud.server.common;

/**
 * Created by sensordb on 16/6/4.
 */
public class PhoneType {
    public static final String APPLE = "apple";

    public static boolean isIosDevice(String terminalType) {
        return APPLE.equalsIgnoreCase(terminalType);
    }
}
