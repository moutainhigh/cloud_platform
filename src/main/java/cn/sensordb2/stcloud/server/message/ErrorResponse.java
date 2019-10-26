package cn.sensordb2.stcloud.server.message;

import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.Globals;
import io.vertx.core.json.JsonObject;

/*
{
    "zl_cloud": "1.0",
    "error": {
        "code": 1#,
        "message": 2#
    },
    "id": 3#
}
 */
public class ErrorResponse extends Response {
	int id;
	int code;
	String message = "";
	
	public ErrorResponse(int id, int code) {
		super();
		this.id = id;
		this.code = code;
	}

	public ErrorResponse(int id, int code, String message) {
		super();
		this.id = id;
		this.code = code;
		this.message = message;
	}

	public JsonObject toJsonObject() {
		JsonObject result = new JsonObject();
		result.put(Globals.jsonVersionFieldName, Globals.jsonVersionFieldValue);
		JsonObject error = new JsonObject();
		error.put("code", code);
		error.put("message", this.message);
		result.put("error", error);
		result.put("id", id);
		return result;
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

}
