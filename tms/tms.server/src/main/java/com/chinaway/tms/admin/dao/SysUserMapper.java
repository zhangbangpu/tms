package com.chinaway.tms.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

public interface SysUserMapper extends BaseMapper<SysUser, Integer> {

	/**
	 * 查询用户根据条件
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
	 * 批量删除用户信息
	 * @param idArry
	 * @return
	 */
	int deleteById(@Param(value="idItem")String[] idArry);
	
}