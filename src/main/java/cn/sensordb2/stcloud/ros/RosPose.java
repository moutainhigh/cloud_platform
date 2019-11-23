package cn.sensordb2.stcloud.ros;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;

public class RosPose {
    private String pose = null;

    public void setPose(String pose) {
        this.pose = pose;
    }

    public String getPose() {
        return pose;
    }

    public  void uavPose() {

        Ros ros = RosInstance.getInstance().getRos();
        Topic topic = new Topic(ros, "/firefly/ground_truth/pose", "geometry_msgs/Pose");
        topic.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                pose = message.toJsonObject().toString();
                topic.unsubscribe();
            }
        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
