package com.chinaway.tms.basic.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.core.BaseService;

public interface OrdersService extends BaseService<Orders, Integer> {

	/**
	 * 查询最大id
	 * @return
	 */
	int selectMaxId();

	/**
	 * 查询所有订单根据条件
	 * @param argsMap
	 * @return
	 */
	List<Orders> selectAllOrdersByCtn(Map<String, Object> argsMap);
	
	/**
	 * 根据订单id查询运单
	 * @param map
	 * @return
	 */
	public Integer queryWlcompanyByOrderId(Map<String, Object> map);
	
	/**
	 * 根据订单id查询运单列表
	 * @param map
	 * @return
	 */
	public List<Integer> queryWlcompanysByOrderId(Map<String, Object> map);
	
}
