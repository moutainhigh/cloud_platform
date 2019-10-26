package cn.sensordb2.stcloud.server.message;

import java.util.HashMap;
import java.util.Map;

import cn.sensordb2.stcloud.util.HYLogger;

public class ResponseErrorMsg {
	private static HYLogger logger =  HYLogger.getLogger(ResponseErrorMsg.class);
	private static Map<Integer, String> msg = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(ResponseErrorCode.SUCCESS, "成功");
			put(ResponseErrorCode.DECODE_ERROR, "非json数据格式");
			put(ResponseErrorCode.INVALID_REQUEST, "无效请求");
			put(ResponseErrorCode.METHOD_NOT_FOUND, "方法不存在");
			put(ResponseErrorCode.INVALID_PARAMETERS, "无效的参数");
			put(ResponseErrorCode.CLIENT_NOT_LOGIN, "未登陆");
			put(ResponseErrorCode.SERVER_ERROR, "服务器繁忙");
			put(ResponseErrorCode.VERIFICATION_CODE_ERROR, "验证码错误");// 验证码错误
			put(ResponseErrorCode.USER_OR_PASSORD_ERROR, "用户名或密码错误");// 用户名或密码错误
			put(ResponseErrorCode.DEVICE_ALREDAY_ADDED, "该设备已经被其它用户添加");// 该设备已经被其他用户添加
			put(ResponseErrorCode.DEVICE_NOT_EXISTED, "设备不存在");// 设备不存在
			put(ResponseErrorCode.MANAGE_CONTROL_DEVICE_ILLEGAL, "非法使用设备");// 非法设备使用
			put(ResponseErrorCode.USER_NOT_EXISTED, "不存在该用户");// 不存在该用户
			put(ResponseErrorCode.ROUTE_ILLEGAL, "非法透传路由");// 非法透传路由
			put(ResponseErrorCode.INVALID_STATUS, "非法状态");// 非法状态
			put(ResponseErrorCode.NO_ROUTE, "路由不通");// 路由不通
			put(ResponseErrorCode.REQUEST_NOT_EXISTED, "请求不存在或过期");// 请求不存在或过期
			put(ResponseErrorCode.QUERY_DATE_TIME_ILLIGAL, "起始时间 和结束时间 查询条件非法"); // 起始时间和结束时间查询条件非法
			put(ResponseErrorCode.GET_VERIFICATION_CODE_TOO_FREQUENT, "获取验证码太频繁");
			put(ResponseErrorCode.SHORT_MESSAGE_ERROR, "短信服务异常");
			put(ResponseErrorCode.USER_EXISTED, "注册用户名已存在");
			put(ResponseErrorCode.INCOMPLETED_RING_CALL_SESSION, "未完成 的语音呼叫会话");// 门铃设备存在未完成的语音呼叫会话
			put(ResponseErrorCode.DEVICE_OR_USER_NOT_ONLINE, "不在线");// 设备或用户不在线
			put(ResponseErrorCode.RING_CALL_SESSION_NOT_EXISTED, "会话不存在"); // 源和目的之间尚未建立语音对讲会话
			put(ResponseErrorCode.NOT_AUTHORIED, "未授权");
			put(ResponseErrorCode.DATA_ERROR, "数据错误");
			put(ResponseErrorCode.ADMIN_PSWD_ERROR, "管理员密码不能被删除或冻结");
			put(ResponseErrorCode.EXISTED, "已存在或已添加");
			put(ResponseErrorCode.NOT_EXISTED, "不存在或无记录");
			put(ResponseErrorCode.TIME_OUT, "超时");

		}
	};

	public static String getErrorMsg(String code,String message){
		 try {
			return getErrorMsg(Integer.valueOf(code),message);
		} catch(Exception e) {
			logger.error(code + " can not convert to number");
			return "服务器繁忙";
		}
	}


	/**
	 * 未找到平台错误码对应信息，则返回原第三方平台的错误消息
	 * @param code
	 * @param message
	 * @return
	 */
	public static String getErrorMsg(int code,String message){
		if (msg.containsKey(code)){
			return  msg.get(code);
		}else{
			logger.info(code + " not found error message in errorMsg Map");
		}

		return message;
	}

	public static String getErrorMsg(int code){
		if (msg.containsKey(code)){
			return  msg.get(code);
		}else{
			logger.info(code + " not found error message in errorMsg Map");
		}

		return "未知错误";
	}

	public static int convertCode(IConvertErrCode convert,int code){
		return convert.convertErrCode(code);
	}

}
