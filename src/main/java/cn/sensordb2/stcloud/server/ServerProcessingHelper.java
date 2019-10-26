package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.message.ErrorResponse;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.server.message.SuccessResponse;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.json.JsonObject;

/*
 *
 */
public class ServerProcessingHelper {
	private static HYLogger logger = HYLogger.getLogger(ServerProcessingHelper.class);
	
	public static void error(ConnectionInfo connectionInfo,
                             Request request, int code) {
		int id = -1;
		if(request!=null) id = request.getID();
		ErrorResponse errorResponse = new ErrorResponse(id, code);
		
		String requestString = request!=null?request.toString():null;
		logger.info(new ServerInternalRequestResponseSession(connectionInfo, requestString, errorResponse.toJsonObject()));
		return;
	}
	
	public static void serverError(ConnectionInfo connectionInfo,
			Request request) {	
		ServerProcessingHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);;
	}
	
	public static void success(ConnectionInfo connectionInfo,
			Request request) {
		ServerProcessingHelper.success(connectionInfo, request, null);;
		return;		
	}
	
	public static void success(ConnectionInfo connectionInfo,
			Request request, JsonObject result) {
		int id = -1;
		if(request!=null) id = request.getID();
		SuccessResponse successResponse = new SuccessResponse(id, result,request.getMethod());
		String requestString = request!=null?request.toString():null;
		logger.info(new ServerInternalRequestResponseSession(connectionInfo, requestString, successResponse.toJsonObject()));
		return;		
	}
}
