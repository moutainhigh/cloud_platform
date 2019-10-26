package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.IniUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class SynTcpClientForRequests {
	private static final String DELIMIT = "\r\n";
	private static final int port = IniUtil.getInstance().getServerPort();
	private static final String host = IniUtil.getInstance().getServerHostName();

	public static void client(Vector<Request> requests, int port) {
		//����Socket
		try {
			Socket socket = new Socket(host,port);		
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			int i = 0;
			for(Request request: requests) {
					out.writeBytes(request.toString());
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
		SynTcpClientForRequests.client(requests, port);
	}
}
