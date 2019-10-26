package cn.sensordb2.stcloud.server.redis;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class RedisIniUtil {
	private static String initFilePath =  System.getenv("HY_HOME")==null?
			"./cloudRedis/conf/redis.ini":System.getenv("HY_HOME")+"/cloudRedis/conf/redis.ini";
	private static RedisIniUtil instance;
	private static String Redis = "redis";
	private Wini ini;
	
	private RedisIniUtil() {
		try {
			ini = new Wini(new File(initFilePath));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static RedisIniUtil getInstance() {
		if(instance==null) {
			instance =  new RedisIniUtil();
		}
		return instance;
	}
	
	public String getServer() {
		return ini.get(Redis, "server");
	}
	
	public String getPassword() {
		return ini.get(Redis, "password");
	}
	
	
	public int getPort() {
		try {
			int port = Integer.parseInt(ini.get(Redis, "port"));
			return port;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 80;
		}
	}

	public int getDatabaseIndex() {
		try {
			int databaseIndex = Integer.parseInt(ini.get(Redis, "databaseIndex"));
			return databaseIndex;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	public boolean isRedisTurnOn() {
		int turnOn = Integer.parseInt(ini.get(Redis, "turnOn"));
		if(turnOn!=0) return true;
		else return false;
	}

}
