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
import com.chinaway.tms.basic.model.Waybill;
import com.chinaway.tms.basic.service.WaybillService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/tckNumRvwed")
public class TckNumRvwedManagerController {
	
	@Autowired
	private WaybillService waybillService;
	
	/**
	 * 根据条件查询所有车次审核信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/selectAllTckNumRvwedByCtn")
	@ResponseBody
	public Result selectAllTckNumRvwedByCtn(HttpServletRequest request) {
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "查询所有站点操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
//		int ret = 0;
//		try {
			List<Waybill> waybillList = waybillService.selectAllTckNumRvwedByCtn(argsMap);
//			if(null != waybillList){
//				ret = waybillList.size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询所有站点操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, waybillList);
	}
	
	/**
	 * 根据条件查询车次审核信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result selectTckNumRvwed2PageBean(HttpServletRequest request) {
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		argsMap.put("state", "0");
		PageBean<Waybill> pageBean = waybillService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 根据条件查询单个车次审核信息<br>
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

		Waybill waybill = null;
//		try {
			waybill = waybillService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != tckNumRvwed) {
//				code = 0;
//				msg = "根据id查询站点操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		Result result = new Result(code, tckNumRvwed, msg);

		return new Result(0, waybill);
	}
	
	/**
	 * 上传excel站点信息<br>
	 * 返回站点的json串
	 * @return
	 */
	@RequestMapping(value = "/export")
	@ResponseBody
	public Result export(HttpServletRequest request, @RequestParam("ids") String ids) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "下载需要审核的运单失败!";

		List<Waybill> waybillList = null;
		try {
			waybillList = waybillService.selectByIds(ids);
			
			if (null != waybillList && waybillList.size() > 0) {
				code = 0;
				msg = "下载需要审核的运单成功!";
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
	 * 添加车次审核信息<br>
	 * 返回站点的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addTckNumRvwed")
	@ResponseBody
	public Result addTckNumRvwed(HttpServletRequest request, Waybill waybill) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作站点失败!";

		int ret = 0;
		try {
			
			if (waybill.getId() != null) {
				ret = waybillService.updateSelective(waybill);
			}else{
				waybill.setCreatetime(new Date());
				ret = waybillService.insert(waybill);
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
	 * 删除车次审核信息<br>
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
			ret = waybillService.deleteById(ids);

			if (ret > 0) {
				code = 0;
				msg = "批量删除操作成功!";
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
	 * 修改车次审核信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateTckNumRvwed")
	@ResponseBody
	public Result updateTckNumRvwed(HttpServletRequest request, Waybill waybill) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "车次审核失败!";

		int ret = 0;
		try {
			ret = waybillService.updateWaybill(waybill);

			if (ret > 0) {
				code = 0;
				msg = "车次审核成功!";
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return result;
	}
	
}