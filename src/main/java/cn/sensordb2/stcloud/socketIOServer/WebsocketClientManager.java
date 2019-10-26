package cn.sensordb2.stcloud.socketIOServer;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ConnectionShutDownReason;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import com.corundumstudio.socketio.SocketIOClient;
import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WebsocketClientManager {
    private static HYLogger logger = HYLogger.getLogger(WebsocketClientManager.class);
    private static WebsocketClientManager instance;
    private static AtomicInteger counter = new AtomicInteger(0);

    //connectionID->ConnectionInfo
    private ConcurrentHashMap<Integer, ConnectionInfo> socketConnectionInfoMap = new ConcurrentHashMap();

    //clientName->ConnectionInfo
    private ConcurrentHashMap<String, ConnectionInfo> userConnectionInfoMap = new ConcurrentHashMap();

    private WebsocketClientManager() {
    }

    public static  int getNewCounter() {
        return counter.addAndGet(1);
    }

    public static WebsocketClientManager getInstance() {
        if (instance == null) instance = new WebsocketClientManager();
        return instance;
    }

    public ConnectionInfo addClient(SocketIOClient socketIOClient) {
        return this.addClient(socketIOClient, false);
    }


    public boolean hasClientLoggin(String clientName) {
        return userConnectionInfoMap.contains(clientName);
    }


    public ConnectionInfo addClient(SocketIOClient socketIOClient, boolean isTemp) {
        int connectionID = socketIOClient.hashCode();
        String type = isTemp?"Temp":"Normal";
        logger.info(String.format("New socketio %s Client:%s", type,new UnLoginClient(socketIOClient, isTemp).toString()));
        if(this.socketConnectionInfoMap.contains(connectionID)) {
            logger.error("connectionID same");
        }

        ConnectionInfo connectionInfo = new ConnectionInfo(connectionID, socketIOClient, isTemp, WebsocketClientManager.getNewCounter());
        this.socketConnectionInfoMap.put(connectionID, connectionInfo);
        this.updateNoDataTimer(connectionID);
        return connectionInfo;
    }


    public void removeClient(int connectionID) {
        ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
        if(connectionInfo==null)return;


        if (IniUtil.getInstance().isRecordTcpConnection()) {
            JsonObject connectionInfoJsonObject = connectionInfo.toJsonObject();
            Database.getInstance().getMongoClient().save(Globals.getTcpConnectionTable(), connectionInfoJsonObject, result -> {
                if (result.failed()) {
                    logger.error(String.format("save tcpConnection:%s info error", connectionInfoJsonObject), connectionInfo);
                }
            });
        }


        if(!connectionInfo.isTemp()&&connectionInfo.getUserID()!=null) {
            this.userConnectionInfoMap.remove(connectionInfo.getUserID());
        }
        this.socketConnectionInfoMap.remove(connectionID);
    }

    public void updateNoDataTimer(int connectionID) {
        ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
        if(connectionInfo==null) return;
//		synchronized(connectionInfo) {
        if(connectionInfo.getNoDataTimerID()!=-1) {
            this.cancelNoDataTimer(connectionID);
        }
        long noDataTimerID = Server.getInstance().getVertx().setTimer(IniUtil.getInstance().getHeartBeatTimeout()*1000,
                id -> {
                    this.noDataTimeout(connectionID);
                });
        connectionInfo.setNoDataTimerID(noDataTimerID);
//		}
    }

    private void setNoDataTimerID(int connectionID, long noDataTimerID) {
        ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
        if(connectionInfo==null) return;
        connectionInfo.setNoDataTimerID(noDataTimerID);
    }

    public void noDataTimeout(int connectionID) {
        ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
        if(connectionInfo!=null) {
//			synchronized(connectionInfo) {
            connectionInfo.setNoDataTimeoutDate(new Date());
            this.shutDown(connectionID, ConnectionShutDownReason.HeartBeatTimeout);
//			}
        }
    }

    public void cancelNoDataTimer(int connectionID) {
        ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
        if(connectionInfo==null)return;
        long noDataTimerID = connectionInfo.getNoDataTimerID();
        Server.getInstance().getVertx().cancelTimer(noDataTimerID);
        this.setNoDataTimerID(connectionID, -1);
    }

    public ConnectionInfo getConnectionInfo(int connectionID) {
        return socketConnectionInfoMap.get(connectionID);
    }

    public ConnectionInfo getConnectionInfo(String userID) {
        return this.userConnectionInfoMap.get(userID);
    }

    public void shutDown(int connectionID, String reason) {
        ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
        if(connectionInfo==null)return;

        connectionInfo.setEndDate(new Date());
        try {
            logger.info(String.format("%s shutDown: %s", reason, connectionInfo.toJsonObject()), connectionInfo);
            this.removeClient(connectionID);
            connectionInfo.getNetSocket().close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.exception(Tools.getTrace(e));
        }
    }

    public void updateUserConnectionInfoMap(ConnectionInfo connectionInfo) {
        this.userConnectionInfoMap.put(connectionInfo.getUserID(), connectionInfo);
    }

    public ConnectionInfo getLocalConnectionInfo(String clientID) {
        if (clientID == null) {
            logger.error(String.format("getLocalConnectionInfo clientID:null"));
            return null;
        }
        return this.userConnectionInfoMap.get(clientID);
    }

    public void getConnectionInfo(String clientID, Handler<ConnectionInfo> handler) {
        if (clientID == null) {
            logger.error(String.format("getConnectionInfo clientID:null"));
            return;
        }
        ConnectionInfo ci = this.userConnectionInfoMap.get(clientID);
        if (ci != null) {
            handler.handle(ci);
        } else {
            handler.handle(null);
            return;
        }
    }

    public boolean isClientConnected(String clientID) {
        return this.userConnectionInfoMap.containsKey(clientID);
    }
}
