package cn.sensordb2.stcloud.control;

import cn.sensordb2.stcloud.ros.ImageUtil;
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
import java.util.UUID;

public class GetPicture extends RequestHandler {
    private String data = null;
    private String rootPath = "C:\\Users\\leey\\Downloads\\";
    private String ext = ".jpeg";
    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {

        Ros ros = RosInstance.getInstance().getRos();
        Topic cameraTopic = new Topic(ros, "/"+connectionInfo.getTo()+"/vi_sensor/camera_depth/camera/image_raw/compressed", "sensor_msgs/CompressedImage");
        cameraTopic.subscribe(new TopicCallback() {
            @Override
            public void handleMessage(Message message) {
               data = message.toJsonObject().toString();
                cameraTopic.unsubscribe();
            }
        });
        String filepath = rootPath + UUID.randomUUID().toString() + ext;
        JsonObject imgdata = new JsonObject(data);
        String imgBase64 = imgdata.getString("data");
        request.setResponseSuccess(true);
        JsonObject result = new JsonObject();
        result.put("code", 1);
        result.put("message", "askgetPic success");
        result.put("filePath", filepath);
        ResponseHandlerHelper.success(connectionInfo, request, result);
        new ImageUtil().GeneratePicFromBase64(imgBase64, filepath);
        return;
    }
}
