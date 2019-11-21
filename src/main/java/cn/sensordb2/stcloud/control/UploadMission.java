package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import io.vertx.core.json.JsonObject;

public class UploadMission extends RequestHandler {
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askupload success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}
