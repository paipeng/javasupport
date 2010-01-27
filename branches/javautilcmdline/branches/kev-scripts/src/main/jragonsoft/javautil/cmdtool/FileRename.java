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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: FileRename.java 4 2006-03-16 15:27:19Z zemian $
 */
public class FileRename {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  FileRename [options] Path SearchPattern ReplaceString"
				+ "\n  Rename all file names that match the SearchPattern with ReplaceString."
				+ "\n" + "\n  [options]" + "\n    -h      Help and version."
				+ "\n    -r      Search Path recursively."
				+ "\n    -d      Dry run. Do not execute any changes." + "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ filerename -d /tmp one_ two_"
				+ "\n  $ filerename /tmp one_ two_"
				+ "\n  $ filerename /tmp \" \" \"_\"" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: FileRename.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the FileRename class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 3) {
			printExitHelp();
		}
		boolean isDryrun = opt.isOpt("d");
		boolean isRecursive = opt.isOpt("r");

		//Proccess arguments
		String path = opt.getArg(0);
		String regex = opt.getArg(1);
		String replace = opt.getArg(2);
		Pattern pattern = Pattern.compile(regex);

		File[] files = FileUtils
				.globFiles(new File(path), pattern, isRecursive);
		//System.out.println("[DEBUG] files " +
		// java.util.Arrays.asList(files));
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			Matcher matcher = pattern.matcher(file.getName());
			String newFilename = matcher.replaceAll(replace);
			File dest = new File(file.getParentFile(), newFilename);
			if (!isDryrun) {
				FileUtils.move(file, dest);
			}
			System.out.println("Re-name " + file.getAbsolutePath() + " to "
					+ dest.getAbsolutePath());
		}
	}
}
