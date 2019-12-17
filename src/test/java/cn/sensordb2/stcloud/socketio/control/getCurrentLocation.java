package cn.sensordb2.stcloud.socketio.control;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;

/**
 * @author: leey
 * @date: create on 2019/12/16 23:46
 * @description:
 */
public class getCurrentLocation {

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


            socket.on(Socket.EVENT_CONNECTING, objects -> System.out.println("client: " + "连接中"));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT,
                    objects -> System.out.println("client: " + "连接超时"));
            socket.on(Socket.EVENT_CONNECT_ERROR,
                    objects -> System.out.println("client: " + "连接失败"));
            socket.on("response", objects -> {
                        System.out.println(new String((byte[]) objects[0]));
                    }
            );
            socket.connect();
            //login test
            JsonObject login = new JsonObject();
            login.put("version", 1);
            login.put("method", "user.Login").put("params",
                    new JsonObject().put("username", "firefly2_user")
                            .put("hashedPassword", "111111")).put("id", 1);

            socket.emit("request", login.toString());


            //bind test
            //"droneName":"#"
            //"methodName":"#"
            JsonObject bind = new JsonObject();
            bind.put("version", "1.0");
            bind.put("method", "control.Bind").put("params",
                    new JsonObject().put("droneName", "firefly1")
                            .put("methodName", "Bind")).put("id", 1);
            socket.emit("request", bind.toString());

            JsonObject params = new JsonObject();
//            params.put("yaw", 0);
//            params.put("roll", 0);
//            params.put("pitch", 0);
            JsonObject request = new JsonObject();
            request.put("version", "1.0");
            request.put("method", "control.GetCurrentLocation");
            request.put("params", params);
            request.put("id", 1);
            socket.emit("request", request.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
