package com.chinaway.tms.basic.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.controller.LoginController;
import com.chinaway.tms.basic.model.Car;
import com.chinaway.tms.basic.service.CarService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/car")
public class CarManagerController {
	
	@Autowired
	private CarService carService;
	
	/**
	 * 根据条件查询所有车辆信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/selectAllCarByCtn")
	@ResponseBody
	public Result selectAllCarByCtn(HttpServletRequest request) {
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "查询所有车辆操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
//		int ret = 0;
//		try {
			List<Car> cartList = carService.selectAllCarByCtn(argsMap);
//			if(null != cartList){
//				ret = cartList.size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询所有车辆操作成功!";
//				resultMap.put("cartList", cartList);
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, cartList);
	}
	
	/**
	 * 根据条件查询所有车辆信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryVelicleModelByCarInfo")
	@ResponseBody
	public Result queryCarTypeByCarInfo(HttpServletRequest request) {
		Map<String, Object> argsMap = new HashMap<String, Object>();
		Car car = carService.queryVelicleModelByCarInfo(argsMap);

		return new Result(0, car);
	}
	
	/**
	 * 根据条件查询车辆信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result selectCar2PageBean(HttpServletRequest request) {
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<Car> pageBean = carService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 根据条件查询单个车辆信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryOneById")
	@ResponseBody
	public Result queryOneById(HttpServletRequest request) {
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		String id = String.valueOf(argsMap.get("id"));
//		int code = 1;
//		String msg = "根据id查询部门操作失败!";

		Car car = null;
//		try {
			car = carService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != car) {
//				code = 0;
//				msg = "根据id查询车辆操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

//		Result result = new Result(code, car, msg);

		return new Result(0, car);
	}
	
	/**
	 * 添加车辆信息<br>
	 * 返回车辆的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addCar")
	@ResponseBody
	public Result addCar(HttpServletRequest request, Car car) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作车辆失败!";

		int ret = 0;
		try {
			
			if (car.getId() != null) {
				ret = carService.updateSelective(car);
			}else{
				ret = carService.insert(car);
			}
			if (ret > 0) {
				code = 0;
				msg = "操作车辆成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, ret);
	}
	
	/**
	 * 添加车辆信息<br>
	 * 返回车辆的json串
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addCarFromCommonCarrier")
	@ResponseBody
	public Result addCarFromCommonCarrier(HttpServletRequest request, Car car) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作车辆失败!";

		int ret = 0;
		try {
			List<Car> carList = new ArrayList<Car>();
			carList.add(car);

			ret = carService.insertCar(carList);
			if (ret > 0) {
				code = 0;
				msg = "操作车辆成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		// Result result = new Result(code, resultMap, msg);

		return new Result(0, ret);
	}
	
	
	/**
	 * 删除部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/deleteById")
	@ResponseBody
	public Result deleteById(HttpServletRequest request, @RequestParam("ids") String ids) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "批量删除操作失败!";

		int ret = 0;
		try {
			ret = carService.deleteById(ids);

			if (ret > 0) {
				code = 0;
				msg = "批量删除操作成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, code);
	}
	
	/**
	 * 修改车辆信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateCar")
	@ResponseBody
	public Result updateCar(HttpServletRequest request, Car car) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改车辆失败!";

		int ret = 0;
		try {
			ret = carService.update(car);

			if (ret > 0) {
				code = 0;
				msg = "修改车辆成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, code);
	}
	
}