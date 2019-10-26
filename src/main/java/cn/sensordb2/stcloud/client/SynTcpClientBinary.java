package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.ServerBinaryConvertorManager;
import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.IniUtil;
import io.vertx.core.buffer.Buffer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class SynTcpClientBinary {
	private static final String DELIMIT = "\r\n";
	private static final int port = IniUtil.getInstance().getServerPort();
	private static final String host = IniUtil.getInstance().getServerHostName();

	public static void client(Vector<Buffer> requests, int port) {
		try {
			System.out.println(String.format("SynTcpClientBinary start to connect server:%s port:%d", host, port));
			Socket socket = new Socket(host,port);
			System.out.println("server connected");
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			int i = 0;
			for(Buffer buffer : requests) {
					out.write(buffer.getBytes());
					out.writeByte(0);
					out.flush();
					
					System.out.println(++i);

					Request request = ServerBinaryConvertorManager.getInstance().convert(buffer);
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
	
	public static void connectNormalAndRequest(Vector<Buffer> requests) {
		SynTcpClientBinary.client(requests, port);
	}
}
