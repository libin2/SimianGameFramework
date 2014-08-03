package com.simian.game.util;

import java.util.HashMap;
import java.util.Map;

import com.simian.game.server.core.ApplicationContextUtil;


public class IdWorkerFactory {

	 private static IdWorker instance = null;
	    
	    private IdWorkerFactory() {
	      //Load configuration information from DB or file
	      //Set values for properties
	    }
	  
	    private static synchronized void syncInit(Integer node_id) {
	      if ( instance == null) {
	        instance = new IdWorker(node_id);
	        
	      }
	    }
	    public static IdWorker getInstance()throws IllegalArgumentException {
	      if (instance == null) {
	    	  System.out.println(ApplicationContextUtil.getContext());
	    	  String node="1";
 	    	  if(node!=null){
	    		  syncInit(Integer.parseInt(node));
	    	  }else{
	    		  throw new IllegalArgumentException(String.format("node id is null "));
	    	  }
	    	 
	        
	      }
	      return instance;
	    }
	   
	   public static void main(String[] args) throws Exception {
		 // SystemConfig.getInstance().put("node_id", 1);
		  try{
		  IdWorkerFactory.getInstance().nextId();
		  }catch(IllegalArgumentException e) {
			  // 如果null 就要初始化 node_id
			  //e.printStackTrace();
		  }
	   }

}
