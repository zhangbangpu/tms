package com.chinaway.tms.ws.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.utils.json.JsonUtil;

/**
 * 承运商
 * @author shu
 *
 */
@Controller
public class CompanyController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

	/**
	 * 添加仓配的用户，主要是承运商
	 * @param companyInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/addCompany")
	@ResponseBody
	public String addCompany(@RequestParam("companyInfo") String companyInfo){
    	
    	LOGGER.info("传入的参数(companyInfo):" + companyInfo);
    	
//    	Site site = JsonUtil.jsonStr2Obj(companyInfo, Site.class);
    	Map<String, String> argsMap = new HashMap<String, String>();
//    	try {
//    		siteService.insert(site);
//    		argsMap.put("status", "true");
//			argsMap.put("msg", "add Site success!");
//		} catch (Exception e) {
//			argsMap.put("status", "false");
//			argsMap.put("msg", "add Site failed!");
//		}
    	
		String ret = JsonUtil.obj2JsonStr(argsMap);
		
		LOGGER.info("addSite传出的参数:" + ret);

		return ret;
	}
	
}
