package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.common.AccountType;
import cn.sensordb2.stcloud.server.common.ConnectionProtocolType;
import cn.sensordb2.stcloud.server.verticle.AddressUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * Created by sensordb on 16/7/13.
 */
public class VerticleConnectionInfoRemote implements Serializable{
    String verticleConnectionAddress;
    String userID;
    boolean isTemp; //
    int connectionID;
    int accountType=-1;

    String tempType; //
    String phoneNumber; //
    int type = ConnectionProtocolType.UNKNOWN; //
    String terminalType;
    String pushId;
    String deviceToken;


    public VerticleConnectionInfoRemote(String verticleConnectionAddress) {
        this.verticleConnectionAddress = verticleConnectionAddress;
    }

    public VerticleConnectionInfoRemote(ConnectionInfo ci) {
        this.setVerticleConnectionAddress(AddressUtil.getConnectionAddress(ci.getConnectionID()));
        this.setUserID(ci.getUserID());
        this.setIsTemp(ci.isTemp());
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

    public String getVerticleConnectionAddress() {
        return verticleConnectionAddress;
    }

    public void setVerticleConnectionAddress(String verticleConnectionAddress) {
        this.verticleConnectionAddress = verticleConnectionAddress;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isTemp() {
        return isTemp;
    }

    public void setIsTemp(boolean isTemp) {
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


    public boolean isBinaryConnection() {
        return this.type==ConnectionProtocolType.BINARY;
    }

    public JsonObject toJsonObject() {
        JsonObject result = new JsonObject();
        result.put("userID", Tools.objectToString(userID));
        result.put("isTemp", isTemp);
        result.put("tempType", Tools.objectToString(tempType));
        result.put("phoneNumber", Tools.objectToString(phoneNumber));
        result.put("accountType", this.getAccountString());
        result.put("type", this.getTypeString());
        return result;
    }
}
