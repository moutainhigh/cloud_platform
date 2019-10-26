package cn.sensordb2.stcloud.server.message;

import io.vertx.core.json.JsonObject;

/*
 * msg_content: {"from":1#,"to":2#,"msg":3#}
 */
public class RelayPushMessage extends PushMessage {

	public RelayPushMessage() {
		super("RELAY_MSG");
	}
	
	public void setFrom(String from) {
		this.getMsg_content().put("from", from);
	}
	
	public void setTo(String to) {
		this.getMsg_content().put("to", to);
	}

	public void setMsg(JsonObject msg) {
		this.getMsg_content().put("msg", msg);
	}
}
