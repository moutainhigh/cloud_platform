package cn.sensordb2.stcloud.server.message;

import io.vertx.core.json.JsonObject;

public abstract class Response{
	public Response() {
		super();
	}

	public abstract JsonObject toJsonObject();
	public abstract String toStringWithDelimit();

	}
