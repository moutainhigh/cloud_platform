package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.ros.Quaternion;
import cn.sensordb2.stcloud.ros.QuaternionUtil;
import cn.sensordb2.stcloud.ros.RosClock;
import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.ros.RosPose;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonObject;

//test pass
public class MoveToOnePose extends RequestHandler {


        @Override
        public void handle (ConnectionInfo connectionInfo, Request request){
            JsonObject jsonObject = new JsonObject().put("method", request.getMethod())
                    .put("params", request.getParams());
            Ros ros = RosInstance.getInstance().getRos();
            Topic poseTopic = new Topic(ros, "/firefly/command/pose", "geometry_msgs/PoseStamped");
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
            jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
            jsonMsg.put("pose", new JsonObject()
                    .put("position", new JsonObject().put("x", jsonObject.getJsonObject("params").getDouble("x"))
                            .put("y", jsonObject.getJsonObject("params").getDouble("y"))
                            .put("z", jsonObject.getJsonObject("params").getDouble("z")))
                    .put("orientation",
                            new JsonObject().put("x", RosPose.POSE.getOrientation().getX())
                                    .put("y", RosPose.POSE.getOrientation().getY())
                                    .put("z", RosPose.POSE.getOrientation().getZ())
                                    .put("w", RosPose.POSE.getOrientation().getW())));
            poseTopic.publish(new Message(jsonMsg.toString()));
//        PushMessageUtil.pushMessage(connectionInfo, request, jsonObject, connectionInfo.getTo(),
//                "RELAY_MSG", res -> {
//                    JsonObject result = new JsonObject();
//                    result.put("code", 1);
//                    result.put("message", "askadjust success");
//                    ResponseHandlerHelper.success(connectionInfo, request, result);
//                });
//    }
            request.setResponseSuccess(true);
            JsonObject result = new JsonObject();
            result.put("code", 1);
            result.put("message", "askadjust success");
            ResponseHandlerHelper.success(connectionInfo, request, result);
            return;
        }
    }

