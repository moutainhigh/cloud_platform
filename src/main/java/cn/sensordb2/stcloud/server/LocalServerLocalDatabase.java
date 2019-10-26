package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.core.VertxInstance;
import cn.sensordb2.stcloud.httpApiServer.HttpApiServer;
import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.socketIOServer.HYSocketIOServer;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.Vertx;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Date;

public class LocalServerLocalDatabase {
    private static String LOG_FILE = System.getenv("HY_HOME")==null?
            "./cloud/conf/log4j.properties":System.getenv("HY_HOME")+"/cloud/conf/log4j.properties";
    public static Logger logger = Logger.getLogger(LocalServerLocalDatabase.class);

    public static void main(String[] args) {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4jLogDelegateFactory");
        Tools.initSystemProperties();
        PropertyConfigurator.configure(LOG_FILE);
//		java.util.logging.Logger.getLogger("com.mongodb").setLevel(Level.OFF);
//
        logger.info("@@@@@@@@@@@@@@@" + Tools.dateToStr(new Date()) + " cloud local server with local database config file startup");

        Vertx vertx = Vertx.vertx();
        VertxInstance.createInstance(vertx);
        IniUtil.changeIniUtil("./cloud/conf/configLocalDebug.ini");
        Globals.setLocalDatabaseConfFile();

        String databaseConfFile = Globals.getDatabaseConfFile();
        Database database = Database.createInstance(vertx, databaseConfFile);

        Server.createInstance(vertx, database).run();
        HttpApiServer.createInstance(vertx, database).run();

        HYSocketIOServer.getInstance().run();
    }

}