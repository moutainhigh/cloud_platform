package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;

import java.util.Date;

public class UploadHandlerHelper {
    private static HYLogger logger = HYLogger.getLogger(UploadHandlerHelper.class);
    public static final String appExceptionReportTable = "appExceptionReport";
    public static final String lockOpenRecord = "lockOpenRecord";
    public static final String lockOpenRecordHistory = "lockOpenRecordHistory";
    public static final String diskKeyLogs = "diskKeyLogs";
    public static final String asyncLockKey = "asyncLockKey";
    public static final String asyncLockKeyOpResult = "asyncLockKeyOpResult";
    public static final String asyncLockKeyLogs = "asyncLockKeyLogs";
    //dist key info for list
    public static final String diskKeyInfo = "diskKeyInfo";
    public static final String lockWarning = "lockWarning";
    public static final String lockUploadException = "lockUploadException";
    public static final String userReportTable = "userReport";

    public static final String lockStatus = "lockStatus";
    public static final String lockStatusHistory = "lockStatusHistory";

    /*
{
    "zl_cloud": "1.0",
    "method": "UPLOAD_USER_REPORT",
    "params": {
        "report_text": 1#,
        "report_pics": [
            {
                "pic_type": 2#,
                "base64_pic": 3#
            }
        ]
    },
    "id": 4#
}
     */
    public static void uploadUserReport(ConnectionInfo connectionInfo, Request request) {
        String userName = connectionInfo.getUserID();
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        JsonObject report = request.getParams().copy().put("userName", userName);
        report.put("dateTime", Tools.dateToStr(new Date()));

        mongoClient.insert(userReportTable, report, res -> {
            if (!res.succeeded()) {
                logger.error(Tools.getRequestException(request, res.cause()), connectionInfo);
                ResponseHandlerHelper.serverError(connectionInfo, request);
                return;
            }

            ResponseHandlerHelper.success(connectionInfo, request);
            return;
        });

    }

    /*
{
    "zl_cloud": "1.0",
    "method": "UPLOAD_APP_EXCEPTION_REPORT",
    "params": {
        "phone_type": 1#,
        "phone_version": 2#,
        "app_version": 3#,
        "exception": 4#
    },
    "id": 5#
    
    table appExceptionReport:
    userName,
    phoneType,
    phoneVersion,
    appVersion,
    exception,
    devID,
}
     */
    public static void uploadAppExceptionReport(ConnectionInfo connectionInfo, Request request,
                                                String phoneType,
                                                String phoneVersion,
                                                String appVersion,
                                                Object exception,
                                                Object devID) {
        String userName = connectionInfo.getUserID();
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        // 
        JsonObject report = new JsonObject().put("userName", userName).put("phoneType",
                phoneType).put("phoneVersion", phoneVersion).put("appVersion", appVersion).put("exception", exception);
        report.put("dateTime", Tools.dateToStrMs(new Date()));
        report.put("devID", devID);
        mongoClient.insert(appExceptionReportTable, report, res -> {
            if (!res.succeeded()) {
                logger.error(Tools.getRequestException(request, res.cause()), connectionInfo);
                ResponseHandlerHelper.serverError(connectionInfo, request);
                return;
            }

            ResponseHandlerHelper.success(connectionInfo, request);
            return;
        });

    }

    /*
    {
    "zl_cloud": "1.0",
    "method": "LOCK_UPLOAD_OPEN_RECORDS",
"params": {
        "lockID": #,
"keyID": #
"userID": #
"dateTimeBegin": #
"dateTimeEnd": #
"isSuccess": #
"openType": #
         },
    "id": #
}
     */
    public static void uploadOpenLockRecord(ConnectionInfo connectionInfo, Request request,
                                            String lockID,
                                            String keyID,
                                            String userID,
                                            String nickName,
                                            String openTimeBegin,
                                            String openTimeEnd,
                                            double spendTime,
                                            int isSuccess, int openType, Object ios,
                                            Object android,
                                            Object debug,
                                            int isRemote) {
        String userName = connectionInfo.getUserID();
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        // �����û����Ƿ����
        JsonObject report = new JsonObject().put("lockID", lockID).put("keyID",
                keyID).put("userID", userID).put("nickName", nickName).put("openTimeBegin",
                openTimeBegin).put("openTimeEnd", openTimeEnd).put("spendTime", new Double(spendTime)).put("isRemote", new Integer(isRemote)).put("isSuccess", isSuccess).put("openType", openType);
        if(ios!=null) report.put("ios", ios);
        if(android!=null) report.put("android",android);
        if(debug!=null) report.put("debug", debug);
        mongoClient.insert(lockOpenRecord, report, res -> {
            if (!res.succeeded()) {
                logger.error(Tools.getRequestException(request, res.cause()), connectionInfo);
                ResponseHandlerHelper.serverError(connectionInfo, request);
                return;
            }

            ResponseHandlerHelper.success(connectionInfo, request);
            return;
        });

    }

