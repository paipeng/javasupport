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

/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: NumberRange.java 4 2006-03-16 15:27:19Z zemian $
 */
public class NumberRange {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  NumberRange [options] [Start] Stop [Step]"
				+ "\n  Print a range of numbers from Start to (Stop - 1) with Step incremental."
				+ "\n  Each number will be ended with NewLine char. Start default to 0, and Step"
				+ "\n  default to 1." + "\n" + "\n  [options]"
				+ "\n    -h      Help and version." + "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ numberrange 10" + "\n  $ numberrange 1 10"
				+ "\n  $ numberrange 0 11 2" + "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: NumberRange.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the NumberRange class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);

		//System.out.println(opt);
		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 1) {
			printExitHelp();
		}

		int start = 0;
		int step = 1;
		int stop = 0;

		if (opt.getArgsCount() == 1) {
			stop = Integer.parseInt(opt.getArg(0));
		} else if (opt.getArgsCount() == 2) {
			start = Integer.parseInt(opt.getArg(0));
			stop = Integer.parseInt(opt.getArg(1));
		} else if (opt.getArgsCount() == 3) {
			start = Integer.parseInt(opt.getArg(0));
			stop = Integer.parseInt(opt.getArg(1));
			step = Integer.parseInt(opt.getArg(2));
		} else {
			printExitHelp();
		}

		if (start <= stop) {
			for (int num = start; num < stop; num += step) {
				System.out.println(num);
			}
		} else {
			for (int num = start; num > stop; num -= step) {
				System.out.println(num);
			}
		}
	}
}
