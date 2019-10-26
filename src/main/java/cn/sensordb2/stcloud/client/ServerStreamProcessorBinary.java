package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.gate.BinaryProtocolUtil;
import cn.sensordb2.stcloud.server.gate.BufferSplitResult;
import io.vertx.core.buffer.Buffer;

import java.util.Vector;

public class ServerStreamProcessorBinary {
	private static ServerStreamProcessorBinary instance;
	private Buffer sb = Buffer.buffer();

	public static ServerStreamProcessorBinary getInstance() {
		if(instance==null) instance = new ServerStreamProcessorBinary();
		return instance;
	}

	private ServerStreamProcessorBinary() {
		
	}

	public void receive(Buffer buffer) {
		this.receive(buffer, "");
	}
	
	public void receive(Buffer buffer, String appUserName) {
		sb.appendBuffer(buffer);

		BufferSplitResult bufferSplitResult = BinaryProtocolUtil.splitBinary(sb);
		sb = bufferSplitResult.getLastBuffer();
		Vector<Buffer> buffers = bufferSplitResult.getTokens();
		for (Buffer b : buffers) {
			this.process(b, appUserName);
		}
	}
	
	/*
	 * connectionID==NetSocket.hashCode()
	 */
	protected void process(Buffer buffer, String appUserName) {
		ServerReponseDispatcherBinary.getInstance().dispatcher(buffer, appUserName);
	}
}
