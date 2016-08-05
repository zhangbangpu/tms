package com.chinaway.tms.admin.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.core.BaseService;

public interface SysUserService extends BaseService<SysUser, Integer> {

	/**
	 * 根据条件查询用户
	 * @param argsMap
	 * @return
	 */
	List<SysUser> queryUserByCondition(Map<String, Object> argsMap);


	
}
