/**
 * (C) 2011-2012 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 */
package com.kaola.kprofiler.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * Desc:TODO
 * 
 * @author wei.zw
 * @since 2017年7月12日 下午8:39:49
 * @version v 0.1
 */
public class DailyRollingLog {
	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 日滚文件名
	 */
	private String rollingFileName;

	/**
	 * BufferedWriter实例
	 */
	private BufferedWriter bufferedWriter;

	/**
	 * 获取下次滚动时间的Calendar
	 */
	private RollingCalendar rollingCalendar = new RollingCalendar();

	/**
	 * 格式化工具
	 */
	private SimpleDateFormat sdf = new SimpleDateFormat("'.'yyyy-MM-dd");

	/**
	 * 下次的滚动时间
	 */
	private long nextRollingTime = rollingCalendar.getNextRollingMillis(new Date());

	/**
	 * @param filePath
	 */
	public DailyRollingLog(String filePath) {
		System.out.println(filePath);
		fileName = filePath;
		Date now = new Date();
		rollingFileName = fileName + sdf.format(now);
		File file = new File(filePath);
		// 文件已经存在
		if (file.exists()) {
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date lastModifiedDate = new Date(file.lastModified());
			String lastModified = dateFormat.format(lastModifiedDate);
			long sizeM = file.length() / 1024 / 1024;
			if (lastModified.equals(dateFormat.format(now))) {
				createWriter(filePath, sizeM < 100);
			} else {
				rollingFileName = fileName + sdf.format(lastModifiedDate);
				rolling(now);
			}
		} else {
			createWriter(file);
		}
	}

	/**
	 * @param log
	 */
	public void info(final String log) {
		long time = System.currentTimeMillis();
		if (time > nextRollingTime) {
			Date now = new Date();
			nextRollingTime = rollingCalendar.getNextRollingMillis(now);
			rolling(now);
		}
		subappend(log);

	}

	public void error(Exception e) {
		try {
			e.printStackTrace(new PrintStream(new File(fileName)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	private void subappend(String log) {
		try {
			bufferedWriter.write(log + "\n");
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * 
	 */
	public void closeFile() {
		if (bufferedWriter != null) {
			try {
				bufferedWriter.flush();
				bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param now
	 */
	private void rolling(Date now) {
		String datedFilename = fileName + sdf.format(now);
		if (rollingFileName.equals(datedFilename)) {
			return;
		}
		closeFile();
		File target = new File(rollingFileName);
		if (target.exists()) {
			target.delete();
		}

		File file = new File(fileName);
		file.renameTo(target);
		createWriter(new File(fileName));
		rollingFileName = datedFilename;
	}

	/**
	 * 可选是否覆盖旧文件
	 * 
	 * @param filename
	 * @param append
	 *            true表示追加，false表示覆盖
	 */
	private void createWriter(String filename, boolean append) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(filename, append), 8 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 直接覆盖旧文件
	 * 
	 * @param file
	 */
	private void createWriter(File file) {
		try {
			file = file.getCanonicalFile();
			File parent = file.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			bufferedWriter = new BufferedWriter(new FileWriter(file), 8 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class RollingCalendar {

		public long getNextRollingMillis(Date now) {
			return getNextRollingDate(now).getTime();
		}

		private Date getNextRollingDate(Date now) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DATE, 1);
			return cal.getTime();
		}
	}
}
