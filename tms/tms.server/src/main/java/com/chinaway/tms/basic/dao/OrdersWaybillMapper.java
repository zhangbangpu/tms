package com.chinaway.tms.basic.dao;

import java.util.Map;

import com.chinaway.tms.basic.model.OrdersWaybill;
import com.chinaway.tms.core.BaseMapper;

public interface OrdersWaybillMapper extends BaseMapper<OrdersWaybill, Integer> {

	/**
	 * 按条件删除运单
	 * @param argsmap
	 * @return
	 */
	int deleteByCtn(Map<String, Object> argsmap);
	
}