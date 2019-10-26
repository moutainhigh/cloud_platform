package cn.sensordb2.stcloud.util;

import io.vertx.core.json.JsonObject;

import java.util.Date;

/**
 * Created by sensordb on 16/9/27.
 */
public class HttpSession {
    private Date begin;
    private Date end;
    private int debugID = -1;
    private String absoluteURI = "";
    private String responseUrl = "";
    private String headerUserName = "";

    public HttpSession() {
        begin = new Date();
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getDebugID() {
        return debugID;
    }

    public void setDebugID(int debugID) {
        this.debugID = debugID;
    }

    public String getAbsoluteURI() {
        return absoluteURI;
    }

    public void setAbsoluteURI(String absoluteURI) {
        this.absoluteURI = absoluteURI;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public String getHeaderUserName() {
        return headerUserName;
    }

    public void setHeaderUserName(String headerUserName) {
        this.headerUserName = headerUserName;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public JsonObject toJsonObject() {
        JsonObject result = new JsonObject();

        if (this.begin != null) {
            result.put("begin", Tools.dateToStrMs(begin));
        }

        if (this.end != null) {
            result.put("end", Tools.dateToStrMs(end));
        }

        if (this.begin != null && this.end != null) {
            long diff = this.end.getTime() - this.begin.getTime();
            result.put("time", diff);
        }

        result.put("debugID", debugID);
        result.put("absoluteURI", absoluteURI);
        result.put("responseUrl", responseUrl);
        result.put("headerUserName", headerUserName);
        return result;
    }

    public String toString() {
        return this.toJsonObject().toString();
   }
}
