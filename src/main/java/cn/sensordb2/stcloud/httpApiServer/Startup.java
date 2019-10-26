package cn.sensordb2.stcloud.httpApiServer;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.Vertx;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Date;
import java.util.logging.Level;

public class Startup {
	private static String LOG_FILE = System.getenv("HY_HOME")==null?
			"./httpApiServer/conf/log4j.properties":System.getenv("HY_HOME")+"/httpApiServer/conf/log4j.properties";
	public static Logger logger = Logger.getLogger(Startup.class);
	
	public static void main(String[] args) {
		Tools.initSystemProperties();
		System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4jLogDelegateFactory");
		PropertyConfigurator.configure(LOG_FILE);

		java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
		logger.info("@@@@@@@@@@@@@@@"+Tools.dateToStr(new Date())+" httpApiServer startup");
		try {
			Vertx vertx = Vertx.vertx();
			Globals.setDatabaseConfFile("httpApiServer/conf/database.conf");
			String databaseConfFile = Globals.getDatabaseConfFile();
			Database database = Database.createInstance(vertx, databaseConfFile);

			HttpApiServer server = HttpApiServer.createInstance(vertx, database);
			server.run();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			String es = Tools.getTrace(e);
			logger.error(es);
			logger.info("@@@@@@@@@@@@@@@"+Tools.dateToStr(new Date())+" httpApiServer exit with exception:"+es);
			return;
		}
	}

}
