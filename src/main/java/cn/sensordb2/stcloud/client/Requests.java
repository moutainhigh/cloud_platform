package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import io.vertx.core.json.JsonObject;

public class Requests {
	public static Request SET_TEMP_SVC_TYPE_REG() {
		int id = 1;
		return RequestFactory.SET_TEMP_SVC_TYPE_REG(id);
	}
	
	public static Request SET_TEMP_SVC_TYPE_FIND_PASSWORD() {
		int id = 1;
		return RequestFactory.SET_TEMP_SVC_TYPE_FIND_PASSWORD(id);
	}
	
	public static Request GET_ACCOUNT_INFO() {
		int id = 1;
		return RequestFactory.GET_ACCOUNT_INFO(id);
	}

	public static Request GET_VERIFICATION_CODE() {
		int id = 1;
		String phone_num = "18020136525";
		return RequestFactory.GET_VERIFICATION_CODE(id, phone_num);
	}
	
	public static Request GET_LOGIN_VERIFICATION_CODE() {
		int id = 1;
		String phone_num = "18020136525";
		return RequestFactory.GET_LOGIN_VERIFICATION_CODE(id, phone_num);
	}

	
	public static Request REG_USER_TERMINAL() {
		int id = 1;
		String verification_code = "888888";
		String hashed_pwd = "1";
		
		return RequestFactory.REG_USER_TERMINAL(id, verification_code, hashed_pwd);
	}

	public static Request LOGIN() {
		int id = 1;
		String login_id = "userID_1a";
		String hashed_pwd = "1";
		
		return RequestFactory.LOGIN(id, login_id, hashed_pwd);
	}

	public static Request LOGIN_BY_CODE() {
		int id = 1;
		String phone_num = "18020136525";
		String veritication_code = "888888";
		
		return RequestFactory.LOGIN_BY_CODE(id, phone_num, veritication_code);
	}

	public static Request CHANGE_PWD() {
		int id = 1;
		String old_hashed_pwd = "1";
		String new_hashed_pwd = "2";
		
		return RequestFactory.CHANGE_PWD(id, old_hashed_pwd, new_hashed_pwd);
	}


	public static Request RESET_PWD() {
		int id = 1;
		String verification_code = "888888";
		String new_hashed_pwd = "3";
		
		return RequestFactory.RESET_PWD(id, verification_code, new_hashed_pwd);
	}


	public static Request ADD_DEV_TO_USER() {
		int id = 1;
		String dev_id = "router_temp";
		boolean is_encrypted = false;
		double longitude = 10.9;
		double latitude = 11.8;
		
		return RequestFactory.ADD_DEV_TO_USER(id, dev_id, is_encrypted, longitude, latitude);
	}

	public static Request DEL_DEV_FROM_USER() {
		int id = 1;
		String dev_id = "router_temp";
		
		return RequestFactory.DEL_DEV_FROM_USER(id, dev_id);
	}


	public static Request SHARE_DEV() {
		int id = 1;
		String shared_dev_id = "router_1";
		String to_user_id = "userID_2a";
		
		return RequestFactory.SHARE_DEV(id, shared_dev_id, to_user_id);
	}


	public static Request CANCEL_SHARE_DEV() {
		int id = 1;
		String shared_dev_id = "router_1";
		String to_user_id = "userID_2a";
		
		return RequestFactory.CANCEL_SHARE_DEV(id, shared_dev_id, to_user_id);
	}

	public static Request GET_DEV_LIST() {
		int id = 1;
		
		return RequestFactory.GET_DEV_LIST(id);
	}

	public static Request RELAY_MSG() {
		int id = 1;
		JsonObject original_msg = new JsonObject();
		String to = "";
		
		return RequestFactory.RELAY_MSG(id, original_msg, to);
	}

	public static Request REG_DEV_TERMINAL() {
		int id = 1;
		String dev_name = "router_register";
		String hashed_pwd = "1";
		int type = 2;
		
		return RequestFactory.REG_DEV_TERMINAL(id, dev_name, hashed_pwd, type);
	}
	
	public static Request HEARTBEAT() {
		int id = 1;
		
		return RequestFactory.HEARTBEAT(id);
	}


	public static Request SET_NICKNAME() {
		int id = 1;
		String nickname = "nickNameTest";
		
		return RequestFactory.SET_NICKNAME(id, nickname);
	}
	
	public static Request SET_DEV_NICKNAME() {
		int id = 1;
		String dev_id = "router_1";
		String nickname = "nickNameTest";
		
		return RequestFactory.SET_DEV_NICKNAME(id, dev_id, nickname);
	}
	
