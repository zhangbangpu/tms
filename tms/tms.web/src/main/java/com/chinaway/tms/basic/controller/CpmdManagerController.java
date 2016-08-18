package com.chinaway.tms.basic.controller;

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

import com.chinaway.tms.admin.controller.LoginController;
import com.chinaway.tms.basic.model.Cpmd;
import com.chinaway.tms.basic.service.CpmdService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/cpmd")
public class CpmdManagerController {
	
	@Autowired
	private CpmdService cpmdService;
	
	/**
	 * 根据条件查询所有站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/selectAllCpmdByCtn")
	@ResponseBody
	public Result selectAllCpmdByCtn(HttpServletRequest request) {
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "查询所有站点操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
//		int ret = 0;
//		try {
			List<Cpmd> cpmdtList = cpmdService.selectAllCpmdByCtn(argsMap);
//			if(null != cpmdtList){
//				ret = cpmdtList.size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询所有站点操作成功!";
//				resultMap.put("cpmdtList", cpmdtList);
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, cpmdtList);
	}
	
	/**
	 * 根据条件查询站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result selectCpmd2PageBean(HttpServletRequest request) {
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<Cpmd> pageBean = cpmdService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 根据条件查询单个站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryOneById")
	@ResponseBody
	public Result queryOneById(HttpServletRequest request) {
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		String id = String.valueOf(argsMap.get("id"));
//		int code = 1;
//		String msg = "根据id查询部门操作失败!";

		Cpmd cpmd = null;
//		try {
			cpmd = cpmdService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != cpmd) {
//				code = 0;
//				msg = "根据id查询站点操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}

//		Result result = new Result(code, cpmd, msg);

		return new Result(0, cpmd);
	}
	
	/**
	 * 添加站点信息<br>
	 * 返回站点的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addCpmd")
	@ResponseBody
	public Result addCpmd(HttpServletRequest request, Cpmd cpmd) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作站点失败!";

		int ret = 0;
		try {
			
			cpmd.setUpdatetime(new Date());
			if (cpmd.getId() != null) {
				ret = cpmdService.updateSelective(cpmd);
			}else{
				ret = cpmdService.insert(cpmd);
			}
			if (ret > 0) {
				code = 0;
				msg = "操作站点成功!";
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
	@RequestMapping(value = "/deleteById")
	@ResponseBody
	public Result deleteById(HttpServletRequest request, @RequestParam("ids") String ids) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "批量删除操作失败!";

		int ret = 0;
		try {
			ret = cpmdService.deleteById(ids);

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

		return new Result(0, code);
	}
	
	/**
	 * 修改站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateCpmd")
	@ResponseBody
	public Result updateCpmd(HttpServletRequest request, Cpmd cpmd) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改站点失败!";

		int ret = 0;
		try {
			ret = cpmdService.update(cpmd);

			if (ret > 0) {
				code = 0;
				msg = "修改站点成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, code);
	}
	
}