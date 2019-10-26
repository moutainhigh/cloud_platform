package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.OpsUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Date;

public class Account {
    /*
     * accounts
	 * 
	 * userName, hashedPassword, type(账号类型：1，用户；2，路由器；3，门铃),
	 * nickName(账号昵称)
	 */

	/*
     * accountDevice
	 * 
	 * userName devID nickName relationShip(用户和设备关系，1，拥有；2，分享
	 */

    public static String accountTable = "user";
    public static String appAccountsLogTable = "appAccountsLog";
    private static HYLogger logger = HYLogger.getLogger(Account.class);

    /**
     * @param
     * @param
     */
    public static void createAccount(ConnectionInfo connectionInfo, Request request,
                                     String hashedPassword, String verificationCode) {
        String userName = connectionInfo.getPhoneNumber();
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        // 查找用户名是否存在
        JsonObject query = new JsonObject().put("userName", userName);
        mongoClient.find(accountTable, query, res -> {
            if (!res.succeeded()) {
                logger.error(Tools.getTrace(request.toString(), res.cause()), connectionInfo);
                ResponseHandlerHelper.serverError(connectionInfo, request);
                return;
            }

            if (res.result().size() >= 1) {
                ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.USER_EXISTED);
                return;
            }

            mongoClient.findOne(VerificationCode.tableName, query, null, res1 -> {
                if (!res1.succeeded()) {
                    logger.error(Tools.getTrace(request.toString(), res1.cause()), connectionInfo);
                    ResponseHandlerHelper.serverError(connectionInfo, request);
                    return;
                }

                JsonObject prev = res1.result();
                if (prev == null) {
                    ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.VERIFICATION_CODE_ERROR);
                    return;
                }

                String prevDateString = prev.getString("recentGetDateTime");
                String prevVerificationCode = prev.getString("verificationCode");
                String prevVerificationCodeType = prev.getString("verificationCodeType");

                // 最近发送了验证码，但时间间隔不满足要求，或者验证码不正确、类型不正确
                if (!VerificationCode.isRecentForCreateAccount(prevDateString)
                        || !prevVerificationCode.equals(verificationCode)
                        || !prevVerificationCodeType.equals(connectionInfo.getTempType())) {
                    ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.VERIFICATION_CODE_ERROR);
                    return;
                } else {
                    JsonObject object = new JsonObject().put("userName", userName).put("hashedPassword", hashedPassword)
                            .put("type", AccountType.USER).put("dataTime", Tools.dateToStr(new Date()));
                    mongoClient.insert(accountTable, object, res2 -> {
                        // 用户名注册成功
                        if (res2.succeeded()) {
                            ResponseHandlerHelper.success(connectionInfo, request);

                            OpsUtil.getInstance().recordAccountsLog(OpsUtil.USAGE_REGISTER, connectionInfo, request,
                                    object, OpsUtil.OPERATION_INSERT, "createAccount",
                                    userName, AccountType.USER);

                            return;
                        } else {// 用户名注册失败
                            logger.error(Tools.getTrace(request.toString(), res2.cause()), connectionInfo);
                            ResponseHandlerHelper.serverError(connectionInfo, request);
                            return;
                        }
                    });
                }
            });
        });
    }


    public static void ping(ConnectionInfo connectionInfo, Request request, String userName, String hashedPassword) {

        MongoClient mongoClient = Database.getInstance().getMongoClient();

        // 查找用户名和密码是否存在
        JsonObject userNameQuery = new JsonObject().put("username", userName);
        mongoClient.find(accountTable, userNameQuery, res -> {
            if (!res.succeeded()) {
                logger.error(Tools.getTrace(request.toString(), res.cause()), connectionInfo);
                ResponseHandlerHelper.serverError(connectionInfo, request);
                return;
            }
            if (res.result().size() == 0) {
                ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.USER_NOT_EXISTED);
                return;
            }

            String realHashedPassword = res.result().get(0).getString("password");
            if (hashedPassword != null && hashedPassword.equals(realHashedPassword)) {
                ResponseHandlerHelper.success(connectionInfo, request);
                return;
            } else {
                ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.USER_OR_PASSORD_ERROR);
                return;
            }
        });
    }

}
