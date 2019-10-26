package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.IniUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;


public class ByteSynTcpClient {
	private static final String DELIMIT = "\r\n";
	private static final int port = IniUtil.getInstance().getServerPort();
	private static final String host = "182.92.108.214";
	//private static final String host = "127.0.0.1";

	public static void client(Vector<Request> requests, int port) {
		//建立Socket
		try {
			Socket socket = new Socket(host,port);		
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());

			int i = 0;
			for(Request request: requests) {
				out.writeBytes(request.toString());
				out.writeBytes(DELIMIT);
				out.flush();
				
				System.out.println(++i);
				System.out.println(request.toString());
				
				byte r;
				while((r=in.readByte())!=-1) {
					System.out.println(r);
				}
			}
			out.close();
			in.close();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void connectNormalAndRequest(Vector<Request> requests) {
		ByteSynTcpClient.client(requests, port);
	}


}
