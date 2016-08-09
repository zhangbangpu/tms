package com.chinaway.tms.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

public interface SysRoleMapper extends BaseMapper<SysRole, Integer> {
	/**
	 * 根据条件查询角色分页信息
	 * @param argsMap
	 * @return
	 */
	PageBean<SysRole> selectAllRole4Page(Map<String, Object> argsMap);
	
	/**
	 * 根据条件查询所有角色
	 * @param argsMap
	 * @return
	 */
	List<SysUser> queAllRoleByCtn(Map<String, Object> argsMap);

	/**
	 * 查询角色根据用户id
	 * @param userId
	 * @return
	 */
	SysRole queryRoleByUserId(@Param(value="userId")Integer userId);

	/**
	 * 批量删除角色根据用户
	 * @param idsArray
	 * @return
	 */
	int deleteByIds(@Param(value="idItem")String[] idsArray);

}