package cn.sensordb2.stcloud.server.common;

import io.vertx.core.AsyncResult;
import io.vertx.ext.mongo.MongoClientDeleteResult;

/**
 * Created by sensordb on 16/12/10.
 */
public class DatabaseRemoveResult extends DatabaseResult{
    AsyncResult<MongoClientDeleteResult> asyncResult;

    public DatabaseRemoveResult(AsyncResult<MongoClientDeleteResult> asyncResult) {
        this.asyncResult = asyncResult;
        this.succeeded = asyncResult.succeeded();
    }

    public AsyncResult<MongoClientDeleteResult> getAsyncResult() {
        return asyncResult;
    }

    public Throwable cause() {
        return this.asyncResult.cause();
    }
}
