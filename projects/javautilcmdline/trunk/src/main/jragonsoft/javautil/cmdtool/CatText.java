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
import java.io.File;
import java.io.FileReader;

import jragonsoft.javautil.support.GetOpt;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: CatText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class CatText {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  CatText [options] File [File ...]"
				+ "\n  Concatenate and Print content of text file out on screen."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h           Help and version."
				+ "\n    -n           Do not print filename if there two or more files."
				+ "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ cattext test.txt"
				+ "\n  $ cattext test.txt test2.txt /tmp/zman*.txt" + "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: CatText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the CatText class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 1) {
			printExitHelp();
		}
		boolean isPrintFilename = !opt.isOpt("n");

		for (int i = 0; i < opt.getArgsCount(); i++) {
			String filename = opt.getArg(i);
			if (i > 0 && isPrintFilename) {
				System.out.println("=== " + filename + " ===");
			}
			try {
				File file = new File(filename);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
				reader.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
