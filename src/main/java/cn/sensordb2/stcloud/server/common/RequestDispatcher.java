package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.control.AdjustAngle;
import cn.sensordb2.stcloud.control.CollectData;
import cn.sensordb2.stcloud.control.StartTakeoff;
import cn.sensordb2.stcloud.data.uploadData;
import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import cn.sensordb2.stcloud.server.handler.common.CreateAppAccount;
import cn.sensordb2.stcloud.server.handler.common.Ping;
import cn.sensordb2.stcloud.server.handler.common.RelayMsg;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.user.Login;
import cn.sensordb2.stcloud.user.RelayTo;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.Tools;

import java.util.Date;
import java.util.Hashtable;

public class RequestDispatcher {
	private static HYLogger logger = HYLogger.getLogger(RequestDispatcher.class);
	private static RequestDispatcher instance;
	private Hashtable<String, RequestHandler> requestHandlerMap = new Hashtable();

	private RequestDispatcher() {
		this.init();
	}

	private void init() {
		this.initCommon();
	}

	private void initCommon() {
		requestHandlerMap.put("common.createAppAccount", new CreateAppAccount());
		requestHandlerMap.put("common.ping", new Ping());
		requestHandlerMap.put("common.RelayMsg", new RelayMsg());
		requestHandlerMap.put("data.uploadData", new uploadData());
		requestHandlerMap.put("control.AdjustAngle", new AdjustAngle());
		requestHandlerMap.put("user.Login",new Login());
		requestHandlerMap.put("control.CollectData",new CollectData());
		requestHandlerMap.put("control.StartTakeoff",new StartTakeoff());
		requestHandlerMap.put("user.RelayTo",new RelayTo());
//		requestHandlerMap.put("")
	}
	
	public static RequestDispatcher getInstance() {
		if(instance==null) instance = new RequestDispatcher();
		return instance;
	}
	
	public void dispatcher(int connectionID, String req, boolean isTempService) {
		Request request = null;
		ConnectionInfo connectionInfo = ClientManager.getInstance().getConnectionInfo(connectionID);

		if (connectionInfo == null) {
			logger.error(String.format("dispatcher process %s for connection %d error connectionInfo:null", req, connectionID));
			return;
		}
		try {
			try {
				//logger.info(String.format("Get Request[%s] from %d", req, connectionID));
				request = new Request(req);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				ResponseHandlerHelper.error(connectionInfo, req, ResponseErrorCode.INVALID_PARAMETERS);
				logger.error(Tools.getTrace(req, e1), connectionInfo);
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
				connectionInfo.getNewRequest(System.currentTimeMillis(), new Date(), request);
				request.setProcessBeginTimeCurrentTime();
				rh.handle(connectionInfo, request);
				RequesResponseLoggerTool.logNewRequest(connectionInfo, req, request);
			} catch (Exception e) {
				ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
				logger.error(Tools.getTrace(req, e), connectionInfo);
				RequesResponseLoggerTool.logNewRequest(connectionInfo, req, request);
				return;
			}	
		} catch (Exception e) {

			ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
			logger.error(Tools.getTrace(req, e), connectionInfo);
			logger.error(Tools.getRequestException(request, e), connectionInfo);
			return;
		}
	}

	public void dispatcher(int connectionID, Request request, boolean isTempService) {
		ConnectionInfo connectionInfo = ClientManager.getInstance().getConnectionInfo(connectionID);;
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
				logger.error(Tools.getTrace(e), connectionInfo);
				return;
			}
		} catch (Exception e) {
			ResponseHandlerHelper.error(connectionInfo, request, ResponseErrorCode.SERVER_ERROR);
			logger.error(Tools.getTrace(e), connectionInfo);
			return;
		}
	}
}
