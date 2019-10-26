package cn.sensordb2.stcloud.server.message;

import io.vertx.core.json.JsonObject;

/*
 * msg_content: {"from":1#,"to":2#,"msg":3#}
 */
public class MsgPushMessage extends PushMessage {

	public MsgPushMessage() {
		super("MSG_ NOTIFY");
	}
	public MsgPushMessage(String type){
		super(type);
	}
	public void setMsg(JsonObject msg) {
		this.setMsg_content(msg);
	}
}
