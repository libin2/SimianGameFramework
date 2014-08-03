package com.simian.game.server.mapping;

import java.lang.annotation.ElementType; 
import java.lang.annotation.Retention; 
import java.lang.annotation.RetentionPolicy; 
import java.lang.annotation.Target; 

import com.simian.game.modules.user.constant.BaseModel;

//session 服务器装配
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.PARAMETER) 
public @interface InSession { 
        public String key();
        
} 
