package cn.sensordb2.stcloud.server.common;

/**
 * Created by sensordb on 16/12/10.
 */
public abstract class DatabaseResult {
    boolean succeeded;

    public boolean succeeded() {
        return this.succeeded;
    }

    public boolean failed() {
        return !this.succeeded();
    }


    public abstract Throwable cause();
}
