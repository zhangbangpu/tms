package com.chinaway.tms.basic.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.controller.LoginController;
import com.chinaway.tms.admin.model.SysDept;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysDeptService;
import com.chinaway.tms.basic.model.OrderItem;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.service.CpmdService;
import com.chinaway.tms.basic.service.OrderItemService;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.lang.StringUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;
import com.chinaway.tms.ws.service.PushService;

@Controller
@RequestMapping(value = "/orders")
public class OrdersManagerController extends BaseController {
	
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private PushService pushService;
	
	@Autowired
	private CpmdService cpmdService;
	
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
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "查询所有订单操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
		int ret = 0;
		try {
			List<Orders> ordersList = ordersService.selectAllOrdersByCtn(argsMap);
			if(null != ordersList){
				ret = ordersList.size();
			}
			
			if (ret > 0) {
				code = 0;
				msg = "查询所有订单操作成功!";
				resultMap.put("ordersList", ordersList);
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
	 * 根据条件查询所有订单信息<br>
	 * 返回用户的json串
	 * 
	 * @param ordersInfo
	 * @return
	 */
	@RequestMapping(value = "/queryStatusById")
	@ResponseBody
	public Result queryStatusById(HttpServletRequest request, @RequestParam("id") String ids) {
		//argsMap.put("state", "0"); //  初始状态
		//argsMap.put("status", "1"); // 手动状态
		List<Orders> retOrdersList = new ArrayList<Orders>();
		int code = 0;
		try {
			if(null != ids){
				List<Orders> ordersList = ordersService.selectByIds(ids);
				for(Orders orders : ordersList){
					if(!"0".equals(orders.getState()) || !"1".equals(orders.getStatus())){
						retOrdersList.add(orders);
					}
				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			code = 1;
		}

//		Result result = new Result(0, retOrdersList, code);
		return new Result(code, retOrdersList);
//		return new Result(0, code);
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
		int code = 1;
		String msg = "查询订单操作失败!";

		PageBean<Orders> pageBean = null;
		try {
			Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
			if(StringUtils.isEmpty(argsMap.get("code").toString()) && StringUtils.isEmpty(argsMap.get("fromcode").toString())){
				Set<String> deptidSet = super.getUserDepts(request);
				//不同角色看到的订单不同，通过deptname来筛选
				argsMap.put("deptids",deptidSet);
			}
			
			pageBean = ordersService.select2PageBean(argsMap);
			code = 0;
			msg = "查询订单操作成功!";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "查询订单出现异常!";
		}
		return new Result(code, pageBean, msg);
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
//		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		String id = String.valueOf(argsMap.get("id"));
		String id = request.getParameter("id");
		int code = 1;
		String msg = "根据id查询订单操作失败!";
//
		Orders orders = null;
		try {
			orders = ordersService.selectById(id == "" ? 0 : Integer.parseInt(id));

			if (null != orders) {
				code = 0;
				msg = "根据id查询订单操作成功!";
			}

		} catch (Exception e) {
			e.getStackTrace();
			msg = "根据id查询出现异常!";
		}

		Result result = new Result(code, orders, msg);

		return result;
	}
	
	/**
	 * 根据条件查询单个订单信息<br>
	 * 返回用户的json串
	 * 
	 * @param ordersInfo
	 * @return
	 */
	@RequestMapping(value = "/queryOrderAndItemById")
	@ResponseBody
	public Result queryOrderAndItemById(HttpServletRequest request) {
//		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		String id = String.valueOf(argsMap.get("id"));
		String id = request.getParameter("id");
		int code = 1;
		String msg = "根据id查询订单操作失败!";
//
		Orders orders = null;
		try {
			orders = ordersService.selectById(id == "" ? 0 : Integer.parseInt(id));
			if("0".equals(orders.getState())){
				List<OrderItem> orderItemList = orderItemService.selectByOrderId(orders.getId());
				orders.setOrderItemList(orderItemList);
			}
			
			if (null != orders) {
				code = 0;
				msg = "根据id查询订单操作成功!";
			}
			
		} catch (Exception e) {
			e.getStackTrace();
			msg = "根据id查询出现异常!";
		}
		
		Result result = new Result(code, orders, msg);
		
		return result;
	}
	
	/**
	 * 根据条件查询订单详情<br>
	 * 此处不通过数据库查，而是通过查接口
	 * 返回用户的json串
	 * 
	 * @param ordersInfo
	 * @return
	 */
	@RequestMapping(value = "/queryOrdersDetail")
	@ResponseBody
	public Result queryOrdersDetail(HttpServletRequest request) {
//		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		String id = String.valueOf(argsMap.get("id"));
		String id = request.getParameter("id");
		int code = 1;
		String msg = "根据id查询订单详情!";

		Orders orders = null;
		Map<String, Object> returnMap = new HashMap<>();
		try {
			orders = ordersService.selectDetailById(id == "" ? 0 : Integer.parseInt(id));
			
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("orderno", orders.getCode());
			Map<String, Object> resultMap = pushService.selectOrderDetail(paramMap);
			Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
			List<Map<String, Object>> departuresList = new ArrayList<>();
			if(dataMap != null){
				departuresList = (List<Map<String, Object>>) dataMap.get("departures");
			}
			
			returnMap.put("baseInfo", orders);
			returnMap.put("departures", departuresList);
			
			if (null != orders) {
				code = 0;
				msg = "根据id查询订单详情成功!";
			}

		} catch (Exception e) {
			e.getStackTrace();
			msg = "根据id查询出现异常!";
		}
		
		Result result = new Result(code, returnMap, msg);
		
		return result;
	}
	
	/**
	 * 生成运单<br>
	 * 返回订单的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/generateWaybill")
	@ResponseBody
	public Result generateWaybill(HttpServletRequest request) {
		int code = 1;
		String msg = "生成运单失败!";
		try {
			Integer id = Integer.parseInt(String.valueOf(request.getParameter("id")));
			Orders order = ordersService.selectById(id);
			List<String> waybills = ordersService.generateWaybill(order);
			if (null == waybills || waybills.size() <= 0) {
				code = 0;
				msg = "生成运单成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
		return new Result(0, code);
	}
	
	/**
	 * 上传excel站点信息<br>
	 * 返回站点的json串
	 * @return
	 */
	@RequestMapping(value = "/export")
	@ResponseBody
	public Result export(HttpServletRequest request, @RequestParam("ids") String ids) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "下载订单失败!";

		List<Orders> ordersList = null;
		try {
			ordersList = ordersService.selectByIds(ids);
			
			if (null != ordersList && ordersList.size() > 0) {
				code = 0;
				msg = "下载订单成功!";
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
	 * 删除订单信息<br>
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
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			ret = ordersService.deleteById(ids);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
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
			ret = ordersService.updateSelective(orders);

			if (ret > 0) {
				code = 0;
				msg = "修改订单成功!";
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
	 * 修改为了智能调度的暂停与开启
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/update2AutoDep")
	@ResponseBody
	public Result update2AutoDep(HttpServletRequest request, Orders orders) {

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改订单失败!";
		
		int ret = 0;
		try {
			ret = ordersService.updateSelective(orders);
			
			if (ret > 0) {
				code = 0;
				msg = "修改订单成功!";
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
	 * 智能调度
	 */
	@RequestMapping(value = "/autoGenerateWaybill")
	@ResponseBody
	public Result autoGenerateWaybill(HttpServletRequest request) {
		
		int code = 0;
		String msg = "自动生成运单失败!";
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "0");
			map.put("state", "0");
			//暂时未限制订单的deptname
			Set<String> deptidSet = super.getUserDepts(request);
			//不同角色看到的订单不同，通过deptname来筛选
			map.put("deptids",deptidSet);
			
			//查询所有订单根据条件
			List<Orders> ordersList = ordersService.selectAllOrdersByCtn(map);
			
			
			for (Orders orders : ordersList) {
				Map<String, Object> argsMap = new HashMap<String, Object>();
				argsMap.put("id", orders.getId());
				//生成运单
				List<String> waybills = ordersService.generateWaybill(orders);
				if (null != waybills && waybills.size() > 0) {
//					code = 0;
					msg = "";
				}else{
					msg = "没有匹配的订单生成运单!";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = "出现异常";
		}
		
		resultMap.put("code", code);
		resultMap.put("message", msg);
		
		return new Result(code, resultMap, msg);
	}
	
	/**
	 * 检查订单
	 */
	@RequestMapping(value = "/checkOrders")
	@ResponseBody
	public Result checkOrders(HttpServletRequest request) {
		
		int code = 1;
		String msg = "自动生成运单失败!";
		Map<String, Object> resultMap = new HashMap<>();
		try {
//			Map<String, Object> map = new HashMap<String, Object>();
			String ids = request.getParameter("ids");
			String[] orderIds = ids.split(",");
			List<String> orderCodeList = new ArrayList<>();
			Set<String> typeSet = new HashSet<>();
			for (int i = 0; i < orderIds.length; i++) {
				int id = Integer.parseInt(orderIds[i]);
				Orders orders = ordersService.selectById(id);
				if (!"0".equals(orders.getState())) {
					orderCodeList.add(orders.getCode());
				}
				typeSet.add(orders.getOrderfrom());
			}
			
			if (orderCodeList.size() > 0) {
				msg = orderCodeList.toString() + "不是初始化订单";
			}else{
				if (typeSet.size() > 1) {
					msg = "选择的订单中含有sap、wms2种，请只选择一种";
				}else{
					code = 0;
					msg = "";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = "出现异常";
		}
		
		resultMap.put("code", 0);
		resultMap.put("msg", msg);
		
		return new Result(code, resultMap, msg);//情况特殊 code返回都是0
	}
}