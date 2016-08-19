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
import com.chinaway.tms.basic.model.OrderItem;
import com.chinaway.tms.basic.service.OrderItemService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/orderItem")
public class OrderItemManagerController {
	
	@Autowired
	private OrderItemService orderItemService;
	
	/**
	 * 根据条件查询所有站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/selectAllOrderItemByCtn")
	@ResponseBody
	public Result selectAllOrderItemByCtn(HttpServletRequest request) {
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "查询所有站点操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
//		int ret = 0;
//		try {
			List<OrderItem> orderItemList = orderItemService.selectAllOrderItemByCtn(argsMap);
//			if(null != orderItemtList){
//				ret = orderItemtList.size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询所有站点操作成功!";
//				resultMap.put("orderItemList", orderItemList);
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, orderItemList);
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
	public Result selectOrderItem2PageBean(HttpServletRequest request) {
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<OrderItem> pageBean = orderItemService.select2PageBean(argsMap);
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

		OrderItem orderItem = null;
//		try {
			orderItem = orderItemService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != orderItem) {
//				code = 0;
//				msg = "根据id查询站点操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

//		Result result = new Result(code, orderItem, msg);

		return new Result(0, orderItem);
	}
	
	/**
	 * 添加站点信息<br>
	 * 返回站点的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addOrderItem")
	@ResponseBody
	public Result addOrderItem(HttpServletRequest request, OrderItem orderItem) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作站点失败!";

		int ret = 0;
		try {
			
			if (orderItem.getId() != null) {
				ret = orderItemService.updateSelective(orderItem);
			}else{
				ret = orderItemService.insert(orderItem);
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
			ret = orderItemService.deleteById(ids);

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
	 * 修改站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateOrderItem")
	@ResponseBody
	public Result updateOrderItem(HttpServletRequest request, OrderItem orderItem) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改站点失败!";

		int ret = 0;
		try {
			ret = orderItemService.update(orderItem);

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