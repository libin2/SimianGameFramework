package com.test;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.Transaction;
import com.sleepycat.je.TransactionConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.StoreConfig;

public class TestDB extends Thread {
 String name="";
 public static String envHomePath = "D:/test/berkeleydata";
 public static String storeName = "micmiu-DPL-demo2";
 @Override
 public void run() {
  // TODO Auto-generated method stub
/*  if(name.equals("1"))
  {

   testDPLSecondaryIndexesPut2(envHomePath, storeName, name);
   testDPLSecondaryIndexesRead(envHomePath, storeName, "1");
  }else{*/
	 name="dsss";
	 
	  BlogItem2 b2 =  testDPLSecondaryIndexesRead(envHomePath, storeName, "1");
	  b2.setNo2( b2.getNo2()+1);
	  System.out.println(b2.getNo2());
  testDPLSecondaryIndexesPut(envHomePath, storeName, name,b2);
 // testDPLSecondaryIndexesRead(envHomePath, storeName, "1");
  //}
 }
 /**
  * 演示数据存储
  *
  * @param envHomePath
  */
 public void testDPLSecondaryIndexesPut(String envHomePath, String storeName,String cc,BlogItem2 b1) {

  Environment mydbEnv = null;
  EntityStore store = null; Transaction txn = null;
       
  try {
   EnvironmentConfig envCfg = new EnvironmentConfig();
   envCfg.setAllowCreate(true);
   envCfg.setTransactional(true);

   
       
               
   mydbEnv = new Environment(new File(envHomePath), envCfg);

   
     TransactionConfig txConfig = new TransactionConfig();
              txConfig.setSerializableIsolation(true);
              txn = mydbEnv.beginTransaction(null, txConfig);
             
   StoreConfig storeConfig = new StoreConfig();
   storeConfig.setAllowCreate(true);
   storeConfig.setTransactional(true);

   store = new EntityStore(mydbEnv, storeName, storeConfig);

   System.out
     .println(" ---- > put objects with Secondary Indexes ...");
   PrimaryIndex<String, BlogItem2> pIndex = store.getPrimaryIndex(
     String.class, BlogItem2.class);

   List<BlogItem2> itemList = new ArrayList<BlogItem2>() ;//this.loadData();
  /* BlogItem2 b1 = new BlogItem2("1");
   b1.setCategory("1");
   b1.setCategory(cc);*/
 //  b1.setNo2(1);
   itemList.add(b1);
   //itemList.add(new BlogItem2("2"));
   
   
   for (BlogItem2 item : itemList) {
    System.out.println("put object:" + item);
    pIndex.putNoReturn(txn,item);
   }
         txn.commit();
   System.out.println(" ---- > put all objects finished");
  } catch (Exception e) {
   e.printStackTrace(); txn.abort();
  } finally {
   if (null != store) {
    store.close();
   }
   if (null != mydbEnv) {
    // 在关闭环境前清理下日志
    mydbEnv.cleanLog();
    mydbEnv.close();
    mydbEnv = null;
   }
  }
 }
 public void testDPLSecondaryIndexesPut2(String envHomePath, String storeName,String cc) {

  Environment mydbEnv = null;
  EntityStore store = null; Transaction txn = null;
       
  try {
   EnvironmentConfig envCfg = new EnvironmentConfig();
   envCfg.setAllowCreate(true);
   envCfg.setTransactional(true);

   
       
               
   mydbEnv = new Environment(new File(envHomePath), envCfg);

   
     TransactionConfig txConfig = new TransactionConfig();
              txConfig.setSerializableIsolation(true);
              txn = mydbEnv.beginTransaction(null, txConfig);
              Thread.sleep(500000);
   StoreConfig storeConfig = new StoreConfig();
   storeConfig.setAllowCreate(true);
   storeConfig.setTransactional(true);

   store = new EntityStore(mydbEnv, storeName, storeConfig);

   System.out
     .println(" ---- > put objects with Secondary Indexes ...");
   PrimaryIndex<String, BlogItem2> pIndex = store.getPrimaryIndex(
     String.class, BlogItem2.class);

   List<BlogItem2> itemList = new ArrayList<BlogItem2>() ;//this.loadData();
   BlogItem2 b1 = new BlogItem2("1");
   b1.setCategory("1");
   b1.setCategory(cc);
   itemList.add(b1);
   //itemList.add(new BlogItem2("2"));
   
   
   for (BlogItem2 item : itemList) {
    System.out.println("put object:" + item);
    pIndex.putNoReturn(txn, item);//(item);
   }
         txn.commit();
   System.out.println(" ---- > put all objects finished");
  } catch (Exception e) {
   e.printStackTrace(); txn.abort();
  } finally {
   if (null != store) {
    store.close();
   }
   if (null != mydbEnv) {
    // 在关闭环境前清理下日志
    mydbEnv.cleanLog();
    mydbEnv.close();
    mydbEnv = null;
   }
  }
 }
 /**
  * 演示数据读取
  *
  * @param envHomePath
  */
 public BlogItem2 testDPLSecondaryIndexesRead(String envHomePath, String storeName,String pkey) {
	  BlogItem2 item4PI = null;
  Environment mydbEnv = null;
  EntityStore store = null;Transaction txn = null;
  try {
   EnvironmentConfig envCfg = new EnvironmentConfig();
   envCfg.setAllowCreate(true);
   envCfg.setTransactional(true);

   mydbEnv = new Environment(new File(envHomePath), envCfg);

   
     TransactionConfig txConfig = new TransactionConfig();
              txConfig.setSerializableIsolation(true);
              txn = mydbEnv.beginTransaction(null, txConfig);
           
   
   StoreConfig storeConfig = new StoreConfig();
   storeConfig.setAllowCreate(true);
   storeConfig.setTransactional(true);

   store = new EntityStore(mydbEnv, storeName, storeConfig);

   System.out.println(" ---- > get objects by Indexes ...");
   PrimaryIndex<String, BlogItem2> pIndex = store.getPrimaryIndex(
     String.class, BlogItem2.class);

 
    item4PI = pIndex.get(pkey);
   System.out.println(item4PI.getNo()+" "+item4PI.getCategory());
   txn.commit();

  } catch (Exception e) {
   e.printStackTrace();txn.abort();
  } finally {
   if (null != store) {
    store.close();
   }
   if (null != mydbEnv) {
    // 在关闭环境前清理下日志
    mydbEnv.cleanLog();
    mydbEnv.close();
    mydbEnv = null;
   }
  }
  return item4PI;
 }
 public static void main(String[] args) {
  for(int i=0;i<10;i++){
   TestDB demo = new TestDB();
   demo.name=i+"";
  demo.start();
  }
  TestDB demo2= new TestDB();
  int d=demo2.testDPLSecondaryIndexesRead(envHomePath, storeName, "1").getNo2();
  System.out.println(d);
  /*
  demo.testDPLSecondaryIndexesPut(envHomePath, storeName,"11");
  demo.testDPLSecondaryIndexesRead(envHomePath, storeName, "1");*/
 }
}