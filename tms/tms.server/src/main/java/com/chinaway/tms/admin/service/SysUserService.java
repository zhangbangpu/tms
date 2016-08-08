package com.chinaway.tms.admin.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.core.BaseService;
import com.chinaway.tms.utils.page.PageBean;

public interface SysUserService extends BaseService<SysUser, Integer> {

	/**
	 * 根据条件查询用户
	 * @param argsMap
	 * @return
	 */
	List<SysUser> queryUserByCondition(Map<String, Object> argsMap);

	
	/**
	 * 根据条件查询角色分页信息
	 * @param argsMap
	 * @return
	 */
	PageBean<SysUser> queUsrByCtnPgBn(Map<String, Object> argsMap);


	/**
	 * 删除用户根据ids
	 * @param idArry
	 * @return
	 */
	int deleteByIds(String[] idArry);


	/**
	 * 根据条件查询所有用户信息
	 * @param argsMap
	 * @return
	 */
	List<SysUser> queAllUsrByCtn(Map<String, Object> argsMap);


	
}
