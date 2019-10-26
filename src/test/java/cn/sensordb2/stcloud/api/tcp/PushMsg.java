package cn.sensordb2.stcloud.api.tcp;

import cn.sensordb2.stcloud.client.SynTcpClient;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.RequestFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class PushMsg {
    public static void main(String[] args){
//        Vertx vertx = Vertx.vertx();
//        NetClient client = vertx.createNetClient();
//        client.connect(9000, "127.0.0.1", res -> {
//            if (res.succeeded()) {
//                System.out.println("Connected!");
//                Socket socket = null;
//                try {
//                    socket = new Socket("127.0.0.1",9000);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                OutputStream outputStream = null;
//                try {
//
//                    outputStream = socket.getOutputStream();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                JsonObject obj = new JsonObject();
//                JsonObject param = new JsonObject();
//                obj.put("username","cgy");
//                obj.put("hashedPassword",654321);
//                param.put("params",obj);
//                param.put("version",1);
//                param.put("method","user.Login");
//                param.put("token",1);
//                param.put("id",1);
//                String tempStr = param.toString();
//                try {
//                    outputStream.write(tempStr.getBytes());
//                    outputStream.write("\r\n".getBytes());
//                    outputStream.flush();//数据发送
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                System.out.println("Failed to connect: " + res.cause().getMessage());
//            }
//        });
        Vector<Request> requests = new Vector<Request>();

        Request ping = RequestFactory.LOGIN(1, "cgy", "654321");
        ping.getParams().put("test", "test");
        Request ping1 = RequestFactory.PING(1, "cgy", "654321");
        ping.getParams().put("test", "test");
        requests.add(ping);
        requests.add(ping1);
        SynTcpClient.connectNormalAndRequest(requests);
    }
}
