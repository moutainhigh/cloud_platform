package cn.sensordb2.stcloud.server.common;

import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by sensordb on 16/12/10.
 */
public class DatabaseQueryResult extends DatabaseResult{
    int resultSize = 0;
    AsyncResult<List<JsonObject>> asyncResult;

    public DatabaseQueryResult(AsyncResult<List<JsonObject>> asyncResult) {
        this.asyncResult = asyncResult;
        this.succeeded = asyncResult.succeeded();
        if(asyncResult.succeeded()) this.resultSize = asyncResult.result().size();
    }

    public int getResultSize() {
        return this.resultSize;
    }

    public AsyncResult<List<JsonObject>> getAsyncResult() {
        return asyncResult;
    }

    public Throwable cause() {
        return this.asyncResult.cause();
    }

    public List<JsonObject> result() {
        return this.asyncResult.result();
    }
}
