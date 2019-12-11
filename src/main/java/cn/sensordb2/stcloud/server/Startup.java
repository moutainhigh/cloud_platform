package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.core.VertxInstance;
import cn.sensordb2.stcloud.httpApiServer.HttpApiServer;
import cn.sensordb2.stcloud.ros.RosInstance;
import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.socketIOServer.HYSocketIOServer;
import cn.sensordb2.stcloud.util.Tools;
import edu.wpi.rail.jrosbridge.Ros;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Date;

public class Startup {

    private static String LOG_FILE = System.getenv("HY_HOME") == null ?
            "./cloud/conf/log4j.properties"
            : System.getenv("HY_HOME") + "/cloud/conf/log4j.properties";
    public static Logger logger = Logger.getLogger(Startup.class);

    public static void main(String[] args) {
        System.setProperty("vertx.logger-delegate-factory-class-name",
                "io.vertx.core.logging.Log4jLogDelegateFactory");
        Tools.initSystemProperties();
        PropertyConfigurator.configure(LOG_FILE);

        logger.info("@@@@@@@@@@@@@@@" + Tools.dateToStr(new Date()) + " cloud server startup");
        Vertx vertx = Vertx.vertx();

        VertxInstance.createInstance(vertx);

        String databaseConfFile = Globals.getDatabaseConfFile();
        Database database = Database.createInstance(vertx, databaseConfFile);
        //初始化无人机可用状态
        database.getMongoClient().dropCollection("AvailableUavs", res -> {
        });
        database.getMongoClient().createCollection("AvailableUavs", res -> {
        });
        database.getMongoClient()
                .insert("AvailableUavs", new JsonObject().put("uav", "firefly5").put("flag", "0"),
                        res -> {
                        })
                .insert("AvailableUavs", new JsonObject().put("uav", "firefly6").put("flag", "0"),
                        res -> {
                        })
                .insert("AvailableUavs", new JsonObject().put("uav", "firefly7").put("flag", "0"),
                        res -> {
                        });

        //启动http服务器
        try {
            Server server = Server.createInstance(vertx, database);
            server.run();
        } catch (Exception e) {
            logger.error(String.format("TcpServer raise an exception:%s", Tools.getTrace(e)));
        }

        try {
            //初始化ros对象,与ros Master 建立连接
            logger.info("初始化rosInstance");
            Ros ros = RosInstance.getInstance().getRos();
            logger.info("与ros Master 通过rosBridge建立 websocket连接");
            ros.connect();
            //启动socket服务器
            HYSocketIOServer.getInstance().run();
        } catch (Exception e) {
            logger.error(
                    String.format("HYSocketIOServer raise an exception:%s", Tools.getTrace(e)));
        }

//		try {
//			HttpApiServer.createInstance(vertx, database).run();
//		} catch (Exception e) {
//			logger.error(String.format("HttpApiServer raise an exception:%s", Tools.getTrace(e)));
//		}
    }


}
