package cn.sensordb2.stcloud.api.tcp;


import cn.sensordb2.stcloud.client.HYHttpClient;
import cn.sensordb2.stcloud.client.SynTcpClient;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import cn.sensordb2.stcloud.util.IniUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

public class Ping {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		IniUtil.changeMode(IniUtil.LOCAL_DEBUG_MODE);
//		IniUtil.changeMode(IniUtil.TEST_MODE);
//		IniUtil.changeMode(IniUtil.PRODUCT_MODE);

		Vector<Request> requests = new Vector<Request>();

		Request ping = RequestFactory.PING(1, "cgy", "654321");
		ping.getParams().put("test", "test");
		requests.add(ping);

		//TCP
		SynTcpClient.connectNormalAndRequest(requests);

		//HTTP
//		HYHttpClient.connectNormalAndRequest(requests);
	}
	
	public static void main(String[] args) {
		new Ping().test();
	}

}
