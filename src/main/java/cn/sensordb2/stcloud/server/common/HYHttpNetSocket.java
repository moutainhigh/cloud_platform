package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.httpApiServer.HttpApiServer;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.HttpStatusCode;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by sensordb on 16/8/19.
 */
public class HYHttpNetSocket extends HYNetSocket {
    private static HYLogger logger = HYLogger.getLogger(HttpApiServer.class);
    private RoutingContext routingContext;

    public HYHttpNetSocket(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    @Override
    public void write(byte[] bytes) {
        HttpServerResponse response = routingContext.response();
//        response.putHeader("Content-Type", HttpHeaderContentType.TEXT_PLAIN);
        response.putHeader("Content-Type", "application/json;charset=utf-8");
        response.putHeader("Content-Length", String.valueOf(bytes.length));
        response.setStatusCode(HttpStatusCode.Status200);
        response.end(Buffer.buffer(bytes).toString());

//        response.close();
        //logger.info(Tools.bufferToPrettyByteString(Buffer.buffer(bytes)));
        //logger.info(Buffer.buffer(bytes).toString());
        //logger.info(response);

    }

    @Override
    public void close() {
    }

}
