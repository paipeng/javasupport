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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: SearchJar.java 4 2006-03-16 15:27:19Z zemian $
 */
public class SearchJar {
	/** Description of the Field */
	public final static String JAR_EXT_PATTERN = "\\.jar$";

	private static boolean isDebug = false;

	private static boolean isRecursive = false;

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  SearchJar [options] SearchText JarFile [JarDir ...]"
				+ "\n  Search filenames from one or more jar files. You may give dir as."
				+ "\n  as argument, and it will find all the jar files in it as input."
				+ "\n" + "\n  [options]" + "\n    -h      Help and version."
				+ "\n    -d      Show debug info."
				+ "\n    -r      Search recursively." + "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ searchjar Servlet /opt/tomcat/common/lib" + "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: SearchJar.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the SearchJar class
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
		isDebug = opt.isOpt("d");
		isRecursive = opt.isOpt("r");

		String searchStr = opt.getArg(0);
		ArrayList jarFiles = new ArrayList();

		//Get all jar files
		for (int i = 1; i < opt.getArgsCount(); i++) {
			File inputFile = new File(opt.getArg(i));
			if (inputFile.isDirectory()) {
				jarFiles.addAll(Arrays.asList(FileUtils.globFiles(inputFile,
						JAR_EXT_PATTERN, isRecursive)));
			} else if (inputFile.getName().toLowerCase().endsWith(".jar")) {
				Pattern p = Pattern.compile(JAR_EXT_PATTERN);
				Matcher m = p.matcher(inputFile.getName());
				if (m.find())
					jarFiles.add(inputFile);
			}
		}
		//debug("Seaching " + jarFiles);

		//Do searching on jar files
		int foundCount = 0;
		for (Iterator itr = jarFiles.iterator(); itr.hasNext();) {
			File item = (File) itr.next();
			debug("Seaching " + item.getAbsolutePath());
			try {
				Enumeration enu = new JarFile(item).entries();
				int checkCount = foundCount;
				while (enu.hasMoreElements()) {
					ZipEntry zipEntry = (ZipEntry) enu.nextElement();
					debug("zipEntry: " + zipEntry);
					if (zipEntry.getName().indexOf(searchStr) != -1) {
						System.out.println(item.getAbsolutePath() + " "
								+ zipEntry.getName());
						foundCount++;
						//break; //zdeng, Do not stop search even found first
						// occurance!
					}
				}
				debug("Found " + (foundCount - checkCount));
			} catch (IOException e) {
				//System.out.println(e); //ignore it for now.
				continue;
			}
		}

		if (foundCount == 0) {
			System.out.println("Can't find " + searchStr
					+ " in given jars or dirs.");
		}
	}

	public static void debug(String msg) {
		if (isDebug)
			System.out.println("[DEBUG] " + msg);
	}
}
