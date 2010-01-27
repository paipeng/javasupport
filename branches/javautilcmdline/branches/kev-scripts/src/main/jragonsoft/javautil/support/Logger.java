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

import java.util.HashMap;
import java.util.Map;

/**
 * A very simple logging facility for small command line program uses. It's not
 * dynamic configurable, only through programatically. If you need more power
 * logger, please use apache commons-logging along with log4j.
 * 
 * If you start using this class for logging, then transition to log4j is much
 * easier.
 */
public class Logger {
	private static String[] LEVEL_NAMES = { "ERROR", "WARN", "INFO", "DEBUG" };

	private static int ERROR = 0;

	private static int WARN = 1;

	private static int INFO = 2;

	private static int DEBUG = 3;

	private static Map loggers = new HashMap();

	private String loggerName;

	private int level;

	private Logger(String name) {
		loggerName = name;
		level = INFO;
	}

	public void setLevel(String levelName) {
		for (int i = LEVEL_NAMES.length - 1; i >= 0; i--) {
			if (LEVEL_NAMES[i].equalsIgnoreCase(levelName)) {
				setLevel(i);
				return;
			}
		}
		throw new RuntimeException("Log level name is not valid.");
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public static Logger getLogger(Class cls) {
		Logger logger = null;
		if (!loggers.containsKey(cls.getName())) {
			logger = new Logger(cls.getName());
			loggers.put(cls.getName(), logger);
		} else {
			logger = (Logger) loggers.get(cls.getName());
		}

		return logger;
	}

	public void debug(String s) {
		if (level >= DEBUG)
			System.out.println(s);
	}

	public void info(String s) {
		if (level >= INFO)
			System.out.println(s);
	}

	public void warn(String s) {
		if (level >= WARN)
			System.out.println(s);
	}

	public void error(String s) {
		if (level >= ERROR)
			System.err.println(s);
	}

	public void error(String s, Throwable cause) {
		if (level >= ERROR) {
			System.err.println(s);
			cause.printStackTrace(System.err);
		}
	}
}