package com.kaola.kprofiler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import com.kaola.kprofiler.config.ProfConfig;
import com.kaola.kprofiler.utils.DailyRollingLog;
import com.kaola.kprofiler.utils.LogFactory;

/**
 * Created by wei.zw on 2017/7/6.
 */
public class ProfilerUtil {

	private static ProfConfig profConfig = new ProfConfig();

	private static final DailyRollingLog log = LogFactory.getLog();

	private static final Pattern METHOD_PATTERN = Pattern.compile("\\.\\w+\\([^()]*\\)");

	/**
	 * 性能日志打印阀值，单位：ms
	 */
	private static final long maxDuration = 200;

	private static final ExecutorService es = Executors.newCachedThreadPool();

	/**
	 * 开始性能日志监控。
	 * <p>
	 * 进入方法时调用这个方法。
	 * <p>
	 * 日志信息
	 */
	public static final void start(final String longMethodName) {
		Profiler.start((int) Thread.currentThread().getId(), longMethodName, System.currentTimeMillis());

	}

	public static final void entery(final String longMethodName) {

		Profiler.entery((int) Thread.currentThread().getId(), longMethodName, System.currentTimeMillis());

	}

	/**
	 * 结束性能日志监控。
	 * <p>
	 * 退出方法时调用这个方法。
	 */
	public static final void release() {
		final int id = (int) Thread.currentThread().getId();
		final long endTime = System.currentTimeMillis();
		Profiler.release(id, endTime);

		if (!Profiler.isReleased(id)) {
			return;
		}
		es.submit(new Runnable() {

			@Override
			public void run() {
				try {
					if (Profiler.getDuration(id) > 2) {
						String s = Profiler.dump(id, "\n", "");
						if (s != null && s.trim().length() > 2) {
							log.info(s);
						}
					}
					Profiler.reset(id);
				} catch (Exception e) {
					LogFactory.getRunLog().error(e);
				}
			}
		});

	}

}
