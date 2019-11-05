package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.ros.RosClock;
import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.ros.RosPose;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

//import java.nio.Buffer;

public class AdjustAngle extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        JsonObject jsonObject = new JsonObject().put("method", request.getMethod())
                .put("params", request.getParams());
        Ros ros = RosInstance.getInstance().getRos();
        Topic poseTopic = new Topic(ros, "/firefly/command/pose", "geometry_msgs/PoseStamped");
        JsonObject jsonMsg = new JsonObject();
        RosClock.getClock();
        RosPose.getPose();
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
        jsonMsg.put("header", new JsonObject().put("stamp", RosClock.NOW).put("frame_id", "world"));
        jsonMsg.put("pose", new JsonObject()
                .put("position", new JsonObject().put("x", RosPose.POSE.getPosition().getX())
                        .put("y", RosPose.POSE.getPosition().getY())
                        .put("z", RosPose.POSE.getPosition().getZ()))
                .put("orientation", new JsonObject().put("x", RosPose.POSE.getOrientation().getX())
                        .put("y", RosPose.POSE.getOrientation().getY())
                        .put("z", RosPose.POSE.getOrientation().getZ())
                        .put("w", RosPose.POSE.getOrientation().getW())));
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
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askadjust success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}
