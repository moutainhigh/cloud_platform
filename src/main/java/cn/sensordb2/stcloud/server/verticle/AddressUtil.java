package cn.sensordb2.stcloud.server.verticle;

import io.vertx.core.net.NetSocket;

/**
 * Created by sensordb on 16/3/23.
 */
public class AddressUtil {
    private static String ConnectionAddressPrefix = "con.";
    private static String VerticleAddressPrefix = "client.";

    public static String getConnectionAddress(int connectionID) {
        return ConnectionAddressPrefix+connectionID;
    }

    public static String getConnectionAddress(NetSocket netSocket) {
        int connectionID = AddressUtil.getConnectionID(netSocket);
        return AddressUtil.getConnectionAddress(connectionID);
    }

    public static String getClientAddress(NetSocket netSocket) {
        int connectionID = AddressUtil.getConnectionID(netSocket);
        return AddressUtil.getClientAddress(connectionID);
    }


    public static String getClientAddress(int connectionID) {
        return VerticleAddressPrefix+connectionID;
    }

    public static int getConnectionID(NetSocket netSocket) {
        return netSocket.hashCode();
    }
}
