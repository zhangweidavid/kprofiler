/**
 * Copyright 2014-2015, NetEase, Inc. All Rights Reserved.
 * 
 * Date: 2017年7月14日
 */

package com.kaola.kprofiler.utils;

/**
 * Desc:TODO
 * 
 * @author wei.zw
 * @since 2017年7月14日 下午10:09:09
 * @version v 0.1
 */
public class StringUtils {

	public static String defaultIfBlank(String str, String def) {
		if (isBlank(str)) {
			return def;
		}
		return str;
	}

	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
}
