/**
 * Copyright 2014-2015, NetEase, Inc. All Rights Reserved.
 * 
 * Date: 2017年7月12日
 */

package com.kaola.kprofiler.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Desc:TODO
 * 
 * @author wei.zw
 * @since 2017年7月12日 下午11:23:15
 * @version v 0.1
 */
public class PropertyUtils {

	private static final Pattern pattern = Pattern.compile("\\$\\{[^{}]*?\\}");

	private static List<String> findPlaceholders(String str) {
		List<String> result = new ArrayList<>();
		if (str == null || str.length() == 0) {
			return result;
		}
		Matcher matcher = pattern.matcher(str);
		// matcher.
		while (matcher.find()) {
			String t = matcher.group();
			if (t != null && t.length() > 3) {
				result.add(t);
			}
		}
		return result;
	}

	public static String replacePlaceholder(String source, Map<Object, Object> context) {
		if (source == null) {
			throw new IllegalArgumentException("source can't be null");
		}

		if (context == null) {
			throw new IllegalArgumentException("context can't be null");
		}
		List<String> placeholders = findPlaceholders(source);
		if (placeholders.isEmpty()) {
			return source;
		}
		for (String placeholder : placeholders) {
			String key = placeholder.substring(2, placeholder.length() - 1);
			if (context.containsKey(key)) {
				source = source.replace(placeholder, String.valueOf(context.get(key)));
			} else {
				throw new IllegalArgumentException();
			}
		}
		return source;
	}

	public static void main(String[] args) {
		String str = "${user.dir}/${user.language}/profiler.log";
		Map<Object, Object> context = new HashMap<>();
		context.put("user.dir", "/home/david");
		context.put("user.language", "china");
		System.out.println(replacePlaceholder(str, context));
	}

}
