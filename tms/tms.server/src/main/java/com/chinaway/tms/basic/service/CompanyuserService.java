package com.chinaway.tms.basic.service;

import com.chinaway.tms.basic.model.Companyuser;
import com.chinaway.tms.core.BaseService;

public interface CompanyuserService extends BaseService<Companyuser, Integer> {
	
	public int insert(Companyuser companyuser);
}
