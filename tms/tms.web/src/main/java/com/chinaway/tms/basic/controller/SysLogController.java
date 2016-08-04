package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chinaway.tms.basic.model.SysLog;
import com.chinaway.tms.basic.service.SysLogService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
public class SysLogController {

	@Autowired
	private SysLogService sysLogService;

	/**
	 * 添加日志信息<br>
	 * 返回用户的json串
	 * 
	 * @param logInfo
	 * @return
	 */
	@RequestMapping(value = "/ws/addLog")
	@ResponseBody
	// http://localhost/tms/ws/addLog?logInfo=
	public String addLog(@RequestParam("logInfo") String logInfo) {
		SysLog sysLog = JsonUtil.jsonStr2Obj(logInfo, SysLog.class);
		Map<String, String> argsMap = new HashMap<String, String>();
		try {
			sysLogService.insert(sysLog);
			argsMap.put("status", "true");
			argsMap.put("msg", "add Log success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add Log failed!");
		}

		return JsonUtil.obj2JsonStr(argsMap);
	}

}