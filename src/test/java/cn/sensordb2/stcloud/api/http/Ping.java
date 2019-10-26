package cn.sensordb2.stcloud.api.http;


import cn.sensordb2.stcloud.client.HYHttpClient;
import cn.sensordb2.stcloud.httpApiServer.HttpApiServerIniUtil;
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
		HttpApiServerIniUtil.changeMode(IniUtil.LOCAL_DEBUG_MODE);
//		HttpApiServerIniUtil.changeMode(IniUtil.TEST_MODE);
//		HttpApiServerIniUtil.changeMode(IniUtil.PRODUCT_MODE);

		Vector<Request> requests = new Vector<Request>();
		Request ping = RequestFactory.PING(1, "userID_1a", "1");
		ping.getParams().put("test", "test");
		requests.add(ping);

		//HTTP
		HYHttpClient.connectNormalAndRequest(requests);
	}
	
	public static void main(String[] args) {
		new Ping().test();
	}

}
