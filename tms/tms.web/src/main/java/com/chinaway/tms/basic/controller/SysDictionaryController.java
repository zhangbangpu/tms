package com.chinaway.tms.basic.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.chinaway.tms.basic.model.SysDictionary;
import com.chinaway.tms.basic.service.SysDictionaryService;
import com.chinaway.tms.utils.json.JsonUtil;

@Controller
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
	@RequestMapping(value = "/ws/addDictionary")
	@ResponseBody
	// http://localhost/tms/ws/addDictionary?dictionaryInfo=
	public String addDictionary(@RequestParam("dictionaryInfo") String dictionaryInfo) {
		SysDictionary sysDictionary = JsonUtil.jsonStr2Obj(dictionaryInfo, SysDictionary.class);
		Map<String, String> argsMap = new HashMap<String, String>();
		try {
			sysDictionaryService.insert(sysDictionary);
			argsMap.put("status", "true");
			argsMap.put("msg", "add dictionary success!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			argsMap.put("status", "false");
			argsMap.put("msg", "add dictionary failed!");
		}

		return JsonUtil.obj2JsonStr(argsMap);
	}

}