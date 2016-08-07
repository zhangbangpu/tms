package com.chinaway.tms.admin.service;

import java.util.Map;

import com.chinaway.tms.admin.model.SysDept;
import com.chinaway.tms.core.BaseService;
import com.chinaway.tms.utils.page.PageBean;

public interface SysDeptService extends BaseService<SysDept, Integer> {

	/**
	 * 根据条件查询
	 * @param argsMap
	 * @return
	 */
	PageBean<SysDept> queDtByCtnPgBn(Map<String, Object> argsMap);

	/**
	 * 根据id数组批量删除部门
	 * @param idsArray
	 * @return
	 */
	int deleteByIds(String[] idsArray);
	
}
