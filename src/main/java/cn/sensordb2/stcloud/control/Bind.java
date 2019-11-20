package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class Bind extends RequestHandler {
    private static HYLogger logger = HYLogger.getLogger(Bind.class);
    //bind 时drone对应的connectioninfo。from set为 user typeConnectionInfoMap 中添加 （username,droneConnectioninfo）这样的记录
    //unbind时drone 的 connectioninfo from 设为null typeConnectionInfoMap移除此条记录
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        if(request.getParams().getString("methodName").equals("Bind")){
            //fix
            //connectionInfo.getUserID() == from    request.getParams().getString("droneName") == to
//            ClientManager.getInstance().setBindConnection(connectionInfo.getUserID(),request.getParams().getString("droneName"));
            connectionInfo.setTo(request.getParams().getString("droneName"));
            logger.info("Bind success");
            JsonObject result = new JsonObject();
            result.put("code",1);
            result.put("message","bind success");
            ResponseHandlerHelper.success(connectionInfo, request,result);
        }else if (request.getParams().getString("methodName").equals("UnBind")){
            //connectionInfo.getUserID() == from    request.getParams().getString("droneName") == to
//            ClientManager.getInstance().removeBindConnection(connectionInfo.getUserID(),request.getParams().getString("droneName"));
            connectionInfo.setTo(null);
            logger.info("unbind success");
            JsonObject result = new JsonObject();
            result.put("code",1);
            result.put("message","unbind success");
            ResponseHandlerHelper.success(connectionInfo, request,result);
        }else{
            logger.info("something wrong");
        }
        return;
    }
}
