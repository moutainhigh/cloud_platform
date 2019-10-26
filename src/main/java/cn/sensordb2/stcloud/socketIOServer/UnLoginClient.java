package cn.sensordb2.stcloud.socketIOServer;

import com.corundumstudio.socketio.SocketIOClient;

/**
 * Created by sensordb on 16/8/16.
 */
public class UnLoginClient {
    SocketIOClient socketIOClient;
    boolean isTemp;

    public UnLoginClient(SocketIOClient socketIOClient, boolean isTemp) {
        this.socketIOClient = socketIOClient;
        this.isTemp = isTemp;
    }

    public String toString() {
        String host = "UNKNOWN";
        int port = -1;

        String result;
        result = String.format("clientID:%d host:null port:null isTemp:%s", socketIOClient.hashCode(),
                new Boolean(isTemp).toString());
        return result;
    }
}