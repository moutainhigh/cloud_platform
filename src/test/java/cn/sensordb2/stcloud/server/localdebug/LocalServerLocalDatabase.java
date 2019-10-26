package cn.sensordb2.stcloud.server.localdebug;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.core.VertxInstance;
import cn.sensordb2.stcloud.httpApiServer.HttpApiServer;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.socketIOServer.HYSocketIOServer;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import cn.sensordb2.stcloud.server.common.Globals;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
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
//		try {
        Vertx vertx = Vertx.vertx();
        VertxInstance.createInstance(vertx);
//			IniUtil.changeMode(IniUtil.TEST_MODE);
        IniUtil.changeIniUtil("./cloud/conf/configLocalDebug.ini");
        Globals.setLocalDatabaseConfFile();

        String databaseConfFile = Globals.getDatabaseConfFile();
        Database database = Database.createInstance(vertx, databaseConfFile);
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        Server.createInstance(vertx, database).run();
        HttpApiServer.createInstance(vertx, database).run();

        HYSocketIOServer.getInstance().run();
        JsonObject jsonObject = new JsonObject().put("user","cgy");
        mongoClient.insert("droneuser",jsonObject,res->{
            if(res.failed()){
                System.out.println(11111);
            }else{
                System.out.println(22222);
            }
        });

//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			System.err.println("Uncaught exception - " + e.getMessage());
//	        e.printStackTrace(System.err);
//			e.printStackTrace();
//			String es = Tools.getTrace(e);
//			logger.error(es);
//			logger.info("@@@@@@@@@@@@@@@"+Tools.dateToStr(new Date())+" hycloud exit with exception:"+es);
//			return;
//		}
        //logger.info("@@@@@@@@@@@@@@@"+Tools.dateToStr(new Date())+" hycloud exit");
    }

}