package com.chinaway.tms.admin.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.core.BaseService;
import com.chinaway.tms.utils.page.PageBean;

public interface SysRoleService extends BaseService<SysRole, Integer> {

	/**
	 * 查询角色分页根据条件
	 * @param argsMap
	 * @return
	 */
	PageBean<SysRole> selectRole2PageBean(Map<String, Object> argsMap);

	/**
	 * 根据条件查询所有角色
	 * @param argsMap
	 * @return
	 */
	List<SysRole> queAllRoleByCtn(Map<String, Object> argsMap);
	
	/**
	 * 根据部门id 连表查询角色
	 * @param argsMap
	 * @return
	 */
	List<SysRole> queryRoleByDeptid(Map<String, Object> argsMap);
	
	/**
	 * 查询角色根据用户id
	 * @param integer
	 * @return
	 */
	SysRole queryRoleByUserId(int userId);

	/**
	 * 批量删除角色
	 * @param idsArray
	 * @return
	 */
	int deleteByIds(String ids);

	/**
	 * 获取角色根据名称
	 * @param argsMap
	 * @return
	 */
	List<SysRole> selectRoleByName(Map<String, Object> argsMap);

}