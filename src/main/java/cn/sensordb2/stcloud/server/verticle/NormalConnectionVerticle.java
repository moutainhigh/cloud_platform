package cn.sensordb2.stcloud.server.verticle;

import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ConnectionShutDownReason;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;

/**
 * Created by sensordb on 16/3/23.
 */
public class NormalConnectionVerticle extends AbstractVerticle {
    public final static String ConnectionIDParamsKey = "connectionID";
    public static HYLogger logger = HYLogger.getLogger(NormalConnectionVerticle.class);
    private final String serverName = IniUtil.getInstance().getServerHostName();
    private final int port = IniUtil.getInstance().getServerPort();
    NetServer server;

    @Override
    public void start() throws Exception {
        super.start();
        server = vertx.createNetServer();
        server.connectHandler(socket -> {
            ClientManager.getInstance().addVerticleClient(socket);

            socket.exceptionHandler(v -> {
                //v.printStackTrace();
                //logger.error(Tools.getTrace(v), socket);
            });

            socket.closeHandler(v -> {
                int connectionID = socket.hashCode();
                ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(connectionID);
                if (ci != null) {
                    logger.info(String.format("Normal Client %s closed", ci.getUserID()), socket);
                }
                ClientManager.getInstance().shutDown(connectionID, ConnectionShutDownReason.ClientClose);

            });
        });
        server.listen(port, "0.0.0.0", res -> {
            if (res.succeeded()) {
                logger.info(String.format("NormalConnectionVerticle is now listening!"));
            } else {
                logger.error(String.format("NormalConnectionVerticle failed to bind!"));
                vertx.close();
            }
        });

    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

  }
