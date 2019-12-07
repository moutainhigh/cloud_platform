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

// test pass
public class StartLanding extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        JsonObject jsonObject = new JsonObject().put("method", request.getMethod())
                .put("params", request.getParams());
        Ros ros = RosInstance.getInstance().getRos();
        Topic poseTopic = new Topic(ros, "/"+connectionInfo.getTo()+"/command/pose", "geometry_msgs/PoseStamped");
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
        rosPose.uavPose(connectionInfo.getTo());
        Pose pose = rosPose.getPose();
        Position position = pose.getPosition();
        Quaternion orientation = pose.getOrientation();
        jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
        //无人机坐标设置为（x,y,0）
        jsonMsg.put("pose", new JsonObject()
                .put("position", new JsonObject().put("x", position.getX())
                        .put("y", position.getY())
                        .put("z", -10D))
                .put("orientation", new JsonObject().put("x", orientation.getX())
                        .put("y", orientation.getY())
                        .put("z", orientation.getZ())
                        .put("w", orientation.getW())));
        Message poseMsg = new Message(jsonMsg.toString());
        poseTopic.publish(poseMsg);
//        PushMessageUtil.pushMessage(connectionInfo, request, jsonObject, connectionInfo.getTo(),
//                "RELAY_MSG", res -> {
//                    JsonObject result = new JsonObject();
//                    result.put("code", 1);
//                    result.put("message", "askadjust success");
//                    ResponseHandlerHelper.success(connectionInfo, request, result);
//                });
//    }
//        //使得螺旋桨停转
//        Topic motorTopic = new Topic(ros, "/firefly/gazebo/command/motor_speed",
//        "mav_msgs/Actuators");
//
//        JsonObject motorsJson = new JsonObject();
//        motorsJson.put("header", new JsonObject().put("frame_id", ""))
//                .put("angles", new JsonArray()).put("angular_velocities",
//                new JsonArray().add(0D).add(0D).add(0D).add(0D).add(0D).add(0D))
//                .put("normalized", new JsonArray());
//        Message motorsMsg = new Message(motorsJson.toString());
//        motorTopic.publish(motorsMsg);
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askstartlanding success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}

