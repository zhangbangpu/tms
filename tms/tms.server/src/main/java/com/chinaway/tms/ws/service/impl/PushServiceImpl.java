package com.chinaway.tms.ws.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
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

@Service
public class PushServiceImpl implements PushService{

	private static final Logger LOGGER = LoggerFactory.getLogger(PushServiceImpl.class);
	
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private WaybillService waybillService;
	
	private String app_secret = "33054ddd19b5ceef400bbfff2b8c9034";
	private String app_key = "yijiu_admin";
//	private String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
//	private String app_key = "wine1919";
	private String url = ProperUtil.read("wsconfig.properties", "ws.g7");
	private String wmsPath = ProperUtil.read("wsconfig.properties", "ws.wms");
	
	private String orgcode = "200UHN";
	
	@Override
	public boolean addOrder(String param) throws Exception {
		String method = "order.order.createOrders";
		Map<String, Object> resultMap = Http2G7Util.post(param, app_secret, app_key, method, url);
		
		return Http2G7Util.getResult(resultMap);
	}

	@Override
	public boolean addDeparture(String param) throws Exception {
		boolean isSuccsee = false;
		
		String method = "order.order.createDeparture";
		Map<String, Object> resultMap = Http2G7Util.post(param, app_secret, app_key, method, url);
		String code = resultMap.get("code").toString();
		if("0".equals(code)){
			isSuccsee = true;
		}else{
			String message = resultMap.get("message").toString();
			LOGGER.info(message);
		}
		return isSuccsee;
		
	}

	@Override
	@Transactional
	public boolean addOrderAndDep(String orderParam, String depParam) throws Exception {
		boolean isSuccsee = false;
		boolean b1 = addOrder(orderParam);
		boolean b2 = addDeparture(depParam);
		if (b1 && b2) {
			isSuccsee = true;
		}
		
		return isSuccsee;
		
	}

	@Override
	public boolean addUser(String param) throws Exception {
		String method = "ucenter.user.createUser";
		Map<String, Object> resultMap = Http2G7Util.post(param, app_secret, app_key, method, url);
		
		return Http2G7Util.getResult(resultMap);
	}

	@Override
	public List<Orders> selectAllOrders(Map<String, Object> paramMap) throws Exception {
		String method = "order.order.getUpdateOrderList";
		paramMap.put("orgcode", orgcode);
		paramMap.put("fields", new String[]{"deleted","id","orderno","wmsno","updatetime","begintime",
				"scompany","slocation","sprovince","scity","sdistricts","sname","sphone","sdatetime","ssitename",
				"rcompany","rlocation","rprovince","rcity","rdistricts","rname","rphone","rdatetime","rsitename",
				"currentstatus","currenttranstype","currentdepartures","finishtype","events"});
		
		//所有订单,由于有分页存在，每次调用都会拿到一个list,将该list添加到totalList
		List<Orders> totalList = new ArrayList<>();
		while(true){
			String param = 	JsonUtil.obj2JsonStr(paramMap);
			Map<String, Object> resultMap = Http2G7Util.post(param, app_secret, app_key, method, url);
			Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
			if(dataMap !=null){
				List<Orders> list = getOrderList(resultMap);
				totalList.addAll(list);
				
				boolean hasNext = (boolean) dataMap.get("hasNext");
				if (hasNext) {
					int pageNo = Integer.parseInt(paramMap.get("pageNo").toString());
					paramMap.put("pageNo", ++pageNo);
				}else{//没有下一页就停止
					break;
				}
			}
			
		}
		
		return totalList;
	}

	/**
	 * 从返回结果中获得订单 （只为更新）
	 * @param resultMap
	 * @return
	 */
	private List<Orders> getOrderList(Map<String, Object> resultMap) {
		List<Orders> list = new ArrayList<>();
		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		if(dataMap != null){
			List<JSONObject> resultList = (List<JSONObject>) dataMap.get("result");
//			List<Map<String, Object>> resultList = (List<Map<String, Object>>) dataMap.get("result");
			Orders orders = null;
			for (JSONObject map : resultList) {
//			for (Map<String, Object> map : resultList) {
				orders = new Orders();
				orders.setCode(map.getString("orderno"));
				orders.setFromcode(map.getString("wmsno"));
				orders.setExceptcount(Integer.parseInt(map.get("events").toString()));
				orders.setUpdatetime(map.get("updatetime").toString());
//				orders.setUpdatetime(DateUtil.strToDate(map.get("updatetime").toString()));
//				orders.setCreatetime(DateUtil.strToDate(map.get("begintime").toString()));
//				orders.setCreatetime(map.get("begintime").toString());
//				orders.setDeptname(deptname);//不会更新
//				orders.setFhaddress(fhaddress);
//				orders.setShaddress(shaddress);
//				orders.setAmount(amount);
//				orders.setVolume(volume);
//				orders.setWeight(weight);
//				orders.setFhsitename(map.get("ssitename").toString());
//				orders.setRequstarttime(DateUtil.strToDate(map.get("sdatetime").toString()));
//				orders.setRequendtime(DateUtil.strToDate(map.get("rdatetime").toString()));
//				orders.setShsitename(map.get("rsitename").toString());

				//状态被重新定义了，需要翻译
				String state = "";
				if((boolean) map.get("deleted")){
					state = "-1";
				}else{
					//g7当前执行阶段  未开始(0)  已配载(5) 已调度(6)  已发车(1) 已到达(2)
					String currentstatus = map.get("currentstatus").toString();
					//tms订单状态：-1删除 0初始 1下发 2在途运输 3签收
					switch (currentstatus) {
						case "1":
							state = "2";
							break;
						case "2":
							state = "3";
							break;
						case "5":
							state = "1";//暂时没已配载，处理为下发
							break;
						case "6":
							state = "1";
							break;
	
						default:
							state = "0";
							break;
					}
				}
				orders.setState(state);
				ordersService.updateSelective(orders);
				
				list.add(orders);
			}
		}
		
		return list;
	}

