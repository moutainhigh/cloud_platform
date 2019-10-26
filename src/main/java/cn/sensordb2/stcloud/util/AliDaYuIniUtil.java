package cn.sensordb2.stcloud.util;

import org.apache.log4j.Logger;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class AliDaYuIniUtil {
	private static Logger logger = Logger.getLogger(AliDaYuIniUtil.class);
	private static AliDaYuIniUtil instance;
	private static String initFilePath =  System.getenv("HY_HOME")==null?
			"./cloud/conf/aliDaYu.ini":System.getenv("HY_HOME")+"/cloud/conf/aliDaYu.ini";
	private static String Alidayu = "alidayu";
	private Wini ini;



	private AliDaYuIniUtil() {
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


	public String getSmsURL() {
		String smsURL = ini.get(Alidayu, "smsURL");
		return smsURL;
	}

	public String getAppKey() {
		String appKey = ini.get(Alidayu, "appKey");
		return appKey;
	}

	public String getAppSecret() {
		String appKey = ini.get(Alidayu, "appSecret");
		return appKey;
	}


	public static AliDaYuIniUtil getInstance() {
		if(instance==null) {
			instance =  new AliDaYuIniUtil();
		}
		return AliDaYuIniUtil.instance;
	}

	public String getServerHostName() {
		String host = ini.get(Alidayu, "host");
		return host;
	}

	public String getSmsTemplateCode() {
		String smsTemplateCode = ini.get(Alidayu, "smsTemplateCode");
		return smsTemplateCode;
	}

	public String getSmsFreeSignName() {
		String smsFreeSignName = ini.get(Alidayu, "smsFreeSignName");
		return smsFreeSignName;
	}

	public int getPort() {
		try {
			return Integer.parseInt(ini.get(Alidayu, "port"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 80;
		}
	}


}
