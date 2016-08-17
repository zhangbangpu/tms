package com.chinaway.tms.utils.lang;

import java.math.BigDecimal;

/**
 * 浮点数（小数）的精确计算,空串默认为0，被除数为1<br>
 * 如果要转成XX类型，使用XXValue()方法。如double类型，使用doubleValue()<br>
 * 
 * 利用double作为参数的构造函数，无法精确构造一个BigDecimal对象，需要自己指定一个上下文的环境，也就是指定精确位。
 * 而利用String对象作为参数传入的构造函数能精确的构造出一个BigDecimal对象。
 * 
 * @ClassName: BigDecimalUtil
 * @author shuheng
 */
public class BigDecimalUtil {
	//检查数字
	private static String checkNum(String num) {
		if ("".equals(num)) {
			num = "0";
		}
		return num;
	}
	
	/**
	 * 加法
	 * 
	 * @Title: add
	 * @param @param num1 	第1个值的字符串表示形式
	 * @param @param num2 	第2个值的字符串表示形式
	 * @param @return 设定文件
	 * @return BigDecimal 返回类型
	 */
	public static BigDecimal add(String num1, String num2) {
		num1 = checkNum(num1);
		num2 = checkNum(num2);
		BigDecimal bd1 = new BigDecimal(num1);
		BigDecimal bd2 = new BigDecimal(num2);
		// bd1.add(bd2).doubleValue();//获取double值
		return bd1.add(bd2);
	}

	/**
	 * 减法
	 * 
	 * @Title: subtract
	 * @param @param num1 	第1个值的字符串表示形式
	 * @param @param num2 	第2个值的字符串表示形式
	 * @param @return 设定文件
	 * @return BigDecimal 返回类型
	 */
	public static BigDecimal subtract(String num1, String num2) {
		num1 = checkNum(num1);
		num2 = checkNum(num2);
		BigDecimal bd1 = new BigDecimal(num1);
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.subtract(bd2);
	}

	/**
	 * 乘法
	 * 
	 * @Title: multiply
	 * @param @param num1	 第1个值的字符串表示形式
	 * @param @param num2	 第2个值的字符串表示形式
	 * @param @return 设定文件
	 * @return BigDecimal 返回类型
	 */
	public static BigDecimal multiply(String num1, String num2) {
		num1 = checkNum(num1);
		num2 = checkNum(num2);
		BigDecimal bd1 = new BigDecimal(num1);
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.multiply(bd2);
	}

	/**
	 * 除法
	 * 
	 * @Title: divide
	 * @param @param num1 	第1个值的字符串表示形式
	 * @param @param num2 	第2个值的字符串表示形式
	 * @param @param scale 	保留几位小数
	 * @param @return 设定文件
	 * @return BigDecimal 返回类型
	 */
	public static BigDecimal divide(String num1, String num2, int scale) {
		num1 = checkNum(num1);
		if ("".equals(num2)) {
			num2 = "1";
		}
		BigDecimal bd1 = new BigDecimal(num1);
		BigDecimal bd2 = new BigDecimal(num2);
		return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 保留小数
	 * 
	 * @param num	需要保留的数
	 * @param scale	保留几位小数
	 * @return
	 */
	public static BigDecimal round(BigDecimal num, int scale) {
		return num.divide(new BigDecimal("1"), scale, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 保留小数
	 * 
	 * @param num	需要保留的数
	 * @param scale	保留几位小数
	 * @return
	 */
	public static BigDecimal round(String num, int scale) {
		num = checkNum(num);
		BigDecimal bd1 = new BigDecimal(num);
		return bd1.divide(new BigDecimal("1"), scale, BigDecimal.ROUND_HALF_UP);
	}

}
