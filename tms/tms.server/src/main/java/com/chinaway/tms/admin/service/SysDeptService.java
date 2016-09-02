package com.chinaway.tms.admin.service;

import java.util.List;
import java.util.Map;

import com.chinaway.tms.admin.model.SysDept;
import com.chinaway.tms.core.BaseService;
import com.chinaway.tms.utils.page.PageBean;

public interface SysDeptService extends BaseService<SysDept, Integer> {

	/**
	 * 根据条件查询部门带分页
	 * @param argsMap
	 * @return
	 */
	PageBean<SysDept> selectDept2PageBean(Map<String, Object> argsMap);

	/**
	 * 查询说有部门信息
	 * @param argsMap
	 * @return
	 */
	List<SysDept> selectDeptByCtn(Map<String, Object> argsMap);
	
	/**
	 * 按名称查询部门信息
	 * @param argsMap
	 * @return
	 */
	List<SysDept> selectDeptByName(Map<String, Object> argsMap);
	
	/**
	 * 根据id数组批量删除部门
	 * @param idsArray
	 * @return
	 */
	int deleteByIds(String ids);

	/**
	 * 获取部门最后记录id
	 * @return
	 */
	int selectMaxId();
}