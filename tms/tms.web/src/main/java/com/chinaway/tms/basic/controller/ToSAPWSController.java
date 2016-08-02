package com.chinaway.tms.basic.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.Order;
import com.chinaway.tms.basic.service.OrderService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class ToSAPWSController {
	@Autowired
	private OrderService orderservice ;
	
	
	Map map = new HashMap<String,String>();
	String status;
	String msg;
	/**
	 * 新增订单及出库单
	 * @param orderInfo
	 * @return
	 */
	@RequestMapping(value="ws/addOrder")
	@ResponseBody
	public String addOrder2(@RequestParam("orderInfo") String orderInfo){
		if(orderInfo != ""){
			Order order = JsonUtil.jsonStr2Obj(orderInfo, Order.class);
			try{
				order.setId(1);
				order.setCode("d1234");
				order.setFromcode("d2345");
				order.setOrderfrom("");
				int r = orderservice.insert(order);
				status = "true";
				msg = "新增订单成功";
			}catch(Exception e){
				e.printStackTrace();
				status = "false";
				msg = "新增订单失败";
			}
			map.put(status, msg);
		}
		return JsonUtil.obj2JsonStr(map);
	}
	
	/**
	 * 删除订单
	 * @param id
	 * @return
	 */
	@RequestMapping(value="ws/deleteOrder")
	@ResponseBody
	public String deleteOrder(@RequestParam("orderId") int id){
		try{
			String state = orderservice.selectById(id).getState();
			if (state.equals("运单已生成")){
				status="true";
				msg = "已生成运单，请走退货渠道";
			}else{
				orderservice.deleteById(id);
				status="true";
				msg="删除成功";
				}
		}catch(Exception e){
			e.printStackTrace();
			status = "false";
			msg = "删除失败";
		}
		map.put(status, msg);
		return JsonUtil.obj2JsonStr(map);
	}
}
