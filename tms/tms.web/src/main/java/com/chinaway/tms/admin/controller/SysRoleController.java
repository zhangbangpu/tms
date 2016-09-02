package com.chinaway.tms.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.admin.model.SysRoleMenu;
import com.chinaway.tms.admin.service.SysMenuService;
import com.chinaway.tms.admin.service.SysRoleMenuService;
import com.chinaway.tms.admin.service.SysRoleService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysRole")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 根据条件查询站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result selectUser2PageBean(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<SysRole> pageBean = sysRoleService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 根据条件查询角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping(value = "/queRoleByCtnPgBn")
	@ResponseBody
	public Result queRoleByCtnPgBn(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		SysRole role = (SysRole)JsonUtil.jsonStr2Obj(sysRole, SysRole.class);
//		Map<String, Object> argsMap = new HashMap<String, Object>();
//		argsMap.put("pageNo", pageNo);
//		argsMap.put("pageSize", pageSize);
//		if(null != role){
//			argsMap.put("name", role.getName());
//			argsMap.put("description", role.getDescription());
//			argsMap.put("type", role.getType());
//		}
		
//		int code = 1;
//		String msg = "按条件查询角色操作失败!";
//		Map<String, Object> resultMap = new HashMap<>();
//
//		int ret = 0;
//		try {
			PageBean<SysRole> sysRolePgBn = sysRoleService.selectRole2PageBean(argsMap);
//			if (null != sysRolePgBn) {
//				ret = sysRolePgBn.getResult().size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "按条件查询角色操作成功!";
//				resultMap.put("sysRoleList", sysRolePgBn.getResult());
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
		return new Result(0, sysRolePgBn);
	}
	
	/**
	 * 根据条件查询所有角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/getRoleByDeptid")
	@ResponseBody
	public Result getRoleByDeptid(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}

		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		List<SysRole> sysRoleList = new ArrayList<SysRole>();
		sysRoleList = sysRoleService.queryRoleByDeptid(argsMap);

		if (null == sysRoleList || sysRoleList.size() <= 0) {
			sysRoleList = sysRoleService.queAllRoleByCtn(null);
		}

		return new Result(0, sysRoleList);
	}
	
	/**
	 * 根据名称查询部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping(value = "/getRoleByName")
	@ResponseBody
	public Result getRoleByName(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		List<SysRole> sysRoleList = new ArrayList<SysRole>();
		if(null != argsMap && null != argsMap.get("name")){
			sysRoleList = sysRoleService.selectRoleByName(argsMap);
		}
		
		return new Result(0, sysRoleList);
	}
	
	/**
	 * 
	 * 根据条件查询所有角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryAllRole")
	@ResponseBody
	public Result queryAllRole(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		List<SysRole> sysRoleList = sysRoleService.queAllRoleByCtn(argsMap);
		return new Result(0, sysRoleList);
	}
	
	/**
	 * 根据条件查询单个角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryOneById")
	@ResponseBody
	public Result queryOneById(HttpServletRequest request, @RequestParam(value="id")String id) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
//		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		id = String.valueOf(argsMap.get("id"));
		
		int code = 1;
		String msg = "根据id查询角色操作失败!";

		SysRole sysRole = new SysRole();
		try {
			List<SysMenu> allMenuList = (List<SysMenu>) sysMenuService.selectAll4Page(new HashMap<String, Object>());
			if (!StringUtils.isEmpty(id)) {
				sysRole = sysRoleService.selectById(Integer.parseInt(id));
				List<Map<String, Object>> sysMenuList = sysMenuService.queryMenuByRoleId(Integer.parseInt(id));
				for (SysMenu sysMenu : allMenuList) {
					for (Map<String, Object> map : sysMenuList) {
						if (sysMenu.getId().equals(map.get("id"))) {
							sysMenu.setChecked(true);
						}
					}
				}
			}
			sysRole.setMenuList(allMenuList);
			
			if (null != sysRole) {
				code = 0;
				msg = "根据id查询部门操作成功!";
			}
//
		} catch (Exception e) {
			e.getStackTrace();
		}

		Result result = new Result(code, sysRole, msg);

//		return JsonUtil.obj2JsonStr(result);
		return result;
	}

	/**
	 * 添加角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping(value = "/addRole")
	@ResponseBody
	public Result addRole(HttpServletRequest request, SysRole sysRole) {
//	public Result addRole(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
//		SysRole role = new SysRole();
//		role = (SysRole)JsonUtil.jsonStr2Obj(sysRole, SysRole.class);
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		if (null != argsMap.get("id") && !StringUtils.isEmpty(String.valueOf(argsMap.get("id")))) {
//			role.setId(Integer.parseInt(String.valueOf(argsMap.get("id"))));
//		}
//		if(null != argsMap.get("deptid")){
//			role.setDeptid(String.valueOf(argsMap.get("deptid")));
//		}
//		if(null != argsMap.get("description")){
//			role.setDescription(String.valueOf(argsMap.get("description")));
//		}
//		if(null != argsMap.get("name")){
//			role.setName(String.valueOf(argsMap.get("name")));
//		}
//		if(null != argsMap.get("type")){
//			role.setType(String.valueOf(argsMap.get("type")));
//		}
		sysRole.setUpdatetime(new Date());
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		int insertRet = 0;
		try {
			sysRole.setCreatetime(new Date());
			if (sysRole.getId() != null) {
				ret = sysRoleService.updateSelective(sysRole);
			} else {
				ret = sysRoleService.insert(sysRole);
			}
			
			if (argsMap.get("menuIds") instanceof String
					&& StringUtils.isNotEmpty(String.valueOf(argsMap.get("menuIds")))) {
				SysRoleMenu sysRoleMenu = new SysRoleMenu();
				sysRoleMenu.setRoleid(sysRole.getId());
				//TODO 待优化成批量插入
				insertRet = sysRoleMenuService.insertRoleMenu(String.valueOf(argsMap.get("menuIds")), sysRoleMenu);
			}
			
			if (ret > 0 && insertRet > 0) {
				code = 0;
				msg = "添加操作成功!";
			}

		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
		return new Result(0, ret);
	}
	
	/**
	 * 批量删除角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/bathDelRole")
	@ResponseBody
	public Result bathDelRole(HttpServletRequest request, @RequestParam(value="ids") String ids) {
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
			ret = sysRoleService.deleteByIds(ids);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
		return new Result(0, ret);
	}
	
	/**
	 * 删除角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/delRole")
	@ResponseBody
	public Result delRole(HttpServletRequest request, @RequestParam(value="ids") String ids) {
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
			ret = sysRoleService.deleteById(ids);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
		return new Result(0, ret);
	}
	
	/**
	 * 修改角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/updateRole")
	@ResponseBody
	public Result updateRole(HttpServletRequest request, @RequestParam(value="sysRole") String sysRole) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		SysRole role = new SysRole();
//		role = (SysRole)JsonUtil.jsonStr2Obj(sysRole, SysRole.class);
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		
		role.setCreatetime(new Date());
		role.setDeptid(String.valueOf(argsMap.get("deptid")));
		role.setDescription(String.valueOf(argsMap.get("description")));
		role.setName(String.valueOf(argsMap.get("name")));
		role.setType(String.valueOf(argsMap.get("type")));
		role.setUpdatetime(new Date());
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改角色操作失败!";

		int ret = 0;
		try {
			ret = sysRoleService.update(role);

			if (ret > 0) {
				code = 0;
				msg = "修改角色操作成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
		return new Result(0, ret);
	}
	
}