	public static Request UPLOAD_USER_REPORT() {
		int id = 1;
		String report_text = "UPLOAD_USER_REPORT REPORT";
		
		return RequestFactory.UPLOAD_USER_REPORT(id, report_text);
	}

	public static Request UPLOAD_APP_EXCEPTION_REPORT() {
		int id = 1;
		String phone_type = "phone_type";
		String phone_version = "phone_version";
		String app_version = "app_version";
		String exception = "exception";
		
		return RequestFactory.UPLOAD_APP_EXCEPTION_REPORT(id, phone_type, phone_version, app_version, exception);
	}	

	public static Request GET_DEV_OWNER() {
		int id = 1;
		String dev_id = "router_1";
		
		return RequestFactory.GET_DEV_OWNER(id, dev_id);
	}
	
	public static Request RING_UPLOAD_IMAGE() {
		int id = 1;
		String type = "SNAPSHOT";
		String user_id = "userID_1a";
		int request_id = 1;
		String[] paths = {"1.jpg"};
		
		return RequestFactory.RING_UPLOAD_IMAGE(id, type, user_id, request_id, paths);
	}
	
	public static Request RING_UPLOAD_IMAGE_WARNING() {
		int id = 1;
		String[] paths = {"1.jpg"};
		
		return RequestFactory.RING_UPLOAD_IMAGE_WARNING(id, paths);
	}

	
	public static Request RING_CALL_REQUEST() {
		int id = 1;
		String[] paths = {"1.jpg"};
		
		return RequestFactory.RING_CALL_REQUEST(id, paths);
	}

	public static Request RING_CALL_ACCEPT() {
		int id = 1;
		String dev_id = "ring_1";
		int call_request_id = 1;
		
		return RequestFactory.RING_CALL_ACCEPT(id, dev_id, call_request_id);
	}
	

	public static Request RING_CALL_REJECT() {
		int id = 1;
		String dev_id = "ring_1";
		int call_request_id = 1;
		
		return RequestFactory.RING_CALL_REJECT(id, dev_id, call_request_id);
	}		
	
	public static Request RING_SEND_VOICE() {
		int id = 1;
		String to = "ring_1";
		String[] paths = {"1.jpg"};
		
		return RequestFactory.RING_SEND_VOICE(id, to, paths);
	}

	public static Request RING_SNAPSHOT() {
		int id = 1;
		String dev_id = "ring_1";
		
		return RequestFactory.RING_SNAPSHOT(id, dev_id);
	}		

	public static Request RING_GET_WARNING_INFO() {
		int id = 1;
		String dev_id = "ring_1";
		String start_time = "2013-10-20 00:00:00";
		String end_time = "2016-11-26 00:00:00";
		
		return RequestFactory.RING_GET_WARNING_INFO(id, dev_id, start_time, end_time);
	}		

	public static Request RING_GET_VISITOR_SESSION_INFO() {
		int id = 1;
		String dev_id = "ring_1";
		String start_time = "2013-10-20 00:00:00";
		String end_time = "2015-11-26 00:00:00";
		
		return RequestFactory.RING_GET_VISITOR_SESSION_INFO(id, dev_id, start_time, end_time);
	}		


	public static Request RING_CONFIG() {
		int id = 1;
		String dev_id = "ring_1";
		int human_check = 1; //	0，不开启人形检查 1，开启人形检查
		int lamp_switch = 1;	//	0，关闭门铃灯开关 1，打开门铃灯开关
		int warning_time = 5; //门铃告警时间，单位为秒，取值范围：[1,20]。注意：人形检查开启时，门铃告警时间有效，否则，门铃告警时间无效
		int cont_cap_num = 4; //连拍张数，取值范围：[1,5]
		int ring_tone = 3; //门铃铃音索引号，取值范围：[1,10]
		int ring_volumn = 4; //门铃音量等级，取值范围：[0,100]
		
		return RequestFactory.RING_CONFIG(id, dev_id, human_check, lamp_switch, warning_time, cont_cap_num, ring_tone, ring_volumn);
	}		

	public static Request RING_GET_CONFIG() {
		int id = 1;
		String dev_id = "ring_1";
		
		return RequestFactory.RING_GET_CONFIG(id, dev_id);
	}		

	public static Request RING_UPDATE_STATUS() {
		int id = 1;
		int battery = 50;
		
		return RequestFactory.RING_UPDATE_STATUS(id, battery);
	}		

	public static Request RING_GET_STATUS() {
		int id = 1;
		String dev_id = "ring_1";
		
		return RequestFactory.RING_GET_STATUS(id, dev_id);
	}		
}
