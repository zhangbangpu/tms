package com.chinaway.tms.basic.service;

import java.util.List;
import java.util.Map;
import com.chinaway.tms.basic.model.OrderItem;
import com.chinaway.tms.core.BaseService;

public interface OrderItemService extends BaseService<OrderItem, Integer> {

	/**
	 * 查询所有订单详情列表
	 * @param argsMap
	 * @return
	 */
	List<OrderItem> selectAllOrderItemByCtn(Map<String, Object> argsMap);
	
}
