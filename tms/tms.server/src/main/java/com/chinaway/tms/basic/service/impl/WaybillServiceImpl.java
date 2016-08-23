package com.chinaway.tms.basic.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinaway.tms.basic.dao.WaybillMapper;
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.WaybillService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class WaybillServiceImpl extends AbstractService<Waybill, Integer> implements WaybillService {
	
	@Autowired
	private WaybillMapper waybillMapper;
	
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


}
