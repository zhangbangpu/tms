package com.chinaway.tms.basic.dao;

import java.util.List;

import com.chinaway.tms.basic.model.SysMenu;
import com.chinaway.tms.basic.model.SysRole;
import com.chinaway.tms.core.BaseMapper;

public interface SysRoleMapper extends BaseMapper<SysRole, Integer> {

	List<SysMenu> queryMenuByRoleId(String roleId);
	
}