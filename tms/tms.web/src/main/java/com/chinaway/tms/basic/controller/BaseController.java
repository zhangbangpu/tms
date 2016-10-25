package com.chinaway.tms.basic.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinaway.tms.admin.model.SysDept;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysDeptService;
import com.chinaway.tms.basic.service.OrdersService;

public class BaseController {

	@Autowired
	protected SysDeptService sysDeptService;
	@Autowired
	protected OrdersService ordersService;
	
	/**
	 * 获取当前用户的deptid集合，包括下级
	 * @param request
	 * @return
	 */
	protected Set<String> getUserDepts(HttpServletRequest request) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		String deptid = sysUser.getDeptid();
		List<SysDept> deptList = sysDeptService.selectChildsByDeptid(deptid);
		
		Set<String> deptidSet = new HashSet<>();
		deptidSet.add(deptid);//加上本身
		for (SysDept sysDept : deptList) {
			//子节点
			deptidSet.add(sysDept.getDeptid());
		}
		return deptidSet;
	}
}
