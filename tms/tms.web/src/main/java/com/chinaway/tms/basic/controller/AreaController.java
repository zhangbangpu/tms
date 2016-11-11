package com.chinaway.tms.basic.controller;

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
import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysDeptService;
import com.chinaway.tms.admin.service.SysUserService;
import com.chinaway.tms.basic.model.Area;
import com.chinaway.tms.basic.model.AreaSite;
import com.chinaway.tms.basic.service.AreaService;
import com.chinaway.tms.basic.service.AreaSiteService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

/**
 * 区域区域站点Controller
 * @author shu
 *
 */
@Controller
@RequestMapping(value = "/area")
public class AreaController {
	
	@Autowired
	private AreaService areaService;
	@Autowired
	private AreaSiteService areaSiteService;
//	@Autowired
//	private SiteService siteService;
	
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysDeptService sysDeptService;
	
	/**
	 * 根据条件查询区域站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result selectArea2PageBean(HttpServletRequest request) {
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<Area> pageBean = areaService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 根据条件查询单个区域站点信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryOneById")
	@ResponseBody
	public Result queryOneById(HttpServletRequest request) {
//		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		String id = String.valueOf(argsMap.get("id"));
		String id = request.getParameter("id");
		int code = 1;
		String msg = "根据id查询部门操作失败!";

		Area area = null;
		try {
			area = areaService.selectById(id == "" ? 0 : Integer.parseInt(id));
			Map<String, Object> argsMap = new HashMap<>();
			argsMap.put("areacode", area.getCode());
			List<AreaSite> areaSiteList = areaSiteService.selectAll4Page(argsMap);
			String sitecodes = "";
			String sitenames = "";
			for (AreaSite areaSite : areaSiteList) {
				sitecodes = sitecodes + areaSite.getSitecode() + ",";
				sitenames = sitenames + areaSite.getSitename() + ",";
			}
			sitecodes = sitecodes.substring(0, sitecodes.length() -1);
			sitenames = sitenames.substring(0, sitenames.length() -1);
			area.setSitecodes(sitecodes);
			area.setSitenames(sitenames);
			
			//设置下拉框的name值
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("deptid", area.getDeptname());
			List<SysDept> sysDeptList = sysDeptService.selectDeptByCtn(map);
			if (null != sysDeptList && sysDeptList.size() > 0) {
				area.setDeptidname(sysDeptList.get(0).getName());
			}
			
			Map<String, Object> comMap = new HashMap<String, Object>();
			comMap.put("id", area.getWlcompany());
			List<SysUser> sysUserList = sysUserService.queAllUserByCtn(comMap);
			if (sysUserList.size() > 0) {
				area.setWlcompanyname(sysUserList.get(0).getName());
			}
			
			if (null != area) {
				code = 0;
				msg = "根据id查询区域站点操作成功!";
			}

		} catch (Exception e) {
			e.getStackTrace();
		}

		Result result = new Result(code, area, msg);

		return result;
	}
	
	/**
	 * 添加区域站点信息<br>
	 * 返回区域站点的json串
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/addArea")
	@ResponseBody
	public Result addArea(HttpServletRequest request, Area Area) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "操作区域站点失败!";

		int ret = 0;
		try {
			
			if (Area.getId() != null) {
				ret = areaService.updatetAreaAndItem(Area);
			}else{
				ret = areaService.insertAreaAndItem(Area);
			}
			
			if (ret > 0) {
				code = 0;
				msg = "操作区域站点成功!";
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
			ret = areaService.deleteById(ids);

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
	
//	/**
//	 * 修改区域站点信息<br>
//	 * 返回用户的json串
//	 * 
//	 * @param userInfo
//	 * @return
//	 */
//	@RequestMapping(value = "/updateArea")
//	@ResponseBody
//	public Result updateArea(HttpServletRequest request, Area Area) {
//		
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "修改区域站点失败!";
//
//		int ret = 0;
//		try {
//			ret = areaService.update(Area);
//
//			if (ret > 0) {
//				code = 0;
//				msg = "修改区域站点成功!";
//			}
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);
//
//		return result;
//	}
	
}