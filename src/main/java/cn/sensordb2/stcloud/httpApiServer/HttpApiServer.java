package cn.sensordb2.stcloud.httpApiServer;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.common.Account;
import cn.sensordb2.stcloud.server.common.AccountType;
import cn.sensordb2.stcloud.util.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.PfxOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class HttpApiServer {
	private static HYHttpServerLogger logger = HYHttpServerLogger.getLogger(HttpApiServer.class);
	public static String httpSessionHistoryTable = "httpApiSessionHistory";
	private static HttpApiServer instance;
	private final String serverName = HttpApiServerIniUtil.getInstance().getServerHostName();
	private final int port = HttpApiServerIniUtil.getInstance().getServerPort();
	public final String serverNameAndPort = serverName + ":" + port;
	private final int portHttps = HttpApiServerIniUtil.getInstance().getServerPortHttps();
	private final String versionFieldName = "version";
	private final String storageDir = HttpApiServerIniUtil.getInstance().getStorageDir();
	private final String apiURLPathPrefix = HttpApiServerIniUtil.getInstance().getServerURL() + ":" + versionFieldName;
	private Router router;
	private HttpServer[] httpServer = new HttpServer[HttpApiServerIniUtil.getInstance().getServerInstancesNum()];
	private HttpServer[] httpServerSSL = new HttpServer[HttpApiServerIniUtil.getInstance().getServerInstancesNum()];

	private Database database;
	private Hashtable<HttpConnection, HttpSession> httpConnectionHttpSessionHashtable = new Hashtable<HttpConnection, HttpSession>();

	private HttpApiServer(Vertx vertx, Database database) {
		this.init(vertx, database);
	}

	private void init(Vertx vertx, Database database) {
		logger.info("*******************************************************************************");

		if (HttpApiServerIniUtil.getInstance().isServerHttpsEnable()) {
			HttpServerOptions httpServerOptions = new HttpServerOptions();
//			httpServerOptions.setSsl(true).setKeyStoreOptions(
//					new JksOptions().setPath(HttpApiServerIniUtil.getInstance().getHttpsKeystoreFile())
//							.setPassword(HttpApiServerIniUtil.getInstance().getHttpsPassword()));

			httpServerOptions.setSsl(true).setPfxKeyCertOptions(
					new PfxOptions().setPath(HttpApiServerIniUtil.getInstance().getHttpsPfxKeyCertFile()).
							setPassword(HttpApiServerIniUtil.getInstance().getHttpsPfxPassword()));

			for (int i = 0; i < HttpApiServerIniUtil.getInstance().getServerInstancesNum(); i++) {
				this.httpServerSSL[i] = vertx.createHttpServer(httpServerOptions);
			}
		}

		if (HttpApiServerIniUtil.getInstance().isServerHttpEnable()) {
			HttpServerOptions httpServerOptions = new HttpServerOptions();
//			httpServerOptions.setTcpKeepAlive(true);
			httpServerOptions.setLogActivity(true);
			for (int i = 0; i < HttpApiServerIniUtil.getInstance().getServerInstancesNum(); i++) {
				this.httpServer[i] = vertx.createHttpServer(httpServerOptions);
			}
		}

		this.router = Router.router(vertx);
		this.router.route()
				.handler(BodyHandler.create().setBodyLimit(HttpApiServerIniUtil.getInstance().getHttpBodyLimit()));
		this.enableCrossDomain();
		this.database = database;

//		this.initFailureHandler();
		this.initPostHandler();
		this.initGetHandler();
//		this.initTestGetHandler();
	}

	protected void enableCrossDomain() {
		Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add("x-requested-with");
		allowHeaders.add("Access-Control-Allow-Origin");
		allowHeaders.add("origin");
		allowHeaders.add("Content-Type");
		allowHeaders.add("accept");

		Set<HttpMethod> allowMethods = new HashSet<>();
		allowMethods.add(HttpMethod.GET);
		allowMethods.add(HttpMethod.POST);
		allowMethods.add(HttpMethod.DELETE);
		allowMethods.add(HttpMethod.PATCH);
		router.route().handler(CorsHandler.create("*").allowedMethods(allowMethods).allowedHeaders(allowHeaders));
	}

	protected void addHttpConnection(HttpConnection httpConnection) {
		if (this.httpConnectionHttpSessionHashtable.containsKey(httpConnection)) {
			logger.error(String.format("httpConnection:%s allready add", httpConnection));
			return;
		}

		this.httpConnectionHttpSessionHashtable.put(httpConnection, new HttpSession());
	}

	protected HttpSession getHttpSession(HttpConnection httpConnection) {
		if (!this.httpConnectionHttpSessionHashtable.containsKey(httpConnection)) {
			logger.error(String.format("httpapiserver getHttpSession httpConnection:%s not existed", httpConnection));
			return null;
		}

		return this.httpConnectionHttpSessionHashtable.get(httpConnection);
	}

	protected void recoredHttpSessionData(HttpConnection httpConnection,
										  RoutingContext routingContext,
										  String responseUrl, String headerUserName) {
		try {
			HttpSession httpSession =  this.httpConnectionHttpSessionHashtable.get(httpConnection);
			if (httpSession == null) {
                logger.error(String.format("httpapiserver record HttpSessionData httpConnection:%s not existed", httpConnection));
                return;
            }

			if(Debug.Debug) {
                int id = Debug.getDebugSessionID(routingContext);
                if(id!=-1) {
                    httpSession.setDebugID(id);
                    httpSession.setAbsoluteURI(routingContext.data().get("absoluteURI").toString());
					httpSession.setResponseUrl(responseUrl);
					httpSession.setHeaderUserName(headerUserName);
                }
            }
		} catch (Exception e) {
			logger.error(String.format("httpapiserver record HttpSessionData exception:%s",
					Tools.getTrace(e)));
		}
	}

	protected void removeHttpConnection(HttpConnection httpConnection) {
		if (!this.httpConnectionHttpSessionHashtable.containsKey(httpConnection)) {
			logger.error(String.format("httpapiserver remove httpConnection:%s not existed", httpConnection));
			return;
		}

		this.httpConnectionHttpSessionHashtable.remove(httpConnection);
	}

	/*
	*HTTPS请求方式: POST
	* HTTPS://:SERVER/httpapiserver/api/v1/
	* 该调用需要鉴权，需要在HTTPS请求头中增加userName、hashedPassword字段。
	 */
	private void initPostHandler() {
		String urlPath = apiURLPathPrefix;
		logger.info("Router:" + urlPath);

		router.post(urlPath).handler(routingContext -> {
			Debug.setDebugSessionID(routingContext);
			int connectionID = Debug.getDebugSessionID(routingContext);

			String version = routingContext.request().getParam(versionFieldName);
			String headerUserName = routingContext.request().getHeader("userName");
			String headerHashedPassword = routingContext.request().getHeader("hashedPassword");

			ConnectionInfo connectionInfo = HttpClientManager.getInstance().addClient(routingContext, headerUserName);

			LogInfo logInfo = new LogInfo(routingContext, LogInfo.httpApiServer, headerUserName, headerHashedPassword);
			logger.info(logInfo);
//			this.httpEventProcessor(routingContext, logInfo);

			routingContext.response().closeHandler(result -> {
				logger.info(String.format("ending http session:%s ", logInfo));
				HttpClientManager.getInstance().shutDown(connectionID, SocketShutDownReason.SHUTDOWN_BY_FRAMEWORK_CLOSEHANDLER);
				return;
			});

			MongoClient mongoClient = Database.getInstance().getMongoClient();
			Buffer httpRequestBody = routingContext.getBody();

			if (HttpApiServerIniUtil.getInstance().isShowHttpBodyData()) {
				logger.info("body content");
				logger.info(Tools.bufferToPrettyByteString(httpRequestBody));
			}

			if (httpRequestBody == null || httpRequestBody.length() == 0) {
				logger.error("(buffer == null || buffer.length() == 0)");
				HttpServerResponseUtil.error400(routingContext);
				return;
			}

			if(headerUserName!=null&&headerHashedPassword!=null) {
				JsonObject userPasswordQuery = new JsonObject().put("userName", headerUserName).put("hashedPassword",
						headerHashedPassword);
				mongoClient.find(Account.accountTable, userPasswordQuery, res -> {
					if (!res.succeeded()) {
						HttpServerResponseUtil.serverError(routingContext);
						return;
					}
					if (res.result().size() == 0) {
						logger.error(String.format("userPasswordQuery:%s error", userPasswordQuery));
						HttpServerResponseUtil.error403(routingContext, -1);
						return;
					}

					Object accountTypeObject = res.result().get(0).getValue("type");
					int accountType = AccountType.UNKNOWN;
					if (accountTypeObject instanceof String) {
						accountType = AccountType.getAccountType(accountTypeObject.toString());
					} else {
						accountType = res.result().get(0).getInteger("type");
					}
					connectionInfo.setUser(headerUserName, accountType);
					HttpServerRequestDispatcher.getInstance().dispatcher(Debug.getDebugSessionID(routingContext),
							httpRequestBody.toString());
					this.recoredHttpSessionData(routingContext.request().connection(),
							routingContext, "", headerUserName);

				});
				return;
			}

			//no complete headerUserName and headerHashedPassword
			HttpServerRequestDispatcher.getInstance().dispatcher(Debug.getDebugSessionID(routingContext),
					httpRequestBody.toString());
			this.recoredHttpSessionData(routingContext.request().connection(),
					routingContext, "", headerUserName);

		});
	}

	private void initGetHandler() {
//		String urlPath = apiURLPathPrefix+"/test";
//		logger.info("Router:" + urlPath);
//
//		router.get(urlPath).handler(routingContext -> {
//
//			LogInfo logInfo = new LogInfo(routingContext, LogInfo.httpApiServer, "", "");
//			logger.info(logInfo);
//			this.httpEventProcessor(routingContext, logInfo);
//
//			HttpServerResponseUtil.success200(routingContext, "hello world");
//
//		});


		String urlPath = apiURLPathPrefix;
		logger.info("Router:" + urlPath);

		router.get(urlPath).handler(routingContext -> {
//			int connectionID = routingContext.request().netSocket().hashCode();

			Debug.setDebugSessionID(routingContext);
			int connectionID = Debug.getDebugSessionID(routingContext);
			String version = routingContext.request().getParam(versionFieldName);
			String headerUserName = routingContext.request().getParam("useName");

			if (headerUserName == null) {
				headerUserName = routingContext.request().getParam("u");
			}
			String headerHashedPassword = routingContext.request().getHeader("hashedPassword");
			if (headerHashedPassword == null) {
				headerHashedPassword = routingContext.request().getParam("p");
			}

			if (headerUserName == null || headerHashedPassword == null) {
				HttpServerResponseUtil.success200(routingContext, "no user name or password error");
				return;
			}
			HttpClientManager.getInstance().addClient(routingContext, headerUserName);

			LogInfo logInfo = new LogInfo(routingContext, LogInfo.httpApiServer, headerUserName, headerHashedPassword);
			logger.info(logInfo);
			this.httpEventProcessor(routingContext, logInfo);

			routingContext.response().closeHandler(result -> {
				logger.info(String.format("ending http session:%s ", logInfo));
//				HttpClientManager.getInstance().shutDown(connectionID, SocketShutDownReason.SHUTDOWN_BY_FRAMEWORK_CLOSEHANDLER);
				return;
			});

			if (headerUserName == null || headerHashedPassword == null) {
				logger.error("headerUserName == null || headerHashedPassword == null");
				HttpServerResponseUtil.error400(routingContext);
				return;
			}
			MongoClient mongoClient = Database.getInstance().getMongoClient();

			Buffer httpRequestBody = routingContext.getBody();

			if (HttpApiServerIniUtil.getInstance().isShowHttpBodyData()) {
				logger.info("body content");
				logger.info(Tools.bufferToPrettyByteString(httpRequestBody));
			}

			if (httpRequestBody == null || httpRequestBody.length() == 0) {
				logger.error("(buffer == null || buffer.length() == 0)");
				HttpServerResponseUtil.error400(routingContext);
				return;
			}

			JsonObject userPasswordQuery = new JsonObject().put("userName", headerUserName).put("hashedPassword",
					headerHashedPassword);
			final String headerUserNameFinal = headerUserName;
			mongoClient.find(Account.accountTable, userPasswordQuery, res -> {
				if (!res.succeeded()) {
					HttpServerResponseUtil.serverError(routingContext);
					return;
				}
				if (res.result().size() == 0) {
					logger.error(String.format("userPasswordQuery:%s error", userPasswordQuery));
					HttpServerResponseUtil.error403(routingContext);
					return;
				}

				HttpServerRequestDispatcher.getInstance().dispatcher(Debug.getDebugSessionID(routingContext),
						httpRequestBody.toString());

				this.recoredHttpSessionData(routingContext.request().connection(),
						routingContext, "", headerUserNameFinal);

			});
		});
	}

	private void initTestGetHandler() {
		String urlPath = apiURLPathPrefix+"/test";
		logger.info("Router:" + urlPath);

		router.get(urlPath).handler(routingContext -> {

			LogInfo logInfo = new LogInfo(routingContext, LogInfo.httpApiServer, "", "");
			logger.info(logInfo);
			this.httpEventProcessor(routingContext, logInfo);

			HttpServerResponseUtil.success200(routingContext, "hello world");

		});
	}

	private void initFailureHandler() {
		this.router.exceptionHandler(exception -> {
			logger.error(String.format("exceptionHandler of router exception:%s",
					Tools.getTrace(exception)));
			return;
		});
	}

	public void run() {
		logger.info("*******************************************************************************");
		if (HttpApiServerIniUtil.getInstance().isServerHttpsEnable()) {
			for (int i = 0; i < HttpApiServerIniUtil.getInstance().getServerInstancesNum(); i++) {
				final int serverIndex = i;
				this.httpServerSSL[i].connectionHandler(httpConnection -> {
					logger.info(String.format("HTTPS Server %d got a new httpConnection:%s at:%s", serverIndex, httpConnection,
							Tools.dateToStrMs(new Date())));
					this.addHttpConnection(httpConnection);

					httpConnection.closeHandler(result -> {
						Date endDate = new Date();
						HttpSession httpSession = this.getHttpSession(httpConnection);
						if (httpSession != null) {
							httpSession.setEnd(endDate);
							logger.info(String.format("httpConnection:%s session info:%s closed at:%s", httpConnection, httpSession,
									Tools.dateToStrMs(endDate)));
							logHttpSessionToDatabase(httpSession);
						}
						logger.info(String.format("httpConnection:%s closed at:%s", httpConnection,
								Tools.dateToStrMs(endDate)));
						this.removeHttpConnection(httpConnection);
					});


				});
				this.httpServerSSL[i].requestHandler(router::accept).listen(portHttps);
				logger.info(String.format("Here We Are!"));
				logger.info(String.format("HTTPS Server %d started", i));
			}
		}

		if (HttpApiServerIniUtil.getInstance().isServerHttpEnable()) {
			for (int i = 0; i < HttpApiServerIniUtil.getInstance().getServerInstancesNum(); i++) {
				final int serverIndex = i;
				httpServer[i].connectionHandler(httpConnection -> {
					logger.info(String.format("httpServer:%d got a new httpConnection:%s at:%s", serverIndex, httpConnection,
							Tools.dateToStrMs(new Date())));
					this.addHttpConnection(httpConnection);

					httpConnection.closeHandler(result -> {
						Date endDate = new Date();
						HttpSession httpSession = this.getHttpSession(httpConnection);
						if (httpSession != null) {
							httpSession.setEnd(endDate);
							logger.info(String.format("httpConnection:%s session info:%s closed at:%s", httpConnection, httpSession,
									Tools.dateToStrMs(endDate)));
							logHttpSessionToDatabase(httpSession);
						}

						logger.info(String.format("httpConnection:%s closed at:%s", httpConnection,
								Tools.dateToStrMs(endDate)));
						this.removeHttpConnection(httpConnection);
					});

				});
				httpServer[i].requestHandler(router::accept).listen(port);
				logger.info(String.format("HTTP Server %d started", i));
			}
		}
	}

	protected void logHttpSessionToDatabase(HttpSession httpSession) {
		MongoClient mongoClient = Database.getInstance().getMongoClient();
		JsonObject httpSessionHistory = httpSession.toJsonObject();

		mongoClient.save(httpSessionHistoryTable, httpSessionHistory, res -> {
			if (!res.succeeded()) {
				logger.error(String.format("logHttpSessionToDatabase:%s error", httpSessionHistory.toString()));
				return;
			}

//			logger.info(String.format("logHttpSessionToDatabase:%s success", httpSessionHistory.toString()));
		});

	}

	public Database getDatabase() {
		return database;
	}

	public static HttpApiServer getInstance() {
		return instance;
	}

	public static HttpApiServer createInstance(Vertx vertx, Database database) {
		instance = new HttpApiServer(vertx, database);
		return instance;
	}

	public MongoClient getMongoClient() {
		return this.database.getMongoClient();
	}


	public String getServerNameAndPort() {
		return this.serverNameAndPort;
	}


	private void httpEventProcessor(RoutingContext routingContext, LogInfo logInfo) {
		routingContext.response().closeHandler(result -> {
			logger.info(String.format("response closeHandler called at %s", Tools.dateToStrMs(new Date())));
			return;
		});

		routingContext.response().bodyEndHandler(result -> {
			logger.info(String.format("response bodyEndHandler called at %s", Tools.dateToStrMs(new Date())));
			return;
		});

		routingContext.response().headersEndHandler(result -> {
			logger.info(String.format("response headersEndHandler called at %s", Tools.dateToStrMs(new Date())));
			return;
		});

		routingContext.response().drainHandler(result -> {
			logger.info(String.format("response drainHandler called at %s", Tools.dateToStrMs(new Date())));
			return;
		});

		routingContext.response().exceptionHandler(result -> {
			logger.error(String.format("response exceptionHandler of exception:%s at %s", Tools.getTrace(result),
					Tools.dateToStrMs(new Date())));
			return;
		});
	}

}
