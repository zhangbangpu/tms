package com.chinaway.tms.basic.dao;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.core.BaseMapper;

public interface CpmdMapper extends BaseMapper<Cpmd, Integer> {

	/**
	 * 查询所有商品信息
	 * @param argsMap
	 * @return
	 */
	List<Cpmd> selectAllCpmdByCtn(Map<String, Object> argsMap);
	
}