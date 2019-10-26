package cn.sensordb2.stcloud.server.common;

import cn.sensordb2.stcloud.server.message.Request;
import cn.sensordb2.stcloud.util.Tools;

/**
 * Created by sensordb on 16/7/9.
 */
public class RequestExceptionPair {
    Request request;
    Throwable throwable;

    public RequestExceptionPair(Request request, Throwable throwable) {
        this.request = request;
        this.throwable = throwable;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("RequestExceptionPair{");
        sb.append("request:");
        if(request!=null) {
            sb.append(request.toString());
        }
        else {
            sb.append("null");
        }

        sb.append(" exception: ");
        if(throwable!=null) {
            sb.append(Tools.getTrace(this.throwable));
        }
        else {
            sb.append("null");
        }
        sb.append("}");
        return sb.toString();
    }
}