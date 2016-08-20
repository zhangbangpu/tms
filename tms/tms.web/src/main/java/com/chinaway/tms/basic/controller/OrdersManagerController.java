package com.chinaway.tms.basic.controller;

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
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/orders")
public class OrdersManagerController {
	
	@Autowired
	private OrdersService ordersService;
	
	/**
	 * 根据条件查询所有订单信息<br>
	 * 返回用户的json串
	 * 
	 * @param ordersInfo
	 * @return
	 */
	@RequestMapping(value = "/selectAllOrdersByCtn")
	@ResponseBody
	public Result selectAllOrdersByCtn(HttpServletRequest request) {
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "查询所有订单操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
//		int ret = 0;
//		try {
			List<Orders> ordersList = ordersService.selectAllOrdersByCtn(argsMap);
//			if(null != ordersList){
//				ret = ordersList.size();
//			}
			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询所有订单操作成功!";
//				resultMap.put("ordersList", ordersList);
//			}
//			
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//		
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);
		
//		return JsonUtil.obj2JsonStr(result);
		return new Result(0, ordersList);
	}
	
	/**
	 * 根据条件查询所有订单信息<br>
	 * 返回用户的json串
	 * 
	 * @param ordersInfo
	 * @return
	 */
	@RequestMapping(value = "/queryWlcompanysByOrderId")
	@ResponseBody
	public Result queryWlcompanysByOrderId(HttpServletRequest request) {
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "查询运单列表操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
//		int ret = 0;
//		try {
			List<Integer> wlcompanyIdList = ordersService.queryWlcompanysByOrderId(argsMap);
//			if(null != wlcompanyIdList){
//				ret = wlcompanyIdList.size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询运单列表操作成功!";
//				resultMap.put("wlcompanyIdList", wlcompanyIdList);
//			}
//			
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
		
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);
		
//		return JsonUtil.obj2JsonStr(result);
		return new Result(0, wlcompanyIdList);
	}
	
	/**
	 * 根据条件查询订单信息<br>
	 * 返回用户的json串
	 * 
	 * @param ordersInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result selectOrders2PageBean(HttpServletRequest request) {
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<Orders> pageBean = ordersService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 根据条件查询单个订单信息<br>
	 * 返回用户的json串
	 * 
	 * @param ordersInfo
	 * @return
	 */
	@RequestMapping(value = "/queryOneById")
	@ResponseBody
	public Result queryOneById(HttpServletRequest request) {
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		String id = String.valueOf(argsMap.get("id"));
//		int code = 1;
//		String msg = "根据id查询部门操作失败!";
//
		Orders orders = null;
//		try {
			orders = ordersService.selectById(id == "" ? 0 : Integer.parseInt(id));
//
//			if (null != orders) {
//				code = 0;
//				msg = "根据id查询订单操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

//		Result result = new Result(code, orders, msg);

//		return result;
		return new Result(0, orders);
	}
	
	/**
	 * 添加订单信息<br>
	 * 返回订单的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/generateWaybill")
	@ResponseBody
	public Result generateWaybill(HttpServletRequest request) {
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		int code = 1;
		String msg = "生成运单失败!";

		int ret = 0;
		try {
			ret = ordersService.generateWaybill(argsMap);
			if (ret > 0) {
				code = 0;
				msg = "生成运单成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return new Result(0, ret);
	}
	
	/**
	 * 添加订单信息<br>
	 * 返回订单的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addOrders")
	@ResponseBody
	public Result addOrders(HttpServletRequest request, Orders orders) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作订单失败!";

		int ret = 0;
		try {
			
			if (orders.getId() != null) {
				ret = ordersService.updateSelective(orders);
			}else{
				orders.setCreatetime(new Date());
				ret = ordersService.insert(orders);
			}
			if (ret > 0) {
				code = 0;
				msg = "操作订单成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

//		return result;
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
			ret = ordersService.deleteById(ids);

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

		return new Result(0, ret);
	}
	
	/**
	 * 修改订单信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateOrders")
	@ResponseBody
	public Result updateOrders(HttpServletRequest request, Orders orders) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改订单失败!";

		int ret = 0;
		try {
			ret = ordersService.update(orders);

			if (ret > 0) {
				code = 0;
				msg = "修改订单成功!";
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