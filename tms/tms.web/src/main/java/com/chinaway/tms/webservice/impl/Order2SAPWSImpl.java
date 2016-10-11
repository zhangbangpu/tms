package com.chinaway.tms.webservice.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.lang.DateUtil;
import com.chinaway.tms.utils.lang.MathUtil;
import com.chinaway.tms.webservice.Order2SAPWS;

public class Order2SAPWSImpl implements Order2SAPWS{

	private static final Logger LOGGER = LoggerFactory.getLogger(Order2SAPWSImpl.class);
	
	@Autowired
	private OrdersService ordersService ;
	
	@Override
	public String addOrder(String orderInfo) {
		LOGGER.info("传入的参数(orderInfo):" + orderInfo);
		String mtype = "E";
		String msg = "新增失败";
		String contents = "";
		if(orderInfo != ""){
			Map<String, Object> map = JsonUtil.jsonStr2Map(orderInfo);
			contents = map.get("CONTENT").toString();
			List<Map<String, Object>> list =  (List<Map<String, Object>>) map.get("ITABLE");
			Map<String, Object> orderMap = list.get(0);
			Orders order = new Orders();
			order.setFromcode(orderMap.get("FROMCODE").toString());
			order.setOrderfrom(orderMap.get("ORDERFROM").toString());
			order.setCreatetime(DateUtil.strToDate(orderMap.get("CREATETIME").toString(),"yyyyMMdd"));
			order.setDeptname(orderMap.get("DEPTNAME").toString());
			order.setAmount(Double.parseDouble(orderMap.get("AMOUNT").toString()));
			order.setWeight(Double.parseDouble(orderMap.get("WEIGHT").toString()));
			order.setVolume(Double.parseDouble(orderMap.get("VOLUME").toString()));
			order.setFhaddress(orderMap.get("FHADDRESS").toString());
			order.setShaddress(orderMap.get("SHADDRESS").toString());
			order.setRequstarttime(DateUtil.strToDate(orderMap.get("REQUSTARTTIME").toString(),"yyyyMMdd"));
			order.setRequendtime(DateUtil.strToDate(orderMap.get("REQUENDTIME").toString(),"yyyyMMdd"));
//			MyBeanUtil.transMap2Bean(orderMap, order);
			
			List<Map<String, Object>> goodsList = (List<Map<String, Object>>) orderMap.get("ITEM");
			//检查goods参数
			if(goodsList != null){
				if(goodsList.size()>0){
					try{
		//				int maxId = ordersService.selectMaxId();
		//				order.setCode("tms" + maxId );
						//设置有标识的主键
						order.setCode("tms" + MathUtil.random() );
						order.setState("0");
						if("wms".equalsIgnoreCase(order.getOrderfrom())){
							order.setStatus("0");
						}else{
							order.setStatus("1");
						}
						
						int count = ordersService.insertOrder(order, goodsList);
						if(count > 0){
							mtype = "S";
							msg = "新增订单成功";
						}
						
					}catch(Exception e){
						e.printStackTrace();
						msg = "新增订单出异常";
					}
					
				}else{
					msg = "goods参数明细不能为空";
				}
			}else{
				msg = "未传入goods参数";
			}
		}else{
			msg = "未传入orderInfo参数";
		}
		
		
		List<Map<String, Object>> tableList = new ArrayList<>();
		Map<String, Object> tableMap = new HashMap<>();
		tableMap.put("MTYPE", mtype);
		tableMap.put("MSSAG", msg);
		tableList.add(tableMap);
		
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("CONTENT", contents);
		returnMap.put("ITABLE", tableList);
		
		LOGGER.info("addOrder传出的参数:" + returnMap);
		return JsonUtil.obj2JsonStr(returnMap);
	}

}
