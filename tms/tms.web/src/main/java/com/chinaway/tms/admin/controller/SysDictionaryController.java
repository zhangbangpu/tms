package com.chinaway.tms.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.admin.model.SysDictionary;
import com.chinaway.tms.admin.service.SysDictionaryService;
import com.chinaway.tms.utils.json.JsonUtil;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/sysDictionary")
public class SysDictionaryController {

	@Autowired
	private SysDictionaryService sysDictionaryService;

	/**
	 * 添加信息<br>
	 * 返回用户的json串
	 * 
	 * @param dictionaryInfo
	 * @return
	 */
	@RequestMapping(value = "/addDictionary")
	@ResponseBody
	public String addDictionary(SysDictionary sysDictionary) {
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "添加操作失败!";

		int ret = 0;
		try {
			sysDictionaryService.insert(sysDictionary);
			if (ret > 0) {
				code = 0;
				msg = "添加操作成功!";
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