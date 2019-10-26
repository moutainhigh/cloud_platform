package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.util.HYLogger;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

/**
 * Created by sensordb on 16/8/19.
 */
public class HYVertxNetSocket extends HYNetSocket {
    public static HYLogger logger = HYLogger.getLogger(HYVertxNetSocket.class);

    private NetSocket netSocket;

    public HYVertxNetSocket(NetSocket netSocket) {
        this.netSocket = netSocket;
    }

    @Override
    public void close() {
        if(this.netSocket!=null)
            this.netSocket.close();
    }

    @Override
    public void write(byte[] bytes) {
//        if(netSocket.writeQueueFull()) {
//            logger.error(String.format("HYVertxNetSocket write size:%d queue full waiting for drainHandler", bytes.length));
//            netSocket.drainHandler(drain -> {
//                logger.error(String.format("HYVertxNetSocket write size:%d queue full drainHandler called", bytes.length));
//                netSocket.write(Buffer.buffer(bytes));
//            });
//            netSocket.endHandler(end->{
//                logger.error(String.format("HYVertxNetSocket write size:%d queue full endHandler called", bytes.length));
//
//            });
//            netSocket.closeHandler(end -> {
//                logger.error(String.format("HYVertxNetSocket write size:%d queue full closeHandler called", bytes.length));
//            });
//
//            netSocket.exceptionHandler(exception->{
//                logger.exception(String.format("HYVertxNetSocket write size:%d queue full exceptionHandler called exception:%s",
//                        bytes.length,
//                        Tools.getTrace(exception)));
//            });
//        }
//        else {
//            netSocket.write(Buffer.buffer(bytes));
//        }

        if (netSocket.writeQueueFull()) {
            logger.error(String.format("HYVertxNetSocket write size:%d queue full waiting for drainHandler", bytes.length));
        }
        netSocket.write(Buffer.buffer(bytes));

    }

    public NetSocket getNetSocket() {
        return netSocket;
    }
}
