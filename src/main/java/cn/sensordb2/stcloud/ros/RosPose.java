package cn.sensordb2.stcloud.ros;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;

public class RosPose {
    private Pose pose = null;

    public Pose getPose() {
        return pose;
    }

    public void setPose(Pose pose) {
        this.pose = pose;
    }

    public  void uavPose(String uavName) {

        Ros ros = RosInstance.getInstance().getRos();
        Topic topic = new Topic(ros, "/"+uavName+"/ground_truth/pose", "geometry_msgs/Pose");
        topic.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                pose = new Pose(message);
            }
        });
        try {
            while (pose == null) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
