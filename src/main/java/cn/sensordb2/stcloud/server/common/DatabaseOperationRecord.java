package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.json.JsonObject;

import java.util.Date;

/**
 * Created by sensordb on 17/3/6.
 */
public class DatabaseOperationRecord {
    public static final String SAVE = "SAVE";
    public static final String UPDATE = "UPDATE";
    public static final String INSERT = "INSERT";
    public static final String REMOVE = "REMOVE";

    public static JsonObject createRecord(String userID, String tableName,
                                          String action, JsonObject param, boolean actionResult, Date date) {
        return createRecord(userID, tableName, action, param, null, actionResult, date);
    }

    public static JsonObject createRecord(String userID, String tableName,
                                          String action, JsonObject param1,
                                          JsonObject param2, boolean actionResult,
                                          Date date) {
        JsonObject record = new JsonObject();
        record.put("userID", userID);
        record.put("tableName", tableName);
        record.put("action", action);
        record.put("param1", param1);
        record.put("param2", param2);
        record.put("actionResult", new Boolean(actionResult));
        record.put("dateTime", Tools.dateToStrMs(date));
        return record;
    }
}
