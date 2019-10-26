package cn.sensordb2.stcloud.ops.client;

import cn.sensordb2.stcloud.util.OpsServerIniUtil;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

/**
 * Created by sensordb on 16/8/5.
 */
public class WebSocketClient {
    private static int port = OpsServerIniUtil.getInstance().getServerPort();
    private static String host = OpsServerIniUtil.getInstance().getServerHostName();

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();
        HttpClientOptions options = new HttpClientOptions().setKeepAlive(false);
        HttpClient client = vertx.createHttpClient(options);

        host = "127.0.0.1";
        client.websocket(port, host, "/ops", websocket -> {
            System.out.println("Connected!");

            websocket.writeFinalTextFrame("hello");
        });

    }
}
