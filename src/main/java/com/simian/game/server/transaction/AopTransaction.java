package com.simian.game.server.transaction;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.simian.game.comm.RedisTemplate;
import com.sun.org.apache.regexp.internal.recompile;

import redis.clients.jedis.Jedis;
@Component
@Aspect
public class AopTransaction {
	

	private static Logger logger = Logger.getLogger(AopTransaction.class);
	
	// 这里的定义步骤:返回类型 package.class.method(parameter)
	@Pointcut("execution (* com.simian.game.modules.user.*.*(..))")
	private void myPointCutMethod() {
	};

	public static ThreadLocal<Jedis> jedislocal = new ThreadLocal<Jedis>();
	public static ThreadLocal<RedisTransaction> contextThreadLocal = new ThreadLocal<RedisTransaction>();

	public RedisTemplate redisTemplate;

	public Jedis getJedis() {
		Jedis jedis = jedislocal.get();
		if (jedis == null) {
			jedis = redisTemplate.getJedis();
			jedislocal.set(jedis);
		}
		return jedis;
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Around("myPointCutMethod()")
	public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {

		//Jedis j =  getJedis();
		// 获取method
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		Transaction transaction = method.getAnnotation(Transaction.class);
		Object retVal = null;
		if (transaction != null) {
			logger.debug(" --------------- redis事务开始 ---------------");
			// 设置事务
			RedisTransaction redisTransaction = new RedisTransaction();
			redisTransaction.setTransaction(true);
			redisTransaction.setStartTransaction(false);
			contextThreadLocal.set(redisTransaction);
			int error = 0;
			int maxErrorCount =  transaction.errorCount();
			  
			while (error <=maxErrorCount) {

				try {
					retVal = pjp.proceed();
					// 结束事务
					if (redisTransaction.isStartTransaction()) {
						List<Object> result = redisTransaction.getTx().exec();
						if (result == null) {
							redisTransaction.setStartTransaction(false);
							throw new TransactionException();
						}  
					} 
					break; 
				} catch (Exception ex) {
					//ex.printStackTrace();
					//ex.printStackTrace();
					logger.debug(" --------------- 乐观锁失败重做  ---------------");
					error++;
				}
			}
			if(error>maxErrorCount){
				logger.debug(" --------------- 事务重做超过3次 抛出异常  ---------------");
				System.out.println("----------------放弃了达到"+error+"");
				throw new TransactionException();
			}
			logger.debug(" --------------- redis事务 结束 ---------------");
			contextThreadLocal.remove();
		} else {
			logger.debug(" --------------- 没有事务  ---------------");
			retVal = pjp.proceed();
		}
		jedislocal.remove();
		return retVal;
	}
}