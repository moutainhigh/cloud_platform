package cn.sensordb2.stcloud.util;

import io.vertx.ext.web.RoutingContext;

public class Debug {
	public static boolean Debug = true;
	private static String DebugSessionIDKey = "DebugSessionIDKey";
	private static int restSessionID = 0;
	
	public static void setDebugSessionID(RoutingContext routingContext) {
		if(Debug&&routingContext!=null) {
			restSessionID++;
			routingContext.data().put(DebugSessionIDKey, restSessionID);
			routingContext.data().put("absoluteURI", routingContext.request().absoluteURI());
//			routingContext.data().put("params", routingContext.request().params());
			routingContext.data().put("uri", routingContext.request().uri());
			routingContext.data().put("query", routingContext.request().query());

		}
	}
	
	public static int getDebugSessionID(RoutingContext routingContext) {
		if(Debug) {
			int sessionID = -1;
			try {
				sessionID = Integer.parseInt(routingContext.data().get(DebugSessionIDKey).toString());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return sessionID;
		}
		return -1;
	}
	
	
}
