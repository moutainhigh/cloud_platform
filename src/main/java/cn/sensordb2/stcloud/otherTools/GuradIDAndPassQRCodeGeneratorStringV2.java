package cn.sensordb2.stcloud.otherTools;

import io.vertx.core.buffer.Buffer;

/**
 * Created by sensordb on 16/10/31.
 */
public class GuradIDAndPassQRCodeGeneratorStringV2 {
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
        byte productQRCodeType = 2;
        byte devIDLength = 15;

        Buffer devIDBuffer = Buffer.buffer("123456789012345");
//        Buffer macBuffer = Tools.charStringToBinaryString("123456");
        String macString = "DCA3AC"+"030011";
        Buffer macBuffer = cn.sensordb2.stcloud.util.Tools.twoCharHexStringToBuffer(macString);
        System.out.println(String.format("macString:%s size:%d", macString, macBuffer.length()));
        String macBase64 = cn.sensordb2.stcloud.util.Tools.encodeBufferBase64(macBuffer);
        String secretPassword = "1234567890123456";

        Buffer qrCode = Buffer.buffer();
        qrCode.appendByte(productType);
        qrCode.appendByte(productVersion);
        qrCode.appendByte(productQRCodeType);
        qrCode.appendByte(devIDLength);

        qrCode.appendBuffer(devIDBuffer);
        qrCode.appendBuffer(Buffer.buffer(macBase64));
        qrCode.appendBuffer(Buffer.buffer(secretPassword));

        System.out.println(String.format("Buffer qrCode size:%d", qrCode.length()));

        System.out.println(String.format("qrcode:%s", cn.sensordb2.stcloud.util.Tools.bufferToPrettyByteString(qrCode)));
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

        String qrCodePath = "./resources/qrCodeV2";
        String qrCodeFileName = "qrcode_"+macString;
        QRCodeUtil.encode(qrCodeString, null, qrCodePath, qrCodeFileName, true);
        System.out.println("generate qrcode successfully");
    }
}
