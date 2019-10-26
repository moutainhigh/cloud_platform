package cn.sensordb2.stcloud.data;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.user.Login;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class GetData extends RequestHandler {
    private static HYLogger logger = HYLogger.getLogger(GetData.class);
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        final MongoClient mongoClient = Server.getInstance().getMongoClient();

        final String startTime = request.getParams().getString("startTime");
        final String endTime = (request.getParams().getString("endTime") == null)?
                Tools.dateToString(new Date()):request.getParams().getString("endTime");
        JsonArray jsonArray = new JsonArray();

        JsonObject query = new JsonObject();
        query.put("time",new JsonObject().put("$gte",startTime).put("$lte",endTime));
        logger.info(query);
        mongoClient.find("data",query,queryResult -> {
           if(queryResult.failed()){
               logger.error(String.format("query:%s exception:%s", query, Tools.getTrace(queryResult.cause())));
               return;
           }else{
               for(int i=0;i<queryResult.result().size();i++){
                   jsonArray.add(queryResult.result().get(i));
               }
           }
           if (jsonArray.size() == 0) {
               JsonObject result = new JsonObject();
               result.put("code", ResponseErrorCode.NOT_EXISTED);
               result.put("message", "no log");
               ResponseHandlerHelper.success(connectionInfo, request,result);
               logger.info(String.format("starttime:%s to endtime:%s has no datalog in database"
                       ,startTime,endTime));
           } else {
               final JsonObject result = new JsonObject().put("recordList", jsonArray);
               ResponseHandlerHelper.success(connectionInfo, request,result);
               logger.info(String.format("return data successed "));
           }
        });
    }
}
