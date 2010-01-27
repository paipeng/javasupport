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

package jragonsoft.javautil.cmdtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.StringUtils;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: JoinText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class JoinText {
	private final static String DEFAULT_SPLIT_DELIM = SystemUtils.LINE_SEP;

	private final static String DEFAULT_JOIN_DELIM = " ";

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  JoinText [options] [JoinDelim]"
				+ "\n  Join mutiple lines of text or columns of text into a single line."
				+ "\n  Input are taking from Standard Input. Useful for piping commands."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n    -iLIST  Comma-sep indexes of elements for joinning from input."
				+ "\n"
				+ "\n  [JoinDelim]"
				+ "\n    Optional argument that change the delimeter for joining lines."
				+ "\n    Default is SPACE." + "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ echo -e 'one\\ntwo\\nthree' | jointext"
				+ "\n  $ ls -1 /tmp | jointext -i'0,2' '|'"
				+ "\n  $ echo -e 'one|two|three' | splittext '\\|' | jointext "
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: JoinText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the JoinText class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);
		String delim = DEFAULT_JOIN_DELIM;
		HashSet showIndexes = new HashSet();

		//System.out.println(opt);
		//Process options
		if (opt.isOpt("h")) {
			printExitHelp();
		}
		if (opt.isOpt("i")) {
			String idxStr = opt.getOpt("i", "");
			idxStr = idxStr.replaceAll(", ", ",");
			//tolerate comma-space
			showIndexes = new HashSet(Arrays.asList(idxStr.split(",")));
		}

		//Process argument
		if (opt.getArgsCount() == 1) {
			delim = opt.getArg(0);
		}

		String text = SystemUtils.getInputString();
		String[] elements = text.split(DEFAULT_SPLIT_DELIM);
		String result = "";
		// Let's use if/else for speed reason.
		if (showIndexes.size() > 0) {
			ArrayList joinItems = new ArrayList();
			for (int i = 0; i < elements.length; i++) {
				if (showIndexes.contains("" + i)) {
					joinItems.add(elements[i]);
				}
			}
			result = StringUtils.join(delim, joinItems);
		} else {
			result = StringUtils.join(delim, Arrays.asList(elements));
		}
		System.out.println(result);
	}
}
