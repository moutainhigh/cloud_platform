package cn.sensordb2.stcloud.server.handler.common;


import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.Account;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.user.Login;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.IniUtil;
import io.vertx.core.json.JsonObject;

/*
 * 
{
    "zl_cloud": "1.0",
    "method": "common.ping",
    "params": {
        "userName": 1#,
        "hashedPassword": 2#,
    },
    "id": 5#
}
 */
public class Ping extends RequestHandler {
	private static HYLogger logger = HYLogger.getLogger(Ping.class);

	protected boolean isValidRequest(Request request) {
		JsonObject params = request.getParams();

		if(params.containsKey("userName")&&params.containsKey("hashedPassword")) return true;
		else return false;
	}

	@Override
	public void handle(ConnectionInfo connectionInfo, Request request) {
		if(connectionInfo.isTemp()||connectionInfo.isLogged()) {
			ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.INVALID_STATUS);
			return;
		}

		if(IniUtil.getInstance().isCheckRequestParam()&&!this.isValidRequest(request)) {
			ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.INVALID_PARAMETERS);
			return;
		}

		JsonObject params = request.getParams();
		String userName = params.getString("userName");
		String hashedPassword = params.getString("hashedPassword");
		Account.ping(connectionInfo, request, userName, hashedPassword);
	}

}
