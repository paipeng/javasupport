package deng.pojo.benchmark;

import java.util.Date;

/**
 * Default will sample rate on every 1000 data or 2 seconds, which ever comes first.
 * 
 * @author dengz1
 *
 */
public class RateSampler {
	private String name;
	private int count;
	private long maxSampleInterval = 2000; // in millis
	private long maxMsgPerSampleInterval = 1000; 
	private long startTime;
	private long stopTime;
	private double maxRate;
	private double currentRate;
	private int currentCount;
	private long lastSampleTime;
	
	public RateSampler(String name) {
		this.name = name;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
		lastSampleTime = startTime;
	}
	public void stop() {
		stopTime = System.currentTimeMillis();
	}
	
	public void sample(Object data) {		
		count ++;
		currentCount ++;

		// Calculate sample rate
		long currentSampleTime = System.currentTimeMillis();
		if (currentCount >= maxMsgPerSampleInterval || (currentSampleTime - lastSampleTime) >= maxSampleInterval) {				
			double ellapsedSecs = (currentSampleTime - lastSampleTime) / 1000.0;				
			currentRate = currentCount / ellapsedSecs;
			//System.out.printf(name + ": Current sample rate=%.2f msgs/sec, maxRate=%.2f, sampleEllapsedTime=%.2f secs, sampleCount=%d, totalCount=%d\n", currentRate, maxRate, ellapsedSecs, currentCount, count);
			System.out.printf(name + ": Current sample rate=%.2f msgs/sec, maxRate=%.2f, totalCount=%d\n", currentRate, maxRate, count);
			if (currentRate > maxRate) {
				maxRate = currentRate;
			}				
			lastSampleTime = currentSampleTime;
			currentCount = 0;
		}
	}
	public void printRates() {
		double ellapsedSecs = (stopTime - startTime) / 1000.0;
		System.out.printf(name + ": Start time: %s\n", new Date(startTime));
		System.out.printf(name + ": Stop time: %s\n", new Date(stopTime));
		System.out.printf(name + ": Ellapsed time: %.2f secs\n", ellapsedSecs);
		System.out.printf(name + ": Sample interval: %d ms\n", maxSampleInterval);
		System.out.printf(name + ": Message count: %d\n", count);
		System.out.printf(name + ": Max rate %.2f msg/sec\n", maxRate);
	}
}