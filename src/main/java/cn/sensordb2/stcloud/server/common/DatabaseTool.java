package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.core.Database;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by sensordb on 16/12/10.
 */
public class DatabaseTool {
    public static void find(String tableName, JsonObject query,
                             Handler<DatabaseQueryResult> handler) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        mongoClient.find(tableName, query, queryRes-> {
            DatabaseQueryResult databaseQueryResult = new DatabaseQueryResult(queryRes);
            handler.handle(databaseQueryResult);
        });
    }

    public static void save(String tableName, JsonObject record,
                             Handler<DatabaseSaveResult> handler) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        mongoClient.save(tableName, record, res -> {
            DatabaseSaveResult databaseSaveResult = new DatabaseSaveResult(res);
            handler.handle(databaseSaveResult);
        });
    }

    public static void removeDocuments(String tableName, JsonObject record,
                             Handler<DatabaseRemoveResult> handler) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        mongoClient.removeDocuments(tableName, record, res -> {
            DatabaseRemoveResult databaseRemoveResult = new DatabaseRemoveResult(res);
            handler.handle(databaseRemoveResult);
        });
    }
}
