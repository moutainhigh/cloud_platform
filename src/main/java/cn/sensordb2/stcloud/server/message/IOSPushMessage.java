package cn.sensordb2.stcloud.server.message;

import cn.sensordb2.stcloud.util.IniUtil;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/*
{"platform":"all","audience":"all","notification":{"alert":"Hi,JPush !",
"android":{"extras":{"android-key1":"android-value1"}},
"ios":{"sound":"sound.caf","badge":"+1","extras":{"ios-key1":"ios-value1"}}}}
 */
public class IOSPushMessage extends JsonObject{

	public IOSPushMessage() {
		super();
	}

	/*

	ios
	push:
	{
		"notification" : {
			 "ios" : {
					 "alert" : "hello, JPush!",
					 "sound" : "sound.caf",
					 "badge" : 1,
					 "extras" : {
						  "news_id" : 134,
						  "my_key" : "a value"
					 }
				}
		 }
	}
	apns:
	{
		"_j_msgid" = 813843507;
		aps =     {
			alert = "hello,JPush!";
			badge = 1;
			sound = "sound.caf";
		};
		"my_key" = "a value";
		"news_id" = 134;
	}
	 */
	public IOSPushMessage(String aliasName) {
		super();
		this.put("platform", "ios");

		JsonArray aliasJsonArray = new JsonArray();
		aliasJsonArray.add(aliasName);
		JsonObject audience = new JsonObject();
		audience.put("alias", aliasJsonArray);
		this.put("audience", audience);

		JsonObject notification = new JsonObject();
		this.put("notification", notification);

		String alert = "Hi,JPush !";

		JsonObject ios = new JsonObject();
		String sound = "default";
		String badge = "+1";

		JsonObject extras = new JsonObject();
		JsonArray msgs = new JsonArray();
		extras.put("msgs", msgs);

		ios.put("sound", sound);
		ios.put("badge", badge);
		ios.put("extras", extras);
		ios.put("alert", alert);
		notification.put("ios", ios);

		/*
		  "message": {
        "msg_content": "Hi,JPush",
        "content_type": "text",
        "title": "msg",
        "extras": {
            "key": "value"
        }
    }
		 */
//		JsonObject message = new JsonObject();
//		this.put("message", message);
//		message.put("msg_content", "Hi,JPush");
//		message.put("content_type", "text");
//		message.put("title", "title");
//		message.put("extras", new JsonObject().put("key", "value"));

		/*
		"options": {
			"time_to_live": 60,
			"apns_production": false
		}
		*/

		JsonObject options = new JsonObject();
		this.put("options", options);
		options.put("time_to_live", IniUtil.getInstance().getTimeToLive());
		if(IniUtil.getInstance().getApnsProduction()!=0)
			options.put("apns_production", true);
		else options.put("apns_production", false);
	}

	public static IOSPushMessage createIOSPushMessageForPushID(String pushID, String alert) {
		IOSPushMessage pushMessage = new IOSPushMessage();
		pushMessage.put("platform", "ios");

		JsonArray pushIDJsonArray = new JsonArray();
		pushIDJsonArray.add(pushID);
		JsonObject audience = new JsonObject();
		audience.put("registration_id", pushIDJsonArray);
		pushMessage.put("audience", audience);

		JsonObject notification = new JsonObject();
		pushMessage.put("notification", notification);

		JsonObject ios = new JsonObject();
		String sound = "sound.caf";
		String badge = "+1";

		JsonObject extras = new JsonObject();
		JsonArray msgs = new JsonArray();
		extras.put("msgs", msgs);

		ios.put("sound", sound);
		ios.put("badge", badge);
		ios.put("extras", extras);
		ios.put("alert", alert);
		notification.put("ios", ios);

		/*
		"options": {
			"time_to_live": 60,
			"apns_production": false
		}
		*/
		JsonObject options = new JsonObject();
		pushMessage.put("options", options);
		options.put("time_to_live", IniUtil.getInstance().getTimeToLive());
		if(IniUtil.getInstance().getApnsProduction()!=0)
			options.put("apns_production", true);
		else options.put("apns_production", false);
		return pushMessage;
	}


	public static IOSPushMessage createIOSPushMessageForRingCall(String pushID, String alert) {
		IOSPushMessage pushMessage = new IOSPushMessage();
		pushMessage.put("platform", "ios");

		JsonArray pushIDJsonArray = new JsonArray();
		pushIDJsonArray.add(pushID);
		JsonObject audience = new JsonObject();
		audience.put("registration_id", pushIDJsonArray);
		pushMessage.put("audience", audience);

		JsonObject notification = new JsonObject();
		pushMessage.put("notification", notification);

		JsonObject ios = new JsonObject();
		String sound = "callsound.caf";
		String badge = "+1";

		JsonObject extras = new JsonObject();
		JsonArray msgs = new JsonArray();
		extras.put("msgs", msgs);

		ios.put("sound", sound);
		ios.put("badge", badge);
		ios.put("extras", extras);
		ios.put("alert", alert);
		notification.put("ios", ios);

		/*
		"options": {
			"time_to_live": 60,
			"apns_production": false
		}
		*/
		JsonObject options = new JsonObject();
		pushMessage.put("options", options);
		options.put("time_to_live", IniUtil.getInstance().getTimeToLive());
		if(IniUtil.getInstance().getApnsProduction()!=0)
			options.put("apns_production", true);
		else options.put("apns_production", false);
		return pushMessage;
	}


	public IOSPushMessage(String aliasName, String alert) {
		super();
		this.put("platform", "ios");

		JsonArray aliasJsonArray = new JsonArray();
		aliasJsonArray.add(aliasName);
		JsonObject audience = new JsonObject();
		audience.put("alias", aliasJsonArray);
		this.put("audience", audience);

		JsonObject notification = new JsonObject();
		this.put("notification", notification);

		JsonObject ios = new JsonObject();
		String sound = "sound.caf";
		String badge = "+1";

		JsonObject extras = new JsonObject();
		JsonArray msgs = new JsonArray();
		extras.put("msgs", msgs);

		ios.put("sound", sound);
		ios.put("badge", badge);
		ios.put("extras", extras);
		ios.put("alert", alert);
		notification.put("ios", ios);

		/*
		"options": {
			"time_to_live": 60,
			"apns_production": false
		}
		*/
		JsonObject options = new JsonObject();
		this.put("options", options);
		options.put("time_to_live", IniUtil.getInstance().getTimeToLive());
		if(IniUtil.getInstance().getApnsProduction()!=0)
			options.put("apns_production", true);
		else options.put("apns_production", false);

	}

	public JsonArray getMsgs() {
		JsonObject notification = this.getJsonObject("notification");
		if (notification == null) {
			return null;
		}
		JsonObject ios = notification.getJsonObject("ios");
		if (ios == null) {
			return null;
		}
		JsonObject extras = ios.getJsonObject("extras");
		if (extras == null) {
			return null;
		}
		return extras.getJsonArray("msgs");
	}

	public static void main(String[] args) {
		IOSPushMessage ioPushMessage = new IOSPushMessage("aliasName", "alert");
		System.out.println(ioPushMessage.toString());
		System.out.println(ioPushMessage.getMsgs().toString());
		ioPushMessage.getMsgs().add(new JsonObject().put("key", "value"));
		System.out.println(ioPushMessage.toString());
	}

}
