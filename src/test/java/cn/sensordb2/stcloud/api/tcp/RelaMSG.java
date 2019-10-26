package cn.sensordb2.stcloud.api.tcp;


import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
{
    "zl_cloud": "1.0",
    "method": "common.relayMsg",
    "params": {
        "originalMsg": 1#，
		"to": 2#
		"childID": 3#
    },
    "id": 4#
}
 */
public class RelaMSG {
    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        NetClient client = vertx.createNetClient();
        client.connect(9000, "59.110.46.27", res -> {
            if (res.succeeded()) {
                System.out.println("Connected!");
                Socket socket = null;
                try {
                    socket = new Socket("59.110.46.27",9000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                OutputStream outputStream = null;
                try {

                    outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonObject obj = new JsonObject();
                JsonObject param = new JsonObject();
//                obj.put("time",1);
//                obj.put("planeagl",1);
//                obj.put("pitch",1);
//                obj.put("lfangle",);
                obj.put("action","start");
                Double collectionRate = 1.0;
                obj.put("collectionRate",collectionRate);
                param.put("params",obj);
                param.put("version",1);
                param.put("method","control.CollectData");
                param.put("token",1);
                param.put("to","cgy");
                param.put("id",1);
                String tempStr = param.toString();
                try {
                    outputStream.write(tempStr.getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.flush();//数据发送
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Failed to connect: " + res.cause().getMessage());
            }
        });



    }
}
