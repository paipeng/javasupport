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

import java.util.Arrays;
import java.util.HashSet;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: SplitText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class SplitText {
	private final static String DEFAULT_DELIM = "\\s+";

	/**
	 * Description of the Method
	 */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  SplitText [options] [SplitDelim]"
				+ "\n  Split a line of text by whitespace and print each element on a new line."
				+ "\n  Input are taking from Standard Input. Useful for piping commands."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n    -iLIST  Comma-sep indexes of elements to print."
				+ "\n    -n      Do not trim text from Standard Input."
				+ "\n"
				+ "\n  [SplitDelim]"
				+ "\n    Optional argument that change the delimeter for split. Default"
				+ "\n    use all whitespaces including SPACEs and NEWLINEs. This delim"
				+ "\n    is REGEX pattern! So make sure to escape special char."
				+ "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ echo 'one two three' | splittext"
				+ "\n  $ ls -m /tmp | splittext -i'0,2' ', '" + "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: SplitText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the SplitText class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);
		String delim = DEFAULT_DELIM;
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
		boolean isTrimInput = !opt.isOpt("n");

		//Process argument
		if (opt.getArgsCount() == 1) {
			delim = opt.getArg(0);
		}

		String text = SystemUtils.getInputString();
		if (isTrimInput) {
			text = text.trim();
		}
		String[] elements = text.split(delim);
		// Let's if/else if/else for speed reason.
		if (showIndexes.size() > 0) {
			for (int i = 0; i < elements.length; i++) {
				if (showIndexes.contains("" + i)) {
					System.out.println(elements[i]);
				}
			}
		} else {
			for (int i = 0; i < elements.length; i++) {
				System.out.println(elements[i]);
			}
		}
	}
}
