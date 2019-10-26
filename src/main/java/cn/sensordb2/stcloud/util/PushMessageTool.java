package cn.sensordb2.stcloud.util;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import cn.sensordb2.stcloud.server.message.IOSPushMessage;
import io.netty.handler.codec.http.HttpHeaders;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;

import java.nio.charset.Charset;
import java.util.Base64;

public class PushMessageTool {
	private static HYLogger logger = HYLogger.getLogger(PushMessageTool.class);
	private static String pushHost = IniUtil.getInstance().getPushHost();
	private static String deviceHost = IniUtil.getInstance().getDeviceHost();
	private static String requestURI = IniUtil.getInstance().getRequestURI();
	//appKey:masterSecret
	private static String authString = IniUtil.getInstance().getAuthString();


	public static void pushMessage(ConnectionInfo connectionInfo, Vertx vertx, JsonObject message,
								   String to,
			Handler<Boolean> handler) {
		String basicAuthorizationHeadValue = PushMessageTool.generateBasicAuthorizationHeadValue(authString);
		HttpClientOptions httpClientOptions = new HttpClientOptions();
		httpClientOptions.setSsl(true).setTrustAll(true).setVerifyHost(false);
		HttpClient client = vertx.createHttpClient(httpClientOptions);
		String body = message.toString();

		HttpClientRequest clientRequest = client.post(443, pushHost, requestURI, response -> {
//			System.out.println("Status code is " + response.statusCode());
//			System.out.println("Status message is " + response.statusMessage());
//			response.bodyHandler(totalBuffer -> {
//				// Now all the body has been read
//				System.out.println("Total response body is " + totalBuffer.toString());
//			});

			if (response.statusCode() == 200) {
				handler.handle(new Boolean(true));
				response.bodyHandler(totalBuffer -> {
					logger.info(String.format("jipush(user:%s) %s, responese: %s", to, message, totalBuffer.toString()), connectionInfo);

				});
			} else {
				handler.handle(new Boolean(false));
				response.bodyHandler(totalBuffer -> {
					logger.error(String.format("jipush(user:%s) %s, responese: %s, code: %d", to, message, totalBuffer.toString(), response.statusCode()), connectionInfo);

				});
			}
		});

		clientRequest.exceptionHandler(e -> {
			logger.exception(String.format("jipush(user:%s) %s, responese: %s", to, message, Tools.getTrace(e)), connectionInfo);
		});

		clientRequest.putHeader(HttpHeaders.Names.AUTHORIZATION, "Basic " + basicAuthorizationHeadValue);
		clientRequest.putHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json");
		clientRequest.end(body);
	}

	/*
	 * 取消用户的别名
	 */
	public static void aliasUserNull(ConnectionInfo connectionInfo, Vertx vertx, String pushUserID,
								 Handler<Boolean> handler) {
		PushMessageTool.aliasUser(connectionInfo, vertx, pushUserID, "", handler);
	}

