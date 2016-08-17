package com.chinaway.tms.utils.lang;

import java.io.UnsupportedEncodingException;

/**
 * String 的使用例子
 * 
 * @ClassName: StringUtil
 * @author shuheng
 */
public class StringUtil {

	/**
	 * 获得32位长度的字符串
	 * 
	 * @Title: getUUId32
	 * @param @return 设定文件
	 * @return String 返回类型
	 */
	public static String getUUId32() {
		// java.util.UUID 是jdk 提供的类
		String str = java.util.UUID.randomUUID().toString();
		String uuids = str.substring(0, 8) + str.substring(9, 13)
				+ str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		return uuids;
	}
	
	/**
	 * 将ISO-8859-1格式 转为其他编码
	 * @param str 需要转码的字符串
	 * @param encoding 编码
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String transcodage(String str, String encoding) throws UnsupportedEncodingException {
		String resultStr = "";
		if (str.equals(new String(str.getBytes("ISO-8859-1"), "ISO-8859-1"))) {
			resultStr = new String(str.getBytes("ISO-8859-1"), encoding);
		} else {
			resultStr = str;
		}
		return resultStr;
	}

	/**
	 * 文件重命名，组成格式：当前日期（yyyyMMddHHmmss）+ 4位随机数
	 * null表示重命名失败，无后缀
	 * @Title: getNewFilename
	 * @param @param oldFilename
	 * @return String    返回类型
	 */
	public static String getNewFilename(String oldFilename) {
//		String suffix = oldFilename.substring(oldFilename.lastIndexOf("."));
		String name = DateUtil.todayStr() + MathUtil.random(1000,9999);
		String suffix = getSuffix(oldFilename);
		if(suffix != null){
			return name + "." +suffix;
		}
		return null;
	}
	
	/**
	 * 获得文件后缀,null表示无后缀
	 * @param str
	 * @return
	 */
	public static String getSuffix(String str) {
		int index = str.lastIndexOf(".");
		if (index != -1) {
			String suffix = str.substring(str.lastIndexOf(".")+1);
			return suffix;
		}else{
			return null;
		}
	}
}
