package cn.sensordb2.stcloud.rule.swarmCreate;

import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonObject;

public class getEmPose extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request)  {
        Ros ros = RosInstance.getInstance().getRos();
        JsonObject params = request.getParams();
        String enemyUav = "firefly7";
        JsonObject ePose = new JsonObject();
        Topic topic1 = new Topic(ros, "/"+enemyUav+"/ground_truth/pose",
                "geometry_msgs/Pose");
        topic1.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                String position = message.toJsonObject().getJsonObject("position")
                        .toString();
                ePose.put("enemy", position);
                topic1.unsubscribe();
            }
        });
        while (ePose.size() != 1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askAllPose success");
        result.put("position", ePose);
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}
