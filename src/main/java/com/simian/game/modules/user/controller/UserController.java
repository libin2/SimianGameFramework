package com.simian.game.modules.user.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simian.game.modules.user.constant.EnumTest;
import com.simian.game.modules.user.constant.UserModules;
import com.simian.game.modules.user.dao.UserDao;
import com.simian.game.modules.user.model.Per;
import com.simian.game.modules.user.service.UserService;
import com.simian.game.server.mapping.InBody;
import com.simian.game.server.mapping.InSession;
import com.simian.game.server.mapping.SocketCommand;
import com.simian.game.server.mapping.SocketModuleCommand;
@Component
@SocketModuleCommand(model=UserModules.module)
public class UserController {

	@Autowired
	UserService userService;
	
	@SocketCommand(method =UserModules.LOGIN,  syn = false ) 
	public Object login(@InBody(key="test") String test1 ,@InBody(key="userId") String userId, String key){
		
		Per per = new Per();
		per.setName("哈哈");
		userService.test(key);	 
		return per;
	}
}
  