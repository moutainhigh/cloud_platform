package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.IniUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class SynTcpClientForTest {
	private static final String DELIMIT = "\r\n";
	private static final int port = 19090;
	private static final String host = "127.0.0.1";
	private static final Object signal = new Object();

	public static void client(Vector<Request> requests, int port) {
		try {
			System.out.println(String.format("start to connect server:%s port:%d", host, port));
			Socket socket = new Socket(host,port);
			System.out.println("server connected");
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			int i = 0;
			for(Request request: requests) {
					out.write(request.toString().getBytes("UTF-8"));
					out.writeBytes(DELIMIT);
					out.flush();
					
					System.out.println(++i);
					System.out.println(request.toString());
					
					String result = in.readLine();
					System.out.println(result);
			}
			
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
		SynTcpClientForTest.client(requests, port);
	}
}