	/*
	 * ��pushUserID���ñ���
	 */
	public static void aliasUser(ConnectionInfo connectionInfo, Vertx vertx, String pushUserID, String userName,
								   Handler<Boolean> handler) {
		String basicAuthorizationHeadValue = PushMessageTool.generateBasicAuthorizationHeadValue(authString);
		HttpClientOptions httpClientOptions = new HttpClientOptions();
		httpClientOptions.setSsl(true).setTrustAll(true).setVerifyHost(false);
		HttpClient client = vertx.createHttpClient(httpClientOptions);

		HttpClientRequest clientRequest = client.post(443, deviceHost, "/v3/devices/" + pushUserID, response -> {
//			System.out.println("Status code is " + response.statusCode());
//			System.out.println("Status message is " + response.statusMessage());
//			response.bodyHandler(totalBuffer -> {
//				// Now all the body has been read
//				System.out.println("Total response body is " + totalBuffer.toString());
//			});

			if (response.statusCode() == 200) {
				handler.handle(new Boolean(true));
				response.bodyHandler(totalBuffer -> {
					logger.info(String.format("aliasUser %s, responese: %s", pushUserID, totalBuffer.toString()), connectionInfo);
				});
			} else {
				handler.handle(new Boolean(false));
				response.bodyHandler(totalBuffer -> {
					logger.error(String.format("aliasUser %s, responese: %s", pushUserID, totalBuffer.toString()), connectionInfo);
				});
			}
		});

		clientRequest.exceptionHandler(e -> {
			logger.exception(String.format("aliasUser %s, responese: %s", pushUserID, Tools.getTrace(e)), connectionInfo);
		});

		clientRequest.putHeader(HttpHeaders.Names.AUTHORIZATION, "Basic " + basicAuthorizationHeadValue);
//		clientRequest.putHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(body.getBytes().length));
		clientRequest.putHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json");

		JsonObject aliasUserBody = new JsonObject();
		aliasUserBody.put("tags", "");
		aliasUserBody.put("alias", userName);
		aliasUserBody.put("mobile", "13645160637");

		clientRequest.end(aliasUserBody.toString());
	}

	/*
	 *删除一个别名，以及该别名与设备的绑定关系。
	 */
	public static void deleteAlias(ConnectionInfo connectionInfo, Vertx vertx, String aliasUserName,
								 Handler<Boolean> handler) {
		String basicAuthorizationHeadValue = PushMessageTool.generateBasicAuthorizationHeadValue(authString);
		HttpClientOptions httpClientOptions = new HttpClientOptions();
		httpClientOptions.setSsl(true).setTrustAll(true).setVerifyHost(false);
		HttpClient client = vertx.createHttpClient(httpClientOptions);

		HttpClientRequest clientRequest = client.delete(443, deviceHost, "/v3/aliases/" + aliasUserName, response -> {
//			System.out.println("Status code is " + response.statusCode());
//			System.out.println("Status message is " + response.statusMessage());
//			response.bodyHandler(totalBuffer -> {
//				// Now all the body has been read
//				System.out.println("Total response body is " + totalBuffer.toString());
//			});

			if (response.statusCode() == 200) {
				handler.handle(new Boolean(true));
				response.bodyHandler(totalBuffer -> {
					logger.info(String.format("deletealias %s, responese: %s", aliasUserName, totalBuffer.toString()), connectionInfo);
				});
			} else {
				handler.handle(new Boolean(false));
				response.bodyHandler(totalBuffer -> {
					logger.error(String.format("deletealias %s, responese: %s", aliasUserName, totalBuffer.toString()), connectionInfo);
				});
			}
		});

		clientRequest.exceptionHandler(e -> {
			logger.exception(String.format("deletealias %s, responese: %s", aliasUserName, Tools.getTrace(e)), connectionInfo);
		});

		clientRequest.putHeader(HttpHeaders.Names.AUTHORIZATION, "Basic " + basicAuthorizationHeadValue);
//		clientRequest.putHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(body.getBytes().length));
		clientRequest.end();
	}


