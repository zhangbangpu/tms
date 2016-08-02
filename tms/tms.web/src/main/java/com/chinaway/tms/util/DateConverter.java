package com.chinaway.tms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * 页面传入的日期是String，后台接收是Date
 * @author shu
 *
 */
public class DateConverter implements Converter<String, Date> {
	
	@Override
	public Date convert(String source) {
		if("".equals(source)|| source == null){
			return null;
		}
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			return dateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}