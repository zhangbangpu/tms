package com.chinaway.tms.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

public interface SysMenuMapper extends BaseMapper<SysMenu, Integer> {
	/**
	 * 查询所有菜单根据条件带分页
	 * @param argsMap
	 * @return
	 */
	PageBean<SysMenu> selectAllMenu4Page(Map<String, Object> argsMap);

	/**
	 * 查询菜单根据角色id
	 * @param id
	 * @return
	 */
	List<SysMenu> queryMenuByRoleId(@Param(value="id") Integer id);
	
	/**
	 * 批量删除菜单
	 * @param idsArray
	 * @return
	 */
	int deleteByIds(@Param(value="idItem")String[] idsArray);
}