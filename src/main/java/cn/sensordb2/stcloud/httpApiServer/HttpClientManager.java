package cn.sensordb2.stcloud.httpApiServer;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.util.Debug;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpClientManager {
    private static HYHttpServerLogger logger = HYHttpServerLogger.getLogger(HttpClientManager.class);
    private static HttpClientManager instance;
    private static AtomicInteger counter = new AtomicInteger(0);

    //connectionID->ConnectionInfo
    private ConcurrentHashMap<Integer, ConnectionInfo> socketConnectionInfoMap = new ConcurrentHashMap();

    //clientName->ConnectionInfo
    private ConcurrentHashMap<String, ConnectionInfo> userConnectionInfoMap = new ConcurrentHashMap();

    private HttpClientManager() {
    }

    public static  int getNewCounter() {
        return counter.addAndGet(1);
    }

    public static HttpClientManager getInstance() {
        if (instance == null) instance = new HttpClientManager();
        return instance;
    }

    public boolean hasClientLoggin(String clientName) {
        return userConnectionInfoMap.contains(clientName);
    }


    public ConnectionInfo addClient(RoutingContext routingContext, String userName) {
        int connectionID = Debug.getDebugSessionID(routingContext);

        logger.info(String.format("New Http Client:%s", connectionID));
        if(this.socketConnectionInfoMap.contains(connectionID)) {
            logger.error("http client connectionID same");
        }

        ConnectionInfo connectionInfo = new ConnectionInfo(connectionID, routingContext, HttpClientManager.getNewCounter());
        connectionInfo.setUserID(userName);
        this.socketConnectionInfoMap.put(connectionID, connectionInfo);
        return connectionInfo;
    }


    public void removeClient(int connectionID) {
        ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
        if(connectionInfo==null)return;


        if (IniUtil.getInstance().isRecordTcpConnection()) {
            JsonObject connectionInfoJsonObject = connectionInfo.toJsonObject();
            Database.getInstance().getMongoClient().save(Globals.getTcpConnectionTable(), connectionInfoJsonObject, result -> {
                if (result.failed()) {
                    logger.error(String.format("save http client tcpConnection:%s info error", connectionInfoJsonObject), connectionInfo);
                }
            });
        }

        if(connectionInfo.getUserID()!=null) {
            this.userConnectionInfoMap.remove(connectionInfo.getUserID());
        }
        this.socketConnectionInfoMap.remove(connectionID);
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
            logger.error(Tools.getTrace(e));
        }
    }

    public void updateUserConnectionInfoMap(ConnectionInfo connectionInfo) {
        this.userConnectionInfoMap.put(connectionInfo.getUserID(), connectionInfo);
    }

    public void getConnectionInfo(String clientID, Handler<ConnectionInfo> handler) {
        ConnectionInfo ci  = this.userConnectionInfoMap.get(clientID);
        if (ci != null) {
            handler.handle(ci);
        }
        else {
            handler.handle(null);
            return;
        }
    }

    public boolean isClientConnected(String clientID) {
        return this.userConnectionInfoMap.containsKey(clientID);
    }
}
