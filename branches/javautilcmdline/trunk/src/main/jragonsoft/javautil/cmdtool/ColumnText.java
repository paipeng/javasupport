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
import java.util.HashSet;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;
import jragonsoft.javautil.util.StringUtils;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: ColumnText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class ColumnText {
	private final static String DEFAULT_SPLIT_DELIM = "\\s+";

	private final static String DEFAULT_JOIN_DELIM = " ";

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  ColumnText [options] ColumnIndex [ColumnIndex...]"
				+ "\n  Extract column text from input lines. ColumnIndex is zero-based"
				+ "\n  counting from LEFT to RIGHT."
				+ "\n  Input are taking from Standard Input. Useful for piping commands."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n    -sDELIM"
				+ "\n            Optional argument that change the delimeter for spliting input"
				+ "\n            text. Default use one or more SPACEs. This delim"
				+ "\n            is REGEX pattern! So make sure to escape special char."
				+ "\n    -jDELIM"
				+ "\n            Optional argument that change the delimeter for joining columns"
				+ "\n            text. Default use one SPACE."
				+ "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ echo -e 'one two three\\nfour five six' | columntext 1 2"
				+ "\n  $ ls -l /tmp | columntext 4 8 " + "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: ColumnText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the ColumnText class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);
		String splitDelim = null;
		String joinDelim = null;
		HashSet showIndexes = new HashSet();

		//System.out.println(opt);
		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 1) {
			printExitHelp();
		}

		splitDelim = opt.getOpt("s", DEFAULT_SPLIT_DELIM);
		joinDelim = opt.getOpt("j", DEFAULT_JOIN_DELIM);

		//Process argument
		for (int i = 0; i < opt.getArgsCount(); i++) {
			String arg = opt.getArg(i).trim();
			try {
				Integer.parseInt(arg);
				showIndexes.add(arg);
			} catch (Exception e) {
			}
		}

		String text = SystemUtils.getInputString();
		String[] elements = text.split(FileUtils.LINE_SEP);
		// Let's if/else if/else for speed reason.
		if (showIndexes.size() > 0) {
			for (int i = 0; i < elements.length; i++) {
				String[] cols = elements[i].split(splitDelim);
				ArrayList columnTexts = new ArrayList();
				for (int j = 0; j < cols.length; j++) {
					if (showIndexes.contains("" + j)) {
						columnTexts.add(cols[j]);
					}
				}
				String out = StringUtils.join(joinDelim, columnTexts);
				System.out.println(out);
			}
		} else {
			System.err
					.println("ERROR: You need to specify at least one valid ColumnIndex");
			System.exit(-1);
		}
	}
}
