package cn.sensordb2.stcloud.server.message;

/*
 * msg_content: null
 */
public class UserConflictPushMessage extends PushMessage {

	public UserConflictPushMessage() {
		super("USER_CONFLICT_NOTIFY");
		this.setMsg_content(null);
	}
	
}
