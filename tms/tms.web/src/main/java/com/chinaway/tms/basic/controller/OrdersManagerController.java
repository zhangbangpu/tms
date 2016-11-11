package com.chinaway.tms.basic.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.chinaway.tms.basic.model.Area;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.VehicleModel;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.AreaService;
import com.chinaway.tms.basic.service.CpmdService;
import com.chinaway.tms.basic.service.OrderItemService;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.basic.service.VehicleModelService;
import com.chinaway.tms.basic.service.WaybillService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.lang.MathUtil;
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
	private AreaService areaService;
	@Autowired
	private VehicleModelService vehicleModelService;
	@Autowired
	private WaybillService waybillService;
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
		int code = 0;
		String msg = "根据id查询订单操作失败!";
//
		Orders orders = null;
		try {
			orders = ordersService.selectById(id == "" ? 0 : Integer.parseInt(id));
			if("0".equals(orders.getState())){
//				List<OrderItem> orderItemList = orderItemService.selectByOrderId(orders.getId());
//				orders.setOrderItemList(orderItemList);
				ordersService.setGoodsByOrderId(orders);
				
				msg = "";
			}
			Map<String, Object> argsMap = new HashMap<String, Object>();
			argsMap.put("pid", orders.getId());
			List<Orders> list = ordersService.selectAll4Page(argsMap);
			for (Orders orders2 : list) {
				ordersService.setGoodsByOrderId(orders2);
			}
			orders.setChildren(list);
			
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
//		Orders orders = null;
		try {
			ret = ordersService.deleteById(ids);
//			String[] idsStr = ids.split(",");
//			if (idsStr.length > 0) {
//				for (String id : idsStr) {
//					int orId = Integer.parseInt(id);
//					orders = ordersService.selectById(orId);
//					if(!"0".equals(orders.getState())){
//						
//					}
//					ordersService.deleteById(orId);
//				}
//			} else {
//			}

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
		String msg = "";
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Map<String, Object> ordMap = new HashMap<String, Object>();
			ordMap.put("status", "0");
			ordMap.put("state", "0");
			
			Set<String> deptidSet = super.getUserDepts(request);
			//不同角色看到的订单不同，通过deptname来筛选
			ordMap.put("deptids",deptidSet);
			
			//查询所有订单根据条件
			List<Orders> ordersList = ordersService.selectAllOrdersByCtn(ordMap);
			//之前简单实现，换成下面的实现
//			for (Orders orders : ordersList) {
//				Map<String, Object> argsMap = new HashMap<String, Object>();
//				argsMap.put("id", orders.getId());
//				//生成运单
//				List<String> waybills = ordersService.generateWaybill(orders);
//				if (null != waybills && waybills.size() > 0) {
//					msg = "";
//				}else{
//					msg = "没有匹配的订单生成运单!";
//				}
//			}
			
			/*
			 * 1、获取满足状态和部门条件的所有订单
			 * 2、根据订单的收货地址匹配 站点所在区域和承运商
			 * 3、根据承运商 查询车型
			 * 4.一个承运商 每个区域内部所有订单匹配最小车型，如果[0,50)就不在匹配，匹配最大车型,大于100,就进行站点分配
			 * 5、一个区域内部根据站点分配订单
			 * 	（1）单个站点。从小往大匹配车型，满足重量90%-100%，体积小于额定就生成
			 * 	（2）多个站点。
			 */
			Set<Area> areaList = new HashSet<>();
			Set<Integer> wlcompanyList = new HashSet<>();
			Set<String> areaCodeList = new HashSet<>();
			Area area = new Area();
			for (Orders orders : ordersList) {
				String siteCode = orders.getShaddress();
				area = areaService.selectBySiteCode(siteCode);
				if(area != null){
					orders.setSubcontractor(area.getWlcompany()+"");
					orders.setAreacode(area.getCode());
					ordersService.updateSelective(orders);
					
					areaList.add(area);
					wlcompanyList.add(area.getWlcompany());
					areaCodeList.add(area.getCode());
				}
			}

			Map<String, Object> vehMap = new HashMap<String, Object>();
			List<VehicleModel> vehicleModelList = null;
			//多个区域,分成每个区域进行
			if (areaList.size() > 1) {
				ordMap.remove("deptids");
				for (Area eachArea : areaList) {
					ordMap.put("subcontractor", eachArea.getWlcompany());
					ordMap.put("areacode", eachArea.getCode());
					//每个区域订单
					List<Orders> eachOrdersList = ordersService.selectAllOrdersByCtn(ordMap);
					System.out.println(eachArea.getCode() + "区域, 共有" + eachOrdersList.size() +"订单");
					vehMap.put("wlcompany", eachArea.getWlcompany());
					vehicleModelList = vehicleModelService.selectAllVehicleModelByCtn(vehMap);
					
					msg = AreaOrderMatchVehicle(vehicleModelList, eachOrdersList, 50);
				}
			}else if (areaList.size() == 1){
				//一个承运商所有车型
				vehMap.put("wlcompanyList", wlcompanyList);
				vehicleModelList = vehicleModelService.selectAllVehicleModelByCtn(vehMap);
				
				msg = AreaOrderMatchVehicle(vehicleModelList, ordersList, 50);
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg = "exception;";
		}
		
		resultMap.put("code", code);
		resultMap.put("message", msg);
		
		return new Result(code, resultMap, msg);
	}

	/**
	 * 每个区域内部所有订单匹配最小车型，如果[0,50)就不在匹配，[50,100]满足，(100,无限大)匹配最大车型,大于100,就进行站点分配
	 * @param msg
	 * @param vehicleModelList
	 * @param ordersList 每个区域内部所有订单
	 * @return
	 */
	private String AreaOrderMatchVehicle(List<VehicleModel> vehicleModelList, List<Orders> ordersList, int percent) {
		String msg = "fail;";
		BigDecimal totalWeight = BigDecimal.ZERO;
		BigDecimal totalVolume = BigDecimal.ZERO;
		BigDecimal totalAmount = BigDecimal.ZERO;
		String deptname = "";
//		String fhaddress = "";
//		String shaddress = "";
		Set<String> siteCodeList = new HashSet<>();//站点
		
		for (Orders orders : ordersList) {
			totalWeight = totalWeight.add(new BigDecimal(orders.getWeight()+""));
			totalVolume = totalVolume.add(new BigDecimal(orders.getVolume()+""));
			totalAmount = totalAmount.add(new BigDecimal(orders.getAmount()+""));
			deptname = orders.getDeptname();
//			fhaddress = orders.getFhaddress();
//			shaddress = orders.getShaddress();
			siteCodeList.add(orders.getShaddress());
		}
		
		VehicleModel vehicleModel = vehicleModelList.get(0);
		System.out.println("订单总重量(kg)：" + totalWeight + "订单总体积：" + totalVolume);
		int num = matching(totalWeight.doubleValue(), vehicleModel.getWeight());
		if( num < percent){
			//不满足最小车型
			System.out.println("不满足最小车型");
		}else if(num <= 100){
			//匹配一车(体积能装下)
			msg = msg + autoWayBill(ordersList, totalWeight, totalVolume, totalAmount, deptname, vehicleModel);
			System.out.println(percent+","+100+ "之间匹配成功");
		}else{
			//超过最小车型，匹配最大车型
			int max = vehicleModelList.size() - 1;
			vehicleModel = vehicleModelList.get(max);
			int num2 = matching(totalWeight.doubleValue(), vehicleModel.getWeight());
			if( num2 < 95){
				//车型大了（去掉首尾）匹配其他车型
				int counts = 0;
				for (int i = 1; i < (max-1); i++) {
					vehicleModel = vehicleModelList.get(i);
					int num3 = matching(totalWeight.doubleValue(), vehicleModel.getWeight());
					if (percent <= num3 && num3 <= 100) {
						msg = msg + autoWayBill(ordersList, totalWeight, totalVolume, totalAmount, deptname,
								vehicleModel);
						System.out.println(percent+","+100+ "之间匹配成功");
						counts ++;
						break;
					}
				}
				
				if(counts == 0){//没匹配上最合适的车型
					msg = msg + autoWayBill(ordersList, totalWeight, totalVolume, totalAmount, deptname,
							vehicleModel);
					System.out.println(percent+","+100+ "之间匹配成功");
				}
				
			}else if(num2 <= 100){
				msg = msg + autoWayBill(ordersList, totalWeight, totalVolume, totalAmount, deptname, vehicleModel);
				System.out.println(percent+","+100+ "之间匹配成功");
			}else{
				//一车装不完
				msg = msg + siteOrder2WayBill(vehicleModelList, ordersList, percent, siteCodeList, msg);
			}
		}
		return msg;
	}

	/**
	 * 5、一个区域内部根据站点分配订单
	 * 	（1）单个站点。从小往大匹配车型，满足重量90%-100%，体积小于额定就生成
	 * 	（2）多个站点。
	 */
	private String siteOrder2WayBill(List<VehicleModel> vehicleModelList, List<Orders> ordersList, int percent, Set<String> siteCodeList, String msg){
		String deptname = "";
		
		Map<String, Object> ordMap = null;
		List<Orders> newSiteOrdersList = new ArrayList<>();//实际各站点订单
		BigDecimal totalWeight2 = BigDecimal.ZERO;
		BigDecimal totalVolume2 = BigDecimal.ZERO;
		BigDecimal totalAmount2 = BigDecimal.ZERO;
		
		int max = vehicleModelList.size() - 1;
		VehicleModel vehicleModel = vehicleModelList.get(max);
		
//		for (String siteCode : siteCodeList) {
		Iterator<String> it = siteCodeList.iterator();
		while (it.hasNext()) {
			String siteCode = it.next();
			
			ordMap = new HashMap<String, Object>();
			ordMap.put("status", "0");
			ordMap.put("state", "0");
			ordMap.put("shaddress", siteCode);
			List<Orders> siteOrdersList = ordersService.selectAllOrdersByCtn(ordMap);
			
//			for (Orders siteOrder : siteOrdersList) {
			Iterator<Orders> orderIter = siteOrdersList.iterator();
			while (orderIter.hasNext()) {
				Orders siteOrder = orderIter.next();
				
				totalWeight2 = totalWeight2.add(new BigDecimal(siteOrder.getWeight()+""));
				totalVolume2 = totalVolume2.add(new BigDecimal(siteOrder.getVolume()+""));
				totalAmount2 = totalAmount2.add(new BigDecimal(siteOrder.getAmount()+""));
				deptname = siteOrder.getDeptname();
				newSiteOrdersList.add(siteOrder);
				
				int siteNum = matching(totalWeight2.doubleValue(), vehicleModel.getWeight());
				//单个站点匹配最大车型
				if(siteNum < 50){
					//添加其他站点的订单
					continue;
				}else if(siteNum < 95){
					/*
					 * 1.如果后面还有订单继续装
					 * 2.如果后面没有了
					 * 		匹配小点的车型，如果有合适就生成车次，没有就根据最大车型生成车次
					 */
					if(orderIter.hasNext() || it.hasNext()){
						continue;
					}else{
						int counts = 0;
						for (int i = 0; i < (max-1); i++) {
							vehicleModel = vehicleModelList.get(i);
							int eachNum = matching(totalWeight2.doubleValue(), vehicleModel.getWeight());
							if (80 <= eachNum && eachNum <= 100) {
								counts ++;
								msg = msg + autoWayBill(newSiteOrdersList, totalWeight2, totalVolume2, totalAmount2, deptname, vehicleModel);
								System.out.println(percent+","+100+ "之间匹配成功");
								//封装的清空，BigDecimal有点问题
//								clearData(newSiteOrdersList, totalWeight2, totalVolume2, totalAmount2);
								//清空之前是计数变量
								ordersList.clear();
								totalWeight2 = BigDecimal.ZERO;
								totalVolume2 = BigDecimal.ZERO;
								totalAmount2 = BigDecimal.ZERO;
								break;
							}
						}
						
						if(counts == 0){//没匹配上最合适的车型
							msg = msg + autoWayBill(newSiteOrdersList, totalWeight2, totalVolume2, totalAmount2, deptname, vehicleModel);
							System.out.println(percent+","+100+ "之间匹配成功");
							//封装的清空，BigDecimal有点问题
//							clearData(newSiteOrdersList, totalWeight2, totalVolume2, totalAmount2);
							//清空之前是计数变量
							ordersList.clear();
							totalWeight2 = BigDecimal.ZERO;
							totalVolume2 = BigDecimal.ZERO;
							totalAmount2 = BigDecimal.ZERO;
						}
					}
				}else if(siteNum <= 100){
					//满足
					msg = msg + autoWayBill(newSiteOrdersList, totalWeight2, totalVolume2, totalAmount2, deptname, vehicleModel);
					System.out.println(percent+","+100+ "之间匹配成功");
					//封装的清空，BigDecimal有点问题
//					clearData(newSiteOrdersList, totalWeight2, totalVolume2, totalAmount2);
					//清空之前是计数变量
					ordersList.clear();
					totalWeight2 = BigDecimal.ZERO;
					totalVolume2 = BigDecimal.ZERO;
					totalAmount2 = BigDecimal.ZERO;
					
				}else{
					//情况太特殊没法解决，如85后还有订单刚好加入超过了100
				}
			}
			
		}
		
		return msg;
	}
	
	/**
	 * 清空之前是计数变量
	 * @param ordersList
	 * @param totalWeight
	 * @param totalVolume
	 * @param totalAmount
	 */
	private void clearData(List<Orders> ordersList, BigDecimal totalWeight, BigDecimal totalVolume,
			BigDecimal totalAmount) {
		ordersList.clear();
		totalWeight = BigDecimal.ZERO;
		totalVolume = BigDecimal.ZERO;
		totalAmount = BigDecimal.ZERO;
//		totalWeight = new BigDecimal("0");
//		totalVolume = new BigDecimal("0");
//		totalAmount = new BigDecimal("0");
	}
	
	/**
	 * 生成运单
	 * @param ordersList
	 * @param totalWeight
	 * @param totalVolume
	 * @param totalAmount
	 * @param deptname
	 * @param vehicleModel
	 * @return
	 */
	private String autoWayBill(List<Orders> ordersList, BigDecimal totalWeight, BigDecimal totalVolume,
			BigDecimal totalAmount, String deptname, VehicleModel vehicleModel) {
		String msg = "fail;";
		if(totalVolume.doubleValue() <= vehicleModel.getVolum()){
//			Waybill waybill = newWayBill(totalWeight, totalVolume, totalAmount, deptname, vehicleModel);
			Waybill waybill = new Waybill();
			waybill.setC_volume(vehicleModel.getVolum());
			waybill.setC_weight(vehicleModel.getWeight());
			waybill.setSubcontractor(vehicleModel.getWlcompany());
			waybill.setDeptname(deptname);
//				waybill.setFhaddress(fhaddress);
//				waybill.setShaddress(shaddress);
			waybill.setCode("TCK" + MathUtil.random());
			waybill.setState("0");// 阶段初始为 0
			waybill.setCreatetime(new Date());
			waybill.setAmount(totalAmount.doubleValue());
			waybill.setVolume(totalVolume.doubleValue());
			waybill.setWeight(totalWeight.doubleValue());
			
			waybillService.insert2Auto(waybill, ordersList);
			msg = "success;";
		}
		return msg;
	}

//	/**
//	 * 创建车次
//	 * @param ordersList
//	 * @param totalWeight
//	 * @param totalVolume
//	 * @param totalAmount
//	 * @param deptname
//	 * @param waybill
//	 * @param vehicleModel
//	 */
//	private Waybill newWayBill(BigDecimal totalWeight, BigDecimal totalVolume,
//			BigDecimal totalAmount, String deptname, VehicleModel vehicleModel) {
//		Waybill waybill = new Waybill();
//		waybill.setC_volume(vehicleModel.getVolum());
//		waybill.setC_weight(vehicleModel.getWeight());
//		waybill.setSubcontractor(vehicleModel.getWlcompany());
//		waybill.setDeptname(deptname);
////			waybill.setFhaddress(fhaddress);
////			waybill.setShaddress(shaddress);
//		waybill.setCode("TCK" + MathUtil.random());
//		waybill.setState("0");// 阶段初始为 0
//		waybill.setCreatetime(new Date());
//		waybill.setAmount(totalAmount.doubleValue());
//		waybill.setVolume(totalVolume.doubleValue());
//		waybill.setWeight(totalWeight.doubleValue());
//		
//		return waybill;
//	}

	/**
	 * 匹配车型的重量 与订单的重量，在[50,100]返回true，否则
	 * @param orderParam
	 * @param vehModParam
	 * @return
	 */
	private int matching(double orderParam, double vehModParam) {
		String mach = String.valueOf((orderParam / vehModParam) * 100);
		mach = mach.substring(0, mach.indexOf("."));
		int num = Integer.parseInt(mach);
		return num;
	}
	
	/**
	 * 检查订单
	 */
	@RequestMapping(value = "/checkOrders")
	@ResponseBody
	public Result checkOrders(HttpServletRequest request) {
		
		int code = 0;
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
	
	/**
	 * 创建 子单
	 */
	@RequestMapping(value = "/createSuborder")
	@ResponseBody
	public Result createSuborder(HttpServletRequest request) {
//		parentOrderid
		String id = request.getParameter("parentOrderid");
		String msg = "创建子单失败!";
		List<Orders> list = null;
		Orders orders = null;
		try {
			int orderid = Integer.parseInt(id);
			orders = ordersService.selectById(orderid);
			
			Map<String, Object> argsMap = new HashMap<String, Object>();
			argsMap.put("pid", orderid);
			list = ordersService.selectAll4Page(argsMap);
//			for (Orders subOrders : list) {
//				ordersService.setGoodsByOrderId(subOrders);
//			}
			
			int num = list.size() + 1;
			orders.setCode(orders.getCode() + "_" + num);
			orders.setCreatetime(new Date());
			orders.setPid(orders.getId());
			//清空数量
			orders.setAmount(0.0);
			orders.setWeight(0.0);
			orders.setVolume(0.0);
			
//			ordersService.setGoodsByOrderId(orders);
			ordersService.insert(orders);
			
			msg = "创建子单成功";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result(0, orders, msg);
	}
	
	/**
	 * 创建 子单
	 */
	@RequestMapping(value = "/deleteSuborder")
	@ResponseBody
	public Result deleteSuborder(HttpServletRequest request) {
		String msg = "删除子单失败!";
		Map<String, Object> returnMap = new HashMap<>();
		List<Map<String, Object>> failList = new ArrayList<>();
		List<Map<String, Object>> succeedList = new ArrayList<>();
		Map<String, Object> orderMap = null;
		try {
			String subOrderid = request.getParameter("suborderids");
			String[] idsStr = subOrderid.split(",");
			//暂时没起作用
//			String parentOrderid = request.getParameter("parentOrderid");
//			orderid orderno errorMsg
			if (idsStr.length > 0) {
				for (String id : idsStr) {
					int orId = Integer.parseInt(id);
					Orders orders = ordersService.selectById(orId);
					
					orderMap = new HashMap<>();
					orderMap.put("orderid", orders.getId());
					orderMap.put("orderno", orders.getCode());
					String state = orders.getState();
					String errorMsg = "";
					if("0".equals(state)){
						ordersService.deleteById(orId);
						succeedList.add(orderMap);
					}else {
						if("1".equals(state)){
							errorMsg = "订单已下发";
						}
						if("2".equals(state)){
							errorMsg = "订单在途运输";
						}
						if("3".equals(state)){
							errorMsg = "订单已签收";
						}
						orderMap.put("errorMsg", errorMsg);
						failList.add(orderMap);
					}
				}
			}
//			ordersService.deleteById(subOrderid);
			returnMap.put("fail", failList);
			returnMap.put("succeed", succeedList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Result(0, returnMap, msg);
	}
	
	/**
	 * 编辑子单
	 */
	@RequestMapping(value = "/editSuborder")
	@ResponseBody
	public Result editSuborder(HttpServletRequest request) {
		String msg = "编辑子单失败!";
		try {
			String subOrderid = request.getParameter("subOrderid");
			//json 数据
			String goodsList = request.getParameter("goodsList");
			int orId = Integer.parseInt(subOrderid);
			Orders orders = ordersService.selectById(orId);
			
			msg = "";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Result(0, msg);
	}
	
}