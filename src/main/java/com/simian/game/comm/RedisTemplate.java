package com.simian.game.comm;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import com.simian.game.server.transaction.AopTransaction;
import com.simian.game.server.transaction.RedisTransaction;
import com.simian.game.util.StringUtil;

public class RedisTemplate {

	private static Logger logger = Logger.getLogger(RedisTemplate.class);
	private  String host = "127.0.0.1";
	private  int port = 6379;
	private  int database = 0;
	private  String password = "123456";
	private  JedisPool connectionPool = null;
	private  int timeout = 10 * 1000;
	// private static int maxActive =350;
	private  int maxIdle = 300;
	private  int maxTotal = 600;
	// private static long maxWait = 100000;
	private  long timeBetweenEvictionRunsMillis = 30000;
	private  long minEvictableIdleTimeMillis = 30000;
	private  boolean testOnBorrow = true;
	private JedisPoolConfig poolConfig;
	
	public  void initPool() {
		
		long s2 = System.currentTimeMillis();
		
	   /*     GenericObjectPoolConfig pol = new GenericObjectPoolConfig();
	        pol.setMinIdle(1);
	        pol.setMaxIdle(5);
	        pol.setTestOnBorrow(true);
	        pol.setTestOnReturn(true);
	        pol.setTestWhileIdle(true);
	        pol.setNumTestsPerEvictionRun(10);
	        pol.setTimeBetweenEvictionRunsMillis(60000);*/
		poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		poolConfig.setTestOnBorrow(testOnBorrow);
		poolConfig.setMaxTotal(maxTotal);	 
		poolConfig.setMinIdle(10);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setTimeBetweenEvictionRunsMillis(60000);

		poolConfig.setTestOnBorrow(true);
		connectionPool = new JedisPool(poolConfig, host, port,5000);// 线程数量限制，IP地址，端口，超时时间
	} 

	public  Jedis getJedis() {
		if(connectionPool==null){
			initPool();
		}
		Jedis jedis = AopTransaction.jedislocal.get();
		if(jedis == null){
			jedis =  connectionPool.getResource();
			AopTransaction.jedislocal.set(jedis);
		}
		return jedis;// connectionPool.getResource();
	}

	public  void returnJedis(Jedis jedis) {
		if (jedis != null){
			connectionPool.returnResource(jedis);
			AopTransaction.jedislocal.remove();
		}
	}
	
	
	public String get(String key){
		Jedis jedis =  getJedis();
		
		String value =  jedis.get(key);
	//	returnJedis(jedis);
		//开启了事务 才会监控key
		if(AopTransaction.contextThreadLocal.get()!=null){
			jedis.watch(key);
		}
		return value;
	}
	
	public int getInt(String key){
		Jedis jedis =  getJedis();
		
		String value =  jedis.get(key);
		//开启了事务 才会监控key
		if(AopTransaction.contextThreadLocal.get()!=null){
			jedis.watch(key);
		}
		
		return StringUtil.paseInt(value);
	}
 
	public Double getDouble(String key){

		Jedis jedis =  getJedis();
		String value =  jedis.get(key);
		returnJedis(jedis);
		return Double.parseDouble(value);
	}
	public Float getFloat(String key){

		Jedis jedis =  getJedis();
		String value =  jedis.get(key);
		returnJedis(jedis);
		return Float.parseFloat(value);
	}
	 //事务
	public void set(String key,Object value){
		Jedis jedis =  getJedis();
		
		RedisTransaction redisTransaction =AopTransaction.contextThreadLocal.get();
		Transaction tx=null;
		if(redisTransaction!=null &&redisTransaction.isTransaction()){
			if(!redisTransaction.isStartTransaction()){
				if(jedis.exists(key)){
				
				tx = jedis.multi();//没有开启事务 则开启
				redisTransaction.setStartTransaction(true);redisTransaction.setTx(tx);
				}
			}
		}
		if(tx!=null){
			tx.set(key, value.toString());
			 AopTransaction.contextThreadLocal.set(redisTransaction);
		}else{

		    jedis.set(key, value.toString());// returnJedis(jedis);
		} 
	//	returnJedis(jedis);
	}
	//isExeu 是否强制中断事务
	public void exeu(){
		RedisTransaction redisTransaction =AopTransaction.contextThreadLocal.get();
		if(redisTransaction!=null){
			Transaction tx = redisTransaction.getTx();
			if(tx!=null){
				 tx.exec();
				 redisTransaction.setStartTransaction(false);
			}
		}
		
	}
	public void set(String key,Object value,boolean isExeu){
		Jedis jedis =  getJedis();
		
		RedisTransaction redisTransaction =AopTransaction.contextThreadLocal.get();
		Transaction tx=null;
		if(redisTransaction.isTransaction()){
			if(!redisTransaction.isStartTransaction()){
				tx = jedis.multi();//没有开启事务 则开启
				redisTransaction.setStartTransaction(true);
				redisTransaction.setTx(tx);
				
			}
		}
		if(tx!=null){
			tx.set(key, value.toString());
			 if(isExeu){
				 tx.exec();
				 redisTransaction.setStartTransaction(false);returnJedis(jedis);
			 }else{
				 redisTransaction.setTx(tx);
			 }
			 AopTransaction.contextThreadLocal.set(redisTransaction);
		}else{

		    jedis.set(key, value.toString());returnJedis(jedis);
		}
	   
	}
	
	
	public void delete(String key){
		Jedis jedis =  getJedis();
	    jedis.del(key);
		returnJedis(jedis);
	}
	public void incrByInt(String key,int value){
		Jedis jedis =  getJedis();
	    jedis.incrBy(key, value);
		returnJedis(jedis);
	}
	
