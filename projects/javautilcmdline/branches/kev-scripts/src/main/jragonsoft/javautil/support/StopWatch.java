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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jragonsoft.javautil.util.DateUtils;


/**
 * A stop watch to count time ellapsed within program. This class provide
 * multiple start and stop count that manage all the interval for you.
 */
public class StopWatch {
	//public static org.apache.commons.logging.Log logger =
	//	org.apache.commons.logging.LogFactory.getLog(StopWatch.class);

	public static Logger logger = Logger.getLogger(StopWatch.class);

	private static Map watches = new HashMap();

	private List intervals = new ArrayList();

	private StopWatch() {
	}

	public void markInterval() {
		intervals.add(new Long(System.currentTimeMillis()));
	}

	public String getEllapsedTime() {
		if (intervals.size() < 1)
			throw new RuntimeException(
					"You need to mark interval at least twice to have ellapse time.");
		return getEllapsedTime(0, intervals.size() - 1);
	}

	public long getEllapsedMillis(int from, int to) {
		return ((Long) intervals.get(to)).longValue()
				- ((Long) intervals.get(from)).longValue();
	}

	public String getEllapsedTime(int from, int to) {
		return DateUtils.getEllapseTimeString(getEllapsedMillis(from, to));
	}

	public static StopWatch newInstance() {
		return new StopWatch();
	}

	public StopWatch start(String name) {
		StopWatch watch = null;
		if (!watches.containsKey(name)) {
			watch = new StopWatch();
			watches.put(name, watch);
		} else {
			watch = (StopWatch) watches.get(name);
			watch.intervals.clear();
		}
		watch.markInterval();
		logger.debug(name + " - started on"
				+ new Date(watch.getLastMarkMillis()));
		return watch;
	}

	public StopWatch stop(String name) {
		if (!watches.containsKey(name)) {
			throw new RuntimeException(name + " stop watch doesn't exist.");
		}
		StopWatch watch = (StopWatch) watches.get(name);
		watch.markInterval();
		logger.debug(name + " - stoped. Ellapsed time "
				+ watch.getEllapsedTime());
		return watch;
	}

	public long getLastMarkMillis() {
		return ((Long) intervals.get(intervals.size() - 1)).longValue();
	}
}

/**
 * Java 1.5 Source public class StopWatch{ public static
 * org.apache.commons.logging.Log logger =
 * org.apache.commons.logging.LogFactory.getLog(StopWatch.class);
 * 
 * private static Map <String, StopWatch> watches = new HashMap <String,
 * StopWatch>(); private List <Long>intervals = new ArrayList <Long>();
 * 
 * private StopWatch(){ }
 * 
 * public void markInterval(){ intervals.add(new
 * Long(System.currentTimeMillis())); }
 * 
 * public String getEllapsedTime(){ if(intervals.size() <1) throw new
 * RuntimeException("You need to mark interval at least twice to have ellapse
 * time."); return getEllapsedTime(0, intervals.size() -1); }
 * 
 * public long getEllapsedMillis(int from, int to){ return
 * intervals.get(to).longValue() - intervals.get(from).longValue(); }
 * 
 * public String getEllapsedTime(int from, int to){ return
 * DateUtils.getEllapseTimeString(getEllapsedMillis(from, to)); }
 * 
 * public static StopWatch newInstance(){ return new StopWatch(); }
 * 
 * public StopWatch start(String name){ StopWatch watch = null;
 * if(!watches.containsKey(name)){ watch = new StopWatch(); watches.put(name,
 * watch); }else{ watch = watches.get(name); watch.intervals.clear(); }
 * watch.markInterval(); logger.debug(name + " - started on" + new
 * Date(watch.getLastMarkMillis())); return watch; }
 * 
 * public StopWatch stop(String name){ if(!watches.containsKey(name)){ throw new
 * RuntimeException(name + " stop watch doesn't exist."); } StopWatch watch =
 * watches.get(name); watch.markInterval(); logger.debug(name + " - stoped.
 * Ellapsed time " + watch.getEllapsedTime()); return watch; }
 * 
 * public long getLastMarkMillis(){ return
 * intervals.get(intervals.size()-1).longValue(); } }
 */