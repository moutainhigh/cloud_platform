package cn.sensordb2.stcloud.util;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.common.UnLoginClient;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.mongo.MongoClient;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Hashtable;

/**
 * Created by sensordb on 16/4/2.
 */
public class HYLogger{
    public static final String ERROR_LOG_TABLE = "errorLog";
    Logger logger;
    final static String logFileStorageDir = System.getenv("HY_HOME")==null?
            "var/logs/cloudLogsEach":System.getenv("HY_HOME")+"/var/logs/cloudLogsEach" ;
    private static Hashtable<String, String> trackedUserIDInLog = IniUtil.getInstance().getTrackedUserIDInLog();
    private static final String BEFORE_LOGIN_LOG_PREFIX = "beforeLogin.";

    public HYLogger(Logger logger) {
        this.logger = logger;
    }

    public static HYLogger getLogger(Class cls) {
        return new HYLogger(Logger.getLogger(cls));
    }

    public static boolean isTrackedUser(String user) {
        if(!hasTrackedUser()) return true;

        return trackedUserIDInLog!=null&&trackedUserIDInLog.contains(user);
    }

    public static boolean hasTrackedUser() {
        return trackedUserIDInLog!=null&&trackedUserIDInLog.size()!=0;
    }

    public void info(Object msg) {
        //HYLogger.resetAllLoggerFile();
        if(HYLogger.hasTrackedUser()) return ;
//todo
       logger.info(msg);
    }

    public void error(Object msg) {
        //HYLogger.resetAllLoggerFile();
        if(HYLogger.hasTrackedUser()) return ;

        logger.error(msg);
    }

    public void exception(Throwable throwable) {
        //HYLogger.resetAllLoggerFile();
        if(HYLogger.hasTrackedUser()) return ;

        logger.error(Tools.getTrace(throwable));
    }

    public void exception(String msg) {
        //HYLogger.resetAllLoggerFile();
        if(HYLogger.hasTrackedUser()) return ;

        logger.error(msg);
    }


