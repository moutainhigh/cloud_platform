package cn.sensordb2.stcloud.server.redis;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.common.ConnectionProtocolType;
import cn.sensordb2.stcloud.server.common.AccountType;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * Created by sensordb on 16/7/13.
 */
public class RedisConnectionInfoRemote implements Serializable{
    public static HYLogger logger = HYLogger.getLogger(RedisConnectionInfoRemote.class);

    String userID;
    int isTemp; //
    int connectionID;
    int accountType=-1;

    String tempType; //
    String phoneNumber; //
    int type = ConnectionProtocolType.UNKNOWN; //
    String terminalType;
    String pushId;
    String deviceToken;
    String host;
    int port;

    public RedisConnectionInfoRemote() {
    }

    public RedisConnectionInfoRemote(ConnectionInfo ci) {
        this.setUserID(ci.getUserID());
        this.setIsTemp(ci.isTemp() ? 1 : 0);
        this.setConnectionID(ci.getConnectionID());

        this.setAccountType(ci.getAccountType());
        this.setTempType(ci.getTempType());
        this.setPhoneNumber(ci.getPhoneNumber());
        this.setType(ci.getType());
        this.setTerminalType(ci.getTerminalType());
        this.setDeviceToken(ci.getDeviceToken());
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(int isTemp) {
        this.isTemp = isTemp;
    }

    public int getConnectionID() {
        return connectionID;
    }

    public void setConnectionID(int connectionID) {
        this.connectionID = connectionID;
    }

    public String getTempType() {
        return tempType;
    }

    public void setTempType(String tempType) {
        this.tempType = tempType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getAccountString() {
        return AccountType.getAccountTypeString(this.accountType);
    }

    public String getTypeString() {
        if (this.isBinaryConnection()) {
            return "Binary";
        }
        else return "Json";
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isBinaryConnection() {
        return this.type==ConnectionProtocolType.BINARY;
    }

    public JsonObject toJsonObject() {
        JsonObject result = new JsonObject();
        result.put("userID", Tools.objectToString(userID));
        result.put("isTemp", isTemp);
        result.put("connectionID", connectionID);
        result.put("accountType", this.accountType);
        result.put("tempType", Tools.objectToString(tempType));
        result.put("phoneNumber", Tools.objectToString(phoneNumber));
        result.put("type", type);
        result.put("terminalType", Tools.objectToString(this.terminalType));
        result.put("pushId", Tools.objectToString(this.pushId));
        result.put("deviceToken", Tools.objectToString(this.deviceToken));
        result.put("host", Tools.objectToString(this.host));
        result.put("port", this.port);
        return result;
    }

    public static RedisConnectionInfoRemote parse(String str) {
        try {
            JsonObject jsonObject = new JsonObject(str);
            RedisConnectionInfoRemote remote = new RedisConnectionInfoRemote();
            remote.setUserID(Tools.stringToObject(jsonObject.getString("userID")));
            remote.setIsTemp(jsonObject.getInteger("isTemp"));
            remote.setConnectionID(jsonObject.getInteger("connectionID"));
            remote.setAccountType(jsonObject.getInteger("accountType"));

            remote.setTempType(Tools.stringToObject(jsonObject.getString("tempType")));
            remote.setPhoneNumber(Tools.stringToObject(jsonObject.getString("phoneNumber")));
            remote.setType(jsonObject.getInteger("type"));
            remote.setTerminalType(Tools.stringToObject(jsonObject.getString("terminalType")));
            remote.setPushId(Tools.stringToObject(jsonObject.getString("pushId")));
            remote.setDeviceToken(Tools.stringToObject(jsonObject.getString("deviceToken")));
            remote.setHost(Tools.stringToObject(jsonObject.getString("host")));
            remote.setPort(jsonObject.getInteger("port"));

            return remote;
        } catch (Exception e) {
            logger.exception(Tools.getTrace(e));
            logger.error(String.format("RedisConnectionInfoRemote parse:%s error", str));
            return null;
        }
    }


}
