package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.util.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Date;
import java.util.Random;
import java.util.Set;


public class VerificationCode {
	public static HYLogger logger = HYLogger.getLogger(VerificationCode.class);
	// {"userName":,"verificationCode":,"recentGetDateTime":,"verificationCodeType"}
	public static String tableName = "verificationCode";
	private static int RECENT = IniUtil.getInstance().getVerificationCodeTimeGap(); // ��λ�룬���λ�ȡ��֤��ļ���費���ڸ�ʱ��
	private static int RecentForCreateAccount = IniUtil.getInstance().getVerificationCodeTimeGapForCreateAccount();

	public static String generateVerificationCode(String phoneNumber) {
		Set<String> testPhoneNum = IniUtil.getInstance().getTestPhoneNum();
		String testVerificationCode = IniUtil.getInstance().getTestVerificationCode();
		if(testPhoneNum!=null&&testPhoneNum.contains(phoneNumber)) {
			if ( testVerificationCode != null) {
				return testVerificationCode;
			}
		}

		if(!IniUtil.getInstance().isVerificationCodeSame()) {
			Random rnd = new Random();
			int num = rnd.nextInt(899999) + 100000;
			return new Integer(num).toString();
		}
		else return "888888";
	}

	public static void getVerificationCode(ConnectionInfo connectionInfo, Request request, String phoneNumber) {
		MongoClient mongoClient = Database.getInstance().getMongoClient();
		JsonObject query = new JsonObject().put("userName", phoneNumber);
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
				// �����������֤�룬��ʱ����������Ҫ�󣬲������ٷ���ע����
				if (VerificationCode.isRecent(prevDateString)) {
					ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.GET_VERIFICATION_CODE_TOO_FREQUENT);
					return;
				}
			}

			String verificationCode = VerificationCode.generateVerificationCode(phoneNumber);
			ShortMessageTool.getVerificationCode(Server.getInstance().getVertx(), verificationCode, phoneNumber, isSuccess->{
				if(!isSuccess) {
					ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SHORT_MESSAGE_ERROR);
					return;					
				}
				else {					
					JsonObject object = new JsonObject().put("userName", phoneNumber);
					object.put("verificationCode", verificationCode);
					object.put("recentGetDateTime", Tools.dateToStr(new Date()));
					object.put("verificationCodeType", connectionInfo.getTempType());
					if (id != null) {
						object.put("_id", id);
					}

					mongoClient.save(tableName, object, res1 -> {
						if (res1.succeeded()) {
							ResponseHandlerHelper.success(connectionInfo, request);
							connectionInfo.setPhoneNumber(phoneNumber);
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

	public static void getLoginVerificationCode(ConnectionInfo connectionInfo, Request request, String phoneNumber) {
		MongoClient mongoClient = Database.getInstance().getMongoClient();
		JsonObject query = new JsonObject().put("userName", phoneNumber);
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
				if (VerificationCode.isRecent(prevDateString)) {
					ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.GET_VERIFICATION_CODE_TOO_FREQUENT);
					return;
				}
			}
			String verificationCode = VerificationCode.generateVerificationCode(phoneNumber);
			AliDaYuShortMessageTool.sendVerificationShortMessage(connectionInfo, Server.getInstance().getVertx(),
					phoneNumber, verificationCode, isSuccess->{
//			ShortMessageTool.getVerificationCode(Server.getInstance().getVertx(), verificationCode, phoneNumber, isSuccess->{
				if(!isSuccess) {
					ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SHORT_MESSAGE_ERROR);
					return;					
				}
				else {				
					String hashedPassword = EncoderHandler.SHA1(verificationCode).toUpperCase();
					JsonObject newAccountQuery = new JsonObject().put("userName", phoneNumber);
					mongoClient.find(Account.accountTable, newAccountQuery, newAccountQueryRes -> {
						if (!newAccountQueryRes.succeeded()) {
							logger.error(Tools.getTrace(request.toString(), newAccountQueryRes.cause()), connectionInfo);
							ResponseHandlerHelper.serverError(connectionInfo, request);
							return;
						}

						if (newAccountQueryRes.result().size() == 0) {
							JsonObject newAccount = new JsonObject().put("userName", phoneNumber).put("hashedPassword", hashedPassword)
									.put("type", AccountType.USER);

							mongoClient.save(Account.accountTable,newAccount, res4 -> {
								if (res4.succeeded()) {
									ResponseHandlerHelper.success(connectionInfo, request);

									OpsUtil.getInstance().recordAccountsLog(OpsUtil.USAGE_GET_LOGIN_VERI_CODE, connectionInfo, request,
											newAccount, OpsUtil.OPERATION_INSERT, "getLoginVerificationCode",
											phoneNumber, AccountType.USER);

									return;
								} else {//
									logger.error(Tools.getTrace(request.toString(), res4.cause()), connectionInfo);
									ResponseHandlerHelper.serverError(connectionInfo, request);
									return;
								}
							});

							JsonObject newRegistedAccount = newAccount.copy().put("dateTime", Tools.currentDateStr());
							mongoClient.save(Account.appAccountsLogTable,newRegistedAccount, saveNewRegistedAccountRes -> {
								if (saveNewRegistedAccountRes.failed()) {
									logger.error(Tools.getTrace(request.toString(), saveNewRegistedAccountRes.cause()), connectionInfo);
									return;
								}
							});
							return;
						}

						JsonObject query1 = new JsonObject().put("_id", newAccountQueryRes.result().get(0).getValue("_id"));
						JsonObject newAccount = new JsonObject().put("$set", new JsonObject().put("hashedPassword", hashedPassword));
						mongoClient.updateCollection(Account.accountTable, query1, newAccount, newAccountRes -> {
							// �û���ע��ɹ�
							if (newAccountRes.succeeded()) {
								ResponseHandlerHelper.success(connectionInfo, request);
								return;
							} else {// �û���ע��ʧ��
								logger.error(Tools.getTrace(request.toString(), newAccountRes.cause()), connectionInfo);
								ResponseHandlerHelper.serverError(connectionInfo, request);
								return;
							}
						});

					});

					JsonObject object = new JsonObject().put("userName", phoneNumber);
					object.put("verificationCode", verificationCode);
					object.put("recentGetDateTime", Tools.dateToStr(new Date()));
					object.put("verificationCodeType", TempServiceType.LOGIN);
					if (id != null) {
						object.put("_id", id);
					}

					mongoClient.save(tableName, object, res1 -> {
					});
				}
			});
		});
	}


	/*
	 * �Ƿ���ڻ�ȡ����֤�룬�����ܷ��ȡ��֤��ʱ����
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
	
	/*
	 * �Ƿ���֤���ǽ��ڵģ����ڴ����˺ŵ���֤
	 */
	public static boolean isRecentForCreateAccount(String prevDateTimeString) {
		Date prevDate = Tools.strToDate(prevDateTimeString);
		if (prevDate == null)
			return false;
		Date curDate = new Date();

		long diffS = (curDate.getTime() - prevDate.getTime()) / 1000;
		if (diffS <= RecentForCreateAccount)
			return true;
		else
			return false;
	}


}
