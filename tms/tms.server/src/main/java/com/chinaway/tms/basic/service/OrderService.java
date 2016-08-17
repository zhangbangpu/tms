package com.chinaway.tms.basic.service;

import com.chinaway.tms.basic.model.Order;
import com.chinaway.tms.core.BaseService;

public interface OrderService extends BaseService<Order, Integer> {

	/**
	 * 查询最大id
	 * @return
	 */
	int selectMaxId();
	
}
