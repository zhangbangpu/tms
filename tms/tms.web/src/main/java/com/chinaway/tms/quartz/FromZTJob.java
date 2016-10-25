package com.chinaway.tms.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.basic.model.Smd;
import com.chinaway.tms.basic.model.Warehouse;
import com.chinaway.tms.basic.service.CpmdService;
import com.chinaway.tms.basic.service.SmdService;
import com.chinaway.tms.basic.service.WarehouseService;
import com.chinaway.tms.util.Http2ZTUtil;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.http.HttpClientUtils;

/**
 * 中台平台的数据拉取
 * @author shu
 *
 */
public class FromZTJob {

	@Autowired
	WarehouseService warehouseService;
	@Autowired
	SmdService smdService;
	@Autowired
	CpmdService cpmdService;
	
	String urlRoot = "http://10.13.30.202/centerDocker/api/mdbWeb";
//	String urlRoot = "http://10.13.30.214:8080/mdbWeb/api/mdbWeb";//本地测试
	
	public void quartMethod(){
		//报错也继续执行
		try {
			selectAllWarehouse();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				selectAllSmd();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}finally{
				try {
					selectAllCpmd();
					
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 拉取仓库信息
	 * @throws Exception
	 */
	public void selectAllWarehouse() throws Exception{
		//get请求的参数
		Map<String, Object> map = new HashMap<>();
		
		String maxUpdatetime = warehouseService.selectMaxUpdateTime();
		if(null == maxUpdatetime || ("").equals(maxUpdatetime)){
			map.put("updatetimeLt", "");//开始时间
		}else{
			map.put("updatetimeLt", maxUpdatetime);//开始时间
		}
		map.put("updatetimeGt", new Date().toLocaleString());//结束时间
		
		List<Map<String, Object>> list = Http2ZTUtil.get(map, urlRoot, "/warehouse/findWarehouseByUpdatetime");
		Warehouse warehouse = null;
		for (Map<String, Object> map2 : list) {
			warehouse = new Warehouse();
//			BeanUtils.populate(warehouse, map2);
			MyBeanUtil.transMap2Bean(map2, warehouse);
			warehouseService.insert(warehouse);
		}
	}
	
	/**
	 * 拉取门店信息
	 * @throws Exception
	 */
	public void selectAllSmd() throws Exception{
		//get请求的参数
		Map<String, Object> map = new HashMap<>();
		String maxUpdatetime = smdService.selectMaxUpdateTime();
		if(null == maxUpdatetime || ("").equals(maxUpdatetime)){
			map.put("updatetimeLt", "");//开始时间
		}else{
			map.put("updatetimeLt", maxUpdatetime);//开始时间
		}
		map.put("updatetimeGt", new Date().toLocaleString());//结束时间
		
		List<Map<String, Object>> list = Http2ZTUtil.get(map, urlRoot, "/smd/findSmdByUpdatetime");
		Smd smd = null;
		for (Map<String, Object> map2 : list) {
			smd = new Smd();
//			BeanUtils.populate(smd, map2);
			MyBeanUtil.transMap2Bean(map2, smd);
			smdService.insert(smd);
		}
	}
	
	/**
	 * 拉取商品信息
	 * @throws Exception
	 */
	public void selectAllCpmd() throws Exception{
		//get请求的参数
		Map<String, Object> map = new HashMap<>();
		String maxUpdatetime = cpmdService.selectMaxUpdateTime();
		if(null == maxUpdatetime || ("").equals(maxUpdatetime)){
			map.put("updatetimeLt", "");//开始时间
		}else{
			map.put("updatetimeLt", maxUpdatetime);//开始时间
		}
		map.put("updatetimeGt", new Date().toLocaleString());//结束时间
		
		List<Map<String, Object>> list = Http2ZTUtil.get(map, urlRoot, "/cpmd/findCpmdByUpdatetime");
		Cpmd cpmd = null;
		for (Map<String, Object> map2 : list) {
			cpmd = new Cpmd();
//			BeanUtils.populate(cpmd, map2);
			MyBeanUtil.transMap2Bean(map2, cpmd);
			cpmdService.insert(cpmd);
		}
	}
	
}
