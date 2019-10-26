package cn.sensordb2.stcloud.server;

import io.vertx.core.json.JsonObject;

public class RequestResponseSession implements RequestResponseSessionInterface{
	ConnectionInfo from;
	ConnectionInfo connectionInfo;
	String request;
	JsonObject response;
	boolean writeError = true;
	
	public RequestResponseSession(ConnectionInfo connectionInfo,
			String request, JsonObject response) {
		super();
		this.connectionInfo = connectionInfo;
		this.request = request;
		this.response = response;
		this.connectionInfo.addRequestResponseSession(this);
	}

	public RequestResponseSession(ConnectionInfo from, ConnectionInfo connectionInfo,
			String request, JsonObject response) {
		super();
		this.from = from;
		this.connectionInfo = connectionInfo;
		this.request = request;
		this.response = response;
		this.connectionInfo.addRequestResponseSession(this);
	}


	public RequestResponseSession(ConnectionInfo connectionInfo,
								  String request, JsonObject response, boolean writeError) {
		super();
		this.connectionInfo = connectionInfo;
		this.request = request;
		this.response = response;
		this.writeError = writeError;
		this.connectionInfo.addRequestResponseSession(this);
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
		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		if(from!=null) {
			sb.append("\t");
			sb.append(from.toJsonObject().toString());
			sb.append(",\n");
		}
		
		if(connectionInfo!=null) {
			sb.append("\t");
			sb.append(connectionInfo.toJsonObject().toString());
			sb.append(",\n");
		}

		sb.append("\t");
		if (request == null) {
			sb.append("request:null");
		}
		else {
			sb.append(request.toString());
		}
		sb.append(",\n");
		
		sb.append("\t");
		sb.append(response.toString());
		sb.append(",\n");

		if(!this.writeError) {
			sb.append("\t");
			sb.append("\"writeError\":"+this.writeError+",");
		}
		sb.append("\n  }");
		return sb.toString();
	}

	public String toStringOld() {
		JsonObject session = new JsonObject();
		session.put("request", request);
		session.put("response", response);
		if(connectionInfo!=null) {
			session.put("to", connectionInfo.toJsonObject());
		}
		return session.toString();
	}

}
