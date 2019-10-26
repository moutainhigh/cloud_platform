package cn.sensordb2.stcloud.util;

import org.apache.log4j.Logger;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class OpsServerIniUtil {
	private static Logger logger = Logger.getLogger(OpsServerIniUtil.class);
	private static OpsServerIniUtil instance;
	private static String initFilePath = System.getenv("HY_HOME")==null?
			"./cloud/conf/opsServerConfig.ini":System.getenv("HY_HOME")+"/cloud/conf/opsServerConfig.ini";
	private static String Server = "server";
	private Wini ini;

	private OpsServerIniUtil() {
		try {
			ini = new Wini(new File(initFilePath));
			System.err.println(String.format("initFilePath:%s", initFilePath));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static OpsServerIniUtil getInstance() {
		if(instance==null) {
			instance =  new OpsServerIniUtil();
		}
		return OpsServerIniUtil.instance;
	}
	
	
	public int getServerPort() {
		try {
			int port = Integer.parseInt(ini.get(Server, "port"));
			return port;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 80;
		}
	}


	public boolean isTurnOn() {
		int turnOn = 0;
		try {
			turnOn = Integer.parseInt(ini.get(Server, "turnOn"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if(turnOn!=0) return true;
		else return false;
	}


	public String getServerHostName() {
		String serverHostName = ini.get(Server, "hostName");
//		serverHostName = "127.0.0.1";
//		serverHostName = "182.92.108.214";
		//product
//		serverHostName = "115.28.239.71";
//		serverHostName = "product.cloud.rbcloudtech.com";
//		serverHostName = "60.205.15.249";
		return serverHostName;
	}
}
