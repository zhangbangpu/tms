package com.chinaway.tms.utils.lang;

/**
 * 数学工具类，如随机数、幂计算等
 *
 * @ClassName: MathUtil
 * @author	shuheng
 */
public class MathUtil {

	/**
	 * 返回的范围是[0,num)
	 * @Title: random
	 * @param @param num
	 * @return int    返回类型
	 */
	public static int random(int num){
		//Math.random() 返回的范围是[0,1)
		return (int)(Math.random()*num);
	}
	
	/**
	 * 返回的范围是[start,end]
	 * @Title: random
	 * @param  start	范围起始值
	 * @param  end	范围终止值
	 * @return int    返回类型
	 */
	public static int random(int start,int end){
		return (int) (Math.random() * (end - start + 1)) + start;
	}
	
	
}
