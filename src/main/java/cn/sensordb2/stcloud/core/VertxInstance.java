package cn.sensordb2.stcloud.core;

import io.vertx.core.Vertx;

/**
 * Created by sensordb on 17/1/1.
 */
public class VertxInstance {
    private Vertx vertx;
    private static VertxInstance instance;

    private VertxInstance(Vertx vertx) {
        this.vertx = vertx;
    }

    public static void createInstance(Vertx vertx) {
        instance = new VertxInstance(vertx);
    }

    public static VertxInstance getInstance() {
        return instance;
    }

    public Vertx getVertx() {
        return vertx;
    }
}
