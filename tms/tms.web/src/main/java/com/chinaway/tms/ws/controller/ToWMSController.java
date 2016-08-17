package com.chinaway.tms.ws.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.Order;
import com.chinaway.tms.basic.service.OrderService;
import com.chinaway.tms.utils.MyConstant;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

/**
 * 订单接口
 * @author shu
 *
 */
public class ToWMSController {
	@Autowired
	private OrderService orderservice ;
	
	
	/**
	 * 新增订单及出库单
	 * @param orderInfo
	 * @return
	 */
	@RequestMapping(value="ws/addOrder")
	@ResponseBody
	public Result addOrder2(@RequestParam("orderInfo") String orderInfo){
		int code = 1;
		String msg = "";
		if(orderInfo != ""){
			Order order = JsonUtil.jsonStr2Obj(orderInfo, Order.class);
			try{
				int maxId = orderservice.selectMaxId();
				order.setCode("tms" + maxId );
				orderservice.insert(order);
				code = 0;
				msg = "新增订单成功";
			}catch(Exception e){
				e.printStackTrace();
				msg = "新增订单失败";
			}
		}else{
			code = 2;
			msg = "参数不能为空";
		}
		return new Result(code,msg);
	}
	
	/**
	 * 删除订单
	 * @param id
	 * @return
	 */
	@RequestMapping(value="ws/deleteOrder")
	@ResponseBody
	public Result deleteOrder(@RequestParam("fromcode") String fromcode){
		int code = 1;
		String msg = "";
		try{
			Map<String,Object> argsMap = new HashMap<>();
			argsMap.put("fromcode", fromcode);
			List<Order> list = orderservice.selectAll4Page(argsMap);
			if(list.size() > 0){
				for (Order order : list) {
					String state = order.getState();
					if (state.equals(MyConstant.ORDER_START)){
						code = 3;
						msg = "已生成运单，请走退货渠道";
					}else{
						orderservice.deleteById(order.getId());
						code = 0;
						msg="删除成功";
					}
				}
			}else{
				code = 2;
				msg = "没有该订单";
			}
				
		}catch(Exception e){
			e.printStackTrace();
			msg = "删除失败,出现异常";
		}
		return new Result(code,msg);
	}
}
