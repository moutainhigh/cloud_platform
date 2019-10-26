package cn.sensordb2.stcloud.server.redis;

import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.redis.RedisClient;

/**
 * Created by sensordb on 16/7/13.
 */
public class RedisClusterConnectionManager {
    private static HYLogger logger = HYLogger.getLogger(RedisClusterConnectionManager.class);
    private static RedisClusterConnectionManager instance;
    private boolean redisClusterMode = IniUtil.getInstance().isRedisClusterMode();
//    private boolean redisClusterMode = true;

    private RedisClusterConnectionManager() {
    }

    public static RedisClusterConnectionManager getInstance() {
        if (instance == null) instance = new RedisClusterConnectionManager();
        return instance;
    }

    public void addClient(RedisConnectionInfoRemote redisConnectionInfoRemote) {
        if (!redisClusterMode) return;

        if (redisConnectionInfoRemote == null) {
            return;
        }

        RedisClient redisClient = RedisPool.getInstance().getRedisClient();
        String redisConnectionInfoRemoteString = redisConnectionInfoRemote.toJsonObject().toString();
        redisClient.set(redisConnectionInfoRemote.getUserID(), redisConnectionInfoRemoteString, v -> {
            if (v.failed()) {
                logger.error(String.format("RedisClusterConnectionManager.addClient redis add key:%s value:%s error", redisConnectionInfoRemote.getUserID(),
                        redisConnectionInfoRemoteString));
                return;
            }
            logger.info(String.format("RedisClusterConnectionManager.addClient redis add key:%s value:%s ", redisConnectionInfoRemote.getUserID(),
                    redisConnectionInfoRemoteString));
        });

        redisClient.sadd(IniUtil.getInstance().getHostID(), redisConnectionInfoRemote.getUserID(), result -> {
            if (result.failed()) {
                logger.error(String.format("RedisClusterConnectionManager.addClient redis sadd key:%s value:%s error", IniUtil.getInstance().getHostID(),
                        redisConnectionInfoRemote.getUserID()));
                return;
            }
            logger.info(String.format("RedisClusterConnectionManager.addClient redis sadd key:%s value:%s success", IniUtil.getInstance().getHostID(),
                    redisConnectionInfoRemote.getUserID()));
        });

    }

    public void removeClient(String userID) {
        if (!redisClusterMode) return;

        if (userID == null) {
            return;
        }

        RedisClient redisClient = RedisPool.getInstance().getRedisClient();
        redisClient.del(userID, v -> {
            if (v.failed()) {
                logger.error(String.format("RedisClusterConnectionManager.removeClient redis del key:%s error", userID));
                return;
            }
            logger.info(String.format("RedisClusterConnectionManager.removeClient redis del key:%s", userID));
        });


        redisClient.srem(IniUtil.getInstance().getHostID(), userID, result -> {
            if (result.failed()) {
                logger.error(String.format("RedisClusterConnectionManager.removeClient redis sremove key:%s member:%s error", IniUtil.getInstance().getHostID(),
                        userID));
                return;
            }
            logger.info(String.format("RedisClusterConnectionManager.removeClient redis sremove key:%s member:%s success", IniUtil.getInstance().getHostID(),
                    userID));
        });
    }

    public void getClient(String userID, Handler<RedisConnectionInfoRemote> handler) {
        if (!redisClusterMode) return;

        RedisClient jedis = RedisPool.getInstance().getRedisClient();
        jedis.get(userID, v -> {
            if (v.failed()) {
                logger.error(String.format("RedisClusterConnectionManager.getClient redis key:%s value:%s error", userID, v));
                handler.handle(null);
                return;
            }
            logger.info(String.format("RedisClusterConnectionManager.getClient redis key:%s value:%s success", userID, v));

            if (v.result() == null) {
                logger.info(String.format("RedisClusterConnectionManager.getClient redis key:%s value:%s", userID, null));
                handler.handle(null);
                return;
            }
            RedisConnectionInfoRemote remote = RedisConnectionInfoRemote.parse(v.result());
            handler.handle(remote);
        });
    }

    public void getHostClients(String hostID, Handler<JsonArray> handler) {
        if (!redisClusterMode) return;

        RedisClient jedis = RedisPool.getInstance().getRedisClient();
        jedis.smembers(hostID, v -> {
            if (v.failed()) {
                logger.error(String.format("RedisClusterConnectionManager.getHostClients redis key:%s value:%s error", hostID, v));
                handler.handle(null);
                return;
            }

            if (v.result() == null) {
                logger.info(String.format("RedisClusterConnectionManager.getHostClients redis key:%s value:%s", hostID, null));
                handler.handle(null);
                return;
            }
            logger.info(String.format("RedisClusterConnectionManager.getHostClients redis key:%s value:%s", hostID, v.result()));
            handler.handle(v.result());
        });
    }


}