package cn.sensordb2.stcloud.rule.swarmCreate;

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
import org.json.JSONObject;

public class attackEnemy extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) throws InterruptedException {
        Ros ros = RosInstance.getInstance().getRos();
        JsonObject params = request.getParams();
        JsonArray jsonArray = params.getJsonArray("uavs");
        for (int i = 0; i < jsonArray.size(); i++) {
            Topic topic = new Topic(ros, "/firefly"+ (i+1) + "/command/pose",
                    "geometry_msgs/PoseStamped");
            JsonObject position = jsonArray.getJsonObject(i);
            JsonObject jsonMsg = new JsonObject();
            jsonMsg.put("header", new JsonObject().put("frame_id", "world"));
            jsonMsg.put("pose", new JsonObject()
                    .put("position", new JsonObject().put("x", position.getDouble("x"))
                            .put("y", position.getDouble("y"))
                            .put("z", 10D)));
            topic.publish(new Message(jsonMsg.toString()));
        }
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "attack success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;

    }
}
