package com.test;

import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.simian.game.model.China;
import com.simian.game.model.City;
import com.simian.game.model.Province;

public class TestFastjson {
	public static void main(String[] args) {
		  China china = new China();  
	        china.setName("�й�");  
	        List<Province> provinces = new ArrayList<Province>();  
	  
	        Province hei = new Province();  
	        hei.setName("����");  
	        City heiCity = new City();  
	        heiCity.setCity(new String[] { "�����", "����" });  
	        hei.setCitys(heiCity);  
	        provinces.add(hei);  
	  
	        Province guang = new Province();  
	        guang.setName("�㶫");  
	        City guangCity = new City();  
	        guangCity.setCity(new String[] { "����", "����", "�麣" });  
	        guang.setCitys(guangCity);  
	        provinces.add(guang);  
	  
	        Province tai = new Province();  
	        tai.setName("̨��");  
	        City taiCity = new City();  
	        taiCity.setCity(new String[] { "̨��", "���� " });  
	        tai.setCitys(taiCity);  
	        provinces.add(tai);  
	  
	        china.setProvince(provinces);  
	  
	        Object cc = china;
	        String result=JSON.toJSONString(cc);  
	        System.out.println(result);
	        Class clazz= TestFastjson.class;
	        String s = clazz.getSimpleName();
	        char[]ss= s.toCharArray();
	        ss[0]=  Character.toLowerCase(ss[0]);
	        System.out.println(new String(ss));
	        
	    //    China c2=JSON.parseObject(result);//ת�ɶ���  
	     //   System.out.println(c2);
	}
}
