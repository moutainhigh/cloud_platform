package cn.sensordb2.stcloud.ros;

import edu.wpi.rail.jrosbridge.messages.Message;

public class RosTime {
    private  Double secs;
    private Double nsecs;

    public RosTime() {
    }

    public RosTime(Double secs, Double nsecs) {
        this.secs = secs;
        this.nsecs = nsecs;
    }

    public RosTime(Message message) {

        this.setSecs(message.toJsonObject().getJsonObject("clock").getJsonNumber("secs")
                .doubleValue());
        this.setNsecs(message.toJsonObject().getJsonObject("clock").getJsonNumber("nsecs")
                .doubleValue());
    }

    public Double getSecs() {
        return secs;
    }

    public void setSecs(Double secs) {
        this.secs = secs;
    }

    public Double getNsecs() {
        return nsecs;
    }

    public void setNsecs(Double nsecs) {
        this.nsecs = nsecs;
    }
}
