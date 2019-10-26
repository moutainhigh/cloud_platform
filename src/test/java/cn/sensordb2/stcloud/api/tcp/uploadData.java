package cn.sensordb2.stcloud.api.tcp;

import cn.sensordb2.stcloud.client.SynTcpClient;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import io.vertx.core.json.JsonObject;

import java.util.Vector;

public class uploadData {
    public static void main(String[] args){
        Vector<Request> requests = new Vector<Request>();
        for(int i=0;i<1;i++){
            JsonObject jsonObject = new JsonObject().put("latitude",123.321).put("longitude",321.123);
            Request ping = RequestFactory.RELAY_MSG(315,jsonObject,"mlb");
            requests.add(ping);
        }
        SynTcpClient.connectNormalAndRequest(requests);
        Request request;
    }
}