    public void info(Object msg, NetSocket socket) {
        try {
            HYLogger.changeAllLoggerFile(socket);
            ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(socket.hashCode());
            if(ci!=null&&ci.getUserID()!=null) {
                String userID = ci.getUserID();
                if(!HYLogger.isTrackedUser(userID)) return;
                logger.info(msg + " within " + userID);
                return;
            }

            if(HYLogger.hasTrackedUser()) return;
            logger.info(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void error(Object msg, NetSocket socket) {
        try {
            if (IniUtil.getInstance().isLogErrorToDatabase()) {
                JsonObject errorJson = new JsonObject();
                errorJson.put("error", msg.toString());
                errorJson.put("dateTime", Tools.dateToStr(new Date()));
                MongoClient mongoClient = Database.getInstance().getMongoClient();
                if(mongoClient!=null) {
                    mongoClient.insert(HYLogger.ERROR_LOG_TABLE, errorJson, result -> {
                    });
                }
            }
            HYLogger.changeAllLoggerFile(socket);
            ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(socket.hashCode());
            if (ci != null && ci.getUserID() != null) {
                String userID = ci.getUserID();
                if(!HYLogger.isTrackedUser(userID)) return;

                logger.error(msg + " within " + userID);
                return;
            }
            if(HYLogger.hasTrackedUser()) return ;
            logger.error(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exception(Object msg, NetSocket socket) {
        try {
            if (IniUtil.getInstance().isLogErrorToDatabase()) {
                JsonObject errorJson = new JsonObject();
                errorJson.put("error", msg.toString());
                errorJson.put("dateTime", Tools.dateToStr(new Date()));
                MongoClient mongoClient = Database.getInstance().getMongoClient();
                if(mongoClient!=null) {
                    mongoClient.insert(HYLogger.ERROR_LOG_TABLE, errorJson, result -> {
                    });
                }
            }
            HYLogger.changeAllLoggerFile(socket);
            ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(socket.hashCode());
            if (ci != null && ci.getUserID() != null) {
                String userID = ci.getUserID();
                if(!HYLogger.isTrackedUser(userID)) return;

                logger.error(msg + " within " + userID);
                return;
            }
            if(HYLogger.hasTrackedUser()) return ;
            logger.error(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void info(Object msg, ConnectionInfo ci) {
        try {
            HYLogger.changeAllLoggerFile(ci);
            if(ci!=null&&ci.getUserID()!=null) {
                String userID = ci.getUserID();
                if(!HYLogger.isTrackedUser(userID)) return;

                logger.info(msg + " within " + userID);
                return;
            }

            if(HYLogger.hasTrackedUser()) return ;
            //todo
            logger.info(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void error(Object msg,  ConnectionInfo ci) {
        try {
            if (IniUtil.getInstance().isLogErrorToDatabase()) {
                JsonObject errorJson = ci==null?new JsonObject():ci.toJsonObject();
                errorJson.put("error", msg.toString());
                errorJson.put("dateTime", Tools.dateToStr(new Date()));
                MongoClient mongoClient = Database.getInstance().getMongoClient();
                if(mongoClient!=null) {
                    mongoClient.insert(HYLogger.ERROR_LOG_TABLE, errorJson, result -> {
                    });
                }
            }
            HYLogger.changeAllLoggerFile(ci);
            if(ci!=null&&ci.getUserID()!=null) {
                String userID = ci.getUserID();
                if(!HYLogger.isTrackedUser(userID)) return;

                logger.error(msg + " within " + userID);
                return;
            }
            if(HYLogger.hasTrackedUser()) return ;
            logger.error(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exception(Object msg,  ConnectionInfo ci) {
        try {
            if (IniUtil.getInstance().isLogErrorToDatabase()) {
                JsonObject errorJson = ci==null?new JsonObject():ci.toJsonObject();
                errorJson.put("error", msg.toString());
                errorJson.put("dateTime", Tools.dateToStr(new Date()));
                MongoClient mongoClient = Database.getInstance().getMongoClient();
                if(mongoClient!=null) {
                    mongoClient.insert(HYLogger.ERROR_LOG_TABLE, errorJson, result -> {
                    });
                }
            }
            HYLogger.changeAllLoggerFile(ci);
            if(ci!=null&&ci.getUserID()!=null) {
                String userID = ci.getUserID();
                if(!HYLogger.isTrackedUser(userID)) return;

                logger.error(msg + " within " + userID);
                return;
            }
            if(HYLogger.hasTrackedUser()) return ;
            logger.error(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receivedDataLogJson(Buffer buffer, NetSocket socket, boolean isTemp) {
        HYLogger.changeAllLoggerFile(socket);
        ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(socket.hashCode());
        if(ci!=null&&ci.getUserID()!=null) {
            if(!HYLogger.isTrackedUser(ci.getUserID())) return;

            logger.info(String.format("Server received:[\n%s\n] from %s %s %s",
                    buffer.toString(),
                    ci.getAccountTypeString(),
                    ci.getUserID(),
                    socket.hashCode()));
            return;
        }
        else if(ci!=null&&ci.getPhoneNumber()!=null) {
            if(!HYLogger.isTrackedUser(ci.getPhoneNumber())) return;

            logger.info(String.format("Server received:[\n%s\n] from %s %s %s",
                    buffer.toString(),
                    "phone number",
                    ci.getPhoneNumber(),
                    socket.hashCode()));
            return;
        }
        else {
            if(HYLogger.hasTrackedUser()) return;
            logger.info(String.format("chyServer received:[\n%s\n] from %s",
                    buffer.toString(),
                    new UnLoginClient(socket, isTemp)));
            return;
        }
    }


    public void receivedDataLogBinary(Buffer buffer, NetSocket socket, boolean isTemp) {
        HYLogger.changeAllLoggerFile(socket);
        ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(socket.hashCode());
        if(ci!=null&&ci.getUserID()!=null) {
            if(!HYLogger.isTrackedUser(ci.getUserID())) return;

            logger.info(String.format("Server received:[\n%s\n{%s}] from %s %s %d",
                    Tools.bufferToPrettyByteStringForGate(buffer),
                    buffer.toString(),
                    ci.getAccountTypeString(),
                    ci.getUserID(),
                    socket.hashCode()));
            return;
        }
        else if(ci!=null&&ci.getPhoneNumber()!=null) {
            if(!HYLogger.isTrackedUser(ci.getPhoneNumber())) return;

            logger.info(String.format("Server received:[\n%s\n{%s}\n] from %s %s %d",
                    Tools.bufferToPrettyByteStringForGate(buffer),
                    buffer.toString(),
                    "phone number",
                    ci.getPhoneNumber(),
                    socket.hashCode()));
            return;
        }
        else {
            if(HYLogger.hasTrackedUser()) return;
            logger.info(String.format("Server received:[\n%s\n{%s}\n] from %s",
                    Tools.bufferToPrettyByteStringForGate(buffer),
                    buffer.toString(),
                    new UnLoginClient(socket, isTemp)));
            return;
        }
    }

    public void receivedDataLogJson(Buffer buffer, ConnectionInfo ci) {
        HYLogger.changeAllLoggerFile(ci);
        if(ci!=null&&ci.getUserID()!=null) {
            if(!HYLogger.isTrackedUser(ci.getUserID())) return;

            logger.info(String.format("Server received:[\n%s\n] from %s %s %d",
                    buffer.toString(),
                    ci.getAccountTypeString(),
                    ci.getUserID(),
                    ci.getConnectionID()));
            return;
        }
        else if(ci!=null&&ci.getPhoneNumber()!=null) {
            if(!HYLogger.isTrackedUser(ci.getPhoneNumber())) return;

            logger.info(String.format("Server received:[\n%s\n] from %s %s %d",
                    buffer.toString(),
                    "phone number",
                    ci.getPhoneNumber(),
                    ci.getConnectionID()));
            return;
        }
        else {
            if(HYLogger.hasTrackedUser()) return;
            logger.info(String.format("Server received:[\n%s\n]",
                    buffer.toString()));
            return;
        }
    }


    public void receivedDataLogBinary(Buffer buffer, ConnectionInfo ci) {
        HYLogger.changeAllLoggerFile(ci);
        if(ci!=null&&ci.getUserID()!=null) {
            if(!HYLogger.isTrackedUser(ci.getUserID())) return;

            logger.info(String.format("Server received:[\n%s\n{%s}] from %s %s %d",
                    Tools.bufferToPrettyByteStringForGate(buffer),
                    buffer.toString(),
                    ci.getAccountTypeString(),
                    ci.getUserID(),
                    ci.getConnectionID()));
            return;
        }
        else if(ci!=null&&ci.getPhoneNumber()!=null) {
            if(!HYLogger.isTrackedUser(ci.getPhoneNumber())) return;

            logger.info(String.format("Server received:[\n%s\n{%s}\n] from %s %s %d",
                    Tools.bufferToPrettyByteStringForGate(buffer),
                    buffer.toString(),
                    "phone number",
                    ci.getPhoneNumber(),
                    ci.getConnectionID()));
            return;
        }
        else {
            if(HYLogger.hasTrackedUser()) return;

            logger.info(String.format("Server received:[\n%s\n{%s}\n]",
                    Tools.bufferToPrettyByteStringForGate(buffer),
                    buffer.toString()));
            return;
        }
    }


    public void sendDataLogJson(Buffer buffer, ConnectionInfo ci) {
        HYLogger.changeAllLoggerFile(ci);
        if(ci!=null&&ci.getUserID()!=null) {
            if(!HYLogger.isTrackedUser(ci.getUserID())) return;

            logger.info(String.format("Server send:[\n%s\n] to %s %s %d",
                    buffer.toString(),
                    ci.getAccountTypeString(),
                    ci.getUserID(),
                    ci.getConnectionID()));
            return;
        }
        else if(ci!=null&&ci.getPhoneNumber()!=null) {
            if(!HYLogger.isTrackedUser(ci.getPhoneNumber())) return;

            logger.info(String.format("Server send:[\n%s\n] to %s %s %d",
                    buffer.toString(),
                    "phone number",
                    ci.getPhoneNumber(),
                    ci.getConnectionID()));
            return;
        }
        else {
            if(HYLogger.hasTrackedUser()) return;

            logger.info(String.format("Server send:[\n%s\n]",
                    buffer.toString()));
            return;
        }
    }


    public void sendDataLogBinary(Buffer buffer, ConnectionInfo ci) {
        HYLogger.changeAllLoggerFile(ci);
        if(ci!=null&&ci.getUserID()!=null) {
            if(!HYLogger.isTrackedUser(ci.getUserID())) return;

            logger.info(String.format("Server send:[\n%s\n{%s}] to %s %s %d",
                    Tools.bufferToPrettyByteStringForGate(buffer),
                    buffer.toString(),
                    ci.getAccountTypeString(),
                    ci.getUserID(),
                    ci.getConnectionID()));
            return;
        }
        else if(ci!=null&&ci.getPhoneNumber()!=null) {
            if(!HYLogger.isTrackedUser(ci.getPhoneNumber())) return;

            logger.info(String.format("Server send:[\n%s\n{%s}\n] to %s %s %d",
                    Tools.bufferToPrettyByteStringForGate(buffer),
                    buffer.toString(),
                    "phone number",
                    ci.getPhoneNumber(),
                    ci.getConnectionID()));
            return;
        }
        else {
            if(HYLogger.hasTrackedUser()) return;

            logger.info(String.format("Server send:[\n%s\n{%s}\n]",
                    Tools.bufferToPrettyByteStringForGate(buffer),
                    buffer.toString()));
            return;
        }
    }

    public static void changeAllLoggerFile(NetSocket socket) {
        try {
            ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(socket.hashCode());

            String path = null;
            String fileName;
            if(ci!=null&&ci.getUserID()!=null) {
                path = Tools.dateToStrYMDDot(ci.getConnectedDate(), null);
                fileName = connectionToLogFileName(ci);
            }
            else {
                Date date = new Date();
                if (ci != null) {
                    date = ci.getConnectedDate();
                }
                path = Tools.dateToStrYMDDot(date, BEFORE_LOGIN_LOG_PREFIX);
                fileName = connectionToLogFileName(socket.hashCode());
            }

            HYLogger.changeAllLoggerFile(path, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String connectionToLogFileName(ConnectionInfo ci) {
        StringBuffer sb = new StringBuffer();
        Date connectedDate = ci.getConnectedDate();
        sb.append(ci.getUserID());

        if (ci.getSessionID() != null) {
            sb.append("_");
            sb.append(ci.getSessionID());
        }

        if(IniUtil.getInstance().getIgnoreUserName().equals(ci.getUserID())) {
            return sb.toString();
        }

        if(connectedDate!=null&&!IniUtil.getInstance().isClientInOneFile()) {
            sb.append('_');
            sb.append(Tools.dateToStrForLog(connectedDate));
            sb.append('.');
            sb.append(ci.getConnectionID());
        }
        return sb.toString();
    }

    public static String connectionToLogFileName(int connectionID) {
        ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(connectionID);

        StringBuffer sb = new StringBuffer();

        if (IniUtil.getInstance().isIgnoreBeforeLogin()) {
            sb.append("client.beforelogin");
        }
        else {
            sb.append("client");
            sb.append('.');
            sb.append(connectionID);
        }

        if(ci!=null&&ci.isSocketIOConnection()) {
            sb.append(".socketio");
        }
        else if(ci!=null&&ci.isSocketIOConnection()) {
            sb.append(".http");
        }
        return sb.toString();
    }


    public static void changeAllLoggerFile(ConnectionInfo ci) {
        if (ci == null) {
            return;
        }
        String path = null;
        String fileName;
        if (ci != null && ci.getUserID() != null) {
            path = Tools.dateToStrYMDDot(ci.getConnectedDate(), null);
            fileName = connectionToLogFileName(ci);
        } else {
            path = Tools.dateToStrYMDDot(ci.getConnectedDate(), BEFORE_LOGIN_LOG_PREFIX);
            fileName = connectionToLogFileName(ci.getConnectionID());
        }

        if(ci!=null&&ci.isSocketIOConnection()) {
            fileName = fileName+"_socketIO";
        }
        else if(ci!=null&&ci.isSocketIOConnection()) {
            fileName = fileName+"_http";
        }
        HYLogger.changeAllLoggerFile(path, fileName);
    }


    public static void changeAllLoggerFile(String path, String filename) {
        try {
            if(!IniUtil.getInstance().isAllInOneLogFile()) {
                HYLogger.changeDebugLoggerFile(path, filename);
                HYLogger.changeErrorLoggerFile(path, filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     */
    public static synchronized void changeDebugLoggerFile(String dir, String filename) {
            DailyRollingFileAppender d = (DailyRollingFileAppender)Logger.getRootLogger().getAppender("D");
            if (d == null) {
                return;
            }
            String logFilePath;
            if (dir != null) {
                logFilePath = String.format(logFileStorageDir+"/%s/%s.debug.log", dir, filename);
            }
            else {
                logFilePath = String.format(logFileStorageDir+"/%s.debug.log", filename);
            }
            CreateFileUtil.createFile(logFilePath);
        try {
            d.setFile(logFilePath);
            d.activateOptions();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("changeDebugLoggerFile %s", logFilePath));
        }
    }

    /*
    */
    public static synchronized void changeErrorLoggerFile(String dir, String filename) {
        DailyRollingFileAppender e = (DailyRollingFileAppender)Logger.getRootLogger().getAppender("E");
        if (e == null) {
            return;
        }
        String logFilePath;
        if (dir != null) {
            logFilePath = String.format(logFileStorageDir+"/%s/%s.error.log", dir, filename);
        }
        else {
            logFilePath = String.format(logFileStorageDir+"/%s.error.log", filename);
        }
        boolean fileCreated = CreateFileUtil.createFile(logFilePath);

        if(fileCreated) {
            e.setFile(logFilePath);
            e.activateOptions();
        }
    }

    /*
 */
    public static synchronized void resetDebugLoggerFile() {
        String filename = "log";
        DailyRollingFileAppender d = (DailyRollingFileAppender) Logger.getRootLogger().getAppender("D");
        if (d == null) {
            return;
        }
        d.setFile(logFileStorageDir+"/" + filename + ".log");//
        d.activateOptions();
    }

    /*
    */
    public static synchronized void resetErrorLoggerFile() {
        String filename = "error";
        DailyRollingFileAppender e = (DailyRollingFileAppender)Logger.getRootLogger().getAppender("E");
        if (e == null) {
            return;
        }
        e.setFile(logFileStorageDir+"/" + filename+".log");//
        e.activateOptions();
    }

    public static void resetAllLoggerFile() {
        HYLogger.resetDebugLoggerFile();
        HYLogger.resetErrorLoggerFile();
    }

}
