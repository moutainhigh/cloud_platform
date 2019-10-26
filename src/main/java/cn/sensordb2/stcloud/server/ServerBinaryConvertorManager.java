package cn.sensordb2.stcloud.server;

import cn.sensordb2.stcloud.server.common.BinaryConvertor;
import cn.sensordb2.stcloud.server.gate.BinaryHeader;
import cn.sensordb2.stcloud.server.gate.binaryToJson.*;
import cn.sensordb2.stcloud.server.message.Request;
import io.vertx.core.buffer.Buffer;

import java.util.Hashtable;

/**
 * Created by sensordb on 16/3/12.
 */
public class ServerBinaryConvertorManager {
    private static ServerBinaryConvertorManager instance = new ServerBinaryConvertorManager();
    private Hashtable<Byte, BinaryConvertor> convertors = new Hashtable();

    private ServerBinaryConvertorManager() {
        this.initGateConvertors();
    }

    private void initGateConvertors() {
        convertors.put(ConvertLogin.CMD_ID, new ConvertLogin());
        convertors.put(ConvertHeartBeat.CMD_ID, new ConvertHeartBeat());
        convertors.put(ConvertLockUploadWarning.CMD_ID, new ConvertLockUploadWarning());

        convertors.put(ConvertAesRandomData.CMD_ID, new ConvertAesRandomData());
        convertors.put(ConvertDecryptAesedRandomData.CMD_ID, new ConvertDecryptAesedRandomData());
        convertors.put(ConvertGetRandomDataFromCloud.CMD_ID, new ConvertGetRandomDataFromCloud());
    }

    public static ServerBinaryConvertorManager getInstance() {
        if (instance == null) {
            instance = new ServerBinaryConvertorManager();
        }
        return instance;
    }

    public Request convert(Buffer buffer) throws Exception {
        BinaryHeader binaryHeader = new BinaryHeader(buffer);
        if (!binaryHeader.isValid()) {
            return null;
        }
        else {
            BinaryConvertor bc = this.convertors.get(binaryHeader.getCmd());
            if(bc==null) return null;
            else return bc.convert(buffer, binaryHeader);
        }
    }
}
