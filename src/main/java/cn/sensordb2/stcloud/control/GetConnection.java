package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class GetConnection extends RequestHandler {
    private static HYLogger logger = HYLogger.getLogger(GetConnection.class);
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();
//        JsonObject jsonObject = request.getParams();
        JsonObject query = new JsonObject();
        JsonArray recordList = new JsonArray();
        mongoClient.find("droneuser", query, queryResult -> {
            if (queryResult.failed()) {
                logger.error("服务器内部异常", connectionInfo);
                //ResponseHandlerHelper.success(connectionInfo, request, result);
                return;
            }
            for(JsonObject json : queryResult.result()){
//                String droneconnection = json.getString("user");
//                if (ClientManager.getInstance().getLocalConnectionInfo(droneconnection) != null) {
//                    if (ClientManager.getInstance().getLocalConnectionInfo(droneconnection).getFrom() == null) {
//                        json.put("bindStatus",0);
//                        recordList.add(json);
//                    } else {
//                        if (ClientManager.getInstance().getLocalConnectionInfo(droneconnection).getFrom().equals(connectionInfo.getUserID())) {
//                            json.put("bindStatus",1);
//                            recordList.add(json);
//                        }
//                    }
//                }
                if (connectionInfo.getTo().equals(json.getString("user"))) {
                    json.put("bindStatus", 1);
                    recordList.add(json);
                } else {
                    json.put("bindStatus", 0);
                    recordList.add(json);
                }
            }
//            {
//                "version": 1,
//                    "result": {
//                "code": "#",
//                        "message":"#";
//                  “uavs”:{“#”，”#”}
//            },
//                "id": "#"
//            }
            request.setResponseSuccess(true);
            JsonObject result = new JsonObject().put("uavs", recordList);
            result.put("code",1);
            result.put("message","getConnection success");
            ResponseHandlerHelper.success(connectionInfo, request, result);
            return;
        });
    }
}
