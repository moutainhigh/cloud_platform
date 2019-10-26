package cn.sensordb2.stcloud.server.verticle;

import cn.sensordb2.stcloud.server.ClientManager;
import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.ServerBinaryConvertorManager;
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
import cn.sensordb2.stcloud.server.ResponseHandlerHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageConsumer;

import java.util.Vector;

/**
 * Created by sensordb on 16/3/23.
 */
public class NormalClientVerticle extends AbstractVerticle {
    private String delimit = "\r\n"; //
    private int BUFFER_MAX_SIZE = 1000;
    private int connectionID = -1;
    private Buffer remainingSb = Buffer.buffer();
    private int connectionType = ConnectionProtocolType.UNKNOWN;
    public static HYLogger logger = HYLogger.getLogger(NormalConnectionVerticle.class);

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        this.connectionID = config().getInteger(NormalConnectionVerticle.ConnectionIDParamsKey);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void start() throws Exception {
        super.start();
        MessageConsumer<Buffer> consumer = vertx.eventBus().consumer(AddressUtil.getClientAddress(connectionID));
        consumer.handler(message -> {
            receive(message.body());
        });
    }

    public void receive(Buffer buffer) {
        this.receive(buffer, false);
    }

    public void receive(Buffer buffer, boolean isTempService) {
        ConnectionInfo ci = ClientManager.getInstance().getConnectionInfo(this.connectionID);

        if (!ci.isBinaryConnection()) {
            if (IniUtil.getInstance().isLogBuffer()) {
                InOutDataLogger.loggerInput(connectionID, buffer.toString());
                logger.info("NewInputBuffer:" + buffer.toString(), ci);
            }
        } else {
            if (IniUtil.getInstance().isLogBuffer()) {
                InOutDataLogger.loggerInput(connectionID, buffer, Tools.bufferToPrettyByteString(buffer));
                logger.info("NewInputBuffer:" + Tools.bufferToPrettyByteString(buffer), ci);
            }
        }

        if (IniUtil.getInstance().isLogBuffer()) {
            logger.info("BinaryBuffer original size:" + remainingSb.length(), ci);
            if (!ci.isBinaryConnection()) {
                logger.info("BinaryBuffer original:" + remainingSb.toString(), ci);
            } else {
                logger.info("BinaryBuffer original:" + Tools.bufferToPrettyByteString(remainingSb), ci);
            }
        }
        remainingSb.appendBuffer(buffer);
        if (IniUtil.getInstance().isLogBuffer()) {
            if (!ci.isBinaryConnection()) {
                logger.info("BinaryBuffer totoal:" + remainingSb.toString(), ci);
            } else {
                logger.info("BinaryBuffer totoal:" + Tools.bufferToPrettyByteString(remainingSb), ci);
            }
        }

        if (connectionType == ConnectionProtocolType.UNKNOWN) {
            if (!BinaryProtocolUtil.isBufferCheckable(remainingSb)) {
                return;
            }

            if (BinaryProtocolUtil.hasBinaryProtocolPacket(remainingSb)) {
                connectionType = ConnectionProtocolType.BINARY;
                ClientManager.getInstance().getConnectionInfo(this.connectionID).setType(connectionType);
                logger.info(String.format("Connection:%d is Binary:", connectionID), ci);
            } else {
                connectionType = ConnectionProtocolType.JSON;
                ClientManager.getInstance().getConnectionInfo(this.connectionID).setType(connectionType);
                logger.info(String.format("Connection:%d is Json:", connectionID), ci);
            }
        }

        if (connectionType == ConnectionProtocolType.BINARY) {
            this.processBinary(isTempService);
            return;
        }

        if (connectionType == ConnectionProtocolType.JSON) {
            this.processJson(isTempService);
            return;
        }
    }

    protected void processJson(boolean isTempService) {
        //???��??
        String[] tokens;
        int validTokensNum;

        String str = remainingSb.toString();
        tokens = str.split(delimit);
        validTokensNum = str.endsWith(delimit)?tokens.length:tokens.length-1;

        if(validTokensNum==tokens.length-1) {
            remainingSb = Buffer.buffer(tokens[tokens.length-1]);
        }
        else {
            remainingSb = Buffer.buffer();
        }

        for(int i=0;i<validTokensNum;i++) {
            this.process(connectionID, tokens[i], isTempService);
        }
    }

    protected void processBinary(boolean isTempService){
        if(remainingSb.length()>BUFFER_MAX_SIZE) {
            logger.error(String.format("Client %d buffer size:%d", connectionID, remainingSb.length()));
        }
        BufferSplitResult bufferSplitResult = BinaryProtocolUtil.splitBinary(remainingSb);
        remainingSb = Buffer.buffer().appendBuffer(bufferSplitResult.getLastBuffer());
        Vector<Buffer> buffers = bufferSplitResult.getTokens();
        Request request;
        boolean requestValid;
        for (Buffer buffer : buffers) {
            requestValid = true;
            try {
                request = ServerBinaryConvertorManager.getInstance().convert(buffer);
                if (request == null) {
                    ResponseHandlerHelper.error(ClientManager.getInstance().getConnectionInfo(connectionID), buffer.toString(), ResponseErrorCode.INVALID_PARAMETERS);
                    continue;
                }
            } catch (Exception e) {
                ResponseHandlerHelper.error(ClientManager.getInstance().getConnectionInfo(connectionID), buffer.toString(), ResponseErrorCode.INVALID_PARAMETERS);
                logger.exception(Tools.getTrace(e));
                requestValid = false;
                return;
            }
            if(requestValid==true) {
                RequestDispatcher.getInstance().dispatcher(connectionID, request, isTempService);
            }
        }
    }


    /*
     * connectionID==NetSocket.hashCode()
     */
    protected void process(int connectionID, String token, boolean isTempService) {
        RequestDispatcher.getInstance().dispatcher(connectionID, token, isTempService);
    }

}
