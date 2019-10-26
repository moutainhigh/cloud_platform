package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import io.vertx.core.json.JsonObject;

public class RecordVideo extends RequestHandler {
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        JsonObject jsonObject = new JsonObject().put("method",request.getMethod()).put("params",request.getParams());
        PushMessageUtil.pushMessage(connectionInfo,request,jsonObject,connectionInfo.getTo(), "RELAY_MSG", res->{
            JsonObject result = new JsonObject();
            result.put("code",1);
            result.put("message","askvideo success");
            ResponseHandlerHelper.success(connectionInfo, request,result);
        });
    }
}
