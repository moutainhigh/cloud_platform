package cn.sensordb2.stcloud.server.handler.common;

import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.socketIOServer.WebsocketClientManager;
import cn.sensordb2.stcloud.user.Login;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import cn.sensordb2.stcloud.util.Tools;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.common.MessageTranportHandlerHelper;
import io.vertx.core.json.JsonObject;

/*
{
    "zl_cloud": "1.0",
    "method": "common.relayMsg",
    "params": {
        "originalMsg": 1#，
		"to": 2#
		"childID": 3#
    },
    "id": 4#
}


 */
public class RelayMsg extends RequestHandler {

	private static HYLogger logger = HYLogger.getLogger(RelayMsg.class);

	protected boolean isValidRequest(Request request, ConnectionInfo connectionInfo) {
		JsonObject params = request.getParams();

		if(!params.containsKey("msg")||!params.containsKey("to")) return false;
		try {
			params.getJsonObject("msg");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Server.logger.exception(Tools.getRequestException(request, e), connectionInfo);
			return false;
		}
		return true;
	}

	@Override
	public void handle(ConnectionInfo connectionInfo, Request request) {
		JsonObject params = request.getParams();
		if(!connectionInfo.isLogged()) {
			ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.INVALID_STATUS);
			return;
		}

		if(IniUtil.getInstance().isCheckRequestParam()&&!this.isValidRequest(request, connectionInfo)) {
			ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.INVALID_PARAMETERS);
			return;
		}

		MessageTranportHandlerHelper.relayMessage(connectionInfo, request);
//		ClientManager.getInstance().getConnectionInfo(request.getParams().getString("to"), toConnectionInfo-> {
//			if(toConnectionInfo == null) {
//				logger.info("is not a app");
//			}else{
//				JsonObject jsonObject = request.getParams();
//				PushMessageUtil.pushMessage(connectionInfo,request,jsonObject,request.getString("to"),"RELAY_MSG",res->{
//					JsonObject result = new JsonObject();
//					result.put("code",1);
//					result.put("message","relay success");
//					ResponseHandlerHelper.success(connectionInfo, request,result);
//				});
//			}
//
//		});
//		WebsocketClientManager.getInstance().getConnectionInfo(request.getParams().getString("to"),toConnectionInfo-> {
//			if ((toConnectionInfo == null)){
//				logger.info("is not a web");
//			}else{
//				JsonObject jsonObject = request.getParams();
//				PushMessageUtil.pushMessageToSocketio(connectionInfo,request,jsonObject,request.getString("to"),"RELAY_MSG",res->{
//					JsonObject result = new JsonObject();
//					result.put("code",1);
//					result.put("message","relay success");
//					ResponseHandlerHelper.success(connectionInfo, request,result);
//				});
//			}
//		});
	}
}
