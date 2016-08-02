package com.chinaway.tms.base.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.basic.service.SiteService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SiteController {
	
	@Autowired
	private SiteService siteService;
	
	/**
	 * 根据账号和密码查询信息,即判断用户是否存在<br>
	 * 返回用户的json串
	 * @param username
	 * @param password
	 * @return
	 */
    @RequestMapping(value = "/ws/addSite")
	@ResponseBody
	public String addSite(@RequestParam("siteInfo") String siteInfo){
    	Site site = JsonUtil.jsonStr2Obj(siteInfo, Site.class);
    	Map<String, String> argsMap = new HashMap<String, String>();
    	try {
    		siteService.insert(site);
//			ret = siteService.addSite(site);
    		argsMap.put("status", "true");
			argsMap.put("msg", "add Site success!");
		} catch (Exception e) {
			argsMap.put("status", "false");
			argsMap.put("msg", "add Site failed!");
		}
    	
    	return JsonUtil.obj2JsonStr(argsMap);
	}
    
}