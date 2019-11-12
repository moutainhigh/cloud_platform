package cn.sensordb2.stcloud.user;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.data.uploadData;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.server.message.SuccessResponse;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

public class Login extends RequestHandler {
    private static HYLogger logger = HYLogger.getLogger(Login.class);

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject jsonObject = request.getParams();
        JsonObject query = new JsonObject().put("username",jsonObject.getValue("username"));
        mongoClient.find("user", query, queryResult -> {
            if (queryResult.failed()) {
                logger.error("服务器内部异常",connectionInfo);
                return;
            }
            if (((List) queryResult.result()).size() == 0) {
                logger.error("不存在这个用户名",connectionInfo);
                ResponseHandlerHelper.error(connectionInfo,request,-103, "用户名不正确");
                return;
            } else {
                JsonObject userRes = queryResult.result().get(0);
                if (userRes.getValue("hashedPassword").toString()
                        .equals(jsonObject.getValue("hashedPassword").toString())) {
                    connectionInfo.setUserID(request.getParams().getString("username"));
                    request.setResponseSuccess(true);
                    JsonObject result = new JsonObject();
                    result.put("code",1);
                    result.put("message","login success");
                    ResponseHandlerHelper.success(connectionInfo, request,result);
                    return;
                } else {
                    logger.info(String.format("query:%s wrong password", query));
                    return;
                }
            }
        });
    }
}
