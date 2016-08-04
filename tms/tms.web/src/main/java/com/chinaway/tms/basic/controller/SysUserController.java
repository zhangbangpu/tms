package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.SysUser;
import com.chinaway.tms.basic.service.SysUserService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 添加用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/addUser")
	@ResponseBody
	// http://localhost/tms/ws/addUser?userInfo=
	public String addUser(@RequestParam("userInfo") String userInfo) {
		SysUser sysUser = JsonUtil.jsonStr2Obj(userInfo, SysUser.class);
		Map<String, String> argsMap = new HashMap<String, String>();
		try {
			sysUserService.insert(sysUser);
			argsMap.put("status", "true");
			argsMap.put("msg", "add sysUser success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add sysUser failed!");
		}

		return JsonUtil.obj2JsonStr(argsMap);
	}

}