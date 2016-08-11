package com.chinaway.tms.admin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysUserService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysUser")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 根据条件分页查询用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queUserByCtnPgBn")
	@ResponseBody
	public Result queUserByCtnPgBn(@RequestParam(value="page", defaultValue="1") int pageNo, 
			@RequestParam(value="rows", defaultValue="10") int pageSize , @RequestParam(value="sysUser") String sysUser) {
		SysUser user = (SysUser)JsonUtil.jsonStr2Obj(sysUser, SysUser.class);
		Map<String, Object> argsMap = new HashMap<String, Object>();
		argsMap.put("pageNo", pageNo);
		argsMap.put("pageSize", pageSize);
		if(null != user){
			argsMap.put("loginname", user.getLoginname());
			argsMap.put("name", user.getName());
			argsMap.put("phone", user.getPhone());
		}
//		argsMap.put(key, sysUser.getState());
		int code = 1;
		String msg = "按条件查询角色操作失败!";
		Map<String, Object> resultMap = new HashMap<>();

		int ret = 0;
		try {
			PageBean<SysUser> sysUserPgBn = sysUserService.selectUser2PageBean(argsMap);
			if (null != sysUserPgBn) {
				ret = sysUserPgBn.getResult().size();
			}

			if (ret > 0) {
				code = 0;
				msg = "按条件查询角色操作成功!";
				resultMap.put("sysUserList", sysUserPgBn.getResult());
			}

		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

//		JsonUtil.obj2JsonStr(result);
		return result;
	}
	
	/**
	 * 根据条件查询所有用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queAllUserByCtn")
	@ResponseBody
	public Result queAllUserByCtn() {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "查询所有用户操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
		int ret = 0;
		try {
			List<SysUser> sysUserList = sysUserService.queAllUserByCtn(argsMap);
			if(null != sysUserList){
				ret = sysUserList.size();
			}
			
			if (ret > 0) {
				code = 0;
				msg = "查询所有用户操作成功!";
				resultMap.put("sysUserList", sysUserList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
		return result;
	}
	
	/**
	 * 根据条件查询单个用戶信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryUserById")
	@ResponseBody
	public Result queryUserById(@RequestParam(value="id") String id) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "根据id查询用戶操作失败!";

		try {
			SysUser sysUser = sysUserService.selectById(id == "" ? 0 : Integer.parseInt(id));

			if (null != sysUser) {
				code = 0;
				msg = "根据id查询用戶操作成功!";
				//用户对象放入map
				resultMap.put("sysUser", sysUser);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
		return result;
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
	public Result addUser(@RequestParam(value="sysUser") String sysUser) {
		SysUser user = (SysUser)JsonUtil.jsonStr2Obj(sysUser, SysUser.class);
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			user.setCreatetime(new Date());
			ret = sysUserService.insert(user);

			if (ret > 0) {
				code = 0;
				msg = "添加操作成功!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);
//		return JsonUtil.obj2JsonStr(result);
		return result;
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
	public Result bathDelUser(@RequestParam(value="ids") String ids) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			String idArry[] = ids.split(",");
			ret = sysUserService.deleteByIds(idArry);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);
//		return JsonUtil.obj2JsonStr(result);
		return result;
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
	public Result delUser(@RequestParam(value="id") String id) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			ret = sysUserService.deleteById(id);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

//		return JsonUtil.obj2JsonStr(result);
		return result;
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
	public Result updateUser(@RequestParam(value="sysUser") String sysUser) {
		SysUser user = (SysUser)JsonUtil.jsonStr2Obj(sysUser, SysUser.class);
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改用户操作失败!";

		int ret = 0;
		try {
			ret = sysUserService.update(user);

			if (ret > 0) {
				code = 0;
				msg = "修改用户操作成功!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return result;
	}

}