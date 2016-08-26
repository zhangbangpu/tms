package com.chinaway.tms.basic.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinaway.tms.basic.dao.WaybillMapper;
import com.chinaway.tms.basic.model.Orders;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.OrdersService;
import com.chinaway.tms.basic.service.OrdersWaybillService;
import com.chinaway.tms.basic.service.WaybillService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class WaybillServiceImpl extends AbstractService<Waybill, Integer> implements WaybillService {
	
	@Autowired
	private WaybillMapper waybillMapper;
	
	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private OrdersWaybillService ordersWaybillService;
	
	/**具体子类service的实现需要使用的mapper*/
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<Waybill, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<Waybill> select2PageBean(Map<String, Object> map) {
		PageBean<Waybill> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		//注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);//是否分页，默认是false不分页
		pageBean.setResult(waybillMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	public List<Waybill> selectAllTckNumByCtn(Map<String, Object> argsMap) {
		return waybillMapper.selectAllTckNumByCtn(argsMap);
	}
	
	@Override
	public List<Waybill> selectAllTckNumRvwedByCtn(Map<String, Object> argsMap) {
		return waybillMapper.selectAllTckNumRvwedByCtn(argsMap);
	}
	
	public List<Waybill> selectByIds(String ids){
		String[] idsArray = ids.split(",");
		return waybillMapper.selectByIds(idsArray);
	}
	
	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				waybillMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public Integer insertWaybill(Waybill waybill , List<Orders> ordersList) {
		// TODO 添加运单信息 初始化状态
		 int ret = waybillMapper.insert(waybill);
		 
		 for(Orders orders: ordersList){
			 ordersService.insertWaybillOrders(orders, waybill.getId(), waybill.getWlcompany());
		 }
		 
		 return ret;
	}

	@Override
	@Transactional
	public int updateWaybill(Waybill waybill) {
		int retCode = waybillMapper.updateSelective(waybill);
		Map<String, Object> map = new HashMap<String, Object>();
		int retOrders = 0;
		String ordersState = "0";
		String ordersStatus = "0";
		// 运单审核通过1，订单阶段修改为 2订单已下发  订单状态改为 0 自动
		if ("1".equals(waybill.getState())) {
			ordersState = "2";
		}else if("-1".equals(waybill.getState())){
			// 运单审核不通过1，订单阶段修改为 0初始 订单状态改为 1手动
			ordersState = "0";
			ordersStatus = "1";
			Map<String, Object> argsmap = new HashMap<String, Object>();
			argsmap.put("waybillid", waybill.getId());
			int ret = ordersWaybillService.deleteByCtn(argsmap);

			if (ret <= 0) {
				// 返回成功标记
				retOrders = 1;
			}
		}else if("2".equals(waybill.getState())){
			// 运单在途，订单阶段修改为 3订单在途
			ordersStatus = "3";
		}else if("3".equals(waybill.getState())){
			//运单3已完成 修改订单状态为0 手动
			ordersStatus = "1";
		}
		
		waybill = waybillMapper.selectById(waybill.getId());
		map.put("waybillId", waybill.getId());
		List<Orders> ordersList = ordersService.selectAllOrdersByCtn(map);
		for (Orders orders : ordersList) {
			// 订单状态设置为自动
			orders.setStatus(ordersStatus);
			// 订单阶段设置为订单已下发
			orders.setState(ordersState);
			retOrders = ordersService.updateSelective(orders);
		}

		if (retCode > 0 && retOrders > 0) {
			// 返回成功标记
			return 1;
		} else {
			// 返回失败标记
			return -1;
		}
	}

	@Override
	public int selectMaxId() {
		return waybillMapper.selectMaxId();
	}

}