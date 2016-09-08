package com.tms.core.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.chinaway.tms.utils.http.HttpClientUtils;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.lang.DateUtil;
import com.chinaway.tms.utils.lang.StringUtil;

//	1、订单导入接口-order.order.createOrders
//	2、订单生成车次接口-order.order.createDeparture
//	3、订单增量列表查询接口-order.order.getUpdateOrderList
//	4、单个订单详情查询接口byNo-order.order.getOrderInfoByNo
//	7、订单批量删除接口-order.order.removeOrders
//	5、车次增量列表查询接口-order.departure.getUpdateDepartureList
//
//	6、取消下发且删除车次（暂没有接口）
public class MyTest {
	
	/**
	 * 订单删除
	 * @throws Exception
	 */
    @Test
    public void test_order_delete() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="{\"orgcode\": \"20016C\", "
    			+ " \"orders\": [\"wxg2016083007\"]"
    			+ "}";
    	
    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
    	String app_key = "wine1919";
    	String method = "order.order.removeOrders";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	//	wine1919 fce6dc5652df05b2a7ae287337702eb6
    	// http://g7s.api.huoyunren.com/interface/index.php?{method}=ucenter.user.createUser
    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    }
	
	
	/**
	 * 订单删除
	 * @throws Exception
	 */
    @Test
    public void test_d_delete() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="{\"orgcode\": \"20016C\", "
    			+ " \"orders\": [\"D2016082900001\"]"
    			+ "}";
    	
    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
    	String app_key = "wine1919";
    	String method = "order.departure.removeDepartures";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	//	wine1919 fce6dc5652df05b2a7ae287337702eb6
    	// http://g7s.api.huoyunren.com/interface/index.php?{method}=ucenter.user.createUser
    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    }
    
	/**
	 * 订单导入
	 * @throws Exception
	 */
    @Test
    public void order_add() throws Exception {
        //post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="{\"orgcode\": \"20016C\",\"repeatway\": 1,\"unification\": 0,\"orders\": [{"
    			+ "\"orderno\" : \"tms2016090601\","
	+ "\"wmsno\" : \"wxg2016051901\","
	+ "\"userorderno\" : \"BSORDER001\", "
	+ "\"begintime\" : \"2016-05-19 12:00:00\","
	+ "\"scompany\" : \"天下汇通\","
	+ "\"sprovince\" : \"北京市\","
	+ "\"scity\"  : \"北京\","
	+ "\"sdistricts\" : \"海淀区\","
	+ "\"slocation\" : \"北京市海淀区农大南路1号\","
	+ "\"sname\" : \"王新刚\", "
	+ "\"sphone\" : \"18701432591\","
	+ "\"sdatetime\" : \"2016-05-20 12:00:00\","
	+ "\"ssitename\" : \"北京站\","
    + "\"rcompany\" : \"天下汇通\","
	+ "\"rprovince\" : \"上海市\", "
	+ "\"rcity\"  : \"上海\", "
	+ "\"rdistricts\" : \"闵行区\", "
	+ "\"rlocation\" : \"上海市闵行区七宝古镇1号\", "
	+ "\"rname\" : \"新刚王\","
	+ "\"rphone\" : \"17701432591\", "
	+ "\"rdatetime\" : \"2016-05-21 12:00:00\","
	+ "\"rsitename\"    : \"上海站\","

    + " \"goods\":[{"
    		+ "\"goodsname\": \"肥皂\","
                + "\"sku\": \"10001\","
                + "\"unit\": \"盒\","
               + " \"number\": 11,"
                + "\"weight\": 11,"
                + "\"volume\": 11"
          + "  }] "
      + "}]"
      + "}";
       
    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
    	String app_key = "wine1919";
    	String method = "order.order.createOrders";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
        map.put("method", method);
        map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
        map.put("timestamp", timestamp);
        map.put("sign", sign);

        String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
        Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
        System.out.println(resultMap);
        
    }
    
    /**
     * 订单增量
     * @throws Exception
     */
    @Test
    public void order_select() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="{\"orgcode\": \"2000FJ\", \"pageNo\":1,\"pageSize\":20, "
    			+ "\"updatetimeGe\" : \"2016-08-30 00:19:49\",\"updatetimeLt\": \"2016-08-30 11:19:49\","
