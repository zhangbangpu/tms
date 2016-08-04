package com.chinaway.tms.basic.service;

import java.util.List;
import com.chinaway.tms.basic.model.SysMenu;
import com.chinaway.tms.basic.model.SysRole;
import com.chinaway.tms.core.BaseService;

public interface SysRoleService extends BaseService<SysRole, Integer> {
	
	public List<SysMenu> queryMenuByRoleId(String roleId);
}
