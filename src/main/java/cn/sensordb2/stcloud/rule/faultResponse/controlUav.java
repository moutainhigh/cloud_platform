package cn.sensordb2.stcloud.rule.faultResponse;

import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.Startup;
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
        String source = params.getString("source");
        Ros ros = RosInstance.getInstance().getRos();
        Topic poseTopic = new Topic(ros, "/fireflyfr" + uav + "/command/pose",
                "geometry_msgs/PoseStamped");
        JsonObject jsonMsg = new JsonObject();
        jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
        jsonMsg.put("pose", new JsonObject()
                .put("position", new JsonObject().put("x", position.getDouble("x"))
                        .put("y", position.getDouble("y"))
                        .put("z", position.getDouble("z")))
        .put("orientation", new JsonObject().put("x", 0D)
                .put("y", 0D)
                .put("z", 0D)
                .put("w", 0D)));
//        System.err.println(jsonMsg);
        poseTopic.publish(new Message(jsonMsg.toString()));
//        ConnectionInfo connectionInfo1 = ClientManager.getInstance()
//                .getConnectionInfo("firefly" + params.getString("source" )+ "_user");
//        connectionInfo1.setTo("firefly"+params.getString("source"));
        GetPose.fruavMap.remove("fireflyfr" + uav);
        //
        Startup.fireflyMap.put("firefly" + source, "fireflyfr" + uav);
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "controlUav success");
        result.put("rm", "fireflyfr" + uav);
        result.put("source", "firefly"+source);
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;

    }
}