//    			+ " \"ordernoIn\": [\"wxg2016051901\"],"
    			+ "\"fields\": [\"wmsno\",\"userorderno\",\"begintime\",\"scompany\",\"sprovince\",\"scity\",\"sdistricts\"]"
    			+ "}";
    	
    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
    	String app_key = "wine1919";
    	String method = "order.order.getUpdateOrderList";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    	
    }
    
    /**
     * 订单详情
     * @throws Exception
     */
    @Test
    public void order_detail() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="{\"orgcode\": \"20016C\", "
    			+ " \"orderno\": \"wxg2016083007\","
    			+ "\"fields\": [\"wmsno\",\"userorderno\",\"begintime\",\"scompany\",\"sprovince\",\"scity\",\"sdistricts\"]"
    			+ "}";
    	
    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
    	String app_key = "wine1919";
    	String method = "order.order.getOrderInfoByNo";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	//	wine1919 fce6dc5652df05b2a7ae287337702eb6
    	// http://g7s.api.huoyunren.com/interface/index.php?{method}=ucenter.user.createUser
    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    	
    }
    
    /**
     * 订单详情(不同账号)
     * @throws Exception
     */
    @Test
    public void order_detail2() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="{\"orgcode\": \"200H1R\", "
    			+ " \"orderno\": \"interface-1\","
    			+ "\"fields\": ["
    				+ " \"userorderno\",\"begintime\",\"fromorgcode\",\"fromtime\", \"scompany\","
    				+ " \"sprovince\",\"scity\",\"sdistricts\",\"slocation\",\"sname\",\"sphone\","
    				+ " \"sdatetime\",\"ssitename\",\"rcompany\",\"rprovince\",\"rcity\",\"rdistricts\","
    				+ " \"rlocation\",\"rname\",\"rphone\",\"rdatetime\",\"rsitename\",\"extendFields\",\"departures\","
    				+ " \"departures.signdetail\",\"departures.zptstatuslist\",\"departures.zptevents\",\"departures.classlineevents\""
    				+ "]"
    			+ "}";
    	
    	String app_secret = "12cf88456f79a0f83e96dc69abbbf3dc";
    	String app_key = "order_test";
    	String method = "order.order.getOrderInfoByNo";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	//	wine1919 fce6dc5652df05b2a7ae287337702eb6
    	// http://g7s.api.huoyunren.com/interface/index.php?{method}=ucenter.user.createUser
    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    	
    }
    
    /**
     * 运单增量查询
     * @throws Exception
     */
    @Test
    public void departure_select() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="{\"orgcode\": \"2000FJ\", \"pageNo\":\"1\",\"pageSize\":\"20\", "
    			+ "\"updatetimeGe\" : \"2016-08-30 00:19:49\","
//    			+ "\"updateimeLt\": \"2016-08-30 01:19:49\","
//    			+ " \"departurenoIn\": [\"wxg2016051901\"],"
    			+ "\"fields\": [ \"departureno\",\"begintime\",\"fromorgcode\",\"fromtime\",\"carnum\",\"carriagetype\",\"driverphone\"]"
    			+ "}";
    	
    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
    	String app_key = "wine1919";
    	String method = "order.departure.getUpdateDepartureList";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    	
    }
    
    /**
     * 运单导入(订单生成车次)
     * @throws Exception
     */
    @Test
    public void departure_add() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="{\"orgcode\": \"20016C\","
    			+ " \"orders\" : [\"tms2016090601\"], "
//    			+ "\"departureno\" : \"D2016090601\", "
//    			+ "\"begintime\" : \"2016-07-10 17:01:00\","
    			+ "\"carriagetype\" : \"3.5M箱式\", "
//    			
    			+ " \"type\":\"1\",\"classlinemode\":\"0\",\"zptmode\":\"1\", "
    			+ " \"issuetogether\":\"0\",\"issued\":\"0\" "
//    			+ "\"starttime\" : \"2016-07-19 17:19:49\","
//    			+ "\"endtime\" : \"2016-07-21 17:19:49\" "
    		+ "}";
    	
    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
    	String app_key = "wine1919";
    	String method = "order.order.createDeparture";
//    	String method = "order.departure.createDeparture";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    	
    }
    
    /**
     * 运单详情
     * @throws Exception
     */
    @Test
    public void test6() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="{\"orgcode\": \"20016C\","
    			+ " \"orders\" : [\"wxg2016083007\"], "
    			+ " \"issued\"  : \"0\", "
    			+ "\"departureno\" : \"D2016082900001\", "
    			+ "\"begintime\" : \"2016-07-10 17:01:00\","
    			+ "\"carriagetype\" : \"3.5M箱式\", "
    			+ " \"type\":\"1\",\"classlinemode\":\"0\",\"zptmode\":\"1\","
    			+ "\"starttime\" : \"2016-07-19 17:19:49\","
    			+ "\"endtime\" : \"2016-07-21 17:19:49\" "
    			+ "}";
    	
    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
    	String app_key = "wine1919";
    	String method = "order.departure.createDeparture";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    	
    }
    
    /**
     * 新增承运商
     * @throws Exception
     */
    @Test
    public void add_user() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
    	String param ="[{\"orgcode\":\"20016C\",\"roleids\":\"aa\",\"username\":\"aa\",\"realname\":\"aa\",\"passwd\":\"aa\",\"telephone\":\"aa\",\"mobile\":\"aa\",\"email\":\"aa\"}]";
    	
    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
    	String app_key = "wine1919";
    	String method = "ucenter.user.createUser";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	//	wine1919 fce6dc5652df05b2a7ae287337702eb6
    	// http://g7s.api.huoyunren.com/interface/index.php?{method}=ucenter.user.createUser
    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    	
    }
    
    @Test
    public void test() throws Exception {
    	String app_secret = "ab179020b82d2fdcd4cea176796f7156";
    	String app_key = "apitest";
    	String data = "{\"carnum\":\"京A09999\"}";
    	String method = "ips2.api.currentStatus";
    	String timestamp = "2012-09-19 19:12:05";
    	String sign = md5(app_secret, app_key, data, method, timestamp);
    	System.out.println(sign);
    }
    
    //uppercase(md5(\"ab179020b82d2fdcd4cea176796f7156app_keyapitestdata{"carnum":"京A09999"}methodips2.api.currentStatustimestamp2012-09-19 19:12:05ab179020b82d2fdcd4cea176796f7156'))的值为：
    //ADE7EE76602120D29FD84363EC470361
    private String md5(String app_secret, String app_key, String data, String method, String timestamp){
    	String md5 = DigestUtils.md5Hex(app_secret+"app_key"+app_key+"data"+data+"method"+method+"timestamp"+timestamp+app_secret);
    	if(md5.length() !=32){
    		md5 = DigestUtils.md5Hex("123");
    	}
//    	String resultStr = md5.toUpperCase();
    	
    	return md5.toUpperCase();
    }
    
}
