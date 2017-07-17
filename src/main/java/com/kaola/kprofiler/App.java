package com.kaola.kprofiler;

import java.lang.instrument.Instrumentation;

import com.kaola.kprofiler.config.KprofilerConfiguration;

/**
 * Hello world!
 */
public class App {
	public static void premain(String args, Instrumentation inst) {
		System.err.println("--------Started---KPROFILER------------");
		KprofilerConfiguration.getInstance();
		inst.addTransformer(new ProfilerTransformer());
		// new ProfileSampler();
	}
}
