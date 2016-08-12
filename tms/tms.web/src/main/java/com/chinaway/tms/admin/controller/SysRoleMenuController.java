package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysRoleMenu;
import com.chinaway.tms.admin.service.SysRoleMenuService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysRoleMenu")
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
	@RequestMapping(value = "/addRoleMenu")
	@ResponseBody
	public Result addRoleMenu(@RequestParam(value="sysRoleMenu") String sysRoleMenu) {
		SysRoleMenu roleMenu = (SysRoleMenu)JsonUtil.jsonStr2Obj(sysRoleMenu, SysRoleMenu.class);
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加角色菜单操作失败!";

		int ret = 0;
		try {
			ret = sysRoleMenuService.insert(roleMenu);
			if (ret > 0) {
				code = 0;
				msg = "添加角色菜单操作成功!";
			}

		} catch (Exception e) {
			e.getStackTrace();
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
	@RequestMapping(value = "/delRoleMenu")
	@ResponseBody
	public Result delRoleMenu(@RequestParam(value="id")String id) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除角色菜单操作失败!";

		int ret = 0;
		try {
			ret = sysRoleMenuService.deleteById(id);

			if (ret > 0) {
				code = 0;
				msg = "删除角色菜单操作成功!";
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