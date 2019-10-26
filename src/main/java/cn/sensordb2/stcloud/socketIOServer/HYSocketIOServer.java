package cn.sensordb2.stcloud.socketIOServer;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ConnectionShutDownReason;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.json.JsonObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by sensordb on 16/8/19.
 */
public class HYSocketIOServer{
    private static HYLogger logger = HYLogger.getLogger(HYSocketIOServer.class);
    private static HYSocketIOServer instance;
    private final String hostName = IniUtil.getInstance().getServerHostName();
    private final int port = IniUtil.getInstance().getSocketIOServerPort();
    private final int portSsl = IniUtil.getInstance().getSocketIOServerPortSsl();
    private SocketIOServer server;
    private SocketIOServer serverSsl;
    private static final String SOCKETIO_EVENT_REQUEST = "request";
    private static final String SOCKETIO_EVENT_RESPONSE = "response";

    public static HYSocketIOServer getInstance() {
        if (instance == null) {
            instance =  new HYSocketIOServer();
        }
        return instance;
    }

    public void initNonSsl() {
        logger.info("init socketio server begin");
        Configuration config = new Configuration();
        config.setHostname(hostName);
        config.setPort(port);
        //hole
        config.getSocketConfig().setReuseAddress(true);

        server  = new SocketIOServer(config);

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                try {
                    WebsocketClientManager.getInstance().addClient(client);
                } catch (Exception e) {
                    logger.error(Tools.getTrace(e));
                }

            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                try {
                    int connectionID = client.hashCode();
                    ConnectionInfo ci = WebsocketClientManager.getInstance().getConnectionInfo(connectionID);
                    if (ci != null && ci.getUserID() != null) {
                        logger.info(String.format("Normal socketio Client %s closed", ci.getUserID()));
                    } else {
                        logger.info(String.format("Normal socketio Client %s closed", new UnLoginClient(client, true)));
                    }
                    WebsocketClientManager.getInstance().shutDown(connectionID, ConnectionShutDownReason.ClientClose);
                } catch (Throwable throwable) {
                    logger.error(Tools.getTrace(throwable));
                }

            }
        });

        server.addEventListener(SOCKETIO_EVENT_REQUEST, String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) { ;
                SocketIORequestDispatcher.getInstance().dispatcher(client.hashCode(), data, false);
            }
        });

        logger.info("init socketio server end");
    }

    public void initSsl() {
        logger.info("init socketio server ssl begin");
        Configuration config = new Configuration();
        config.setHostname(hostName);
        config.setPort(portSsl);
        config.setKeyStorePassword(Globals.JKS_PASSWORD);
        try {
            config.setKeyStore(new FileInputStream(Globals.JKS_FILE_PATH));
        } catch (FileNotFoundException e) {
            logger.error(Tools.getTrace(e));
            return;
        }
        //hole
        config.getSocketConfig().setReuseAddress(true);

        serverSsl  = new SocketIOServer(config);
        serverSsl.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                try {
                    WebsocketClientManager.getInstance().addClient(client);
                } catch (Exception e) {
                    logger.error(Tools.getTrace(e));
                }

            }
        });

        serverSsl.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                try {
                    int connectionID = client.hashCode();
                    ConnectionInfo ci = WebsocketClientManager.getInstance().getConnectionInfo(connectionID);
                    if (ci != null && ci.getUserID() != null) {
                        logger.info(String.format("Normal socketio ssl Client %s closed", ci.getUserID()));
                    } else {
                        logger.info(String.format("Normal socketio ssl  Client %s closed", new UnLoginClient(client, true)));
                    }
                    WebsocketClientManager.getInstance().shutDown(connectionID, ConnectionShutDownReason.ClientClose);
                } catch (Throwable throwable) {
                    logger.error(Tools.getTrace(throwable));
                }

            }
        });

        serverSsl.addEventListener(SOCKETIO_EVENT_REQUEST, String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                SocketIORequestDispatcher.getInstance().dispatcher(client.hashCode(), data, false);
                client.sendEvent("收到了！",data);

            }
        });
        logger.info("init socketio ssl server end");
    }

    public void runNonSsl() {
        logger.info("start socketio server begin");

        Thread serverThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        server.start();
                        logger.info("socketio server is started");
                        logger.info(String.format("socket server is listening port:%d", port));
                        while(true)
                            Thread.sleep(Integer.MAX_VALUE);
                    } catch (Throwable e) {
                        logger.info("socketio server raise an exception");
                        logger.error(String.format("socket server raise an exception:%s", Tools.getTrace(e)));
                    } finally {
                        server.stop();
                        logger.error(String.format("socketio server is stopped"));
                        logger.error(String.format("socketio server will be restarted"));
                    }
                }
            }
        };
        serverThread.start();
    }

    public void runSsl() {
        logger.info("start socketio ssl server  begin");

        Thread serverThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        serverSsl.start();
                        logger.info("socketio ssl server is started");
                        logger.info(String.format("socketio ssl server is listening port:%d", port));
                        while(true)
                            Thread.sleep(Integer.MAX_VALUE);
                    } catch (Throwable e) {
                        logger.info("socketio ssl server raise an exception");
                        logger.error(String.format("socketio ssl server raise an exception:%s", Tools.getTrace(e)));
                    } finally {
                        server.stop();
                        logger.error(String.format("socketio ssl server is stopped"));
                        logger.error(String.format("socketio server will be restarted"));
                    }
                }
            }
        };
        serverThread.start();
    }

    private HYSocketIOServer() {
        this.initNonSsl();
        this.initSsl();
    }

    public static void main(String[] args) {
        HYSocketIOServer.getInstance().runNonSsl();
        HYSocketIOServer.getInstance().runSsl();
    }

    public void run() {
        this.runNonSsl();
        this.runSsl();
    }
}