	/*
	 * ��pushUserID���ñ���
	 */
	public static void queryDeviceTagAndAlias(Vertx vertx, String pushUserID,
								 Handler<Boolean> handler) {
		String basicAuthorizationHeadValue = PushMessageTool.generateBasicAuthorizationHeadValue(authString);
		HttpClientOptions httpClientOptions = new HttpClientOptions();
		httpClientOptions.setSsl(true).setTrustAll(true).setVerifyHost(false);
		HttpClient client = vertx.createHttpClient(httpClientOptions);

		HttpClientRequest clientRequest = client.get(443, deviceHost, "/v3/devices/" + pushUserID, response -> {
//			System.out.println("Status code is " + response.statusCode());
//			System.out.println("Status message is " + response.statusMessage());
//			response.bodyHandler(totalBuffer -> {
//				// Now all the body has been read
//				System.out.println("Total response body is " + totalBuffer.toString());
//			});

			if (response.statusCode() == 200) {
				handler.handle(new Boolean(true));
				response.bodyHandler(totalBuffer -> {
					logger.info(String.format("queryDeviceTagAndAlias %s, responese: %s", pushUserID, totalBuffer.toString()));
					System.out.println(String.format("queryDeviceTagAndAlias %s, responese: %s", pushUserID, totalBuffer.toString()));
				});
			} else {
				handler.handle(new Boolean(false));
				response.bodyHandler(totalBuffer -> {
					logger.error(String.format("queryDeviceTagAndAlias %s, responese: %s", pushUserID, totalBuffer.toString()));
				});
			}
		});

		clientRequest.exceptionHandler(e -> {
			logger.exception(String.format("queryDeviceTagAndAlias %s, responese: %s", pushUserID, Tools.getTrace(e)));
		});

		clientRequest.putHeader(HttpHeaders.Names.AUTHORIZATION, "Basic " + basicAuthorizationHeadValue);
//		clientRequest.putHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(body.getBytes().length));
		clientRequest.putHeader(HttpHeaders.Names.CONTENT_TYPE, "application/json");

		clientRequest.end();

	}

	public static String generateBasicAuthorizationHeadValue(String authString) {
		return Base64.getEncoder().encodeToString(authString.getBytes(Charset.forName("utf-8")));
	}

	public static void main(String[] args) {
		try {
			//System.out.println(ShortMessageTool.sendVerificationCode("40000", "18020136525"));
//			test();
//			testUnicast();
//			testAlias();
//			testDeleteAlias();
//			testQueryDeviceTagAndAlias();
			testUnicastWithPushID();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void test() {
		try {
			Vertx vertx = Vertx.vertx();
			IOSPushMessage iosPushMessage = new IOSPushMessage("all");
			PushMessageTool.pushMessage(null, vertx, iosPushMessage, "000", result -> {
				System.out.println(result);
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testAlias() {
		try {
			Vertx vertx = Vertx.vertx();
			String pushUserID = "101d855909479757bb2";
			String userName = "userID_n2";
			PushMessageTool.aliasUser(null, vertx, pushUserID, userName, result -> {
				System.out.println(String.format("PushMessageTool.aliasUser %s", result));
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void testDeleteAlias() {
		try {
			Vertx vertx = Vertx.vertx();
			String userName = "userID_n2";
			PushMessageTool.deleteAlias(null, vertx, userName, result -> {
				System.out.println(String.format("PushMessageTool.deleteAlias %s", result));
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void testQueryDeviceTagAndAlias() {
		try {
			Vertx vertx = Vertx.vertx();
			String pushUserID = "18171adc030cf2f9c1c";
//			String pushUserID = "101d855909479757bb2";
			PushMessageTool.queryDeviceTagAndAlias(vertx, pushUserID, result -> {
				System.out.println(result);
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void testUnicast() {
		try {
			Vertx vertx = Vertx.vertx();
//			String phoneNumber = "13588241822";
//			String phoneNumber = "userID_n1";
			String phoneNumber = "13645160637";
			//191e35f7e045d8deadd
			IOSPushMessage iosPushMessage = new IOSPushMessage(phoneNumber);
			System.out.println(iosPushMessage.toString());
			PushMessageTool.pushMessage(null, vertx, iosPushMessage, phoneNumber, result -> {
				System.out.println(result);
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testUnicastWithPushID() {
		try {
			Vertx vertx = Vertx.vertx();
//			String phoneNumber = "13588241822";
//			String phoneNumber = "userID_n1";
			String pushID = "101d855909469e09b3f";
			//191e35f7e045d8deadd
			IOSPushMessage iosPushMessage = IOSPushMessage.createIOSPushMessageForPushID(pushID, "liangliu send");
			System.out.println(iosPushMessage.toString());
			PushMessageTool.pushMessage(null, vertx, iosPushMessage, "000", result -> {
				System.out.println(result);
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
