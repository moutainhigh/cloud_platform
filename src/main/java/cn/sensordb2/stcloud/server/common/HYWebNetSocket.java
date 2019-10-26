package cn.sensordb2.stcloud.server.common;

import com.corundumstudio.socketio.SocketIOClient;

/**
 * Created by sensordb on 16/8/19.
 */
public class HYWebNetSocket extends HYNetSocket {
    private SocketIOClient socketIOClient;

    public HYWebNetSocket(SocketIOClient socketIOClient) {
        this.socketIOClient = socketIOClient;
    }

    @Override
    public void write(byte[] bytes) {

        this.socketIOClient.sendEvent("response", bytes);
    }

    @Override
    public void close() {
        this.socketIOClient.disconnect();
    }

}
