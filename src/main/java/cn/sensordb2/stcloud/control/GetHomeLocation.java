package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class GetHomeLocation extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        JsonObject jsonObject = new JsonObject().put("method", request.getMethod()).put("from",
                ClientManager.getInstance().getLocalConnectionInfo(connectionInfo.getTo())
                        .getFrom());
//        PushMessageUtil.pushMessage(connectionInfo,request,jsonObject,connectionInfo
//        .getTo(), "RELAY_MSG", res->{
////            JsonObject result = new JsonObject();
////            result.put("code",1);
////            result.put("message","askgetHomeLocation success");
////            ResponseHandlerHelper.success(connectionInfo, request,result);
////        });
        JsonObject result = new JsonObject();
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        mongoClient.find("homeLand", new JsonObject().put("name", "firefly"), res -> {
            for (JsonObject entries : res.result()) {
                result.put("position", new JsonObject()
                        .put("x", entries.getJsonObject("position").getDouble("x"))
                        .put("y", entries.getJsonObject("position").getDouble("y"))
                        .put("z", entries.getJsonObject("position").getDouble("z")));
            }
        });
        result.put("code",1);
        result.put("message","askgetHomeLocation success");
        ResponseHandlerHelper.success(connectionInfo, request,result);
    }
}
