package com.chinaway.tms.admin.dao;

import org.apache.ibatis.annotations.Param;

import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.core.BaseMapper;

public interface SysRoleMapper extends BaseMapper<SysRole, Integer> {

	/**
	 * 查询角色根据用户id
	 * @param userId
	 * @return
	 */
	SysRole queryRoleByUserId(@Param(value="userId")Integer userId);
	
}