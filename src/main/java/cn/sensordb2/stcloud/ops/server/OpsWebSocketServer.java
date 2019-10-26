package cn.sensordb2.stcloud.ops.server;

import cn.sensordb2.stcloud.util.OpsServerIniUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by sensordb on 16/7/22.
 */
public class OpsWebSocketServer {
    private static String LOG_FILE = System.getenv("HY_HOME")==null?
            "./cloud/conf/log4jWebSocketServer.properties":System.getenv("HY_HOME")+"/cloud/conf/log4jWebSocketServer.properties";
    private static Logger logger = Logger.getLogger(OpsWebSocketServer.class);

    private static OpsWebSocketServer instance;
    private Vertx vertx;

    private OpsWebSocketServer(Vertx vertx) {
        this.vertx = vertx;
    }

    public static OpsWebSocketServer getInstance(Vertx vertx) {
        if (instance == null) {
            instance = new OpsWebSocketServer(vertx);
        }
        return instance;
    }

    public void run() {
        if(!OpsServerIniUtil.getInstance().isTurnOn()) {
            return;
        }

        HttpServerOptions options = new HttpServerOptions().setMaxWebsocketFrameSize(1000000);
        HttpServer server = vertx.createHttpServer(options);

        server.requestHandler(request -> {
            if (request.path().equals("/ops")) {

                ServerWebSocket websocket = request.upgrade();
                websocket.handler(buffer-> {
                    logger.info(Tools.bufferToPrettyByteStringForGate(buffer));
                });
                // Do something

                websocket.closeHandler(res -> {
                    logger.info(res.toString());

                });

                websocket.exceptionHandler(res-> {
                    logger.error(Tools.getTrace(res));
                });

            } else {
                // Reject
                request.response().setStatusCode(400).end();
            }
        });


        server.listen(OpsServerIniUtil.getInstance().getServerPort(), res->{
            if(res.failed()) {
                logger.error(String.format("create ops websocket server:%s", Tools.getTrace(res.cause())));
            }
            else {
                logger.info("create ops websocket server success");
            }
        });
    }

    public static void main(String[] args) {
        Tools.initSystemProperties();
        PropertyConfigurator.configure(LOG_FILE);

        Vertx vertx = Vertx.vertx();
        OpsWebSocketServer.getInstance(vertx).run();

    }
}
