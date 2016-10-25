package com.chinaway.tms.quartz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysUserService;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.basic.service.WaybillService;
import com.chinaway.tms.util.Http2G7Util;
import com.chinaway.tms.utils.http.HttpClientUtils;
import com.chinaway.tms.utils.io.ProperUtil;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.lang.DateUtil;
import com.chinaway.tms.ws.service.PushService;

/**
 * 订单和运单的计时器
 * @author shu
 *
 */
public class OrdersJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrdersJob.class);
	
	private static String warehousePath = ProperUtil.read("wsconfig.properties", "ws.warehouse");
	private static String wmsPath = ProperUtil.read("wsconfig.properties", "ws.wms");

	@Autowired
	private OrdersService ordersService;
	@Autowired
	private WaybillService waybillService;
	@Autowired
	private PushService pushService;
	@Autowired
	private SysUserService sysUserService;
	
	public void quartMethod(){
		//报错也继续执行
		try {
			selectAllOrder();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				selectAllDeparture();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 每过20min更新一次订单数据<br>
	 * 其中涉及到订单状态（3签收）的特殊处理：state=3需要将信息推送给仓配平台
	 * @throws Exception 
	 */
	private void selectAllOrder() throws Exception {
		
		String updatetimeGe = "";
		String updatetimeLt = "";
		Date updatetime = ordersService.selectMaxUpdateTime();
		
		if(updatetime != null){
			//有记录时间 -- 有记录时间+2
			Calendar c = Calendar.getInstance();
			c.setTime(updatetime);
			c.add(Calendar.SECOND, 1);
			updatetimeGe = c.getTime().toLocaleString();
			
			c.add(Calendar.DAY_OF_MONTH, 2);
			updatetimeLt = c.getTime().toLocaleString();
		}else{
			//设置2天前
			Calendar c = Calendar.getInstance();
			Date now = new Date();
			c.setTime(now);
			c.add(Calendar.DAY_OF_MONTH, -2);
			updatetimeGe = c.getTime().toLocaleString();
			updatetimeLt = now.toLocaleString();
		}
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("updatetimeGe", updatetimeGe);
		paramMap.put("updatetimeLt", updatetimeLt);
		paramMap.put("pageNo", 1);
		paramMap.put("pageSize", 100);
		paramMap.put("useHasNext", true);
		
		List<Orders> list = pushService.selectAllOrders(paramMap);
		//更新状态后，其中Orders数据其实不完整
		Map<String, Object> map =new HashMap<>();
		Orders deOrders = null;
		for (Orders orders : list) {
			if ("3".equals(orders.getState())) {
				map.put("code", orders.getCode());
				List<Orders> deList = ordersService.selectAll4Page(map);
				if (deList.size() >0) {
					deOrders = deList.get(0);
					orderWS(deOrders);
				}
			}
		}
		
	}

	/**
	 * 订单推给仓配接口
	 * @param orders
	 * @throws Exception
	 */
	private void orderWS(Orders orders) throws Exception {
		//将订单推送给仓配平台
		//http://localhost/warehouse/wsorder/add?orderInfo=
		//{"orderno":"32","fromplace":"89890","destination":"98098","fydate":"2016-08-23%2015:48:27","fyamount":"909","dddate":"2016-08-23%2015:48:27","wlcompanyid":"1","brandedcompanyid":"2","latedays":"23","status":"0","weight":"343","volume":"890","type":"2"}
		Map<String,Object> orderMap = new HashMap<>();
		orderMap.put("orderno", orders.getCode());
		orderMap.put("fromplace", orders.getFhaddress());
		orderMap.put("destination", orders.getShaddress());
		orderMap.put("fydate", orders.getGstarttime());
		orderMap.put("dddate", orders.getGarrivetime());
		long mins = DateUtil.timeDiff2(orders.getGarrivetime(), orders.getRequendtime(), DateUtil.MINUTE);
		orderMap.put("latedays", mins);//实际到达时间 - 计划到达时间
		orderMap.put("fyamount", orders.getAmount());
		orderMap.put("weight", orders.getWeight());
		orderMap.put("volume", orders.getVolume());
		orderMap.put("type", orders.getType());
		orderMap.put("status", orders.getState());
		//此处特殊处理，传的是loginname，因为与仓配的id不匹配
		SysUser sysUser =sysUserService.selectById(Integer.parseInt(orders.getSubcontractor()));
		if (sysUser != null) {
			orderMap.put("wlcompanyid", sysUser.getLoginname());
		}
		orderMap.put("brandedcompanyid", orders.getDeptname());
		
		Map<String,Object> httpMap = new HashMap<>();
		String method = "wsorder/add";
		LOGGER.info("调用仓配平台接口："+ method +", 参数：" + orderMap);
		httpMap.put("orderInfo", JsonUtil.obj2JsonStr(orderMap));
		Map<String,Object> returnMap = HttpClientUtils.getResult(httpMap, warehousePath, method, "post");
		LOGGER.info("调用仓配平台接口："+ method +", 返回结果：" + returnMap);
	}

	/**
	 * 每过20min更新一次运单数据<br>
	 * 其中涉及到运单状态（2在途运输 3签收）的特殊处理：<br>
	 * 		state=2第一次需要将运单信息更新推送给wms
	 * 		state=3 需要将运单信息生成一份账单
	 */
	private void selectAllDeparture() throws Exception {
		String updatetimeGe = "";
		String updatetimeLt = "";
		Date updatetime = waybillService.selectMaxUpdateTime();
		
		if(updatetime != null){
			Calendar c = Calendar.getInstance();
			c.setTime(updatetime);
			c.add(Calendar.SECOND, 1);
			updatetimeGe = c.getTime().toLocaleString();
			
			//不设置上限
//			c.add(Calendar.DAY_OF_MONTH, 2);
//			updatetimeLt = c.getTime().toLocaleString();
		}else{
			updatetimeLt = new Date().toLocaleString();
		}
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("updatetimeGe", updatetimeGe);
		paramMap.put("updatetimeLt", updatetimeLt);
		paramMap.put("pageNo", 1);
		paramMap.put("pageSize", 100);
		paramMap.put("useHasNext", true);
		
		List<Waybill> list = pushService.selectAllDeparture(paramMap);
		//更新状态后，其中Orders数据其实不完整
		Map<String, Object> map =new HashMap<>();
		Waybill deWaybill = null;
		for (Waybill waybill : list) {
			if ("2".equals(waybill.getState())) {
				map.put("code", waybill.getCode());
				List<Waybill> deList = waybillService.selectAll4Page(map);
				if (deList.size() >0) {
					deWaybill = deList.get(0);
					//开始运输 时运单 推送给wms （修改状态）
					updateDep2wms(deWaybill);
				}
			}else if ("3".equals(waybill.getState())) {
				map.put("code", waybill.getCode());
				List<Waybill> deList = waybillService.selectAll4Page(map);
				if (deList.size() >0) {
					deWaybill = deList.get(0);
					//签收的处理（仓配平台）：费用、时效信息
					billWS(deWaybill);
					efficiencyWS(deWaybill);
				}
			}
			
		}
		
	}

	/**
	 * 开始运输 时运单 推送给wms （修改状态）
	 * @param waybill
	 */
	private void updateDep2wms(Waybill waybill) throws Exception {
		Map<String, Object> map =new HashMap<>();
		map.put("stowageNumber", waybill.getCode());
		map.put("status", "2");
		LOGGER.info("调用wms平台接口, 参数：" + map.toString());
		Map<String, Object> resultMap = HttpClientUtils.getResult(map, wmsPath, "", "get");
		LOGGER.info("调用仓配平台接口, 返回结果：" + resultMap);
	}

	/**
	 * 费用接口
	 * @throws Exception
	 */
	private void billWS(Waybill waybill) throws Exception {
		Map<String,Object> billMap = new HashMap<>();
		//{"type":"1","city":"wuhan","brandedname":"wenfeng","wlname":"shnfeng","billdate":"2016-07-01","total":"100","price":"34","number":"343","brandedcompanystatus":"34","wlcompanystatus":"354","brandedcompanyid":"232","wlcompanyid":"34"}
		billMap.put("type", "1");//运输费
//		billMap.put("city", "");
		billMap.put("brandedname", waybill.getDeptname());
		billMap.put("wlname", waybill.getWlcompany());
		billMap.put("billdate", new Date());
		billMap.put("total", waybill.getAmount() * 100);
		billMap.put("price", 100);
		billMap.put("number", waybill.getAmount());
		billMap.put("brandedcompanystatus", "0");
		billMap.put("wlcompanystatus", "0");
		billMap.put("brandedcompanyid", waybill.getDeptname());
		billMap.put("wlcompanyid", waybill.getWlcompany());
		
		Map<String,Object> httpMap = new HashMap<>();
		String method = "wsbill/add";
		LOGGER.info("调用仓配平台接口："+ method +", 参数：" + billMap);
		httpMap.put("billInfo", JsonUtil.obj2JsonStr(billMap));
		Map<String,Object> resultMap = HttpClientUtils.getResult(httpMap, warehousePath, method, "post");
		LOGGER.info("调用仓配平台接口："+ method +", 返回结果：" + resultMap);
	}
	
	/**
	 * 时效接口
	 * @throws Exception
	 */
	private void efficiencyWS(Waybill waybill) throws Exception {
		Map<String,Object> efficiencyMap = new HashMap<>();
		//{"awb":"32","wlcompanyid":"1","brandedcompanyid":"23","pzdate":"2016-08-17%2015:20:58","fsdate":"2016-08-17%2015:20:58","jhdate":"2016-08-17%2015:20:58","dddate":"2016-08-17%2015:20:58","latetimes":"5","money":"90"}
		efficiencyMap.put("awb", waybill.getCode());
		efficiencyMap.put("wlcompanyid", waybill.getWlcompany());
		efficiencyMap.put("brandedcompanyid", waybill.getDeptname());
		efficiencyMap.put("pzdate", "");
		efficiencyMap.put("fsdate", waybill.getGstarttime());
		efficiencyMap.put("jhdate", waybill.getRequendtime());
		efficiencyMap.put("dddate", waybill.getGarrivetime());
		long mins = DateUtil.timeDiff2(waybill.getGarrivetime(), waybill.getRequendtime(), DateUtil.MINUTE);
		efficiencyMap.put("latetimes", mins);//实际到达时间 - 计划到达时间
		String money = "0";
		if (mins > 5) {
			money = "500";
		}
		efficiencyMap.put("money", money);
		
		
		Map<String,Object> httpMap = new HashMap<>();
		String method = "wsefficiency/add";
		LOGGER.info("调用仓配平台接口："+ method +", 参数：" + efficiencyMap);
		httpMap.put("efficiencyInfo", JsonUtil.obj2JsonStr(efficiencyMap));
		Map<String,Object> resultMap = HttpClientUtils.getResult(httpMap, warehousePath, method, "post");
		LOGGER.info("调用仓配平台接口："+ method +", 返回结果：" + resultMap);
	}
}
