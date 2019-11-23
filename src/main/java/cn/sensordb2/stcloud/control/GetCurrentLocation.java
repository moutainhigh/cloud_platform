package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.ros.Pose;
import cn.sensordb2.stcloud.ros.Position;
import cn.sensordb2.stcloud.ros.Quaternion;
import cn.sensordb2.stcloud.ros.RosClock;
import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.ros.RosPose;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import io.vertx.core.json.JsonObject;

public class GetCurrentLocation extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
//        JsonObject jsonObject = new JsonObject().put("method",request.getMethod()).put("from",
//        ClientManager.getInstance().getLocalConnectionInfo(connectionInfo.getTo()).getFrom());
//        PushMessageUtil.pushMessage(connectionInfo,request,jsonObject,connectionInfo.getTo(),
//        "RELAY_MSG", res->{
//            JsonObject result = new JsonObject();
//            result.put("code",1);
//            result.put("message","askgetcurrentLocation success");
//            ResponseHandlerHelper.success(connectionInfo, request,result);
//        });
        JsonObject jsonObject = new JsonObject().put("method", request.getMethod())
                .put("params", request.getParams());
        RosPose rosPose = new RosPose();
        rosPose.uavPose(connectionInfo.getTo());
        Pose pose = rosPose.getPose();
        Position position = pose.getPosition();
        Quaternion orientation = pose.getOrientation();
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askgetcurrentLocation success");
        result.put("position", new JsonObject().put("latitude", position.getX())
                        .put("longitude", position.getY())
                        .put("height", position.getZ()));
        ResponseHandlerHelper.success(connectionInfo, request,result);
        return;
    }
}
