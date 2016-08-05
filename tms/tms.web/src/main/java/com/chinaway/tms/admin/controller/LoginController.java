package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysMenuService;
import com.chinaway.tms.admin.service.SysRoleService;
import com.chinaway.tms.admin.service.SysUserService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysMenuService sysMenuService;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 用户登录<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public String login(@RequestParam("username") String username, @RequestParam("password") String password) {

		LOGGER.info("username=" + username + "password=" + password);

		Map<String, Object> argsMap = new HashMap<String, Object>();
		try {
			argsMap.put("loginname", username);
			argsMap.put("password", password);
			List<SysUser> sysUserList = sysUserService.queryUserByCondition(argsMap);
			if (null != sysUserList && sysUserList.size() > 0) {
				System.out.println("userId=" + sysUserList.get(0).getId());
				SysRole sysRole = sysRoleService.queryRoleByUserId(sysUserList.get(0).getId());

				System.out.println("sysRole=" + sysRole == null ? "" : sysRole.getId());
				
				List<SysMenu> sysMenuList = sysMenuService.queryMenuByRoleId(sysRole.getId());

				System.out.println("sysMenuList=" + sysMenuList == null ? "" : sysMenuList.size());
				
				request.getSession().setAttribute("sysRole", sysRole);
				request.getSession().setAttribute("sysMenuList", sysMenuList);
				argsMap.put("status", "true");
				argsMap.put("msg", "login success!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "login failed!");
		}

		String ret = JsonUtil.obj2JsonStr(argsMap);
		LOGGER.info("addUser传出的参数:" + ret);
		return ret;
	}

}