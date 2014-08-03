package com.simian.game.model;

import java.util.HashMap;
import java.util.Map;

public class User {
	//Ĭ�����ݿ�ID
	 private String id;  
     
	    private String name;  
	      
	    private int age;  
	      
	    /** 
	     * <br>------------------------------<br> 
	     */  
	    public User() {  
	    }  
	  
	    /** 
	     *  
	     * <br>------------------------------<br> 
	     * @param id 
	     * @param name 
	     * @param age 
	     */  
	    public User(String id, String name, int age) {  
	        super();  
	        this.id = id;  
	        this.name = name;  
	        this.age = age;  
	    }  
	  
	    /** 
	     * ���id 
	     * @return the id 
	     */  
	    public String getId() {  
	        return id;  
	    }  
	  
	    /** 
	     * ����id 
	     * @param id the id to set 
	     */  
	    public void setId(String id) {  
	        this.id = id;  
	    }  
	  
	    /** 
	     * ���name 
	     * @return the name 
	     */  
	    public String getName() {  
	        return name;  
	    }  
	  
	    /** 
	     * ����name 
	     * @param name the name to set 
	     */  
	    public void setName(String name) {  
	        this.name = name;  
	    }  
	  
	    /** 
	     * ���age 
	     * @return the age 
	     */  
	    public int getAge() {  
	        return age;  
	    }  
	  
	    /** 
	     * ����age 
	     * @param age the age to set 
	     */  
	    public void setAge(int age) {  
	        this.age = age;  
	    }  
	      
	    /** 
	     * toString 
	     */  
	    public String toString() {  
	        Map<String, String> map = new HashMap<String, String>();  
	        map.put("id", id);  
	        map.put("name", name);  
	        map.put("age", String.valueOf(age));  
	        return map.toString();  
	    }  
}
