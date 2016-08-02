package com.chinaway.tms.utils.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtil {

	public static Object getBean(String beanName) {
		
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");
		return ac.getBean(beanName);
	}
}
