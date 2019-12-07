package cn.sensordb2.stcloud.socketIOServer;

import cn.sensordb2.stcloud.control.*;
import cn.sensordb2.stcloud.data.GetData;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.common.RequesResponseLoggerTool;
import cn.sensordb2.stcloud.server.common.RequestDispatcher;
import cn.sensordb2.stcloud.server.common.RequestHandler;
import cn.sensordb2.stcloud.server.handler.common.CreateAppAccount;
import cn.sensordb2.stcloud.server.handler.common.Ping;
import cn.sensordb2.stcloud.server.handler.common.RelayMsg;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.user.Login;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.Tools;

import java.util.Date;
import java.util.Hashtable;

public class SocketIORequestDispatcher {
    private static HYLogger logger = HYLogger.getLogger(RequestDispatcher.class);
    private static SocketIORequestDispatcher instance;
    private Hashtable<String, RequestHandler> requestHandlerMap = new Hashtable();

    private SocketIORequestDispatcher() {
        this.init();
    }

    private void init() {
        this.initCommon();
    }

    private void initCommon() {
        requestHandlerMap.put("common.createAppAccount", new CreateAppAccount());
        requestHandlerMap.put("common.ping", new Ping());
        requestHandlerMap.put("common.RelayMsg", new RelayMsg());
        requestHandlerMap.put("user.Login",new Login());
        requestHandlerMap.put("data.GetData",new GetData());
        requestHandlerMap.put("control.AdjustAngle",new AdjustAngle());
        requestHandlerMap.put("control.CollectData",new CollectData());
        requestHandlerMap.put("control.StartTakeoff",new StartTakeoff());
        requestHandlerMap.put("control.StartLanding",new StartLanding());
        requestHandlerMap.put("control.UploadMission",new UploadMission());
        requestHandlerMap.put("control.GetCurrentState",new GetCurrentState());
        requestHandlerMap.put("control.OperateMission",new OperateMission());
        requestHandlerMap.put("control.GetHomeLocation",new GetHomeLocation());
        requestHandlerMap.put("control.SetWaypointMission",new SetWaypointMission());
        requestHandlerMap.put("control.GetCurrentLocation",new GetCurrentLocation());
        requestHandlerMap.put("control.GetConnection",new GetConnection());
        requestHandlerMap.put("control.Bind",new Bind());
        requestHandlerMap.put("control.GetPicture", new GetPicture());
        requestHandlerMap.put("control.RandomCrashOne", new RandomCrashOne());
        requestHandlerMap.put("control.RescueUav", new RescueUav());
//        requestHandlerMap.put("control.RecordVideo",new RecordVideo());

    }

    public static SocketIORequestDispatcher getInstance() {
        if(instance==null) instance = new SocketIORequestDispatcher();
        return instance;
    }

    public void dispatcher(int connectionID, String req, boolean isTempService) {
        Request request = null;
        logger.info(req);
        ConnectionInfo connectionInfo = WebsocketClientManager.getInstance().getConnectionInfo(connectionID);

        if (connectionInfo == null) {
            logger.error(String.format("dispatcher process %s for connection %d error connectionInfo:null", req, connectionID));
            return;
        }
        try {
            try {
                logger.info(String.format("Get Request[%s] from %d  within socketio server", req, connectionID), connectionInfo);
                request = new Request(req);
            } catch (Exception e1) {
                ResponseHandlerHelper.error(connectionInfo, req, ResponseErrorCode.INVALID_PARAMETERS);
                logger.exception(Tools.getTrace(req, e1), connectionInfo);
                RequesResponseLoggerTool.logNewRequest(connectionInfo, req, request);
                return;
            }
            RequestHandler rh = requestHandlerMap.get(request.getMethod());

            if(rh==null) {
                ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.METHOD_NOT_FOUND);
                RequesResponseLoggerTool.logNewRequest(connectionInfo, req, request);
                return;
            }

            try {
                connectionInfo.getNewRequestSocketIO(System.currentTimeMillis(), new Date(), request);
                request.setProcessBeginTimeCurrentTime();
                rh.handle(connectionInfo, request);
                RequesResponseLoggerTool.logNewRequest(connectionInfo, req, request);
            } catch (Exception e) {
                ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
                logger.exception(Tools.getTrace(req, e), connectionInfo);
                RequesResponseLoggerTool.logNewRequest(connectionInfo, req, request);
                return;
            }
        } catch (Exception e) {

            ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
            logger.exception(Tools.getTrace(req, e), connectionInfo);
            logger.exception(Tools.getRequestException(request, e), connectionInfo);
            return;
        }
    }

    public void dispatcher(int connectionID, Request request, boolean isTempService) {
        ConnectionInfo connectionInfo = WebsocketClientManager.getInstance().getConnectionInfo(connectionID);;
        try {
            RequestHandler rh = requestHandlerMap.get(request.getMethod());

            if(rh==null) {
                ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.METHOD_NOT_FOUND);
                return;
            }

            try {
                connectionInfo.getNewRequest(System.currentTimeMillis(), new Date(), request);
                rh.handle(connectionInfo, request);
            } catch (Exception e) {
                ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
                logger.exception(Tools.getTrace(e), connectionInfo);
                return;
            }
        } catch (Exception e) {
            ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
            logger.exception(Tools.getTrace(e), connectionInfo);
            return;
        }
    }
}
