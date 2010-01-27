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

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * A simple java.io.FilenameFilter implementation that supports regex pattern as
 * filter. On each #accept(File dir, String name), we use a
 * java.util.regex.Matcher instance's find() method to do regex matching.
 * 
 * @author zemian
 * @version $Id: RegExFilenameFilter.java 26 2006-05-04 23:46:14Z zdeng $
 */
public class RegExFilenameFilter implements FilenameFilter {
	private Pattern pattern;

	/**
	 * Constructor for the RegExFilenameFilter object
	 * 
	 * @param regex
	 *            Description of the Parameter
	 */
	public RegExFilenameFilter(String regex) {
		pattern = Pattern.compile(regex);
	}

	/**
	 * Constructor for the RegExFilenameFilter object
	 * 
	 * @param pattern
	 *            Description of the Parameter
	 */
	public RegExFilenameFilter(Pattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * We are using matcher's find() to do regex matching.
	 * 
	 * @param dir
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public boolean accept(File dir, String name) {
		return pattern.matcher(name).find();
	}
}
