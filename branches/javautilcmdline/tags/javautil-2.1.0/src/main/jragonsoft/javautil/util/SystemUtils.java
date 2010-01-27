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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Provide system wide static methods.
 * 
 * @author zemian
 * @version $Id: SystemUtils.java 42 2006-05-10 23:25:49Z zemian $
 */
public class SystemUtils {

	/** \n, \r\n, \r etc. */
	public static String LINE_SEP = FileUtils.LINE_SEP;

	/** / or \\ etc */
	public static String FILE_SEP = FileUtils.FILE_SEP;

	/** :, ; etc */
	public static String PATH_SEP = FileUtils.PATH_SEP;

	/**
	 * Read all standard input until EOF and return as one big String.
	 * 
	 * @return The inputString value
	 */
	public static String getInputString() {
		return FileUtils.getString(System.in);
	}

	/**
	 * Gets the inputLines attribute of the SystemUtils class
	 * 
	 * @return The inputLines value
	 */
	public static String[] getInputLines() {
		List lines = FileUtils.getLinesAsList(System.in);
		return ArrayUtils.toStringArray(lines);
	}

	/**
	 * Read a single (first) line from standard input.
	 * 
	 * @param lineNumber
	 *            Line number starting with one.
	 * @return The inputLine value
	 */
	public static String getInputLine(int lineNumber) {
		return FileUtils.getLine(System.in, lineNumber);
	}

	/**
	 * Execute system command abd return any STDERR & STDOUT string concatenated
	 * 
	 * @param systemCmd
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String exec(String systemCmd) {
		StringBuffer sb = new StringBuffer();
		try {
			Process proc = Runtime.getRuntime().exec(systemCmd);
			//int exitValue = proc.waitFor();

			String line = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(proc
					.getErrorStream()));
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in = new BufferedReader(
					new InputStreamReader(proc.getInputStream()));
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
