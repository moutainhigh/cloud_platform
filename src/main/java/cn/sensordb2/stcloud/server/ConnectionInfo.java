package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.common.*;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.server.verticle.AddressUtil;
import com.corundumstudio.socketio.SocketIOClient;
import cn.sensordb2.stcloud.httpApiServer.HttpClientManager;
import cn.sensordb2.stcloud.server.message.Response;
import cn.sensordb2.stcloud.server.redis.OnlineUserMonitorRedisUtil;
import cn.sensordb2.stcloud.server.redis.RedisPool;
import cn.sensordb2.stcloud.socketIOServer.WebsocketClientManager;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.RedisClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

public class ConnectionInfo {
	public static HYLogger logger = HYLogger.getLogger(ConnectionInfo.class);

	//inner userID
	String userID;

	//outer userID;
	String outerUserID;

	String userType;

	boolean isTemp;
	int connectionID;

	//for drone from where
	String from;

	//for user to where
	String to;

	//original no verticle
	HYNetSocket netSocket;
	//use verticle
	Vertx vertx = null;
	String connectionAddress;

	String tempType;
	String phoneNumber;
	int accountType=-1;
	long recentHeartBeatTimeMillis = 0;
	Date connectedDate;
	Date recentHeartBeatDate;
	Date noDataTimeoutDate;
	Date endDate;
	int type = ConnectionProtocolType.UNKNOWN; //

	String terminalType;
	String pushId;
	String deviceToken;
	String nickName;
	String sessionID;
	JsonObject tempUserParam = new JsonObject();
	boolean isForRealTime = false;


	long noDataTimerID = -1;
	long index = -1;

	String clientHost;
	int clientPort = -1;
	boolean receivedFirstCompletPack = false;
	boolean isAlive = false;

	RequestResponseSessionList requestResponseSessionList = new RequestResponseSessionList();

	public ConnectionInfo(int connectionID, NetSocket netSocket, boolean isTemp, long index) {
		super();
		this.connectionID = connectionID;
		this.netSocket = new HYVertxNetSocket(netSocket);
		this.isTemp = isTemp;
		this.connectedDate = new Date();
		this.index = index;
		this.connectionAddress = AddressUtil.getConnectionAddress(connectionID);
	}

	public ConnectionInfo(int connectionID, NetSocket netSocket, boolean isTemp, long index, boolean isForRealTime) {
		super();
		this.connectionID = connectionID;
		this.netSocket = new HYVertxNetSocket(netSocket);
		this.isTemp = isTemp;
		this.connectedDate = new Date();
		this.index = index;
		this.isForRealTime = isForRealTime;
		this.connectionAddress = AddressUtil.getConnectionAddress(connectionID);
	}

	public ConnectionInfo(int connectionID, SocketIOClient socketIOClient, boolean isTemp, long index) {
		super();
		this.connectionID = connectionID;
		this.netSocket = new HYWebNetSocket(socketIOClient);
		this.isTemp = isTemp;
		this.connectedDate = new Date();
		this.index = index;
		this.connectionAddress = AddressUtil.getConnectionAddress(connectionID);
	}

	public ConnectionInfo(int connectionID, RoutingContext routingContext, long index) {
		super();
		this.connectionID = connectionID;
		this.netSocket = new HYHttpNetSocket(routingContext);
		this.isTemp = false;
		this.connectedDate = new Date();
		this.index = index;
		this.connectionAddress = AddressUtil.getConnectionAddress(connectionID);
	}


	public ConnectionInfo() {
		super();
	}

	public void addRequestResponseSession(RequestResponseSessionInterface requestResponseSession) {
		this.requestResponseSessionList.add(requestResponseSession);
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}

	public RequestResponseSessionList getRequestResponseSessionList() {
		return requestResponseSessionList;
	}

	public String getUserID() {
		return userID;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;

		if (userID == null) {
			return;
		}
		if (this.isSocketIOConnection()) {
			WebsocketClientManager.getInstance().updateUserConnectionInfoMap(this);

		} else if (this.isSocketConnection()) {
			ClientManager.getInstance().updateUserConnectionInfoMap(this);
		} else if (this.isHttpConnection()) {
			HttpClientManager.getInstance().updateUserConnectionInfoMap(this);
		} else {
			logger.error(String.format("setUserID:%s error because connection type not valid", userID));
		}
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}
	
