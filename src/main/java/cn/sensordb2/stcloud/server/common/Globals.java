package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.util.IniUtil;

/**
 * Created by sensordb on 16/7/9.
 */
public class Globals {
    private static boolean DEBUG = false;
    public static final String DELIMIT = "\r\n";
    public static String DEFAULT_CREATED_USER_PASSWORD = "123456";
    public static String DEFAULT_TEST_VERIFICATION_CODE = "888888";
    public static String DEFAULT_HASHED_PASSWORD = "1F82C942BEFDA29B6ED487A51DA199F78FCE7F05";
    public static final String JKS_FILE_PATH = "cloud/conf/private.jks";
    public static final String JKS_PASSWORD = "111111";
    public static final String tcpConnectionTable = "tcpConnectionInfo";
    public static final String guardPassTable = "guardPass";
    public static final String NULL = null;


    public final static String msgs = "msgs";
    public final static String msgsLog = "msgsLog";

    public final static String pushMsgs = "pushMsgs";
    public final static String hlockConfig = "hlockConfig";

    public final static String jsonVersionFieldName = IniUtil.getInstance().getJsonVersionFieldName();
    public final static String jsonVersionFieldValue = IniUtil.getInstance().getJsonVersionFieldValue();


    private static String databaseConfFile = "cloud/conf/database.conf";
//  private static  String databaseConfFile = "cloud/conf/databaseRemoteDebug.conf";
    //private static  String databaseConfFile = "cloud/conf/databaseLocalDebug.conf";

    private static String realTimeDatabaseConfFile = "cloudRT/conf/database.conf";
    private static String resCloudDatabaseConfFile = "resCloud/conf/database.conf";
//    private static String resCloudDatabaseConfFile = "resCloud/conf/databaseLocalDebug.conf";

    /*
     * for debug
     */
    public static void setDatabaseConfFile(String databaseConfFile) {
        Globals.databaseConfFile = databaseConfFile;
    }

    public static void setRemoteDatabaseConfFile() {
        Globals.databaseConfFile = "cloud/conf/databaseRemoteDebug.conf";
    }

    public static void setLocalDatabaseConfFile() {
        Globals.databaseConfFile = "cloud/conf/databaseLocalDebug.conf";
    }

    public static String getDatabaseConfFile() {
        return databaseConfFile;
    }

    public static String getRealTimeDatabaseConfFile() {
        return realTimeDatabaseConfFile;
    }

    public static void setRealTimeDatabaseConfFile(String realTimeDatabaseConfFile) {
        Globals.realTimeDatabaseConfFile = realTimeDatabaseConfFile;
    }

    public static String getResCloudDatabaseConfFile() {
        return resCloudDatabaseConfFile;
    }

    public static void setResCloudDatabaseConfFile(String resCloudDatabaseConfFile) {
        Globals.resCloudDatabaseConfFile = resCloudDatabaseConfFile;
    }

    public static String getTcpConnectionTable() {
        return tcpConnectionTable;
    }
}
