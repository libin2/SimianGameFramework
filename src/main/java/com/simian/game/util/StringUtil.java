package com.simian.game.util;

public class StringUtil {
	public static String toOneLowerCase(String s){
	        char[]ss= s.toCharArray();
	        ss[0]=  Character.toLowerCase(ss[0]);
	        return new String(ss);
	}
	public static int paseInt(String str){
		int i = 0;
			if(str!=null && isNumeric(str)){

				i = Integer.parseInt(str);
			}
		 
		return i;
	}
	public static boolean isNumeric(String str){
		  for (int i = str.length();--i>=0;){   
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		  }
		  return true;
		 }

}
