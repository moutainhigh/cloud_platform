package cn.sensordb2.stcloud.util;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.common.Account;
import cn.sensordb2.stcloud.server.common.PhoneType;
import cn.sensordb2.stcloud.server.message.MsgPushMessage;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.message.IOSPushMessage;
import cn.sensordb2.stcloud.socketIOServer.WebsocketClientManager;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by sensordb on 16/5/28.
 */
public class PushMessageUtil {
    private static HYLogger logger = HYLogger.getLogger(PushMessageUtil.class);

    public static void pushMessage(ConnectionInfo connectionInfo, Request request, JsonObject message, String to,String type,
                                   Handler<Boolean> handler) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();
        //
        JsonObject userNameQuery = new JsonObject().put("username", to);
//        mongoClient.find(Account.accountTable, userNameQuery, res -> {
//            if (!res.succeeded()) {
//                logger.error(String.format("PushMessageUtil query user:%s terminal type error", to), connectionInfo);
//                return;
//            }
//            if (res.result().size() == 0) {
//                logger.error(String.format("PushMessageUtil query user:%s terminal type no user found", to), connectionInfo);
//                return;
//            }
//
//            String terminalType = res.result().get(0).getString("terminalType");
//            if (PhoneType.isIosDevice(terminalType)) {
//                String pushID = res.result().get(0).getString("pushID");
//                if (pushID == null || "-1".equals(pushID) || "".equals(pushID)) {
//                    logger.error(String.format("call jipush(%s) query ios user:%s pushID no pushID found so push error", to, to), connectionInfo);
//                }
//                else {
//                    logger.info(String.format("find ios userName:%s pushID:%s", to, pushID));
//                    IOSPushMessage ioPushMessage = IOSPushMessage.createIOSPushMessageForPushID(pushID, message.getString("title"));
//                    ioPushMessage.getMsgs().add(message);
//                    PushMessageTool.pushMessage(connectionInfo, Server.getInstance().getVertx(), ioPushMessage, to, result -> {
//                        handler.handle(result.booleanValue());
//                        if(result.booleanValue()) {
//                            logger.info(String.format("call jipush(%s) %s to ios %s isSuccess %s", to, ioPushMessage, to, result), connectionInfo);
//                        }
//                        else {
//                            logger.error(String.format("call jipush(%s) %s to ios %s isSuccess %s", to, ioPushMessage, to, result), connectionInfo);
//                        }
//
//                        if (IniUtil.getInstance().isLogPushToDatabase()) {
//                            JsonObject pushMsg = new JsonObject();
//                            pushMsg.put("request", request);
//                            pushMsg.put("ioPushMessage", ioPushMessage);
//                            pushMsg.put("to", to);
//                            if(connectionInfo!=null&&connectionInfo.getUserID()!=null)
//                                pushMsg.put("from", connectionInfo.getUserID());
//                            pushMsg.put("success", new Boolean(result.booleanValue()));
//
//                            mongoClient.save("pushMsgs", pushMsg, pushMsgRes1 -> {
//                                if (!pushMsgRes1.succeeded()) {
//                                    logger.error("save pushMsgs error: " + pushMsg.toString(), connectionInfo);
//                                    return;
//                                }
//                                logger.info(String.format("save pushMsgs %s success", pushMsg.toString()), connectionInfo);
//                            });
//
//                        }
//                    });
//                }
//            }
//        String clientid = to;
            ClientManager.getInstance().getConnectionInfo(to, toConnectionInfo-> {
                if (toConnectionInfo != null) {
                    MsgPushMessage msgPushMessage = new MsgPushMessage(type);
                    msgPushMessage.setMsg_content(message);
                    msgPushMessage.setFrom(connectionInfo.getUserID());
                    msgPushMessage.setId(request.getID());
                    ResponseHandlerHelper.pushWrite(connectionInfo, toConnectionInfo, request, msgPushMessage);
                    handler.handle(true);
                    logger.info(String.format("socket push(%s) %s to %s isSuccess %s", to, msgPushMessage, to, new Boolean(true)), connectionInfo);
                }

                if (toConnectionInfo == null) {
                    logger.error(String.format("push(%s) offline android usesName:%s isSuccess:%s", to, to, new Boolean(false)), connectionInfo);
                }
            });
    }

    public static void pushMessageToSocketio(ConnectionInfo connectionInfo, Request request, JsonObject message, String to,String type,
                                             Handler<Boolean> handler){
        WebsocketClientManager.getInstance().getConnectionInfo(to, toConnectionInfo-> {
            if (toConnectionInfo != null) {
                MsgPushMessage msgPushMessage = new MsgPushMessage(type);
                msgPushMessage.setMsg_content(message);
                msgPushMessage.setFrom(connectionInfo.getUserID());
                msgPushMessage.setId(request.getID());
                ResponseHandlerHelper.pushWrite(connectionInfo, toConnectionInfo, request, msgPushMessage);
                handler.handle(true);
                logger.info(String.format("socketio push(%s) %s to %s isSuccess %s", to, msgPushMessage, to, new Boolean(true)), connectionInfo);
            }

            if (toConnectionInfo == null) {
                logger.error(String.format("push(%s) offline socketio usesName:%s isSuccess:%s", to, to, new Boolean(false)), connectionInfo);
            }
        });

    }
}
