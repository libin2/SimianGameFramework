package com.test;


import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.simian.game.model.User;

public class Test {	private static Logger logger = Logger.getLogger(Test.class);

	public static void main(String[] args) {
		
		
		
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml"); 
		
	/*
		UserDao userDao = (UserDao)context.getBean("userDao");
		long d=System.currentTimeMillis();
		
	for(int i=0;i<1;i++){	
		User user= new User();
		user.setId("55002"+i);
		
		userDao.getMongoTemplate().save(user);
	}
	long d2=System.currentTimeMillis();
	logger.debug("dds");
		 
		System.out.println(d2-d);*/
		
	}
	public void test(String s2){
		
	}public void test2(String s2,String s3){
		
	}
}
