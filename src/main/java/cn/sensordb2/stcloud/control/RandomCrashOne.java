package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.ros.Pose;
import cn.sensordb2.stcloud.ros.Position;
import cn.sensordb2.stcloud.ros.Quaternion;
import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.ros.RosPose;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class RandomCrashOne extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        int uavNo = (int) ((Math.random() * 4) + 1);
        String uavName = "firefly" + uavNo;
        Ros ros = RosInstance.getInstance().getRos();
        Topic poseTopic = new Topic(ros, "/"+uavName+"/command/pose", "geometry_msgs/PoseStamped");
        JsonObject jsonMsg = new JsonObject();
//        '{
//        header: {
//            stamp: now,
//                    frame_id: "world"
//        },
//        pose: {
//            position: {
//                x: -4,
//                        y: -5,
//                        z: 2
//            },
//            orientation: {
//                w: 1.0
//            }
//        }
//    }'
        RosPose rosPose = new RosPose();
        rosPose.uavPose(uavName);
        Pose pose = rosPose.getPose();
        Position position = pose.getPosition();
        jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
        jsonMsg.put("pose", new JsonObject()
                .put("position", new JsonObject().put("x", position.getX())
                        .put("y", position.getY())
                        .put("z", -10D)));
        Message message = new Message(jsonMsg.toString());
        poseTopic.publish(message);
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "One uav crashed!");
        result.put("position", new JsonObject().put("x", position.getX())
                .put("y", position.getY())
                .put("z", position.getZ()));
        result.put("uavs", new JsonArray().add(5).add(6).add(7));
        ResponseHandlerHelper.success(connectionInfo, request, result);
    }
}
