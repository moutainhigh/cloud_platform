package cn.sensordb2.stcloud.api.tcp;

import io.socket.client.IO;
import io.socket.client.Socket;

import io.socket.emitter.Emitter;
import io.socket.emitter.Emitter.Listener;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;

public class iosocket {

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
//            {
//                "version": 1,
//                    "method": "user.Login",
//                    "params": {
//                "username": "firefly",
//                        "hashedPassword": "111111"
//            },
//                "id": "1"
//            }
            socket.on("response", new Listener() {

                @Override
                public void call(Object... objects) {
                    System.out.println(new String((byte[])objects[0]));
                }
            });

            socket.on(Socket.EVENT_CONNECTING, objects -> System.out.println("client: " + "连接中"));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT,
                    objects -> System.out.println("client: " + "连接超时"));
            socket.on(Socket.EVENT_CONNECT_ERROR,
                    objects -> System.out.println("client: " + "连接失败"));
            socket.connect();
//            //login test
//            JsonObject obj = new JsonObject();
//            obj.put("version", 1);
//            obj.put("method", "user.Login").put("params",
//                    new JsonObject().put("username", "firefly")
//                            .put("hashedPassword", "111111")).put("id", 1);
//
//            socket.emit("request", obj.toString());
//            Thread.sleep(1000);
//            System.out.println("starttakeoffrequest");
////            takeoff test
//            JsonObject obj1 = new JsonObject();
//            obj1.put("version", 1);
//            obj1.put("method", "control.StartTakeoff").put("params",
//                    new JsonObject().put("height", 10)).put("id", 2);
//            System.out.println("hello");
//            socket.emit("request", obj1.toString());
//            //landing test
//            JsonObject obj1 = new JsonObject();
//            obj1.put("version", 1);
//            obj1.put("method", "control.StartLanding").put("id", 2);
//            System.out.println("control.StartLanding");
//            socket.emit("request", obj1.toString());
            //getPic test
            JsonObject obj1 = new JsonObject();
            obj1.put("version", 1);
            obj1.put("method", "control.GetPicture").put("id", 2);
            System.out.println("control.StartLanding");
            socket.emit("request", obj1.toString());
                Thread.sleep(1000);



        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

