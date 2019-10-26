package cn.sensordb2.stcloud.client;

public class ServerReponseDispatcher {
	private static ServerReponseDispatcher instance;
	private static int count = 0;
	
	private ServerReponseDispatcher() {	
	}
	
	
	public static ServerReponseDispatcher getInstance() {
		if(instance==null) instance = new ServerReponseDispatcher();
		return instance;
	}

	public void dispatcher(String response) {
		this.dispatcher(response, "");
	}

	public void dispatcher(String response, String appUserName) {
		System.out.println(++count);
		System.out.println(String.format("[%s]:%s", appUserName, response));
	}
}
