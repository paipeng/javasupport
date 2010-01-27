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

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: SortText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class SortText {
	/** Help page */
	public static void printExitHelp() {
		String help = "USAGE:" + "\n  SortText [options]"
				+ "\n  Take input text and sort it." + "\n" + "\n  [options]"
				+ "\n    -h      Help and version." + "\n" + "\nEXAMPLES:"
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: SortText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the SortText class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h")) {
			printExitHelp();
		}

		String[] lines = SystemUtils.getInputLines();
		Arrays.sort(lines);
		for (int i = 0, max = lines.length; i < max; i++) {
			System.out.println(lines[i]);
		}
	}
}
