package com.chinaway.tms.otd.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chinaway.tms.otd.model.Otd;
import com.chinaway.tms.otd.service.OtdService;
import com.chinaway.tms.utils.MyBeanUtil;
import com.chinaway.tms.utils.lang.BigDecimalUtil;
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
	@RequestMapping(value = "/queryAll")
	@ResponseBody
	public Result queryAll(HttpServletRequest request) {
		int code = 1;
		String msg = "查询失败";
		List<Otd> list = null;
		
		try {
//			Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
			Map<String, Object> argsMap = new HashMap<>();
			list = otdService.selectAll4Page(argsMap);
			code = 0;
			msg = "查询成功";
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = "查询出现异常";
		}
		return new Result(code, list, msg);
	}
	
	/**
	 * 根据条件查询otd信息<br>
	 * 返回用户的json串
	 * 
	 * @param deptInfo
	 * @return
	 */
	@RequestMapping(value = "/page")
	@ResponseBody
	public Result page(HttpServletRequest request) {
		
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
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Map<String, Object> argsMap = MyBeanUtil.getParameterMap(request);
			Otd otd = null;
			for (int i = 1; i < 7; i++) {
				int id = Integer.parseInt(argsMap.get("id"+(i)).toString());
				String name = argsMap.get("name"+(i)).toString();
				String status = argsMap.get("status"+(i)).toString();
				int hours = Integer.parseInt(argsMap.get("hours"+(i)).toString());
				int minute = Integer.parseInt(argsMap.get("minute"+(i)).toString());
				int seconds = Integer.parseInt(argsMap.get("seconds"+(i)).toString());
				//使用
				BigDecimal num1 = BigDecimalUtil.add(hours * 60 +"", minute +"");
				BigDecimal num2 = BigDecimalUtil.divide(seconds +"", "60", 2);
				double tims = BigDecimalUtil.add(num1.toString(), num2.toString()).doubleValue();
//				double tims = hours * 60 + minute + seconds/60.0;
				otd = new Otd();
				otd.setId(id);
				otd.setName(name);
				otd.setStatus(status);
				otd.setHours(hours);
				otd.setMinute(minute);
				otd.setSeconds(seconds);
				otd.setTims(tims);
				
				otdService.updateSelective(otd);
			}
			code = 0;
			msg = "修改otd成功!";
			
			//Todo:更改订单状态和时效
			
		} catch (Exception e) {
			e.getStackTrace();
			msg = "修改otd出现异常!";
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		return new Result(code, resultMap, msg);
	}
	
}
