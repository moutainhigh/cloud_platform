package cn.sensordb2.stcloud.otherTools;

import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/10/31.
 */
public class GuradIDAndPassQRCodeGeneratorStringV1 {
    public class ProductType {
        public static final int ROUTER = 1;
        public static final int RING = 2;
        public static final int LOCK = 3;
        public static final int GATEWAY = 4;
        public static final int GUARD = 5;
    }

    public static void main(String[] args) throws Exception {
        byte productType = ProductType.GUARD;
        byte productVersion = 1;
        byte productQRCodeType = 1;
        byte devIDLength = 15;

        Buffer devIDBuffer = Buffer.buffer("123456789012345");
//        Buffer macBuffer = Tools.charStringToBinaryString("123456");
        String macString = "DCA3AC"+"030011";
        Buffer macBuffer = Tools.twoCharHexStringToBuffer(macString);
        System.out.println(String.format("macString:%s size:%d", macString, macBuffer.length()));
        String macBase64 = Tools.encodeBufferBase64(macBuffer);


//        String ffLengthOf16 = Tools.createSameValueString("20", 16);
//        String ffLengthOf16 = Tools.createSameValueString("DF", 16);
        String ffLengthOf16 = Tools.createSameValueString("FF", 16);
        Buffer keyPassBuffer = Tools.twoCharHexStringToBuffer(ffLengthOf16);
        Buffer keyEncPasBuffer = Tools.twoCharHexStringToBuffer(ffLengthOf16);
//        Buffer keyPassBuffer = Buffer.buffer(Tools.sameCharWithLength('A', 16));
//        Buffer keyEncPasBuffer = Buffer.buffer(Tools.sameCharWithLength('A', 16));

        Buffer qrCode = Buffer.buffer();
        qrCode.appendByte(productType);
        qrCode.appendByte(productVersion);
        qrCode.appendByte(productQRCodeType);
        qrCode.appendByte(devIDLength);

        qrCode.appendBuffer(devIDBuffer);
        qrCode.appendBuffer(Buffer.buffer(macBase64));
        qrCode.appendBuffer(Buffer.buffer(Tools.encodeBufferBase64(keyPassBuffer)));
        System.out.println(String.format("keyPassBuffer size:%d",
                Buffer.buffer(Tools.encodeBufferBase64(keyPassBuffer)).length()));

        qrCode.appendBuffer(Buffer.buffer(Tools.encodeBufferBase64(keyEncPasBuffer)));

        System.out.println(String.format("Buffer qrCode size:%d", qrCode.length()));

        System.out.println(String.format("qrcode:%s", Tools.bufferToPrettyByteString(qrCode)));
        String qrCodeString = new String(qrCode.getBytes());
        System.out.println(String.format("String qrCode size:%d", qrCodeString.length()));
        System.out.println(String.format("String qrCode:%s", qrCodeString));

        byte[] qrCodeStringBytes = qrCodeString.getBytes();
        System.out.println(String.format("qrCodeStringBytes size:%s", qrCodeStringBytes.length));
        for (int i = 0; i < qrCodeStringBytes.length; i++) {
            System.out.println(String.format("[%d]%d", i, qrCodeStringBytes[i]));
        }
//
//        String text = qrCodeString;
//
//        try {
//            InputStream is = new ByteArrayInputStream(text.getBytes());
//
//            int byteRead;
//            int index = 0;
//            while ((byteRead = is.read()) != -1) {
//                System.out.println(String.format("[%d]%d", index, byteRead));
//                index++;
//            }
//            System.out.println();
//            is.close();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String qrCodePath = "./resources/qrCode";
        String qrCodeFileName = "qrcode_"+macString;
        QRCodeUtil.encode(qrCodeString, null, qrCodePath, qrCodeFileName, true);
        System.out.println("generate qrcode successfully");
    }
}
