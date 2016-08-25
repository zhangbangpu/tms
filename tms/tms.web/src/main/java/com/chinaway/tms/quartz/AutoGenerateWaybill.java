package com.chinaway.tms.quartz;

import org.springframework.stereotype.Component;

@Component
public class AutoGenerateWaybill {

//	private final Logger logger = LoggerFactory.getLogger(getClass());
//	
//	@Autowired
//	private OrdersService ordersService;

	
	
	public void execute() {
		System.out.println("quartz success!");
//		try {
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("status", "0");
//			map.put("state", "0");
//			List<Orders> ordersList = ordersService.selectAllOrdersByCtn(map);
//			for (Orders orders : ordersList) {
//				Map<String, Object> argsMap = new HashMap<String, Object>();
//				argsMap.put("id", orders.getId());
//				int ret = ordersService.generateWaybill(argsMap);
//				if(ret == 0){
//					// TODO 打日志
//					System.out.println("orders=" + orders.getId());
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
}
