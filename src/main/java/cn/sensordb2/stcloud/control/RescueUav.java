package cn.sensordb2.stcloud.control;

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

public class RescueUav extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        JsonObject params = new JsonObject().put("method", request.getMethod())
                .put("params", request.getParams());
        Ros ros = RosInstance.getInstance().getRos();
        String uavName = "firefly"+params.getString("uavName");
        String position = params.getString("position");
        Topic poseTopic = new Topic(ros, "/"+uavName+"/command/pose", "geometry_msgs/PoseStamped");
        poseTopic.publish(new Message(position));
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "rescueUav success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
    }
}
