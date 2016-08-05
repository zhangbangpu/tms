package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysDept;
import com.chinaway.tms.admin.service.SysDeptService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SysDeptController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SysDeptController.class);
	
	@Autowired
	private SysDeptService sysDeptService;

	/**
	 * 添加部门信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/addDept")
	@ResponseBody
	// http://localhost/tms/ws/addDept?userInfo=
	public String addDept(@RequestParam("userInfo") String userInfo) {
		
		LOGGER.info("传入的参数(userInfo):" + userInfo);
		
		SysDept sysDept = JsonUtil.jsonStr2Obj(userInfo, SysDept.class);
		Map<String, String> argsMap = new HashMap<String, String>();
		try {
			sysDeptService.insert(sysDept);
			argsMap.put("status", "true");
			argsMap.put("msg", "add dept success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add dept failed!");
		}

		String ret = JsonUtil.obj2JsonStr(argsMap);
		
		LOGGER.info("addUser传出的参数:" + ret);

		return ret;
	}

}