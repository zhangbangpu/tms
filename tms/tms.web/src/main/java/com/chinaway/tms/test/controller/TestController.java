package com.chinaway.tms.test.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.chinaway.tms.admin.service.SysUserService;
import com.chinaway.tms.utils.excel.ExcelInfo;
import com.chinaway.tms.utils.excel.ExcelUtil;

@Controller
public class TestController {

	@Autowired
	SysUserService sysUserService;
	
//	@RequestMapping("/export")
//	public void export(HttpServletResponse response) {
//		try {
//			String fileName = "aa.xls";
//			String sheetName = "用户";
//			String[] titles = {"登录名"," 公司名称","联系方式"};
//			String[] fields = {"loginname","name","phone"};
//			Map<String, Object> argsMap = new HashMap<>();
//			List<Map<String, Object>> list = sysUserService.selectAll2Excel(argsMap);
//			ExcelInfo excelInfo =new ExcelInfo(fileName, sheetName, titles, fields, list);
//			
//			ExcelUtil.export2Http(excelInfo, response);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	@RequestMapping("/import2")
	public void import2(@RequestParam(value = "file") MultipartFile file, HttpServletResponse response) {
		try {
			List<String[]> list = ExcelUtil.read(file.getOriginalFilename(),file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
