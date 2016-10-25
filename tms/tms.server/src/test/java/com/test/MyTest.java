package com.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinaway.tms.basic.model.Warehouse;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.WarehouseService;
import com.chinaway.tms.util.Http2ZTUtil;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.http.HttpClientUtils;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.lang.DateUtil;

public class MyTest {
	
	@Autowired
	WarehouseService warehouseService;

	@Test
    public void departure_select() throws Exception {
    	//post请求的参数
    	Map<String, Object> map = new HashMap<>();
//    	String param ="{\"orgcode\": \"200UHN\", \"pageNo\":\"1\",\"pageSize\":\"20\", "
////    			+ "\"updatetimeGe\" : \"2016-07-30 00:19:49\","
//    			+ "\"updateimeLt\": \"2016-10-30 01:19:49\","
////    			+ " \"departurenoIn\": [\"wxg2016051901\"],"
//    			+ "\"fields\": [\"departureno\",\"begintime\",\"updatetime\",\"carnum\",\"carriagetype\",\"drivername\",\"driverphone\","
//    			+ "\"starttime\",\"endtime\",\"garrivetime\",\"gstarttime\",\"type\",\"status\"]"
//    			+ "}";
    	Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("updatetimeGe", "2016-10-18 01:19:49");
//		paramMap.put("updatetimeLt", "2016-10-20 01:19:49");
		paramMap.put("pageNo", 1);
		paramMap.put("pageSize", 100);
		paramMap.put("useHasNext", true);
    	paramMap.put("orgcode", "200UHN");
		paramMap.put("fields", new String[]{"departureno","begintime","updatetime","carnum","carriagetype","drivername","driverphone",
				"starttime","endtime","garrivetime","gstarttime","type","status"});
		String param = 	JsonUtil.obj2JsonStr(paramMap);
		
    	String  urlRoot ="http://g7s.api.huoyunren.com/interface/index.php";
//    	String  urlRoot ="http://test.api.g7s.chinawayltd.com/interface/index.php";
//    	String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
//    	String app_key = "wine1919";
    	 String app_secret = "33054ddd19b5ceef400bbfff2b8c9034";
    	 String app_key = "yijiu_admin";
    	String method = "order.departure.getUpdateDepartureList";
    	String timestamp = new Date().toLocaleString();
    	String sign = md5(app_secret, app_key, param, method, timestamp);
    	
    	map.put("data", param);
    	map.put("method", method);
    	map.put("app_key", app_key);
//        map.put("app_secret", app_secret);//只参与计算
    	map.put("timestamp", timestamp);
    	map.put("sign", sign);
    	
    	Map<String, Object> resultMap = HttpClientUtils.getResult(map, urlRoot, "", "post");
    	System.out.println(resultMap);
    	
    	Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
    	Object obj = dataMap.get("hasNext");
//    	boolean hasNext = Boolean.parseBoolean(obj.toString());
    	boolean hasNext = (boolean) dataMap.get("hasNext");
    	System.out.println(hasNext);
//    	Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
//    	List<Waybill> list = getDepList(resultMap);
//    	System.out.println(list);
    }
	
    private String md5(String app_secret, String app_key, String data, String method, String timestamp){
    	String md5 = DigestUtils.md5Hex(app_secret+"app_key"+app_key+"data"+data+"method"+method+"timestamp"+timestamp+app_secret);
    	if(md5.length() !=32){
    		md5 = DigestUtils.md5Hex("123");
    	}
//    	String resultStr = md5.toUpperCase();
    	
    	return md5.toUpperCase();
    }
    
    private List<Waybill> getDepList(Map<String, Object> resultMap) {
		List<Waybill> list = new ArrayList<>();
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		if(dataMap != null){
			List<Map<String, Object>> resultList = (List<Map<String, Object>>) dataMap.get("result");
			Waybill waybill = null;
			for (Map<String, Object> map : resultList) {
				waybill = new Waybill();
				waybill.setCode(map.get("departureno").toString());
				waybill.setUpdatetime(DateUtil.strToDate(map.get("updatetime").toString()));
//				waybill.setRequstarttime(DateUtil.strToDate(map.get("requstarttime").toString()));
//				waybill.setRequendtime(DateUtil.strToDate(map.get("requendtime").toString()));
				if(map.get("gstarttime") != null){
					waybill.setGstarttime(DateUtil.strToDate(map.get("gstarttime").toString()));
				}
				if(map.get("garrivetime") != null){
					waybill.setGarrivetime(DateUtil.strToDate(map.get("garrivetime").toString()));
				}
				waybill.setCarnum(map.get("carnum")+"");
				waybill.setCarriagetype(map.get("carriagetype")+"");
				waybill.setDrivername(map.get("drivername")+"");
				waybill.setDriverphone(map.get("driverphone")+"");
				
				//状态被重新定义了，需要翻译
				String state = "";
				if((boolean) map.get("deleted")){
					state = "-1";
				}else{
					//g7当前执行阶段  status   0:未开始 1:已开始 3:已完成
					String currentstatus = map.get("status").toString();
					//运单状态：-1删除 0初始 1下发 2在途运输 3签收
					switch (currentstatus) {
						case "0":
							state = "1";
							break;
						case "1":
							state = "2";
							break;
					}
				}
				waybill.setState(state);
				
				list.add(waybill);
			}
			
		}
		return list;
	}
	
