package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.Future;

import java.util.ArrayList;

/**
 * Created by sensordb on 16/12/6.
 */
public class HYFuture{
    private static HYLogger logger = HYLogger.getLogger(HYFuture.class);

    public static void addFutureInfo(ArrayList futures, Future future,
                                 String message, ConnectionInfo connectionInfo) {
        futures.add(future);
        logger.info(String.format("add Future %s",message), connectionInfo);
    }

    public static void addFutureError(ArrayList futures, Future future,
                                 String message, ConnectionInfo connectionInfo) {
        futures.add(future);
        logger.error(String.format("add Future %s", message), connectionInfo);
    }

    public static void completeInfo(Future future, boolean success,
                                String message, ConnectionInfo connectionInfo) {
        future.complete(success);
        logger.info(String.format("Future complete %s",message), connectionInfo);
    }

    public static void completeError(Future future, boolean success,
                                String message, ConnectionInfo connectionInfo) {
        future.complete(success);
        logger.error(String.format("Future complete %s", message), connectionInfo);
    }

    public static void completeInfo(Future future,
                                String message, ConnectionInfo connectionInfo) {
        future.complete();
        logger.info(String.format("Future complete %s",message), connectionInfo);
    }

    public static void completeError(Future future,
                                String message, ConnectionInfo connectionInfo) {
        future.complete();
        logger.error(String.format("Future complete %s", message), connectionInfo);
    }
}
