package com.chinaway.tms.admin.controller;

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
	public String queUserByCtnPgBn(@RequestParam(value="page", defaultValue="1") int pageNo, 
			@RequestParam(value="rows", defaultValue="10") int pageSize , SysUser sysUser) {
		Map<String, Object> argsMap = new HashMap<String, Object>();
		argsMap.put("pageNo", pageNo);
		argsMap.put("pageSize", pageSize);
		argsMap.put("loginname", sysUser.getLoginname());
		argsMap.put("name", sysUser.getName());
		argsMap.put("phone", sysUser.getPhone());
//		argsMap.put(key, sysUser.getState());
		
		PageBean<SysUser> sysUserPgBn = sysUserService.selectUser2PageBean(argsMap);

		return JsonUtil.obj2JsonStr(sysUserPgBn);
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
	public String queAllUserByCtn() {
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
			}
			
			resultMap.put("sysUserList", sysUserList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
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
	public String queryUserById(String id) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "根据id查询用戶操作失败!";

		try {
			SysUser sysUser = sysUserService.selectById(id == "" ? 0 : Integer.parseInt(id));

			if (null != sysUser) {
				code = 0;
				msg = "根据id查询用戶操作成功!";
			}

			//用户对象放入map
			resultMap.put("sysUser", sysUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
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
	public String addUser(SysUser sysUser) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			ret = sysUserService.insert(sysUser);

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

		return JsonUtil.obj2JsonStr(result);
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
	public String bathDelUser(String ids) {
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

		return JsonUtil.obj2JsonStr(result);
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
	public String delUser(String id) {
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

		return JsonUtil.obj2JsonStr(result);
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
	public String updateUser(SysUser sysUser) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			ret = sysUserService.update(sysUser);

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

		return JsonUtil.obj2JsonStr(result);
	}

}