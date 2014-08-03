package com.simian.game.comm;


import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TestThreadRedis2 extends Thread {
	
	int error=0;
 @Override
 public void run() {
  // TODO Auto-generated method stub
  super.run();
  Jedis jedis1 = new Jedis("127.0.0.1");
  while(true){
	if(ex(jedis1,error)==null){
		error++;
		if(error>3){
			System.out.println("3333333333333333333");
			break;
			
		}
	}else{
		break;
	}
	
  }
  


 }
 public List<Object> ex(Jedis jedis1,int error ){
	  String count = jedis1.get("count1");
	  int countInt = 0;
	  if (count != null) {
	   countInt = Integer.parseInt(count);
	   countInt++;
	  }
	  jedis1.watch("count1");
	  jedis1.watch("count2");
	 
	  String count2 = jedis1.get("count2");
	  int countInt2 = 0;
	  if (count2 != null) {
	   countInt2 = Integer.parseInt(count2);
	   countInt2++;
	  }
	  // jedis1 对 name 进行监控
	  Transaction tx = jedis1.multi();
	
	  // 从 jedis1 中获得'事物'
	  tx.set("count1", countInt + "");
	  tx.set("count2", countInt2 + "");
	  // 在jedis1执行后，插入 jedis2 的命令
	  // jedis2.set("name","session_02");
	  // 当redis1开启事物后，只能用Transaction进行修改，不能用Jedis进行修改。
	  // 此时的result是null，如果没有 jedis2 对 name 的修改，结果返回["OK","OK"]
	  // 表示本次提交事物的返回结果列表。
	  List<Object> result = tx.exec();
	  if(result==null){
		  System.out.println(""+error+"   error"+this.getName());
	  }else{
	  System.out.println("执行"+result.size()+" "+result.toString());}
	//  System.out.println("TestThreadRedis2 "+result + " " + countInt+" "+countInt2);
	  return result;
 }
 public static void main(String[] args) {
/*	 Jedis jedis1 = new Jedis("127.0.0.1");

  jedis1.del("count1");
  jedis1.del("count2");*/
 
  for (int i = 0; i < 15; i++) {
   TestThreadRedis2 testThreadRedis = new TestThreadRedis2();
   testThreadRedis.setName("thread "+i);
   testThreadRedis.start();
  }
 
 }
}
