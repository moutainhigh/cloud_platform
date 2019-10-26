package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.common.ConnectionProtocolType;
import cn.sensordb2.stcloud.server.common.RequestDispatcher;
import cn.sensordb2.stcloud.server.gate.BinaryProtocolUtil;
import cn.sensordb2.stcloud.server.gate.BufferSplitResult;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.message.ResponseErrorCode;
import cn.sensordb2.stcloud.util.HYLogger;
import cn.sensordb2.stcloud.util.InOutDataLogger;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.util.Hashtable;
import java.util.Vector;

public class StreamProcessor {
	private static StreamProcessor instance;
	private static HYLogger logger = HYLogger.getLogger(RequestDispatcher.class);
	private Hashtable<NetSocket, Buffer> socketBuffer = new Hashtable();
	private Hashtable<NetSocket,Integer> socketType = new Hashtable();
	private String delimit = "\r\n";
	private int BUFFER_MAX_SIZE = 1000;

	public static StreamProcessor getInstance() {
		if(instance==null) instance = new StreamProcessor();
		return instance;
	}
	
	private StreamProcessor() {
		
	}
	
	private Buffer getSocketBuffer(NetSocket socket) {
//		synchronized(socketStringBuffer) {
			Buffer remainingSb = socketBuffer.get(socket);
			if(remainingSb==null) {
				remainingSb = Buffer.buffer();
				socketBuffer.put(socket, remainingSb);
			}
			return remainingSb;
//		}
	}
	
	public void receive(NetSocket socket, Buffer buffer) {
		this.receive(socket, buffer, false);
	}

	protected int getConnectionType(NetSocket socket) {
		if(this.socketType.get(socket)==null) {
			return ConnectionProtocolType.UNKNOWN;
		}
		else return this.socketType.get(socket);
	}

	public void receive(NetSocket socket, Buffer buffer, boolean isTempService) {
		try {
			int connectionType = this.getConnectionType(socket);

			if (connectionType == ConnectionProtocolType.JSON) {
				if (IniUtil.getInstance().isLogBuffer()) {
					InOutDataLogger.loggerInput(socket.hashCode(), buffer.toString());
					logger.info("Socket buffer has new data", socket);
					logger.info("NewInputBuffer:" + buffer.toString(), socket);
				}
			} else {
				if (IniUtil.getInstance().isLogBuffer()) {
					InOutDataLogger.loggerInput(socket.hashCode(), buffer, Tools.bufferToPrettyByteString(buffer));
					logger.info("NewInputBuffer:" + Tools.bufferToPrettyByteString(buffer), socket);
				}
			}

			Buffer remainingSb = this.getSocketBuffer(socket);
			if (IniUtil.getInstance().isLogBuffer()) {
				logger.info("BinaryBuffer original size:" + remainingSb.length(), socket);
				if (connectionType == ConnectionProtocolType.JSON) {
					logger.info("BinaryBuffer original:" + remainingSb.toString(), socket);
				} else {
					logger.info("BinaryBuffer original:" + Tools.bufferToPrettyByteString(remainingSb), socket);
				}
			}
			remainingSb.appendBuffer(buffer);
			socketBuffer.put(socket, remainingSb);
			if (IniUtil.getInstance().isLogBuffer()) {
				if (connectionType == ConnectionProtocolType.JSON) {
					logger.info("BinaryBuffer totoal:" + remainingSb.toString(), socket);
				} else {
					logger.info("BinaryBuffer totoal:" + Tools.bufferToPrettyByteString(remainingSb), socket);
				}
			}
			if (connectionType == ConnectionProtocolType.UNKNOWN) {
				if (!BinaryProtocolUtil.isBufferCheckable(remainingSb)) {
					logger.info(String.format("Connection:%d with buffer:%s type not checkable:", socket.hashCode(),
							Tools.bufferToPrettyByteString(remainingSb)), socket);
					return;
				}

				if (BinaryProtocolUtil.hasBinaryProtocolPacket(remainingSb)) {
					this.socketType.put(socket, new Integer(ConnectionProtocolType.BINARY));
					connectionType = ConnectionProtocolType.BINARY;
					ClientManager.getInstance().getConnectionInfo(socket.hashCode()).setType(connectionType);
					logger.info(String.format("Connection:%d is Binary:", socket.hashCode()), socket);
				} else {
					this.socketType.put(socket, new Integer(ConnectionProtocolType.JSON));
					connectionType = ConnectionProtocolType.JSON;
					ClientManager.getInstance().getConnectionInfo(socket.hashCode()).setType(connectionType);
					logger.info(String.format("Connection:%d is Json:", socket.hashCode()), socket);
				}
			}

			if (connectionType == ConnectionProtocolType.BINARY) {
				this.processBinary(socket, isTempService);
				return;
			}

			if (connectionType == ConnectionProtocolType.JSON) {
				this.processJson(socket, isTempService);
				return;
			}
		} catch (Throwable throwable) {
			logger.error(Tools.getTrace(buffer.toString(), throwable), socket);
		}
	}

