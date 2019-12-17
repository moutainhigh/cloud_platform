package cn.sensordb2.stcloud.util;

import java.util.Vector;
import org.apache.log4j.Logger;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class IniUtil {
	private static Logger logger = Logger.getLogger(IniUtil.class);
	private static IniUtil instance;
	private static String initFilePath = System.getenv("HY_HOME")==null?
			"./cloud/conf/config.ini":System.getenv("HY_HOME")+"/cloud/conf/config.ini";
	private static String Server = "server";
	private static String Uav = "uav";
	private static String SocketIO = "socketIO";
	private static String Other = "other";
	private static String Debug = "debug";
	private static String Log = "log";
	private static String ShortMessageService = "shortMessageService";
	private static String OssStorage = "ossStorage";
	private static String Apns = "apns";
	private static String Push = "push";
	private Wini ini;
	private String hostID;

	public static final int TEST_MODE = 0;
	public static final int LOCAL_DEBUG_MODE = 1;
	public static final int PRODUCT_MODE = 2;

	private IniUtil() {
		try {
			ini = new Wini(new File(initFilePath));
			hostID = this.getServerHostName()+":"+this.getServerPort();
			System.err.println(String.format("initFilePath:%s", initFilePath));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * for debug, comment these functions while release
	 */
	public IniUtil(String initFilePath) {
		try {
			ini = new Wini(new File(initFilePath));
			hostID = this.getServerHostName()+":"+this.getServerPort();
			System.err.println(String.format("initFilePath:%s", initFilePath));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setInstance(IniUtil iniUtil) {
		IniUtil.instance = iniUtil;
	}

	/*
	    IniUtil.changeIniUtil("./cloud/conf/configTestNode.ini");
        IniUtil.changeIniUtil("./cloud/conf/configLocalDebugNode.ini");
	 */
	public static void changeIniUtil(String initFilePath) {
		IniUtil iniUtil = new IniUtil(initFilePath);
		IniUtil.setInstance(iniUtil);
	}

	public static void changeToTestServer() {
		IniUtil.changeIniUtil("./cloud/conf/configTestNode.ini");
	}

	public static void changeToProductServer() {
		IniUtil.changeIniUtil("./cloud/conf/configProductNode.ini");
	}

	public static void changeToLocalDebugServer() {
		IniUtil.changeIniUtil("./cloud/conf/configLocalDebug.ini");
	}

	public static void changeMode(int mode) {
		if (mode == IniUtil.TEST_MODE) {
			IniUtil.changeToTestServer();
		}
		else if(mode == IniUtil.LOCAL_DEBUG_MODE) {
			IniUtil.changeToLocalDebugServer();
		}
		else if(mode == IniUtil.PRODUCT_MODE) {
			IniUtil.changeToProductServer();
		}
	}

	/*
	 * for debug, comment the above  functions while release
	 */
	public static IniUtil getInstance() {
		if(instance==null) {
			instance =  new IniUtil();
		}
		return IniUtil.instance;
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

	public int getSocketIOServerPort() {
		try {
			int port = Integer.parseInt(ini.get(SocketIO, "port"));
			return port;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 49090;
		}
	}


	public int getServerPortSsl() {
		try {
			return Integer.parseInt(ini.get(Server, "portSsl"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 80;
		}
	}


	public boolean isServerTcpEnable() {
		int httpsEnable = Integer.parseInt(ini.get(Server, "tcpEnable"));
		if(httpsEnable!=0) return true;
		else return false;
	}

	public boolean isCheckRequestParam() {
		int checkRequestParam = Integer.parseInt(ini.get(Server, "checkRequestParam"));
		if(checkRequestParam!=0) return true;
		else return false;
	}


	public boolean isServerSslEnable() {
		int httpsEnable = Integer.parseInt(ini.get(Server, "sslEnable"));
		if(httpsEnable!=0) return true;
		else return false;
	}

	public int getSocketIOServerPortSsl() {
		try {
			return Integer.parseInt(ini.get(SocketIO, "portSsl"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 80;
		}
	}

	public boolean isSocketIOServerEnable() {
		int httpsEnable = Integer.parseInt(ini.get(SocketIO, "sslEnable"));
		if(httpsEnable!=0) return true;
		else return false;
	}


	public boolean isVerificationCodeSame() {
		int verificationCodeSame = Integer.parseInt(ini.get(Debug, "verificationCodeSame"));
		if(verificationCodeSame!=0) return true;
		else return false;
	}

	public boolean isLoginRandomDataSame() {
		int loginRandomDataSame = 0;
		try {
			loginRandomDataSame = Integer.parseInt(ini.get(Debug, "loginRandomDataSame"));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if(loginRandomDataSame!=0) return true;
		else return false;
	}

	public boolean isAllInOneLogFile() {
		int allInOneLogFile = Integer.parseInt(ini.get(Log, "allInOneLogFile"));
		if(allInOneLogFile!=0) return true;
		else return false;
	}

	public int getServerInstancesNum() {
		try {
			return Integer.parseInt(ini.get(Server, "instancesNum"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
	}

	public int getVerificationCodeTimeGap() {
		try {
			return Integer.parseInt(ini.get(Other, "verificationCodeTimeGap"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 60;
		}
	}

	public int getLoginRandomDataTimeGap() {
		try {
			return Integer.parseInt(ini.get(Other, "loginRandomDataTimeGap"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 60;
		}
	}

	public int getRandomDataTimeGap() {
		try {
			return Integer.parseInt(ini.get(Other, "randomDataTimeGap"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 60;
		}
	}

	public int getVerificationCodeTimeGapForCreateAccount() {
		try {
			return Integer.parseInt(ini.get(Other, "verificationCodeTimeGapForCreateAccount"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 60;
		}
	}

	public boolean isLogAllRequestString() {
		try {
			return Integer.parseInt(ini.get(Debug, "logAllRequestString"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public Set<String> getTestPhoneNum() {

		String testPhoneNum = ini.get(Debug, "testPhoneNum");

		if (testPhoneNum == null) {
			return null;
		}

		try {
			String[] testPhoneNums = testPhoneNum.split(",");
			HashSet<String> testPhoneNumsHashSet = new HashSet<String>();
			for (String ph : testPhoneNums) {
                testPhoneNumsHashSet.add(ph);
            }
			return testPhoneNumsHashSet;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getTestVerificationCode() {
		return ini.get(Debug, "testVerificationCode");
	}

	public String getJsonVersionFieldValue() {
		if(ini.get(Other, "jsonVersionFieldValue")==null) {
			return "1.0";
		}
		else {
			return ini.get(Other, "jsonVersionFieldValue");
		}
	}

	public String getJsonVersionFieldName() {
		if(ini.get(Other, "jsonVersionFieldName")==null) {
			return "cloud";
		}
		else {
			return ini.get(Other, "jsonVersionFieldName");
		}
	}

	public boolean isLogInOutDataToDatabase() {
		try {
			return Integer.parseInt(ini.get(Debug, "logInOutDataToDatabase"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean isLogPushToDatabase() {
		try {
			return Integer.parseInt(ini.get(Debug, "logPushToDatabase"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

	public boolean isLogBuffer() {
		try {
			return Integer.parseInt(ini.get(Log, "logBuffer"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean isIgnoreBeforeLogin() {
		try {
			return Integer.parseInt(ini.get(Log, "ignoreBeforeLogin"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

	public boolean isLogHeartBeatSession() {
		try {
			return Integer.parseInt(ini.get(Log, "logHeartBeatSession"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

	public boolean islogRequesResponse() {
		try {
			return Integer.parseInt(ini.get(Log, "logRequesResponse"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean isLogReceived() {
		try {
			return Integer.parseInt(ini.get(Log, "logReceived"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

	public boolean isLogErrorToDatabase() {
		try {
			return Integer.parseInt(ini.get(Log, "logErrorToDatabase"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

	public boolean isClientInOneFile() {
		try {
			return Integer.parseInt(ini.get(Log, "clientInOneFile"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean isClusterMode() {
		try {
			return Integer.parseInt(ini.get(Server, "clusterMode"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean isRedisClusterMode() {
		try {
			return Integer.parseInt(ini.get(Server, "redisClusterMode"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean isRecordCallMethodSpendTime() {
		try {
			return Integer.parseInt(ini.get(Debug, "recordCallMethodSpendTime"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean isRecordTcpConnection() {
		try {
			return Integer.parseInt(ini.get(Debug, "recordTcpConnection"))!=0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public String getRecordMethods() {
		try {
			return ini.get(Debug, "recordMethods");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public int getHeartBeatGap() {
		try {
			return Integer.parseInt(ini.get(Server, "heartBeatGap"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 10;
		}
	}

	public int getHeartBeatTimeout() {
		try {
			return Integer.parseInt(ini.get(Server, "heartBeatTimeout"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 30;
		}
	}

	public String getIgnoreUserName() {
		return ini.get(Log, "ignoreUserName");
	}

	public String getSmsSign() {
		return ini.get(ShortMessageService, "smsSign");
	}

	public String getSmsUserName() {
		return ini.get(ShortMessageService, "smsUserName");
	}

	public String getSmsPwd() {
		return ini.get(ShortMessageService, "smsPwd");
	}

	public String getSmsMSGContentPrefix() {
		return ini.get(ShortMessageService, "smsMSGContentPrefix");
	}

	public String getSmsHost() {
		return ini.get(ShortMessageService, "smsHost");
	}

	public String getSmsURL() {
		return ini.get(ShortMessageService, "smsURL");
	}

	public String getAccessKeyID() {
		return ini.get(OssStorage, "accessKeyID");
	}

	public String getAccessKeySecret() {
		return ini.get(OssStorage, "accessKeySecret");
	}

	public String getUrlPrefix() {
		return ini.get(OssStorage, "urlPrefix");
	}

	public String getBucketDir() {
		return ini.get(OssStorage, "bucketDir");
	}

	public String getApnsPassword() {
		String serverHostName = ini.get(Apns, "password");
		return serverHostName;
	}

	public String getCertificateFilePath() {
		String serverHostName = ini.get(Apns, "certificateFilePath");
		return serverHostName;
	}

	public String getAuthString() {
		String authString = ini.get(Push, "authString");
		return authString;
	}

	public String getPushHost() {
		String pushHost = ini.get(Push, "pushHost");
		return pushHost;
	}

	public String getDeviceHost() {
		String deviceHost = ini.get(Push, "deviceHost");
		return deviceHost;
	}


	public String getRequestURI() {
		String requestURI = ini.get(Push, "requestURI");
		return requestURI;
	}

	public int getApnsProduction() {
		try {
			return Integer.parseInt(ini.get(Push, "apnsProduction"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
	}

	public int getTimeToLive() {
		try {
			return Integer.parseInt(ini.get(Push, "timeToLive"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 60;
		}
	}

	public Hashtable<String, String> getTrackedUserIDInLog() {
		Hashtable<String, String> trackedUserIDInLog = new Hashtable<String, String>();

		String userIDsString = ini.get(Debug, "trackedUserIDInLog");
		if (userIDsString == null || userIDsString.trim().equals("")) {
			return null;
		}

		String[] userIDs = userIDsString.trim().split(",");

		for (String userID : userIDs) {
			trackedUserIDInLog.put(userID, userID);
		}
		return trackedUserIDInLog;
	}

	public String getHostID() {
		return this.hostID;
	}

    public <T> T get(Object sectionName, Object optionName, Class<T> clazz){
    	return ini.get(sectionName,optionName,clazz);
    }
    public String get(Object sectionName, Object optionName){
    	return ini.get(sectionName,optionName,String.class);
    }

	public String getServerHostName() {
		String serverHostName = ini.get(Server, "hostName");
		return serverHostName;
	}

	public String[] getUavNames() {
		String uavNames = ini.get(Uav, "uavNames");
		return uavNames.split(",");
	}
}