	@Override
	public List<Waybill> selectAllDeparture(Map<String, Object> paramMap) throws Exception {
		String method = "order.departure.getUpdateDepartureList";
		paramMap.put("orgcode", orgcode);
		paramMap.put("fields", new String[]{"departureno","begintime","updatetime","carnum","carriagetype","drivername","driverphone",
				"starttime","endtime","garrivetime","gstarttime","type","status"});
		
		//所有订单,由于有分页存在，每次调用都会拿到一个list,将该list添加到totalList
		List<Waybill> totalList = new ArrayList<>();
		while(true){
			String param = 	JsonUtil.obj2JsonStr(paramMap);
			Map<String, Object> resultMap = Http2G7Util.post(param, app_secret, app_key, method, url);
			Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
			List<Waybill> list = getDepList(resultMap);
			totalList.addAll(list);
			
//			boolean hasNext = Boolean.parseBoolean(dataMap.get("hasNext").toString());
			boolean hasNext = (boolean) dataMap.get("hasNext");
			if (hasNext) {
				int pageNo = Integer.parseInt(paramMap.get("paramMap").toString());
				paramMap.put("pageNo", ++pageNo);
			}else{//没有下一页就停止
				break;
			}
			
		}
		
		return totalList;
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
				if(map.get("updatetime") != null){
					waybill.setUpdatetime(DateUtil.strToDate(map.get("updatetime").toString()));
				}
				if(map.get("starttime") != null){
					waybill.setRequstarttime(DateUtil.strToDate(map.get("starttime").toString()));
				}
				if(map.get("endtime") != null){
					waybill.setRequendtime(DateUtil.strToDate(map.get("endtime").toString()));
				}
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
					//g7当前执行阶段  status   0:未开始 1:已开始 2:已完成
					String currentstatus = map.get("status").toString();
					//运单状态：-1删除 0初始 1下发 2在途运输 3签收
					switch (currentstatus) {
						case "0":
							state = "1";
							break;
						case "1":
							state = "2";
							break;
						case "2":
							state = "3";
							break;
					}
				}
				waybill.setState(state);
				waybillService.updateSelective(waybill);
				
				list.add(waybill);
			}
			
		}
		return list;
	}

	@Override
	public Map<String, Object> selectOrderDetail(Map<String, Object> paramMap) throws Exception {
		String method = "order.order.getOrderInfoByNo";
		paramMap.put("orgcode", orgcode);
//		paramMap.put("orderno", orderno);
		paramMap.put("fields", new String[]{"userorderno","begintime","fromorgcode","fromtime",
				"sprovince","scity","sdistricts","slocation","sname","sphone","sdatetime","ssitename","scompany",
				"rprovince","rcity","rdistricts","rlocation","rname","rphone","rdatetime","rsitename","rcompany",
				"departures","departures.signdetail","departures.zptstatuslist","departures.zptevents","departures.classlineevents"});
		
		String param = 	JsonUtil.obj2JsonStr(paramMap);
		Map<String, Object> resultMap = Http2G7Util.post(param, app_secret, app_key, method, url);
		
		return resultMap;
	}

	@Override
	public boolean dep2wmsWS(Waybill waybill) throws Exception {
		String param = "{\"carrierCode\":\"anxun\",\"carrierName\":\"安迅\",\"stowageNumber\":\"tms201609131153011865\",\"wh\":\"51\","
				+ "\"details\":[{\"obdNumber\":\"OBSO16082700131\"}]}";
		Map<String, Object> orderMap =new HashMap<>();
		orderMap.put("carrierCode", "");
		
		Map<String, Object> map =new HashMap<>();
		map.put("orderInfo", JsonUtil.obj2JsonStr(orderMap));
		LOGGER.info("调用wms平台接口, 参数：" + orderMap);
		Map<String, Object> resultMap = HttpClientUtils.getResult(map, wmsPath, "", "post");
		LOGGER.info("调用仓配平台接口, 返回结果：" + resultMap);
		
		return false;
	}

}
