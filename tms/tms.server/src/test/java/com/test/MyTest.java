package com.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.basic.model.Warehouse;
import com.chinaway.tms.basic.service.WarehouseService;
import com.chinaway.tms.utils.http.HttpClientUtils;
import com.chinaway.tms.utils.json.JsonUtil;

public class MyTest {
	
	@Autowired
	WarehouseService warehouseService;
	
	/**
	 * 仓库测试
	 * @throws Exception
	 */
	@Test
	public void test2() throws Exception {
		//查询表里面是否有数据，没有就查询所有，有则增量查询下
		Map<String, Object> argsMap = new HashMap<>();
		List<Warehouse> oldList = warehouseService.selectAll4Page(argsMap);
		//post请求的参数
		Map<String, Object> map = new HashMap<>();
		if (oldList.size() > 0 ) {
			//加上增量条件
//			String param ="{\"address\":\"四川省成都市双流区天府四街软件园\",\"area\":\"0\",\"city\":\"成都\",\"code\":\"028\",\"createtime\":\"2016-08-02 17:28:21\",\"deptname\":\"1919酒类直供\",\"id\":0,\"isprivate\":\"私有\",\"name\":\"天府四街仓库\",\"province\":\"四川省\",\"types\":\"标点\",\"updatetime\":\"2016-08-02 17:28:21\"}";
//			map.put("siteInfo", param);
		}
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
