package com.simian.game.server.core;

import org.springframework.context.ApplicationListener; 
import org.springframework.context.event.ContextRefreshedEvent; 
import org.springframework.stereotype.Component; 

import com.simian.game.server.mapping.AnnotationInit;


@Component("BeanDefineConfigue") 
public class BeanDefineConfigue implements 
ApplicationListener<ContextRefreshedEvent> {
	//初始化完毕事件，spring还有很多事件可以利用 
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("------------------扫描socketCommand---------------------------");
		AnnotationInit.init("com.simian.game.modules");
	}

 
} 
