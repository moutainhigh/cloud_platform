package cn.sensordb2.stcloud.rule.faultResponse;

import cn.sensordb2.stcloud.core.Database;
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
import io.vertx.ext.mongo.MongoClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPose extends RequestHandler {

    public static Map<String, String> fruavMap = new HashMap() {
    };

    static {
        fruavMap.put("fireflyfr1", "fireflyfr1");
        fruavMap.put("fireflyfr2", "fireflyfr2");
        fruavMap.put("fireflyfr3", "fireflyfr3");
    }


    @Override

    public void handle(ConnectionInfo connectionInfo, Request request) {
        Ros ros = RosInstance.getInstance().getRos();
        int uavNums = 4;

        String uavName = "firefly";
        String fruavName = "fireflyfr";
        JsonObject uavs = new JsonObject(); //key ： “1” ；“2”等字符数字
        JsonObject fruavs = new JsonObject();//同上
        JsonObject fireflyMap = request.getParams().getJsonObject("fireflyMap");
        for (int i = 1; i <= uavNums; i++) {
            int count = i;
            Topic topic = new Topic(ros,
                    "/" + (fireflyMap.containsKey(uavName + i) ? fireflyMap.getString(uavName + i)
                            : (uavName + i)) + "/ground_truth/pose",
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
        int i = 1;
        for (String frUavNums : fruavMap.keySet()) {
            int count = i;
            Topic topic = new Topic(ros, "/" + frUavNums + "/ground_truth/pose",
                    "geometry_msgs/Pose");
            topic.subscribe(new TopicCallback() {
                @Override
                public void handleMessage(Message message) {
                    String position = message.toJsonObject().getJsonObject("position")
                            .toString();
                    fruavs.put(String.valueOf(frUavNums.charAt(9)), new JsonObject(position));
                    topic.unsubscribe();
                }
            });
            i++;
        }

        boolean uavsflag = true;
        boolean frUavsFlag = true;
        while (uavsflag || frUavsFlag) {
            if (uavs.size() == uavNums) {
                uavsflag = false;
            }
            if (fruavs.size() == fruavMap.size()) {
                frUavsFlag = false;
            }
//            System.out.println(flag);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //从fruav中移除已被使用的飞机

        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askAllPose success");
        result.put("firefly", uavs);
        result.put("fruav", fruavs);
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}
