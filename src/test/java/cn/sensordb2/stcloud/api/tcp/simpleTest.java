package cn.sensordb2.stcloud.api.tcp;

import cn.sensordb2.stcloud.ros.RosInstance;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class simpleTest {

    public static void main(String[] args) throws InterruptedException {
        Ros ros = RosInstance.getInstance().getRos();
        ros.connect();
        Topic motorTopic = new Topic(ros, "/firefly/command/motor_speed", "mav_msgs/Actuators");

        motorTopic.unadvertise();
        JsonObject motorsJson = new JsonObject();
        motorsJson.put("header", new JsonObject().put("frame_id", ""))
                .put("angles", new JsonArray())
                .put("angular_velocities",
                        new JsonArray().add(0D).add(0D).add(0D).add(0D).add(0D).add(0D))
                .put("normalized", new JsonArray());
        System.out.println(motorsJson.toString());
        Message motorsMsg = new Message(motorsJson.toString());
        while (true) {
            motorTopic.publish(motorsMsg);
            Thread.sleep(100);
        }

    }

}
