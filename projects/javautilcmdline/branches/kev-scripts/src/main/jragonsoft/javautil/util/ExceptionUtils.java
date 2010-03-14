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

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Manipulate Exception and Throwable stack trace string.
 * 
 * @author zemian
 * @version $Id: ExceptionUtils.java 42 2006-05-10 23:25:49Z zemian $
 */
public class ExceptionUtils {
	/**
	 * Gets the lastThrowable attribute of the ExceptionUtils class
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @return The lastThrowable value
	 */
	public static Throwable getLastThrowable(Throwable source) {
		Throwable ret = source.getCause();
		if (ret == null) {
			return source;
		}

		while (ret != null) {
			ret = ret.getCause();
		}

		return ret;
	}

	/**
	 * Gets the stackTrace attribute of the ExceptionUtils class
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @return The stackTrace value
	 */
	public static String getStackTrace(Throwable source) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		source.printStackTrace(pw);
		return sw.toString();
	}

	/**
	 * Gets the lastStackTrace attribute of the ExceptionUtils class
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @return The lastStackTrace value
	 */
	public static String getLastStackTrace(Throwable source) {
		return getStackTrace(getLastThrowable(source));
	}
}