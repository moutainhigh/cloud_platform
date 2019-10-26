package cn.sensordb2.stcloud.socketio;

/**
 * Created by sensordb on 16/8/19.
 */

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

public class DemoServer {
    public static void main(String[] args) throws InterruptedException
    {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        SocketIOServer server = new SocketIOServer(config);
//        CharteventListener listner = new CharteventListener();
//        listner.setServer(server);
        // chatevent为事件名称
        //server.addEventListener("chatevent", String.class, listner);
        //启动服务
        System.out.println("server started");
        server.start();

        Thread.sleep(Integer.MAX_VALUE) ;
        server.stop();
        System.out.println("server end");
    }
}



