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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jragonsoft.javautil.support.GetOpt;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: FileGrep.java 4 2006-03-16 15:27:19Z zemian $
 */
public class FileGrep {
	private static boolean isPrintLineNum;

	private static boolean isPrintFilename;

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  FileGrep [options] Pattern [File] [File ...]"
				+ "\n  Grap text from text file or Standard Input. If no argument passed after"
				+ "\n  Pattern then Standard Input will be used. Else only use File's as input."
				+ "\n  Pattern is any valid Java RegEx pattern string." + "\n"
				+ "\n  [options]" + "\n    -h      Help and version."
				+ "\n    -l      Print line number where text found."
				+ "\n    -n      Print filename where text found." + "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ grep import Main.java"
				+ "\n  $ cat Main.java | grep import -l -n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: FileGrep.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the FileGrep class
	 * 
	 * @param args
	 *            The command line arguments
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void main(String[] args) throws Exception {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 1) {
			printExitHelp();
		}
		String regex = opt.getArg(0);
		isPrintLineNum = opt.isOpt("l");
		isPrintFilename = opt.isOpt("n");

		Pattern pattern = Pattern.compile(regex);

		if (opt.getArgsCount() >= 2) {
			int max = opt.getArgsCount() - 1;
			for (int i = 1; i <= max; i++) {
				String filename = opt.getArg(i);
				grep(filename, new FileInputStream(filename), pattern);
			}
		} else {
			grep("STDIN", System.in, pattern);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param filename
	 *            Description of the Parameter
	 * @param in
	 *            Description of the Parameter
	 * @param pattern
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void grep(String filename, InputStream in, Pattern pattern)
			throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		long lineCount = 0;
		while ((line = reader.readLine()) != null) {
			lineCount++;
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				if (isPrintFilename) {
					System.out.print(filename + ": ");
				}
				if (isPrintLineNum) {
					System.out.print(lineCount + ": ");
				}
				System.out.println(line);
			}
		}
		reader.close();
	}
}
