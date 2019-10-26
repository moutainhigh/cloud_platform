package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.server.common.RequesResponseLoggerTool;
import cn.sensordb2.stcloud.server.gate.BinaryResponseFactory;
import cn.sensordb2.stcloud.server.message.ErrorResponse;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.server.message.SuccessResponse;
import cn.sensordb2.stcloud.util.*;
import cn.sensordb2.stcloud.server.common.DatabaseResult;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

public class ResponseHandlerHelper {
	private static HYLogger logger = HYLogger.getLogger(ResponseHandlerHelper.class);
	public static final String DELIMIT = Globals.DELIMIT;
	private static Vertx vertx = null;

	public static void setVertx(Vertx vertx) {
		ResponseHandlerHelper.vertx = vertx;
	}

	public static void error(ConnectionInfo connectionInfo,
							 Request request, int code, boolean writeError) {
		request.setResponseSuccess(false);
		int id = -1;
		if(request!=null) id = request.getID();
		ErrorResponse errorResponse = new ErrorResponse(id, code);

		if (connectionInfo == null) {
			logger.error("error write to null");
			return;
		}

		if(request.isNeedResponse()) {
			if (connectionInfo.isBinaryConnection()) {
				Buffer buffer = BinaryResponseFactory.errorResponse(request, (byte) code);
				logger.info(String.format("Server write binary :%s", errorResponse.toString()), connectionInfo);

				if (writeError) connectionInfo.write(buffer);
				InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), buffer, Tools.bufferToPrettyByteString(buffer));
				if (IniUtil.getInstance().isLogAllRequestString()) {
					logger.sendDataLogBinary(buffer, connectionInfo);
				}
			} else {
				String result = errorResponse.toStringWithDelimit();
				if (writeError) connectionInfo.write(errorResponse);
				InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), result);
			}

			if (connectionInfo.isWriteQueueFull()) {
				logger.error(String.format("Request:%s write:%s to userID:%s but write queue full", request.toString(),
						errorResponse, connectionInfo.getUserID()), connectionInfo);
			}
		}

		String requestString = request!=null?request.toString():null;
		JsonObject response = errorResponse.toJsonObject();
		request.handle(response);
		OpsUtil.getInstance().recordOperationTime(connectionInfo, request, response);
		logger.info(new RequestResponseSession(connectionInfo, requestString, response, false), connectionInfo);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request, errorResponse);
		return;
	}

	public static void error(ConnectionInfo connectionInfo,
							 Request request, int code, String message) {
		request.setResponseSuccess(false);

		int id = -1;
		if(request!=null) id = request.getID();
		ErrorResponse errorResponse = new ErrorResponse(id, code, message);

		if (connectionInfo == null) {
			logger.error("error write to connection null");
			return;
		}

		if(request.isNeedResponse()) {
			String result = errorResponse.toStringWithDelimit();
			connectionInfo.write(errorResponse);
			InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), result);

			if (connectionInfo.isWriteQueueFull()) {
				logger.error(String.format("Request:%s write:%s to userID:%s but write queue full", request.toString(),
						errorResponse, connectionInfo.getUserID()), connectionInfo);
			}
		}

		String requestString = request!=null?request.toString():null;
		JsonObject response = errorResponse.toJsonObject();
		request.handle(response);
		OpsUtil.getInstance().recordOperationTime(connectionInfo, request, response);
		logger.info(new RequestResponseSession(connectionInfo, requestString, response, false), connectionInfo);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request, errorResponse);
		return;
	}

	public static void error(ConnectionInfo connectionInfo,
			Request request, int code) {
		error(connectionInfo, request, code, true);
	}

	public static void error(ConnectionInfo connectionInfo,
			String request, int code) {
		ErrorResponse errorResponse = new ErrorResponse(-1, code);

		if (connectionInfo.isWriteQueueFull()) {
			logger.error(String.format("Request:%s write:%s to userID:%s but write queue full", request.toString(),
					errorResponse, connectionInfo.getUserID()), connectionInfo);
		}

		if (connectionInfo.isBinaryConnection()) {
			Buffer buffer = BinaryResponseFactory.errorResponseForInvalidFormatRequest((byte) code);
			connectionInfo.write(buffer);

			InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), buffer, Tools.bufferToPrettyByteString(buffer));
			if (IniUtil.getInstance().isLogAllRequestString()) {
				logger.sendDataLogBinary(buffer, connectionInfo);
			}
		} else {
			String result = errorResponse.toStringWithDelimit();
			InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), result);
			connectionInfo.write(errorResponse);
		}

		JsonObject response = errorResponse.toJsonObject();

		logger.error(new RequestResponseSession(connectionInfo, request, response), connectionInfo);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request, errorResponse);

		return;
	}

	public static void serverError(ConnectionInfo connectionInfo,
								   Request request, AsyncResult result) {
		logger.exception(Tools.getRequestException(request, result.cause()), connectionInfo);
		ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request,
				new ErrorResponse(request.getID(), ResponseErrorCode.SERVER_ERROR));
	}

	public static void serverError(ConnectionInfo connectionInfo,
								   Request request, DatabaseResult result) {
		logger.exception(Tools.getRequestException(request, result.cause()), connectionInfo);
		ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request,
				new ErrorResponse(request.getID(), ResponseErrorCode.SERVER_ERROR));
	}

	public static void serverError(ConnectionInfo connectionInfo,
			Request request) {	
		ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request,
				new ErrorResponse(request.getID(), ResponseErrorCode.SERVER_ERROR));
	}

	public static void serverError(ConnectionInfo connectionInfo,
			Request request, String context) {
		ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request,
				new ErrorResponse(request.getID(), ResponseErrorCode.SERVER_ERROR));

	}

	public static void success(ConnectionInfo connectionInfo,
			Request request) {
		request.setResponseSuccess(true);
		int id = -1;
		if(request!=null) id = request.getID();
		SuccessResponse successResponse = new SuccessResponse(id);
		
		if(request.isNeedResponse()) {
			if (connectionInfo.isBinaryConnection()) {
				final Buffer buffer = BinaryResponseFactory.successResponse(request);
				logger.info(String.format("Server write binary :%s", successResponse.toString()), connectionInfo);

				connectionInfo.write(buffer);
				InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), buffer, Tools.bufferToPrettyByteString(buffer));
				if (IniUtil.getInstance().isLogAllRequestString()) {
					logger.sendDataLogBinary(buffer, connectionInfo);
				}
			} else {
				String result = successResponse.toStringWithDelimit();
				connectionInfo.write(successResponse);
				InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), result);
			}

			if (connectionInfo.isWriteQueueFull()) {
				logger.error(String.format("Request:%s write:%s to userID:%s but write queue full", request.toString(),
						successResponse, connectionInfo.getUserID()), connectionInfo);
			}
		}

		JsonObject successResponseJson = successResponse.toJsonObject();
		OpsUtil.getInstance().recordOperationTime(connectionInfo, request, successResponseJson);
		request.handle(successResponseJson);
		RequestResponseSession requestResponseSession = new RequestResponseSession(connectionInfo, request.toString(), successResponseJson);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request, successResponse);
		logger.info(requestResponseSession, connectionInfo);
		return;		
	}

	public static void successForHeartBeat(ConnectionInfo connectionInfo,
							   Request request) {
		request.setResponseSuccess(true);
		int id = -1;
		if(request!=null) id = request.getID();
		SuccessResponse successResponse = new SuccessResponse(id);

		if(request.isNeedResponse()) {

			if (connectionInfo.isBinaryConnection()) {
				final Buffer buffer = BinaryResponseFactory.successResponse(request);
				logger.info(String.format("Server write heartbeat response binary :%s", successResponse.toString()), connectionInfo);
				connectionInfo.write(buffer);
				InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), buffer, Tools.bufferToPrettyByteString(buffer));
				if (IniUtil.getInstance().isLogAllRequestString()) {
					logger.sendDataLogBinary(buffer, connectionInfo);
				}
			} else {
				String result = successResponse.toStringWithDelimit();
				connectionInfo.write(successResponse);
				InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), result);
			}

			if (connectionInfo.isWriteQueueFull()) {
				logger.error(String.format("Request:%s write:%s to userID:%s but write queue full", request.toString(),
						successResponse, connectionInfo.getUserID()), connectionInfo);
			}
		}

		JsonObject response = successResponse.toJsonObject();
		OpsUtil.getInstance().recordOperationTime(connectionInfo, request, response);
		request.handle(response);

		RequestResponseSession requestResponseSession = new RequestResponseSession(connectionInfo, request.toString(), response);
		if(IniUtil.getInstance().isLogHeartBeatSession()) {
			logger.info(requestResponseSession, connectionInfo);
			RequesResponseLoggerTool.logRequestResponse(connectionInfo, request, successResponse);
		}
		return;
	}

	public static void success(ConnectionInfo connectionInfo,
			Request request, JsonObject result) {
		request.setResponseSuccess(true);
		if (connectionInfo.isBinaryConnection()) {
			logger.error(String.format("write binary connection:%s json data null", connectionInfo.toJsonObject().toString()));
		}

		SuccessResponse successResponse = new SuccessResponse(request.getID(), result,request.getMethod());
		if(request.isNeedResponse()) {
			String s = successResponse.toStringWithDelimit();
			connectionInfo.write(successResponse);

			if (connectionInfo.isWriteQueueFull()) {
				logger.error(String.format("Request:%s write:%s to userID:%s but write queue full", request.toString(),
						result, connectionInfo.getUserID()), connectionInfo);
			}

			InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), s);
		}

		JsonObject response = successResponse.toJsonObject();
		request.handle(response);
		OpsUtil.getInstance().recordOperationTime(connectionInfo, request, response);
		logger.info(new RequestResponseSession(connectionInfo, request.toString(), response), connectionInfo);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request, successResponse);
		return;
	}
	
	public static void write(ConnectionInfo connectionInfo,
			Request request, JsonObject result) {
		if (connectionInfo.isBinaryConnection()) {
			logger.error(String.format("write binary connection:%s json data null", connectionInfo.toJsonObject().toString()));
			return;
		}

		StringBuffer sb = new StringBuffer(result.toString());
		sb.append(DELIMIT);
		String finalResult = sb.toString();
		if(request.isNeedResponse()) {
			if (connectionInfo.isWriteQueueFull()) {
				logger.error(String.format("Request:%s write:%s to userID:%s but write queue full", request.toString(),
						result, connectionInfo.getUserID()), connectionInfo);
			}
			connectionInfo.write(finalResult);
		}

		OpsUtil.getInstance().recordOperationTime(connectionInfo, request, result);
		InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), finalResult);
		logger.info(new RequestResponseSession(connectionInfo, request.toString(), result), connectionInfo);
		RequesResponseLoggerTool.logRequestResponse(connectionInfo, request, result);

		return;
	}

	public static void pushWrite(ConnectionInfo from, ConnectionInfo connectionInfo,
			Request request, JsonObject result) {
		if (connectionInfo.isBinaryConnection()) {
			logger.error(String.format("pushWrite binary connection:%s json data null", connectionInfo.toJsonObject().toString()));
			return;
		}
		StringBuffer sb = new StringBuffer(result.toString());
		sb.append(DELIMIT);
		String finalResult = sb.toString();

		if(request.isNeedResponse()) {
			connectionInfo.write(finalResult);
			if (connectionInfo.isWriteQueueFull()) {
				logger.error(String.format("Request:%s write:%s to userID:%s but write queue full", request.toString(),
						result, connectionInfo.getUserID()), connectionInfo);
			}
		}
		OpsUtil.getInstance().recordOperationTime(connectionInfo, request, result);
		InOutDataLogger.loggerOutput(connectionInfo.getConnectionID(), finalResult);
		logger.info(new RequestResponseSession(from, connectionInfo, request.toString(), result), connectionInfo);
		RequesResponseLoggerTool.logRequestResponse(from, connectionInfo, request, result);

		return;
	}


	public static void pushWrite(ConnectionInfo from,
								 ConnectionInfo to,
							 Request request, Buffer result) {
		if(request.isNeedResponse()) {
			to.write(result);

			if (to.isWriteQueueFull()) {
				logger.error(String.format("Request:%s pushwrite:%s to userID:%s from:%s but write queue full", request.toString(),
						result.toString(), to.getUserID(), from.getUserID()), to);
			}
		}

		InOutDataLogger.loggerOutput(to.getConnectionID(), result, Tools.bufferToPrettyByteString(result));
		logger.info(new RequestBinaryResponseSession(from, to, request.toString(), result), to);
		logger.info(new RequestBinaryResponseSession(from, to, request.toString(), result), from);
		RequesResponseLoggerTool.logRequestResponse(from, to, request, result);
		return;
	}

	public static void write(ConnectionInfo to,
							 Request request, Buffer result) {
		if(request.isNeedResponse()) {
			logger.info(String.format("Connection.write:%s", Tools.bufferToPrettyByteString(result)));
			to.write(result);

			if (to.isWriteQueueFull()) {
				logger.error(String.format("Request:%s write:%s to userID:%s but write queue full", request.toString(),
						result.toString(), to.getUserID()), to);
			}
		}

		InOutDataLogger.loggerOutput(to.getConnectionID(), result, Tools.bufferToPrettyByteString(result));
		logger.info(new RequestBinaryResponseSession(to, request.toString(), result), to);
		RequesResponseLoggerTool.logRequestResponse(to, request, result);

		return;
	}

}
