package com.chinaway.tms.basic.dao;

import java.util.List;
import java.util.Map;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.core.BaseMapper;

public interface OrdersMapper extends BaseMapper<Orders, Integer> {

	/**
	 * 查询最大id
	 * @return
	 */
	int selectMaxId();

	/**
	 * 查询所有订单根据条件
	 * @param map
	 * @return
	 */
	List<Orders> selectAllOrdersByCtn(Map<String, Object> map);
	
}