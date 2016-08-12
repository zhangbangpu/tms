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

import com.chinaway.tms.admin.model.SysDept;
import com.chinaway.tms.admin.service.SysDeptService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysDept")
public class SysDeptController {

	@Autowired
	private SysDeptService sysDeptService;

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
		PageBean<SysDept> pageBean = sysDeptService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 根据条件查询部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryDeptByCondition")
	@ResponseBody
	public Result queryDeptByCondition(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		SysDept dept = (SysDept)JsonUtil.jsonStr2Obj(sysDept, SysDept.class);
//		
//		Map<String, Object> argsMap = new HashMap<String, Object>();
//		argsMap.put("pageNo", pageNo);
//		argsMap.put("pageSize", pageSize);
//		if(null != dept){
//			argsMap.put("name", dept.getName());
//			argsMap.put(key, sysDept.);
//			argsMap.put(key, value);
//			argsMap.put(key, value);
//		}
		
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "根据条件查询部门操作失败!";
//		
//		int ret = 0;
//		try {
			PageBean<SysDept> sysDeptPgBn = sysDeptService.selectDept2PageBean(argsMap);
//			if (null != sysDeptPgBn) {
//				ret = sysDeptPgBn.getResult().size();
//			}
//
//			if (ret > 0) {
//				code = 0;
//				msg = "根据条件查询部门操作成功!";
//				resultMap.put("sysDeptList", sysDeptPgBn.getResult());
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, sysDeptPgBn);
	}
	
	/**
	 * 根据说有部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryAllDept")
	@ResponseBody
	public Result queryAllDept(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "查询所有部门操作失败!";
//		Map<String, Object> argsMap = new HashMap<String, Object>();
//		int ret = 0;
//		try {
			List<SysDept> sysDeptList = sysDeptService.selectDeptByCtn(argsMap);
//			if(null != sysDeptList){
//				ret = sysDeptList.size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询所有部门操作成功!";
//				resultMap.put("sysDeptList", sysDeptList);
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, sysDeptList);
	}
	
	/**
	 * 根据条件查询单个部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryOneById")
	@ResponseBody
	public Result queryOneById(HttpServletRequest request, @RequestParam(value="id") String id) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "根据id查询部门操作失败!";
//
//		try {
			SysDept sysDept = sysDeptService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != sysDept) {
//				code = 0;
//				msg = "根据id查询部门操作成功!";
//				//用户对象放入map
//				resultMap.put("sysDept", sysDept);
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, sysDept);
	}
	
	/**
	 * 添加部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/addDept")
	@ResponseBody
	public Result addDept(HttpServletRequest request, @RequestParam(value="sysDept") String  sysDept) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		SysDept dept = (SysDept)JsonUtil.jsonStr2Obj(sysDept, SysDept.class);
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			ret = sysDeptService.insert(dept);
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

		return new Result(0, ret);
	}
	
	/**
	 * 批量删除部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/bathDelDept")
	@ResponseBody
	public Result bathDelDept(HttpServletRequest request, @RequestParam(value="ids") String ids) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "批量删除操作失败!";

		int ret = 0;
		try {
			ret = sysDeptService.deleteByIds(ids);

			if (ret > 0) {
				code = 0;
				msg = "批量删除操作成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, ret);
	}
	
	/**
	 * 删除部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/delDept")
	@ResponseBody
	public Result delDept(HttpServletRequest request, @RequestParam(value="ids") String ids) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除部门操作失败!";

		int ret = 0;
		try {
			ret = sysDeptService.deleteById(ids);

			if (ret > 0) {
				code = 0;
				msg = "删除部门操作成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, ret);
	}
	
	/**
	 * 修改用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateDept")
	@ResponseBody
	public Result updateDept(HttpServletRequest request, @RequestParam(value="sysDept") String sysDept) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		SysDept dept = (SysDept)JsonUtil.jsonStr2Obj(sysDept, SysDept.class);
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改部门操作失败!";

		int ret = 0;
		try {
			ret = sysDeptService.update(dept);

			if (ret > 0) {
				code = 0;
				msg = "修改部门操作成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, ret);
	}
	
}