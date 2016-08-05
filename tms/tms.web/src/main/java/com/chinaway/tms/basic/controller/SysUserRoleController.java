package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysUserRole;
import com.chinaway.tms.admin.service.SysUserRoleService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SysUserRoleController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SysUserRoleController.class);
	
	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 添加用户角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param userRoleInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/addUserRole")
	@ResponseBody
	// http://localhost/tms/ws/addUserRole?userInfo=
	public String addUserRole(@RequestParam("userInfo") String userInfo) {
		LOGGER.info("传入的参数(userInfo):" + userInfo);
		
		SysUserRole sysUserRole = JsonUtil.jsonStr2Obj(userInfo, SysUserRole.class);
		Map<String, String> argsMap = new HashMap<String, String>();
		try {
			sysUserRoleService.insert(sysUserRole);
			argsMap.put("status", "true");
			argsMap.put("msg", "add User Role success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add User Role failed!");
		}

		String ret = JsonUtil.obj2JsonStr(argsMap);
		
		LOGGER.info("addUser传出的参数:" + ret);

		return ret;
	}

}