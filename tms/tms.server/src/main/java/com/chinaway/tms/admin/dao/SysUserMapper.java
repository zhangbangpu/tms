package com.chinaway.tms.admin.dao;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.core.BaseMapper;

public interface SysUserMapper extends BaseMapper<SysUser, Integer> {

	/**
	 * 查询用户根据条件
	 * @param argsMap
	 * @return
	 */
	List<SysUser> queryUserByCondition(Map<String, Object> argsMap);
	
}