package cn.sensordb2.stcloud.rule.faultResponse;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.IniUtil;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.callback.TopicCallback;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;

public class GetPoseNew extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        Ros ros = RosInstance.getInstance().getRos();
        String[] uavNames = IniUtil.getInstance().getUavNames();
        int uavNums = 4;
        int frUavNums = 3;
        String uavName = "firefly";
        String fruavName = "fireflyfr";
        JsonObject uavs = new JsonObject(); //key ： “1” ；“2”等字符数字
        JsonObject fruavs = new JsonObject();//同上

        for (int i = 1; i <= uavNames.length; i++) {
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

        for (int i = 1; i <= frUavNums; i++) {
            int count = i;
            Topic topic = new Topic(ros, "/" + fruavName + i + "/ground_truth/pose",
                    "geometry_msgs/Pose");
            topic.subscribe(new TopicCallback() {
                @Override
                public void handleMessage(Message message) {
                    String position = message.toJsonObject().getJsonObject("position")
                            .toString();
                    fruavs.put(String.valueOf(count), new JsonObject(position));
                    topic.unsubscribe();
                }
            });
        }

        boolean uavsflag = true;
        boolean frUavsFlag = true;
        while (uavsflag || frUavsFlag) {
            if (uavs.size() == uavNums) {
                uavsflag = false;
            }
            if (fruavs.size() == frUavNums) {
                frUavsFlag = false;
            }
//            System.out.println(flag);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //从fruav中移除已被使用的飞机
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        mongoClient.find("usedFrList", new JsonObject(), res -> {
            if (res.succeeded()) {
                List<JsonObject> result = res.result();
                if (result.size() != 0) {
                    for (int i = 0; i < result.size(); i++) {
                        String usedFrNo = result.get(i).getString("usedFr");
                        fruavs.remove(usedFrNo);
                    }
                }
            }
        });
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
