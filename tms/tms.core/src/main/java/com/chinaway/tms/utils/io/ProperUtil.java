package com.chinaway.tms.utils.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取proper配置文件
 * @author shu
 *
 */
public class ProperUtil {
	public static Properties prop = null;
	
	/**
	 * 初始化
	 * @param clazz
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static Properties newInstall(Class clazz, String filePath) throws IOException {
		InputStream input = clazz.getClassLoader().getResourceAsStream(filePath);
		prop= new Properties();
		prop.load(input);
		return prop;
	}
	
	/**
	 * 读取
	 * @param filePath
	 * @param key
	 * @return
	 */
	public static String read(String filePath, String key){
		
		try {
			if(prop == null){
				prop = newInstall(ProperUtil.class, filePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return prop.getProperty(key);
    }
	
}