	public void incrByFloat(String key,Float value){
		Jedis jedis =  getJedis();
	    jedis.incrByFloat(key, value);
		returnJedis(jedis);
	}
	public void setSet(String key,Float value){
		Jedis jedis =  getJedis();
	    jedis.incrByFloat(key, value);
		returnJedis(jedis);
	}
	
	public void setList(String key,List<Object> value){
	
		Jedis jedis =  getJedis();
		for(Object obj:value){
			jedis.rpush(key, obj.toString()); 
		}
		returnJedis(jedis);
	}
	public void addToList(String key,Object value){
		Jedis jedis =  getJedis();
		jedis.rpush(key, value.toString()); 
		returnJedis(jedis);
	}
	public  List<String> getList(String key){
		Jedis jedis =  getJedis(); 
		
        List<String> values = jedis.lrange(key, 0, -1);  
		
		returnJedis(jedis);
		return values;
	}
	
	
	public void setSet(String key,Set<Object> value){
		
		Jedis jedis =  getJedis();
		Iterator<Object> it =  value.iterator();
		while(it.hasNext()){
			jedis.sadd(key, it.next().toString());
		}
		 
		returnJedis(jedis);
	}
	public void addToSet(String key,Object value){
		Jedis jedis =  getJedis();
		jedis.sadd(key, value.toString()); 
		returnJedis(jedis);
	}
	public  Set<String> getSet(String key){
		Jedis jedis =  getJedis(); 
		
        Set<String> values = jedis.smembers(key);  
		
		returnJedis(jedis);
		return values;
	}
	
	
	public void setHash(String key,Map<String,String> value){
		
		Jedis jedis =  getJedis();
		Iterator<String> it= value.keySet().iterator();
		
		while(it.hasNext()){
			String k = it.next();
			jedis.hset(key, k, value.get(k).toString());
		}
		 
		returnJedis(jedis);
	}
	public void addToHash(String key,String filed,Object value){
		
		Jedis jedis =  getJedis();
		
		long s1 = System.currentTimeMillis();
		
		for(int i=0;i<100000;i++){
		//jedis.hset(key, filed, value.toString());
		jedis.set(key,   "");
		 
		}
		long s2 = System.currentTimeMillis();
		System.out.println(s2-s1);
		returnJedis(jedis);
	}
	public String getHashByFiled(String key,String filed){
		
		Jedis jedis =  getJedis();
		List<String> list=jedis.hmget(key, filed);
		 
		returnJedis(jedis);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	
	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConnectionPool(JedisPool connectionPool) {
		this.connectionPool = connectionPool;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public static void main(String[] args) {
		
		RedisTemplate r = new RedisTemplate();
		Jedis jedis =  r.getJedis();

		long s1 = System.currentTimeMillis();
		for(int i=0;i<100000;i++){
		//jedis.hset(key, filed, value.toString());
		//jedis.set("1",   "");
		r.set("1","1");
		}
		long s2 = System.currentTimeMillis();
		System.out.println(s2-s1);
	}
}
