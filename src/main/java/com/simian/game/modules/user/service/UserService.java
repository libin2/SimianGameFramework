package com.simian.game.modules.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simian.game.modules.user.dao.UserDao;
import com.simian.game.server.transaction.Transaction;

@Component
public class UserService {
	
	@Autowired
	UserDao userDao;
	
 	@Transaction(errorCount=3)
	public void test(String key){
		int value = userDao.getInt(key);
		value++;
		userDao.set(key, value);
		userDao.exeu();
		value = userDao.getInt(key);//这样会出错
		System.out.println(value);
	}
}
