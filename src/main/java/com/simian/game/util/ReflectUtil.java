package com.simian.game.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class ReflectUtil {
	/**
	 * 
	 * @param name
	 *             
	 * @param classParas
	 *            Class类信息参数列�? 如果是基本数据类型是可以使用其Tpye类型，如果用class字段是无效的
	 *            如果是非数据类型可以使用的class字段来创建其Class类信息对象，这些都要遵守�?
	 * @param paras
	 *            实际参数列表数据
	 * @return 返回Object引用的对象，实际实际创建出来的对象，如果要使用可以强制转换为自己想要的对�?
	 * 
	 *         带参数的反射创建对象
	 */
	public static Object getInstance(Class c, Class classParas[],
			Object paras[]) {
		Object o = null;
		try {
			Constructor con = c.getConstructor();// 获取使用当前构�?方法来创建对象的Constructor对象，用它来获取构�?函数的一�?
			try {
				// 信息
				o = con.newInstance();// 传入当前构�?函数要的参数列表
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (NoSuchMethodException ex) {
		} catch (SecurityException ex) {
		}

		return o;// 返回这个用Object引用的对�?
	}
	
	/**
	 * 以文件的形式来获取包下的�?��Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, Set<Class<?>> classes) {
		// 获取此包的目�?建立�?��File
		File dir = new File(packagePath);
		// 如果不存在或�?也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文�?);
			return;
		}
		// 如果存在 就获取包下的�?��文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规�?如果可以循环(包含子目�? 或则是以.class结尾的文�?编译好的java类文�?
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});
		// 循环�?��文件
		for (File file : dirfiles) {
			// 如果是目�?则继续扫�?
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, classes);
			} else {
				// 如果是java类文�?去掉后面�?class 只留下类�?
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					// 添加到集合中�?
					// classes.add(Class.forName(packageName + '.' +
					// className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(Thread.currentThread().getContextClassLoader()
							.loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param pack
	 * @return
	 */
	public static Set<Class<?>> getClasses(String pack) {

		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替�?
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义�?��枚举的集�?并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元�?
				URL url = dirs.nextElement();
				// 得到协议的名�?
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文�?并添加到集合�?
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文�?
					// 定义�?��JarFile
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection())
								.getJarFile();
						// 从此jar�?得到�?��枚举�?
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭�?
						while (entries.hasMoreElements()) {
							// 获取jar里的�?��实体 可以是目�?和一些jar包里的其他文�?如META-INF等文�?
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/�?���?
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果�?/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 �?/"替换�?."
									packageName = name.substring(0, idx)
											.replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一�?class文件 而且不是目录
									if (name.endsWith(".class")
											&& !entry.isDirectory()) {
										// 去掉后面�?.class" 获取真正的类�?
										String className = name.substring(
												packageName.length() + 1,
												name.length() - 6);
										try {
											// 添加到classes
											classes.add(Class
													.forName(packageName + '.'
															+ className));
										} catch (ClassNotFoundException e) {
											// log
											// .error("添加用户自定义视图类错误 找不到此类的.class文件");
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						// log.error("在扫描用户定义视图时从jar包获取文件出�?);
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}
	//获取某个方法的 参数名字
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


		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return map;

	}

}
