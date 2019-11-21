package cn.sensordb2.stcloud.ros;

import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.std.Time;

public class RosClock {

    public static RosTime NOW;
    public static void getClock() {

        Ros ros = RosInstance.getInstance().getRos();
        Topic topic = new Topic(ros, "/clock", "rosgraph_msgs/Clock");
        topic.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                NOW = new RosTime(message);
                topic.unsubscribe();
            }
        });
        try {
            System.out.println(NOW);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
