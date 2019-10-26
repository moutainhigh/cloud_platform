package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

public class RequestBinaryResponseSession implements RequestResponseSessionInterface{
	ConnectionInfo from;
	ConnectionInfo to;
	String request;
	Buffer response;
	boolean isRelay;

	public RequestBinaryResponseSession(ConnectionInfo to,
										String request, Buffer response) {
		super();
		this.to = to;
		this.request = request;
		this.response = response;
		this.isRelay = false;
//		this.to.addRequestResponseSession(this);
	}

	public RequestBinaryResponseSession(ConnectionInfo to, String request, Buffer response, boolean isRelay) {
		this.to = to;
		this.request = request;
		this.response = response;
		this.isRelay = isRelay;
//		this.to.addRequestResponseSession(this);
	}

	public RequestBinaryResponseSession(ConnectionInfo from, ConnectionInfo to, String request, Buffer response) {
		this.from = from;
		this.to = to;
		this.request = request;
		this.response = response;
		this.isRelay = true;
//		this.to.addRequestResponseSession(this);
	}

	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public Buffer getResponse() {
		return response;
	}
	public void setResponse(Buffer response) {
		this.response = response;
	}

	public boolean isRelay() {
		return isRelay;
	}

	public void setIsRelay(boolean isRelay) {
		this.isRelay = isRelay;
	}

	public ConnectionInfo getTo() {
		return to;
	}

	public void setTo(ConnectionInfo to) {
		this.to = to;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		if(from !=null) {
			sb.append("\t");
			sb.append(from.toJsonObject().toString());
			sb.append(",\n");
		}

		if(to !=null) {
			sb.append("\t");
			sb.append(to.toJsonObject().toString());
			sb.append(",\n");
		}

		sb.append("\t");
		sb.append(request.toString());
		sb.append(",\n");

		if(this.isRelay()) {
			sb.append("\t");
			sb.append("RELAY");
			sb.append(",\n");
		}
		sb.append("\t");
		sb.append(Tools.bufferToPrettyByteStringForGate(response));
		sb.append("\n  }");
		return sb.toString();
	}

	public String toStringOld() {
		JsonObject session = new JsonObject();
		session.put("request", request);
		session.put("response", response);
		if(to !=null) {
			session.put("to", to.toJsonObject());
		}
		return session.toString();
	}

}
