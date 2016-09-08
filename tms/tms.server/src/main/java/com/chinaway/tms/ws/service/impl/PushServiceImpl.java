package com.chinaway.tms.ws.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinaway.tms.util.Http2G7Util;
import com.chinaway.tms.ws.service.PushService;

@Service
public class PushServiceImpl implements PushService{

	private static final Logger LOGGER = LoggerFactory.getLogger(PushServiceImpl.class);
	
	private String app_secret = "fce6dc5652df05b2a7ae287337702eb6";
	private String app_key = "wine1919";
	private String url = "http://test.api.g7s.chinawayltd.com/interface/index.php";
	
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

}
