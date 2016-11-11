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
import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.basic.service.SiteService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/site")
public class SiteManagerController {
	
	@Autowired
	private SiteService siteService;
	
	/**
	 * 根据条件查询所有站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/selectAllSiteByCtn")
	@ResponseBody
	public Result selectAllSiteByCtn(HttpServletRequest request) {
		int code = 1;
		String msg = "查询所有站点操作失败!";
//		Map<String, Object> argsMap = new HashMap<String, Object>();
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		int ret = 0;
		List<Site> sitetList = null;
		try {
			sitetList = siteService.selectAllSiteByCtn(argsMap);
			if(null != sitetList){
				ret = sitetList.size();
			}
			
			if (ret > 0) {
				code = 0;
				msg = "查询所有站点操作成功!";
			}

		} catch (Exception e) {
			e.getStackTrace();
		}
		Result result = new Result(code, sitetList, msg);

		return result;
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
	public Result selectSite2PageBean(HttpServletRequest request) {
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<Site> pageBean = siteService.select2PageBean(argsMap);
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

		Site site = null;
//		try {
			site = siteService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != site) {
//				code = 0;
//				msg = "根据id查询站点操作成功!";
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		Result result = new Result(code, site, msg);

		return new Result(0, site);
	}
	
	/**
	 * 添加站点信息<br>
	 * 返回站点的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addSite")
	@ResponseBody
	public Result addSite(HttpServletRequest request, Site site) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作站点失败!";

		int ret = 0;
		try {
			
			site.setUpdatetime(new Date());
			if (site.getId() != null) {
				ret = siteService.updateSelective(site);
			}else{
				site.setCreatetime(new Date());
				ret = siteService.insert(site);
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
	 * 上传excel站点信息<br>
	 * 返回站点的json串
	 * @return
	 */
	@RequestMapping(value = "/importSite")
	@ResponseBody
	public Result importSite(HttpServletRequest request, Site site) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作站点失败!";

		int ret = 0;
		try {
			
			site.setUpdatetime(new Date());
			
//			ret = siteService.insert(site);
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
	 * 上传excel站点信息<br>
	 * 返回站点的json串
	 * @return
	 */
	@RequestMapping(value = "/export")
	@ResponseBody
	public Result export(HttpServletRequest request, @RequestParam("ids") String ids) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "下载站点失败!";

		List<Site> siteList = null;
		try {
			siteList = siteService.selectByIds(ids);
			
			if (null != siteList && siteList.size() > 0) {
				code = 0;
				msg = "下载站点成功!";
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
			ret = siteService.deleteById(ids);

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
	 * 修改站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateSite")
	@ResponseBody
	public Result updateSite(HttpServletRequest request, Site site) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改站点失败!";

		int ret = 0;
		try {
			ret = siteService.update(site);

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

		return new Result(0, ret);
	}
	
}