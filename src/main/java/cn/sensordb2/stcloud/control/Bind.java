package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.json.JsonObject;

public class Bind extends RequestHandler {
    private static HYLogger logger = HYLogger.getLogger(Bind.class);
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        if(request.getParams().getString("methodName").equals("Bind")){
            ClientManager.getInstance().setBindConnection(connectionInfo.getUserID(),request.getParams().getString("droneName"));
            logger.info("Bind success");
            JsonObject result = new JsonObject();
            result.put("code",1);
            result.put("message","bind success");
            connectionInfo.setTo(request.getParams().getString("droneName"));

            ResponseHandlerHelper.success(connectionInfo, request,result);
        }else if (request.getParams().getString("methodName").equals("UnBind")){
            ClientManager.getInstance().removeBindConnection(connectionInfo.getUserID(),request.getParams().getString("droneName"));
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
