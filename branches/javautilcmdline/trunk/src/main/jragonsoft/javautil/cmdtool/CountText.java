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

import java.io.File;
import java.io.IOException;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: CountText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class CountText {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  CountText [options] [FileInput]"
				+ "\n  Count number of lines from input text. If no argument is passed, input is"
				+ "\n  reading from Standard Input."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n    -c      Calculate sum and average of all line's value."
				+ "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ counttext test.txt" + "\n  $ ls -1 /tmp | counttext"
				+ "\n  $ numberrange 100 | counttext -c" + "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: CountText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the CountText class
	 * 
	 * @param args
	 *            The command line arguments
	 * @exception IOException
	 *                Description of the Exception
	 */
	public static void main(String[] args) throws IOException {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() > 1) {
			printExitHelp();
		}

		//Process arguments
		String[] lines;
		if (opt.getArgsCount() == 1) {
			lines = FileUtils.getLines(new File(opt.getArg(0)));
		} else {
			lines = SystemUtils.getInputLines();
		}

		if (opt.isOpt("c")) {
			int sum = 0;
			for (int i = 0, maxIndex = lines.length; i < maxIndex; i++) {
				String line = lines[i];
				sum += Integer.parseInt(line);
			}
			System.out.println("lines=" + lines.length + ", sum=" + sum
					+ ", ave=" + sum / lines.length);
		} else {
			System.out.println("lines=" + lines.length);
		}
	}
}
