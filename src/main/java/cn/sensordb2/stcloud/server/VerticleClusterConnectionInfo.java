package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.message.Response;
import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/7/13.
 */
public class VerticleClusterConnectionInfo extends  ConnectionInfo{
    private VerticleConnectionInfoRemote verticleConnectionInfoRemote;

    public VerticleClusterConnectionInfo(VerticleConnectionInfoRemote verticleConnectionInfoRemote) {
        this.verticleConnectionInfoRemote = verticleConnectionInfoRemote;
        this.connectionAddress = verticleConnectionInfoRemote.getVerticleConnectionAddress();
    }


    public void write(String s) {
        this.vertx.eventBus().send(connectionAddress, Buffer.buffer(s));

    }


    public void write(Response response) {
        this.vertx.eventBus().send(connectionAddress, Buffer.buffer(response.toStringWithDelimit()));

    }


    public void write(Buffer buffer) {
        this.vertx.eventBus().send(connectionAddress, buffer);
    }


}
