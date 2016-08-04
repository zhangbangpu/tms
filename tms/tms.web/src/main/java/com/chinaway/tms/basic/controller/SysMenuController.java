package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chinaway.tms.basic.model.SysMenu;
import com.chinaway.tms.basic.service.SysMenuService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 添加菜单信息<br>
	 * 返回用户的json串
	 * 
	 * @param menuInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/addMenu")
	@ResponseBody
	// http://localhost/tms/ws/addMenu?menuInfo=
	public String addMenu(@RequestParam("menuInfo") String menuInfo) {
		SysMenu sysMenu = JsonUtil.jsonStr2Obj(menuInfo, SysMenu.class);
		Map<String, String> argsMap = new HashMap<String, String>();
		try {
			sysMenuService.insert(sysMenu);
			argsMap.put("status", "true");
			argsMap.put("msg", "add Menu success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add Menu failed!");
		}

		return JsonUtil.obj2JsonStr(argsMap);
	}

}