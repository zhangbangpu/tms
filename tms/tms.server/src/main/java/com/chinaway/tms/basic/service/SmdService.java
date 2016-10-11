package com.chinaway.tms.basic.service;

import com.chinaway.tms.basic.model.Smd;
import com.chinaway.tms.core.BaseService;

/**
 * 门店service
 * @author shu
 *
 */
public interface SmdService extends BaseService<Smd, Integer> {

	/**
	 * 查询最新的更新时间
	 * @return
	 */
	String selectMaxUpdateTime();
	
}
