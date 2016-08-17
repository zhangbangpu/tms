package com.chinaway.tms.utils.lang;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Date工具类 Date 、String 、XMLGregorianCalendar 3种类型的转换<br/>
 * 以及日期的常见操作,底层依赖的是java.util.Calendar
 * 
 * @ClassName: DateUtil
 * @author shuheng
 */
public class DateUtil {
	/**日期格式为yyyy-MM-dd HH:mm:ss*/
	public static final String YMD_HMS ="yyyy-MM-dd HH:mm:ss";
	/**日期格式为yyyy-MM-dd*/
	public static final String YMD ="yyyy-MM-dd";
//	public static final int YEAR = 1;
//	public static final int MOUTH = 2;
//	public static final int WEEK_OF_YEAR = 3;
//	public static final int WEEK_OF_MONTH = 4;
	/**天*/
	public static final int DAY = 5;
	/**时(12小时制)*/
	public static final int HOUR = 10;
	/**时(24小时制)*/
	public static final int HOUR_OF_DAY = 11;
	/**分钟*/
	public static final int MINUTE = 12;
	/**秒*/
	public static final int SECOND = 13;
	/**毫秒*/
	public static final int MILLISECOND = 14;
	 
	/**
	 * 将指定字符(String)串转换成日期 (Date),格式是yyyy-MM-dd HH:mm:ss<br/>
	 * 如果发生错误，则返回 null。
	 * 
	 * @param dateStr	String 日期字符串
	 * @return Date
	 */
	public static Date strToDate(String dateStr) {
		SimpleDateFormat sd = new SimpleDateFormat(YMD_HMS);
		// return sd.parse(dateStr);//这样可能会出 类型现转换异常
		return sd.parse(dateStr, new java.text.ParsePosition(0));
	}
	
	/**
	 * 将指定字符(String)串转换成日期 (Date)<br/>
	 * 如果发生错误，则返回 null。
	 * 
	 * @param dateStr	String 日期字符串
	 * @param datePattern	String 日期格式
	 * @return Date
	 */
	public static Date strToDate(String dateStr, String datePattern) {
		SimpleDateFormat sd = new SimpleDateFormat(datePattern);
		// return sd.parse(dateStr);//这样可能会出 类型现转换异常
		return sd.parse(dateStr, new java.text.ParsePosition(0));
	}

	/**
	 * 将指定日期(Date)对象转换成 格式化字符串 (String),格式是yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date	Date 日期对象
	 * @return String
	 */
	public static String dateToStr(Date date) {
		SimpleDateFormat sd = new SimpleDateFormat(YMD_HMS);
		return sd.format(date);
	}
	
	/**
	 * 将指定日期(Date)对象转换成 格式化字符串 (String)
	 * 
	 * @param date	Date 日期对象
	 * @param datePattern	String 日期格式
	 * @return String
	 */
	public static String dateToStr(Date date, String datePattern) {
		SimpleDateFormat sd = new SimpleDateFormat(datePattern);
		return sd.format(date);
	}
	
	/**
	 * 将指定XML日期(XMLGregorianCalendar)对象转换成 格式化字符串 (String),格式是yyyy-MM-dd HH:mm:ss
	 * 
	 * @param xmlDate	Date XML日期对象
	 * @param datePattern	String 日期格式
	 * @return String
	 */
	public static String xmlDateToStr(XMLGregorianCalendar xmlDate) {
		SimpleDateFormat sd = new SimpleDateFormat(YMD_HMS);
		Calendar calendar = xmlDate.toGregorianCalendar();
		return sd.format(calendar.getTime());
	}
	
	/**
	 * 将指定XML日期(XMLGregorianCalendar)对象转换成 格式化字符串 (String)
	 * 
	 * @param xmlDate	Date XML日期对象
	 * @param datePattern	String 日期格式
	 * @return String
	 */
	public static String xmlDateToStr(XMLGregorianCalendar xmlDate,String datePattern) {
		SimpleDateFormat sd = new SimpleDateFormat(datePattern);
		Calendar calendar = xmlDate.toGregorianCalendar();
		return sd.format(calendar.getTime());
	}
	
