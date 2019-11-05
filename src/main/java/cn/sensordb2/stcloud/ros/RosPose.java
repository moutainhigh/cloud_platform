package cn.sensordb2.stcloud.ros;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;

public class RosPose {

    public static Pose POSE;

    public static void getPose() {

        Ros ros = RosInstance.getInstance().getRos();
        Topic topic = new Topic(ros, "/firefly/ground_truth/pose", "geometry_msgs/Pose");
        topic.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                POSE = new Pose(message);
            }
        });
    }
}
