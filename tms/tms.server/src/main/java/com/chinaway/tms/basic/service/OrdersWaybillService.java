package com.chinaway.tms.basic.service;

import java.util.Map;
import com.chinaway.tms.basic.model.OrdersWaybill;
import com.chinaway.tms.core.BaseService;

public interface OrdersWaybillService extends BaseService<OrdersWaybill, Integer> {
	
	/**
	 * 按条件删除
	 * @param argsmap
	 * @return
	 */
	public int deleteByCtn(Map<String, Object> argsmap);
}
