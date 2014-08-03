package com.simian.game.server.mapping;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.context.ApplicationContext;

import com.simian.game.server.core.ApplicationContextUtil;
import com.simian.game.util.ReflectUtil;
import com.simian.game.util.StringUtil;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

//初始化游戏指令
public class AnnotationInit {
	
	public static void main(String[] args) throws NoSuchMethodException,
			NoSuchFieldException {
		// TestAction t = new TestAction();
		AnnotationInit.init("com.simian.game.modules");
		System.out.println("-----------MyAnnotation4Class注解信息---------");

	}

	public static void init(String packageName) {

		System.out .println("-----------指令注册  start---------");
		// 获得包下的class
		Set set = ReflectUtil.getClasses(packageName);

		Iterator it = set.iterator();
		while (it.hasNext()) {
			Class c = (Class) it.next();
			SocketModuleCommand socketModuleCommand =  (SocketModuleCommand) c.getAnnotation(SocketModuleCommand.class);
			
			//
			Method[] arrm = c.getMethods();
			for (Method method : arrm) {
				// 获得注解 获得上面定义的url
				SocketCommand an4method = method
						.getAnnotation(SocketCommand.class);
				
				
				if (an4method == null) {
					continue;
				}
				
				CommandParameterObj parameterObj = new CommandParameterObj();
				//方法参数名字 
				parameterObj.setMapMethodPro( ReflectUtil.getMethodPro(c, method.getName()));
				 
				  Class[] params = method.getParameterTypes();
				  	//解析方法参数
				   for (int j = 0; j < params.length; j++) {
					   Class pp = params[j] ;
					   parameterObj.getMapClass().put(j,pp);
		            }
				   parameterObj.setSize(params.length);
				   Annotation[][] ans=    method.getParameterAnnotations();
				   
				   for(int j=0;j<ans.length;j++){ 
					   Annotation[] aa =ans[j];
					   if(aa.length>0){//取出对应的类型
						   Annotation annotationPa = aa[0];
						   if(annotationPa instanceof InBody){
							   InBody inBody = (InBody) annotationPa;
							   System.out.println("inbody "+inBody.key());
						   } else if(annotationPa instanceof InSession){
							   InSession inBody = (InSession) annotationPa;
							   System.out.println("inbody "+inBody.key());
						   }
						   parameterObj.getMapAn().put(j,annotationPa);
					   }
				   }
				   
				Integer t_m = an4method.method();
				Integer t_model = socketModuleCommand.model();//an4method.model();

				//取消反射检查
				method.setAccessible(true);
				
				String beanName=StringUtil.toOneLowerCase(c.getSimpleName());
				System.out.println("--------------------------指令注册"+t_model+","+t_m+" "+ beanName.getClass().toString()+", "+method.getName());
				Object obj = ApplicationContextUtil.getContext().getBean(beanName);// ReflectUtil.getInstance(c, null, null);
				CommandObj commandObj = new CommandObj();
				commandObj.setMethod(method);
				commandObj.setObj(obj);
				CommandManger.mapObj.put(t_m + "_" + t_model, commandObj);
				CommandManger.mapParme.put(t_m + "_" + t_model, parameterObj);
				// CommandManger.mapMethod.put(obj, method);
				/*
				 * //classUtil 存放 类实�?方法�? 供反射调�?
				 * if(MultiThreadHandlerApi.mapUrl.get(an4method.url() )!=null){
				 * throw new MappingException("存在�?��url地址映射到多个类"); }
				 * 
				 * MultiThreadHandlerApi.mapUrl.put(an4method.url(), classUtil);
				 */ 
			}
			System.out .println("-----------指令注册 end---------");
		}

	}

	

}
