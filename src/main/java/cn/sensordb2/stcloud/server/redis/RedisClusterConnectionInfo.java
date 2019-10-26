package cn.sensordb2.stcloud.server.redis;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.message.Response;
import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.buffer.Buffer;
import io.vertx.redis.RedisClient;

/**
 * Created by sensordb on 16/7/13.
 */
public class RedisClusterConnectionInfo extends ConnectionInfo {
    public static HYLogger logger = HYLogger.getLogger(ConnectionInfo.class);
    private RedisConnectionInfoRemote connectionInfoRemote;

    public RedisClusterConnectionInfo(RedisConnectionInfoRemote connectionInfoRemote) {
        this.connectionInfoRemote = connectionInfoRemote;
    }

    public void write(String s) {
        RedisClient redis = RedisPool.getInstance().getRedisClient();

        if(connectionInfoRemote.getUserID()==null) {
            logger.error(String.format("publish to user null a message error"));
            return;
        }
        if(s==null) {
            logger.error(String.format("publish to user:%s a message:null error", connectionInfoRemote.getUserID()));
            return;
        }

        redis.publish(connectionInfoRemote.getUserID(), s, result-> {
            if (result.failed()) {
                logger.error(String.format("publish to user:%s message:%s error", connectionInfoRemote.getUserID(),
                        s));
                return;
            }
            logger.info(String.format("publish to user:%s message:%s success", connectionInfoRemote.getUserID(),
                    s));
        });
    }

    public void write(Response response) {
        this.write(response.toString());
    }

    public void write(Buffer buffer) {
        this.write(buffer.toString());
    }
}
