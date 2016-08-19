package com.chinaway.tms.basic.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chinaway.tms.basic.dao.CarMapper;
import com.chinaway.tms.basic.dao.VehicleModelMapper;
import com.chinaway.tms.basic.model.Car;
import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.basic.service.CarService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class CarServiceImpl extends AbstractService<Car, Integer>implements CarService {

	@Autowired
	private CarMapper carMapper;

	@Autowired
	private VehicleModelMapper vehicleModelMapper;

	/** 具体子类service的实现需要使用的mapper */
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<Car, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<Car> select2PageBean(Map<String, Object> map) {
		PageBean<Car> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		// 注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);// 是否分页，默认是false不分页
		pageBean.setResult(carMapper.selectAll4Page(map));
		return pageBean;
	}

	@Override
	public Car queryVelicleModelByCarInfo(Map<String, Object> argsMap) {
		List<Car> carList = carMapper.selectAllCarByCtn(argsMap);

		if (null != carList && carList.size() > 0) {
			return carList.get(0);
		}
		return null;
	}

	@Override
	public List<Car> selectAllCarByCtn(Map<String, Object> argsMap) {
		argsMap.put("needPage", false);
		return carMapper.selectAll4Page(argsMap);
	}

	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				carMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int insertCar(List<Car> carList) {
		int code = -1;
		for (Car car : carList) {
			carMapper.insert(car);
			Map<String, Object> argsMap = new HashMap<String, Object>();
			argsMap.put("volum", car.getVolum());
			argsMap.put("weight", car.getWeight());

			List<VehicleModel> vehicleModelList = vehicleModelMapper.selectAllVehicleModelByCtn(argsMap);
			if (null == vehicleModelList || vehicleModelList.size() <= 0) {
				VehicleModel vehicleModel = new VehicleModel();
				vehicleModel.setVolum(car.getVolum());
				vehicleModel.setWeight(car.getWeight());
				vehicleModel.setWlcompany(car.getWlcompany());
				// TODO 车系名称+年代款+排量+变速箱类型/驱动方式+型号名称
				// vehicleModel.setName(car.getName() + car.getCode());
				vehicleModel.setName(car.getVehicleModelName());
				vehicleModelMapper.insert(vehicleModel);
				code = 0;
			}
		}

		return code;
	}

}