	@Test
	public void test5() throws Exception {
		Warehouse warehouse = new Warehouse();
		Map<String, Object> map = new HashMap<>();
		map.put("id", 3);
		map.put("bukrs", "1919");
		map.put("updatetime","20160911110101");
		
		MyBeanUtil.transMap2Bean(map, warehouse);
		System.out.println(warehouse);
	}
	
	/**
	 * 仓库测试
	 * @throws Exception
	 */
	@Test
	public void test3() throws Exception {
		
		//查询表里面是否有数据，没有就查询所有，有则增量查询下
//		Map<String, Object> argsMap = new HashMap<>();
//		List<Warehouse> oldList = warehouseService.selectAll4Page(argsMap);
		//post请求的参数
		Map<String, Object> map = new HashMap<>();
//		if (oldList.size() > 0 ) {
			//加上增量条件
//			String param ="{\"address\":\"四川省成都市双流区天府四街软件园\",\"area\":\"0\",\"city\":\"成都\",\"code\":\"028\",\"createtime\":\"2016-08-02 17:28:21\",\"deptname\":\"1919酒类直供\",\"id\":0,\"isprivate\":\"私有\",\"name\":\"天府四街仓库\",\"province\":\"四川省\",\"types\":\"标点\",\"updatetime\":\"2016-08-02 17:28:21\"}";
//			map.put("siteInfo", param);
//		}
		//http:// 10.13.30.202/centerDocker/api/mdbWeb/warehouse/findAll
		List<Map<String, Object>> list = Http2ZTUtil.get(map, "http://10.13.30.202/centerDocker/","/api/mdbWeb/warehouse/findAll");
		Warehouse warehouse = null;
		for (Map<String, Object> map2 : list) {
			warehouse = new Warehouse();
			BeanUtils.populate(warehouse, map2);
			MyBeanUtil.transMap2Bean(map2, warehouse);
			warehouseService.insert(warehouse);
		}
	}
	/**
	 * 仓库测试
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		//查询表里面是否有数据，没有就查询所有，有则增量查询下
//		Map<String, Object> argsMap = new HashMap<>();
//		List<Warehouse> oldList = warehouseService.selectAll4Page(argsMap);
		//post请求的参数
		Map<String, Object> map = new HashMap<>();
//		if (oldList.size() > 0 ) {
		//加上增量条件
//			String param ="{\"address\":\"四川省成都市双流区天府四街软件园\",\"area\":\"0\",\"city\":\"成都\",\"code\":\"028\",\"createtime\":\"2016-08-02 17:28:21\",\"deptname\":\"1919酒类直供\",\"id\":0,\"isprivate\":\"私有\",\"name\":\"天府四街仓库\",\"province\":\"四川省\",\"types\":\"标点\",\"updatetime\":\"2016-08-02 17:28:21\"}";
//			map.put("siteInfo", param);
//		}
		//http:// 10.13.30.202/centerDocker/api/mdbWeb/warehouse/findAll
		HttpResponse response = HttpClientUtils.getHttpResponse(map, "http:// 10.13.30.202/centerDocker/","/api/mdbWeb/warehouse/findAll", "post");
//		HttpResponse response = HttpClientUtils.getHttpResponse(map, "http://localhost:80/tms/","/ws/addSite", "post");
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity, "utf-8");
			System.out.println(message);
			//返回结果
			Map<String, Object> resultMap = JsonUtil.jsonStr2Map(message);
			String status = (String) resultMap.get("status");
			String msg = (String) resultMap.get("msg");
			if (!"EXECUTE_SUCCESS".equals(status)) {
				throw new Exception(msg);
			}else{
				//拉取后返回的结果
				List<Warehouse> list2 = (List<Warehouse>) resultMap.get("respBody");
//				List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get("respBody");
				for (Warehouse warehouse : list2) {
					warehouseService.insert(warehouse);
				}
				
			}
			System.out.println(status);
		} else {
			System.out.println("请求失败");
		}
	}
	
	/**
	 * 站点测试
	 * @throws Exception
	 */
	@Test
	public void test1() throws Exception {
		//post请求的参数
		Map<String, Object> map = new HashMap<>();
		String param ="{\"address\":\"四川省成都市双流区天府四街软件园\",\"area\":\"0\",\"city\":\"成都\",\"code\":\"028\",\"createtime\":\"2016-08-02 17:28:21\",\"deptname\":\"1919酒类直供\",\"id\":0,\"isprivate\":\"私有\",\"name\":\"天府四街仓库\",\"province\":\"四川省\",\"types\":\"标点\",\"updatetime\":\"2016-08-02 17:28:21\"}";
		map.put("siteInfo", param);
		
		HttpResponse response = HttpClientUtils.getHttpResponse(map, "http://localhost:80/tms/","/ws/addSite", "post");
//        String ip = "10.0.13.190";
//        HttpClient client = getConnection(ip,80);
//        String urlPath = "http://10.0.13.190:80/payms/cxfws/addCompany";
//        HttpUriRequest post = getRequestMethod(map, urlPath, "post");
//        HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = response.getEntity();
			String message = EntityUtils.toString(entity, "utf-8");
			System.out.println(message);
			
			Map<String, Object> resultMap = JsonUtil.jsonStr2Map(message);
			boolean status = (boolean) resultMap.get("status");
			String msg = (String) resultMap.get("msg");
			if (!status) {
				throw new Exception(msg);
			}
			System.out.println(status);
		} else {
			System.out.println("请求失败");
		}
	}

}
