package cn.sensordb2.stcloud.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Date;
import java.util.HashMap;

public class LogInfoResponse {
	public final static String TYPE="type";
	public final static String ERRORResponse403 = "ERRORResponse403";
	public final static String ERRORResponse500= "ERRORResponse500";
	public final static String ERRORResponse400= "ERRORResponse400";
	public final static String ERRORResponse404= "ERRORResponse404";
	public final static String Response200 = "Response200";
	public final static String Response201 = "Response201";
	public final static String ServerError = "ServerError";

	private RoutingContext routingContext;
	private String type;
	private String user;
	private String password;
	private HashMap<String,Object> requestParams = new HashMap<String,Object>();
	private String body;
	private String blackBox;

	public LogInfoResponse(RoutingContext routingContext, String type, String user, String password, String body) {
		super();
		this.routingContext = routingContext;
		this.type = type;
		this.user = user;
		this.password = password;
		this.body = body;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getBlackBox() {
		return blackBox;
	}

	public LogInfoResponse setBlackBox(String blackBox) {
		this.blackBox = blackBox;
		return this;
	}

	public LogInfoResponse put(String key, String value) {
		this.requestParams.put(key, value);
		return this;
	}
	
	public LogInfoResponse put(String key, int value) {
		this.requestParams.put(key, value);
		return this;
	}

	public String toString() {
		JsonObject jsonObject = new JsonObject();

		jsonObject.put("dateTime", Tools.dateToStrMs(new Date()));

		if(this.type!=null)
			jsonObject.put(TYPE, this.type);
		jsonObject.put("source", "Server");
		if(this.user!=null) {
			jsonObject.put("user", this.user);
		}
		else {
			jsonObject.put("user", this.routingContext.request().getParam("userName"));
		}
		
		if(this.password!=null) {
			jsonObject.put("password", this.password);
		}
		else {
			jsonObject.put("password", this.routingContext.request().getParam("hashedPassword"));			
		}
		
		if(this.body!=null) {
			jsonObject.put("body", this.body);
		}
		
		if(this.blackBox!=null) {
			jsonObject.put("blackBox", this.blackBox);
			
		}
	
		JsonObject params = new JsonObject();
		jsonObject.put("params", params);

		for(String key: requestParams.keySet()){
		   params.put(key, requestParams.get(key));
        }

		JsonObject responseHeads = new JsonObject();
		jsonObject.put("responseHeads", responseHeads);
		for(String key: routingContext.response().headers().names()) {
			responseHeads.put(key, routingContext.response().headers().get(key));
		}


		JsonObject httpParams = new JsonObject();
		jsonObject.put("httpParams", httpParams);
		for(String key: routingContext.request().params().names()) {
			httpParams.put(key, routingContext.request().params().get(key));
		}


		JsonObject heads = new JsonObject();
		jsonObject.put("heads", heads);
		for(String key: routingContext.request().headers().names()) {
			heads.put(key, routingContext.request().headers().get(key));
		}

		if(Debug.Debug) {
		   int id = Debug.getDebugSessionID(routingContext);
		   if(id!=-1) {
			   String sessionID = String.valueOf(id);
				jsonObject.put("absoluteURI", routingContext.data().get("absoluteURI"));
				jsonObject.put("query", routingContext.data().get("query"));
				jsonObject.put("uri", routingContext.data().get("uri"));
//				jsonObject.put("params", routingContext.data().get("params"));

			   return "*["+sessionID+"] "+jsonObject.toString();
		   }
		   else return "*[]"+jsonObject.toString();
	   }
	   else {
		   return "*[]"+jsonObject.toString();
	   }
	}
	
	
}
