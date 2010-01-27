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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import jragonsoft.javautil.support.GetLongOpt;
import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: FileFind.java 4 2006-03-16 15:27:19Z zemian $
 */
public class FileFind {
	private static String name;

	private static boolean isRecursive;

	private static boolean isFileOnly;

	private static boolean isDirOnly;

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  FileFind [options] Path [Path ...]"
				+ "\n  Find files in Paths."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h                 Help and version."
				+ "\n    -name=Pattern      Print only file name that match this Pattern"
				+ "\n    -d                 Print directory only"
				+ "\n    -f                 Print file only"
				+ "\n    -N                 Do NOT do recursive in Path."
				+ "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ filefind ." + "\n  $ filefind /usr/bin /tmp"
				+ "\n  $ filefind /tmp -name=txt" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: FileFind.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the FileFind class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetLongOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 1) {
			printExitHelp();
		}
		name = opt.getOpt("name", ".*");
		isRecursive = !opt.isOpt("N");
		isFileOnly = opt.isOpt("f");
		isDirOnly = opt.isOpt("d");

		Pattern pattern = Pattern.compile(name);
		for (int i = 0; i < opt.getArgsCount(); i++) {
			String path = opt.getArg(i);
			findFiles(new File(path), pattern, isRecursive, isFileOnly,
					isDirOnly);
		}

	}

	/**
	 * Description of the Method
	 * 
	 * @param dir
	 *            Description of the Parameter
	 * @param pattern
	 *            Description of the Parameter
	 * @param isRecursive
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static File[] findFiles(File dir, final Pattern pattern,
			boolean isRecursive, final boolean isFileOnly,
			final boolean isDirOnly) {
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (pattern.matcher(name).find()) {
					File file = null;
					try {
						file = new File(dir, name);
						boolean doPrint = false;
						if (isDirOnly && file.isDirectory())
							doPrint = true;
						else if (isFileOnly && file.isFile())
							doPrint = true;
						else if (!isFileOnly && !isDirOnly)
							doPrint = true;
						if (doPrint)
							System.out.println(file.getCanonicalPath());
					} catch (Exception e) {
						if (file != null) {
							System.err.println("Error: "
									+ file.getAbsolutePath());
						}
					}
					return true;
				}
				return false;
			}
		};

		ArrayList files = new ArrayList();
		File[] childs = dir.listFiles(filter);
		if (childs == null) {
			return new File[0];
		}

		files.addAll(Arrays.asList(childs));
		if (isRecursive) {
			File[] subdirs = FileUtils.globDirs(dir);
			for (int i = subdirs.length - 1; i >= 0; i--) {
				File subdir = subdirs[i];
				File[] results = findFiles(subdir, pattern, isRecursive,
						isFileOnly, isDirOnly);
				files.addAll(Arrays.asList(results));
			}
		}

		return FileUtils.toFileArray(files);
	}
}
