package com.chinaway.tms.admin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinaway.tms.admin.dao.SysDeptMapper;
import com.chinaway.tms.admin.model.SysDept;
import com.chinaway.tms.admin.service.SysDeptService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class SysDeptServiceImpl extends AbstractService<SysDept, Integer> implements SysDeptService {
	
	@Autowired
	private SysDeptMapper sysDeptMapper;
	
	/**具体子类service的实现需要使用的mapper*/
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<SysDept, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<SysDept> select2PageBean(Map<String, Object> map) {
		PageBean<SysDept> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		//注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);//是否分页，默认是false不分页
		pageBean.setResult(sysDeptMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	public PageBean<SysDept> selectDept2PageBean(Map<String, Object> map) {
		PageBean<SysDept> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		//注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);//是否分页，默认是false不分页
		pageBean.setResult(sysDeptMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	public List<SysDept> selectDeptByCtn(Map<String, Object> argsMap) {
		return sysDeptMapper.selectDeptByCtn(argsMap);
	}
	
	@Override
	public List<SysDept> selectDeptByName(Map<String, Object> argsMap) {
		return sysDeptMapper.selectDeptByName(argsMap);
	}
	
	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				sysDeptMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int deleteByIds(String ids) {
		String[] idsArray = ids.split(",");
		return sysDeptMapper.deleteByIds(idsArray);
	}

	@Override
	public int selectMaxId() {
		return sysDeptMapper.selectMaxId();
	}
	
}