package cn.sensordb2.stcloud.util;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.common.AccountType;
import cn.sensordb2.stcloud.server.message.Request;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sensordb on 16/7/21.
 */
public class OpsUtil {
    private static HYLogger logger = HYLogger.getLogger(OpsUtil.class);
    private static OpsUtil instance;
    private final static String OPS_TABLE_NAME = "callMethodTime";
    private final static String USER_DEV_RELATION_LOG = "userDevRelationLog";
    private final static String USER_CURD_LOG = "accountsCURDLog";

    public final static String OPERATION_INSERT = "INSERT";
    public final static String OPERATION_REMOVE = "REMOVE";
    public final static String OPERATION_UPDATE = "UPDATE";

    public final static String USAGE_REGISTER = "REGISTER";
    public final static String USAGE_GET_LOGIN_VERI_CODE = "GET_LOGIN_VERI_CODE";

    private Set<String> recordMethods = new HashSet<>();

    private OpsUtil() {
        String[] rms = IniUtil.getInstance().getRecordMethods().split(",");
        for(String rm: rms) {
            recordMethods.add(rm);
        }
    }

    public static OpsUtil getInstance() {
        if (instance == null) {
            instance = new OpsUtil();
        }
        return instance;
    }

    public void recordOperationTime(ConnectionInfo connectionInfo, Request request, JsonObject result) {
        try {
            if (!IniUtil.getInstance().isRecordCallMethodSpendTime()) return;

            if (connectionInfo == null || request == null) {
                logger.error("connectionInfo == null || request == null");
                return;
            }

            if (this.recordMethods.contains(request.getMethod())) {
                request.setProcessEndTimeCurrentTime();
                recordOperationTimeReal(connectionInfo, request, result);
            }
        } catch (Exception e) {
            logger.error(Tools.getRequestException(request, e), connectionInfo);
        }
    }

    public void recordOperationTimeReal(ConnectionInfo connectionInfo, Request request, JsonObject result) {

        MongoClient mongoClient = Database.getInstance().getMongoClient();

        JsonObject record = new JsonObject();

        if (connectionInfo.getUserID() != null) {
            record.put("userID", connectionInfo.getUserID());
        } else {
            record.put("userID", "connection" + connectionInfo.getIndex());
        }
        record.put("method", request.getMethod());
        record.put("request", request);
        record.put("dateTime", Tools.dateToStr(new Date()));
        record.put("spendTime", new Long(request.getProcessSpendTime()));
        mongoClient.save(OPS_TABLE_NAME, record, saveResult -> {
            if (saveResult.failed()) {
                logger.error(Tools.getTrace(saveResult.cause()));
            }
        });
    }

    public void recordAppUserDevRelationLog(ConnectionInfo connectionInfo, Request request,
                                            JsonObject jsonObject, String operation, String devID, String source) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        JsonObject record = new JsonObject();

        if(connectionInfo==null||request==null||jsonObject==null) {
            logger.error("recordAppUserDevRelationLog connectionInfo==null||request==null||devID==null", connectionInfo);
            return;
        }
        if (connectionInfo.getUserID() != null) {
            record.put("userID", connectionInfo.getUserID());
        } else {
            logger.error("recordAppUserDevRelationLog userID==null", connectionInfo);
            return;
        }

        record.put("request", request);
        record.put("dateTime", Tools.dateToStr(new Date()));
        record.put("operation", operation);
        record.put("data", jsonObject);
        if(devID!=null) record.put("devID", devID);
        if(source!=null) record.put("source", source);

        mongoClient.save(USER_DEV_RELATION_LOG, record, saveResult -> {
            if (saveResult.failed()) {
                logger.error(Tools.getTrace(request.toString(), record.toString(), saveResult.cause()));
            }
        });
    }

    public void recordAccountsLog(String usage, ConnectionInfo connectionInfo, Request request,
                                            JsonObject jsonObject, String operation,
                                  String source,
                                  String accountID, int type, String openPassword,
                                  JsonObject admin,
                                  String adminPass) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        JsonObject record = new JsonObject();

        if(connectionInfo==null||request==null||jsonObject==null) {
            logger.error("recordAccountsLog connectionInfo==null||request==null||devID==null", connectionInfo);
            return;
        }
        if (connectionInfo.getUserID() != null) {
            record.put("creator", connectionInfo.getUserID());
        } else {
            logger.error("recordAccountsLog userID==null", connectionInfo);
            return;
        }

        record.put("request", request);
        record.put("dateTime", Tools.dateToStr(new Date()));
        record.put("operation", operation);
        record.put("data", jsonObject);


        record.put("userName", accountID).put("type", AccountType.getAccountTypeString(type));

        if (openPassword != null) {
            record.put("openPassword", openPassword);
        }

        if (source != null) {
            record.put("source", source);
        }

        if (admin != null) {
            record.put("admin", admin);
        }

        if (adminPass != null) {
            record.put("adminPass", adminPass);
        }

        if (usage != null) {
            record.put("usage", usage);
        }

        mongoClient.save(USER_CURD_LOG, record, saveResult -> {
            if (saveResult.failed()) {
                logger.error(Tools.getTrace(request.toString(), record.toString(), saveResult.cause()));
            }
        });
    }

    public void recordAccountsLog(String usage, ConnectionInfo connectionInfo, Request request,
                                            JsonObject jsonObject, String operation,
                                  String source,
                                  String accountID, int type) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        JsonObject record = new JsonObject();

        if(connectionInfo==null||request==null||jsonObject==null) {
            logger.error("recordAccountsLog connectionInfo==null||request==null||devID==null", connectionInfo);
            return;
        }
        if (connectionInfo.getUserID() != null) {
            record.put("creator", connectionInfo.getUserID());
        } else {
            logger.error(String.format("recordAccountsLog userID==null usage:%s request:%s", usage,
                    request.toString()), connectionInfo);
        }

        record.put("request", request);
        record.put("dateTime", Tools.dateToStr(new Date()));
        record.put("operation", operation);
        record.put("data", jsonObject);


        record.put("userName", accountID).put("type", AccountType.getAccountTypeString(type));

        if (source != null) {
            record.put("source", source);
        }

        if (usage != null) {
            record.put("usage", usage);
        }

        mongoClient.save(USER_CURD_LOG, record, saveResult -> {
            if (saveResult.failed()) {
                logger.error(Tools.getTrace(request.toString(), record.toString(), saveResult.cause()));
            }
        });
    }

}
