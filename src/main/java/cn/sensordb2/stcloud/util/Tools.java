package cn.sensordb2.stcloud.util;

import cn.sensordb2.stcloud.core.Database;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.server.singleServer.Server;
import cn.sensordb2.stcloud.server.common.RequestExceptionPair;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
//import sun.misc.BASE64Encoder;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.abs;

public class Tools {
    private static final int LINE_BINARY_LENGTH = 30;
    private static HYLogger logger = HYLogger.getLogger(Server.class);

    public static String sameCharWithLength(char c, int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String readFileToString(String fileName) {
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(filecontent);
    }


    public static boolean isValidPhoneNumber(String phoneNumber) {
        // TODO
        return true;
    }

    public static boolean isValidYYYYMMDDString(String dateString) {
        if (dateString == null) return false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = format.parse(dateString);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public static boolean isValidHHmmString(String dateString) {
        if (dateString == null) return false;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        try {
            Date date = format.parse(dateString);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    /*
     * with millisecond
     */
    public static String dateToStrMs(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String str = format.format(date);
        return str;
    }


    public static String dateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        return str;
    }

    public static String currentDateStr() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(new Date());
        return str;
    }


    public static String dateToStrYMD(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    public static String dateToStrYMDDot(Date date, String prefix) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        String str = format.format(date);
        if (prefix == null) {
            return str;
        } else return prefix + str;
    }


    public static String dateToStrForLog(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String str = format.format(date);
        return str;
    }

    /*
     * mongodb date utc: new Date("yyyy-MM-dd HH:mm:ss")
     */
    public static String dateToMongoDBStr(Date date) {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        sb.append("new Date(\"");
        sb.append(str);
        sb.append("\")");
        return sb.toString();
    }


    public static double timeGap(long start, long end) {
        return (end - start) * 1.0 / 1000;
    }

    /**
     * �ַ���ת��������
     *
     * @param str
     * @return date
     */
    public static Date strToDate(String str) {
        if (str == null) return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
//          e.printStackTrace();
            format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = format.parse(str);
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return date;
    }

    public static Date strToDateHMOrS(String str) {
        if (str == null) return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
//          e.printStackTrace();
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                date = format.parse(str);
            } catch (ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return date;
    }

    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static String getTrace(String request, Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        if (request != null) {
            writer.append("request:");
            writer.append(request);
            writer.append('\n');
        }
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }

    public static String getTrace(String request, String context, Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        if (request != null) {
            writer.append("request:");
            writer.append(request);
            writer.append('\n');
        }
        if (context != null) {
            writer.append("context:");
            writer.append(context);
            writer.append('\n');
        }
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }


    public static String getRequestException(Request request, Throwable t) {
        return new RequestExceptionPair(request, t).toString();
    }


    public static String boolToString(boolean b) {
        if (b) return "true";
        else return "false";
    }

    public static String objectToString(Object o) {
        if (o == null) return "NULL";
        else return o.toString();
    }

    public static String stringToObject(String string) {
        if (string == null || "NULL".equals(string)) return null;
        else return string;
    }

    /*
     * HTTPS://:SERVER/restserver/api/v1/accounts/:userName/image|voice
     */
    public static String uploadResource(String userName,
                                        String hashedPassword, String host, int port, String file, String contentType) throws Exception {
        String urls = "HTTP://" + host + ":" + port + "/restserver/api/v1/accounts/" + userName + "/" + (contentType.endsWith("jpeg") ? "image" : "voice");
        URL url = null;
        HttpURLConnection http = null;

        try {
            url = new URL(urls);
            http = (HttpURLConnection) url.openConnection();
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setUseCaches(false);
            http.setConnectTimeout(50000);// 设置连接超时
            // 如果在建立连接之前超时期满，则会引发一个
            // java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
            http.setReadTimeout(50000);// 设置读取超时
            // 如果在数据可读取之前超时期满，则会引发一个
            // java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
            http.setRequestMethod("POST");
            // http.setRequestProperty("Content-Type","text/xml;
            // charset=UTF-8");
            http.setRequestProperty("Content-Type", contentType);
            http.setRequestProperty("userName", userName);
            http.setRequestProperty("hashedPassword", hashedPassword);
            http.connect();

            DataOutputStream osw = new DataOutputStream(http.getOutputStream());
            File uploadFile = new File(file);
            InputStream is = new FileInputStream(uploadFile);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                osw.write(buffer, 0, len);
            }

            osw.flush();
            is.close();
            osw.close();

            StringBuffer result = new StringBuffer();
            if (http.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result.append(inputLine);
                }
                in.close();
                return result.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "err";
        }
        return "NULL";
    }

    public static String bufferToPrettyByteStringForGate(Buffer buffer) {
        byte b;
        Buffer result = Buffer.buffer("APPID  : ");
        String byteString;
        for (int i = 0; i < buffer.length(); i++) {
            b = buffer.getByte(i);
            if (i == 32) {
                byteString = Tools.bytePrettyString(i, b, true);
            } else byteString = Tools.bytePrettyString(i, b);

            if (i == 30) {
                result.appendString("LEN");
            }
            if (i == 31) {
                result.appendString("SUM");
            }
            if (i == 32) {
                result.appendString("CMD");
            }
            if (i == 33) {
                result.appendString("ID");
            }

            result.appendString(byteString);
            result.appendString(" ");

            //appID isRelay reserve mac header
            if (i == 14) {
                result.appendString("\n");
                result.appendString("IsRelay: ");
            }

            if (i == 15) {
                result.appendString("\n");
                result.appendString("Reserve: ");
            }

            if (i == 19) {
                result.appendString("\n");
                result.appendString("Mac    : ");
            }

            if (i == 25) {
                result.appendString("\n");
                result.appendString("Header : ");
            }

            if (i == 33) {
                result.appendString("\n");
                result.appendString("Body   : ");
            }


        }
        return result.toString();
    }

    public static String bufferToPrettyByteString(Buffer buffer) {
        byte b;
        Buffer result = Buffer.buffer("");
        String byteString;
        for (int i = 0; i < buffer.length(); i++) {
            b = buffer.getByte(i);
            byteString = Tools.bytePrettyString(i, b);
            result.appendString(byteString);
            result.appendString(" ");
        }
        return result.toString();
    }


    public static String bytePrettyString(int index, byte b) {
        return Tools.bytePrettyString(index, b, false);
    }

    public static String bytePrettyString(int index, byte b, boolean hex) {
        StringBuffer sb = new StringBuffer();
        sb.append(index);
        sb.append('(');
        if (hex) {
            sb.append("0X" + Integer.toHexString(b & 0xFF));
        } else {
            sb.append(String.valueOf(b));
        }
        sb.append(',');
        if (b < 0x20 || b == 0x7F) {
            sb.append(" ");
        } else {
            sb.append((char) b);
        }

        sb.append(')');
        return sb.toString();
    }

    public static int indexOf(Buffer buffer, String str) {
        if (buffer == null || buffer.length() == 0 || str == null || str.length() == 0) return -1;

        boolean found;
        for (int i = 0; i < buffer.length(); i++) {
            if (i + str.length() > buffer.length()) return -1;

            found = true;
            for (int k = 0; k < str.length(); k++) {

                if (buffer.getByte(i + k) != str.charAt(k)) {
                    found = false;
                    break;
                }
            }

            if (found) {
                return i;
            }
        }
        return -1;
    }


    public static int indexOf(Buffer buffer, int data) {
        if (buffer == null || buffer.length() == 0) return -1;

        for (int i = 0; i < buffer.length(); i++) {
            if (buffer.getByte(i) == data) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 编码
     *
     * @param bstr
     * @return String
     */
    public static String encodeBase64(byte[] bstr) {
        return Base64.encodeBase64String(bstr);
    }

    /**
     * 解码
     *
     * @param str
     * @return string
     */

    public static byte[] decodeBase64(String str) {
        byte[] bt = null;
        try {
            bt = Base64.decodeBase64(str);
        } catch (Exception e) {
            logger.exception(e);
        }
//        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
//        try {
//            bt = decoder.decodeBuffer(str);
//        } catch (IOException e) {
//            logger.exception(e);
//        }
        return bt;
    }

    public static byte[] randomKey() {
        int keyLength = 16;
        byte[] key = new byte[keyLength];
        Random random = new Random();
        random.nextBytes(key);
        return key;
    }

    public static String randomKeyString() {
        return Tools.encodeBase64(Tools.randomKey());
    }

    /**
     * 解码
     *
     * @param str
     * @return string
     */

    public static Buffer decodeBase64ToBuffer(String str) {
        byte[] bt = null;
//        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
//        byte[] encrypted1 = new BASE64Decoder();
//        byte[] encrypted1 =Base64.decodeBase64(str);
        try {
            bt = Base64.decodeBase64(str);
        } catch (Exception e) {
            logger.exception(e);
        }
//        try {
//            bt = Base64.decodeBase64(str);
////            bt = decoder.decodeBuffer(str);
//        } catch (IOException e) {
//            logger.exception(e);
//        }
        return Buffer.buffer(bt);
    }

    public static Buffer charStringToBinaryString(String string) {
        int a;
        byte[] bytes = new byte[string.length()];
        for (int i = 0; i < string.length(); i++) {
            bytes[i] = (byte) (string.charAt(i) - '0');
        }
        return Buffer.buffer(bytes);
    }

    public static Buffer createSameValueBuffer(int value, int size) {
        int a;
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) value;
        }
        return Buffer.buffer(bytes);
    }

    public static String createSameValueString(String str, int size) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size; i++) {
            sb.append(str);
        }
        return sb.toString();
    }


    public static String encodeBufferBase64(Buffer buffer) {
        return Base64.encodeBase64String(buffer.getBytes());
    }

    public static Vector<JsonObject> jsonArrayToVector(JsonArray jsonArray) {
        Vector vector = new Vector();
        if (jsonArray == null) return vector;
        JsonObject object;
        for (int i = 0; i < jsonArray.size(); i++) {
            object = jsonArray.getJsonObject(i);
            vector.add(object);
        }
        return vector;
    }

    public static String netAddressToString(SocketAddress socketAddress) {
        return String.format("(host:%s port:%d)", socketAddress.host(), socketAddress.port());
    }

    public static void initSystemProperties() {
        if (System.getenv("HY_HOME") != null) {
            System.setProperty("HY_HOME", System.getenv("HY_HOME"));
        } else {
            System.setProperty("HY_HOME", ".");

        }
    }

    public static String lockIDToDefaultNickName(String lockID) {
        Buffer mac = Tools.decodeBase64ToBuffer(lockID);
        if (mac.length() != 6) {
            return "FFFFFF";
        }
        byte[] lastThreeBytes = mac.getBytes(3, mac.length());
        String lastThreeBytesString = MACUtil.byteArrayToHexString(lastThreeBytes).toUpperCase();
        return "虹云门锁_" + lastThreeBytesString;
    }

    public static String stringToHexString(String str) {
        String hex;
        char c;
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            hex = Integer.toHexString(c);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }

            if (i != str.length() - 1)
                sb.append("0x" + hex + ",");
            else {
                sb.append("0x" + hex);
            }
        }
        return sb.toString();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static Buffer twoCharHexStringToBuffer(String hexString) {
        if (hexString == null || hexString.equals("") || hexString.length() % 2 != 0) {
            return null;
        }

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return Buffer.buffer(d);
    }

    public static String intWithZeroPadding(int number, int length) throws Exception {
        String numberSring = String.valueOf(number);

        if (numberSring.length() > length) {
            throw new Exception("");
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length - numberSring.length(); i++) {
            sb.append("0");
        }
        sb.append(numberSring);
        return sb.toString();
    }

    /**
     * 将字符串集合按200一个分组
     *
     * @param collection
     * @return
     */
    public static List<List<String>> groupByCollection(Collection<String> collection, int step) {
        List<String> needHandle = new ArrayList<>(collection);
        if (step == 0)
            return new ArrayList<List<String>>() {{
                add(needHandle);
            }};
        int size = needHandle.size() / step + 1;
        List<List<String>> result = new ArrayList<>(size);
        List<String> eachElements;
        for (int i = 0; i < needHandle.size(); i = i + step) {
            if (i + step > needHandle.size()) {//最后一个分组
                eachElements = needHandle.subList(i, needHandle.size());
            } else {
                eachElements = needHandle.subList(i, i + step);
            }
            result.add(eachElements);
        }
        return result;
    }

    public static Future<List<JsonObject>> findResultByCollection(Collection<String> collection
            , String keyName, String tableName) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        List<List<String>> nested = groupByCollection(collection, 200);
        List<Future> futures = new ArrayList<>();
        for (List<String> strings : nested) {
            Future<List<JsonObject>> future = Future.future();
            futures.add(future);
            JsonObject condition = new JsonObject().put(keyName, new JsonObject().put("$in", new JsonArray(strings)));
            mongoClient.find(tableName, condition, future.completer());
        }

        Future<List<JsonObject>> returnFuture = Future.future();
        CompositeFuture.join(futures).setHandler(find -> {
            List<JsonObject> result = new ArrayList<>();
            for (Future future : futures) {
                if (future.failed()) {
                    logger.error("findReslutByCollection -> findReslutByCollection failed: " + future.cause());
                } else {
                    result.addAll((ArrayList<JsonObject>) future.result());
                }
            }
            returnFuture.complete(result);
        });
        return returnFuture;
    }

    public static Future<List<JsonObject>> findResultByCollection(Collection<String> collection
            , String keyName, String tableName, FindOptions findOptions) {
        MongoClient mongoClient = Database.getInstance().getMongoClient();

        List<List<String>> nested = groupByCollection(collection, 200);
        List<Future> futures = new ArrayList<>();
        for (List<String> strings : nested) {
            Future<List<JsonObject>> future = Future.future();
            futures.add(future);
            JsonObject condition = new JsonObject().put(keyName, new JsonObject().put("$in", new JsonArray(strings)));
            mongoClient.findWithOptions(tableName, condition, findOptions, future.completer());
        }

        Future<List<JsonObject>> returnFuture = Future.future();
        CompositeFuture.join(futures).setHandler(find -> {
            List<JsonObject> result = new ArrayList<>();
            for (Future future : futures) {
                if (future.failed())
                    logger.error("findReslutByCollection -> findReslutByCollection failed: " + future.cause());
                else
                    result.addAll((ArrayList<JsonObject>) future.result());
            }
            returnFuture.complete(result);
        });
        return returnFuture;
    }
    public static String dateToString(Date date){
        SimpleDateFormat string = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return string.format(date);
    }
    public static long stringToDate(String string){
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            time = date.parse(string).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    public static long datePeriod(String starttime,String endtime){
        SimpleDateFormat time= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long startTime = 0;
        long endTime = 0;
        try {
            endTime = time.parse(endtime).getTime();
            startTime = time.parse(starttime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (abs(endTime-startTime))/1000;
    }
    public static void main(String[] args) {
        String lockID = "3KOsAgAM";
        System.out.println(Tools.lockIDToDefaultNickName(lockID));
    }
}