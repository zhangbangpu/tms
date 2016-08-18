package com.chinaway.tms.basic.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chinaway.tms.basic.dao.CpmdMapper;
import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.basic.service.CpmdService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class CpmdServiceImpl extends AbstractService<Cpmd, Integer> implements CpmdService {
	
	@Autowired
	private CpmdMapper cpmdMapper;
	
	/**具体子类service的实现需要使用的mapper*/
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<Cpmd, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<Cpmd> select2PageBean(Map<String, Object> map) {
		PageBean<Cpmd> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		//注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);//是否分页，默认是false不分页
		pageBean.setResult(cpmdMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	public List<Cpmd> selectAllCpmdByCtn(Map<String, Object> argsMap) {
		return cpmdMapper.selectAllCpmdByCtn(argsMap);
	}
	
	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				cpmdMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}

}