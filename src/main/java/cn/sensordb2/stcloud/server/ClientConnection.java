package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.message.Response;

import io.vertx.core.net.NetSocket;

public class ClientConnection {
	private NetSocket netSocket;

	public ClientConnection(NetSocket netSocket) {
		super();
		this.netSocket = netSocket;
	}
	
	public void send(Response response) {
		this.netSocket.write(response.toString());
	}
	
}
