package cn.sensordb2.stcloud.server.message;

import cn.sensordb2.stcloud.server.ResponseHandlerHelper;

import io.vertx.core.json.JsonObject;

/*
{
    "zl_cloud": "1.0",
    "result": null,
    "id": 2#
}
 */
public class SuccessResponse extends Response {
	int id;
	String method = null;
	String SUCCESS = "success";
	JsonObject result = null;
//	JsonObject loginResult = new JsonObject().put("code",1).put("message","登陆成功");
//	JsonObject elseResult = new JsonObject().put("code",1).put("message",SUCCESS);

	public SuccessResponse(int id) {
		super();
		this.id = id;
	}

	//要加入方法
	public SuccessResponse(int id, JsonObject result,String method) {
		super();
		this.id = id;
		this.result = result;
		this.method = method;
	}
	
	public JsonObject toJsonObject() {
		JsonObject r = new JsonObject();

		r.put("version", "1.0");
		r.put("id", id);
		r.put("method",method);
		r.put("result", result);
		return r;
	}
	
	public String toString() {
		return this.toJsonObject().toString();
	}
	
	public String toStringWithDelimit() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.toJsonObject().toString());
		sb.append(ResponseHandlerHelper.DELIMIT);
		return sb.toString();
	}


	public String toStringNoDelimit() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.toJsonObject().toString());
		return sb.toString();
	}


}
