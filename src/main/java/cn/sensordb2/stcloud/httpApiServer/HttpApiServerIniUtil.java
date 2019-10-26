package cn.sensordb2.stcloud.httpApiServer;

import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import org.apache.log4j.Logger;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class HttpApiServerIniUtil {
	private static Logger logger = Logger.getLogger(HttpApiServerIniUtil.class);
	private static HttpApiServerIniUtil instance;
	private static String initFilePath = System.getenv("HY_HOME")==null?
			"./httpApiServer/conf/config.ini":System.getenv("HY_HOME")+"/httpApiServer/conf/config.ini";
	private static String Server = "server";
	private static String Debug = "debug";
	private static String HTTPS = "https";
	private Wini ini;


	public static final int TEST_MODE = 0;
	public static final int LOCAL_DEBUG_MODE = 1;
	public static final int PRODUCT_MODE = 2;

	public static void changeIniUtil(String initFilePath) {
		IniUtil iniUtil = new IniUtil(initFilePath);
		IniUtil.setInstance(iniUtil);
	}

	public static void changeToTestServer() {
		HttpApiServerIniUtil.changeIniUtil("./httpApiServer/conf/configTestNode.ini");
	}

	public static void changeToProductServer() {
		HttpApiServerIniUtil.changeIniUtil("./httpApiServer/conf/configProductNode.ini");
	}

	public static void changeToLocalDebugServer() {
		HttpApiServerIniUtil.changeIniUtil("./httpApiServer/conf/configLocalDebugNode.ini");
	}

	public static void changeMode(int mode) {
		if (mode == HttpApiServerIniUtil.TEST_MODE) {
			HttpApiServerIniUtil.changeToTestServer();
		}
		else if(mode == HttpApiServerIniUtil.LOCAL_DEBUG_MODE) {
			HttpApiServerIniUtil.changeToLocalDebugServer();
		}
		else if(mode == HttpApiServerIniUtil.PRODUCT_MODE) {
			HttpApiServerIniUtil.changeToProductServer();
		}
	}

	private HttpApiServerIniUtil() {
		try {
			ini = new Wini(new File(initFilePath));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			logger.error(Tools.getTrace(e));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(Tools.getTrace(e));
		}
	}
	
	public static HttpApiServerIniUtil getInstance() {
		if(instance==null) {
			instance =  new HttpApiServerIniUtil();
		}
		return HttpApiServerIniUtil.instance;
	}
		
	public int getServerPort() {
		try {
			return Integer.parseInt(ini.get(Server, "port"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error(Tools.getTrace(e));
			return 80;
		}
	}
	
	public long getHttpBodyLimit() {
		try {
			return Long.parseLong(ini.get(Server, "httpBodyLimit"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error(Tools.getTrace(e));
			return 100000;
		}
	}

	
	public int getServerPortHttps() {
		try {
			return Integer.parseInt(ini.get(Server, "portHttps"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error(Tools.getTrace(e));
			return 80;
		}
	}
	
	public int getServerInstancesNum() {
		try {
			return Integer.parseInt(ini.get(Server, "instancesNum"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error(Tools.getTrace(e));
			return 1;
		}
	}


	public String getStorageDir() {
		return ini.get(Server, "storageDir");
	}

	
	public String getServerURL() {
		return ini.get(Server, "url");
	}
	
	public String getServerVersion() {
		return ini.get(Server, "version");
	}
	
	public boolean isServerHttpsEnable() {
		int httpsEnable = Integer.parseInt(ini.get(Server, "httpsEnable"));
		if(httpsEnable!=0) return true;
		else return false;
	}
	
	public boolean isShowHttpBodyData() {
		int isShowHttpBodyData = Integer.parseInt(ini.get(Debug, "isShowHttpBodyData"));
		if(isShowHttpBodyData!=0) return true;
		else return false;
	}

	public boolean isServerHttpEnable() {
		int httpsEnable = Integer.parseInt(ini.get(Server, "httpEnable"));
		if(httpsEnable!=0) return true;
		else return false;
	}

	public String getHttpsPfxKeyCertFile() {
		return ini.get(HTTPS, "pfxKeyCertFile");
	}

	public String getHttpsPfxPassword() {
		return ini.get(HTTPS, "pfxPassword");
	}

	public String getServerHostName() {
		String serverHostName = ini.get(Server, "hostName");
		return serverHostName;
	}
}
