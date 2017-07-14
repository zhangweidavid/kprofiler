package com.kaola.kprofiler.config;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wei.zw on 2017/7/7.
 */
public class ProfFilter {
	/**
	 * 注入的Package集合
	 */
	protected static Set<String> includePackage = new HashSet<String>();

	/**
	 * 不注入的Package集合
	 */
	private static Set<String> excludePackage = new HashSet<String>();

	/**
	 * 不注入的ClassLoader集合
	 */
	private static Set<String> excludeClassLoader = new HashSet<String>();

	private static Set<String> excludeMethod = new HashSet<>();
	
	public static Set<String> modifiedClass=new HashSet<>();

	static {
		// 默认不注入的Package
		excludePackage.add("java");
		excludePackage.add("sun");// 包含sunw
		excludePackage.add("com/sun/");
		excludePackage.add("org/");//
		excludePackage.add("javassist/");
		// 不注入profile本身
		excludePackage.add("com/kaola/kprofiler/");
		excludePackage.add("com/taobao/");
		excludePackage.add("com/alibaba/");
		excludePackage.add("io/");
		excludePackage.add("ch/qos/logback/");
		excludePackage.add("junit/");
		excludePackage.add("oracle/");
		excludePackage.add("com/google/");
		excludePackage.add("net/sf/");
		excludePackage.add("com/caucho/hessian/");
		excludePackage.add("com/netease/kaola/kschedule");
		excludePackage.add("com/netease/sentry/");
		excludePackage.add("com/netease/soa/trace");
		excludePackage.add("com/baidu/disconf/");
		excludePackage.add("com/netease/napm/");
		excludePackage.add("com/netease/haitao/framework/");
		excludePackage.add("redis/clients/");
		excludePackage.add("rx/");
		includePackage.add("com/netease/kaola/compose/promotion/activity/serviceimpl/");
		//includePackage.add("com/netease/kaola/compose/promotion/activity/online/serviceimpl/");
		includePackage.add("common/proxy/");
		excludeMethod.add("set");
		excludeMethod.add("get");
	}

	/**
	 *
	 * @param className
	 */
	public static void addIncludeClass(String className) {
		String icaseName = className.toLowerCase().replace('.', '/');
		includePackage.add(icaseName);
	}

	/**
	 *
	 * @param className
	 */
	public static void addExcludeClass(String className) {
		String icaseName = className.toLowerCase().replace('.', '/');
		excludePackage.add(icaseName);
	}

	/**
	 *
	 * @param classLoader
	 */
	public static void addExcludeClassLoader(String classLoader) {
		excludeClassLoader.add(classLoader);
	}

	/**
	 * 是否是需要注入的类
	 *
	 * @param className
	 * @return
	 */
	public static boolean isNeedInject(String className) {
		if (className == null || className.trim().length() == 0) {
			return false;
		}
		String icaseName = className.toLowerCase().replace('.', '/');
		for (String v : includePackage) {
			if (icaseName.startsWith(v)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 是否是不需要注入的类
	 *
	 * @param className
	 * @return
	 */
	public static boolean isNotNeedInject(String className) {
		if (className == null) {
			return false;
		}
		String icaseName = className.toLowerCase().replace('.', '/');
		if (icaseName.contains("$$")) {
			return true;
		}
		for (String v : excludePackage) {
			if (icaseName.startsWith(v)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isNotNeedInjectMethod(String shortMethodName) {
		if (shortMethodName == null) {
			return false;
		}
		for (String v : excludeMethod) {
			if (shortMethodName.startsWith(v)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否是不需要注入的类加载器
	 *
	 * @param classLoader
	 * @return
	 */
	public static boolean isNotNeedInjectClassLoader(String classLoader) {
		for (String v : excludeClassLoader) {
			if (classLoader.equals(v)) {
				return true;
			}
		}
		return false;
	}
}
