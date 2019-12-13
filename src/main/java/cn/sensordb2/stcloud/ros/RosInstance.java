package cn.sensordb2.stcloud.ros;

import edu.wpi.rail.jrosbridge.JRosbridge.WebSocketType;
import edu.wpi.rail.jrosbridge.Ros;

public class RosInstance {

    private static volatile RosInstance rosInstance = null;

    private Ros ros;
    //ros master 所在的主机地址
//    private String hostName = "192.168.1.164";
    private String hostName = "192.168.43.114";
    //默认端口 9090
    private Integer port = 9090;
    //默认协议 websocket  http:ws  https:wss
    private WebSocketType type = WebSocketType.ws;

    public Ros getRos() {
        return ros;
    }

    public void setRos(Ros ros) {
        this.ros = ros;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public WebSocketType getType() {
        return type;
    }

    public void setType(WebSocketType type) {
        this.type = type;
    }

    private RosInstance(String hostName, int port, WebSocketType type) {
        this.ros = new Ros(hostName, port, type);
    }

    private RosInstance(String hostName, int port) {
        this.ros = new Ros(hostName, port, type);
    }

    private RosInstance(String hostName) {
        this.ros = new Ros(hostName, port, type);
    }

    private RosInstance() {
        this.ros = new Ros(hostName, port, type);
    }

    public static RosInstance getInstance() {
        if (rosInstance == null) {
            synchronized (RosInstance.class) {
                if (rosInstance == null) {
                    rosInstance = new RosInstance();
                }
            }
        }
        return rosInstance;
    }

    public static RosInstance getInstance(String hostName) {
        if (rosInstance == null) {
            synchronized (RosInstance.class) {
                if (rosInstance == null) {
                    rosInstance = new RosInstance(hostName);
                }
            }
        }
        return rosInstance;
    }

    public static RosInstance getInstance(String hostName, Integer port) {
        if (rosInstance == null) {
            synchronized (RosInstance.class) {
                if (rosInstance == null) {
                    rosInstance = new RosInstance(hostName, port);
                }
            }
        }
        return rosInstance;
    }

    public static RosInstance getInstance(String hostName, Integer port, WebSocketType type) {
        if (rosInstance == null) {
            synchronized (RosInstance.class) {
                if (rosInstance == null) {
                    rosInstance = new RosInstance(hostName, port, type);
                }
            }
        }
        return rosInstance;
    }
}
