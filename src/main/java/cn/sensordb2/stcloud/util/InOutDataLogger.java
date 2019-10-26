package cn.sensordb2.stcloud.util;

import cn.sensordb2.stcloud.core.Database;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Created by sensordb on 16/3/16.
 */
/*
 *
 */
public class InOutDataLogger {
    public static Logger logger = Logger.getLogger(InOutDataLogger.class);
    public static String inOutData = "inOutData";
    public static String INPUT_TYPE = "I";
    public static String OUTPUT_TYPE = "O";


    public static void loggerInput(int connectionID, Buffer data, String prettyData) {
        if(!IniUtil.getInstance().isLogInOutDataToDatabase())return;
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject object = new JsonObject().put("connectionID", connectionID).put("data", data.toString());
        object.put("prettyData", prettyData);
        object.put("date", Tools.dateToStr(new Date())).put("type",INPUT_TYPE);
        mongoClient.insert(InOutDataLogger.inOutData, object, res -> {
            logger.info("InOutDataLogger loggerInput to database result:" + res.succeeded());
            logger.info("InOutDataLogger loggerInput:" + prettyData);
        });
    }


    public static void loggerInput(int connectionID, String prettyData) {
        if(!IniUtil.getInstance().isLogInOutDataToDatabase())return;
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject object = new JsonObject().put("connectionID", connectionID);
        object.put("prettyData", prettyData);
        object.put("date", Tools.dateToStr(new Date())).put("type",INPUT_TYPE);
        mongoClient.insert(InOutDataLogger.inOutData, object, res -> {
            logger.info("InOutDataLogger loggerInput to database result:" + res.succeeded());
            logger.info("InOutDataLogger loggerInput:" + prettyData);
        });
    }


    public static void loggerOutput(int connectionID, Buffer data, String prettyData) {
        if(!IniUtil.getInstance().isLogInOutDataToDatabase())return;
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject object = new JsonObject().put("connectionID", connectionID).put("data", data.toString());
        object.put("prettyData", prettyData);
        object.put("date", Tools.dateToStr(new Date())).put("type",OUTPUT_TYPE);
        mongoClient.insert(InOutDataLogger.inOutData, object, res -> {
            logger.info("InOutDataLogger loggerInput to database result:" + res.succeeded());
            logger.info("InOutDataLogger loggerInput:" + prettyData);
        });
    }


    public static void loggerOutput(int connectionID, String prettyData) {
        if(!IniUtil.getInstance().isLogInOutDataToDatabase())return;
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject object = new JsonObject().put("connectionID", connectionID);
        object.put("prettyData", prettyData);
        object.put("date", Tools.dateToStr(new Date())).put("type",OUTPUT_TYPE);
        mongoClient.insert(InOutDataLogger.inOutData, object, res -> {
            logger.info("InOutDataLogger loggerOutput to database result:" + res.succeeded());
            logger.info("InOutDataLogger loggerOutput:" + prettyData);
        });
    }
}
