package com.chinaway.tms.basic.controller;

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
import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.basic.service.CarService;
import com.chinaway.tms.basic.service.VehicleModelService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/vehicleModel")
public class VehicleModelManagerController {
	
	@Autowired
	private VehicleModelService vehicleModelService;
	
	@Autowired
	private CarService carService;
	
	/**
	 * 根据条件查询所有站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/selectAllVehicleModelByCtn")
	@ResponseBody
	public Result selectAllVehicleModelByCtn(HttpServletRequest request) {
		int code = 1;
		String msg = "查询所有车型,操作失败!";
//		Map<String, Object> argsMap = new HashMap<String, Object>();
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		List<VehicleModel> list = null;
		try {
			list = vehicleModelService.selectAllVehicleModelByCtn(argsMap);
			
			if (list.size() > 0) {
				code = 0;
				msg = "查询所有车型,操作成功!";
			}

		} catch (Exception e) {
			e.getStackTrace();
			msg = "查询所有车型,出现异常!";
		}

		return new Result(code, list, msg);
	}
	
	/**
	 * 根据条件查询站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result selectVehicleModel2PageBean(HttpServletRequest request) {
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<VehicleModel> pageBean = vehicleModelService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 根据条件查询单个站点信息<br>
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

		VehicleModel vehicleModel = null;
//		try {
			vehicleModel = vehicleModelService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != vehicleModel) {
//				code = 0;
//				msg = "根据id查询站点操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

//		Result result = new Result(code, vehicleModel, msg);

		return new Result(0, vehicleModel);
	}
	
	/**
	 * 添加站点信息<br>
	 * 返回站点的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addVehicleModel")
	@ResponseBody
	public Result addVehicleModel(HttpServletRequest request, VehicleModel vehicleModel) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作站点失败!";

		int ret = 0;
		try {
			if (vehicleModel.getId() != null) {
				ret = vehicleModelService.updateSelective(vehicleModel);
			}else{
				Map<String, Object> reqMap = new HashMap<>();
				reqMap.put("volum", vehicleModel.getVolum());
				reqMap.put("weight", vehicleModel.getWeight());
				List<Car> carList = carService.selectAllCarByCtn(reqMap);
				
				for (Car car : carList) {
					if (null != car.getWlcompany()) {
						vehicleModel.setWlcompany(car.getWlcompany());
						ret = vehicleModelService.insert(vehicleModel);
					}
				}
			}
			if (ret > 0) {
				code = 0;
				msg = "操作站点成功!";
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
			ret = vehicleModelService.deleteById(ids);

			if (ret > 0) {
				code = 0;
				msg = "批量删除操作成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return result;
	}
	
	/**
	 * 修改站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateVehicleModel")
	@ResponseBody
	public Result updateVehicleModel(HttpServletRequest request, VehicleModel vehicleModel) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改站点失败!";

		int ret = 0;
		try {
			ret = vehicleModelService.update(vehicleModel);

			if (ret > 0) {
				code = 0;
				msg = "修改站点成功!";
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