package com.simian.game.comm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class BaseDao {
	@Autowired
	public RedisTemplate redisTemplate;

	@Autowired
	 public MongoTemplate mongoTemplate;
	 
	
	public String get(String key){
		return redisTemplate.get(key);
	}
	public int getInt(String key){
		return redisTemplate.getInt(key);
	}
	
	public void set(String key,Object value){
		 redisTemplate.set(key,value);
	}
	public void exeu(){
		 redisTemplate.exeu();
	}
}
