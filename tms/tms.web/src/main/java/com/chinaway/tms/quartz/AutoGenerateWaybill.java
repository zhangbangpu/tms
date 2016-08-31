package com.chinaway.tms.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.service.OrdersService;

/**
 * 定时调用生成运单
 * @author zhang
 *
 */
public class AutoGenerateWaybill {

//	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OrdersService ordersService;
	
	public OrdersService getOrdersService() {
		return ordersService;
	}

	public void setOrdersService(OrdersService ordersService) {
		this.ordersService = ordersService;
	}

	/**
	 * 定时生成运单 默认5分钟
	 */
	public void execute() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "0");
			map.put("state", "0");
			//查询所有订单根据条件
			List<Orders> ordersList = ordersService.selectAllOrdersByCtn(map);
			for (Orders orders : ordersList) {
				Map<String, Object> argsMap = new HashMap<String, Object>();
				argsMap.put("id", orders.getId());
				//生成运单
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
