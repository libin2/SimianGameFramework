package com.simian.game.util;

import java.util.Date;
import java.util.logging.Logger;

 
/**
 * 来自于twitter项目<a
 * href="https://github.com/twitter/snowflake">snowflake</a>的id产生方案，全局唯一，时间有序
 * 
 * 
 * @see https://github.com/twitter/snowflake
 * @author boyan
 * @Date 2011-4-27
 * 
 */
public class IdWorker {
    private final long workerId;
    private final long twepoch = 1303895660503L;
    private long sequence = 0L;
    private final long workerIdBits = 10L;
    private final long maxWorkerId = -1L ^ -1L << this.workerIdBits;
    private final long sequenceBits = 12L;

    private final long workerIdShift = this.sequenceBits;
    private final long timestampLeftShift = this.sequenceBits + this.workerIdBits;
    private final long sequenceMask = -1L ^ -1L << this.sequenceBits;

    private long lastTimestamp = -1L;


    public IdWorker(long workerId) {
        super();
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0",
                this.maxWorkerId));
        }
        this.workerId = workerId;
    }


    public synchronized Long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1 & this.sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        }
        else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
        	 throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        this.lastTimestamp = timestamp;
        return timestamp - this.twepoch << this.timestampLeftShift | this.workerId << this.workerIdShift
                | this.sequence;
    }
    public synchronized String nextStrId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1 & this.sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        }
        else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
        	 throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        this.lastTimestamp = timestamp;
        long value = timestamp - this.twepoch << this.timestampLeftShift | this.workerId << this.workerIdShift
                | this.sequence;
        return String.valueOf(value);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }


    private long timeGen() {
        return System.currentTimeMillis();
    }
    public static void main(String[] args) {
    	IdWorker worker2 = new IdWorker(1);
    	 long d = System.currentTimeMillis();
    	// String s ="";
    	 for(int i=0;i<10000;i++){
    		//java.util.UUID.randomUUID().toString();
    		String s =java.util.UUID.randomUUID().toString();// worker2.nextId()+"";
    	      //   System.out.println(s);
    	 }
    	 long d2 = System.currentTimeMillis();
    	 System.out.println(d2 - d);
    	  //       System.out.println(new Date(1303895660503L).toLocaleString());
	}

}
