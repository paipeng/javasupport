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
import java.util.regex.Pattern;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;
import jragonsoft.javautil.util.StringUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: WhichCommand.java 4 2006-03-16 15:27:19Z zemian $
 */
public class WhichCommand {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  WhichCommand [options] SystemCmd SystemPath"
				+ "\n  Search SystemCmd file location in all SystemPath dirs."
				+ "\n"
				+ "\n  NOTE: In Windows, you must quote your SystemPath var to eliminate"
				+ "\n  space in directory problem." + "\n" + "\n  [options]"
				+ "\n    -h      Help and version." + "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ whichcommand java $PATH #In Unix"
				+ "\n  $ whichcommand javac $PATH #In Unix"
				+ "\n  $ whichcommand notepad \"%PATH%\" #In Windows"
				+ "\n  $ whichcommand win.* \"%PATH%\" #In Windows" + "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: WhichCommand.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the WhichCommand class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 2) {
			printExitHelp();
		}

		String cmd = opt.getArg(0);
		String path = opt.getArg(1);

		String[] paths = StringUtils.split(path, FileUtils.PATH_SEP);
		for (int i = 0; i < paths.length; i++) {
			path = paths[i];
			//System.out.println("[DEBUG] path " + path);
			File[] files = null;
			if (System.getProperty("os.name").indexOf("Win") >= 0) {
				boolean isRecursive = false;
				Pattern pattern = Pattern.compile("^" + cmd
						+ "\\.((exe)|(bat)|(com))$", Pattern.CASE_INSENSITIVE);
				files = FileUtils.globFiles(new File(path), pattern,
						isRecursive);
			} else {
				files = FileUtils.globFiles(new File(path), "^" + cmd + "$");
			}

			for (int idx = 0; idx < files.length; idx++) {
				System.out.println(files[idx].getAbsolutePath());
			}
		}
	}
}
