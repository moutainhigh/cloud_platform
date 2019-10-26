package cn.sensordb2.stcloud.util;

import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/4/1.
 */
public class MACUtil {
    public static byte[] macHexStringToByte(String hexString) {
        if(hexString.length()!=12) {
            return null;
        }

        byte[] result = new byte[6];
        String hex;
        byte b;
        for (int i = 0; i < 6; i++) {
            hex = hexString.substring(i*2,i*2+2);
            b = (byte)Integer.parseInt(hex, 16);
            result[i] = b;
        }
        return result;
    }

    public static Buffer macHexStringToBuffer(String hexString) {
        byte[] bytes = MACUtil.macHexStringToByte(hexString);
        return Buffer.buffer(bytes);
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();

    }

    public static void main(String[] args) {
        String macHexString = "DCA3AC000001";
        Buffer mac = MACUtil.macHexStringToBuffer(macHexString);
        String lockID = Tools.encodeBufferBase64(mac);
        System.out.println(String.format("mac %s to base64 %s", macHexString, lockID));
    }


}
