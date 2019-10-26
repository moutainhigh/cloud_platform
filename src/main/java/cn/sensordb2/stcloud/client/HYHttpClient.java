package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.httpApiServer.HttpApiServerIniUtil;
import cn.sensordb2.stcloud.server.message.Request;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

public class HYHttpClient {
	private static final String DELIMIT = "\r\n";
	private static int port = HttpApiServerIniUtil.getInstance().getServerPort();
	private static String host = HttpApiServerIniUtil.getInstance().getServerHostName();
	private static final String versionField = "v1";
	private static final String apiURLPathPrefix = HttpApiServerIniUtil.getInstance().getServerURL() + versionField;

	public static void clientAsyncGet(Vector<Request> requests) {
		int i = 0;
		Vertx vertx = Vertx.vertx();
		for(Request request: requests) {
			System.out.println(i);
			System.out.println(request.toString());
			HYHttpClient.clientAsyncGet(vertx, i, request, false);
		}
	}

	public static void clientAsyncPost(Vector<Request> requests) {
		int i = 0;
		Vertx vertx = Vertx.vertx();
		for(Request request: requests) {
			System.out.println(i);
			System.out.println(request.toString());
			HYHttpClient.clientAsyncPost(vertx, i, request, false);
		}
	}

	public static void clientAsyncPostSsl(Vector<Request> requests) {
		int i = 0;
		Vertx vertx = Vertx.vertx();
		for(Request request: requests) {
			System.out.println(i);
			System.out.println(request.toString());
			HYHttpClient.clientAsyncPost(vertx, i, request, true);
		}
	}

	public static void client(Vector<Request> requests) {
		int i = 0;
		for(Request request: requests) {
			System.out.println(i);
			System.out.println(request.toString());
			HYHttpClient.client(i, request);
		}

	}

	public static void client(Vector<Request> requests, String userName, String hashedPassword) {
		int i = 0;
		for(Request request: requests) {
			System.out.println(i);
			System.out.println(request.toString());
			HYHttpClient.client(i, request, userName, hashedPassword);
		}

	}

