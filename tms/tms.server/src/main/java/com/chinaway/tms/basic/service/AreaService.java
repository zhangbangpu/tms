package com.chinaway.tms.basic.service;

import com.chinaway.tms.basic.model.Area;
import com.chinaway.tms.core.BaseService;

public interface AreaService extends BaseService<Area, Integer> {

	/**
	 * 通过站点查询区域
	 * @param siteCode
	 * @return
	 */
	Area selectBySiteCode(String siteCode);

	/**
	 * 新增区域和明细
	 * @param area
	 * @return
	 */
	int insertAreaAndItem(Area area);

	/**
	 * 修改区域和明细
	 * @param area
	 * @return
	 */
	int updatetAreaAndItem(Area area);
	
}
