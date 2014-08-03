package com.simian.game.server.transaction;

import java.lang.annotation.ElementType; 
import java.lang.annotation.Retention; 
import java.lang.annotation.RetentionPolicy; 
import java.lang.annotation.Target; 

import com.simian.game.modules.user.constant.BaseModel;

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD) 
public @interface Transaction { 
        public int errorCount() default 3;
        
} 
