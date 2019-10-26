package cn.sensordb2.stcloud.util;

/**
 * Created by sensordb on 16/10/19.
 */
public class SocketShutDownReason {
    public static final String SHUTDOWN_OLD = "SHUTDOWN_OLD";
    public static final String SHUTDOWN_BY_CLIENT = "SHUTDOWN_BY_CLIENT";
    public static final String SHUTDOWN_BY_FRAMEWORK_CLOSEHANDLER = "SHUTDOWN_BY_FRAMEWORK_CLOSEHANDLER";
    public static final String SHUTDOWN_NOT_RING_OR_APP = "SHUTDOWN_NOT_RING_OR_APP";
    public static final String SHUTDOWN_EXCEPTION = "SHUTDOWN_EXCEPTION";
    public static final String SHUTDOWN_USER_PASSWORD_ERROR = "SHUTDOWN_USER_PASSWORD_ERROR";
}
