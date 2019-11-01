package cn.sensordb2.stcloud.ros;

public class Euler {

    private Double yaw;
    private Double pitch;
    private Double roll;

    public Euler(Double yaw, Double pitch, Double roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public Euler() {
    }

    public Double getYaw() {
        return yaw;
    }

    public void setYaw(Double yaw) {
        this.yaw = yaw;
    }

    public Double getPitch() {
        return pitch;
    }

    public void setPitch(Double pitch) {
        this.pitch = pitch;
    }

    public Double getRoll() {
        return roll;
    }

    public void setRoll(Double roll) {
        this.roll = roll;
    }
}
