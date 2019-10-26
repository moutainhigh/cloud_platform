package cn.sensordb2.stcloud.server.common;

/**
 * Created by sensordb on 16/11/4.
 */
public class AppSubAccountType {
    public static final int NO_LIMIT = 0;
    public static final int OPEN_DOOR_TIME_LIMIT = 1;
    public static final int OPEN_DOOR_COUNT_LIMIT = 2;

    public static boolean isValidType(int type) {
        return type>=0 && type<=2;
    }
}
