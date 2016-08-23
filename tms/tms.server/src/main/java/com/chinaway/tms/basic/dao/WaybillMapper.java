package com.chinaway.tms.basic.dao;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.core.BaseMapper;

public interface WaybillMapper extends BaseMapper<Waybill, Integer> {

	/**
	 * 按条件查询所有车次
	 * @param argsMap
	 * @return
	 */
	List<Waybill> selectAllTckNumByCtn(Map<String, Object> argsMap);

	/**
	 * 按条件查询所有审核
	 * @param argsMap
	 * @return
	 */
	List<Waybill> selectAllTckNumRvwedByCtn(Map<String, Object> argsMap);
	
}