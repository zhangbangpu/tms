package com.chinaway.tms.admin.service;

import com.chinaway.tms.admin.model.SysRoleMenu;
import com.chinaway.tms.core.BaseService;

public interface SysRoleMenuService extends BaseService<SysRoleMenu, Integer> {

	/**
	 * 添加角色菜单关联
	 * @param menuids 
	 * @param sysRoleMenu
	 * @return
	 */
	int insertRoleMenu(String menuids, SysRoleMenu sysRoleMenu);
	
}
