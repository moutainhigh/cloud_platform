package cn.sensordb2.stcloud.server.message;

import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.server.common.Globals;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;


/*
{
    "zl_cloud": "1.0",
    "method": 1#,
    "params": 2#,
    "id": 3#
}
 */
public class Request extends JsonObject {
	BinaryHeader binaryHeader;
	long processBeginTime;
	long processEndTime;

	//do not need response
	boolean needResponse = true;
	//handle(request) return error
	boolean responseSuccess;
	//after this request has been handled, if the handler is not null,
	//it will be called by ResponseHandlerHelper.
	Handler<JsonObject> responseHandler;

	public Request(String request) throws Exception {
		super(request);
		//
		this.getID();
		this.getJsonObject("params");
//		if(this.containsKey(Globals.jsonVersionFieldName)&&this.containsKey("method"))
//			return;
		if (this.containsKey("method"))
			return;
		else {
			throw new Exception(request);
		}
	}

	public Request(int id, String method) {
		super();
		this.put(Globals.jsonVersionFieldName, Globals.jsonVersionFieldValue);
		this.setID(id);
		this.setMethod(method);
		this.setParams(new JsonObject());
	}

	public int getID() {
		return this.getInteger("id");
	}

	public void setID(int id) {
		this.put("id", id);
	}

	public String getMethod() {
		return this.getString("method");
	}

	public void setMethod(String method) {
		this.put("method", method);
	}

	public JsonObject getParams() {
		return this.getJsonObject("params");
	}

	public void setParams(JsonObject params) {
		this.put("params", params);
	}

	public String getVersion() {
		return this.getString(Globals.jsonVersionFieldName);
	}

	/*
	 */
	public JsonObject changeToNoCloudJsonObject() {
		JsonObject copy = super.copy();
		copy.remove(Globals.jsonVersionFieldName);
		return copy;
	}

	public JsonObject copy() {
		return (JsonObject) super.copy();
	}

	public BinaryHeader getBinaryHeader() {
		return binaryHeader;
	}

	public void setBinaryHeader(BinaryHeader binaryHeader) {
		this.binaryHeader = binaryHeader;
	}

	public long getProcessBeginTime() {
		return processBeginTime;
	}

	public void setProcessBeginTimeCurrentTime() {
		if (IniUtil.getInstance().isRecordCallMethodSpendTime())
			this.processBeginTime = System.currentTimeMillis();
	}

	public long getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTimeCurrentTime() {
		if (IniUtil.getInstance().isRecordCallMethodSpendTime())
			this.processEndTime = System.currentTimeMillis();
	}

	public long getProcessSpendTime() {
		return this.getProcessEndTime() - this.getProcessBeginTime();
	}

	public boolean isNeedResponse() {
		return needResponse;
	}

	public void setNeedResponse(boolean needResponse) {
		this.needResponse = needResponse;
	}

	public boolean isResponseSuccess() {
		return responseSuccess;
	}

	public void setResponseSuccess(boolean responseSuccess) {
		this.responseSuccess = responseSuccess;
	}

	public void setResponseHandler(Handler<JsonObject> responseHandler) {
		this.responseHandler = responseHandler;
	}

	public void handle(JsonObject response) {
		if (this.responseHandler != null) {
			this.responseHandler.handle(response);
		}
	}

	public String toStringWithDelimit() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.toString());
		sb.append(Globals.DELIMIT);
		return sb.toString();
	}
}
