package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.SysMenu;
import com.chinaway.tms.basic.model.SysRole;
import com.chinaway.tms.basic.model.SysUser;
import com.chinaway.tms.basic.service.SysRoleService;
import com.chinaway.tms.basic.service.SysUserService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired  
	private  HttpServletRequest request;

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
	
	/**
	 * 添加用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/login")
	@ResponseBody
	// http://localhost/tms/ws/addUser?userInfo=
	public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
		Map<String, Object> argsMap = new HashMap<String, Object>();
		try {
			argsMap.put("loginname", username);
			argsMap.put("password", password);
			List<SysUser> sysUserList = sysUserService.selectAll4Page(argsMap);
			if (null != sysUserList && sysUserList.size() > 0) {
				argsMap.put("status", "true");
				argsMap.put("msg", "login success!");
				List<SysRole> roleList = sysRoleService.selectAll4Page(argsMap);

				if (null != roleList && roleList.size() > 0) {
					String roleId = String.valueOf(roleList.get(0).getId());
					List<SysMenu> sysMenuList = sysRoleService.queryMenuByRoleId(roleId);

					request.getSession().setAttribute("role", roleList.get(0));
					request.getSession().setAttribute("sysMenuList", sysMenuList);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "login failed!");
		}

		return JsonUtil.obj2JsonStr(argsMap);
	}

}