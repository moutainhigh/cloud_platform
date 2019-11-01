package cn.sensordb2.stcloud.ros;

public class Pose {

    private Position position;
    private Quaternion orientation;

    public Pose() {
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
