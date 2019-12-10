package cn.sensordb2.stcloud.ros;

import edu.wpi.rail.jrosbridge.messages.Message;
import javax.json.JsonObject;

public class Pose {

    private Position position;
    private Quaternion orientation;

    public Pose() {
    }

    //    public RosTime(Message message) {
//
//        this.setSecs(message.toJsonObject().getJsonObject("clock").getJsonNumber("secs")
//                .doubleValue());
//        this.setNsecs(message.toJsonObject().getJsonObject("clock").getJsonNumber("nsecs")
//                .doubleValue());
//
    public Pose(Message message) {
        JsonObject position = message.toJsonObject().getJsonObject("position");
        JsonObject orientation = message.toJsonObject().getJsonObject("orientation");
        this.setPosition(new Position(position.getJsonNumber("x").doubleValue(),
                position.getJsonNumber("y").doubleValue(),
                position.getJsonNumber("z").doubleValue()));
        this.setOrientation(new Quaternion(orientation.getJsonNumber("x").doubleValue(),
                orientation.getJsonNumber("y").doubleValue(),
                orientation.getJsonNumber("z").doubleValue(),
                orientation.getJsonNumber("w").doubleValue()));
    }

    public Pose(Position position, Quaternion orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Quaternion getOrientation() {
        return orientation;
    }

    public void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
    }
}
