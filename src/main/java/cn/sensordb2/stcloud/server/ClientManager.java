package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.common.UnLoginClient;
import cn.sensordb2.stcloud.server.verticle.TempClientVerticle;
import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.server.common.HYVertxNetSocket;
import cn.sensordb2.stcloud.server.redis.OnlineUserMonitorRedisUtil;
import cn.sensordb2.stcloud.server.redis.RedisClusterConnectionInfo;
import cn.sensordb2.stcloud.server.redis.RedisClusterConnectionManager;
import cn.sensordb2.stcloud.server.redis.RedisConnectionInfoRemote;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.server.verticle.AddressUtil;
import cn.sensordb2.stcloud.server.verticle.NormalClientVerticle;
import cn.sensordb2.stcloud.server.verticle.NormalConnectionVerticle;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Verticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientManager {
	private static HYLogger logger = HYLogger.getLogger(ClientManager.class);
	private static ClientManager instance;
	private static AtomicInteger counter = new AtomicInteger(0);

	//connectionID->ConnectionInfo
	private ConcurrentHashMap<Integer, ConnectionInfo> socketConnectionInfoMap = new ConcurrentHashMap();

	//clientName->ConnectionInfo
	private ConcurrentHashMap<String, ConnectionInfo> userConnectionInfoMap = new ConcurrentHashMap();

	//connectionID -> VerticleID
	private ConcurrentHashMap<Integer, String> connectionIDVerticleIDMap = new ConcurrentHashMap();

	//connetcionType -> ConnectionInfo
	private ConcurrentHashMap<String, ConnectionInfo> typeConnectionInfoMap = new ConcurrentHashMap();

	private ClientManager() {		
	}

	public static  int getNewCounter() {
		return counter.addAndGet(1);
	}
	
	public static ClientManager getInstance() {
		if(instance==null) instance = new ClientManager();
		return instance;
	}

	protected void deployClientVerticle(NetSocket netSocket, boolean isTemp) {
		//send data by netsocket
		int connectionID = AddressUtil.getConnectionID(netSocket);
		MessageConsumer<Buffer> consumer = Server.getInstance().getVertx().eventBus().consumer(AddressUtil.getConnectionAddress(connectionID));
		consumer.handler(message -> {
			netSocket.write(message.body());
		});
		this.deployVerticle(netSocket, isTemp);
	}

	protected void undeployClientVerticle(int connectionID) {
		MessageConsumer<Buffer> consumer = Server.getInstance().getVertx().eventBus().consumer(AddressUtil.getConnectionAddress(connectionID));
		if (consumer != null) {
			consumer.unregister(res -> {
				if (res.succeeded()) {
					logger.info(String.format("The handler of client %d un-registration has reached all nodes", connectionID));
				} else {
					logger.error(String.format("The handler of client %d Un-registration failed!", connectionID));
				}
			});
		}
		this.unDeployVerticle(connectionID);

	}

	protected void unDeployVerticle(int connectionID) {
		String verticleID = this.connectionIDVerticleIDMap.get(connectionID);
		this.connectionIDVerticleIDMap.remove(connectionID);
		if (verticleID != null) {
			Server.getInstance().getVertx().undeploy(verticleID, res -> {
				if (res.succeeded()) {
					logger.info(String.format("Client verticle %d is undeployed successfully", connectionID));
				} else {
					logger.error(String.format("Client verticle %d is undeployed with failure", connectionID));
				}
			});
		}
	}

	protected void deployVerticle(NetSocket socket) {
		this.deployVerticle(socket, true);
	}

	protected void deployVerticle(NetSocket socket, boolean isTemp) {
		int connectionID = socket.hashCode();
		JsonObject config = new JsonObject().put(NormalConnectionVerticle.ConnectionIDParamsKey, connectionID);
		DeploymentOptions options = new DeploymentOptions().setConfig(config);

		Verticle verticle;
		if(isTemp) {
			verticle = new TempClientVerticle();
		} else {
			verticle = new NormalClientVerticle();
		}

		Server.getInstance().getVertx().deployVerticle(verticle, options, res -> {
			if (res.succeeded()) {
				this.connectionIDVerticleIDMap.put(connectionID, res.result());
				socket.handler(buffer -> {
					if (IniUtil.getInstance().isLogAllRequestString()) {
						ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(socket.hashCode());
						if (ci != null && !ci.isBinaryConnection()) {
							logger.receivedDataLogJson(buffer, socket, isTemp);
						} else {
							logger.receivedDataLogBinary(buffer, socket, isTemp);
						}
					}

					Server.getInstance().getVertx().eventBus().send(AddressUtil.getClientAddress(socket), buffer);
				});

				logger.info(String.format("Client verticle %d is deployed successfully", connectionID));
			} else {
				logger.error(String.format("Client verticle %d is deployed with failure", connectionID));
			}
		});
	}


	public ConnectionInfo addClient(NetSocket netSocket) {
		return this.addClient(netSocket, false);
	}


	public ConnectionInfo addVerticleClient(NetSocket netSocket) {
		return this.addVerticleClient(netSocket, false);
	}


	public boolean hasClientLoggin(String clientName) {
		return userConnectionInfoMap.contains(clientName);
	}
	
	
	public ConnectionInfo addClient(NetSocket netSocket, boolean isTemp) {
		netSocket.setWriteQueueMaxSize(1500);
		int connectionID = netSocket.hashCode();
		String type = isTemp ? "Temp" : "Normal";
		logger.info(String.format("New %s Client:%s", type,
				new UnLoginClient(netSocket, isTemp).toString()));
		if (this.socketConnectionInfoMap.contains(connectionID)) {
			logger.error("connectionID same");
		}

		ConnectionInfo connectionInfo = new ConnectionInfo(connectionID, netSocket, isTemp, ClientManager.getNewCounter());
		connectionInfo.setClientHost(netSocket.remoteAddress().host());
		connectionInfo.setClientPort(netSocket.remoteAddress().port());
		this.socketConnectionInfoMap.put(connectionID, connectionInfo);
		this.updateNoDataTimer(connectionID);
		OnlineUserMonitorRedisUtil.updateConnection(connectionInfo);

		return connectionInfo;
	}

	public ConnectionInfo addVerticleClient(NetSocket netSocket, boolean isTemp) {
		int connectionID = netSocket.hashCode();
		String type = isTemp?"Temp":"Normal";
		logger.info(String.format("New %s Client:%d", type,connectionID));
		logger.info(String.format("New %s Client:%d remoteAddr:%s localAddr:%s", type, connectionID,
				Tools.netAddressToString(netSocket.remoteAddress()),
				Tools.netAddressToString(netSocket.localAddress())));
		if(this.socketConnectionInfoMap.contains(connectionID)) {
			logger.info(String.format("connectionID %s same", connectionID));
		}
		ConnectionInfo connectionInfo = new ConnectionInfo(connectionID, netSocket, isTemp, ClientManager.getNewCounter());
		connectionInfo.setClientHost(netSocket.remoteAddress().host());
		connectionInfo.setClientPort(netSocket.remoteAddress().port());
		connectionInfo.setVertx(Server.getInstance().getVertx());
		this.socketConnectionInfoMap.put(connectionID, connectionInfo);

		this.deployClientVerticle(netSocket, isTemp);
		OnlineUserMonitorRedisUtil.updateConnection(connectionInfo);
		return connectionInfo;
	}


	public void removeClient(int connectionID) {
		ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
		this.socketConnectionInfoMap.remove(connectionID);
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
		if(connectionInfo.isUseVerticle()) {
			this.undeployClientVerticle(connectionID);
			VerticleClusterConnectionManager.getInstance().removeVerticleClient(connectionInfo.getUserID());
		}
		if(IniUtil.getInstance().isRedisClusterMode()) {
			RedisClusterConnectionManager.getInstance().removeClient(connectionInfo.getUserID());
			connectionInfo.unSubscribeRemoteMessage();
		}

		HYVertxNetSocket netSocket = (HYVertxNetSocket)connectionInfo.getNetSocket();
		StreamProcessor.getInstance().shutDownSocket(netSocket.getNetSocket());
	}
	
	public void updateNoDataTimer(int connectionID) {
		ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);		
		if(connectionInfo==null) return;

		if(connectionInfo.getNoDataTimerID()!=-1) {
			this.cancelNoDataTimer(connectionID);
		}

		long noDataKickOffTime = IniUtil.getInstance().getHeartBeatTimeout()*1000;
		long noDataTimerID = Server.getInstance().getVertx().setTimer(noDataKickOffTime,
				id -> {
					this.noDataTimeout(connectionID);
				});
		connectionInfo.setNoDataTimerID(noDataTimerID);
	}
	
	private void setNoDataTimerID(int connectionID, long noDataTimerID) {
		ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
		if(connectionInfo==null) return;
		connectionInfo.setNoDataTimerID(noDataTimerID);
	}

	public void noDataTimeout(int connectionID) {
		ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
		if(connectionInfo!=null) {
//			synchronized(to) {
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
			OnlineUserMonitorRedisUtil.removeConnection(connectionInfo);
			
//			logger.info(String.format("%s shutDown: %s", reason, connectionInfo.toJsonObject()), connectionInfo);
			this.removeClient(connectionID);
			connectionInfo.getNetSocket().close();
			logger.info(String.format("call to.getNetSocket().close() for connection:%s reason:%s", connectionInfo.toJsonObject(), reason), connectionInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(Tools.getTrace(e));
		}
	}
	
	public void updateUserConnectionInfoMap(ConnectionInfo connectionInfo) {
		if (connectionInfo.getUserID() == null) {
			logger.error(String.format("updateUserConnectionInfoMap connectionInfo userID:null"), connectionInfo);
			return;
		}
		if (this.userConnectionInfoMap.get(connectionInfo.getUserID()) != null) {
			logger.error(String.format("updateUserConnectionInfoMap alreay login user:%s old ConnectionInfo:%s",
					connectionInfo.getUserID(), this.userConnectionInfoMap.get(connectionInfo.getUserID()).toJsonObject()), connectionInfo);
			logger.error(String.format("updateUserConnectionInfoMap alreay login user:%s new ConnectionInfo:%s",
					connectionInfo.getUserID(), connectionInfo.toJsonObject()), connectionInfo);
		}
		this.userConnectionInfoMap.put(connectionInfo.getUserID(), connectionInfo);
	}

	public ConnectionInfo getLocalConnectionInfo(String clientID) {
		ConnectionInfo ci  = this.userConnectionInfoMap.get(clientID);
		return ci;
	}

	public void setBindConnection(String from,String to){
		getLocalConnectionInfo(to).setFrom(from);
		this.typeConnectionInfoMap.put(from,getLocalConnectionInfo(to));
	}

	public void removeBindConnection(String from,String to){
		getLocalConnectionInfo(to).setFrom(null);
		this.typeConnectionInfoMap.remove(from);
	}

//	public ConnectionInfo getTypeConnectionInfo(String user){
//		ConnectionInfo ci  = this.typeConnectionInfoMap.get(user);
//		return ci;
//	}

//	public String getTypeConnetcionInfo(ConnectionInfo ci){
//		String string = this.typeConnectionInfoMap.get(ci);
//	}

	public void getConnectionInfo(String clientID, Handler<ConnectionInfo> handler) {
		ConnectionInfo ci  = this.userConnectionInfoMap.get(clientID);
		if (ci != null) {
			handler.handle(ci);
		}
		else {
			if(IniUtil.getInstance().isClusterMode()) {

				VerticleClusterConnectionManager.getInstance().getVerticleClient(clientID, res->{
					VerticleConnectionInfoRemote verticleConnectionInfoRemote = res;
					if (verticleConnectionInfoRemote == null) {
						handler.handle(null);
						return;
					}
					else {
						VerticleClusterConnectionInfo cci = new VerticleClusterConnectionInfo(verticleConnectionInfoRemote);
						cci.setVertx(Server.getInstance().getVertx());
						cci.setConnectionAddress(verticleConnectionInfoRemote.getVerticleConnectionAddress());
						handler.handle(cci);
						return;
					}
				});
			}
			else if(IniUtil.getInstance().isRedisClusterMode()) {

				RedisClusterConnectionManager.getInstance().getClient(clientID, res -> {
					RedisConnectionInfoRemote remote = res;
					if (remote == null) {
						handler.handle(null);
						return;
					} else {
						RedisClusterConnectionInfo rci = new RedisClusterConnectionInfo(remote);
						handler.handle(rci);
						return;
					}
				});
			}
			else {
				handler.handle(null);
				return;
			}
		}
	}
	
	public boolean isClientConnected(String clientID) {
		return this.userConnectionInfoMap.containsKey(clientID);
	}

	public ConcurrentHashMap<String, ConnectionInfo>  getSktCntInfoMap(){
		return this.userConnectionInfoMap;
	}
}

