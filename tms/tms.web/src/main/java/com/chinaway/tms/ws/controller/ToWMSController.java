package com.chinaway.tms.ws.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.utils.MyConstant;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.lang.DateUtil;
import com.chinaway.tms.utils.lang.MathUtil;
import com.chinaway.tms.utils.lang.StringUtil;
import com.chinaway.tms.vo.Result;

/**
 * 订单接口
 * @author shu
 *
 */
@Controller
public class ToWMSController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ToWMSController.class);
	
	@Autowired
	private OrdersService ordersService ;
	
	/**
	 * 新增订单及出库单
	 * @param orderInfo
	 * @return
	 */
	@RequestMapping(value="ws/addOrder")
	@ResponseBody
	public Result addOrder(@RequestParam("orderInfo") String orderInfo){
		LOGGER.info("传入的参数(orderInfo):" + orderInfo);
		int code = 1;
		String msg = "";
		if(orderInfo != ""){
			Orders order = JsonUtil.jsonStr2Obj(orderInfo, Orders.class);
			try{
//				int maxId = ordersService.selectMaxId();
//				order.setCode("tms" + maxId );
				//设置有标识的主键
				order.setCode("tms" + MathUtil.random() );
				int count = ordersService.insert(order);
				if(count > 0){
					code = 0;
					msg = "新增订单成功";
				}
				
			}catch(Exception e){
				e.printStackTrace();
				msg = "新增订单出异常";
			}
		}else{
			code = 2;
			msg = "参数不能为空";
		}
		
		Result result = new Result(code,msg);
		LOGGER.info("addOrder传出的参数:" + result);
		
		return result;
	}
	
	/**
	 * 删除订单
	 * @param id
	 * @return
	 */
	@RequestMapping(value="ws/deleteOrder")
	@ResponseBody
	public Result deleteOrder(@RequestParam("fromcode") String fromcode){
		LOGGER.info("传入的参数(fromcode):" + fromcode);
		int code = 1;
		String msg = "";
		try{
			Map<String,Object> argsMap = new HashMap<>();
			argsMap.put("fromcode", fromcode);
			List<Orders> list = ordersService.selectAll4Page(argsMap);
			if(list.size() > 0){
				for (Orders order : list) {
					String state = order.getState();
					if (state.equals(MyConstant.ORDER_START)){
						code = 3;
						msg = "已生成运单，请走退货渠道";
					}else{
						ordersService.deleteById(order.getId());
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
		
		Result result = new Result(code,msg);
		LOGGER.info("deleteOrder传出的参数:" + result);
		
		return result;
	}
}
