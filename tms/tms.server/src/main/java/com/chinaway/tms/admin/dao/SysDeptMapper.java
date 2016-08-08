package com.chinaway.tms.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.chinaway.tms.admin.model.SysDept;
import com.chinaway.tms.core.BaseMapper;
import com.chinaway.tms.utils.page.PageBean;

public interface SysDeptMapper extends BaseMapper<SysDept, Integer> {

	/**
	 * 查询部门分页根据条件
	 * @param argsMap
	 * @return
	 */
	PageBean<SysDept> queDtByCtnPgBn(Map<String, Object> argsMap);

	/**
	 * 批量删除部门
	 * @param idsArray
	 * @return
	 */
	int deleteByIds(@Param(value="idItem")String[] idItem);

	/**
	 * 根据条件查询所有部门信息
	 * @param argsMap
	 * @return
	 */
	List<SysDept> queAllDeptByCtn(Map<String, Object> argsMap);
	
}