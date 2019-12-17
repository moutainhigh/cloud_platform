package cn.sensordb2.stcloud.rule.qos;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.ArrayList;
import java.util.List;

public class RecoveryQoS extends RequestHandler {

    //    public void handle(ConnectionInfo connectionInfo, Request request) throws
    //    InterruptedException {
//        Ros ros = RosInstance.getInstance().getRos();
//        JsonObject params = request.getParams();
//        System.err.println("fs;lkfhad;gh");
//        System.err.println(params);
//        JsonObject position = params.getJsonObject("position");
//        ArrayList<String> uavList = new ArrayList<>();
//        Database.getInstance().getMongoClient()
//                .find("AvailableUavs", new JsonObject().put("flag", "0"), res -> {
//                    if (res.succeeded()) {
//                        List<JsonObject> result = res.result();
//                        for (int i = 0; i < result.size(); i++) {
//                            uavList.add(result.get(i).getString("uav"));
//                        }
//                    }
//                    if (uavList.size() == 0) {
//                        request.setResponseSuccess(true);
//                        JsonObject result = new JsonObject();
//                        result.put("code", 1);
//                        result.put("message", "no uav available");
//                        ResponseHandlerHelper.success(connectionInfo, request, result);
//                        return;
//                    } else {
//                        String uavNo = uavList.get(0);
//                        Topic topic = new Topic(ros, "/"+ uavNo + "/command/pose",
//                                "geometry_msgs/PoseStamped");
//                        System.err.println(topic.getName());
//                        JsonObject jsonMsg = new JsonObject();
//                        jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
//                        jsonMsg.put("pose", new JsonObject()
//                                .put("position", new JsonObject().put("x", position.getDouble
//                                ("x"))
//                                        .put("y", position.getDouble("y"))
//                                        .put("z", position.getDouble("z"))));
//                        System.err.println(jsonMsg);
//                        topic.publish(new Message(jsonMsg.toString()));
//                        request.setResponseSuccess(true);
//                        JsonObject result = new JsonObject();
//                        result.put("code", 1);
//                        result.put("message", "qos recovery success");
//                        ResponseHandlerHelper.success(connectionInfo, request, result);
//                        return;
//                    }
//                });
//    }
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) throws InterruptedException {
        Ros ros = RosInstance.getInstance().getRos();
        JsonObject params = request.getParams();
        JsonArray array = params.getJsonArray("uavs");
        int uavNo = 0;
        for (int i = 0; i < array.size(); i++) {
            JsonObject jsonObject = array.getJsonObject(i);
            JsonObject positionI = jsonObject.getJsonObject("spos");
            JsonObject positionJ = jsonObject.getJsonObject("dpos");
            double x = positionI.getDouble("x") - positionJ.getDouble("x");
            double y = positionI.getDouble("y") - positionJ.getDouble("y");
            double z = positionI.getDouble("z") - positionJ.getDouble("z");
            double distance = Math.sqrt(x * x + y * y + z * z);
//            System.out.println(x);
//            System.out.println(y);
//            System.out.println(z);
//            System.out.println(distance+"111111111111111111");
            int num = (int) Math.floor(distance / 10) ;
            for (int j = 0; j < num; j++) {
                int uav = uavNo+ 1;
                Topic poseTopic = new Topic(ros, "/fireflyqos" + uav + "/command/pose",
                        "geometry_msgs/PoseStamped");
                JsonObject jsonMsg = new JsonObject();
                jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
                jsonMsg.put("pose", new JsonObject()
                        .put("position", new JsonObject()
                                .put("x", (positionI.getDouble("x"))-(x / (num + 1) * (j + 1)))
                                .put("y", (positionI.getDouble("y"))-(y / (num + 1) * (j + 1)))
                                .put("z", (positionI.getDouble("z"))-(z / (num + 1) * (j + 1)))));
                poseTopic.publish(new Message(jsonMsg.toString()));
//                System.out.println(poseTopic.getName());
//                System.out.println(jsonMsg.toString());
                uavNo = uav;
            }

        }

        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "Recovery QoS success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }

}
