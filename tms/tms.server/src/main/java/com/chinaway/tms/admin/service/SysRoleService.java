package com.chinaway.tms.admin.service;

import java.util.List;

import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.core.BaseService;

public interface SysRoleService extends BaseService<SysRole, Integer> {
	
	public List<SysMenu> queryMenuByRoleId(String roleId);
}
