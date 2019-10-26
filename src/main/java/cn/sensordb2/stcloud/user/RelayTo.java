package cn.sensordb2.stcloud.user;

import cn.sensordb2.stcloud.data.uploadData;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.Logger;

public class RelayTo extends RequestHandler {
    public static Logger logger = Logger.getLogger(RelayTo.class);
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        PushMessageUtil.pushMessageToSocketio(connectionInfo,request,request.getParams(),connectionInfo.getFrom(), "RELAY_MSG", res->{
            JsonObject result = new JsonObject();
            result.put("code",1);
            result.put("message","relay success");
            RelayTo.logger.info("push to"+connectionInfo.getFrom()+"success!");
            ResponseHandlerHelper.success(connectionInfo, request,result);
            return;
        });
    }
}
