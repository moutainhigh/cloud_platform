package cn.sensordb2.stcloud.socketio.data;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;

public class GetAllPose {
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
                    System.out.println(new String((byte[]) objects[0])+"111111111111");
                }
            });

            socket.on(Socket.EVENT_CONNECTING, objects -> System.out.println("client: " + "连接中"));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT,
                    objects -> System.out.println("client: " + "连接超时"));
            socket.on(Socket.EVENT_CONNECT_ERROR,
                    objects -> System.out.println("client: " + "连接失败"));
            socket.connect();

            JsonObject bind = new JsonObject();
            bind.put("version", 1);
            bind.put("method", "data.GetAllPose");
            bind.put("id", 2);
            System.out.println(bind.toString());
            socket.emit("request", bind.toString());
            Thread.sleep(1000);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

