package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.IniUtil;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;

/**
 * Created by sensordb on 16/7/13.
 */
public class VerticleClusterConnectionManager {
    private static HYLogger logger = HYLogger.getLogger(VerticleClusterConnectionManager.class);
    private static VerticleClusterConnectionManager instance;
    private AsyncMap<String, VerticleConnectionInfoRemote> map;
    private static final String ClusterWideMapName = "ClusterWideMap";
    private boolean clusterMode = IniUtil.getInstance().isClusterMode();

    private VerticleClusterConnectionManager() {
        if (!clusterMode) return;
        Vertx vertx = Server.getInstance().getVertx();
        SharedData sd = vertx.sharedData();

        sd.<String, VerticleConnectionInfoRemote>getClusterWideMap(ClusterWideMapName, res -> {
            if (res.succeeded()) {
                map = res.result();
                logger.info("VerticleClusterConnectionManager clusterWideMap init success");
            } else {
                logger.error("VerticleClusterConnectionManager clusterWideMap init failed");
            }
        });
    }

    public static VerticleClusterConnectionManager getInstance() {
        if (instance == null) instance = new VerticleClusterConnectionManager();
        return instance;
    }

    public void addVerticleClient(String clientID,
                                  VerticleConnectionInfoRemote verticleConnectionInfoRemote) {
        if (!clusterMode) return;

        if (clientID == null) {
            return;
        }

        map.put(clientID, verticleConnectionInfoRemote, resPut -> {
            if (resPut.succeeded()) {
                if (verticleConnectionInfoRemote.getUserID() != null)
                    logger.info(String.format("VerticleClusterConnectionManager remoteMap put clientID:%s userInfo:%s success",
                            clientID, verticleConnectionInfoRemote.toJsonObject().toString()));
                else {
                    logger.info(String.format("VerticleClusterConnectionManager remoteMap put clientID:%s userInfo:%s success",
                            clientID, verticleConnectionInfoRemote.toJsonObject().toString()));
                }
            } else {
                if (verticleConnectionInfoRemote.getUserID() != null)
                    logger.error(String.format("VerticleClusterConnectionManager remoteMap put clientID:%s userInfo:%s failed",
                            clientID, verticleConnectionInfoRemote.toJsonObject().toString()));
                else {
                    logger.error(String.format("VerticleClusterConnectionManager remoteMap put clientID:%s userInfo:%s failed",
                            clientID, verticleConnectionInfoRemote.toJsonObject().toString()));
                }
            }
        });
    }


    public void removeVerticleClient(String clientID) {
        if (!clusterMode) return;

        if (clientID == null) {
            return;
        }
        map.remove(clientID, res -> {
            if (res.succeeded()) {
                logger.info(String.format("VerticleClusterConnectionManager remoteMap remove clientID:%s success",
                        clientID));
            } else {
                logger.error(String.format("VerticleClusterConnectionManager remoteMap remove clientID:%s failed",
                        clientID));
            }
        });
    }

    public void getVerticleClient(String clientID, Handler<VerticleConnectionInfoRemote> handler) {
        if (!clusterMode) return;

        map.get(clientID, res -> {
            if (res.succeeded()) {
                VerticleConnectionInfoRemote val = res.result();
                handler.handle(val);
            } else {
                logger.error(String.format("VerticleClusterConnectionManager remoteMap get clientID:%s failed",
                        clientID));
            }
        });
    }

}