	public static void clientAsyncGet(Vertx vertx, int index, Request request, boolean ssl) {
		HttpClientOptions httpClientOptions = new HttpClientOptions();
		if (ssl) {
			httpClientOptions.setSsl(true).setTrustAll(true);
		}
		HttpClient client = vertx.createHttpClient(httpClientOptions);
		String userName = "userID_1a";
		String hashedPassword = "1";


		System.out.println(String.format("[%d] start to connect http api server:%s port:%d", index, host, port));
//		host = "product.rescloud.rbcloudtech.com";
//		host = "test.rescloud.rbcloudtech.com";
//		host = "127.0.0.1";

		// Specify both port and host name
		HttpClientRequest clientRequest = client.get(port, host, apiURLPathPrefix, response -> {
			response.bodyHandler(totalBuffer -> {
				try {
					String bodyString = totalBuffer.toString();
					JsonObject result = new JsonObject(bodyString);
					System.out.println(String.format("receive response:%s", result.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});

		clientRequest.exceptionHandler(e -> {
			e.printStackTrace();
		});

//        clientRequest.connectionHandler(httpConnection -> {
//            logger.info(String.format("clientRequest connected"));
//
//        });

		System.out.println("send request:"+request.toString());

		clientRequest.putHeader("userName", userName);
		clientRequest.putHeader("hashedPassword", hashedPassword);

		clientRequest.putHeader("Content-Type", "application/json;charset=utf-8");
		clientRequest.putHeader("Content-Length", String.valueOf(request.toString().length()));

		clientRequest.end(request.toString());
	}

	public static void clientAsyncPost(Vertx vertx, int index, Request request, boolean ssl) {
		HttpClientOptions httpClientOptions = new HttpClientOptions();
		if (ssl) {
			httpClientOptions.setSsl(true).setTrustAll(true);
		}
		HttpClient client = vertx.createHttpClient(httpClientOptions);
		String userName = "userID_1a";
		String hashedPassword = "1";



//		host = "product.rescloud.rbcloudtech.com";
//		host = "test.cloud.heycloud.cn";
		host = "wechat.heycloud.cn";
//		host = "127.0.0.1";
		port = 443;
		port = 49001;
		System.out.println(String.format("[%d] start to connect http api server:%s port:%d", index, host, port));

		// Specify both port and host name
		HttpClientRequest clientRequest = client.post(port, host, apiURLPathPrefix, response -> {
			response.bodyHandler(totalBuffer -> {
				try {
					String bodyString = totalBuffer.toString();
					System.out.println(String.format("receive response:%s", bodyString));
					JsonObject result = new JsonObject(bodyString);
					System.out.println(String.format("receive response:%s", result.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		});

		clientRequest.exceptionHandler(e -> {
			e.printStackTrace();
		});

//        clientRequest.connectionHandler(httpConnection -> {
//            logger.info(String.format("clientRequest connected"));
//
//        });

		System.out.println("send request:"+request.toString());

		clientRequest.putHeader("userName", userName);
		clientRequest.putHeader("hashedPassword", hashedPassword);

		clientRequest.putHeader("Content-Type", "application/json;charset=utf-8");
		clientRequest.putHeader("Content-Length", String.valueOf(request.toString().length()));

		clientRequest.end(request.toString());
	}

	public static void client(int index, Request request) {
		String userName = "userID_1a";
		String hashedPassword = "1";


		System.out.println(String.format("[%d] start to connect http api server:%s port:%d", index, host, port));
//		host = "product.rescloud.rbcloudtech.com";
//		host = "test.rescloud.rbcloudtech.com";
//		host = "127.0.0.1";
		String urls = "HTTP://" + host + ":" + port + apiURLPathPrefix;
//		String urls = "www.baidu.com";
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
			http.setRequestProperty("Content-Type", "text/json");
			http.setRequestProperty("userName", userName);
			http.setRequestProperty("hashedPassword", hashedPassword);
			System.out.println(String.format("url:%s", http.getURL()));
			http.connect();
			System.out.println("http api server connected");

			DataOutputStream osw = new DataOutputStream(http.getOutputStream());
			osw.writeBytes(request.toString());
			osw.flush();

			StringBuffer result = new StringBuffer();
			if (http.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					System.out.println(String.format("received:%s", inputLine));
					result.append(inputLine);
				}
				in.close();
				System.out.println(String.format("[%d] result: %s", index, result));
			} else {
				System.out.println(String.format("[%d] result: %s", index, http.getResponseMessage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("err");
		} finally {
			if (http != null)
				http.disconnect();
		}
	}

	public static void client(int index, Request request,
							  String userName, String hashedPassword) {
		System.out.println(String.format("[%d] start to connect http api server:%s port:%d", index, host, port));
//		host = "product.rescloud.rbcloudtech.com";
//		host = "test.rescloud.rbcloudtech.com";
//		host = "127.0.0.1";
		String urls = "HTTP://" + host + ":" + port + apiURLPathPrefix;
//		String urls = "www.baidu.com";
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
			http.setRequestProperty("Content-Type", "text/json");
			http.setRequestProperty("userName", userName);
			http.setRequestProperty("hashedPassword", hashedPassword);
			System.out.println(String.format("url:%s", http.getURL()));
			http.connect();
			System.out.println("http api server connected");

			DataOutputStream osw = new DataOutputStream(http.getOutputStream());
			osw.writeBytes(request.toString());
			osw.flush();

			StringBuffer result = new StringBuffer();
			if (http.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					System.out.println(String.format("received:%s", inputLine));
					result.append(inputLine);
				}
				in.close();
				System.out.println(String.format("[%d] result: %s", index, result));
			} else {
				System.out.println(String.format("[%d] result: %s", index, http.getResponseMessage()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("err");
		} finally {
			if (http != null)
				http.disconnect();
		}
	}

	public static void connectNormalAndRequest(Vector<Request> requests) {
		HYHttpClient.client(requests);
	}

	public static void connectNormalAndRequest(Vector<Request> requests,
											   String userName, String hashedPassword) {
		HYHttpClient.client(requests, userName, hashedPassword);
	}

	public static void connectNormalAndRequestAyncGet(Vector<Request> requests) {
		HYHttpClient.clientAsyncGet(requests);
	}

	public static void connectNormalAndRequestAyncPostSsl(Vector<Request> requests) {
		HYHttpClient.clientAsyncPostSsl(requests);
	}
}
