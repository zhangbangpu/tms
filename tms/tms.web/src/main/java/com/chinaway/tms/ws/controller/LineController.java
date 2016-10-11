package com.chinaway.tms.ws.controller;

import java.util.ArrayList;
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
import com.chinaway.tms.basic.model.Line;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.service.LineService;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
@Deprecated
public class LineController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LineController.class);
	
	@Autowired
	private LineService lineService;
	
	@Autowired
	private OrdersService ordersService;
	
	/**
	 * 添加班线信息<br>
	 * 返回用户的json串
	 * @param username
	 * @param password
	 * @return
	 */
    @RequestMapping(value = "/ws/addLine")
	@ResponseBody
	public String addLine(@RequestParam("lineInfo") String lineInfo){
    	Line line = JsonUtil.jsonStr2Obj(lineInfo, Line.class);
    	
    	LOGGER.info("传入的参数(line):" + line);
    	
    	Map<String, String> argsMap = new HashMap<String, String>();
    	try {
    		lineService.insert(line);
    		argsMap.put("status", "true");
			argsMap.put("msg", "add Line success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add Line failed!");
		}
    	
		String ret = JsonUtil.obj2JsonStr(argsMap);
		
		LOGGER.info("addLine传出的参数:" + ret);

		return ret;
	}
    
	/**
	 * 测试手动调度信息<br>
	 * 返回用户的json串
	 * 
	 * @return
	 */
	@RequestMapping(value = "/ws/autoGeneratewaybill")
	@ResponseBody
	public String autoGenerateWaybill() {
		System.out.println("quartz success!");
		List<String> waybills = new ArrayList<String>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "0");
			map.put("state", "0");
			List<Orders> ordersList = ordersService.selectAllOrdersByCtn(map);
			for (Orders orders : ordersList) {
				waybills.addAll(ordersService.generateWaybill(orders));
				if (null != waybills && waybills.size() > 0) {
					System.out.println("---------------orders----------------" + orders.getId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return JsonUtil.obj2JsonStr(waybills);
	}
    
}