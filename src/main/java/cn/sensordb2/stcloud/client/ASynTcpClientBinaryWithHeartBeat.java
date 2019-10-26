package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.gate.GateBinaryRequestFactory;
import cn.sensordb2.stcloud.util.IniUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

import java.util.Vector;

public class ASynTcpClientBinaryWithHeartBeat {
	private static final int port = IniUtil.getInstance().getServerPort();
	private static final String host = IniUtil.getInstance().getServerHostName();
	private static Vertx vertx = Vertx.vertx();
	private static boolean heartBeat = true;
	private static int HeartBeatTimeGap = 1000;
	private static int heartBeatID = 10;

	public static void main(String[] args) {
		NetClientOptions options = new NetClientOptions().setConnectTimeout(10000);
		NetClient client = vertx.createNetClient(options);
		client.connect(port, host, res -> {
			if (res.succeeded()) {
				System.out.println("Connected!");
				NetSocket socket = res.result();
				socket.handler(buffer -> {
					ServerStreamProcessorBinary.getInstance().receive(buffer);
				});

				if(heartBeat) {
					vertx.setPeriodic(HeartBeatTimeGap, id -> {
						socket.write(GateBinaryRequestFactory.heartBeat("", Buffer.buffer("123456"), heartBeatID++));
					});
				}

			} else {
				System.out.println("Failed to connect: " + res.cause().getMessage());
			}
		});
	}
	
	/*
	 * ��������ҵ�����˿ڣ�����������
	 */
	public static void connectNormalAndRequest(Vector<Buffer> requests) {
		NetClientOptions options = new NetClientOptions().setConnectTimeout(10000);
		NetClient client = vertx.createNetClient(options);
		client.connect(port, host, res -> {
			if (res.succeeded()) {
				System.out.println("Connected!");
				NetSocket socket = res.result();

				for(Buffer buffer: requests) {
					Buffer test = Buffer.buffer();
					test.appendByte((byte)0);
					test.appendByte((byte)0);
					socket.write(test);
					socket.write(buffer);
					socket.write(test);
				}

				socket.handler(buffer -> {
					ServerStreamProcessorBinary.getInstance().receive(buffer);
				});


				if(heartBeat) {
					vertx.setPeriodic(HeartBeatTimeGap, id -> {
						socket.write(GateBinaryRequestFactory.heartBeat("", Buffer.buffer("123456"), heartBeatID++));
					});
				}

			} else {
				System.out.println("Failed to connect: " + res.cause().getMessage());
			}
		});		
	}
}
