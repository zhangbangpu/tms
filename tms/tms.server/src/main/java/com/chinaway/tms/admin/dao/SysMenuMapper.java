package com.chinaway.tms.admin.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.core.BaseMapper;

public interface SysMenuMapper extends BaseMapper<SysMenu, Integer> {

	List<SysMenu> queryMenuByRoleId(@Param(value="id") Integer id);
	
}