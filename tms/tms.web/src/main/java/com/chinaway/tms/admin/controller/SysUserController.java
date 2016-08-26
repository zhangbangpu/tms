package com.chinaway.tms.admin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.model.SysUserRole;
import com.chinaway.tms.admin.service.SysUserRoleService;
import com.chinaway.tms.admin.service.SysUserService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysUser")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 根据条件查询站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result selectUser2PageBean(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}

		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<SysUser> pageBean = sysUserService.select2PageBean(argsMap);
		// String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		// return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}

	/**
	 * 根据条件分页查询用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/queUserByCtnPgBn")
	@ResponseBody
	public Result queUserByCtnPgBn(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}

		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		// SysUser user = (SysUser)JsonUtil.jsonStr2Obj(sysUser, SysUser.class);
		// Map<String, Object> argsMap = new HashMap<String, Object>();
		// argsMap.put("pageNo", pageNo);
		// argsMap.put("pageSize", pageSize);
		// if(null != user){
		// argsMap.put("loginname", user.getLoginname());
		// argsMap.put("name", user.getName());
		// argsMap.put("phone", user.getPhone());
		// }
		// argsMap.put(key, sysUser.getState());
		// int code = 1;
		// String msg = "按条件查询角色操作失败!";
		// Map<String, Object> resultMap = new HashMap<>();
		//
		// int ret = 0;
		// try {
		PageBean<SysUser> sysUserPgBn = sysUserService.selectUser2PageBean(argsMap);
		// if (null != sysUserPgBn) {
		// ret = sysUserPgBn.getResult().size();
		// }
		//
		// if (ret > 0) {
		// code = 0;
		// msg = "按条件查询角色操作成功!";
		// resultMap.put("sysUserList", sysUserPgBn.getResult());
		// }
		//
		// } catch (Exception e) {
		// e.getStackTrace();
		// }
		//
		// resultMap.put("code", code);
		// resultMap.put("msg", msg);
		// Result result = new Result(code, resultMap, msg);

		// JsonUtil.obj2JsonStr(result);
		return new Result(0, sysUserPgBn);
	}

	/**
	 * 根据条件查询所有用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/queAllUserByCtn")
	@ResponseBody
	public Result queAllUserByCtn(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		// Map<String, Object> resultMap = new HashMap<>();
		// int code = 1;
		// String msg = "查询所有用户操作失败!";
		// Map<String, Object> argsMap = new HashMap<String, Object>();
		// int ret = 0;
		// try {
		List<SysUser> sysUserList = sysUserService.queAllUserByCtn(argsMap);
		// if(null != sysUserList){
		// ret = sysUserList.size();
		// }
		//
		// if (ret > 0) {
		// code = 0;
		// msg = "查询所有用户操作成功!";
		// resultMap.put("sysUserList", sysUserList);
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// resultMap.put("code", code);
		// resultMap.put("msg", msg);
		// Result result = new Result(code, resultMap, msg);

		// return JsonUtil.obj2JsonStr(result);
		return new Result(0, sysUserList);
	}

	/**
	 * 根据条件查询单个用戶信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/queryOneById")
	@ResponseBody
	public Result queryOneById(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}

		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);

		String id = String.valueOf(argsMap.get("id"));
		// Map<String, Object> resultMap = new HashMap<>();
		// int code = 1;
		// String msg = "根据id查询用戶操作失败!";

		// try {
		SysUser sysUser = sysUserService.selectById(id == "" ? 0 : Integer.parseInt(id));

		// if (null != sysUser) {
		// code = 0;
		// msg = "根据id查询用戶操作成功!";
		// //用户对象放入map
		// resultMap.put("sysUser", sysUser);
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// resultMap.put("code", code);
		// resultMap.put("msg", msg);
		// Result result = new Result(code, resultMap, msg);

		// return JsonUtil.obj2JsonStr(result);
		return new Result(0, sysUser);
	}

	/**
	 * 添加用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/addUser")
	@ResponseBody
	public Result addUser(HttpServletRequest request) {
		// public Result addUser(HttpServletRequest request,
		// @RequestParam(value="sysUser") String sysUser) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}

		SysUser user = new SysUser();
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		// user = (SysUser)JsonUtil.jsonStr2Obj(sysUser, SysUser.class);
		if (null != argsMap.get("id")) {
			user.setId(Integer.parseInt(String.valueOf(argsMap.get("id"))));
		}
		if (null != argsMap.get("certificate")) {
			user.setCertificate(String.valueOf(argsMap.get("certificate")));
		}
		if (null != argsMap.get("codeid")) {
			user.setCodeid(Integer.parseInt(String.valueOf(argsMap.get("codeid"))));
		}
		if (null != argsMap.get("corporation")) {
			user.setCorporation(String.valueOf(argsMap.get("corporation")));
		}
		if (null != argsMap.get("corporationim")) {
			user.setCorporationim(String.valueOf(argsMap.get("corporationim")));
		}
		if (null != argsMap.get("deptid")) {
			user.setDeptid(String.valueOf(argsMap.get("deptid")));
		}
		if (null != argsMap.get("deptname")) {
			user.setDeptname(String.valueOf(argsMap.get("deptname")));
		}
		if (null != argsMap.get("email")) {
			user.setEmail(String.valueOf(argsMap.get("email")));
		}
		if (null != argsMap.get("intro")) {
			user.setIntro(String.valueOf(argsMap.get("intro")));
		}
		if (null != argsMap.get("loginname")) {
			user.setLoginname(String.valueOf(argsMap.get("loginname")));
		}
		if (null != argsMap.get("name")) {
			user.setName(String.valueOf(argsMap.get("name")));
		}
		if (null != argsMap.get("password")) {
			user.setPassword(String.valueOf(argsMap.get("password")));
		}
		if (null != argsMap.get("phone")) {
			user.setPhone(String.valueOf(argsMap.get("phone")));
		}
		if (null != argsMap.get("realname")) {
			user.setRealname(String.valueOf(argsMap.get("realname")));
		}
		if (null != argsMap.get("rolename")) {
			user.setRolename(String.valueOf(argsMap.get("rolename")));
		}
		if (null != argsMap.get("state")) {
			user.setState(String.valueOf(argsMap.get("state")));
		}
		if (null != argsMap.get("type")) {
			user.setType(String.valueOf(argsMap.get("type")));
		}

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			user.setCreatetime(new Date());
			if (user.getId() != null) {
				ret = sysUserService.updateSelective(user);
				if (ret > 0) {
					code = 0;
					msg = "添加操作成功!";
				}

			} else {
				user.setCreatetime(new Date());
				ret = sysUserService.insert(user);
				int retUr = 0;
				if (argsMap.get("roleId") instanceof Integer && null != argsMap.get("roleId")) {
					SysUserRole sysUserRole = new SysUserRole();
					sysUserRole.setRoleid((int) (argsMap.get("roleId")));
					sysUserRole.setUserid(user.getId());
					retUr = sysUserRoleService.insert(sysUserRole);
				}
				if (ret > 0 && retUr > 0) {
					code = 0;
					msg = "添加操作成功!";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		// Result result = new Result(code, resultMap, msg);
		// return JsonUtil.obj2JsonStr(result);
		return new Result(0, ret);
	}

	/**
	 * 批量删除用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/bathDelUser")
	@ResponseBody
	public Result bathDelUser(HttpServletRequest request, @RequestParam(value = "ids") String ids) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}

		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);

		ids = String.valueOf(argsMap.get("ids"));

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			ret = sysUserService.deleteByIds(ids);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		// Result result = new Result(code, resultMap, msg);
		// return JsonUtil.obj2JsonStr(result);
		return new Result(0, ret);
	}

	/**
	 * 删除用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/delUser")
	@ResponseBody
	public Result delUser(HttpServletRequest request, @RequestParam(value = "ids") String ids) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		ids = String.valueOf(argsMap.get("ids"));

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			ret = sysUserService.deleteById(ids);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		// Result result = new Result(code, resultMap, msg);

		// return JsonUtil.obj2JsonStr(result);
		return new Result(0, ret);
	}

	/**
	 * 修改用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateUser")
	@ResponseBody
	public Result updateUser(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		SysUser user = new SysUser();

		// user = (SysUser)JsonUtil.jsonStr2Obj(sysUser, SysUser.class);
		if (null != argsMap.get("id")) {
			user.setId(Integer.parseInt(String.valueOf(argsMap.get("id"))));
		}
		if (null != argsMap.get("certificate")) {
			user.setCertificate(String.valueOf(argsMap.get("certificate")));
		}
		if (null != argsMap.get("codeid")) {
			user.setCodeid(Integer.parseInt(String.valueOf(argsMap.get("codeid"))));
		}
		if (null != argsMap.get("corporation")) {
			user.setCorporation(String.valueOf(argsMap.get("corporation")));
		}
		if (null != argsMap.get("corporationim")) {
			user.setCorporationim(String.valueOf(argsMap.get("corporationim")));
		}
		if (null != argsMap.get("deptid")) {
			user.setDeptid(String.valueOf(argsMap.get("deptid")));
		}
		if (null != argsMap.get("deptname")) {
			user.setDeptname(String.valueOf(argsMap.get("deptname")));
		}
		if (null != argsMap.get("email")) {
			user.setEmail(String.valueOf(argsMap.get("email")));
		}
		if (null != argsMap.get("intro")) {
			user.setIntro(String.valueOf(argsMap.get("intro")));
		}
		if (null != argsMap.get("loginname")) {
			user.setLoginname(String.valueOf(argsMap.get("loginname")));
		}
		if (null != argsMap.get("name")) {
			user.setName(String.valueOf(argsMap.get("name")));
		}
		if (null != argsMap.get("password")) {
			user.setPassword(String.valueOf(argsMap.get("password")));
		}
		if (null != argsMap.get("phone")) {
			user.setPhone(String.valueOf(argsMap.get("phone")));
		}
		if (null != argsMap.get("realname")) {
			user.setRealname(String.valueOf(argsMap.get("realname")));
		}
		if (null != argsMap.get("rolename")) {
			user.setRolename(String.valueOf(argsMap.get("rolename")));
		}
		if (null != argsMap.get("state")) {
			user.setState(String.valueOf(argsMap.get("state")));
		}
		if (null != argsMap.get("type")) {
			user.setType(String.valueOf(argsMap.get("type")));
		}
		user.setCreatetime(new Date());

		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改用户操作失败!";

		int ret = 0;
		try {
			ret = sysUserService.updateSelective(user);

			if (ret > 0) {
				code = 0;
				msg = "修改用户操作成功!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		// Result result = new Result(code, resultMap, msg);

		return new Result(0, ret);
	}

}