	protected void processJson(NetSocket socket, boolean isTempService) {
		//�ı�Э��
		String[] requests;
		int validTokensNum;

		Buffer remainingSb = this.getSocketBuffer(socket);
		String str = remainingSb.toString();
		requests = str.split(delimit);
		validTokensNum = str.endsWith(delimit)?requests.length:requests.length-1;
//		logger.info(String.format(requests[0]));
		if(validTokensNum==requests.length-1) {
			remainingSb = Buffer.buffer(requests[requests.length-1]);
			socketBuffer.put(socket, remainingSb);
		}
		else {
			this.socketBuffer.put(socket, Buffer.buffer());
		}

		for(int i=0;i<validTokensNum;i++) {
			this.process(socket.hashCode(), requests[i], isTempService);
		}
	}

	protected void processBinary(NetSocket socket, boolean isTempService){
		Buffer remainingSb = this.getSocketBuffer(socket);
		if(remainingSb.length()>BUFFER_MAX_SIZE) {
			logger.error(String.format("Client %d buffer size:%d", socket.hashCode(), remainingSb.length()), socket);
		}
		BufferSplitResult bufferSplitResult = BinaryProtocolUtil.splitBinary(remainingSb);
		if (bufferSplitResult.getTokens().size() == 0) {
			logger.info(String.format("Client %d buffer size:%d has no complete packet", socket.hashCode(), remainingSb.length()), socket);
		}
		this.socketBuffer.put(socket, Buffer.buffer().appendBuffer(bufferSplitResult.getLastBuffer()));
		Vector<Buffer> buffers = bufferSplitResult.getTokens();
		Request request;
		boolean requestValid;
		for (Buffer buffer : buffers) {
			requestValid = true;
			try {
				request = ServerBinaryConvertorManager.getInstance().convert(buffer);
				if (request == null) {
					logger.error("New request convert to json error:"+Tools.bufferToPrettyByteString(buffer), socket);
					ResponseHandlerHelper.error(ClientManager.getInstance().getConnectionInfo(socket.hashCode()), buffer.toString(), ResponseErrorCode.INVALID_PARAMETERS);
					continue;
				}
				else {
					logger.info(String.format("receive json request(converted): %s", request), socket);

					if(IniUtil.getInstance().isLogBuffer()) {
						logger.info(String.format("convert buffer to json request: %s", request), socket);
					}
				}
			} catch (Exception e) {
				logger.error("New request convert to json error:"+Tools.bufferToPrettyByteString(buffer), socket);
				ResponseHandlerHelper.error(ClientManager.getInstance().getConnectionInfo(socket.hashCode()), buffer.toString(), ResponseErrorCode.INVALID_PARAMETERS);
				logger.error(Tools.getTrace(e), socket);
				requestValid = false;
				return;
			}
			if(requestValid==true)RequestDispatcher.getInstance().dispatcher(socket.hashCode(), request, isTempService);
		}
	}


	/*
	 * connectionID==NetSocket.hashCode()
	 */
	protected void process(int connectionID, String request, boolean isTempService) {
		RequestDispatcher.getInstance().dispatcher(connectionID, request, isTempService);
	}

	public void shutDownSocket(NetSocket netSocket) {
		this.socketBuffer.remove(netSocket);
		this.socketType.remove(netSocket);
	}
}
