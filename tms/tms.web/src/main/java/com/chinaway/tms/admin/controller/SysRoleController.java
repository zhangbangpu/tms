package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.admin.service.SysRoleService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysRole")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;

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
	@RequestMapping(value = "/queryAllRole")
	@ResponseBody
	public Result queryAllRole(HttpServletRequest request) {
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		int code = 1;
//		String msg = "查询所有角色操作失败!";
//		Map<String, Object> argsMap = new HashMap<String, Object>();
//		int ret = 0;
//		try {
			List<SysRole> sysRoleList = sysRoleService.queAllRoleByCtn(argsMap);
//			if(null != sysRoleList){
//				ret = sysRoleList.size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询所有角色操作成功!";
//				resultMap.put("sysRoleList", sysRoleList);
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
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
	public Result queryOneById(@RequestParam(value="id")String id) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "根据id查询角色操作失败!";

//		try {
			SysRole sysRole = sysRoleService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != sysRole) {
//				code = 0;
//				msg = "根据id查询部门操作成功!";
//				resultMap.put("sysRole", sysRole);
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
		return new Result(0, sysRole);
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
	public Result addRole(@RequestParam(value="sysRole") String sysRole) {
		SysRole role = (SysRole)JsonUtil.jsonStr2Obj(sysRole, SysRole.class);
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			ret = sysRoleService.insert(role);
			if (ret > 0) {
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
	public Result bathDelRole(@RequestParam(value="ids") String ids) {
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
	public Result delRole(@RequestParam(value="ids") String ids) {
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
	public Result updateRole(@RequestParam(value="sysRole") String sysRole) {
		SysRole role = (SysRole)JsonUtil.jsonStr2Obj(sysRole, SysRole.class);
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