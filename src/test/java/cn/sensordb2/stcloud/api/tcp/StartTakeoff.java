package cn.sensordb2.stcloud.api.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class StartTakeoff {
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        NetClient client = vertx.createNetClient();
        client.connect(9000, "127.0.0.1", res -> {
            if (res.succeeded()) {
                System.out.println("Connected!");
                Socket socket = null;
                try {
                    socket = new Socket("127.0.0.1",9000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                OutputStream outputStream = null;
                try {

                    outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonObject param = new JsonObject();
                param.put("version",1);
                param.put("method","control.StartTakeoff");
                param.put("token",1);
                param.put("to","cgy");
                param.put("id",1);
                String tempStr = param.toString();
                try {
                    outputStream.write(tempStr.getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.flush();//数据发送
                    outputStream.close();
                } catch (IOException  e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Failed to connect: " + res.cause().getMessage());
            }
        });
    }
}
