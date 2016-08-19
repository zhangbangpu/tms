package com.chinaway.tms.basic.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.core.BaseService;

public interface VehicleModelService extends BaseService<VehicleModel, Integer> {

	/**
	 * 查询车辆装载列表
	 * @param argsMap
	 * @return
	 */
	List<VehicleModel> selectAllVehicleModelByCtn(Map<String, Object> argsMap);
	
}
