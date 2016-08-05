package com.chinaway.tms.admin.service;

import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.core.BaseService;

public interface SysRoleService extends BaseService<SysRole, Integer> {

	/**
	 * 查询角色根据用户id
	 * @param integer
	 * @return
	 */
	SysRole queryRoleByUserId(int userId);

}
