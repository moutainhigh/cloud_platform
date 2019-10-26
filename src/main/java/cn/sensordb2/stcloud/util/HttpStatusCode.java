package cn.sensordb2.stcloud.util;

/*
 * 
 */

public class HttpStatusCode {
	//服务器成功返回网页
	public static int Status200 = 200;
	//HTTP Status 201(已创建)
	public static int Status201 = 201;

	
	//400   （错误请求） 服务器不理解请求的语法。 
	public static int Status400 = 400;
	//401   （未授权） 请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。 
	public static int Status401 = 401;
	//403   （禁止） 服务器拒绝请求。 
	public static int Status403 = 403;
	//404   （未找到） 服务器找不到请求的网页。 
	public static int Status404 = 404;
	//405   （方法禁用） 禁用请求中指定的方法。 
	public static int Status405 = 405;
	
	//5xx（服务器错误） 
	//这些状态代码表示服务器在尝试处理请求时发生内部错误。 这些错误可能是服务器本身的错误，而不是请求出错。

	//500   （服务器内部错误）  服务器遇到错误，无法完成请求。 
	public static int Status500 = 500;
	//501   （尚未实施） 服务器不具备完成请求的功能。 例如，服务器无法识别请求方法时可能会返回此代码。 
	public static int Status501 = 501;
	//502   （错误网关） 服务器作为网关或代理，从上游服务器收到无效响应。 
	public static int Status502 = 502;
	//503   （服务不可用） 服务器目前无法使用（由于超载或停机维护）。 通常，这只是暂时状态。 
	public static int Status503 = 503;
	//504   （网关超时）  服务器作为网关或代理，但是没有及时从上游服务器收到请求。 
	public static int Status504 = 504;
	//505   （HTTP 版本不受支持） 服务器不支持请求中所用的 HTTP 协议版本。
	public static int Status505 = 505;
	
	public static String getCodeMessage(int code) {
		return "";
	}
	
	public static int getServerErrorCode() {
		return HttpStatusCode.Status500;
	}
	
	public static boolean isSuccessCode(int code) {
		return code>=200&&code<=201;
	}
	
	
}