	public void setUser(String userID, int accountType) {
		this.userID = userID;
		if(this.isSocketIOConnection()) {
			WebsocketClientManager.getInstance().updateUserConnectionInfoMap(this);

		}
		else if(this.isSocketConnection()){
			ClientManager.getInstance().updateUserConnectionInfoMap(this);
		}
		else if(this.isHttpConnection()){
			HttpClientManager.getInstance().updateUserConnectionInfoMap(this);
		}
		else {
			logger.error(String.format("setUserID:%s error because connection type not valid", userID));
		}
		this.accountType = accountType;
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}

	public void setFrom(String from){ this.from = from; }

	public void setTo(String to){this.to = to;}
	
	public boolean isTemp() {
		return isTemp;
	}
	
	public void setTemp(boolean isTemp) {
		this.isTemp = isTemp;
	}

	public String getOuterOrTempUserID() {
		return outerUserID;
	}

	public void setOuterOrTempUserID(String outerUserID) {
		this.outerUserID = outerUserID;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public HYNetSocket getNetSocket() {
		return netSocket;
	}

	public String getTempType() {
		return tempType;
	}

	public void setTempType(String tempType) {
		this.tempType = tempType;
	}
	
	public boolean hasSetTempType() {
		return tempType!=null;
	}
	
	public boolean isTempTypeRegister() {
		return tempType!=null&&tempType.equals(TempServiceType.REG);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isLogged() {
		return this.userID!=null;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public boolean isDevice() {
		return this.accountType!=AccountType.USER
				&&this.accountType!=AccountType.UNKNOWN
				&&this.accountType!=AccountType.CLOUDADMIN
				&&this.accountType!=AccountType.OUTER
				&&this.accountType!=AccountType.TEMP
				&&this.accountType!= AccountType.WORK_MANAGER;
	}
	
	public boolean isRing() {
		return this.accountType==AccountType.RING;
	}
	
	public boolean isAppUser() {
		return this.accountType==AccountType.USER
				||this.accountType==AccountType.OUTER
				||this.accountType==AccountType.TEMP
				||this.accountType==AccountType.CLOUDADMIN;
	}

	public boolean isOuterAppUser() {
		return this.accountType==AccountType.OUTER;
	}

	public boolean isOuterOrTempAppUser() {
		return this.accountType==AccountType.OUTER || this.accountType==AccountType.TEMP;
	}

	public boolean isTempAppUser() {
		return this.accountType==AccountType.TEMP;
	}

	public boolean isCloudAdminUser() {
		return this.accountType==AccountType.CLOUDADMIN;
	}

	public String getAccountTypeString() {
		return AccountType.getAccountTypeString(this.getAccountType());
	}

	public long getRecentHeartBeatTimeMillis() {
		return recentHeartBeatTimeMillis;
	}

	public void updateRecentHeartBeatTimeMillis(long recentHeartBeatTimeMillis, Date recentHeartBeatDate) {
		this.recentHeartBeatTimeMillis = recentHeartBeatTimeMillis;
		this.recentHeartBeatDate = recentHeartBeatDate;
		if(this.isSocketConnection())
			ClientManager.getInstance().updateNoDataTimer(this.getConnectionID());
		else if (this.isSocketIOConnection()) {
			WebsocketClientManager.getInstance().updateNoDataTimer(this.getConnectionID());
		}
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}

	public void updateRecentHeartBeatTimeMillisSocketIO(long recentHeartBeatTimeMillis, Date recentHeartBeatDate) {
		this.recentHeartBeatTimeMillis = recentHeartBeatTimeMillis;
		this.recentHeartBeatDate = recentHeartBeatDate;
		WebsocketClientManager.getInstance().updateNoDataTimer(this.getConnectionID());
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}

	public void getNewRequest(long recentHeartBeatTimeMillis, Date recentHeartBeatDate, Request request) {
		this.updateRecentHeartBeatTimeMillis(recentHeartBeatTimeMillis, recentHeartBeatDate);
	}

	public void getNewRequestSocketIO(long recentHeartBeatTimeMillis, Date recentHeartBeatDate, Request request) {
		this.updateRecentHeartBeatTimeMillisSocketIO(recentHeartBeatTimeMillis, recentHeartBeatDate);
	}


	public Date getConnectedDate() {
		return connectedDate;
	}

	public void setConnectedDate(Date connectedDate) {
		this.connectedDate = connectedDate;
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}

	public Date getRecentHeartBeatDate() {
		return recentHeartBeatDate;
	}

	public void setRecentHeartBeatDate(Date recentHeartBeatDate) {
		this.recentHeartBeatDate = recentHeartBeatDate;
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}

	public Date getNoDataTimeoutDate() {
		return noDataTimeoutDate;
	}

	public void setNoDataTimeoutDate(Date noDataTimeoutDate) {
		this.noDataTimeoutDate = noDataTimeoutDate;
		this.endDate = noDataTimeoutDate;
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}

	public long getNoDataTimerID() {
		return noDataTimerID;
	}

	public void setNoDataTimerID(long noDataTimerID) {
		this.noDataTimerID = noDataTimerID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	public void setRecentHeartBeatTimeMillis(long recentHeartBeatTimeMillis) {
		this.recentHeartBeatTimeMillis = recentHeartBeatTimeMillis;
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		OnlineUserMonitorRedisUtil.updateConnection(this);
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public boolean isIosAppUser() {
		return PhoneType.APPLE.equalsIgnoreCase(this.terminalType);
	}

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public long getIndex() {
		return index;
	}

	public String getFrom(){return from;}

	public String getTo(){return to;}

	public void setIndex(long index) {
		this.index = index;
	}

	public Hashtable<String, String> toMap() {
		Hashtable<String, String> result = new Hashtable<String, String>();
		if(userID!=null)
			result.put("userID", userID);

		result.put("isTemp", Tools.boolToString(isTemp));
		
		if(tempType!=null)
			result.put("tempType", tempType);
		if(phoneNumber!=null)
			result.put("phoneNumber", phoneNumber);

		result.put("accountType", AccountType.getAccountTypeString(accountType));
		
		if(connectedDate!=null)
			result.put("connectedDate", Tools.dateToStr(connectedDate));
		/*
		if(recentHeartBeatDate!=null)
			result.put("recentHeartBeatDate", Tools.dateToStr(recentHeartBeatDate));
		if(noDataTimeoutDate!=null)
			result.put("noDataTimeoutDate", Tools.dateToStr(noDataTimeoutDate));
			*/
		if(endDate!=null)
			result.put("endDate", Tools.dateToStr(endDate));
		return result;
	}
	
	public String getAccountString() {
		return AccountType.getAccountTypeString(accountType);
	}

	public JsonObject toJsonObject() {
		JsonObject result = new JsonObject();
		result.put("userID", Tools.objectToString(userID));
		result.put("isTemp", isTemp);
		result.put("tempType", Tools.objectToString(tempType));
		result.put("phoneNumber", Tools.objectToString(phoneNumber));
		result.put("accountType", this.getAccountString());
		result.put("type", this.getTypeString());
		result.put("index", index);

		if(this.netSocket!=null && (this.netSocket instanceof HYWebNetSocket)) {
			result.put("channel", "WebSocket");
		}
		else {
			result.put("channel", "TCPSocket");
		}

		if(this.getClientHost()!=null)
			result.put("clientHost", this.getClientHost());
		result.put("clientPort", this.getClientPort());


		if(connectedDate!=null)
			result.put("connectedDate", Tools.dateToStr(connectedDate));
		if(recentHeartBeatDate!=null)
			result.put("recentHeartBeatDate", Tools.dateToStr(recentHeartBeatDate));
		if(noDataTimeoutDate!=null)
			result.put("noDataTimeoutDate", Tools.dateToStr(noDataTimeoutDate));
		if(endDate!=null)
			result.put("endDate", Tools.dateToStr(endDate));

		result.put("connectionID",this.connectionID);

		if(this.connectedDate!=null&&this.endDate!=null) {
			result.put("endTimeGap", Tools.timeGap(this.connectedDate.getTime(), this.endDate.getTime()));
		}

		if (this.connectedDate != null && this.recentHeartBeatDate != null) {
			result.put("recentHeartBeatTimeGap", Tools.timeGap(this.connectedDate.getTime(), this.recentHeartBeatDate.getTime()));
		}
		return result;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public String getTypeString() {
		if (this.isBinaryConnection()) {
			return "Binary";
		}
		else return "Json";
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getClientHost() {
		return clientHost;
	}

	public void setClientHost(String clientHost) {
		this.clientHost = clientHost;
	}

	public int getClientPort() {
		return clientPort;
	}

	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}

	public boolean isBinaryConnection() {
		return this.type==ConnectionProtocolType.BINARY;
	}

	public void setNetSocket(NetSocket netSocket) {
		this.netSocket = new HYVertxNetSocket(netSocket);
	}

	public boolean isUseVerticle() {
		return this.vertx!=null;
	}

	public void setVertx(Vertx vertx) {
		this.vertx = vertx;
	}

	public String getConnectionAddress() {
		return connectionAddress;
	}

	public void setConnectionAddress(String connectionAddress) {
		this.connectionAddress = connectionAddress;
	}

	public JsonObject getTempUserParam() {
		return tempUserParam;
	}

	public void setTempUserParam(JsonObject tempUserParam) {
		this.tempUserParam = tempUserParam;
	}

	public void write(String s) {
		this.getNetSocket().write(s.getBytes());
		return;
	}


	public void write(Response response) {
		if(this.isSocketIOConnection()) {

			this.getNetSocket().write(response.toString().getBytes());
		}
		else {
			this.getNetSocket().write(response.toStringWithDelimit().getBytes());
		}
		return;
	}


	public void write(Buffer buffer) {
		this.getNetSocket().write(buffer.getBytes());
		return;
	}

	public boolean isReceivedFirstCompletPack() {
		return receivedFirstCompletPack;
	}

	public void setReceivedFirstCompletPack(boolean receivedFirstCompletPack) {
		this.receivedFirstCompletPack = receivedFirstCompletPack;
	}

	/*
         * receive remote message
         */
	public void subscribeRemoteMessage() {
		Vertx vertx = Server.getInstance().getVertx();
		vertx.eventBus().<JsonObject>consumer("io.vertx.redis." + this.getUserID(), received -> {
			// do whatever you need to do with your message
			logger.info(String.format("redis.consumer of user:%s receive result:%s", this.getUserID(), received), this);

			JsonObject value = received.body().getJsonObject("value");
			logger.info(String.format("redis channel user:%s receive msg:%s", this.getUserID(), value.toString()), this);
			// the value is a JSON doc with the following properties
			// channel - The channel to which this message was sent
			// pattern - Pattern is present if you use psubscribe command and is the pattern that matched this message channel
			// message - The message payload
			this.write(value.getString("message"));
		});

		RedisClient redis = RedisPool.getInstance().getRedisClient();

		redis.subscribe(this.getUserID(), res -> {
			if (res.succeeded()) {
				logger.info(String.format("redis subscribe channel:%s succeed", this.getUserID()), this);
			} else {
				logger.info(String.format("redis subscribe channel:%s failed", this.getUserID()), this);
			}

			logger.info(String.format("redis.subscribe user:%s result:%s", this.getUserID(), res.result()), this);
		});
	}

	public void unSubscribeRemoteMessage() {
		RedisClient redis = RedisPool.getInstance().getRedisClient();
		ArrayList subChannel = new ArrayList<String>();
		subChannel.add(this.getUserID());

		redis.unsubscribe(subChannel, res -> {
			if (res.succeeded()) {
				logger.info(String.format("redis unSubscribe channel:%s succeed", this.getUserID()), this);
			} else {
				logger.info(String.format("redis unSubscribe channel:%s failed", this.getUserID()), this);
			}
		});
	}

	public void shutdown(String reason) {
		logger.info(String.format("%s shutDown: %s", reason, this.toJsonObject()), this);
		this.getNetSocket().close();

	}

	public boolean isWriteQueueFull() {
		if (this.netSocket instanceof HYVertxNetSocket) {
			return ((HYVertxNetSocket)this.netSocket).getNetSocket().writeQueueFull();
		}
		return false;
	}

	public boolean isSocketIOConnection() {
		return this.netSocket!=null && (this.netSocket instanceof  HYWebNetSocket);
	}

	public boolean isSocketConnection() {
		return this.netSocket!=null && (this.netSocket instanceof  HYVertxNetSocket);
	}

	public boolean isHttpConnection() {
		return this.netSocket!=null && (this.netSocket instanceof  HYHttpNetSocket);
	}

	public boolean isForRealTime() {
		return isForRealTime;
	}

	public boolean isRealTimeConnection() {
		return this.isForRealTime();
	}

	public boolean isAlive(){ return this.isAlive; }

	public void setIsAlive(boolean isAlive){ this.isAlive = isAlive; }
}
