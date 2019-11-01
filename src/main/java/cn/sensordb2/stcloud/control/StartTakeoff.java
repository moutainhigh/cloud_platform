package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.ros.RosClock;
import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.Message;
import io.vertx.core.json.JsonObject;

public class StartTakeoff extends RequestHandler {
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        JsonObject jsonObject = new JsonObject().put("method",request.getMethod());
        //获取ros连接
        Ros ros = RosInstance.getInstance().getRos();
//        坐标控制无人机移动   rostopic pub /firefly/command/pose geometry_msgs/PoseStamped
//        '{
//        header: {
//            stamp: now,
//                    frame_id: "world"
//        },
//        pose: {
//            position: {
//                x: -4,
//                        y: -5,
//                        z: 2
//            },
//            orientation: {
//                w: 1.0
//            }
//        }
//    }'
        Topic topic = new Topic(ros, "/firefly/command/pose", "geometry_msgs/PoseStamped");
        JsonObject json = new JsonObject();
        RosClock.getClock();
        while (RosClock.NOW == null) {
            json.put("header", new JsonObject().put("stamp", RosClock.NOW).put("frame_id", "world"));
        }

        Message toSend = new Message();

        PushMessageUtil.pushMessage(connectionInfo,request,jsonObject,connectionInfo.getTo(), "RELAY_MSG", res->{
            JsonObject result = new JsonObject();
            result.put("code",1);
            result.put("message","asktakeoff success");
            ResponseHandlerHelper.success(connectionInfo, request,result);
        });
    }
}
