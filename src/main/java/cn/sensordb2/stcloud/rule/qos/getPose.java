package cn.sensordb2.stcloud.rule.qos;

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

public class getPose extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) throws InterruptedException {
        Ros ros = RosInstance.getInstance().getRos();
//        System.err.println("2222222222222222222222222222222");
        int uavNums = 4;
        int qosUavNums = 3;
        String uavName = "firefly";
        String qosuavName = "fireflyqos";
        JsonObject uavs = new JsonObject(); //key ： “1” ；“2”等字符数字
        JsonObject qosuavs = new JsonObject();//同上
        //加入基站信息
        uavs.put("0", new JsonObject().put("x", 0).put("y", 0).put("z", 5));
        for (int i = 1; i <= uavNums; i++) {
            int count = i;
            Topic topic = new Topic(ros, "/" + uavName + i + "/ground_truth/pose",
                    "geometry_msgs/Pose");
            topic.subscribe(new TopicCallback() {
                @Override
                public void handleMessage(Message message) {
                    String position = message.toJsonObject().getJsonObject("position")
                            .toString();
                    uavs.put(String.valueOf(count), new JsonObject(position));
                    topic.unsubscribe();
                }
            });
        }

        for (int i = 0; i < qosUavNums; i++) {
            int count = i;
            Topic topic = new Topic(ros, "/" + qosuavName + i + "/ground_truth/pose",
                    "geometry_msgs/Pose");
            topic.subscribe(new TopicCallback() {
                @Override
                public void handleMessage(Message message) {
                    String position = message.toJsonObject().getJsonObject("position")
                            .toString();
                    qosuavs.put(String.valueOf(count), new JsonObject(position));
                    topic.unsubscribe();
                }
            });
        }

        boolean uavsflag = true;
        boolean qosUavsFlag = true;
        while (uavsflag || qosUavsFlag) {
            if (uavs.size() == (uavNums+1)) {
                uavsflag = false;
            }
            if (qosuavs.size() == qosUavNums) {
                qosUavsFlag = false;
            }
//            System.out.println(flag);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askAllPoseInQos Success");
        result.put("uavs", uavs);
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}
