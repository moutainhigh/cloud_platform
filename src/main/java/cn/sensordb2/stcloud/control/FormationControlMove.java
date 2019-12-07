package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.ros.Pose;
import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.ros.RosPose;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonObject;
//todo
public class FormationControlMove extends RequestHandler {
    private Pose pose1;
    private Pose pose2;
    private Pose pose3;
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        JsonObject jsonObject = new JsonObject().put("method", request.getMethod())
                .put("params", request.getParams());
        Ros ros = RosInstance.getInstance().getRos();
        Topic leaderTopic = new Topic(ros, "/firefly1/command/pose", "geometry_msgs/PoseStamped");
        Topic follower2Topic = new Topic(ros, "/firefly2/command/pose", "geometry_msgs/PoseStamped");
        Topic follower3Topic = new Topic(ros, "/firefly3/command/pose", "geometry_msgs/PoseStamped");
        Topic leaderPoseTopic = new Topic(ros, "/firefly1/ground_truth/pose", "geometry_msgs/Pose");
        leaderPoseTopic.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                pose1 = new Pose(message);
            }
        });

    }
}
