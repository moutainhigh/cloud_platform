package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.ros.Pose;
import cn.sensordb2.stcloud.ros.Position;
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
    public void handle(ConnectionInfo connectionInfo, Request request) {
        JsonObject jsonObject = new JsonObject().put("method", request.getMethod())
                .put("params", request.getParams());
        JsonObject params = request.getParams();
        Ros ros = RosInstance.getInstance().getRos();
        Topic poseTopic = new Topic(ros, "/" + connectionInfo.getTo() + "/command/pose",
                "geometry_msgs/PoseStamped");
        JsonObject jsonMsg = new JsonObject();


        jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
        jsonMsg.put("pose", new JsonObject()
                .put("position",
                        new JsonObject().put("x", jsonObject.getJsonObject("params").getDouble("x"))
                                .put("y", jsonObject.getJsonObject("params").getDouble("y"))
                                .put("z", jsonObject.getJsonObject("params").getDouble("z"))));
        poseTopic.publish(new Message(jsonMsg.toString()));
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "move success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}

