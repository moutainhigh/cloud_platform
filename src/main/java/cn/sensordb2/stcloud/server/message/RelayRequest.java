package cn.sensordb2.stcloud.server.message;

import io.vertx.core.json.JsonObject;

/**
 * Created by sensordb on 16/2/23.
 */
public class RelayRequest extends Request {
    private final static String METHOD = "RelayMsg";

    public RelayRequest(int id) {
        super(id, METHOD);
    }

    public void setOriginalMsg(JsonObject originalMsg) {
        this.getParams().put("original_msg", originalMsg);
    }

    public void setTo(String to) {
        this.getParams().put("to", to);
    }

    public void setChildID(String childID) {
        this.getParams().put("childID", childID);
    }

}
