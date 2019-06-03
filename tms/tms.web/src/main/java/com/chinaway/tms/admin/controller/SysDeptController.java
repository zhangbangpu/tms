package com.chinaway.tms.admin.controller;

import java.util.ArrayList;
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

import com.chinaway.tms.admin.model.SysDept;
import com.chinaway.tms.admin.service.SysDeptService;
import com.chinaway.tms.util.Constants;
import com.chinaway.tms.utils.MyBeanUtil;
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
//		if (!LoginController.checkLogin(request)) {
//			return new Result(2, "");
//		}
		
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
//		if (!LoginController.checkLogin(request)) {
//			return new Result(2, "");
//		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = Constants.BY_CTN_QUERY_DEPT_FAILED;
		List<SysDept> sysDeptList = null;
		try {
			sysDeptList = sysDeptService.selectDeptByCtn(argsMap);

			if (sysDeptList.size() > 0) {
				code = 0;
				msg = Constants.BY_CTN_QUERY_DEPT_SUCCESS;
			}

		} catch (Exception e) {
			e.getStackTrace();
		}
		Result result = new Result(code, sysDeptList, msg);

		return result;
	}
	
	/**
	 * 根据名称查询部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/getDeptByName")
	@ResponseBody
	public Result getDeptByName(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		List<SysDept> sysDeptList = new ArrayList<SysDept>();
		if(null != argsMap && null != argsMap.get("name")){
			sysDeptList = sysDeptService.selectDeptByName(argsMap);
		}
		
		return new Result(0, sysDeptList);
	}
	
	/**
	 * 根据所有部门信息<br>
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
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = Constants.QUERY_ALL_DEPT_FAILED;
//		Map<String, Object> argsMap = new HashMap<String, Object>();
		int ret = 0;
		try {
			List<SysDept> sysDeptList = sysDeptService.selectDeptByCtn(argsMap);
			if(null != sysDeptList){
				ret = sysDeptList.size();
			}
			
			if (ret > 0) {
				code = 0;
				msg = Constants.QUERY_ALL_DEPT_SUCCESS;
				resultMap.put("sysDeptList", sysDeptList);
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
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		id = String.valueOf(argsMap.get("id"));
		
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
//	public Result addDept(HttpServletRequest request) {
	public Result addDept(HttpServletRequest request, SysDept dept) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
//		SysDept dept = new SysDept();
//		dept = (SysDept)JsonUtil.jsonStr2Obj(sysDept, SysDept.class);
//		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		if(null != argsMap.get("id") && !StringUtils.isEmpty(String.valueOf(argsMap.get("id")))){
//			dept.setId(Integer.parseInt(String.valueOf(argsMap.get("id"))));
//		}
//		if(null != argsMap.get("address")){
//			dept.setAddress(String.valueOf(argsMap.get("address")));
//		}
//		if(null != argsMap.get("contact")){
//			dept.setContact(String.valueOf(argsMap.get("contact")));
//		}
//		dept.setCreatetime(new Date());
//		if(null != argsMap.get("customerid")){
//			dept.setCustomerid(String.valueOf(argsMap.get("customerid")));
//		}
//		if(null != argsMap.get("deptid")){
//			dept.setDeptid(String.valueOf(argsMap.get("deptid")));
//		}else{
//			dept.setDeptid("1");
//		}
//		if(null != argsMap.get("description")){
//			dept.setDescription(String.valueOf(argsMap.get("description")));
//		}
//		if(null != argsMap.get("isenable")){
//			dept.setIsenable(String.valueOf(argsMap.get("isenable")));
//		}
//		if(null != argsMap.get("levels")){
//			dept.setLevels(String.valueOf(argsMap.get("levels")));
//		}
//		if(null != argsMap.get("name")){
//			dept.setName(String.valueOf(argsMap.get("name")));
//		}
//		if(null != argsMap.get("pid")){
//			dept.setPid(Integer.parseInt(String.valueOf(argsMap.get("pid"))));
//		}
//		if(null != argsMap.get("remark")){
//			dept.setRemark(String.valueOf(argsMap.get("remark")));
//		}
//		if(null != argsMap.get("sotid")){
//			dept.setSotid(Integer.parseInt(String.valueOf(argsMap.get("sotid"))));
//		}
//		if(null != argsMap.get("state")){
//			dept.setState(String.valueOf(argsMap.get("state")));
//		}
//		if(null != argsMap.get("tel")){
//			dept.setTel(String.valueOf(argsMap.get("tel")));
//		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = Constants.ADD_OPRATION_FAILED;

		int ret = 0;
		try {
			if (dept.getId() != null) {
				ret = sysDeptService.updateSelective(dept);
			} else {
				int maxId = sysDeptService.selectMaxId();
				dept.setDeptid(String.valueOf(maxId));
				ret = sysDeptService.insert(dept);
			}
			if (ret > 0) {
				code = 0;
				msg = Constants.OPRATION_SUCCESS;
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
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		ids = String.valueOf(argsMap.get("ids"));
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = Constants.BATH_DELETE_OPRATION_FAILED;

		int ret = 0;
		try {
			ret = sysDeptService.deleteByIds(ids);

			if (ret > 0) {
				code = 0;
				msg = Constants.BATH_DELETE_OPRATION_SUCCESS;
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
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		ids = String.valueOf(argsMap.get("ids"));
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = Constants.DELETE_DEPT_OPRATION_FAILED;

		int ret = 0;
		try {
			ret = sysDeptService.deleteById(ids);

			if (ret > 0) {
				code = 0;
				msg = Constants.DELETE_DEPT_OPRATION_SUCCESS;
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
	 * 修改用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateDept")
	@ResponseBody
	public Result updateDept(HttpServletRequest request,SysDept sysDept) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
//		SysDept dept = new SysDept();
//		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		dept.setAddress(String.valueOf(argsMap.get("address")));
//		dept.setContact(String.valueOf(argsMap.get("contact")));
//		dept.setCreatetime(new Date());
//		dept.setCustomerid(String.valueOf(argsMap.get("customerid")));
//		dept.setDeptid(String.valueOf(argsMap.get("deptid")));
//		dept.setDescription(String.valueOf(argsMap.get("description")));
//		dept.setIsenable(String.valueOf(argsMap.get("isenable")));
//		dept.setLevels(String.valueOf(argsMap.get("levels")));
//		dept.setName(String.valueOf(argsMap.get("name")));
//		if(null != String.valueOf(argsMap.get("pid"))){
//			dept.setPid(Integer.parseInt(String.valueOf(argsMap.get("pid"))));
//		}
//		dept.setRemark(String.valueOf(argsMap.get("remark")));
//		if(null != String.valueOf(argsMap.get("sotid"))){
//			dept.setSotid(Integer.parseInt(String.valueOf(argsMap.get("sotid"))));
//		}
//		dept.setState(String.valueOf(argsMap.get("state")));
//		dept.setTel(String.valueOf(argsMap.get("tel")));
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = Constants.UPDATE_DEPT_OPRATION_FAILED;

		int ret = 0;
		try {
			ret = sysDeptService.update(sysDept);

			if (ret > 0) {
				code = 0;
				msg = Constants.UPDATE_DEPT_OPRATION_SUCCESS;
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(code, ret);
	}
	
}