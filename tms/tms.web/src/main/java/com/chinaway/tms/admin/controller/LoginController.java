package com.chinaway.tms.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysMenuService;
import com.chinaway.tms.admin.service.SysRoleService;
import com.chinaway.tms.admin.service.SysUserService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

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

	/**
	 * 用户登录<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	@ResponseBody
	public Result login(HttpServletRequest request) {
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		LOGGER.info("username=" + argsMap.get("username") + "password=" + argsMap.get("password"));

		int code = 1;
		String msg = "登录异常!";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			argsMap.put("loginname", argsMap.get("username"));
//			argsMap.put("password", password);
			
			SysUser sysUser = sysUserService.queOneUserByCtn(argsMap);
			if(null != sysUser && null != sysUser.getId()){
				request.getSession().setAttribute("sysUser", sysUser);
				request.getSession().setAttribute("username", sysUser.getLoginname());
				resultMap.put("username", sysUser.getLoginname());
				
				SysRole sysRole = sysRoleService.queryRoleByUserId(sysUser.getId());
				request.getSession().setAttribute("rolename", sysRole.getName());
				List<Map<String, Object>> sysMenuMap = sysMenuService.queryMenuByRoleId(sysRole.getId());
				for (Map<String, Object> map : sysMenuMap) {
					if ("menu".equals(map.get("menutype"))) {
						resultMap.put("defaultIndex", map.get("resUrl"));
						break;
					}
				}
				request.getSession().setAttribute("sysMenu", sysMenuMap);
//				//连表查询角色信息
				code = 0;
				msg = "登录成功!";
			}else{
				code = 2;
				msg = "用户名或密码不正确!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);
		return result;
	}
	
	/**
	 * 用户登录<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/loginGetMenuList")
	@ResponseBody
	public Result loginGetMenuList(HttpServletRequest request) {
		if (!checkLogin(request)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", 2);
			return new Result(0, map);
		}
//		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
//		argsMap.put("id", sysUser.getId());

		int code = 1;
		String msg = "获取菜单异常!";
		List<Map<String, Object>> sysMenuMap = new ArrayList<Map<String, Object>>();
		try {
//			SysRole sysRole = sysRoleService.queryRoleByUserId(sysUser.getId());
//			sysMenuMap = sysMenuService.queryMenuByRoleId(sysRole.getId());
			
			sysMenuMap = (List<Map<String, Object>>) request.getSession().getAttribute("sysMenu");
			if (null != sysMenuMap) {
				code = 0;
				msg = "获取菜单成功!";
			} else {
				code = 1;
				msg = "获取菜单不正确!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Result result = new Result(code, sysMenuMap, msg);
		return result;
	}
	
	/**
	 * 用户登出<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public Result logout(HttpServletRequest request) {
		if (!checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		LOGGER.info("username=" + argsMap.get("username") + "password=" + argsMap.get("password"));

		int code = 1;
		String msg = "注销异常!";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			argsMap.put("loginname", argsMap.get("username"));
			argsMap.put("password", argsMap.get("password"));
			List<SysUser> sysUserList = sysUserService.queryUserByCtn(argsMap);
			if (null != sysUserList && sysUserList.size() > 0) {
				request.getSession().removeAttribute("sysUser");
				request.getSession().removeAttribute("username");
				argsMap.put("status", "true");
				argsMap.put("msg", "logout success!");
				code = 0;
				msg = "注销成功!";
			}else{
				code = 1;
				msg = "用户注销不正确!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			argsMap.put("status", "false");
			argsMap.put("msg", "login failed!");
		}

		String ret = JsonUtil.obj2JsonStr(argsMap);
		LOGGER.info("logout传出的参数:" + ret);
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		
		Result result = new Result(code, resultMap, msg);
		return result;
	}
	
	/**
	 * 
	 * 判断用户是否登录
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/isLogin")
	@ResponseBody
	public Result isLogin(HttpServletRequest request) {
		int code = 1;
		String msg = "登录校验异常!";
		if (checkLogin(request)) {
			code = 0;
			msg = "用户未登录!";
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(0, resultMap, msg);
		return result;
	}
	
	/**
	 * 检查是否登录
	 * @param request
	 * @return
	 */
	public static boolean checkLogin(HttpServletRequest request) {
		String username = (String) request.getSession().getAttribute("username");
		if (null == username) {
			return false;
		} else {
			return true;
		}
	}

}