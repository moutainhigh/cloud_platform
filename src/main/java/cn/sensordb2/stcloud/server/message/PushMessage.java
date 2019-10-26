package cn.sensordb2.stcloud.server.message;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/*
{
    "zl_cloud": "1.0",
    "method": "push_msg",
    "msg_type": 1#,
    "msg_content": 2#
}
 */
public class PushMessage extends JsonObject{	
	public PushMessage(String msg_type) {
		super();
		this.put("version", "1");
		this.put("method", "pushMsg");
		JsonObject jsonObject = new JsonObject().put("msgType",msg_type).put("msgContent",new JsonObject());

		this.put("params", jsonObject);
		this.put("id", 0);
	}
	
	public JsonObject getMsg_content() {
		return this.getJsonObject("msgContent");
	}

	public void setMsg_content(JsonObject msg_content) {
		JsonObject jsonObject = new JsonObject().put("msgContent",msg_content);
		this.getJsonObject("params").put("msgContent",msg_content);
	}

	public void setFrom(String from){
		this.put("from",from);
	}

	public void setId(int id) {this.put("id",id); }
}
