package cn.sensordb2.stcloud.util;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import io.netty.handler.codec.http.HttpHeaders;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;

public class AliDaYuShortMessageTool {
    private static HYLogger logger = HYLogger.getLogger(AliDaYuShortMessageTool.class);
	public static final String HONG_YUN_SIGN = "虹云网络";
	private static final String SMSHOST = AliDaYuIniUtil.getInstance().getServerHostName();
    private static final String smsURL = AliDaYuIniUtil.getInstance().getSmsURL();
    private static final int port = AliDaYuIniUtil.getInstance().getPort();

	public static void sendVerificationShortMessage(ConnectionInfo connectionInfo, Vertx vertx, String phoneNumber,
													String verificationCode,
													Handler<Boolean> handler) {
		if (IniUtil.getInstance().getTestPhoneNum() != null && IniUtil.getInstance().getTestPhoneNum().contains(phoneNumber)) {
			handler.handle(true);
			return;
		}

		HttpClientOptions httpClientOptions = new HttpClientOptions();
//        httpClientOptions.setSsl(true).setTrustAll(true).setVerifyHost(false);
		HttpClient client = vertx.createHttpClient(httpClientOptions);

		// Specify both port and host name
		HttpClientRequest clientRequest = client.post(port, SMSHOST, smsURL, response -> {
			response.bodyHandler(totalBuffer -> {
				try {
					logger.info(String.format("AliDayu sendVerificationShortMessage responese: %s", totalBuffer), connectionInfo);

					JsonObject result = new JsonObject(totalBuffer.toString());
					if (result.containsKey("alibaba_aliqin_fc_sms_num_send_response")) {
						if (result.getJsonObject("alibaba_aliqin_fc_sms_num_send_response").
								getJsonObject("result").getBoolean("success")) {
							handler.handle(new Boolean(true));
							logger.info(String.format("AliDayu sendVerificationShortMessage responese: %s", new Boolean(true)), connectionInfo);
						}
					} else {
						handler.handle(new Boolean(false));
						logger.error(String.format("AliDayu sendVerificationShortMessage responese: %s", totalBuffer), connectionInfo);
						logger.error(String.format("AliDayu sendVerificationShortMessage to %s responese: %s", phoneNumber, new Boolean(false)), connectionInfo);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(String.format("AliDayu sendVerificationShortMessage to %s responese: %s", phoneNumber, Tools.getTrace(e)), connectionInfo);
					handler.handle(new Boolean(false));
				}
			});
		});


		clientRequest.exceptionHandler(e -> {
			logger.error(String.format("AliDayu sendVerificationShortMessage to %s responese: %s", phoneNumber, Tools.getTrace(e)), connectionInfo);
		});

        	/*
		curl -X POST 'http://gw.api.taobao.com/router/rest' \
		-H 'Content-Type:application/x-www-form-urlencoded;charset=utf-8' \
		-d 'app_key=12129701' \
		-d 'format=json' \
		-d 'method=alibaba.aliqin.fc.sms.num.send' \
		-d 'partner_id=apidoc' \
		-d 'sign=F26293F68F3334CB77E3A47E9864D6B6' \
		-d 'sign_method=hmac' \
		-d 'timestamp=2016-06-01+22%3A48%3A41' \
		-d 'v=2.0' \
		-d 'extend=123456' \
		-d 'rec_num=13000000000' \
		-d 'sms_free_sign_name=%E9%98%BF%E9%87%8C%E5%A4%A7%E9%B1%BC' \
		-d 'sms_param=%7B%5C%22code%5C%22%3A%5C%221234%5C%22%2C%5C%22product%5C%22%3A%5C%22alidayu%5C%22%7D' \
		-d 'sms_template_code=SMS_585014' \
		-d 'sms_type=normal'
	 */
		clientRequest.putHeader(HttpHeaders.Names.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

		Map<String, String> params = new HashMap<>();
		params.put("app_key", AliDaYuIniUtil.getInstance().getAppKey());
		params.put("format", "json");
		params.put("method", "alibaba.aliqin.fc.sms.num.send");
		params.put("partner_id", "apidoc");
		params.put("sign_method", "hmac");
		params.put("timestamp", Tools.dateToStr(new Date()));
		params.put("v", "2.0");
		params.put("extend", "123456");
		params.put("rec_num", phoneNumber);
//        params.put("sms_free_sign_name", AliDaYuIniUtil.getInstance().getSmsFreeSignName());
		params.put("sms_free_sign_name", HONG_YUN_SIGN);
//        params.put("sms_free_sign_name", "登录验证");

		//sms param
		//验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！
		JsonObject smsParam = new JsonObject();
		smsParam.put("code", verificationCode);
		smsParam.put("product", "虹云智慧生活");

		params.put("sms_param", smsParam.toString());
		params.put("sms_template_code", "SMS_10627505");
		params.put("sms_type", "normal");

		try {
			String appSecret = AliDaYuIniUtil.getInstance().getAppSecret();
			String sign = signTopRequest(params, appSecret);
			params.put("sign", sign);

			String body = AliDaYuShortMessageTool.generateBody(params);

			clientRequest.end(body);

		} catch (Exception e) {
			logger.error(String.format("sendVerificationShortMessage %s, responese: %s", params.toString(), Tools.getTrace(e)), connectionInfo);
		}
	}


	/*
	 * 钥匙分发通知、没有注册用户
	 * 用户${phone}分发了一把智能门锁钥匙给您，快去下载虹云智慧生活APP，开启智能门锁的崭新体验吧。
	 */
	public static void sendNewUserLockKeyMessage(ConnectionInfo connectionInfo, Vertx vertx, String fromPhoneNumber, String toPhoneNumber,
													Handler<Boolean> handler) {
		HttpClientOptions httpClientOptions = new HttpClientOptions();
//        httpClientOptions.setSsl(true).setTrustAll(true).setVerifyHost(false);
		HttpClient client = vertx.createHttpClient(httpClientOptions);

		// Specify both port and host name
		HttpClientRequest clientRequest = client.post(port, SMSHOST, smsURL, response -> {
			response.bodyHandler(totalBuffer -> {
				try {
					logger.info(String.format("AliDayu sendNewUserLockKeyMessage responese: %s", totalBuffer), connectionInfo);

					JsonObject result = new JsonObject(totalBuffer.toString());
					if(result.containsKey("alibaba_aliqin_fc_sms_num_send_response")) {
						if(result.getJsonObject("alibaba_aliqin_fc_sms_num_send_response").
								getJsonObject("result").getBoolean("success")) {
							handler.handle(new Boolean(true));
							logger.info(String.format("AliDayu sendNewUserLockKeyMessage responese: %s", new Boolean(true)), connectionInfo);
						}
					}
					else {
						handler.handle(new Boolean(false));
						logger.error(String.format("AliDayu sendNewUserLockKeyMessage responese: %s", new Boolean(true)), connectionInfo);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(String.format("AliDayu sendNewUserLockKeyMessage responese: %s", Tools.getTrace(e)), connectionInfo);
					handler.handle(new Boolean(false));
				}
			});
		});


		clientRequest.exceptionHandler(e -> {
			logger.error(String.format("AliDayu sendNewUserLockKeyMessage responese: %s", Tools.getTrace(e)), connectionInfo);
		});

        	/*
		curl -X POST 'http://gw.api.taobao.com/router/rest' \
		-H 'Content-Type:application/x-www-form-urlencoded;charset=utf-8' \
		-d 'app_key=12129701' \
		-d 'format=json' \
		-d 'method=alibaba.aliqin.fc.sms.num.send' \
		-d 'partner_id=apidoc' \
		-d 'sign=F26293F68F3334CB77E3A47E9864D6B6' \
		-d 'sign_method=hmac' \
		-d 'timestamp=2016-06-01+22%3A48%3A41' \
		-d 'v=2.0' \
		-d 'extend=123456' \
		-d 'rec_num=13000000000' \
		-d 'sms_free_sign_name=%E9%98%BF%E9%87%8C%E5%A4%A7%E9%B1%BC' \
		-d 'sms_param=%7B%5C%22code%5C%22%3A%5C%221234%5C%22%2C%5C%22product%5C%22%3A%5C%22alidayu%5C%22%7D' \
		-d 'sms_template_code=SMS_585014' \
		-d 'sms_type=normal'
	 */
		clientRequest.putHeader(HttpHeaders.Names.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

		Map<String, String> params = new HashMap<>();
		params.put("app_key", AliDaYuIniUtil.getInstance().getAppKey());
		params.put("format", "json");
		params.put("method", "alibaba.aliqin.fc.sms.num.send");
		params.put("partner_id", "apidoc");
		params.put("sign_method", "hmac");
		params.put("timestamp", Tools.dateToStr(new Date()));
		params.put("v", "2.0");
		params.put("extend", "123456");
		params.put("rec_num", toPhoneNumber);
//        params.put("sms_free_sign_name", AliDaYuIniUtil.getInstance().getSmsFreeSignName());
		params.put("sms_free_sign_name", HONG_YUN_SIGN);

		//sms param
		//验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！
		JsonObject smsParam = new JsonObject();
		smsParam.put("phone", fromPhoneNumber);

		params.put("sms_param", smsParam.toString());
//		params.put("sms_template_code", "SMS_10607486");
		params.put("sms_template_code", "SMS_12940131");
		params.put("sms_type", "normal");

		try {
			String appSecret = AliDaYuIniUtil.getInstance().getAppSecret();
			String sign = signTopRequest(params, appSecret);
			params.put("sign", sign);

			String body = AliDaYuShortMessageTool.generateBody(params);

			clientRequest.end(body);

		} catch (Exception e) {
			logger.error(String.format("sendNewUserLockKeyMessage %s, responese: %s", params.toString(), Tools.getTrace(e)), connectionInfo);
		}
	}

	public static String generateBody(Map<String,String> params) throws UnsupportedEncodingException {
        StringJoiner sj = new StringJoiner("&");
        for(Map.Entry<String,String> entry : params.entrySet())
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                    + URLEncoder.encode(entry.getValue(), "UTF-8"));
        return sj.toString();
    }

	public static String signTopRequest(Map<String, String> params, String secret) throws IOException {
		// 第一步：检查参数是否已经排序
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		// 第二步：把所有参数名和参数值串在一起
		StringBuilder query = new StringBuilder();
		for (String key : keys) {
			String value = params.get(key);
			query.append(key).append(value);
		}

		// 第三步：使用HMAC加密
		byte[] bytes;
		bytes = encryptHMAC(query.toString(), secret);

		// 第四步：把二进制转化为大写的十六进制
		return byte2hex(bytes);
	}

	public static byte[] encryptHMAC(String data, String secret) throws IOException {
		byte[] bytes = null;
		try {
			SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacMD5");
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		} catch (GeneralSecurityException gse) {
			throw new IOException(gse.toString());
		}
		return bytes;
	}

	public static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}

	/**
	 * ת������ֵ����ΪUTF-8��ʽ.
	 *
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
		StringBuilder sb1 = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;

		try {
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, "UTF-8");
				sb1.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb1.toString();
	}

	public static void main(String[] args) {
		try {
			//System.out.println(ShortMessageTool.sendVerificationCode("40000", "18020136525"));
			test();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test() {
		try {
			Vertx vertx = Vertx.vertx();
			AliDaYuShortMessageTool.sendVerificationShortMessage(null, vertx, "13770648944", "234567",result -> {
				System.out.println(result);
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void test1() {
		try {
			Vertx vertx = Vertx.vertx();
			AliDaYuShortMessageTool.sendNewUserLockKeyMessage(null, vertx, "18020136525", "13645160637", result -> {
				System.out.println(result);
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
