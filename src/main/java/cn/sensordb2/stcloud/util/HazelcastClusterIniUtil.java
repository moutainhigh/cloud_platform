package cn.sensordb2.stcloud.util;

import org.apache.log4j.Logger;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

public class HazelcastClusterIniUtil {
	private static Logger logger = Logger.getLogger(HazelcastClusterIniUtil.class);
	private static HazelcastClusterIniUtil instance;
	private static String initFilePath = System.getenv("HY_HOME")==null?
			"./cloud/conf/hazelcastCluster.ini":System.getenv("HY_HOME")+"/cloud/conf/hazelcastCluster.ini";
	private static String HazelcastCluster = "hazelcastCluster";
	private Wini ini;

	private HazelcastClusterIniUtil() {
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
	
	public static HazelcastClusterIniUtil getInstance() {
		if(instance==null) {
			instance =  new HazelcastClusterIniUtil();
		}
		return HazelcastClusterIniUtil.instance;
	}

	public String[] getMembers() {
		String members = ini.get(HazelcastCluster, "members");
		String[] results = members.split(",");
		return results;
	}

	public static void main(String[] args) {
		String[] members = HazelcastClusterIniUtil.getInstance().getMembers();
		System.out.println(members);
	}

}
