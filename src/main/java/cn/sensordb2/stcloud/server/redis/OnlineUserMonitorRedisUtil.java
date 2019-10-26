package cn.sensordb2.stcloud.server.redis;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.common.AccountType;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;

public class OnlineUserMonitorRedisUtil {
	public static HYLogger logger = HYLogger.getLogger(OnlineUserMonitorRedisUtil.class);
	private static String CLIENT = "client";
	private static String HOSTNAME = "hostName";
	private static String TOP = "top";
	private static boolean redisOff = !RedisIniUtil.getInstance().isRedisTurnOn();

	/*
	 * request response list key
	 */
	private static String getConnectionInfoTopKey(ConnectionInfo connectionInfo) {
		return OnlineUserMonitorRedisUtil.getConnectionInfoKey(connectionInfo)+":"+TOP;
	}

	/*
	 * request response list key
	 */
	private static String getConnectionInfoUserUnknowTopKey(ConnectionInfo connectionInfo) {
		return OnlineUserMonitorRedisUtil.getConnectionInfoUserUnknowKey(connectionInfo)+":"+TOP;
	}

	private static String getConnectionInfoKey(ConnectionInfo connectionInfo) {
		StringBuffer sb = new StringBuffer();
		if(connectionInfo.getAccountType()== AccountType.UNKNOWN) {
			return OnlineUserMonitorRedisUtil.getConnectionInfoUserUnknowKey(connectionInfo);
		}
		else {
			sb.append(connectionInfo.getAccountString());
			sb.append(":");
			sb.append(connectionInfo.getUserID());
		}
		return sb.toString();
	}

	private static String getConnectionInfoUserUnknowKey(ConnectionInfo connectionInfo) {
		StringBuffer sb = new StringBuffer();
		if(connectionInfo.isTemp())
			sb.append("Temp");
		else sb.append("Normal");
		sb.append(connectionInfo.getIndex());
		return sb.toString();
	}

	public static void updateConnection(ConnectionInfo connectionInfo) {
		if(redisOff) return;

		if(RedisPool.getInstance()==null||RedisPool.getInstance().getRedisClient()==null) {
			logger.error("updateConnection RedisPool.getInstance()==null||RedisPool.getInstance().getRedisClient()==null");
			return;
		}

		RedisClient jedis = RedisPool.getInstance().getRedisClient();
		try {
			JsonObject jsonObject = connectionInfo.toJsonObject();
			jsonObject.put(HOSTNAME, IniUtil.getInstance().getServerHostName());
			String connectionInfoKey = getConnectionInfoKey(connectionInfo);
			jedis.hmset(connectionInfoKey, jsonObject, v -> {
//				if (v.failed()) {
//					logger.error(String.format("redis hmset key:%s value:%s", connectionInfoKey,
//					jsonObject));
//					return;
//				}
//				logger.info(String.format("redis hmset key:%s value:%s", connectionInfoKey,
//						jsonObject));
			});

			String connectionInfoTopKey = getConnectionInfoTopKey(connectionInfo);
			JsonObject requestResponseSessionList = connectionInfo.getRequestResponseSessionList().toJsonObject();
			jedis.hmset(connectionInfoTopKey, requestResponseSessionList, v->{
//				if (v.failed()) {
//					logger.error(String.format("redis hmset key:%s value:%s", connectionInfoTopKey,
//							requestResponseSessionList));
//					return;
//				}
//				logger.info(String.format("redis hmset key:%s value:%s", connectionInfoTopKey,
//						requestResponseSessionList));

			});
		} catch (Exception e) {
            logger.exception(Tools.getTrace(e));
        }   
	}

	/*

	 */
	public static void subMessage(ConnectionInfo connectionInfo) {

	}

	/*
	 * connection login
	 */
	public static void connectionLogin(ConnectionInfo connectionInfo) {
		if(redisOff) return;

		if(RedisPool.getInstance()==null||RedisPool.getInstance().getRedisClient()==null) {
			logger.error("connectionLogin RedisPool.getInstance()==null||RedisPool.getInstance().getRedisClient()==null");
			return;
		}

		RedisClient jedis = RedisPool.getInstance().getRedisClient();
		try {
			JsonObject jsonObject = connectionInfo.toJsonObject();
			jsonObject.put(HOSTNAME, IniUtil.getInstance().getServerHostName());
			String connectionInfoUserUnknowKey = getConnectionInfoUserUnknowKey(connectionInfo);
			jedis.del(connectionInfoUserUnknowKey, v->{
//				if (v.failed()) {
//					logger.error(String.format("redis del key:%s", connectionInfoUserUnknowKey));
//					return;
//				}
//				logger.info(String.format("redis del key:%s", connectionInfoUserUnknowKey));
//
			});
			String connectionInfoUserUnknowTopKey = getConnectionInfoUserUnknowTopKey(connectionInfo);
			jedis.del(connectionInfoUserUnknowTopKey, v->{
//				if (v.failed()) {
//					logger.error(String.format("redis del key:%s", connectionInfoUserUnknowTopKey));
//					return;
//				}
//				logger.info(String.format("redis del key:%s", connectionInfoUserUnknowTopKey));
			});
			jedis.hmset(getConnectionInfoKey(connectionInfo), jsonObject, v -> {
			});
			jedis.hmset(getConnectionInfoTopKey(connectionInfo), connectionInfo.getRequestResponseSessionList().toJsonObject(), v->{});
		} catch (Exception e) {
			logger.exception(Tools.getTrace(e));
		}
	}

	public static void removeConnection(ConnectionInfo connectionInfo) {
		if(redisOff) return;

		if(RedisPool.getInstance()==null||RedisPool.getInstance().getRedisClient()==null) {
			logger.error("removeConnection RedisPool.getInstance()==null||RedisPool.getInstance().getRedisClient()==null");
			return;
		}

		RedisClient jedis = RedisPool.getInstance().getRedisClient();
		try {
			String connectionInfoKey = getConnectionInfoKey(connectionInfo);
			jedis.del(connectionInfoKey, v -> {
//				if (v.failed()) {
//					logger.error(String.format("redis del key:%s", connectionInfoKey));
//					return;
//				}
//				logger.info(String.format("redis del key:%s", connectionInfoKey));
			});
			String connectionInfoUserUnknowKey = getConnectionInfoUserUnknowKey(connectionInfo);
			jedis.del(connectionInfoUserUnknowKey, v->{
//				if (v.failed()) {
//					logger.error(String.format("redis del key:%s", connectionInfoUserUnknowKey));
//					return;
//				}
//				logger.info(String.format("redis del key:%s", connectionInfoUserUnknowKey));
			});
			String connectionInfoTopKey = getConnectionInfoTopKey(connectionInfo);
			jedis.del(connectionInfoTopKey, v->{
//				if (v.failed()) {
//					logger.error(String.format("redis del key:%s", connectionInfoTopKey));
//					return;
//				}
//				logger.info(String.format("redis del key:%s", connectionInfoTopKey));
			});
			String connectionInfoUserUnknowTopKey = getConnectionInfoUserUnknowTopKey(connectionInfo);
			jedis.del(connectionInfoUserUnknowTopKey, v->{
//				if (v.failed()) {
//					logger.error(String.format("redis del key:%s", connectionInfoUserUnknowTopKey));
//					return;
//				}
//				logger.info(String.format("redis del key:%s", connectionInfoUserUnknowTopKey));
			});
		} catch (Exception e) {
            logger.exception(Tools.getTrace(e));
        } 
	}
}
