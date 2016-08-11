package com.chinaway.tms.admin.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chinaway.tms.admin.dao.SysUserMapper;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysUserService;
import com.chinaway.tms.core.AbstractService;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

@Service
public class SysUserServiceImpl extends AbstractService<SysUser, Integer>implements SysUserService {

	@Autowired
	private SysUserMapper sysUserMapper;

	/** 具体子类service的实现需要使用的mapper */
	@Override
	@Autowired
	public void setBaseMapper(BaseMapper<SysUser, Integer> baseMapper) {
		super.setBaseMapper(baseMapper);
	}

	@Override
	public PageBean<SysUser> select2PageBean(Map<String, Object> map) {
		PageBean<SysUser> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		// 注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);// 是否分页，默认是false不分页
		pageBean.setResult(sysUserMapper.selectAll4Page(map));
		return pageBean;
	}
	
	@Override
	public PageBean<SysUser> selectUser2PageBean(Map<String, Object> map) {
		PageBean<SysUser> pageBean = new PageBean<>();
		pageBean.setPageNo(Integer.parseInt(map.get("pageNo").toString()));
		pageBean.setPageSize(Integer.parseInt(map.get("pageSize").toString()));
		// 注意map要先设置pageBean,拦截器里面要获取其值
		map.put("pageBean", pageBean);
		map.put("needPage", true);// 是否分页，默认是false不分页
		pageBean.setResult(sysUserMapper.selectAllUser4Page(map));
		return pageBean;
	}
	
	@Override
	public List<SysUser> queryUserByCtn(Map<String, Object> argsMap) {
		return sysUserMapper.queryUserByCtn(argsMap);
	}
	
	@Override
	public List<SysUser> queAllUserByCtn(Map<String, Object> argsMap) {
		return sysUserMapper.queAllUserByCtn(argsMap);
	}
	
	@Override
	public SysUser queOneUserByCtn(Map<String, Object> argsMap) {
		List<SysUser> sysUserList = sysUserMapper.queAllUserByCtn(argsMap);
		if (null != sysUserList && sysUserList.size() > 0) {
			return sysUserList.get(0);
		}

		return new SysUser();
	}

	@Override
	public int deleteByIds(String[] idArry) {
		return sysUserMapper.deleteById(idArry);
	}

	@Override
	@Transactional
	public int deleteById(String ids) {
		String[] idsStr = ids.split(",");
		if (idsStr.length > 0) {
			for (String id : idsStr) {
				sysUserMapper.deleteById(Integer.parseInt(id));
			}
			return 1;
		} else {
			return 0;
		}
	}
}