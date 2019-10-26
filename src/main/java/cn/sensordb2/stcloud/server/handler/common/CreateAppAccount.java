package cn.sensordb2.stcloud.server.handler.common;


import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.Account;
import cn.sensordb2.stcloud.server.common.AccountType;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.util.*;
import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.Arrays;
import java.util.Date;

/*
{
    "zl_cloud": "1.0",
    "method": "common.createAppAccount",
    "params": {
      	"userID": #,
      	"password": #,
        "nickname": #,
        "name": #,
        "headImage": #,
        "sex": #,
        "birth": #,
        "phone": #,
        "email": #,
        "area": #,
    },
    "id": #
}

 */
public class CreateAppAccount extends RequestHandler {
	private static HYLogger logger = HYLogger.getLogger(Account.class);
	public static final int USER_EXISTED = -101;

	public static final String[] paramFields = {
			"userID",
			"password",
			"nickname",
			"name",
			"headImage",
			"sex",
			"birth",
			"phone",
			"email",
			"area",
			"usage",
	};

	protected boolean isValidRequest(Request request) {
		JsonObject params = request.getParams();

		if (!Arrays.asList(paramFields).containsAll(params.getMap().keySet())) return false;

		String birth = params.getString("birth");

		if(birth!=null&&!Tools.isValidYYYYMMDDString(birth)) return false;
		if((params.getValue("sex")!=null && !(params.getValue("sex") instanceof  Integer))) return false;
		if(!(params.getValue("userID") instanceof  String)) return false;
		return true;
	}

	@Override
	public void handle(ConnectionInfo connectionInfo, Request request) {
		if(!connectionInfo.isLogged()) {
			ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.INVALID_STATUS);
			return;
		}

		if(IniUtil.getInstance().isCheckRequestParam()&&!this.isValidRequest(request)) {
			ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.INVALID_PARAMETERS);
			return;
		}

		JsonObject params = request.getParams();
		String nickName = params.getString("nickname");
		String name = params.getString("name");
		String headImage = params.getString("headImage");
		Object sex = params.getValue("sex");
		String birth = params.getString("birth");
		String phone = params.getString("phone");
		String email = params.getString("email");
		String area = params.getString("area");
		String usage = params.getString("usage");

		String userID = params.getString("userID");
		String password = params.getString("password")==null?"888888":params.getString("password");
		//device rom version
		String romVersion = params.getString("romVersion");

		MongoClient mongoClient = Database.getInstance().getMongoClient();

		//
		JsonObject query = new JsonObject().put("userName", userID);
		mongoClient.find(Account.accountTable, query, res -> {
			if (!res.succeeded()) {
				logger.exception(Tools.getTrace(request.toString(), res.cause()), connectionInfo);
				ResponseHandlerHelper.serverError(connectionInfo, request);
				return;
			}

			if (res.result().size() > 0) {
				ResponseHandlerHelper.error(connectionInfo, request, USER_EXISTED);
				return;
			}

			JsonObject acccountInfo = new JsonObject();

			acccountInfo.put("userName", userID);
			String hashedPassword = EncoderHandler.SHA1(password).toUpperCase();
			acccountInfo.put("hashedPassword", hashedPassword);
			acccountInfo.put("type", AccountType.USER);
			acccountInfo.put("dataTime", Tools.dateToStr(new Date()));

			if (name != null) {
				acccountInfo.put("name", name);
			}
			if (nickName != null) {
				acccountInfo.put("nickName", nickName);
			}
			if (headImage != null) {
				acccountInfo.put("headImage", headImage);
			}
			if (sex != null) {
				acccountInfo.put("sex", sex);
			}
			if (birth != null) {
				acccountInfo.put("birth", birth);
			}
			if (phone != null) {
				acccountInfo.put("phone", phone);
			}
			if (email != null) {
				acccountInfo.put("email", email);
			}
			if (area != null) {
				acccountInfo.put("area", area);
			}
			if (usage != null) {
				acccountInfo.put("usage", usage);
			}
			if(!StringUtil.isNullOrEmpty(romVersion)){
				acccountInfo.put("romVersion", romVersion);
			}

			mongoClient.save(Account.accountTable, acccountInfo, saveAccountInfoRes -> {
				if (!saveAccountInfoRes.succeeded()) {
					ResponseHandlerHelper.serverError(connectionInfo, request);
					OpsUtil.getInstance().recordAccountsLog("CreateAppAccount", connectionInfo, request,
							acccountInfo, OpsUtil.OPERATION_INSERT, "CreateAppAccount",
							userID, AccountType.TEMP);

					return;
				} else {// �ɹ�
					ResponseHandlerHelper.success(connectionInfo, request);
					return;
				}
			});
		});
	}
}
