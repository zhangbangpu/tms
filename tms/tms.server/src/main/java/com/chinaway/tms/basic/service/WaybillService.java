package com.chinaway.tms.basic.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.core.BaseService;

public interface WaybillService extends BaseService<Waybill, Integer> {

	List<Waybill> selectAllTckNumByCtn(Map<String, Object> argsMap);

	List<Waybill> selectAllTckNumRvwedByCtn(Map<String, Object> argsMap);
	
}
