package cn.sensordb2.stcloud.server.redis;

import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import org.apache.log4j.Logger;

public final class RedisPool {
	public static Logger logger = Logger.getLogger(RedisPool.class);

	private static RedisPool instance;
	//
	private static String SERVER = RedisIniUtil.getInstance().getServer();

	//
	private static int PORT = RedisIniUtil.getInstance().getPort();

	//
	private static String PASSWORD = RedisIniUtil.getInstance().getPassword();

	//database index
	private static int databaseIndex = RedisIniUtil.getInstance().getDatabaseIndex();
	private RedisClient redis;
	
	private RedisPool() {
		
	}
	
	public static void initInstance(Vertx vertx) {
		if(!RedisIniUtil.getInstance().isRedisTurnOn()) return;
		if(instance==null) {
			instance =  new RedisPool();
			instance.init(vertx);
		}
	}

	public static RedisPool getInstance() {
		if(!RedisIniUtil.getInstance().isRedisTurnOn()) return null;
		return RedisPool.instance;
	}

	/**
	 * ��ʼ��Redis���ӳ�
	 */
	private void init(Vertx vertx) {			
		RedisOptions config = new RedisOptions();
		config.setHost(SERVER);
		config.setPort(PORT);
		config.setAuth(PASSWORD);
		config.setSelect(databaseIndex);
		redis = RedisClient.create(vertx, config);
	}
	
	public RedisClient getRedisClient() {
		return this.redis;
	}
}
