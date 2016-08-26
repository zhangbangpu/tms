package com.chinaway.tms.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.service.OrdersService;

public class AutoGenerateWaybill {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OrdersService ordersService;
	
	public OrdersService getOrdersService() {
		return ordersService;
	}

	public void setOrdersService(OrdersService ordersService) {
		this.ordersService = ordersService;
	}

	public void execute() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "0");
			map.put("state", "0");
			List<Orders> ordersList = ordersService.selectAllOrdersByCtn(map);
			for (Orders orders : ordersList) {
				Map<String, Object> argsMap = new HashMap<String, Object>();
				argsMap.put("id", orders.getId());
				List<String> waybills = ordersService.generateWaybill(orders);
				if (null != waybills && waybills.size() > 0) {
//					logger.debug("orders=" + orders.getId());
				}
			}

		} catch (Exception e) {
//			logger.error(e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
//				logger.error(e.getMessage());
			} finally {
				try {
				} catch (Exception e) {
//					logger.error(e.getMessage());
				}
			}
		}
	}
}
