package cn.sensordb2.stcloud.server.common;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Vector;

/*
{
    "zl_cloud": "1.0",
    "result": {
        "owned_devs": [
            {
                "dev_id": 1#,
                "type": 2#,
                "nick_name": 3#,
                "shared_to": [
                    {
                        "user_id": 4#,
                        "nick_name": 5#
                    }
                ],
                "online_status":6#
            }
        ],
        "external_available_devs": [
            {
                "dev_id": 7#,
                "type": 8#,
                "nick_name": 9#,
                "from": {
                    "user_id": 10#,
                    "nick_name": 11#
                }��
              "online_status":12#
            }
        ]
    },
    "id": 13#
}

 */
public class OwnedDev {
    private String devId;
    private int type;
    private String nickName;
    private String mac;
    private boolean online = false;
    private boolean remoteSwitch = false;
    private JsonObject pos;
    private Vector<AppUserInfo> sharedToAppUsers = new Vector();

    public OwnedDev(String devId, int type, String nickName, JsonObject pos) {
        super();
        this.devId = devId;
        this.type = type;
        this.nickName = nickName;
        this.pos = pos;
    }

    public boolean isRemoteSwitch() {
        return remoteSwitch;
    }

    public void setRemoteSwitch(boolean remoteSwitch) {
        this.remoteSwitch = remoteSwitch;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void addAppUserInfo(AppUserInfo appUserInfo) {
        this.sharedToAppUsers.add(appUserInfo);
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public JsonObject toJsonObject() {
        JsonObject result = new JsonObject();
        result.put("dev_id", this.devId);
        result.put("type", AccountType.getAccountTypeString(this.type));
        result.put("nick_name", this.nickName);
        result.put("mac", this.mac);
        result.put("online_status", this.isOnline() ? 1 : 0);
        result.put("remoteSwitch", this.isRemoteSwitch() ? 1 : 0);

        JsonArray sharedTo = new JsonArray();
        result.put("shared_to", sharedTo);

        if (this.pos != null) {
            result.put("pos", this.pos);
        }

        for (AppUserInfo appUserInfo : this.sharedToAppUsers) {
            sharedTo.add(appUserInfo.toJsonObject());
        }
        return result;
    }
}
