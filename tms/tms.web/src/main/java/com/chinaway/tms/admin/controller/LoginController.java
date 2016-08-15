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
//			argsMap.put("loginname", username);
//			argsMap.put("password", password);
			
			SysUser sysUser = sysUserService.queOneUserByCtn(argsMap);
			if(null != sysUser){
				request.getSession().setAttribute("sysUser", sysUser);
				request.getSession().setAttribute("username", sysUser.getLoginname());
				resultMap.put("username", sysUser.getLoginname());
			}
			
			if (null != sysUser) {
//				//连表查询角色信息
				code = 0;
				msg = "登录成功!";
			}else{
				code = 1;
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
	@RequestMapping(value = "/loginGetMenuList")
	@ResponseBody
	public Result loginGetMenuList(HttpServletRequest request) {
		if (!checkLogin(request)) {
			return new Result(2, "");
		}
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		argsMap.put("id", sysUser.getId());
		
//		SysUser retSysUser = sysUserService.queOneUserByCtn(argsMap);
		//连表查询角色信息
//		List<SysMenu> sysMenuList = (List<SysMenu>)request.getSession().getAttribute("sysMenuList");
//		SysUser sysUser = (SysUser)request.getSession().getAttribute("sysUser");
		SysRole sysRole = sysRoleService.queryRoleByUserId(sysUser.getId());
		List<Map<String, Object>> sysMenuMap = sysMenuService.queryMenuByRoleId(sysRole.getId());
		Result result = new Result(0, sysMenuMap, "success");
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
			return new Result(0, "");
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
				request.getSession().removeAttribute("sysRole");
				request.getSession().removeAttribute("sysMenuList");
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
	 * 检查是否登录
	 * @param request
	 * @return
	 */
	public static boolean checkLogin(HttpServletRequest request) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		if (null == sysUser) {
			return false;
		} else {
			return true;
		}
	}

}