	/**
	 * 字符串 (String),格式是yyyy-MM-dd HH:mm:ss转换成XML日期(XMLGregorianCalendar)对象
	 * 
	 * @param dateStr	String 日期字符串
	 * @return XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar strToXmlDate(String dateStr) {
		return dateToXmlDate(strToDate(dateStr, YMD_HMS));
	}
	
	/**
	 * 字符串 (String) 转换成XML日期(XMLGregorianCalendar)对象
	 * 
	 * @param dateStr	String 日期字符串
	 * @param datePattern	String 日期格式
	 * @return XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar strToXmlDate(String dateStr, String datePattern) {
		return dateToXmlDate(strToDate(dateStr, datePattern));
	}

	/**
	 * 将指定XML日期(XMLGregorianCalendar)对象转换成 日期对象 (Date)
	 * 
	 * @param xmlDate	Date XML日期对象
	 * @return Date
	 */
	public static Date xmlDateToDate(XMLGregorianCalendar xmlDate) {
		return xmlDate.toGregorianCalendar().getTime();
	}
	
	/**
	 * 将指定日期对象 (Date)转换成 XML日期(XMLGregorianCalendar) 对象
	 * 
	 * @param date	Date 日期对象
	 * @return XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar dateToXmlDate(Date date) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		XMLGregorianCalendar gc = null;
		try {
			gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gc;
	}

	/**
	 * 获得当前时间的str格式，格式为yyyyMMddHHmmss(可以当成随机数)
	 * 
	 * @Title: todayStr
	 * @param @return    设定文件
	 * @return String    返回类型
	 */
	public static String todayStr() {
		Date date = new Date();
		return dateToStr(date, "yyyyMMddHHmmss");
	}
	
	/**
	 * time1与time2的时间差，返回单位是毫秒数
	 * 
	 * @param time1	当前时间
	 * @param time2	比较时间
	 * @return
	 */
	public static long timeDiff(Date time1, Date time2) {
		return time1.getTime() - time2.getTime();
	}
	
	/**
	 * time1与time2的时间差，没有type返回单位是毫秒数,设定了type则根据其返回<br/>
	 * 结果都是整型,默认的取整方式(向上取整),如0.6返回是0
	 * @param time1	当前时间
	 * @param time2	比较时间
	 * @param type	获取时间类型——DateUtil.DAY(天)、DateUtil.HOUR(小时)、DateUtil.MINUTE(分钟)、DateUtil.SECOND(秒)
	 * @return
	 */
	public static long timeDiff(Date time1, Date time2, int type) {
		long times = time1.getTime() - time2.getTime();
		switch (type) {
			case DateUtil.DAY:
				times = times / 1000 /3600 /24;
				break;
			case DateUtil.HOUR:
				times = times / 1000 /3600;
				break;
			case DateUtil.MINUTE:
				times = times / 1000 /60;
				break;
			case DateUtil.SECOND:
				times = times / 1000;
				break;
		}
		return times;
	}
	
	/**
	 * time1与time2的时间差，没有type返回单位是毫秒数,设定了type则根据其返回,结果都是整型,依据四舍五入
	 * 
	 * @param time1	当前时间
	 * @param time2	比较时间
	 * @param type	获取时间类型——DateUtil.DAY(天)、DateUtil.HOUR(小时)、DateUtil.MINUTE(分钟)、DateUtil.SECOND(秒)
	 * @return
	 */
	public static long timeDiff2(Date time1, Date time2, int type) {
		long times = time1.getTime() - time2.getTime();
		switch (type) {
		case DateUtil.DAY:
			times = (long) (times / 1000.0 /3600 /24 + 0.5);
			break;
		case DateUtil.HOUR:
			times = (long) (times / 1000.0 /3600 + 0.5);
			break;
		case DateUtil.MINUTE:
			times = (long) (times / 1000.0 /60 + 0.5);
			break;
		case DateUtil.SECOND:
			times = (long) (times / 1000.0 + 0.5);
			break;
		}
		return times;
	}
	
