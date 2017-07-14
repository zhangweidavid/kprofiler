package com.kaola.kprofiler;

import java.lang.instrument.Instrumentation;

/**
 * Hello world!
 */
public class App {
	public static void premain(String args, Instrumentation inst) {
		System.err.println("--------Started---KPROFILER------------");
		inst.addTransformer(new ProfilerTransformer());
		// new ProfileSampler();
	}
}
