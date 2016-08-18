package com.chinaway.tms.basic.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.core.BaseService;

public interface CpmdService extends BaseService<Cpmd, Integer> {

	List<Cpmd> selectAllCpmdByCtn(Map<String, Object> argsMap);
	
}
