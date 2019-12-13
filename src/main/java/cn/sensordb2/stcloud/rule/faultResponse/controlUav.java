package cn.sensordb2.stcloud.rule.faultResponse;

import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonObject;

public class controlUav extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) throws InterruptedException {
        JsonObject params = request.getParams();
        String uav = params.getString("uavNum");
//        JsonObject position = new JsonObject(params.getString("position"));
        JsonObject position = params.getJsonObject("position");
        Ros ros = RosInstance.getInstance().getRos();
        Topic poseTopic = new Topic(ros, "/firefly" + uav + "/command/pose",
                "geometry_msgs/PoseStamped");
        JsonObject jsonMsg = new JsonObject();
        jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
        jsonMsg.put("pose", new JsonObject()
                .put("position", new JsonObject().put("x", position.getDouble("x"))
                        .put("y", position.getDouble("y"))
                        .put("z", position.getDouble("z"))));
        System.err.println(jsonMsg);
        poseTopic.publish(new Message(jsonMsg.toString()));
        connectionInfo.setTo("firefly" + uav);
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "controlUav success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}
