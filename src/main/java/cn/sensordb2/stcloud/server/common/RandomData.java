package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.ShortMessageTool;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Date;
import java.util.Random;

public class RandomData {
	public static HYLogger logger = HYLogger.getLogger(RandomData.class);
	// {"userName":,"verificationCode":,"recentGetDateTime":,"verificationCodeType"}
	public static String tableName = "randomData";
	private static int RECENT = IniUtil.getInstance().getRandomDataTimeGap();

	public static String generateRandomData(String userID) {
		Random rnd = new Random();
		int num = rnd.nextInt(899999) + 100000;
		return new Integer(num).toString();
	}

	public static void getRandomData(ConnectionInfo connectionInfo, Request request, String userID) {
		MongoClient mongoClient = Database.getInstance().getMongoClient();
		JsonObject query = new JsonObject().put("userName", userID);
		mongoClient.findOne(tableName, query, null, res -> {
			if (res.failed()) {
				logger.error(Tools.getTrace(request.toString(), res.cause()), connectionInfo);
				ResponseHandlerHelper.serverError(connectionInfo, request);
				return;
			}

			JsonObject prev = res.result();

			final String id = prev!=null?prev.getString("_id"):null;
			if (prev != null) {
				String prevDateString = prev.getString("recentGetDateTime");

				if (RandomData.isRecent(prevDateString)) {
					ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.GET_VERIFICATION_CODE_TOO_FREQUENT);
					return;
				}
			}

			String verificationCode = RandomData.generateRandomData(userID);
			ShortMessageTool.getVerificationCode(Server.getInstance().getVertx(), verificationCode, userID, isSuccess->{
				if(!isSuccess) {
					ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SHORT_MESSAGE_ERROR);
					return;
				}
				else {
					JsonObject object = new JsonObject().put("userName", userID);
					object.put("verificationCode", verificationCode);
					object.put("recentGetDateTime", Tools.dateToStr(new Date()));
					object.put("verificationCodeType", connectionInfo.getTempType());
					if (id != null) {
						object.put("_id", id);
					}

					mongoClient.save(tableName, object, res1 -> {
						if (res1.succeeded()) {
							ResponseHandlerHelper.success(connectionInfo, request);
							connectionInfo.setPhoneNumber(userID);
							return;
						} else {
							logger.error(Tools.getTrace(request.toString(), res1.cause()), connectionInfo);
							ResponseHandlerHelper.serverError(connectionInfo, request);
							return;
						}
					});
				}
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
