package cn.sensordb2.stcloud.server.common;

import io.vertx.core.AsyncResult;

/**
 * Created by sensordb on 16/12/10.
 */
public class DatabaseSaveResult extends DatabaseResult {
    AsyncResult<String> asyncResult;

    public DatabaseSaveResult(AsyncResult<String>asyncResult) {
        this.asyncResult = asyncResult;
        this.succeeded = asyncResult.succeeded();
    }

    public AsyncResult<String> getAsyncResult() {
        return asyncResult;
    }

    public Throwable cause() {
        return this.asyncResult.cause();
    }
}
