package com.chinaway.tms.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * map与bean的转换，反射的基本方法
 * BeanUtils.populate(obj, map); Map转成Bean
 * BeanUtils.copyProperties(dest, orig); Bean的复制
 * @author shu
 * http://blog.csdn.net/cuidiwhere/article/details/8130434
 * http://www.oschina.net/code/snippet_1864608_37795
 */
public class MyBeanUtil{
	
	/**
	 * 请求参数转换为map
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getParameterMap(HttpServletRequest request){
		Map<String, Object> map =new HashMap<>();
		Enumeration<String> paraNames = request.getParameterNames();
		while (paraNames.hasMoreElements()) {
			String key = paraNames.nextElement();
			String value = request.getParameter(key);
		    map.put(key, value);
		}
		return map;
	}
	
	/**
	 * Map转成Bean <br>
	 * 不依赖BeanUtils工具类的实现
	 * javaBean与Map<String,Object>互转利用到了java的内省（ Introspector ）和反射（reflect）机制。<br>
	 * 其思路为： 通过类 Introspector 来获取某个对象的 BeanInfo 信息，然后通过 BeanInfo 来获取属性的描述器PropertyDescriptor，<br>
	 * 再利用属性描述器获取某个属性对应的 getter/setter 方法，然后通过反射机制来getter和setter。
	 * @param map
	 * @param obj
	 */
	public static void transMap2Bean(Map<String, Object> map, Object obj) {  
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();  
  
                if (map.containsKey(key)) {  
                    Object value = map.get(key);
                    if (value != null) {
                    	String type = value.getClass().getName();//java.lang.String java.lang.Integer
	                    // 得到property对应的setter方法  
	                    Method setter = property.getWriteMethod();
                    	setter.invoke(obj, value);
					}
                }  
            }
        } catch (Exception e) {  
            System.out.println("transMap2Bean Error " + e);  
        }
    }
	
	/**
	 * Bean转成Map<br>
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> transBean2Map(Object obj) {  
		  
        if(obj == null){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
  
                // 过滤class属性  
                if (!key.equals("class")) {
                    // 得到property对应的getter方法  
                    Method getter = property.getReadMethod();  
                    Object value = getter.invoke(obj);
  
                    map.put(key, value);
                }  
  
            }  
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);  
        }  
  
        return map;  
    }  
	
	/**
     * 暴力获取当前类声明的private/protected变量
     */
    static public Object getDeclaredProperty(Object object, String propertyName)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(propertyName);
        return getDeclaredProperty(object, field);
    }
 
    /**
     * 暴力获取当前类声明的private/protected变量
     */
    static public Object getDeclaredProperty(Object object, Field field)
            throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        Object result = field.get(object);
        field.setAccessible(accessible);
        return result;
    }
 
    /**
     * 暴力设置当前类声明的private/protected变量
     */
    static public void setDeclaredProperty(Object object, String propertyName,
            Object newValue) throws IllegalAccessException,
            NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(propertyName);
        setDeclaredProperty(object, field, newValue);
    }
 
    /**
     * 暴力设置当前类声明的private/protected变量
     */
    static public void setDeclaredProperty(Object object, Field field,
            Object newValue) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        field.set(object, newValue);
        field.setAccessible(accessible);
    }
 
    /**
     * 暴力调用当前类声明的private/protected函数
     */
    static public Object invokePrivateMethod(Object object, String methodName,
            Object[] params) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Class<?>[] types = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
        }
        Method method = object.getClass().getDeclaredMethod(methodName, types);
 
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        Object result = method.invoke(object, params);
        method.setAccessible(accessible);
        return result;
    }
 
    /**
     * 按Filed的类型取得Field列表
     */
    static public List<Object> getFieldsByType(Object object, Class<?> type) {
        ArrayList<Object> list = new ArrayList<Object>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getType().isAssignableFrom(type)) {
                list.add(fields[i]);
            }
        }
        return list;
    }
 
    /**
     * 获得field的getter名称
     */
    public static String getAccessorName(Class<?> type, String fieldName) {
        if (type.getName().equals("boolean")) {
            return "is" + StringUtils.capitalize(fieldName);
        } else {
            return "get" + StringUtils.capitalize(fieldName);
        }
    }
 
    /**
     * 获得field的getter名称
     */
    public static Method getAccessor(Class<?> type, String fieldName) {
        try {
            return type.getMethod(getAccessorName(type, fieldName),
                    new Class[] { type });
        } catch (NoSuchMethodException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
