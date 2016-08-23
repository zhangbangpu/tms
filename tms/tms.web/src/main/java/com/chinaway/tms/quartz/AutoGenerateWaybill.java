package com.chinaway.tms.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.service.OrdersService;

public class AutoGenerateWaybill {

	@Autowired
	private OrdersService ordersService;

	public void quartMethod() {
		// 报错也继续执行
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("state", "0");
			map.put("state", "0");
			List<Orders> ordersList = ordersService.selectAllOrdersByCtn(map);
			for (Orders orders : ordersList) {
				Map<String, Object> argsMap = new HashMap<String, Object>();
				argsMap.put("id", orders.getId());
				int ret = ordersService.generateWaybill(argsMap);
				if(ret == 0){
					// TODO 打日志
					System.out.println("orders=" + orders.getId());
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
	}
}
