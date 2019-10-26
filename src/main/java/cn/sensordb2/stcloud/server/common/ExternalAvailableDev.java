package cn.sensordb2.stcloud.server.common;

import io.vertx.core.json.JsonObject;

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
                ]
            }
        ],
        "external_available_devs": [
            {
                "dev_id": 6#,
                "type": 7#,
                "nick_name": 8#,
                "from": {
                    "user_id": 9#,
                    "nick_name": 10#
                }
            }
        ]
    },
    "id": 11#
}

 */
public class ExternalAvailableDev {
    private String devId;
    private int type;
    private String nickName;
    private String mac;
    private boolean online = false;
    private boolean remoteSwitch = false;
    private JsonObject pos;

    private AppUserInfo fromAppUserInfo;

    public ExternalAvailableDev(String devId, int type, String nickName) {
        super();
        this.devId = devId;
        this.type = type;
        this.nickName = nickName;
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

    public AppUserInfo getFromAppUserInfo() {
        return fromAppUserInfo;
    }

    public void setFromAppUserInfo(AppUserInfo fromAppUserInfo) {
        this.fromAppUserInfo = fromAppUserInfo;
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

        if (this.pos != null) {
            result.put("pos", this.pos);
        }

        result.put("from", this.fromAppUserInfo.toJsonObject());
        return result;
    }

    public JsonObject getPos() {
        return pos;
    }

    public void setPos(JsonObject pos) {
        this.pos = pos;
    }
}
