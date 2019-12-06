package cn.sensordb2.stcloud.api.tcp;

import cn.sensordb2.stcloud.ros.Pose;
import cn.sensordb2.stcloud.ros.RosInstance;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonObject;

public class RosTest {

    public static void main(String[] args) {
        RosInstance instance = RosInstance.getInstance();
        Ros ros = instance.getRos();
        ros.connect();
        ros.registerTopicCallback("/firefly5/command/pose", new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                System.out.println(message.toString());
            }
        });
        Topic topic = new Topic(ros, "/firefly2/command/pose", "geometry_msgs/PoseStamped");
        JsonObject msgJson = new JsonObject();
//        jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
//        jsonMsg.put("pose", new JsonObject()
//                .put("position", new JsonObject().put("x", position.getX())
//                        .put("y", position.getY())
//                        .put("z", height))
//                .put("orientation", new JsonObject().put("x", orientation.getX())
//                        .put("y", orientation.getY())
//                        .put("z", orientation.getZ())
//                        .put("w", orientation.getW())));
//
//
//
//        msgJson.put("header", new JsonObject().put("frame_id", "world"))
//                .put("pose", new JsonObject().put("position", new JsonObject().put("x", 10)
//                        .put("y", 10)
//                        .put("z", 10))
//                .put("orientation", new JsonObject().put("x", orientation.getX())
//                        .put("y", orientation.getY())
//                        .put("z", orientation.getZ())
//                        .put("w", orientation.getW())));
//        Message message = new Message();
    }
}
