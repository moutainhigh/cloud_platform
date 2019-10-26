package cn.sensordb2.stcloud.server;

import io.vertx.core.json.JsonObject;

import java.util.Vector;

/**
 * Created by sensordb on 16/7/18.
 */
public class RequestResponseSessionList {
    private final static int MAX_SIZE = 10;
    int topIndex = -1;
    Vector requestResponseSessions = new Vector<RequestResponseSessionInterface>(MAX_SIZE);

    public RequestResponseSessionList() {
        for (int k = 0; k < MAX_SIZE; k++) {
            this.requestResponseSessions.add(null);
        }
    }

    public void add(RequestResponseSessionInterface requestResponseSession) {
        topIndex = (topIndex+1)%MAX_SIZE;
        this.requestResponseSessions.setElementAt(requestResponseSession, topIndex);
    }

    public JsonObject toJsonObject() {
        JsonObject result = new JsonObject();
        int index;
        for (int k = 0; k < MAX_SIZE; k++) {
            index = topIndex - k;
            if (index < 0) {
                index = index + MAX_SIZE;
            }
            if (this.requestResponseSessions.elementAt(index)!=null) {
                result.put(String.valueOf(k), this.requestResponseSessions.elementAt(index).toString());
            }
            else break;
        }
        return result;
    }
}
