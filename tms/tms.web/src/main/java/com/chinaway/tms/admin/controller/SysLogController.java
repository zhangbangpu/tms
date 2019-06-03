package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysLog;
import com.chinaway.tms.admin.service.SysLogService;
import com.chinaway.tms.util.Constants;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysLog")
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
	@RequestMapping(value = "/addLog")
	@ResponseBody
	public String addLog(SysLog sysLog) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = Constants.ADD_OPRATION_FAILED;

		int ret = 0;
		try {
			sysLogService.insert(sysLog);
			if (ret > 0) {
				code = 0;
				msg = Constants.ADD_OPRATION_SUCCESS;
			}

		} catch (Exception e) {
			e.getStackTrace();
		}

		resultMap.put("code", code);
		resultMap.put("msg", msg);
		Result result = new Result(code, resultMap, msg);

		return JsonUtil.obj2JsonStr(result);
	}

}