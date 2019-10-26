package cn.sensordb2.stcloud.server.message;

/*
错误码	错误信息
-1	解析错误
-2	无效的请求
-3	方法未找到
-4	无效的参数
-5	终端未登陆
-6	服务器内部错误
-7 ~ -100	服务器端错误
-101	手机已绑定
-102	验证码错误
-103	用户名或密码错误
-104	该设备已经被其他用户添加
-105	设备不存在
-106	非法设备使用
-107	不存在该用户
-108	非法透传路由
-109	非法状态
-110	路由不通
-111	保存图片失败
-112	请求ID非法
-113	资源路径非法或不存在
-114	起始时间和结束时间查询条件非法
-115：	获取验证码太频繁
-116	短信服务异常
-117	注册用户名已存在
 */

public class ResponseErrorCode {
	public static final int SUCCESS = 0;
	public static final int DECODE_ERROR = -1;
	public static final int INVALID_REQUEST = -2;
	public static final int METHOD_NOT_FOUND = -3;
	public static final int INVALID_PARAMETERS = -4;
	public static final int CLIENT_NOT_LOGIN = -5;
	public static final int SERVER_ERROR = -6;
	
	public static final int VERIFICATION_CODE_ERROR = -102;//	验证码错误
	public static final int USER_OR_PASSORD_ERROR = -103;//	用户名或密码错误
	public static final int DEVICE_ALREDAY_ADDED = -104;//	该设备已经被其他用户添加
	public static final int DEVICE_NOT_EXISTED = -105;//	设备不存在
	public static final int MANAGE_CONTROL_DEVICE_ILLEGAL =-106;//	非法设备使用
	public static final int USER_NOT_EXISTED = -107;//	不存在该用户
	public static final int ROUTE_ILLEGAL = -108;//	非法透传路由
	public static final int INVALID_STATUS = -109;//	非法状态
	public static final int NO_ROUTE = -110;//	路由不通
	public static final int REQUEST_NOT_EXISTED = -112;//	请求不存在或过期
	public static final int QUERY_DATE_TIME_ILLIGAL = -114; //起始时间和结束时间查询条件非法
	public static final int GET_VERIFICATION_CODE_TOO_FREQUENT = -115;
	public static final int SHORT_MESSAGE_ERROR = -116;
	public static final int USER_EXISTED = -117;
	public static final int INCOMPLETED_RING_CALL_SESSION = -118;//	门铃设备存在未完成的语音呼叫会话
	public static final int DEVICE_OR_USER_NOT_ONLINE = -119;//	设备或用户不在线
	public static final int RING_CALL_SESSION_NOT_EXISTED = -120; //源和目的之间尚未建立语音对讲会话


    public static final int NOT_AUTHORIED = -121;
	public static final int DATA_ERROR = -122;


	public static final int UNKNOWN_ERROR = -123;//	

	public static final int ADMIN_PSWD_ERROR = -125;//管理员密码不能被 删除或冻结
	public static final int EXISTED = -126;//已存在或已添加 
	public static final int NOT_EXISTED = -127;//无记录
	public static final int TIME_OUT = -128;//超时

	public static final int THIRD_PARTY_ERRCODE = -129;//	未知错误，由第三方平台发来的错误码，未在本平台上有对应错误码，统一CODE


}
