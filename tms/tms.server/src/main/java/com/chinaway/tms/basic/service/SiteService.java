package com.chinaway.tms.basic.service;

import java.util.List;
import java.util.Map;
import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.core.BaseService;
import com.chinaway.tms.utils.page.PageBean;

public interface SiteService extends BaseService<Site, Integer> {

	/**
	 * 根据条件查询所有站点
	 * @param argsMap
	 * @return
	 */
	List<Site> queAllSiteByCtn(Map<String, Object> argsMap);

	/**
	 * 根据条件分页查询说有站点
	 * @param argsMap
	 * @return
	 */
	PageBean<Site> queSiteByCtnPgBn(Map<String, Object> argsMap);
	
}
