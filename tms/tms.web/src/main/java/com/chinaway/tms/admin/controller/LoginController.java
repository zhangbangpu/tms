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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysMenuService;
import com.chinaway.tms.admin.service.SysRoleService;
import com.chinaway.tms.admin.service.SysUserService;
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

	@Autowired
	private HttpServletRequest request;

	/**
	 * 用户登录<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html; charset=utf-8")
	@ResponseBody
	public Result login(@RequestParam("username") String username, @RequestParam("password") String password) {
		LOGGER.info("username=" + username + "password=" + password);

		Map<String, Object> argsMap = new HashMap<String, Object>();
		int code = 1;
		String msg = "登录异常!";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			argsMap.put("loginname", username);
			argsMap.put("password", password);
			
			List<SysUser> sysUserList = sysUserService.queryUserByCtn(argsMap);
			if (null != sysUserList && sysUserList.size() > 0) {
				//连表查询角色信息
				SysRole sysRole = sysRoleService.queryRoleByUserId(sysUserList.get(0).getId());
				List<SysMenu> sysMenuList = sysMenuService.queryMenuByRoleId(sysRole.getId());
				request.getSession().setAttribute("sysRole", sysRole);
				request.getSession().setAttribute("sysMenuList", sysMenuList);
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
	 * 用户登出<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public String logout(@RequestParam("username") String username, @RequestParam("password") String password) {
		LOGGER.info("username=" + username + "password=" + password);

		Map<String, Object> argsMap = new HashMap<String, Object>();
		try {
			argsMap.put("loginname", username);
			argsMap.put("password", password);
			List<SysUser> sysUserList = sysUserService.queryUserByCtn(argsMap);
			if (null != sysUserList && sysUserList.size() > 0) {
				request.getSession().removeAttribute("sysRole");
				request.getSession().removeAttribute("sysMenuList");
				argsMap.put("status", "true");
				argsMap.put("msg", "logout success!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "login failed!");
		}

		String ret = JsonUtil.obj2JsonStr(argsMap);
		LOGGER.info("logout传出的参数:" + ret);
		return ret;
	}

}