package cn.sensordb2.stcloud.core;

import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class Database {
	public static HYLogger logger = HYLogger.getLogger(Database.class);
	private Vertx vertx;
	private MongoClient mongoClient;
	private String databaseConfFile;
	private static Database instance;

	public static Database createInstance(Vertx vertx, String databaseConfFile) {
		instance = new Database(vertx, databaseConfFile);
		return instance;
	}

	public static Database getInstance() {
		return instance;
	}

	private Database(Vertx vertx, String databaseConfFile) {
		super();
		this.vertx = vertx;
		this.databaseConfFile = databaseConfFile;
		JsonObject config = new JsonObject(Tools.readFileToString(databaseConfFile));
		this.mongoClient = MongoClient.createShared(vertx, config);

		this.mongoClient.getCollections(res -> {
			if (res.succeeded()) {
				logger.info(String.format("mongodb database:%s init success", config.toString()));
			} else {
				logger.error(String.format("mongodb database:%s init failed", config.toString()));
				this.vertx.close();
			}
		});
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}
}
