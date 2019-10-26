package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.gate.BinaryResponseFactory;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Date;
import java.util.Random;


public class LoginRandomData {
	public static HYLogger logger = HYLogger.getLogger(LoginRandomData.class);
	// {"userName":,"loginRandomData":,"recentGetDateTime":,}
	public static String tableName = "loginRandomData";
	private static int RECENT = IniUtil.getInstance().getLoginRandomDataTimeGap();
	private static final int GET_RANDOM_DATA_TOO_FREQUENT = -101;
	private static final int USER_NOT_EXISTED = -105;

	public static String generateLoginRandomDataBase64(String phoneNumber) {
		byte[] bytes = new byte[16];
		if(!IniUtil.getInstance().isLoginRandomDataSame()) {
			Random rnd = new Random();
			rnd.nextBytes(bytes);
		}
		else {
			for(int i=0;i<16;i++) {
				bytes[i] = 0;
			}
		}
		return Tools.encodeBase64(bytes);
	}

	public static Buffer generateLoginRandomData(String phoneNumber) {
		byte[] bytes = new byte[16];
		if(!IniUtil.getInstance().isLoginRandomDataSame()) {
			Random rnd = new Random();
			rnd.nextBytes(bytes);
		}
		else {
			for(int i=0;i<16;i++) {
				bytes[i] = 0;
			}
		}
		return Buffer.buffer(bytes);
	}

	public static void getLoginRandomData(ConnectionInfo connectionInfo, Request request, String userID) {
		MongoClient mongoClient = Database.getInstance().getMongoClient();
		JsonObject userNameQuery = new JsonObject().put("userName", userID);
		mongoClient.find(Account.accountTable, userNameQuery, userNameQueryRes -> {
			if (!userNameQueryRes.succeeded()) {
				logger.error(Tools.getTrace(request.toString(), userNameQueryRes.cause()), connectionInfo);
				ResponseHandlerHelper.serverError(connectionInfo, request);
				return;
			}

			if (userNameQueryRes.result().size() == 0) {
				ResponseHandlerHelper.error(connectionInfo, request, USER_NOT_EXISTED);
				return;
			}

			JsonObject query = new JsonObject().put("userName", userID);
			mongoClient.findOne(tableName, query, null, res -> {
				if (res.failed()) {
					logger.error(Tools.getTrace(request.toString(), res.cause()), connectionInfo);
					ResponseHandlerHelper.serverError(connectionInfo, request);
					return;
				}

				JsonObject prev = res.result();

				final String id = prev != null ? prev.getString("_id") : null;
				//			if (prev != null) {
				//				String prevDateString = prev.getString("recentGetDateTime");
				//				if (LoginRandomData.isRecent(prevDateString)) {
				//					ResponseHandlerHelper.error(connectionInfo, request, GET_RANDOM_DATA_TOO_FREQUENT);
				//					return;
				//				}
				//			}

				Buffer loginRandomData = LoginRandomData.generateLoginRandomData(userID);
				String loginRandomDataBase64 = Tools.encodeBufferBase64(loginRandomData);

				JsonObject userLoginRandomData = new JsonObject().put("userName", userID);
				userLoginRandomData.put("loginRandomData", loginRandomDataBase64);
				userLoginRandomData.put("recentGetDateTime", Tools.dateToStr(new Date()));
				if (id != null) {
					userLoginRandomData.put("_id", id);
				}

				mongoClient.save(tableName, userLoginRandomData, saveUserLoginRandomDataRes -> {
					if (saveUserLoginRandomDataRes.succeeded()) {
						if(connectionInfo.isBinaryConnection()) {
							Buffer extra = Buffer.buffer();
							extra.appendBuffer(loginRandomData);
							extra.appendBuffer(Tools.createSameValueBuffer(0, 7));
							ResponseHandlerHelper.write(connectionInfo, request, BinaryResponseFactory.successResponse(request, extra));
						}
						else {
							JsonObject result = new JsonObject().put("randomData", loginRandomDataBase64);
							ResponseHandlerHelper.success(connectionInfo, request, result);
						}
						return;
					} else {
						logger.error(Tools.getTrace(request.toString(), saveUserLoginRandomDataRes.cause()), connectionInfo);
						ResponseHandlerHelper.serverError(connectionInfo, request);
						return;
					}
				});
			});
		});
	}

	/*
	 *
	 */
	public static boolean isRecent(String prevDateTimeString) {
		Date prevDate = Tools.strToDate(prevDateTimeString);
		if (prevDate == null)
			return false;
		Date curDate = new Date();

		long diffS = (curDate.getTime() - prevDate.getTime()) / 1000;
		if (diffS <= RECENT)
			return true;
		else
			return false;
	}


}