	/**
	 * time1与time2的时间差，没有type返回单位是毫秒数,设定了type则根据其返回
	 * 
	 * @param time1	当前时间
	 * @param time2	比较时间
	 * @param type	获取时间类型——DateUtil.HOUR(小时)、DateUtil.MINUTE(分钟)、DateUtil.SECOND(秒)
	 * @return
	 */
	public static double timeDiff2Double(Date time1, Date time2, int type) {
		double times = time1.getTime() - time2.getTime();
		switch (type) {
		case DateUtil.HOUR:
			times = times / 1000.0 /3600;
			break;
		case DateUtil.MINUTE:
			times = times / 1000.0 /60;
			break;
		case DateUtil.SECOND:
			times = times / 1000.0;
			break;
		}
		return times;
	}
	
	/**
	 * 获取日期的年份
	 * 
	 * @param date	日期对象
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 如果需要将int 转成 String，请用 String.valueOf()方法
		return c.get(Calendar.YEAR);
	}

	/**
	 * 获取日期的月份
	 * 
	 * @param date	日期对象
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 判断日期 是不是周末,true表示是周末
	 * 	 
	 * @param date	日期对象
	 * @return true表示该日期是周末
	 */
	public static boolean isWeekend(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 获取当前日期星期，英国算法(周日为一周第一天)
		int day = c.get(Calendar.DAY_OF_WEEK);
		// 如果是周六或周日就返回true
		if (day == 7 || day == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取当年指定月份第一天的字符串日期<br/>
	 * 如"2016-6-6 10:10:10",指定为5月,返回"2016-5-1 0:00:00"
	 * 
	 * @param specifiedMonth	指定月份 
	 * @param datePattern	日期格式 
	 * @return
	 */
	public static String getFirstDayOfSpecifiedMonth(int specifiedMonth, String datePattern) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MONTH, specifiedMonth - 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return dateToStr(c.getTime(), datePattern);
	}

	/**
	 * 获取某个日期该月有多少天
	 * 
	 * @param date	日期对象
	 * @return
	 */
	public static int getDaysOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DATE);
	}
	
	/**
	 * 获取某个日期的开始时间<br/>
	 * 如"2016-6-6 10:10:10",返回"2016-6-6 0:00:00"
	 * 
	 * @param date	日期对象
	 * @return
	 */
	public static Date getStartTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 获取某个日期的结束时间<br/>
	 * 如"2016-6-6 10:10:10",返回"2016-6-7 0:00:00"<br/>
	 * 获取的不是"2016-6-6 23:59:59",为了避免出现转点的问题
	 * 
	 * @param date	日期对象
	 * @return
	 */
	public static Date getEndTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 24);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
//	//与上面的效果相同
//	public static Date getEndTime(Date date) {
//		Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		c.add(Calendar.DAY_OF_MONTH, 1);
//		c.set(Calendar.HOUR_OF_DAY, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		return c.getTime();
//	}
	
	/**
	 * 获取某个日期该月的第一天<br/>
	 * 如"2016-6-6 10:10:10",返回"2016-6-1 0:00:00"
	 * 
	 * @param date	日期对象
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 获取某个日期该月的最后一天<br/>
	 * 如"2016-6-6 10:10:10",返回"2016-6-30 23:59:59"
	 * 
	 * @param date	日期对象
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DATE));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
	
	/**
	 * 获取某个日期下个月的第一天<br/>
	 * 如"2016-6-6 10:10:10",返回"2016-7-1 0:00:00"
	 * 
	 * @param date	日期对象
	 * @return
	 */
	public static Date getFirstDayOfNextMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取某个日期上个月的第一天<br/>
	 * 如"2016-6-6 10:10:10",返回"2016-5-1 0:00:00"
	 * 
	 * @param date	日期对象
	 * @return
	 */
	public static Date getFirstDayOfLastMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

}
