package com.chinaway.tms.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaway.tms.utils.http.HttpClientUtils;

/**
 * 专门访问g7环境接口工具类
 * @author shu
 *
 */
public class Http2G7Util {

	private static final Logger LOGGER = LoggerFactory.getLogger(Http2G7Util.class);
	
	private Http2G7Util(){}
	
	/**
	 * 获取返回值，并处理
	 * @param resultMap
	 * @return
	 */
	public static boolean getResult(Map<String, Object> resultMap){
		boolean isSuccsee = false;
		String code = resultMap.get("code").toString();
		if("0".equals(code)){
			Map<String, List<Map<String, String>>> dataMap = (Map<String, List<Map<String, String>>>) resultMap.get("data");
			List<Map<String, String>> failList =  dataMap.get("fail");
			if(failList.size() > 0){
				LOGGER.info(failList.toString());
			}else{
				isSuccsee = true;
			}
		}else{
			String message = resultMap.get("message").toString();
			LOGGER.info(message);
		}
		return isSuccsee;
	}
	
	/**
	 * 发送post请求给接口平台获得响应的返回值
	 * @param param
	 * @param app_secret
	 * @param app_key
	 * @param method
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> post(String param, String app_secret, String app_key, String method, String  url) throws Exception {
		LOGGER.info("调用g7接口："+ method +",参数：" + param);
		//post请求的参数
    	Map<String, Object> map = new HashMap<>();
//    	String param ="[{\"orgcode\":\"20016C\",\"roleids\":\"aa\",\"username\":\"aa\",\"realname\":\"aa\",\"passwd\":\"aa\",\"telephone\":\"aa\",\"mobile\":\"aa\",\"email\":\"aa\"}]";
    	
//    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
//    	String app_key = "wine1919";
//    	String method = "ucenter.user.createUser";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
//    	String  url ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, url, "", "post");
    	
    	return resultMap;
	}
	
	/**
	 * 请求中参数sign的MD5算法
	 * @param app_secret	
	 * @param app_key	登录账户
	 * @param data		参数
	 * @param method	接口方法
	 * @param timestamp	时间，格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
    private static String md5(String app_secret, String app_key, String data, String method, String timestamp){
    	String md5 = DigestUtils.md5Hex(app_secret+"app_key"+app_key+"data"+data+"method"+method+"timestamp"+timestamp+app_secret);
    	if(md5.length() !=32){
    		md5 = DigestUtils.md5Hex("123");
    	}
    	
    	return md5.toUpperCase();
    }
	
}
