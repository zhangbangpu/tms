package com.chinaway.tms.basic.dao;

import com.chinaway.tms.basic.model.Smd;
import com.chinaway.tms.core.BaseMapper;

public interface SmdMapper extends BaseMapper<Smd, Integer> {

	/**
	 * 查询最新的更新时间
	 * @return
	 */
	String selectMaxUpdateTime();
	
}