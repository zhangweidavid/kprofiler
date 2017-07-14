/**
 * Copyright 2014-2015, NetEase, Inc. All Rights Reserved.
 * 
 * Date: 2017年7月11日
 */

package com.kaola.kprofiler.utils;

import java.io.File;

/**
 * Desc:TODO
 * 
 * @author wei.zw
 * @since 2017年7月11日 下午11:28:44
 * @version v 0.1
 */

public class LogFactory {
	private static String homePath = (String) System.getProperties().get("user.home");

	public static DailyRollingLog getLog() {
		return LogHolder.log;
	}

	public static DailyRollingLog getRunLog() {
		return LogHolder.runLog;
	}

	private static class LogHolder {
		public static DailyRollingLog log = new DailyRollingLog(homePath + File.separator + "kprofiler.log");

		public static DailyRollingLog runLog = new DailyRollingLog(homePath + File.separator + "run.log");
	}
}
