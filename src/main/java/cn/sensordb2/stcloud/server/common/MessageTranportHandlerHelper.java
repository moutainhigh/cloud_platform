package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.message.RelayPushMessage;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.socketIOServer.WebsocketClientManager;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class MessageTranportHandlerHelper {
	private static HYLogger logger = HYLogger.getLogger(Server.class);

	public static void relayMessage(ConnectionInfo connectionInfo, Request request) {
		MongoClient mongoClient = Database.getInstance().getMongoClient();
		JsonObject params = request.getParams();
		
		JsonObject msg = params.getJsonObject("msg");
		String to = params.getString("to");
		String from = connectionInfo.getUserID();

		ClientManager.getInstance().getConnectionInfo(to, toConnectionInfo -> {
			if (toConnectionInfo == null) {
				ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.NO_ROUTE);
				return;
			}

			String userName;
			String devID;
			if (AccountType.isAppUser(connectionInfo.getAccountType()) &&
					!AccountType.isAppUser(toConnectionInfo.getAccountType())) {
				userName = from;
				devID = to;
			} else if (!AccountType.isAppUser(connectionInfo.getAccountType()) &&
					AccountType.isAppUser(toConnectionInfo.getAccountType())) {
				userName = to;
			} else if (!connectionInfo.isBinaryConnection()) {
				ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.ROUTE_ILLEGAL);
				return;
			} else {
				ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.ROUTE_ILLEGAL, false);
				return;
			}

//			JsonObject query = new JsonObject().put("userName", userName).put("devID", devID);
//			mongoClient.find(Account.accountDeviceTable, query, res -> {
//				if (!res.succeeded()) {
//					logger.error(Tools.getTrace(request.toString(), res.cause()), connectionInfo);
//					ResponseHandlerHelper.serverError(connectionInfo, request);
//					return;
//				}
//
//				if (res.result().size() == 0) {
//					if (!connectionInfo.isBinaryConnection()) {
//						ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.ROUTE_ILLEGAL);
//						return;
//					} else {
//						ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.ROUTE_ILLEGAL, false);
//						return;
//					}
//				}

				RelayPushMessage relayPushMessage = new RelayPushMessage();
				relayPushMessage.setMsg_content(request.getParams().getJsonObject("msg"));

				ResponseHandlerHelper.write(toConnectionInfo, request, relayPushMessage);
				logger.info(String.format("process request:%s relay:%s to:%s", request, relayPushMessage, to), connectionInfo);
				return;
//			});
		});

		WebsocketClientManager.getInstance().getConnectionInfo(to, toConnectionInfo -> {
			if (toConnectionInfo == null) {
				ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.NO_ROUTE);
				return;
			}
			String userName;
			String devID;
//			if (AccountType.isAppUser(connectionInfo.getAccountType()) &&
//					!AccountType.isAppUser(toConnectionInfo.getAccountType())) {
//				userName = from;
//				devID = to;
//			} else if (!AccountType.isAppUser(connectionInfo.getAccountType()) &&
//					AccountType.isAppUser(toConnectionInfo.getAccountType())) {
//				userName = to;
//			} else if (!connectionInfo.isBinaryConnection()) {
//				ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.ROUTE_ILLEGAL);
//				return;
//			} else {
//				ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.ROUTE_ILLEGAL, false);
//				return;
//			}

//			JsonObject query = new JsonObject().put("userName", userName).put("devID", devID);
//			mongoClient.find(Account.accountDeviceTable, query, res -> {
//				if (!res.succeeded()) {
//					logger.error(Tools.getTrace(request.toString(), res.cause()), connectionInfo);
//					ResponseHandlerHelper.serverError(connectionInfo, request);
//					return;
//				}
//
//				if (res.result().size() == 0) {
//					if (!connectionInfo.isBinaryConnection()) {
//						ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.ROUTE_ILLEGAL);
//						return;
//					} else {
//						ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.ROUTE_ILLEGAL, false);
//						return;
//					}
//				}

				RelayPushMessage relayPushMessage = new RelayPushMessage();
				relayPushMessage.setMsg_content(request.getParams().getJsonObject("msg"));
				relayPushMessage.setId(Integer.parseInt(request.getString("id")));
				logger.info("cgy123456");
				ResponseHandlerHelper.write(toConnectionInfo, request, relayPushMessage);
				logger.info(String.format("socketio process request:%s relay:%s to:%s", request, relayPushMessage, to), connectionInfo);

				return;
//			});
		});

	}
}
