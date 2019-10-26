package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.core.VertxInstance;
import cn.sensordb2.stcloud.httpApiServer.HttpApiServer;
import cn.sensordb2.stcloud.server.common.Globals;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.socketIOServer.HYSocketIOServer;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.Vertx;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Date;

public class Startup {
	private static String LOG_FILE = System.getenv("HY_HOME")==null?
			"./cloud/conf/log4j.properties":System.getenv("HY_HOME")+"/cloud/conf/log4j.properties";
	public static Logger logger = Logger.getLogger(Startup.class);

	public static void main(String[] args) {
		System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.Log4jLogDelegateFactory");
		Tools.initSystemProperties();
		PropertyConfigurator.configure(LOG_FILE);

		logger.info("@@@@@@@@@@@@@@@" + Tools.dateToStr(new Date()) + " cloud server startup");
		Vertx vertx = Vertx.vertx();

		VertxInstance.createInstance(vertx);

		String databaseConfFile = Globals.getDatabaseConfFile();
		Database database = Database.createInstance(vertx, databaseConfFile);

		try {
			Server server = Server.createInstance(vertx, database);
			server.run();
		} catch (Exception e) {
			logger.error(String.format("TcpServer raise an exception:%s", Tools.getTrace(e)));
		}

		try {
			HYSocketIOServer.getInstance().run();
		} catch (Exception e) {
			logger.error(String.format("HYSocketIOServer raise an exception:%s", Tools.getTrace(e)));
		}

//		try {
//			HttpApiServer.createInstance(vertx, database).run();
//		} catch (Exception e) {
//			logger.error(String.format("HttpApiServer raise an exception:%s", Tools.getTrace(e)));
//		}
	}


}
