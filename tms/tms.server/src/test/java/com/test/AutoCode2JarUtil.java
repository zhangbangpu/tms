package com.test;

import com.chinaway.tms.auto.AutoCreateClassUtil;
import com.chinaway.tms.auto.AutoModel;

public class AutoCode2JarUtil {
	
	public static void main(String[] args) {
		
		String diver = "com.mysql.jdbc.Driver"; 
		String jdbcUrl = "jdbc:mysql://localhost:3306/tms?useUnicode=true&characterEncoding=utf-8"; 
		String username = "root"; 
		String password = "root"; 
		String tableName = "area_site"; 
		String appPackagePrefix = "com.chinaway.tms"; 
		String appName = "basic"; 
		String sourceRoot = "src/main/java";
		AutoModel autoModel = new AutoModel(diver, jdbcUrl, username, password, 
				tableName, sourceRoot,"", appPackagePrefix,appName);
		
		
		AutoCreateClassUtil.generate2Jar(autoModel);
		
//		autoModel.setTableName("sys_role");
//		AutoCreateClassUtil.generate2Jar(autoModel);
	}
}