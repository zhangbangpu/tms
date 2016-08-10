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
	List<Site> selectAllSiteByCtn(Map<String, Object> argsMap);

}
