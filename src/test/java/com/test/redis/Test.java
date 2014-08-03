package com.test.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.simian.game.modules.user.dao.UserDao;

public class Test extends Thread {
	static UserDao userDao = null;
	public String key="";
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml"); 
		 userDao = (UserDao)context.getBean("userDao");
		for(int i=0;i<100;i++ ){
			
			Test t = new Test();//.start();
			t.key="1";
			t.start();
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=0;i<1;i++ ){
			
		//userDao.sleep(key); 
		}
	}

}
