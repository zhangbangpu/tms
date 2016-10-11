package com.chinaway.tms.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinaway.tms.basic.model.Warehouse;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.http.HttpClientUtils;
import com.chinaway.tms.utils.lang.DateUtil;

/**
 * 专门访问中台环境接口工具类
 * @author shu
 *
 */
public class Http2ZTUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(Http2ZTUtil.class);
	
	private Http2ZTUtil(){}
	
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
	public static Map<String, Object> post(Map<String, Object> map, String urlRoot, String url) throws Exception {
		LOGGER.info("调用中台接口："+ url);
		//post请求的参数
//    	Map<String, Object> map = new HashMap<>();
//    	String param ="[{\"orgcode\":\"20016C\",\"roleids\":\"aa\",\"username\":\"aa\",\"realname\":\"aa\",\"passwd\":\"aa\",\"telephone\":\"aa\",\"mobile\":\"aa\",\"email\":\"aa\"}]";
    	
    	String shopBn = "pos";
    	String timestamp = DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss");
    	
    	String sign = md5(shopBn, timestamp);
    	
    	map.put("shopBn", shopBn);
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, url, "post");
    	
    	return resultMap;
	}
	
	/**
	 * 发送get请求给接口平台获得响应的返回值,处理后返回respBody 的List结果
	 * @param param
	 * @param app_secret
	 * @param app_key
	 * @param method
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> get(Map<String, Object> map, String urlRoot, String url) throws Exception {
		LOGGER.info("调用中台接口："+ url);
		//post请求的参数
//    	Map<String, Object> map = new HashMap<>();
//    	String param ="[{\"orgcode\":\"20016C\",\"roleids\":\"aa\",\"username\":\"aa\",\"realname\":\"aa\",\"passwd\":\"aa\",\"telephone\":\"aa\",\"mobile\":\"aa\",\"email\":\"aa\"}]";
		
		String shopBn = "pos";
		String timestamp = DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss");
		
		String sign = md5(shopBn, timestamp);
		
		map.put("shopBn", shopBn);
		map.put("timestamp", timestamp);
		map.put("sign", sign);
		
		Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, url, "get");
		String status = (String) resultMap.get("status");
		String subMsg = (String) resultMap.get("subMsg");
		if (!"EXECUTE_SUCCESS".equals(status)) {
			throw new Exception(subMsg);
		}else{
			//拉取后返回的结果
			List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get("respBody");
			
			return list;
		}
		
	}
	
	/**
	 * 请求中参数sign的MD5算法
	 * @param shopBn 
	 * @param timestamp
	 * @return
	 */
	private static String md5(String shopBn, String timestamp) {
		String md5 = DigestUtils.md5Hex("shopBn" + shopBn + "timestamp" + timestamp);

		return md5;
	}
	
}
