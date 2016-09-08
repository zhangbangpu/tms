package com.chinaway.tms.otd.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.basic.model.Site;
import com.chinaway.tms.otd.model.Otd;
import com.chinaway.tms.otd.service.OtdService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.page.PageBean;
import com.chinaway.tms.vo.Result;

@Controller
@RequestMapping(value = "/otd")
public class OtdController {

	@Autowired
	private OtdService otdService;
	
	/**
	 * 根据条件查询otd信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result selectSite2PageBean(HttpServletRequest request) {
		
		Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
		PageBean<Otd> pageBean = otdService.select2PageBean(argsMap);
		//String resultJson = JsonUtil.obj2JsonStr(new Result(0, pageBean));
		//return JsonUtil.obj2JsonStr(resultJson);
		return new Result(0, pageBean);
	}
	
	/**
	 * 修改 <br>
	 * 由于比较特殊,
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Result update(HttpServletRequest request) {
		
		int code = 1;
		String msg = "修改otd失败!";

		try {
//			int ret = otdService.update(site);
//			if (ret > 0) {
//				code = 0;
//				msg = "修改otd成功!";
//			}
		} catch (Exception e) {
			e.getStackTrace();
			msg = "修改otd出现异常!";
		}
		return new Result(0, msg);
	}
	
}
