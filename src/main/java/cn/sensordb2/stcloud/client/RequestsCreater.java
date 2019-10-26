package cn.sensordb2.stcloud.client;

import cn.sensordb2.stcloud.server.message.Request;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

public class RequestsCreater {
	RequestsCreater instance = new RequestsCreater();
	Hashtable<String, Method> methods = new Hashtable();
	
	private RequestsCreater() {
		Method[] methods = Requests.class.getMethods();
		
		for(Method method: methods) {
			this.methods.put(method.getName(), method);
		}

	}
	
	public Vector<Request> createRequests(String[] methodNames) {
		Vector<Request> results = new Vector();
		
		for(String methodName : methodNames) {
			Method method = this.methods.get(methodName);
			if(method==null) continue;
			try {
				results.add((Request)(method.invoke(null, null)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return results;
	}
}
