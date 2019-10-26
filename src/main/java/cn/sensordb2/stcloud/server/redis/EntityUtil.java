package cn.sensordb2.stcloud.server.redis;

import cn.sensordb2.stcloud.server.ConnectionInfo;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;

public class EntityUtil {
 
    static {
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new ByteConverter(null), Byte.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new FloatConverter(null), Float.class);
        ConvertUtils.register(new DateConverter(null), DateConverter.class);
    }
 
    public static Map<String, String> objectToHash(Object obj) {
        try {
            Hashtable<String, String> map = new Hashtable<String, String>();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                if (!property.getName().equals("class")) {
                    if (property.getReadMethod().invoke(obj) != null) {
                        map.put(property.getName(), "" + property.getReadMethod().invoke(obj));
                    }
                }
            }
            return map;
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
 
    @SuppressWarnings("unchecked")
    private static void hashToObject(Map<?, ?> map, Object obj) {
        try {
            BeanUtils.populate(obj, (Map)map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void main(String[] args) {
    	ConnectionInfo ci = new ConnectionInfo(1, (NetSocket)null, true, 1);
    	Map<String, String> map = EntityUtil.objectToHash(ci);	
    	Vertx vertx = Vertx.vertx();
    	RedisPool.initInstance(vertx);
    	OnlineUserMonitorRedisUtil.updateConnection(ci);
    	System.out.println(map);
    }
 }
