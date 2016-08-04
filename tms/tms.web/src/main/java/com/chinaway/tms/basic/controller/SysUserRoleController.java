package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.SysUserRole;
import com.chinaway.tms.basic.service.SysUserRoleService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SysUserRoleController {

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

		return JsonUtil.obj2JsonStr(argsMap);
	}

}