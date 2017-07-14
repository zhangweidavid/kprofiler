/**
 * (C) 2011-2012 Alibaba Group Holding Limited.
 * <p>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 */
package com.kaola.kprofiler.config;

import java.io.FileReader;
import java.util.Properties;

/**
 *
 */
public class ProfConfig {

	/**
	 * 是否开启debug模式
	 */
	private boolean debugModel = true;

	private String logHome = System.getProperty("user.home") == null ? "/temp/logs/"
			: System.getProperty("user.home") + "/logs/";

	private static int ignoreThreshold = 2;

	/**
	 * 日志目录
	 */
	private String logName = "kprofiler.log";

	public Properties loadProperties(String file) {
		DelegatingProperties properties = new DelegatingProperties(System.getProperties());
		try {
			properties.load(new FileReader(file)); // 配置文件原始内容，未进行变量替换

		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	public boolean getDebugModel() {
		return debugModel;
	}

	public static int getIgnoreThreshold() {
		return ignoreThreshold;
	}

	private void initConfig(Properties properties) {
		String debugModel = properties.getProperty("profiler.debug");
		String logName = properties.getProperty("profiler.logName");
		String logHome = properties.getProperty("profiler.logHome");
		String ignoreThreshold = properties.getProperty("profiler.ignoreThreshold");
	}

}
