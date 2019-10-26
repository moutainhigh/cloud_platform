package cn.sensordb2.stcloud.server.common;

public class AccountDeviceRelationShipType {
	public static final int OWN = 1;
	public static final int SHARE = 2;

	public static boolean isOwn(int relationShip) {
		return AccountDeviceRelationShipType.OWN == relationShip;
	}

}
