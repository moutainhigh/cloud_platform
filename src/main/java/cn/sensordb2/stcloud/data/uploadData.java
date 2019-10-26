package cn.sensordb2.stcloud.data;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.util.PushMessageUtil;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;

public class uploadData extends RequestHandler {
    public static Logger logger = Logger.getLogger(uploadData.class);


//    public void handle(RoutingContext routingContext, Request request) {
//        routingContext.response().putHeader("content-type","application/json");
//        final MongoClient mongoClient = Server.getInstance().getMongoClient();
//
//        JsonArray jsonArray = request.getJsonObject("params").getJsonArray("recordList");
//
//        if(jsonArray.isEmpty()){
//            //什么也没传过来。应该还有token验证功能，但是现阶段不用写。
//            logger.info("数组为空");
//            return;
//        }else{
//            final int pos = jsonArray.size();
//            for(int i = 0;i<pos;i++){
//                final int successedPos = i;
//                mongoClient.insert("data",jsonArray.getJsonObject(i),result->{
//                    if(result.failed()){
//                        //失败处理
//                        //routingContext.response().end(ResponseFactory
//                        //        .error(-1, ResponseErrorCode.DECODE_ERROR,"服务器内部错误").toString());
//                        uploadData.logger.error(String.format("query:%s exception:%s",result.cause()));
//                        return;
//                    }else{
//                        //成功处理
//                        if(successedPos == pos - 1) {
//                            routingContext.response().end();
//                            //UploadData.logger.info(String.format("successed insert in Database："
//                             //       , jsonArray.getJsonObject(successedPos).getValue("time")));
//                        }
//                    }
//                });
//            }
//        }
//    }

    @Override
    public void handle(ConnectionInfo connectionInfo, Request request) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        JsonObject jsonObject = request.getParams();
        mongoClient.insert("data",jsonObject,res->{
            if(res.failed()){
                //失败处理
                //routingContext.response().end(ResponseFactory
                //        .error(-1, ResponseErrorCode.DECODE_ERROR,"服务器内部错误").toString());
                uploadData.logger.error(String.format("query:%s exception:%s",res.cause()));
                return;
            }else{
                //成功处理
                JsonObject result = new JsonObject();
                result.put("code",1);
                result.put("message","upload success");
                PushMessageUtil.pushMessageToSocketio(connectionInfo,request,jsonObject,connectionInfo.getFrom(), "RELAY_MSG", respush->{
                    uploadData.logger.info("push to"+connectionInfo.getTo()+"success!");
                });
                ResponseHandlerHelper.success(connectionInfo, request,result);
                return;
                }
        });

    }
}
