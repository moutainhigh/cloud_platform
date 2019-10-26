package cn.sensordb2.stcloud.server.verticle;

import cn.sensordb2.stcloud.util.Tools;
import com.hazelcast.config.*;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Created by sensordb on 16/7/14.
 */
public class HazelcastCluster {
    public static void main(String[] args) {
        Config hazelcastConfig = new Config();
        NetworkConfig networkConfig = new NetworkConfig();
        hazelcastConfig.setNetworkConfig(networkConfig);
        JoinConfig joinConfig = new JoinConfig();
        networkConfig.setJoin(joinConfig);
        TcpIpConfig tcpIpConfig = new TcpIpConfig();
        tcpIpConfig.setEnabled(true);
        joinConfig.setTcpIpConfig(tcpIpConfig);
        joinConfig.getMulticastConfig().setEnabled(false);
        tcpIpConfig.addMember("115.28.239.71");
        tcpIpConfig.addMember("182.92.108.214");

// Now set some stuff on the config (omitted)

        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);

        VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
            } else {
                System.out.println(Tools.getTrace(res.cause()));
            }
        });
    }
}
