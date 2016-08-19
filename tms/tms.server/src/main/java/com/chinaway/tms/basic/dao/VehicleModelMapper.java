package com.chinaway.tms.basic.dao;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.core.BaseMapper;

public interface VehicleModelMapper extends BaseMapper<VehicleModel, Integer> {

	/**
	 * 查询车辆装载
	 * @param argsMap
	 * @return
	 */
	List<VehicleModel> selectAllVehicleModelByCtn(Map<String, Object> argsMap);
	
}