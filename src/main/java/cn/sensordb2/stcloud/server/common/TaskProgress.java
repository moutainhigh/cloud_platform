package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.json.JsonObject;

/**
 * Created by sensordb on 16/11/6.
 */
public class TaskProgress {
    private static HYLogger logger = HYLogger.getLogger(TaskProgress.class);
    int totalSize;
    int size = 0;

    //for debug
    String name;
    ConnectionInfo connectionInfo;

    public TaskProgress(int totalSize, String name, ConnectionInfo connectionInfo) {
        this.totalSize = totalSize;
        this.name = name;
        this.connectionInfo = connectionInfo;
        logger.info(String.format("create new task:%s", this.toJsonObject()), connectionInfo);
    }

    public TaskProgress(int totalSize) {
        this.totalSize = totalSize;
        this.name = "";
        logger.info(String.format("create new task:%s", this.toJsonObject()));
    }

    public TaskProgress(int totalSize, String name) {
        this.totalSize = totalSize;
        this.name = name;
        logger.info(String.format("create new task:%s", this.toJsonObject()), connectionInfo);

    }

    public void completeOne() {
        this.size++;
        if(this.connectionInfo!=null)
            logger.info(String.format("completeOne task:%s ", this.toJsonObject()), connectionInfo);
    }

    public boolean isComplete() {
        return this.size==this.totalSize;
    }

    public void decreaseTotalSize() {
        this.totalSize -= 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public JsonObject toJsonObject() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("name", name);
        jsonObject.put("totalSize", totalSize);
        jsonObject.put("size", size);
        return jsonObject;
    }
}
