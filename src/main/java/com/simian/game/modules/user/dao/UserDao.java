package com.simian.game.modules.user.dao;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.simian.game.comm.BaseDao;
import com.simian.game.modules.user.model.Per;
import com.simian.game.server.transaction.Transaction;

@Component
public class UserDao extends BaseDao {

	public String test(String key) {
		long s2 = System.currentTimeMillis();
		int c = redisTemplate.getInt(key);

		System.out.println("c--- " + c);
		c++;
		redisTemplate.set(key, c);
		long s1 = System.currentTimeMillis();
		// redisTemplate.addToHash("dl", "d", 1111111111);
		// System.out.println("sleep");
		return "";
	}
}
