package cn.sensordb2.stcloud.rule.qos;

import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonObject;

public class getPose extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) throws InterruptedException {
        Ros ros = RosInstance.getInstance().getRos();
//        System.err.println("2222222222222222222222222222222");
        JsonObject uavs = new JsonObject();
        uavs.put("0", new JsonObject().put("x", 0).put("y", 0).put("z", 5));
        Topic topic1 = new Topic(ros, "/firefly1/ground_truth/pose",
                "geometry_msgs/Pose");

        topic1.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                String position = message.toJsonObject().getJsonObject("position")
                        .toString();
                uavs.put("1", new JsonObject(position));
                topic1.unsubscribe();
            }
        });
        Topic topic2 = new Topic(ros, "/firefly2/ground_truth/pose",
                "geometry_msgs/Pose");
        topic2.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                String position = message.toJsonObject().getJsonObject("position")
                        .toString();
                uavs.put("2", new JsonObject(position));
                topic2.unsubscribe();
            }
        });
        Topic topic3 = new Topic(ros, "/firefly3/ground_truth/pose",
                "geometry_msgs/Pose");
        topic3.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                String position = message.toJsonObject().getJsonObject("position")
                        .toString();
                uavs.put("3", new JsonObject(position));
                topic3.unsubscribe();
            }
        });
        Topic topic4 = new Topic(ros, "/firefly4/ground_truth/pose",
                "geometry_msgs/Pose");
        topic4.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                String position = message.toJsonObject().getJsonObject("position")
                        .toString();
                uavs.put("4", new JsonObject(position));
                topic4.unsubscribe();
            }
        });
        boolean flag = true;
        while (flag) {
            if (uavs.size() == 5) {
                flag = false;
            }
//            System.out.println(flag);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askAllPoseInQos Success");
        result.put("uavs", uavs);
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}
