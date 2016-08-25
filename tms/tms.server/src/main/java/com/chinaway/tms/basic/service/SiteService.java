package com.chinaway.tms.basic.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.core.BaseService;

public interface SiteService extends BaseService<Site, Integer> {

	/**
	 * 根据条件查询所有站点
	 * @param argsMap
	 * @return
	 */
	List<Site> selectAllSiteByCtn(Map<String, Object> argsMap);

	/**
	 * 根据id查询站点列表
	 * @param resultMap
	 * @return
	 */
	List<Site> selectByIds(String ids);

}
