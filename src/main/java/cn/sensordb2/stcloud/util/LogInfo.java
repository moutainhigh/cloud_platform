package cn.sensordb2.stcloud.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.Date;
import java.util.HashMap;

public class LogInfo {
	public final static String TYPE="type";
	
	public final static String httpApiServer = "httpApiServer";
	private RoutingContext routingContext;
	private String type;
	private String user;
	private String password;
	private HashMap<String,Object> requestParams = new HashMap<String,Object>();
	private String body;
	private String blackBox;
	
	public LogInfo(RoutingContext routingContext, String type, String user, String password, String body) {
		super();
		this.routingContext = routingContext; 
		this.type = type;
		this.user = user;
		this.password = password;
		this.body = body;
	}

	public LogInfo(RoutingContext routingContext, String type, String user, String password) {
		this(routingContext, type, user, password, null);
	}

	public LogInfo(RoutingContext routingContext, String type, String user) {
		this(routingContext, type, user, null, null);
	}

	public LogInfo(RoutingContext routingContext, String type) {
		this(routingContext, type, null, null, null);
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
	public HashMap<String, Object> getRequestParams() {
		return requestParams;
	}
	public void setRequestParams(HashMap<String, Object> requestParams) {
		this.requestParams = requestParams;
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

	public LogInfo setBlackBox(String blackBox) {
		this.blackBox = blackBox;
		return this;
	}

	public LogInfo put(String key, String value) {
		this.requestParams.put(key, value);
		return this;
	}
	
	public LogInfo put(String key, int value) {
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

		JsonObject heads = new JsonObject();
		jsonObject.put("heads", heads);
		for(String key: routingContext.request().headers().names()) {
			heads.put(key, routingContext.request().headers().get(key));
		}

		JsonObject httpParams = new JsonObject();
		jsonObject.put("httpParams", httpParams);
		for(String key: routingContext.request().params().names()) {
			httpParams.put(key, routingContext.request().params().get(key));
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
