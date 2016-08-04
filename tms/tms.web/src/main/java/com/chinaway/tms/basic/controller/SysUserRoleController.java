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
public class SysUserRoleController {

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 添加物流公司用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param companyuserInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/addCompanyuser")
	@ResponseBody
	// http://localhost/tms/ws/addCompanyuser?userInfo=
	public String addLine(@RequestParam("userInfo") String userInfo) {
		SysUser sysUser = JsonUtil.jsonStr2Obj(userInfo, SysUser.class);
		Map<String, String> argsMap = new HashMap<String, String>();
		try {
			sysUserService.insert(sysUser);
			argsMap.put("status", "true");
			argsMap.put("msg", "add Line success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add Line failed!");
		}

		return JsonUtil.obj2JsonStr(argsMap);
	}

}