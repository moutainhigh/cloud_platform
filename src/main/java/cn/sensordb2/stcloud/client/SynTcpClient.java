package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.IniUtil;
import cn.sensordb2.stcloud.util.Tools;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class SynTcpClient {
	private static final String DELIMIT = "\r\n";
	private static final int port = IniUtil.getInstance().getServerPort();
	private static final String host = IniUtil.getInstance().getServerHostName();
	private static final Object signal = new Object();
	private static boolean showRequestResponse = true;

	public static void client(Vector<Request> requests, int port) {
		SynTcpClient.client(requests, port, null);
	}

	public static void client(Vector<Request> requests, int port, Vector<JsonObject> requestParams) {
		try {
			System.out.println(String.format("start to connect server:%s port:%d", host, port));
			Socket socket = new Socket(host,port);
			System.out.println("server connected");
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			//
			Thread readThread = new Thread() {
				public void run() {
					try {
						synchronized(signal) {						
							signal.wait();
						}
						String line = in.readLine();
						while(line!=null) {
							System.out.println(line);
							System.out.println(Tools.bufferToPrettyByteString(Buffer.buffer(line)));
							line = in.readLine();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			readThread.start();

			int errorCount = 0;
			for(int i=0;i<requests.size();i++) {
				Request request = requests.elementAt(i);

				JsonObject requestParam = requestParams!=null?(JsonObject)requestParams.elementAt(i):null;
				out.write(request.toString().getBytes("UTF-8"));
				out.writeBytes(DELIMIT);
				out.flush();

                System.out.println(i+1);

                if(showRequestResponse)
        	        System.out.println(request.toString());

                String result = in.readLine();
				if(showRequestResponse)
					System.out.println(result);

				try {
					JsonObject resultJson = new JsonObject(result);
					JsonObject error = resultJson.getJsonObject("error");

					if (requestParam != null) {
						requestParam.put("error", error);
						System.out.println(String.format("%s", requestParam.toString()));
					}

					if (error != null) {
						errorCount++;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(String.format("errorCount:%d", errorCount));
			System.out.println(String.format("totalCount:%d", requests.size()));

			synchronized(signal) {
				signal.notify();
			}
			readThread.join();
			out.close();
			in.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	public static void connectNormalAndRequest(Vector<Request> requests) {
		SynTcpClient.client(requests, port);
	}

	public static void connectNormalAndRequest(Vector<Request> requests, Vector<JsonObject> requestParams) {
		SynTcpClient.client(requests, port, requestParams);
	}

	public static void setShowRequestResponse(boolean showRequestResponse) {
		SynTcpClient.showRequestResponse = showRequestResponse;
	}
}
