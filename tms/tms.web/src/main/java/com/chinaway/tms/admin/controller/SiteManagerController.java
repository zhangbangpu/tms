package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.basic.service.SiteService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/site")
public class SiteManagerController {
	
	@Autowired
	private SiteService siteService;
	
	/**
	 * 添加站点信息<br>
	 * 返回站点的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addSite")
	@ResponseBody
	public String addSite(Site site) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加站点失败!";

		int ret = 0;
		try {
			siteService.insert(site);
			if (ret > 0) {
				code = 0;
				msg = "添加站点成功!";
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
	 * 删除部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/deleteById")
	@ResponseBody
	public String deleteById(@RequestParam("ids") String ids) {
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
			System.out.println(e.getMessage());
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}
	
	/**
	 * 修改站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/delDept")
	@ResponseBody
	public String updateDept(Site site) {
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
			System.out.println(e.getMessage());
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}
	
	/**
	 * 根据条件查询站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/querySiteByCondition")
	@ResponseBody
	public String querySiteByCondition(Site site) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "根据条件查询站点操作失败!";

		Map<String, Object> argsMap = new HashMap<String, Object>();
		argsMap.put("name", site.getName());
//		argsMap.put(key, sysDept.);
//		argsMap.put(key, value);
//		argsMap.put(key, value);
		int ret = 0;
		try {
			PageBean<Site> sitePgBn = siteService.queSiteByCtnPgBn(argsMap);
			if (null != sitePgBn) {
				ret = sitePgBn.getResult().size();
			}

			if (ret > 0) {
				code = 0;
				msg = "根据条件查询站点操作成功!";
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
	 * 根据条件查询单个站点信息<br>
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
		String msg = "根据id查询部门操作失败!";

		try {
			Site site = siteService.selectById(id == "" ? 0 : Integer.parseInt(id));

			if (null != site) {
				code = 0;
				msg = "根据id查询站点操作成功!";
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
	 * 根据条件查询所有站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryAllSite")
	@ResponseBody
	public String queryAllSite() {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "查询所有站点操作失败!";
		Map<String, Object> argsMap = new HashMap<String, Object>();
		int ret = 0;
		try {
			List<Site> sitetList = siteService.queAllSiteByCtn(argsMap);
			if(null != sitetList){
				ret = sitetList.size();
			}
			
			if (ret > 0) {
				code = 0;
				msg = "查询所有站点操作成功!";
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