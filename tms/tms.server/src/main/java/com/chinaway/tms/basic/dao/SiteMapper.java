package com.chinaway.tms.basic.dao;

import java.util.List;
import java.util.Map;
import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

public interface SiteMapper extends BaseMapper<Site, Integer> {
	/**
	 * 根据条件分页查询站点
	 * @param argsMap
	 * @return
	 */
	PageBean<Site> selectSite2PageBean(Map<String, Object> argsMap);

	/**
	 * 根据条件查询所有站点
	 * @param argsMap
	 * @return
	 */
	List<Site> selectAllSiteByCtn(Map<String, Object> argsMap);

	
}