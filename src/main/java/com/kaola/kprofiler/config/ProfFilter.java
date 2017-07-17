package com.kaola.kprofiler.config;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wei.zw on 2017/7/7.
 */
public class ProfFilter {
	public static Set<String> modifiedClass=new HashSet<>();

	


	/**
	 * 是否是需要注入的类
	 *
	 * @param className
	 * @return
	 */
	public static boolean isNeedProfiler(String className) {
		if (className == null || className.trim().length() == 0) {
			return false;
		}
		String icaseName = className.toLowerCase().replace('.', '/');
		for (String v : KprofilerConfiguration.getInstance().getProfilerPackage()) {
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
		for (String v : KprofilerConfiguration.getInstance().getExcludePackage()) {
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
		for (String v : KprofilerConfiguration.getInstance().getExcludeMethod()) {
			if (shortMethodName.startsWith(v)) {
				return true;
			}
		}
		return false;
	}

	
}
