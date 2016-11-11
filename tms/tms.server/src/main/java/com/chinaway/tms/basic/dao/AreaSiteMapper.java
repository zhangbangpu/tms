package com.chinaway.tms.basic.dao;

import com.chinaway.tms.basic.model.AreaSite;
import com.chinaway.tms.core.BaseMapper;

public interface AreaSiteMapper extends BaseMapper<AreaSite, Integer> {
	
	int deleteByAreaCode(String areaCode);
	
}