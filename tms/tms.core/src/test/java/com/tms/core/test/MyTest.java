package com.tms.core.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.chinaway.tms.utils.http.HttpClientUtils;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.lang.DateUtil;

public class MyTest {

    @Test
    public void test1() throws Exception {
        //post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="[{\"orgcode\":\"20016C\",\"roleids\":\"aa\",\"username\":\"aa\",\"realname\":\"aa\",\"passwd\":\"aa\",\"telephone\":\"aa\",\"mobile\":\"aa\",\"email\":\"aa\"}]";
        map.put("data", param);

        map.put("method", "ucenter.user.createUser");
        map.put("app_key", "wine1919");
        map.put("app_secret", "fce6dc5652df05b2a7ae287337702eb6");
//        map.put("timestamp", new Date().toLocaleString());
        map.put("timestamp", new Date().toLocaleString());
        
        //	wine1919 fce6dc5652df05b2a7ae287337702eb6
        // http://g7s.api.huoyunren.com/interface/index.php?{method}=ucenter.user.createUser
        HttpResponse response = HttpClientUtils.getHttpResponse(map, "http://test.api.g7s.chinawayltd.com/interface/index.php","", "post");
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            String message = EntityUtils.toString(entity, "utf-8");
            System.out.println(message);
            
            Map<String, Object> resultMap = JsonUtil.jsonStr2Map(message);
            int code = (int) resultMap.get("code");
            System.out.println(code);
            String msg = (String) resultMap.get("msg");
        } else {
            System.out.println("请求失败");
        }
        
    }
    
    //测试
//    public static void main(String args[]) throws Exception {
//    	//post请求的参数
//    	Map<String, String> map = new HashMap<String, String>();
//    	String param ="{\"address\":\"四川省成都市双流区天府四街软件园\",\"area\":\"0\",\"city\":\"成都\",\"code\":\"028\",\"createtime\":\"2016-08-02 17:28:21\",\"deptname\":\"1919酒类直供\",\"id\":0,\"isprivate\":\"私有\",\"name\":\"天府四街仓库\",\"province\":\"四川省\",\"types\":\"标点\",\"updatetime\":\"2016-08-02 17:28:21\"}";
//    	map.put("siteInfo", param);
//    	
//    	HttpResponse response = HttpClientUtils.getHttpResponse(map, "http://localhost/","/ws/addSite", "post");
//    	if (response.getStatusLine().getStatusCode() == 200) {
//    		HttpEntity entity = response.getEntity();
//    		String message = EntityUtils.toString(entity, "utf-8");
//    		System.out.println(message);
//    		
//    		Map<String, Object> resultMap = JsonUtil.jsonStr2Map(message);
//    		boolean status = (boolean) resultMap.get("status");
//    		String msg = (String) resultMap.get("msg");
//    		if (!status) {
//    			throw new Exception(msg);
//    		}
//    		System.out.println(status);
//    	} else {
//    		System.out.println("请求失败");
//    	}
//    	
//    }
}
