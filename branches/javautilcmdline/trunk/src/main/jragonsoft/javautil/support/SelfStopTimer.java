/*
 *	  5/4/2006 Zemian Deng
 * 
 *	  Licensed under the Apache License, Version 2.0 (the "License");
 *	  you may not use this file except in compliance with the License.
 *	  You may obtain a copy of the License at
 * 
 *		  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *	  Unless required by applicable law or agreed to in writing, software
 *	  distributed under the License is distributed on an "AS IS" BASIS,
 *	  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	  See the License for the specific language governing permissions and
 *	  limitations under the License.
 *																				 
 */

package jragonsoft.javautil.support;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer that can do self termination after a specific period of time or based
 * on counter
 * 
 * @author zemian
 * @version $Id: SelfStopTimer.java 26 2006-05-04 23:46:14Z zdeng $
 */
public class SelfStopTimer extends Timer {
	Timer stopperTimer = new Timer();

	/** Constructor for the SelfStopTimer object */
	public SelfStopTimer() {
	}

	/**
	 * Constructor for the SelfStopTimer object
	 * 
	 * @param isDaemon
	 *            Description of the Parameter
	 */
	public SelfStopTimer(boolean isDaemon) {
		super(isDaemon);
	}

	/**
	 * schedule task that will run ONCE and stop.
	 * 
	 * @param task
	 *            Description of the Parameter
	 * @param firstTime
	 *            Description of the Parameter
	 */
	public void schedule(TimerTask task, Date firstTime) {
		super.schedule(new CounterTimerStopperTask(task, new Long(1), this),
				firstTime);
	}

	/**
	 * schedule task that will run XNum of times and stop.
	 * 
	 * @param task
	 *            Description of the Parameter
	 * @param firstTime
	 *            Description of the Parameter
	 * @param maxRepeat
	 *            Description of the Parameter
	 */
	public void schedule(TimerTask task, Date firstTime, Long maxRepeat) {
		super.schedule(new CounterTimerStopperTask(task, maxRepeat, this),
				firstTime);
	}

	/**
	 * schedule task same as parent, except it will stop on lastTime date.
	 * 
	 * @param task
	 *            Description of the Parameter
	 * @param delay
	 *            Description of the Parameter
	 * @param period
	 *            Description of the Parameter
	 * @param lastTime
	 *            Description of the Parameter
	 */
	public void schedule(TimerTask task, long delay, long period, Date lastTime) {
		super.schedule(task, delay, period);
		stopperTimer.schedule(
				new DateTimerStopperTask(null, this, stopperTimer), lastTime);
	}

	/**
	 * schedule task same as parent, except it will stop on lastTime date.
	 * 
	 * @param task
	 *            Description of the Parameter
	 * @param firstTime
	 *            Description of the Parameter
	 * @param period
	 *            Description of the Parameter
	 * @param lastTime
	 *            Description of the Parameter
	 */
	public void schedule(TimerTask task, Date firstTime, long period,
			Date lastTime) {
		super.schedule(task, firstTime, period);
		stopperTimer.schedule(
				new DateTimerStopperTask(null, this, stopperTimer), lastTime);
	}

	/**
	 * Description of the Class
	 * 
	 * @author zemian
	 * @version $Id: SelfStopTimer.java 26 2006-05-04 23:46:14Z zdeng $
	 */
	public static class DateTimerStopperTask extends TimerTask {
		Timer regTimer = null;

		Timer stopperTimer = null;

		TimerTask task = null;

		/**
		 * Constructor for the DateTimerStopperTask object
		 * 
		 * @param task
		 *            Description of the Parameter
		 * @param regTimer
		 *            Description of the Parameter
		 * @param stopperTimer
		 *            Description of the Parameter
		 */
		public DateTimerStopperTask(TimerTask task, Timer regTimer,
				Timer stopperTimer) {
			this.task = task;
			this.regTimer = regTimer;
			this.stopperTimer = stopperTimer;
		}

		/** Main processing method for the DateTimerStopperTask object */
		public void run() {
			if (task != null) {
				task.run();
			}
			if (regTimer != null) {
				regTimer.cancel();
			}
			if (stopperTimer != null) {
				stopperTimer.cancel();
			}
		}
	}

	/**
	 * Description of the Class
	 * 
	 * @author zemian
	 * @version $Id: SelfStopTimer.java 26 2006-05-04 23:46:14Z zdeng $
	 */
	public static class CounterTimerStopperTask extends TimerTask {
		Timer stopperTimer = null;

		long counter = 0;

		TimerTask task = null;

		/**
		 * Constructor for the CounterTimerStopperTask object
		 * 
		 * @param task
		 *            Description of the Parameter
		 * @param maxCount
		 *            Description of the Parameter
		 * @param stopperTimer
		 *            Description of the Parameter
		 */
		public CounterTimerStopperTask(TimerTask task, Long maxCount,
				Timer stopperTimer) {
			this.task = task;
			this.counter = maxCount.longValue();
			this.stopperTimer = stopperTimer;
		}

		/** Main processing method for the CounterTimerStopperTask object */
		public void run() {
			while (counter-- > 0) {
				if (task != null) {
					task.run();
				}
			}

			if (stopperTimer != null) {
				stopperTimer.cancel();
			}
		}
	}
}
