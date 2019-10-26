package cn.sensordb2.stcloud.server.message;
import cn.sensordb2.stcloud.util.EncoderHandler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class RequestFactory {
	public static Request SET_TEMP_SVC_TYPE_REG(int id) {
		Request r = new Request(id, "SET_TEMP_SVC_TYPE");
		JsonObject params = new JsonObject();
		params.put("type", "REG");
		r.setParams(params);
		return r;
	}

	public static Request GET_ACCOUNT_INFO(int id) {
		Request r = new Request(id, "GET_ACCOUNT_INFO");
		r.setParams(null);
		return r;
	}

	public static Request SET_TEMP_SVC_TYPE_FIND_PASSWORD(int id) {
		Request r = new Request(id, "SET_TEMP_SVC_TYPE");
		JsonObject params = new JsonObject();
		params.put("type", "FIND_PASSWORD");
		r.setParams(params);
		return r;
	}

	public static Request GET_VERIFICATION_CODE(int id, String phone_num) {
		Request r = new Request(id, "GET_VERIFICATION_CODE");
		JsonObject params = new JsonObject();
		params.put("phone_num", phone_num);
		r.setParams(params);
		return r;
	}


	public static Request GET_LOGIN_VERIFICATION_CODE(int id, String phone_num) {
		Request r = new Request(id, "GET_LOGIN_VERIFICATION_CODE");
		JsonObject params = new JsonObject();
		params.put("phone_num", phone_num);
		r.setParams(params);
		return r;
	}

	public static Request  GET_RANDOM_DATA(int id, String userID) {
		Request r = new Request(id, "GET_RANDOM_DATA");
		JsonObject params = new JsonObject();
		params.put("userID", userID);
		r.setParams(params);
		return r;
	}

	public static Request  AES_RANDOM_DATA(int id, String userID, String randomData) {
		Request r = new Request(id, "AES_RANDOM_DATA");
		JsonObject params = new JsonObject();
		params.put("userID", userID);
		params.put("randomData", randomData);
		r.setParams(params);
		return r;
	}

	public static Request  GET_RANDOM_DATA_RESPONSE(int id, String randomData) {
		Request r = new Request(id, "GET_RANDOM_DATA_RESPONSE");
		JsonObject params = new JsonObject();
		params.put("randomData", randomData);
		r.setParams(params);
		return r;
	}

	public static Request  AES_DESCRPT_RANDOM_DATA(int id, String userID, String randomDataAes) {
		Request r = new Request(id, "AES_DESCRPT_RANDOM_DATA");
		JsonObject params = new JsonObject();
		params.put("userID", userID);
		params.put("randomDataAes", randomDataAes);
		r.setParams(params);
		return r;
	}


	public static Request REG_USER_TERMINAL(int id, String verification_code,
			String hashed_pwd) {
		Request r = new Request(id, "REG_USER_TERMINAL");
		JsonObject params = new JsonObject();
		params.put("verification_code", verification_code);
		params.put("hashed_pwd", hashed_pwd);
		r.setParams(params);
		return r;
	}

/*
{
    "zl_cloud": "1.0",
    "method": "LOGIN_BY_CODE",
    "params": {
        "phone_num": 1#,
        "code": 2#
    },
    "id": 3#
}
*/

	public static Request LOGIN_BY_CODE(int id, String phone_num,
			String code) {
		Request r = new Request(id, "LOGIN_BY_CODE");
		JsonObject params = new JsonObject();
		params.put("phone_num", phone_num);
		params.put("code", code);
		r.setParams(params);
		return r;
	}

	public static Request LOGIN_BY_RANDOM_DATA_DEV(int id, String userID,
			String code) {
		Request r = new Request(id, "LOGIN_BY_RANDOM_DATA");
		JsonObject params = new JsonObject();
		params.put("userID", userID);
		params.put("code", code);
		r.setParams(params);
		return r;
	}

	public static Request LOGIN_BY_RANDOM_DATA_RESPONSE(int id,
			int code) {
		Request r = new Request(id, "LOGIN_BY_RANDOM_DATA_RESPONSE");
		JsonObject params = new JsonObject();
		params.put("code", code);
		r.setParams(params);
		return r;
	}

	public static Request LOGIN(int id, String login_id,
			String hashed_pwd) {
		Request r = new Request(id, "user.Login");
		JsonObject params = new JsonObject();
		params.put("username", login_id);
		params.put("hashedPassword", hashed_pwd);
		r.setParams(params);
		return r;
	}

	public static Request UPLOADDATA(int id){
		Request r = new Request(id,"data.uploadData");
		JsonObject params = new JsonObject();
		Double longitude = 123.321;
		Double latitude = 321.123;
		Double height = 111.111;
		Double signalValue	= 1.0;
		params.put("longitude",longitude);
		params.put("latitude",latitude);
		params.put("height",height);
		params.put("signalValue",signalValue);
		r.setParams(params);
		return r;
	}


	public static Request PING(int id, String userName,
							   String hashedPassword) {
		Request r = new Request(id, "common.ping");
		JsonObject params = new JsonObject();
		params.put("userName", userName);
		params.put("hashedPassword", hashedPassword);
		r.setParams(params);
		return r;
	}


	public static Request CHANGE_PWD(int id, String old_hashed_pwd,
			String new_hashed_pwd) {
		Request r = new Request(id, "CHANGE_PWD");
		JsonObject params = new JsonObject();
		params.put("old_hashed_pwd", old_hashed_pwd);
		params.put("new_hashed_pwd", new_hashed_pwd);
		r.setParams(params);
		return r;
	}


	public static Request RESET_PWD(int id, String verification_code,
			String new_hashed_pwd) {
		Request r = new Request(id, "RESET_PWD");
		JsonObject params = new JsonObject();
		params.put("verification_code", verification_code);
		params.put("new_hashed_pwd", new_hashed_pwd);
		r.setParams(params);
		return r;
	}


	/*
	{
		"zl_cloud": "1.0",
			"method": "CreateAppAccount",
			"params": {
		"userID": #,
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
	public static Request CREATE_APP_ACCOUNT(int id, String userID,
											 String nickname, String name,
											 String headImage, int sex,
											 String birth, String phone,
											 String email, String area) {
		Request r = new Request(id, "CreateAppAccount");
		JsonObject params = new JsonObject();
		params.put("userID", userID);
		params.put("nickname", nickname);

		params.put("name", name);
		params.put("headImage", headImage);
		params.put("sex", sex);

		params.put("birth", birth);
		params.put("phone", phone);
		params.put("email", email);
		params.put("area", area);
		r.setParams(params);
		return r;
	}


	public static Request CREATE_APP_ACCOUNT_WITH_PASSWORD(int id, String userID,
														   String password,
														   String nickname, String name,
														   String headImage, int sex,
														   String birth, String phone,
														   String email, String area, String usage) {
		Request r = new Request(id, "CreateAppAccount");
		JsonObject params = new JsonObject();
		params.put("userID", userID);
		params.put("password", password);
		params.put("nickname", nickname);

		params.put("name", name);
		params.put("headImage", headImage);
		params.put("sex", sex);

		params.put("birth", birth);
		params.put("phone", phone);
		params.put("email", email);
		params.put("area", area);
		params.put("usage", usage);
		r.setParams(params);
		return r;
	}

	public static Request CREATE_APP_ACCOUNT_WITH_PASSWORD(int id, String userID,
														   String password,
											 String nickname, String name,
											 String headImage, int sex,
											 String birth, String phone,
											 String email, String area) {
		Request r = new Request(id, "CreateAppAccount");
		JsonObject params = new JsonObject();
		params.put("userID", userID);
		params.put("password", password);
		params.put("nickname", nickname);

		params.put("name", name);
		params.put("headImage", headImage);
		params.put("sex", sex);

		params.put("birth", birth);
		params.put("phone", phone);
		params.put("email", email);
		params.put("area", area);
		r.setParams(params);
		return r;
	}


	/*
	{
		"zl_cloud": "1.0",
		"method": "CREATE_APP_SUB_ACCOUNT",
		"params": {
			"userID": #,
			"hashedPassword": #,
			"subType”: #,
			"rightsParam”: {},
		},
		“id”: #
	}
	 */
	public static Request CREATE_APP_SUB_ACCOUNT(int id, String userID,
											 String hashedPassword, int subType,
											 JsonObject rightsParam) {
		Request r = new Request(id, "CREATE_APP_SUB_ACCOUNT");
		JsonObject params = new JsonObject();
		params.put("userID", userID);
		params.put("hashedPassword", hashedPassword);

		params.put("subType", subType);
		params.put("rightsParam", rightsParam);
		r.setParams(params);
		return r;
	}

	/*
	{
		"zl_cloud": "1.0",
		"method": "UPDATE_ACCOUNT_INFO",
		"params": {
			"nickName": #,
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
	public static Request UPDATE_ACCOUNT_INFO(int id,
											 String nickName, String name,
											 String headImage, int sex,
											 String birth, String phone,
											 String email, String area) {
		Request r = new Request(id, "UPDATE_ACCOUNT_INFO");
		JsonObject params = new JsonObject();
		params.put("nickName", nickName);

		params.put("name", name);
		params.put("headImage", headImage);
		params.put("sex", sex);

		params.put("birth", birth);
		params.put("phone", phone);
		params.put("email", email);
		params.put("area", area);
		r.setParams(params);
		return r;
	}


	public static Request ADD_DEV_TO_USER(int id, String dev_id,
			boolean is_encrypted, double longitude, double latitude) {
		Request r = new Request(id, "ADD_DEV_TO_USER");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		params.put("is_encrypted", is_encrypted);

		JsonObject pos = new JsonObject();
		pos.put("longitude", longitude);
		pos.put("latitude", latitude);

		params.put("pos", pos);
		r.setParams(params);
		return r;
	}

	public static Request ADD_DEV_TO_USER_CHANGE_DEV_TYPE(int id, String dev_id, String type,
			boolean is_encrypted, double longitude, double latitude) {
		Request r = new Request(id, "ADD_DEV_TO_USER");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		params.put("type", type);
		params.put("is_encrypted", is_encrypted);

		JsonObject pos = new JsonObject();
		pos.put("longitude", longitude);
		pos.put("latitude", latitude);

		params.put("pos", pos);
		r.setParams(params);
		return r;
	}

	public static Request DEL_DEV_FROM_USER(int id, String dev_id) {
		Request r = new Request(id, "DEL_DEV_FROM_USER");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		r.setParams(params);
		return r;
	}

	/*
{
    "zl_cloud": "1.0",
    "method": "DELETE_OUTER_ACCOUNT",
    "params": {
        "outerID": #,
    },
    "id": #
}
     */
	public static Request DELETE_OUTER_ACCOUNT(int id, String outerID) {
		Request r = new Request(id, "DELETE_OUTER_ACCOUNT");
		JsonObject params = new JsonObject();
		params.put("outerID", outerID);
		r.setParams(params);
		return r;
	}

	public static Request DELETE_ALL_OUTER_ACCOUNT(int id) {
		Request r = new Request(id, "DELETE_OUTER_ACCOUNT");
		JsonObject params = new JsonObject();
		r.setParams(params);
		return r;
	}


	public static Request SHARE_DEV(int id, String shared_dev_id,
			String to_user_id) {
		Request r = new Request(id, "SHARE_DEV");
		JsonObject params = new JsonObject();
		params.put("shared_dev_id", shared_dev_id);
		params.put("to_user_id", to_user_id);
		r.setParams(params);
		return r;
	}


	public static Request CANCEL_SHARE_DEV(int id, String shared_dev_id,
			String to_user_id) {
		Request r = new Request(id, "CANCEL_SHARE_DEV");
		JsonObject params = new JsonObject();
		params.put("shared_dev_id", shared_dev_id);
		params.put("to_user_id", to_user_id);
		r.setParams(params);
		return r;
	}

	public static Request GET_DEV_LIST(int id) {
		Request r = new Request(id, "GET_DEV_LIST");
		r.setParams(null);
		return r;
	}

	public static Request GET_DEV_LIST_SINGLE(int id) {
		Request r = new Request(id, "GET_DEV_LIST_SINGLE");
		r.setParams(null);
		return r;
	}

	public static Request RELAY_MSG(int id, JsonObject original_msg,
			String to) {
		Request r = new Request(id, "common.RelayMsg");
		JsonObject params = new JsonObject();
		params.put("msg", original_msg);
		params.put("to", to);
		r.setParams(params);
		return r;
	}

	public static Request REG_DEV_TERMINAL(int id, String dev_name,
			String hashed_pwd, int type) {
		Request r = new Request(id, "REG_DEV_TERMINAL");
		JsonObject params = new JsonObject();
		params.put("dev_name", dev_name);
		params.put("hashed_pwd", hashed_pwd);
		params.put("type", type);
		r.setParams(params);
		return r;
	}

	public static Request HEARTBEAT(int id) {
		Request r = new Request(id, "HEARTBEAT");
		r.setParams(null);
		return r;
	}

	public static Request LOCK_UPLOAD_WARNING(int id, String lockID, int type) {
		Request r = new Request(id, "LOCK_UPLOAD_WARNING");
		JsonObject params = new JsonObject();
		params.put("lockID", lockID);
		params.put("type", type);
		params.put("desc", "");
		r.setParams(params);
		return r;
	}


	public static Request SET_NICKNAME(int id, String nickname) {
		Request r = new Request(id, "SET_NICKNAME");
		JsonObject params = new JsonObject();
		params.put("nickname", nickname);
		r.setParams(params);
		return r;
	}

	public static Request SET_DEV_NICKNAME(int id, String dev_id, String nickname) {
		Request r = new Request(id, "SET_DEV_NICKNAME");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		params.put("nickname", nickname);
		r.setParams(params);
		return r;
	}


	public static Request UPLOAD_USER_REPORT(int id, String report_text) {
		Request r = new Request(id, "UPLOAD_USER_REPORT");
		JsonObject params = new JsonObject();
		params.put("report_text", report_text);
		params.put("report_pics", new JsonArray());
		params.put("devType", "ios");
		params.put("phone", "13645160637");
		r.setParams(params);
		return r;
	}

	public static Request UPLOAD_APP_EXCEPTION_REPORT(int id, String phone_type, String phone_version,
			String app_version, String exception) {
		Request r = new Request(id, "UPLOAD_APP_EXCEPTION_REPORT");
		JsonObject params = new JsonObject();
		params.put("phone_type", phone_type);
		params.put("phone_version", phone_version);
		params.put("app_version", app_version);
		params.put("exception", exception);
		r.setParams(params);
		return r;
	}

	public static Request UPLOAD_OPERATION_DATA(int id, int loginSpendTime) {
		Request r = new Request(id, "UPLOAD_OPERATION_DATA");
		JsonObject params = new JsonObject();
		params.put("loginSpendTime", loginSpendTime);
		r.setParams(params);
		return r;
	}

	public static Request GET_DEV_OWNER(int id, String dev_id) {
		Request r = new Request(id, "GET_DEV_OWNER");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		r.setParams(params);
		return r;
	}

	public static Request RING_UPLOAD_IMAGE(int id, String type, String user_id,
			int request_id, String[] paths) {
		Request r = new Request(id, "RING_UPLOAD_IMAGE");
		JsonObject params = new JsonObject();
		params.put("type", type);
		params.put("user_id", user_id);
		params.put("request_id", request_id);

		JsonArray images = new JsonArray();
		JsonObject image;
		for(String path: paths) {
			image = new JsonObject();
			image.put("path", path);
			images.add(image);
		}
		params.put("images", images);
		r.setParams(params);
		return r;
	}

	public static Request RING_UPLOAD_IMAGE_WARNING(int id, String[] paths) {
		Request r = new Request(id, "RING_UPLOAD_IMAGE");
		JsonObject params = new JsonObject();
		params.put("type", "WARN");

		JsonArray images = new JsonArray();
		JsonObject image;
		for(String path: paths) {
			image = new JsonObject();
			image.put("path", path);
			images.add(image);
		}
		params.put("images", images);
		r.setParams(params);
		return r;
	}


	public static Request RING_UPLOAD_IMAGE_WARNING_URL(int id, String url) {
		Request r = new Request(id, "RING_UPLOAD_IMAGE");
		JsonObject params = new JsonObject();
		params.put("type", "WARN");

		JsonArray images = new JsonArray();
		JsonObject image;
		image = new JsonObject();
		image.put("url", url);
		images.add(image);

		params.put("images", images);
		r.setParams(params);
		return r;
	}

	public static Request RING_CALL_REQUEST(int id, String[] paths) {
		Request r = new Request(id, "RING_CALL_REQUEST");
		JsonObject params = new JsonObject();

		JsonArray images = new JsonArray();
		JsonObject image;
		for(String path: paths) {
			image = new JsonObject();
			image.put("path", path);
			images.add(image);
		}
		params.put("images", images);
		r.setParams(params);
		return r;
	}

	public static Request RING_GET_SESSEION_INFO(int id) {
		Request r = new Request(id, "RING_GET_SESSEION_INFO");
		JsonObject params = new JsonObject();
		r.setParams(params);
		return r;
	}

/*
{
    "zl_cloud": "1.0",
    "method": "RING_BIND",
	"params": {
        "devID": #,
        "pos": {
           "longitude": #,
           "latitude": #
    	}
     },
    "id": #
}
 */
	public static Request RING_BIND(int id, String devID) {
		Request r = new Request(id, "RING_BIND");
		JsonObject params = new JsonObject();
		params.put("devID", devID);
		r.setParams(params);
		return r;
	}

	public static Request RING_BIND(int id, String devID, int longitude, int latitude) {
		Request r = new Request(id, "RING_BIND");
		JsonObject params = new JsonObject();
		params.put("devID", devID);

		JsonObject pos = new JsonObject();
		pos.put("longitude", longitude);
		pos.put("latitude", latitude);

		params.put("pos", pos);
		r.setParams(params);
		return r;
	}


/*
{
    "zl_cloud": "1.0",
    "method": "RING_UPDATE_STATUS",
	"params": {
        "battery": #,
        "configed": #,
	}
    "id": #
} */
	public static Request RING_UPDATE_STATUS(int id, int battery, int configed) {
		Request r = new Request(id, "RING_UPDATE_STATUS");
		JsonObject params = new JsonObject();
		params.put("battery", battery);
		params.put("configed", configed);
		r.setParams(params);
		return r;
	}

/*
{
    "zl_cloud": "1.0",
    "method": "RING_RTSERVER_UPDATE_SESSION_STATUS",
	"params": {
	     "devID": #,
	     "streamSize": #,
	}
    "id": #
}
*/
	public static Request RING_RTSERVER_UPDATE_SESSION_STATUS(int id, String devID) {
		Request r = new Request(id, "RING_RTSERVER_UPDATE_SESSION_STATUS");
		JsonObject params = new JsonObject();
		params.put("devID", devID);
//		params.put("streamSize", streamSize);
		r.setParams(params);
		return r;
	}


	public static Request RING_CALL_REQUEST_URLS(int id, String[] urls) {
		Request r = new Request(id, "RING_CALL_REQUEST");
		JsonObject params = new JsonObject();

		JsonArray images = new JsonArray();
		JsonObject image;
		for(String url: urls) {
			image = new JsonObject();
			image.put("url", url);
			images.add(image);
		}
		params.put("images", images);
		r.setParams(params);
		return r;
	}


	public static Request RING_CALL_REQUEST_Paths(int id, String[] paths) {
		Request r = new Request(id, "RING_CALL_REQUEST");
		JsonObject params = new JsonObject();

		JsonArray images = new JsonArray();
		JsonObject image;
		for(String path: paths) {
			image = new JsonObject();
			image.put("path", path);
			images.add(image);
		}
		params.put("images", images);
		r.setParams(params);
		return r;
	}

	public static Request RING_CALL_ACCEPT(int id, String dev_id, int call_request_id) {
		Request r = new Request(id, "RING_CALL_ACCEPT");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		params.put("call_request_id", call_request_id);
		r.setParams(params);
		return r;
	}


	public static Request RING_CALL_REJECT(int id, String dev_id, int call_request_id) {
		Request r = new Request(id, "RING_CALL_REJECT");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		params.put("call_request_id", call_request_id);
		r.setParams(params);
		return r;
	}

	public static Request RING_SEND_VOICE(int id, String to, String[] paths) {
		Request r = new Request(id, "RING_SEND_VOICE");
		JsonObject params = new JsonObject();
		params.put("to", to);

		JsonArray voices = new JsonArray();
		JsonObject voice;
		for(String path: paths) {
			voice = new JsonObject();
			voice.put("path", path);
			voices.add(voice);
		}
		params.put("voice", voices);
		r.setParams(params);
		return r;
	}

	public static Request RING_SNAPSHOT(int id, String dev_id) {
		Request r = new Request(id, "RING_SNAPSHOT");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		r.setParams(params);
		return r;
	}

	public static Request RING_GET_WARNING_INFO(int id, String dev_id,
			String start_time, String end_time) {
		Request r = new Request(id, "RING_GET_WARNING_INFO");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		params.put("start_time", start_time);
		params.put("end_time", end_time);
		r.setParams(params);
		return r;
	}

	public static Request RING_GET_VISITOR_SESSION_INFO(int id, String dev_id,
			String start_time, String end_time) {
		Request r = new Request(id, "RING_GET_VISITOR_SESSION_INFO");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		params.put("start_time", start_time);
		params.put("end_time", end_time);
		r.setParams(params);
		return r;
	}


	public static Request RING_CONFIG(int id, String dev_id,
			int human_check, int lamp_switch, int warning_time,
			int cont_cap_num, int ring_tone, int ring_volumn,
			int capture_interval, int capture_image_num, int mic_sensitivity) {
		Request r = new Request(id, "RING_CONFIG");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		params.put("human_check", human_check);
		params.put("lamp_switch", lamp_switch);
		params.put("warning_time", warning_time);
		params.put("cont_cap_num", cont_cap_num);
		params.put("ring_tone", ring_tone);
		params.put("ring_volumn", ring_volumn);
		params.put("capture_interval", capture_interval);
		params.put("capture_image_num", capture_image_num);
		params.put("mic_sensitivity", mic_sensitivity);
		r.setParams(params);
		return r;
	}

	public static Request RING_CONFIG(int id, String dev_id,
			int human_check, int lamp_switch, int warning_time,
			int cont_cap_num, int ring_tone, int ring_volumn) {
		Request r = new Request(id, "RING_CONFIG");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		params.put("human_check", human_check);
		params.put("lamp_switch", lamp_switch);
		params.put("warning_time", warning_time);
		params.put("cont_cap_num", cont_cap_num);
		params.put("ring_tone", ring_tone);
		params.put("ring_volumn", ring_volumn);
		r.setParams(params);
		return r;
	}

	public static Request RING_GET_CONFIG(int id, String dev_id) {
		Request r = new Request(id, "RING_GET_CONFIG");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		r.setParams(params);
		return r;
	}

	public static Request RING_UPDATE_STATUS(int id, int battery) {
		Request r = new Request(id, "RING_UPDATE_STATUS");
		JsonObject params = new JsonObject();
		params.put("battery", battery);
		r.setParams(params);
		return r;
	}

	public static Request RING_GET_STATUS(int id, String dev_id) {
		Request r = new Request(id, "RING_GET_STATUS");
		JsonObject params = new JsonObject();
		params.put("dev_id", dev_id);
		r.setParams(params);
		return r;
	}

	public static Request ACC_DEV_GROUP_ADD(int id,String devID, String groupID){
		Request r = new Request(id, "ACC_DEV_GROUP_ADD");
		JsonObject params = new JsonObject();
		params.put("groupID",groupID);
		params.put("devID",devID);
		r.setParams(params);
		return r;
	}
	public static Request ACC_DEV_GROUP_GET(int id,String groupID, Integer type){
			Request r = new Request(id, "ACC_DEV_GROUP_GET");
			JsonObject params = new JsonObject();
			params.put("groupID",groupID);
			params.put("type",type);
			r.setParams(params);
			return r;
	}
	public static Request ACC_DEV_GROUP_UPDATE(int id,String itemID, String userID, String devID, String groupID ){
		Request r = new Request(id, "ACC_DEV_GROUP_UPDATE");
		JsonObject params = new JsonObject();
		params.put("ID",itemID);
		params.put("userID",userID);
		params.put("groupID",groupID);
		params.put("devID",devID);
		r.setParams(params);
		return r;
	}
	public static Request ACG_ADD_GROUPS(int id,String userID ,List<String> list){
		Request r = new Request(id, "ACG_ADD_GROUPS");
		JsonObject params = new JsonObject();
		params.put("userID",userID);
		params.put("groupIDs", new JsonArray(list));
		r.setParams(params);
		return r;
	}
	public static Request ACG_GET_GROUPS(int id,String userId, Integer status){
		Request r = new Request(id, "ACG_GET_GROUPS");
		JsonObject params = new JsonObject();
		params.put("userID",userId);
		params.put("status",status);
		r.setParams(params);
		return r;
	}
	public static Request ACG_UPDATE_STATUS(int id, String userId, String groupId, int status){
		Request r = new Request(id, "ACG_UPDATE_STATUS");
		JsonObject params = new JsonObject();
		params.put("userID",userId);
		params.put("groupID",groupId);
		params.put("status",status);
		r.setParams(params);
		return r;
	}

	public static Request ACG_DEL_GROUP(int i, String userID, String groupID) {

		Request r = new Request(i, "ACG_DEL_GROUP");
		JsonObject params = new JsonObject();
		params.put("userID",userID);
		params.put("groupID", groupID);
		r.setParams(params);
		return r;
	}

	public static Request ACC_DEV_GROUP_DELETE(int i, String test00001, String s) {

		Request r = new Request(i, "ACC_DEV_GROUP_DELETE");
		JsonObject params = new JsonObject();
		params.put("groupID",s);
		params.put("devID",test00001);
		r.setParams(params);
		return r;
	}

	public static Request ACC_DELETE_AVAIL_DEV(int i, String devID) {

		Request r = new Request(i, "ACC_DELETE_AVAIL_DEV");
		JsonObject params = new JsonObject();
		params.put("devID", devID);
		r.setParams(params);
		return r;
	}

	public static Request ADD_DEV_TO_BRANCH(int id,String devID, String groupID){
		Request r = new Request(id, "ADD_DEV_TO_BRANCH");
		JsonObject params = new JsonObject();
		params.put("groupID",groupID);
		params.put("devID",devID);
		r.setParams(params);
		return r;
	}

	public static Request GROUP_REFERENCE_ADD(int id,String g1, String g2){
		Request r = new Request(id, "GROUP_REFERENCE_ADD");
		JsonObject params = new JsonObject();
		params.put("groupID",g1);
		params.put("referenceGroupID",g2);
		r.setParams(params);
		return r;
	}

	public static Request GROUP_REFERENCE_DELETE(int id,String g1, String g2){
		Request r = new Request(id, "GROUP_REFERENCE_DELETE");
		JsonObject params = new JsonObject();
		params.put("groupID",g1);
		params.put("referenceGroupID",g2);
		r.setParams(params);
		return r;
	}

	public static Request EDIT_DEVICE_DESCRIPTION(int id, String devId, String description) {

		Request r = new Request(id, "EDIT_DEVICE_DESCRIPTION");
		JsonObject params = new JsonObject();
		params.put("deviceID",devId);
		params.put("description",description);
		r.setParams(params);
		return r;
	}

	public static Request GET_GROUPS_BY_PHONE_NO(int id, String phoneNo) {

		Request r = new Request(id, "GET_GROUPS_BY_PHONE_NO");
		JsonObject params = new JsonObject();
		params.put("phoneNo",phoneNo);
		r.setParams(params);
		return r;
	}

	public static Request GET_DEVICE_ADMIN_KEYS(int id, String devID) {

		Request r = new Request(id, "GET_DEVICE_ADMIN_KEYS");
		JsonObject params = new JsonObject();
		params.put("devID", devID);
		r.setParams(params);
		return r;
	}

	public static Request GET_DEVICE_ADDED_STATUS(int id, String devID, String groupID) {

		Request r = new Request(id, "GET_DEVICE_ADDED_STATUS");
		JsonObject params = new JsonObject();
		params.put("devID",devID);
		params.put("groupID",groupID);
		r.setParams(params);
		return r;
	}

	public static Request LOGIN_WITH_CODE_HASHED(int id, String login_id,
												 String code) {
		Request r = new Request(id, "LOGIN");
		JsonObject params = new JsonObject();
		params.put("login_id", login_id);
		String hashedPassword = EncoderHandler.SHA1(code).toUpperCase();
		params.put("hashed_pwd", hashedPassword);
		r.setParams(params);
		return r;
	}

	public static Request REG_THIRD_PARTY_ACCOUNT(int id, String dev_name,
												  String hashed_pwd, String usage) {
		String type = "THIRDPARTYDEVICE";
		Request r = new Request(id, "REG_DEV_TERMINAL");
		JsonObject params = new JsonObject();
		params.put("dev_name", dev_name);
		params.put("hashed_pwd", hashed_pwd);
		params.put("type", type);

		if(usage!=null)
			params.put("usage", usage);
		r.setParams(params);
		return r;
	}


	/*
{
    "zl_cloud": "1.0",
    "method": "REG_THIRD_DEV_TERMINAL",
    "params": {
        "devID": #,
        "subType": #,
        "productID": #,
        "thirdDevID": #,
        "gateway": #,
        "params": #,
        "usage": #
        "isProduct": #
    },
    "id": #
}
	*/
	public static Request REG_THIRD_DEV_TERMINAL(int id, String devID,
												 Integer subType, String productID,
												 String thirdDevID, String gateway,
												 JsonObject p, String usage,
												 int isProduct) {
		Request r = new Request(id, "REG_THIRD_DEV_TERMINAL");
		JsonObject params = new JsonObject();
		params.put("devID", devID);
		params.put("subType", subType);

		params.put("productID", productID);
		params.put("thirdDevID", thirdDevID);
		params.put("gateway", gateway);

		params.put("params", p);
		params.put("usage", usage);
		params.put("isProduct", new Integer(isProduct));
		r.setParams(params);
		return r;
	}

	public static Request  GUARD_GET_ADMIN_KEY_BY_PASS(int id, String guardID,
													   String password) {

		Request r = new Request(id, "GUARD_GET_ADMIN_KEY_BY_PASS");
		JsonObject params = new JsonObject();
		params.put("guardID", guardID);
		params.put("password", password);
		r.setParams(params);
		return r;

	}
	public static Request CREATE_INIT_DEVICE (int id, Integer type, String userName,String password) {
		Request r = new Request(id, "CREATE_INIT_DEVICE");
		JsonObject params = new JsonObject();
		params.put("type", type);
		params.put("userName", userName);
		params.put("password", password);
		r.setParams(params);
		return r;
	}

}
