package com.kaola.kprofiler.worker;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.kaola.kprofiler.ProfileEntry;
import com.kaola.kprofiler.Profiler;

/**
 * Created by wei.zw on 2017/7/10.
 */
public class ProfileSampler {

	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	public ProfileSampler() {
		scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				 List<ProfileEntry> list=	Profiler.sample();
				 if(!list.isEmpty()) {
				 }
				 
			}
		}, 1, 1, TimeUnit.SECONDS);
	}

}
