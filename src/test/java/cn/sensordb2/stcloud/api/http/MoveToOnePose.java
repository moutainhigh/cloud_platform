package cn.sensordb2.stcloud.api.http;

import cn.sensordb2.stcloud.client.HYHttpClient;
import cn.sensordb2.stcloud.httpApiServer.HttpApiServerIniUtil;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import cn.sensordb2.stcloud.util.IniUtil;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;
import java.util.Vector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MoveToOnePose {

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
        Request r = new Request(1, "control.MoveToOnePose");
        JsonObject params = new JsonObject();
        params.put("id", "firefly1").put("x", 0D).put("y", 2D).put("z", 7D);
        r.setParams(params);
        requests.add(r);

        //HTTP
        HYHttpClient.connectNormalAndRequest(requests);
    }

    public static void main(String[] args) {
        new MoveToOnePose().test();
    }

}

