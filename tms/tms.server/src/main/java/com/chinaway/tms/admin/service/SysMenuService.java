package com.chinaway.tms.admin.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.core.BaseService;
import com.chinaway.tms.utils.page.PageBean;

public interface SysMenuService extends BaseService<SysMenu, Integer> {
	/**
	 * 根据条件查询所有菜单带分页
	 * @param argsMap
	 * @return
	 */
	public PageBean<SysMenu> selectMenu2PageBean(Map<String, Object> argsMap);
	
	/**
	 * 查询菜单根据角色id
	 * 
	 * @param integer
	 * @return
	 */
	public List<Map<String,Object>> queryMenuByRoleId(int roleId);

	/**
	 * 批量删除菜单信息
	 * @param idsArray
	 * @return
	 */
	int deleteByIds(String ids);
}