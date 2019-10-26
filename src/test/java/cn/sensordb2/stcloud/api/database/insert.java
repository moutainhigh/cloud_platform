package cn.sensordb2.stcloud.api.database;

import cn.sensordb2.stcloud.core.Database;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class insert {
    public static void main(String[] args){
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject jsonObject = new JsonObject().put("user","cgy");
        mongoClient.insert("droneuser",jsonObject,res->{
           if(res.failed()){
               System.out.println(11111);
           }else{
               System.out.println(22222);
           }
        });
    }
}
