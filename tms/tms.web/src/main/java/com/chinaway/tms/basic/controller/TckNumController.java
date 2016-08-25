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
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.basic.service.VehicleModelService;
import com.chinaway.tms.basic.service.WaybillService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/tckNum")
public class TckNumController {
	
	@Autowired
	private WaybillService waybillService;
	
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private VehicleModelService vehicleModelService;
	
	/**
	 * 根据条件查询所有站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/selectAllTckNumByCtn")
	@ResponseBody
	public Result selectAllTckNumByCtn(HttpServletRequest request) {
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "查询所有站点操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
//		int ret = 0;
//		try {
			List<Waybill> tckNumtList = waybillService.selectAllTckNumByCtn(argsMap);
//			if(null != tckNumtList){
//				ret = tckNumtList.size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询所有站点操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, tckNumtList);
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
	public Result selectTckNum2PageBean(HttpServletRequest request) {
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<Waybill> pageBean = waybillService.select2PageBean(argsMap);
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

		Waybill waybill = null;
//		try {
			waybill = waybillService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != tckNum) {
//				code = 0;
//				msg = "根据id查询站点操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		Result result = new Result(code, tckNum, msg);

		return new Result(0, waybill);
	}
	
	/**
	 * 添加站点信息<br>
	 * 返回站点的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addTckNum")
	@ResponseBody
	public Result addTckNum(HttpServletRequest request, Waybill waybill) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作站点失败!";

		int ret = 0;
		try {
			
			if (waybill.getId() != null) {
				ret = waybillService.updateSelective(waybill);
			}else{
				if(null != waybill.getOrdersid()){
					Orders orders = ordersService.selectById(waybill.getOrdersid());
					Map<String, Object> map = new HashMap<String,Object>();
					map.put("wlcompany", waybill.getWlcompany());
					map.put("name", waybill.getVehiclemodel());
					List<VehicleModel> vehicleModelList = vehicleModelService.selectAllVehicleModelByCtn(map);
					if(null != vehicleModelList && vehicleModelList.size() > 0){
						waybill = ordersService.setWaybill(orders, vehicleModelList.get(0));
					}
					
				}
				ret = waybillService.insertWaybill(waybill);
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
			ret = waybillService.deleteById(ids);

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
	@RequestMapping(value = "/updateTckNum")
	@ResponseBody
	public Result updateTckNum(HttpServletRequest request, Waybill waybill) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改站点失败!";

		int ret = 0;
		try {
			ret = waybillService.update(waybill);

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

		return new Result(0, ret);
	}
	
}