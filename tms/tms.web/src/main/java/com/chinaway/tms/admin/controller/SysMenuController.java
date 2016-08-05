package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.admin.service.SysMenuService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

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
	public String addMenu(SysMenu sysMenu) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			sysMenuService.insert(sysMenu);
			if (ret > 0) {
				code = 0;
				msg = "添加操作成功!";
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}

}