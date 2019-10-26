package cn.sensordb2.stcloud.client;

import io.vertx.core.buffer.Buffer;

public class ServerStreamProcessor {
	private static ServerStreamProcessor instance;
	private String delimit = "\r\n"; //token֮�g�ķָ���
	private StringBuffer sb = new StringBuffer();
	
	public static ServerStreamProcessor getInstance() {
		if(instance==null) instance = new ServerStreamProcessor();
		return instance;
	}
	
	private ServerStreamProcessor() {
		
	}

	public void receive(Buffer buffer) {
		this.receive(buffer, "");
	}
	
	public void receive(Buffer buffer, String appUserName) {
		String[] tokens;
		int validTokensNum;
		
		sb.append(buffer.toString());

		String str = sb.toString();
		tokens = str.split(delimit);
		validTokensNum = str.endsWith(delimit)?tokens.length:tokens.length-1;

		if(validTokensNum==tokens.length-1) {
			sb = new StringBuffer(tokens[tokens.length-1]);
		}
		else {
			sb = new StringBuffer();
		}

		for(int i=0;i<validTokensNum;i++) {
			this.process(tokens[i], appUserName);
		}
	}
	
	/*
	 * connectionID==NetSocket.hashCode()
	 */
	protected void process(String token, String appUserName) {
		ServerReponseDispatcher.getInstance().dispatcher(token, appUserName);
	}
}
