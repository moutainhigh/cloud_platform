package cn.sensordb2.stcloud.httpApiServer;

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
import cn.sensordb2.stcloud.server.message.ResponseErrorMsg;
import cn.sensordb2.stcloud.user.Login;
import cn.sensordb2.stcloud.util.Tools;

import java.util.Hashtable;

public class HttpServerRequestDispatcher {
    private static HYHttpServerLogger logger = HYHttpServerLogger.getLogger(RequestDispatcher.class);
    private static HttpServerRequestDispatcher instance;
    private Hashtable<String, RequestHandler> requestHandlerMap = new Hashtable<>();
    

    public Hashtable<String, RequestHandler> getRequestHandlerMap() {
		return requestHandlerMap;
	}

	@Override
	public String toString() {
		return "HttpServerRequestDispatcher [requestHandlerMap=" + requestHandlerMap + "]";
	}

	private HttpServerRequestDispatcher() {
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
    }

    public static HttpServerRequestDispatcher getInstance() {
        if (instance == null) instance = new HttpServerRequestDispatcher();
        return instance;
    }

    public void dispatcher(int connectionID, String req) {
        Request request = null;
        ConnectionInfo connectionInfo = HttpClientManager.getInstance().getConnectionInfo(connectionID);

        if (connectionInfo == null) {
            logger.error(String.format("http api server dispatcher process %s for connection %d error connectionInfo:null", req, connectionID));
            return;
        }
        try {
            try {
                logger.info(String.format("Get Request[%s] from %d  within http api server", req, connectionID), connectionInfo);
                request = new Request(req);
            } catch (Exception e1) {
                ResponseHandlerHelper.error(connectionInfo, req, ResponseErrorCode.INVALID_PARAMETERS);
                logger.error(Tools.getTrace(req, e1), connectionInfo);
                RequesResponseLoggerTool.logNewRequest(connectionInfo, req, request);
                return;
            }

            RequestHandler rh = requestHandlerMap.get(request.getMethod());

            if (rh == null) {
                ResponseHandlerHelper.error(connectionInfo, request, 
                		ResponseErrorCode.METHOD_NOT_FOUND,ResponseErrorMsg.getErrorMsg(ResponseErrorCode.METHOD_NOT_FOUND));
                RequesResponseLoggerTool.logNewRequest(connectionInfo, req, request);
                return;
            }

            request.setProcessBeginTimeCurrentTime();
            rh.handle(connectionInfo, request);
            RequesResponseLoggerTool.logNewRequest(connectionInfo, req, request);
        } catch (Exception e) {
            ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
            logger.error(Tools.getTrace(req, e), connectionInfo);
            logger.error(Tools.getRequestException(request, e), connectionInfo);
            return;
        }
    }
}
