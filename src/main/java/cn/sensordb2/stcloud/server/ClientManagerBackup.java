package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.redis.OnlineUserMonitorRedisUtil;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.server.verticle.AddressUtil;
import cn.sensordb2.stcloud.server.verticle.NormalClientVerticle;
import cn.sensordb2.stcloud.server.verticle.NormalConnectionVerticle;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;

import java.util.Date;
import java.util.Hashtable;

public class ClientManagerBackup {
	private static HYLogger logger = HYLogger.getLogger(ClientManagerBackup.class);
	private static ClientManagerBackup instance;
	private static int counter = 0;

	//��������ʱҵ�� connectionID->ConnectionInfo
	private Hashtable<Integer, ConnectionInfo> socketConnectionInfoMap = new Hashtable();

	//����ҵ�� clientName->ConnectionInfo
	private Hashtable<String, ConnectionInfo> userConnectionInfoMap = new Hashtable();

	//connectionID -> VerticleID
	private Hashtable<Integer, String> connectionIDVerticleIDMap = new Hashtable();

	private ClientManagerBackup() {
	}
	
	public static ClientManagerBackup getInstance() {
		if(instance==null) instance = new ClientManagerBackup();
		return instance;
	}

	protected void deployClientVerticle(NetSocket netSocket) {
		//send data by netsocket
		int connectionID = AddressUtil.getConnectionID(netSocket);
		MessageConsumer<Buffer> consumer = Server.getInstance().getVertx().eventBus().consumer(AddressUtil.getConnectionAddress(connectionID));
		consumer.handler(message -> {
			netSocket.write(message.body());
		});
		this.deployVerticle(netSocket);
	}

	protected void undeployClientVerticle(int connectionID) {
		MessageConsumer<Buffer> consumer = Server.getInstance().getVertx().eventBus().consumer(AddressUtil.getConnectionAddress(connectionID));
		if (consumer != null) {
			consumer.unregister(res -> {
				if (res.succeeded()) {
					logger.info(String.format("The handler of client %d un-registration has reached all nodes", connectionID));
				} else {
					logger.info(String.format("The handler of client %d Un-registration failed!",connectionID));
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
					logger.info(String.format("Client verticle %d is undeployed with failure", connectionID));
				}
			});
		}
	}

	protected void deployVerticle(NetSocket socket) {
		int connectionID = socket.hashCode();
		JsonObject config = new JsonObject().put(NormalConnectionVerticle.ConnectionIDParamsKey, connectionID);
		DeploymentOptions options = new DeploymentOptions().setConfig(config);

		Verticle verticle = new NormalClientVerticle();
		Server.getInstance().getVertx().deployVerticle(verticle, options, res -> {
			if (res.succeeded()) {
				this.connectionIDVerticleIDMap.put(connectionID, res.result());
				socket.handler(buffer -> {
					if (IniUtil.getInstance().isLogAllRequestString()) {
						ConnectionInfo ci = ClientManagerBackup.getInstance().getConnectionInfo(socket.hashCode());
						if(ci!=null&&!ci.isBinaryConnection()){
							logger.receivedDataLogJson(buffer, socket, false);
						}
						else {
							logger.receivedDataLogBinary(buffer, socket, false);
						}
					}

					if (IniUtil.getInstance().isLogAllRequestString()) {
						logger.info(String.format("Normal Server received:[\n%s\n%s\n] from %d",
								Tools.bufferToPrettyByteStringForGate(buffer),
								buffer.toString(),
								socket.hashCode()));
					}
					Server.getInstance().getVertx().eventBus().send(AddressUtil.getClientAddress(socket), buffer);
				});

				logger.info(String.format("Client verticle %d is deployed successfully", connectionID));
			} else {
				logger.info(String.format("Client verticle %d is deployed with failure", connectionID));
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
		int connectionID = netSocket.hashCode();
		String type = isTemp?"Temp":"Normal";
		logger.info(String.format("New %s Client:%d", type,connectionID));
		if(this.socketConnectionInfoMap.contains(connectionID)) {
			logger.info("connectionID same");
		}
		ConnectionInfo connectionInfo = new ConnectionInfo(connectionID, netSocket, isTemp, ++counter);
		this.socketConnectionInfoMap.put(connectionID, connectionInfo);		
		OnlineUserMonitorRedisUtil.updateConnection(connectionInfo);
		return connectionInfo;
	}


	public ConnectionInfo addVerticleClient(NetSocket netSocket, boolean isTemp) {
		int connectionID = netSocket.hashCode();
		String type = isTemp?"Temp":"Normal";
		logger.info(String.format("New %s Client:%d", type,connectionID));
		if(this.socketConnectionInfoMap.contains(connectionID)) {
			logger.info("connectionID same");
		}
		ConnectionInfo connectionInfo = new ConnectionInfo(connectionID, netSocket, isTemp, ++counter);
		connectionInfo.setVertx(Server.getInstance().getVertx());
		this.socketConnectionInfoMap.put(connectionID, connectionInfo);

		this.deployClientVerticle(netSocket);
		OnlineUserMonitorRedisUtil.updateConnection(connectionInfo);
		return connectionInfo;
	}


	public void removeClient(int connectionID) {
		ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
		if(connectionInfo==null)return;
		if(!connectionInfo.isTemp()&&connectionInfo.getUserID()!=null) {
			this.userConnectionInfoMap.remove(connectionInfo.getUserID());
		}
		this.socketConnectionInfoMap.remove(connectionID);
		this.undeployClientVerticle(connectionID);
		OnlineUserMonitorRedisUtil.removeConnection(connectionInfo);
}
	
	public void updateNoDataTimer(int connectionID) {
		ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);		
		if(connectionInfo==null) return;
//		synchronized(to) {
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
	
	public void shutDown(int connectionID, String reason) {
		ConnectionInfo connectionInfo = this.socketConnectionInfoMap.get(connectionID);
		if(connectionInfo==null)return;

		try {
			logger.info(String.format("%s shutDown: %s", reason, connectionInfo.toJsonObject()));
			this.removeClient(connectionID);
			connectionInfo.getNetSocket().close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(Tools.getTrace(e));
		}
	}
	
	public void updateUserConnectionInfoMap(ConnectionInfo connectionInfo) {
		this.userConnectionInfoMap.put(connectionInfo.getUserID(), connectionInfo);
	}
	
	public ConnectionInfo getConnectionInfo(String clientID) {
		return this.userConnectionInfoMap.get(clientID);
	}
	
	public boolean isClientConnected(String clientID) {
		return this.userConnectionInfoMap.containsKey(clientID);
	}
}

