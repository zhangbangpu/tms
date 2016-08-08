package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysRole;
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysRoleService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysRole")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 添加角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping(value = "/addRole")
	@ResponseBody
	public String addRole(SysRole sysRole) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			sysRoleService.insert(sysRole);
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
	
	/**
	 * 批量删除角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/bathDelRole")
	@ResponseBody
	public String bathDelRole(String ids) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			String idsArray[] = ids.split(",");
			ret = sysRoleService.deleteByIds(idsArray);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}
	
	/**
	 * 删除角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/delRole")
	@ResponseBody
	public String delRole(String id) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			ret = sysRoleService.deleteById(id);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}
	
	/**
	 * 修改角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/delRole")
	@ResponseBody
	public String updateRole(SysRole sysRole) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			ret = sysRoleService.update(sysRole);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}
	
	/**
	 * 根据条件查询角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param roleInfo
	 * @return
	 */
	@RequestMapping(value = "/queryRoleByCondition")
	@ResponseBody
	public String queryRoleByCondition(SysRole sysRole) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "按条件查询操作失败!";

		Map<String, Object> argsMap = new HashMap<String, Object>();
		argsMap.put("name", sysRole.getName());
//		argsMap.put(key, value);
//		argsMap.put(key, value);
		int ret = 0;
		try {
			PageBean<SysRole> sysRolePgBn = sysRoleService.queRolByCtnPgBn(argsMap);
			if (null != sysRolePgBn) {
				ret = sysRolePgBn.getResult().size();
			}
			
			if (ret > 0) {
				code = 0;
				msg = "按条件查询操作成功!";
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}
	
	/**
	 * 根据所以角色信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryAllRole")
	@ResponseBody
	public String queryAllRole() {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "查询所有角色操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
		int ret = 0;
		try {
			List<SysUser> sysUserList = sysRoleService.queAllRoleByCtn(argsMap);
			if(null != sysUserList){
				ret = sysUserList.size();
			}
			
			if (ret > 0) {
				code = 0;
				msg = "查询所有角色操作成功!";
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
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
	public String queryOneById(String id) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "根据id查询角色操作失败!";

		try {
			SysRole sysRole = sysRoleService.selectById(id == "" ? 0 : Integer.parseInt(id));

			if (null != sysRole) {
				code = 0;
				msg = "根据id查询部门操作成功!";
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