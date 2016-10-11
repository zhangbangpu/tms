package com.chinaway.tms.basic.dao;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.OrderItem;
import com.chinaway.tms.core.BaseMapper;

public interface OrderItemMapper extends BaseMapper<OrderItem, Integer> {

	/**
	 * 查询所有订单详情列表
	 * @param argsMap
	 * @return
	 */
	List<OrderItem> selectAllOrderItemByCtn(Map<String, Object> argsMap);

	/**
	 * 根据orderid查询 订单物品
	 * @param orderid
	 * @return
	 */
	List<OrderItem> selectByOrderId(int orderid);
	
}