package cn.sensordb2.stcloud.util;

import cn.sensordb2.stcloud.server.singleServer.Server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncoderHandler {

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // �ֽ�����ת��Ϊ ʮ������ ��
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
 
        } catch (NoSuchAlgorithmException e) {
            Server.logger.exception(Tools.getTrace(e));
        }
        return "";
    }
    
	public static void main(String[] args) {
		System.out.println("111111 SHA1 :"
				+ EncoderHandler.SHA1("111111"));
	}

}