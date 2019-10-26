package cn.sensordb2.stcloud.util;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;

public class HttpServerResponseUtil {
	private static Logger logger = Logger.getLogger(HttpServerResponseUtil.class);

	public static void serverError(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.setStatusCode(HttpStatusCode.getServerErrorCode());
		response.putHeader("Content-Type", HttpHeaderContentType.TEXT_PLAIN);
		response.end();		
		logger.info(new LogInfoResponse(routingContext, LogInfoResponse.ServerError, null, null, null));
	}
	
	/*
	 * ����	403   �������ܾ����� 

	 */
	public static void error403(RoutingContext routingContext, int errorCode) {
		HttpServerResponse response = routingContext.response();
		response.putHeader("Content-Type", HttpHeaderContentType.TEXT_PLAIN);
		response.setStatusCode(HttpStatusCode.Status403);
		String body = ""+errorCode;
		response.end(body);
		logger.info(new LogInfoResponse(routingContext, LogInfoResponse.ERRORResponse403, null, null, body));
	}
	
	public static void error403(RoutingContext routingContext, int errorCode, Exception e) {
		e.printStackTrace();
		HttpServerResponseUtil.error403(routingContext, errorCode);
	}

	
	/*
	 * ����	403   �������ܾ����� 

	 */
	public static void error403(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.putHeader("Content-Type", HttpHeaderContentType.TEXT_PLAIN);
		response.setStatusCode(HttpStatusCode.Status403);
		response.end();
		logger.info(new LogInfoResponse(routingContext, LogInfoResponse.ERRORResponse403, null, null, null));
	}
	
	/*
	 * ����	200   �������ɹ�������ҳ 

	 */
	public static void success200(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.setStatusCode(HttpStatusCode.Status200);
		response.end();
		logger.info(new LogInfoResponse(routingContext, LogInfoResponse.Response200, null, null, null));
	}
	
	/*
	 */
	public static void success200(RoutingContext routingContext, String body) {
		HttpServerResponse response = routingContext.response();
		response.setStatusCode(HttpStatusCode.Status200);
		response.end(body);

		if(response.closed()) {
			logger.error(String.format("send success200 %s while response.closed()", body));
		}

		logger.info(new LogInfoResponse(routingContext, LogInfoResponse.Response200, null, null, body));
		logger.info(String.format("success200 %s", body));
	}

	
	/*
	 * ����	201   ��Դ�ɹ�����

	 */
	public static void success201(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.setStatusCode(HttpStatusCode.Status201);
		response.end();
		logger.info(new LogInfoResponse(routingContext, LogInfoResponse.Response201, null, null, null));
	}

	/*
	 * 	//400   ���������� �����������������﷨�� 
	 */
	public static void error400(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.setStatusCode(HttpStatusCode.Status400);
		response.end();
		logger.info(new LogInfoResponse(routingContext, LogInfoResponse.ERRORResponse400, null, null, null));
	}

	/*
	 * 	//404   ��δ�ҵ��� �������Ҳ����������ҳ�� 
	 */
	public static void error404(RoutingContext routingContext) {
		HttpServerResponse response = routingContext.response();
		response.setStatusCode(HttpStatusCode.Status404);
		response.end();
		logger.info(new LogInfoResponse(routingContext, LogInfoResponse.ERRORResponse404, null, null, null));
	}
	
	/*
	 * ����	200   �������ɹ������ļ�
	 * �����ļ���body����
	 */
	public static void success200File(RoutingContext routingContext, String fileName, long size, String type) {
		HttpServerResponse response = routingContext.response();
		response.setStatusCode(HttpStatusCode.Status200);
		response.putHeader("Content-Type", type);
		response.putHeader("Content-Length", ""+size);
		response.sendFile(fileName);
		logger.info(new LogInfo(routingContext, LogInfoResponse.Response200, null, null, "File:"+fileName));
	}


}
