package com.test;

import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import com.simian.game.modules.user.controller.UserController;

public class TestMethod {

	public static Map<Integer, String> getMethodPro(Class clazz,
			String methodName) {
		Map<Integer, String> map = new HashMap<Integer, String>();

		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = pool.get(clazz.getName());

			CtMethod cm = cc.getDeclaredMethod(methodName);

			// 使用javaassist的反射方法获取方法的参数名
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			if (attr == null) {
				// exception
			}

			String[] paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			for (int i = 0; i < paramNames.length; i++) {
				paramNames[i] = attr.variableName(i + pos);
				map.put(i, attr.variableName(i + pos));
			}

			// paramNames即参数名
			for (int i = 0; i < paramNames.length; i++) {
				System.out.println(paramNames[i]);
			}

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return map;

	}

	public static void main(String[] args) {
		getMethodPro(Test.class, "test2");
	}
}
