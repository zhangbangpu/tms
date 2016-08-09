package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysUserRole;
import com.chinaway.tms.admin.service.SysUserRoleService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysUserRole")
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
	@RequestMapping(value = "/addUserRole")
	@ResponseBody
	public String addUserRole(SysUserRole sysUserRole) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			sysUserRoleService.insert(sysUserRole);
			if (ret > 0) {
				code = 0;
				msg = "添加操作成功!";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}
	
	/**
	 * 删除用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/delUserRole")
	@ResponseBody
	public String delUserRole(String id) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			ret = sysUserRoleService.deleteById(id);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}
}