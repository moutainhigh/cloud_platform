package cn.sensordb2.stcloud.server.singleServer;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionShutDownReason;
import cn.sensordb2.stcloud.server.common.UnLoginClient;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.StreamProcessor;
import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.server.redis.RedisPool;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.Vertx;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.ext.mongo.MongoClient;

import java.util.Date;

public class Server {
	public static HYLogger logger = HYLogger.getLogger(Server.class);
	private static Server instance;
	private final String serverName = IniUtil.getInstance().getServerHostName();
	private final int port = IniUtil.getInstance().getServerPort();
	private final int portSsl = IniUtil.getInstance().getServerPortSsl();
	NetServer[] server = new NetServer[IniUtil.getInstance().getServerInstancesNum()];
	NetServer[] serverSsl = new NetServer[IniUtil.getInstance().getServerInstancesNum()];

	private Database database;
	private Vertx vertx;

	private Server(Vertx vertx, Database database) {
		this.init(vertx, database);
	}

	public void init(Vertx vertx, Database database) {
		logger.info("*******************************************************************************");
		this.vertx = vertx;
		RedisPool.initInstance(vertx);
		this.database = database;
	}

	public Database getDatabase() {
		return database;
	}

	public static Server getInstance() {
		if (instance == null) {
			logger.error("Server.getInstance() null");
		}
		return instance;
	}

	public static Server createInstance(Vertx vertx, Database database) {
		instance = new Server(vertx, database);
		return instance;
	}

	public MongoClient getMongoClient() {
		return this.database.getMongoClient();
	}

	public Vertx getVertx() {
		return vertx;
	}

	public void setVertx(Vertx vertx) {
		this.vertx = vertx;
	}

	static class CleanWorkThread extends Thread {
		@Override
		public void run() {
			logger.info("@@@@@@@@@@@@@@@" + Tools.dateToStr(new Date()) + " hycloud exit");
		}
	}

	protected void runServer() {
		if (IniUtil.getInstance().isServerTcpEnable()) {
			NetServerOptions netServerOptions = new NetServerOptions().setIdleTimeout(1200);
			for (int i = 0; i < IniUtil.getInstance().getServerInstancesNum(); i++) {
				server[i] = vertx.createNetServer(netServerOptions);
				final int j = i;
				server[j].connectHandler(socket -> {
					ClientManager.getInstance().addClient(socket);
					socket.handler(buffer -> {
						if (IniUtil.getInstance().isLogAllRequestString()) {
							ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(socket.hashCode());
							if (ci != null && !ci.isBinaryConnection()) {
								logger.receivedDataLogJson(buffer, socket, false);
							} else {
								logger.receivedDataLogBinary(buffer, socket, false);
							}
						}
						StreamProcessor.getInstance().receive(socket, buffer, false);
					});

					socket.exceptionHandler(v -> {
//						logger.error("socket exceptionHandler:"+Tools.getTrace(v), socket);
					});

					socket.closeHandler(v -> {
						try {
							int connectionID = socket.hashCode();
							ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(connectionID);
							if (ci != null && ci.getUserID() != null) {
								logger.info(String.format("Normal Client %s closed", ci.getUserID()), socket);
							} else {
								logger.info(String.format("Normal Client %s closed", new UnLoginClient(socket, false)), socket);
							}
							ClientManager.getInstance().shutDown(connectionID, ConnectionShutDownReason.ClientClose);
						} catch (Throwable throwable) {
							logger.error(Tools.getTrace(throwable));
						}
					});
				});
					server[j].listen(port, "0.0.0.0", res -> {
						if (res.succeeded()) {
							logger.info(String.format("Normal Server %d is now listening at port:%d!", j, port));
						} else {
							logger.info(String.format("Normal Server %d failed to bind at port:%d!", j, port));
						}
					});
			}
		}
	}

	protected void runServerSsl() {
		if (IniUtil.getInstance().isServerSslEnable()) {

			NetServerOptions netServerOptions = new NetServerOptions();
			netServerOptions.setSsl(true);
			JksOptions jksOptions = new JksOptions();
			jksOptions.setPath(Globals.JKS_FILE_PATH);
			jksOptions.setPassword(Globals.JKS_PASSWORD);
			netServerOptions.setKeyStoreOptions(jksOptions);

			for (int i = 0; i < IniUtil.getInstance().getServerInstancesNum(); i++) {
				serverSsl[i] = vertx.createNetServer(netServerOptions);
				final int j = i;
				serverSsl[j].connectHandler(socket -> {
					ClientManager.getInstance().addClient(socket);
					socket.handler(buffer -> {
						if (IniUtil.getInstance().isLogAllRequestString()) {
							ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(socket.hashCode());
							if (ci != null && !ci.isBinaryConnection()) {
								logger.receivedDataLogJson(buffer, socket, false);
							} else {
								logger.receivedDataLogBinary(buffer, socket, false);
							}
						}
						StreamProcessor.getInstance().receive(socket, buffer, false);
					});

					socket.exceptionHandler(v -> {
//					logger.error("socket exceptionHandler:"+Tools.getTrace(v), socket);
					});

					socket.closeHandler(v -> {
						try {
							int connectionID = socket.hashCode();
							ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(connectionID);
							if (ci != null && ci.getUserID() != null) {
								logger.info(String.format("Normal SSL Client %s closed", ci.getUserID()), socket);
							} else {
								logger.info(String.format("Normal SSL Client %s closed", new UnLoginClient(socket, false)), socket);
							}
							ClientManager.getInstance().shutDown(connectionID, ConnectionShutDownReason.ClientClose);
						} catch (Throwable throwable) {
							logger.error(Tools.getTrace(throwable));
						}
					});
				});
				serverSsl[j].listen(portSsl, "0.0.0.0", res -> {
					if (res.succeeded()) {
						logger.info(String.format("Normal SSL Server %d is now listening at port:%d!", j, portSsl));
					} else {
						logger.info(String.format("Normal SSL Server %d failed to bind at port:%d!", j, portSsl));
					}
				});
			}
		}
	}

	public void run() {
		Runtime.getRuntime().addShutdownHook(new CleanWorkThread());

		this.runServer();
		this.runServerSsl();
	}
}