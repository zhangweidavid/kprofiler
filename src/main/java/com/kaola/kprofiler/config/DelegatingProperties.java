/**
 * Copyright 2014-2015, NetEase, Inc. All Rights Reserved.
 * 
 * Date: 2017年7月12日
 */

package com.kaola.kprofiler.config;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import com.kaola.kprofiler.utils.PropertyUtils;

/**
 * Desc:TODO
 * 
 * @author wei.zw
 * @since 2017年7月12日 下午11:46:55
 * @version v 0.1
 */
public class DelegatingProperties extends Properties {

	/**  */
	private static final long serialVersionUID = -3237643173324159463L;

	private Properties context;

	public DelegatingProperties(Properties context) {
		this.context = context;
	}
	
	
	

	/** 
	 * @see java.util.Properties#load(java.io.Reader)
	 */
	@Override
	public synchronized void load(Reader reader)
			throws IOException {
		super.load(reader);
		context.putAll(this);
	}




	public String getProperty(String key) {
		String value = super.getProperty(key);
		return PropertyUtils.replacePlaceholder(value, context);

	}

	public String getProperty(String key, String defaultValue) {
		String value = super.getProperty(key, defaultValue);
		return PropertyUtils.replacePlaceholder(value, context);
	}

}
