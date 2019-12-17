package cn.sensordb2.stcloud.socketio.control;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;

public class MoveToOnePoseForRuleTest {
    public static void main(String[] args) {
        String url = "http://localhost:29090";

        try {
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket"};
            //失败重试次数
            options.reconnectionAttempts = 10;
            //失败重连的时间间隔
            options.reconnectionDelay = 1000;
            //连接超时时间(ms)
            options.timeout = 500;
            final Socket socket = IO.socket(url, options);
            //监听自定义msg事件
            socket.on("msg",
                    objects -> System.out.println("client: 收到msg->" + Arrays.toString(objects)));
            //监听自定义订阅事件
            socket.on("sub", objects -> System.out
                    .println("client: " + "订阅成功，收到反馈->" + Arrays.toString(objects)));
            socket.on(Socket.EVENT_CONNECT, objects -> {
                socket.emit("sub", "我是訂閲對象");
                System.out.println("client: " + "连接成功");
            });
            socket.on("response", new Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println(new String((byte[]) objects[0]));
                }
            });

            socket.on(Socket.EVENT_CONNECTING, objects -> System.out.println("client: " + "连接中"));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT,
                    objects -> System.out.println("client: " + "连接超时"));
            socket.on(Socket.EVENT_CONNECT_ERROR,
                    objects -> System.out.println("client: " + "连接失败"));
            socket.connect();

            JsonObject params = new JsonObject();
            params.put("x", 0D).put("y", 0D).put("z", 5D);
            params.put("id", "firefly5");
            JsonObject request = new JsonObject();
            request.put("version", "1.0");
            request.put("method", "control.MoveToOnePoseForRule");
            request.put("params", params);
            request.put("id", 1);
            socket.emit("request", request);
            Thread.sleep(1000);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

