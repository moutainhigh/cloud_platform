package cn.sensordb2.stcloud.control;

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

public class GetPicture extends RequestHandler {

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {

        Ros ros = RosInstance.getInstance().getRos();
        Topic cameraTopic = new Topic(ros, "/firefly/vi_sensor/camera_depth/camera/image_raw/compressed", "sensor_msgs/CompressedImage");
        cameraTopic.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
                System.out.println("11111111111111111");
                System.out.println(message.toJsonObject().toString());
                cameraTopic.unsubscribe();
            }
        });
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askgetPic success");
        ResponseHandlerHelper.success(connectionInfo, request, result);
        return;
    }
}
