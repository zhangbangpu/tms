package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysLog;
import com.chinaway.tms.admin.service.SysLogService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

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
	public String addLog(SysLog sysLog) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			sysLogService.insert(sysLog);
			if (ret > 0) {
				code = 0;
				msg = "添加操作成功!";
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