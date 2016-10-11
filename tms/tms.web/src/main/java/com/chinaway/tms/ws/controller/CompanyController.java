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

import com.chinaway.tms.admin.model.SysUser;
import com.chinaway.tms.admin.service.SysUserService;
import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

/**
 * 承运商接口（共用了SysUser）
 * ）
 * @author shu
 *
 */
@Controller
public class CompanyController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	private SysUserService sysUserService;
	
	/**
	 * 添加仓配的用户，主要是承运商
	 * @param companyInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/addCompany")
	@ResponseBody
	public Result addCompany(@RequestParam("companyInfo") String companyInfo){
    	
    	LOGGER.info("传入的参数(companyInfo):" + companyInfo);
    	int code = 1;
		String msg = "新增信息失败";
    	SysUser sysUser = JsonUtil.jsonStr2Obj(companyInfo, SysUser.class);
    	try {
    		int count = sysUserService.insert(sysUser);
    		if(count > 0){
    			code = 0;
    			msg = "新增信息成功";
    		}
		} catch (Exception e) {
			msg = "新增信息出现异常";
		}
    	
    	Result result = new Result(code, msg);
		LOGGER.info("addCompany传出的参数:" + result);

		return result;
	}
	
	/**
	 * 更新仓配的用户，主要是承运商
	 * @param companyInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/updateCompany")
	@ResponseBody
	public Result updateCompany(@RequestParam("companyInfo") String companyInfo){
		
		LOGGER.info("传入的参数(companyInfo):" + companyInfo);
		int code = 1;
		String msg = "更新信息失败";
		SysUser sysUser = JsonUtil.jsonStr2Obj(companyInfo, SysUser.class);
		try {
			int count = sysUserService.update(sysUser);
			if(count > 0){
				code = 0;
				msg = "更新信息成功";
			}
		} catch (Exception e) {
			msg = "更新信息出现异常";
		}
		
		Result result = new Result(code, msg);
		LOGGER.info("updateCompany传出的参数:" + result);
		
		return result;
	}
	
}
