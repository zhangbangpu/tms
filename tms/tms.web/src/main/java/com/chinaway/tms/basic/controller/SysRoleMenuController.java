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

import com.chinaway.tms.admin.model.SysRoleMenu;
import com.chinaway.tms.admin.service.SysRoleMenuService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SysRoleMenuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysRoleController.class);
	
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
	public String addRoleMenu(@RequestParam("roleMenuInfo") String roleMenuInfo) {
		LOGGER.info("传入的参数(roleMenuInfo):" + roleMenuInfo);
		
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

		String ret = JsonUtil.obj2JsonStr(argsMap);
		
		LOGGER.info("addUser传出的参数:" + ret);

		return ret;
	}

}