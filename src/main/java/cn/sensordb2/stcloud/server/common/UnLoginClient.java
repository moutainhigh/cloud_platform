package cn.sensordb2.stcloud.server.common;

import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;

/**
 * Created by sensordb on 16/8/16.
 */
public class UnLoginClient {
    NetSocket netSocket;
    boolean isTemp;

    public UnLoginClient(NetSocket netSocket, boolean isTemp) {
        this.netSocket = netSocket;
        this.isTemp = isTemp;
    }

    public String toString() {
        SocketAddress socketAddress = netSocket.remoteAddress();

        String result;
        if(socketAddress!=null) {
            result = String.format("clientID:%d host:%s port:%d isTemp:%s", netSocket.hashCode(),
                    socketAddress.host(), socketAddress.port(),
                    new Boolean(isTemp).toString());
        }
        else {
            result = String.format("clientID:%d host:null port:null isTemp:%s", netSocket.hashCode(),
                    new Boolean(isTemp).toString());
        }
        return result;
    }
}
