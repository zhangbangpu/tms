package com.chinaway.tms.admin.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.core.BaseMapper;

public interface SysMenuMapper extends BaseMapper<SysMenu, Integer> {

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