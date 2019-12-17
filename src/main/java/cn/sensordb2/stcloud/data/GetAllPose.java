package cn.sensordb2.stcloud.data;

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
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.LinkedList;
import java.util.List;

public class GetAllPose extends RequestHandler {
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        Ros ros = RosInstance.getInstance().getRos();
        String[] uavNames = IniUtil.getInstance().getUavNames();

        JsonObject uavs = new JsonObject();
        List futures = new LinkedList();
        for (int i = 0; i < uavNames.length; i++) {
            Future future = Future.future();
            futures.add(future);
            final int index = i;
            Topic topic = new Topic(ros, "/" + uavNames[i] + "/ground_truth/pose",
                    "geometry_msgs/Pose");
            topic.subscribe(new TopicCallback() {
                @Override
                public void handleMessage(Message message) {
                    String position = message.toJsonObject().getJsonObject("position")
                            .toString();
                    uavs.put(uavNames[index], new JsonObject(position));
                    future.complete();
                    topic.unsubscribe();
                }
            });
        }

        CompositeFuture.all(futures).setHandler(res->{
            request.setResponseSuccess(true);
            JsonObject result = new JsonObject();
            result.put("code", 1);
            result.put("message", "askAllPose success");
            result.put("uavs", uavs);

            JsonObject allUavInfos = new JsonObject();
            for(String uavName : uavNames ) {
                JsonObject uavInfo = new JsonObject();
                allUavInfos.put(uavName, uavInfo);
            }
            result.put("allUavInfos", allUavInfos);

            ResponseHandlerHelper.success(connectionInfo, request, result);
        });
        return;
    }
}
