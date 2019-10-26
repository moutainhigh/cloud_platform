package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Date;

/**
 * Created by sensordb on 16/11/17.
 */
public class RequesResponseLoggerTool {
    private static HYLogger logger = HYLogger.getLogger(RequesResponseLoggerTool.class);
    private static final String requestResponseLogTable = "requestResponseLog";
    private static final String testRequestResponseLogTable = "testRequestResponseLog";

    public static void logNewRequest(ConnectionInfo connectionInfo, String req, Request request) {
        if(!IniUtil.getInstance().islogRequesResponse()) return;

        String logTableName = requestResponseLogTable;
        if(request!=null&&request.getMethod().startsWith("Ping")) {
            logTableName = testRequestResponseLogTable;
        }

        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject requestResponse = new JsonObject();

        if(connectionInfo!=null&&connectionInfo.getUserID()!=null) {
            requestResponse.put("userID", connectionInfo.getUserID());
        }

        if(connectionInfo!=null) {
            requestResponse.put("connection", connectionInfo.toJsonObject());
        }

        requestResponse.put("requestString", req);
        requestResponse.put("dateTime", Tools.dateToStrMs(new Date()));

        if(request!=null)
            requestResponse.put("request", request);
        else {
            Object n = null;
            requestResponse.put("request", n);
        }

        mongoClient.insert(logTableName, requestResponse, res-> {
            if (res.failed()) {
                logger.exception(res.cause(), connectionInfo);
            }
        });
    }

    public static void logRequestResponse(ConnectionInfo connectionInfo, Request request, Object response) {
        if(!IniUtil.getInstance().islogRequesResponse()) return;

        String logTableName = requestResponseLogTable;
        if(request!=null&&request.getMethod().startsWith("Ping")) {
            logTableName = testRequestResponseLogTable;
        }

        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject requestResponse = new JsonObject();

        if(connectionInfo!=null&&connectionInfo.getUserID()!=null) {
            requestResponse.put("userID", connectionInfo.getUserID());
        }

        if(connectionInfo!=null) {
            requestResponse.put("connection", connectionInfo.toJsonObject());
        }

        requestResponse.put("requestString", request.toString());
        requestResponse.put("dateTime", Tools.dateToStrMs(new Date()));

        if(request!=null)
            requestResponse.put("request", request);
        else {
            Object n = null;
            requestResponse.put("request", n);
        }

        if(response!=null) {
            requestResponse.put("response", response.toString());
        }
        else {
            Object n = null;
            requestResponse.put("response", n);
        }

        mongoClient.insert(logTableName, requestResponse, res-> {
            if (res.failed()) {
                logger.exception(res.cause(), connectionInfo);
            }
        });
    }

    public static void logRequestResponse(ConnectionInfo from, ConnectionInfo to, Request request, Object response) {
        if(!IniUtil.getInstance().islogRequesResponse()) return;

        String logTableName = requestResponseLogTable;
        if(request!=null&&request.getMethod().startsWith("Ping")) {
            logTableName = testRequestResponseLogTable;
        }

        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject requestResponse = new JsonObject();

        if(to!=null&&to.getUserID()!=null) {
            requestResponse.put("userID", to.getUserID());
        }

        if(to!=null) {
            requestResponse.put("connection", to.toJsonObject());
        }

        if(from!=null) {
            requestResponse.put("fromConnection", from.toJsonObject());
        }

        requestResponse.put("requestString", request.toString());
        requestResponse.put("dateTime", Tools.dateToStrMs(new Date()));

        if(request!=null)
            requestResponse.put("request", request);
        else {
            Object n = null;
            requestResponse.put("request", n);
        }

        if(response!=null) {
            requestResponse.put("response", response.toString());
        }
        else {
            Object n = null;
            requestResponse.put("response", n);
        }

        mongoClient.insert(logTableName, requestResponse, res-> {
            if (res.failed()) {
                logger.exception(res.cause(), to);
            }
        });
    }

    public static void logRequestResponse(ConnectionInfo connectionInfo, String request, Object response) {
        if(!IniUtil.getInstance().islogRequesResponse()) return;

        String logTableName = requestResponseLogTable;

        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject requestResponse = new JsonObject();

        if(connectionInfo!=null&&connectionInfo.getUserID()!=null) {
            requestResponse.put("userID", connectionInfo.getUserID());
        }

        if(connectionInfo!=null) {
            requestResponse.put("connection", connectionInfo.toJsonObject());
        }

        requestResponse.put("requestString", request);
        requestResponse.put("dateTime", Tools.dateToStrMs(new Date()));


        Object requestNull = null;
        requestResponse.put("request", requestNull);

        if(response!=null)
            requestResponse.put("response", response.toString());
        else {
            Object n = null;
            requestResponse.put("response", n);
        }

        mongoClient.insert(logTableName, requestResponse, res-> {
            if (res.failed()) {
                logger.exception(res.cause(), connectionInfo);
            }
        });
    }
}
