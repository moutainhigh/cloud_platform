package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
//test pass!
public class SetWaypointMission extends RequestHandler {

    private Double times = 5D;
    private Double startTime = 0D;

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        JsonObject jsonObject = new JsonObject().put("method", request.getMethod())
                .put("params", request.getParams());
        JsonArray waypointList = jsonObject.getJsonObject("params").getJsonArray("waypointList");
        JsonObject waypoint = new JsonObject();
        waypoint.put("header", new JsonObject().put("frame_id", "world"))
                .put("joint_names", new JsonArray().add("base_link"));
        JsonArray points = new JsonArray();
        for (int i = 0; i < waypointList.size(); i++) {
            JsonObject position = waypointList.getJsonObject(i);
            points.add(new JsonObject().put("transforms", new JsonArray()
                    .add(new JsonObject().put("translation",
                            new JsonObject().put("x", position.getDouble("x"))
                                    .put("y", position.getDouble("y"))
                                    .put("z", position.getDouble("z")))))
                    .put("time_from_start",
                            new JsonObject().put("secs", startTime + i * times).put("nsecs", 0)));
        }
        waypoint.put("points", points);
        Ros ros = RosInstance.getInstance().getRos();
        Topic topic = new Topic(ros, "/firefly/command/trajectory",
                "trajectory_msgs/MultiDOFJointTrajectory");
        topic.publish(new Message(waypoint.toString()));
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "setwaypoint success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}

