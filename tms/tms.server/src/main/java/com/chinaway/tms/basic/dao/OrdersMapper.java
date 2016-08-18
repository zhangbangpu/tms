package com.chinaway.tms.basic.dao;

import com.chinaway.tms.basic.model.Order;
import com.chinaway.tms.core.BaseMapper;

public interface OrderMapper extends BaseMapper<Order, Integer> {

	/**
	 * 查询最大id
	 * @return
	 */
	int selectMaxId();
	
}