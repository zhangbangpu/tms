package com.chinaway.tms.basic.service;

import com.chinaway.tms.basic.model.Warehouse;
import com.chinaway.tms.core.BaseService;

/**
 * 仓库service
 * @author shu
 *
 */
public interface WarehouseService extends BaseService<Warehouse, Integer> {
	
	/**
	 * 查询最新的更新时间
	 * @return
	 */
	String selectMaxUpdateTime();
}
