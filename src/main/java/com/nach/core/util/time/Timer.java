package com.nach.core.util.time;

import java.util.Calendar;

public class Timer {

	private long start;
	
	private long stop;
	
	public void start() {
		this.start = Calendar.getInstance().getTimeInMillis();
	}
	
	public void stop() {
		this.stop = Calendar.getInstance().getTimeInMillis();
	}
	
	public long getElapsedInMilliseconds() {
		return stop - start;
	}
	
	public double getElapsed() {
		double elapsed = ((double)getElapsedInMilliseconds()) / 1000L;
		return elapsed;
	}
	
}
