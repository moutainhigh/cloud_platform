package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.message.Request;

public abstract class RequestHandler {
	String name;

	public RequestHandler(String name) {
		super();
		this.name = name;
	}
	
	public RequestHandler() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public abstract void handle(ConnectionInfo connectionInfo, Request request)
            throws InterruptedException;

}
