package com.chinaway.tms.basic.dao;

import java.util.List;
import java.util.Map;
import com.chinaway.tms.basic.model.Car;
import com.chinaway.tms.core.BaseMapper;

public interface CarMapper extends BaseMapper<Car, Integer> {

	/**
	 * 根据条件查询所有车辆不分页
	 * @param argsMap
	 * @return
	 */
	List<Car> selectAllCarByCtn(Map<String, Object> argsMap);
	
}