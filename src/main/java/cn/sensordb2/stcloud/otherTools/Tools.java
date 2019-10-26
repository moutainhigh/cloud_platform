package cn.sensordb2.stcloud.otherTools;


import io.vertx.core.json.JsonObject;

/**
 * Created by sensordb on 16/12/31.
 */
public class Tools {
    public static String jsonToUrlQueryString(JsonObject jsonObject) {
        StringBuffer sb = new StringBuffer();

        boolean isFirst = true;
        for (String key : jsonObject.fieldNames()) {
            if(!isFirst) sb.append("&");
            sb.append(key);
            sb.append("=");
            sb.append(jsonObject.getString(key));
            isFirst = false;
        }

        return sb.toString();
    }
}
