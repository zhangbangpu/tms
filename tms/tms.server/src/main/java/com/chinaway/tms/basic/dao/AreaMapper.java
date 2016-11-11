package com.chinaway.tms.basic.dao;

import com.chinaway.tms.basic.model.Area;
import com.chinaway.tms.core.BaseMapper;

public interface AreaMapper extends BaseMapper<Area, Integer> {
	
	/**
	 * 通过站点查询区域
	 * @param siteCode
	 * @return
	 */
	Area selectBySiteCode(String siteCode);
}