    public static void uploadOpenLockRecordHistory(ConnectionInfo connectionInfo, Request request,
                                            String lockID,
                                            String keyID,
                                            String userID,
                                            String nickName,
                                            String openTimeBegin,
                                            String openTimeEnd,
                                            double spendTime,
                                            int isSuccess, int openType,Object ios,
                                                   Object android,
                                                   Object debug,
                                            int isRemote) {
        String userName = connectionInfo.getUserID();
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        // �����û����Ƿ����
        JsonObject report = new JsonObject().put("lockID", lockID).put("keyID",
                keyID).put("userID", userID).put("nickName", nickName).put("openTimeBegin",
                openTimeBegin).put("openTimeEnd", openTimeEnd).put("spendTime", new Double(spendTime)).put("isRemote", new Integer(isRemote)).put("isSuccess", isSuccess).put("openType", openType);
        if(ios!=null) report.put("ios", ios);
        if(android!=null) report.put("android",android);
        if(debug!=null) report.put("debug", debug);
        mongoClient.insert(lockOpenRecordHistory, report, res -> {
            if (!res.succeeded()) {
                logger.error(String.format("uploadOpenLockRecordHistory %s", Tools.getTrace(res.cause())));
                return;
            }
            return;
        });
    }


    /*
    {
    "zl_cloud": "1.0",
    "method": "LOCK_UPLOAD_WARNING",
    "params": {
        "lockID": #,
        "type": #,
        "desc": #,
    },
    "id": #
}
     */
    public static void uploadLockWarning(ConnectionInfo connectionInfo, Request request, String lockID, String type, String desc) {
        String userName = connectionInfo.getUserID();
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        // �����û����Ƿ����
        JsonObject report = new JsonObject().put("lockID", lockID).put("type",
                type).put("desc", desc).put("dateTime", Tools.dateToStr(new Date()));
        mongoClient.insert(lockWarning, report, res -> {
            if (!res.succeeded()) {
                logger.error(Tools.getRequestException(request, res.cause()), connectionInfo);
                ResponseHandlerHelper.serverError(connectionInfo, request);
                return;
            }

            ResponseHandlerHelper.success(connectionInfo, request);
            return;
        });
    }

    public static void uploadLockInfo(ConnectionInfo connectionInfo, Request request,
                                      String lockID,
                                      int battery) {
        String userName = connectionInfo.getUserID();
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        //
        JsonObject report = new JsonObject().put("userName", userName)
                .put("dateTime", Tools.dateToStr(new Date())).put("battery", new Integer(battery));
        JsonObject update = new JsonObject().put("$set", report);

        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.setUpsert(true);

        JsonObject query = new JsonObject().put("lockID", lockID);
        mongoClient.updateCollectionWithOptions(lockStatus, query, update, updateOptions, res -> {
            if (res.failed()) {
                logger.error(Tools.getRequestException(request, res.cause()), connectionInfo);
                ResponseHandlerHelper.serverError(connectionInfo, request);
                return;
            }

            ResponseHandlerHelper.success(connectionInfo, request);

            report.put("lockID", lockID);
            mongoClient.insert(lockStatusHistory, report, lockStatusHistoryRes -> {
                if (lockStatusHistoryRes.failed()) {
                    logger.error(String.format("save %s error", report.toString()), connectionInfo);
                    return;
                }
            });

            return;
        });
    }

    /*
    {
    "zl_cloud": "1.0",
    "method": "LOCK_UPLOAD_EXCEPTION",
    "params": {
        "lockID": #,
        "keyID": #,
        "type": #
        "desc": #
    },
    "id": #
}
     */
    public static void uploadLockException(ConnectionInfo connectionInfo, Request request, String lockID,
                                           String keyID, String type, Object desc) {
        String userName = connectionInfo.getUserID();
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        JsonObject report = new JsonObject().put("lockID", lockID).put("keyID",keyID).put("type",
                type).put("desc", desc);
        report.put("userName", userName);

        mongoClient.insert(lockUploadException, report, res -> {
            if (!res.succeeded()) {
                logger.error(Tools.getRequestException(request, res.cause()), connectionInfo);
                ResponseHandlerHelper.serverError(connectionInfo, request);
                return;
            }

            ResponseHandlerHelper.success(connectionInfo, request);
            return;
        });
    }

}
