package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;

public class ServerReponseDispatcherBinary {
	private static ServerReponseDispatcherBinary instance;
	private static int count = 0;

	private ServerReponseDispatcherBinary() {
	}
	
	
	public static ServerReponseDispatcherBinary getInstance() {
		if(instance==null) instance = new ServerReponseDispatcherBinary();
		return instance;
	}

	public void dispatcher(Buffer buffer) {
		this.dispatcher(buffer, "");
	}

	public void dispatcher(Buffer buffer, String appUserName) {
		System.out.println(++count);
		BinaryHeader binaryHeader = new BinaryHeader(buffer);

		System.out.println(String.format("[%s]:%s", appUserName, Tools.bufferToPrettyByteStringForGate(buffer)));
		System.out.println(String.format("[%s]:%s", appUserName, binaryHeader.toString()));
	}
}
