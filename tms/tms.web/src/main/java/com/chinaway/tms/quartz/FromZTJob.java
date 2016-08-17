package com.chinaway.tms.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.basic.model.Smd;
import com.chinaway.tms.basic.model.Warehouse;
import com.chinaway.tms.basic.service.CpmdService;
import com.chinaway.tms.basic.service.SmdService;
import com.chinaway.tms.basic.service.WarehouseService;
import com.chinaway.tms.utils.http.HttpClientUtils;

/**
 * 中台平台的数据拉取
 * @author shu
 *
 */
@Component
public class FromZTJob {

	@Autowired
	WarehouseService warehouseService;
	@Autowired
	SmdService smdService;
	@Autowired
	CpmdService cpmdService;
	
	public void quartMethod(){
		//报错也继续执行
		try {
			selectAllWarehouse();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				selectAllSmd();
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					selectAllCpmd();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 拉取仓库信息
	 * @throws Exception
	 */
	public void selectAllWarehouse() throws Exception{
		//查询表里面是否有数据，没有就查询所有，有则增量查询下
		Map<String, Object> argsMap = new HashMap<>();
		List<Warehouse> oldList = warehouseService.selectAll4Page(argsMap);
		//post请求的参数
		Map<String, String> map = new HashMap<String, String>();
		if (oldList.size() > 0 ) {
			//加上增量条件
//				String param ="{\"address\":\"四川省成都市双流区天府四街软件园\",\"area\":\"0\",\"city\":\"成都\",\"code\":\"028\",\"createtime\":\"2016-08-02 17:28:21\",\"deptname\":\"1919酒类直供\",\"id\":0,\"isprivate\":\"私有\",\"name\":\"天府四街仓库\",\"province\":\"四川省\",\"types\":\"标点\",\"updatetime\":\"2016-08-02 17:28:21\"}";
//				map.put("siteInfo", param);
		}
		//http:// 10.13.30.202/centerDocker/api/mdbWeb/warehouse/findAll
		Map<String, Object> resultMap = HttpClientUtils.getResult(map, "http:// 10.13.30.202/centerDocker/", "/api/mdbWeb/warehouse/findAll", "post");
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
	}
	
	/**
	 * 拉取门店信息
	 * @throws Exception
	 */
	public void selectAllSmd() throws Exception{
		//查询表里面是否有数据，没有就查询所有，有则增量查询下
		Map<String, Object> argsMap = new HashMap<>();
		List<Smd> oldList = smdService.selectAll4Page(argsMap);
		//post请求的参数
		Map<String, String> map = new HashMap<String, String>();
		if (oldList.size() > 0 ) {
			//加上增量条件
//				String param ="{\"address\":\"四川省成都市双流区天府四街软件园\",\"area\":\"0\",\"city\":\"成都\",\"code\":\"028\",\"createtime\":\"2016-08-02 17:28:21\",\"deptname\":\"1919酒类直供\",\"id\":0,\"isprivate\":\"私有\",\"name\":\"天府四街仓库\",\"province\":\"四川省\",\"types\":\"标点\",\"updatetime\":\"2016-08-02 17:28:21\"}";
//				map.put("siteInfo", param);
		}
		//http:// 10.13.30.202/centerDocker/api/mdbWeb/smd/findAll
		Map<String, Object> resultMap = HttpClientUtils.getResult(map, "http:// 10.13.30.202/centerDocker/", "/api/mdbWeb/smd/findAll", "post");
		String status = (String) resultMap.get("status");
		String msg = (String) resultMap.get("msg");
		if (!"EXECUTE_SUCCESS".equals(status)) {
			throw new Exception(msg);
		}else{
			//拉取后返回的结果
			List<Smd> list2 = (List<Smd>) resultMap.get("respBody");
//				List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get("respBody");
			for (Smd smd : list2) {
				smdService.insert(smd);
			}
		}
	}
	
	/**
	 * 拉取商品信息
	 * @throws Exception
	 */
	public void selectAllCpmd() throws Exception{
		//查询表里面是否有数据，没有就查询所有，有则增量查询下
		Map<String, Object> argsMap = new HashMap<>();
		List<Cpmd> oldList = cpmdService.selectAll4Page(argsMap);
		//post请求的参数
		Map<String, String> map = new HashMap<String, String>();
		if (oldList.size() > 0 ) {
			//加上增量条件
//				String param ="{\"address\":\"四川省成都市双流区天府四街软件园\",\"area\":\"0\",\"city\":\"成都\",\"code\":\"028\",\"createtime\":\"2016-08-02 17:28:21\",\"deptname\":\"1919酒类直供\",\"id\":0,\"isprivate\":\"私有\",\"name\":\"天府四街仓库\",\"province\":\"四川省\",\"types\":\"标点\",\"updatetime\":\"2016-08-02 17:28:21\"}";
//				map.put("siteInfo", param);
		}
		//http:// 10.13.30.202/centerDocker/api/mdbWeb/cpmd/findAll
		Map<String, Object> resultMap = HttpClientUtils.getResult(map, "http:// 10.13.30.202/centerDocker/", "/api/mdbWeb/cpmd/findAll", "post");
		String status = (String) resultMap.get("status");
		String msg = (String) resultMap.get("msg");
		if (!"EXECUTE_SUCCESS".equals(status)) {
			throw new Exception(msg);
		}else{
			//拉取后返回的结果
			List<Cpmd> list2 = (List<Cpmd>) resultMap.get("respBody");
//				List<Map<String, Object>> list = (List<Map<String, Object>>) resultMap.get("respBody");
			for (Cpmd cpmd : list2) {
				cpmdService.insert(cpmd);
			}
		}
	}
	
	
	
	
}
