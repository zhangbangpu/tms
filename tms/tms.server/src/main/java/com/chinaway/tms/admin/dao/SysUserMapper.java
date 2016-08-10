package com.chinaway.tms.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.core.BaseMapper;


public interface SysUserMapper extends BaseMapper<SysUser, Integer> {

	/**
	 * 根据条件查询角色分页信息
	 * @param argsMap
	 * @return
	 */
	List<SysUser> selectAllUser4Page(Map<String, Object> argsMap);
	
	/**
	 * 查询用户根据条件连表不分页
	 * @param argsMap
	 * @return
	 */
	List<SysUser> queryUserByCtn(Map<String, Object> argsMap);
	
	/**
	 * 查询所有用户根据条件不连表不分页
	 * @param argsMap
	 * @return
	 */
	List<SysUser> queAllUserByCtn(Map<String, Object> argsMap);

	

	/**
	 * 批量删除用户信息
	 * @param idArry
	 * @return
	 */
	int deleteById(@Param(value="idItem")String[] idArry);
}