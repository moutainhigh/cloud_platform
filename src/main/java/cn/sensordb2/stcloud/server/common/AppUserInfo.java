package cn.sensordb2.stcloud.server.common;

import io.vertx.core.json.JsonObject;

/*
                     {
                        "user_id": 4#,
                        "nick_name": 5#
                    }
 
 */
public class AppUserInfo {
	private String userID;
	private String nickName;
	
	public AppUserInfo(String userID, String nickName) {
		super();
		this.userID = userID;
		this.nickName = nickName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public JsonObject toJsonObject() {
		JsonObject result = new JsonObject();
		result.put("user_id", this.userID);
		result.put("nick_name", this.nickName);		
		return result;
	}

}
