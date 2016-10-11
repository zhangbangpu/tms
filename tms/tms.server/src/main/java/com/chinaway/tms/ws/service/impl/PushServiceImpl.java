package com.chinaway.tms.ws.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.util.Http2G7Util;
import com.chinaway.tms.utils.io.ProperUtil;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.lang.DateUtil;
import com.chinaway.tms.ws.service.PushService;

@Service
public class PushServiceImpl implements PushService{

	private static final Logger LOGGER = LoggerFactory.getLogger(PushServiceImpl.class);
	
	private String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
	private String app_key = "wine1919";
	private String url = ProperUtil.read("wsconfig.properties", "ws.g7");
	
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
		paramMap.put("orgcode", "20016C");
		paramMap.put("fields", "[\"deleted\",\"id\",\"orderno\",\"wmsno\",\"updatetime\",\"begintime\","
				+ "\"scompany\",\"slocation\",\"sprovince\",\"scity\",\"sdistricts\",\"sname\",\"sphone\",\"sdatetime\",\"ssitename\","
				+ "\"rcompany\",\"rlocation\",\"rprovince\",\"rcity\",\"rdistricts\",\"rname\",\"rphone\",\"rdatetime\",\"rsitename\","
				+ "\"currentstatus\",\"currenttranstype\",\"currentdepartures\",\"finishtype\",\"events\"]");
		
		//所有订单,由于有分页存在，每次调用都会拿到一个list,将该list添加到totalList
		List<Orders> totalList = new ArrayList<>();
		while(true){
			String param = 	JsonUtil.obj2JsonStr(paramMap);
			Map<String, Object> resultMap = Http2G7Util.post(param, app_secret, app_key, method, url);
			Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
			List<Orders> list = getOrderList(dataMap);
			totalList.addAll(list);
			
			boolean hasNext = (boolean) dataMap.get("hasNext");
			if (hasNext) {
				int pageNo = Integer.parseInt(paramMap.get("pageNo").toString());
				paramMap.put("pageNo", ++pageNo);
			}else{//没有下一页就停止
				break;
			}
			
		}
		
		return totalList;
	}

	/**
	 * 从返回结果中获得订单
	 * @param resultMap
	 * @return
	 */
	private List<Orders> getOrderList(Map<String, Object> dataMap) {
		List<Orders> list = new ArrayList<>();
//		Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
		if(dataMap != null){
			List<Map<String, Object>> resultList = (List<Map<String, Object>>) dataMap.get("result");
			Orders orders = null;
			for (Map<String, Object> map : resultList) {
				orders = new Orders();
				orders.setCode(map.get("orderno").toString());
				orders.setFromcode(map.get("wmsno").toString());
				orders.setCreatetime(DateUtil.strToDate(map.get("begintime").toString()));
//				orders.setCreatetime(map.get("begintime").toString());
//				orders.setDeptname(deptname);//不会更新
//				orders.setFhaddress(fhaddress);
//				orders.setShaddress(shaddress);
//				orders.setAmount(amount);
//				orders.setVolume(volume);
//				orders.setWeight(weight);
				orders.setExceptcount(Integer.parseInt(map.get("events").toString()));
				orders.setFhsitename(map.get("ssitename").toString());
				orders.setRequstarttime(DateUtil.strToDate(map.get("sdatetime").toString()));
				orders.setRequendtime(DateUtil.strToDate(map.get("rdatetime").toString()));
				orders.setShsitename(map.get("rsitename").toString());
				orders.setUpdatetime(map.get("updatetime").toString());
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
				
				list.add(orders);
			}
		}
		
		return list;
	}

	@Override
	public List<Waybill> selectAllDeparture(Map<String, Object> paramMap) throws Exception {
		String method = "order.departure.getUpdateDepartureList";
		paramMap.put("orgcode", "20016C");
		paramMap.put("fields", "[\"departureno\",\"begintime\",\"updatetimetime\",\"carnum\",\"carriagetype\",\"drivername\",\"driverphone\","
    			+ "\"starttime\",\"endtime\",\"garrivetime\",\"gstarttime\",\"type\",\"status\"]");
		
		//所有订单,由于有分页存在，每次调用都会拿到一个list,将该list添加到totalList
		List<Waybill> totalList = new ArrayList<>();
		while(true){
			String param = 	JsonUtil.obj2JsonStr(paramMap);
			Map<String, Object> resultMap = Http2G7Util.post(param, app_secret, app_key, method, url);
			List<Waybill> list = getDepList(resultMap);
			totalList.addAll(list);
			
			boolean hasNext = (boolean) resultMap.get("hasNext");
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
				waybill.setUpdatetime(DateUtil.strToDate(map.get("updatetime").toString()));
				waybill.setRequstarttime(DateUtil.strToDate(map.get("requstarttime").toString()));
				waybill.setRequendtime(DateUtil.strToDate(map.get("requendtime").toString()));
				waybill.setGstarttime(DateUtil.strToDate(map.get("gstarttime").toString()));
				waybill.setGarrivetime(DateUtil.strToDate(map.get("garrivetime").toString()));
				waybill.setState(map.get("status").toString());
				waybill.setCarnum(map.get("carnum").toString());
				waybill.setCarriagetype(map.get("carriagetype").toString());
				waybill.setDrivername(map.get("drivername").toString());
				waybill.setDriverphone(map.get("driverphone").toString());
				
				list.add(waybill);
			}
			
		}
		return list;
	}

}
