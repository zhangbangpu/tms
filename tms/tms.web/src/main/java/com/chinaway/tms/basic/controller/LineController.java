package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.Line;
import com.chinaway.tms.basic.service.LineService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class LineController {
	
	@Autowired
	private LineService lineService;
	
	/**
	 * 根据账号和密码查询信息,即判断用户是否存在<br>
	 * 返回用户的json串
	 * @param username
	 * @param password
	 * @return
	 */
    @RequestMapping(value = "/ws/addLine")
	@ResponseBody
	//http://localhost/tms/ws/addLine?lineInfo=
	public String addLine(@RequestParam("lineInfo") String lineInfo){
    	Line line = JsonUtil.jsonStr2Obj(lineInfo, Line.class);
    	Map<String, String> argsMap = new HashMap<String, String>();
    	try {
    		lineService.insert(line);
    		argsMap.put("status", "true");
			argsMap.put("msg", "add Line success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add Line failed!");
		}
    	
    	return JsonUtil.obj2JsonStr(argsMap);
	}
    
}