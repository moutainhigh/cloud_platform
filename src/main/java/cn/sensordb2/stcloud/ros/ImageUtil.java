package cn.sensordb2.stcloud.ros;

import cn.sensordb2.stcloud.util.HYLogger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class ImageUtil {

    private static HYLogger logger = HYLogger.getLogger(ImageUtil.class);

    //base64编码图片转成文件存储
    public Boolean GeneratePicFromBase64(String picture, String imgPath) {
        if (picture == null) {
            return false;
        }
        Decoder decoder = Base64.getDecoder();
        //base64 解码成二进制
        byte[] imgBytes = decoder.decode(picture);
        for (int i = 0; i < imgBytes.length; ++i) {
            // 调整异常数据
            if (imgBytes[i] < 0) {
                imgBytes[i] += 256;
            }
        }
        //生成图片
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imgPath);
            fileOutputStream.write(imgBytes);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            logger.exception("base64转图片失败，图片路径有误！：" + imgPath);
            return false;
        } catch (IOException e) {
            logger.exception("base64转图片失败,base64格式错误");
            return false;
        }

    }
    // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
    public String GetImageStr(String imgPath) {
        byte[] data = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(imgPath);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            fileInputStream.close();
            //对bytes二进制文件进行base64编码
            Encoder encoder = Base64.getEncoder();
            String imgBase64 = encoder.encodeToString(bytes);
            return imgBase64;
        } catch (Exception e) {
            logger.exception("图片转base64失败");
        }
        return null;
    }
}
