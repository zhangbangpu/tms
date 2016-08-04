package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.SysRoleMenu;
import com.chinaway.tms.basic.service.SysRoleMenuService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SysRoleMenuController {

	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 添加角色菜单信息<br>
	 * 返回用户的json串
	 * 
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/addRoleMenu")
	@ResponseBody
	// http://localhost/tms/ws/addRoleMenu?roleMenuInfo=
	public String addRole(@RequestParam("roleMenuInfo") String roleMenuInfo) {
		SysRoleMenu sysRoleMenu = JsonUtil.jsonStr2Obj(roleMenuInfo, SysRoleMenu.class);
		Map<String, String> argsMap = new HashMap<String, String>();
		try {
			sysRoleMenuService.insert(sysRoleMenu);
			argsMap.put("status", "true");
			argsMap.put("msg", "add RoleMenu success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add RoleMenu failed!");
		}

		return JsonUtil.obj2JsonStr(argsMap);
	}

}