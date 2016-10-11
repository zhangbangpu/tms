package com.chinaway.tms.ws.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.basic.service.SiteService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

@Controller
@Deprecated
public class SiteController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SiteController.class);
	
	@Autowired
	private SiteService siteService;
	
	/**
	 * 添加站点信息<br>
	 * 返回用户的json串
	 * @param username
	 * @param password
	 * @return
	 */
    @RequestMapping(value = "/ws/addSite")
	@ResponseBody
	public Result addSite(@RequestParam("siteInfo") String siteInfo){
    	
    	LOGGER.info("传入的参数(siteInfo):" + siteInfo);
    	int code = 1;
		String msg = "";
		if(siteInfo != ""){
			try {
				Site site = JsonUtil.jsonStr2Obj(siteInfo, Site.class);
				int count = siteService.insert(site);
				if(count > 0){
					code = 0;
					msg = "新增站点成功";
				}
			} catch (Exception e) {
				e.printStackTrace();
				msg = "新增站点出异常";
			}
		}else{
			code = 2;
			msg = "参数不能为空";
		}
		
		Result result = new Result(code,msg);
		LOGGER.info("addSite传出的参数:" + result);

		return result;
	}
    
}