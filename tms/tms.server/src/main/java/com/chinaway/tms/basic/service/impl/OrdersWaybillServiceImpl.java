package com.chinaway.tms.basic.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinaway.tms.basic.dao.OrdersWaybillMapper;
import com.chinaway.tms.basic.model.OrdersWaybill;
import com.chinaway.tms.basic.service.OrdersWaybillService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class OrdersWaybillServiceImpl extends AbstractService<OrdersWaybill, Integer> implements OrdersWaybillService {
	
	@Autowired
	private OrdersWaybillMapper ordersWaybillMapper;
	
	/**具体子类service的实现需要使用的mapper*/
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<OrdersWaybill, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<OrdersWaybill> select2PageBean(Map<String, Object> map) {
		PageBean<OrdersWaybill> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		//注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);//是否分页，默认是false不分页
		pageBean.setResult(ordersWaybillMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				ordersWaybillMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int deleteByCtn(Map<String, Object> argsmap) {
		return ordersWaybillMapper.deleteByCtn(argsmap);
	}
}
