package com.chinaway.tms.basic.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.CpmdService;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.basic.service.VehicleModelService;
import com.chinaway.tms.basic.service.WaybillService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.lang.BigDecimalUtil;
import com.chinaway.tms.utils.lang.MathUtil;
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
	private CpmdService cpmdService;
	
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
	 * 手动新增运单
	 * @param request
	 * @param waybill
	 * @return
	 */
	@RequestMapping(value = "/addTckNum")
	@ResponseBody
	public Result addTckNum(HttpServletRequest request, Waybill waybill) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "生成运单失败!";

		int ret = 0;
		try {
			
			if (waybill.getId() != null) {
				ret = waybillService.updateSelective(waybill);
			}else{
				if(null != waybill.getOrdersid()){
					List<Orders> ordersList = ordersService.selectByIds(waybill.getOrdersid());
					Orders ordersNew = new Orders();
					double totalVolume = 0;
					double totalWeight = 0;
					for(Orders orders: ordersList){
						//更新订单，主要是承运商
						orders.setSubcontractor(waybill.getWlcompany());
						ordersService.updateSelective(orders);
						//计算总重量和总体积
						totalVolume = BigDecimalUtil.add(totalVolume+"", orders.getVolume().toString()).doubleValue();
						totalWeight = BigDecimalUtil.add(totalWeight+"", orders.getWeight().toString()).doubleValue();
						ordersNew = orders;
					}
					waybill.setVolume(totalVolume);
					waybill.setWeight(totalWeight);
					
//					Map<String, Object> map = new HashMap<String,Object>();
//					map.put("wlcompany", waybill.getWlcompany());
//					map.put("name", waybill.getVehiclemodel());
//					List<VehicleModel> vehicleModelList = vehicleModelService.selectAllVehicleModelByCtn(map);
//					if(null != vehicleModelList && vehicleModelList.size() > 0){
//						//不科学的地方，给waybill 赋值
//						waybill = ordersService.setWaybill(ordersNew, vehicleModelList.get(0));
//					}
					String name = waybill.getVehiclemodel();//之前是文本框，现在改为下拉框，参数名未改
					VehicleModel v = vehicleModelService.selectById(Integer.parseInt(name));
					//不科学的地方，给waybill 赋值
					waybill = ordersService.setWaybill(ordersNew, v);
					waybill.setState("1");
					ret = waybillService.insertWaybill(waybill, ordersList);

				}
			}
			if (ret > 0) {
				code = 0;
				msg = "生成运单成功!";
			}
			//
			
		} catch (Exception e) {
			e.getStackTrace();
			msg = "生成运单出现异常!";
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return result;
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
	 * 审核运单信息<br>
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
		String msg = "审核运单失败!";

		int ret = 0;
		try {
			ret = waybillService.update(waybill);

			if (ret > 0) {
				code = 0;
				msg = "审核运单成功!";
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
	 * 审核运单信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/queryWaybillDetail")
	@ResponseBody
	public Result queryWaybillDetail(HttpServletRequest request) {
		
		int code = 1;
		String msg = "查看运单明细失败!";
		
		Waybill waybill = null;
		try {
			String id = request.getParameter("id");
			waybill = waybillService.selectById(Integer.parseInt(id));
			List<Orders> orderList = ordersService.selectByWayId(Integer.parseInt(id));
			waybill.setOrdersList(orderList);
			List<Cpmd> cpmdsList = new ArrayList<>();
			for (Orders orders : orderList) {
				List<Cpmd> cpmds = cpmdService.selectCpmdByOrdersId(orders.getId());
				cpmdsList.addAll(cpmds);
			}
			waybill.setGoodsList(cpmdsList);
			
			if (waybill != null) {
				code = 0;
				msg = "查看运单明细成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
			msg = "出现异常";
		}
		
		Result result = new Result(code, waybill, msg);
		
		return result;
	}
	
}