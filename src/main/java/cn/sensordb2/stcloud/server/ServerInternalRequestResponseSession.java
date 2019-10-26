package cn.sensordb2.stcloud.server;

import io.vertx.core.json.JsonObject;

public class ServerInternalRequestResponseSession {
	ConnectionInfo connectionInfo;
	String request;
	JsonObject response;
	
	public ServerInternalRequestResponseSession(ConnectionInfo connectionInfo, 
			String request, JsonObject response) {
		super();
		this.connectionInfo = connectionInfo;
		this.request = request;
		this.response = response;
	}
	
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public JsonObject getResponse() {
		return response;
	}
	public void setResponse(JsonObject response) {
		this.response = response;
	}
	
	public ConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	public void setConnectionInfo(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	public String toString() {
		JsonObject session = new JsonObject();
		session.put("request", request);
		session.put("response", response);
		if(connectionInfo!=null) {
			session.put("to", connectionInfo.toJsonObject());
		}
		return "ServerInternal:"+session.toString();
	}
}
