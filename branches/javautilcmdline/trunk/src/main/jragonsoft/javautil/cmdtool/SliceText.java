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

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.StringUtils;
import jragonsoft.javautil.util.SystemUtils;

/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: SliceText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class SliceText {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  SliceText [options] [Start]:[Stop][:Step]"
				+ "\n  Slice out particular lines of text from input lines. Each line is assumed"
				+ "\n  ended with NewLine and treated as one element. Start default to 0,"
				+ "\n  Stop default to -1, and Step default to 1. Start and Step MUST be positive number!"
				+ "\n  Input are taking from Standard Input. Useful for piping commands."
				+ "\n" + "\n  [options]" + "\n    -h      Help and version."
				+ "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ echo -e 'one\\ntwo\\nthree' | slicetext :"
				+ "\n  $ echo -e 'one\\ntwo\\nthree' | slicetext 0:-1"
				+ "\n  $ echo -e 'one\\ntwo\\nthree' | slicetext 0:-1:1"
				+ "\n  $ numberrange 100 |slicetext 18:-3:20" + "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: SliceText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the SliceText class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);
		//System.out.println("[DEBUG] " + opt);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 1
				|| opt.getArg(0).indexOf(":") < 0) {
			printExitHelp();
		}

		int start = 0;
		int step = 1;
		int stop = -1;
		String sliceOp = opt.getArg(0);
		String[] parts = sliceOp.split(":");
		//System.out.println("[DEBUG] " + Arrays.asList(parts));

		if (parts.length >= 1 && StringUtils.isNotBlank(parts[0])) {
			start = Integer.parseInt(parts[0]);
		}

		if (parts.length >= 2 && StringUtils.isNotBlank(parts[1])) {
			stop = Integer.parseInt(parts[1]);
		}

		if (parts.length >= 3 && StringUtils.isNotBlank(parts[2])) {
			step = Integer.parseInt(parts[2]);
		}

		if (start < 0) {
			printExitHelp();
		}

		String text = SystemUtils.getInputString();
		String[] elements = text.split(SystemUtils.LINE_SEP);

		//System.out.println("[DEBUG] " + start);
		//System.out.println("[DEBUG] " + stop);
		//System.out.println("[DEBUG] " + step);

		int stopNum = 0;
		if (stop < 0) {
			stopNum = (elements.length + stop + 1);
		} else {
			stopNum = stop;
		}
		for (int i = start; i < stopNum; i += step) {
			System.out.println(elements[i]);
		}
	}
}
