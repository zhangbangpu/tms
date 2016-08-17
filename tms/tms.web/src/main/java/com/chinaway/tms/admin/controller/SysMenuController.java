package com.chinaway.tms.admin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysMenu;
import com.chinaway.tms.admin.service.SysMenuService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysMenu")
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;

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
		PageBean<SysMenu> pageBean = sysMenuService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 用户登录<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ajaxLoginGetMenuList")
	@ResponseBody
	public String AjaxLoginGetMenuList(HttpServletRequest request) {
//		if (!LoginController.checkLogin(request)) {
//			return new Result(2, "");
//		}
		
		//连表查询角色信息
		List<SysMenu> sysMenuList = (List<SysMenu>)request.getSession().getAttribute("sysMenuList");

		String ret = JsonUtil.obj2JsonStr(sysMenuList);
		return ret;
	}
	
	/**
	 * 根据所有菜单信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/queryAllMenu")
	@ResponseBody
	public Result queryAllMenu(HttpServletRequest request) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
//		SysMenu menu = (SysMenu)JsonUtil.jsonStr2Obj(sysMenu, SysMenu.class);
//		
//		Map<String, Object> argsMap = new HashMap<String, Object>();
//		argsMap.put("pageNo", pageNo);
//		argsMap.put("pageSize", pageSize);
//		if(null != menu){
//			argsMap.put("name", menu.getName());
//			argsMap.put("levels", menu.getLevels());
//			argsMap.put("menutype", menu.getMenutype());
//			argsMap.put("pid", menu.getPid());
//			argsMap.put("requesturl", menu.getRequesturl());
//			argsMap.put("sotid", menu.getSotid());
//		}
		
//		Map<String, Object> resultMap = new HashMap<>();
//		int code = 1;
//		String msg = "查询所有菜单操作失败!";
//		int ret = 0;
//		try {
			PageBean<SysMenu> sysMenuPgBn = sysMenuService.selectMenu2PageBean(argsMap);
//			if(null != sysMenuPgBn && null != sysMenuPgBn.getResult()){
//				ret = sysMenuPgBn.getResult().size();
//			}
//			
//			if (ret > 0) {
//				code = 0;
//				msg = "查询所有菜单操作成功!";
//				resultMap.put("sysMenuList", sysMenuPgBn.getResult());
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, sysMenuPgBn);
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
//		String msg = "根据id查询菜单操作失败!";

//		try {
			SysMenu sysMenu = sysMenuService.selectById(id == "" ? 0 : Integer.parseInt(id));

//			if (null != sysMenu) {
//				code = 0;
//				msg = "根据id查询菜单操作成功!";
//				resultMap.put("sysMenu", sysMenu);
//			}
//
//		} catch (Exception e) {
//			e.getStackTrace();
//		}
//
//		resultMap.put("code", code);
//		resultMap.put("msg", msg);
//		Result result = new Result(code, resultMap, msg);

		return new Result(0, sysMenu);
	}
	
	/**
	 * 添加菜单信息<br>
	 * 返回用户的json串
	 * 
	 * @param menuInfo
	 * @return
	 */
	@RequestMapping(value = "/addMenu")
	@ResponseBody
	public Result addMenu(HttpServletRequest request) {
//		public Result addMenu(HttpServletRequest request, @RequestParam(value="sysMenu") String sysMenu) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		SysMenu menu = new SysMenu();
//		menu = (SysMenu)JsonUtil.jsonStr2Obj(sysMenu, SysMenu.class);
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		if (null != argsMap.get("id") && !StringUtils.isEmpty(String.valueOf(argsMap.get("id")))) {
			menu.setId(Integer.parseInt(String.valueOf(argsMap.get("id"))));
		}
		if(null != argsMap.get("img")){
			menu.setImg(String.valueOf(argsMap.get("img")));
		}
		if(null != argsMap.get("levels")){
			menu.setLevels(String.valueOf(argsMap.get("levels")));
		}
		if(null != argsMap.get("menutype")){
			menu.setMenutype(String.valueOf(argsMap.get("menutype")));
		}
		if(null != argsMap.get("name")){
			menu.setName(String.valueOf(argsMap.get("name")));
		}
		if(null != argsMap.get("pid")){
			menu.setPid(Integer.parseInt(String.valueOf(argsMap.get("pid"))));
		}else{
			menu.setPid(0);
		}
		if(null != argsMap.get("requesturl")){
			menu.setRequesturl(String.valueOf(argsMap.get("requesturl")));
		}
		if(null != argsMap.get("sotid")){
			menu.setSotid(Integer.parseInt(String.valueOf(argsMap.get("sotid"))));
		}
		if(null != argsMap.get("subsystem")){
			menu.setSubsystem(String.valueOf(argsMap.get("subsystem")));
		}
		if(null != argsMap.get("target")){
			menu.setTarget(String.valueOf(argsMap.get("target")));
		}
		if(null != argsMap.get("title")){
			menu.setTitle(String.valueOf(argsMap.get("title")));
		}
		if(null != argsMap.get("type")){
			menu.setType(String.valueOf(argsMap.get("type")));
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加菜单操作失败!";

		int ret = 0;
		try {
			menu.setCreatetime(new Date());
			if (menu.getId() != null) {
				ret = sysMenuService.updateSelective(menu);
			} else {
				ret = sysMenuService.insert(menu);
			}

			if (ret > 0) {
				code = 0;
				msg = "添加菜单操作成功!";
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
	 * 批量删除用户信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/bathDelMenu")
	@ResponseBody
	public Result bathDelMenu(HttpServletRequest request, @RequestParam(value="ids") String ids) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		ids = String.valueOf(argsMap.get("ids"));
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "批量删除操作失败!";

		int ret = 0;
		try {
			ret = sysMenuService.deleteByIds(ids);

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
	 * 删除菜单信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/delMenu")
	@ResponseBody
	public Result delMenu(HttpServletRequest request, @RequestParam(value="ids") String ids) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		ids = String.valueOf(argsMap.get("ids"));
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "删除操作失败!";

		int ret = 0;
		try {
			ret = sysMenuService.deleteById(ids);

			if (ret > 0) {
				code = 0;
				msg = "删除操作成功!";
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
	 * 修改菜单信息<br>
	 * 返回用户的json串
	 * 
	 * @param userInfo
	 * @return
	 */
	@RequestMapping(value = "/updateMenu")
	@ResponseBody
	public Result updateMenu(HttpServletRequest request, @RequestParam(value="sysMenu") String sysMenu) {
		if (!LoginController.checkLogin(request)) {
			return new Result(2, "");
		}
		
		SysMenu menu = new SysMenu();
//		menu = (SysMenu)JsonUtil.jsonStr2Obj(sysMenu, SysMenu.class);
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		menu.setCreatetime(new Date());
		menu.setImg(String.valueOf(argsMap.get("img")));
		menu.setLevels(String.valueOf(argsMap.get("levels")));
		menu.setMenutype(String.valueOf(argsMap.get("menutype")));
		menu.setName(String.valueOf(argsMap.get("name")));
		if(null != String.valueOf(argsMap.get("pid"))){
			menu.setPid(Integer.parseInt(String.valueOf(argsMap.get("pid"))));
		}
		menu.setRequesturl(String.valueOf(argsMap.get("requesturl")));
		if(null != String.valueOf(argsMap.get("sotid"))){
			menu.setSotid(Integer.parseInt(String.valueOf(argsMap.get("sotid"))));
		}
		menu.setSubsystem(String.valueOf(argsMap.get("subsystem")));
		menu.setTarget(String.valueOf(argsMap.get("target")));
		menu.setTitle(String.valueOf(argsMap.get("title")));
		menu.setType(String.valueOf(argsMap.get("type")));
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "修改菜单操作失败!";

		int ret = 0;
		try {
			ret = sysMenuService.update(menu);

			if (ret > 0) {
				code = 0;
				msg = "修改菜单操作成功!";
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