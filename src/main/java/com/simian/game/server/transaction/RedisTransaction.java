package com.simian.game.server.transaction;

import redis.clients.jedis.Transaction;

public class RedisTransaction {
	private boolean isTransaction;//是否可以开启事务
	private boolean isStartTransaction;//是否已经开启了事务
	private    Transaction tx ;
	
	 
	public Transaction getTx() {
		return tx;
	}
	public void setTx(Transaction tx) {
		this.tx = tx;
	}
	public boolean isTransaction() {
		return isTransaction;
	}
	public void setTransaction(boolean isTransaction) {
		this.isTransaction = isTransaction;
	}
	public boolean isStartTransaction() {
		return isStartTransaction;
	}
	public void setStartTransaction(boolean isStartTransaction) {
		this.isStartTransaction = isStartTransaction;
	}
	
}
