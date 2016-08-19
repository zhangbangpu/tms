package com.chinaway.tms.basic.service;

import java.util.List;
import java.util.Map;
import com.chinaway.tms.basic.model.Car;
import com.chinaway.tms.core.BaseService;

public interface CarService extends BaseService<Car, Integer> {

	/**
	 * 查询所有车辆
	 * 
	 * @param argsMap
	 * @return
	 */
	List<Car> selectAllCarByCtn(Map<String, Object> argsMap);

	/**
	 * 根据车辆体积和重量查出车型
	 * 
	 * @param argsMap
	 * @return
	 */
	Car queryVelicleModelByCarInfo(Map<String, Object> argsMap);
	
	/**
	 * 定时任务调用，添加车辆信息,以及车型信息（保护承运商id）
	 * @param car
	 * @return
	 */
	public int insertCar(List<Car> car);

}
