package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysUserRole;
import com.chinaway.tms.admin.service.SysUserRoleService;
import com.chinaway.tms.util.Constants;
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
	public Result addUserRole(@RequestParam(value="sysUserRole") String sysUserRole) {
		SysUserRole userRole = (SysUserRole)JsonUtil.jsonStr2Obj(sysUserRole, SysUserRole.class);
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = Constants.ADD_USER_ROLE_OPRATION_FAILED;

		int ret = 0;
		try {
			ret = sysUserRoleService.insert(userRole);
			if (ret > 0) {
				code = 0;
				msg = Constants.ADD_USER_ROLE_OPRATION_SUCCESS;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, ret);
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
	public Result delUserRole(@RequestParam(value="id") String id) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = Constants.DELETE_USER_ROLE_OPRATION_FAILED;

		int ret = 0;
		try {
			ret = sysUserRoleService.deleteById(id);

			if (ret > 0) {
				code = 0;
				msg = Constants.DELETE_USER_ROLE_OPRATION_SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, ret);
	}
}