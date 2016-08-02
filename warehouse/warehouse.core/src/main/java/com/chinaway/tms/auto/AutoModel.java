package com.chinaway.tms.auto;

/**
 * 自动生成需要的参数（常量）
 * 
 * @author shu
 *
 */
public class AutoModel {
	
	/**数据库-类型*/
	private String databaseType;
	/**数据库-驱动*/
	private String diver;
	/**数据库-连接url*/
	private String jdbcUrl;
	/**数据库-用户名*/
	private String username;
	/**数据库-密码*/
	private String password;
	/**数据库-表名*/
	private String tableName;
	/**jar资源根目录*/
	private String sourceRoot;
	/**jsp资源目录*/
	private String jspPath;
	/**包名前缀*/
	private String appPackagePrefix;
	/**模块名称*/
	private String appName;
	/**包名Url：根据包名来生成*/
	private String appPackageUrl;
	/**包名*/
	private String bussPackage;

	public AutoModel(){}

	/**
	 * 生成jar时需要初始化sourceRoot,jspPath可以设置为""<br>
	 * 生成war时需要初始化jspPath
	 */
	public AutoModel(String diver, String jdbcUrl, String username, String password, String tableName,
			String sourceRoot, String jspPath, String appPackagePrefix, String appName) {
		this.diver = diver;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.tableName = tableName;
		this.sourceRoot = sourceRoot;
		this.jspPath = jspPath;
		this.appPackagePrefix = appPackagePrefix;
		this.appName = appName;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
	
	public String getDiver() {
		return diver;
	}

	public void setDiver(String diver) {
		this.diver = diver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSourceRoot() {
		return sourceRoot;
	}

	public void setSourceRoot(String sourceRoot) {
		this.sourceRoot = sourceRoot;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getJspPath() {
		return jspPath;
	}

	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}

	public String getAppPackagePrefix() {
		return appPackagePrefix;
	}

	public void setAppPackagePrefix(String appPackagePrefix) {
		this.appPackagePrefix = appPackagePrefix;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getBussPackage() {
		
		return getAppPackagePrefix()+"."+getAppName();
	}

//	//不设置该set就是避免人为赋值
//	public void setBussPackage(String bussPackage) {
//		this.bussPackage = bussPackage;
//	}

	public String getAppPackageUrl() {
		String appPackageUrl = getBussPackage().replace(".", "/");
		return appPackageUrl;
	}
//	//不设置该set就是避免人为赋值
//	public void setAppPackageUrl(String appPackageUrl) {
//		getAppPackage();
//		this.appPackageUrl = appPackageUrl;
//	}

	@Override
	public String toString() {
		return "AutoModel [databaseType=" + databaseType + ", diver=" + diver + ", jdbcUrl=" + jdbcUrl + ", username="
				+ username + ", password=" + password + ", tableName=" + tableName + ", sourceRoot=" + sourceRoot
				+ ", jspPath=" + jspPath + ", appPackagePrefix=" + appPackagePrefix + ", appName=" + appName
				+ ", appPackageUrl=" + appPackageUrl + ", bussPackage=" + bussPackage + "]";
	}

}
