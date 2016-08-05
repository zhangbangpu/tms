package com.chinaway.tms.admin.service;

import java.util.List;
import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.core.BaseService;

public interface SysMenuService extends BaseService<SysMenu, Integer> {
	
	/**
	 * 查询菜单根据角色id
	 * 
	 * @param integer
	 * @return
	 */
	public List<SysMenu> queryMenuByRoleId(int roleId);
	
}
