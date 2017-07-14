package com.kaola.kprofiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:调用轮廓辅助类
 * 
 * <pre>
 * </pre>
 *
 * @author wei.zw
 * @version v 0.1
 * @since 2016年9月8日 下午2:36:36
 */
public final class Profiler {

	private static final ProfileEntry[] entryHolder = new ProfileEntry[65536];

	/**
	 * 开始计时。
	 *
	 * @param longClassName
	 *            第一个entry的信息
	 */
	public static void start(int id, String message, long startTime) {

		if (entryHolder[id] == null) {
			entryHolder[id] = new ProfileEntry(message, null, null, startTime);
		} else {
			entery(id, message.toString(), startTime);
		}
	}

	/**
	 * 清除计时器。
	 * <p>
	 * <p>
	 * 清除以后必须再次调用<code>start</code>方可重新计时。
	 * </p>
	 */
	public static void reset(int id) {
		// 发射事件
		// ProfileSampler.publish.onNext(entryHolder[(int)Thread.currentThread().getId()]);
		entryHolder[id] = null;
	}

	/**
	 * 开始一个新的entry，并计时。
	 *
	 * @param message
	 *            新entry的信息
	 */
	public static void entery(int threadId, String message, long startTime) {
		ProfileEntry currentEntry = getCurrentEntry(threadId);

		if (currentEntry != null) {
			currentEntry.enterSubEntry(message, startTime);
		}
	}

	/**
	 * 结束最近的一个entry，记录结束时间。
	 */
	public static void release(int id, long endTime) {
		ProfileEntry currentEntry = getCurrentEntry(id);

		if (currentEntry != null) {
			currentEntry.release(endTime);
		}
	}

	/**
	 * 取得耗费的总时间。
	 *
	 * @return 耗费的总时间，如果未开始计时，则返回<code>-1</code>
	 */
	public static long getDuration(int threadId) {
		ProfileEntry entry = getEntry(threadId);

		if (entry != null) {
			return entry.getDuration();
		} else {
			return -1;
		}
	}

	public static boolean isReleased(int id) {
		ProfileEntry entry = getEntry(id);
		if (entry != null) {
			return entry.isReleased();
		}
		return false;
	}

	/**
	 * 列出所有的entry。
	 *
	 * @return 列出所有entry，并统计各自所占用的时间
	 */
	public static String dump(int threadId) {
		return dump(threadId, "", "");
	}

	/**
	 * 列出所有的entry。
	 *
	 * @param prefix1
	 *            首行前缀
	 * @param prefix2
	 *            后续行前缀
	 * @return 列出所有entry，并统计各自所占用的时间
	 */
	public static String dump(int threadId, String prefix1, String prefix2) {
		ProfileEntry entry = entryHolder[threadId];
		entryHolder[threadId] = null;
		if (entry != null) {
			return entry.toString(prefix1, prefix2);
		} else {
			return "";
		}
	}

	/**
	 * 取得第一个entry。
	 *
	 * @return 第一个entry，如果不存在，则返回<code>null</code>
	 */
	public static ProfileEntry getEntry(int threadId) {
		return entryHolder[threadId];
	}

	/**
	 * 取得最近的一个entry。
	 *
	 * @return 最近的一个entry，如果不存在，则返回<code>null</code>
	 */
	private static ProfileEntry getCurrentEntry(int threadId) {
		ProfileEntry subEntry = getEntry(threadId);
		ProfileEntry entry = null;

		if (subEntry != null) {
			do {
				entry = subEntry;
				subEntry = entry.getUnreleasedEntry();
			} while (subEntry != null);
		}

		return entry;
	}

	public static List<ProfileEntry> sample() {
		List<ProfileEntry> result = new ArrayList<>();
		for (int i = 0; i < entryHolder.length; i++) {
			ProfileEntry pe = entryHolder[i];
			if (pe != null && pe.isReleased()) {
				result.add(pe);
				entryHolder[i] = null;
			}
		}
		return result;
	}

}
