package com.simian.game.server.mapping;
import java.lang.annotation.ElementType; 
import java.lang.annotation.Retention; 
import java.lang.annotation.RetentionPolicy; 
import java.lang.annotation.Target; 

import com.simian.game.modules.user.constant.BaseModel;

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD) 
public @interface SocketCommand { 
    
        public int method(); 
        public boolean syn(); 
         
        //public BaseModel classType() default BaseModel.LIU;
        
} 
