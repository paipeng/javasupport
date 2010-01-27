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

package jragonsoft.javautil.util;

import java.util.Calendar;

/**
 * Class for date manipulations.
 * 
 * @author zemian
 * @version $Id: DateUtils.java 42 2006-05-10 23:25:49Z zemian $
 */
public class DateUtils {
	private static Calendar calendar = Calendar.getInstance();

	/** return Month/Day/Year format of today's date. */
	public static String getTodayDate() {
		return getTodayMonth() + "/" + getTodayDay() + "/" + getTodayYear();
	}

	public static String getTodayMonth() {
		return StringUtils.toString(calendar.get(Calendar.MONTH) + 1);
	}

	public static String getTodayYear() {
		return StringUtils.toString(calendar.get(Calendar.YEAR));
	}

	public static String getTodayDay() {
		return StringUtils.toString(calendar.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Return a string describe the ellapse time period in format of:
	 * Years # Days # Minutes # Seconds # Milliseconds
	 */
	public static String getEllapseTimeString(long milliSec) {
		StringBuffer sb = new StringBuffer();
		long MSEC_IN_SEC = 1000;
		long MSEC_IN_MIN = 60 * 1000;
		long MSEC_IN_HOUR = 60 * 60 * 1000;
		long MSEC_IN_DAY = 24 * 60 * 60 * 1000;
		double MSEC_IN_YEAR = 24 * 60 * 60 * 1000 * 365.242199;

		long year = (long) (milliSec / MSEC_IN_YEAR);
		long day = (long) ((milliSec % MSEC_IN_YEAR) / MSEC_IN_DAY);
		long hour = (long) ((milliSec % MSEC_IN_DAY) / MSEC_IN_HOUR);
		long min = (long) ((milliSec % MSEC_IN_HOUR) / MSEC_IN_MIN);
		long sec = (long) ((milliSec % MSEC_IN_MIN) / MSEC_IN_SEC);
		long msec = (long) (milliSec % MSEC_IN_SEC);

		if (year > 0) {
			sb.append(year).append(" Year").append(year>1? "s " : " ");
		}
		if (day > 0) {
			sb.append(day).append(" Day").append(day>1? "s " : " ");
		}
		if (hour > 0) {
			sb.append(hour).append(" Hour").append(hour>1? "s " : " ");
		}
		if (min > 0) {
			sb.append(min).append(" Minute").append(min>1? "s " : " ");
		}
		if (sec > 0) {
			sb.append(sec).append(" Second").append(sec>1? "s " : " ");
		}
		if (msec > 0) {
			sb.append(msec).append(" Millisecond").append(msec>1? "s " : " ");
		}
		

		return sb.toString();
	}
}
