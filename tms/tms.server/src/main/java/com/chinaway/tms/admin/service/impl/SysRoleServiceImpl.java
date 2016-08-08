package com.chinaway.tms.admin.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chinaway.tms.admin.dao.SysRoleMapper;
import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysRoleService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class SysRoleServiceImpl extends AbstractService<SysRole, Integer> implements SysRoleService {
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	/**具体子类service的实现需要使用的mapper*/
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<SysRole, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<SysRole> select2PageBean(Map<String, Object> map) {
		PageBean<SysRole> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		//注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);//是否分页，默认是false不分页
		pageBean.setResult(sysRoleMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				sysRoleMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public SysRole queryRoleByUserId(int userId) {
		return sysRoleMapper.queryRoleByUserId(userId);
	}

	@Override
	public PageBean<SysRole> queRolByCtnPgBn(Map<String, Object> argsMap) {
		return sysRoleMapper.queRolByCtnPgBn(argsMap);
	}

	@Override
	public int deleteByIds(String[] idsArray) {
		return sysRoleMapper.deleteByIds(idsArray);
	}

	@Override
	public List<SysUser> queAllRoleByCtn(Map<String, Object> argsMap) {
		return sysRoleMapper.queAllRoleByCtn(argsMap);
	}

}
